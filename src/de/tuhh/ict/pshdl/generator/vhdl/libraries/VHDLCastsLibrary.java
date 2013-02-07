package de.tuhh.ict.pshdl.generator.vhdl.libraries;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLLiteral.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.type.*;

public class VHDLCastsLibrary {
	// public static final FunctionDeclaration STR_TO_SIGNED = new
	// FunctionDeclaration("strToSigned", NumericStd.SIGNED, new Constant("s",
	// Standard.STRING));
	public static final FunctionDeclaration MAX = new FunctionDeclaration("max", Standard.INTEGER, new Constant("left", Standard.INTEGER), new Constant("right", Standard.INTEGER));
	public static final FunctionDeclaration MIN = new FunctionDeclaration("min", Standard.INTEGER, new Constant("left", Standard.INTEGER), new Constant("right", Standard.INTEGER));
	public static final FunctionDeclaration ABS = new FunctionDeclaration("abs", Standard.INTEGER, new Constant("left", Standard.INTEGER));
	public static final FunctionDeclaration STR_TO_UNSIGNED = new FunctionDeclaration("strToUnsigned", NumericStd.UNSIGNED, new Constant("s", Standard.STRING));
	public static final FunctionDeclaration ACCESS_BITS_SLV = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR,
			new Constant("s", StdLogic1164.STD_LOGIC_VECTOR), new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final FunctionDeclaration ACCESS_BITS_SL = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", StdLogic1164.STD_LOGIC),
			new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final FunctionDeclaration ACCESS_BITS_NAT = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", Standard.NATURAL),
			new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final FunctionDeclaration ACCESS_BITS_INT = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", Standard.INTEGER),
			new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final FunctionDeclaration ACCESS_BITS_UNSIGNED = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", NumericStd.UNSIGNED),
			new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final FunctionDeclaration ACCESS_BITS_SIGNED = new FunctionDeclaration("accessBits", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", NumericStd.SIGNED),
			new Constant("beginRange", Standard.NATURAL), new Constant("endRange", Standard.NATURAL));
	public static final UseClause USE_CLAUSE = new UseClause("pshdl.Casts.ALL");
	public static final PackageDeclaration PACKAGE;
	public static final FunctionDeclaration RESIZE_SLV = new FunctionDeclaration("resizeSLV", StdLogic1164.STD_LOGIC_VECTOR, new Constant("s", StdLogic1164.STD_LOGIC_VECTOR),
			new Constant("newSize", Standard.NATURAL));
	public static final FunctionDeclaration RESIZE_BIT = new FunctionDeclaration("resizeBit", StdLogic1164.STD_LOGIC, new Constant("s", StdLogic1164.STD_LOGIC), new Constant(
			"newSize", Standard.NATURAL));
	public static final FunctionDeclaration RESIZE_INT = new FunctionDeclaration("resizeInt", NumericStd.SIGNED, new Constant("s", NumericStd.SIGNED), new Constant("newSize",
			Standard.NATURAL));
	public static final FunctionDeclaration RESIZE_UINT = new FunctionDeclaration("resizeUint", NumericStd.UNSIGNED, new Constant("s", NumericStd.UNSIGNED), new Constant(
			"newSize", Standard.NATURAL));
	public static final FunctionDeclaration RESIZE_INTEGER = new FunctionDeclaration("resizeInteger", NumericStd.SIGNED, new Constant("s", Standard.INTEGER), new Constant(
			"newSize", Standard.NATURAL));
	public static final FunctionDeclaration RESIZE_NATURAL = new FunctionDeclaration("resizeNatural", NumericStd.UNSIGNED, new Constant("s", Standard.NATURAL), new Constant(
			"newSize", Standard.NATURAL));
	static {
		PACKAGE = new PackageDeclaration("pshdl.Casts");
		List<PackageDeclarativeItem> declarations = PACKAGE.getDeclarations();
		HDLPrimitiveType[] values = HDLPrimitiveType.values();
		for (int i = 0; i < values.length; i++) {
			HDLPrimitiveType left = values[i];
			for (int j = 0; j < values.length; j++) {
				HDLPrimitiveType right = values[j];
				if (right == left) {
					continue;
				}
				String name = getCastName(left, right);
				SubtypeIndication rt = getType(right);
				SubtypeIndication lt = getType(left);
				if ((lt != null) && (rt != null)) {
					FunctionDeclaration fd = new FunctionDeclaration(name, rt, new Constant("arg", lt));
					declarations.add(fd);
				}
			}
		}

		declarations.add(STR_TO_UNSIGNED);
		// declarations.add(STR_TO_SIGNED);
		declarations.add(ACCESS_BITS_SLV);
		declarations.add(ACCESS_BITS_SL);
		declarations.add(ACCESS_BITS_NAT);
		declarations.add(ACCESS_BITS_INT);
		declarations.add(ACCESS_BITS_UNSIGNED);
		declarations.add(ACCESS_BITS_SIGNED);
		declarations.add(RESIZE_SLV);
		declarations.add(RESIZE_BIT);
		declarations.add(RESIZE_INT);
		declarations.add(RESIZE_UINT);
		declarations.add(RESIZE_INTEGER);
		declarations.add(RESIZE_NATURAL);
		declarations.add(MAX);
		declarations.add(MIN);
		declarations.add(ABS);
	}

	public static SubtypeIndication getType(HDLPrimitiveType left) {
		switch (left) {
		case BOOL:
		case BIT:
			return StdLogic1164.STD_LOGIC;
		case BITVECTOR:
			return StdLogic1164.STD_LOGIC_VECTOR;
		case INT:
			return NumericStd.SIGNED;
		case UINT:
			return NumericStd.UNSIGNED;
		case INTEGER:
			return Standard.INTEGER;
		case NATURAL:
			return Standard.NATURAL;
		case STRING:
			return Standard.STRING;
		}
		throw new IllegalArgumentException("Unexpected Type:" + left);
	}

	public static SubtypeIndication getType(HDLPrimitive left) {
		HDLExpression width = left.getWidth();
		HDLRange range = null;
		if (width != null) {
			range = new HDLRange().setFrom(new HDLArithOp().setLeft(width).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1))).setTo(HDLLiteral.get(0));
			range = range.copyDeepFrozen(left);
		}
		switch (left.getType()) {
		case BOOL:
			return Standard.BOOLEAN;
		case BIT:
			return StdLogic1164.STD_LOGIC;
		case BITVECTOR:
			if (range == null)
				throw new IllegalArgumentException("Can not have null width");
			return StdLogic1164.STD_LOGIC_VECTOR(VHDLExpressionExtension.INST.toVHDL(range, Direction.DOWNTO));
		case INT:
			if (range == null)
				throw new IllegalArgumentException("Can not have null width");
			return NumericStd.SIGNED(VHDLExpressionExtension.INST.toVHDL(range, Direction.DOWNTO));
		case UINT:
			if (range == null)
				throw new IllegalArgumentException("Can not have null width");
			return NumericStd.UNSIGNED(VHDLExpressionExtension.INST.toVHDL(range, Direction.DOWNTO));
		case INTEGER:
			return Standard.INTEGER;
		case NATURAL:
			return Standard.NATURAL;
		case STRING:
			return Standard.STRING;
		}
		throw new IllegalArgumentException("Unexpected Type:" + left);
	}

	public static class TargetType {
		public final Expression<?> resized;
		public final HDLPrimitiveType newType;

		public TargetType(Expression<?> resized, HDLPrimitiveType newType) {
			super();
			this.resized = resized;
			this.newType = newType;
		}
	}

	public static TargetType getResize(Expression<?> exp, HDLPrimitive actualType, HDLExpression tWidth) {
		if (actualType.getWidth() != null) {
			BigInteger bt = ConstantEvaluate.valueOf(actualType.getWidth(), null);
			if (bt != null) {
				BigInteger btw = ConstantEvaluate.valueOf(tWidth, null);
				if (bt.equals(btw)) {
					return new TargetType(exp, actualType.getType());
				}
			}
		}
		Expression<?> width = VHDLExpressionExtension.vhdlOf(tWidth);
		FunctionCall resize = null;
		HDLPrimitiveType resType = actualType.getType();
		switch (actualType.getType()) {
		case BOOL:
		case STRING:
			throw new IllegalArgumentException(actualType + " can't have a width.");
		case BIT:
			resize = new FunctionCall(VHDLCastsLibrary.RESIZE_BIT);
			resType = HDLPrimitiveType.BITVECTOR;
			break;
		case NATURAL:
			resize = new FunctionCall(VHDLCastsLibrary.RESIZE_NATURAL);
			resType = HDLPrimitiveType.UINT;
			break;
		case INTEGER:
			resize = new FunctionCall(VHDLCastsLibrary.RESIZE_INTEGER);
			resType = HDLPrimitiveType.INT;
			break;
		case INT:
			resize = new FunctionCall(RESIZE_INT);
			break;
		case UINT:
			resize = new FunctionCall(RESIZE_UINT);
			break;
		case BITVECTOR:
			resize = new FunctionCall(VHDLCastsLibrary.RESIZE_SLV);
			break;
		}
		if (resize == null)
			throw new IllegalArgumentException("Should not happen");
		resize.getParameters().add(new AssociationElement(exp));
		resize.getParameters().add(new AssociationElement(width));
		return new TargetType(resize, resType);
	}

	public static Expression<?> handleLiteral(IHDLObject container, HDLLiteral lit, HDLPrimitive targetType, HDLExpression tWidth) {
		if ((container != null) && (container.getClassType() == HDLClass.HDLArithOp))
			return VHDLExpressionExtension.vhdlOf(lit);
		BigInteger val = lit.getValueAsBigInt();
		BigInteger width = null;
		if (tWidth != null)
			width = ConstantEvaluate.valueOf(tWidth, null);
		switch (targetType.getType()) {
		case BIT:
			if (BigInteger.ZERO.equals(val))
				return new CharacterLiteral('0');
			return new CharacterLiteral('1');
		case NATURAL:
		case INTEGER:
			return VHDLExpressionExtension.vhdlOf(lit);
		case INT:
			return handleIntUint(container, tWidth, lit, val, width, HDLPrimitiveType.INT, NumericStd.TO_SIGNED, RESIZE_INTEGER);
		case UINT:
			return handleIntUint(container, tWidth, lit, val, width, HDLPrimitiveType.UINT, NumericStd.TO_UNSIGNED, RESIZE_NATURAL);
		case BITVECTOR:
			if (BigInteger.ZERO.equals(val) && (container != null) && (container.getClassType() == HDLClass.HDLAssignment))
				return Aggregate.OTHERS(new CharacterLiteral('0'));
			if (width != null)
				return VHDLExpressionExtension.INST.toVHDL(lit, width.intValue(), true);
			FunctionCall functionCall = new FunctionCall(VHDLCastsLibrary.RESIZE_SLV);
			functionCall.getParameters().add(new AssociationElement(VHDLExpressionExtension.INST.toVHDL(lit, val.bitLength(), true)));
			functionCall.getParameters().add(new AssociationElement(VHDLExpressionExtension.vhdlOf(tWidth)));
			return functionCall;
		case STRING:
			if (lit.getPresentation() == HDLLiteralPresentation.STR)
				return VHDLExpressionExtension.vhdlOf(lit);
			throw new IllegalArgumentException("String is not castable");
		case BOOL:
			if (lit.getPresentation() == HDLLiteralPresentation.BOOL)
				return VHDLExpressionExtension.vhdlOf(lit);
			if (BigInteger.ZERO.equals(val))
				return VHDLExpressionExtension.vhdlOf(HDLLiteral.getFalse());
			return VHDLExpressionExtension.vhdlOf(HDLLiteral.getTrue());
		}
		throw new IllegalArgumentException("Should not get here");
	}

	private static Expression<?> handleIntUint(IHDLObject container, HDLExpression tWidth, HDLLiteral lit, BigInteger val, BigInteger width, HDLPrimitiveType to,
			Function castFunc, FunctionDeclaration resize) {
		if (BigInteger.ZERO.equals(val) && (container != null) && (container.getClassType() == HDLClass.HDLAssignment))
			return Aggregate.OTHERS(new CharacterLiteral('0'));
		if ((width != null) && (lit.getPresentation() != HDLLiteralPresentation.NUM))
			return VHDLCastsLibrary.cast(VHDLExpressionExtension.INST.toVHDL(lit, width.intValue(), true), HDLPrimitiveType.BITVECTOR, to);
		if (val.bitLength() > 31) {
			if (width != null)
				return VHDLCastsLibrary.cast(VHDLExpressionExtension.INST.toVHDL(lit, width.intValue(), true), HDLPrimitiveType.BITVECTOR, to);
			FunctionCall functionCall = new FunctionCall(resize);
			functionCall.getParameters().add(
					new AssociationElement(VHDLCastsLibrary.cast(VHDLExpressionExtension.INST.toVHDL(lit, val.bitLength(), true), HDLPrimitiveType.BITVECTOR, to)));
			functionCall.getParameters().add(new AssociationElement(VHDLExpressionExtension.vhdlOf(tWidth)));
			return functionCall;
		}
		FunctionCall functionCall = new FunctionCall(castFunc);
		functionCall.getParameters().add(new AssociationElement(VHDLExpressionExtension.vhdlOf(lit)));
		functionCall.getParameters().add(new AssociationElement(VHDLExpressionExtension.vhdlOf(tWidth)));
		return functionCall;
	}

	public static Expression<?> cast(Expression<?> vhdlExpr, HDLPrimitiveType from, HDLPrimitiveType to) {
		if (from.equals(to))
			return vhdlExpr;
		String name = getCastName(from, to);
		Function resolve = PACKAGE.getScope().resolve(name, Function.class);
		FunctionCall call = new FunctionCall(resolve);
		call.getParameters().add(new AssociationElement(vhdlExpr));
		return call;
	}

	private static String getCastName(HDLPrimitiveType from, HDLPrimitiveType to) {
		String rightName = to.name().charAt(0) + to.name().substring(1).toLowerCase();
		String name = from.name().toLowerCase() + "To" + rightName;
		return name;
	}

	private VHDLCastsLibrary() {

	}
}
