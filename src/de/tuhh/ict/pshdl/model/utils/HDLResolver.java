package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public class HDLResolver {

	private boolean descent;

	private Map<HDLQualifiedName, HDLEnum> enumCache;

	private Map<HDLQualifiedName, HDLInterface> ifCache;

	private final IStatementContainer resolveTo;

	private Map<HDLQualifiedName, HDLType> typeCache;

	private Map<HDLQualifiedName, HDLVariable> variableCache;

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
				enumCache = new HashMap<HDLQualifiedName, HDLEnum>();
				for (HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(hdlEnumDeclaration.getHEnum().getFullName(), hdlEnumDeclaration.getHEnum());
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		HDLEnum checkCache = checkCache(hEnum, enumCache);
		if (checkCache != null)
			return checkCache;
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveEnum(hEnum);
	}

	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterface> ifDecl = resolveTo.doGetInterfaceDeclarations();
				ifCache = new HashMap<HDLQualifiedName, HDLInterface>();
				for (HDLInterface hdlIfDeclaration : ifDecl) {
					ifCache.put(hdlIfDeclaration.getFullName(), hdlIfDeclaration);
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		HDLInterface checkCache = checkCache(hIf, ifCache);
		if (checkCache != null)
			return checkCache;
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveInterface(hIf);
	}

	public HDLType resolveType(HDLQualifiedName var) {
		if (typeCache == null) {
			synchronized (this) {
				List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = new HashMap<HDLQualifiedName, HDLType>();
				for (HDLType hdlTypeDeclaration : typeDecl) {
					typeCache.put(hdlTypeDeclaration.getFullName(), hdlTypeDeclaration);
				}
			}
		}
		HDLType checkCache = checkCache(var, typeCache);
		if (checkCache != null)
			return checkCache;
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return resolveTo.getContainer().resolveType(var);
	}

	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				List<HDLVariableDeclaration> varDecl = resolveTo.doGetVariableDeclarations();
				variableCache = new HashMap<HDLQualifiedName, HDLVariable>();
				for (HDLVariableDeclaration hdlVarDeclaration : varDecl) {
					for (HDLVariable declVars : hdlVarDeclaration.getVariables()) {
						variableCache.put(declVars.getFullName(), declVars);
					}
				}
			}
		}
		HDLVariable checkCache = checkCache(var, variableCache);
		if (checkCache != null)
			return checkCache;
		if (var.length > 1) {
			HDLQualifiedName skipLast = var.skipLast(1);
			if (!resolveTo.getFullName().equals(skipLast)) {
				HDLType type = resolveType(skipLast);
				if (type != null) {
					HDLVariable variable = type.resolveVariable(var);
					if (variable != null)
						return variable;
				}
			}
		}
		HDLObject container = resolveTo.getContainer();
		if ((container == null) || !descent)
			return null;
		return container.resolveVariable(var);
	}

	private <T> T checkCache(HDLQualifiedName var, Map<HDLQualifiedName, T> map) {
		if (map.get(var) != null)
			return map.get(var);
		if (var.length == 1) {
			HDLQualifiedName fqn = resolveTo.getFullName().append(var);
			if (map.get(fqn) != null)
				return map.get(fqn);
		}
		return null;
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
