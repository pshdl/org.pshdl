package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public abstract class HDLStatement extends AbstractHDLStatement {
	public HDLStatement(HDLObject container) {
		super(container);
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
				List<HDLEnumDeclaration> enumDecl = doGetEnumDeclarations();
				enumCache = new HashMap<String, HDLEnum>();
				for (HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(hdlEnumDeclaration.getHEnum().getName(), hdlEnumDeclaration.getHEnum());
				}
			}
		}
		if (enumCache.get(hEnum) != null)
			return enumCache.get(hEnum);
		return container.resolveEnum(hEnum);
	}

	protected List<HDLEnumDeclaration> getallEnumDeclarations(List<HDLStatement> stmnts) {
		List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLEnumDeclaration) {
				HDLEnumDeclaration hed = (HDLEnumDeclaration) hdlStatement;
				res.add(hed);
			}
		}
		return res;
	}

	abstract protected List<HDLEnumDeclaration> doGetEnumDeclarations();

	private Map<String, HDLInterface> ifCache;

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				List<HDLInterfaceDeclaration> ifDecl = doGetInterfaceDeclarations();
				ifCache = new HashMap<String, HDLInterface>();
				for (HDLInterfaceDeclaration hdlIfDeclaration : ifDecl) {
					ifCache.put(hdlIfDeclaration.getHIf().getName(), hdlIfDeclaration.getHIf());
				}
			}
		}
		if (ifCache.get(hIf) != null)
			return ifCache.get(hIf);
		return container.resolveInterface(hIf);
	}

	protected List<HDLInterfaceDeclaration> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		List<HDLInterfaceDeclaration> res = new LinkedList<HDLInterfaceDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLInterfaceDeclaration) {
				HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) hdlStatement;
				res.add(hid);
			}
		}
		return res;
	}

	abstract protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations();

	@Override
	public HDLType resolveType(HDLQualifiedName type) {
		return super.resolveType(type);
	}

	protected List<HDLType> doGetTypeDeclarations() {
		List<HDLType> types = new LinkedList<HDLType>();
		for (HDLEnumDeclaration hEnumDecl : doGetEnumDeclarations()) {
			types.add(hEnumDecl.getHEnum());
		}
		for (HDLVariableDeclaration varDecl : doGetVariableDeclarations()) {
			types.add(varDecl.getType());
		}
		for (HDLInterfaceDeclaration ifDecl : doGetInterfaceDeclarations()) {
			types.add(ifDecl.getHIf());
		}
		return types;
	}

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		return super.resolveVariable(var);
	}

	protected List<HDLVariableDeclaration> getallVariableDeclarations(List<HDLStatement> stmnts) {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hid = (HDLVariableDeclaration) hdlStatement;
				res.add(hid);
			}
		}
		return res;
	}

	abstract protected List<HDLVariableDeclaration> doGetVariableDeclarations();
	// $CONTENT-END$
}
