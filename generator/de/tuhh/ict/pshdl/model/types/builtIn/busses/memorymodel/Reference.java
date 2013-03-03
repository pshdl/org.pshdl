package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

import org.antlr.v4.runtime.*;

public class Reference implements NamedElement {
	public String name;
	public List<Integer> dimensions = new LinkedList<Integer>();

	public Reference(String name, Integer... dimensions) {
		super();
		this.name = name;
		for (Integer integer : dimensions) {
			this.dimensions.add(integer);
		}
	}

	public Reference(String name) {
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
		for (Integer arr : dimensions) {
			sb.append('[').append(arr).append(']');
		}
		return name + sb;
	}

	public Token token;

	@Override
	public void setLocation(Token start) {
		this.token = start;
	}

}
