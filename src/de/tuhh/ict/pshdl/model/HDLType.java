package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLType(HDLObject container, String name, boolean validate) {
		super(container, name, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 */
	public HDLType(HDLObject container, String name) {
		this(container, name, true);
	}

	public HDLType() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLType;
	}

	// $CONTENT-BEGIN$
	public HDLQualifiedName asRef() {
		return new HDLQualifiedName(getName());
	}
	// $CONTENT-END$

}
