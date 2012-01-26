package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public abstract class HDLExpression extends AbstractHDLExpression {
	public HDLExpression(HDLObject container) {
		super(container);
	}
	public HDLExpression() {
		super();
	}
}	
