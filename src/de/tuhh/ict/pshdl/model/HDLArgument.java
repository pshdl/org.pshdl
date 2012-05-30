package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLArgument contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String value. Can be <code>null</code>.</li>
 * <li>HDLExpression expression. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLArgument extends AbstractHDLArgument {
	/**
	 * Constructs a new instance of {@link HDLArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArgument(HDLObject container, String name, String value, HDLExpression expression, boolean validate) {
		super(container, name, value, expression, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can be <code>null</code>.
	 */
	public HDLArgument(HDLObject container, String name, String value, HDLExpression expression) {
		this(container, name, value, expression, true);
	}

	public HDLArgument() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArgument;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
