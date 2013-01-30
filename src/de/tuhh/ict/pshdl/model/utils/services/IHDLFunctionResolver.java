package de.tuhh.ict.pshdl.model.utils.services;

import java.math.*;
import java.util.*;

import com.google.common.collect.*;

import de.tuhh.ict.pshdl.generator.vhdl.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.upb.hni.vmagic.expression.*;

public interface IHDLFunctionResolver {
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function);

	public BigInteger evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context);

	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context);

	public String[] getFunctionNames();

	public VHDLContext toVHDL(HDLFunctionCall function, int pid);

	public Expression<?> toVHDLExpression(HDLFunctionCall function);

	public FunctionInformation getFunctionInfo(String funcName);
}
