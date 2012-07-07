package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLStatement extends AbstractHDLStatement implements de.tuhh.ict.pshdl.model.utils.IStatementContainer {
	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLStatement(int containerID, @Nullable HDLObject container, boolean validate) {
		super(containerID, container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLStatement(int containerID, @Nullable HDLObject container) {
		this(containerID, container, true);
	}

	public HDLStatement() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLStatement;
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
