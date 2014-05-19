/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.types.builtIn;

import java.math.BigInteger;
import java.util.List;

import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;
import org.pshdl.model.utils.services.IHDLFunctionResolver;

import com.google.common.base.Optional;
import com.google.common.collect.Range;

public class TestbenchFunctions implements IHDLFunctionResolver {

	public static enum SimulationFunctions {
		waitFor, waitUntil, wait, pulse;
		public HDLQualifiedName getName() {
			return HDLQualifiedName.create("pshdl", name());
		}
	}

	public static HDLFunction WAIT = createWait();

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function) {
		try {
			final SimulationFunctions func = SimulationFunctions.valueOf(function.getNameRefName().getLastSegment());
			switch (func) {
			case wait:
				if (function.getParams().size() == 0)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBool());
				break;
			case waitFor:
				if (function.getParams().size() == 2)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getUint(), PSHDLLib.TIMEUNIT);
				break;
			case waitUntil:
				if (function.getParams().size() == 1)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBool());
				break;
			case pulse:
				if (function.getParams().size() == 3)
					return new HDLTypeInferenceInfo(HDLPrimitive.getBool(), HDLPrimitive.getBit(), HDLPrimitive.getUint(), PSHDLLib.TIMEUNIT);
				break;
			}
		} catch (final Exception e) {
		}
		return null;
	}

	private static HDLFunction createPulse() {
		return new HDLNativeFunction()
				.setSimOnly(true)
				.setName(SimulationFunctions.pulse.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.REG_BIT).setName(new HDLVariable().setName("toggleExpression")).setRw(RWType.WRITE))
				.addArgs(new HDLFunctionParameter().setType(Type.ANY_UINT).setName(new HDLVariable().setName("amount")).setRw(RWType.READ))
				.addArgs(
						new HDLFunctionParameter().setType(Type.ENUM).setEnumSpec(new HDLQualifiedName("pshdl.TimeUnit")).setName(new HDLVariable().setName("timeUnit"))
								.setRw(RWType.READ));
	}

	private static HDLFunction createWait() {
		return new HDLNativeFunction().setSimOnly(true).setName(SimulationFunctions.wait.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setRw(RWType.RETURN));
	}

	private static HDLFunction createWaitFor() {
		return new HDLNativeFunction()
				.setSimOnly(true)
				.setName(SimulationFunctions.waitFor.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setName(new HDLVariable().setName("expression")).setRw(RWType.READ))
				.addArgs(new HDLFunctionParameter().setType(Type.ANY_UINT).setName(new HDLVariable().setName("amount")).setRw(RWType.READ))
				.addArgs(
						new HDLFunctionParameter().setType(Type.ENUM).setEnumSpec(new HDLQualifiedName("pshdl.TimeUnit")).setName(new HDLVariable().setName("timeUnit"))
								.setRw(RWType.READ));
	}

	private static HDLFunction createWaitUntil() {
		return new HDLNativeFunction().setSimOnly(true).setName(SimulationFunctions.waitUntil.name())
				.setReturnType(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(Type.BOOL_TYPE).setName(new HDLVariable().setName("expression")).setRw(RWType.READ));
	}

	@Override
	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		return Optional.absent();
	}

	@Override
	public Optional<Range<BigInteger>> range(HDLFunctionCall function, HDLEvaluationContext context) {
		return Optional.absent();
	}

	@Override
	public String[] getFunctionNames() {
		final String[] res = new String[SimulationFunctions.values().length];
		final SimulationFunctions[] values = SimulationFunctions.values();
		for (int i = 0; i < values.length; i++) {
			final SimulationFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		return new FunctionInformation(funcName, TestbenchFunctions.class.getSimpleName(), "", "does not return", );
	}
}
