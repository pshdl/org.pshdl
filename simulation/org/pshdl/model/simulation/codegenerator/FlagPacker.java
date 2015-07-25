package org.pshdl.model.simulation.codegenerator;

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.google.common.primitives.Ints;

public class FlagPacker {

	int[][] inputSet = new int[][] {//
	{ 1, 7, 9, 13 }, //
			{ 1, 5, 9, 13 },//
			{ 7, 8, 9, 13 },//
			{ 1, 5 },//
	};
	int[] assignmentSet = new int[] {//
	0, // 0
			1, // 1
			2, // 2
			10, // 3
			2, // 4
			0, // 5
			2, // 6
			0, // 7
			3, // 8
			1, // 9
			3, // 10
			0, // 11
			0, // 12
			1, // 13
	};

	public static class SetCount implements Comparable<SetCount> {
		public final SortedSet<Integer> set;
		public int count = 1;

		public SetCount(SortedSet<Integer> set) {
			super();
			this.set = set;
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("SetCount [set=");
			builder.append(set);
			builder.append(", count=");
			builder.append(count);
			builder.append("]");
			return builder.toString();
		}

		@Override
		public int compareTo(SetCount o) {
			int comp = o.count - count;
			if (comp != 0)
				return comp;
			comp = set.size() - o.set.size();
			if (comp != 0)
				return comp;
			return 0;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + set.hashCode();
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
			final SetCount other = (SetCount) obj;
			if (!set.equals(other))
				return false;
			return true;
		}

		public SetCount merge(SetCount b) {
			final SortedSet<Integer> values = Sets.newTreeSet();
			values.addAll(set);
			values.addAll(b.set);
			final SetCount res = new SetCount(values);
			res.count = count + b.count;
			return res;
		}

	}

	public Set<SetCount> createPairs() {
		final Map<SetCount, SetCount> pairs = Maps.newHashMap();
		for (final int[] is : inputSet) {
			for (int i = 0; i < is.length; i++) {
				final int a = is[i];
				for (int k = i + 1; k < is.length; k++) {
					final int b = is[k];
					final SetCount set = new SetCount(Sets.newTreeSet(Ints.asList(a, b)));
					final SetCount oldSet = pairs.put(set, set);
					if (oldSet != null) {
						set.count += oldSet.count;
					}
				}
			}
		}
		return Sets.newTreeSet(pairs.values());
	}

	public int score() {
		int score = 0;
		for (final int[] js : inputSet) {
			final Set<Integer> values = Sets.newHashSet();
			for (final int key : js) {
				values.add(assignmentSet[key]);
			}
			score += values.size();
		}
		return score;
	}

	public static void main(String[] args) {
		final FlagPacker flagPacker = new FlagPacker();
		final Set<SetCount> pairs = flagPacker.createPairs();
		final Set<SetCount> threeSet = flagPacker.mergePairs(pairs);
		final Set<SetCount> fourSet = flagPacker.mergePairs(threeSet);
		System.out.println(pairs);
		System.out.println(threeSet);
		System.out.println(fourSet);
		flagPacker.score();
	}

	private Set<SetCount> mergePairs(Set<SetCount> pairs) {
		final Set<SetCount> res = Sets.newHashSet();
		final SetCount[] oldPairs = pairs.toArray(new SetCount[pairs.size()]);
		for (int i = 0; i < oldPairs.length; i++) {
			final SetCount a = oldPairs[i];
			nextPair: for (int j = i + 1; j < oldPairs.length; j++) {
				final SetCount b = oldPairs[j];
				final SetView<Integer> diff = Sets.difference(a.set, b.set);
				if (diff.size() == 2) {
					res.add(a.merge(b));
				}
			}
		}
		return res;
	}
}
