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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLType;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.utils.services.CompilerInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;
import org.pshdl.model.utils.services.IHDLFunctionResolver;
import org.pshdl.model.utils.services.IServiceProvider;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public class HDLFunctions {

	private HDLFunctions() {

	}

	private static Map<String, List<IHDLFunctionResolver>> resolvers;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		resolvers = new HashMap<String, List<IHDLFunctionResolver>>();
		for (final IHDLFunctionResolver resolver : sp.getAllFunctions()) {
			final String[] names = resolver.getFunctionNames();
			for (final String funcName : names) {
				List<IHDLFunctionResolver> list = resolvers.get(funcName);
				if (list == null) {
					list = new LinkedList<IHDLFunctionResolver>();
					resolvers.put(funcName, list);
				}
				info.registeredFunctions.put(funcName, resolver.getFunctionInfo(funcName));
				list.add(resolver);
			}
		}
		for (final HDLFunction func : PSHDLLib.FUNCTIONS) {
			FunctionType type;
			switch (func.getClassType()) {
			case HDLNativeFunction:
				type = FunctionType.NATIVE;
				break;
			case HDLInlineFunction:
				type = FunctionType.INLINE;
				break;
			case HDLSubstituteFunction:
				type = FunctionType.SUBSTITUTION;
				break;
			default:
				throw new IllegalArgumentException("Unknown type:" + func);
			}
			final FunctionInformation fi = new FunctionInformation(func.getName(), "PSHDL Standard Lib", func.toString(), null, false, type);
			info.registeredFunctions.put(func.getName(), fi);
		}
	}

	public static HDLTypeInferenceInfo getInferenceInfo(HDLFunctionCall function) {
		final List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (final IHDLFunctionResolver resolver : list) {
				final HDLTypeInferenceInfo resolve = resolver.resolve(function);
				if (resolve != null)
					return resolve;
			}
		}
		final Optional<HDLFunction> rFunc = function.resolveName();
		if (rFunc.isPresent())
			if (rFunc.get() instanceof HDLInlineFunction) {
				final HDLInlineFunction hif = (HDLInlineFunction) rFunc.get();
				final HDLExpression expression = hif.getReplacementExpression(function);
				if (expression != null) {
					final Optional<? extends HDLType> type = TypeExtension.typeOf(expression);
					if (type.isPresent() && (type.get() instanceof HDLPrimitive)) {
						final HDLPrimitive result = (HDLPrimitive) type.get();
						final HDLType args[] = new HDLType[function.getParams().size()];
						int i = 0;
						for (final HDLExpression exp : function.getParams()) {
							final Optional<? extends HDLType> expType = TypeExtension.typeOf(exp);
							if (expType.isPresent()) {
								args[i++] = expType.get();
							} else
								return null;
						}
						return new HDLTypeInferenceInfo(result, args);
					}
				}
			}
		return null;
	}

	public static Optional<BigInteger> constantEvaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		final List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (final IHDLFunctionResolver resolver : list) {
				final Optional<BigInteger> eval = resolver.evaluate(function, args, context);
				if (eval.isPresent())
					return eval;
			}
		}
		return Optional.absent();
	}

	public static Optional<Range<BigInteger>> determineRange(HDLFunctionCall function, HDLEvaluationContext context) {
		final List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (final IHDLFunctionResolver resolver : list) {
				final Range<BigInteger> eval = resolver.range(function, context);
				if (eval != null)
					return Optional.of(eval);
			}
		}
		return Optional.absent();
	}

}
