package de.tuhh.ict.pshdl.model.simulation;

import java.io.*;
import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives.ValueRange;
import de.tuhh.ict.pshdl.model.utils.*;

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
		return insulin;
		// Create a signal for each Array Element
		// Find all bit access
		// Determine value ranges for array and bit accesses
		// Create non overlapping ranges
		// generate cat statement for ranges
		// Starting from the end, generate multiplexer for each switch/if
		// statement
	}

	private static HDLUnit createMultiplexArrayWrite(HDLEvaluationContext context, HDLUnit unit) {
		List<HDLAssignment> asss = unit.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss) {
			if (ass.getLeft() instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) ass.getLeft();
				for (HDLExpression arr : ref.getArray()) {
					BigInteger index = arr.constantEvaluate(context);
					if (index == null) {
						HDLType type = arr.determineType();
						HDLPrimitive pt = (HDLPrimitive) type;
						ValueRange range = HDLPrimitives.getInstance().getValueRange(pt, context);

					}
				}
			}
		}
		return null;
	}

	private static HDLUnit unrollForLoops(HDLEvaluationContext context, HDLUnit insulin) {
		List<HDLForLoop> loops = insulin.getAllObjectsOf(HDLForLoop.class, true);
		ModificationSet ms = new ModificationSet();
		for (HDLForLoop loop : loops) {
			HDLVariable param = loop.getParam();
			HDLRange range = loop.getRange().get(0); // Only one range expected
														// here because of
														// Insulin
			BigInteger to = range.getTo().constantEvaluate(context);
			BigInteger from = to;
			if (range.getFrom() != null)
				from = range.getFrom().constantEvaluate(context);
			if (to.compareTo(from) < 1) {
				BigInteger tmp = from;
				from = to;
				to = tmp;
			}
			System.out.println("HDLSimulator.createSimulationModel()From:" + from + " to:" + to);
			List<HDLStatement> newStmnts = new ArrayList<HDLStatement>();
			for (HDLStatement stmnt : loop.getDos()) {
				List<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(stmnt).where(HDLVariableRef.fVar).isEqualTo(param.asRef()).getAll();
				if (refs.size() == 0)
					newStmnts.add(stmnt.copy());
				else {
					BigInteger counter = from;
					do {
						ModificationSet stmntMs = new ModificationSet();
						for (HDLVariableRef ref : refs) {
							stmntMs.replace(ref, HDLLiteral.get(counter));
						}
						newStmnts.add(stmntMs.apply(stmnt));
						counter = counter.add(BigInteger.ONE);
					} while (counter.compareTo(to) <= 0);
				}
			}
			ms.replace(loop, newStmnts.toArray(new HDLStatement[0]));
		}
		return ms.apply(insulin);
	}
}
