package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLShiftOp contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLShiftOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLShiftOp extends AbstractHDLShiftOp {
	/**
	 * Constructs a new instance of {@link HDLShiftOp}
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
	public HDLShiftOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLShiftOpType type, boolean validate) {
		super(containerID, container, left, right, type, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLShiftOp}
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
	public HDLShiftOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLShiftOpType type) {
		this(containerID, container, left, right, type, true);
	}

	public HDLShiftOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLShiftOp;
	}

	public static enum HDLShiftOpType {
		SLL("<<"), SRA(">>"), SRL(">>>");
		String str;

		HDLShiftOpType(String op) {
			this.str = op;
		}

		public static HDLShiftOpType getOp(String op) {
			for (HDLShiftOpType ass : values()) {
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
	 * The accessor for the field type which is of type HDLShiftOpType.
	 */
	public static HDLFieldAccess<HDLShiftOp, HDLShiftOpType> fType = new HDLFieldAccess<HDLShiftOp, HDLShiftOpType>() {
		@Override
		public HDLShiftOpType getValue(HDLShiftOp obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
