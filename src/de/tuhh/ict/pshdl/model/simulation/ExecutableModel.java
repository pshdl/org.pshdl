package de.tuhh.ict.pshdl.model.simulation;

public class ExecutableModel {
	public final Frame[] frames;
	public String[] inputs;
	public String[] outputs;
	public int maxDataWidth;
	public int maxStackDepth;

	public ExecutableModel(Frame[] frames, String[] inputs, String[] outputs, int maxDataWidth, int maxStackDepth) {
		super();
		this.frames = frames;
		this.inputs = inputs;
		this.outputs = outputs;
		this.maxDataWidth = maxDataWidth;
		this.maxStackDepth = maxStackDepth;
	}

}
