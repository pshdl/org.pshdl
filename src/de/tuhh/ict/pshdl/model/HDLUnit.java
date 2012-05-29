package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLUnit contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<String> imports. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> statements. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnit(HDLObject container, String libURI, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements, boolean validate) {
		super(container, libURI, name, imports, statements, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 */
	public HDLUnit(HDLObject container, String libURI, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		this(container, libURI, name, imports, statements, true);
	}

	public HDLUnit() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnit;
	}

	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		unitIF = new HDLInterface().setName(getName());
		List<HDLVariableDeclaration> declarations = HDLResolver.getallVariableDeclarations(getStatements());
		for (HDLVariableDeclaration hdlVariableDeclaration : declarations) {
			switch (hdlVariableDeclaration.getDirection()) {
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(hdlVariableDeclaration.copyFiltered(CopyFilter.DEEP));
				break;
			default:
				break;
			}
			// TODO Add annotations and deeper Declarations
			// TODO add clk/rst;
		}
		return unitIF;
	}

	private HDLResolver resolver = new HDLResolver(this, false);

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		HDLEnum resolveEnum = resolver.resolveEnum(hEnum);
		if (resolveEnum != null)
			return resolveEnum;
		return (HDLEnum) resolveType(hEnum);
	}

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		HDLInterface resolveInterface = resolver.resolveInterface(hIf);
		if (resolveInterface != null)
			return resolveInterface;
		return (HDLInterface) resolveType(hIf);
	}

	@Override
	public HDLType resolveType(HDLQualifiedName type) {
		HDLType resolveType = resolver.resolveType(type);
		if (resolveType != null)
			return resolveType;
		if (library == null)
			library = HDLLibrary.getLibrary(libURI);
		return library.resolve(getName(), getImports(), type);
	}

	private HDLLibrary library;

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		HDLVariable hdlVariable = resolver.resolveVariable(var);
		if (hdlVariable != null)
			return hdlVariable;
		// String varName = var.getLastSegment();
		// if (varName.equals("$clk"))
		// return new HDLVariable(null, "$clk", null, null);
		// if (varName.equals("$rst"))
		// return new HDLVariable(null, "$rst", null, null);
		return null;
	}

	@Override
	public List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return HDLResolver.getallEnumDeclarations(statements);
	}

	@Override
	public List<HDLInterface> doGetInterfaceDeclarations() {
		return HDLResolver.getallInterfaceDeclarations(statements);
	}

	@Override
	public List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = HDLResolver.getallVariableDeclarations(statements);
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBit()).setDirection(HDLDirection.IN);
		hvd.setContainer(this);
		res.add(hvd.addVariables(new HDLVariable(null, "$clk", null, null)));
		res.add(hvd.addVariables(new HDLVariable(null, "$rst", null, null)));
		return res;
	}

	// $CONTENT-END$

}
