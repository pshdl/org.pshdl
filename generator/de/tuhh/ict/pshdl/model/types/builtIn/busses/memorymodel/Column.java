package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Column implements NamedElement {
	public String name;
	public List<NamedElement> rows = new LinkedList<NamedElement>();
	public int index = -1;

	public Column(String name) {
		super();
		this.name = name;
	}

	public Column(String name, NamedElement... rows) {
		super();
		this.name = name;
		for (NamedElement namedElement : rows) {
			this.rows.add(namedElement);
		}
	}

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
