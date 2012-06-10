package de.tuhh.ict.pshdl.model.validation;

import static de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection.*;
import static de.tuhh.ict.pshdl.model.validation.HDLValidator.ErrorCode.*;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLAssignment.*;
import de.tuhh.ict.pshdl.model.HDLObject.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.*;

public class RWValidation {
	public static void checkVariableUsage(HDLPackage unit, Set<Problem> problems) {
		annotateReadCount(unit);
		annotateWriteCount(unit);
		List<HDLVariable> vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			if (hdlVariable.hasContainer(HDLInterfaceDeclaration.class))
				continue;
			Integer readCount = hdlVariable.getMeta(IntegerMeta.READ_COUNT);
			readCount = readCount == null ? 0 : readCount;
			Integer writeCount = hdlVariable.getMeta(IntegerMeta.WRITE_COUNT);
			writeCount = writeCount == null ? 0 : writeCount;
			Integer accessCount = hdlVariable.getMeta(IntegerMeta.ACCESS);
			accessCount = accessCount == null ? 0 : accessCount;
			if ((readCount == 0) && (writeCount == 0) && (accessCount == 0)) {
				problems.add(new Problem(ProblemSeverity.WARNING, UNUSED_VARIABLE, hdlVariable));
			} else {
				HDLDirection dir = hdlVariable.getDirection();
				if ((readCount == 0) && (dir == IN)) {
					problems.add(new Problem(ProblemSeverity.WARNING, IN_PORT_NEVER_READ, hdlVariable));
				}
				if ((writeCount > 0) && (dir == IN)) {
					problems.add(new Problem(ProblemSeverity.ERROR, WRITE_ACCESS_TO_IN_PORT, hdlVariable));
				}
				if ((writeCount == 0) && (dir == OUT)) {
					problems.add(new Problem(ProblemSeverity.WARNING, OUT_PORT_NEVER_WRITTEN, hdlVariable));
				}
				if ((writeCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(ProblemSeverity.WARNING, INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN, hdlVariable));
				}
				if ((readCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(ProblemSeverity.WARNING, INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ, hdlVariable));
				}
				if ((readCount == 0) && ((dir == PARAMETER) || (dir == CONSTANT))) {
					problems.add(new Problem(ProblemSeverity.WARNING, PARAMETER_OR_CONSTANT_NEVER_READ, hdlVariable));
				}
			}
		}
		List<HDLInterfaceInstantiation> his = unit.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (HDLInterfaceInstantiation hii : his) {
			ArrayList<HDLVariableDeclaration> ports = hii.resolveHIf().getPorts();
			for (HDLVariableDeclaration hvd : ports) {
				ArrayList<HDLVariable> variables = hvd.getVariables();
				for (HDLVariable hdlVariable : variables) {
					Integer readCount = hdlVariable.getMeta(IntegerMeta.READ_COUNT);
					readCount = readCount == null ? 0 : readCount;
					Integer writeCount = hdlVariable.getMeta(IntegerMeta.WRITE_COUNT);
					writeCount = writeCount == null ? 0 : writeCount;
					HDLDirection dir = hdlVariable.getDirection();
					if ((readCount == 0) && (writeCount == 0) && !((dir == PARAMETER) || (dir == CONSTANT))) {
						problems.add(new Problem(ProblemSeverity.WARNING, ErrorCode.INTERFACE_UNUSED_PORT, hdlVariable, hii));
					} else {
						if ((readCount == 0) && (dir == OUT)) {
							problems.add(new Problem(ProblemSeverity.WARNING, ErrorCode.INTERFACE_OUT_PORT_NEVER_READ, hdlVariable, hii));
						}
						if ((writeCount > 0) && (dir == OUT)) {
							problems.add(new Problem(ProblemSeverity.ERROR, ErrorCode.INTERFACE_OUT_WRITTEN, hdlVariable, hii));
						}
						if ((writeCount == 0) && (dir == IN)) {
							problems.add(new Problem(ProblemSeverity.WARNING, ErrorCode.INTERFACE_IN_PORT_NEVER_WRITTEN, hdlVariable, hii));
						}
					}
				}
			}
		}
	}

	public static void annotateReadCount(HDLPackage orig) {
		List<HDLReference> list = orig.getAllObjectsOf(HDLReference.class, true);
		for (HDLReference ref : list) {
			if (ref.getContainer() instanceof HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) ref.getContainer();
				if ((ass.getLeft() == ref) && (ass.getType() == HDLAssignmentType.ASSGN))
					// If it is a non-trivial assign, it contains a read
					continue;
			}
			// XXX check for interface and ensure that it is only this instance
			HDLVariable var = ref.resolveVar();
			incMeta(var, IntegerMeta.READ_COUNT);
			if (ref instanceof HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				HDLVariable hVar = hir.resolveHIf();
				incMeta(hVar, IntegerMeta.ACCESS);
			}
		}
	}

	public static void annotateWriteCount(HDLPackage orig) {
		List<HDLAssignment> list = orig.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : list) {
			HDLReference left = ass.getLeft();
			if (left instanceof HDLReference) {
				HDLVariableRef ref = (HDLVariableRef) left;
				HDLVariable var = ref.resolveVar();
				incMeta(var, IntegerMeta.WRITE_COUNT);
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					HDLVariable hVar = hir.resolveHIf();
					incMeta(hVar, IntegerMeta.ACCESS);
				}
			}
		}
		List<HDLVariable> defVal = HDLQuery.select(HDLVariable.class).from(orig).where(HDLVariable.fDefaultValue).isNotEqualTo(null).getAll();
		for (HDLVariable var : defVal) {
			incMeta(var, IntegerMeta.WRITE_COUNT);
		}
	}

	private static void incMeta(HDLVariable var, MetaAccess<Integer> metaAccess) {
		Integer meta = var.getMeta(metaAccess);
		if (meta == null)
			meta = 1;
		else
			meta++;
		var.addMeta(metaAccess, meta);
	}
}
