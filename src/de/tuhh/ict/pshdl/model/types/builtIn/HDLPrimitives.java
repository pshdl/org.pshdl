package de.tuhh.ict.pshdl.model.types.builtIn;

import static de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType.*;
import static de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType.*;

import java.io.*;
import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLPrimitives {

	private static HDLPrimitives hdlPrimitives;

	public static class HDLInferenceTriple {
		public HDLPrimitiveType left, right, result;

		public HDLInferenceTriple(HDLPrimitiveType left, HDLPrimitiveType right, HDLPrimitiveType result) {
			super();
			this.left = left;
			this.right = right;
			this.result = result;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((left == null) ? 0 : left.hashCode());
			result = (prime * result) + ((right == null) ? 0 : right.hashCode());
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
			HDLInferenceTriple other = (HDLInferenceTriple) obj;
			if (left != other.left)
				return false;
			if (right != other.right)
				return false;
			return true;
		}
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> arithResolutionTable = initArithResolution();
	private static Map<HDLInferenceTriple, HDLInferenceTriple> shiftResolutionTable = initShiftResolution();
	private static Map<HDLInferenceTriple, HDLInferenceTriple> bitResolutionTable = initBitResolution();
	private static Map<HDLInferenceTriple, HDLInferenceTriple> equalityResolutionTable = initEqualityResolution();

	private HDLPrimitives() {

	}

	public static HDLPrimitives getInstance() {
		if (hdlPrimitives == null)
			hdlPrimitives = new HDLPrimitives();
		return hdlPrimitives;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initEqualityResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = initArithResolution();
		for (HDLInferenceTriple result : res.values()) {
			result.result = BOOL;
		}
		for (HDLPrimitiveType right : HDLPrimitiveType.values()) {
			if (right == BOOL)
				continue;
			res.put(new HDLInferenceTriple(BIT, right, null), new HDLInferenceTriple(BITVECTOR, BITVECTOR, BOOL));
			res.put(new HDLInferenceTriple(BITVECTOR, right, null), new HDLInferenceTriple(BITVECTOR, BITVECTOR, BOOL));
		}
		res.put(new HDLInferenceTriple(BOOL, BOOL, null), new HDLInferenceTriple(BOOL, BOOL, BOOL));
		res.put(new HDLInferenceTriple(BIT, BIT, null), new HDLInferenceTriple(BIT, BIT, BOOL));
		return res;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initBitResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = new HashMap<HDLInferenceTriple, HDLInferenceTriple>();
		for (HDLPrimitiveType left : HDLPrimitiveType.values()) {
			if (left == BOOL)
				continue;
			for (HDLPrimitiveType right : HDLPrimitiveType.values()) {
				if (right == BOOL)
					continue;
				res.put(new HDLInferenceTriple(left, right, null), new HDLInferenceTriple(left, left, left));
			}
		}
		res.put(new HDLInferenceTriple(BIT, BIT, null), new HDLInferenceTriple(BIT, BIT, BIT));
		return res;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initArithResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = new HashMap<HDLInferenceTriple, HDLInferenceTriple>();
		res.put(new HDLInferenceTriple(INT, INT, null), new HDLInferenceTriple(INT, INT, INT));
		res.put(new HDLInferenceTriple(INT, UINT, null), new HDLInferenceTriple(INT, INT, INT));
		res.put(new HDLInferenceTriple(INT, INTEGER, null), new HDLInferenceTriple(INT, INTEGER, INT));
		res.put(new HDLInferenceTriple(INT, NATURAL, null), new HDLInferenceTriple(INT, INTEGER, INT));

		res.put(new HDLInferenceTriple(INTEGER, INT, null), new HDLInferenceTriple(INTEGER, INT, INT));
		res.put(new HDLInferenceTriple(INTEGER, UINT, null), new HDLInferenceTriple(INTEGER, INT, INT));
		res.put(new HDLInferenceTriple(INTEGER, INTEGER, null), new HDLInferenceTriple(INTEGER, INTEGER, INTEGER));
		res.put(new HDLInferenceTriple(INTEGER, NATURAL, null), new HDLInferenceTriple(INTEGER, INTEGER, INTEGER));

		res.put(new HDLInferenceTriple(UINT, INT, null), new HDLInferenceTriple(INT, INT, INT));
		res.put(new HDLInferenceTriple(UINT, INTEGER, null), new HDLInferenceTriple(INT, INTEGER, INT));
		res.put(new HDLInferenceTriple(UINT, UINT, null), new HDLInferenceTriple(UINT, UINT, UINT));
		res.put(new HDLInferenceTriple(UINT, NATURAL, null), new HDLInferenceTriple(UINT, NATURAL, UINT));

		res.put(new HDLInferenceTriple(NATURAL, INT, null), new HDLInferenceTriple(INTEGER, INT, INT));
		res.put(new HDLInferenceTriple(NATURAL, INTEGER, null), new HDLInferenceTriple(INTEGER, INTEGER, INTEGER));
		res.put(new HDLInferenceTriple(NATURAL, UINT, null), new HDLInferenceTriple(NATURAL, UINT, UINT));
		res.put(new HDLInferenceTriple(NATURAL, NATURAL, null), new HDLInferenceTriple(NATURAL, NATURAL, NATURAL));
		return res;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initShiftResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = new HashMap<HDLInferenceTriple, HDLInferenceTriple>();
		res.put(new HDLInferenceTriple(INT, INT, null), new HDLInferenceTriple(INT, NATURAL, INT));
		res.put(new HDLInferenceTriple(INT, UINT, null), new HDLInferenceTriple(INT, NATURAL, INT));
		res.put(new HDLInferenceTriple(INT, INTEGER, null), new HDLInferenceTriple(INT, NATURAL, INT));
		res.put(new HDLInferenceTriple(INT, NATURAL, null), new HDLInferenceTriple(INT, NATURAL, INT));

		res.put(new HDLInferenceTriple(INTEGER, INT, null), new HDLInferenceTriple(INTEGER, NATURAL, INTEGER));
		res.put(new HDLInferenceTriple(INTEGER, UINT, null), new HDLInferenceTriple(INTEGER, NATURAL, INTEGER));
		res.put(new HDLInferenceTriple(INTEGER, INTEGER, null), new HDLInferenceTriple(INTEGER, NATURAL, INTEGER));
		res.put(new HDLInferenceTriple(INTEGER, NATURAL, null), new HDLInferenceTriple(INTEGER, NATURAL, INTEGER));

		res.put(new HDLInferenceTriple(UINT, INT, null), new HDLInferenceTriple(UINT, NATURAL, UINT));
		res.put(new HDLInferenceTriple(UINT, INTEGER, null), new HDLInferenceTriple(UINT, NATURAL, UINT));
		res.put(new HDLInferenceTriple(UINT, UINT, null), new HDLInferenceTriple(UINT, NATURAL, UINT));
		res.put(new HDLInferenceTriple(UINT, NATURAL, null), new HDLInferenceTriple(UINT, NATURAL, UINT));

		res.put(new HDLInferenceTriple(NATURAL, INT, null), new HDLInferenceTriple(NATURAL, NATURAL, NATURAL));
		res.put(new HDLInferenceTriple(NATURAL, INTEGER, null), new HDLInferenceTriple(NATURAL, NATURAL, NATURAL));
		res.put(new HDLInferenceTriple(NATURAL, UINT, null), new HDLInferenceTriple(NATURAL, NATURAL, NATURAL));
		res.put(new HDLInferenceTriple(NATURAL, NATURAL, null), new HDLInferenceTriple(NATURAL, NATURAL, NATURAL));

		res.put(new HDLInferenceTriple(BIT, INT, null), new HDLInferenceTriple(BIT, NATURAL, BIT));
		res.put(new HDLInferenceTriple(BIT, INTEGER, null), new HDLInferenceTriple(BIT, NATURAL, BIT));
		res.put(new HDLInferenceTriple(BIT, UINT, null), new HDLInferenceTriple(BIT, NATURAL, BIT));
		res.put(new HDLInferenceTriple(BIT, NATURAL, null), new HDLInferenceTriple(BIT, NATURAL, BIT));

		res.put(new HDLInferenceTriple(BITVECTOR, INT, null), new HDLInferenceTriple(BITVECTOR, NATURAL, BITVECTOR));
		res.put(new HDLInferenceTriple(BITVECTOR, INTEGER, null), new HDLInferenceTriple(BITVECTOR, NATURAL, BITVECTOR));
		res.put(new HDLInferenceTriple(BITVECTOR, UINT, null), new HDLInferenceTriple(BITVECTOR, NATURAL, BITVECTOR));
		res.put(new HDLInferenceTriple(BITVECTOR, NATURAL, null), new HDLInferenceTriple(BITVECTOR, NATURAL, BITVECTOR));
		return res;
	}

	public static void main(String[] args) {
		System.out.println(intRange(BigInteger.valueOf(0)));
		System.out.println(intRange(BigInteger.valueOf(1)));
		System.out.println(intRange(BigInteger.valueOf(16)));
		System.out.println(intRange(BigInteger.valueOf(9)));
		System.out.println(intRange(BigInteger.valueOf(32)));
		System.out.println("IntMin:" + Integer.MIN_VALUE + " IntMax:" + Integer.MAX_VALUE);
		System.out.println(uintRange(BigInteger.valueOf(0)));
		System.out.println(uintRange(BigInteger.valueOf(1)));
		System.out.println(uintRange(BigInteger.valueOf(16)));
		System.out.println(uintRange(BigInteger.valueOf(9)));
		System.out.println(uintRange(BigInteger.valueOf(32)));
		try {
			StringBuilder sb = new StringBuilder("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"cast.css\" /></head><body>\n");
			sb.append("<p>Arithmetic operation conversion/result table</p>");
			printTable(sb, arithResolutionTable);
			sb.append("<p>Shift operation conversion/result table</p>");
			printTable(sb, shiftResolutionTable);
			sb.append("<p>Equality operation conversion/result table</p>");
			printTable(sb, equalityResolutionTable);
			sb.append("<p>Bit operation conversion/result table</p>");
			printTable(sb, bitResolutionTable);
			sb.append("</body></html>");
			System.out.println(sb);
			FileOutputStream fos = new FileOutputStream("castTable.html");
			fos.write(sb.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printTable(StringBuilder sb, Map<HDLInferenceTriple, HDLInferenceTriple> map) {
		sb.append("<table>\n");
		sb.append("<tr><td>left \\ right</td>");
		for (HDLPrimitiveType left : HDLPrimitiveType.values()) {
			sb.append("<td colspan='3'>").append(left.name().toLowerCase()).append("</td>");
		}
		sb.append("</tr>");
		for (HDLPrimitiveType left : HDLPrimitiveType.values()) {
			sb.append("\n<tr><td>").append(left.name().toLowerCase()).append("</td>");
			for (HDLPrimitiveType right : HDLPrimitiveType.values()) {
				HDLInferenceTriple triple = map.get(new HDLInferenceTriple(left, right, null));
				if (triple != null) {
					sb.append("<td>");
					if (triple.left != left)
						sb.append("<span class='cast'>").append(triple.left.name().toLowerCase()).append("</span>");
					else
						sb.append(left.name().toLowerCase());
					sb.append("</td><td>");
					if (triple.right != right)
						sb.append("<span class='cast'>").append(triple.right.name().toLowerCase()).append("</span>");
					else
						sb.append(right.name().toLowerCase());
					sb.append("</td><td>");
					sb.append(triple.result.name().toLowerCase());
					sb.append("</td>");
				} else {
					sb.append("<td colspan='3'></td>");
				}
			}
			sb.append("</tr>");
		}
		sb.append("\n</table>");
	}

	public HDLTypeInferenceInfo getArithOpType(HDLArithOp op) {
		HDLPrimitive lType = (HDLPrimitive) op.getLeft().determineType();
		HDLPrimitive rType = (HDLPrimitive) op.getRight().determineType();
		HDLArithOpType type = op.getType();
		if (HDLPrimitive.isTargetMatching(lType)) {
			if (HDLPrimitive.isTargetMatching(rType))
				lType = rType;
		}
		if (HDLPrimitive.isTargetMatching(rType))
			rType = lType;
		HDLInferenceTriple triple = arithResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(null, lType.setType(triple.left), rType.setType(triple.right));
		HDLExpression width = simplifyWidth(op, getWidth(type, info));
		if (width != null)
			width = width.copy();
		info.result = new HDLPrimitive().setType(triple.result).setWidth(width);
		return normalize(info, op);
	}

	public static HDLExpression simplifyWidth(HDLObject container, HDLExpression width) {
		if (width == null)
			return null;
		if (width.getContainer() == null)
			width.setContainer(container);
		BigInteger newW = width.constantEvaluate(null);
		if (newW != null)
			width = new HDLLiteral().setVal(newW.toString());
		return width;
	}

	private HDLTypeInferenceInfo normalize(HDLTypeInferenceInfo info, HDLExpression op) {
		if (info.result instanceof HDLPrimitive) {
			HDLPrimitive result = (HDLPrimitive) info.result;
			if (result.getWidth() != null) {
				switch (result.getType()) {
				case BIT:
					result = result.setType(BITVECTOR);
					break;
				case INTEGER:
					result = result.setType(INT);
					break;
				case NATURAL:
					result = result.setType(UINT);
					break;
				default:
				}
			} else {
				switch (result.getType()) {
				case BITVECTOR:
					result = result.setType(BIT);
					break;
				case INT:
					result = result.setType(INTEGER);
					break;
				case UINT:
					result = result.setType(NATURAL);
					break;
				default:
				}
			}
			info.result = result.setContainer(op);
		}
		return info;
	}

	private HDLExpression getWidth(HDLArithOpType type, HDLTypeInferenceInfo info) {
		HDLExpression leftW = ((HDLPrimitive) info.args[0]).getWidth();
		if (leftW != null)
			leftW = leftW.copyFiltered(CopyFilter.DEEP);
		HDLExpression rightW = ((HDLPrimitive) info.args[1]).getWidth();
		if (rightW != null)
			rightW = rightW.copyFiltered(CopyFilter.DEEP);
		switch (type) {
		case POW:
			// The result type of pow can only be natural
			return null;
		case DIV:
			if ((leftW == null) && (rightW == null))
				return null;
			if ((leftW != null) && (rightW == null))
				return leftW;
			if ((leftW == null) && (rightW != null))
				return rightW;
			return leftW;
		case MOD:
			if ((leftW == null) && (rightW == null))
				return null;
			if ((leftW != null) && (rightW == null))
				return leftW;
			if ((leftW == null) && (rightW != null))
				return rightW;
			return rightW;
		case MINUS:
		case PLUS:
			if ((leftW == null) && (rightW == null))
				return null;
			if ((leftW != null) && (rightW == null))
				return leftW;
			if ((leftW == null) && (rightW != null))
				return rightW;
			HDLFunction max = new HDLFunction().setName("max").addParams(leftW).addParams(rightW);
			return max;
			// return new HDLArithOp().setLeft(max).setType(PLUS).setRight(new
			// HDLLiteral().setVal("1"));
		case MUL:
			if ((leftW == null) && (rightW == null))
				return null;
			if ((leftW != null) && (rightW == null))
				return new HDLArithOp().setLeft(leftW).setType(PLUS).setRight(leftW);
			if ((leftW == null) && (rightW != null))
				return new HDLArithOp().setLeft(rightW).setType(PLUS).setRight(rightW);
			return new HDLArithOp().setLeft(leftW).setType(PLUS).setRight(rightW);
		}

		return null;
	}

	public HDLTypeInferenceInfo getShiftOpType(HDLShiftOp op) {
		HDLPrimitive lType = (HDLPrimitive) op.getLeft().determineType();
		HDLPrimitive rType = (HDLPrimitive) op.getRight().determineType();
		if (HDLPrimitive.isTargetMatching(lType)) {
			if (HDLPrimitive.isTargetMatching(rType))
				lType = rType;
		}
		if (HDLPrimitive.isTargetMatching(rType))
			rType = lType;
		HDLInferenceTriple triple = shiftResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + op.getType() + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}
		HDLExpression width = lType.getWidth();
		if (width != null)
			width = width.copy();
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result).setWidth(width), lType, new HDLPrimitive().setType(triple.right));
		return normalize(info, op);
	}

	public HDLTypeInferenceInfo getEqualityOpType(HDLEqualityOp op) {
		HDLType determineTypeL = op.getLeft().determineType();
		HDLType determineTypeR = op.getRight().determineType();
		if ((determineTypeL instanceof HDLPrimitive) && (determineTypeR instanceof HDLPrimitive)) {
			HDLPrimitive lType = (HDLPrimitive) determineTypeL;
			HDLPrimitive rType = (HDLPrimitive) determineTypeR;
			if (HDLPrimitive.isTargetMatching(lType)) {
				if (HDLPrimitive.isTargetMatching(rType))
					lType = rType;
			}
			if (HDLPrimitive.isTargetMatching(rType))
				rType = lType;
			HDLInferenceTriple triple = equalityResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
			if (triple == null) {
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
				hdi.error = "The operation " + op.getType() + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
				return hdi;
			}
			HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result), lType.setType(triple.left), lType.setType(triple.right));
			return normalize(info, op);
		}
		return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), determineTypeL, determineTypeR);
	}

	public HDLTypeInferenceInfo getBitOpType(HDLBitOp op) {
		HDLPrimitive lType = (HDLPrimitive) op.getLeft().determineType();
		HDLPrimitive rType = (HDLPrimitive) op.getRight().determineType();
		HDLBitOpType type = op.getType();
		if (HDLPrimitive.isTargetMatching(lType)) {
			if (HDLPrimitive.isTargetMatching(rType))
				lType = rType;
		}
		if (HDLPrimitive.isTargetMatching(rType))
			rType = lType;
		if ((type == HDLBitOpType.LOGI_AND) || (type == HDLBitOpType.LOGI_OR))
			return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool(), HDLPrimitive.getBool());
		HDLInferenceTriple triple = bitResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}
		HDLExpression width = lType.getWidth();
		if ((width == null) && (rType.getWidth() != null))
			width = rType.getWidth();
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result).setWidth(width == null ? null : width.copy()), lType.setType(triple.left),
				lType.setType(triple.right));
		return normalize(info, op);
	}

	public HDLTypeInferenceInfo getManipOpType(HDLManip manip) {
		HDLExpression target = manip.getTarget();
		HDLType castTo = manip.getCastTo();
		HDLPrimitive determineType = (HDLPrimitive) target.determineType();
		switch (manip.getType()) {
		case CAST:
			// XXX If there ever happens to be another cast, this has to be
			// updated
			return new HDLTypeInferenceInfo((HDLPrimitive) castTo, determineType);
		case ARITH_NEG:
			switch (determineType.getType()) {
			case INT:
			case INTEGER:
				return new HDLTypeInferenceInfo(determineType, determineType);
			case UINT:
				return new HDLTypeInferenceInfo(determineType.setType(INT), determineType.setType(INT));
			case NATURAL:
				return new HDLTypeInferenceInfo(determineType.setType(INTEGER), determineType.setType(INTEGER));
			case BIT:
			case BITVECTOR:
			case BOOL:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Arithmetic negation does not support bit/boolean operands";
				return hdi;
			}
			break;
		case BIT_NEG:
			switch (determineType.getType()) {
			case INT:
			case INTEGER:
			case UINT:
			case NATURAL:
			case BIT:
			case BITVECTOR:
				return new HDLTypeInferenceInfo(determineType, determineType);
			case BOOL:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Bit negation does not support boolean operands";
				return hdi;
			}
			break;
		case LOGIC_NEG:
			switch (determineType.getType()) {
			case INT:
			case INTEGER:
			case UINT:
			case NATURAL:
			case BITVECTOR:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Logic negation does not support bit operands";
				return hdi;
			case BIT:
			case BOOL:
				return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool());
			}
			break;
		}
		return null;
	}

	public ValueRange getValueRange(HDLPrimitive pt, HDLEvaluationContext context) {
		switch (pt.getType()) {
		case BOOL:
		case BIT:
		case BITVECTOR:
			throw new IllegalArgumentException(pt.getType() + " is not numerical!");
		case INT:
			BigInteger bitWidth = pt.getWidth().constantEvaluate(context);
			return intRange(bitWidth);
		case INTEGER:
			return intRange(BigInteger.valueOf(32));
		case UINT:
			return uintRange(pt.getWidth().constantEvaluate(context));
		case NATURAL:
			return uintRange(BigInteger.valueOf(32));
		}
		throw new IllegalArgumentException("Did not expect type:" + pt.getType());
	}

	private static ValueRange intRange(BigInteger bitWidth) {
		BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue() - 1).subtract(BigInteger.ONE);
		BigInteger min = max.negate().subtract(BigInteger.ONE);
		return new ValueRange(min, max);
	}

	private static ValueRange uintRange(BigInteger bitWidth) {
		BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE);
		BigInteger min = BigInteger.ZERO;
		return new ValueRange(min, max);
	}

}
