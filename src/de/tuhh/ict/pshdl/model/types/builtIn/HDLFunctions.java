package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.evaluation.*;

public class HDLFunctions implements IHDLFunctionResolver {

	private static Map<String, List<IHDLFunctionResolver>> resolvers = new HashMap<String, List<IHDLFunctionResolver>>();
	static {
		IHDLFunctionResolver builtIn = new HDLFunctions();
		resolvers.put("max", Arrays.asList(builtIn));
		resolvers.put("min", Arrays.asList(builtIn));
		resolvers.put("abs", Arrays.asList(builtIn));
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

	private static EnumSet<HDLPrimitiveType> disallowedTypes = EnumSet.of(HDLPrimitiveType.BIT, HDLPrimitiveType.BITVECTOR, HDLPrimitiveType.BOOL);

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
		if ("max".equals(name) || "min".equals(name)) {
			return new HDLTypeInferenceInfo(HDLPrimitive.getInteger(), HDLPrimitive.getInteger(), HDLPrimitive.getInteger());
		}
		if ("abs".equals(name)) {
			return new HDLTypeInferenceInfo(HDLPrimitive.getInteger(), HDLPrimitive.getInteger());
		}
		return null;
	}

	@Override
	public BigInteger evaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context) {
		String name = function.getName();
		if ("abs".equals(name)) {
			return args.get(0).abs();
		}
		if ("min".equals(name)) {
			return args.get(0).min(args.get(1));
		}
		if ("max".equals(name)) {
			return args.get(0).max(args.get(1));
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

	@Override
	public ValueRange range(HDLFunction function, HDLEvaluationContext context) {
		String name = function.getName();
		ValueRange zeroArg = function.getParams().get(0).determineRange(context);
		if ("abs".equals(name)) {
			return new ValueRange(zeroArg.from.abs(), zeroArg.to.abs());
		}
		ValueRange oneArg = function.getParams().get(1).determineRange(context);
		if ("min".equals(name)) {
			return new ValueRange(zeroArg.from.min(oneArg.from), zeroArg.to.min(oneArg.to));
		}
		if ("max".equals(name)) {
			return new ValueRange(zeroArg.from.max(oneArg.from), zeroArg.to.max(oneArg.to));
		}
		return null;
	}
}
