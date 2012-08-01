package de.tuhh.ict.pshdl.model.utils;

import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class HDLCore {
	public static void init(IServiceProvider sp) {
		HDLFunctions.init(sp);
		HDLAnnotations.init(sp);
		HDLGenerators.init(sp);
	}
}
