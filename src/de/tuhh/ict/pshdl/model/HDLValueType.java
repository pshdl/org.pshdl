package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLValueType contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLValueType extends AbstractHDLValueType {
	/**
	 * Constructs a new instance of {@link HDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLValueType(HDLObject container, String name, boolean validate) {
		super(container, name, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 */
	public HDLValueType(HDLObject container, String name) {
		this(container, name, true);
	}

	public HDLValueType() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLValueType;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
