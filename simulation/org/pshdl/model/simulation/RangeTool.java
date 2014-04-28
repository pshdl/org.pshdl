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
package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Range;

public class RangeTool {
	public static class RangeVal implements Comparable<RangeVal> {
		public final BigInteger value;
		public int count;

		public RangeVal(BigInteger value, int count) {
			super();
			this.value = value;
			this.count = count;
		}

		@Override
		public String toString() {
			return value + (isStart() ? "S" : "E") + count;
		}

		@Override
		public int compareTo(RangeVal o) {
			// Sort by value first
			final int v = value.compareTo(o.value);
			if (v != 0)
				return v;
			// Then sort Starts before ends
			return -count;
		}

		public boolean isStart() {
			return count > 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + count;
			result = (prime * result) + ((value == null) ? 0 : value.hashCode());
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
			final RangeVal other = (RangeVal) obj;
			if (count != other.count)
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

	}

	/**
	 * Sort a List of ranges by their number, then start/end and merge multiple
	 * start/ends
	 *
	 * @param temp
	 *            a list of RangeVal which can be unsorted
	 */
	public static void preSort(List<RangeVal> temp) {
		Collections.sort(temp);
		RangeVal last = null;
		for (final Iterator<RangeVal> iterator = temp.iterator(); iterator.hasNext();) {
			final RangeVal rangeVal = iterator.next();
			if ((last != null) && last.value.equals(rangeVal.value) && (last.isStart() == rangeVal.isStart())) {
				iterator.remove();
				last.count += rangeVal.count;
			} else {
				last = rangeVal;
			}
		}
	}

	/**
	 * Splits a list into ValueRange Objects that do not overlap each other, but
	 * fully represent the ranges given by value
	 *
	 * @param value
	 *            a list of RangeVal Objects that need to be split
	 * @return
	 */
	public static SortedSet<Range<BigInteger>> split(List<RangeVal> value) {
		preSort(value);
		final SortedSet<Range<BigInteger>> res = new TreeSet<Range<BigInteger>>(new Comparator<Range<BigInteger>>() {
			@Override
			public int compare(Range<BigInteger> arg0, Range<BigInteger> arg1) {
				return ComparisonChain.start() //
						.compare(arg0.lowerEndpoint(), arg1.lowerEndpoint()) //
						.compare(arg0.upperEndpoint(), arg1.upperEndpoint()) //
						.result();
			}
		});
		RangeVal last = null;
		int count = 0;
		for (final RangeVal current : value) {
			if (last != null) {
				if (last.isStart()) {
					if (current.isStart()) {
						res.add(createRange(last.value, current.value.subtract(BigInteger.ONE)));
					} else {
						res.add(createRange(last.value, current.value));
					}
				} else {
					if (current.isStart()) {
						if ((count > 0) && (current.value.subtract(last.value).compareTo(BigInteger.ONE) > 0)) {
							res.add(createRange(last.value.add(BigInteger.ONE), current.value.subtract(BigInteger.ONE)));
						}
					} else {
						res.add(createRange(last.value.add(BigInteger.ONE), current.value));
					}
				}
			} else {
				if (!current.isStart())
					throw new IllegalArgumentException("this should not happen");
			}
			count += current.count;
			last = current;
		}
		return res;
	}

	public static void main(String[] args) {
		validate(split(createRanges(6, 10, 5, 5, 0, 10)));
		validate(split(createRanges(1, 2, 5, 6)));
		validate(split(createRanges(1, 2, 5, 6, 1, 6)));
		// 5->8 9->10 11
		validate(split(createRanges(5, 8, 9, 10, 11, 11)));
		// 5, 6->7, 8, 9, 10
		validate(split(createRanges(5, 10, 6, 8, 8, 9)));
		validate(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9)));
		validate(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9, 6, 11, 8, 9)));
		validate(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9, 6, 11, 8, 9, 14, 18)));
		validate(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7)));
		validate(split(createRanges(2, 2, 7, 13, 7, 10, 5, 5, 6, 6, 7, 7, 5, 8, 10, 10)));
		validate(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7, 5, 8)));
		validate(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7, 5, 7)));
	}

	private static void validate(Set<Range<BigInteger>> split) {
		System.out.println("Result:" + split);
		Range<BigInteger> last = Range.closed(BigInteger.valueOf(-1), BigInteger.valueOf(-1));
		for (final Range<BigInteger> range : split) {
			if (range.isConnected(last))
				throw new IllegalArgumentException("Ranges are connected:" + last + " and " + range);
			last = range;
		}
	}

	private static List<RangeVal> createRanges(int... r) {
		final List<RangeVal> temp = new LinkedList<RangeVal>();
		for (int i = 0; i < r.length; i += 2) {
			temp.add(new RangeVal(BigInteger.valueOf(r[i]), 1));
			temp.add(new RangeVal(BigInteger.valueOf(r[i + 1]), -1));
			System.out.print("[" + r[i] + "," + r[i + 1] + "] ");
		}
		return temp;
	}

	public static <C extends Comparable<C>> Range<C> createRange(C lower, C upper) {
		if (lower.compareTo(upper) > 0)
			return Range.closed(upper, lower);
		return Range.closed(lower, upper);
	}
}
