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

import java.math.*;
import java.util.*;
import java.util.concurrent.atomic.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLIfStatement.TreeSide;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.simulation.RangeTool.RangeVal;
import org.pshdl.model.utils.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class HDLSimulator {

	public static HDLUnit createSimulationModel(HDLUnit unit, HDLEvaluationContext context, String src) {
		HDLUnit insulin = Insulin.transform(unit, src);
		insulin = flattenAll(context, insulin);
		insulin = convertArrayInits(context, insulin);
		insulin = unrollForLoops(context, insulin);
		insulin = createBitRanges(context, insulin);
		insulin = literalBitRanges(context, insulin);
		insulin = convertTernary(context, insulin);
		insulin = removeDoubleAssignments(context, insulin);
		insulin.validateAllFields(insulin.getContainer(), true);
		return insulin;
	}

	private static HDLUnit convertArrayInits(HDLEvaluationContext context, HDLUnit insulin) {
		final ModificationSet ms = new ModificationSet();
		final HDLArrayInit[] inits = insulin.getAllObjectsOf(HDLArrayInit.class, true);
		for (final HDLArrayInit arrayInit : inits) {
			final IHDLObject container = arrayInit.getContainer();
			switch (container.getClassType()) {
			case HDLArrayInit:
				break;
			case HDLAssignment:
				final HDLReference left = ((HDLAssignment) container).getLeft();
				if (!(left instanceof HDLVariableRef))
					throw new IllegalArgumentException("Unsupported assignment target for ArrayInit:" + left);
				final HDLVariableRef varRef = (HDLVariableRef) left;
				addInit(varRef, arrayInit, container, ms);
				ms.remove(container);
				break;
			case HDLVariable:
				final HDLVariable var = (HDLVariable) container;
				addInit(var.asHDLRef(), arrayInit, var.getContainer(), ms);
				ms.replace(var, var.setDefaultValue(null));
				break;
			case HDLRegisterConfig:
				// resetValues are taken care of later
				break;
			default:
				throw new IllegalArgumentException("Unsupported container for ArrayInit:" + container);
			}
		}
		return ms.apply(insulin);
	}

	private static void addInit(HDLVariableRef varRef, HDLArrayInit arrayInit, IHDLObject container, ModificationSet ms) {
		final ArrayList<HDLExpression> exp = arrayInit.getExp();
		for (int i = 0; i < exp.size(); i++) {
			final HDLExpression subExp = exp.get(i);
			final HDLVariableRef addArray = varRef.addArray(HDLLiteral.get(i));
			if (subExp instanceof HDLArrayInit) {
				final HDLArrayInit subArray = (HDLArrayInit) subExp;
				addInit(addArray, subArray, container, ms);
			} else {
				final HDLAssignment ass = new HDLAssignment().setLeft(addArray).setRight(subExp);
				ms.insertAfter(container, ass);
			}
		}
	}

	private static HDLUnit flattenAll(HDLEvaluationContext context, HDLUnit insulin) {
		HDLInterfaceInstantiation[] hii = null;
		while ((hii = insulin.getAllObjectsOf(HDLInterfaceInstantiation.class, true)).length > 0) {
			final HDLLibrary library = insulin.getLibrary();
			final HDLInterfaceInstantiation hi = hii[0];
			final HDLQualifiedName asRef = hi.resolveHIf().get().asRef();
			HDLUnit unit = library.getUnit(asRef);
			if (unit == null)
				throw new IllegalArgumentException("Can not find unit for interface:" + asRef);
			unit = Insulin.transform(unit, library.getSrc(asRef));
			final HDLEvaluationContext hiContext = hi.getContext(HDLEvaluationContext.createDefault(unit));
			final HDLUnit subUnit = flattenAll(hiContext, unit);
			insulin = Refactoring.inlineUnit(insulin, hi, subUnit);
		}
		return insulin;
	}

	private static class InitTuple {
		public final HDLReference ref;
		public final IHDLObject container;

		public InitTuple(HDLReference ref, IHDLObject container) {
			super();
			this.ref = ref;
			this.container = container;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((container == null) ? 0 : container.hashCode());
			result = (prime * result) + ((ref == null) ? 0 : ref.hashCode());
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
			final InitTuple other = (InitTuple) obj;
			if (container != other.container)
				return false;
			if (ref == null) {
				if (other.ref != null)
					return false;
			} else if (!ref.equals(other.ref))
				return false;
			return true;
		}

	}

	private static HDLUnit removeDoubleAssignments(HDLEvaluationContext context, HDLUnit insulin) {
		final ModificationSet ms = new ModificationSet();
		final HDLAssignment[] asss = insulin.getAllObjectsOf(HDLAssignment.class, true);
		final Map<InitTuple, HDLAssignment> scopeWrites = new HashMap<InitTuple, HDLAssignment>();
		for (final HDLAssignment hdlAssignment : asss) {
			final IHDLObject container = hdlAssignment.getContainer();
			final InitTuple it = new InitTuple(hdlAssignment.getLeft(), container);
			final HDLAssignment otherAss = scopeWrites.get(it);
			if (otherAss != null) {
				if (container instanceof HDLIfStatement) {
					final HDLIfStatement hdlIfStatement = (HDLIfStatement) container;
					final TreeSide oSide = hdlIfStatement.treeSide(otherAss);
					final TreeSide tSide = hdlIfStatement.treeSide(hdlAssignment);
					if (oSide == tSide) {
						ms.remove(otherAss);
					}
				} else {
					ms.remove(otherAss);
				}
			}
			scopeWrites.put(it, hdlAssignment);
		}
		return ms.apply(insulin);
	}

	private static AtomicInteger tempID = new AtomicInteger();

	private static HDLUnit convertTernary(HDLEvaluationContext context, HDLUnit insulin) {
		final ModificationSet ms = new ModificationSet();
		final HDLTernary[] ternaries = insulin.getAllObjectsOf(HDLTernary.class, true);
		for (final HDLTernary ternary : ternaries) {
			final Optional<? extends HDLType> typeOf = TypeExtension.typeOf(ternary);
			final String name = "$tmp_" + tempID.getAndIncrement();
			final HDLVariable var = new HDLVariable().setName(name);
			final HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(typeOf.get()).addVariables(var);
			final HDLAssignment newAss = new HDLAssignment().setLeft(var.asHDLRef());
			final HDLIfStatement ifStatement = new HDLIfStatement()//
					.setIfExp(ternary.getIfExpr())//
					.addThenDo(newAss.setRight(ternary.getThenExpr()))//
					.addElseDo(newAss.setRight(ternary.getElseExpr()));
			ms.replace(ternary, var.asHDLRef());
			ms.insertBefore(ternary.getContainer(HDLStatement.class), hvd, ifStatement);
		}
		return ms.apply(insulin);
	}

	private static HDLUnit literalBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		final ModificationSet ms = new ModificationSet();
		final HDLRange[] ranges = insulin.getAllObjectsOf(HDLRange.class, true);
		for (final HDLRange hdlRange : ranges) {
			final Optional<BigInteger> toBig = ConstantEvaluate.valueOf(hdlRange.getTo(), context);
			if (!toBig.isPresent())
				throw new IllegalArgumentException("Given the context it should always be non null");
			final HDLExpression from = hdlRange.getFrom();
			if (from != null) {
				final Optional<BigInteger> fromBig = ConstantEvaluate.valueOf(from, context);
				if (!fromBig.isPresent())
					throw new IllegalArgumentException("Given the context it should always be non null");
				ms.replace(hdlRange, hdlRange.setFrom(HDLLiteral.get(fromBig.get())).setTo(HDLLiteral.get(toBig.get())));
			} else {
				ms.replace(hdlRange, hdlRange.setTo(HDLLiteral.get(toBig.get())));
			}
		}
		return ms.apply(insulin);
	}

	private static HDLUnit createBitRanges(HDLEvaluationContext context, HDLUnit insulin) {
		final HDLVariableRef[] refs = insulin.getAllObjectsOf(HDLVariableRef.class, true);
		final Map<HDLQualifiedName, List<RangeVal>> ranges = new LinkedHashMap<HDLQualifiedName, List<RangeVal>>();
		final Map<HDLQualifiedName, Range<BigInteger>> fullRanges = new LinkedHashMap<HDLQualifiedName, Range<BigInteger>>();
		for (final HDLVariableRef ref : refs)
			if (ref.getContainer() instanceof HDLAssignment) {
				final HDLAssignment ass = (HDLAssignment) ref.getContainer();
				if (ass.getLeft() == ref) {
					final Optional<HDLVariable> resolved = ref.resolveVar();
					if (!resolved.isPresent())
						throw new IllegalArgumentException("Can not resolve:" + ref.getVarRefName());
					final HDLVariable resolveVar = resolved.get();
					if (resolveVar.getDirection() != HDLDirection.IN) {
						final HDLQualifiedName varRefName = ref.getVarRefName();
						if (ref.getBits().size() > 0) {
							List<RangeVal> set = ranges.get(varRefName);
							if (set == null) {
								set = new LinkedList<RangeVal>();
								ranges.put(varRefName, set);
							}
							for (final HDLRange r : ref.getBits()) {
								final Optional<Range<BigInteger>> determineRange = RangeExtension.rangeOf(r, context);
								if (!determineRange.isPresent())
									throw new IllegalArgumentException("Can not determine Range for:" + r);
								set.add(new RangeVal(determineRange.get().lowerEndpoint(), 1));
								set.add(new RangeVal(determineRange.get().upperEndpoint(), -1));
							}
						} else {
							final HDLExpression width = TypeExtension.getWidth(resolveVar);
							if (width != null) {
								final Optional<BigInteger> bWidth = ConstantEvaluate.valueOf(width, context);
								if (!bWidth.isPresent())
									throw new IllegalArgumentException("Given the context this should be constant");
								fullRanges.put(varRefName, Ranges.closed(BigInteger.ZERO, bWidth.get().subtract(BigInteger.ONE)));
							}
						}
					}
				}
			}
		final ModificationSet ms = new ModificationSet();
		final Map<HDLQualifiedName, SortedSet<Range<BigInteger>>> splitRanges = new LinkedHashMap<HDLQualifiedName, SortedSet<Range<BigInteger>>>();
		for (final Map.Entry<HDLQualifiedName, List<RangeVal>> entry : ranges.entrySet()) {
			final List<RangeVal> value = entry.getValue();
			final HDLQualifiedName varName = entry.getKey();
			if (fullRanges.containsKey(varName)) {
				final Range<BigInteger> fullWidth = fullRanges.get(varName);
				value.add(new RangeVal(fullWidth.lowerEndpoint(), 1));
				value.add(new RangeVal(fullWidth.upperEndpoint(), -1));
			}
			final SortedSet<Range<BigInteger>> split = RangeTool.split(value);
			splitRanges.put(varName, split);
		}
		// Change bit access to broken down ranges
		for (final HDLVariableRef ref : refs) {
			final SortedSet<Range<BigInteger>> list = splitRanges.get(ref.getVarRefName());
			if (list != null) {
				final ArrayList<HDLRange> newRanges = new ArrayList<HDLRange>();
				if (!ref.getBits().isEmpty()) {
					for (final HDLRange bit : ref.getBits())
						if (bit.getFrom() != null) { // Singular ranges don't do
														// anything
							final Optional<Range<BigInteger>> range = RangeExtension.rangeOf(bit, context);
							if (!range.isPresent())
								throw new IllegalArgumentException("Can not determine Range of:" + bit);
							for (final Range<BigInteger> newRange : list)
								if (range.get().isConnected(newRange)) {
									newRanges.add(0, createRange(newRange));
								}
						} else {
							newRanges.add(0, bit);
						}
				} else {
					for (final Range<BigInteger> vRange : list) {
						newRanges.add(0, createRange(vRange));
					}
				}
				if (newRanges.size() != 0) {
					ms.replace(ref, ref.setBits(newRanges));
				}
			}
		}
		final HDLUnit apply = ms.apply(insulin);
		return Insulin.handleMultiBitAccess(apply, context);
	}

	private static HDLRange createRange(Range<BigInteger> newRange) {
		if (newRange.lowerEndpoint().equals(newRange.upperEndpoint()))
			return new HDLRange().setTo(HDLLiteral.get(newRange.upperEndpoint()));
		return new HDLRange().setTo(HDLLiteral.get(newRange.lowerEndpoint())).setFrom(HDLLiteral.get(newRange.upperEndpoint()));
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
		final HDLForLoop[] loops = unit.getAllObjectsOf(HDLForLoop.class, true);
		final ModificationSet ms = new ModificationSet();
		for (final HDLForLoop loop : loops) {
			final HDLVariable param = loop.getParam();
			final Optional<Range<BigInteger>> r = RangeExtension.rangeOf(loop.getRange().get(0), context);
			final List<HDLStatement> newStmnts = new ArrayList<HDLStatement>();
			for (final HDLStatement stmnt : loop.getDos()) {
				final Collection<HDLVariableRef> refs = HDLQuery.select(HDLVariableRef.class).from(stmnt).where(HDLResolvedRef.fVar).lastSegmentIs(param.getName()).getAll();
				if (refs.size() == 0) {
					newStmnts.add(stmnt);
				} else {
					BigInteger counter = r.get().lowerEndpoint();
					do {
						final ModificationSet stmntMs = new ModificationSet();
						for (final HDLVariableRef ref : refs) {
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

	public static void resetTempIDs() {
		tempID.set(0);
	}
}
