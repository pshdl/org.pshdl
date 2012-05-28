package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLDeclaration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */

public abstract class HDLDeclaration extends AbstractHDLDeclaration {
	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDeclaration(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLDeclaration(HDLObject container) {
		this(container, true);
	}

	public HDLDeclaration() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLDeclaration;
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
	protected List<HDLType> doGetTypeDeclarations() {
		return Collections.emptyList();
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.emptyList();
	}
	// $CONTENT-END$

}
