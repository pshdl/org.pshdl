package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

public class HDLEnum extends AbstractHDLEnum {
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnum(HDLObject container, String name, ArrayList<HDLVariable> enums, boolean validate) {
		super(container, name, enums, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLEnum(HDLObject container, String name, ArrayList<HDLVariable> enums) {
		this(container, name, enums, true);
	}

	public HDLEnum() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnum;
	}

	// $CONTENT-BEGIN$
	public HDLVariable getVariable(String lastSegment) {
		for (HDLVariable var : getEnums()) {
			if (var.getName().equals(lastSegment))
				return var;
		}
		return null;
	}
	// $CONTENT-END$

}
