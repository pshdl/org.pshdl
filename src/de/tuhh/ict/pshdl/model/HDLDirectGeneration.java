package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLDirectGeneration contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLVariable var. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLArgument> arguments. Can be <code>null</code>.</li>
 * <li>HDLInterface hIf. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorID. Can <b>not</b> be <code>null</code>.</li>
 * <li>String generatorContent. Can <b>not</b> be <code>null</code>.</li>
 * <li>boolean include. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param include
	 *            the value for include. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLDirectGeneration(HDLObject container, HDLVariable var, ArrayList<HDLArgument> arguments, HDLInterface hIf, String generatorID, String generatorContent,
			boolean include, boolean validate) {
		super(container, var, arguments, hIf, generatorID, generatorContent, include, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param include
	 *            the value for include. Can <b>not</b> be <code>null</code>.
	 */
	public HDLDirectGeneration(HDLObject container, HDLVariable var, ArrayList<HDLArgument> arguments, HDLInterface hIf, String generatorID, String generatorContent,
			boolean include) {
		this(container, var, arguments, hIf, generatorID, generatorContent, include, true);
	}

	public HDLDirectGeneration() {
		super();
	}

	@Override
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
