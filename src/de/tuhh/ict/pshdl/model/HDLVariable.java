package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

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
	 *            if <code>true</code> the paramaters will be validated.
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

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLVariable;
	}

	// $CONTENT-BEGIN$

	public HDLRegisterConfig getRegisterConfig() {
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration vhd = (HDLVariableDeclaration) container;
			return vhd.getRegister();
		}
		return null;
	}

	public HDLQualifiedName asRef() {
		return HDLQualifiedName.create(getName());
	}

	// $CONTENT-END$

}
