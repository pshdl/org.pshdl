package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;

public class Frame {

	public final byte[] instructions;
	public final byte[] dependencies;
	public final BigInteger[] store;
	public final int outputId;
	public final int maxDataWidth;
	public final int maxStackDepth;
	public final String id;

	public Frame(byte[] instructions, byte[] dependencies, int outputId, int maxDataWidth, int maxStackDepth, BigInteger[] store, String id) {
		super();
		this.store = store;
		this.instructions = instructions;
		this.dependencies = dependencies;
		this.outputId = outputId;
		this.maxDataWidth = maxDataWidth;
		this.maxStackDepth = maxStackDepth;
		this.id = id;
	}
}
