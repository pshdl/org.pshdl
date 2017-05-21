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
package org.pshdl.model.types.builtIn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLType;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.ModificationSet;
import org.pshdl.model.utils.services.CompilerInformation;
import org.pshdl.model.utils.services.IDynamicFunctionProvider;
import org.pshdl.model.utils.services.INativeFunctionProvider;
import org.pshdl.model.utils.services.IServiceProvider;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class HDLFunctions {

	private HDLFunctions() {
	}

	public static class FunctionScore implements Comparable<FunctionScore> {
		public int score = 0;
		public List<String> penalties = Lists.newArrayList();
		public final HDLFunction function;

		public FunctionScore(HDLFunction function) {
			super();
			this.function = function;
		}

		@Override
		public int compareTo(FunctionScore o) {
			return score - o.score;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((function == null) ? 0 : function.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final FunctionScore other = (FunctionScore) obj;
			if (function == null) {
				if (other.function != null) {
					return false;
				}
			} else if (!function.equals(other.function)) {
				return false;
			}
			return true;
		}

		public void incScore(int amount, String explain) {
			score += amount;
			penalties.add(explain);
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Score: ").append(score).append('\n');
			for (final String string : penalties) {
				sb.append('\t').append(string).append('\n');
			}
			sb.append(function).append('\n');
			return sb.toString();
		}

	}

	private static Multimap<HDLQualifiedName, HDLFunction> signatures;
	private static Multimap<HDLQualifiedName, HDLFunctionImplementation> provider;
	private static Multimap<HDLQualifiedName, IDynamicFunctionProvider> dynamicProvider;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		signatures = LinkedListMultimap.create();
		provider = LinkedListMultimap.create();
		dynamicProvider = LinkedListMultimap.create();
		final Collection<IDynamicFunctionProvider> dyns = sp.getAllDynamicFunctionProvider();
		for (final IDynamicFunctionProvider dynProv : dyns) {
			final HDLQualifiedName[] registeredNames = dynProv.getDynamicFunctions();
			for (final HDLQualifiedName hdlQualifiedName : registeredNames) {
				dynamicProvider.put(hdlQualifiedName, dynProv);
			}
		}
		final Collection<INativeFunctionProvider> nativeStaticProvider = sp.getAllNativeFunctionProvider();
		for (final INativeFunctionProvider snfp : nativeStaticProvider) {
			final HDLFunctionImplementation[] functionImpl = snfp.getStaticFunctions();
			for (final HDLFunctionImplementation funcImpl : functionImpl) {
				for (final HDLFunction hdlFunction : funcImpl.signatures()) {
					final HDLQualifiedName fqnFunction = new HDLQualifiedName(hdlFunction.getName());
					provider.put(fqnFunction, funcImpl);
					signatures.put(fqnFunction, hdlFunction);
				}
			}
		}
	}

	public static Optional<HDLFunction> resolveNativeFunction(HDLQualifiedName functionRefName, HDLFunctionCall call) {
		final Iterable<HDLFunction> list = getCandidateFunctions(functionRefName, call);
		if (!list.iterator().hasNext()) {
			return Optional.absent();
		}
		final SortedSet<FunctionScore> scored = scoreList(list, call);
		final FunctionScore first = scored.first();
		if (first.score < 1000) {
			return Optional.of(first.function);
		}
		return Optional.absent();
	}

	public static Iterable<HDLFunction> getCandidateFunctions(HDLQualifiedName functionRefName, HDLFunctionCall call) {
		final List<HDLFunction> res = Lists.newArrayList();
		final Collection<IDynamicFunctionProvider> dyamics = dynamicProvider.get(functionRefName);
		for (final IDynamicFunctionProvider provider : dyamics) {
			final Optional<? extends HDLFunctionImplementation> optFunc = provider.getFunctionFor(call, functionRefName);
			if (optFunc.isPresent()) {
				final HDLFunction[] hdlFunctions = optFunc.get().signatures();
				for (final HDLFunction hdlFunction : hdlFunctions) {
					res.add(hdlFunction);
				}
			}
		}
		res.addAll(signatures.get(functionRefName));
		return res;
	}

	public static SortedSet<FunctionScore> scoreList(Iterable<HDLFunction> list, HDLFunctionCall call) {
		final List<HDLType> types = Lists.newArrayList();
		final SortedSet<FunctionScore> res = Sets.newTreeSet();
		final ArrayList<HDLExpression> params = call.getParams();
		for (final HDLExpression param : params) {
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(param);
			if (!typeOf.isPresent()) {
				types.add(null);
			} else {
				types.add(typeOf.get());
			}
		}
		for (final HDLFunction hdlFunction : list) {
			final FunctionScore funcScore = new FunctionScore(hdlFunction);
			final ArrayList<HDLFunctionParameter> args = hdlFunction.getArgs();
			final int missingArgs = args.size() - types.size();
			if (missingArgs != 0) {
				funcScore.incScore(10000 * Math.abs(missingArgs), "Missing arguments");
				continue;
			}
			for (int i = 0; i < args.size(); i++) {
				final HDLFunctionParameter arg = args.get(i);
				final HDLExpression param = params.get(i);
				switch (arg.getRw()) {
				case READ:
				case RETURN:
					break;
				case READWRITE:
				case WRITE:
					if (!(param instanceof HDLReference)) {
						funcScore.incScore(10000, "The argument for " + arg.getName().getName() + " needs to be a reference as it is written.");
					}
					break;
				}

				final HDLType type = types.get(i);
				checkRoughType(funcScore, arg, type, param);
				if (funcScore.score < 1000) {
					HDLPrimitive prim = null;
					if (type instanceof HDLPrimitive) {
						prim = (HDLPrimitive) type;
					}
					final String paramName = args.get(i).getName().getName();
					if (prim != null) {
						switch (arg.getType()) {
						case PARAM_BIT:
						case PARAM_ANY_BIT:
							if (!prim.isBits()) {
								funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to a bit representation for parameter: " + paramName);
							}
							break;
						case PARAM_INT:
						case PARAM_ANY_INT:
							if (!prim.isBits()) {
								funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to a bit representation for parameter: " + paramName);
							} else if (!prim.isNumber()) {
								funcScore.incScore(1000, "There is no automatic cast from this bit representation to int for parameter: " + paramName);
							} else if ((prim.getType() != HDLPrimitiveType.INT) && (prim.getType() != HDLPrimitiveType.INTEGER)) {
								funcScore.incScore(5, "Automatic casting from uint to int will occour for parameter: " + paramName);
							}
							break;
						case PARAM_UINT:
						case PARAM_ANY_UINT:
							if (!prim.isBits()) {
								funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to a bit representation for parameter: " + paramName);
							} else if (!prim.isNumber()) {
								funcScore.incScore(1000, "There is no automatic cast from this bit representation to int for parameter: " + paramName);
							} else if ((prim.getType() != HDLPrimitiveType.UINT) && (prim.getType() != HDLPrimitiveType.NATURAL)) {
								funcScore.incScore(50, "Automatic casting from int to uint will occour for parameter: " + paramName);
							}
							break;
						case PARAM_BOOL:
							if (!prim.isBits()) {
								funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to a bit representation for parameter: " + paramName);
							} else if (prim.getType() != HDLPrimitiveType.BOOL) {
								funcScore.incScore(100, "Automatic casting to boolean will occour for parameter: " + paramName);
							}
							break;
						case PARAM_STRING:
							if (prim.getType() != HDLPrimitiveType.STRING) {
								funcScore.incScore(1000, "There is no automatic casting to String for parameter: " + paramName);
							}
							break;
						case PARAM_ENUM:
						case PARAM_FUNCTION:
						case PARAM_IF:
						case PARAM_ANY_ENUM:
						case PARAM_ANY_IF:
							break;
						}
					}
				}
			}
			res.add(funcScore);
		}
		return res;
	}

	public static int checkRoughType(FunctionScore funcScore, final HDLFunctionParameter arg, final HDLType type, HDLExpression param) {
		if (type == null) {
			funcScore.incScore(1000, "Unkonwn type can not be cast to a bit representation for parameter: " + arg.getName().getName());
			return funcScore.score;
		}
		switch (arg.getType()) {
		case PARAM_ANY_ENUM:
		case PARAM_ENUM:
			if (type.getClassType() != HDLClass.HDLEnum) {
				funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to an enum for parameter: " + arg.getName().getName());
			} else if (arg.getType() == Type.PARAM_ENUM) {
				final Optional<HDLEnum> enumSpec = arg.resolveEnumSpec();
				if (enumSpec.isPresent()) {
					if (!type.equals(enumSpec.get())) {
						funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to enum " + enumSpec.get().getName() + " for parameter: " + arg.getName().getName());
					}
				}
			}
			break;
		case PARAM_ANY_IF:
		case PARAM_IF:
			if (type.getClassType() != HDLClass.HDLInterface) {
				funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to an interface for parameter: " + arg.getName().getName());
			} else if (arg.getType() == Type.PARAM_IF) {
				final Optional<HDLInterface> ifSpec = arg.resolveIfSpec();
				if (ifSpec.isPresent()) {
					final HDLInterface hIf = (HDLInterface) type;
					if (hIf.conformsTo(ifSpec.get(), false)) {
						funcScore.incScore(1000,
								"The interface :" + type.getName() + " does not conform to the interface " + ifSpec.get().getName() + " for parameter: " + arg.getName().getName());
					}
				}
			}
			break;
		case PARAM_INT:
		case PARAM_UINT:
		case PARAM_ANY_INT:
		case PARAM_ANY_UINT:
		case PARAM_BOOL:
			if ((arg.getConstant() != null) && arg.getConstant()) {
				final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(param, null);
				if (!valueOf.isPresent()) {
					funcScore.incScore(1000, "The argument:" + param + " is not constant");
				}
			}
			//$FALL-THROUGH$
		case PARAM_BIT:
		case PARAM_STRING:
		case PARAM_ANY_BIT:
			if (type.getClassType() != HDLClass.HDLPrimitive) {
				funcScore.incScore(1000, "Can not cast from:" + type.getName() + " to a bit representation for parameter: " + arg.getName().getName());
			}
			break;
		case PARAM_FUNCTION:
			if (!(param instanceof HDLFunction)) {
				funcScore.incScore(1000, "Expected a function, but found:" + param + " for parameter: " + arg.getName().getName());
			}
			break;
		}
		return funcScore.score;
	}

	public static Optional<BigInteger> constantEvaluate(HDLFunctionCall call, HDLEvaluationContext context) {
		final Optional<HDLFunction> func = call.resolveFunction();
		if (!func.isPresent()) {
			return Optional.absent();
		}
		final Collection<HDLFunctionImplementation> funcProvider = provider.get(FullNameExtension.fullNameOf(func.get()));
		for (final HDLFunctionImplementation funcImpl : funcProvider) {
			final Optional<BigInteger> value = funcImpl.getConstantValue(call, context);
			if (value.isPresent()) {
				return value;
			}
		}
		return Optional.absent();
	}

	public static void transform(HDLFunctionCall call, HDLEvaluationContext context, ModificationSet ms) {
		final Optional<HDLFunction> function = call.resolveFunction();
		if (!function.isPresent()) {
			return;
		}
		switch (function.get().getClassType()) {
		case HDLInlineFunction:
			final HDLInlineFunction hif = (HDLInlineFunction) function.get();
			final HDLExpression equivalentExpression = hif.getReplacementExpression(call);
			ms.replace(call, equivalentExpression);
			break;
		case HDLSubstituteFunction:
			final HDLSubstituteFunction hsf = (HDLSubstituteFunction) function.get();
			final HDLStatement[] statements = hsf.getReplacementStatements(call);
			ms.replace(call, statements);
			break;
		case HDLNativeFunction:
			final Collection<HDLFunctionImplementation> funcProvider = provider.get(FullNameExtension.fullNameOf(function.get()));
			for (final HDLFunctionImplementation funcImpl : funcProvider) {
				funcImpl.transform(call, context, ms);
				return;
			}
			break;
		default:
			throw new IllegalArgumentException("Unexpected type:" + function.get().getClassType());
		}
	}

	public static Optional<Range<BigInteger>> determineRange(HDLFunctionCall call, HDLEvaluationContext context) {
		for (final HDLFunctionImplementation funcImpl : provider.get(call.getFunctionRefName())) {
			final Optional<Range<BigInteger>> value = funcImpl.getRange(call, context);
			if (value.isPresent()) {
				return value;
			}
		}
		return Optional.absent();
	}

	public static void initLibrary(HDLLibrary hdlLibrary) {
		hdlLibrary.functions.putAll(signatures);
	}

	public static Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
		final HDLQualifiedName fqn = FullNameExtension.fullNameOf(function);
		final Collection<HDLFunctionImplementation> providers = provider.get(fqn);
		for (final HDLFunctionImplementation funcImpl : providers) {
			final Optional<? extends HDLType> specified = funcImpl.specifyReturnType(function, call, context);
			if (specified.isPresent()) {
				return specified;
			}
		}
		return Optional.absent();
	}

}
