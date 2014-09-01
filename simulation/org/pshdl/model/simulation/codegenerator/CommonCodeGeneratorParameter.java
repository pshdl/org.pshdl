package org.pshdl.model.simulation.codegenerator;

import org.pshdl.interpreter.ExecutableModel;

public class CommonCodeGeneratorParameter {
	public final ExecutableModel em;
	public final int bitWidth;

	@Option(description = "A parameter to specify the amount of work per barrier (use Integer.MAX_VALUE to disable)", optionName = "maxCosts", hasArg = true)
	public int maxCosts = Integer.MAX_VALUE;
	@Option(description = "Disable the removal of all variables that are simply renames of other variables.", optionName = "noPurge", hasArg = false)
	public boolean purgeAliases = true;

	public CommonCodeGeneratorParameter(ExecutableModel em, int bitWidth, int maxCosts, boolean purgeAliases) {
		this.em = em;
		this.bitWidth = bitWidth;
		this.maxCosts = maxCosts;
		this.purgeAliases = purgeAliases;
	}

	public CommonCodeGeneratorParameter(ExecutableModel em, int bitWidth) {
		this.em = em;
		this.bitWidth = bitWidth;
	}
}