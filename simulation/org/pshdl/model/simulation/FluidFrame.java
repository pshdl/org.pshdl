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
import java.util.Map.Entry;
import java.util.concurrent.atomic.*;
import java.util.regex.*;

import org.pshdl.interpreter.*;
import org.pshdl.interpreter.Frame.FastInstruction;
import org.pshdl.interpreter.VariableInformation.Direction;
import org.pshdl.interpreter.VariableInformation.Type;
import org.pshdl.interpreter.utils.*;

import com.google.common.collect.*;

public class FluidFrame {

	public static class ArgumentedInstruction {
		public final String args[];
		public final Instruction instruction;

		public ArgumentedInstruction(Instruction instruction, String... args) {
			super();
			this.instruction = instruction;
			if (args != null) {
				for (String string : args) {
					if (string == null)
						throw new IllegalArgumentException("Null is not a valid argument");
				}
			}
			this.args = args;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(instruction);
			if (args.length != 0) {
				builder.append(Arrays.toString(args));
			}
			return builder.toString();
		}
	}

	private static class FrameRegister {
		public final Set<String> inputs = new LinkedHashSet<String>();
		private int internalIdCounter = 0;
		public final Map<String, Integer> internalIds = new LinkedHashMap<String, Integer>();

		public Integer getInternal(String in) {
			return internalIds.get(in);
		}

		public Integer registerInternal(String in) {
			if (internalIds.get(in) == null) {
				internalIds.put(in, internalIdCounter++);
			}
			return internalIds.get(in);
		}

		public Integer registerInput(String string) {
			inputs.add(string);
			return registerInternal(string);
		}

	}

	/**
	 * A global counter for frame ids
	 */
	private static AtomicInteger gid = new AtomicInteger();

	/**
	 * The local id of this frame
	 */
	public final int id;

	/**
	 * All constants by name
	 */
	public final Map<String, BigInteger> constants = new LinkedHashMap<String, BigInteger>();

	public final LinkedList<ArgumentedInstruction> instructions = new LinkedList<ArgumentedInstruction>();
	public String outputName;
	public final boolean constant;

	public final Set<FluidFrame> references = new LinkedHashSet<FluidFrame>();
	public final Map<String, VariableInformation> vars = new HashMap<String, VariableInformation>();

	public FluidFrame() {
		this(null, false);
	}

	public FluidFrame(String outputName, boolean constant) {
		this.id = gid.incrementAndGet();
		if (outputName != null) {
			this.outputName = outputName;
		} else {
			this.outputName = Integer.toString(id);
		}
		this.constant = constant;
	}

	public void add(ArgumentedInstruction argumentedInstruction) {
		instructions.add(argumentedInstruction);
	}

	public void add(Instruction inst) {
		instructions.add(new ArgumentedInstruction(inst));
	}

	public void addReferencedFrame(FluidFrame frame) {
		vars.putAll(frame.vars);
		if (frame.hasInstructions()) {
			references.add(frame);
		} else {
			references.addAll(frame.references);
		}
	}

	public FluidFrame append(FluidFrame frame) {
		// Don't copy references
		vars.putAll(frame.vars);
		constants.putAll(frame.constants);
		instructions.addAll(frame.instructions);
		return this;
	}

	public ExecutableModel getExecutable() {
		FrameRegister register = new FrameRegister();
		for (FluidFrame entry : references) {
			entry.registerFrame(register);
		}
		List<Frame> res = toFrame(register);
		InternalInformation[] internals = new InternalInformation[register.internalIds.size()];
		for (Entry<String, Integer> e : register.internalIds.entrySet()) {
			String name = e.getKey();
			String basicName = InternalInformation.getBaseName(name, false, false);
			VariableInformation info = vars.get(basicName);
			internals[e.getValue()] = new InternalInformation(name, info);
		}
		Map<String, List<Frame>> lastID = Maps.newHashMap();
		for (Frame frame : res) {
			InternalInformation ii = internals[frame.outputId];
			LinkedList<Frame> lID = (LinkedList<Frame>) lastID.get(ii.info.name);
			if (lID != null) {
				Iterator<Frame> iterator = lID.descendingIterator();
				PredicateChain pred = getPredicate(internals, frame);
				while (iterator.hasNext()) {
					Frame previous = iterator.next();
					PredicateChain predPrev = getPredicate(internals, previous);
					if ((pred == null) || (predPrev == null) || pred.equals(predPrev)) {
						frame.executionDep = previous.uniqueID;
						break;
					}
					if (!predPrev.isMutual(pred)) {
						frame.executionDep = previous.uniqueID;
						break;
					}
					// System.out.println(pred + " -> \n" + predPrev);
				}
			} else {
				lID = new LinkedList<Frame>();
				lastID.put(ii.info.name, lID);
			}
			int maxData = 0;
			for (int i : frame.internalDependencies) {
				maxData = Math.max(internals[i].actualWidth, maxData);
			}
			maxData = Math.max(ii.actualWidth, maxData);
			frame.maxDataWidth = Math.max(frame.maxDataWidth, maxData);
			lID.add(frame);
		}
		VariableInformation[] fVars = vars.values().toArray(new VariableInformation[vars.values().size()]);
		return new ExecutableModel(res.toArray(new Frame[res.size()]), internals, fVars);
	}

	private static class PredicateChain {
		private static final Pattern globalPattern = Pattern.compile("\\" + InternalInformation.PRED_PREFIX + "(.*?)(\\$.*)");
		private static final Pattern partPattern = Pattern.compile("\\$([^\\$\\.]*?)(\\d+)([pn]?)\\.?");
		public final String base;
		public final LinkedList<Part> parts = Lists.newLinkedList();

		public static enum PartType {
			p, n, none
		}

		private static class Part {

			public final String part;
			public final int num;
			public PartType type;

			public Part(String part, int num, PartType type) {
				super();
				this.part = part;
				this.num = num;
				this.type = type;
			}

			@Override
			public String toString() {
				if (type != PartType.none)
					return '$' + part + num + type;
				return '$' + part + num;
			}
		}

		public PredicateChain(String predicate, PartType last) {
			Matcher matcher = globalPattern.matcher(predicate);
			if (matcher.find()) {
				base = matcher.group(1);
				String part = matcher.group(2);
				if (part != null) {
					Matcher pMatch = partPattern.matcher(part);
					while (pMatch.find()) {
						int id = Integer.parseInt(pMatch.group(2));
						PartType t = PartType.none;
						if (!"".equals(pMatch.group(3))) {
							t = PartType.valueOf(pMatch.group(3));
						}
						parts.add(new Part(pMatch.group(1), id, t));
					}
					parts.getLast().type = last;
				}
			} else {
				base = predicate.substring(6);
			}
		}

		public boolean isMutual(PredicateChain pred) {
			Iterator<Part> piter = parts.iterator();
			for (Part p : pred.parts) {
				if (!piter.hasNext())
					// This is shorter, but has been the same so far, so it is a
					// super statement
					return false;
				Part s = piter.next();
				if (!p.part.equals(s.part))
					return false;
				boolean isCase = "case".equals(p.part);
				if (p.num != s.num)
					if (isCase)
						return isCase;
				if (p.type != s.type)
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(base);
			for (Part p : parts) {
				sb.append(p);
			}
			return sb.toString();
		}
	}

	public PredicateChain getPredicate(InternalInformation[] internals, Frame frame) {
		if (frame.predNegDepRes.length != 0) {
			int i = frame.predNegDepRes[frame.predNegDepRes.length - 1];
			String fullName = internals[i].fullName;
			return new PredicateChain(fullName, PredicateChain.PartType.n);
		}
		if (frame.predPosDepRes.length != 0) {
			int i = frame.predPosDepRes[frame.predPosDepRes.length - 1];
			String fullName = internals[i].fullName;
			return new PredicateChain(fullName, PredicateChain.PartType.p);
		}
		return null;
	}

	private void registerFrame(FrameRegister register) {
		if (outputName.endsWith(InternalInformation.REG_POSTFIX)) {
			register.registerInternal(InternalInformation.stripReg(outputName));
		}
		register.registerInternal(outputName);
		for (FluidFrame entry : references) {
			entry.registerFrame(register);
		}
	}

	private int[] toIntArray(Collection<Integer> instr) {
		int[] instrRes = new int[instr.size()];
		int pos = 0;
		for (int i : instr) {
			instrRes[pos++] = i;
		}
		return instrRes;
	}

	private List<Frame> toFrame(FrameRegister register) {
		Set<Integer> internalDependencies = new LinkedHashSet<Integer>();
		List<BigInteger> constants = new LinkedList<BigInteger>();
		int stackCount = 0;
		int maxStackCount = -1;
		int maxDataWidth = 0;
		int constantIdCount = 0;
		int posEdge = -1, negEdge = -1;
		List<Integer> posPred = new LinkedList<Integer>(), negPred = new LinkedList<Integer>();
		List<FastInstruction> instr = new LinkedList<FastInstruction>();
		for (ArgumentedInstruction ai : instructions) {
			stackCount += ai.instruction.push;
			stackCount -= ai.instruction.pop;
			maxStackCount = Math.max(maxStackCount, stackCount);
			int arg1 = 0, arg2 = 0;
			Instruction i = ai.instruction;
			switch (ai.instruction) {
			case negPredicate: {
				Integer internalId = register.registerInternal(InternalInformation.PRED_PREFIX + toFullRef(ai));
				if (internalId == null)
					throw new IllegalArgumentException(ai.toString());
				internalDependencies.add(internalId);
				negPred.add(internalId);
				arg1 = internalId;
				maxDataWidth = Math.max(1, maxDataWidth);
				break;
			}
			case posPredicate: {
				Integer internalId = register.registerInternal(InternalInformation.PRED_PREFIX + toFullRef(ai));
				if (internalId == null)
					throw new IllegalArgumentException(ai.toString());
				internalDependencies.add(internalId);
				posPred.add(internalId);
				maxDataWidth = Math.max(1, maxDataWidth);
				arg1 = internalId;
				break;
			}
			case isFallingEdge: {
				Integer internalId = register.registerInternal(toFullRef(ai));
				if (internalId == null)
					throw new IllegalArgumentException(ai.toString());
				internalDependencies.add(internalId);
				negEdge = internalId;
				maxDataWidth = Math.max(1, maxDataWidth);
				arg1 = internalId;
				break;
			}
			case isRisingEdge: {
				Integer internalId = register.registerInternal(toFullRef(ai));
				if (internalId == null)
					throw new IllegalArgumentException(ai.toString());
				internalDependencies.add(internalId);
				posEdge = internalId;
				maxDataWidth = Math.max(1, maxDataWidth);
				arg1 = internalId;
				break;
			}
			case writeInternal:
			case loadInternal:
				Integer internalId = register.getInternal(toFullRef(ai));
				if (internalId != null) {
					internalDependencies.add(internalId);
					arg1 = internalId;
				} else {
					internalId = register.getInternal(ai.args[0]);
					if (internalId == null) {
						internalId = register.registerInput(ai.args[0]);
						System.out.println("FluidFrame.toFrame() Registering suspected input:" + ai);
					}
					internalDependencies.add(internalId);
					if (ai.args.length > 1) {
						instr.add(new FastInstruction(ai.instruction, internalId, -1));
						if (ai.args[1].indexOf(':') != -1) {
							String[] split = ai.args[1].split(":");
							i = Instruction.bitAccessSingleRange;
							arg1 = Integer.parseInt(split[0]);
							arg2 = Integer.parseInt(split[1]);
						} else {
							i = Instruction.bitAccessSingle;
							arg1 = Integer.parseInt(ai.args[1]);
						}
					} else {
						arg1 = internalId;
					}
				}
				break;
			case loadConstant:
				BigInteger c = this.constants.get(ai.args[0]);
				maxDataWidth = Math.max(c.bitLength(), maxDataWidth);
				constants.add(c);
				arg1 = constantIdCount++;
				break;
			case cast_uint:
			case cast_int:
				arg1 = Integer.parseInt(ai.args[0]);
				arg2 = Integer.parseInt(ai.args[1]);
				break;
			case concat:
				// System.out.println("FluidFrame.toFrame()" + ai);
				int lWidth = Integer.parseInt(ai.args[0]);
				arg1 = lWidth;
				int rWidth = Integer.parseInt(ai.args[1]);
				arg2 = rWidth;
				maxDataWidth = Math.max(lWidth + rWidth, maxDataWidth);
				break;
			default:
			}
			instr.add(new FastInstruction(i, arg1, arg2));
		}
		for (VariableInformation w : vars.values()) {
			maxDataWidth = Math.max(w.width, maxDataWidth);
		}
		List<Frame> res = new LinkedList<Frame>();
		if (hasInstructions()) {
			int outputId = register.registerInternal(outputName);
			int[] internalDepRes = toIntArray(internalDependencies);
			FastInstruction[] instArray = instr.toArray(new FastInstruction[instr.size()]);
			BigInteger[] consts = constants.toArray(new BigInteger[constants.size()]);
			Frame frame = new Frame(instArray, internalDepRes, toIntArray(posPred), toIntArray(negPred), posEdge, negEdge, outputId, maxDataWidth, maxStackCount, consts, id,
					constant);
			for (FluidFrame ff : references) {
				ff.toFrame(register);
			}
			res.add(frame);
		}
		for (FluidFrame sub : references) {
			res.addAll(sub.toFrame(register));
		}
		return res;
	}

	private String toFullRef(ArgumentedInstruction ai) {
		StringBuilder sb = new StringBuilder();
		sb.append(ai.args[0]);
		for (int i = 1; i < ai.args.length; i++) {
			sb.append('{').append(ai.args[i]).append('}');
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nFrame: ").append(outputName).append('[').append(id).append("]\n");
		if (constants.size() != 0) {
			sb.append("Constants:\n");
			for (Entry<String, BigInteger> entry : constants.entrySet()) {
				sb.append("\t").append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
			}
		}

		for (ArgumentedInstruction ai : instructions) {
			sb.append(ai).append('\n');
		}
		for (FluidFrame refs : references) {
			sb.append(refs);
		}
		return sb.toString();
	}

	public void setName(String string) {
		this.outputName = string;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + id;
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
		FluidFrame other = (FluidFrame) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public boolean hasInstructions() {
		return !instructions.isEmpty();
	}

	public static void resetUniqueIDs() {
		gid.set(0);
	}

	public void addConstant(String refName, BigInteger bVal) {
		constants.put(refName, bVal);
		instructions.add(new ArgumentedInstruction(Instruction.loadConstant, refName));
	}

	public void createPredVar() {
		vars.put(outputName, new VariableInformation(Direction.INTERNAL, outputName, 1, Type.BIT, false));
	}

	public void addVar(VariableInformation information) {
		vars.put(information.name, information);
	}
}
