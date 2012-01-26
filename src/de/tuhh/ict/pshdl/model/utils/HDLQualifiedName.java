package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

public class HDLQualifiedName {
	private final String[] qfn;
	public final int length;

	public HDLQualifiedName(String[] qfn) {
		this.qfn = qfn;
		this.length = qfn.length;
	}

	public HDLQualifiedName(String qfn) {
		this(qfn.split("\\."));
	}

	public String[] getQfn() {
		return qfn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(qfn);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HDLQualifiedName other = (HDLQualifiedName) obj;
		if (!Arrays.equals(qfn, other.qfn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		char c = '.';
		return toString(c);
	}

	public String getSegment(int i) {
		return qfn[i];
	}

	public static HDLQualifiedName create(String... segments) {
		return new HDLQualifiedName(segments);
	}

	public HDLQualifiedName skipLast(int i) {
		int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	public String getLastSegment() {
		return qfn[qfn.length - 1];
	}

	public String toString(char c) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (String part : qfn) {
			if (!first) {
				sb.append(c);
			}
			sb.append(part);
			first = false;
		}
		return sb.toString();
	}

	public HDLQualifiedName append(String name) {
		int len = length + 1;
		String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, length);
		newQfn[length] = name;
		return new HDLQualifiedName(newQfn);
	}

	public HDLQualifiedName skipFirst(int i) {
		int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		String[] newQfn = new String[len];
		System.arraycopy(qfn, i, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	public static HDLQualifiedName create(List<String> segments) {
		return new HDLQualifiedName(segments.toArray(new String[segments.size()]));
	}
}
