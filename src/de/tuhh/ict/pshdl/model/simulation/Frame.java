package de.tuhh.ict.pshdl.model.simulation;

import java.io.*;
import java.math.*;
import java.util.*;

public class Frame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1690021519637432408L;
	public final byte[] instructions;
	public final byte[] inputDependencies;
	public final byte[] internalDependencies;
	public final BigInteger[] constants;
	public final int outputId;
	public final int maxDataWidth;
	public final int maxStackDepth;
	public final String id;
	public final boolean isInternal;

	public Frame(byte[] instructions, byte[] inputDependencies, byte[] internalDependencies, int outputId, int maxDataWidth, int maxStackDepth, BigInteger[] constants, String id,
			boolean isInternal) {
		super();
		this.constants = constants;
		this.instructions = instructions;
		this.inputDependencies = inputDependencies;
		this.internalDependencies = internalDependencies;
		this.outputId = outputId;
		this.maxDataWidth = maxDataWidth;
		this.maxStackDepth = maxStackDepth;
		this.id = id;
		this.isInternal = isInternal;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Frame [");
		if (instructions != null)
			builder.append("instructions=").append(Arrays.toString(instructions)).append(", ");
		if (inputDependencies != null)
			builder.append("inputDependencies=").append(Arrays.toString(inputDependencies)).append(", ");
		if (internalDependencies != null)
			builder.append("internalDependencies=").append(Arrays.toString(internalDependencies)).append(", ");
		if (constants != null)
			builder.append("constants=").append(Arrays.toString(constants)).append(", ");
		if (isInternal)
			builder.append("internal");
		builder.append("outputId=").append(outputId).append(", maxDataWidth=").append(maxDataWidth).append(", maxStackDepth=").append(maxStackDepth).append(", ");
		if (id != null)
			builder.append("id=").append(id);
		builder.append("]\n");
		return builder.toString();
	}
}
