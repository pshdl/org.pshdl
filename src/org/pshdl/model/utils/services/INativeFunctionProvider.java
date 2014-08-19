package org.pshdl.model.utils.services;

import org.pshdl.model.types.builtIn.HDLFunctionImplementation;

public interface INativeFunctionProvider {
	public HDLFunctionImplementation[] getStaticFunctions();
}
