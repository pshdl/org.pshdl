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

import java.math.*;
import java.util.*;

import org.pshdl.generator.vhdl.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.upb.hni.vmagic.expression.*;

public class HDLFunctions {

	private HDLFunctions() {

	}

	private static Map<String, List<IHDLFunctionResolver>> resolvers;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		resolvers = new HashMap<String, List<IHDLFunctionResolver>>();
		for (IHDLFunctionResolver resolver : sp.getAllFunctions()) {
			String[] names = resolver.getFunctionNames();
			for (String funcName : names) {
				List<IHDLFunctionResolver> list = resolvers.get(funcName);
				if (list == null) {
					list = new LinkedList<IHDLFunctionResolver>();
					resolvers.put(funcName, list);
				}
				info.registeredFunctions.put(funcName, resolver.getFunctionInfo(funcName));
				list.add(resolver);
			}
		}
		for (HDLFunction func : PSHDLLib.FUNCTIONS) {
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
			FunctionInformation fi = new FunctionInformation(func.getName(), "PSHDL Standard Lib", func.toString(), null, false, type);
			info.registeredFunctions.put(func.getName(), fi);
		}
	}

	public static HDLTypeInferenceInfo getInferenceInfo(HDLFunctionCall function) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (IHDLFunctionResolver resolver : list) {
				HDLTypeInferenceInfo resolve = resolver.resolve(function);
				if (resolve != null)
					return resolve;
			}
		}
		Optional<HDLFunction> rFunc = function.resolveName();
		if (rFunc.isPresent())
			if (rFunc.get() instanceof HDLInlineFunction) {
				HDLInlineFunction hif = (HDLInlineFunction) rFunc.get();
				HDLExpression expression = hif.getReplacementExpression(function);
				if (expression != null) {
					Optional<? extends HDLType> type = TypeExtension.typeOf(expression);
					if (type.isPresent() && (type.get() instanceof HDLPrimitive)) {
						HDLPrimitive result = (HDLPrimitive) type.get();
						HDLType args[] = new HDLType[function.getParams().size()];
						int i = 0;
						for (HDLExpression exp : function.getParams()) {
							Optional<? extends HDLType> expType = TypeExtension.typeOf(exp);
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
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (IHDLFunctionResolver resolver : list) {
				Optional<BigInteger> eval = resolver.evaluate(function, args, context);
				if (eval.isPresent())
					return eval;
			}
		}
		return Optional.absent();
	}

	public static Optional<Range<BigInteger>> determineRange(HDLFunctionCall function, HDLEvaluationContext context) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (IHDLFunctionResolver resolver : list) {
				Range<BigInteger> eval = resolver.range(function, context);
				if (eval != null)
					return Optional.of(eval);
			}
		}
		return Optional.absent();
	}

	public static VHDLContext toVHDL(HDLFunctionCall function, int pid) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (IHDLFunctionResolver resolver : list) {
				VHDLContext eval = resolver.toVHDL(function, pid);
				if (eval != null)
					return eval;
			}
		}
		return null;
	}

	public static Expression<?> toVHDLExpression(HDLFunctionCall function) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null) {
			for (IHDLFunctionResolver resolver : list) {
				Expression<?> eval = resolver.toVHDLExpression(function);
				if (eval != null)
					return eval;
			}
		}
		return null;
	}
}
