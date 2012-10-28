package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Row implements NamedElement {
	public String name;

	public Row(String name) {
		super();
		this.name = name;
	}

	public List<NamedElement> definitions = new LinkedList<NamedElement>();

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
