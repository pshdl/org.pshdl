/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model.simulation.codegenerator;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isArrayIndex;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isNegEdgeActive;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isNegEdgeHandled;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPosEdgeActive;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPosEdgeHandled;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPredFresh;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPredicate;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPrev;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isPublic;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isShadowReg;
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.isUpdate;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.Frame.FastInstruction;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.VariableInformation.Direction;
import org.pshdl.interpreter.VariableInformation.Type;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.utils.services.AuxiliaryContent;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public abstract class CommonCodeGenerator {

	protected static final VariableInformation EPS_CYCLE = createVar("epsCycle", 32, Type.UINT);
	protected static final VariableInformation DELTA_CYCLE = createVar("deltaCycle", 64, Type.UINT);
	protected static final VariableInformation TIMESTAMP = createVar("timeStamp", 64, Type.UINT);
	protected static final VariableInformation DISABLE_EDGES = createVar("disableEdges", -1, Type.BOOL);
	protected static final VariableInformation DISABLE_REG_OUTPUTLOGIC = createVar("disableRegOutputLogic", -1, Type.BOOL);

	protected static final EnumSet<Attributes> UPDATE = EnumSet.of(isUpdate);
	protected static final EnumSet<Attributes> PREDICATE = EnumSet.of(isPredicate);
	protected static final EnumSet<Attributes> SHADOWREG = EnumSet.of(isShadowReg);
	protected static final EnumSet<Attributes> NONE = EnumSet.noneOf(Attributes.class);
	protected final ExecutableModel em;
	protected int indent = 0;
	protected final Map<String, Integer> varIdx = Maps.newLinkedHashMap();
	protected final Set<String> prevMapPos = Sets.newLinkedHashSet();
	protected final Set<String> prevMapNeg = Sets.newLinkedHashSet();
	protected final boolean hasClock;
	protected final int bitWidth;
	protected final int maxCosts;
	protected final boolean purgeAliases;

	protected CommonCodeGenerator() {
		em = null;
		bitWidth = 64;
		maxCosts = Integer.MAX_VALUE;
		hasClock = false;
		purgeAliases = false;
	}

	protected CommonCodeGenerator(CommonCodeGeneratorParameter parameterObject) {
		this.em = parameterObject.em;
		this.bitWidth = parameterObject.bitWidth;
		this.maxCosts = parameterObject.maxCosts;
		this.purgeAliases = parameterObject.purgeAliases;
		for (int i = 0; i < parameterObject.em.variables.length; i++) {
			varIdx.put(parameterObject.em.variables[i].name, i);
		}
		for (final Frame f : parameterObject.em.frames) {
			if (f.edgeNegDepRes != -1) {
				prevMapNeg.add(asInternal(f.edgeNegDepRes).info.name);
			}
			if (f.edgePosDepRes != -1) {
				prevMapPos.add(asInternal(f.edgePosDepRes).info.name);
			}
		}
		this.hasClock = !prevMapPos.isEmpty() || !prevMapNeg.isEmpty();
	}

	protected InternalInformation asInternal(int id) {
		final InternalInformation internal = em.internals[id];
		if (purgeAliases && (internal.aliasID != -1))
			return em.internals[internal.aliasID];
		return internal;
	}

	public String generateMainCode() {
		final StringBuilder sb = new StringBuilder();
		sb.append(header());
		preBody();
		sb.append(preFieldDeclarations());
		sb.append(fieldDeclarations(true, true));
		sb.append(postFieldDeclarations());
		sb.append(preFrames());
		sb.append(frames());
		sb.append(postFrames());
		sb.append(preRunMethods());
		sb.append(createRunAndStageMethods());
		final CharSequence tbMethods = testbenchMethod();
		if (tbMethods != null) {
			sb.append(tbMethods);
		}
		sb.append(postRunMethods());
		postBody();
		sb.append(footer());
		return sb.toString();
	}

	protected CharSequence createRunAndStageMethods() {
		final Multimap<Integer, Frame> schedulingStage = ArrayListMultimap.create();
		int maxStage = 0;
		for (final Frame f : em.frames) {
			schedulingStage.put(f.scheduleStage, f);
			maxStage = Math.max(f.scheduleStage, maxStage);
		}
		final StringBuilder sb = new StringBuilder();
		sb.append(createStageMethods(schedulingStage, maxStage, true));
		sb.append(createStageMethods(schedulingStage, maxStage, false));
		return sb;
	}

	protected StringBuilder createStageMethods(final Multimap<Integer, Frame> schedulingStage, int maxStage, final boolean createConstant) {
		final List<Integer> selectedScheduleStage = Lists.newArrayList();
		final StringBuilder sb = new StringBuilder();
		final Set<Integer> handledPredicates = Sets.newLinkedHashSet();
		final Set<Integer> handledNegEdges = Sets.newLinkedHashSet();
		final Set<Integer> handledPosEdges = Sets.newLinkedHashSet();

		for (int i = 0; i <= maxStage; i++) {
			final Collection<Frame> frames = schedulingStage.get(i);
			for (final Frame frame : frames) {
				if (frame.constant == createConstant) {
					selectedScheduleStage.add(i);
					break;
				}
			}
		}

		sb.append(createStageMethods(schedulingStage, createConstant, selectedScheduleStage, handledPredicates, handledNegEdges, handledPosEdges));

		createRunMethod(schedulingStage, maxStage, createConstant, selectedScheduleStage, sb);
		return sb;
	}

	protected void createRunMethod(final Multimap<Integer, Frame> schedulingStage, int maxStage, final boolean createConstant, final List<Integer> selectedScheduleStage,
			final StringBuilder sb) {
		sb.append(indent()).append(runMethodsHeader(createConstant));
		indent++;
		sb.append(indent()).append(assignConstant(EPS_CYCLE, ZERO, NONE, true)).append(newLine());
		if (!createConstant) {
			sb.append(indent()).append(incVar(DELTA_CYCLE)).append(newLine());
		}
		if (!createConstant && hasClock) {
			sb.append(indent()).append(doLoopStart()).append(newLine());
			indent++;
			sb.append(indent()).append(clearRegUpdates()).append(newLine());
		}
		sb.append(indent()).append(assignVariable(TIMESTAMP, "(" + idName(DELTA_CYCLE, true, NONE) + " << 16) | " + idName(EPS_CYCLE, true, NONE), NONE, true, false, false))
				.append(newLine());
		for (final Integer stage : selectedScheduleStage) {
			sb.append(indent()).append(callStage(stage, createConstant));
		}
		if (!createConstant && hasClock) {
			sb.append(indent()).append(applyRegUpdates()).append(newLine());
			sb.append(indent()).append(incVar(EPS_CYCLE)).append(newLine());
			final StringBuilder condition = new StringBuilder();
			condition.append(checkRegupdates());
			condition.append(" && !").append(idName(DISABLE_REG_OUTPUTLOGIC, true, NONE));
			indent--;
			sb.append(indent()).append(doLoopEnd(condition)).append(newLine());
			for (final String prev : prevMapNeg) {
				final VariableInformation var = em.variables[varIdx.get(prev)];
				copyPrev(sb, var);
			}
			for (final String prev : prevMapPos) {
				if (prevMapNeg.contains(prev)) {
					continue;
				}
				final VariableInformation var = em.variables[varIdx.get(prev)];
				copyPrev(sb, var);
			}
		}
		indent--;
		sb.append(indent()).append(runMethodsFooter(createConstant));
	}

	protected void copyPrev(final StringBuilder sb, final VariableInformation var) {
		if (isArray(var)) {
			sb.append(indent()).append(copyArray(var));
		} else {
			sb.append(indent()).append(assignVariable(var, idName(var, true, NONE), EnumSet.of(isPrev), true, false, false)).append(newLine());
		}
	}

	protected abstract CharSequence copyArray(VariableInformation var);

	protected abstract CharSequence clearRegUpdates();

	protected CharSequence createStageMethods(final Multimap<Integer, Frame> schedulingStage, final boolean createConstant, final List<Integer> selectedScheduleStage,
			final Set<Integer> handledPredicates, Set<Integer> handledNegEdges, Set<Integer> handledPosEdges) {
		final StringBuilder sb = new StringBuilder();
		for (final int stage : selectedScheduleStage) {
			final Collection<Frame> stageFrames = schedulingStage.get(stage);
			final List<Frame> matchingFrames = Lists.newArrayList();
			int totalStageCosts = 0;
			for (final Frame frame : stageFrames) {
				if (frame.constant != createConstant) {
					continue;
				}
				matchingFrames.add(frame);
				totalStageCosts += estimateFrameCosts(frame);
			}
			sb.append(createStageMethod(createConstant, handledPredicates, stage, matchingFrames, totalStageCosts, handledNegEdges, handledPosEdges));
		}
		return sb;
	}

	protected CharSequence createStageMethod(final boolean createConstant, final Set<Integer> handledPredicates, final int stage, final List<Frame> matchingFrames,
			int totalStageCosts, Set<Integer> handledNegEdges, Set<Integer> handledPosEdges) {
		final StringBuilder sb = new StringBuilder();
		sb.append(indent()).append(stageMethodsHeader(stage, totalStageCosts, createConstant));
		indent++;
		for (final Frame frame : matchingFrames) {
			sb.append(handleEdge(handledNegEdges, false, frame.edgeNegDepRes));
			sb.append(handleEdge(handledPosEdges, true, frame.edgePosDepRes));
			sb.append(handlePredicates(handledPredicates, false, frame.predNegDepRes));
			sb.append(handlePredicates(handledPredicates, true, frame.predPosDepRes));
		}
		int stageCosts = 0;
		if ((maxCosts != Integer.MAX_VALUE) && (totalStageCosts > maxCosts)) {
			sb.append(indent()).append(barrierBegin(stage, totalStageCosts, createConstant));
		}
		for (final Iterator<Frame> iterator = matchingFrames.iterator(); iterator.hasNext();) {
			final Frame frame = iterator.next();
			if (purgeAliases && frame.isRename(em)) {
				continue;
			}
			stageCosts += estimateFrameCosts(frame);
			sb.append(predicateCheckedFrameCall(frame));
			if ((stageCosts > maxCosts) && iterator.hasNext()) {
				sb.append(indent()).append(barrier());
				stageCosts = 0;
			}
		}
		if ((maxCosts != Integer.MAX_VALUE) && (totalStageCosts > maxCosts)) {
			sb.append(indent()).append(barrierEnd(stage, totalStageCosts, createConstant));
		}
		indent--;
		sb.append(indent()).append(stageMethodsFooter(stage, totalStageCosts, createConstant));
		return sb;
	}

	protected abstract CharSequence applyRegUpdates();

	protected abstract CharSequence checkRegupdates();

	protected StringBuilder incVar(VariableInformation incVar) {
		return assignVariable(incVar, idName(incVar, true, NONE) + " + 1", NONE, true, false, false);
	}

	protected CharSequence doLoopStart() {
		indent++;
		return "do {" + newLine();
	}

	protected CharSequence doLoopEnd(CharSequence condition) {
		indent--;
		return "} while (" + condition + ")" + getStatementSeparator() + newLine();
	}

	protected CharSequence predicateCheckedFrameCall(Frame frame) {
		if ((frame.edgeNegDepRes == -1) && (frame.edgePosDepRes == -1) && !hasPredicate(frame.predPosDepRes) && !hasPredicate(frame.predNegDepRes)) {
			final StringBuilder sb = new StringBuilder();
			sb.append(indent()).append(callFrame(frame));
			return sb;
		}
		final List<CharSequence> predicates = Lists.newArrayList();
		final List<Integer> arr = Collections.emptyList();
		if (frame.edgeNegDepRes != -1) {
			predicates.add(internalWithArrayAccess(asInternal(frame.edgeNegDepRes), arr, EnumSet.of(isNegEdgeActive)));
			predicates.add("!" + internalWithArrayAccess(asInternal(frame.edgeNegDepRes), arr, EnumSet.of(isNegEdgeHandled)));
		}
		if (frame.edgePosDepRes != -1) {
			predicates.add(internalWithArrayAccess(asInternal(frame.edgePosDepRes), arr, EnumSet.of(isPosEdgeActive)));
			predicates.add("!" + internalWithArrayAccess(asInternal(frame.edgePosDepRes), arr, EnumSet.of(isPosEdgeHandled)));
		}
		if (frame.predNegDepRes != null) {
			for (final int pred : frame.predNegDepRes) {
				predicates.add("!" + internalWithArrayAccess(asInternal(pred), arr, PREDICATE));
				predicates.add(internalWithArrayAccess(asInternal(pred), arr, EnumSet.of(isPredFresh)));
			}
		}
		if (frame.predPosDepRes != null) {
			for (final int pred : frame.predPosDepRes) {
				predicates.add(internalWithArrayAccess(asInternal(pred), arr, PREDICATE));
				predicates.add(internalWithArrayAccess(asInternal(pred), arr, EnumSet.of(isPredFresh)));
			}
		}
		return callFrameWithPredicates(frame, predicates);
	}

	protected boolean hasPredicate(int[] pred) {
		return (pred != null) && (pred.length != 0);
	}

	protected CharSequence callFrameWithPredicates(Frame frame, final List<CharSequence> predicates) {
		final StringBuilder sb = new StringBuilder();
		final String join = Joiner.on(" &&" + newLine() + indent() + "   ").join(predicates);
		sb.append(indent()).append("if (" + join + ") {" + newLine());
		indent++;
		sb.append(indent()).append(callFrame(frame));
		indent--;
		sb.append(indent()).append('}').append(newLine());
		return sb;
	}

	protected CharSequence handlePredicates(Set<Integer> handledPredicates, boolean positive, int[] predicates) {
		final StringBuilder sb = new StringBuilder();
		if (predicates != null) {
			for (final int pred : predicates) {
				if ((pred != -1) && !handledPredicates.contains(pred)) {
					sb.append(indent()).append(updatePredicateFreshness(pred, positive)).append(newLine());
					handledPredicates.add(pred);
				}
			}
		}
		return sb;
	}

	protected CharSequence updatePredicateFreshness(int pred, boolean positive) {
		final StringBuilder sb = new StringBuilder();
		final List<Integer> arr = Collections.emptyList();
		final InternalInformation internal = asInternal(pred);
		final CharSequence updateInternal = internalWithArrayAccess(internal, arr, UPDATE);
		final CharSequence isFresh = condition(Condition.isEqual, updateInternal, idName(TIMESTAMP, true, NONE));
		sb.append(assignInternal(asInternal(pred), isFresh, arr, EnumSet.of(Attributes.isPredFresh)));
		return sb;
	}

	protected CharSequence handleEdge(final Set<Integer> handledEdges, final boolean posEdge, final int edgeDepRes) {
		final StringBuilder sb = new StringBuilder();
		if ((edgeDepRes != -1) && !handledEdges.contains(edgeDepRes)) {
			sb.append(indent()).append(updateEdge(edgeDepRes, posEdge)).append(newLine());
			sb.append(indent()).append(updateHandledClk(edgeDepRes, posEdge)).append(newLine());
			handledEdges.add(edgeDepRes);
		}
		return sb;
	}

	protected CharSequence updateHandledClk(int edgeDepRes, boolean posEdge) {
		final List<Integer> arr = Collections.emptyList();
		final InternalInformation internal = asInternal(edgeDepRes);
		final CharSequence updateInternal = internalWithArrayAccess(internal, arr, UPDATE);
		final StringBuilder sb = new StringBuilder();
		sb.append(assignInternal(internal, skipEdge(updateInternal), arr, EnumSet.of(posEdge ? isPosEdgeHandled : isNegEdgeHandled)));
		return sb;
	}

	protected CharSequence skipEdge(CharSequence updateInternal) {
		return callMethod("skipEdge", updateInternal);
	}

	protected CharSequence getOutputCases(CharSequence cast, EnumSet<Attributes> attributes) {
		indent++;
		final StringBuilder result = new StringBuilder();
		final VariableInformation[] variables = em.variables;
		for (int i = 0; i < variables.length; i++) {
			final VariableInformation v = variables[i];
			InternalInformation ii = v.asInternal();
			if (purgeAliases && (v.aliasVar != null)) {
				ii = v.aliasVar;
			}
			final StringBuilder value = new StringBuilder();
			value.append(indent());
			final CharSequence internal = loadInternal(ii, dimList(ii), attributes);
			if (ii.isPred) {
				value.append(ifCondition(internal, returnValue("1"), returnValue("0")));
			} else {
				value.append(returnValue(internal));
			}
			result.append(makeCase(constant(i, true), value, false));
		}
		indent--;
		return result;
	}

	protected CharSequence returnValue(CharSequence value) {
		return "return " + value + getStatementSeparator();
	}

	protected CharSequence setInputCases(CharSequence valueName, CharSequence cast, EnumSet<Attributes> attributes) {
		indent++;
		final StringBuilder result = new StringBuilder();
		final VariableInformation[] variables = em.variables;
		for (int i = 0; i < variables.length; i++) {
			final VariableInformation v = variables[i];
			InternalInformation ii = v.asInternal();
			if (purgeAliases && (v.aliasVar != null)) {
				ii = v.aliasVar;
			}
			final StringBuilder value = new StringBuilder();
			value.append(indent());
			final CharSequence zeroOnePredicate = condition(Condition.isNotEqual, valueName, constant(ZERO, true, -1));
			CharSequence assignValue = ii.isPred ? zeroOnePredicate : valueName;
			if ((cast != null) && !ii.isPred) {
				assignValue = doCast(cast, assignValue);
			}
			value.append(writeInternal(ii, assignValue.toString(), dimList(ii), attributes));
			result.append(makeCase(constant(i, true), value, true));
		}
		indent--;
		return result;
	}

	private List<Integer> dimList(InternalInformation ii) {
		final ArrayList<Integer> res = new ArrayList<Integer>();
		for (int i = 0; i < ii.arrayIdx.length; i++) {
			if (ii.arrayIdx[i] < 0) {
				res.add(-(i + 1));
			}
		}
		return res;
	}

	protected CharSequence doCast(CharSequence cast, CharSequence assignValue) {
		return cast + "(" + assignValue + ")";
	}

	protected CharSequence updateRegCases() {
		indent++;
		final StringBuilder result = new StringBuilder();
		for (final VariableInformation vi : em.variables) {
			if (vi.isRegister) {
				final StringBuilder value = new StringBuilder();
				value.append(indent());
				if (isArray(vi)) {
					final CharSequence condition = condition(Condition.isNotEqual, regOffset(), constant(ONE.negate(), false, -1));
					final CharSequence assignArrayElement = assignArrayElement(vi, getArrayElement(vi, regOffset(), true, SHADOWREG), regOffset(), true, NONE, true);
					value.append(ifCondition(condition, assignArrayElement, fillArray(vi, regFillValue())));
				} else {
					value.append(assignVariable(vi, idName(vi, true, SHADOWREG), NONE, true, false, false));
				}
				result.append(makeCase(constant(varIdx.get(vi.name), true), value, true));
			}
		}
		indent--;
		return result;
	}

	protected CharSequence makeCase(CharSequence caseLabel, CharSequence value, boolean includeBreak) {
		final StringBuilder result = new StringBuilder();
		result.append("case ").append(caseLabel).append(": {").append(newLine());
		result.append(value).append(newLine());
		if (includeBreak) {
			result.append(indent()).append("break").append(getStatementSeparator()).append(newLine());
		}
		result.append(indent()).append('}').append(newLine());
		return result;
	}

	protected abstract CharSequence fillArray(VariableInformation vi, CharSequence regFillValue);

	protected CharSequence regFillValue() {
		return "reg.fillValue";
	}

	protected CharSequence assignArrayElement(VariableInformation vi, CharSequence value, CharSequence offset, boolean field, EnumSet<Attributes> attributes, boolean doMask) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getArrayElement(vi, offset, field, attributes));
		sb.append(doAssign(value, getTargetSizeWithType(vi), doMask));
		return sb;
	}

	protected CharSequence getArrayElement(VariableInformation vi, CharSequence offset, boolean field, EnumSet<Attributes> attributes) {
		return idName(vi, field, attributes) + "[" + offset + "]";
	}

	protected CharSequence regOffset() {
		return "reg.offset";
	}

	protected CharSequence updateEdge(int edgeDepRes, boolean posEdge) {
		final List<Integer> arr = Collections.emptyList();
		final InternalInformation internal = asInternal(edgeDepRes);
		final CharSequence prevInternal = internalWithArrayAccess(internal, arr, EnumSet.of(isPrev));
		final CharSequence currInternal = internalWithArrayAccess(internal, arr, NONE);
		final StringBuilder value = new StringBuilder();
		if (posEdge) {
			value.append("((").append(prevInternal).append(" == 0) || ").append(idName(DISABLE_EDGES, true, NONE)).append(") && (").append(currInternal).append(" == 1)");
		} else {
			value.append("((").append(prevInternal).append(" == 1) || ").append(idName(DISABLE_EDGES, true, NONE)).append(") && (").append(currInternal).append(" == 0)");
		}
		return assignInternal(internal, value, arr, EnumSet.of(posEdge ? isPosEdgeActive : isNegEdgeActive));
	}

	protected CharSequence barrierBegin(int stage, int totalStageCosts, boolean createConstant) {
		return "";
	}

	protected CharSequence barrierEnd(int stage, int totalStageCosts, boolean createConstant) {
		return "";
	}

	protected CharSequence barrier() {
		return comment("barrier");
	}

	protected int estimateFrameCosts(Frame frame) {
		int res = 0;
		for (final FastInstruction fi : frame.instructions) {
			switch (fi.inst) {
			case and:
			case or:
			case xor:
			case bit_neg:
				res += 1;
				break;
			case bitAccessSingle:
			case bitAccessSingleRange:
				res += 3; // shift and mask
				break;
			case cast_int:
				res += 4; // two shifts
				break;
			case cast_uint:
				res += 1; // mask
				break;
			case concat:
				res += 3; // shift and OR
				break;
			case const0:
			case const1:
			case const2:
			case constAll1:
				res += 1;
				break;
			case mod:
			case div:
				res += 4;
				break;
			case eq:
			case greater:
			case greater_eq:
			case less_eq:
			case less:
			case logiAnd:
			case logiNeg:
			case logiOr:
			case not_eq:
				res += 2;// Arithmetic op
				break;
			case isFallingEdge:
			case isRisingEdge:
			case posPredicate:
			case negPredicate:
				res += 1; // boolean comp
				break;
			case loadConstant:
				res += 1;
				break;
			case loadInternal:
				res += 4; // Potential sign extension
				break;
			case minus:
			case plus:
			case mul:
			case arith_neg:
				res += 3;
				break;
			case noop:
			case pushAddIndex:
			case writeInternal:
				break;
			case pow: // pow is realized as shift
			case sll:
			case sra:
			case srl:
				res += 2;
				break;
			}
		}
		return res;
	}

	protected abstract CharSequence callStage(int stage, boolean constant);

	protected CharSequence callFrame(Frame f) {
		return callMethod(getFrameName(f)) + ";\n";
	}

	protected abstract CharSequence stageMethodsHeader(int stage, int totalStageCosts, boolean constant);

	protected abstract CharSequence stageMethodsFooter(int stage, int totalStageCosts, boolean constant);

	protected abstract CharSequence runMethodsHeader(boolean constant);

	protected abstract CharSequence runMethodsFooter(boolean constant);

	protected CharSequence postRunMethods() {
		return "";
	}

	protected CharSequence preRunMethods() {
		return "";
	}

	protected CharSequence frames() {
		final StringBuilder sb = new StringBuilder();
		for (final Frame frame : em.frames) {
			if (purgeAliases && frame.isRename(em)) {
				continue;
			}
			sb.append(indent()).append(functionHeader(frame));
			sb.append(preFrameExecution(frame));
			sb.append(frameExecution(frame));
			sb.append(postFrameExecution(frame));
			sb.append(indent()).append(functionFooter(frame));
		}
		return sb;
	}

	protected CharSequence frameExecution(Frame frame) {
		final StringBuilder sb = new StringBuilder();
		int pos = 0;
		int arrPos = 0;
		final Stack<Integer> stack = new Stack<>();
		final List<Integer> arr = Lists.newArrayList();
		for (final FastInstruction instruction : frame.instructions) {
			int a = 0;
			int b = 0;
			if (instruction.inst.pop > 0) {
				a = stack.pop();
			}
			if (instruction.inst.pop > 1) {
				b = stack.pop();
			}
			if (instruction.inst.push > 0) {
				stack.push(pos);
			}
			if (instruction.inst == Instruction.pushAddIndex) {
				arr.add(arrPos++);
			}
			sb.append(indent()).append(toExpression(instruction, frame, pos, a, b, arr, arrPos));
			if (instruction.inst != Instruction.pushAddIndex) {
				pos++;
			}
		}
		if (frame.outputIds.length > 0) {
			final String last = getTempName(stack.pop(), NONE);
			for (final int outputId : frame.outputIds) {
				final InternalInformation outputInternal = asInternal(outputId);
				sb.append(indent());
				sb.append(writeInternal(outputInternal, last, arr, NONE)).append(newLine());
				sb.append(updatePrediateTimestamp(arr, outputInternal));
			}
		}
		return sb;
	}

	protected CharSequence updatePrediateTimestamp(final List<Integer> arr, final InternalInformation outputInternal) {
		final StringBuilder sb = new StringBuilder();
		if (outputInternal.isPred) {
			sb.append(indent()).append(assignInternal(outputInternal, idName(TIMESTAMP, true, NONE), arr, UPDATE)).append(comment("update timestamp"));
		}
		return sb;
	}

	protected CharSequence calcRegUpdateOffset(final List<Integer> arr, final InternalInformation outputInternal) {
		CharSequence offset = "0";
		final VariableInformation varInfo = outputInternal.info;
		if (isArray(varInfo)) {
			if (outputInternal.fixedArray && arr.isEmpty()) {
				if (outputInternal.arrayIdx.length != varInfo.dimensions.length) {
					offset = "-1";
				} else {
					offset = Integer.toString(calculateFixedAccesIndex(outputInternal));
				}
			} else {
				offset = calculateVariableAccessIndex(arr, varInfo, NONE);
			}
		}
		return offset;
	}

	protected abstract CharSequence scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset, boolean force,
			CharSequence fillValue);

	protected CharSequence assignInternal(InternalInformation output, CharSequence value, List<Integer> arr, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(internalWithArrayAccess(output, arr, attributes));
		int targetSizeWithType = output.actualWidth << 1;
		if (output.info.type == Type.INT) {
			targetSizeWithType++;
		}
		sb.append(doAssign(value, targetSizeWithType, false));
		return sb;
	}

	protected StringBuilder assignVariable(VariableInformation var, CharSequence assignValue, EnumSet<Attributes> attributes, boolean field, boolean declare, boolean doMask) {
		final StringBuilder sb = new StringBuilder();
		if (declare) {
			sb.append(inlineVarDecl(var, field, attributes));
		} else {
			sb.append(idName(var, field, attributes));
		}
		sb.append(doAssign(assignValue, getTargetSizeWithType(var), doMask));
		return sb;
	}

	protected int getTargetSizeWithType(VariableInformation var) {
		int targetSizeWithType = var.width << 1;
		if (var.type == Type.INT) {
			targetSizeWithType++;
		}
		return targetSizeWithType;
	}

	protected int getTargetSizeWithType(InternalInformation internal) {
		int targetSizeWithType = internal.actualWidth << 1;
		if (internal.info.type == Type.INT) {
			targetSizeWithType++;
		}
		return targetSizeWithType;
	}

	protected StringBuilder assignTempVar(int targetSizeWithType, int pos, EnumSet<Attributes> attributes, CharSequence assignValue, boolean doMask) {
		final StringBuilder sb = new StringBuilder();
		sb.append(tempVar(pos, targetSizeWithType, attributes));
		sb.append(doAssign(assignValue, targetSizeWithType, doMask));
		return sb;
	}

	protected CharSequence doAssign(CharSequence assignValue, int targetSizeWithType, boolean doMask) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" = ");
		sb.append(fixupValue(assignValue, targetSizeWithType, doMask));
		sb.append(getStatementSeparator());
		return sb;
	}

	protected CharSequence fixupValue(CharSequence assignValue, int targetSizeWithType, boolean doMask) {
		final StringBuilder sb = new StringBuilder();
		if (isSignedType(targetSizeWithType)) {
			sb.append(signExtend(assignValue, targetSizeWithType));
		} else {
			if (doMask) {
				sb.append(mask(assignValue, targetSizeWithType >> 1));
			} else {
				sb.append(assignValue);
			}
		}
		return sb;
	}

	protected CharSequence comment(String string) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("//");
		stringBuilder.append(string);
		stringBuilder.append("\n");
		return stringBuilder;
	}

	protected CharSequence toExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case cast_int:
		case cast_uint:
		case bitAccessSingle:
		case bitAccessSingleRange:
			sb.append(toCastExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case isFallingEdge:
		case isRisingEdge:
			sb.append(toEdgeExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case concat:
			sb.append(toConcatExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case const0:
		case const1:
		case const2:
		case constAll1:
		case loadConstant:
		case loadInternal:
		case writeInternal:
		case pushAddIndex:
			sb.append(toLoadStoreExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case bit_neg:
		case and:
		case or:
		case xor:
			sb.append(toBitExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case plus:
		case minus:
		case mul:
		case div:
		case mod:
		case pow:
		case arith_neg:
			sb.append(toArithExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case sll:
		case srl:
		case sra:
			sb.append(toShiftExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case eq:
		case not_eq:
		case less:
		case less_eq:
		case greater:
		case greater_eq:
			sb.append(toEqualityExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case logiAnd:
		case logiNeg:
		case logiOr:
		case negPredicate:
		case posPredicate:
			sb.append(toPredicateExpression(exec, frame, pos, a, b, arr, arrPos));
			break;
		case noop:
			break;
		}
		sb.append(comment(exec.toString(em)));
		return sb;
	}

	protected CharSequence toConcatExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case concat:
			sb.append(assignTempVar((exec.arg1 + exec.arg2) << 1, pos, NONE, "((" + getTempName(b, NONE) + " << " + exec.arg2 + ") | " + getTempName(a, NONE) + ")", true));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toEdgeExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case isFallingEdge:
		case isRisingEdge:
			sb.append(assignInternal(asInternal(exec.arg1), idName(TIMESTAMP, true, NONE), arr, EnumSet.of(Attributes.isUpdate)));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toCastExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		final String tempName = getTempName(a, NONE);
		switch (exec.inst) {
		case bitAccessSingle:
			sb.append(assignTempVar(2, pos, NONE, mask(tempName + " >> " + exec.arg1, 1), false));
			break;
		case bitAccessSingleRange:
			final int highBit = exec.arg1;
			final int lowBit = exec.arg2;
			final int targetSize = (highBit - lowBit) + 1;
			sb.append(assignTempVar(targetSize << 1, pos, NONE, mask(tempName + " >> " + lowBit, targetSize), false));
			break;
		case cast_int:
			// Create a targetSizeWitType with int indication
			sb.append(assignTempVar((Math.min(exec.arg1, exec.arg2) << 1) | 1, pos, NONE, tempName, false));
			break;
		case cast_uint:
			sb.append(assignTempVar(Math.min(exec.arg1, exec.arg2) << 1, pos, NONE, mask(tempName, Math.min(exec.arg1, exec.arg2)), false));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toLoadStoreExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case const0:
			sb.append(assignTempVar(-1, pos, NONE, constant(ZERO, true, -1), false));
			break;
		case const1:
			sb.append(assignTempVar(-1, pos, NONE, constant(ONE, true, -1), false));
			break;
		case const2:
			sb.append(assignTempVar(-1, pos, NONE, constant(BigInteger.valueOf(2), true, -1), false));
			break;
		case constAll1:
			sb.append(assignTempVar(-1, pos, NONE, constant(calcMask(exec.arg1), true, -1), false));
			break;
		case loadConstant:
			sb.append(assignTempVar(-1, pos, NONE, constant(frame.constants[exec.arg1], true, -1), true));
			break;
		case loadInternal:
			sb.append(assignTempVar(-1, pos, NONE, loadInternal(asInternal(exec.arg1), arr, NONE), false));
			break;
		case writeInternal:
			sb.append(writeInternal(asInternal(exec.arg1), getTempName(a, NONE), arr, NONE));
			break;
		case pushAddIndex:
			sb.append(assignIndexVar(arr, a));
			// sb.append('''int a«arr.last»=(int)t«a»;''')
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence assignIndexVar(List<Integer> arr, int a) {
		final StringBuilder sb = new StringBuilder();
		sb.append(assignTempVar(-1, arr.get(arr.size() - 1).intValue(), EnumSet.of(isArrayIndex), getTempName(a, NONE), true));
		return sb;
	}

	public Iterable<AuxiliaryContent> getAuxiliaryContent() {
		return Collections.emptyList();
	}

	public void writeAuxiliaryContents(File outputDir) throws IOException {
		for (final AuxiliaryContent aux : getAuxiliaryContent()) {
			final File auxFile = new File(outputDir, aux.relPath);
			Files.createParentDirs(auxFile);
			Files.write(aux.contents, auxFile);
		}
	}

	protected CharSequence writeInternal(InternalInformation internal, String tempName, List<Integer> arr, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		CharSequence assignValue = createBitAccessIfNeeded(internal, tempName, arr, sb);
		if (!internal.isPred) {
			assignValue = fixupValue(assignValue, getTargetSizeWithType(internal.info), true);
		}
		if (internal.isShadowReg) {
			final String cpyName = idName(internal.fullName, false, append(NONE, attributes)) + tempName + "_cpy";
			final boolean forceRegUpdate = internal.fixedArray && internal.isFillArray;
			if (!forceRegUpdate) {
				final VariableInformation cpyVar = createVar(cpyName, internal.actualWidth, internal.info.type);
				sb.append(assignVariable(cpyVar, internalWithArrayAccess(internal, arr, append(NONE, attributes)), append(NONE, attributes), false, true, false)).append(newLine());
				sb.append(indent());
			}
			sb.append(assignInternal(internal, assignValue, arr, append(SHADOWREG, attributes))).append(comment("Assign value"));
			final CharSequence offset = calcRegUpdateOffset(arr, internal);
			sb.append(indent()).append(
					scheduleShadowReg(internal, internalWithArrayAccess(internal, arr, append(SHADOWREG, attributes)), cpyName, offset, forceRegUpdate, tempName));
		} else {
			sb.append(assignInternal(internal, assignValue, arr, append(NONE, attributes))).append(comment("Assign value"));
		}
		arr.clear();
		return sb;
	}

	private EnumSet<Attributes> append(EnumSet<Attributes> input, EnumSet<Attributes> attributes) {
		if (attributes == null)
			return input;
		final EnumSet<Attributes> clone = input.clone();
		clone.addAll(attributes);
		return clone;
	}

	private EnumSet<Attributes> append(EnumSet<Attributes> input, Attributes[] attributes) {
		if (attributes == null)
			return input;
		final EnumSet<Attributes> clone = input.clone();
		clone.addAll(Arrays.asList(attributes));
		return clone;
	}

	protected String createBitAccessIfNeeded(InternalInformation internal, String tempName, List<Integer> arr, final StringBuilder sb) {
		if (internal.actualWidth != internal.info.width) {
			final VariableInformation currVar = createVar(idName(internal.fullName, false, NONE) + tempName + "_current", internal.actualWidth, internal.info.type);
			final CharSequence currentValue = internalWithArrayAccess(internal, arr, internal.isShadowReg ? SHADOWREG : NONE);
			final BigInteger mask = calcMask(internal.actualWidth);
			final CharSequence writeMask = constant(mask.shiftLeft(internal.bitEnd).not(), true, internal.info.width);
			sb.append(assignVariable(currVar, doMask(currentValue, writeMask), NONE, false, true, false)).append(comment("Current value"));
			final VariableInformation maskVar = createVar(idName(internal.fullName, false, NONE) + tempName + "_mask_shift", internal.actualWidth, internal.info.type);
			sb.append(indent()).append(assignVariable(maskVar, "(" + mask(tempName, internal.actualWidth) + ") << " + internal.bitEnd, NONE, false, true, false))
					.append(comment("Masked and shifted"));
			tempName = "(" + currVar.name + " | " + maskVar.name + ")";
			sb.append(indent());
		}
		return tempName;
	}

	protected CharSequence doMask(final CharSequence currentValue, final CharSequence writeMask) {
		return "(" + currentValue + ") & " + writeMask;
	}

	protected CharSequence loadInternal(InternalInformation info, List<Integer> arr, EnumSet<Attributes> attributes) {
		final CharSequence internalWithArray = internalWithArrayAccess(info, arr, attributes);
		if (info.actualWidth == info.info.width)
			return internalWithArray;
		return bitAccess(internalWithArray, info);
	}

	protected CharSequence bitAccess(CharSequence internalWithArray, InternalInformation info) {
		if (info.actualWidth == 1)
			return mask(internalWithArray + " >> " + info.bitStart, 1);
		return mask(internalWithArray + " >> " + info.bitEnd, info.actualWidth);
	}

	protected CharSequence internalWithArrayAccess(InternalInformation internal, List<Integer> arr, EnumSet<Attributes> attributes) {
		final CharSequence varName = idName(internal, true, attributes);
		final VariableInformation varInfo = internal.info;
		final StringBuilder sb = new StringBuilder();
		if (isArray(varInfo)) {
			if (internal.fixedArray && arr.isEmpty()) {
				sb.append(fixedArrayAccess(varName, calculateFixedAccesIndex(internal)));
			} else {
				sb.append(arrayAccess(varName, calculateVariableAccessIndex(arr, varInfo, attributes)));
			}
		} else {
			sb.append(varName);
		}
		return sb;
	}

	protected int calculateFixedAccesIndex(InternalInformation internal) {
		int idx = 0;
		final int lastIndex = internal.arrayIdx.length - 1;
		for (int i = 0; i < lastIndex; i++) {
			idx += internal.info.dimensions[i] * internal.arrayIdx[i];
		}
		if (lastIndex >= 0) {
			idx += internal.arrayIdx[lastIndex];
		}
		return idx;
	}

	protected CharSequence calculateVariableAccessIndex(List<Integer> arr, final VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (attributes.contains(Attributes.useArrayOffset))
			return "offset";
		final int lastIndex = arr.size() - 1;
		final StringBuilder arrayAccess = new StringBuilder();
		for (int i = 0; i < lastIndex; i++) {
			if (i != 0) {
				arrayAccess.append(" + ");
			}
			arrayAccess.append(Integer.toString(varInfo.dimensions[i]) + " * " + getTempName(arr.get(i), append(EnumSet.of(isArrayIndex), attributes)));
		}
		if (lastIndex != 0) {
			arrayAccess.append(" + ");
		}
		arrayAccess.append(getTempName(arr.get(lastIndex), append(EnumSet.of(isArrayIndex), attributes)));
		return arrayAccess;
	}

	protected CharSequence fixedArrayAccess(CharSequence varName, int idx) {
		return arrayAccess(varName, Integer.toString(idx));
	}

	protected CharSequence arrayAccess(CharSequence varName, CharSequence idx) {
		return varName + "[" + idx + "]";
	}

	protected String getTempName(int pos, EnumSet<Attributes> attribute) {
		if (attribute.contains(Attributes.isPredicate))
			return "p" + pos;
		if (attribute.contains(Attributes.isArrayArg)) {
			if (pos >= 0)
				return Integer.toString(pos);
			return arrayAccess("arrayIdx", constant(Math.abs(pos) - 1, false)).toString();
		}
		if (attribute.contains(Attributes.isArrayIndex))
			return "a" + pos;
		return "t" + pos;
	}

	protected CharSequence toEqualityExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case eq:
			sb.append(twoOp(exec, "==", 2, pos, b, a, PREDICATE, false));
			break;
		case not_eq:
			sb.append(twoOp(exec, "!=", 2, pos, b, a, PREDICATE, false));
			break;
		case less:
			sb.append(twoOp(exec, "<", 2, pos, b, a, PREDICATE, false));
			break;
		case less_eq:
			sb.append(twoOp(exec, "<=", 2, pos, b, a, PREDICATE, false));
			break;
		case greater:
			sb.append(twoOp(exec, ">", 2, pos, b, a, PREDICATE, false));
			break;
		case greater_eq:
			sb.append(twoOp(exec, ">=", 2, pos, b, a, PREDICATE, false));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toPredicateExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case logiNeg:
			sb.append(singleOp(exec, "!", exec.arg1, pos, a, PREDICATE, false));
			break;
		case logiAnd:
			sb.append(twoOp(exec, "&&", exec.arg1, pos, b, a, PREDICATE, false));
			break;
		case logiOr:
			sb.append(twoOp(exec, "||", exec.arg1, pos, b, a, PREDICATE, false));
			break;
		case negPredicate:
		case posPredicate:
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toShiftExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case sll:
			sb.append(twoOp(exec, "<<", exec.arg1, pos, b, a, NONE, true));
			break;
		case srl:
			sb.append(twoOp(exec, ">>>", exec.arg1, pos, b, a, NONE, true));
			break;
		case sra:
			sb.append(twoOp(exec, ">>", exec.arg1, pos, b, a, NONE, true));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toArithExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case arith_neg:
			sb.append(singleOp(exec, "-", exec.arg1, pos, a, NONE, true));
			break;
		case plus:
			sb.append(twoOp(exec, "+", exec.arg1, pos, b, a, NONE, true));
			break;
		case minus:
			sb.append(twoOp(exec, "-", exec.arg1, pos, b, a, NONE, true));
			break;
		case mul:
			sb.append(twoOp(exec, "*", exec.arg1, pos, b, a, NONE, true));
			break;
		case div:
			sb.append(twoOp(exec, "/", exec.arg1, pos, b, a, NONE, true));
			break;
		case mod:
			sb.append(twoOp(exec, "%", exec.arg1, pos, b, a, NONE, true));
			break;
		case pow:
			sb.append(pow(exec, "%", exec.arg1, pos, b, a, NONE, true));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}
		return sb;
	}

	protected abstract CharSequence pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes,
			boolean doMask);

	protected CharSequence toBitExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case bit_neg:
			sb.append(singleOp(exec, "~", exec.arg1, pos, a, NONE, true));
			break;
		case and:
			sb.append(twoOp(exec, "&", exec.arg1, pos, b, a, NONE, false));
			break;
		case or:
			sb.append(twoOp(exec, "|", exec.arg1, pos, b, a, NONE, true));
			break;
		case xor:
			sb.append(twoOp(exec, "^", exec.arg1, pos, b, a, NONE, true));
			break;
		default:
			throw new IllegalArgumentException("Did not expect instruction:" + exec + " here");
		}

		return sb;
	}

	protected CharSequence singleOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int a, EnumSet<Attributes> attributes, boolean doMask) {
		final CharSequence assignValue = singleOpValue(op, getCast(targetSizeWithType), a, targetSizeWithType, attributes);
		return assignTempVar(targetSizeWithType, pos, attributes, assignValue, doMask);
	}

	protected CharSequence twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes, boolean doMask) {
		final CharSequence assignValue = twoOpValue(op, getCast(targetSizeWithType), leftOperand, rightOperand, targetSizeWithType, attributes);
		return assignTempVar(targetSizeWithType, pos, attributes, assignValue, doMask);
	}

	protected CharSequence tempVar(int pos, int targetSizeWithType, EnumSet<Attributes> attributes) {
		final int width = targetSizeWithType >> 1;
		Type type = Type.UINT;
		if (attributes.contains(isPredicate)) {
			type = Type.BOOL;
			attributes = filterPredicate(attributes);
		}
		if (isSignedType(targetSizeWithType)) {
			type = Type.INT;
		}
		final VariableInformation var = createVar(getTempName(pos, attributes), width, type);
		final StringBuilder sb = new StringBuilder();
		sb.append(inlineVarDecl(var, false, NONE));
		return sb;
	}

	protected CharSequence inlineVarDecl(final VariableInformation var, boolean field, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(fieldType(var, attributes)).append(" ");
		sb.append(idName(var, field, attributes));
		return sb;
	}

	protected EnumSet<Attributes> filterPredicate(EnumSet<Attributes> attributes) {
		attributes = attributes.clone();
		// We don't want to differentiate beween regular temp variables and
		// boolean variables
		attributes.remove(isPredicate);
		return attributes;
	}

	protected CharSequence getCast(int targetSizeWithType) {
		return "";
	}

	protected CharSequence singleOpValue(CharSequence op, CharSequence cast, int a, int targetSizeWithType, EnumSet<Attributes> attributes) {
		final String finalOp = op + " " + getTempName(a, NONE);
		return assignOpValue(targetSizeWithType, attributes, finalOp);
	}

	protected CharSequence twoOpValue(CharSequence op, CharSequence cast, int leftOperand, int rightOperand, int targetSizeWithType, EnumSet<Attributes> attributes) {
		final String finalOp = getTempName(leftOperand, NONE) + " " + op + " " + getTempName(rightOperand, NONE);
		return finalOp;
	}

	protected CharSequence assignOpValue(int targetSizeWithType, EnumSet<Attributes> attributes, final String finalOp) {
		final int targetSize = (targetSizeWithType >> 1);
		if (isSignedType(targetSizeWithType))
			return signExtend(finalOp, targetSizeWithType);
		if (attributes.contains(isPredicate))
			return finalOp;
		return mask(finalOp, targetSize);
	}

	protected boolean isSignedType(int targetSizeWithType) {
		if (targetSizeWithType < 0)
			return false;
		return (targetSizeWithType & 1) == 1;
	}

	protected CharSequence mask(CharSequence op, int targetSize) {
		if (targetSize == bitWidth)
			return op;
		final BigInteger mask = calcMask(targetSize);
		return doMask(op, constant(mask, true, -1));
	}

	protected CharSequence invertedMask(CharSequence op, int targetSize) {
		if (targetSize == bitWidth)
			return op;
		final BigInteger mask = calcMask(targetSize).not();
		return "(" + op + ") & " + constant(mask, true, -1);
	}

	protected BigInteger calcMask(int targetSize) {
		return (ONE.shiftLeft(targetSize)).subtract(ONE);
	}

	protected CharSequence signExtend(CharSequence op, int targetSizeWithType) {
		final int targetSize = targetSizeWithType >> 1;
		if (targetSize == 0)
			return op;
		final CharSequence cast = getCast(targetSizeWithType);
		final int shift = bitWidth - targetSize;
		final String shiftPart = " << " + shift + ") >> " + shift;
		if (shift == 0)
			return op;
		else if (op.toString().endsWith(shiftPart))
			return op;
		if ((cast != null) && (cast.length() != 0))
			return "(" + doCast(cast, op) + shiftPart;
		return "((" + op + ")" + shiftPart;
	}

	protected CharSequence preFrameExecution(Frame frame) {
		indent++;
		return "";
	}

	protected CharSequence postFrameExecution(Frame frame) {
		indent--;
		return "";
	}

	protected abstract CharSequence functionHeader(Frame frame);

	protected abstract CharSequence functionFooter(Frame frame);

	protected CharSequence postFrames() {
		return "";
	}

	protected CharSequence preFrames() {
		return "";
	}

	protected CharSequence postFieldDeclarations() {
		return "";
	}

	protected CharSequence preFieldDeclarations() {
		return "";
	}

	protected void postBody() {
	}

	protected void preBody() {
	}

	protected CharSequence fieldDeclarations(boolean includePrivate, boolean initialize) {
		final StringBuilder sb = new StringBuilder();
		preFieldDeclaration();
		for (final VariableInformation var : em.variables) {
			if (purgeAliases && (var.aliasVar != null)) {
				continue;
			}
			if (hasPrev(var) && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPrev), initialize)).append(newLine());
				if (prevMapPos.contains(var.name)) {
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPosEdgeActive), initialize)).append(newLine());
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPosEdgeHandled), initialize)).append(newLine());
				}
				if (prevMapNeg.contains(var.name)) {
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isNegEdgeActive), initialize)).append(newLine());
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isNegEdgeHandled), initialize)).append(newLine());
				}
			}
			if (hasUpdate(var)) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isUpdate, isPublic), initialize)).append(newLine());
			}
			if (isPredicate(var) && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPredFresh), initialize)).append(newLine());
			}
			if (var.isRegister && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, SHADOWREG, initialize)).append(newLine());
			}
			sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPublic), initialize)).append(newLine());
		}
		sb.append(indent()).append(createVarDeclaration(EPS_CYCLE, EnumSet.of(isPublic), initialize)).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DELTA_CYCLE, EnumSet.of(isPublic), initialize)).append(newLine());
		sb.append(indent()).append(createVarDeclaration(TIMESTAMP, EnumSet.of(isPublic), initialize)).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DISABLE_EDGES, EnumSet.of(isPublic), initialize)).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DISABLE_REG_OUTPUTLOGIC, EnumSet.of(isPublic), initialize)).append(newLine());
		postFieldDeclaration();
		return sb;
	}

	protected static VariableInformation createVar(String name, int width, Type type) {
		return new VariableInformation(Direction.INTERNAL, name, width, type, false, false, false, null, null);
	}

	protected CharSequence newLine() {
		return "\n";
	}

	protected void postFieldDeclaration() {
	}

	protected void preFieldDeclaration() {
	}

	protected CharSequence indent() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
		return sb;
	}

	public static enum Attributes {
		baseType, isPrev, isUpdate, isShadowReg, isPublic, isPredicate, isArrayIndex, isArrayArg, useArrayOffset, isPosEdgeActive, isPosEdgeHandled, isNegEdgeActive, isNegEdgeHandled, isPredFresh, isArray
	}

	protected CharSequence createVarDeclaration(VariableInformation var, EnumSet<Attributes> attributes, boolean initialize) {
		final StringBuilder sb = new StringBuilder();
		sb.append(preField(var, attributes));
		sb.append(fieldType(var, attributes)).append(" ");
		if (initialize) {
			if (isArray(var)) {
				sb.append(assignArrayInit(var, ZERO, attributes));
			} else {
				sb.append(assignConstant(var, ZERO, attributes, true));
			}
		} else {
			sb.append(justDeclare(var, attributes));
		}
		sb.append(postField(var));
		return sb;
	}

	protected CharSequence justDeclare(VariableInformation var, EnumSet<Attributes> attributes) {
		return fieldName(var, attributes) + getStatementSeparator() + newLine();
	}

	protected int maxRegUpdates() {
		int maxUpdates = 0;
		for (final Frame f : em.frames) {
			if (f.outputIds.length == 0) {
				for (final FastInstruction inst : f.instructions) {
					if (inst.inst == Instruction.writeInternal) {
						if (asInternal(inst.arg1).isShadowReg) {
							maxUpdates = maxUpdates + 1;
						}
					}
				}
			} else {
				for (final int oi : f.outputIds) {
					if (asInternal(oi).isShadowReg) {
						maxUpdates = maxUpdates + 1;
					}
				}
			}
		}
		return maxUpdates;
	}

	protected CharSequence assignArrayInit(VariableInformation var, BigInteger initValue, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(fieldName(var, attributes));
		sb.append(doAssign(arrayInit(var, initValue, attributes), -1, false));
		return sb;
	}

	protected abstract CharSequence arrayInit(VariableInformation var, BigInteger initValue, EnumSet<Attributes> attributes);

	protected boolean isBoolean(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		return ((varInfo.type == VariableInformation.Type.BOOL) || //
				isPredicate(varInfo) || //
				attributes.contains(isPosEdgeHandled) || //
				attributes.contains(isPosEdgeActive) || //
				attributes.contains(isNegEdgeHandled) || //
				attributes.contains(isNegEdgeActive) || //
				attributes.contains(isPredFresh) //
				)
				&& !attributes.contains(isUpdate);
	}

	protected int getArraySize(VariableInformation vi) {
		int size = 1;
		if ((vi.dimensions == null) || (vi.dimensions.length == 0))
			return size;
		for (final int dim : vi.dimensions) {
			size *= dim;
		}
		return size;
	}

	protected CharSequence stageMethodName(int stage, boolean constant) {
		if (constant)
			return "const_stage" + String.format("%04d", stage);
		return "stage" + String.format("%04d", stage);
	}

	protected boolean isArray(VariableInformation var) {
		return (var.dimensions != null) && (var.dimensions.length != 0);
	}

	protected CharSequence assignConstant(VariableInformation var, BigInteger constantValue, EnumSet<Attributes> attributes, boolean forceUnsigned) {
		final StringBuilder sb = new StringBuilder();
		sb.append(fieldName(var, attributes));
		CharSequence assignValue;
		if (isBoolean(var, attributes)) {
			assignValue = constantBoolean(constantValue);
		} else {
			assignValue = constant(constantValue, forceUnsigned, -1);
		}
		sb.append(doAssign(assignValue, -1, false));
		return sb;
	}

	protected CharSequence constantBoolean(BigInteger constantValue) {
		if (constantValue.equals(ZERO))
			return "false";
		return "true";
	}

	protected CharSequence constant(long constantValue, boolean forceUnsigned) {
		return constant(BigInteger.valueOf(constantValue), forceUnsigned, -1);
	}

	protected CharSequence constant(BigInteger constantValue, boolean forceUnsigned) {
		return constant(constantValue, forceUnsigned, -1);
	}

	protected CharSequence constant(BigInteger constantValue, boolean forceUnsigned, int maxLen) {
		if (maxLen > 0)
			return constantVarLength(constantValue, maxLen, forceUnsigned);
		if ((constantValue.signum() >= 0) && (constantValue.compareTo(BigInteger.TEN) < 0))
			return constantValue.toString(10);
		if ((constantValue.signum() < 0) && !forceUnsigned)
			return constantValue.toString(10);
		if (constantValue.bitLength() <= 32)
			return constant32Bit(constantValue.intValue());
		if (constantValue.bitLength() <= 64)
			return constant64Bit(constantValue.longValue());
		return constantVarLength(constantValue, maxLen, forceUnsigned);
	}

	protected String constantSuffix() {
		return "L";
	}

	protected CharSequence constantVarLength(BigInteger constantValue, int maxLen, boolean forceUnsigned) {
		constantValue = force(constantValue, maxLen);
		int width = (maxLen + 3) / 4;
		if (width == 0) {
			width = 1;
		}
		return String.format("%#0" + width + "X%s", constantValue, constantSuffix());
	}

	protected BigInteger force(BigInteger constantValue, int maxLen) {
		if (constantValue.signum() < 0) {
			final BigInteger mask = ONE.shiftLeft(maxLen).subtract(ONE);
			return constantValue.and(mask);
		}
		return constantValue;
	}

	protected CharSequence constant32Bit(int intValue) {
		return String.format("%#08X%s", intValue, constantSuffix());
	}

	protected CharSequence constant64Bit(long longValue) {
		return String.format("%#016X%s", longValue, constantSuffix());
	}

	protected CharSequence fieldName(VariableInformation var, EnumSet<Attributes> attributes) {
		return idName(var, true, attributes);
	}

	protected abstract CharSequence fieldType(VariableInformation var, EnumSet<Attributes> attributes);

	protected CharSequence postField(VariableInformation var) {
		return "";
	}

	protected CharSequence preField(VariableInformation var, EnumSet<Attributes> attributes) {
		return "";
	}

	protected boolean isPredicate(VariableInformation info) {
		return info.name.startsWith(InternalInformation.PRED_PREFIX);
	}

	protected CharSequence idName(VariableInformation information, boolean field, EnumSet<Attributes> attributes) {
		return idName(information.name, field, attributes);
	}

	protected CharSequence idName(InternalInformation ii, boolean field, EnumSet<Attributes> attributes) {
		return idName(ii.info, field, attributes);
	}

	protected CharSequence idName(String name, boolean field, EnumSet<Attributes> attributes) {
		String res = name;
		final boolean isReg = isShadowReg(name);
		if (isReg) {
			res = name.substring(0, name.length() - 4);
		}
		res = res.replaceAll("[\\.\\$\\@\\/]+", "_")//
				.replaceAll("\\{", "Bit")//
				.replaceAll("\\}", "")//
				.replaceAll(":", "to")//
				.replaceAll("\\[", "arr")//
				.replaceAll("-1", "dyn")//
				.replaceAll("\\]", "");
		if (res.startsWith("#")) {
			res = res.substring(1);
		}
		if (field) {
			res = fieldPrefix() + res;
		}
		if (attributes.contains(isShadowReg)) {
			res = res + "$reg";
		}
		if (attributes.contains(isPrev))
			return res + "$prev";
		if (attributes.contains(isPredFresh))
			return res + "$fresh";
		if (attributes.contains(isPosEdgeActive))
			return res + "$pos_active";
		if (attributes.contains(isPosEdgeHandled))
			return res + "$pos_handled";
		if (attributes.contains(isNegEdgeActive))
			return res + "$neg_active";
		if (attributes.contains(isNegEdgeHandled))
			return res + "$neg_handled";
		if (attributes.contains(isUpdate))
			return res + "$update";
		return res;
	}

	protected CharSequence getFrameName(Frame frame) {
		final Formatter f = new Formatter();
		if (frame.constant) {
			f.format("const_");
		}
		if (frame.process != null) {
			f.format("%s_", frame.process);
		}
		f.format("s%03d_frame_%04X", Math.max(frame.scheduleStage, 0), frame.uniqueID);
		final String result = f.toString();
		f.close();
		return result;
	}

	protected int getVarIdx(InternalInformation ii) {
		return getVarIdx(ii.info, purgeAliases);
	}

	protected int getVarIdx(VariableInformation vi, boolean aliased) {
		if (aliased && (vi.aliasVar != null))
			return varIdx.get(vi.aliasVar.info.name);
		return varIdx.get(vi.name);
	}

	protected boolean isShadowReg(String name) {
		return name.endsWith("$reg");
	}

	protected String fieldPrefix() {
		return "";
	}

	protected boolean hasPrev(VariableInformation var) {
		return prevMapPos.contains(var.name) || prevMapNeg.contains(var.name);
	}

	protected boolean hasUpdate(VariableInformation var) {
		return (isPredicate(var) || hasPrev(var));
	}

	protected Iterable<VariableInformation> excludeNullAndAlias(Iterable<VariableInformation> vars) {
		return Iterables.filter(vars, new Predicate<VariableInformation>() {

			@Override
			public boolean apply(VariableInformation input) {
				return (input.aliasVar == null);
			}
		});
	}

	protected abstract CharSequence header();

	protected abstract CharSequence footer();

	protected CharSequence calculateVariableAccessIndexArr(VariableInformation varInfo) {
		final int lastIndex = varInfo.dimensions.length - 1;
		final StringBuilder arrayAccess = new StringBuilder();
		for (int i = 0; i < lastIndex; i++) {
			if (i != 0) {
				arrayAccess.append(" + ");
			}
			arrayAccess.append(Integer.toString(varInfo.dimensions[i]) + " * arrayIdx[" + i + "]");
		}
		if (lastIndex != 0) {
			arrayAccess.append(" + ");
		}
		arrayAccess.append("arrayIdx[" + lastIndex + "]");
		return arrayAccess;
	}

	protected class ProcessData {
		public List<Frame> frames = Lists.newArrayList();
		public final Set<Integer> handledPredicates = Sets.newLinkedHashSet();
		public final Set<Integer> handledNegEdge = Sets.newLinkedHashSet();
		public final Set<Integer> handledPosEdge = Sets.newLinkedHashSet();
		public final StringBuilder predicates = new StringBuilder();
		public final StringBuilder calls = new StringBuilder();
		public final String processName;

		public ProcessData(String processName) {
			this.processName = processName;
		}

		public void addFrame(Frame frame) {
			frames.add(frame);
			predicates.append(handleEdge(handledNegEdge, false, frame.edgeNegDepRes));
			predicates.append(handleEdge(handledPosEdge, true, frame.edgePosDepRes));
			predicates.append(handlePredicates(handledPredicates, false, frame.predNegDepRes));
			predicates.append(handlePredicates(handledPredicates, true, frame.predPosDepRes));
			calls.append(predicateCheckedFrameCall(frame));
		}

	}

	protected CharSequence testbenchMethod() {
		final Iterable<Frame> processframes = getProcessframes(em);
		if (!processframes.iterator().hasNext())
			return null;
		final Map<String, ProcessData> processes = Maps.newLinkedHashMap();
		for (final Frame frame : processframes) {
			ProcessData pd = processes.get(frame.process);
			if (pd == null) {
				pd = new ProcessData(frame.process);
				processes.put(frame.process, pd);
			}
			pd.addFrame(frame);
		}
		final StringBuilder result = new StringBuilder();
		result.append(createProcessMethods(processes));
		result.append(indent()).append(runTestbenchHeader());
		final VariableInformation stepCount = createVar("stepCount", 64, Type.UINT);
		result.append(indent()).append(assignVariable(stepCount, constant(ZERO, true, -1), NONE, false, true, false)).append(newLine());
		result.append(indent()).append(whileLoopStart(runTestBenchOuterLoopCondition(stepCount)));
		final VariableInformation modified = createVar("modified", -1, Type.BOOL);
		result.append(indent()).append(assignVariable(modified, constantBoolean(ZERO), NONE, false, true, false)).append(newLine());
		result.append(indent()).append(doLoopStart());
		result.append(indent()).append(assignVariable(modified, constantBoolean(ZERO), NONE, false, false, false)).append(newLine());
		for (final ProcessData pd : processes.values()) {
			result.append(indent()).append(ifCondition(callProcessMethod(pd), assignVariable(modified, constantBoolean(ONE), NONE, false, false, false), null));
		}
		result.append(indent()).append(doLoopEnd(condition(Condition.isTrue, modified.name, null)));
		result.append(indent()).append(callRunMethod());
		result.append(indent()).append(incVar(stepCount)).append(newLine());
		final VariableInformation nextTime = createVar("nextTime", 64, Type.UINT);
		result.append(indent()).append(assignVariable(nextTime, constant(Long.MAX_VALUE, true), NONE, false, true, false)).append(newLine());
		for (final ProcessData pd : processes.values()) {
			final CharSequence stateCondition = condition(Condition.isGreateEqual, processState(pd.processName), constant(ZERO, true, -1));
			final CharSequence stateBlockedCondition = condition(Condition.isNotEqual, processState(pd.processName), processStale());
			final CharSequence condition = condition(Condition.logiAnd, stateCondition, stateBlockedCondition);
			result.append(indent()).append(ifCondition(condition, assignNextTime(nextTime, processTime(pd.processName)), null));
		}

		result.append(indent()).append(assignVariable(timeName(), nextTime.name, NONE, true, false, false)).append(newLine());
		result.append(checkTestbenchListener());
		result.append(indent()).append(whileLoopEnd());
		result.append(indent()).append(runTestbenchFooter());
		return result;
	}

	protected abstract CharSequence checkTestbenchListener();

	protected CharSequence runTestbenchFooter() {
		return "}" + newLine();
	}

	protected abstract CharSequence assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime);

	protected abstract CharSequence callRunMethod();

	public static enum Condition {
		isTrue, isFalse, isLess, isLessEqual, isGreater, isGreateEqual, isEqual, isNotEqual, logiAnd, logiOr
	}

	protected CharSequence condition(Condition condition, CharSequence a, CharSequence b) {
		switch (condition) {
		case isEqual:
			return a + " == " + b;
		case isNotEqual:
			return a + " != " + b;
		case isFalse:
			return "!" + a;
		case isTrue:
			return a;
		case isGreateEqual:
			return a + " >= " + b;
		case isGreater:
			return a + " > " + b;
		case isLess:
			return a + " < " + b;
		case isLessEqual:
			return a + " <= " + b;
		case logiAnd:
			return "(" + a + ") && (" + b + ")";
		case logiOr:
			return "(" + a + ") || (" + b + ")";
		}
		throw new IllegalStateException("Should not get here");
	}

	protected CharSequence ifCondition(CharSequence condition, CharSequence thenDo, CharSequence elseDo) {
		final StringBuilder sb = new StringBuilder();
		sb.append("if (").append(condition).append(") {").append(newLine());
		indent++;
		sb.append(indent()).append(thenDo).append(newLine());
		indent--;
		if (elseDo != null) {
			sb.append(indent()).append("} else {").append(newLine());
			indent++;
			sb.append(indent()).append(elseDo).append(newLine());
			indent--;
		}
		sb.append(indent()).append("}").append(newLine());
		return sb;
	}

	protected CharSequence callProcessMethod(ProcessData pd) {
		return callMethod(processMethodName(pd));
	}

	public String processMethodName(ProcessData pd) {
		return "runProcess" + pd.processName;
	}

	protected abstract CharSequence callMethod(CharSequence methodName, CharSequence... args);

	protected CharSequence createProcessMethods(final Map<String, ProcessData> processes) {
		final StringBuilder result = new StringBuilder();
		for (final ProcessData pd : processes.values()) {
			final String processName = pd.processName;
			result.append(indent()).append(runProcessHeader(pd));
			final VariableInformation oldTime = createVar("oldTime", 64, Type.UINT);
			result.append(indent()).append(assignVariable(oldTime, processTime(processName), NONE, false, true, false)).append(newLine());
			final VariableInformation oldState = createVar("oldState", 64, Type.UINT);
			result.append(indent()).append(assignVariable(oldState, processState(processName), NONE, false, true, false)).append(newLine());
			result.append(indent()).append(whileLoopStart(processCondition(processName)));
			result.append(indent()).append(pd.predicates);
			result.append(indent()).append(pd.calls);
			result.append(indent()).append(whileLoopEnd());
			result.append(indent()).append(runProcessFooter(processName, oldTime, oldState));
		}
		return result;
	}

	protected CharSequence runTestBenchOuterLoopCondition(VariableInformation stepCount) {
		final CharSequence lessMaxTime = condition(Condition.isLessEqual, idName(timeName(), true, NONE), "maxTime");
		final CharSequence lessMaxSteps = condition(Condition.isLess, stepCount.name, "maxSteps");
		return condition(Condition.logiAnd, lessMaxTime, lessMaxSteps);
	}

	protected abstract CharSequence runTestbenchHeader();

	protected CharSequence whileLoopStart(CharSequence processCondition) {
		indent++;
		return "while (" + processCondition + ") {" + newLine();
	}

	protected CharSequence whileLoopEnd() {
		indent--;
		return "}" + newLine();
	}

	protected CharSequence processCondition(String processName) {
		final CharSequence isTimeGood = condition(Condition.isGreateEqual, idName(timeName(), true, NONE), processTime(processName));
		final CharSequence isNotWaiting = condition(Condition.isGreateEqual, processState(processName), constant(ZERO, true, -1));
		final CharSequence isNotStale = condition(Condition.isNotEqual, processState(processName), processStale());
		return condition(Condition.logiAnd, condition(Condition.logiAnd, isNotStale, isNotWaiting), isTimeGood);
	}

	protected VariableInformation timeName() {
		return varByName("$time");
	}

	public CharSequence processStale() {
		return constant(Integer.MAX_VALUE, true);
	}

	protected abstract CharSequence runProcessHeader(ProcessData pd);

	protected CharSequence runProcessFooter(String processName, VariableInformation oldTime, VariableInformation oldState) {
		final CharSequence timeModified = condition(Condition.isNotEqual, oldTime.name, processTime(processName));
		final CharSequence stateModified = condition(Condition.isNotEqual, oldState.name, processState(processName));
		final StringBuilder sb = new StringBuilder();
		sb.append(indent()).append(returnValue(condition(Condition.logiOr, stateModified, timeModified)));
		indent--;
		sb.append(indent()).append("}").append(newLine());
		return sb;
	}

	protected String getStatementSeparator() {
		return ";";
	}

	protected CharSequence processState(CharSequence processName) {
		return idName(varByName("$process_state_@" + processName), false, NONE);
	}

	protected CharSequence processTime(CharSequence processName) {
		return idName(varByName("$process_time_next_@" + processName), false, NONE);
	}

	protected VariableInformation varByName(String varName) {
		final String fullName = em.moduleName + '.' + varName;
		for (final VariableInformation var : em.variables) {
			if (var.name.equals(varName))
				return var;
			if (var.name.equals(fullName))
				return var;
		}
		throw new IllegalArgumentException("Did not find variable " + varName + " in model");
	}

	protected Iterable<Frame> getNonProcessframes(ExecutableModel model) {
		final List<Frame> res = Lists.newArrayList();
		for (final Frame frame : model.frames) {
			if (frame.process == null) {
				res.add(frame);
			}
		}
		return res;
	}

	protected Iterable<Frame> getProcessframes(ExecutableModel model) {
		final List<Frame> res = Lists.newArrayList();
		for (final Frame frame : model.frames) {
			if (frame.process != null) {
				res.add(frame);
			}
		}
		return res;
	}

}
