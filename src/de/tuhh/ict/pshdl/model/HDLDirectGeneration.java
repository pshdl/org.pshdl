package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLDirectGeneration extends AbstractHDLDirectGeneration {
	public HDLDirectGeneration(HDLObject container, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments) {
		super(container, generatorID, generatorContent, arguments);
	}

	public HDLDirectGeneration() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return Collections.EMPTY_LIST;
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		// Implement the HDLInterfaceGeneration
		return Collections.singletonList(new HDLInterfaceDeclaration(null, null));
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		return Collections.EMPTY_LIST;
	}
	// $CONTENT-END$
}
