package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLConcat extends AbstractHDLConcat {
	public HDLConcat(HDLObject container, ArrayList<HDLExpression> cats) {
		super(container, cats);
	}
	public HDLConcat() {
		super();
	}
}	
