package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLSwitchCaseStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression label. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> dos. Can be <code>null</code>.</li>
 * </ul>
 */

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

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSwitchCaseStatement;
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return HDLUtils.getallEnumDeclarations(dos);
	}

	@Override
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return HDLUtils.getallInterfaceDeclarations(dos);
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return HDLUtils.getallVariableDeclarations(dos);
	}
	// $CONTENT-END$

}
