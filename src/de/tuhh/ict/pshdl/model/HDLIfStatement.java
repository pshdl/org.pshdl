package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLIfStatement extends AbstractHDLIfStatement {
	public HDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> ifDo, ArrayList<HDLStatement> thenDo) {
		super(container, ifExp, ifDo, thenDo);
	}

	public HDLIfStatement() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		res.addAll(getallEnumDeclarations(thenDo));
		res.addAll(getallEnumDeclarations(elseDo));
		return res;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		List<HDLInterfaceDeclaration> res = new LinkedList<HDLInterfaceDeclaration>();
		res.addAll(getallInterfaceDeclarations(thenDo));
		res.addAll(getallInterfaceDeclarations(elseDo));
		return res;
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		res.addAll(getallVariableDeclarations(thenDo));
		res.addAll(getallVariableDeclarations(elseDo));
		return res;
	}
	// $CONTENT-END$
}
