package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLInstantiation contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public abstract class HDLInstantiation extends AbstractHDLInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInstantiation(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLInstantiation(HDLObject container) {
		this(container, true);
	}

	public HDLInstantiation() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLInstantiation;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
