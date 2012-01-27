package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

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

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLType> doGetTypeDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.EMPTY_LIST;
	}
	// $CONTENT-END$

}
