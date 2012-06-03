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
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLReference(HDLObject container, HDLQualifiedName var, boolean validate) {
		super(container, var, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLReference}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 */
	public HDLReference(HDLObject container, HDLQualifiedName var) {
		this(container, var, true);
	}

	public HDLReference() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLReference;
	}

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
