package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

public class LevenshteinDistance {
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(CharSequence str1, CharSequence str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];

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
			int diff = distance - o.distance;
			if (diff != 0)
				return diff;
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
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Score other = (Score) obj;
			if (distance != other.distance)
				return false;
			if (string == null) {
				if (other.string != null)
					return false;
			} else if (!string.equals(other.string))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Score [distance=" + distance + ", string=" + string + "]";
		}
	}

	public static Score[] getTopMatches(String search, boolean ignoreCase, String... input) {
		String s = ignoreCase ? search.toLowerCase() : search;
		TreeSet<Score> res = new TreeSet<Score>();
		for (String string : input) {
			String t = ignoreCase ? string.toLowerCase() : string;
			res.add(new Score(computeLevenshteinDistance(s, t), string));
		}
		return res.toArray(new Score[res.size()]);
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(getTopMatches("Hallo", true, "hallo", "xallo", "kallo", "hello", "moin")));
	}
}