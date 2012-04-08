package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

public abstract class HDLCompound extends AbstractHDLCompound {
	/**
	 * Constructs a new instance of {@link HDLCompound}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLCompound(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLCompound}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLCompound(HDLObject container) {
		this(container, true);
	}

	public HDLCompound() {
		super();
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
