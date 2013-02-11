package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLReference contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLReference extends AbstractHDLReference {
	/**
	 * Constructs a new instance of {@link HDLReference}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLReference(@Nullable IHDLObject container, boolean validate) {
		super(container, validate);
	}

	public HDLReference() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLReference;
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
