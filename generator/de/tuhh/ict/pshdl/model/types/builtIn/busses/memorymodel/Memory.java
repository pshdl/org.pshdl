package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

import org.antlr.v4.runtime.*;

public class Memory implements NamedElement {
	public List<Reference> references = new LinkedList<Reference>();

	public Memory() {
	}

	public Memory(Reference... references) {
		super();
		for (Reference reference : references) {
			this.references.add(reference);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("memory {\n");
		for (Reference ref : references) {
			sb.append('\t').append(ref).append('\n');
		}
		sb.append('}');
		return sb.toString();
	}

	@Override
	public String getName() {
		return "memory";
	}

	public Token token;

	@Override
	public void setLocation(Token start) {
		this.token = start;
	}
}
