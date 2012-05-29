package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLDirectGeneration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorID. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorContent. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLGeneratorArgument> arguments. Can be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDirectGeneration(HDLObject container, HDLInterface hIf, HDLVariable var, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments,
			boolean validate) {
		super(container, hIf, var, generatorID, generatorContent, arguments, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 */
	public HDLDirectGeneration(HDLObject container, HDLInterface hIf, HDLVariable var, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments) {
		this(container, hIf, var, generatorID, generatorContent, arguments, true);
	}

	public HDLDirectGeneration() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLDirectGeneration;
	}

	// $CONTENT-BEGIN$

	@Override
	public HDLInterface getHIf() {
		return HDLGenerators.getInterface(this).setContainer(this);
	}

	public HDLQualifiedName getIfName() {
		return super.getHIf().asRef();
	}

	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.emptyList();
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return Collections.singletonList(getHIf());
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(getHIf().copy()).addVariables(getVar().copy());
		hvd.setContainer(this);
		return Collections.singletonList(hvd);
	}
	// $CONTENT-END$

}
