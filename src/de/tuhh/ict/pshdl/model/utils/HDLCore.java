package de.tuhh.ict.pshdl.model.utils;

import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.*;

public class HDLCore {
	private static final CompilerInformation info = new CompilerInformation("0.1a");

	public static void init(IServiceProvider sp) {
		info.registeredAnnotations.clear();
		info.registeredFunctions.clear();
		info.registeredGenerators.clear();
		HDLFunctions.init(info, sp);
		HDLAnnotations.init(info, sp);
		HDLGenerators.init(info, sp);
		HDLValidator.init(info, sp);
	}

	public static CompilerInformation getCompilerInformation() {
		return info;
	}
}
