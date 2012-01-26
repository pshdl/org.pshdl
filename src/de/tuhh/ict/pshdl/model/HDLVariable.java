package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLVariable extends AbstractHDLVariable {
	public HDLVariable(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, ArrayList<HDLExpression> dimensions) {
		super(container, name, annotations, dimensions);
	}

	public HDLVariable() {
		super();
	}
}
