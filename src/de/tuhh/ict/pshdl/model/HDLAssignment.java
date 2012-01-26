package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLAssignment extends AbstractHDLAssignment {
	public HDLAssignment(HDLObject container, HDLVariableRef left, HDLExpression right) {
		super(container, left, right);
	}

	public HDLAssignment() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.EMPTY_LIST;
	}
	// $CONTENT-END$
}
