package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLReference contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLReference extends AbstractHDLReference {
	/**
	 * Constructs a new instance of {@link HDLReference}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLReference(int containerID, HDLObject container, HDLQualifiedName var, boolean validate) {
		super(containerID, container, var, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLReference}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 */
	public HDLReference(int containerID, HDLObject container, HDLQualifiedName var) {
		this(containerID, container, var, true);
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

	/**
	 * The accessor for the field var which is of type HDLQualifiedName
	 */
	public static HDLFieldAccess<HDLReference, HDLQualifiedName> fVar = new HDLFieldAccess<HDLReference, HDLQualifiedName>() {
		@Override
		public HDLQualifiedName getValue(HDLReference obj) {
			if (obj == null)
				return null;
			return obj.getVarRefName();
		}
	};

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
