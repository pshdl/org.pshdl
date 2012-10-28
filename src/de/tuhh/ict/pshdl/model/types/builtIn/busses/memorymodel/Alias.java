package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Alias implements NamedElement {
	public String name;

	@Override
	public String getName() {
		return name;
	}

	public Alias(String name) {
		super();
		this.name = name;
	}

	public LinkedList<NamedElement> definitions = new LinkedList<NamedElement>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("alias ").append(name).append(" {\n");
		for (NamedElement dec : definitions) {
			sb.append('\t').append(dec).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}
}
