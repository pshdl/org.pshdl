package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLRange extends AbstractHDLRange {
	public HDLRange(HDLObject container, HDLExpression from, HDLExpression to) {
		super(container, from, to);
	}
	public HDLRange() {
		super();
	}
}	
