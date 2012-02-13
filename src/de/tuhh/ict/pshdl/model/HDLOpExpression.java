package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;


public abstract class HDLOpExpression extends AbstractHDLOpExpression {
	/**
	 * Constructs a new instance of {@link HDLOpExpression}
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
	public HDLOpExpression(HDLObject container, HDLExpression left, HDLExpression right, boolean validate) {
		super(container, left, right, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLOpExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public HDLOpExpression(HDLObject container, HDLExpression left, HDLExpression right) {
		this(container, left, right, true);
	}
	public HDLOpExpression() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
