package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLArithOp extends AbstractHDLArithOp {
	public HDLArithOp(HDLObject container, HDLExpression left, HDLExpression right, HDLArithOpType type) {
		super(container, left, right, type);
	}

	public HDLArithOp() {
		super();
	}

	public static enum HDLArithOpType {
		MUL, DIV, MINUS, PLUS, MOD, POW,
	}
}
