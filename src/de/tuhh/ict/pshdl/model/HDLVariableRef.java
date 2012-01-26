package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLVariableRef extends AbstractHDLVariableRef {
	public HDLVariableRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits) {
		super(container, var, array, bits);
	}

	public HDLVariableRef() {
		super();
	}
}
