package de.tuhh.ict.pshdl.model.validation;

import static de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection.*;
import static de.tuhh.ict.pshdl.model.validation.builtin.ErrorCode.*;

import java.util.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;
import de.tuhh.ict.pshdl.model.validation.builtin.BuiltInValidator.IntegerMeta;

public class RWValidation {
	public static void checkVariableUsage(HDLPackage unit, Set<Problem> problems) {
		annotateReadCount(unit);
		annotateWriteCount(unit);
		HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			if ((hdlVariable.getContainer(HDLInterfaceDeclaration.class) != null) || BuiltInValidator.skipExp(hdlVariable)) {
				continue;
			}
			Integer readCount = hdlVariable.getMeta(IntegerMeta.READ_COUNT);
			readCount = readCount == null ? 0 : readCount;
			Integer writeCount = hdlVariable.getMeta(IntegerMeta.WRITE_COUNT);
			writeCount = writeCount == null ? 0 : writeCount;
			Integer accessCount = hdlVariable.getMeta(IntegerMeta.ACCESS);
			accessCount = accessCount == null ? 0 : accessCount;
			if ((hdlVariable.getAnnotation(HDLBuiltInAnnotations.clock) != null) || (hdlVariable.getAnnotation(HDLBuiltInAnnotations.reset) != null)) {
				continue;
			}
			if ((readCount == 0) && (writeCount == 0) && (accessCount == 0)) {
				problems.add(new Problem(UNUSED_VARIABLE, hdlVariable));
			} else {
				HDLDirection dir = hdlVariable.getDirection();
				if ((readCount == 0) && (dir == IN)) {
					problems.add(new Problem(IN_PORT_NEVER_READ, hdlVariable));
				}
				if ((writeCount > 0) && (dir == IN)) {
					problems.add(new Problem(WRITE_ACCESS_TO_IN_PORT, hdlVariable));
				}
				if ((writeCount == 0) && (dir == OUT)) {
					problems.add(new Problem(OUT_PORT_NEVER_WRITTEN, hdlVariable));
				}
				if ((writeCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN, hdlVariable));
				}
				if ((readCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ, hdlVariable));
				}
				if ((readCount == 0) && ((dir == PARAMETER) || (dir == CONSTANT))) {
					problems.add(new Problem(PARAMETER_OR_CONSTANT_NEVER_READ, hdlVariable));
				}
			}
		}
		HDLInterfaceInstantiation[] his = unit.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (HDLInterfaceInstantiation hii : his) {
			Set<String> readList = hii.getVar().getMeta(Init.read);
			if (readList == null) {
				readList = new HashSet<String>();
			}
			Set<String> writeList = hii.getVar().getMeta(Init.written);
			if (writeList == null) {
				writeList = new HashSet<String>();
			}
			Optional<HDLInterface> resolveHIf = hii.resolveHIf();
			if (!resolveHIf.isPresent()) {
				continue;
			}
			ArrayList<HDLVariableDeclaration> ports = resolveHIf.get().getPorts();
			for (HDLVariableDeclaration hvd : ports) {
				ArrayList<HDLVariable> variables = hvd.getVariables();
				for (HDLVariable hdlVariable : variables) {
					boolean written = writeList.contains(hdlVariable.getName());
					boolean read = readList.contains(hdlVariable.getName());
					if ((hdlVariable.getAnnotation(HDLBuiltInAnnotations.clock) != null) || (hdlVariable.getAnnotation(HDLBuiltInAnnotations.reset) != null)) {
						continue;
					}
					HDLDirection dir = hdlVariable.getDirection();
					// XXX Take care of inout
					if (!read && !written && !((dir == PARAMETER) || (dir == CONSTANT))) {
						problems.add(new Problem(ErrorCode.INTERFACE_UNUSED_PORT, hii, hdlVariable, null));
					} else {
						if (!read && (dir == OUT)) {
							problems.add(new Problem(ErrorCode.INTERFACE_OUT_PORT_NEVER_READ, hii, hdlVariable, null));
						}
						if (written && (dir == OUT)) {
							problems.add(new Problem(ErrorCode.INTERFACE_OUT_WRITTEN, hii, hdlVariable, null));
						}
						if (!written && (dir == IN)) {
							problems.add(new Problem(ErrorCode.INTERFACE_IN_PORT_NEVER_WRITTEN, hii, hdlVariable, null));
						}
					}
				}
			}
		}
	}

	public static void annotateReadCount(IHDLObject orig) {
		HDLReference[] list = orig.getAllObjectsOf(HDLReference.class, true);
		for (HDLReference ref : list) {
			if (BuiltInValidator.skipExp(ref)) {
				continue;
			}
			IHDLObject container = ref.getContainer();
			if (container instanceof HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) container;
				if ((ass.getLeft() == ref) && (ass.getType() == HDLAssignmentType.ASSGN)) {
					// If it is a non-trivial assign, it contains a read
					continue;
				}
			}
			// XXX check for interface and ensure that it is only this instance
			if (ref instanceof HDLUnresolvedFragment)
				return;
			Optional<HDLVariable> var = ((HDLResolvedRef) ref).resolveVar();
			if (var.isPresent())
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					Optional<HDLVariable> hVar = hir.resolveHIf();
					if (hVar.isPresent()) {
						incMeta(hVar.get(), IntegerMeta.ACCESS);
						addStringMeta(var.get().getName(), hVar.get(), Init.read);
					}
				} else {
					incMeta(var.get(), IntegerMeta.READ_COUNT);
				}
		}
	}

	public static enum BlockMeta implements MetaAccess<HDLBlock> {
		block;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public static enum BlockMetaClash implements MetaAccess<Set<HDLBlock>> {
		clash;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public static enum Init implements MetaAccess<Set<String>> {
		full, written, read;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public static HDLBlock UNIT_BLOCK = new HDLBlock();

	public static void annotateWriteCount(IHDLObject orig) {
		HDLAssignment[] list = orig.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : list) {
			HDLReference left = ass.getLeft();
			if (left instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) left;
				if (BuiltInValidator.skipExp(ref)) {
					continue;
				}
				Optional<HDLVariable> var = ref.resolveVar();
				if (!var.isPresent()) {
					continue;
				}
				IHDLObject container = ass.getContainer();
				HDLVariable hdlVariable = var.get();
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					Optional<HDLVariable> hVar = hir.resolveHIf();
					if (hVar.isPresent()) {
						incMeta(hVar.get(), IntegerMeta.ACCESS);
						addStringMeta(hdlVariable.getName(), hVar.get(), Init.written);
						if ((container != null) && (container.getClassType() == HDLClass.HDLUnit) && (ref.getArray().size() == 0) && (ref.getBits().size() == 0)) {
							addStringMeta(hir.getVarRefName().getLastSegment(), hVar.get(), Init.full);
						}
					}
				} else {
					if ((container != null) && (container.getClassType() == HDLClass.HDLUnit) && (ref.getArray().size() == 0) && (ref.getBits().size() == 0)) {
						hdlVariable.addMeta(Init.full, Collections.singleton(hdlVariable.getName()));
					}
					incMeta(hdlVariable, IntegerMeta.WRITE_COUNT);
				}
				HDLBlock block = ass.getContainer(HDLBlock.class);
				if (block == null) {
					block = UNIT_BLOCK;
				}
				if ((hdlVariable.getMeta(BlockMeta.block) != null) && (hdlVariable.getMeta(BlockMeta.block) != block)) {
					Set<HDLBlock> meta = hdlVariable.getMeta(BlockMetaClash.clash);
					if (meta == null) {
						meta = new HashSet<HDLBlock>();
					}
					if (block == null) {
						meta.add(UNIT_BLOCK);
					} else {
						meta.add(block);
					}
					hdlVariable.addMeta(BlockMetaClash.clash, meta);
				}
				hdlVariable.addMeta(BlockMeta.block, block);
			}
		}
		Collection<HDLVariable> defVal = HDLQuery.select(HDLVariable.class).from(orig).where(HDLVariable.fDefaultValue).isNotEqualTo(null).getAll();
		for (HDLVariable var : defVal) {
			incMeta(var, IntegerMeta.WRITE_COUNT);
		}
	}

	private static void addStringMeta(String value, HDLVariable hVar, Init init) {
		Set<String> written = hVar.getMeta(init);
		if (written == null) {
			written = new HashSet<String>();
		}
		written.add(value);
		hVar.addMeta(init, written);
	}

	private static void incMeta(HDLVariable var, MetaAccess<Integer> metaAccess) {
		Integer meta = var.getMeta(metaAccess);
		if (meta == null) {
			meta = 1;
		} else {
			meta++;
		}
		var.addMeta(metaAccess, meta);
	}
}
