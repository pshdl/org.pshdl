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
import java.util.List;

import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.RangeExtension;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;
import org.pshdl.model.utils.services.IHDLFunctionResolver;
import org.pshdl.model.utils.services.IStaticNativeFunctionProvider;
import org.pshdl.model.utils.services.StaticNativeFunction;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public class HDLBuiltInFunctions implements IHDLFunctionResolver, IStaticNativeFunctionProvider {

	public static final HDLFunction ABS_UINT = createABS(Type.ANY_UINT);
	public static final HDLFunction ABS_INT = createABS(Type.ANY_INT);

	public static final HDLFunction HIGHZ = createHighZ();

	private static HDLFunction createHighZ() {
		return new HDLNativeFunction().setSimOnly(false).setName("highZ").setReturnType(new HDLFunctionParameter().setType(Type.ANY_BIT).setRw(RWType.RETURN));
	}

	private static HDLFunction createABS(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName("abs").setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ));
	}

	public static final HDLFunction LOG2CEIL_UINT = createLog2(Type.ANY_UINT, "ceil");
	public static final HDLFunction LOG2CEIL_INT = createLog2(Type.ANY_INT, "ceil");
	public static final HDLFunction LOG2FLOOR_UINT = createLog2(Type.ANY_UINT, "floor");
	public static final HDLFunction LOG2FLOOR_INT = createLog2(Type.ANY_INT, "floor");

	private static HDLFunction createLog2(Type type, String rounding) {
		return new HDLNativeFunction().setSimOnly(false).setName(BuiltInFunctions.valueOf("log2" + rounding).name())
				.setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ));
	}

	public static final HDLFunction MIN_UINT = createMIN(Type.ANY_UINT);
	public static final HDLFunction MIN_INT = createMIN(Type.ANY_INT);

	private static HDLFunction createMIN(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName(BuiltInFunctions.min.name()).setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("b")).setRw(RWType.READ));
	}

	public static final HDLFunction MAX_INT = createMAX(Type.ANY_INT);
	public static final HDLFunction MAX_UINT = createMAX(Type.ANY_UINT);

	private static HDLFunction createMAX(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName(BuiltInFunctions.max.name()).setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("b")).setRw(RWType.READ));
	}

	public static final HDLFunction[] FUNCTIONS = new HDLFunction[] {//
	MIN_UINT, MAX_UINT, ABS_UINT, LOG2CEIL_UINT, LOG2FLOOR_UINT,//
			MIN_INT, MAX_INT, ABS_INT, LOG2CEIL_INT, LOG2FLOOR_INT, HIGHZ };

	public static enum BuiltInFunctions {
		highZ, min, max, abs, log2ceil, log2floor
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function) {
		final String name = function.getNameRefName().getLastSegment();
		try {
			final BuiltInFunctions func = BuiltInFunctions.valueOf(name);
			switch (func) {
			case min:
			case max:
				return new HDLTypeInferenceInfo(HDLPrimitive.getInteger(), HDLPrimitive.getInteger(), HDLPrimitive.getInteger());
			case abs:
				return new HDLTypeInferenceInfo(HDLPrimitive.getNatural(), HDLPrimitive.getInteger());

			case highZ:
				if (function.getParams().size() == 1)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBit(), HDLPrimitive.getNatural());
				return new HDLTypeInferenceInfo(HDLPrimitive.getBit());
			}
		} catch (final Exception e) {
		}
		return null;
	}

	@Override
	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		switch (getFuncEnum(function)) {
		case abs:
			return Optional.of(args.get(0).abs());
		case min:
			return Optional.of(args.get(0).min(args.get(1)));
		case max:
			return Optional.of(args.get(0).max(args.get(1)));

		case highZ:
			return Optional.absent();
		}
		return Optional.absent();
	}

	public static BuiltInFunctions getFuncEnum(HDLFunctionCall function) {
		final String name = function.getNameRefName().getLastSegment();
		final BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		return func;
	}

	@Override
	public Optional<Range<BigInteger>> range(HDLFunctionCall function, HDLEvaluationContext context) {
		if (function.getParams().isEmpty())
			return null;
		final Optional<Range<BigInteger>> zeroArg = RangeExtension.rangeOf(function.getParams().get(0), context);
		if (!zeroArg.isPresent())
			return null;
		final Range<BigInteger> zeroRange = zeroArg.get();
		switch (getFuncEnum(function)) {
		case abs:
			final BigInteger from = zeroRange.lowerEndpoint().abs();
			final BigInteger to = zeroRange.upperEndpoint().abs();
			return asRange(from, to);
		case min:
			final Optional<Range<BigInteger>> oneArgMin = RangeExtension.rangeOf(function.getParams().get(1), context);
			if (!oneArgMin.isPresent())
				return Optional.absent();
			return asRange(zeroRange.lowerEndpoint().min(oneArgMin.get().lowerEndpoint()), zeroRange.upperEndpoint().min(oneArgMin.get().upperEndpoint()));
		case max:
			final Optional<Range<BigInteger>> oneArgMax = RangeExtension.rangeOf(function.getParams().get(1), context);
			if (!oneArgMax.isPresent())
				return Optional.absent();
			return asRange(zeroRange.lowerEndpoint().max(oneArgMax.get().lowerEndpoint()), zeroRange.upperEndpoint().max(oneArgMax.get().upperEndpoint()));
		case highZ:
			return Optional.absent();
		}
		return Optional.absent();
	}

	public Optional<Range<BigInteger>> asRange(BigInteger from, BigInteger to) {
		if (from.compareTo(to) > 0)
			return Optional.of(RangeTool.createRange(to, from));
		return Optional.of(RangeTool.createRange(from, to));
	}

	@Override
	public String[] getFunctionNames() {
		final String[] res = new String[BuiltInFunctions.values().length];
		final BuiltInFunctions[] values = BuiltInFunctions.values();
		for (int i = 0; i < values.length; i++) {
			final BuiltInFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		switch (BuiltInFunctions.valueOf(funcName)) {
		case highZ: {
			final FunctionInformation fi = new FunctionInformation(HDLBuiltInFunctions.class.getSimpleName(),
					"Returns a high Z. This is useful for tri-state busses, high z however is not supported in PSHDL as computational value.", "highZ", HIGHZ);
			return fi;
		}
		case abs: {
			final FunctionInformation fi = new FunctionInformation(HDLBuiltInFunctions.class.getSimpleName(), "Returns the absolute value of a number (makes it positive)",
					"uint - the absolute (positive) value of a number", ABS_INT, ABS_UINT);
			fi.arguments.put("int number", "The number. Bit types are not allowed as they don't have an interpretable value");
			return fi;
		}
		case max: {
			final FunctionInformation fi = new FunctionInformation(HDLBuiltInFunctions.class.getSimpleName(), "Returns the bigger value of two numbers",
					"int - the bigger value of two numbers", MAX_INT, MAX_UINT);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		case min: {
			final FunctionInformation fi = new FunctionInformation(HDLBuiltInFunctions.class.getSimpleName(), "Returns the smaller value of two numbers",
					"int - the smaller value of two numbers", MIN_INT, MIN_UINT);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		}
		return null;
	}

	@Override
	public StaticNativeFunction[] getFunctions() {
		// TODO Auto-generated method stub
		return null;
	}

}
