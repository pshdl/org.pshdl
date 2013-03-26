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
import java.util.Map.Entry;

import javax.annotation.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.simulation.RangeTool.RangeVal;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

public class HDLSimulator {

	public static HDLUnit createSimulationModel(HDLUnit unit, HDLEvaluationContext context, String src) {
		HDLUnit insulin = Insulin.transform(unit, src);
		insulin = unrollForLoops(context, insulin);
		insulin = createMultiplexArrayWrite(context, insulin);
		insulin = renameArrayAccess(context, insulin);
		insulin = createBitRanges(context, insulin);
		insulin = literalBitRanges(context, insulin);
		insulin = createTernary(context, insulin);
		insulin.validateAllFields(insulin.getContainer(), true);
		return insulin;
		// generate cat statement for ranges
		// generate reset condition
		// Starting from the end, generate ternary op for each switch/if
		// statement
	}

	private static HDLUnit renameArrayAccess(HDLEvaluationContext context, HDLUnit insulin) {
		ModificationSet ms = new ModificationSet();
		HDLVariableRef[] refs = insulin.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef variableRef : refs)
			if (!variableRef.getArray().isEmpty()) {
				HDLQualifiedName newName = variableRef.getVarRefName();
				String lastSegment = newName.getLastSegment();
				for (HDLExpression arr : variableRef.getArray()) {
					Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(arr, context);
					if (valueOf.isPresent()) {
						lastSegment += "[" + valueOf.get() + "]";
					} else
						throw new IllegalArgumentException(arr + " not constant");
				}
				newName = newName.skipLast(1).append(lastSegment);
				ms.replace(variableRef, variableRef.setVar(newName).setArray(null));
			}
		HDLVariableDeclaration[] varDecls = insulin.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : varDecls) {
			for (HDLVariable var : hvd.getVariables()) {
				ArrayList<HDLExpression> dim = var.getDimensions();
				if (!dim.isEmpty()) {
					List<HDLVariable> newVariables = new LinkedList<HDLVariable>();
					newVariables = (createArrayVar(dim, 0, var.getName(), context));
					ms.replace(var, newVariables.toArray(new HDLVariable[newVariables.size()]));
				}
			}
		}
		return ms.apply(insulin);
	}

	private static List<HDLVariable> createArrayVar(ArrayList<HDLExpression> dim, int idx, @Nonnull String name, HDLEvaluationContext context) {
		if (idx >= dim.size())
			return Collections.singletonList(new HDLVariable().setName(name));
		Optional<BigInteger> size = ConstantEvaluate.valueOf(dim.get(idx), context);
		if (!size.isPresent())
			throw new IllegalArgumentException("The dimension " + dim.get(idx) + " is not constant.");
		List<HDLVariable> res = new LinkedList<HDLVariable>();
		for (int i = 0; i < size.get().intValue(); i++) {
			res.addAll(createArrayVar(dim, i + 1, name + "[" + i + "]", context));
		}
		return res;
	}

	private static HDLUnit literalBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		ModificationSet ms = new ModificationSet();
		HDLRange[] ranges = insulin.getAllObjectsOf(HDLRange.class, true);
		for (HDLRange hdlRange : ranges) {
			Optional<BigInteger> toBig = ConstantEvaluate.valueOf(hdlRange.getTo(), context);
			if (!toBig.isPresent())
				throw new IllegalArgumentException("Given the context it should always be non null");
			HDLExpression from = hdlRange.getFrom();
			if (from != null) {
				Optional<BigInteger> fromBig = ConstantEvaluate.valueOf(from, context);
				if (!fromBig.isPresent())
					throw new IllegalArgumentException("Given the context it should always be non null");
				ms.replace(hdlRange, hdlRange.setFrom(HDLLiteral.get(fromBig.get())).setTo(HDLLiteral.get(toBig.get())));
			} else {
				ms.replace(hdlRange, hdlRange.setTo(HDLLiteral.get(toBig.get())));
			}
		}
		return ms.apply(insulin);
	}

	private static HDLUnit createTernary(HDLEvaluationContext context, HDLUnit insulin) {
		HDLAssignment[] asss = insulin.getAllObjectsOf(HDLAssignment.class, true);
		Map<String, LinkedList<HDLAssignment>> writeOps = new LinkedHashMap<String, LinkedList<HDLAssignment>>();
		Map<String, HDLReference> references = new LinkedHashMap<String, HDLReference>();
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
		ArrayList<HDLStatement> initStatements = new ArrayList<HDLStatement>();
		finalStatements.addAll(Arrays.asList(hvds));
		int insertIndex = finalStatements.size();
		for (Entry<String, LinkedList<HDLAssignment>> entry : writeOps.entrySet()) {
			LinkedList<HDLAssignment> list = entry.getValue();
			Iterator<HDLAssignment> reverseIter = list.descendingIterator();
			while (reverseIter.hasNext()) {
				HDLAssignment next = reverseIter.next();
				// XXX Add if cases

				IHDLObject container = next.getContainer();
				if (container.getClassType() == HDLClass.HDLUnit) {
					initStatements.add(next);
					break;
				}
				if (container.getClassType() == HDLClass.HDLIfStatement) {
					HDLIfStatement hif = (HDLIfStatement) container;
					finalStatements.add(insertIndex, hif.setThenDo(HDLObject.asList((HDLStatement) next)));
				}
			}
		}
		return insulin.setInits(initStatements).setStatements(finalStatements).copyDeepFrozen(insulin.getContainer());
	}

	private static HDLUnit createBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		HDLVariableRef[] refs = insulin.getAllObjectsOf(HDLVariableRef.class, true);
		Map<HDLQualifiedName, List<RangeVal>> ranges = new LinkedHashMap<HDLQualifiedName, List<RangeVal>>();
		Map<HDLQualifiedName, Range<BigInteger>> fullRanges = new LinkedHashMap<HDLQualifiedName, Range<BigInteger>>();
		for (HDLVariableRef ref : refs)
			if (ref.getContainer() instanceof HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) ref.getContainer();
				if (ass.getLeft() == ref) {
					HDLVariable resolveVar = ref.resolveVar().get();
					if (resolveVar.getDirection() != HDLDirection.IN) {
						HDLQualifiedName varRefName = ref.getVarRefName();
						if (ref.getBits().size() > 0) {
							List<RangeVal> set = ranges.get(varRefName);
							if (set == null) {
								set = new LinkedList<RangeVal>();
								ranges.put(varRefName, set);
							}
							for (HDLRange r : ref.getBits()) {
								Optional<Range<BigInteger>> determineRange = RangeExtension.rangeOf(r, context);
								if (!determineRange.isPresent())
									throw new IllegalArgumentException("Can not determine Range for:" + r);
								set.add(new RangeVal(determineRange.get().lowerEndpoint(), 1));
								set.add(new RangeVal(determineRange.get().upperEndpoint(), -1));
							}
						} else {
							HDLExpression width = TypeExtension.getWidth(resolveVar);
							if (width != null) {
								Optional<BigInteger> bWidth = ConstantEvaluate.valueOf(width, context);
								if (!bWidth.isPresent())
									throw new IllegalArgumentException("Given the context this should be constant");
								fullRanges.put(varRefName, Ranges.closed(BigInteger.ZERO, bWidth.get().subtract(BigInteger.ONE)));
							}
						}
					}
				}
			}
		ModificationSet ms = new ModificationSet();
		Map<HDLQualifiedName, SortedSet<Range<BigInteger>>> splitRanges = new LinkedHashMap<HDLQualifiedName, SortedSet<Range<BigInteger>>>();
		for (Map.Entry<HDLQualifiedName, List<RangeVal>> entry : ranges.entrySet()) {
			List<RangeVal> value = entry.getValue();
			HDLQualifiedName varName = entry.getKey();
			if (fullRanges.containsKey(varName)) {
				Range<BigInteger> fullWidth = fullRanges.get(varName);
				value.add(new RangeVal(fullWidth.lowerEndpoint(), 1));
				value.add(new RangeVal(fullWidth.upperEndpoint(), -1));
			}
			SortedSet<Range<BigInteger>> split = RangeTool.split(value);
			splitRanges.put(varName, split);
		}
		// Change bit access to broken down ranges
		for (HDLVariableRef ref : refs) {
			SortedSet<Range<BigInteger>> list = splitRanges.get(ref.getVarRefName());
			if (list != null) {
				ArrayList<HDLRange> newRanges = new ArrayList<HDLRange>();
				if (!ref.getBits().isEmpty()) {
					for (HDLRange bit : ref.getBits())
						if (bit.getFrom() != null) { // Singular ranges don't do
														// anything
							Optional<Range<BigInteger>> range = RangeExtension.rangeOf(bit, context);
							if (!range.isPresent())
								throw new IllegalArgumentException("Can not determine Range of:" + bit);
							for (Range<BigInteger> newRange : list)
								if (range.get().isConnected(newRange)) {
									newRanges.add(0, createRange(newRange));
								}
						} else {
							newRanges.add(0, bit);
						}
				} else {
					for (Range<BigInteger> vRange : list) {
						newRanges.add(0, createRange(vRange));
					}
				}
				if (newRanges.size() != 0) {
					ms.replace(ref, ref.setBits(newRanges));
				}
			}
		}
		HDLUnit apply = ms.apply(insulin);
		return Insulin.handleMultiBitAccess(apply, context);
	}

	private static HDLRange createRange(Range<BigInteger> newRange) {
		if (newRange.lowerEndpoint().equals(newRange.upperEndpoint()))
			return new HDLRange().setTo(HDLLiteral.get(newRange.upperEndpoint()));
		return new HDLRange().setTo(HDLLiteral.get(newRange.lowerEndpoint())).setFrom(HDLLiteral.get(newRange.upperEndpoint()));
	}

	private static HDLUnit createMultiplexArrayWrite(HDLEvaluationContext context, HDLUnit unit) {
		ModificationSet ms = new ModificationSet();
		HDLAssignment[] asss = unit.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : asss)
			if (ass.getLeft() instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) ass.getLeft();
				// XXX check for multi-dimensional arrays and create appropriate
				// code
				for (HDLExpression arr : ref.getArray()) {
					// The range that potentially could be accessed
					Optional<Range<BigInteger>> accessRangeRaw = RangeExtension.rangeOf(arr, context);
					// The range that the array could be sized
					if (!accessRangeRaw.isPresent())
						throw new IllegalArgumentException("Can not determine Range of :" + arr);
					Range<BigInteger> accessRange = accessRangeRaw.get();
					Optional<Range<BigInteger>> dimensionRangeRaw = RangeExtension.rangeOf(ref.resolveVar().get().getDimensions().get(0), context);
					if (!dimensionRangeRaw.isPresent())
						throw new IllegalArgumentException("Can not determine Range of :" + arr);
					Range<BigInteger> dimensionRange = dimensionRangeRaw.get();
					// Take the maximum range as the upper boundary and 0 as
					// lower
					dimensionRange = Ranges.closed(BigInteger.ZERO, dimensionRange.upperEndpoint().subtract(BigInteger.ONE));
					if (!accessRange.isConnected(dimensionRange))
						throw new HDLProblemException(new Problem(ErrorCode.ARRAY_INDEX_OUT_OF_BOUNDS, arr));
					BigInteger counter = accessRange.lowerEndpoint();
					List<HDLStatement> replacements = new ArrayList<HDLStatement>();
					do {
						HDLExpression ifExp = new HDLEqualityOp().setLeft(arr).setType(HDLEqualityOpType.EQ).setRight(HDLLiteral.get(counter)).setContainer(ass);
						ifExp = ifExp.copyDeepFrozen(ass);
						Optional<BigInteger> evaluate = ConstantEvaluate.valueOf(ifExp, context);
						if (!evaluate.isPresent()) {
							HDLVariableRef writeRef = ref.setArray(HDLObject.asList((HDLExpression) HDLLiteral.get(counter)));
							HDLIfStatement ifStmnt = new HDLIfStatement().setIfExp(ifExp).addThenDo(ass.setLeft(writeRef));
							replacements.add(ifStmnt);
						} else {
							replacements.add(ass);
						}
						counter = counter.add(BigInteger.ONE);
					} while (counter.compareTo(accessRange.upperEndpoint()) <= 0);
					ms.replace(ass, replacements.toArray(new HDLStatement[0]));
				}
			}
		return ms.apply(unit);
	}

	/**
	 * Unrolls a loop and creates the statements with an accordingly replaced
	 * loop parameter
	 * 
	 * @param context
	 * @param unit
	 * @return
	 */
	private static HDLUnit unrollForLoops(HDLEvaluationContext context, HDLUnit unit) {
		HDLForLoop[] loops = unit.getAllObjectsOf(HDLForLoop.class, true);
		ModificationSet ms = new ModificationSet();
		for (HDLForLoop loop : loops) {
			HDLVariable param = loop.getParam();
			Optional<Range<BigInteger>> r = RangeExtension.rangeOf(loop.getRange().get(0), context);
			List<HDLStatement> newStmnts = new ArrayList<HDLStatement>();
			for (HDLStatement stmnt : loop.getDos()) {
				Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(stmnt).where(HDLResolvedRef.fVar).lastSegmentIs(param.getName()).getAll();
				if (refs.size() == 0) {
					newStmnts.add(stmnt);
				} else {
					BigInteger counter = r.get().lowerEndpoint();
					do {
						ModificationSet stmntMs = new ModificationSet();
						for (HDLVariableRef ref : refs) {
							stmntMs.replace(ref, HDLLiteral.get(counter));
						}
						newStmnts.add(stmntMs.apply(stmnt));
						counter = counter.add(BigInteger.ONE);
					} while (counter.compareTo(r.get().upperEndpoint()) <= 0);
				}
			}
			ms.replace(loop, newStmnts.toArray(new HDLStatement[0]));
		}
		return ms.apply(unit);
	}
}
