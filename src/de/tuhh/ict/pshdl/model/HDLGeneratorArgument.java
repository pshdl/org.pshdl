package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLGeneratorArgument contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String value. Can be <code>null</code>.</li>
 * <li>HDLExpression expression. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLGeneratorArgument extends AbstractHDLGeneratorArgument {
	/**
	 * Constructs a new instance of {@link HDLGeneratorArgument}
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
	public HDLGeneratorArgument(HDLObject container, String name, String value, HDLExpression expression, boolean validate) {
		super(container, name, value, expression, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLGeneratorArgument}
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
	public HDLGeneratorArgument(HDLObject container, String name, String value, HDLExpression expression) {
		this(container, name, value, expression, true);
	}

	public HDLGeneratorArgument() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLGeneratorArgument;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
