package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import com.google.common.collect.Range;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;

public class HDLBuiltInFunctions implements IHDLFunctionResolver {

	public static enum BuiltInFunctions {
		highZ
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function) {
		String name = function.getNameRefName().getLastSegment();
		try {
			BuiltInFunctions func = BuiltInFunctions.valueOf(name);
			switch (func) {
			case highZ:
				if (function.getParams().size() == 1)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBit(), HDLPrimitive.getNatural());
				return new HDLTypeInferenceInfo(HDLPrimitive.getBit());
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public BigInteger evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		switch (getFuncEnum(function)) {
		case highZ:
			return null;
		}
		return null;
	}

	private BuiltInFunctions getFuncEnum(HDLFunctionCall function) {
		String name = function.getNameRefName().getLastSegment();
		BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		return func;
	}

	@Override
	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context) {
		switch (getFuncEnum(function)) {
		case highZ:
			return null;
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
	public VHDLContext toVHDL(HDLFunctionCall function, int pid) {
		return null;
	}

	@Override
	public Expression<?> toVHDLExpression(HDLFunctionCall function) {
		switch (getFuncEnum(function)) {
		case highZ:
			if (function.getParams().size() == 0)
				return new CharacterLiteral('Z');

			Aggregate aggregate = new Aggregate();
			HDLRange range = new HDLRange().setFrom(HDLLiteral.get(1)).setTo(function.getParams().get(0));
			aggregate.createAssociation(new CharacterLiteral('Z'), VHDLExpressionExtension.INST.toVHDL(range, Direction.TO));
			return aggregate;
		}
		return null;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		switch (BuiltInFunctions.valueOf(funcName)) {
		case highZ: {
			FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(),
					"Returns a high Z. This is useful for tri-state busses, high z however is not supported in PSHDL as computational value.", "highZ", false, FunctionType.NATIVE);
			return fi;
		}
		}
		return null;
	}
}
