package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLConcat contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> cats. Can <b>not</b> be <code>null</code>,
 * additionally the collection must contain at least one element.</li>
 * </ul>
 */
public class HDLConcat extends AbstractHDLConcat {
	/**
	 * Constructs a new instance of {@link HDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
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
	 *            the value for cats. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 */
	public HDLConcat(HDLObject container, ArrayList<HDLExpression> cats) {
		this(container, cats, true);
	}

	public HDLConcat() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLConcat;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
