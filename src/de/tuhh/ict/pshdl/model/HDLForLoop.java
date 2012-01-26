package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLValueType.HDLDirection;
import de.tuhh.ict.pshdl.model.impl.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLForLoop extends AbstractHDLForLoop {
	public HDLForLoop(HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos) {
		super(container, range, param, dos);
	}

	public HDLForLoop() {
		super();
	}

	// $CONTENT-BEGIN$
	@Override
	protected List<HDLEnumDeclaration> doGetEnumDeclarations() {
		return getallEnumDeclarations(dos);
	}

	@Override
	protected List<HDLInterfaceDeclaration> doGetInterfaceDeclarations() {
		return getallInterfaceDeclarations(dos);
	}

	@Override
	protected List<HDLVariableDeclaration> doGetVariableDeclarations() {
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		res.addAll(getallVariableDeclarations(dos));
		HDLPrimitive primitive = new HDLPrimitive(null, HDLRegisterConfig.defaultConfig(), HDLDirection.HIDDEN, HDLPrimitiveType.INTEGER, null);
		res.add(new HDLVariableDeclaration(this, primitive, asList(param)));
		return res;
	}
	// $CONTENT-END$
}
