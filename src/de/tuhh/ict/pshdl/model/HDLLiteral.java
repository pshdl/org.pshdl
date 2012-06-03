package de.tuhh.ict.pshdl.model;

import java.math.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLLiteral contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String val. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLLiteral extends AbstractHDLLiteral {
	/**
	 * Constructs a new instance of {@link HDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLLiteral(HDLObject container, String val, boolean validate) {
		super(container, val, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 */
	public HDLLiteral(HDLObject container, String val) {
		this(container, val, true);
	}

	public HDLLiteral() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLLiteral;
	}

	public static HDLFieldAccess<HDLLiteral, String> fVal = new HDLFieldAccess<HDLLiteral, String>() {
		@Override
		public String getValue(HDLLiteral obj) {
			if (obj == null)
				return null;
			return obj.getVal();
		}
	};

	// $CONTENT-BEGIN$

	public BigInteger getValueAsBigInt() {
		String string = getVal();
		char zeroChar = string.charAt(0);
		if (zeroChar == '0') {
			if (string.length() > 1) {
				String str = string.substring(2).replaceAll("_", "");
				switch (string.charAt(1)) {
				case 'x':
					return new BigInteger(str, 16);
				case 'b':
					return new BigInteger(str, 2);
				}
			} else {
				return BigInteger.ZERO;
			}
		}
		return new BigInteger(string.replaceAll("_", ""));
	}

	@Override
	public void validateAllFields(HDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		if (getValueAsBigInt() == null)
			throw new IllegalArgumentException("The value:" + val + " is not a valid Literal!");
	}

	public static HDLLiteral get(int i) {
		return new HDLLiteral().setVal(Integer.toString(i));
	}

	public static enum HDLLiteralPresentation {
		HEX, BIN, NUM;
	}

	public HDLLiteralPresentation getPresentation() {
		String string = getVal();
		char zeroChar = string.charAt(0);
		if (zeroChar == '0') {
			if (string.length() > 1) {
				switch (string.charAt(1)) {
				case 'x':
					return HDLLiteralPresentation.HEX;
				case 'b':
					return HDLLiteralPresentation.BIN;
				}
			} else {
				return HDLLiteralPresentation.NUM;
			}
		}
		return HDLLiteralPresentation.NUM;
	}

	public static HDLExpression get(BigInteger constant) {
		return new HDLLiteral().setVal(constant.toString());
	}

	// $CONTENT-END$

}
