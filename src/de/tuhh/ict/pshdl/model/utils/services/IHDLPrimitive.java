package de.tuhh.ict.pshdl.model.utils.services;

import java.math.*;

import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;

public interface IHDLPrimitive {

	public abstract HDLTypeInferenceInfo getArithOpType(HDLArithOp op);

	public abstract HDLTypeInferenceInfo getShiftOpType(HDLShiftOp op);

	public abstract HDLTypeInferenceInfo getEqualityOpType(HDLEqualityOp op);

	public abstract HDLTypeInferenceInfo getBitOpType(HDLBitOp op);

	public abstract HDLTypeInferenceInfo getManipOpType(HDLManip manip);

	public abstract Range<BigInteger> getValueRange(HDLPrimitive pt, HDLEvaluationContext context);

}