package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

public class HDLBitOp extends AbstractHDLBitOp {
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
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLBitOp(HDLObject container, HDLExpression left, HDLExpression right, HDLBitOpType type, boolean validate) {
		super(container, left, right, type, validate);
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
	public HDLBitOp(HDLObject container, HDLExpression left, HDLExpression right, HDLBitOpType type) {
		this(container, left, right, type, true);
	}

	public HDLBitOp() {
		super();
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

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
