package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLShiftOp extends AbstractHDLShiftOp {
	public HDLShiftOp(HDLObject container, HDLExpression left, HDLExpression right, HDLShiftOpType type) {
		super(container, left, right, type);
	}
	public HDLShiftOp() {
		super();
	}
	 	public static enum HDLShiftOpType {
			SLL,SRA,SRL,
		}
}	
