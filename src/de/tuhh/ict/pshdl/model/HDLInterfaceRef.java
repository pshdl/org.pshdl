package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLInterfaceRef extends AbstractHDLInterfaceRef {
	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceRef(HDLObject container, HDLQualifiedName hIf, HDLVariableRef var, ArrayList<HDLExpression> array, boolean validate) {
		super(container, hIf, var, array, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 */
	public HDLInterfaceRef(HDLObject container, HDLQualifiedName hIf, HDLVariableRef var, ArrayList<HDLExpression> array) {
		this(container, hIf, var, array, true);
	}
	public HDLInterfaceRef() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
