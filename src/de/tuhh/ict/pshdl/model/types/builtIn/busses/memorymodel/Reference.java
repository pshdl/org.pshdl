package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Reference implements NamedElement {

	public Reference(String name) {
		super();
		this.name = name;
	}

	public String name;
	public List<Integer> dimensions = new LinkedList<Integer>();

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
}
