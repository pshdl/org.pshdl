package de.tuhh.ict.pshdl.model.validation.builtin;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;

public class BuiltInValidator implements IHDLValidator {

	public static final GenericMeta<Range<BigInteger>> ARRAY_RANGE = new GenericMeta<Range<BigInteger>>("arrayRange", true);
	public static final GenericMeta<Range<BigInteger>> ACCESS_RANGE = new GenericMeta<Range<BigInteger>>("accessRange", true);
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
	public boolean check(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// TODO find a way to distinguish between context dependent problems and
		// others
		try {
			checkUnresolved(pkg, problems);
			RWValidation.checkVariableUsage(pkg, problems);
			checkFunctionCalls(pkg, problems, hContext);
			pkg = Insulin.inlineFunctions(pkg);
			pkg = Insulin.setParameterOnInstance(pkg);
			checkVariableNaming(pkg, problems);
			checkClockAndResetAnnotation(pkg, problems);
			checkConstantBoundaries(pkg, problems, hContext);
			checkArrayBoundaries(pkg, problems, hContext);
			checkConstantEquals(pkg, problems, hContext);
			checkBitAcces(pkg, problems, hContext);
			// TODO Validate value ranges, check for 0 divide
			checkRangeDirections(pkg, problems, hContext);
			// TODO Check for POW only power of 2
			checkCombinedAssignment(pkg, problems, hContext);
			checkAnnotations(pkg, problems, hContext);
			checkType(pkg, problems, hContext);
			checkProessWrite(pkg, problems, hContext);
			checkGenerators(pkg, problems, hContext);
			checkLiteralConcat(pkg, problems);
			// TODO Validate bitWidth mismatch
			checkAssignments(pkg, problems, hContext);
			// TODO Multi-bit Write only for Constants
			// TODO check for signals named clk or rst and warn about the
			// collision
			// TODO check for proper variable naming
			// TODO check for valid parameter
			checkSwitchStatements(pkg, problems, hContext);
			// TODO Type checking!
			// TODO Check for combinatorical loop.
			// TODO Check for multiple assignment in same Scope
			// TODO No processes in Module
			// TODO no I/O variables in block
			// TODO warn for name collision in generators
			// TODO Out port array size need to be constant
			// TODO Check for bit access on left assignment side when right is
			// matching
			// TODO no Registers in Testbench/Global gen
			// TODO check for register of constant/parameter
			// TODO check for register of global constants
			checkRegisters(pkg, problems, hContext);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void checkRegisters(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds) {
			if (hvd.getRegister() != null) {
				switch (hvd.getDirection()) {
				case CONSTANT:
				case PARAMETER:
					problems.add(new Problem(ErrorCode.CONSTANT_PORT_CANT_REGISTER, hvd));
					break;
				case IN:
					problems.add(new Problem(ErrorCode.IN_PORT_CANT_REGISTER, hvd));
					break;
				default:
				}
				if (hvd.getContainer().getClassType() == HDLClass.HDLPackage) {
					problems.add(new Problem(ErrorCode.GLOBAL_CANT_REGISTER, hvd));
				}
			}
		}
	}

	private void checkRangeDirections(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLRange[] ranges = pkg.getAllObjectsOf(HDLRange.class, true);
		for (HDLRange hdlRange : ranges) {
			// If it is a single range, there is no direction :)
			HDLExpression from = hdlRange.getFrom();
			if (from == null)
				continue;
			if (skipExp(hdlRange))
				continue;
			HDLEvaluationContext context = getContext(hContext, hdlRange);
			// For loop ranges are up to
			Range<BigInteger> fromRangeOf = RangeExtension.rangeOf(from, context);
			HDLExpression to = hdlRange.getTo();
			Range<BigInteger> toRangeOf = RangeExtension.rangeOf(to, context);
			if (fromRangeOf == null) {
				problems.add(new Problem(ErrorCode.UNKNOWN_RANGE, from));
				continue;
			}
			if (toRangeOf == null) {
				problems.add(new Problem(ErrorCode.UNKNOWN_RANGE, to));
				continue;
			}
			if (fromRangeOf.isConnected(toRangeOf)) {
				problems.add(new Problem(ErrorCode.RANGE_OVERLAP, hdlRange));
				continue;
			}
			if (hdlRange.getContainer() instanceof HDLForLoop) {
				if (fromRangeOf.upperEndpoint().compareTo(toRangeOf.lowerEndpoint()) > 0) {
					problems.add(new Problem(ErrorCode.RANGE_NOT_UP, hdlRange));
				}
			} else {
				if (toRangeOf.lowerEndpoint().compareTo(fromRangeOf.upperEndpoint()) > 0) {
					problems.add(new Problem(ErrorCode.RANGE_NOT_DOWN, hdlRange));
				}
			}
		}
	}

	private void checkBitAcces(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariableRef[] refs = pkg.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (skipExp(ref))
				continue;
			if (ref.getBits().size() != 0) {
				HDLType varType = TypeExtension.typeOf(ref.resolveVar());
				if (!(varType instanceof HDLPrimitive)) {
					problems.add(new Problem(ErrorCode.BIT_ACCESS_NOT_POSSIBLE_ON_TYPE, ref, varType));
				} else {
					HDLPrimitive primitive = (HDLPrimitive) varType;
					switch (primitive.getType()) {
					case BITVECTOR:
					case INT:
					case UINT:
						break;
					default:
						problems.add(new Problem(ErrorCode.BIT_ACCESS_NOT_POSSIBLE, ref, varType));
					}
				}
			}
		}
	}

	private void checkUnresolved(HDLPackage pkg, Set<Problem> problems) {
		HDLUnresolvedFragment[] fragments = pkg.getAllObjectsOf(HDLUnresolvedFragment.class, true);
		for (HDLUnresolvedFragment fragment : fragments) {
			IHDLObject container = fragment.getContainer();
			if ((container instanceof HDLUnresolvedFragment)) {
				HDLUnresolvedFragment contFrag = (HDLUnresolvedFragment) container;
				if (contFrag.getSub() == fragment)
					continue;
			}
			problems.add(new Problem(ErrorCode.UNRESOLVED_FRAGMENT, fragment));
		}
		HDLResolvedRef[] refs = pkg.getAllObjectsOf(HDLResolvedRef.class, true);
		for (HDLResolvedRef ref : refs) {
			HDLVariable resolveVar = ref.resolveVar();
			if (resolveVar == null) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, ref));
			}
			if (ref instanceof HDLEnumRef) {
				HDLEnumRef enumRef = (HDLEnumRef) ref;
				HDLEnum resolveHEnum = enumRef.resolveHEnum();
				if (resolveHEnum == null) {
					problems.add(new Problem(ErrorCode.UNRESOLVED_ENUM, ref));
				}
			}
			if (ref instanceof HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				HDLVariable hIf = hir.resolveHIf();
				if (hIf == null) {
					problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, ref));
				}
			}
		}
		HDLFunctionCall[] functionCalls = pkg.getAllObjectsOf(HDLFunctionCall.class, true);
		for (HDLFunctionCall call : functionCalls) {
			HDLFunction function = call.resolveName();
			if (function == null)
				problems.add(new Problem(ErrorCode.UNRESOLVED_FUNCTION, call));
		}
		HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds) {
			HDLType type = hvd.resolveType();
			if (type == null)
				problems.add(new Problem(ErrorCode.UNRESOLVED_TYPE, hvd));
		}
		HDLInterfaceInstantiation[] hiis = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (HDLInterfaceInstantiation hii : hiis) {
			HDLType type = hii.resolveHIf();
			if (type == null)
				problems.add(new Problem(ErrorCode.UNRESOLVED_INTERFACE, hii));
		}
		HDLRegisterConfig[] regs = pkg.getAllObjectsOf(HDLRegisterConfig.class, true);
		for (HDLRegisterConfig reg : regs) {
			if (reg.resolveClk() == null) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, reg));
			}
			if (reg.resolveRst() == null) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, reg));
			}
		}
	}

	private void checkAssignments(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLAssignment[] asss = pkg.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss) {
			if (skipExp(ass))
				continue;
			HDLEvaluationContext context = getContext(hContext, ass);
			checkAss(ass, ass.getLeft(), ass.getRight(), problems, context);
		}
		HDLVariable[] vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			if (hdlVariable.getDefaultValue() != null) {
				HDLEvaluationContext context = getContext(hContext, hdlVariable);
				checkAss(hdlVariable, hdlVariable, hdlVariable.getDefaultValue(), problems, context);
			}
		}
	}

	private void checkAss(IHDLObject obj, IHDLObject leftRef, HDLExpression rightExp, Set<Problem> problems, HDLEvaluationContext context) {
		HDLType lType = TypeExtension.typeOf(leftRef);
		HDLType rType = TypeExtension.typeOf(rightExp);
		if ((lType == null) || (rType == null))
			return;
		switch (lType.getClassType()) {
		case HDLEnum:
			if (rType.getClassType() != HDLClass.HDLEnum)
				problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_ENUM, obj));
			break;
		case HDLPrimitive:
			if (rType.getClassType() != HDLClass.HDLPrimitive) {
				problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_PRIMITIVE, obj));
			} else {
				HDLPrimitive left = (HDLPrimitive) lType;
				HDLPrimitive right = (HDLPrimitive) rType;
				switch (left.getType()) {
				case BIT:
					if (right.getType() != HDLPrimitiveType.BIT) {
						if (right.getWidth() != null) {
							BigInteger w = ConstantEvaluate.valueOf(right.getWidth(), context);
							if (!w.equals(BigInteger.ONE))
								problems.add(new Problem(ErrorCode.ASSIGNMENT_CLIPPING_WILL_OCCUR, rightExp, obj));
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
			problems.add(new Problem(ErrorCode.ASSIGNEMENT_NOT_SUPPORTED, obj));
		}
	}

	private void checkLiteralConcat(HDLPackage pkg, Set<Problem> problems) {
		HDLConcat[] concats = pkg.getAllObjectsOf(HDLConcat.class, true);
		for (HDLConcat hdlConcat : concats) {
			ArrayList<HDLExpression> cats = hdlConcat.getCats();
			for (HDLExpression exp : cats) {
				HDLType type = TypeExtension.typeOf(exp);
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
			HDLType type = TypeExtension.typeOf(switchStatement.getCaseExp());
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
						BigInteger constant = ConstantEvaluate.valueOf(label, null);
						if (constant == null) {
							problems.add(new Problem(ErrorCode.SWITCH_LABEL_NOT_CONSTANT, caseStatement));
						} else {
							if (!values.add(constant))
								problems.add(new Problem(ErrorCode.SWITCH_LABEL_DUPLICATE, caseStatement));
						}
					} else {
						HDLType labelType = TypeExtension.typeOf(label);
						if (!type.equals(labelType))
							problems.add(new Problem(ErrorCode.SWITCH_LABEL_WRONG_ENUM, caseStatement));
						if ((label instanceof HDLEnumRef) && !enums.add(((HDLEnumRef) label).getVarRefName()))
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
			HDLQualifiedName fullName = fullNameOf(hdlVariable);
			if (keywordSet.contains(fullName.getLastSegment())) {
				problems.add(new Problem(ErrorCode.VARIABLE_KEYWORD_NAME, hdlVariable));
			}
			HDLVariable put = nameMap.put(fullName.toString().toLowerCase(), hdlVariable);
			if (put != null) {
				HDLQualifiedName otherName = fullNameOf(put);
				if (otherName.equals(fullName))
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME, hdlVariable, put));
				else
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME_DIFFERENT_CASE, hdlVariable, put));
			}
		}
		for (Entry<String, HDLVariable> entry : nameMap.entrySet()) {
			HDLVariable var = entry.getValue();
			HDLQualifiedName fullName = fullNameOf(var);
			String lastSegment = fullName.getLastSegment();
			HDLQualifiedName type = fullName.getTypePart();
			HDLQualifiedName localPart = fullName.getLocalPart().skipLast(1);
			for (int i = 0; i < localPart.length; i++) {
				HDLQualifiedName scoped = localPart.skipLast(i + 1).append(lastSegment);
				HDLQualifiedName newName = type.append(scoped);
				HDLVariable otherVar = nameMap.get(newName.toString().toLowerCase());
				if (otherVar != null) {
					HDLQualifiedName otherName = fullNameOf(otherVar);
					if (otherName.equals(newName))
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME, var, otherVar));
					else
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE, var, otherVar));
				}
			}
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
			HDLReference ref = ass.getLeft();
			if (ref instanceof HDLUnresolvedFragment) {
				return;
			}
			HDLVariable var = ((HDLResolvedRef) ref).resolveVar();
			if ((var != null) && (var.getRegisterConfig() == null)) {
				problems.add(new Problem(ErrorCode.COMBINED_ASSIGNMENT_NOT_ALLOWED, ass));
			}
		}
	}

	private static void checkConstantEquals(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLEqualityOp[] equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (HDLEqualityOp op : equalities) {
			if (skipExp(op))
				continue;
			BigInteger res = ConstantEvaluate.valueOf(op, getContext(hContext, op));
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
					BigInteger constant = ConstantEvaluate.valueOf(def, getContext(hContext, var));
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
				BigInteger evalTo = ConstantEvaluate.valueOf(r.getTo(), getContext(hContext, r));
				if (evalTo == null) {
					problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					BigInteger evalFrom = ConstantEvaluate.valueOf(r.getFrom(), getContext(hContext, r));
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
			HDLVariable resolveVar = ref.resolveVar();
			if (resolveVar != null) {
				ArrayList<HDLExpression> dimensions = resolveVar.getDimensions();
				compareBoundaries(problems, hContext, ref, dimensions, ref.getArray());
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					HDLVariable var = hir.resolveHIf();
					if (var != null)
						compareBoundaries(problems, hContext, ref, var.getDimensions(), hir.getIfArray());
				}
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
			IHDLObject container = ref.getContainer();
			if (container instanceof HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) container;
				HDLReference left = ass.getLeft();
				if (left.getClassType() == HDLClass.HDLEnumRef) {
					problems.add(new Problem(ErrorCode.ASSIGNMENT_ENUM_NOT_WRITABLE, left));
				} else {
					HDLVariableRef varRef = (HDLVariableRef) left;
					HDLVariable var = varRef.resolveVar();
					if (var != null) {
						ArrayList<HDLExpression> targetDim = var.getDimensions();
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
				}
			} else if (container instanceof HDLVariable) {
				HDLVariable var = (HDLVariable) container;
				validateArrayAssignment(problems, hContext, ref, var, var, var.getDimensions());
			} else
				problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION, ref));
		}
		int dim = 0;
		for (HDLExpression arr : array) {
			HDLEvaluationContext context = getContext(hContext, arr);
			Range<BigInteger> accessRange = RangeExtension.rangeOf(arr, context);
			if (accessRange == null) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, arr));
				break;
			}
			Range<BigInteger> arrayRange = RangeExtension.rangeOf(dimensions.get(dim), context);
			if (arrayRange == null) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, dimensions.get(dim)));
				break;
			}
			BigInteger upperEndpoint = arrayRange.upperEndpoint();
			arrayRange = Ranges.closed(BigInteger.ZERO, upperEndpoint.subtract(BigInteger.ONE));
			String info = "Expected value range:" + accessRange;
			if (accessRange.upperEndpoint().signum() < 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			else if (accessRange.lowerEndpoint().signum() < 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			if (!arrayRange.isConnected(accessRange))
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			else if (accessRange.upperEndpoint().compareTo(upperEndpoint) > 0)
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));

			dim++;
		}
	}

	private static void validateArrayAssignment(Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext, HDLVariableRef ref, IHDLObject ass, IHDLObject left,
			ArrayList<HDLExpression> targetDim) {
		HDLVariable right = ref.resolveVar();
		if (right == null)
			return;
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
				BigInteger t = ConstantEvaluate.valueOf(target, context);
				BigInteger s = ConstantEvaluate.valueOf(source, context);
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
		HDLEvaluationContext hdlEvaluationContext = hContext.get(fullNameOf(container));
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
