package de.tuhh.ict.pshdl.model.simulation;

import java.io.*;
import java.math.*;
import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

public class HDLSimulator {

	public static HDLUnit createSimulationModel(HDLUnit unit, HDLEvaluationContext context) {
		HDLUnit insulin = Insulin.transform(unit);
		insulin = unrollForLoops(context, insulin);
		insulin = createMultiplexArrayWrite(context, insulin);
		insulin = createBitRanges(context, insulin);
		insulin = createTernary(context, insulin);
		insulin.validateAllFields(insulin.getContainer(), true);
		return insulin;
		// generate cat statement for ranges
		// generate reset condition
		// Starting from the end, generate ternary op for each switch/if
		// statement
	}

	private static HDLUnit createTernary(HDLEvaluationContext context, HDLUnit insulin) {
		HDLAssignment[] asss = insulin.getAllObjectsOf(HDLAssignment.class, true);
		Map<String, LinkedList<HDLAssignment>> writeOps = new HashMap<String, LinkedList<HDLAssignment>>();
		Map<String, HDLReference> references = new HashMap<String, HDLReference>();
		for (HDLAssignment ass : asss) {
			String key = ass.getLeft().toString();
			LinkedList<HDLAssignment> list = writeOps.get(key);
			if (list == null) {
				list = new LinkedList<HDLAssignment>();
				writeOps.put(key, list);
			}
			references.put(key, ass.getLeft());
			list.add(ass);
		}
		HDLVariableDeclaration[] hvds = insulin.getAllObjectsOf(HDLVariableDeclaration.class, true);
		ArrayList<HDLStatement> finalStatements = new ArrayList<HDLStatement>();
		finalStatements.addAll(Arrays.asList(hvds));
		for (Entry<String, LinkedList<HDLAssignment>> entry : writeOps.entrySet()) {
			LinkedList<HDLAssignment> list = entry.getValue();
			Iterator<HDLAssignment> reverseIter = list.descendingIterator();
			HDLExpression current = null;
			while (reverseIter.hasNext()) {
				HDLAssignment next = reverseIter.next();
				if (current == null)
					current = next.getRight();
				// XXX Add if cases
				if (next.getContainer().getClassType() == HDLClass.HDLUnit) {
					break;
				}
			}
			finalStatements.add(new HDLAssignment().setLeft(references.get(entry.getKey())).setRight(current));
		}
		return insulin.setStatements(finalStatements).copyDeepFrozen(insulin.getContainer());
	}

	private static HDLUnit createBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		HDLVariableRef[] refs = insulin.getAllObjectsOf(HDLVariableRef.class, true);
		Map<HDLQualifiedName, List<RangeVal>> ranges = new HashMap<HDLQualifiedName, List<RangeVal>>();
		for (HDLVariableRef ref : refs) {
			if (ref.getBits().size() > 0) {
				List<RangeVal> set = ranges.get(ref.getVarRefName());
				if (set == null) {
					set = new LinkedList<RangeVal>();
					ranges.put(ref.getVarRefName(), set);
				}
				for (HDLRange r : ref.getBits()) {
					ValueRange determineRange = r.determineRange(context);
					set.add(new RangeVal(determineRange.from, 1));
					set.add(new RangeVal(determineRange.to, -1));
				}
			}
		}
		Map<HDLQualifiedName, SortedSet<ValueRange>> splitRanges = new HashMap<HDLQualifiedName, SortedSet<ValueRange>>();
		for (Map.Entry<HDLQualifiedName, List<RangeVal>> entry : ranges.entrySet()) {
			splitRanges.put(entry.getKey(), split(entry.getValue()));
		}
		// Change bit access to broken down ranges
		ModificationSet ms = new ModificationSet();
		for (HDLVariableRef ref : refs) {
			ArrayList<HDLRange> newRanges = new ArrayList<HDLRange>();
			SortedSet<ValueRange> list = splitRanges.get(ref.getVarRefName());
			for (HDLRange bit : ref.getBits()) {
				if (bit.getFrom() != null) { // Singular ranges don't do
												// anything
					ValueRange range = bit.determineRange(context);
					for (ValueRange newRange : list) {
						if (range.contains(newRange)) {
							if (newRange.from.equals(newRange.to))
								newRanges.add(new HDLRange().setTo(HDLLiteral.get(newRange.to)));
							else
								newRanges.add(new HDLRange().setFrom(HDLLiteral.get(newRange.from)).setTo(HDLLiteral.get(newRange.to)));
						}
					}
				} else {
					newRanges.add(bit);
				}
			}
			if (newRanges.size() != 0)
				ms.replace(ref, ref.setBits(newRanges));
		}
		HDLUnit apply = ms.apply(insulin);
		return Insulin.handleMultiBitAccess(apply, context);
	}

	private static class RangeVal implements Comparable<RangeVal> {
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
	private static void preSort(List<RangeVal> temp) {
		Collections.sort(temp);
		RangeVal last = null;
		for (Iterator<RangeVal> iterator = temp.iterator(); iterator.hasNext();) {
			RangeVal rangeVal = iterator.next();
			if ((last != null) && last.value.equals(rangeVal.value) && (last.isStart() == rangeVal.isStart())) {
				iterator.remove();
				last.count += rangeVal.count;
			} else
				last = rangeVal;
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
	private static SortedSet<ValueRange> split(List<RangeVal> value) {
		preSort(value);
		SortedSet<ValueRange> res = new TreeSet<ValueRange>();
		int count = 0;
		BigInteger start = null;
		for (RangeVal rangeVal : value) {
			count += rangeVal.count;
			if (rangeVal.isStart()) {
				if (start != null) {
					// If there was an unended start, then we have to end it
					if (start.equals(rangeVal.value))
						// Or at the same location
						res.add(new ValueRange(start, rangeVal.value));
					else
						// just one before the new start
						res.add(new ValueRange(start, rangeVal.value.subtract(BigInteger.ONE)));
				}
				// Set the start to the current Element
				start = rangeVal.value;
			} else {
				// End the current range at this Element
				res.add(new ValueRange(start, rangeVal.value));
				if (count > 0) {
					// If we expect another end later, the element following
					// this will have to start one after
					start = rangeVal.value.add(BigInteger.ONE);
				} else
					// No new range anymore
					start = null;
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

	private static HDLUnit createMultiplexArrayWrite(HDLEvaluationContext context, HDLUnit unit) {
		ModificationSet ms = new ModificationSet();
		HDLAssignment[] asss = unit.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss) {
			if (ass.getLeft() instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) ass.getLeft();
				// XXX check for multi-dimensional arrays and create appropriate
				// code
				for (HDLExpression arr : ref.getArray()) {
					// The range that potentially could be accessed
					ValueRange accessRange = arr.determineRange(context);
					// The range that the array could be sized
					ValueRange dimensionRange = ref.resolveVar().getDimensions().get(0).determineRange(context);
					// Take the maximum range as the upper boundary and 0 as
					// lower
					dimensionRange = new ValueRange(BigInteger.ZERO, dimensionRange.to.subtract(BigInteger.ONE));
					accessRange = accessRange.and(dimensionRange);
					if (accessRange == null)
						throw new HDLProblemException(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr));
					BigInteger counter = accessRange.from;
					List<HDLStatement> replacements = new ArrayList<HDLStatement>();
					do {
						HDLExpression ifExp = new HDLEqualityOp().setLeft(arr).setType(HDLEqualityOpType.EQ).setRight(HDLLiteral.get(counter)).setContainer(ass);
						ifExp = ifExp.copyDeepFrozen(ass);
						BigInteger evaluate = ifExp.constantEvaluate(context);
						if (evaluate == null) {
							HDLVariableRef writeRef = ref.setArray(HDLObject.asList((HDLExpression) HDLLiteral.get(counter)));
							HDLIfStatement ifStmnt = new HDLIfStatement().setIfExp(ifExp).addThenDo(ass.setLeft(writeRef));
							replacements.add(ifStmnt);
						} else {
							replacements.add(ass);
						}
						counter = counter.add(BigInteger.ONE);
					} while (counter.compareTo(accessRange.to) <= 0);
					ms.replace(ass, replacements.toArray(new HDLStatement[0]));
				}
			}
		}
		return ms.apply(unit);
	}

	/**
	 * Unrolls a loop and creates the statements with an accordingly replaced
	 * loop parameter
	 * 
	 * @param context
	 * @param insulin
	 * @return
	 */
	private static HDLUnit unrollForLoops(HDLEvaluationContext context, HDLUnit insulin) {
		HDLForLoop[] loops = insulin.getAllObjectsOf(HDLForLoop.class, true);
		ModificationSet ms = new ModificationSet();
		for (HDLForLoop loop : loops) {
			HDLVariable param = loop.getParam();
			ValueRange r = loop.getRange().get(0).determineRange(context);
			List<HDLStatement> newStmnts = new ArrayList<HDLStatement>();
			for (HDLStatement stmnt : loop.getDos()) {
				Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(stmnt).where(HDLVariableRef.fVar).lastSegmentIs(param.getName()).getAll();
				if (refs.size() == 0)
					newStmnts.add(stmnt);
				else {
					BigInteger counter = r.from;
					do {
						ModificationSet stmntMs = new ModificationSet();
						for (HDLVariableRef ref : refs) {
							stmntMs.replace(ref, HDLLiteral.get(counter));
						}
						newStmnts.add(stmntMs.apply(stmnt));
						counter = counter.add(BigInteger.ONE);
					} while (counter.compareTo(r.to) <= 0);
				}
			}
			ms.replace(loop, newStmnts.toArray(new HDLStatement[0]));
		}
		return ms.apply(insulin);
	}
}
