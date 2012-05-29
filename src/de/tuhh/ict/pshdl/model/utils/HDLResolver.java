package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLResolver {

	private boolean descent;

	private Map<String, HDLEnum> enumCache;

	private Map<String, HDLInterface> ifCache;

	private final IStatementContainer resolveTo;

	private Map<String, HDLType> typeCache;

	private Map<String, HDLVariable> variableCache;

	public HDLResolver(IStatementContainer resolveTo, boolean descent) {
		super();
		this.resolveTo = resolveTo;
		this.descent = descent;
	}

	protected List<HDLType> doGetTypeDeclarations() {
		List<HDLType> types = new LinkedList<HDLType>();
		for (HDLEnumDeclaration hEnumDecl : resolveTo.doGetEnumDeclarations()) {
			types.add(hEnumDecl.getHEnum());
		}
		for (HDLVariableDeclaration varDecl : resolveTo.doGetVariableDeclarations()) {
			if (varDecl.getPrimitive() != null)
				types.add(varDecl.getPrimitive());
		}
		for (HDLInterface ifDecl : resolveTo.doGetInterfaceDeclarations()) {
			types.add(ifDecl);
		}
		return types;
	}

	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		if (enumCache == null) {
			synchronized (this) {
				List<HDLEnumDeclaration> enumDecl = resolveTo.getAllObjectsOf(HDLEnumDeclaration.class, false);
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
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveEnum(hEnum);
	}

	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterface> ifDecl = resolveTo.doGetInterfaceDeclarations();
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
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveInterface(hIf);
	}

	public HDLType resolveType(HDLQualifiedName var) {
		if (typeCache == null) {
			synchronized (this) {
				List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = new HashMap<String, HDLType>();
				for (HDLType hdlTypeDeclaration : typeDecl) {
					typeCache.put(hdlTypeDeclaration.getName(), hdlTypeDeclaration);
				}
			}
		}
		if (typeCache.get(var.getLastSegment()) != null)
			return typeCache.get(var.getLastSegment());
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveType(var);
	}

	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				List<HDLVariableDeclaration> varDecl = resolveTo.doGetVariableDeclarations();
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
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveVariable(var);
	}

	public static List<HDLEnumDeclaration> getallEnumDeclarations(List<HDLStatement> stmnts) {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(hdlStatement.doGetEnumDeclarations());
		}
		return res;
	}

	public static List<HDLInterface> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(hdlStatement.doGetInterfaceDeclarations());
		}
		return res;
	}

	public static List<HDLVariableDeclaration> getallVariableDeclarations(List<HDLStatement> stmnts) {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(hdlStatement.doGetVariableDeclarations());
		}
		return res;
	}
}
