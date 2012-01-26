package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLEnumRef extends AbstractHDLEnumRef {
	public HDLEnumRef(HDLObject container, HDLQualifiedName hEnum, HDLQualifiedName var) {
		super(container, hEnum, var);
	}

	public HDLEnumRef() {
		super();
	}
}
