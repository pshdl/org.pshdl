package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
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
	}

	public static HDLTypeInferenceInfo getInferenceInfo(HDLFunction function) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getName());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				HDLTypeInferenceInfo resolve = resolver.resolve(function);
				if (resolve != null)
					return resolve;
			}
		return null;
	}

	public static BigInteger constantEvaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getName());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				BigInteger eval = resolver.evaluate(function, args, context);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static ValueRange determineRange(HDLFunction function, HDLEvaluationContext context) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getName());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				ValueRange eval = resolver.range(function, context);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static VHDLContext toVHDL(HDLFunction function, int pid) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getName());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				VHDLContext eval = resolver.toVHDL(function, pid);
				if (eval != null)
					return eval;
			}
		return null;
	}

	public static FunctionCall toVHDLExpression(HDLFunction function) {
		List<IHDLFunctionResolver> list = resolvers.get(function.getName());
		if (list != null)
			for (IHDLFunctionResolver resolver : list) {
				FunctionCall eval = resolver.toVHDLExpression(function);
				if (eval != null)
					return eval;
			}
		return null;
	}
}
