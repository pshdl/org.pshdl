package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import static de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import static de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType.*;

public class HDLPrimitives {

	private static EnumSet<HDLPrimitiveType> disAllowedArithTypes = EnumSet.of(BIT, BITVECTOR, BOOL);
	private static EnumSet<HDLPrimitiveType> disAllowedShiftByTypes = EnumSet.of(BIT, BITVECTOR, BOOL, INT, INTEGER);
	private static HDLPrimitives hdlPrimitives;

	public static class HDLTypeInferenceInfo {
		public HDLPrimitive left, right, result;
		public String error;

		public HDLTypeInferenceInfo(HDLPrimitive left, HDLPrimitive right, HDLPrimitive result) {
			super();
			this.left = left;
			this.right = right;
			this.result = result;
		}
	}

	public static HDLPrimitives getInstance() {
		if (hdlPrimitives == null)
			hdlPrimitives = new HDLPrimitives();
		return hdlPrimitives;
	}

	@SuppressWarnings("incomplete-switch")
	public HDLTypeInferenceInfo getArithOpType(HDLExpression left, HDLArithOpType type, HDLExpression right) {
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(left.determineType(), right.determineType(), null);
		if (disAllowedArithTypes.contains(info.left.getType()) || disAllowedArithTypes.contains(info.right.getType())) {
			info.error = "Arithmetic with bits not allowed. Left:" + info.left + " Right:" + info.right;
			return info;
		}

		switch (info.left.getType()) {
		case INT:
			switch (info.right.getType()) {
			case INT:
			case INTEGER:
			case UINT:
			case NATURAL:
				info.result = new HDLPrimitive().setType(INT).setWidth(getWidth(left, type, right, info));
				break;
			}
			break;
		case INTEGER:
			switch (info.right.getType()) {
			case INT:
			case UINT:
				info.result = new HDLPrimitive().setType(INT).setWidth(getWidth(left, type, right, info));
				break;
			case INTEGER:
			case NATURAL:
				info.result = new HDLPrimitive().setType(INT);
				break;
			}
			break;
		case UINT:
			switch (info.right.getType()) {
			case INTEGER:
			case INT:
				info.result = new HDLPrimitive().setType(INT).setWidth(getWidth(left, type, right, info));
				break;
			case NATURAL:
			case UINT:
				info.result = new HDLPrimitive().setType(UINT).setWidth(getWidth(left, type, right, info));
				break;
			}
			break;
		case NATURAL:
			switch (info.right.getType()) {
			case INT:
				info.result = new HDLPrimitive().setType(INT).setWidth(getWidth(left, type, right, info));
				break;
			case INTEGER:
				info.result = new HDLPrimitive().setType(INT);
				break;
			case UINT:
				info.result = new HDLPrimitive().setType(UINT).setWidth(getWidth(left, type, right, info));
				break;
			case NATURAL:
				info.result = new HDLPrimitive().setType(UINT);
				break;
			}
			break;
		}
		return info;
	}

	private HDLExpression getWidth(HDLExpression left, HDLArithOpType type, HDLExpression right, HDLTypeInferenceInfo info) {
		HDLExpression leftW = info.left.getWidth();
		HDLExpression rightW = info.right.getWidth();
		switch (type) {
		case POW:
			// The result type of pow can only be natural
			return null;
		case DIV:
			return leftW;
		case MOD:
			return rightW;
		case MINUS:
		case PLUS:
			if (leftW == null)
				return rightW;
			if (rightW == null)
				return leftW;
			return new HDLArithOp().setLeft(new HDLFunction().setName("max").addParams(leftW).addParams(rightW)).setType(PLUS).setRight(new HDLLiteral().setVal("1"));
		case MUL:
			if (leftW == null)
				return rightW;
			if (rightW == null)
				return leftW;
			return new HDLArithOp().setLeft(leftW).setType(PLUS).setRight(rightW);
		}

		return null;
	}

	public HDLTypeInferenceInfo getShiftOpType(HDLExpression left, HDLShiftOpType type, HDLExpression right) {
		HDLPrimitive lType = left.determineType();
		HDLPrimitive rType = right.determineType();
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(lType, new HDLPrimitive().setType(NATURAL), lType);
		if (disAllowedShiftByTypes.contains(rType)) {
			info.error = "Shift can not take right hand operands of type:" + rType;
		}
		return info;
	}

	public HDLTypeInferenceInfo getEqualityOpType(HDLExpression left, HDLEqualityOpType type, HDLExpression right) {
		HDLPrimitive lType = left.determineType();
		HDLPrimitive rType = right.determineType();
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(lType, new HDLPrimitive().setType(NATURAL), lType);
		if (disAllowedShiftByTypes.contains(rType)) {
			info.error = "Shift can not take right hand operands of type:" + rType;
		}
		return info;
	}

	public HDLTypeInferenceInfo getBitOpType(HDLExpression left, HDLBitOpType type, HDLExpression right) {
		HDLPrimitive lType = left.determineType();
		HDLPrimitive rType = right.determineType();
		if ((type == HDLBitOpType.LOGI_AND) || (type == HDLBitOpType.LOGI_OR))
			return new HDLTypeInferenceInfo(new HDLPrimitive().setType(BOOL), new HDLPrimitive().setType(BOOL), new HDLPrimitive().setType(BOOL));
		HDLTypeInferenceInfo info = new HDLTypeInferenceInfo(lType, rType, lType);
		if (disAllowedShiftByTypes.contains(rType)) {
			info.error = "Shift can not take right hand operands of type:" + rType;
		}
		return info;
	}
}
