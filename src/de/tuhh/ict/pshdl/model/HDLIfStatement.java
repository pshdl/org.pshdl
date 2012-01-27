package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLIfStatement extends AbstractHDLIfStatement {
	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> thenDo, ArrayList<HDLStatement> elseDo, boolean validate) {
		super(container, ifExp, thenDo, elseDo, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 */
	public HDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> thenDo, ArrayList<HDLStatement> elseDo) {
		this(container, ifExp, thenDo, elseDo, true);
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
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
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
