package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLDeclaration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLDeclaration extends AbstractHDLDeclaration {
	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDeclaration(int containerID, @Nullable HDLObject container, boolean validate) {
		super(containerID, container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLDeclaration(int containerID, @Nullable HDLObject container) {
		this(containerID, container, true);
	}

	public HDLDeclaration() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLDeclaration;
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
