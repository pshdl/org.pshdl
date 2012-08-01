package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.*;

public class HDLGenerators {
	private static Map<String, IHDLGenerator> generators;

	public static void init(IServiceProvider sp) {
		generators = new HashMap<String, IHDLGenerator>();
		for (IHDLGenerator gen : sp.getAllGenerators()) {
			for (String name : gen.getNames())
				generators.put(name, gen);
		}
	}

	public static HDLInterface getInterface(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			return generator.getInterface(hdl);
		}
		return null;
	}

	public static HDLGenerationInfo getImplementation(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			return generator.getImplementation(hdl);
		}
		return null;
	}

}
