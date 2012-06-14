package de.tuhh.ict.pshdl.model.validation;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.simulation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives.ValueRange;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLValidator {

	public static enum IntegerMeta implements MetaAccess<Integer> {
		READ_COUNT, WRITE_COUNT, ACCESS
	}

	public static Set<Problem> validate(HDLPackage unit, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		Set<Problem> problems = new HashSet<Problem>();
		Map<HDLQualifiedName, HDLEvaluationContext> hContext = context;
		if (context == null)
			hContext = HDLEvaluationContext.createDefault(unit);
		try {
			RWValidation.checkVariableUsage(unit, problems);
			checkClockAndResetAnnotation(unit, problems);
			checkConstantBoundaries(unit, problems, hContext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return problems;
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		List<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class).from(unit).where(HDLVariableDeclaration.fDirection).isEqualTo(HDLDirection.CONSTANT)
				.or(HDLDirection.PARAMETER);
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					HDLExpression def = var.getDefaultValue();
					BigInteger constant = eval(hContext, var, def);
					if (constant == null)
						problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, def, var, null));
				}
			}
		}
		List<HDLForLoop> forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
		for (HDLForLoop hdlForLoop : forLoops) {
			for (HDLRange r : hdlForLoop.getRange()) {
				BigInteger evalTo = eval(hContext, r, r.getTo());
				if (evalTo == null) {
					problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					BigInteger evalFrom = eval(hContext, r, r.getFrom());
					if (evalFrom == null) {
						problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getFrom(), r, null));
					}
				}
			}
		}
		checkArrayBoundaries(unit, problems, hContext);
	}

	private static void checkArrayBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		List<HDLAssignment> asss = unit.getAllObjectsOf(HDLAssignment.class, true);
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
				BigInteger index = arr.constantEvaluate(null);
				if (index == null) {
					HDLType type = arr.determineType();
					if (type instanceof HDLPrimitive) {
						HDLPrimitive pt = (HDLPrimitive) type;
						HDLEvaluationContext context = getContext(hContext, arr);
						ValueRange range = HDLPrimitives.getInstance().getValueRange(pt, context);
						if (range.from.signum() < 0)
							problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref));
						HDLExpression arrWidth = dimensions.get(dim);
						BigInteger arrWidthBI = arrWidth.constantEvaluate(null);
						if (arrWidthBI != null) {
							if (arrWidthBI.compareTo(range.to) > 0) {
								problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref));
							}
						} else {
							HDLType determineType = arrWidth.determineType();
							ValueRange valueRange = HDLPrimitives.getInstance().getValueRange((HDLPrimitive) determineType, context);
							if (range.to.compareTo(valueRange.to) > 0) {
								problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref));
							}
						}
					} else {
						problems.add(new Problem(ErrorCode.ARRAY_INDEX_NOT_PRIMITIVE, arr, ref));
					}
				} else {
					if (index.signum() < 0) {
						problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref));
					} else {

					}
				}
				dim++;
			}
		}
	}

	private static BigInteger eval(Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLObject var, HDLExpression def) {
		HDLEvaluationContext hdlEvaluationContext = getContext(hContext, var);
		BigInteger constant = def.constantEvaluate(hdlEvaluationContext);
		return constant;
	}

	private static HDLEvaluationContext getContext(Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLObject var) {
		HDLUnit container = var.getContainer(HDLUnit.class);
		HDLEvaluationContext hdlEvaluationContext = hContext.get(container.getFullName());
		return hdlEvaluationContext;
	}

	private static void checkClockAndResetAnnotation(HDLPackage unit, Set<Problem> problems) {
		List<HDLAnnotation> clocks = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLAnnotations.clock.toString()).getAll();
		if (clocks.size() > 1)
			for (HDLAnnotation anno : clocks) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
			}
		List<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLAnnotations.reset.toString()).getAll();
		if (resets.size() > 1)
			for (HDLAnnotation anno : resets) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
			}
	}

}
