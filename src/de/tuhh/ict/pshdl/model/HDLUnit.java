package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

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

	// $CONTENT-BEGIN$
	private Map<String, HDLEnum> enumCache;

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		if (enumCache == null) {
			synchronized (this) {
				List<HDLEnumDeclaration> enumDecl = HDLUtils.getallEnumDeclarations(getStatements());
				enumCache = new HashMap<String, HDLEnum>();
				for (HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(hdlEnumDeclaration.getHEnum().getName(), hdlEnumDeclaration.getHEnum());
				}
			}
		}
		if (enumCache.get(hEnum.getLastSegment()) != null)
			return enumCache.get(hEnum.getLastSegment());
		return container.resolveEnum(hEnum);
	}

	private Map<String, HDLInterface> ifCache;

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterface> ifDecl = HDLUtils.getallInterfaceDeclarations(getStatements());
				ifCache = new HashMap<String, HDLInterface>();
				for (HDLInterface hdlIfDeclaration : ifDecl) {
					ifCache.put(hdlIfDeclaration.getName(), hdlIfDeclaration);
				}
			}
		}
		if (ifCache.get(hIf.getLastSegment()) != null)
			return ifCache.get(hIf.getLastSegment());
		return library.getUnit(hIf).asInterface();
	}

	private HDLInterface asInterface() {
		HDLInterface unitIF = new HDLInterface().setName(getName());
		List<HDLVariableDeclaration> declarations = HDLUtils.getallVariableDeclarations(getStatements());
		for (HDLVariableDeclaration hdlVariableDeclaration : declarations) {
			HDLType type = hdlVariableDeclaration.resolveType();
			if (type instanceof HDLValueType) {
				HDLValueType valueType = (HDLValueType) type;
				switch (valueType.getDirection()) {
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
		return library.getUnit(type).asInterface();
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
		return container.resolveVariable(var);
	}

	public void setLibrary(HDLLibrary library) {
		this.library = library;
	}

	// $CONTENT-END$

}
