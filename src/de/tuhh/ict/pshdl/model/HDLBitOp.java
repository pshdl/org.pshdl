package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
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
	 *			  if <code>true</code> the paramaters will be validated.
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
			AND,OR,XOR,LOGI_AND,LOGI_OR,
		}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
