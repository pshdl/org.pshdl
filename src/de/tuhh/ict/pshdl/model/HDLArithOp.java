package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLArithOp contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLArithOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLArithOp extends AbstractHDLArithOp {
	/**
	 * Constructs a new instance of {@link HDLArithOp}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArithOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLArithOpType type, boolean validate) {
		super(containerID, container, left, right, type, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLArithOp}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 */
	public HDLArithOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLArithOpType type) {
		this(containerID, container, left, right, type, true);
	}

	public HDLArithOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLArithOp;
	}

	public static enum HDLArithOpType {
		MUL("*"), DIV("/"), MINUS("-"), PLUS("+"), MOD("%"), POW("**");
		String str;

		HDLArithOpType(String op) {
			this.str = op;
		}

		public static HDLArithOpType getOp(String op) {
			for (HDLArithOpType ass : values()) {
				if (ass.str.equals(op)) {
					return ass;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field type which is of type HDLArithOpType.
	 */
	public static HDLFieldAccess<HDLArithOp, HDLArithOpType> fType = new HDLFieldAccess<HDLArithOp, HDLArithOpType>() {
		@Override
		public HDLArithOpType getValue(HDLArithOp obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
