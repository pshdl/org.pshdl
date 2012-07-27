package de.tuhh.ict.pshdl.model.validation;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.simulation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLAnnotations.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLValidator {

	public static enum IntegerMeta implements MetaAccess<Integer> {
		READ_COUNT, WRITE_COUNT, ACCESS
	}

	public static Set<Problem> validate(HDLPackage unit, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		Set<Problem> problems = new HashSet<Problem>();
		Map<HDLQualifiedName, HDLEvaluationContext> hContext = context;
		if (context == null)
			hContext = HDLEvaluationContext.createDefault(unit);
		// TODO find a way to distinguish between context dependent problems and
		// others
		try {
			RWValidation.checkVariableUsage(unit, problems);
			checkClockAndResetAnnotation(unit, problems);
			checkConstantBoundaries(unit, problems, hContext);
			checkArrayBoundaries(unit, problems, hContext);
			checkConstantEquals(unit, problems, hContext);
			// TODO Validate value ranges, check for 0 divide
			// TODO Check for POW only power of 2
			checkCombinedAssignment(unit, problems, hContext);
			checkAnnotations(unit, problems, hContext);
			checkType(unit, problems, hContext);
			checkProessWrite(unit, problems, hContext);
			// TODO Validate bitWidth mismatch
			// TODO Check bit access direction
			// TODO Multi-bit Write only for Constants
			// TODO check for signals named clk or rst and warn about the
			// collision
			// TODO check for proper variable naming
			// TODO check for valid parameter
			// TODO Check for unconstrained arrays in switch case
			// TODO Check for covered cases in switch clause
			// TODO Check for only one default clause
			// TODO Check for Range direction
			// TODO Type checking!
			// TODO HDLConcat need known width
			// TODO Check for combinatorical loop.
			// TODO Check for multiple assignment in same Scope
			// TODO No processes in Module
			// TODO no I/O variables in block
			// TODO Switch expression needs to have width (no natural etc..)

		} catch (Exception e) {
			e.printStackTrace();
		}
		return problems;
	}

	private static void checkProessWrite(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLVariable> vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable var : vars) {
			if (var.hasMeta(RWValidation.BlockMetaClash.clash)) {
				problems.add(new Problem(ErrorCode.MULTI_PROCESS_WRITE, var));
			}
		}
	}

	private static void checkType(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLOpExpression> ops = unit.getAllObjectsOf(HDLOpExpression.class, true);
		for (HDLOpExpression ope : ops) {
			HDLTypeInferenceInfo info = null;
			switch (ope.getClassType()) {
			case HDLArithOp:
				info = HDLPrimitives.getInstance().getArithOpType((HDLArithOp) ope);
				break;
			case HDLBitOp:
				info = HDLPrimitives.getInstance().getBitOpType((HDLBitOp) ope);
				break;
			case HDLShiftOp:
				info = HDLPrimitives.getInstance().getShiftOpType((HDLShiftOp) ope);
				break;
			case HDLEqualityOp:
				info = HDLPrimitives.getInstance().getEqualityOpType((HDLEqualityOp) ope);
				break;
			default:
				throw new IllegalArgumentException("Did not expect class:" + ope.getClassType());
			}
			if (info == null)
				throw new IllegalArgumentException("Info should not be null");
			if (info.error != null)
				problems.add(new Problem(ErrorCode.UNSUPPORTED_TYPE_FOR_OP, ope, info.error));
		}
	}

	private static void checkAnnotations(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Set<HDLAnnotation> annos = unit.getAllObjectsOf(HDLAnnotation.class, true);
		for (HDLAnnotation hdlAnnotation : annos) {
			Problem[] p = HDLAnnotations.validate(hdlAnnotation);
			for (Problem problem : p) {
				problems.add(problem);
			}
		}
	}

	private static void checkCombinedAssignment(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLAssignment> all = HDLQuery.select(HDLAssignment.class).from(unit).where(HDLAssignment.fType).isNotEqualTo(HDLAssignment.HDLAssignmentType.ASSGN).getAll();
		for (HDLAssignment ass : all) {
			if (ass.getLeft().resolveVar().getRegisterConfig() == null) {
				problems.add(new Problem(ErrorCode.COMBINED_ASSIGNMENT_NOT_ALLOWED, ass));
			}
		}
	}

	private static void checkConstantEquals(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLEqualityOp> equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (HDLEqualityOp op : equalities) {
			BigInteger res = op.constantEvaluate(getContext(hContext, op));
			if (res != null) {
				if (res.equals(BigInteger.ONE))
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_TRUE, op));
				else
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_FALSE, op));
			}
		}
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class).from(unit).where(HDLVariableDeclaration.fDirection)
				.isEqualTo(HDLDirection.CONSTANT).or(HDLDirection.PARAMETER);
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					HDLExpression def = var.getDefaultValue();
					BigInteger constant = def.constantEvaluate(getContext(hContext, var));
					if (constant == null)
						problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, def, var, null));
				}
			}
		}
		Collection<HDLForLoop> forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
		for (HDLForLoop hdlForLoop : forLoops) {
			for (HDLRange r : hdlForLoop.getRange()) {
				BigInteger evalTo = r.getTo().constantEvaluate(getContext(hContext, r));
				if (evalTo == null) {
					problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					BigInteger evalFrom = r.getFrom().constantEvaluate(getContext(hContext, r));
					if (evalFrom == null) {
						problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getFrom(), r, null));
					}
				}
			}
		}
	}

	private static void checkArrayBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLAssignment> asss = unit.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss) {
			if (ass.getLeft() instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) ass.getLeft();
				compareBoundaries(problems, hContext, ref, ref.resolveVar().getDimensions(), ref.getArray());
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					HDLVariable var = hir.resolveHIf();
					compareBoundaries(problems, hContext, ref, var.getDimensions(), hir.getIfArray());
				}
			}
		}
	}

	private static void compareBoundaries(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, ArrayList<HDLExpression> dimensions,
			ArrayList<HDLExpression> array) {
		if (dimensions.size() != array.size()) {
			problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_NOT_SAME_DIMENSIONS, ref));
		} else {
			int dim = 0;
			for (HDLExpression arr : array) {
				HDLEvaluationContext context = getContext(hContext, arr);
				ValueRange accessRange = arr.determineRange(context);
				String info = "Expected value range:" + accessRange;
				if (accessRange.to.signum() < 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref, info));
				else if (accessRange.from.signum() < 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info));
				ValueRange arrayRange = dimensions.get(dim).determineRange(context);
				arrayRange = new ValueRange(BigInteger.ZERO, arrayRange.to);
				ValueRange commonRange = arrayRange.and(accessRange);
				if (commonRange == null)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info));
				else if (accessRange.to.compareTo(arrayRange.to) >= 0)
					problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info));

				dim++;
			}
		}
	}

	private static HDLEvaluationContext getContext(Map<HDLQualifiedName, HDLEvaluationContext> hContext, IHDLObject var) {
		HDLUnit container = var.getContainer(HDLUnit.class);
		HDLEvaluationContext hdlEvaluationContext = hContext.get(container.getFullName());
		return hdlEvaluationContext;
	}

	private static void checkClockAndResetAnnotation(HDLPackage unit, Set<Problem> problems) {
		Collection<HDLAnnotation> clocks = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.clock.toString()).getAll();
		if (clocks.size() > 1)
			for (HDLAnnotation anno : clocks) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
			}
		Collection<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.reset.toString()).getAll();
		if (resets.size() > 1)
			for (HDLAnnotation anno : resets) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
			}
	}

}
