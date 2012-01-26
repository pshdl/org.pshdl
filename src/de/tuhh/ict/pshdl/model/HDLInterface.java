package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLInterface extends AbstractHDLInterface {
	public HDLInterface(HDLObject container, String name, ArrayList<HDLVariable> ports) {
		super(container, name, ports);
	}
	public HDLInterface() {
		super();
	}
}	
