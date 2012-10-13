package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.type.*;

public class HDLBuiltInFunctions implements IHDLFunctionResolver {
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
				return new HDLTypeInferenceInfo(HDLPrimitive.getNatural(), HDLPrimitive.getInteger());
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

	@Override
	public VHDLContext toVHDL(HDLFunction function, int pid) {
		return null;
	}

	@Override
	public FunctionCall toVHDLExpression(HDLFunction function) {
		FunctionDeclaration fd = new FunctionDeclaration(function.getName(), UnresolvedType.NO_NAME);
		FunctionCall res = new FunctionCall(fd);
		for (HDLExpression exp : function.getParams()) {
			res.getParameters().add(new AssociationElement(exp.toVHDL()));
		}
		return res;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		switch (BuiltInFunctions.valueOf(funcName)) {
		case abs: {
			FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(), "Returns the absolute value of a number (makes it positive)",
					"uint - the absolute (positive) value of a number", false);
			fi.arguments.put("int number", "The number. Bit types are not allowed as they don't have an interpretable value");
			return fi;
		}
		case max: {
			FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(), "Returns the bigger value of two numbers",
					"int - the bigger value of two numbers", false);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		case min: {
			FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(), "Returns the smaller value of two numbers",
					"int - the smaller value of two numbers", false);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		}
		return null;
	}
}
