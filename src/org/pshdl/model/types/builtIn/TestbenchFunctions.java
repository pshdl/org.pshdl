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

import java.math.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
import org.pshdl.model.utils.services.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class TestbenchFunctions implements IHDLFunctionResolver {

	public static enum SimulationFunctions {
		waitFor, waitUntil, wait, pulse;
		public HDLQualifiedName getName() {
			return HDLQualifiedName.create("pshdl", name());
		}
	}

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

	@Override
	public Optional<BigInteger> evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		return Optional.absent();
	}

	@Override
	public Range<BigInteger> range(HDLFunctionCall function, HDLEvaluationContext context) {
		return null;
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
		return new FunctionInformation(funcName, TestbenchFunctions.class.getSimpleName(), "", "does not return", true, FunctionType.NATIVE);
	}

}
