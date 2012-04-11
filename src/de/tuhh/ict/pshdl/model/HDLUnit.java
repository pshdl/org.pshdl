package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnit(HDLObject container, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements, boolean validate) {
		super(container, name, imports, statements, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 */
	public HDLUnit(HDLObject container, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		this(container, name, imports, statements, true);
	}

	public HDLUnit() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnit;
	}

	// $CONTENT-BEGIN$

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		return (HDLEnum) resolveType(hEnum);
	}

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		return (HDLInterface) resolveType(hIf);
	}

	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		unitIF = new HDLInterface().setName(getName());
		List<HDLVariableDeclaration> declarations = HDLUtils.getallVariableDeclarations(getStatements());
		for (HDLVariableDeclaration hdlVariableDeclaration : declarations) {
			switch (hdlVariableDeclaration.getDirection()) {
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(hdlVariableDeclaration.copy());
				break;
			default:
				break;
			}
			// TODO Add annotations and deeper Declarations
			// TODO add clk/rst;
		}
		return unitIF;
	}

	private Map<String, HDLType> typeCache;

	@Override
	public HDLType resolveType(HDLQualifiedName type) {
		if (typeCache == null) {
			synchronized (this) {
				List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = new HashMap<String, HDLType>();
				for (HDLType hdlTypeDeclaration : typeDecl) {
					typeCache.put(hdlTypeDeclaration.getName(), hdlTypeDeclaration);
				}
			}
		}
		if (typeCache.get(type.getLastSegment()) != null)
			return typeCache.get(type.getLastSegment());
		if (type.getLastSegment().startsWith("#"))
			return HDLPrimitive.forName(type);
		return library.resolve(getName(), getImports(), type);
	}

	protected List<HDLType> doGetTypeDeclarations() {
		List<HDLType> types = new LinkedList<HDLType>();
		for (HDLEnumDeclaration hEnumDecl : HDLUtils.getallEnumDeclarations(getStatements())) {
			types.add(hEnumDecl.getHEnum());
		}
		for (HDLVariableDeclaration varDecl : HDLUtils.getallVariableDeclarations(getStatements())) {
			if (varDecl.getTypeRefName().getLastSegment().startsWith("#"))
				types.add(HDLPrimitive.forName(varDecl.getTypeRefName()));
		}
		for (HDLInterface ifDecl : HDLUtils.getallInterfaceDeclarations(getStatements())) {
			types.add(ifDecl);
		}
		return types;
	}

	private Map<String, HDLVariable> variableCache;
	private HDLLibrary library;

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				List<HDLVariable> varDecl = HDLUtils.getallVariables(getStatements());
				variableCache = new HashMap<String, HDLVariable>();
				for (HDLVariable vars : varDecl) {
					variableCache.put(vars.getName(), vars);
				}
			}
		}
		if (variableCache.get(var.getLastSegment()) != null)
			return variableCache.get(var.getLastSegment());
		if (var.getLastSegment().equals("$clk"))
			return new HDLVariable(null, "$clk", null, null);
		if (var.getLastSegment().equals("$rst"))
			return new HDLVariable(null, "$rst", null, null);
		return null;
	}

	public void setLibrary(HDLLibrary library) {
		this.library = library;
	}

	// $CONTENT-END$

}
