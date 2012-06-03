package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLSwitchStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression caseExp. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLSwitchCaseStatement> cases. Can be <code>null</code>.</li>
 * </ul>
 */
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

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLSwitchStatement;
	}

	public static HDLFieldAccess<HDLSwitchStatement, HDLExpression> fCaseExp = new HDLFieldAccess<HDLSwitchStatement, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCaseExp();
		}
	};
	public static HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>> fCases = new HDLFieldAccess<HDLSwitchStatement, ArrayList<HDLSwitchCaseStatement>>() {
		@Override
		public ArrayList<HDLSwitchCaseStatement> getValue(HDLSwitchStatement obj) {
			if (obj == null)
				return null;
			return obj.getCases();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetEnumDeclarations());
		}
		return res;
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetInterfaceDeclarations());
		}
		return res;
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		for (HDLSwitchCaseStatement c : cases) {
			res.addAll(c.doGetVariableDeclarations());
		}
		return res;
	}
	// $CONTENT-END$

}
