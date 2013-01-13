package de.tuhh.ict.pshdl.model.simulation;

import java.util.*;

import de.tuhh.ict.pshdl.model.simulation.FluidFrame.Instruction;

public class HDLFrameInterpreter {
	private ExecutableModel model;

	protected long storage[];

	private final class EncapsulatedAccess {
		public final int shift;
		public final long mask;
		public final long writeMask;
		public final String name;
		public final int accessIndex;

		public EncapsulatedAccess(String name, int accessIndex) {
			super();
			this.accessIndex = accessIndex;
			int openBrace = name.indexOf('{');
			if (openBrace == -1) {
				this.shift = 0;
				this.mask = 0xFFFFFFFFFFFFFFFFL;
				this.writeMask = 0;
				this.name = name;
			} else {
				this.name = name.substring(0, openBrace);
				String range = name.substring(openBrace + 1, name.length() - 1);
				this.shift = Integer.parseInt(range);
				this.mask = 1;
				this.writeMask = ~(mask << shift);
			}
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("EncapsulatedAccess [shift=").append(shift).append(", mask=").append(mask).append(", writeMask=").append(Long.toHexString(writeMask)).append(", name=")
					.append(name).append(", accessIndex=").append(accessIndex).append("]");
			return builder.toString();
		}

		public void setData(long data) {
			long current = storage[accessIndex];
			current &= writeMask;
			current |= (data & mask) << shift;
			// System.out.println("HDLFrameInterpreter.EncapsulatedAccess.setData()"
			// + name + " to:" + current + " input was:" + data);
			storage[accessIndex] = current;
		}

		public long getData() {
			long current = storage[accessIndex];
			current >>= shift;
			current &= mask;
			return current;
		}
	}

	private EncapsulatedAccess[] internals;
	private EncapsulatedAccess[] input;
	private long[] outputs;
	protected Map<String, Integer> idx = new HashMap<String, Integer>();

	public HDLFrameInterpreter(ExecutableModel model) {
		this.model = model;
		int currentIdx = 0;
		this.internals = new EncapsulatedAccess[model.internals.length];
		for (int i = 0; i < model.internals.length; i++) {
			String in = model.internals[i];
			String basicName = getBasicName(in);
			Integer accessIndex = idx.get(basicName);
			if (accessIndex == null) {
				accessIndex = currentIdx++;
				idx.put(basicName, accessIndex);
			}
			internals[i] = new EncapsulatedAccess(in, accessIndex);
		}
		this.input = new EncapsulatedAccess[model.inputs.length];
		for (int i = 0; i < model.inputs.length; i++) {
			String in = model.inputs[i];
			String basicName = getBasicName(in);
			Integer accessIndex = idx.get(basicName);
			if (accessIndex == null) {
				accessIndex = currentIdx++;
				idx.put(basicName, accessIndex);
			}
			input[i] = new EncapsulatedAccess(in, accessIndex);
		}
		this.outputs = new long[model.outputs.length];
		storage = new long[currentIdx];
	}

	private String getBasicName(String name) {
		int openBrace = name.indexOf('{');
		if (openBrace != -1) {
			name = name.substring(0, openBrace);
		}
		return name;
	}

	public long[] run(long... args) {
		storage[idx.get("de.tuhh.ict.bitAdder.a")] = args[0];
		storage[idx.get("de.tuhh.ict.bitAdder.b")] = args[1];
		long stack[] = new long[model.maxStackDepth];
		Instruction[] values = Instruction.values();
		for (Frame f : model.frames) {
			int stackPos = -1;
			int execPos = 0;
			byte[] inst = f.instructions;
			do {
				Instruction instruction = values[inst[execPos] & 0xff];
				// System.out.println("HDLFrameInterpreter.run()" +
				// instruction);
				switch (instruction) {
				case and: {
					long b = stack[stackPos--];
					long a = stack[stackPos];
					stack[stackPos] = a & b;
					break;
				}
				case arith_neg: {
					long a = stack[stackPos];
					stack[stackPos] = -a;
					break;
				}
				case bitAccess:
					break;
				case bit_neg: {
					long a = stack[stackPos];
					stack[stackPos] = ~a;
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
				case ifCall:
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
				case mod: {
					long b = stack[stackPos--];
					long a = stack[stackPos];
					stack[stackPos] = a % b;
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
				case pow:
					break;
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
				}
				execPos++;
			} while (execPos < inst.length);
			if (f.isInternal)
				internals[f.outputId & 0xff].setData(stack[0]);
			else
				outputs[f.outputId & 0xff] = stack[0];
		}
		return new long[] { outputs[0], storage[idx.get("de.tuhh.ict.bitAdder.sum")] };
	}
}
