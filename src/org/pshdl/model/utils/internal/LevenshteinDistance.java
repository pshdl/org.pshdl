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
package org.pshdl.model.utils.internal;

import java.util.Arrays;
import java.util.TreeSet;

public class LevenshteinDistance {
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence str1, CharSequence str2) {
		final int[][] distance = new int[str1.length() + 1][str2.length() + 1];

		for (int i = 0; i <= str1.length(); i++) {
			distance[i][0] = i;
		}
		for (int j = 1; j <= str2.length(); j++) {
			distance[0][j] = j;
		}

		for (int i = 1; i <= str1.length(); i++) {
			for (int j = 1; j <= str2.length(); j++) {
				distance[i][j] = minimum(distance[i - 1][j] + 1, distance[i][j - 1] + 1, distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
			}
		}

		return distance[str1.length()][str2.length()];
	}

	public static class Score implements Comparable<Score> {
		public final int distance;
		public final String string;

		public Score(int distance, String string) {
			super();
			this.distance = distance;
			this.string = string;
		}

		@Override
		public int compareTo(Score o) {
			final int diff = distance - o.distance;
			if (diff != 0) {
				return diff;
			}
			return string.compareTo(o.string);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + distance;
			result = (prime * result) + ((string == null) ? 0 : string.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Score other = (Score) obj;
			if (distance != other.distance) {
				return false;
			}
			if (string == null) {
				if (other.string != null) {
					return false;
				}
			} else if (!string.equals(other.string)) {
				return false;
			}
			return true;
		}

		@Override
		public String toString() {
			return "Score [distance=" + distance + ", string=" + string + "]";
		}
	}

	public static Score[] getTopMatches(String search, boolean ignoreCase, String... input) {
		final String s = ignoreCase ? search.toLowerCase() : search;
		final TreeSet<Score> res = new TreeSet<>();
		for (final String string : input) {
			final String t = ignoreCase ? string.toLowerCase() : string;
			res.add(new Score(computeLevenshteinDistance(s, t), string));
		}
		return res.toArray(new Score[res.size()]);
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(getTopMatches("Hallo", true, "hallo", "xallo", "kallo", "hello", "moin")));
	}
}
