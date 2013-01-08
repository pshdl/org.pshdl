package de.tuhh.ict.pshdl.model.simulation;

import java.util.*;

public class ExecutableModel {
	public final int maxDataWidth;
	public final int maxStackDepth;
	public final Frame[] frames;
	public final String[] inputs;
	public final String[] outputs;
	public final String[] internals;

	public ExecutableModel(Frame[] frames, String[] inputs, String[] outputs, String[] internals, int maxDataWidth, int maxStackDepth) {
		super();
		this.frames = frames;
		this.inputs = inputs;
		this.outputs = outputs;
		this.internals = internals;
		this.maxDataWidth = maxDataWidth;
		this.maxStackDepth = maxStackDepth;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExecutableModel [maxDataWidth=").append(maxDataWidth).append(", maxStackDepth=").append(maxStackDepth).append(", ");
		if (frames != null)
			builder.append("frames=\n").append(Arrays.toString(frames)).append(", ");
		if (inputs != null)
			builder.append("inputs=").append(Arrays.toString(inputs)).append(", ");
		if (outputs != null)
			builder.append("outputs=").append(Arrays.toString(outputs));
		builder.append("]");
		return builder.toString();
	}

	public String toDotFile() {
		StringBuilder sb = new StringBuilder();
		sb.append("digraph ExecutableModel {\n");
		for (int i = 0; i < inputs.length; i++) {
			String input = inputs[i];
			sb.append("node [shape = box, color=green, label=\"").append(input).append("\"]");
			sb.append(" in").append(i);
			sb.append(";\n");
		}
		for (int i = 0; i < outputs.length; i++) {
			String input = outputs[i];
			sb.append("node [shape = box, color=red, label=\"").append(input).append("\"]");
			sb.append(" out").append(i);
			sb.append(";\n");
		}
		for (int i = 0; i < internals.length; i++) {
			String input = internals[i];
			sb.append("node [shape = box, color=orange, label=\"").append(input).append("\"]");
			sb.append(" int").append(i);
			sb.append(";\n");
		}
		for (int i = 0; i < frames.length; i++) {
			sb.append("node [shape = circle, color=black, label=\"").append(i).append("\"]");
			sb.append(" frame").append(i);
			sb.append(";\n");
		}
		for (int i = 0; i < frames.length; i++) {
			Frame input = frames[i];
			String frameId = "frame" + i;
			for (byte in : input.inputDependencies) {
				sb.append("in").append(in).append(" -> ").append(frameId).append(";\n");
			}
			for (byte in : input.internalDependencies) {
				sb.append("int").append(in).append(" -> ").append(frameId).append(";\n");
			}
			sb.append(frameId).append(" -> ");
			if (input.isInternal)
				sb.append("int");
			else
				sb.append("out");
			sb.append(input.outputId).append(";\n");
		}
		sb.append("}");
		return sb.toString();
	}
}
