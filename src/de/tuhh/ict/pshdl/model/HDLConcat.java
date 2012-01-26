package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLConcat extends AbstractHDLConcat {
	/**
	 * Constructs a new instance of {@link HDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLConcat(HDLObject container, ArrayList<HDLExpression> cats, boolean validate) {
		super(container, cats, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public HDLConcat(HDLObject container, ArrayList<HDLExpression> cats) {
		this(container, cats, true);
	}
	public HDLConcat() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
