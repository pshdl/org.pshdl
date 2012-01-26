package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLBitOp extends AbstractHDLBitOp {
	public HDLBitOp(HDLObject container, HDLExpression left, HDLExpression right, HDLBitOpType type) {
		super(container, left, right, type);
	}
	public HDLBitOp() {
		super();
	}
	 	public static enum HDLBitOpType {
			AND,OR,XOR,LOGI_AND,LOGI_OR,
		}
}	
