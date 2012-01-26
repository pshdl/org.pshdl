package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLRange extends AbstractHDLRange {
	/**
	 * Constructs a new instance of {@link HDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLRange(HDLObject container, HDLExpression from, HDLExpression to, boolean validate) {
		super(container, from, to, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 */
	public HDLRange(HDLObject container, HDLExpression from, HDLExpression to) {
		this(container, from, to, true);
	}
	public HDLRange() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
