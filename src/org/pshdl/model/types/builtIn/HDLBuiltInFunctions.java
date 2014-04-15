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

import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.simulation.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
import org.pshdl.model.utils.services.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class HDLBuiltInFunctions implements IHDLFunctionResolver {

	public static enum BuiltInFunctions {
		highZ, min, max, abs
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
	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context) {
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
				return null;
			return asRange(zeroRange.lowerEndpoint().min(oneArgMin.get().lowerEndpoint()), zeroRange.upperEndpoint().min(oneArgMin.get().upperEndpoint()));
		case max:
			final Optional<Range<BigInteger>> oneArgMax = RangeExtension.rangeOf(function.getParams().get(1), context);
			if (!oneArgMax.isPresent())
				return null;
			return asRange(zeroRange.lowerEndpoint().max(oneArgMax.get().lowerEndpoint()), zeroRange.upperEndpoint().max(oneArgMax.get().upperEndpoint()));
		case highZ:
			return null;
		}
		return null;
	}

	public Range<BigInteger> asRange(BigInteger from, BigInteger to) {
		if (from.compareTo(to) > 0)
			return RangeTool.createRange(to, from);
		return RangeTool.createRange(from, to);
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
			final FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(),
					"Returns a high Z. This is useful for tri-state busses, high z however is not supported in PSHDL as computational value.", "highZ", false, FunctionType.NATIVE);
			return fi;
		}
		case abs: {
			final FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(),
					"Returns the absolute value of a number (makes it positive)", "uint - the absolute (positive) value of a number", false, FunctionType.NATIVE);
			fi.arguments.put("int number", "The number. Bit types are not allowed as they don't have an interpretable value");
			return fi;
		}
		case max: {
			final FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(), "Returns the bigger value of two numbers",
					"int - the bigger value of two numbers", false, FunctionType.NATIVE);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		case min: {
			final FunctionInformation fi = new FunctionInformation(funcName, HDLBuiltInFunctions.class.getSimpleName(), "Returns the smaller value of two numbers",
					"int - the smaller value of two numbers", false, FunctionType.NATIVE);
			fi.arguments.put("int numberA", "The first number");
			fi.arguments.put("int numberB", "The second number");
			return fi;
		}
		}
		return null;
	}
}
