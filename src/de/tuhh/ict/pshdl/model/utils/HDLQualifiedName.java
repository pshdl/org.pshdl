package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import org.eclipse.jdt.annotation.*;

public class HDLQualifiedName implements Comparable<HDLQualifiedName> {
	public static final HDLQualifiedName EMPTY = new HDLQualifiedName(new String[0]);
	private final String[] qfn;
	public final int length;

	public HDLQualifiedName(String[] qfn) {
		this.qfn = qfn;
		if (qfn == null)
			throw new IllegalArgumentException("Segments may not be null");
		this.length = qfn.length;
		for (String string : qfn) {
			if (string == null)
				throw new IllegalArgumentException("Segments may not be null");
			if (string.contains("."))
				throw new IllegalArgumentException("Segments may not contain dots");
			if (string.isEmpty())
				throw new IllegalArgumentException("Segments may not be empty");
		}
	}

	public HDLQualifiedName(String qfn) {
		this(qfn == null ? new String[0] : qfn.split("\\."));
	}

	@NonNull
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

	@NonNull
	public String getSegment(int i) {
		return qfn[i];
	}

	@NonNull
	public static HDLQualifiedName create(String... segments) {
		return new HDLQualifiedName(segments);
	}

	@NonNull
	public HDLQualifiedName skipLast(int i) {
		int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@NonNull
	public String getLastSegment() {
		return qfn[qfn.length - 1];
	}

	/**
	 * Prints the Qualified name using c as separator
	 * 
	 * @param c
	 *            the separator
	 * @return
	 */
	@NonNull
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

	/**
	 * Generates a new HDLQualifiedName with the name appended
	 * 
	 * @param name
	 *            the segment to append
	 * @return a new HDLQualifiedName instance
	 */
	@NonNull
	public HDLQualifiedName append(String name) {
		int len = length + 1;
		String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, length);
		newQfn[length] = name;
		return new HDLQualifiedName(newQfn);
	}

	@NonNull
	public HDLQualifiedName skipFirst(int i) {
		int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		String[] newQfn = new String[len];
		System.arraycopy(qfn, i, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@NonNull
	public static HDLQualifiedName create(List<String> segments) {
		return new HDLQualifiedName(segments.toArray(new String[segments.size()]));
	}

	@NonNull
	public HDLQualifiedName toSegment(int i) {
		int len = i;
		if ((len < 0) || (len >= qfn.length))
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@NonNull
	public HDLQualifiedName append(HDLQualifiedName hdlQualifiedName) {
		LinkedList<String> ll = new LinkedList<String>();
		ll.addAll(Arrays.asList(this.qfn));
		ll.addAll(Arrays.asList(hdlQualifiedName.qfn));
		return HDLQualifiedName.create(ll);
	}

	@NonNull
	public HDLQualifiedName getTypePart() {
		List<String> res = new LinkedList<String>();
		for (String segment : qfn) {
			if (segment.charAt(0) != '$')
				res.add(segment);
			else
				break;
		}
		return create(res);
	}

	@NonNull
	public HDLQualifiedName getLocalPart() {
		List<String> res = new LinkedList<String>();
		if (qfn.length <= 1)
			return this;
		for (String segment : qfn) {
			if (segment.charAt(0) == '$')
				res.add(segment);
		}
		res.add(getLastSegment());
		return create(res);
	}

	@Override
	public int compareTo(HDLQualifiedName arg0) {
		return toString().compareTo(arg0.toString());
	}
}
