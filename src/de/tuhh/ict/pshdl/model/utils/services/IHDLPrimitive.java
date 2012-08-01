package de.tuhh.ict.pshdl.model.utils.services;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public interface IHDLPrimitive {

	public abstract HDLTypeInferenceInfo getArithOpType(HDLArithOp op);

	public abstract HDLTypeInferenceInfo getShiftOpType(HDLShiftOp op);

	public abstract HDLTypeInferenceInfo getEqualityOpType(HDLEqualityOp op);

	public abstract HDLTypeInferenceInfo getBitOpType(HDLBitOp op);

	public abstract HDLTypeInferenceInfo getManipOpType(HDLManip manip);

	public abstract ValueRange getValueRange(HDLPrimitive pt, HDLEvaluationContext context);

}