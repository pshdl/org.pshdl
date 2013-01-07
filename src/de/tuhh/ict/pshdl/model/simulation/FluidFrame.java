package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.*;

public class FluidFrame {
	private static AtomicInteger gid = new AtomicInteger();
	public final int id;

	public FluidFrame() {
		this(null);
	}

	public FluidFrame(String outputName) {
		this.id = gid.incrementAndGet();
		if (outputName != null)
			this.outputName = outputName;
		else
			this.outputName = Integer.toString(id);
	}

	public static enum Instruction {
		loadInput(false, -1, "Loads a value from an input. The first value is the input name, then followed by bit access indexes"), //
		loadStore(false, 1, "Loads a value from the internal storage"), //
		// store(false, 1, "Stores a value to the internal storage"), //
		callFrame(false, 1, "Calls a frame"), //
		// switchCall(false, -1,
		// "Calls a frame depending on the value. The value format is: value, frame id"),
		// //
		bitAccess(false, -1, "Use the bits given as operands"), //
		ifCall(false, 2, "Calls a frame depending on the value. The value format is: true frame, false frame id"), //
		concat(false, 0, "Concatenate bits"), //
		and(false, 0, "A binary & operation"), //
		logiAnd(false, 0, "A logical && operation"), //
		or(false, 0, "A binary | operation"), //
		logiOr(false, 0, "A logical || operation"), //
		xor(false, 0, "A binary ^ operation"), //
		div(false, 0, "An arithmetic / operation"), //
		minus(false, 0, "An arithmetic - operation"), //
		mod(false, 0, "An arithmetic % operation"), //
		mul(false, 0, "An arithmetic * operation"), //
		plus(false, 0, "An arithmetic + operation"), //
		pow(false, 0, "An arithmetic ** operation"), //
		sll(false, 0, "A shift << operation"), //
		sra(false, 0, "A shift >> operation"), //
		srl(false, 0, "A shift >>> operation"), //
		eq(false, 0, "An equality == operation"), //
		greater(false, 0, "An equality > operation"), //
		greater_eq(false, 0, "An equality >= operation"), //
		not_eq(false, 0, "An equality != operation"), //
		less(false, 0, "An equality < operation"), //
		less_eq(false, 0, "An equality <= operation"), //
		arith_neg(false, 0, "Arithmetically negates"), //
		bit_neg(false, 0, "Bit inverts"), //
		logic_neg(false, 0, "Logically negates"), //
		cast_int(false, 1, "Re-interprets the operand as int and resizes it"), //
		cast_uint(false, 1, "Re-interprets the operand as uint and resizes it"), //

		;
		final int argCount;
		final boolean immediate;
		final String description;

		Instruction(boolean immediate, int argCount, String desc) {
			this.argCount = argCount;
			this.immediate = immediate;
			this.description = desc;
		}
	}

	public static class ArgumentedInstruction {
		public final Instruction instruction;
		public final String args[];

		public ArgumentedInstruction(Instruction instruction, String... args) {
			super();
			this.instruction = instruction;
			this.args = args;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(instruction);
			if (args.length != 0)
				builder.append(Arrays.toString(args));
			return builder.toString();
		}
	}

	public final Map<String, FluidFrame> references = new HashMap<String, FluidFrame>();
	public final Map<String, BigInteger> store = new HashMap<String, BigInteger>();
	public final Set<String> inputs = new LinkedHashSet<String>();
	public final String outputName;

	public final LinkedList<ArgumentedInstruction> instructions = new LinkedList<ArgumentedInstruction>();

	public FluidFrame append(FluidFrame frame) {
		// Don't copy references
		inputs.addAll(frame.inputs);
		store.putAll(frame.store);
		instructions.addAll(frame.instructions);
		return this;
	}

	public void add(Instruction inst) {
		instructions.add(new ArgumentedInstruction(inst));
	}

	public void addReferencedFrame(FluidFrame frame) {
		references.put(frame.outputName, frame);
	}

	public void addInput(String string) {
		inputs.add(string);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nFrame: ").append(outputName).append('[').append(id).append("]\n");
		if (store.size() != 0) {
			sb.append("Store:\n");
			for (Entry<String, BigInteger> entry : store.entrySet()) {
				sb.append("\t").append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
			}
		}
		if (inputs.size() != 0) {
			sb.append("Inputs:\t");
			boolean first = true;
			for (String input : inputs) {
				if (!first)
					sb.append(", ");
				first = false;
				sb.append(input);
			}
			sb.append('\n');
		}
		for (ArgumentedInstruction ai : instructions) {
			sb.append(ai).append('\n');
		}
		for (FluidFrame refs : references.values()) {
			sb.append(refs);
		}
		return sb.toString();
	}

	private static class FrameRegister {
		public final Map<String, Byte> inputIds = new HashMap<String, Byte>();
		private int inIdCounter = 0;
		public final Map<String, Byte> outputIds = new HashMap<String, Byte>();
		private int outIdCounter = 0;
		public final Map<String, Byte> frameIds = new HashMap<String, Byte>();
		private int frameIdCounter = 0;
		public final List<Frame> frames = new ArrayList<Frame>();

		public Byte registerInput(String in) {
			if (inputIds.get(in) == null) {
				inputIds.put(in, (byte) inIdCounter++);
			}
			return inputIds.get(in);
		}

		public Byte registerOutput(String in) {
			if (outputIds.get(in) == null) {
				outputIds.put(in, (byte) outIdCounter++);
			}
			return outputIds.get(in);
		}

		public Byte registerFrame(String in) {
			if (frameIds.get(in) == null) {
				frameIds.put(in, (byte) frameIdCounter++);
			}
			return frameIds.get(in);
		}

		public void addFrame(Frame frame) {
			frames.add(frame);
		}

		public Byte getFrame(String string) {
			return frameIds.get(string);
		}
	}

	public ExecutableModel getExecutable() {
		FrameRegister register = new FrameRegister();
		for (FluidFrame entry : references.values()) {
			entry.registerFrame(register);
		}
		for (FluidFrame entry : references.values()) {
			entry.toFrame(register);
		}
		return null;
	}

	private void registerFrame(FrameRegister register) {
		register.registerFrame(outputName);
		for (FluidFrame entry : references.values()) {
			entry.registerFrame(register);
		}
	}

	private Frame toFrame(FrameRegister register) {
		List<Byte> instr = new LinkedList<Byte>();
		List<Byte> dependencies = new LinkedList<Byte>();
		List<BigInteger> store = new LinkedList<BigInteger>();
		int stackCount = 0;
		int maxStackCount = -1;
		int storeIdCount = 0;
		for (ArgumentedInstruction ai : instructions) {
			int ordinal = ai.instruction.ordinal();
			instr.add((byte) (ordinal & 0xff));
			switch (ai.instruction) {
			case loadInput:
				stackCount++;
				maxStackCount = Math.max(maxStackCount, stackCount);
				Byte inputId = register.registerInput(ai.args[0]);
				dependencies.add(inputId);
				instr.add(inputId);
				break;
			case loadStore:
				store.add(new BigInteger(ai.args[0]));
				instr.add((byte) storeIdCount++);
				break;
			case callFrame:
				instr.add(register.getFrame(ai.args[0]));
				break;
			default:
				stackCount--;
			}
		}
		Byte outputId = register.registerOutput(outputName);
		byte[] instrRes = toByteArray(instr);
		byte[] depRes = toByteArray(dependencies);
		// XXX determine maxBitWidth
		Frame frame = new Frame(instrRes, depRes, outputId, 32, maxStackCount, store.toArray(new BigInteger[store.size()]), outputName);
		register.addFrame(frame);
		for (FluidFrame ff : references.values()) {
			ff.toFrame(register);
		}
		return frame;
	}

	private byte[] toByteArray(List<Byte> instr) {
		byte[] instrRes = new byte[instr.size()];
		int pos = 0;
		for (Byte i : instr) {
			instrRes[pos++] = i;
		}
		return instrRes;
	}
}
