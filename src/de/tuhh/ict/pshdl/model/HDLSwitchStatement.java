package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLSwitchStatement extends AbstractHDLSwitchStatement {
	/**
	 * Constructs a new instance of {@link HDLSwitchStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLSwitchStatement(HDLObject container, HDLExpression caseExp, ArrayList<HDLSwitchCaseStatement> cases, boolean validate) {
		super(container, caseExp, cases, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLSwitchStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 */
	public HDLSwitchStatement(HDLObject container, HDLExpression caseExp, ArrayList<HDLSwitchCaseStatement> cases) {
		this(container, caseExp, cases, true);
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
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
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
