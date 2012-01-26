package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLGeneratorArgument extends AbstractHDLGeneratorArgument {
	public HDLGeneratorArgument(HDLObject container, String name, String value, HDLExpression expression) {
		super(container, name, value, expression);
	}
	public HDLGeneratorArgument() {
		super();
	}
}	
