package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

import java.math.*;

public class HDLLiteral extends AbstractHDLLiteral {
	/**
	 * Constructs a new instance of {@link HDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
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
	
//$CONTENT-BEGIN$

	@Override
	protected String validateVal(String val) {
		String validateVal = super.validateVal(val);
		if (getValueAsBigInt() == null)
			throw new IllegalArgumentException("Literal String is " + validateVal + " is not a valid literal.");
		return validateVal;
	}

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
					new BigInteger(str, 2);
				}
			} else {
				return BigInteger.ZERO;
			}
		}
		return new BigInteger(string.replaceAll("_", ""));
	}

//$CONTENT-END$
	
}	
