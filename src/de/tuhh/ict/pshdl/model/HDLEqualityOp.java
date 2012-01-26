package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLEqualityOp extends AbstractHDLEqualityOp {
	public HDLEqualityOp(HDLObject container, HDLExpression left, HDLExpression right, HDLEqualityOpType type) {
		super(container, left, right, type);
	}
	public HDLEqualityOp() {
		super();
	}
	 	public static enum HDLEqualityOpType {
			EQ,GREATER,GREATER_EQ,LESS,LESS_EQ,NOT_EQ,
		}
}	
