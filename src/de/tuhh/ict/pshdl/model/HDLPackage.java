package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLPackage contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String pkg. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLUnit> units. Can be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public class HDLPackage extends AbstractHDLPackage {
	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLPackage(HDLObject container, String libURI, String pkg, ArrayList<HDLUnit> units, boolean validate) {
		super(container, libURI, pkg, units, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 */
	public HDLPackage(HDLObject container, String libURI, String pkg, ArrayList<HDLUnit> units) {
		this(container, libURI, pkg, units, true);
	}

	public HDLPackage() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLPackage;
	}

	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
