package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

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

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
