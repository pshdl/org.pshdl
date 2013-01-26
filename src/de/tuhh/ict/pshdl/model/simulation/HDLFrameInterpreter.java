package de.tuhh.ict.pshdl.model.simulation;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.*;

import de.tuhh.ict.pshdl.model.simulation.FluidFrame.Instruction;

public final class HDLFrameInterpreter {
	private final ExecutableModel model;

	protected final long storage[], storage_prev[];

	protected static final Pattern aiFormatName = Pattern.compile("(.*?)(?:\\{(?:(\\d+)(?:\\:(\\d+))?)\\})?");

	private final class EncapsulatedAccess {
		public final int shift;
		public final long mask;
		public final long writeMask;
		public final String name;
		public final int accessIndex;
		public final boolean prev;

		public EncapsulatedAccess(String name, int accessIndex, boolean prev) {
			super();
			this.accessIndex = accessIndex;
			this.prev = prev;
			Matcher matcher = aiFormatName.matcher(name);
			if (matcher.matches()) {
				this.name = matcher.group(1);
				if (matcher.group(2) == null) {
					int width = model.getWidth(name);
					this.shift = 0;
					if (width == 64)
						this.mask = 0xFFFFFFFFFFFFFFFFL;
					else
						this.mask = (1l << width) - 1;
					this.writeMask = 0;
				} else if (matcher.group(3) != null) {
					int start = Integer.parseInt(matcher.group(2));
					int end = Integer.parseInt(matcher.group(3));
					int actualWidth = (start - end) + 1;
					this.shift = end;
					this.mask = (1l << actualWidth) - 1;
					this.writeMask = ~(mask << shift);
				} else {
					this.shift = Integer.parseInt(matcher.group(2));
					this.mask = 1;
					this.writeMask = ~(mask << shift);
				}
			} else
				throw new IllegalArgumentException("Name:" + name + " is not valid!");
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("EncapsulatedAccess [shift=").append(shift).append(", mask=").append(mask).append(", writeMask=").append(Long.toHexString(writeMask)).append(", name=")
					.append(name).append(", accessIndex=").append(accessIndex).append("]");
			return builder.toString();
		}

		public void setData(long data) {
			long initial;
			initial = storage[accessIndex];
			long current = initial & writeMask;
			storage[accessIndex] = current | ((data & mask) << shift);
		}

		public long getData() {
			if (prev) {
				return (storage_prev[accessIndex] >> shift) & mask;
			}
			return (storage[accessIndex] >> shift) & mask;
		}
	}

	private final EncapsulatedAccess[] internals, internals_prev;
	private final EncapsulatedAccess[] input, input_prev;
	private final EncapsulatedAccess[] outputs;
	private final int[] regIndex, regIndexTarget;
	private final Map<String, Integer> idx = new TreeMap<String, Integer>();
	private int deltaCycle = 0;

	public HDLFrameInterpreter(ExecutableModel model) {
		this.model = model;
		int currentIdx = 0;
		this.internals = new EncapsulatedAccess[model.internals.length];
		this.internals_prev = new EncapsulatedAccess[model.internals.length];
		for (int i = 0; i < model.internals.length; i++) {
			String in = model.internals[i];
			String basicName = getBasicName(in);
			Integer accessIndex = idx.get(basicName);
			if (accessIndex == null) {
				accessIndex = currentIdx++;
				idx.put(basicName, accessIndex);
			}
			internals[i] = new EncapsulatedAccess(in, accessIndex, false);
			internals_prev[i] = new EncapsulatedAccess(in, accessIndex, true);
		}
		this.input = new EncapsulatedAccess[model.inputs.length];
		this.input_prev = new EncapsulatedAccess[model.inputs.length];
		for (int i = 0; i < model.inputs.length; i++) {
			String in = model.inputs[i];
			String basicName = getBasicName(in);
			Integer accessIndex = idx.get(basicName);
			if (accessIndex == null) {
				accessIndex = currentIdx++;
				idx.put(basicName, accessIndex);
			}
			input[i] = new EncapsulatedAccess(in, accessIndex, false);
			input_prev[i] = new EncapsulatedAccess(in, accessIndex, true);
		}
		this.outputs = new EncapsulatedAccess[model.outputs.length];
		for (int i = 0; i < model.outputs.length; i++) {
			String in = model.outputs[i];
			String basicName = getBasicName(in);
			Integer accessIndex = idx.get(basicName);
			if (accessIndex == null) {
				accessIndex = currentIdx++;
				idx.put(basicName, accessIndex);
			}
			outputs[i] = new EncapsulatedAccess(in, accessIndex, false);
		}
		regIndex = new int[model.registerOutputs.length];
		regIndexTarget = new int[model.registerOutputs.length];
		for (int i = 0; i < model.registerOutputs.length; i++) {
			int ridx = model.registerOutputs[i];
			String name = model.internals[ridx];
			regIndex[i] = idx.get(name);
			regIndexTarget[i] = idx.get(ExecutableModel.stripReg(name));
		}
		storage = new long[currentIdx];
		storage_prev = new long[currentIdx];
	}

	private String getBasicName(String name) {
		int openBrace = name.indexOf('{');
		if (openBrace != -1) {
			name = name.substring(0, openBrace);
		}
		return name;
	}

	public void setInput(String name, long value) {
		Integer integer = idx.get(name);
		if (integer == null)
			throw new IllegalArgumentException("Could not find an input named:" + name);
		storage[integer] = value;
	}

	public long getOutput(String name) {
		Integer integer = idx.get(name);
		if (integer == null)
			throw new IllegalArgumentException("Could not find an input named:" + name);
		return storage[integer];
	}

	private static final Instruction[] values = Instruction.values();

	public void run() {
		boolean regUpdated = false;
		deltaCycle++;
		do {
			regUpdated = false;
			long stack[] = new long[model.maxStackDepth];
			nextFrame: for (Frame f : model.frames) {
				int stackPos = -1;
				int execPos = 0;
				byte[] inst = f.instructions;
				do {
					Instruction instruction = values[inst[execPos] & 0xff];
					switch (instruction) {
					case noop:
						break;
					case and: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a & b;
						break;
					}
					case arith_neg: {
						stack[stackPos] = -stack[stackPos];
						break;
					}
					case bitAccess:
						break;
					case bit_neg: {
						stack[stackPos] = ~stack[stackPos];
						break;
					}
					case callFrame:
						break;
					case cast_int:
						break;
					case cast_uint:
						break;
					case concat:
						break;
					case const0:
						stack[++stackPos] = 0;
						break;
					case div: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a / b;
						break;
					}
					case eq:
						break;
					case greater:
						break;
					case greater_eq:
						break;
					case less:
						break;
					case less_eq:
						break;
					case loadConstant:
						stack[++stackPos] = f.constants[inst[++execPos] & 0xff].longValue();
						break;
					case loadInput:
						stack[++stackPos] = input[inst[++execPos] & 0xff].getData();
						break;
					case loadInternal:
						stack[++stackPos] = internals[inst[++execPos] & 0xff].getData();
						break;
					case logiAnd:
						break;
					case logiOr:
						break;
					case logic_neg:
						break;
					case minus: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a - b;
						break;
					}
					case mul: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a * b;
						break;
					}
					case not_eq:
						break;
					case or: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a | b;
						break;
					}
					case plus: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a + b;
						break;
					}
					case sll: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a << b;
						break;
					}
					case sra: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a >> b;
						break;
					}
					case srl: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a >>> b;
						break;
					}
					case xor: {
						long b = stack[stackPos--];
						long a = stack[stackPos];
						stack[stackPos] = a ^ b;
						break;
					}
					case isFallingEdgeInput: {
						int off = inst[++execPos] & 0xFF;
						long curr = input[off].getData();
						long prev = input_prev[off].getData();
						if (f.lastUpdate == deltaCycle)
							continue nextFrame;
						else if ((prev == 1) && (curr == 0)) {
							f.lastUpdate = deltaCycle;
							regUpdated = true;
						} else
							continue nextFrame;
						break;
					}
					case isFallingEdgeInternal: {
						int off = inst[++execPos] & 0xFF;
						long curr = internals[off].getData() & 1;
						long prev = internals_prev[off].getData() & 1;
						if (f.lastUpdate == deltaCycle)
							continue nextFrame;
						else if ((prev == 1) && (curr == 0)) {
							f.lastUpdate = deltaCycle;
							regUpdated = true;
						} else
							continue nextFrame;
						break;
					}
					case isRisingEdgeInput: {
						int off = inst[++execPos] & 0xFF;
						long curr = input[off].getData() & 1;
						long prev = input_prev[off].getData() & 1;
						if (f.lastUpdate == deltaCycle)
							continue nextFrame;
						else if ((prev == 0) && (curr == 1)) {
							f.lastUpdate = deltaCycle;
							regUpdated = true;
						} else
							continue nextFrame;
						break;
					}
					case isRisingEdgeInternal: {
						int off = inst[++execPos] & 0xFF;
						long curr = internals[off].getData() & 1;
						long prev = internals_prev[off].getData() & 1;
						if (f.lastUpdate == deltaCycle)
							continue nextFrame;
						else if ((prev == 0) && (curr == 1)) {
							f.lastUpdate = deltaCycle;
							regUpdated = true;
						} else
							continue nextFrame;
						break;
					}
					}
					execPos++;
				} while (execPos < inst.length);
				if (f.isInternal)
					internals[f.outputId & 0xff].setData(stack[0]);
				else
					outputs[f.outputId & 0xff].setData(stack[0]);
			}
			if (regUpdated) {
				for (int i = 0; i < regIndex.length; i++) {
					long oldValue = storage[regIndex[i]];
					storage[regIndexTarget[i]] = oldValue;
				}
			}
		} while (regUpdated);
		System.arraycopy(storage, 0, storage_prev, 0, storage.length);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Current Cycle:" + deltaCycle + "\n");
		for (Entry<String, Integer> e : idx.entrySet()) {
			sb.append('\t').append(e.getKey()).append("=").append(storage[e.getValue()]).append('\n');
		}
		return sb.toString();
	}
}
