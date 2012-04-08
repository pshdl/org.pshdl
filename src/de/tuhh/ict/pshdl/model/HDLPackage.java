package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLPackage extends AbstractHDLPackage {
	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLPackage(HDLObject container, String pkg, ArrayList<HDLUnit> units, boolean validate) {
		super(container, pkg, units, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 */
	public HDLPackage(HDLObject container, String pkg, ArrayList<HDLUnit> units) {
		this(container, pkg, units, true);
	}

	public HDLPackage() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLPackage;
	}

	// $CONTENT-BEGIN$

	private HDLLibrary library;

	public void setLibrary(HDLLibrary library) {
		this.library = library;
		for (HDLUnit unit : getUnits())
			unit.setLibrary(library);
	}
	// $CONTENT-END$

}
