package de.tuhh.ict.pshdl.model.utils.services;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunction function);

	public BigInteger evaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context);

	public ValueRange range(HDLFunction function, HDLEvaluationContext context);
}
