package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLTernary contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression ifExpr. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression thenExpr. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression elseExpr. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLTernary extends AbstractHDLTernary {
	/**
	 * Constructs a new instance of {@link HDLTernary}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExpr
	 *            the value for ifExpr. Can <b>not</b> be <code>null</code>.
	 * @param thenExpr
	 *            the value for thenExpr. Can <b>not</b> be <code>null</code>.
	 * @param elseExpr
	 *            the value for elseExpr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLTernary(int containerID, @Nullable HDLObject container, @NonNull HDLExpression ifExpr, @NonNull HDLExpression thenExpr, @NonNull HDLExpression elseExpr,
			boolean validate) {
		super(containerID, container, ifExpr, thenExpr, elseExpr, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLTernary}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExpr
	 *            the value for ifExpr. Can <b>not</b> be <code>null</code>.
	 * @param thenExpr
	 *            the value for thenExpr. Can <b>not</b> be <code>null</code>.
	 * @param elseExpr
	 *            the value for elseExpr. Can <b>not</b> be <code>null</code>.
	 */
	public HDLTernary(int containerID, @Nullable HDLObject container, @NonNull HDLExpression ifExpr, @NonNull HDLExpression thenExpr, @NonNull HDLExpression elseExpr) {
		this(containerID, container, ifExpr, thenExpr, elseExpr, true);
	}

	public HDLTernary() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLTernary;
	}

	/**
	 * The accessor for the field ifExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fIfExpr = new HDLFieldAccess<HDLTernary, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getIfExpr();
		}
	};
	/**
	 * The accessor for the field thenExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fThenExpr = new HDLFieldAccess<HDLTernary, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getThenExpr();
		}
	};
	/**
	 * The accessor for the field elseExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fElseExpr = new HDLFieldAccess<HDLTernary, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getElseExpr();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
