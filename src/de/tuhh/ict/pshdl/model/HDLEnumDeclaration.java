package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLEnumDeclaration extends AbstractHDLEnumDeclaration {
	public HDLEnumDeclaration(HDLObject container, HDLEnum hEnum) {
		super(container, hEnum);
	}

	public HDLEnumDeclaration() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.singletonList(this);
	}
	// $CONTENT-END$
}
