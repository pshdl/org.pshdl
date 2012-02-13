package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;


public class HDLEnumRef extends AbstractHDLEnumRef {
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumRef(HDLObject container, HDLQualifiedName hEnum, HDLQualifiedName var, boolean validate) {
		super(container, hEnum, var, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 */
	public HDLEnumRef(HDLObject container, HDLQualifiedName hEnum, HDLQualifiedName var) {
		this(container, hEnum, var, true);
	}
	public HDLEnumRef() {
		super();
	}
	
//$CONTENT-BEGIN$
	@Override
	public HDLVariable resolveVar() {
		HDLEnum resolveHEnum = resolveHEnum();
		return resolveHEnum.getVariable(getVarRefName().getLastSegment());
	}

//$CONTENT-END$
	
}	
