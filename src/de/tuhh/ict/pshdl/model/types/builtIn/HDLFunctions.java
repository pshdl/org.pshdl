package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation.*;
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
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				HDLTypeInferenceInfo resolve = resolver.resolve(function);
				if (resolve != null)
					return resolve;
			}
		HDLFunction rFunc = function.resolveName();
		if (rFunc != null) {
			if (rFunc instanceof HDLInlineFunction) {
				HDLInlineFunction hif = (HDLInlineFunction) rFunc;
				HDLExpression expression = hif.getReplacementExpression(function);
				if (expression != null) {
					HDLType type = expression.determineType();
					if (type instanceof HDLPrimitive) {
						HDLPrimitive result = (HDLPrimitive) type;
						HDLType args[] = new HDLType[function.getParams().size()];
						int i = 0;
						for (HDLExpression exp : function.getParams()) {
							args[i++] = exp.determineType();
						}
						return new HDLTypeInferenceInfo(result, args);
					}
				}
			}
		}
		return null;
	}

	public static BigInteger constantEvaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				BigInteger eval = resolver.evaluate(function, args, context);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static ValueRange determineRange(HDLFunctionCall function, HDLEvaluationContext context) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				ValueRange eval = resolver.range(function, context);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static VHDLContext toVHDL(HDLFunctionCall function, int pid) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				VHDLContext eval = resolver.toVHDL(function, pid);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static Expression<?> toVHDLExpression(HDLFunctionCall function) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getNameRefName().getLastSegment());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				Expression<?> eval = resolver.toVHDLExpression(function);
				if (eval != null)
					return eval;
			}
		return null;
	}
}
