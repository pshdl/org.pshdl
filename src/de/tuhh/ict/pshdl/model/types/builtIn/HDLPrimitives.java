package de.tuhh.ict.pshdl.model.types.builtIn;

import static com.google.common.base.Preconditions.*;
import static de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType.*;
import static de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType.*;

import java.io.*;
import java.math.*;
import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLPrimitives {

	private static HDLPrimitives hdlPrimitives;

	public static class HDLInferenceTriple {
		public HDLPrimitiveType left, right, result;

		public HDLInferenceTriple(@Nonnull HDLPrimitiveType left, @Nonnull HDLPrimitiveType right, HDLPrimitiveType result) {
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
		if (hdlPrimitives == null) {
			hdlPrimitives = new HDLPrimitives();
		}
		return hdlPrimitives;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initEqualityResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = initArithResolution();
		for (HDLInferenceTriple result : res.values()) {
			result.result = BOOL;
		}
		for (HDLPrimitiveType right : HDLPrimitiveType.values()) {
			if (right == BOOL) {
				continue;
			}
			if (right == STRING) {
				continue;
			}
			res.put(new HDLInferenceTriple(BIT, right, null), new HDLInferenceTriple(BITVECTOR, BITVECTOR, BOOL));
			res.put(new HDLInferenceTriple(BITVECTOR, right, null), new HDLInferenceTriple(BITVECTOR, BITVECTOR, BOOL));
		}
		res.put(new HDLInferenceTriple(BOOL, BOOL, null), new HDLInferenceTriple(BOOL, BOOL, BOOL));
		res.put(new HDLInferenceTriple(STRING, STRING, null), new HDLInferenceTriple(STRING, STRING, BOOL));
		res.put(new HDLInferenceTriple(BIT, BIT, null), new HDLInferenceTriple(BIT, BIT, BOOL));
		return res;
	}

	private static Map<HDLInferenceTriple, HDLInferenceTriple> initBitResolution() {
		Map<HDLInferenceTriple, HDLInferenceTriple> res = new HashMap<HDLInferenceTriple, HDLInferenceTriple>();
		for (HDLPrimitiveType left : HDLPrimitiveType.values()) {
			if (left == BOOL) {
				continue;
			}
			if (left == STRING) {
				continue;
			}
			for (HDLPrimitiveType right : HDLPrimitiveType.values()) {
				if (right == BOOL) {
					continue;
				}
				if (right == STRING) {
					continue;
				}
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
			fos.write(sb.toString().getBytes(Charsets.UTF_8));
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
					if (triple.left != left) {
						sb.append("<span class='cast'>").append(triple.left.name().toLowerCase()).append("</span>");
					} else {
						sb.append(left.name().toLowerCase());
					}
					sb.append("</td><td>");
					if (triple.right != right) {
						sb.append("<span class='cast'>").append(triple.right.name().toLowerCase()).append("</span>");
					} else {
						sb.append(right.name().toLowerCase());
					}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.tuhh.ict.pshdl.model.types.builtIn.IHDLPrimitive#getArithOpType(de
	 * .tuhh.ict.pshdl.model.HDLArithOp)
	 */
	public HDLTypeInferenceInfo getArithOpType(HDLArithOp op) {
		HDLPrimitive lType = (HDLPrimitive) TypeExtension.typeOf(op.getLeft());
		HDLPrimitive rType = (HDLPrimitive) TypeExtension.typeOf(op.getRight());
		HDLArithOpType type = op.getType();
		if (HDLPrimitive.isTargetMatching(lType)) {
			if (HDLPrimitive.isTargetMatching(rType)) {
				lType = rType;
			}
		}
		if (HDLPrimitive.isTargetMatching(rType)) {
			rType = lType;
		}
		HDLInferenceTriple triple = arithResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}

		// Extend uint by 1 to have not lose information when interpreting uint
		// as int
		HDLPrimitive newLType = lType.setType(triple.left);
		HDLPrimitive newRType = rType.setType(triple.right);
		if ((triple.left == INT) && (triple.right == UINT)) {
			HDLExpression rTypeWidth = checkNotNull(newRType.getWidth(), "The type should have been Integer or natural if width equals null");
			newRType = newRType.setWidth(new HDLArithOp().setLeft(rTypeWidth).setType(PLUS).setRight(HDLLiteral.get(1)));
		}
		if ((triple.left == UINT) && (triple.right == INT)) {
			HDLExpression lTypeWidth = checkNotNull(newLType.getWidth(), "The type should have been Integer or natural if width equals null");
			newLType = newLType.setWidth(new HDLArithOp().setLeft(lTypeWidth).setType(PLUS).setRight(HDLLiteral.get(1)));
		}
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(null, newLType, newRType);
		HDLExpression width = simplifyWidth(op, getWidth(op, type, info));
		info.result = new HDLPrimitive().setType(triple.result).setWidth(width);
		return normalize(info, op);
	}

	public static HDLExpression simplifyWidth(HDLObject container, HDLExpression width) {
		if (width == null)
			return null;
		width = width.copyDeepFrozen(container);
		Optional<BigInteger> newW = ConstantEvaluate.valueOf(width, null);
		if (newW.isPresent()) {
			width = new HDLLiteral().setVal(newW.get().toString());
		}
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

	private HDLExpression getWidth(IHDLObject exp, HDLArithOpType type, HDLTypeInferenceInfo info) {
		HDLExpression leftW = ((HDLPrimitive) info.args[0]).getWidth();
		if (leftW != null) {
			leftW = leftW.copyFiltered(CopyFilter.DEEP_META); // XXX Remove
		}
		HDLExpression rightW = ((HDLPrimitive) info.args[1]).getWidth();
		if (rightW != null) {
			rightW = rightW.copyFiltered(CopyFilter.DEEP_META); // XXX Remove
		}
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
			return PSHDLLib.MAX.getReplacementExpressionArgs(exp, leftW, rightW);
			// return new HDLArithOp().setLeft(max).setType(PLUS).setRight(new
			// HDLLiteral().setVal("1"));
		case MUL:
			if ((leftW == null) && (rightW == null))
				return null;
			if ((leftW != null) && (rightW == null))
				return new HDLArithOp().setLeft(leftW).setType(PLUS).setRight(leftW);
			if ((leftW == null) && (rightW != null))
				return new HDLArithOp().setLeft(rightW).setType(PLUS).setRight(rightW);
			if ((leftW != null) && (rightW != null))
				return new HDLArithOp().setLeft(leftW).setType(PLUS).setRight(rightW);
		}
		return null;
	}

	public HDLTypeInferenceInfo getShiftOpType(HDLShiftOp op) {
		HDLPrimitive lType = (HDLPrimitive) TypeExtension.typeOf(op.getLeft());
		HDLPrimitive rType = (HDLPrimitive) TypeExtension.typeOf(op.getRight());

		HDLInferenceTriple triple = shiftResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + op.getType() + " is not defined for the type of the right-handside: " + rType;
			return hdi;
		}
		HDLExpression width = lType.getWidth();
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result).setWidth(width), lType, new HDLPrimitive().setType(triple.right));
		return normalize(info, op);
	}

	EnumSet<HDLPrimitiveType> nonOrderType = EnumSet.of(HDLPrimitiveType.BIT, HDLPrimitiveType.BITVECTOR, HDLPrimitiveType.BOOL);
	EnumSet<HDLEqualityOpType> nonOrderCompType = EnumSet.of(HDLEqualityOpType.EQ, HDLEqualityOpType.NOT_EQ);

	public HDLTypeInferenceInfo getEqualityOpType(HDLEqualityOp op) {
		HDLType determineTypeL = TypeExtension.typeOf(op.getLeft());
		HDLType determineTypeR = TypeExtension.typeOf(op.getRight());
		if ((determineTypeL instanceof HDLPrimitive) && (determineTypeR instanceof HDLPrimitive)) {
			HDLPrimitive lType = (HDLPrimitive) determineTypeL;
			HDLPrimitive rType = (HDLPrimitive) determineTypeR;
			if (HDLPrimitive.isTargetMatching(lType)) {
				if (HDLPrimitive.isTargetMatching(rType)) {
					lType = rType;
				}
			}
			if (HDLPrimitive.isTargetMatching(rType)) {
				rType = lType;
			}
			if (nonOrderType.contains(lType.getType()) || nonOrderType.contains(rType.getType()))
				if (!nonOrderCompType.contains(op.getType())) {
					HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
					hdi.error = "The operation " + op.getType() + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
					return hdi;
				}
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
		HDLBitOpType type = op.getType();
		if ((type == HDLBitOpType.LOGI_AND) || (type == HDLBitOpType.LOGI_OR))
			return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool(), HDLPrimitive.getBool());
		HDLPrimitive lType = (HDLPrimitive) TypeExtension.typeOf(op.getLeft());
		HDLPrimitive rType = (HDLPrimitive) TypeExtension.typeOf(op.getRight());
		if (HDLPrimitive.isTargetMatching(lType)) {
			if (HDLPrimitive.isTargetMatching(rType)) {
				lType = rType;
			}
		}
		if (HDLPrimitive.isTargetMatching(rType)) {
			rType = lType;
		}
		HDLInferenceTriple triple = bitResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}
		HDLExpression width = lType.getWidth();
		if ((width == null) && (rType.getWidth() != null)) {
			width = rType.getWidth();
		}
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result).setWidth(width == null ? null : width), lType.setType(triple.left),
				lType.setType(triple.right));
		return normalize(info, op);
	}

	public HDLTypeInferenceInfo getManipOpType(HDLManip manip) {
		HDLExpression target = manip.getTarget();
		HDLType castTo = manip.getCastTo();
		HDLPrimitive determineType = (HDLPrimitive) TypeExtension.typeOf(target);
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
			case STRING:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Arithmetic negation does not support bit/boolean/string operands";
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
			case STRING:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Bit negation does not support boolean/string operands";
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
			case STRING:
				HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
				hdi.error = "Logic negation does not support bit/string operands";
				return hdi;
			case BIT:
			case BOOL:
				return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool());
			}
			break;
		}
		return null;
	}

	public Optional<Range<BigInteger>> getValueRange(HDLPrimitive pt, HDLEvaluationContext context) {
		switch (pt.getType()) {
		case BOOL:
		case BIT:
		case BITVECTOR:
		case STRING:
			return Optional.absent();
		case INT: {
			Optional<BigInteger> bitWidth = ConstantEvaluate.valueOf(pt.getWidth(), context);
			if (!bitWidth.isPresent())
				return null;
			return Optional.of(intRange(bitWidth.get()));
		}
		case INTEGER:
			return Optional.of(intRange(BigInteger.valueOf(32)));
		case UINT: {
			Optional<BigInteger> bitWidth = ConstantEvaluate.valueOf(pt.getWidth(), context);
			if (!bitWidth.isPresent())
				return null;
			return Optional.of(uintRange(bitWidth.get()));
		}
		case NATURAL:
			return Optional.of(uintRange(BigInteger.valueOf(32)));
		}
		throw new IllegalArgumentException("Did not expect type:" + pt.getType());
	}

	private static Range<BigInteger> intRange(BigInteger bitWidth) {
		BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue() - 1).subtract(BigInteger.ONE);
		BigInteger min = max.negate().subtract(BigInteger.ONE);
		return Ranges.closed(min, max);
	}

	private static Range<BigInteger> uintRange(BigInteger bitWidth) {
		BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE);
		BigInteger min = BigInteger.ZERO;
		return Ranges.closed(min, max);
	}

	public static Integer getWidth(HDLType type, HDLEvaluationContext context) {
		if (type.getClassType() == HDLClass.HDLPrimitive) {
			HDLPrimitive determineType = (HDLPrimitive) type;
			BigInteger width = null;
			switch (determineType.getType()) {
			case BIT:
				width = BigInteger.ONE;
				break;
			case INTEGER:
			case NATURAL:
				width = BigInteger.valueOf(32);
				break;
			case UINT:
			case INT:
			case BITVECTOR:
				width = ConstantEvaluate.valueOf(determineType.getWidth(), context).orNull();
				break;
			default:
				throw new IllegalArgumentException("Can not concatenate " + determineType);
			}
			if (width != null)
				return width.intValue();
		}
		return null;
	}

}
