package de.tuhh.ict.pshdl.model.utils;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;

public class HDLResolver {

	private boolean descent;

	private Map<HDLQualifiedName, HDLEnum> enumCache;

	private Map<HDLQualifiedName, HDLInterface> ifCache;

	private Map<HDLQualifiedName, HDLFunction> funcCache;

	private final IHDLObject resolveTo;

	private Map<HDLQualifiedName, HDLType> typeCache;

	private Map<HDLQualifiedName, HDLVariable> variableCache;

	public HDLResolver(IHDLObject resolveTo, boolean descent) {
		super();
		this.resolveTo = resolveTo;
		this.descent = descent;
	}

	public HDLResolver(IHDLObject resolveTo, boolean descent, String libURI) {
		super();
		this.resolveTo = resolveTo;
		this.descent = descent;
	}

	protected List<HDLType> doGetTypeDeclarations() {
		List<HDLType> types = new LinkedList<HDLType>();
		for (HDLEnumDeclaration hEnumDecl : ScopingExtension.INST.doGetEnumDeclarations(resolveTo)) {
			types.add(hEnumDecl.getHEnum());
		}
		for (HDLVariable varDecl : ScopingExtension.INST.doGetVariables(resolveTo)) {
			IHDLObject container = varDecl.getContainer();
			if (container instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
				if (hvd.getPrimitive() != null) {
					types.add(hvd.getPrimitive());
				}
			}
		}
		for (HDLInterface ifDecl : ScopingExtension.INST.doGetInterfaceDeclarations(resolveTo)) {
			types.add(ifDecl);
		}
		return types;
	}

	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		if (enumCache == null) {
			synchronized (this) {
				HDLEnumDeclaration[] enumDecl = resolveTo.getAllObjectsOf(HDLEnumDeclaration.class, false);
				enumCache = new HashMap<HDLQualifiedName, HDLEnum>();
				for (HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(fullNameOf(hdlEnumDeclaration.getHEnum()), hdlEnumDeclaration.getHEnum());
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
		return ScopingExtension.INST.resolveEnum(resolveTo.getContainer(), hEnum);
	}

	public HDLFunction resolveFunction(HDLQualifiedName hEnum) {
		if (funcCache == null) {
			synchronized (this) {
				HDLFunction[] enumDecl = resolveTo.getAllObjectsOf(HDLFunction.class, false);
				funcCache = new HashMap<HDLQualifiedName, HDLFunction>();
				for (HDLFunction hdlEnumDeclaration : enumDecl) {
					funcCache.put(fullNameOf(hdlEnumDeclaration), hdlEnumDeclaration);
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		HDLFunction checkCache = checkCache(hEnum, funcCache);
		if (checkCache != null)
			return checkCache;
		if ((resolveTo.getContainer() == null) || !descent)
			return null;
		return ScopingExtension.INST.resolveFunction(resolveTo.getContainer(), hEnum);
	}

	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterface> ifDecl = ScopingExtension.INST.doGetInterfaceDeclarations(resolveTo);
				ifCache = new HashMap<HDLQualifiedName, HDLInterface>();
				for (HDLInterface hdlIfDeclaration : ifDecl) {
					ifCache.put(fullNameOf(hdlIfDeclaration), hdlIfDeclaration);
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
		return ScopingExtension.INST.resolveInterface(resolveTo.getContainer(), hIf);
	}

	public HDLType resolveType(HDLQualifiedName var) {
		if (typeCache == null) {
			synchronized (this) {
				List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = new HashMap<HDLQualifiedName, HDLType>();
				for (HDLType hdlTypeDeclaration : typeDecl) {
					typeCache.put(fullNameOf(hdlTypeDeclaration), hdlTypeDeclaration);
				}
			}
		}
		HDLType checkCache = checkCache(var, typeCache);
		if (checkCache != null)
			return checkCache;
		if ((resolveTo.getContainer() == null) || !descent) {
			if (resolveTo instanceof HDLUnit) {
				HDLUnit unit = (HDLUnit) resolveTo;
				String uri = unit.getLibURI();
				if (uri != null) {
					HDLLibrary library = HDLLibrary.getLibrary(uri);
					if (library != null) {
						ArrayList<String> imports = unit.getImports();
						imports.add(fullNameOf(unit).skipLast(1).append("*").toString());
						return library.resolve(imports, var);
					}
				}
			}
			return null;
		}
		return ScopingExtension.INST.resolveType(resolveTo.getContainer(), var);
	}

	public HDLVariable resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				List<HDLVariable> varDecl = ScopingExtension.INST.doGetVariables(resolveTo);
				variableCache = new HashMap<HDLQualifiedName, HDLVariable>();
				for (HDLVariable declVars : varDecl) {
					variableCache.put(fullNameOf(declVars), declVars);
				}
			}
		}
		HDLVariable checkCache = checkCache(var, variableCache);
		if (checkCache != null)
			return checkCache;
		if (var.length > 1) {
			// Using lastSgement if $for0.I or ThisObject.I
			if (var.getSegment(0).startsWith("$") || var.getTypePart().equals(fullNameOf(resolveTo).getTypePart())) {
				String string = var.getLastSegment();
				for (Entry<HDLQualifiedName, HDLVariable> entry : variableCache.entrySet()) {
					if (entry.getKey().getLastSegment().equals(string))
						return entry.getValue();
				}
			}
			HDLQualifiedName skipLast = var.skipLast(1);
			if (!fullNameOf(resolveTo).equals(skipLast)) {
				HDLType type = resolveType(skipLast);
				if ((type != null) && (type.getClassType() != HDLClass.HDLPrimitive)) {
					HDLVariable variable = ScopingExtension.INST.resolveVariable(type, var);
					if (variable != null)
						return variable;
				}
			}
		}
		if (HDLRegisterConfig.DEF_CLK.equals(var.getLastSegment()))
			return HDLRegisterConfig.defaultClk();
		if (HDLRegisterConfig.DEF_RST.equals(var.getLastSegment()))
			return HDLRegisterConfig.defaultRst();
		IHDLObject container = resolveTo.getContainer();
		if ((container == null) || !descent)
			return null;
		return ScopingExtension.INST.resolveVariable(container, var);
	}

	private <T> T checkCache(HDLQualifiedName var, Map<HDLQualifiedName, T> map) {
		if (map.get(var) != null)
			return map.get(var);
		if (var.length == 1) {
			HDLQualifiedName fqn = fullNameOf(resolveTo).append(var);
			if (map.get(fqn) != null)
				return map.get(fqn);
		}
		return null;
	}

	public static List<HDLEnumDeclaration> getallEnumDeclarations(List<HDLStatement> stmnts) {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetEnumDeclarations(hdlStatement));
		}
		return res;
	}

	public static List<HDLInterface> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetInterfaceDeclarations(hdlStatement));
		}
		return res;
	}

	public static List<HDLVariable> getallVariableDeclarations(List<HDLStatement> stmnts) {
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		for (HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetVariables(hdlStatement));
		}
		return res;
	}
}
