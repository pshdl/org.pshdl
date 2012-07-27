package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public interface IStatementContainer extends IHDLObject {
	public List<HDLEnumDeclaration> doGetEnumDeclarations();

	public List<HDLInterface> doGetInterfaceDeclarations();

	public List<HDLVariable> doGetVariables();

	public HDLQualifiedName getFullName();
}
