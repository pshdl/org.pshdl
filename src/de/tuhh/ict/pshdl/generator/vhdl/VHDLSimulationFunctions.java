package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class VHDLSimulationFunctions implements IHDLFunctionResolver {

	private static enum SimulationFunctions {
		waitFor, waitUntil, toggle, pulse
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunction function) {
		try {
			SimulationFunctions func = SimulationFunctions.valueOf(function.getName());
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public BigInteger evaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context) {
		return null;
	}

	@Override
	public ValueRange range(HDLFunction function, HDLEvaluationContext context) {
		return null;
	}

	@Override
	public String[] getFunctionNames() {
		String[] res = new String[SimulationFunctions.values().length];
		SimulationFunctions[] values = SimulationFunctions.values();
		for (int i = 0; i < values.length; i++) {
			SimulationFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}

}
