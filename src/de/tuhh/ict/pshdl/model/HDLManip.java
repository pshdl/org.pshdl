package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLManip extends AbstractHDLManip {
	public HDLManip(HDLObject container, HDLManipType type, HDLExpression target, HDLType castTo) {
		super(container, type, target, castTo);
	}

	public HDLManip() {
		super();
	}

	public static enum HDLManipType {
		CAST, ARITH_NEG, BIT_NEG, LOGIC_NEG,
	}
}
