package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLOpExpression contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLOpExpression extends AbstractHDLOpExpression {
	/**
	 * Constructs a new instance of {@link HDLOpExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLOpExpression(@Nullable IHDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, boolean validate) {
		super(container, left, right, validate);
	}

	public HDLOpExpression() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLOpExpression;
	}

	/**
	 * The accessor for the field left which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLOpExpression, HDLExpression> fLeft = new HDLFieldAccess<HDLOpExpression, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLOpExpression obj) {
			if (obj == null)
				return null;
			return obj.getLeft();
		}
	};
	/**
	 * The accessor for the field right which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLOpExpression, HDLExpression> fRight = new HDLFieldAccess<HDLOpExpression, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLOpExpression obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}
	};

	// $CONTENT-BEGIN$
	public abstract Enum<?> getType();
	// $CONTENT-END$

}
