package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface IStatementContainer {
	public List<HDLEnumDeclaration> doGetEnumDeclarations();

	public List<HDLInterface> doGetInterfaceDeclarations();

	public List<HDLVariable> doGetVariables();

	public HDLObject getContainer();

	public <T> Set<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep);

	public HDLQualifiedName getFullName();
}
