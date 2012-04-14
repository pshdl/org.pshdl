package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;

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
}
