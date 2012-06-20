package de.tuhh.ict.pshdl.model.simulation;

import java.io.*;
import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;

public class HDLSimulator {
	public static void createDottyGraph(HDLUnit unit, HDLEvaluationContext context, File f) throws FileNotFoundException {
		PrintStream out = new PrintStream(f);
		out.println("digraph " + unit.getFullName().toString('_') + " {");
		List<HDLVariableDeclaration> hvds = unit.getAllObjectsOf(HDLVariableDeclaration.class, true);
		Map<String, String> connectTo = new HashMap<String, String>();
		for (HDLVariableDeclaration hvd : hvds) {
			for (HDLVariable var : hvd.getVariables()) {
				if (hvd.getRegister() != null) {
					out.println(var.getName() + "[shape=box,color=blue];");
					out.println(var.getName() + "target[shape=box,color=green,label=\"" + var.getName() + "\"];");
					connectTo.put(var.getName(), var.getName() + "target");
				} else {
					out.println(var.getName() + "[shape=box,color=orange];");
					connectTo.put(var.getName(), var.getName());
				}
			}
		}
		List<HDLExpression> exps = unit.getAllObjectsOf(HDLExpression.class, true);
		for (HDLExpression exp : exps) {
			HDLObject container = exp.getContainer();
			if (container instanceof HDLType) {
				continue;
			}
			if (exp instanceof HDLVariableRef) {
				HDLVariableRef href = (HDLVariableRef) exp;
				if ((href.getBits().size() == 0) && (href.getArray().size() == 0))
					continue;
			}
			out.println(Math.abs(exp.containerID) + "[label=\"" + exp.toDottyLabel() + "\"];");
		}
		for (HDLExpression exp : exps) {
			HDLObject container = exp.getContainer();
			if (container instanceof HDLType) {
				continue;
			}
			if (container instanceof HDLStatement) {
				HDLStatement stmnt = (HDLStatement) container;
				switch (stmnt.getClassType()) {
				case HDLAssignment:
					HDLAssignment ass = (HDLAssignment) stmnt;
					if (exp == ass.getLeft()) {
						int tempLeft = ass.getLeft().containerID;

						String varName = ass.getLeft().getVarRefName().getLastSegment();
						if (exp instanceof HDLVariableRef) {
							HDLVariableRef href = (HDLVariableRef) exp;
							if ((href.getBits().size() == 0) && (href.getArray().size() == 0)) {
								out.println(Math.abs(ass.getRight().containerID) + "->" + connectTo.get(varName));
							} else {
								out.println(connect(ass, ass.getRight().containerID, tempLeft));
								out.println(tempLeft + "->" + connectTo.get(varName));
							}
						} else
							out.println(tempLeft + "->" + connectTo.get(varName));
					} else {
						if (exp instanceof HDLVariableRef) {
							HDLVariableRef href = (HDLVariableRef) exp;
							out.println(href.getVarRefName().getLastSegment() + "->" + Math.abs(exp.containerID));
						}
					}
					break;
				default:
				}
				System.out.println("HDLSimulator.createDottyGraph()" + container);
				continue;
			}
			if (container instanceof HDLVariable) {
				HDLVariable var = (HDLVariable) container;
				out.println(Math.abs(exp.containerID) + "->" + connectTo.get(var.getName()));
				continue;
			}
			if (exp instanceof HDLVariableRef) {
				HDLVariableRef href = (HDLVariableRef) exp;
				if ((href.getBits().size() == 0) && (href.getArray().size() == 0)) {
					out.println(href.getVarRefName().getLastSegment() + "->" + Math.abs(exp.getContainer().containerID));
					continue;
				}
				out.println(href.getVarRefName().getLastSegment() + "->" + Math.abs(exp.containerID));
			}
			out.println(connect(exp));
		}
		out.println("}");
	}

	public static String connect(HDLObject obj) {
		int src = obj.containerID;
		int target = obj.getContainer().containerID;
		return connect(obj, src, target);
	}

	private static String connect(HDLObject obj, int src, int target) {
		return Math.abs(src) + "->" + Math.abs(target) + "; //" + obj;
	}

	public static HDLUnit createSimulationModel(HDLUnit unit, HDLEvaluationContext context) {
		HDLUnit insulin = Insulin.transform(unit);
		insulin = unrollForLoops(context, insulin);
		insulin = createMultiplexArrayWrite(context, insulin);
		insulin = createBitRanges(context, insulin);
		insulin.validateAllFields(insulin.getContainer(), true);
		return insulin;
		// Find all bit access
		// Determine value ranges for bit accesses
		// Create non overlapping ranges
		// generate cat statement for ranges
		// Starting from the end, generate multiplexer for each switch/if
		// statement
	}

	private static HDLUnit createBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		List<HDLVariableRef> refs = insulin.getAllObjectsOf(HDLVariableRef.class, true);
		Map<HDLQualifiedName, List<RangeVal>> ranges = new TreeMap<HDLQualifiedName, List<RangeVal>>();
		for (HDLVariableRef ref : refs) {
			if (ref.getBits().size() > 0) {
				List<RangeVal> set = ranges.get(ref.getVarRefName());
				if (set == null) {
					set = new LinkedList<RangeVal>();
				}
				for (HDLRange r : ref.getBits()) {
					ValueRange determineRange = r.determineRange(context);
					set.add(new RangeVal(determineRange.from, 1));
					set.add(new RangeVal(determineRange.to, -1));
				}
			}
		}
		Map<HDLQualifiedName, SortedSet<ValueRange>> splitRanges = new TreeMap<HDLQualifiedName, SortedSet<ValueRange>>();
		for (Map.Entry<HDLQualifiedName, List<RangeVal>> entry : ranges.entrySet()) {
			splitRanges.put(entry.getKey(), split(entry.getValue()));
		}

		ModificationSet ms = new ModificationSet();
		return ms.apply(insulin);
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
		List<HDLAssignment> asss = unit.getAllObjectsOf(HDLAssignment.class, true);
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
						HDLExpression ifExp = new HDLEqualityOp().setLeft(arr.copy()).setType(HDLEqualityOpType.EQ).setRight(HDLLiteral.get(counter)).setContainer(ass);
						BigInteger evaluate = ifExp.constantEvaluate(context);
						if (evaluate == null) {
							HDLVariableRef writeRef = ref.copy().setArray(HDLObject.asList(HDLLiteral.get(counter)));
							HDLIfStatement ifStmnt = new HDLIfStatement().setIfExp(ifExp.copy()).addThenDo(ass.copy().setLeft(writeRef));
							replacements.add(ifStmnt);
						} else {
							replacements.add(ass.copy());
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
		List<HDLForLoop> loops = insulin.getAllObjectsOf(HDLForLoop.class, true);
		ModificationSet ms = new ModificationSet();
		for (HDLForLoop loop : loops) {
			HDLVariable param = loop.getParam();
			ValueRange r = loop.getRange().get(0).determineRange(context);
			List<HDLStatement> newStmnts = new ArrayList<HDLStatement>();
			for (HDLStatement stmnt : loop.getDos()) {
				List<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(stmnt).where(HDLVariableRef.fVar).isEqualTo(param.asRef()).getAll();
				if (refs.size() == 0)
					newStmnts.add(stmnt.copy());
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
