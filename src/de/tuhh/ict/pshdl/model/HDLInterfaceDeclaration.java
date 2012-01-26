package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLInterfaceDeclaration extends AbstractHDLInterfaceDeclaration {
	public HDLInterfaceDeclaration(HDLObject container, HDLInterface hIf) {
		super(container, hIf);
	}

	public HDLInterfaceDeclaration() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return Collections.singletonList(this);
	}
	// $CONTENT-END$
}
