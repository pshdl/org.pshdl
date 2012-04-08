package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

public class HDLArithOp extends AbstractHDLArithOp {
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
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLArithOp(HDLObject container, HDLExpression left, HDLExpression right, HDLArithOpType type, boolean validate) {
		super(container, left, right, type, validate);
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
	public HDLArithOp(HDLObject container, HDLExpression left, HDLExpression right, HDLArithOpType type) {
		this(container, left, right, type, true);
	}

	public HDLArithOp() {
		super();
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

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
