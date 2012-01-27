package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLSwitchCaseStatement extends AbstractHDLSwitchCaseStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchCaseStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSwitchCaseStatement(HDLObject container, HDLExpression label, ArrayList<HDLStatement> dos, boolean validate) {
		super(container, label, dos, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLSwitchCaseStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 */
	public HDLSwitchCaseStatement(HDLObject container, HDLExpression label, ArrayList<HDLStatement> dos) {
		this(container, label, dos, true);
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
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return getallInterfaceDeclarations(dos);
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return getallVariableDeclarations(dos);
	}
	// $CONTENT-END$

}
