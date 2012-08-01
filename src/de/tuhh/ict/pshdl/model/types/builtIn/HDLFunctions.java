package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLFunctions implements IHDLFunctionResolver {

	public HDLFunctions() {

	}

	private static Map<String, List<IHDLFunctionResolver>> resolvers;

	public static void init(IServiceProvider sp) {
		resolvers = new HashMap<String, List<IHDLFunctionResolver>>();
		for (IHDLFunctionResolver resolver : sp.getAllFunctions()) {
			String[] names = resolver.getFunctionNames();
			for (String funcName : names) {
				List<IHDLFunctionResolver> list = resolvers.get(funcName);
				if (list == null) {
					list = new LinkedList<IHDLFunctionResolver>();
					resolvers.put(funcName, list);
				}
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

	private static EnumSet<HDLPrimitiveType> disallowedTypes = EnumSet.of(HDLPrimitiveType.BIT, HDLPrimitiveType.BITVECTOR, HDLPrimitiveType.BOOL);

	public static enum BuiltInFunctions {
		max, min, abs
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunction function) {
		String name = function.getName();
		ArrayList<HDLExpression> params = function.getParams();
		for (HDLExpression exp : params) {
			HDLPrimitive type = (HDLPrimitive) exp.determineType();
			if (disallowedTypes.contains(type.getType())) {
				HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(HDLPrimitive.getInteger());
				info.error = "The parameter " + exp + " of type:" + type + " is not allowed for function:" + name;
				return info;
			}
		}
		try {
			BuiltInFunctions func = BuiltInFunctions.valueOf(name);
			switch (func) {
			case min:
			case max:
				return new HDLTypeInferenceInfo(HDLPrimitive.getInteger(), HDLPrimitive.getInteger(), HDLPrimitive.getInteger());
			case abs:
				return new HDLTypeInferenceInfo(HDLPrimitive.getInteger(), HDLPrimitive.getInteger());
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public BigInteger evaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context) {
		String name = function.getName();
		BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		switch (func) {
		case abs:
			return args.get(0).abs();
		case min:
			return args.get(0).min(args.get(1));
		case max:
			return args.get(0).max(args.get(1));
		}
		return null;
	}

	@Override
	public ValueRange range(HDLFunction function, HDLEvaluationContext context) {
		String name = function.getName();
		ValueRange zeroArg = function.getParams().get(0).determineRange(context);
		BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		switch (func) {
		case abs:
			return new ValueRange(zeroArg.from.abs(), zeroArg.to.abs());
		case min:
			ValueRange oneArgMin = function.getParams().get(1).determineRange(context);
			return new ValueRange(zeroArg.from.min(oneArgMin.from), zeroArg.to.min(oneArgMin.to));
		case max:
			ValueRange oneArgMax = function.getParams().get(1).determineRange(context);
			return new ValueRange(zeroArg.from.max(oneArgMax.from), zeroArg.to.max(oneArgMax.to));
		}
		return null;
	}

	@Override
	public String[] getFunctionNames() {
		String[] res = new String[BuiltInFunctions.values().length];
		BuiltInFunctions[] values = BuiltInFunctions.values();
		for (int i = 0; i < values.length; i++) {
			BuiltInFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}
}
