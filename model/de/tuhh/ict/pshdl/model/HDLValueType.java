package de.tuhh.ict.pshdl.model;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLValueType contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dim. Can be <code>null</code>.</li>
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
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLValueType(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(container, name, dim, validate);
	}

	public HDLValueType() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLValueType;
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
