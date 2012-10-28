package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Column implements NamedElement {
	public String name;

	public Column(String name) {
		super();
		this.name = name;
	}

	public List<NamedElement> rows = new LinkedList<NamedElement>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("column ").append(name).append(" {\n");
		for (NamedElement dec : rows) {
			sb.append('\t').append(dec).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}
}
