package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.impl.AbstractHDLPrimitive.*;
import de.tuhh.ict.pshdl.model.impl.AbstractHDLValueType.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLSwitchCaseStatement extends AbstractHDLSwitchCaseStatement {
	public HDLSwitchCaseStatement(HDLObject container, HDLExpression label, ArrayList<HDLStatement> dos) {
		super(container, label, dos);
	}

	public HDLSwitchCaseStatement() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return getallEnumDeclarations(dos);
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return getallInterfaceDeclarations(dos);
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return getallVariableDeclarations(dos);
	}
	// $CONTENT-END$
}
