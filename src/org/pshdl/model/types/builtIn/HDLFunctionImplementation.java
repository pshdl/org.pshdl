package org.pshdl.model.types.builtIn;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLType;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.utils.ModificationSet;
import org.pshdl.model.validation.Problem;

import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public interface HDLFunctionImplementation {
	public static abstract class HDLDefaultFunctionImpl implements HDLFunctionImplementation {

		private final boolean allowInHDL;
		private final boolean allowInExecutable;

		public HDLDefaultFunctionImpl(boolean allowInHDL, boolean allowInExecutable) {
			super();
			this.allowInHDL = allowInHDL;
			this.allowInExecutable = allowInExecutable;
		}

		@Override
		public boolean allowInHDL() {
			return allowInHDL;
		}

		@Override
		public boolean allowInExecutable() {
			return allowInExecutable;
		}

		@Override
		public Map<String, String> getArgumentDocumentation(HDLFunction function) {
			return Collections.emptyMap();
		}

		@Override
		public String getReturnDocumentation(HDLFunction function) {
			return null;
		}

		@Override
		public void transform(HDLFunctionCall call, HDLEvaluationContext context, ModificationSet ms) {
		}

		@Override
		public Set<Problem> validateCall(HDLFunctionCall call, HDLEvaluationContext context) {
			return Sets.newLinkedHashSet();
		}

		@Override
		public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.absent();
		}

		@Override
		public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.absent();
		}

		@Override
		public Optional<HDLExpression> simplifyCall(HDLFunctionCall call, HDLEvaluationContext context) {
			return Optional.absent();
		}

		@Override
		public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context) {
			throw new UnsupportedOperationException("Need to be implemented for:" + this);
		}

	}

	public String getDocumentation(HDLFunction function);

	public String getReturnDocumentation(HDLFunction function);

	public Map<String, String> getArgumentDocumentation(HDLFunction function);

	public boolean allowInHDL();

	public boolean allowInExecutable();

	public HDLFunction[] signatures();

	public void transform(HDLFunctionCall call, HDLEvaluationContext context, ModificationSet ms);

	public Set<Problem> validateCall(HDLFunctionCall call, HDLEvaluationContext context);

	public Optional<Range<BigInteger>> getRange(HDLFunctionCall call, HDLEvaluationContext context);

	public Optional<BigInteger> getConstantValue(HDLFunctionCall call, HDLEvaluationContext context);

	public Optional<HDLExpression> simplifyCall(HDLFunctionCall call, HDLEvaluationContext context);

	public Optional<? extends HDLType> specifyReturnType(HDLFunction function, HDLFunctionCall call, HDLEvaluationContext context);

}
