package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.plb.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.*;

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

	public static HDLGenerationInfo getImplementation(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null) {
			return generator.getImplementation(hdl);
		}
		return null;
	}

}
