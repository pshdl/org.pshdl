package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEnumRef contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName hEnum. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEnumRef extends AbstractHDLEnumRef {
	/**
	 * Constructs a new instance of {@link HDLEnumRef}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnumRef(int containerID, HDLObject container, HDLQualifiedName var, HDLQualifiedName hEnum, boolean validate) {
		super(containerID, container, var, hEnum, validate);
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
	public HDLEnumRef(int containerID, HDLObject container, HDLQualifiedName var, HDLQualifiedName hEnum) {
		this(containerID, container, var, hEnum, true);
	}

	public HDLEnumRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEnumRef;
	}

	/**
	 * The accessor for the field hEnum which is of type HDLQualifiedName
	 */
	public static HDLFieldAccess<HDLEnumRef, HDLQualifiedName> fHEnum = new HDLFieldAccess<HDLEnumRef, HDLQualifiedName>() {
		@Override
		public HDLQualifiedName getValue(HDLEnumRef obj) {
			if (obj == null)
				return null;
			return obj.getHEnumRefName();
		}
	};

	// $CONTENT-BEGIN$
	@Override
	public HDLVariable resolveVar() {
		HDLEnum resolveHEnum = resolveHEnum();
		return resolveHEnum.getVariable(getVarRefName().getLastSegment());
	}

	// $CONTENT-END$

}
