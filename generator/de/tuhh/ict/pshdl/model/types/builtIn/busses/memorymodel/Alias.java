package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Alias implements NamedElement {
	public String name;
	public LinkedList<NamedElement> definitions = new LinkedList<NamedElement>();

	public Alias(String name, NamedElement... definitions) {
		super();
		this.name = name;
		for (NamedElement namedElement : definitions) {
			this.definitions.add(namedElement);
		}
	}

	public Alias(String name) {
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
		sb.append("alias ").append(name).append(" {\n");
		for (NamedElement dec : definitions) {
			sb.append('\t').append(dec).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}
}
