package de.tuhh.ict.pshdl.model.aspects;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLArithOp.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;

import java.util.*;

public aspect TypeInference {
	public HDLPrimitive HDLConcat.determineType() {
		Iterator<HDLExpression> iter = getCats().iterator();
		HDLExpression width = iter.next().determineType().getWidth();
		while (iter.hasNext()) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(iter.next().determineType().getWidth());
		}
		return new HDLPrimitive().setType(HDLPrimitiveType.BITVECTOR).setWidth(width);
	}

	public HDLPrimitive HDLEnumRef.determineType() {
		return null;
	}
	
	public HDLPrimitive HDLManip.determineType() {
		switch (getType()){
		case LOGIC_NEG :
			return new HDLPrimitive().setType(HDLPrimitiveType.BOOL);
		case BIT_NEG :
			return getTarget().determineType();
		case ARITH_NEG :
			return getTarget().determineType().setType(HDLPrimitiveType.INT);
		case CAST :
			HDLType castTo = getCastTo();
			if (castTo instanceof HDLPrimitive) {
				return (HDLPrimitive) castTo;
			}
		}
		return null;
	}

	public HDLPrimitive HDLFunction.determineType() {
		// XXX Really good question!
		return HDLPrimitive.TARGET;
	}

	public HDLPrimitive HDLLiteral.determineType() {
		// Actually depends on context
		return HDLPrimitive.TARGET;
	}

	public HDLPrimitive HDLVariableRef.determineType() {
		if (getBits().size() == 0)
			return (HDLPrimitive) resolveVar().determineType();
		Iterator<HDLRange> iter = getBits().iterator();
		HDLExpression width = iter.next().getWidth();
		while (iter.hasNext()) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(iter.next().getWidth());
		}
		return new HDLPrimitive().setType(HDLPrimitiveType.BITVECTOR).setWidth(width);
	}

	public HDLPrimitive HDLArithOp.determineType() {
		return HDLPrimitives.getInstance().getArithOpType(getLeft(), getType(), getRight()).result;
	}

	public HDLPrimitive HDLBitOp.determineType() {
		return HDLPrimitives.getInstance().getBitOpType(getLeft(), getType(), getRight()).result;
	}

	public HDLPrimitive HDLShiftOp.determineType() {
		return HDLPrimitives.getInstance().getShiftOpType(getLeft(), getType(), getRight()).result;
	}

	public HDLPrimitive HDLEqualityOp.determineType() {
		return HDLPrimitives.getInstance().getEqualityOpType(getLeft(), getType(), getRight()).result;
	}
}
