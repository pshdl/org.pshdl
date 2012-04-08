package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

public abstract class HDLStatement extends AbstractHDLStatement {
	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLStatement(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLStatement(HDLObject container) {
		this(container, true);
	}

	public HDLStatement() {
		super();
	}

	// $CONTENT-BEGIN$
	private Map<String, HDLEnum> enumCache;

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		if (enumCache == null) {
			synchronized (this) {
				List<HDLEnumDeclaration> enumDecl = getAllObjectsOf(HDLEnumDeclaration.class, false);
				enumCache = new HashMap<String, HDLEnum>();
				for (HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(hdlEnumDeclaration.getHEnum().getName(), hdlEnumDeclaration.getHEnum());
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		if (enumCache.get(hEnum.getLastSegment()) != null)
			return enumCache.get(hEnum.getLastSegment());
		return container.resolveEnum(hEnum);
	}

	abstract protected List<HDLEnumDeclaration> doGetEnumDeclarations();

	private Map<String, HDLInterface> ifCache;

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterface> ifDecl = doGetInterfaceDeclarations();
				ifCache = new HashMap<String, HDLInterface>();
				for (HDLInterface hdlIfDeclaration : ifDecl) {
					ifCache.put(hdlIfDeclaration.getName(), hdlIfDeclaration);
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		if (ifCache.get(hIf.getLastSegment()) != null)
			return ifCache.get(hIf.getLastSegment());
		return container.resolveInterface(hIf);
	}

	abstract protected List<HDLInterface> doGetInterfaceDeclarations();

	private Map<String, HDLType> typeCache;

	@Override
	public HDLType resolveType(HDLQualifiedName var) {
		if (typeCache == null) {
			synchronized (this) {
				List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = new HashMap<String, HDLType>();
				for (HDLType hdlTypeDeclaration : typeDecl) {
					// Primitives do not have a name and can thus not be
					// resolved..
					if (hdlTypeDeclaration instanceof HDLEnum) {
						HDLEnum hEnum = (HDLEnum) hdlTypeDeclaration;
						typeCache.put(hEnum.getName(), hEnum);
					}
					if (hdlTypeDeclaration instanceof HDLInterface) {
						HDLInterface hIf = (HDLInterface) hdlTypeDeclaration;
						typeCache.put(hIf.getName(), hIf);
					}
				}
			}
		}
		if (typeCache.get(var.getLastSegment()) != null)
			return typeCache.get(var.getLastSegment());
		return container.resolveType(var);
	}

	protected List<HDLType> doGetTypeDeclarations() {
		List<HDLType> types = new LinkedList<HDLType>();
		for (HDLEnumDeclaration hEnumDecl : doGetEnumDeclarations()) {
			types.add(hEnumDecl.getHEnum());
		}
		for (HDLVariableDeclaration varDecl : doGetVariableDeclarations()) {
			if (varDecl.getTypeRefName().getLastSegment().startsWith("#"))
				types.add(HDLPrimitive.forName(varDecl.getTypeRefName()));
		}
		for (HDLInterface ifDecl : doGetInterfaceDeclarations()) {
			types.add(ifDecl);
		}
		return types;
	}

	private Map<String, HDLVariable> variableCache;

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				List<HDLVariableDeclaration> varDecl = doGetVariableDeclarations();
				variableCache = new HashMap<String, HDLVariable>();
				for (HDLVariableDeclaration hdlVarDeclaration : varDecl) {
					for (HDLVariable declVars : hdlVarDeclaration.getVariables()) {
						variableCache.put(declVars.getName(), declVars);
					}
				}
			}
		}
		if (variableCache.get(var.getLastSegment()) != null)
			return variableCache.get(var.getLastSegment());
		return container.resolveVariable(var);
	}

	abstract protected List<HDLVariableDeclaration> doGetVariableDeclarations();
	// $CONTENT-END$

}
