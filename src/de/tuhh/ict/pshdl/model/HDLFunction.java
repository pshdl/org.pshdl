package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLFunction extends AbstractHDLFunction {
	public HDLFunction(HDLObject container, String name, ArrayList<HDLExpression> params) {
		super(container, name, params);
	}
	public HDLFunction() {
		super();
	}
}	
