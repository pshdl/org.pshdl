package org.pshdl.model.simulation;

import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isArrayIndex;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isNegEdgeActive;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isNegEdgeHandled;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPosEdgeActive;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPosEdgeHandled;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPredFresh;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPredicate;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPrev;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isPublic;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isShadowReg;
import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.isUpdate;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
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

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

public abstract class CommonCodeGenerator {

	protected static final VariableInformation EPS_CYCLE = createVar("epsCycle", 32, Type.UINT);
	protected static final VariableInformation DELTA_CYCLE = createVar("deltaCycle", 32, Type.UINT);
	protected static final VariableInformation TIMESTAMP = createVar("timeStamp", 64, Type.UINT);
	protected static final VariableInformation DISABLE_EDGES = createVar("diableEdge", -1, Type.BOOL);
	protected static final VariableInformation DISABLE_REG_OUTPUTLOGIC = createVar("disableRegOutputLogic", -1, Type.BOOL);
	protected static final EnumSet<Attributes> NONE = EnumSet.noneOf(Attributes.class);
	protected final ExecutableModel em;
	protected int indent = 0;
	protected Map<String, Integer> varIdx = new HashMap<>();
	protected Map<String, Integer> intIdx = new HashMap<>();
	protected Set<String> prevMapPos = new HashSet<>();
	protected Set<String> prevMapNeg = new HashSet<>();
	protected boolean hasClock;
	protected final int bitWidth;
	protected final int maxCosts;

	public CommonCodeGenerator(ExecutableModel em, int bitWidth, int maxCosts) {
		this.em = em;
		this.bitWidth = bitWidth;
		for (int i = 0; i < em.variables.length; i++) {
			varIdx.put(em.variables[i].name, i);
		}
		for (int i = 0; i < em.internals.length; i++) {
			intIdx.put(em.internals[i].fullName, i);
		}
		for (final Frame f : em.frames) {
			if (f.edgeNegDepRes != -1) {
				prevMapNeg.add(asInternal(f.edgeNegDepRes).info.name);
			}
			if (f.edgePosDepRes != -1) {
				prevMapPos.add(asInternal(f.edgePosDepRes).info.name);
			}
		}
		this.hasClock = !prevMapPos.isEmpty() || !prevMapNeg.isEmpty();
		this.maxCosts = maxCosts;
	}

	protected InternalInformation asInternal(int id) {
		return em.internals[id];
	}

	public String doGenerateMainUnit() {
		final StringBuilder sb = new StringBuilder();
		sb.append(header());
		preBody();
		sb.append(preFieldDeclarations());
		sb.append(fieldDeclarations(true));
		sb.append(postFieldDeclarations());
		sb.append(preFrames());
		sb.append(frames());
		sb.append(postFrames());
		sb.append(preRunMethods());
		sb.append(runMethods());
		sb.append(postRunMethods());
		postBody();
		sb.append(footer());
		return sb.toString();
	}

	protected CharSequence runMethods() {
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
		final List<Integer> handledPredicates = Lists.newArrayList();
		sb.append(indent()).append(runMethodsHeader(createConstant));
		indent++;
		sb.append(indent()).append(assignConstant(EPS_CYCLE, BigInteger.ZERO, NONE)).append(newLine());
		if (!createConstant) {
			sb.append(indent()).append(incVar(DELTA_CYCLE)).append(newLine());
		}
		if (!createConstant && hasClock) {
			sb.append(indent()).append(doLoopStart()).append(newLine());
			indent++;
			sb.append(indent()).append(clearRegUpdates());
		}
		sb.append(indent()).append(assignVariable(TIMESTAMP, "(" + DELTA_CYCLE.name + " << 16) | " + EPS_CYCLE.name, NONE, true, false)).append(newLine());
		for (int i = 0; i <= maxStage; i++) {
			final Collection<Frame> frames = schedulingStage.get(i);
			for (final Frame frame : frames) {
				if (frame.constant == createConstant) {
					sb.append(indent()).append(callStage(i, createConstant));
					selectedScheduleStage.add(i);
					break;
				}
			}
		}
		if (!createConstant && hasClock) {
			sb.append(indent()).append(applyRegUpdates()).append(newLine());
			sb.append(indent()).append(incVar(EPS_CYCLE)).append(newLine());
			final StringBuilder condition = new StringBuilder();
			condition.append(checkRegupdates());
			condition.append(" && !").append(DISABLE_REG_OUTPUTLOGIC.name);
			indent--;
			sb.append(indent()).append(doLoopEnd(condition)).append(newLine());
			for (final String prev : prevMapNeg) {
				final VariableInformation var = em.variables[varIdx.get(prev)];
				sb.append(indent()).append(assignVariable(var, idName(var, true, NONE), EnumSet.of(isPrev), true, false)).append(newLine());
			}
			for (final String prev : prevMapPos) {
				if (prevMapNeg.contains(prev)) {
					continue;
				}
				final VariableInformation var = em.variables[varIdx.get(prev)];
				sb.append(indent()).append(assignVariable(var, idName(var, true, NONE), EnumSet.of(isPrev), true, false)).append(newLine());
			}
		}
		indent--;
		sb.append(indent()).append(runMethodsFooter(createConstant));
		sb.append(createStageMethods(schedulingStage, createConstant, selectedScheduleStage, handledPredicates));
		return sb;
	}

	protected abstract CharSequence clearRegUpdates();

	protected CharSequence createStageMethods(final Multimap<Integer, Frame> schedulingStage, final boolean createConstant, final List<Integer> selectedScheduleStage,
			final List<Integer> handledPredicates) {
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
			sb.append(createStageMethod(createConstant, handledPredicates, stage, matchingFrames, totalStageCosts));
		}
		return sb;
	}

	protected CharSequence createStageMethod(final boolean createConstant, final List<Integer> handledPredicates, final int stage, final List<Frame> matchingFrames,
			int totalStageCosts) {
		final StringBuilder sb = new StringBuilder();
		sb.append(indent()).append(stageMethodsHeader(stage, totalStageCosts, createConstant));
		indent++;
		for (final Frame frame : matchingFrames) {
			sb.append(handleEdge(handledPredicates, false, frame.edgeNegDepRes));
			sb.append(handleEdge(handledPredicates, true, frame.edgePosDepRes));
			sb.append(handlePredicates(handledPredicates, false, frame.predNegDepRes));
			sb.append(handlePredicates(handledPredicates, true, frame.predPosDepRes));
		}
		int stageCosts = 0;
		if (maxCosts != Integer.MAX_VALUE) {
			sb.append(indent()).append(barrierBegin(stage, totalStageCosts, createConstant));
		}
		for (final Iterator<Frame> iterator = matchingFrames.iterator(); iterator.hasNext();) {
			final Frame frame = iterator.next();
			stageCosts += estimateFrameCosts(frame);
			sb.append(predicateCheckedFrameCall(frame));
			if ((stageCosts > maxCosts) && iterator.hasNext()) {
				sb.append(indent()).append(barrier());
				stageCosts = 0;
			}
		}
		if (maxCosts != Integer.MAX_VALUE) {
			sb.append(indent()).append(barrierEnd(stage, totalStageCosts, createConstant));
		}
		indent--;
		sb.append(indent()).append(stageMethodsFooter(stage, totalStageCosts, createConstant));
		return sb;
	}

	protected abstract CharSequence applyRegUpdates();

	protected abstract CharSequence checkRegupdates();

	protected StringBuilder incVar(VariableInformation incVar) {
		return assignVariable(incVar, incVar.name + " + 1", NONE, true, false);
	}

	protected CharSequence doLoopStart() {
		return "do {";
	}

	protected CharSequence doLoopEnd(CharSequence condition) {
		return "} while (" + condition + ");";
	}

	protected CharSequence predicateCheckedFrameCall(Frame frame) {
		if ((frame.edgeNegDepRes == -1) && (frame.edgePosDepRes == -1) && (frame.predNegDepRes.length == 0) && (frame.predPosDepRes.length == 0)) {
			final StringBuilder sb = new StringBuilder();
			sb.append(indent()).append(callFrame(frame));
			return sb;
		}
		final List<CharSequence> predicates = Lists.newArrayList();
		final List<Integer> arr = Collections.emptyList();
		if (frame.edgeNegDepRes != -1) {
			predicates.add(internalWithArrayAccess(em.internals[frame.edgeNegDepRes], arr, EnumSet.of(isNegEdgeActive)));
			predicates.add("!" + internalWithArrayAccess(em.internals[frame.edgeNegDepRes], arr, EnumSet.of(isNegEdgeHandled)));
		}
		if (frame.edgePosDepRes != -1) {
			predicates.add(internalWithArrayAccess(em.internals[frame.edgePosDepRes], arr, EnumSet.of(isPosEdgeActive)));
			predicates.add("!" + internalWithArrayAccess(em.internals[frame.edgePosDepRes], arr, EnumSet.of(isPosEdgeHandled)));
		}
		for (final int pred : frame.predNegDepRes) {
			predicates.add("!" + internalWithArrayAccess(em.internals[pred], arr, EnumSet.of(isPredicate)));
			predicates.add(internalWithArrayAccess(em.internals[pred], arr, EnumSet.of(isPredFresh)));
		}
		for (final int pred : frame.predPosDepRes) {
			predicates.add(internalWithArrayAccess(em.internals[pred], arr, EnumSet.of(isPredicate)));
			predicates.add(internalWithArrayAccess(em.internals[pred], arr, EnumSet.of(isPredFresh)));
		}
		return callFrameWithPredicates(frame, predicates);
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

	protected CharSequence handlePredicates(List<Integer> handledPredicates, boolean positive, int[] predicates) {
		final StringBuilder sb = new StringBuilder();
		for (final int pred : predicates) {
			if ((pred != -1) && !handledPredicates.contains(pred)) {
				sb.append(indent()).append(updatePredicate(pred, positive)).append(newLine());
				handledPredicates.add(pred);
			}
		}
		return sb;
	}

	protected CharSequence updatePredicate(int pred, boolean positive) {
		final StringBuilder sb = new StringBuilder();
		final List<Integer> arr = Collections.emptyList();
		final InternalInformation internal = em.internals[pred];
		final CharSequence updateInternal = internalWithArrayAccess(internal, arr, EnumSet.of(isUpdate));
		sb.append(assignInternal(em.internals[pred], updateInternal + " == " + TIMESTAMP.name, arr, EnumSet.of(Attributes.isPredFresh)));
		return sb;
	}

	protected CharSequence handleEdge(final List<Integer> handledEdges, final boolean posEdge, final int edgeDepRes) {
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
		final InternalInformation internal = em.internals[edgeDepRes];
		final CharSequence updateInternal = internalWithArrayAccess(internal, arr, EnumSet.of(isUpdate));
		final StringBuilder sb = new StringBuilder();
		sb.append(assignInternal(internal, skipEdge(updateInternal), arr, EnumSet.of(posEdge ? isPosEdgeHandled : isNegEdgeHandled)));
		return sb;
	}

	protected CharSequence skipEdge(CharSequence updateInternal) {
		return "skipEdge(" + updateInternal + ")";
	}

	protected CharSequence updateEdge(int edgeDepRes, boolean posEdge) {
		final List<Integer> arr = Collections.emptyList();
		final InternalInformation internal = em.internals[edgeDepRes];
		final CharSequence prevInternal = internalWithArrayAccess(internal, arr, EnumSet.of(isPrev));
		final CharSequence currInternal = internalWithArrayAccess(internal, arr, NONE);
		final StringBuilder value = new StringBuilder();
		if (posEdge) {
			value.append("((").append(prevInternal).append(" == 0) || ").append(DISABLE_EDGES.name).append(") && (").append(currInternal).append(" == 1)");
		} else {
			value.append("((").append(prevInternal).append(" == 1) || ").append(DISABLE_EDGES.name).append(") && (").append(currInternal).append(" == 0)");
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

	protected abstract CharSequence callStage(int i, boolean constant);

	protected CharSequence callFrame(Frame f) {
		final StringBuilder sb = new StringBuilder();
		sb.append(getFrameName(f)).append("();\n");
		return sb;
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
		final String last = getTempName(stack.pop(), NONE);
		final InternalInformation outputInternal = asInternal(frame.outputId);
		sb.append(indent());
		final VariableInformation varInfo = outputInternal.info;
		if (!isNull(varInfo)) {
			sb.append(writeInternal(outputInternal, last, arr)).append(newLine());
			if (outputInternal.isPred) {
				sb.append(indent()).append(assignInternal(outputInternal, TIMESTAMP.name, arr, EnumSet.of(isUpdate))).append(comment("update timestamp"));
			}
		} else {
			sb.append(comment("Write to #null"));
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
				offset = calculateVariableAccessIndex(arr, varInfo);
			}
		}
		return offset;
	}

	protected abstract CharSequence scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset, boolean force);

	protected CharSequence assignInternal(InternalInformation output, CharSequence value, List<Integer> arr, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(internalWithArrayAccess(output, arr, attributes));
		int targetSizeWithType = output.actualWidth << 1;
		if (output.info.type == Type.INT) {
			targetSizeWithType++;
		}
		sb.append(doAssign(value, targetSizeWithType));
		return sb;
	}

	protected StringBuilder assignVariable(VariableInformation var, CharSequence assignValue, EnumSet<Attributes> attributes, boolean field, boolean declare) {
		final StringBuilder sb = new StringBuilder();
		if (declare) {
			sb.append(fieldType(var, attributes));
		}
		sb.append(idName(var, field, attributes));
		sb.append(doAssign(assignValue, var.width << 1));
		return sb;
	}

	protected StringBuilder assignTempVar(int targetSizeWithType, int pos, EnumSet<Attributes> attributes, CharSequence assignValue) {
		final StringBuilder sb = new StringBuilder();
		sb.append(tempVar(pos, targetSizeWithType, attributes));
		sb.append(doAssign(assignValue, targetSizeWithType));
		return sb;
	}

	protected CharSequence doAssign(CharSequence assignValue, int targetSizeWithType) {
		final StringBuilder sb = new StringBuilder();
		sb.append(" = ");
		if (isSignedType(targetSizeWithType)) {
			sb.append(signExtend(assignValue, targetSizeWithType));
		} else {
			sb.append(assignValue);
		}
		sb.append("; ");
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
			sb.append(assignTempVar((exec.arg1 + exec.arg2) << 1, pos, NONE, "(" + getTempName(b, NONE) + " << " + exec.arg2 + ") | " + getTempName(a, NONE)));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toEdgeExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case isFallingEdge:
		case isRisingEdge:
			sb.append(assignInternal(em.internals[exec.arg1], TIMESTAMP.name, arr, EnumSet.of(Attributes.isUpdate)));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toCastExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		final String tempName = getTempName(a, NONE);
		switch (exec.inst) {
		case bitAccessSingle:
			sb.append(assignTempVar(2, pos, NONE, mask(tempName + " >> " + exec.arg1, 1)));
			break;
		case bitAccessSingleRange:
			final int highBit = exec.arg1;
			final int lowBit = exec.arg2;
			final int targetSize = (highBit - lowBit) + 1;
			sb.append(assignTempVar(targetSize << 1, pos, NONE, mask(tempName + " >> " + lowBit, targetSize)));
			break;
		case cast_int:
			// Create a targetSizeWitType with int indication
			sb.append(assignTempVar((exec.arg1 << 1) + 1, pos, NONE, tempName));
			break;
		case cast_uint:
			sb.append(assignTempVar(exec.arg1 << 1, pos, NONE, mask(tempName, exec.arg1)));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toLoadStoreExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case const0:
			sb.append(assignTempVar(-1, pos, NONE, constant(BigInteger.ZERO)));
			break;
		case const1:
			sb.append(assignTempVar(-1, pos, NONE, constant(BigInteger.ONE)));
			break;
		case const2:
			sb.append(assignTempVar(-1, pos, NONE, constant(BigInteger.valueOf(2))));
			break;
		case constAll1:
			sb.append(assignTempVar(-1, pos, NONE, constant(calcMask(exec.arg1))));
			break;
		case loadConstant:
			sb.append(assignTempVar(-1, pos, NONE, constant(frame.constants[exec.arg1])));
			break;
		case loadInternal:
			sb.append(assignTempVar(-1, pos, NONE, loadInternal(em.internals[exec.arg1], arr, NONE)));
			break;
		case writeInternal:
			sb.append(writeInternal(em.internals[exec.arg1], getTempName(a, NONE), arr));
			break;
		case pushAddIndex:
			sb.append(assignIndexVar(arr, a));
			// sb.append('''int a«arr.last»=(int)t«a»;''')
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence assignIndexVar(List<Integer> arr, int a) {
		final StringBuilder sb = new StringBuilder();
		sb.append(assignTempVar(-1, arr.get(arr.size() - 1).intValue(), EnumSet.of(isArrayIndex), getTempName(a, NONE)));
		return sb;
	}

	protected CharSequence writeInternal(InternalInformation internal, String tempName, List<Integer> arr) {
		final StringBuilder sb = new StringBuilder();
		if (internal.isShadowReg) {
			final String cpyName = tempName + "_cpy";
			final boolean forceRegUpdate = internal.fixedArray && internal.isFillArray;
			if (!forceRegUpdate) {
				// TODO save fill value
				final VariableInformation cpyVar = createVar(cpyName, internal.actualWidth, internal.info.type);
				sb.append(assignVariable(cpyVar, internalWithArrayAccess(internal, arr, NONE), NONE, false, true)).append(comment("Backup of current value"));
				sb.append(indent());
			}
			tempName = createBitAccessIfNeeded(internal, tempName, arr, sb);
			sb.append(assignInternal(internal, tempName, arr, EnumSet.of(isShadowReg))).append(comment("Assign value"));
			final CharSequence offset = calcRegUpdateOffset(arr, internal);
			sb.append(indent()).append(scheduleShadowReg(internal, internalWithArrayAccess(internal, arr, EnumSet.of(isShadowReg)), cpyName, offset, forceRegUpdate));
		} else {
			tempName = createBitAccessIfNeeded(internal, tempName, arr, sb);
			sb.append(assignInternal(internal, tempName, arr, NONE)).append(comment("Assign value"));
		}
		arr.clear();
		return sb;
	}

	protected String createBitAccessIfNeeded(InternalInformation internal, String tempName, List<Integer> arr, final StringBuilder sb) {
		if (internal.actualWidth != internal.info.width) {
			final VariableInformation currVar = createVar(tempName + "_current", internal.actualWidth, internal.info.type);
			final CharSequence currentValue = internalWithArrayAccess(internal, arr, internal.isShadowReg ? EnumSet.of(isShadowReg) : NONE);
			final BigInteger mask = calcMask(internal.actualWidth);
			final CharSequence writeMask = constant(mask.shiftLeft(internal.bitEnd).not());
			sb.append(assignVariable(currVar, currentValue + " & " + writeMask, NONE, false, true)).append(comment("Current value"));
			final VariableInformation maskVar = createVar(tempName + "_mask_shift", internal.actualWidth, internal.info.type);
			sb.append(indent()).append(assignVariable(maskVar, "(" + mask(tempName, internal.actualWidth) + ") << " + internal.bitEnd, NONE, false, true))
					.append(comment("Masked and shifted"));
			tempName = "(" + currVar.name + " | " + maskVar.name + ")";
			sb.append(indent());
		}
		return tempName;
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
				sb.append(arrayAccess(varName, calculateVariableAccessIndex(arr, varInfo)));
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

	protected CharSequence calculateVariableAccessIndex(List<Integer> arr, final VariableInformation varInfo) {
		final int lastIndex = arr.size() - 1;
		final StringBuilder arrayAccess = new StringBuilder();
		for (int i = 0; i < lastIndex; i++) {
			if (i != 0) {
				arrayAccess.append(" + ");
			}
			arrayAccess.append(Integer.toString(varInfo.dimensions[i]) + " * " + getTempName(arr.get(i), EnumSet.of(isArrayIndex)));
		}
		if (lastIndex != 0) {
			arrayAccess.append(" + ");
		}
		arrayAccess.append(getTempName(arr.get(lastIndex), EnumSet.of(isArrayIndex)));
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
		if (attribute.contains(Attributes.isArrayIndex))
			return "a" + pos;
		return "t" + pos;
	}

	protected CharSequence toEqualityExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case eq:
			sb.append(twoOp("==", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case not_eq:
			sb.append(twoOp("!=", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case less:
			sb.append(twoOp("<", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case less_eq:
			sb.append(twoOp("<=", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case greater:
			sb.append(twoOp(">", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case greater_eq:
			sb.append(twoOp(">=", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toPredicateExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case logiNeg:
			sb.append(singleOp("!", exec.arg1, pos, a, EnumSet.of(isPredicate)));
			break;
		case logiAnd:
			sb.append(twoOp("&&", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case logiOr:
			sb.append(twoOp("||", exec.arg1, pos, b, a, EnumSet.of(isPredicate)));
			break;
		case negPredicate:
		case posPredicate:
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toShiftExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case sll:
			sb.append(twoOp("<<", exec.arg1, pos, b, a, NONE));
			break;
		case srl:
			sb.append(twoOp(">>>", exec.arg1, pos, b, a, NONE));
			break;
		case sra:
			sb.append(twoOp(">>", exec.arg1, pos, b, a, NONE));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toArithExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case arith_neg:
			sb.append(singleOp("-", exec.arg1, pos, a, NONE));
			break;
		case plus:
			sb.append(twoOp("+", exec.arg1, pos, b, a, NONE));
			break;
		case minus:
			sb.append(twoOp("-", exec.arg1, pos, b, a, NONE));
			break;
		case mul:
			sb.append(twoOp("*", exec.arg1, pos, b, a, NONE));
			break;
		case div:
			sb.append(twoOp("/", exec.arg1, pos, b, a, NONE));
			break;
		case mod:
			sb.append(twoOp("%", exec.arg1, pos, b, a, NONE));
			break;
		case pow:
			sb.append(twoOp("%", exec.arg1, pos, b, a, NONE));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}
		return sb;
	}

	protected CharSequence toBitExpression(FastInstruction exec, Frame frame, int pos, int a, int b, List<Integer> arr, int arrPos) {
		final StringBuilder sb = new StringBuilder();
		switch (exec.inst) {
		case bit_neg:
			sb.append(singleOp("~", exec.arg1, pos, a, NONE));
			break;
		case and:
			sb.append(twoOp("&", exec.arg1, pos, b, a, NONE));
			break;
		case or:
			sb.append(twoOp("|", exec.arg1, pos, b, a, NONE));
			break;
		case xor:
			sb.append(twoOp("^", exec.arg1, pos, b, a, NONE));
			break;
		default:
			throw new IllegalArgumentException("Did not instruction:" + exec + " here");
		}

		return sb;
	}

	protected StringBuilder singleOp(String op, int targetSizeWithType, int pos, int a, EnumSet<Attributes> attributes) {
		final CharSequence assignValue = singleOpValue(op, getCast(targetSizeWithType), a, targetSizeWithType, attributes);
		return assignTempVar(targetSizeWithType, pos, attributes, assignValue);
	}

	protected StringBuilder twoOp(String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes) {
		final CharSequence assignValue = twoOpValue(op, getCast(targetSizeWithType), leftOperand, rightOperand, targetSizeWithType, attributes);
		return assignTempVar(targetSizeWithType, pos, attributes, assignValue);
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
		sb.append(fieldType(var, NONE));
		sb.append(var.name);
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
		return assignOpValue(targetSizeWithType, attributes, finalOp);
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
		return "(" + op + ") & " + constant(mask);
	}

	protected CharSequence invertedMask(CharSequence op, int targetSize) {
		if (targetSize == bitWidth)
			return op;
		final BigInteger mask = calcMask(targetSize).not();
		return "(" + op + ") & " + constant(mask);
	}

	protected BigInteger calcMask(int targetSize) {
		return (BigInteger.ONE.shiftLeft(targetSize)).subtract(BigInteger.ONE);
	}

	protected CharSequence signExtend(CharSequence op, int targetSizeWithType) {
		final int targetSize = targetSizeWithType >> 1;
		if (targetSize == 0)
			return op;
		final CharSequence cast = getCast(targetSizeWithType);
		final int shift = bitWidth - targetSize;
		if ((cast != null) && (cast.length() != 0))
			return "((" + cast + "(" + op + ")) << " + shift + ") >> " + shift;
		return "((" + op + ") << " + shift + ") >> " + shift;
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
		indent--;
	}

	protected void preBody() {
		indent++;
	}

	protected CharSequence fieldDeclarations(boolean includePrivate) {
		final StringBuilder sb = new StringBuilder();
		preFieldDeclaration();
		for (final VariableInformation var : excludeNull(em.variables)) {
			if (hasPrev(var) && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPrev))).append(newLine());
				if (prevMapPos.contains(var.name)) {
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPosEdgeActive))).append(newLine());
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPosEdgeHandled))).append(newLine());
				}
				if (prevMapNeg.contains(var.name)) {
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isNegEdgeActive))).append(newLine());
					sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isNegEdgeHandled))).append(newLine());
				}
			}
			if (hasUpdate(var)) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isUpdate, isPublic))).append(newLine());
			}
			if (isPredicate(var) && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPredFresh))).append(newLine());
			}
			if (var.isRegister && includePrivate) {
				sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isShadowReg))).append(newLine());
			}
			sb.append(indent()).append(createVarDeclaration(var, EnumSet.of(isPublic))).append(newLine());
		}
		sb.append(indent()).append(createVarDeclaration(EPS_CYCLE, EnumSet.of(isPublic))).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DELTA_CYCLE, EnumSet.of(isPublic))).append(newLine());
		sb.append(indent()).append(createVarDeclaration(TIMESTAMP, EnumSet.of(isPublic))).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DISABLE_EDGES, EnumSet.of(isPublic))).append(newLine());
		sb.append(indent()).append(createVarDeclaration(DISABLE_REG_OUTPUTLOGIC, EnumSet.of(isPublic))).append(newLine());
		postFieldDeclaration();
		return sb;
	}

	public static VariableInformation createVar(String name, int width, Type type) {
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
		baseType, isPrev, isUpdate, isShadowReg, isPublic, isPredicate, isArrayIndex, isPosEdgeActive, isPosEdgeHandled, isNegEdgeActive, isNegEdgeHandled, isPredFresh, isArray
	}

	protected CharSequence createVarDeclaration(VariableInformation var, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(preField(var, attributes));
		sb.append(fieldType(var, attributes));
		if (isArray(var)) {
			sb.append(assignArrayInit(var, BigInteger.ZERO, attributes));
		} else {
			sb.append(assignConstant(var, BigInteger.ZERO, attributes));
		}
		sb.append(postField(var));
		return sb;
	}

	protected CharSequence assignArrayInit(VariableInformation var, BigInteger zero, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(fieldName(var, attributes));
		sb.append(doAssign(arrayInit(var, zero, attributes), -1));
		return sb;
	}

	protected abstract CharSequence arrayInit(VariableInformation var, BigInteger zero, EnumSet<Attributes> attributes);

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

	protected boolean isArray(VariableInformation var) {
		return (var.dimensions != null) && (var.dimensions.length != 0);
	}

	protected CharSequence assignConstant(VariableInformation var, BigInteger constantValue, EnumSet<Attributes> attributes) {
		final StringBuilder sb = new StringBuilder();
		sb.append(fieldName(var, attributes));
		CharSequence assignValue;
		if (isBoolean(var, attributes)) {
			assignValue = constantBoolean(constantValue);
		} else {
			assignValue = constant(constantValue);
		}
		sb.append(doAssign(assignValue, -1));
		return sb;
	}

	protected CharSequence constantBoolean(BigInteger constantValue) {
		if (constantValue.equals(BigInteger.ZERO))
			return "false";
		return "true";
	}

	protected CharSequence constant(BigInteger constantValue) {
		if ((constantValue.compareTo(BigInteger.TEN) < 0) && (constantValue.compareTo(BigInteger.TEN.negate()) > 0))
			return constantValue.toString(10);
		if (constantValue.bitLength() <= 32)
			return constant32Bit(constantValue.intValue());
		if (constantValue.bitLength() <= 64)
			return constant64Bit(constantValue.longValue());
		return constantVarLength(constantValue);
	}

	protected CharSequence constantVarLength(BigInteger constantValue) {
		return String.format("0x%X", constantValue);
	}

	protected CharSequence constant32Bit(int intValue) {
		return String.format("0x%08XL", intValue);
	}

	protected CharSequence constant64Bit(long longValue) {
		return String.format("0x%016XL", longValue);
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
			return res + "_prev";
		if (attributes.contains(isPredFresh))
			return res + "_fresh";
		if (attributes.contains(isPosEdgeActive))
			return res + "_pos_active";
		if (attributes.contains(isPosEdgeHandled))
			return res + "_pos_handled";
		if (attributes.contains(isNegEdgeActive))
			return res + "_neg_active";
		if (attributes.contains(isNegEdgeHandled))
			return res + "_neg_handled";
		if (attributes.contains(isUpdate))
			return res + "_update";
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

	protected Iterable<VariableInformation> excludeNull(VariableInformation... vars) {
		return excludeNull(Lists.newArrayList(vars));
	}

	protected Iterable<VariableInformation> excludeNull(Iterable<VariableInformation> vars) {
		return Iterables.filter(vars, new Predicate<VariableInformation>() {

			@Override
			public boolean apply(VariableInformation input) {
				return !isNull(input);
			}
		});
	}

	protected abstract CharSequence header();

	protected abstract CharSequence footer();

	protected boolean isNull(VariableInformation input) {
		return input.name.equals("#null");
	}
}
