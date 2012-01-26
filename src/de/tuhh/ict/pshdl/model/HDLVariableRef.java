package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLVariableRef extends AbstractHDLVariableRef {
	/**
	 * Constructs a new instance of {@link HDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits, boolean validate) {
		super(container, var, array, bits, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 */
	public HDLVariableRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits) {
		this(container, var, array, bits, true);
	}
	public HDLVariableRef() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
