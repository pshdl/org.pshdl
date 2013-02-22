package de.tuhh.ict.pshdl.model;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLResolvedRef contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName var. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLResolvedRef extends AbstractHDLResolvedRef {
	/**
	 * Constructs a new instance of {@link HDLResolvedRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLResolvedRef(@Nullable IHDLObject container, @Nonnull HDLQualifiedName var, boolean validate) {
		super(container, var, validate);
	}

	public HDLResolvedRef() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLResolvedRef;
	}

	/**
	 * The accessor for the field var which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLResolvedRef, HDLQualifiedName> fVar = new HDLFieldAccess<HDLResolvedRef, HDLQualifiedName>("var") {
		@Override
		public HDLQualifiedName getValue(HDLResolvedRef obj) {
			if (obj == null)
				return null;
			return obj.getVarRefName();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
