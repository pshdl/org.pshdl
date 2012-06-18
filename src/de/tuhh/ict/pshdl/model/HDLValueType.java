package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

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
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLValueType(int containerID, @Nullable HDLObject container, @NonNull String name, boolean validate) {
		super(containerID, container, name, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 */
	public HDLValueType(int containerID, @Nullable HDLObject container, @NonNull String name) {
		this(containerID, container, name, true);
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
