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
package org.pshdl.model.utils;

import static org.pshdl.model.extensions.FullNameExtension.*;

import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLBitOp.HDLBitOpType;
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLShiftOp.HDLShiftOpType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.utils.services.IHDLGenerator.HDLGenerationInfo;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.RWValidation.Init;
import org.pshdl.model.validation.builtin.*;
import org.pshdl.model.validation.builtin.BuiltInValidator.IntegerMeta;

import com.google.common.base.*;
import com.google.common.collect.*;

public class Insulin {
	public static final GenericMeta<Boolean> insulated = new GenericMeta<Boolean>("insulated", true);

	public static <T extends IHDLObject> T transform(T orig, String src) {
		if (orig.hasMeta(insulated))
			return orig;
		T apply = resolveFragments(orig);
		RWValidation.annotateReadCount(apply);
		RWValidation.annotateWriteCount(apply);
		apply = inlineConstants(apply);
		apply = handlePostfixOp(apply);
		apply = handleDelayedSignals(apply);
		apply = handleOutPortRead(apply);
		apply = includeGenerators(apply, src);
		apply = inlineFunctions(apply);
		apply = setParameterOnInstance(apply);
		apply = pushSignIntoLiteral(apply);
		apply = generateClkAndReset(apply);
		apply = handleMultiBitAccess(apply, null);
		apply = handleMultiForLoop(apply);
		apply = generateInitializations(apply);
		apply = fixMultiDimAssignments(apply);
		apply = fixDoubleNegate(apply);
		apply = fortifyType(apply);
		apply.validateAllFields(orig.getContainer(), false);
		apply.setMeta(insulated);
		return apply;
	}

	public static <T extends IHDLObject> T fixMultiDimAssignments(T pkg) {
		final ModificationSet ms = new ModificationSet();
		final HDLAssignment[] asses = pkg.getAllObjectsOf(HDLAssignment.class, true);
		for (final HDLAssignment ass : asses) {
			if (BuiltInValidator.skipExp(ass)) {
				continue;
			}
			final HDLReference left = ass.getLeft();
			if (left.getClassType() == HDLClass.HDLVariableRef) {
				final HDLExpression right = ass.getRight();
				final HDLVariableRef lRef = (HDLVariableRef) left;
				final HDLType lRefType = TypeExtension.typeOf(lRef).get();
				// Generate loop for each assigned dimension
				if (right.getClassType() == HDLClass.HDLVariableRef) {
					final HDLVariableRef rRef = (HDLVariableRef) right;
					final HDLType rRefType = TypeExtension.typeOf(rRef).get();
					if (!lRefType.getDim().isEmpty() || !rRefType.getDim().isEmpty()) {
						final HDLStatement initLoop = createArrayForLoop(Collections.<HDLExpression> emptyList(), lRef.resolveVar().get().getDimensions(), lRef.getArray().size(),
								rRef, lRef, true);
						ms.replace(ass, initLoop);
					}
				}
				// Generate 0 fill then assign each value in a single assigment
				if (right.getClassType() == HDLClass.HDLArrayInit) {
					final HDLStatement initLoop = createArrayForLoop(Collections.<HDLExpression> emptyList(), lRef.resolveVar().get().getDimensions(), lRef.getArray().size(),
							HDLLiteral.get(0), lRef, false);
					final HDLArrayInit arr = (HDLArrayInit) right;
					final List<HDLStatement> assignments = Lists.newLinkedList();
					assignments.add(initLoop);
					final LinkedList<HDLLiteral> depth = Lists.newLinkedList();
					generateStaticAssignments(lRef, arr, depth, assignments);
					ms.replace(ass, assignments.toArray(new HDLStatement[assignments.size()]));

				}
			}
		}
		return ms.apply(pkg);
	}

	private static void generateStaticAssignments(HDLVariableRef lRef, HDLArrayInit arr, LinkedList<HDLLiteral> depth, List<HDLStatement> assignments) {
		final ArrayList<HDLExpression> exp = arr.getExp();
		for (int i = 0; i < exp.size(); i++) {
			final HDLExpression expression = exp.get(i);
			if (expression.getClassType() == HDLClass.HDLArrayInit) {
				@SuppressWarnings("unchecked")
				final LinkedList<HDLLiteral> clone = (LinkedList<HDLLiteral>) depth.clone();
				clone.add(HDLLiteral.get(i));
				generateStaticAssignments(lRef, (HDLArrayInit) expression, clone, assignments);
			} else {
				final ArrayList<HDLExpression> array = lRef.getArray();
				@SuppressWarnings("unchecked")
				final LinkedList<HDLLiteral> clone = (LinkedList<HDLLiteral>) depth.clone();
				clone.add(HDLLiteral.get(i));
				array.addAll(clone);
				assignments.add(new HDLAssignment().setLeft(lRef.setArray(array)).setRight(expression));
			}
		}
	}

	/**
	 * Find all constants and inline them
	 */
	public static <T extends IHDLObject> T inlineConstants(T pkg) {
		final ModificationSet ms = new ModificationSet();
		final Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class).from(pkg).where(HDLVariableDeclaration.fDirection)
				.isEqualTo(HDLDirection.CONSTANT).getAll();
		for (final HDLVariableDeclaration hvd : constants) {
			final Set<HDLVariable> remove = Sets.newHashSet();
			final ArrayList<HDLVariable> variables = hvd.getVariables();
			for (final HDLVariable var : variables) {
				final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(var.getDefaultValue());
				if (constVal.isPresent()) {
					final Collection<HDLVariableRef> refs = HDLQuery.getVarRefs(pkg, var);
					boolean allRefUsed = true;
					for (final HDLVariableRef ref : refs) {
						final Optional<BigInteger> refVal = ConstantEvaluate.valueOf(ref);
						if (refVal.isPresent()) {
							ms.replace(ref, HDLLiteral.get(refVal.get()));
						} else {
							allRefUsed = false;
						}
					}
					if (allRefUsed) {
						remove.add(var);
					}
				}
			}
			// Don't remove global constants
			if (hvd.getContainer(HDLUnit.class) == null) {
				continue;
			}
			variables.removeAll(remove);
			if (variables.isEmpty()) {
				ms.remove(hvd);
			} else {
				ms.replace(hvd, hvd.setVariables(variables));
			}
		}
		return ms.apply(pkg);
	}

	/**
	 * Finds all register config with delays in them and replaces them with a
	 * non delay version and a fifo
	 */
	public static <T extends IHDLObject> T handleDelayedSignals(T pkg) {
		final ModificationSet ms = new ModificationSet();
		final Collection<HDLRegisterConfig> delayRegs = HDLQuery.select(HDLRegisterConfig.class).from(pkg).where(HDLRegisterConfig.fDelay).isNotEqualTo(null).getAll();
		for (final HDLRegisterConfig delayedReg : delayRegs) {
			final HDLExpression delay = delayedReg.getDelay();
			final Optional<BigInteger> delayValue = ConstantEvaluate.valueOf(delay);
			if (delayValue.isPresent()) {
				if (delayValue.get().signum() == 0) {
					continue;
				}
			}
			final HDLRegisterConfig undelayedReg = delayedReg.setDelay(null);
			ms.replace(delayedReg, undelayedReg);
			HDLForLoop loop = HDLForLoop.tempLoop(HDLLiteral.get(1), delay);
			final HDLArithOp delayPlusOne = new HDLArithOp().setLeft(delay).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
			final HDLVariableDeclaration hvd = delayedReg.getContainer(HDLVariableDeclaration.class);
			for (final HDLVariable var : hvd.getVariables()) {
				ms.replace(var, var.setDefaultValue(null));
				final String dlyName = Insulin.getTempName(var.getName(), "dlyd");
				final HDLQualifiedName fqnDelay = HDLQualifiedName.create(dlyName);
				final HDLVariable dlyVar = var.setName(dlyName).setDefaultValue(null);

				// Insert dimension as first dimension. Rewrite all existing
				// references to the new delayed signal.
				final ArrayList<HDLExpression> dimensions = dlyVar.getDimensions();
				dimensions.add(0, delayPlusOne);
				ms.insertAfter(hvd, hvd.setDirection(HDLDirection.INTERNAL).setRegister(undelayedReg).setVariables(HDLObject.asList(dlyVar.setDimensions(dimensions))));
				final Collection<HDLVariableRef> varRefs = HDLQuery.getVarRefs(var.getContainer(HDLUnit.class), var);
				for (final HDLVariableRef ref : varRefs) {
					if (ref.getContainer().getClassType() == HDLClass.HDLAssignment) {
						final HDLAssignment ass = (HDLAssignment) ref.getContainer();
						if (ass.getLeft() == ref) {
							final ArrayList<HDLExpression> array = ref.getArray();
							array.add(0, delay);
							ms.replace(ass, ass.setLeft(ref.setVar(fqnDelay).setArray(array)));
						} else {
							final ArrayList<HDLExpression> array = ref.getArray();
							array.add(0, HDLLiteral.get(0));
							ms.replace(ref, ref.setVar(fqnDelay).setArray(array));
						}
					} else {
						final ArrayList<HDLExpression> array = ref.getArray();
						array.add(0, HDLLiteral.get(0));
						ms.replace(ref, ref.setVar(fqnDelay).setArray(array));
					}
				}
				final HDLExpression defaultValue = var.getDefaultValue();
				final HDLVariableRef dlyRef = dlyVar.asHDLRef();
				if (defaultValue != null) {
					final ArrayList<HDLExpression> array = dlyRef.getArray();
					array.add(0, delay);
					ms.insertAfter(hvd, new HDLAssignment().setLeft(dlyRef.setArray(array)).setRight(defaultValue));
				}
				final ArrayList<HDLExpression> arrayZero = dlyRef.getArray();
				arrayZero.add(0, HDLLiteral.get(0));
				ms.insertAfter(hvd, new HDLAssignment().setLeft(var.asHDLRef()).setRight(dlyRef.setArray(arrayZero)));
				final HDLVariableRef dim = loop.getParam().asHDLRef();
				final HDLArithOp dimMinusOne = new HDLArithOp().setLeft(dim).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
				final ArrayList<HDLExpression> arrayDimMinOne = dlyRef.getArray();
				arrayDimMinOne.add(0, dimMinusOne);
				final ArrayList<HDLExpression> arrayDim = dlyRef.getArray();
				arrayDim.add(0, dim);
				loop = loop.addDos(new HDLAssignment().setLeft(dlyRef.setArray(arrayDimMinOne)).setRight(dlyRef.setArray(arrayDim)));
				// Replace original hvd without registers
			}
			ms.replace(hvd, hvd.setRegister(null));
			if (delayValue.isPresent()) {
				ms.insertAfter(hvd, loop);
			} else {
				final HDLIfStatement hIf = new HDLIfStatement().setIfExp(new HDLEqualityOp().setLeft(delay).setType(HDLEqualityOpType.GREATER).setRight(HDLLiteral.get(0)))
						.addThenDo(loop);
				ms.insertAfter(hvd, hIf);
			}
		}
		return ms.apply(pkg);
	}

	public static <T extends IHDLObject> T resolveFragments(T pkg) {
		HDLUnresolvedFragment[] fragments;
		do {
			final ModificationSet ms = new ModificationSet();
			fragments = pkg.getAllObjectsOf(HDLUnresolvedFragment.class, true);
			for (final HDLUnresolvedFragment uFrag : fragments) {
				final IHDLObject container = uFrag.getContainer();
				if ((container instanceof HDLUnresolvedFragment)) {
					final HDLUnresolvedFragment fragment = (HDLUnresolvedFragment) container;
					if (fragment.getSub() == uFrag) {
						continue;
					}
				}
				final Optional<? extends IHDLObject> resolved = resolveFragment(uFrag);
				if (resolved.isPresent()) {
					ms.replace(uFrag, resolved.get());
				}
			}
			final T newPkg = ms.apply(pkg);
			if ((newPkg == pkg) && (fragments.length != 0))
				return pkg;
			pkg = newPkg;
		} while (fragments.length > 0);
		return pkg;
	}

	public static Optional<? extends IHDLObject> resolveFragment(HDLUnresolvedFragment uFrag) {
		// Single fragment (no function) resolves to variable -> Variable
		// Single fragment (function) resolved to function -> FunctionCall
		// Fragment (no bits, no function) resolves to variable,
		// -> fragment (no function) -> Interface Ref
		// -> fragment (function) -> FunctionCall (first fragment as arg)
		// Fragment (no bits, no function, no array) resolves to Enum
		// -> fragment (no bits, no function, no array) -> EnumRef
		// Fragment (no bits, no array, no function) does not resolve
		// -> append until we get a Type
		// -> resolves to enum ->
		HDLQualifiedName fqn = HDLQualifiedName.EMPTY;
		HDLUnresolvedFragment cFrag = uFrag;
		while (cFrag != null) {
			fqn = fqn.append(cFrag.getFrag());
			final Optional<? extends IHDLObject> attemptResolve = attemptResolve(cFrag, fqn);
			if (attemptResolve.isPresent()) {
				final Optional<HDLFunctionCall> methodChaining = tryMethodChaining(cFrag, attemptResolve);
				if (methodChaining.isPresent())
					return methodChaining;
				return attemptResolve;
			}
			cFrag = cFrag.getSub();
		}
		final IHDLObject container = uFrag.getContainer();
		if (container instanceof HDLSwitchCaseStatement) {
			final HDLSwitchCaseStatement caseStatement = (HDLSwitchCaseStatement) container;
			final IHDLObject caseContainer = caseStatement.getContainer();
			if (caseContainer instanceof HDLSwitchStatement) {
				final HDLSwitchStatement switchStatement = (HDLSwitchStatement) caseContainer;
				final Optional<? extends HDLType> switchType = TypeExtension.typeOf(switchStatement.getCaseExp());
				if (switchType.isPresent())
					return createFullEnum(uFrag, switchType.get());
			}
		}
		if (container instanceof HDLEqualityOp) {
			final HDLEqualityOp equalityOp = (HDLEqualityOp) container;
			if (equalityOp.getLeft() == uFrag) {
				final Optional<? extends HDLType> type = TypeExtension.typeOf(equalityOp.getRight());
				if (type.isPresent())
					return createFullEnum(uFrag, type.get());
			}
			if (equalityOp.getRight() == uFrag) {
				final Optional<? extends HDLType> type = TypeExtension.typeOf(equalityOp.getLeft());
				if (type.isPresent())
					return createFullEnum(uFrag, type.get());
			}
		}
		if (container instanceof HDLAssignment) {
			final HDLAssignment assignment = (HDLAssignment) container;
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(assignment.getLeft());
			if (typeOf.isPresent())
				return createFullEnum(uFrag, typeOf.get());
		}
		if (container instanceof HDLVariable) {
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(container);
			if (typeOf.isPresent())
				return createFullEnum(uFrag, typeOf.get());
		}
		return Optional.absent();
	}

	private static Optional<HDLFunctionCall> tryMethodChaining(HDLUnresolvedFragment cFrag, Optional<? extends IHDLObject> attemptResolve) {
		final HDLUnresolvedFragment sub = cFrag.getSub();
		if (sub instanceof HDLUnresolvedFragmentFunction) {
			final HDLUnresolvedFragmentFunction huf = (HDLUnresolvedFragmentFunction) sub;
			final HDLQualifiedName funcName = HDLQualifiedName.create(huf.getFrag());
			final Optional<HDLFunction> function = ScopingExtension.INST.resolveFunction(cFrag, funcName);
			if (function.isPresent()) {
				final ArrayList<HDLExpression> params = huf.getParams();
				params.add(0, (HDLExpression) attemptResolve.get());
				final HDLFunctionCall funcCall = new HDLFunctionCall().setName(funcName).setParams(params);
				final Optional<HDLFunctionCall> funcOp = Optional.of(funcCall);
				final Optional<HDLFunctionCall> methodChaining = tryMethodChaining(sub, funcOp);
				if (methodChaining.isPresent())
					return methodChaining;
				return funcOp;
			}
		}
		return Optional.absent();
	}

	protected static Optional<? extends IHDLObject> attemptResolve(HDLUnresolvedFragment uFrag, HDLQualifiedName hVar) {
		if (uFrag.getClassType() != HDLClass.HDLUnresolvedFragmentFunction) {
			final Optional<HDLVariable> variableRaw = ScopingExtension.INST.resolveVariable(uFrag, hVar);
			final HDLUnresolvedFragment sub = uFrag.getSub();
			if (variableRaw.isPresent()) {
				final HDLVariable variable = variableRaw.get();
				if (sub == null)
					return Optional.of(variable.asHDLRef().setArray(uFrag.getArray()).setBits(uFrag.getBits()));
				final Optional<? extends HDLType> type = TypeExtension.typeOf(variable);
				if ((type.isPresent()) && (type.get().getClassType() == HDLClass.HDLInterface)) {
					final HDLQualifiedName typeName = fullNameOf(type.get());
					final HDLInterfaceRef hir = new HDLInterfaceRef().setHIf(variable.asRef()).setIfArray(uFrag.getArray()).setVar(typeName.append(sub.getFrag()))
							.setArray(sub.getArray()).setBits(sub.getBits());
					return Optional.of(hir);
				}
				return Optional.of(variable.asHDLRef().setArray(uFrag.getArray()).setBits(uFrag.getBits()));
			}
			final Optional<HDLEnum> enumRaw = ScopingExtension.INST.resolveEnum(uFrag, hVar);
			if (enumRaw.isPresent())
				if (sub != null) {
					final HDLQualifiedName typeName = fullNameOf(enumRaw.get());
					final HDLEnumRef enumRef = new HDLEnumRef().setHEnum(typeName).setVar(typeName.append(sub.getFrag()));
					return Optional.of(enumRef);
				}
			final Optional<? extends HDLType> typeRaw = ScopingExtension.INST.resolveType(uFrag, hVar);
			if (typeRaw.isPresent())
				if (sub != null) {
					final HDLQualifiedName typeName = fullNameOf(typeRaw.get());
					if (typeRaw.get() instanceof HDLEnum) {
						final HDLEnumRef enumRef = new HDLEnumRef().setHEnum(typeName).setVar(typeName.append(sub.getFrag()));
						return Optional.of(enumRef);
					}
				}
		} else {
			final HDLUnresolvedFragmentFunction uff = (HDLUnresolvedFragmentFunction) uFrag;
			final HDLFunctionCall call = new HDLFunctionCall().setName(hVar).setParams(uff.getParams());
			return Optional.of(call);
		}
		return Optional.absent();
	}

	/**
	 * Attempt to create an enum if the type provided is an actual enum
	 * 
	 * @param uFrag
	 *            the fragment
	 * @param type
	 *            the type
	 * @return may return null if not successful
	 */
	private static Optional<HDLEnumRef> createFullEnum(HDLUnresolvedFragment uFrag, HDLType type) {
		if (type instanceof HDLEnum) {
			final HDLEnum enumType = (HDLEnum) type;
			final Optional<HDLVariable> variable = ScopingExtension.getVariable(enumType, uFrag.getFrag());
			if (variable.isPresent()) {
				final HDLQualifiedName typeName = fullNameOf(enumType);
				final HDLEnumRef enumRef = new HDLEnumRef().setHEnum(typeName).setVar(typeName.append(uFrag.getFrag()));
				return Optional.of(enumRef);
			}
		}
		return Optional.absent();
	}

	private static <T extends IHDLObject> T pushSignIntoLiteral(T pkg) {
		final HDLLiteral[] literals = pkg.getAllObjectsOf(HDLLiteral.class, true);
		final ModificationSet ms = new ModificationSet();
		for (final HDLLiteral hdlLiteral : literals)
			if (hdlLiteral.getContainer() instanceof HDLManip) {
				final HDLManip manip = (HDLManip) hdlLiteral.getContainer();
				if (manip.getType() == HDLManipType.ARITH_NEG)
					if (hdlLiteral.getVal().charAt(0) == '-') {
						ms.replace(manip, hdlLiteral.setVal(hdlLiteral.getVal().substring(1)));
					} else {
						ms.replace(manip, hdlLiteral.setVal("-" + hdlLiteral.getVal()));
					}
			}
		return ms.apply(pkg);
	}

	/**
	 * Finds cases where a either a ARITH_NEG contains another ARITH_NEG, or
	 * where an ARITH_NEG contains a negative literal
	 * 
	 */
	public static <T extends IHDLObject> T fixDoubleNegate(T pkg) {
		final ModificationSet ms = new ModificationSet();
		final Collection<HDLManip> manips = HDLQuery.select(HDLManip.class).from(pkg) //
				.where(HDLManip.fType).isEqualTo(HDLManipType.ARITH_NEG).getAll();
		for (final HDLManip manip : manips) {
			final HDLExpression target = manip.getTarget();
			if (target instanceof HDLManip) {
				final HDLManip innerManip = (HDLManip) target;
				if (innerManip.getType() == HDLManipType.ARITH_NEG) {
					ms.replace(manip, innerManip.getTarget());
				}
			}
			if (target instanceof HDLLiteral) {
				final HDLLiteral lit = (HDLLiteral) target;
				final BigInteger valueAsBigInt = lit.getValueAsBigInt();
				if (valueAsBigInt.signum() <= 0) {
					ms.replace(manip, HDLLiteral.get(valueAsBigInt.negate()));
				}
			}
		}
		return ms.apply(pkg);
	}

	public static <T extends IHDLObject> T inlineFunctions(T pkg) {
		final ModificationSet ms = new ModificationSet();
		boolean doRepeat = false;
		do {
			doRepeat = false;
			final HDLFunctionCall[] functions = pkg.getAllObjectsOf(HDLFunctionCall.class, true);
			outer: for (final HDLFunctionCall hdi : functions) {
				final Optional<HDLFunction> function = hdi.resolveName();
				if (function.isPresent()) {
					final HDLFunctionCall[] subFunctions = hdi.getAllObjectsOf(HDLFunctionCall.class, true);
					for (final HDLFunctionCall sub : subFunctions) {
						if (sub != hdi) {
							final Optional<HDLFunction> func = sub.resolveName();
							if (func.isPresent() && (func.get().getClassType() == HDLClass.HDLInlineFunction)) {
								doRepeat = true;
								continue outer;
							}
						}
					}
					switch (function.get().getClassType()) {
					case HDLInlineFunction:
						final HDLInlineFunction hif = (HDLInlineFunction) function.get();
						final HDLExpression equivalenExpression = hif.getReplacementExpression(hdi);
						ms.replace(hdi, equivalenExpression);
						break;
					case HDLSubstituteFunction:
						final HDLSubstituteFunction hsf = (HDLSubstituteFunction) function.get();
						final HDLStatement[] statements = hsf.getReplacementStatements(hdi);
						ms.replace(hdi, statements);
						break;
					default:
					}
				}
			}
			pkg = ms.apply(pkg);
		} while (doRepeat);
		return pkg;
	}

	public static <T extends IHDLObject> T setParameterOnInstance(T apply) {
		final ModificationSet ms = new ModificationSet();
		final HDLInterfaceInstantiation[] hiis = apply.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		final Map<String, HDLExpression> argMap = new HashMap<String, HDLExpression>();
		for (final HDLInterfaceInstantiation hdi : hiis) {
			final ArrayList<HDLArgument> arguments = hdi.getArguments();
			for (final HDLArgument hdlArgument : arguments) {
				argMap.put(hdlArgument.getName(), hdlArgument.getExpression());
			}
			final Optional<HDLInterface> hIf = hdi.resolveHIf();
			if (!hIf.isPresent()) {
				continue;
			}
			for (final HDLVariableDeclaration hvd : hIf.get().getPorts())
				if (hvd.getDirection() == HDLDirection.PARAMETER) {
					HDLVariableDeclaration newHVD = new HDLVariableDeclaration().setType(hvd.resolveType().get()).setDirection(HDLDirection.CONSTANT);
					for (HDLVariable var : hvd.getVariables())
						if (!ScopingExtension.INST.resolveVariable(hdi, HDLQualifiedName.create(var.getName())).isPresent()) {
							String argName = var.getMeta(HDLInterfaceInstantiation.ORIG_NAME);
							if (argName == null) {
								argName = var.getName();
							}
							if (argMap.get(argName) != null) {
								var = var.setDefaultValue(argMap.get(argName));
							}
							newHVD = newHVD.addVariables(var);
						}
					if (!newHVD.getVariables().isEmpty()) {
						ms.insertBefore(hdi, newHVD);
					}
				}
		}
		return ms.apply(apply);
	}

	/**
	 * Checks for HDLDirectGenerations and calls the generators. If they are
	 * includes, it will be included and the references resolved
	 * 
	 */
	private static <T extends IHDLObject> T includeGenerators(T apply, String src) {
		final ModificationSet ms = new ModificationSet();
		final HDLDirectGeneration[] gens = apply.getAllObjectsOf(HDLDirectGeneration.class, true);
		for (final HDLDirectGeneration generation : gens) {
			final Optional<HDLGenerationInfo> optional = HDLGenerators.getImplementation(generation);
			if (optional.isPresent()) {
				final HDLGenerationInfo generationInfo = optional.get();
				if (generation.getInclude()) {
					final HDLQualifiedName ifRef = generation.getHIf().asRef();
					final HDLQualifiedName fullName = fullNameOf(generation);
					final HDLStatement[] stmnts = generationInfo.unit.getStatements().toArray(new HDLStatement[0]);
					final HDLStatement[] inits = generationInfo.unit.getInits().toArray(new HDLStatement[0]);
					final ArrayList<HDLStatement> allStmnt = new ArrayList<HDLStatement>();
					allStmnt.addAll(Arrays.asList(stmnts));
					allStmnt.addAll(Arrays.asList(inits));
					ms.replace(generation, allStmnt.toArray(new HDLStatement[allStmnt.size()]));
					final HDLInterfaceRef[] ifRefs = apply.getAllObjectsOf(HDLInterfaceRef.class, true);
					for (final HDLInterfaceRef hdI : ifRefs)
						if (TypeExtension.typeOf(hdI.resolveHIf().get()).get().asRef().equals(ifRef)) {
							final HDLQualifiedName newName = fullName.append(hdI.getVarRefName().getLastSegment());
							ms.replace(hdI, new HDLVariableRef().setVar(newName).setArray(hdI.getArray()).setBits(hdI.getBits()));
						}
				}
				String libURI = null;
				switch (apply.getClassType()) {
				case HDLPackage:
					libURI = ((HDLPackage) apply).getLibURI();
					break;
				case HDLUnit:
					libURI = ((HDLUnit) apply).getLibURI();
					break;
				default:
					libURI = apply.getContainer(HDLUnit.class).getLibURI();
					break;
				}
				final HDLLibrary library = HDLLibrary.getLibrary(libURI);
				library.addSideFiles(generationInfo.files, src);
			}
		}
		return ms.apply(apply);
	}

	private static EnumSet<HDLDirection> doNotInit = EnumSet.of(HDLDirection.HIDDEN, HDLDirection.CONSTANT, HDLDirection.PARAMETER, HDLDirection.IN);

	/**
	 * Generate the default initialization code for Arrays, Enums and Non
	 * registers
	 */
	private static <T extends IHDLObject> T generateInitializations(T apply) {
		final ModificationSet ms = new ModificationSet();
		final HDLVariableDeclaration[] allVarDecls = apply.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : allVarDecls)
			if (!doNotInit.contains(hvd.getDirection())) {
				final HDLRegisterConfig register = hvd.getRegister();
				for (final HDLVariable var : hvd.getVariables()) {
					final Set<String> meta = var.getMeta(Init.full);
					if ((meta != null) && (hvd.getRegister() == null) && (var.getDimensions().size() == 0)) {
						continue;
					}
					final HDLExpression defaultValue = getDefaultValue(hvd, register, var);
					if ((defaultValue != null) && (hvd.getContainer(HDLInterface.class) == null)) {
						final HDLVariableRef setVar = new HDLVariableRef().setVar(var.asRef());
						generateInit(Collections.<HDLExpression> emptyList(), var.getDimensions(), ms, var, var, defaultValue, setVar);
					}

				}
			}
		// Initialize all Interface instantiations by assigning the default
		// value to each Port
		final HDLInterfaceInstantiation[] hii = apply.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (final HDLInterfaceInstantiation hi : hii) {
			final HDLUnit unit = hi.getContainer(HDLUnit.class);
			if (unit.getSimulation()) {
				continue;
			}
			Set<String> meta = hi.getVar().getMeta(Init.full);
			final ArrayList<HDLExpression> ifDim = hi.getVar().getDimensions();
			// Either no port is fully initialized, or we do have multiple
			// dimensions.
			// In the case that we do have multiple dimensions we ensure that
			// each dimensions is initialized. Because it might be possible that
			// we don't write each dimension
			if ((meta == null) || !ifDim.isEmpty()) {
				meta = new HashSet<String>();
			}
			final Optional<HDLInterface> hIf = hi.resolveHIf();
			if (!hIf.isPresent()) {
				continue;
			}
			for (final HDLVariableDeclaration hvd : hIf.get().getPorts()) {
				final HDLDirection direction = hvd.getDirection();
				// Only ports of direction IN need to be initialized
				if ((direction == HDLDirection.IN) || (direction == HDLDirection.INOUT)) {
					for (final HDLVariable var : hvd.getVariables()) {
						// If this port has a full assignment, we don't need to
						// initialize it.
						if (meta.contains(var.getName())) {
							continue;
						}
						final ArrayList<HDLExpression> varDim = var.getDimensions();
						final HDLExpression defaultValue = getDefaultValue(hvd, null, var);
						if (defaultValue != null) {
							final HDLVariableRef setVar = new HDLInterfaceRef().setHIf(hi.getVar().asRef()).setVar(var.asRef());
							generateInit(ifDim, varDim, ms, var, hi.getContainer(HDLUnit.class), defaultValue, setVar);
						}
					}
				}
			}
		}
		return ms.apply(apply);
	}

	private static void generateInit(List<HDLExpression> ifDim, List<HDLExpression> varDim, ModificationSet ms, HDLVariable var, IHDLObject container, HDLExpression defaultValue,
			HDLVariableRef setVar) {
		boolean synchedArray = false;
		if (defaultValue instanceof HDLVariableRef) {
			final HDLVariableRef ref = (HDLVariableRef) defaultValue;
			synchedArray = ref.resolveVar().get().getDimensions().size() != 0;
		}
		HDLStatement init;
		if (defaultValue instanceof HDLArrayInit) {
			final HDLArrayInit hai = (HDLArrayInit) defaultValue;
			init = new HDLAssignment().setLeft(setVar).setRight(hai);
		} else {
			init = createArrayForLoop(ifDim, varDim, 0, defaultValue, setVar, synchedArray);
		}
		final HDLBlock obj = var.getMeta(RWValidation.BLOCK_META);
		if ((obj != null) && (obj != RWValidation.UNIT_BLOCK)) {
			insertFirstStatement(ms, obj, init);
		} else {
			insertFirstStatement(ms, container, init);
		}
		if (!(setVar instanceof HDLInterfaceRef)) {
			ms.replace(var, var.setDefaultValue(null));
		}
	}

	/**
	 * Returns the defaultvalue for the given Variable, taken the Type into
	 * consideration. For Enums the first enum is returned.
	 */
	private static HDLExpression getDefaultValue(HDLVariableDeclaration hvd, HDLRegisterConfig register, HDLVariable var) {
		final HDLExpression defaultValue = var.getDefaultValue();
		if ((defaultValue == null) && (register == null))
			if ((hvd.getPrimitive() != null)) {
				if (var.getAnnotation(HDLBuiltInAnnotations.VHDLLatchable) == null) {
					switch (hvd.getPrimitive().getType()) {
					case STRING:
						return HDLLiteral.getString("");
					case BOOL:
						return HDLLiteral.getFalse();
					case BIT:
					case BITVECTOR:
					case INT:
					case INTEGER:
					case NATURAL:
					case UINT:
						return HDLLiteral.get(0);
					}
				}
			} else {
				final HDLType resolveType = hvd.resolveType().get();
				if (resolveType instanceof HDLEnum) {
					final HDLEnum hEnum = (HDLEnum) resolveType;
					return new HDLVariableRef().setVar(hEnum.getEnums().get(0).asRef());
				}
			}
		return defaultValue;
	}

	/**
	 * Generate the code to initialize an array with the given default value.
	 * 
	 * @param varDim
	 *            all dimensions that need to be filled
	 * @param i
	 *            the current depth
	 * @param defaultValue
	 *            the defaultValue to assign to each dimension
	 * @param ref
	 *            the reference which will be used for writing
	 * @param synchedArray
	 *            if <code>true</code> the defaultValue will become the same
	 *            dimension accesses as the variable
	 * @param interfaceDim
	 *            if <code>true</code> the ref parameter will be treated as
	 *            HDLIinterfaceRef and gets the interface array filled
	 * @return
	 */
	public static HDLStatement createArrayForLoop(List<HDLExpression> ifDim, List<HDLExpression> varDim, int i, HDLExpression defaultValue, HDLVariableRef ref, boolean synchedArray) {
		final boolean interfaceDim = i < ifDim.size();
		if (i == (varDim.size() + ifDim.size()))
			return new HDLAssignment().setLeft(ref).setRight(defaultValue);
		final HDLExpression dim = interfaceDim ? ifDim.get(i) : varDim.get(i);
		final HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(new HDLArithOp().setLeft(dim).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1)));
		final HDLVariable param = new HDLVariable().setName(getTempName("init", null));
		final HDLForLoop loop = new HDLForLoop().setRange(HDLObject.asList(range)).setParam(param);
		final HDLVariableRef paramRef = new HDLVariableRef().setVar(param.asRef());
		if (synchedArray) {
			final HDLVariableRef defRef = (HDLVariableRef) defaultValue;
			if (interfaceDim) {
				defaultValue = ((HDLInterfaceRef) defRef).addIfArray(paramRef);
			} else {
				defaultValue = defRef.addArray(paramRef);
			}
		}
		HDLVariableRef subRef;
		if (interfaceDim) {
			subRef = ((HDLInterfaceRef) ref).addIfArray(paramRef);
		} else {
			subRef = ref.addArray(paramRef);
		}
		return loop.addDos(createArrayForLoop(ifDim, varDim, i + 1, defaultValue, subRef, synchedArray));
	}

	private static void insertFirstStatement(ModificationSet ms, IHDLObject container, HDLStatement stmnt) {
		if (container == null)
			// This can happen if the container is HDLPackage for example for
			// global constants
			return;
		if ((container.getClassType() != HDLClass.HDLUnit) && (container.getClassType() != HDLClass.HDLBlock)) {
			insertFirstStatement(ms, container.getContainer(), stmnt);
			return;
		}
		if (container instanceof HDLInterface)
			return;
		if (container instanceof HDLUnit) {
			final HDLUnit unit = (HDLUnit) container;
			ms.addTo(unit, HDLUnit.fInits, stmnt);
		}
		if (container instanceof HDLBlock) {
			final HDLBlock block = (HDLBlock) container;
			final HDLStatement statement = block.getStatements().get(0);
			ms.insertBefore(statement, stmnt);
		}
	}

	/**
	 * Checks for the clock and reset annotation and replaces all $clk and $rst
	 * references with it. It also inserts the clk and rst signals into the
	 * HDLUnit if needed.
	 * 
	 * @param unit
	 * @return
	 */
	public static <T extends IHDLObject> T generateClkAndReset(T apply) {
		final ModificationSet ms = new ModificationSet();
		final HDLUnit[] units = apply.getAllObjectsOf(HDLUnit.class, true);
		for (final HDLUnit unit : units) {
			HDLVariable defClkVar = HDLRegisterConfig.defaultClk(false).addAnnotations(HDLBuiltInAnnotations.clock.create(null));
			HDLVariable defRstVar = HDLRegisterConfig.defaultRst(false).addAnnotations(HDLBuiltInAnnotations.reset.create(null));
			boolean customClk = false, customRst = false;
			// Find all clock annotated Signals
			HDLVariable newVar = extractVar(unit, HDLBuiltInAnnotations.clock);
			if (newVar != null) {
				defClkVar = newVar;
				customClk = true;
			}
			newVar = extractVar(unit, HDLBuiltInAnnotations.reset);
			if (newVar != null) {
				defRstVar = newVar;
				customRst = true;
			}
			boolean hasRegister = false;
			// Replace all $clk and $rst with the clock in HDLRegisters
			final Collection<HDLRegisterConfig> clkRefs = HDLQuery.select(HDLRegisterConfig.class).from(unit).where(HDLRegisterConfig.fClk)
					.lastSegmentIs(HDLRegisterConfig.DEF_CLK).getAll();
			for (final HDLRegisterConfig reg : clkRefs) {
				hasRegister = true;
				ms.replace(reg, reg.setClk(HDLQualifiedName.create(defClkVar.getName())));
				if (!customClk) {
					insertSig(ms, reg.getContainer(), defClkVar, SignalInserted.ClkInserted);
				}
			}
			final Collection<HDLRegisterConfig> rstRefs = HDLQuery.select(HDLRegisterConfig.class).from(unit).where(HDLRegisterConfig.fRst)
					.lastSegmentIs(HDLRegisterConfig.DEF_RST).getAll();
			for (HDLRegisterConfig reg : rstRefs) {
				hasRegister = true;
				final HDLRegisterConfig orig = reg;
				reg = ms.getReplacement(reg);
				ms.replacePrune(orig, reg.setRst(HDLQualifiedName.create(defRstVar.getName())));
				if (!customRst) {
					insertSig(ms, orig.getContainer(), defRstVar, SignalInserted.RstInserted);
				}
			}

			boolean hasClkRef = false, hasRstRef = false;
			// Replace all $clk, $rst, $ena VariableRefs
			final Collection<HDLVariableRef> clkVarRefs = HDLQuery.select(HDLVariableRef.class).from(unit).where(HDLResolvedRef.fVar).lastSegmentIs(HDLRegisterConfig.DEF_CLK)
					.getAll();
			for (final HDLVariableRef clkRef : clkVarRefs) {
				ms.replace(clkRef, defClkVar.asHDLRef());
				hasClkRef = true;
			}
			final Collection<HDLVariableRef> rstVarRefs = HDLQuery.select(HDLVariableRef.class).from(unit).where(HDLResolvedRef.fVar).lastSegmentIs(HDLRegisterConfig.DEF_RST)
					.getAll();
			for (final HDLVariableRef rstRef : rstVarRefs) {
				ms.replace(rstRef, defRstVar.asHDLRef());
				hasRstRef = true;
			}
			if (hasClkRef && !hasRegister) {
				insertSig(ms, unit, defClkVar, SignalInserted.ClkInserted);
			}
			if (hasRstRef && !hasRegister) {
				insertSig(ms, unit, defRstVar, SignalInserted.RstInserted);
			}
		}
		return ms.apply(apply);
	}

	private static HDLVariable extractVar(HDLObject apply, HDLBuiltInAnnotations annotation) {
		final HDLAnnotation clock = HDLQuery.select(HDLAnnotation.class).from(apply).where(HDLAnnotation.fName).isEqualTo(annotation.toString()).getFirst();
		if (clock != null) {
			if (clock.getContainer() instanceof HDLVariableDeclaration) {
				final HDLVariableDeclaration hvd = (HDLVariableDeclaration) clock.getContainer();
				return hvd.getVariables().get(0);
			}
			if (clock.getContainer() instanceof HDLVariable)
				return (HDLVariable) clock.getContainer();
		}
		return null;
	}

	public static enum SignalInserted implements MetaAccess<Boolean> {
		ClkInserted, RstInserted;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	private static void insertSig(ModificationSet ms, IHDLObject container, HDLVariable defVar, SignalInserted signalInserted) {
		if (container.getClassType() != HDLClass.HDLUnit) {
			insertSig(ms, container.getContainer(), defVar, signalInserted);
			return;
		}
		final boolean hasMeta = container.hasMeta(signalInserted);
		if (!hasMeta) {
			final HDLUnit unit = (HDLUnit) container;
			container.setMeta(signalInserted);
			if (HDLQuery.select(HDLVariable.class).from(unit).where(HDLVariable.fName).isEqualTo(defVar.getName()).getFirst() == null) {
				final HDLStatement statement = unit.getStatements().get(0);
				final HDLPrimitive bit = HDLPrimitive.getBit();
				ms.insertBefore(statement, new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(bit).addVariables(defVar));
			}
		}
	}

	private static <T extends IHDLObject> T fortifyType(T apply) {
		final ModificationSet ms = new ModificationSet();
		fortifyOpExpressions(apply, ms);
		fortifyAssignments(apply, ms);
		fortifyIfExpressions(apply, ms);
		fortifyRanges(apply, ms);
		fortifyWidthExpressions(apply, ms);
		fortifyDefaultAndResetExpressions(apply, ms);
		foritfyArrays(apply, ms);
		foritfyFunctions(apply, ms);
		foritfyConcats(apply, ms);
		fortifyTernaryOp(apply, ms);
		rewriteBoolCasts(apply, ms);
		return ms.apply(apply);
	}

	private static void rewriteBoolCasts(IHDLObject apply, ModificationSet ms) {
		final Collection<HDLManip> casts = HDLQuery.select(HDLManip.class).from(apply).where(HDLManip.fType).isEqualTo(HDLManipType.CAST).getAll();
		for (final HDLManip manip : casts) {
			final HDLExpression target = manip.getTarget();
			final Optional<? extends HDLType> tt = TypeExtension.typeOf(target);
			if (tt.isPresent()) {
				if ((tt.get().getType() == HDLPrimitiveType.BOOL) && (manip.getCastTo().getType() != HDLPrimitiveType.BOOL)) {
					final HDLTernary tern = new HDLTernary().setIfExpr(manip.getTarget()).setThenExpr(HDLLiteral.get(1)).setElseExpr(HDLLiteral.get(0));
					ms.replace(manip, manip.setTarget(tern));
				}
			}
		}

	}

	private static void fortifyTernaryOp(IHDLObject apply, ModificationSet ms) {
		final HDLTernary[] ternaries = apply.getAllObjectsOf(HDLTernary.class, true);
		for (final HDLTernary hdlTernary : ternaries) {
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(hdlTernary.getThenExpr());
			if (typeOf.isPresent()) {
				final HDLType type = typeOf.get();
				if (type instanceof HDLPrimitive) {
					final HDLPrimitive pt = (HDLPrimitive) type;
					fortify(ms, hdlTernary.getElseExpr(), pt);
				}
			}
		}
	}

	private static void foritfyConcats(IHDLObject apply, ModificationSet ms) {
		final HDLConcat cats[] = apply.getAllObjectsOf(HDLConcat.class, true);
		for (final HDLConcat hdlConcat : cats) {
			final ArrayList<HDLExpression> catExp = hdlConcat.getCats();
			for (final HDLExpression hdlExpression : catExp) {
				final HDLPrimitive typeOf = (HDLPrimitive) TypeExtension.typeOf(hdlExpression).get();
				switch (typeOf.getType()) {
				case BIT:
				case BITVECTOR:
					continue;
				case INT:
				case UINT:
					cast(ms, typeOf.setType(HDLPrimitiveType.BITVECTOR), hdlExpression);
					break;
				default:
					throw new IllegalArgumentException("Type:" + typeOf + " not supported in concatenations");
				}
			}
		}

	}

	private static void foritfyFunctions(IHDLObject apply, ModificationSet ms) {
		final HDLFunctionCall[] functions = apply.getAllObjectsOf(HDLFunctionCall.class, true);
		for (final HDLFunctionCall function : functions) {
			final HDLTypeInferenceInfo info = HDLFunctions.getInferenceInfo(function);
			if (info != null) {
				final ArrayList<HDLExpression> params = function.getParams();
				for (int j = 0; j < params.size(); j++) {
					final HDLExpression exp = params.get(j);
					fortify(ms, exp, info.args[j]);
				}
			}
		}
	}

	private static void foritfyArrays(IHDLObject apply, ModificationSet ms) {
		final HDLVariableRef[] varRefs = apply.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : varRefs) {
			for (final HDLExpression exp : ref.getArray()) {
				fortify(ms, exp, HDLPrimitive.getNatural());
			}
			if (ref.getClassType() == HDLClass.HDLInterfaceRef) {
				final HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				for (final HDLExpression exp : hir.getIfArray()) {
					fortify(ms, exp, HDLPrimitive.getNatural());
				}
			}
		}
	}

	private static void fortifyWidthExpressions(IHDLObject apply, ModificationSet ms) {
		final HDLPrimitive[] primitives = apply.getAllObjectsOf(HDLPrimitive.class, true);
		for (final HDLPrimitive hdlPrimitive : primitives) {
			final HDLExpression width = hdlPrimitive.getWidth();
			if (width != null) {
				fortify(ms, width, HDLPrimitive.getNatural());
			}
		}
	}

	private static void fortifyDefaultAndResetExpressions(IHDLObject apply, ModificationSet ms) {
		final HDLVariableDeclaration[] primitives = apply.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : primitives) {
			final HDLRegisterConfig reg = hvd.getRegister();
			final HDLType determineType = TypeExtension.typeOf(hvd).get();
			if (reg != null) {
				final HDLExpression resetValue = reg.getResetValue();
				if (!(resetValue instanceof HDLArrayInit)) {
					if (resetValue != null) {
						fortify(ms, resetValue, determineType);
					}
				} else {
					final HDLArrayInit hai = (HDLArrayInit) resetValue;
					fortifyArrayInitExp(ms, determineType, hai);
				}
			}
			for (final HDLVariable var : hvd.getVariables()) {
				final HDLExpression defaultValue = var.getDefaultValue();
				if (!(defaultValue instanceof HDLArrayInit)) {
					if (defaultValue != null) {
						fortify(ms, defaultValue, determineType);
					}
				} else {
					final HDLArrayInit hai = (HDLArrayInit) defaultValue;
					fortifyArrayInitExp(ms, determineType, hai);
				}
			}
		}
	}

	private static void fortifyArrayInitExp(ModificationSet ms, HDLType determineType, HDLArrayInit hai) {
		for (final HDLExpression exp : hai.getExp()) {
			if (exp instanceof HDLArrayInit) {
				final HDLArrayInit subHai = (HDLArrayInit) exp;
				fortifyArrayInitExp(ms, determineType, subHai);
			} else {
				fortify(ms, exp, determineType);
			}
		}
	}

	private static void fortifyRanges(IHDLObject apply, ModificationSet ms) {
		final HDLRange[] ranges = apply.getAllObjectsOf(HDLRange.class, true);
		for (final HDLRange range : ranges) {
			if (BuiltInValidator.skipExp(range)) {
				continue;
			}
			final HDLExpression exp = range.getFrom();
			if (exp != null) {
				fortify(ms, exp, HDLPrimitive.getNatural());
			}
			fortify(ms, range.getTo(), HDLPrimitive.getNatural());
		}
	}

	private static void fortifyIfExpressions(IHDLObject apply, ModificationSet ms) {
		final HDLIfStatement[] ifs = apply.getAllObjectsOf(HDLIfStatement.class, true);
		for (final HDLIfStatement assignment : ifs) {
			fortify(ms, assignment.getIfExp(), HDLPrimitive.getBool());
		}
		final HDLTernary[] ternaries = apply.getAllObjectsOf(HDLTernary.class, true);
		for (final HDLTernary hdlTernary : ternaries) {
			fortify(ms, hdlTernary.getIfExpr(), HDLPrimitive.getBool());
		}
	}

	/**
	 * Ensures that the right hand-side can be assigned to the left hand-side.
	 * This includes converting booleans to ternary operators.
	 */
	private static void fortifyAssignments(IHDLObject apply, ModificationSet ms) {
		final HDLAssignment[] assignments = apply.getAllObjectsOf(HDLAssignment.class, true);
		for (final HDLAssignment assignment : assignments) {
			if (BuiltInValidator.skipExp(assignment)) {
				continue;
			}
			final Optional<? extends HDLType> opLeft = TypeExtension.typeOf(assignment.getLeft());
			final HDLType leftType = opLeft.get();
			final HDLExpression exp = assignment.getRight();
			final HDLType rightType = TypeExtension.typeOf(exp).get();
			if (exp.getClassType() == HDLClass.HDLArrayInit) {
				fortifyArrayInitExp(ms, leftType, (HDLArrayInit) exp);
			} else if ((rightType.getType() == HDLPrimitiveType.BOOL) && (leftType.getType() != HDLPrimitiveType.BOOL)) {
				final HDLTernary newIf = new HDLTernary().setIfExpr(exp).setThenExpr(HDLLiteral.get(1)).setElseExpr(HDLLiteral.get(0));
				ms.replace(assignment, assignment.setRight(new HDLManip().setType(HDLManipType.CAST).setCastTo(leftType).setTarget(newIf)));
			} else if (leftType instanceof HDLPrimitive) {
				final HDLPrimitive pt = (HDLPrimitive) leftType;
				fortify(ms, exp, pt);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private static void fortifyOpExpressions(IHDLObject apply, ModificationSet ms) {
		final HDLExpression[] opEx = apply.getAllObjectsOf(HDLExpression.class, true);
		for (final HDLExpression opExpression : opEx) {
			if (BuiltInValidator.skipExp(opExpression)) {
				continue;
			}
			HDLTypeInferenceInfo inferenceInfo = null;
			HDLExpression left = null;
			HDLExpression right = null;
			switch (opExpression.getClassType()) {
			case HDLArithOp:
				final HDLArithOp aop = (HDLArithOp) opExpression;
				left = aop.getLeft();
				right = aop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getArithOpType(aop);
				break;
			case HDLShiftOp:
				final HDLShiftOp sop = (HDLShiftOp) opExpression;
				left = sop.getLeft();
				right = sop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getShiftOpType(sop);
				break;
			case HDLBitOp:
				final HDLBitOp bop = (HDLBitOp) opExpression;
				left = bop.getLeft();
				right = bop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getBitOpType(bop);
				break;
			case HDLEqualityOp:
				final HDLEqualityOp eop = (HDLEqualityOp) opExpression;
				left = eop.getLeft();
				right = eop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getEqualityOpType(eop);
				break;
			case HDLManip:
				final HDLManip manip = (HDLManip) opExpression;
				left = manip.getTarget();
				inferenceInfo = HDLPrimitives.getInstance().getManipOpType(manip);
			}
			if (inferenceInfo != null) {
				if (inferenceInfo.error != null)
					throw new IllegalArgumentException("The expression " + opExpression + " has an error:" + inferenceInfo.error);
				fortify(ms, left, inferenceInfo.args[0]);
				if (right != null) {
					fortify(ms, right, inferenceInfo.args[1]);
				}
			}
		}
	}

	private static void cast(ModificationSet ms, HDLPrimitive targetType, HDLExpression exp) {
		ms.replace(exp, new HDLManip().setType(HDLManipType.CAST).setCastTo(targetType).setTarget(exp));
	}

	private static void makeBool(ModificationSet ms, HDLExpression exp) {
		final HDLManip zero = new HDLManip().setType(HDLManipType.CAST).setCastTo(TypeExtension.typeOf(exp).get()).setTarget(HDLLiteral.get(0));
		ms.replace(exp, new HDLEqualityOp().setLeft(exp).setType(HDLEqualityOpType.NOT_EQ).setRight(zero));
	}

	private static void fortify(ModificationSet ms, HDLExpression exp, HDLType targetType) {
		if (targetType instanceof HDLPrimitive) {
			final HDLPrimitive pt = (HDLPrimitive) targetType;
			if (BuiltInValidator.skipExp(exp))
				return;
			final Optional<? extends HDLType> lt = TypeExtension.typeOf(exp);
			if (lt.isPresent() && !targetType.equals(lt.get()))
				if (pt.getType() == HDLPrimitiveType.BOOL) {
					makeBool(ms, exp);
				} else {
					cast(ms, pt, exp);
				}
		}
	}

	private static <T extends IHDLObject> T handlePostfixOp(T apply) {
		final ModificationSet ms = new ModificationSet();
		final HDLAssignment[] loops = apply.getAllObjectsOf(HDLAssignment.class, true);
		for (final HDLAssignment hdlAssignment : loops) {
			final HDLOpExpression op = toOpExpression(hdlAssignment);
			if (op == null) {
				continue;
			}
			final HDLAssignment newAss = new HDLAssignment().setLeft(hdlAssignment.getLeft()).setType(HDLAssignmentType.ASSGN).setRight(op);
			ms.replace(hdlAssignment, newAss);
		}
		return ms.apply(apply);
	}

	public static HDLOpExpression toOpExpression(HDLAssignment hdlAssignment) {
		final HDLAssignmentType type = hdlAssignment.getType();
		HDLOpExpression op = null;
		switch (type) {
		case DIV_ASSGN:
			op = new HDLArithOp().setType(HDLArithOpType.DIV);
			break;
		case MOD_ASSGN:
			op = new HDLArithOp().setType(HDLArithOpType.MOD);
			break;
		case MUL_ASSGN:
			op = new HDLArithOp().setType(HDLArithOpType.MUL);
			break;
		case ADD_ASSGN:
			op = new HDLArithOp().setType(HDLArithOpType.PLUS);
			break;
		case SUB_ASSGN:
			op = new HDLArithOp().setType(HDLArithOpType.MINUS);
			break;
		case AND_ASSGN:
			op = new HDLBitOp().setType(HDLBitOpType.AND);
			break;
		case OR_ASSGN:
			op = new HDLBitOp().setType(HDLBitOpType.OR);
			break;
		case XOR_ASSGN:
			op = new HDLBitOp().setType(HDLBitOpType.XOR);
			break;
		case SLL_ASSGN:
			op = new HDLShiftOp().setType(HDLShiftOpType.SLL);
			break;
		case SRA_ASSGN:
			op = new HDLShiftOp().setType(HDLShiftOpType.SRA);
			break;
		case SRL_ASSGN:
			op = new HDLShiftOp().setType(HDLShiftOpType.SRL);
			break;
		case ASSGN:
			return op;
		}
		if (op == null)
			return null;
		return op.setLeft(hdlAssignment.getLeft()).setRight(hdlAssignment.getRight());
	}

	private static <T extends IHDLObject> T handleMultiForLoop(T apply) {
		final ModificationSet ms = new ModificationSet();
		final HDLForLoop[] loops = apply.getAllObjectsOf(HDLForLoop.class, true);
		for (final HDLForLoop loop : loops)
			if (loop.getRange().size() > 1) {
				final HDLForLoop[] newLoops = new HDLForLoop[loop.getRange().size()];
				int i = 0;
				for (final HDLRange r : loop.getRange()) {
					newLoops[i++] = loop.setRange(HDLObject.asList(r));
				}
				ms.replace(loop, newLoops);
			}
		return ms.apply(apply);
	}

	private static AtomicInteger objectID = new AtomicInteger();

	public static void resetID() {
		objectID.set(0);
	}

	public static String getTempName(String prefix, String suffix) {
		if ((prefix == null) && (suffix == null))
			return "$tmp_" + objectID.incrementAndGet();
		if (prefix == null)
			return "$tmp_" + objectID.incrementAndGet() + "_" + suffix;
		if (suffix == null)
			return "$tmp_" + prefix + "_" + objectID.incrementAndGet();
		return "$tmp_" + prefix + "_" + objectID.incrementAndGet() + "_" + suffix;
	}

	public static <T extends IHDLObject> T handleMultiBitAccess(T apply, HDLEvaluationContext context) {
		final ModificationSet ms = new ModificationSet();
		final HDLVariableRef[] refs = apply.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			final ArrayList<HDLRange> bits = ref.getBits();
			if (bits.size() > 1) {
				if (ref.getContainer() instanceof HDLAssignment) {
					final HDLAssignment ass = (HDLAssignment) ref.getContainer();
					if (ass.getLeft() == ref) {
						// Multi Bit write access i.e b{1,2:3}=8;
						// variant 1
						// b{1}=8>>((3-2)+1)
						// b{2:3}=8
						// variant 2
						// bit<widthOfResult> b_bitAcces=8;
						// b{1}=b_bitAcces{sumOfWidthRightToIdx};
						// b{2:3}=b_bitAccess{from-min(from,to)+sumOfWidthRightToIdx:to-min(from,to)+sumOfWidthRightToIdx};
						final List<HDLStatement> replacements = new LinkedList<HDLStatement>();
						final Optional<BigInteger> constant = ConstantEvaluate.valueOf(ass.getRight(), context);
						if (constant.isPresent()) {
							BigInteger shift = BigInteger.ZERO;
							for (int j = bits.size() - 1; j >= 0; j--) {
								final HDLRange r = bits.get(j);
								final Optional<Range<BigInteger>> vr = RangeExtension.rangeOf(r, context);
								if (vr.isPresent()) {
									final BigInteger add = shift.add(vr.get().upperEndpoint().subtract(vr.get().lowerEndpoint()).abs());
									final BigInteger res = constant.get().shiftRight(shift.intValue()).and(BigInteger.ONE.shiftLeft(add.intValue()).subtract(BigInteger.ONE));
									final HDLVariableRef newRef = ref.setBits(HDLObject.asList(r));
									final HDLAssignment newAss = new HDLAssignment().setLeft(newRef).setType(ass.getType()).setRight(HDLLiteral.get(res));
									replacements.add(newAss);
									shift = add.add(BigInteger.ONE);
								} else
									throw new IllegalArgumentException("Can not determine range of " + r + " for multi bit access");
							}
						} else {
							final HDLQualifiedName varRefName = ref.getVarRefName();
							final String varName = getTempName(varRefName.getLastSegment(), "bitAccess");
							final HDLQualifiedName hVarName = new HDLQualifiedName(varName);
							replacements.add(new HDLVariableDeclaration().setType(TypeExtension.typeOf(ref).get()).addVariables(new HDLVariable().setName(varName)));
							replacements.add(new HDLAssignment().setLeft(new HDLVariableRef().setVar(varRefName.skipLast(1).append(varName))).setRight(ass.getRight()));
							BigInteger shift = BigInteger.ZERO;
							for (int j = bits.size() - 1; j >= 0; j--) {
								final HDLRange r = bits.get(j);
								final Optional<Range<BigInteger>> vr = RangeExtension.rangeOf(r, context);
								if (vr.isPresent()) {
									final BigInteger add = shift.add(vr.get().upperEndpoint().subtract(vr.get().lowerEndpoint()).abs());
									final HDLRange newRange = new HDLRange().setTo(HDLLiteral.get(shift)).setFrom(HDLLiteral.get(add)).normalize();
									final HDLExpression bitOp = new HDLVariableRef().setVar(hVarName).setBits(HDLObject.asList(newRange));
									final HDLVariableRef newRef = ref.setBits(HDLObject.asList(r));
									final HDLAssignment newAss = new HDLAssignment().setLeft(newRef).setType(ass.getType()).setRight(bitOp);
									replacements.add(newAss);
									shift = add.add(BigInteger.ONE);
								} else
									throw new IllegalArgumentException("Can not determine range of " + r + " for multi bit access");
							}
						}
						ms.replace(ass, replacements.toArray(new HDLStatement[0]));
					}
				}
				// Multi bit read access
				// b{1,4:5} == b{1}#b{4:5}
				HDLConcat concat = new HDLConcat();
				for (final HDLRange r : bits) {
					concat = concat.addCats(ref.setBits(HDLObject.asList(r)));
				}
				final HDLManip cast = new HDLManip().setType(HDLManipType.CAST).setCastTo(TypeExtension.typeOf(ref).get()).setTarget(concat);
				if (ref.getContainer() instanceof HDLManip) {
					final HDLManip manip = (HDLManip) ref.getContainer();
					if (manip.getType() == HDLManipType.CAST) {
						ms.replace(ref, concat);
					} else {
						ms.replace(ref, cast);
					}
				} else {
					ms.replace(ref, cast);
				}
			}
		}
		T tmp = ms.apply(apply);
		if (tmp != apply) {
			tmp = handleMultiBitAccess(tmp, context);
		}
		return tmp;
	}

	private static <T extends IHDLObject> T handleOutPortRead(T orig) {
		final ModificationSet ms = new ModificationSet();
		final Collection<HDLVariableDeclaration> list = HDLQuery.select(HDLVariableDeclaration.class).from(orig).where(HDLVariableDeclaration.fDirection)
				.isEqualTo(HDLDirection.OUT).getAll();
		for (HDLVariableDeclaration hdv : list) {
			if (hdv.getContainer(HDLInterface.class) != null) {
				continue;
			}
			final HDLVariableDeclaration origHdv = hdv;
			for (final HDLVariable var : hdv.getVariables()) {
				final Integer readCount = var.getMeta(IntegerMeta.READ_COUNT);
				if (readCount != null) {
					// Create new Declaration out uint<W> bar=bar_OutRead;
					// Extract bar from declaration out uint<W> x,y,bar=5;
					// Create a new Declaration uint<W> bar_outRead=5;
					// Change all reference to bar_OutRead

					final HDLVariable outVar = var.setName(getTempName(var.getName(), "OutRead"));
					ms.insertAfter(origHdv, hdv.setRegister(null).setVariables(HDLObject.asList(var.setDefaultValue(outVar.asHDLRef()))));

					if (hdv.getVariables().size() == 1) {
						hdv = hdv.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(outVar));
					} else {
						HDLRegisterConfig register = null;
						if (hdv.getRegister() != null) {
							register = hdv.getRegister();
						}
						ms.insertAfter(origHdv, new HDLVariableDeclaration().setRegister(register).setType(origHdv.resolveType().get()).addVariables(outVar));
						hdv = hdv.removeVariables(var);
					}

					// Replace all read occourances with the new bar_outRead
					final HDLQualifiedName originalFQN = var.asRef();
					final HDLQualifiedName newFQN = outVar.asRef();
					final HDLVariableRef[] reads = hdv.getContainer().getAllObjectsOf(HDLVariableRef.class, true);
					for (final HDLVariableRef varRef : reads)
						if (varRef.getVarRefName().equals(originalFQN)) {
							ms.replace(varRef, varRef.setVar(newFQN));
						}
				}
			}
			if (origHdv != hdv) {
				ms.replace(origHdv, hdv);
			}
		}
		return ms.apply(orig);
	}

}
