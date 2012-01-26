package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLSwitchStatement extends AbstractHDLSwitchStatement {
	public HDLSwitchStatement(HDLObject container, ArrayList<HDLSwitchCaseStatement> cases) {
		super(container, cases);
	}

	public HDLSwitchStatement() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetEnumDeclarations());
		}
		return res;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		List<HDLInterfaceDeclaration> res = new LinkedList<HDLInterfaceDeclaration>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetInterfaceDeclarations());
		}
		return res;
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetVariableDeclarations());
		}
		return res;
	}
	// $CONTENT-END$
}
