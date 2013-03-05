package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
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
		HDLFunction rFunc = function.resolveName();
		if (rFunc != null) {
			if (rFunc instanceof HDLInlineFunction) {
				HDLInlineFunction hif = (HDLInlineFunction) rFunc;
				HDLExpression expression = hif.getReplacementExpression(function);
				if (expression != null) {
					Optional<? extends HDLType> type = TypeExtension.typeOf(expression);
					if (type.isPresent() && (type.get() instanceof HDLPrimitive)) {
						HDLPrimitive result = (HDLPrimitive) type.get();
						HDLType args[] = new HDLType[function.getParams().size()];
						int i = 0;
						for (HDLExpression exp : function.getParams()) {
							Optional<? extends HDLType> expType = TypeExtension.typeOf(exp);
							if (expType.isPresent())
								args[i++] = expType.get();
							else
								return null;
						}
						return new HDLTypeInferenceInfo(result, args);
					}
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
