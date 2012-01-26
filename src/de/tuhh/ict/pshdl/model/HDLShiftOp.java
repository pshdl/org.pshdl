package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLShiftOp extends AbstractHDLShiftOp {
	/**
	 * Constructs a new instance of {@link HDLShiftOp}
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
	public HDLShiftOp(HDLObject container, HDLExpression left, HDLExpression right, HDLShiftOpType type, boolean validate) {
		super(container, left, right, type, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLShiftOp}
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
	public HDLShiftOp(HDLObject container, HDLExpression left, HDLExpression right, HDLShiftOpType type) {
		this(container, left, right, type, true);
	}
	public HDLShiftOp() {
		super();
	}
	 	public static enum HDLShiftOpType {
			SLL,SRA,SRL,
		}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
