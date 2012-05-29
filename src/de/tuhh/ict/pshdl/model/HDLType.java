package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLType contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLType(HDLObject container, String name, boolean validate) {
		super(container, name, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 */
	public HDLType(HDLObject container, String name) {
		this(container, name, true);
	}

	public HDLType() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLType;
	}

	// $CONTENT-BEGIN$
	public HDLQualifiedName asRef() {
		return new HDLQualifiedName(getName());
	}

	public HDLExpression getWidth() {
		throw new IllegalArgumentException("Not implemented for this type:" + this);
	}
	// $CONTENT-END$

}
