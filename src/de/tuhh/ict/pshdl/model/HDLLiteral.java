package de.tuhh.ict.pshdl.model;

import java.math.*;

import org.eclipse.jdt.annotation.*;

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
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLLiteral(int containerID, @Nullable HDLObject container, @NonNull String val, boolean validate) {
		super(containerID, container, val, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 */
	public HDLLiteral(int containerID, @Nullable HDLObject container, @NonNull String val) {
		this(containerID, container, val, true);
	}

	public HDLLiteral() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLLiteral;
	}

	/**
	 * The accessor for the field val which is of type String.
	 */
	public static HDLFieldAccess<HDLLiteral, String> fVal = new HDLFieldAccess<HDLLiteral, String>() {
		@Override
		public String getValue(HDLLiteral obj) {
			if (obj == null)
				return null;
			return obj.getVal();
		}
	};
	// $CONTENT-BEGIN$

	private BigInteger bigIntegerVal = null;

	public BigInteger getValueAsBigInt() {
		if (bigIntegerVal != null)
			return bigIntegerVal;
		String string = getVal();
		char zeroChar = string.charAt(0);
		if (zeroChar == '0') {
			if (string.length() > 1) {
				String str = string.substring(2).replaceAll("_", "");
				switch (string.charAt(1)) {
				case 'x':
					bigIntegerVal = new BigInteger(str, 16);
					break;
				case 'b':
					bigIntegerVal = new BigInteger(str, 2);
					break;
				}
				return bigIntegerVal;
			}
			bigIntegerVal = BigInteger.ZERO;
			return bigIntegerVal;
		}
		bigIntegerVal = new BigInteger(string.replaceAll("_", ""));
		return bigIntegerVal;
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
