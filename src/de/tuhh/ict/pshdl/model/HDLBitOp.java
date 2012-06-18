package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLBitOp contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLBitOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLBitOp extends AbstractHDLBitOp {
	/**
	 * Constructs a new instance of {@link HDLBitOp}
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
	public HDLBitOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLBitOpType type, boolean validate) {
		super(containerID, container, left, right, type, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLBitOp}
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
	public HDLBitOp(int containerID, @Nullable HDLObject container, @NonNull HDLExpression left, @NonNull HDLExpression right, @NonNull HDLBitOpType type) {
		this(containerID, container, left, right, type, true);
	}

	public HDLBitOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLBitOp;
	}

	public static enum HDLBitOpType {
		AND("&"), OR("|"), XOR("^"), LOGI_AND("&&"), LOGI_OR("||");
		String str;

		HDLBitOpType(String op) {
			this.str = op;
		}

		public static HDLBitOpType getOp(String op) {
			for (HDLBitOpType ass : values()) {
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
	 * The accessor for the field type which is of type HDLBitOpType.
	 */
	public static HDLFieldAccess<HDLBitOp, HDLBitOpType> fType = new HDLFieldAccess<HDLBitOp, HDLBitOpType>() {
		@Override
		public HDLBitOpType getValue(HDLBitOp obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
