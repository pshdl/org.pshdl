package de.tuhh.ict.pshdl.model.types.builtIn;

import de.tuhh.ict.pshdl.model.*;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunction function);
}
