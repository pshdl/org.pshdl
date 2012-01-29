package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLVariable extends AbstractHDLVariable {
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLExpression> dimensions, HDLExpression defaultValue, boolean validate) {
		super(container, name, dimensions, defaultValue, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLExpression> dimensions, HDLExpression defaultValue) {
		this(container, name, dimensions, defaultValue, true);
	}
	public HDLVariable() {
		super();
	}
	
//$CONTENT-BEGIN$

	/**
	 * Attempt to determine the type of this HDLVariable. For this to work it
	 * needs to have a valid container.
	 * 
	 * @return the HDLType if it could be determined, <code>null</code>
	 *         otherwise.
	 */
	public HDLType determineType() {
		if (getContainer() instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration hvd = (HDLVariableDeclaration) getContainer();
			return hvd.resolveType();
		}
		if (getContainer() instanceof HDLDirectGeneration) {
			HDLDirectGeneration hdg = (HDLDirectGeneration) getContainer();
			return hdg.getHIf();
		}
		if (getContainer() instanceof HDLInterfaceInstantiation) {
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) getContainer();
			return hii.resolveHIf();
		}
		return null;
	}

//$CONTENT-END$
	
}	
