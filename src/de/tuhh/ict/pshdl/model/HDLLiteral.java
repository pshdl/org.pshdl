package de.tuhh.ict.pshdl.model;

import java.math.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLLiteral contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String val. Can <b>not</b> be <code>null</code>.</li>
 * <li>Boolean str. Can be <code>null</code>.</li>
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
	 * @param str
	 *            the value for str. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLLiteral(@Nullable IHDLObject container, @NonNull String val, @Nullable Boolean str, boolean validate) {
		super(container, val, str, validate);
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
	/**
	 * The accessor for the field str which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLLiteral, Boolean> fStr = new HDLFieldAccess<HDLLiteral, Boolean>() {
		@Override
		public Boolean getValue(HDLLiteral obj) {
			if (obj == null)
				return null;
			return obj.getStr();
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
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		switch (getPresentation()) {
		case STR:
		case BOOL:
			break;
		default:
			if (getValueAsBigInt() == null)
				throw new IllegalArgumentException("The value:" + val + " is not a valid Literal!");
		}
	}

	public static HDLLiteral get(int i) {
		return new HDLLiteral().setStr(false).setVal(Integer.toString(i));
	}

	public static enum HDLLiteralPresentation {
		HEX, BIN, NUM, STR, BOOL;
	}

	@Override
	public Boolean getStr() {
		if (super.str == null)
			return false;
		return super.str;
	}

	public HDLLiteralPresentation getPresentation() {
		if (getStr())
			return HDLLiteralPresentation.STR;
		String string = getVal();
		if ("true".equals(string))
			return HDLLiteralPresentation.BOOL;
		if ("false".equals(string))
			return HDLLiteralPresentation.BOOL;
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
		return new HDLLiteral().setStr(false).setVal(constant.toString());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLLiteral))
			return false;
		HDLLiteral other = (HDLLiteral) obj;
		if (val == null) {
			if (other.getVal() != null)
				return false;
		} else if (val.equals(other.getVal()))
			return true;
		BigInteger bigVal = getValueAsBigInt();
		BigInteger otherbigVal = other.getValueAsBigInt();
		if (bigVal != null) {
			return bigVal.equals(otherbigVal);
		}
		return true;
	}

	// $CONTENT-END$

}
