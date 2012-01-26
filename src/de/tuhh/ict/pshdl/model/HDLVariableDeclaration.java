package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLVariableDeclaration extends AbstractHDLVariableDeclaration {
	public HDLVariableDeclaration(HDLObject container, HDLType type, ArrayList<HDLVariable> variables) {
		super(container, type, variables);
	}
	public HDLVariableDeclaration() {
		super();
	}
}	
