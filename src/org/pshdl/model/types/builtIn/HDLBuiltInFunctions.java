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

import static org.pshdl.model.HDLFunctionParameter.param;
import static org.pshdl.model.HDLFunctionParameter.returnType;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.RangeExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.types.builtIn.HDLFunctionImplementation.HDLDefaultFunctionImpl;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.utils.ModificationSet;
import org.pshdl.model.utils.services.IDynamicFunctionProvider;
import org.pshdl.model.utils.services.INativeFunctionProvider;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.math.BigIntegerMath;

public class HDLBuiltInFunctions implements INativeFunctionProvider, IDynamicFunctionProvider {

	public static class HighZFunction extends HDLDefaultFunctionImpl {

		public HighZFunction() {
			super(true, false);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			return "Returns a high Z. This is useful for tri-state busses, high z however is not supported in PSHDL as computational value.";
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			return "Returns a 'z' that is of the required width";
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { HIGHZ, HIGHZ_BW };
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Optional.of(HDLPrimitive.getBit());
			}
			final HDLExpression expression = call.getParams().get(0);
			final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(expression, context);
			if (valueOf.isPresent()) {
				final BigInteger val = valueOf.get();
				if (val.equals(BigInteger.ONE)) {
					return Optional.of(HDLPrimitive.getBit());
				}
			}
			return Optional.of(HDLPrimitive.getBitvector().setWidth(expression));
		}

	}

	public static class AssertThat extends HDLDefaultFunctionImpl {

		public AssertThat() {
			super(true, true);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			return "Logs something when the assertion is false.\n" + "An assert of level INFO will simply print something on the console. This is equivalent to NOTE in VHDL.\n"
					+ "An assert of level WARNING will print something on the console and probably highlight it. This is equivalent to WARNING in VHDL.\n"
					+ "An assert of level ERROR will print something on the console and probably highlight it. It might also stop the simulation. This is equivalent to ERROR in VHDL.\n"
					+ "An assert of level FATAL will stop the simulation and print something on the console. This is equivalent to FAILURE in VHDL.\n";
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			final Map<String, String> res = Maps.newLinkedHashMap();
			res.put("assumption", "The assumption that should be true");
			res.put("assert", "The level as pshdl.Assert enum (example: Assert.ERROR)");
			res.put("message", "The message that will be printed when the assumption fails");
			return res;
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { ASSERT };
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.absent();
		}

	}

	public static class WidthFunctions extends HDLDefaultFunctionImpl {

		public WidthFunctions() {
			super(true, false);
		}

		@Override
		public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context) {
			final HDLExpression a = call.getParams().get(0);
			final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(TypeExtension.getWidth(a), context);
			switch (call.getFunctionRefName().getLastSegment()) {
			case "widthOf":
				return valueOf;
			case "msbOf":
				if (valueOf.isPresent()) {
					return Optional.of(valueOf.get().subtract(BigInteger.ONE));
				}
				return valueOf;
			default:
				throw new IllegalArgumentException("I know nothing about this function");
			}
		}

		@Override
		public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context) {
			final HDLExpression a = call.getParams().get(0);
			final Optional<Range<BigInteger>> rangeOf = RangeExtension.rangeOf(TypeExtension.getWidth(a), context);
			switch (call.getFunctionRefName().getLastSegment()) {
			case "widthOf":
				return rangeOf;
			case "msbOf":
				if (rangeOf.isPresent()) {
					final Range<BigInteger> range = rangeOf.get();
					final BigInteger lower = range.lowerEndpoint();
					final BigInteger upper = range.upperEndpoint().subtract(BigInteger.ONE);
					if (range.lowerEndpoint().equals(range.upperEndpoint())) {
						return Optional.of(Range.singleton(upper));
					}
					return Optional.of(Range.closed(lower, upper));
				}
				return rangeOf;
			default:
				throw new IllegalArgumentException("I know nothing about this function");
			}
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
			res.put("a", "any expression");
			return res;
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "widthOf":
				return "The width of the expression";
			case "msbOf":
				return "The higest possible bit index of the expression";
			default:
				throw new IllegalArgumentException("I know nothing about this function");
			}
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "widthOf":
				return "Returns the width of the expression";
			case "msbOf":
				return "Returns the higest possible bit index of the expression";
			default:
				throw new IllegalArgumentException("I know nothing about this function");
			}
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.of(HDLPrimitive.getNatural());
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { WIDTH_OF, MSB_OF };
		}

	}

	public static class AbsFunction extends HDLDefaultFunctionImpl {

		public AbsFunction() {
			super(true, true);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			return "Returns the absolute value of a number (makes it positive).";
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			return "the absolute (positive) value of a number";
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
			res.put("a", "the possibly negative number");
			return res;
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { ABS_INT, ABS_UINT };
		}

		@Override
		public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Optional.absent();
			}
			final HDLExpression arg = call.getParams().get(0);
			final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(arg, context);
			if (!constVal.isPresent()) {
				return Optional.absent();
			}
			return Optional.of(constVal.get().abs());
		}

		@Override
		public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Optional.absent();
			}
			final Optional<Range<BigInteger>> zeroArg = RangeExtension.rangeOf(call.getParams().get(0), context);
			if (!zeroArg.isPresent()) {
				return Optional.absent();
			}
			final Range<BigInteger> zeroRange = zeroArg.get();
			final BigInteger from = zeroRange.lowerEndpoint().abs();
			final BigInteger to = zeroRange.upperEndpoint().abs();
			return asRange(from, to);
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			final HDLExpression arg = call.getParams().get(0);
			final Optional<? extends HDLType> argType = TypeExtension.typeOf(arg);
			if (argType.isPresent()) {
				final HDLPrimitive prim = (HDLPrimitive) argType.get();
				switch (prim.getType()) {
				case INT:
					return Optional.of(prim.setType(HDLPrimitiveType.UINT));
				case INTEGER:
					return Optional.of(HDLPrimitive.getNatural());
				default:
				}
			}
			return argType;
		}
	}

	public static class MinMaxFunction extends HDLDefaultFunctionImpl {

		public MinMaxFunction() {
			super(true, true);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "min":
				return "Returns the smaller of two int values.";
			case "max":
				return "Returns the greater of two int values.";
			}
			return null;
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
			res.put("a", "first value");
			res.put("b", "second value");
			return res;
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "min":
				return "the smaller of two int values.";
			case "max":
				return "the greater of two int values.";
			}
			throw new IllegalArgumentException("I know nothing about this function! " + function.getName());
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { MAX_INT, MAX_UINT, MIN_INT, MIN_UINT };
		}

		@Override
		public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().size() < 2) {
				return Optional.absent();
			}
			final HDLExpression arg0 = call.getParams().get(0);
			final HDLExpression arg1 = call.getParams().get(1);
			final Optional<BigInteger> constVal0 = ConstantEvaluate.valueOf(arg0, context);
			if (!constVal0.isPresent()) {
				return Optional.absent();
			}
			if (arg0.equals(arg1)) {
				return constVal0;
			}
			final Optional<BigInteger> constVal1 = ConstantEvaluate.valueOf(arg1, context);
			if (!constVal1.isPresent()) {
				return Optional.absent();
			}
			switch (call.getFunctionRefName().getLastSegment()) {
			case "min":
				return Optional.of(constVal0.get().min(constVal1.get()));
			case "max":
				return Optional.of(constVal0.get().max(constVal1.get()));
			}
			throw new IllegalArgumentException("I know nothing about this function:" + call);
		}

		@Override
		public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().size() < 2) {
				return Optional.absent();
			}
			final HDLExpression arg0 = call.getParams().get(0);
			final HDLExpression arg1 = call.getParams().get(1);
			final Optional<Range<BigInteger>> zeroArg = RangeExtension.rangeOf(arg0, context);
			if (!zeroArg.isPresent()) {
				return Optional.absent();
			}
			if (arg0.equals(arg1)) {
				return zeroArg;
			}
			final Optional<Range<BigInteger>> oneArg = RangeExtension.rangeOf(arg1, context);
			if (!oneArg.isPresent()) {
				return Optional.absent();
			}
			final Range<BigInteger> zeroRange = zeroArg.get();
			switch (call.getFunctionRefName().getLastSegment()) {
			case "min":
				return asRange(zeroRange.lowerEndpoint().min(oneArg.get().lowerEndpoint()), zeroRange.upperEndpoint().min(oneArg.get().upperEndpoint()));
			case "max":
				return asRange(zeroRange.lowerEndpoint().max(oneArg.get().lowerEndpoint()), zeroRange.upperEndpoint().max(oneArg.get().upperEndpoint()));
			}
			throw new IllegalArgumentException("I know nothing about this function:" + call);
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			final HDLExpression arg0 = call.getParams().get(0);
			final HDLExpression arg1 = call.getParams().get(1);
			final Optional<? extends HDLType> arg0TypeOpt = TypeExtension.typeOf(arg0);
			if (!arg0TypeOpt.isPresent()) {
				return Optional.absent();
			}
			if (arg0.equals(arg1)) {
				return arg0TypeOpt;
			}
			// This should always work as the signature apparently matched
			final HDLPrimitive arg0Type = (HDLPrimitive) arg0TypeOpt.get();
			final Optional<? extends HDLType> arg1TypeOpt = TypeExtension.typeOf(arg1);
			if (!arg1TypeOpt.isPresent()) {
				return Optional.absent();
			}
			final HDLPrimitive arg1Type = (HDLPrimitive) arg1TypeOpt.get();
			final HDLExpression arg1Width = arg1Type.getWidth();
			final HDLExpression arg0Width = arg0Type.getWidth();
			switch (arg0Type.getType()) {
			case INT:
				return createWidthType(call, arg1Type, arg1Width, arg0Width, HDLPrimitive.getInt(), HDLPrimitive.getInteger(), context);
			case UINT:
				return createWidthType(call, arg1Type, arg1Width, arg0Width, HDLPrimitive.getUint(), HDLPrimitive.getNatural(), context);
			case INTEGER:
				return createWidthTypeNoWidth(call, arg1Type, arg1Width, HDLPrimitive.getInt(), HDLPrimitive.getInteger(), context);
			case NATURAL:
				return createWidthTypeNoWidth(call, arg1Type, arg1Width, HDLPrimitive.getUint(), HDLPrimitive.getNatural(), context);
			default:
				return Optional.absent();
			}

		}

		public Optional<? extends HDLType> createWidthTypeNoWidth(HDLFunctionCall call, final HDLPrimitive arg1Type, final HDLExpression arg1Width, HDLPrimitive posType,
				HDLPrimitive posTypeNoWidth, HDLEvaluationContext context) {
			switch (arg1Type.getType()) {
			case INT:
				posType = HDLPrimitive.getInt();
				posTypeNoWidth = HDLPrimitive.getInteger();
				//$FALL-THROUGH$
			case UINT: {
				final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(arg1Width, context);
				if (constVal.isPresent()) {
					if (constVal.get().compareTo(BigInteger.valueOf(32)) <= 0) {
						return Optional.of(posTypeNoWidth);
					}
				}
				return Optional.of(posType.setWidth(arg1Width));
			}
			case INTEGER:
				return Optional.of(HDLPrimitive.getInteger());
			case NATURAL:
				return Optional.of(posTypeNoWidth);
			default:
				throw new IllegalArgumentException("Can not specify call:" + call);
			}
		}

		public Optional<? extends HDLType> createWidthType(HDLFunctionCall call, final HDLPrimitive arg1Type, final HDLExpression arg1Width, final HDLExpression arg0Width,
				HDLPrimitive posType, HDLPrimitive posTypeNoWidth, HDLEvaluationContext context) {
			switch (arg1Type.getType()) {
			case INT:
				if (arg0Width.equals(arg1Width)) {
					return Optional.of(HDLPrimitive.getInt().setWidth(arg0Width));
				}
				return Optional.of(HDLPrimitive.getInt().setWidth(MAX_UINT.getCall(arg0Width, arg1Width)));
			case UINT:
				if (arg0Width.equals(arg1Width)) {
					return Optional.of(HDLPrimitive.getInt().setWidth(arg0Width));
				}
				return Optional.of(posType.setWidth(MAX_UINT.getCall(arg0Width, arg1Width)));
			case INTEGER:
				posType = HDLPrimitive.getInt();
				posTypeNoWidth = HDLPrimitive.getInteger();
				//$FALL-THROUGH$
			case NATURAL: {
				final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(arg0Width, context);
				if (constVal.isPresent()) {
					if (constVal.get().compareTo(BigInteger.valueOf(32)) <= 0) {
						return Optional.of(posTypeNoWidth);
					}
				}
				return Optional.of(posType.setWidth(arg0Width));
			}
			default:
				throw new IllegalArgumentException("Can not specify call:" + call);
			}
		}
	}

	public static class OrdinalFunction extends HDLDefaultFunctionImpl {

		public OrdinalFunction() {
			super(true, true);
		}

		@Override
		public void transform(HDLFunctionCall call, HDLEvaluationContext context, ModificationSet ms) {
			if (call.getParams().isEmpty()) {
				return;
			}
			final HDLExpression enumRef = call.getParams().get(0);
			final HDLEnum enumType = (HDLEnum) TypeExtension.typeOfForced(enumRef, "Insulin");
			final HDLQualifiedName hEnum = FullNameExtension.fullNameOf(enumType);
			HDLSwitchStatement switchStmnt = new HDLSwitchStatement().setCaseExp(enumRef);
			final HDLVariable tmpVar = new HDLVariable().setName(Insulin.getTempName("func", "ordinal"));
			final HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getNatural()).addVariables(tmpVar);
			int pos = 0;
			for (final HDLVariable e : enumType.getEnums()) {
				HDLSwitchCaseStatement caseStatement = new HDLSwitchCaseStatement().setLabel(new HDLEnumRef().setVar(e.asRef()).setHEnum(hEnum));
				caseStatement = caseStatement.addDos(new HDLAssignment().setLeft(tmpVar.asHDLRef()).setRight(HDLLiteral.get(pos)));
				switchStmnt = switchStmnt.addCases(caseStatement);
				pos++;
			}
			switchStmnt = switchStmnt.addCases(new HDLSwitchCaseStatement());
			ms.replace(call, tmpVar.asHDLRef());
			ms.insertBefore(call.getContainer(HDLStatement.class), hvd, switchStmnt);
		}

		@Override
		public Set<Problem> validateCall(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Sets.newHashSet(new Problem(ErrorCode.FUNCTION_NOT_ENOUGH_PARAMETER, call));
			}
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(call.getParams().get(0));
			if (!typeOf.isPresent()) {
				return Sets.newHashSet(new Problem(ErrorCode.UNRESOLVED_TYPE, call));
			}
			if (!(typeOf.get() instanceof HDLEnum)) {
				return Sets.newHashSet(new Problem(ErrorCode.UNSUPPORTED_TYPE_FOR_OP, call));
			}
			return super.validateCall(call, context);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			return "Returns the ordinal of the given enum. The enumeration starts at 0";
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { ORDINAL };
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			return null;
		}

	}

	public static class Log2class extends HDLDefaultFunctionImpl {

		public Log2class() {
			super(true, true);
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "log2ceil":
				return "Returns the minimum number of bits to represent the given number, excluding a sign bit";
			case "log2floor":
				return "Returns the highest bit set, excluding a sign bit";
			}
			return null;
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			final LinkedHashMap<String, String> res = Maps.newLinkedHashMap();
			res.put("a", "the number");
			return res;
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			switch (function.getName()) {
			case "log2ceil":
				return "ceil(log2(abs(number)))";
			case "log2floor":
				return "floor(log2(abs(number)))";
			}
			throw new IllegalArgumentException("I know nothing about this function! " + function.getName());
		}

		@Override
		public HDLFunction[] signatures() {
			return new HDLFunction[] { LOG2CEIL_INT, LOG2CEIL_UINT, LOG2FLOOR_INT, LOG2FLOOR_UINT };
		}

		@Override
		public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Optional.absent();
			}
			final HDLExpression arg = call.getParams().get(0);
			final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(arg, context);
			if (!constVal.isPresent()) {
				return Optional.absent();
			}
			switch (call.getFunctionRefName().getLastSegment()) {
			case "log2ceil":
				return Optional.of(log2ceil(constVal.get()));
			case "log2floor":
				return Optional.of(log2floor(constVal.get()));
			}
			throw new IllegalArgumentException("I know nothing about this function:" + call);
		}

		@Override
		public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context) {
			if (call.getParams().isEmpty()) {
				return Optional.absent();
			}
			final Optional<Range<BigInteger>> zeroArg = RangeExtension.rangeOf(call.getParams().get(0), context);
			if (!zeroArg.isPresent()) {
				return Optional.absent();
			}
			final Range<BigInteger> zeroRange = zeroArg.get();
			switch (call.getFunctionRefName().getLastSegment()) {
			case "log2ceil": {
				final BigInteger from = log2ceil(zeroRange.lowerEndpoint());
				final BigInteger to = log2ceil(zeroRange.upperEndpoint());
				return asRange(from, to);
			}
			case "log2floor": {
				final BigInteger from = log2floor(zeroRange.lowerEndpoint());
				final BigInteger to = log2floor(zeroRange.upperEndpoint());
				return asRange(from, to);
			}
			}
			throw new IllegalArgumentException("I know nothing about this function:" + call);
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.of(HDLPrimitive.getNatural());
		}

	}

	public static final HDLFunction ORDINAL = (HDLFunction) createOrdinal().freeze(null);

	public static HDLNativeFunction createOrdinal() {
		return basicFunc(BuiltInFunctions.ordinal).setReturnType(returnType(Type.PARAM_UINT)).addArgs(param(Type.PARAM_ANY_ENUM, "e"));
	}

	public static final HDLFunction HIGHZ = (HDLFunction) createHighZ().freeze(null);

	public static HDLNativeFunction createHighZ() {
		return basicFunc(BuiltInFunctions.highZ).setReturnType(returnType(Type.PARAM_ANY_BIT));
	}

	public static final HDLFunction HIGHZ_BW = (HDLFunction) createHighZ().freeze(null);

	public static HDLNativeFunction createHighZ_BW() {
		return basicFunc(BuiltInFunctions.highZ).setReturnType(returnType(Type.PARAM_ANY_BIT)).addArgs(param(Type.PARAM_UINT, "width"));
	}

	public static final HDLFunction ASSERT = (HDLFunction) createAssert().freeze(null);

	public static HDLNativeFunction createAssert() {
		return basicFunc(BuiltInFunctions.assertThat)//
				.addArgs(param(Type.PARAM_BOOL, "assumption"))//
				.addArgs(param(Type.PARAM_ENUM, "assert").setEnumSpec(PSHDLLib.ASSERT.asRef()))//
				.addArgs(param(Type.PARAM_STRING, "message"))//
		;
	}

	public static final HDLFunction ABS_UINT = (HDLFunction) createABS(Type.PARAM_ANY_UINT).freeze(null);
	public static final HDLFunction ABS_INT = (HDLFunction) createABS(Type.PARAM_ANY_INT).freeze(null);

	private static HDLFunction createABS(Type type) {
		return basicFunc(BuiltInFunctions.abs).setReturnType(returnType(Type.PARAM_ANY_UINT)).addArgs(param(type, "a"));
	}

	public static final HDLFunction LOG2CEIL_UINT = (HDLFunction) createLog2(Type.PARAM_ANY_UINT, "ceil").freeze(null);
	public static final HDLFunction LOG2CEIL_INT = (HDLFunction) createLog2(Type.PARAM_ANY_INT, "ceil").freeze(null);
	public static final HDLFunction LOG2FLOOR_UINT = (HDLFunction) createLog2(Type.PARAM_ANY_UINT, "floor").freeze(null);
	public static final HDLFunction LOG2FLOOR_INT = (HDLFunction) createLog2(Type.PARAM_ANY_INT, "floor").freeze(null);

	private static HDLFunction createLog2(Type type, String rounding) {
		return new HDLNativeFunction().setSimOnly(false).setName("pshdl." + BuiltInFunctions.valueOf("log2" + rounding).name()).setReturnType(returnType(Type.PARAM_UINT))
				.addArgs(param(type, "a"));
	}

	public static final HDLFunction MIN_UINT = (HDLFunction) createMIN(Type.PARAM_ANY_UINT).freeze(null);
	public static final HDLFunction MIN_INT = (HDLFunction) createMIN(Type.PARAM_ANY_INT).freeze(null);

	private static HDLFunction createMIN(Type type) {
		return basicFunc(BuiltInFunctions.min).setReturnType(returnType(type)).addArgs(param(type, "a")).addArgs(param(type, "b"));
	}

	public static final HDLFunction MAX_INT = (HDLFunction) createMAX(Type.PARAM_ANY_INT).freeze(null);
	public static final HDLFunction MAX_UINT = (HDLFunction) createMAX(Type.PARAM_ANY_UINT).freeze(null);

	private static HDLFunction createMAX(Type type) {
		return basicFunc(BuiltInFunctions.max).setReturnType(returnType(type)).addArgs(param(type, "a")).addArgs(param(type, "b"));
	}

	private static HDLNativeFunction basicFunc(BuiltInFunctions name) {
		return new HDLNativeFunction().setSimOnly(false).setName(name.qfn().toString());
	}

	public static final HDLFunction WIDTH_OF = (HDLFunction) createWidthOf().freeze(null);

	public static HDLNativeFunction createWidthOf() {
		return basicFunc(BuiltInFunctions.widthOf).addArgs(param(Type.PARAM_ANY_BIT, "a")).setReturnType(returnType(Type.PARAM_UINT));
	}

	public static final HDLFunction MSB_OF = (HDLFunction) createMSBOf().freeze(null);

	public static HDLNativeFunction createMSBOf() {
		return basicFunc(BuiltInFunctions.msbOf).addArgs(param(Type.PARAM_ANY_BIT, "a")).setReturnType(returnType(Type.PARAM_UINT));
	}

	public static enum BuiltInFunctions {
		highZ, min, max, abs, log2ceil, log2floor, assertThat, ordinal, printf, widthOf, msbOf;

		public HDLQualifiedName qfn() {
			return HDLQualifiedName.create("pshdl", name());
		}
	}

	public static BuiltInFunctions getFuncEnum(HDLFunctionCall function) {
		final String name = function.getFunctionRefName().getLastSegment();
		final BuiltInFunctions func = BuiltInFunctions.valueOf(name);
		return func;
	}

	public static BigInteger log2ceil(BigInteger le) {
		return BigInteger.valueOf(BigIntegerMath.log2(le, RoundingMode.CEILING));
	}

	public static BigInteger log2floor(BigInteger le) {
		return BigInteger.valueOf(BigIntegerMath.log2(le, RoundingMode.FLOOR));
	}

	public static Optional<Range<BigInteger>> asRange(BigInteger from, BigInteger to) {
		if (from.compareTo(to) > 0) {
			return Optional.of(RangeTool.createRange(to, from));
		}
		return Optional.of(RangeTool.createRange(from, to));
	}

	@Override
	public HDLFunctionImplementation[] getStaticFunctions() {
		return new HDLFunctionImplementation[] { new AbsFunction(), new AssertThat(), new HighZFunction(), new Log2class(), new MinMaxFunction(), new OrdinalFunction(),
				new WidthFunctions() };
	}

	@Override
	public Optional<? extends HDLFunctionImplementation> getFunctionFor(HDLFunctionCall call, HDLQualifiedName fqn) {
		switch (fqn.toString()) {
		case "pshdl.printf":
			return Optional.of(new PrintFunction(call.getParams()));
		}
		return Optional.absent();
	}

	public class PrintFunction extends HDLDefaultFunctionImpl {

		private HDLFunction signature;

		public PrintFunction(Iterable<HDLExpression> arguments) {
			super(true, true);
			HDLFunction sig = basicFunc(BuiltInFunctions.printf);
			int pos = 0;
			for (final HDLExpression hdlExpression : arguments) {
				final HDLVariable name = new HDLVariable().setName("param" + (pos++));
				HDLFunctionParameter param = new HDLFunctionParameter().setConstant(false).setRw(RWType.READ).setName(name);
				final Optional<? extends HDLType> optType = TypeExtension.typeOf(hdlExpression);
				if (!optType.isPresent()) {
					// Handle function references
					return;
				}
				final HDLType type = optType.get();
				if (type instanceof HDLInterface) {
					final HDLInterface hIf = (HDLInterface) type;
					param = param.setType(Type.PARAM_IF).setIfSpec(hIf.asRef());
				} else if (type instanceof HDLPrimitive) {
					final HDLPrimitive primitive = (HDLPrimitive) type;
					switch (primitive.getType()) {
					case ANY_BIT:
						param = param.setType(Type.PARAM_ANY_BIT);
						break;
					case ANY_INT:
						param = param.setType(Type.PARAM_ANY_INT);
						break;
					case ANY_UINT:
						param = param.setType(Type.PARAM_ANY_UINT);
						break;
					case BIT:
						param = param.setType(Type.PARAM_BIT);
						break;
					case BITVECTOR:
						param = param.setType(Type.PARAM_BIT).setWidth(primitive.getWidth());
						break;
					case BOOL:
						param = param.setType(Type.PARAM_BOOL);
						break;
					case INT:
						param = param.setType(Type.PARAM_INT).setWidth(primitive.getWidth());
						break;
					case INTEGER:
						param = param.setType(Type.PARAM_INT);
						break;
					case NATURAL:
						param = param.setType(Type.PARAM_UINT);
						break;
					case STRING:
						param = param.setType(Type.PARAM_STRING);
						break;
					case UINT:
						param = param.setType(Type.PARAM_UINT).setWidth(primitive.getWidth());
						break;
					}
				} else if (type instanceof HDLEnum) {
					final HDLEnum hdlEnum = (HDLEnum) type;
					param = param.setType(Type.PARAM_ENUM).setEnumSpec(hdlEnum.asRef());
				}
				sig = sig.addArgs(param);
			}
			signature = sig;
		}

		@Override
		public String getDocumentation(HDLFunction function) {
			return "Prints the arguments formatted";
		}

		@Override
		public HDLFunction[] signatures() {
			if (signature == null) {
				return new HDLFunction[0];// This should actually never happen
			}
			return new HDLFunction[] { signature };
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.absent();
		}

	}

	@Override
	public HDLQualifiedName[] getDynamicFunctions() {
		return new HDLQualifiedName[] { BuiltInFunctions.printf.qfn() };
	}

}
