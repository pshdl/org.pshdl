package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLFunction contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunction extends AbstractHDLFunction {
	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLFunction(HDLObject container, String name, ArrayList<HDLExpression> params, boolean validate) {
		super(container, name, params, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 */
	public HDLFunction(HDLObject container, String name, ArrayList<HDLExpression> params) {
		this(container, name, params, true);
	}

	public HDLFunction() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunction;
	}

	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
