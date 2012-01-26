package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLInterfaceRef extends AbstractHDLInterfaceRef {
	public HDLInterfaceRef(HDLObject container, HDLQualifiedName hIf, HDLVariableRef var, ArrayList<HDLExpression> array) {
		super(container, hIf, var, array);
	}

	public HDLInterfaceRef() {
		super();
	}
}
