package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
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
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var, boolean validate) {
		super(container, hIf, var, validate);
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
	 */
	public HDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var) {
		this(container, hIf, var, true);
	}
	public HDLInterfaceInstantiation() {
		super();
	}
//$CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.singletonList(new HDLVariableDeclaration(this, resolveInterface(hIf), asList(var)));
	}
//$CONTENT-END$
}	
