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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.pshdl.model.HDLArithOp.HDLArithOpType.PLUS;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.BIT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.BITVECTOR;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.BOOL;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.INT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.INTEGER;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.NATURAL;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.STRING;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.UINT;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Map;

import javax.annotation.Nonnull;

import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBitOp.HDLBitOpType;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLType;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.io.Files;

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
			final HDLInferenceTriple other = (HDLInferenceTriple) obj;
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
		final Map<HDLInferenceTriple, HDLInferenceTriple> res = initArithResolution();
		for (final HDLInferenceTriple result : res.values()) {
			result.result = BOOL;
		}
		for (final HDLPrimitiveType right : HDLPrimitiveType.values()) {
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
		final Map<HDLInferenceTriple, HDLInferenceTriple> res = Maps.newLinkedHashMap();
		for (final HDLPrimitiveType left : HDLPrimitiveType.values()) {
			if (left == BOOL) {
				continue;
			}
			if (left == STRING) {
				continue;
			}
			for (final HDLPrimitiveType right : HDLPrimitiveType.values()) {
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
		final Map<HDLInferenceTriple, HDLInferenceTriple> res = Maps.newLinkedHashMap();
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
		final Map<HDLInferenceTriple, HDLInferenceTriple> res = Maps.newLinkedHashMap();
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
			final StringBuilder sb = new StringBuilder("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"cast.css\" /></head><body>\n");
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
			Files.write(sb, new File("castTable.html"), StandardCharsets.UTF_8);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static void printTable(StringBuilder sb, Map<HDLInferenceTriple, HDLInferenceTriple> map) {
		sb.append("<table>\n");
		sb.append("<tr><td>left \\ right</td>");
		for (final HDLPrimitiveType left : HDLPrimitiveType.values()) {
			sb.append("<td colspan='3'>").append(left.name().toLowerCase()).append("</td>");
		}
		sb.append("</tr>");
		for (final HDLPrimitiveType left : HDLPrimitiveType.values()) {
			sb.append("\n<tr><td>").append(left.name().toLowerCase()).append("</td>");
			for (final HDLPrimitiveType right : HDLPrimitiveType.values()) {
				final HDLInferenceTriple triple = map.get(new HDLInferenceTriple(left, right, null));
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

	public HDLTypeInferenceInfo getArithOpType(HDLArithOp op) {
		final Optional<? extends HDLType> typeOfLeft = TypeExtension.typeOf(op.getLeft());
		if (!typeOfLeft.isPresent())
			return createError("left", op);
		HDLPrimitive lType = (HDLPrimitive) typeOfLeft.get();
		final Optional<? extends HDLType> typeOfRight = TypeExtension.typeOf(op.getRight());
		if (!typeOfRight.isPresent())
			return createError("right", op);
		HDLPrimitive rType = (HDLPrimitive) typeOfRight.get();
		if (HDLPrimitive.isTargetMatching(lType))
			if (HDLPrimitive.isTargetMatching(rType)) {
				lType = rType;
			}
		if (HDLPrimitive.isTargetMatching(rType)) {
			rType = lType;
		}
		if ((op.getType() == PLUS) && (lType.getType() == STRING) && (rType.getType() == STRING))
			return new HDLTypeInferenceInfo(HDLPrimitive.getString(), lType, rType);
		final HDLInferenceTriple triple = arithResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		final HDLArithOpType type = op.getType();
		if (triple == null) {
			final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}

		// Extend uint by 1 to have not lose information when interpreting uint
		// as int
		HDLPrimitive newLType = lType.setType(triple.left);
		HDLPrimitive newRType = rType.setType(triple.right);
		if ((triple.left == INT) && (triple.right == UINT)) {
			final HDLExpression rTypeWidth = checkNotNull(newRType.getWidth(), "The type should have been Integer or natural if width equals null");
			newRType = newRType.setWidth(new HDLArithOp().setLeft(rTypeWidth).setType(PLUS).setRight(HDLLiteral.get(1)));
		}
		if ((triple.left == UINT) && (triple.right == INT)) {
			final HDLExpression lTypeWidth = checkNotNull(newLType.getWidth(), "The type should have been Integer or natural if width equals null");
			newLType = newLType.setWidth(new HDLArithOp().setLeft(lTypeWidth).setType(PLUS).setRight(HDLLiteral.get(1)));
		}

		final HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(null, newLType, newRType);
		final HDLExpression width = simplifyWidth(op, getWidth(op, type, info));
		info.result = new HDLPrimitive().setType(triple.result).setWidth(width);
		return normalize(info, op);
	}

	public static HDLExpression simplifyWidth(HDLObject container, HDLExpression width) {
		if (width == null)
			return null;
		width = width.copyDeepFrozen(container);
		final Optional<BigInteger> newW = ConstantEvaluate.valueOf(width, null);
		if (newW.isPresent()) {
			width = new HDLLiteral().setVal(newW.get().toString());
		}
		return width;
	}

	private HDLTypeInferenceInfo normalize(HDLTypeInferenceInfo info, HDLExpression op) {
		info.result = normalizeType(op, info.result);
		if (info.args != null) {
			final HDLType[] args = info.args;
			for (int i = 0; i < args.length; i++) {
				args[i] = normalizeType(op, args[i]);
			}
		}
		return info;
	}

	public HDLType normalizeType(HDLExpression op, HDLType arg) {
		if (arg instanceof HDLPrimitive) {
			HDLPrimitive result = (HDLPrimitive) arg;
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
			arg = result.copyDeepFrozen(op);
		}
		return arg;
	}

	private HDLExpression getWidth(IHDLObject exp, HDLArithOpType type, HDLTypeInferenceInfo info) {
		final HDLExpression leftW = ((HDLPrimitive) info.args[0]).getWidth();
		final HDLExpression rightW = ((HDLPrimitive) info.args[1]).getWidth();
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
			if (leftW.equals(rightW))
				return leftW;
			return HDLBuiltInFunctions.MAX_UINT.getCall(leftW, rightW);
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
		final Optional<? extends HDLType> typeOfLeft = TypeExtension.typeOf(op.getLeft());
		if (!typeOfLeft.isPresent())
			return createError("left", op);
		final HDLPrimitive lType = (HDLPrimitive) typeOfLeft.get();
		final Optional<? extends HDLType> typeOfRight = TypeExtension.typeOf(op.getRight());
		if (!typeOfRight.isPresent())
			return createError("right", op);
		final HDLPrimitive rType = (HDLPrimitive) typeOfRight.get();
		final HDLInferenceTriple triple = shiftResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + op.getType() + " is not defined for the type of the right-handside: " + rType;
			return hdi;
		}
		final HDLExpression width = lType.getWidth();
		final HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result).setWidth(width), lType, new HDLPrimitive().setType(triple.right));
		return normalize(info, op);
	}

	EnumSet<HDLPrimitiveType> nonOrderType = EnumSet.of(HDLPrimitiveType.BIT, HDLPrimitiveType.BITVECTOR, HDLPrimitiveType.BOOL);
	EnumSet<HDLEqualityOpType> nonOrderCompType = EnumSet.of(HDLEqualityOpType.EQ, HDLEqualityOpType.NOT_EQ);

	public HDLTypeInferenceInfo getEqualityOpType(HDLEqualityOp op) {
		final Optional<? extends HDLType> typeOfLeft = TypeExtension.typeOf(op.getLeft());
		if (!typeOfLeft.isPresent())
			return createError("left", op);
		final HDLType determineTypeL = typeOfLeft.get();
		final Optional<? extends HDLType> typeOfRight = TypeExtension.typeOf(op.getRight());
		if (!typeOfRight.isPresent())
			return createError("right", op);
		final HDLType determineTypeR = typeOfRight.get();
		if ((determineTypeL instanceof HDLPrimitive) && (determineTypeR instanceof HDLPrimitive)) {
			HDLPrimitive lType = (HDLPrimitive) determineTypeL;
			HDLPrimitive rType = (HDLPrimitive) determineTypeR;
			if (HDLPrimitive.isTargetMatching(lType))
				if (HDLPrimitive.isTargetMatching(rType)) {
					lType = rType;
				}
			if (HDLPrimitive.isTargetMatching(rType)) {
				rType = lType;
			}
			if (nonOrderType.contains(lType.getType()) || nonOrderType.contains(rType.getType()))
				if (!nonOrderCompType.contains(op.getType())) {
					final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
					hdi.error = "The operation " + op.getType() + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
					return hdi;
				}
			final HDLInferenceTriple triple = equalityResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
			if (triple == null) {
				final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
				hdi.error = "The operation " + op.getType() + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
				return hdi;
			}
			final HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setType(triple.result), lType.setType(triple.left), lType.setType(triple.right));
			return normalize(info, op);
		}
		return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), determineTypeL, determineTypeR);
	}

	public HDLTypeInferenceInfo getBitOpType(HDLBitOp op) {
		final HDLBitOpType type = op.getType();
		if ((type == HDLBitOpType.LOGI_AND) || (type == HDLBitOpType.LOGI_OR))
			return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool(), HDLPrimitive.getBool());
		final Optional<? extends HDLType> typeOfLeft = TypeExtension.typeOf(op.getLeft());
		if (!typeOfLeft.isPresent())
			return createError("left", op);
		HDLPrimitive lType = (HDLPrimitive) typeOfLeft.get();
		final Optional<? extends HDLType> typeOfRight = TypeExtension.typeOf(op.getRight());
		if (!typeOfRight.isPresent())
			return createError("right", op);
		HDLPrimitive rType = (HDLPrimitive) typeOfRight.get();
		if (HDLPrimitive.isTargetMatching(lType))
			if (HDLPrimitive.isTargetMatching(rType)) {
				lType = rType;
			}
		if (HDLPrimitive.isTargetMatching(rType)) {
			rType = lType;
		}
		final HDLInferenceTriple triple = bitResolutionTable.get(new HDLInferenceTriple(lType.getType(), rType.getType(), null));
		if (triple == null) {
			final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, lType, rType);
			hdi.error = "The operation " + type + " is not defined for left-handside:" + lType + " and right-handside:" + rType;
			return hdi;
		}
		HDLExpression width = lType.getWidth();
		if ((width == null) && (rType.getWidth() != null)) {
			width = rType.getWidth();
		}
		final HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(new HDLPrimitive().setWidth(width == null ? null : width).setType(triple.result), lType.setType(triple.left),
				lType.setType(triple.right));
		return normalize(info, op);
	}

	public HDLTypeInferenceInfo getManipOpType(HDLManip manip) {
		final HDLExpression target = manip.getTarget();
		final HDLType castTo = manip.getCastTo();
		final Optional<? extends HDLType> typeOfLeft = TypeExtension.typeOf(target);
		if (!typeOfLeft.isPresent())
			return createError("cast", manip);
		if (!(typeOfLeft.get() instanceof HDLPrimitive))
			return createError("cast", manip);
		final HDLPrimitive determineType = (HDLPrimitive) typeOfLeft.get();
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
				final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
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
				final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
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
				final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, determineType);
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

	protected HDLTypeInferenceInfo createError(String side, HDLExpression op) {
		final HDLTypeInferenceInfo hdi = new HDLTypeInferenceInfo(null, null, null);
		hdi.error = "The type of the " + side + " hand side can not be determined for expression:" + op;
		return hdi;
	}

	public Optional<Range<BigInteger>> getValueRange(HDLPrimitive pt, HDLEvaluationContext context) {
		switch (pt.getType()) {
		case BOOL:
		case BIT:
		case BITVECTOR:
		case STRING:
			return Optional.absent();
		case INT: {
			final Optional<BigInteger> bitWidth = ConstantEvaluate.valueOf(pt.getWidth(), context);
			if (!bitWidth.isPresent())
				return Optional.absent();
			return Optional.of(intRange(bitWidth.get()));
		}
		case INTEGER:
			return Optional.of(intRange(BigInteger.valueOf(32)));
		case UINT: {
			final Optional<BigInteger> bitWidth = ConstantEvaluate.valueOf(pt.getWidth(), context);
			if (!bitWidth.isPresent())
				return Optional.absent();
			return Optional.of(uintRange(bitWidth.get()));
		}
		case NATURAL:
			return Optional.of(uintRange(BigInteger.valueOf(32)));
		}
		throw new IllegalArgumentException("Did not expect type:" + pt.getType());
	}

	public static Range<BigInteger> intRange(BigInteger bitWidth) {
		final BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue() - 1).subtract(BigInteger.ONE);
		final BigInteger min = max.negate().subtract(BigInteger.ONE);
		return RangeTool.createRange(min, max);
	}

	public static Range<BigInteger> uintRange(BigInteger bitWidth) {
		final BigInteger max = BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE);
		final BigInteger min = BigInteger.ZERO;
		return RangeTool.createRange(min, max);
	}

	public static HDLExpression getWidthEpression(HDLType type) {
		if (type.getClassType() == HDLClass.HDLPrimitive) {
			final HDLPrimitive determineType = (HDLPrimitive) type;
			switch (determineType.getType()) {
			case BIT:
				return HDLLiteral.get(1);
			case INTEGER:
			case NATURAL:
				return HDLLiteral.get(32);
			case UINT:
			case INT:
			case BITVECTOR:
				return determineType.getWidth();
			default:
				return null;
			}
		}
		return null;
	}

	public static Integer getWidth(HDLType type, HDLEvaluationContext context) {
		if (type.getClassType() == HDLClass.HDLPrimitive) {
			final HDLPrimitive determineType = (HDLPrimitive) type;
			BigInteger width = null;
			switch (determineType.getType()) {
			case BOOL:
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
				return null;
			}
			if (width != null)
				return width.intValue();
		}
		return null;
	}

}
