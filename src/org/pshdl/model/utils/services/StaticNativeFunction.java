package org.pshdl.model.utils.services;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.evaluation.HDLEvaluationContext;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public interface StaticNativeFunction {

	public String getDescription();

	public Map<String, String> getArgumentDescription();

	public String getReturnDescription();

	public HDLNativeFunction getSignature();

	/**
	 * Returns a constant value for the native function
	 *
	 * @param function
	 * @param args
	 * @param context
	 * @return
	 */
	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context);

	/**
	 * Return a value range for the native function
	 *
	 * @param function
	 * @param context
	 * @return
	 */
	public Optional<Range<BigInteger>> range(HDLFunctionCall function, HDLEvaluationContext context);
}