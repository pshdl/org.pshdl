package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

public class HDLEqualityOp extends AbstractHDLEqualityOp {
	/**
	 * Constructs a new instance of {@link HDLEqualityOp}
	 * 
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
	public HDLEqualityOp(HDLObject container, HDLExpression left, HDLExpression right, HDLEqualityOpType type, boolean validate) {
		super(container, left, right, type, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLEqualityOp}
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
	public HDLEqualityOp(HDLObject container, HDLExpression left, HDLExpression right, HDLEqualityOpType type) {
		this(container, left, right, type, true);
	}

	public HDLEqualityOp() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEqualityOp;
	}

	public static enum HDLEqualityOpType {
		EQ("=="), NOT_EQ("!="), LESS("<"), LESS_EQ("<="), GREATER(">"), GREATER_EQ(">=");
		String str;

		HDLEqualityOpType(String op) {
			this.str = op;
		}

		public static HDLEqualityOpType getOp(String op) {
			for (HDLEqualityOpType ass : values()) {
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

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
