/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package de.tuhh.ict.pshdl.model.validation.builtin;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;
import static de.tuhh.ict.pshdl.model.utils.HDLQuery.*;

import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.*;
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
			checkFunctionCalls(pkg, problems, hContext);
			pkg = Insulin.inlineFunctions(pkg);
			RWValidation.checkVariableUsage(pkg, problems);
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
			checkConstantPackageDeclarations(pkg, problems, hContext);
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
			checkRegisters(pkg, problems, hContext);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void checkConstantPackageDeclarations(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, false);
		for (HDLVariableDeclaration hvd : hvds) {
			if (hvd.getRegister() != null) {
				problems.add(new Problem(ErrorCode.GLOBAL_CANT_REGISTER, hvd));
			}
			if (hvd.getDirection() != HDLDirection.CONSTANT) {
				problems.add(new Problem(ErrorCode.GLOBAL_NOT_CONSTANT, hvd));
			}
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.GLOBAL_NOT_CONSTANT, var));
				} else {
					Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(var.getDefaultValue());
					if (!valueOf.isPresent()) {
						problems.add(new Problem(ErrorCode.GLOBAL_NOT_CONSTANT, var));
					}
				}
			}
		}
	}

	private void checkRegisters(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds)
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
			}
	}

	private void checkRangeDirections(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLRange[] ranges = pkg.getAllObjectsOf(HDLRange.class, true);
		for (HDLRange hdlRange : ranges) {
			// If it is a single range, there is no direction :)
			HDLExpression from = hdlRange.getFrom();
			if (from == null) {
				continue;
			}
			if (skipExp(hdlRange)) {
				continue;
			}
			HDLEvaluationContext context = getContext(hContext, hdlRange);
			// For loop ranges are up to
			Optional<Range<BigInteger>> fromRangeOf = RangeExtension.rangeOf(from, context);
			if (!fromRangeOf.isPresent()) {
				problems.add(new Problem(ErrorCode.UNKNOWN_RANGE, from));
				continue;
			}
			HDLExpression to = hdlRange.getTo();
			Optional<Range<BigInteger>> toRangeOf = RangeExtension.rangeOf(to, context);
			if (!toRangeOf.isPresent()) {
				problems.add(new Problem(ErrorCode.UNKNOWN_RANGE, to));
				continue;
			}
			if (fromRangeOf.get().isConnected(toRangeOf.get())) {
				problems.add(new Problem(ErrorCode.RANGE_OVERLAP, hdlRange));
				continue;
			}
			if (hdlRange.getContainer() instanceof HDLForLoop) {
				if (fromRangeOf.get().upperEndpoint().compareTo(toRangeOf.get().lowerEndpoint()) > 0) {
					problems.add(new Problem(ErrorCode.RANGE_NOT_UP, hdlRange));
				}
			} else if (toRangeOf.get().lowerEndpoint().compareTo(fromRangeOf.get().upperEndpoint()) > 0) {
				problems.add(new Problem(ErrorCode.RANGE_NOT_DOWN, hdlRange));
			}
		}
	}

	private void checkBitAcces(HDLPackage pkg, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariableRef[] refs = pkg.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (skipExp(ref)) {
				continue;
			}
			if (ref.getBits().size() != 0) {
				Optional<HDLVariable> resolveVar = ref.resolveVar();
				if (resolveVar.isPresent()) {
					Optional<? extends HDLType> varType = TypeExtension.typeOf(resolveVar.get());
					if (!varType.isPresent()) {
						// Don't know type, do nothing then..
					} else if (!(varType.get() instanceof HDLPrimitive)) {
						problems.add(new Problem(ErrorCode.BIT_ACCESS_NOT_POSSIBLE_ON_TYPE, ref, varType.get()));
					} else {
						HDLPrimitive primitive = (HDLPrimitive) varType.get();
						switch (primitive.getType()) {
						case BITVECTOR:
						case INT:
						case UINT:
							break;
						default:
							problems.add(new Problem(ErrorCode.BIT_ACCESS_NOT_POSSIBLE, ref, varType.get()));
						}
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
				if (contFrag.getSub() == fragment) {
					continue;
				}
			}
			problems.add(new Problem(ErrorCode.UNRESOLVED_FRAGMENT, fragment));
		}
		HDLResolvedRef[] refs = pkg.getAllObjectsOf(HDLResolvedRef.class, true);
		for (HDLResolvedRef ref : refs) {
			Optional<HDLVariable> resolveVar = ref.resolveVar();
			if (!resolveVar.isPresent()) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, ref));
			}
			if (ref instanceof HDLEnumRef) {
				HDLEnumRef enumRef = (HDLEnumRef) ref;
				Optional<HDLEnum> resolveHEnum = enumRef.resolveHEnum();
				if (!resolveHEnum.isPresent()) {
					problems.add(new Problem(ErrorCode.UNRESOLVED_ENUM, ref));
				}
			}
			if (ref instanceof HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				Optional<HDLVariable> hIf = hir.resolveHIf();
				if (!hIf.isPresent()) {
					problems.add(new Problem(ErrorCode.UNRESOLVED_VARIABLE, ref));
				}
			}
		}
		HDLFunctionCall[] functionCalls = pkg.getAllObjectsOf(HDLFunctionCall.class, true);
		for (HDLFunctionCall call : functionCalls) {
			Optional<HDLFunction> function = call.resolveName();
			if (!function.isPresent()) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_FUNCTION, call));
			}
		}
		HDLVariableDeclaration[] hvds = pkg.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds) {
			Optional<? extends HDLType> type = hvd.resolveType();
			if (!type.isPresent()) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_TYPE, hvd));
			}
		}
		HDLInterfaceInstantiation[] hiis = pkg.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (HDLInterfaceInstantiation hii : hiis) {
			Optional<HDLInterface> type = hii.resolveHIf();
			if (!type.isPresent()) {
				problems.add(new Problem(ErrorCode.UNRESOLVED_INTERFACE, hii));
			}
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
			if (skipExp(ass)) {
				continue;
			}
			HDLEvaluationContext context = getContext(hContext, ass);
			checkAss(ass, ass.getLeft(), ass.getRight(), problems, context);
		}
		HDLVariable[] vars = pkg.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars)
			if (hdlVariable.getDefaultValue() != null) {
				HDLEvaluationContext context = getContext(hContext, hdlVariable);
				checkAss(hdlVariable, hdlVariable, hdlVariable.getDefaultValue(), problems, context);
			}
	}

	private void checkAss(IHDLObject obj, IHDLObject leftRef, HDLExpression rightExp, Set<Problem> problems, HDLEvaluationContext context) {
		Optional<? extends HDLType> lType = TypeExtension.typeOf(leftRef);
		Optional<? extends HDLType> rType = TypeExtension.typeOf(rightExp);
		if ((!lType.isPresent()) || (!rType.isPresent()))
			return;
		switch (lType.get().getClassType()) {
		case HDLEnum:
			if (rType.get().getClassType() != HDLClass.HDLEnum) {
				problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_ENUM, obj));
			}
			break;
		case HDLPrimitive:
			if (rType.get().getClassType() != HDLClass.HDLPrimitive) {
				problems.add(new Problem(ErrorCode.ASSIGNMENT_NOT_PRIMITIVE, obj));
			} else {
				HDLPrimitive left = (HDLPrimitive) lType.get();
				HDLPrimitive right = (HDLPrimitive) rType.get();
				switch (left.getType()) {
				case BIT:
					if (right.getType() != HDLPrimitiveType.BIT)
						if (right.getWidth() != null) {
							Optional<BigInteger> w = ConstantEvaluate.valueOf(right.getWidth(), context);
							if (w.isPresent() && !w.get().equals(BigInteger.ONE)) {
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
				Optional<? extends HDLType> type = TypeExtension.typeOf(exp);
				if (type.isPresent())
					if (type.get() instanceof HDLPrimitive) {
						HDLPrimitive prim = (HDLPrimitive) type.get();
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
					} else {
						problems.add(new Problem(ErrorCode.CONCAT_TYPE_NOT_ALLOWED, exp, type.get()));
					}
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
			Optional<? extends HDLType> type = TypeExtension.typeOf(switchStatement.getCaseExp());
			if (!type.isPresent()) {
				continue;
			}
			if (type.get() instanceof HDLPrimitive) {
				HDLPrimitive primitive = (HDLPrimitive) type.get();
				if (primitive.getWidth() == null) {
					problems.add(new Problem(ErrorCode.SWITCH_CASE_NEEDS_WIDTH, switchStatement.getCaseExp()));
				}
			}
			boolean isEnum = type.get() instanceof HDLEnum;
			for (HDLSwitchCaseStatement caseStatement : cases) {
				HDLExpression label = caseStatement.getLabel();
				if (label == null) {
					if (defaultFound) {
						problems.add(new Problem(ErrorCode.SWITCH_MULTIPLE_DEFAULT, caseStatement));
					}
					defaultFound = true;
				} else if (!isEnum) {
					Optional<BigInteger> constant = ConstantEvaluate.valueOf(label, null);
					if (!constant.isPresent()) {
						problems.add(new Problem(ErrorCode.SWITCH_LABEL_NOT_CONSTANT, caseStatement));
					} else if (!values.add(constant.get())) {
						problems.add(new Problem(ErrorCode.SWITCH_LABEL_DUPLICATE, caseStatement));
					}
				} else {
					Optional<? extends HDLType> labelType = TypeExtension.typeOf(label);
					if (labelType.isPresent() && !type.get().equals(labelType.get())) {
						problems.add(new Problem(ErrorCode.SWITCH_LABEL_WRONG_ENUM, caseStatement));
					}
					if ((label instanceof HDLEnumRef) && !enums.add(((HDLEnumRef) label).getVarRefName())) {
						problems.add(new Problem(ErrorCode.SWITCH_LABEL_DUPLICATE, caseStatement));
					}
				}
			}
			if (!defaultFound) {
				problems.add(new Problem(ErrorCode.SWITCH_NO_DEFAULT, switchStatement));
			}
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
				if (otherName.equals(fullName)) {
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME, hdlVariable, put));
				} else {
					problems.add(new Problem(ErrorCode.VARIABLE_SAME_NAME_DIFFERENT_CASE, hdlVariable, put));
				}
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
					if (otherName.equals(newName)) {
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME, var, otherVar));
					} else {
						problems.add(new Problem(ErrorCode.VARIABLE_SCOPE_SAME_NAME_DIFFERENT_CASE, var, otherVar));
					}
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
			// Substitute functions don't have any interference info because
			// they don't actually return anything
			if (info == null) {
				try {
					Optional<HDLFunction> f = function.resolveName();
					if (!f.isPresent()) {
						problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
					} else {
						HDLFunction hdlFunction = f.get();
						switch (hdlFunction.getClassType()) {
						case HDLSubstituteFunction:
							HDLSubstituteFunction sub = (HDLSubstituteFunction) hdlFunction;
							if (sub.getArgs().size() != function.getParams().size()) {
								problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
							}
						}
					}
				} catch (Exception e) {
					problems.add(new Problem(ErrorCode.NO_SUCH_FUNCTION, function));
				}
			}
		}
	}

	private static void checkProessWrite(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable var : vars)
			if (var.hasMeta(RWValidation.BlockMetaClash.clash)) {
				problems.add(new Problem(ErrorCode.MULTI_PROCESS_WRITE, var));
			}
	}

	private static void checkType(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLOpExpression[] ops = unit.getAllObjectsOf(HDLOpExpression.class, true);
		for (HDLOpExpression ope : ops) {
			if (skipExp(ope)) {
				continue;
			}
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
			if (info.error != null) {
				problems.add(new Problem(ErrorCode.UNSUPPORTED_TYPE_FOR_OP, ope, info.error));
			}
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
			if (ref instanceof HDLUnresolvedFragment)
				return;
			Optional<HDLVariable> var = ((HDLResolvedRef) ref).resolveVar();
			if ((var.isPresent()) && (var.get().getRegisterConfig() == null)) {
				HDLBlock container = ass.getContainer(HDLBlock.class);
				if ((container != null) && container.getProcess()) {
					// If the assignment is happening within a process, chances
					// are that the dev is trying something legal
					continue;
				}
				problems.add(new Problem(ErrorCode.COMBINED_ASSIGNMENT_NOT_ALLOWED, ass));
			}
		}
	}

	private static void checkConstantEquals(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLEqualityOp[] equalities = unit.getAllObjectsOf(HDLEqualityOp.class, true);
		for (HDLEqualityOp op : equalities) {
			if (skipExp(op)) {
				continue;
			}
			Optional<BigInteger> res = ConstantEvaluate.valueOf(op, getContext(hContext, op));
			if (res.isPresent())
				if (res.get().equals(BigInteger.ONE)) {
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_TRUE, op));
				} else {
					problems.add(new Problem(ErrorCode.EQUALITY_ALWAYS_FALSE, op));
				}
		}
	}

	public static boolean skipExp(IHDLObject op) {
		return (op.getContainer(HDLInlineFunction.class) != null) || (op.getContainer(HDLSubstituteFunction.class) != null);
	}

	private static void checkConstantBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		// XXX Check Array Dimensions
		Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class)//
				.from(unit).where(HDLVariableDeclaration.fDirection)//
				.matches(isEqualTo(HDLDirection.CONSTANT))//
				.or(isEqualTo(HDLDirection.PARAMETER)) //
				.getAll();
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				HDLExpression def = var.getDefaultValue();
				if (var.getDefaultValue() == null) {
					problems.add(new Problem(ErrorCode.CONSTANT_NEED_DEFAULTVALUE, var));
				} else {
					if (def instanceof HDLArrayInit) {
						checkConstantsArrayInit(problems, (HDLArrayInit) def);
					} else {
						Optional<BigInteger> constant = ConstantEvaluate.valueOf(def, getContext(hContext, var));
						if (!constant.isPresent())
							if (!(def instanceof HDLLiteral)) {
								problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, def, var, null));
							}
					}
				}
			}
		}
		HDLForLoop[] forLoops = unit.getAllObjectsOf(HDLForLoop.class, true);
		for (HDLForLoop hdlForLoop : forLoops) {
			for (HDLRange r : hdlForLoop.getRange()) {
				Optional<BigInteger> evalTo = ConstantEvaluate.valueOf(r.getTo(), getContext(hContext, r));
				if (!evalTo.isPresent()) {
					problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getTo(), r, null));
				}
				if (r.getFrom() != null) {
					Optional<BigInteger> evalFrom = ConstantEvaluate.valueOf(r.getFrom(), getContext(hContext, r));
					if (!evalFrom.isPresent()) {
						problems.add(new Problem(ErrorCode.FOR_LOOP_RANGE_NOT_CONSTANT, r.getFrom(), r, null));
					}
				}
			}
		}
	}

	private static void checkConstantsArrayInit(Set<Problem> problems, HDLArrayInit arrayInit) {
		for (HDLExpression exp : arrayInit.getExp()) {
			Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(exp);
			if (!valueOf.isPresent())
				if (exp instanceof HDLArrayInit) {
					HDLArrayInit hai = (HDLArrayInit) exp;
					checkConstantsArrayInit(problems, hai);
				} else {
					problems.add(new Problem(ErrorCode.CONSTANT_DEFAULT_VALUE_NOT_CONSTANT, exp, arrayInit, null));
				}
		}
	}

	private static void checkArrayBoundaries(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> hContext) {
		HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable var : vars) {
			HDLDirection dir = var.getDirection();
			if ((dir != null) && dir.isIO()) {
				for (HDLExpression dim : var.getDimensions()) {
					Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(dim);
					if (!valueOf.isPresent()) {
						problems.add(new Problem(ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT, dim));
					}
				}
			}
		}
		HDLVariableRef[] refs = unit.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (skipExp(ref)) {
				continue;
			}
			Optional<HDLVariable> resolveVar = ref.resolveVar();
			if (resolveVar.isPresent()) {
				ArrayList<HDLExpression> dimensions = resolveVar.get().getDimensions();
				compareBoundaries(problems, hContext, ref, dimensions, ref.getArray());
				if (ref instanceof HDLInterfaceRef) {
					HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					Optional<HDLVariable> var = hir.resolveHIf();
					if (var.isPresent()) {
						compareBoundaries(problems, hContext, ref, var.get().getDimensions(), hir.getIfArray());
					}
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
					Optional<HDLVariable> var = varRef.resolveVar();
					if (var.isPresent()) {
						HDLEvaluationContext context = getContext(hContext, ass);
						ArrayList<HDLExpression> targetDim = var.get().getDimensions();
						for (int i = 0; i < varRef.getArray().size(); i++) {
							if (targetDim.size() == 0) {
								problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, varRef));
								return;
							}
							targetDim.remove(0);
						}
						if (left != ref) {
							validateArrayAssignment(problems, context, ref, ass, left, targetDim);
						} else if (ass.getRight().getClassType() != HDLClass.HDLVariableRef) {
							problems.add(new Problem(ErrorCode.ARRAY_WRITE_MULTI_DIMENSION, ass));
						}
					}
				}
			} else if (container instanceof HDLVariable) {
				HDLVariable var = (HDLVariable) container;
				HDLEvaluationContext context = getContext(hContext, var);
				validateArrayAssignment(problems, context, ref, var, var, var.getDimensions());
			} else {
				problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_FEW_DIMENSIONS_IN_EXPRESSION, ref));
			}
		}
		int dim = 0;
		for (HDLExpression arr : array) {
			HDLEvaluationContext context = getContext(hContext, arr);
			Optional<Range<BigInteger>> accessRangeRaw = RangeExtension.rangeOf(arr, context);
			if (!accessRangeRaw.isPresent()) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, arr));
				break;
			}
			Optional<Range<BigInteger>> arrayRangeRaw = RangeExtension.rangeOf(dimensions.get(dim), context);
			if (!arrayRangeRaw.isPresent()) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NO_RANGE, dimensions.get(dim)));
				break;
			}
			Range<BigInteger> accessRange = accessRangeRaw.get();
			Range<BigInteger> arrayRange = arrayRangeRaw.get();
			BigInteger upperEndpoint = arrayRange.upperEndpoint();
			arrayRange = Ranges.closed(BigInteger.ZERO, upperEndpoint.subtract(BigInteger.ONE));
			String info = "Expected value range:" + accessRange;
			if (accessRange.upperEndpoint().signum() < 0) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			} else if (accessRange.lowerEndpoint().signum() < 0) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_NEGATIVE, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			}
			if (!arrayRange.isConnected(accessRange)) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			} else if (accessRange.upperEndpoint().compareTo(upperEndpoint) > 0) {
				problems.add(new Problem(ErrorCode.ARRAY_INDEX_POSSIBLY_OUT_OF_BOUNDS, arr, ref, info).addMeta(ACCESS_RANGE, accessRange).addMeta(ARRAY_RANGE, arrayRange));
			}

			dim++;
		}
	}

	private static void validateArrayAssignment(Set<Problem> problems, HDLEvaluationContext context, HDLVariableRef ref, IHDLObject ass, IHDLObject left,
			ArrayList<HDLExpression> targetDim) {
		Optional<HDLVariable> right = ref.resolveVar();
		if (!right.isPresent())
			return;
		ArrayList<HDLExpression> sourceDim = right.get().getDimensions();
		for (int i = 0; i < ref.getArray().size(); i++) {
			if (sourceDim.size() == 0) {
				problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_TOO_MANY_DIMENSIONS, ref));
				return;
			}
			sourceDim.remove(0);
		}
		if (targetDim.size() != sourceDim.size()) {
			problems.add(new Problem(ErrorCode.ARRAY_REFERENCE_NOT_SAME_DIMENSIONS, ass));
		} else {
			HDLDirection direction = right.get().getDirection();
			if ((direction != null) && direction.isIO()) {
				context = null;
			}
			for (int i = 0; i < targetDim.size(); i++) {
				HDLExpression source = sourceDim.get(i);
				Optional<BigInteger> s = ConstantEvaluate.valueOf(source, context);
				if (!s.isPresent()) {
					problems.add(new Problem(ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT, right.get()));
				}
				HDLExpression target = targetDim.get(i);
				Optional<BigInteger> t = ConstantEvaluate.valueOf(target, context);
				if (!t.isPresent()) {
					problems.add(new Problem(ErrorCode.ARRAY_DIMENSIONS_NOT_CONSTANT, left));
				}
				if ((t != null) && (s != null))
					if (!s.equals(t)) {
						problems.add(new Problem(ErrorCode.ARRAY_ASSIGNMENT_NOT_SAME_DIMENSIONS, ass));
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
		if (clocks.size() > 1) {
			for (HDLAnnotation anno : clocks) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_CLOCK_ANNOTATION_ALLOWED, anno));
			}
		}
		Collection<HDLAnnotation> resets = HDLQuery.select(HDLAnnotation.class).from(unit).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.reset.toString()).getAll();
		if (resets.size() > 1) {
			for (HDLAnnotation anno : resets) {
				problems.add(new Problem(ErrorCode.ONLY_ONE_RESET_ANNOTATION_ALLOWED, anno));
			}
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
