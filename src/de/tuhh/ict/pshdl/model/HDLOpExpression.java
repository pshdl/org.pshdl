package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public abstract class HDLOpExpression extends AbstractHDLOpExpression {
	public HDLOpExpression(HDLObject container, HDLExpression left, HDLExpression right) {
		super(container, left, right);
	}
	public HDLOpExpression() {
		super();
	}
}	
