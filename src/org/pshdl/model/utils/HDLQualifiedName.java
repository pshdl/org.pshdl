/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.utils;

import java.util.*;

import javax.annotation.*;

public class HDLQualifiedName implements Comparable<HDLQualifiedName> {
	public static final HDLQualifiedName EMPTY = new HDLQualifiedName(new String[0]);
	private final String[] qfn;
	public final int length;

	public HDLQualifiedName(String[] qfn) {
		this.qfn = qfn;
		if (qfn == null)
			throw new IllegalArgumentException("Segments may not be null");
		this.length = qfn.length;
		for (final String string : qfn) {
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

	@Nonnull
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
		final HDLQualifiedName other = (HDLQualifiedName) obj;
		if (!Arrays.equals(qfn, other.qfn))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final char c = '.';
		return toString(c);
	}

	@Nonnull
	public String getSegment(int i) {
		return qfn[i];
	}

	@Nonnull
	public static HDLQualifiedName create(String... segments) {
		return new HDLQualifiedName(segments);
	}

	@Nonnull
	public HDLQualifiedName skipLast(int i) {
		final int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		final String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@Nonnull
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
	@Nonnull
	public String toString(char c) {
		boolean first = true;
		final StringBuilder sb = new StringBuilder();
		for (final String part : qfn) {
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
	@Nonnull
	public HDLQualifiedName append(String name) {
		final int len = length + 1;
		final String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, length);
		newQfn[length] = name;
		return new HDLQualifiedName(newQfn);
	}

	@Nonnull
	public HDLQualifiedName skipFirst(int i) {
		final int len = qfn.length - i;
		if (len < 0)
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		final String[] newQfn = new String[len];
		System.arraycopy(qfn, i, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@Nonnull
	public static HDLQualifiedName create(List<String> segments) {
		return new HDLQualifiedName(segments.toArray(new String[segments.size()]));
	}

	@Nonnull
	public HDLQualifiedName toSegment(int i) {
		final int len = i;
		if ((len < 0) || (len >= qfn.length))
			throw new IllegalArgumentException("Can not skip last:" + i + " of " + this);
		final String[] newQfn = new String[len];
		System.arraycopy(qfn, 0, newQfn, 0, len);
		return new HDLQualifiedName(newQfn);
	}

	@Nonnull
	public HDLQualifiedName append(HDLQualifiedName hdlQualifiedName) {
		final LinkedList<String> ll = new LinkedList<String>();
		ll.addAll(Arrays.asList(this.qfn));
		ll.addAll(Arrays.asList(hdlQualifiedName.qfn));
		return HDLQualifiedName.create(ll);
	}

	@Nonnull
	public HDLQualifiedName getTypePart() {
		final List<String> res = new LinkedList<String>();
		for (final String segment : qfn)
			if (segment.charAt(0) != '$') {
				res.add(segment);
			} else {
				break;
			}
		return create(res);
	}

	@Nonnull
	public HDLQualifiedName getLocalPart() {
		final List<String> res = new LinkedList<String>();
		if (qfn.length <= 1)
			return this;
		for (final String segment : qfn)
			if (segment.charAt(0) == '$') {
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
