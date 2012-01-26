package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLInterfaceInstantiation extends AbstractHDLInterfaceInstantiation {
	public HDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var) {
		super(container, hIf, var);
	}

	public HDLInterfaceInstantiation() {
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
		return Collections.singletonList(new HDLVariableDeclaration(this, resolveInterface(hIf), asList(var)));
	}
	// $CONTENT-END$
}
