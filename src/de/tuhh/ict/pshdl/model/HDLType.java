package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLType contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
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

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLType;
	}

	public static HDLFieldAccess<HDLType, String> fName = new HDLFieldAccess<HDLType, String>() {
		@Override
		public String getValue(HDLType obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};

	// $CONTENT-BEGIN$
	public HDLQualifiedName asRef() {
		return new HDLQualifiedName(getName());
	}

	public HDLExpression getWidth() {
		throw new IllegalArgumentException("Not implemented for this type:" + this);
	}
	// $CONTENT-END$

}
