package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface IStatementContainer {
	public List<HDLEnumDeclaration> doGetEnumDeclarations();

	public List<HDLInterface> doGetInterfaceDeclarations();

	public List<HDLVariableDeclaration> doGetVariableDeclarations();

	public HDLObject getContainer();

	public <T> List<T> getAllObjectsOf(Class<? extends T> clazz, boolean deep);
}
