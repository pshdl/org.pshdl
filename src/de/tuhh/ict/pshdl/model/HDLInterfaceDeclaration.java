package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public class HDLInterfaceDeclaration extends AbstractHDLInterfaceDeclaration {
	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceDeclaration(HDLObject container, HDLInterface hIf, boolean validate) {
		super(container, hIf, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 */
	public HDLInterfaceDeclaration(HDLObject container, HDLInterface hIf) {
		this(container, hIf, true);
	}

	public HDLInterfaceDeclaration() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.singletonList(getHIf());
	}
	// $CONTENT-END$

}
