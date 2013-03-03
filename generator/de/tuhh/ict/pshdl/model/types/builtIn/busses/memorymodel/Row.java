package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

import org.antlr.v4.runtime.*;

import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;

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

	public final Map<String, Integer> defCount = new HashMap<String, Integer>();
	public boolean readOnly = true;
	public boolean writeOnly = true;

	public void updateInfo() {
		int bitPos = 31;
		for (NamedElement ne : definitions) {
			Definition def = (Definition) ne;
			def.bitPos = bitPos;
			bitPos -= MemoryModel.getSize(def);
			Integer integer = defCount.get(def.name);
			if (integer == null)
				integer = 0;
			def.arrayIndex = integer;
			defCount.put(def.name, ++integer);
			if ((def.rw == RWType.rw) || (def.rw == RWType.w))
				readOnly = false;
			if ((def.rw == RWType.rw) || (def.rw == RWType.r))
				writeOnly = false;
		}
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

	public Token token;

	@Override
	public void setLocation(Token start) {
		this.token = start;
	}
}
