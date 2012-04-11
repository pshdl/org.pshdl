package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLInterfaceInstantiation extends AbstractHDLInterfaceInstantiation {
	/**
	 * Constructs a new instance of {@link HDLInterfaceInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var, ArrayList<HDLExpression> dimensions, boolean validate) {
		super(container, hIf, var, dimensions, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInterfaceInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 */
	public HDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var, ArrayList<HDLExpression> dimensions) {
		this(container, hIf, var, dimensions, true);
	}

	public HDLInterfaceInstantiation() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInterfaceInstantiation;
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	@Override
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.emptyList();
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.emptyList();
	}
	// $CONTENT-END$

}
