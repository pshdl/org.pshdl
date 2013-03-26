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
package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;
import java.util.*;

import com.google.common.collect.*;

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
			int v = value.compareTo(o.value);
			if (v != 0)
				return v;
			// Then sort Starts before ends
			return -count;
		}

		public boolean isStart() {
			return count > 0;
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
		for (Iterator<RangeVal> iterator = temp.iterator(); iterator.hasNext();) {
			RangeVal rangeVal = iterator.next();
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
		SortedSet<Range<BigInteger>> res = new TreeSet<Range<BigInteger>>(new Comparator<Range<BigInteger>>() {
			@Override
			public int compare(Range<BigInteger> arg0, Range<BigInteger> arg1) {
				return ComparisonChain.start() //
						.compare(arg0.lowerEndpoint(), arg1.lowerEndpoint()) //
						.compare(arg0.upperEndpoint(), arg1.upperEndpoint()) //
						.result();
			}
		});
		int count = 0;
		BigInteger start = null;
		for (RangeVal rangeVal : value) {
			count += rangeVal.count;
			if (rangeVal.isStart()) {
				if (start != null)
					// If there was an unended start, then we have to end it
					if (start.equals(rangeVal.value)) {
						// Or at the same location
						res.add(Ranges.closed(start, rangeVal.value));
						// res.add(new ValueRange(start, rangeVal.value));
					} else {
						// just one before the new start
						res.add(Ranges.closed(start, rangeVal.value.subtract(BigInteger.ONE)));
					}
				// res.add(new ValueRange(start,
				// rangeVal.value.subtract(BigInteger.ONE)));
				// Set the start to the current Element
				start = rangeVal.value;
			} else {
				// End the current range at this Element
				res.add(Ranges.closed(start, rangeVal.value));
				if (count > 0) {
					// If we expect another end later, the element following
					// this will have to start one after
					start = rangeVal.value.add(BigInteger.ONE);
				} else {
					// No new range anymore
					start = null;
				}
			}
		}
		return res;
	}

	public static void main(String[] args) {
		// 5->8 9->10 11
		System.out.println(split(createRanges(5, 8, 9, 10, 11, 11)));
		// 5, 6->7, 8, 9, 10
		System.out.println(split(createRanges(5, 10, 6, 8, 8, 9)));
		System.out.println(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9)));
		System.out.println(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9, 6, 11, 8, 9)));
		System.out.println(split(createRanges(5, 10, 6, 8, 8, 9, 6, 9, 6, 11, 8, 9, 14, 18)));
		System.out.println(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7)));
		System.out.println(split(createRanges(2, 2, 7, 13, 7, 10, 5, 5, 6, 6, 7, 7, 5, 8, 10, 10)));
		System.out.println(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7, 5, 8)));
		System.out.println(split(createRanges(7, 10, 5, 5, 6, 6, 7, 7, 5, 7)));
	}

	private static List<RangeVal> createRanges(int... r) {
		List<RangeVal> temp = new LinkedList<RangeVal>();
		for (int i = 0; i < r.length; i++) {
			temp.add(new RangeVal(BigInteger.valueOf(r[i]), (i % 2) == 0 ? 1 : -1));
		}
		System.out.println("HDLSimulator.createRanges()" + temp);
		return temp;
	}
}
