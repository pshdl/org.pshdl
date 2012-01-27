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
				List<HDLInterface> ifDecl = doGetInterfaceDeclarations();
				ifCache = new HashMap<String, HDLInterface>();
				for (HDLInterface hdlIfDeclaration : ifDecl) {
					ifCache.put(hdlIfDeclaration.getName(), hdlIfDeclaration);
				}
			}
		}
		if (ifCache.get(hIf) != null)
			return ifCache.get(hIf);
		return container.resolveInterface(hIf);
	}

	protected List<HDLInterface> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (HDLStatement hdlStatement : stmnts) {
			if (hdlStatement instanceof HDLInterfaceDeclaration) {
				HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) hdlStatement;
				res.add(hid.getHIf());
			}
			if (hdlStatement instanceof HDLDirectGeneration) {
				HDLDirectGeneration hid = (HDLDirectGeneration) hdlStatement;
				res.add(hid.getHIf());
			}
		}
		return res;
	}

	abstract protected List<HDLInterface> doGetInterfaceDeclarations();

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
		for (HDLInterface ifDecl : doGetInterfaceDeclarations()) {
			types.add(ifDecl);
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
