package de.tuhh.ict.pshdl.model;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLType contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dim. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLType(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(container, name, dim, validate);
	}

	public HDLType() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLType;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLType, String> fName = new HDLFieldAccess<HDLType, String>("name") {
		@Override
		public String getValue(HDLType obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}
	};
	/**
	 * The accessor for the field dim which is of type ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLType, ArrayList<HDLExpression>> fDim = new HDLFieldAccess<HDLType, ArrayList<HDLExpression>>("dim") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLType obj) {
			if (obj == null)
				return null;
			return obj.getDim();
		}
	};

	// $CONTENT-BEGIN$
	@Nonnull
	public HDLQualifiedName asRef() {
		return new HDLQualifiedName(getName());
	}

	public HDLExpression getWidth() {
		throw new IllegalArgumentException("Not implemented for this type:" + this);
	}
	// $CONTENT-END$

}
