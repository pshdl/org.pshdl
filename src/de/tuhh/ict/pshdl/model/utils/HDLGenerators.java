package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.plb.*;

public class HDLGenerators {
	// XXX Use service loaders (java.util.ServiceLoader) for non-OSGI contexts
	private static Map<String, IHDLGenerator> generators = new HashMap<String, IHDLGenerator>();

	static {
		generators.put("plb", new PLBGenerator());
	}

	public static HDLInterface getInterface(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			return generator.getInterface(hdl);
		}
		return null;
	}

}
