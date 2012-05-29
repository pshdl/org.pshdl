package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLEnumRef contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName hEnum. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public class HDLEnumRef extends AbstractHDLEnumRef {
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumRef(HDLObject container, HDLQualifiedName var, HDLQualifiedName hEnum, boolean validate) {
		super(container, var, hEnum, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 */
	public HDLEnumRef(HDLObject container, HDLQualifiedName var, HDLQualifiedName hEnum) {
		this(container, var, hEnum, true);
	}

	public HDLEnumRef() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLEnumRef;
	}

	// $CONTENT-BEGIN$
	@Override
	public HDLVariable resolveVar() {
		HDLEnum resolveHEnum = resolveHEnum();
		return resolveHEnum.getVariable(getVarRefName().getLastSegment());
	}

	// $CONTENT-END$

}
