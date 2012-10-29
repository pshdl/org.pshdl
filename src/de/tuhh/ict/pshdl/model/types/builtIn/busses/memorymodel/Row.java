package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Row implements NamedElement {
	public String name;
	public Column column;
	public List<NamedElement> definitions = new LinkedList<NamedElement>();
	public int colIndex;

	public Row(String name, Column column, NamedElement... definitions) {
		super();
		this.name = name;
		this.column = column;
		for (NamedElement namedElement : definitions) {
			this.definitions.add(namedElement);
		}
	}

	public Row(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("row ").append(name).append(" {\n");
		for (NamedElement dec : definitions) {
			sb.append('\t').append(dec).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}
}
