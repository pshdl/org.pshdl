package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLAssignment extends AbstractHDLAssignment {
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLAssignment(HDLObject container, HDLVariableRef left, HDLExpression right, boolean validate) {
		super(container, left, right, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public HDLAssignment(HDLObject container, HDLVariableRef left, HDLExpression right) {
		this(container, left, right, true);
	}
	public HDLAssignment() {
		super();
	}
//$CONTENT-BEGIN$
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
//$CONTENT-END$
}	
