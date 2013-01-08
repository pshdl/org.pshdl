package de.tuhh.ict.pshdl.model.validation.builtin;

import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.LevenshteinDistance.Score;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;

public class BuiltInValidator implements IHDLValidator {

	public static String[] PSHDL_KEYWORDS = new String[] { "bit", "out", "string", "switch", "include", "process", "for", "function", "import", "else", "extends", "native",
			"package", "testbench", "int", "if", "in", "default", "enum", "const", "module", "inline", "generate", "bool", "simulation", "uint", "case", "inout", "substitute",
			"param", "register", "interface" };
	public final static Set<String> keywordSet;
	static {
		keywordSet = new HashSet<String>();
		for (String keyword : PSHDL_KEYWORDS) {
			keywordSet.add(keyword);
		}
	}

	public static enum IntegerMeta implements MetaAccess<Integer> {
		READ_COUNT, WRITE_COUNT, ACCESS;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	@Override
	public void check(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// TODO find a way to distinguish between context dependent problems and
		// others
		try {
			RWValidation.checkVariableUsage(pkg, problems);
			checkFunctionCalls(pkg, problems, hContext);
			pkg = Insulin.inlineFunctions(pkg);
			pkg = Insulin.setParameterOnInstance(pkg);
			checkVariableNaming(pkg, problems);
			checkClockAndResetAnnotation(pkg, problems);
			checkConstantBoundaries(pkg, problems, hContext);
			checkArrayBoundaries(pkg, problems, hContext);
			checkConstantEquals(pkg, problems, hContext);
			// TODO Validate value ranges, check for 0 divide
			// TODO Check for POW only power of 2
			checkCombinedAssignment(pkg, problems, hContext);
			checkAnnotations(pkg, problems, hContext);
			checkType(pkg, problems, hContext);
			checkProessWrite(pkg, problems, hContext);
			checkGenerators(pkg, problems, hContext);
			checkInputReg(pkg, problems, hContext);
			checkLiteralConcat(pkg, problems);
			// TODO Validate bitWidth mismatch
			// checkBitWidthAssignments(pkg, problems, hContext);
			// TODO Check bit access direction
			// TODO Multi-bit Write only for Constants
			// TODO check for signals named clk or rst and warn about the
			// collision
			// TODO check for proper variable naming
			// TODO check for valid parameter
			checkSwitchStatements(pkg, problems, hContext);
			// TODO Check for Range direction
			// TODO Type checking!
			// TODO Check for combinatorical loop.
			// TODO Check for multiple assignment in same Scope
			// TODO No processes in Module
			// TODO no I/O variables in block
			// TODO no Registers in Testbench
			// TODO warn for name collision in generators
			// TODO check for same name with different case
			// TODO Out port array need to be constant
			// TODO Check for bit access on left assignment side when right is
			// matching

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void checkBitWidthAssignments(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLAssignment[] asss = pkg.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss) {
			HDLEvaluationContext context = getContext(hContext, ass);
			HDLType lType = ass.getLeft().determineType();
			HDLType rType = ass.getRight().determineType();
			switch (lType.getClassType()) {
			case HDLEnum:
				if (rType.getClassType() != HDLClass.HDLEnum)
					problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_ENUM, ass));
				break;
			case HDLPrimitive:
				if (rType.getClassType() != HDLClass.HDLPrimitive) {
					problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_PRIMITIVE, ass));
				} else {
					HDLPrimitive left = (HDLPrimitive) lType;
					HDLPrimitive right = (HDLPrimitive) rType;
					switch (left.getType()) {
					case BIT:
						if (right.getType() != HDLPrimitiveType.BIT) {
							if (right.getWidth() != null) {
								BigInteger w = right.getWidth().constantEvaluate(context);
								if (!w.equals(BigInteger.ONE))
									problems.add(new Problem(ErrorCode.ASSIGNMENT_CLIPPING_WILL_OCCUR, ass.getRight(), ass));
							}
						}
						break;
					case BITVECTOR:
						break;
					case BOOL:
						break;
					case INT:
						break;
					case INTEGER:
						break;
					case NATURAL:
						break;
					case STRING:
						break;
					case UINT:
						break;
					default:
						break;

					}
				}
				break;
			case HDLInterface:
			default:
				problems.add(new Problem(ErrorCode.ASSIGNEMENT_NOT_SUPPORTED, ass));
			}
		}
	}

	private void checkLiteralConcat(HDLPackage pkg, Set<Problem> problems) {
		HDLConcat[] concats = pkg.getAllObjectsOf(HDLConcat.class, true);
		for (HDLConcat hdlConcat : concats) {
			ArrayList<HDLExpression> cats = hdlConcat.getCats();
			for (HDLExpression exp : cats) {
				HDLType type = exp.determineType();
				if (type instanceof HDLPrimitive) {
					HDLPrimitive prim = (HDLPrimitive) type;
					switch (prim.getType()) {
					case BIT:
					case BITVECTOR:
					case INT:
					case UINT:
						break;
					case BOOL:
					case INTEGER:
					case NATURAL:
					case STRING:
						problems.add(new Problem(ErrorCode.CONCAT_TYPE_NOT_ALLOWED, exp, prim));
						break;
					}
				} else
					problems.add(new Problem(ErrorCode.CONCAT_TYPE_NOT_ALLOWED, exp, type));

			}
		}
	}

	private void checkSwitchStatements(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLSwitchStatement[] switches = pkg.getAllObjectsOf(HDLSwitchStatement.class, true);
		for (HDLSwitchStatement switchStatement : switches) {
			boolean defaultFound = false;
			ArrayList<HDLSwitchCaseStatement> cases = switchStatement.getCases();
			Set<BigInteger> values = new HashSet<BigInteger>();
			Set<HDLQualifiedName> enums = new HashSet<HDLQualifiedName>();
			HDLType type = switchStatement.getCaseExp().determineType();
			if (type instanceof HDLPrimitive) {
				HDLPrimitive primitive = (HDLPrimitive) type;
				if (primitive.getWidth() == null)
					problems.add(new Problem(ErrorCode.SWITCH_CASE_NEEDS_WIDTH, switchStatement.getCaseExp()));
			}
			boolean isEnum = type instanceof HDLEnum;
			for (HDLSwitchCaseStatement caseStatement : cases) {
				HDLExpression label = caseStatement.getLabel();
				if (label == null) {
					if (defaultFound)
						problems.add(new Problem(ErrorCode.SWITCH_MULTIPLE_DEFAULT, caseStatement));
					defaultFound = true;
				} else {
					if (!isEnum) {
						BigInteger constant = label.constantEvaluate(null);
						if (constant == null) {
							problems.add(new Problem(ErrorCode.SWITCH_LABEL_NOT_CONSTANT, caseStatement));
						} else {
							if (!values.add(constant))
								problems.add(new Problem(ErrorCode.SWITCH_LABEL_DUPLICATE, caseStatement));
						}
					} else {
						if (!enums.add(((HDLEnumRef) label).getVarRefName()))
							problems.add(new Problem(ErrorCode.SWITCH_LABEL_DUPLICATE, caseStatement));
					}
				}
			}
			if (!defaultFound)
				problems.add(new Problem(ErrorCode.SWITCH_NO_DEFAULT, switchStatement));
		}
	}

	private void checkVariableNaming(HDLPackage pkg, Set<Problem> problems) {
		HDLVariable[] vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		Map<String, HDLVariable> nameMap = new HashMap<String, HDLVariable>();
		for (HDLVariable hdlVariable : vars) {
			HDLQualifiedName fullName = hdlVariable.getFullName();
			if (keywordSet.contains(fullName.getLastSegment())) {
				problems.add(new Problem(ErrorCode.VARIABLE_KEYWORD_NAME, hdlVariable));
			}
			HDLVariable put = nameMap.put(fullName.toString().toLowerCase(), hdlVariable);
			if (put != null) {
				HDLQualifiedName otherName = put.getFullName();
				if (otherName.equals(fullName))
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME, hdlVariable, put));
				else
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME_DIFFERENT_CASE, hdlVariable, put));
			}
		}
		for (Entry<String, HDLVariable> entry : nameMap.entrySet()) {
			HDLVariable var = entry.getValue();
			HDLQualifiedName fullName = var.getFullName();
			String lastSegment = fullName.getLastSegment();
			HDLQualifiedName type = fullName.getTypePart();
			HDLQualifiedName localPart = fullName.getLocalPart().skipLast(1);
			for (int i = 0; i < localPart.length; i++) {
				HDLQualifiedName scoped = localPart.skipLast(i + 1).append(lastSegment);
				HDLQualifiedName newName = type.append(scoped);
				HDLVariable otherVar = nameMap.get(newName.toString().toLowerCase());
				if (otherVar != null) {
					HDLQualifiedName otherName = otherVar.getFullName();
					if (otherName.equals(newName))
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME, var, otherVar));
					else
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE, var, otherVar));
				}
			}
		}
	}

	private static void checkInputReg(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		Collection<HDLVariableDeclaration> allIns = HDLQuery.select(HDLVariableDeclaration.class).from(pkg).where(HDLVariableDeclaration.fDirection).isEqualTo(HDLDirection.IN)
				.getAll();
		for (HDLVariableDeclaration inPort : allIns) {
			if (inPort.getRegister() != null)
				problems.add(new Problem(ErrorCode.IN_PORT_CANT_REGISTER, inPort));
		}

	}

	private static void checkGenerators(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLDirectGeneration[] generators = unit.getAllObjectsOf(HDLDirectGeneration.class, true);
		for (HDLDirectGeneration hdg : generators) {
			HDLGenerators.validate(hdg, problems, getContext(hContext, hdg));
		}
	}

	private static void checkFunctionCalls(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLFunctionCall[] functions = unit.getAllObjectsOf(HDLFunctionCall.class, true);
		for (HDLFunctionCall function : functions) {
			HDLTypeInferenceInfo info = HDLFunctions.getInferenceInfo(function);
			if (info == null) {
				try {
					HDLFunction f = function.resolveName();
					if (f == null)
						problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
				} catch (Exception e) {
					problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
				}
			}
		}
	}

	private static void checkProessWrite(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable var : vars) {
			if (var.hasMeta(RWValidation.BlockMetaClash.clash)) {
				problems.add(new Problem(ErrorCode.MULTI_PROCESS_WRITE, var));
			}
		}
	}

	private static void checkType(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLOpExpression[] ops = unit.getAllObjectsOf(HDLOpExpression.class, true);
		for (HDLOpExpression ope : ops) {
			if (skipExp(ope))
				continue;
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
		HDLAnnotation[] annos = unit.getAllObjectsOf(HDLAnnotation.class, true);
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
		HDLEqualityOp[] equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (HDLEqualityOp op : equalities) {
			if (skipExp(op))
				continue;
			BigInteger res = op.constantEvaluate(getContext(hContext, op));
			if (res != null) {
				if (res.equals(BigInteger.ONE))
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_TRUE, op));
				else
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_FALSE, op));
			}
		}
	}

	public static boolean skipExp(IHDLObject op) {
		return (op.getContainer(HDLInlineFunction.class) != null) || (op.getContainer(HDLSubstituteFunction.class) != null);
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// XXX Check Array Dimensions
		Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class).from(unit).where(HDLVariableDeclaration.fDirection)
				.isEqualTo(HDLDirection.CONSTANT).or(HDLDirection.PARAMETER);
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					HDLExpression def = var.getDefaultValue();
					BigInteger constant = def.constantEvaluate(getContext(hContext, var));
					if (constant == null) {
						if (!(def instanceof HDLLiteral))
							problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, def, var, null));

					}
				}
			}
		}
		HDLForLoop[] forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
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
		HDLVariableRef[] refs = unit.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (skipExp(ref))
				continue;
			ArrayList<HDLExpression> dimensions = ref.resolveVar().getDimensions();
			compareBoundaries(problems, hContext, ref, dimensions, ref.getArray());
			if (ref instanceof HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				HDLVariable var = hir.resolveHIf();
				compareBoundaries(problems, hContext, ref, var.getDimensions(), hir.getIfArray());
			}
		}
	}

	/**
	 * Compares whether the actual access array fits within the declared array
	 * dimensions
	 * 
	 * @param problems
	 * @param hContext
	 * @param ref
	 *            the subject upon which the error will be declared
	 * @param dimensions
	 *            the declared dimensions of the variable
	 * @param array
	 *            the accessed dimensions of the variable
	 */
	private static void compareBoundaries(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, ArrayList<HDLExpression> dimensions,
			ArrayList<HDLExpression> array) {
		if (dimensions.size() < array.size()) {
			problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
			return;
		} else if ((dimensions.size() > array.size())) {
			// Check whether dimensions have been left out. This is only ok when
			// it is an assignment and the other dimension is the same
			HDLClass containerType = ref.getContainer().getClassType();
			if (containerType == HDLClass.HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) ref.getContainer();
				HDLReference left = ass.getLeft();
				if (left.getClassType() == HDLClass.HDLEnumRef) {
					problems.add(new Problem(ErrorCode.ASSIGNMENT_ENUM_NOT_WRITABLE, left));
				} else {
					HDLVariableRef varRef = (HDLVariableRef) left;
					ArrayList<HDLExpression> targetDim = varRef.resolveVar().getDimensions();
					for (int i = 0; i < varRef.getArray().size(); i++) {
						if (targetDim.size() == 0) {
							problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, varRef));
							return;
						}
						targetDim.remove(0);
					}
					if (left != ref) {
						validateArrayAssignment(problems, hContext, ref, ass, left, targetDim);
					} else {
						if (ass.getRight().getClassType() != HDLClass.HDLVariableRef) {
							problems.add(new Problem(ErrorCode.ARRAY_WRITE_MULTI_DIMENSION, ass));
						}
					}
				}
			} else if (containerType == HDLClass.HDLVariable) {
				HDLVariable var = (HDLVariable) ref.getContainer();
				validateArrayAssignment(problems, hContext, ref, var, var, var.getDimensions());
			} else
				problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION, ref));
		}
		int dim = 0;
		for (HDLExpression arr : array) {
			HDLEvaluationContext context = getContext(hContext, arr);
			ValueRange accessRange = arr.determineRange(context);
			if (accessRange == null) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, arr));
				break;
			}
			ValueRange arrayRange = dimensions.get(dim).determineRange(context);
			if (arrayRange == null) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, dimensions.get(dim)));
				break;
			}
			arrayRange = new ValueRange(BigInteger.ZERO, arrayRange.to.subtract(BigInteger.ONE));
			String info = "Expected value range:" + accessRange;
			if (accessRange.to.signum() < 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
			else if (accessRange.from.signum() < 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
			ValueRange commonRange = arrayRange.and(accessRange);
			if (commonRange == null)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));
			else if (accessRange.to.compareTo(arrayRange.to) > 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info).addMeta("accessRange", accessRange).addMeta("arrayRange", arrayRange));

			dim++;
		}
	}

	private static void validateArrayAssignment(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, IHDLObject ass, IHDLObject left,
			ArrayList<HDLExpression> targetDim) {
		HDLVariable right = ref.resolveVar();
		ArrayList<HDLExpression> sourceDim = right.getDimensions();
		for (int i = 0; i < ref.getArray().size(); i++) {
			if (sourceDim.size() == 0) {
				problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
				return;
			}
			sourceDim.remove(0);
		}
		if (targetDim.size() != sourceDim.size())
			problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_NOT_SAME_DIMENSIONS, ass));
		else {
			HDLEvaluationContext context = getContext(hContext, ass);
			for (int i = 0; i < targetDim.size(); i++) {
				HDLExpression target = targetDim.get(i);
				HDLExpression source = sourceDim.get(i);
				BigInteger t = target.constantEvaluate(context);
				BigInteger s = source.constantEvaluate(context);
				if (s == null) {
					problems.add(new Problem(ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT, right));
				}
				if (t == null) {
					problems.add(new Problem(ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT, left));
				}
				if ((t != null) && (s != null)) {
					if (!s.equals(t)) {
						problems.add(new Problem(ErrorCode.ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS, ass));
					}
				}
			}
		}
	}

	private static HDLEvaluationContext getContext(Map<HDLQualifiedName, HDLEvaluationContext> hContext, IHDLObject var) {
		HDLUnit container = var.getContainer(HDLUnit.class);
		if (container == null)
			return null;
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

	@Override
	public Class<?> getErrorClass() {
		return ErrorCode.class;
	}

	@Override
	public HDLAdvise advise(Problem problem) {
		return BuiltInAdvisor.advise(problem);
	}

	@Override
	public String getName() {
		return "PSHDL Validator";
	}

}
