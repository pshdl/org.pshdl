/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model;

import java.math.*;

import javax.annotation.*;

import org.pshdl.model.impl.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLLiteral(int id, @Nullable IHDLObject container, @Nonnull String val, @Nullable Boolean str, boolean validate) {
		super(id, container, val, str, validate);
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
	public static HDLFieldAccess<HDLLiteral, String> fVal = new HDLFieldAccess<HDLLiteral, String>("val") {
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
	public static HDLFieldAccess<HDLLiteral, Boolean> fStr = new HDLFieldAccess<HDLLiteral, Boolean>("str") {
		@Override
		public Boolean getValue(HDLLiteral obj) {
			if (obj == null)
				return null;
			return obj.getStr();
		}
	};
	// $CONTENT-BEGIN$
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	private BigInteger bigIntegerVal = null;

	public BigInteger getValueAsBigInt() {
		if (bigIntegerVal != null)
			return bigIntegerVal;
		if (getStr())
			return null;
		String string = getVal();
		if (TRUE.equals(string))
			return null;
		// bigIntegerVal = BigInteger.ONE;
		// return bigIntegerVal;
		if (FALSE.equals(string))
			return null;
		// bigIntegerVal = BigInteger.ZERO;
		// return bigIntegerVal;
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

	/**
	 * Generate a literal with the given Value as decimal
	 * 
	 * @param val
	 * @return
	 */
	public static @Nonnull
	HDLLiteral get(long val) {
		return new HDLLiteral().setStr(false).setVal(Long.toString(val));
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
		if (TRUE.equals(string))
			return HDLLiteralPresentation.BOOL;
		if (FALSE.equals(string))
			return HDLLiteralPresentation.BOOL;
		char zeroChar = string.charAt(0);
		if (zeroChar == '0')
			if (string.length() > 1) {
				switch (string.charAt(1)) {
				case 'x':
					return HDLLiteralPresentation.HEX;
				case 'b':
					return HDLLiteralPresentation.BIN;
				}
			} else
				return HDLLiteralPresentation.NUM;
		return HDLLiteralPresentation.NUM;
	}

	public static @Nonnull
	HDLLiteral get(@Nonnull BigInteger constant) {
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
		BigInteger bigVal = getValueAsBigInt();
		BigInteger otherbigVal = other.getValueAsBigInt();
		if (bigVal != null)
			return bigVal.equals(otherbigVal);
		if (val == null) {
			if (other.getVal() != null)
				return false;
		} else if (val.equals(other.getVal()))
			return true;
		return true;
	}

	private Integer hashCache = null;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		BigInteger valueAsBigInt = getValueAsBigInt();
		if (valueAsBigInt != null) {
			hashCache = valueAsBigInt.hashCode();
			return hashCache;
		}
		return super.hashCode();
	}

	public static HDLLiteral getFalse() {
		return new HDLLiteral().setVal(FALSE);
	}

	public static HDLLiteral getTrue() {
		return new HDLLiteral().setVal(TRUE);
	}

	public static HDLExpression getString(String string) {
		return new HDLLiteral().setStr(true).setVal(string);
	}

	// $CONTENT-END$

}
