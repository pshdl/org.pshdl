package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>.
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLDirectGeneration(HDLObject container, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments, boolean validate) {
		super(container, generatorID, generatorContent, arguments, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>.
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 */
	public HDLDirectGeneration(HDLObject container, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments) {
		this(container, generatorID, generatorContent, arguments, true);
	}
	public HDLDirectGeneration() {
		super();
	}
//$CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		// Implement the HDLInterfaceGeneration
		return Collections.singletonList(new HDLInterfaceDeclaration(null, null));
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.EMPTY_LIST;
	}
//$CONTENT-END$
}	
