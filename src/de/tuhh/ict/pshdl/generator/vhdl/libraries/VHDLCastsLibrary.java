package de.tuhh.ict.pshdl.generator.vhdl.libraries;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
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
	public static final FunctionDeclaration RESIZE_INT = new FunctionDeclaration("resizeInt", NumericStd.SIGNED, new Constant("s", StdLogic1164.STD_LOGIC), new Constant("newSize",
			Standard.NATURAL));
	private static final Map<FunctionCallMap, FunctionCallMap> castMap = new HashMap<VHDLCastsLibrary.FunctionCallMap, VHDLCastsLibrary.FunctionCallMap>();
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
		putCastMap(HDLPrimitiveType.INT, HDLPrimitiveType.UINT, null);
		putCastMap(HDLPrimitiveType.INT, HDLPrimitiveType.BITVECTOR, null);
		putCastMap(HDLPrimitiveType.INT, HDLPrimitiveType.INTEGER, NumericStd.TO_INTEGER);
		putCastMap(HDLPrimitiveType.UINT, HDLPrimitiveType.INT, null);
		putCastMap(HDLPrimitiveType.UINT, HDLPrimitiveType.BITVECTOR, null);

		declarations.add(STR_TO_UNSIGNED);
		// declarations.add(STR_TO_SIGNED);
		declarations.add(ACCESS_BITS_SLV);
		declarations.add(ACCESS_BITS_SL);
		declarations.add(ACCESS_BITS_NAT);
		declarations.add(ACCESS_BITS_INT);
		declarations.add(ACCESS_BITS_UNSIGNED);
		declarations.add(ACCESS_BITS_SIGNED);
		declarations.add(RESIZE_SLV);
		declarations.add(MAX);
		declarations.add(MIN);
		declarations.add(ABS);
	}

	private static void putCastMap(HDLPrimitiveType left, HDLPrimitiveType right, FunctionDeclaration call) {
		FunctionCallMap callMap = new FunctionCallMap(left, right, call);
		castMap.put(callMap, callMap);
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
			return StdLogic1164.STD_LOGIC_VECTOR(range.toVHDL(Direction.DOWNTO));
		case INT:
			if (range == null)
				throw new IllegalArgumentException("Can not have null width");
			return NumericStd.SIGNED(range.toVHDL(Direction.DOWNTO));
		case UINT:
			if (range == null)
				throw new IllegalArgumentException("Can not have null width");
			return NumericStd.UNSIGNED(range.toVHDL(Direction.DOWNTO));
		case INTEGER:
			return Standard.INTEGER;
		case NATURAL:
			return Standard.NATURAL;
		case STRING:
			return Standard.STRING;
		}
		throw new IllegalArgumentException("Unexpected Type:" + left);
	}

	private static class FunctionCallMap {
		public final HDLPrimitiveType from;
		public final HDLPrimitiveType to;
		public final FunctionDeclaration call;

		public FunctionCallMap(HDLPrimitiveType from, HDLPrimitiveType to, FunctionDeclaration call) {
			super();
			this.from = from;
			this.to = to;
			this.call = call;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((from == null) ? 0 : from.hashCode());
			result = (prime * result) + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FunctionCallMap other = (FunctionCallMap) obj;
			if (from != other.from)
				return false;
			if (to != other.to)
				return false;
			return true;
		}
	}

	public static Expression<?> cast(Expression<?> vhdlExpr, HDLPrimitiveType from, HDLPrimitiveType to) {
		if (from.equals(to))
			return vhdlExpr;
		FunctionCallMap callMap = castMap.get(new FunctionCallMap(from, to, null));
		String name = getCastName(from, to);
		Function resolve = PACKAGE.getScope().resolve(name, Function.class);
		FunctionCall call = new FunctionCall(resolve);
		if (callMap != null) {
			if (callMap.call == null) {
				return new TypeConversion(getType(to), vhdlExpr);
			}
			call = new FunctionCall(callMap.call);
		}
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
