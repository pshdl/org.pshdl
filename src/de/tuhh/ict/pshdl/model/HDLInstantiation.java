package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLInstantiation contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLInstantiation extends AbstractHDLInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInstantiation(HDLObject container, HDLVariable var, ArrayList<HDLArgument> arguments, boolean validate) {
		super(container, var, arguments, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 */
	public HDLInstantiation(HDLObject container, HDLVariable var, ArrayList<HDLArgument> arguments) {
		this(container, var, arguments, true);
	}

	public HDLInstantiation() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInstantiation;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
