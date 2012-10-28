package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Unit {
	public int rowWidth = 32;
	public Map<String, NamedElement> declarations = new LinkedHashMap<String, NamedElement>();
	public Memory memory;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("rowWidth=").append(rowWidth).append(":\n");
		for (NamedElement dec : declarations.values()) {
			sb.append(dec).append('\n');
		}
		sb.append(memory).append('\n');
		return sb.toString();
	}

	public NamedElement resolve(Reference ref) {
		NamedElement decl = declarations.get(ref.getName());
		if (decl == null)
			throw new IllegalArgumentException("Can not resolve reference:" + ref.name);
		return decl;
	}
}
