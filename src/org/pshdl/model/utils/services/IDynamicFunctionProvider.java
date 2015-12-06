package org.pshdl.model.utils.services;

import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.types.builtIn.HDLFunctionImplementation;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.base.Optional;

public interface IDynamicFunctionProvider {
	public HDLQualifiedName[] getDynamicFunctions();

	public Optional<? extends HDLFunctionImplementation> getFunctionFor(HDLFunctionCall call, HDLQualifiedName fqn);
}
