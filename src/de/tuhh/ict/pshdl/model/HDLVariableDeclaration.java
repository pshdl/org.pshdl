package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLVariableDeclaration extends AbstractHDLVariableDeclaration {
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLType type, ArrayList<HDLVariable> variables, boolean validate) {
		super(container, type, variables, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public HDLVariableDeclaration(HDLObject container, HDLType type, ArrayList<HDLVariable> variables) {
		this(container, type, variables, true);
	}
	public HDLVariableDeclaration() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
