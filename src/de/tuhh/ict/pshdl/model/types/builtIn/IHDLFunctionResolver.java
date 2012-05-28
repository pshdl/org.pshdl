package de.tuhh.ict.pshdl.model.types.builtIn;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunction function);

	public BigInteger evaluate(HDLFunction function, List<BigInteger> args, HDLEvaluationContext context);
}
