package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Memory {
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
}
