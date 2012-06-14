package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLCompound contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLCompound extends AbstractHDLCompound {
	/**
	 * Constructs a new instance of {@link HDLCompound}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLCompound(int containerID, HDLObject container, boolean validate) {
		super(containerID, container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLCompound}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLCompound(int containerID, HDLObject container) {
		this(containerID, container, true);
	}

	public HDLCompound() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLCompound;
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
