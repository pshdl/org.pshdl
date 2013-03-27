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
package org.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;
import org.pshdl.model.utils.services.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.statement.*;

public class VHDLSimulationFunctions implements IHDLFunctionResolver {

	private static enum SimulationFunctions {
		waitFor, waitUntil, wait, pulse;
		public HDLQualifiedName getName() {
			return HDLQualifiedName.create("pshdl", name());
		}
	}

	@Override
	public HDLTypeInferenceInfo resolve(HDLFunctionCall function) {
		try {
			SimulationFunctions func = SimulationFunctions.valueOf(function.getNameRefName().getLastSegment());
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
		} catch (Exception e) {
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
		String[] res = new String[SimulationFunctions.values().length];
		SimulationFunctions[] values = SimulationFunctions.values();
		for (int i = 0; i < values.length; i++) {
			SimulationFunctions bif = values[i];
			res[i] = bif.name();
		}
		return res;
	}

	@Override
	public VHDLContext toVHDL(HDLFunctionCall function, int pid) {
		SimulationFunctions func = SimulationFunctions.valueOf(function.getNameRefName().getLastSegment());
		switch (func) {
		case wait: {
			VHDLContext res = new VHDLContext();
			res.setNoSensitivity(pid);
			WaitStatement ws = new WaitStatement();
			res.addUnclockedStatement(pid, ws, function);
			return res;
		}
		case waitFor: {
			VHDLContext res = new VHDLContext();
			res.setNoSensitivity(pid);
			HDLEnumRef ref = (HDLEnumRef) function.getParams().get(1);
			Optional<BigInteger> hdlExpression = ConstantEvaluate.valueOf(function.getParams().get(0));
			if (!hdlExpression.isPresent())
				throw new IllegalArgumentException(function.getParams().get(0) + " is not constant");
			WaitStatement ws = new WaitStatement(new PhysicalLiteral(hdlExpression.get().toString(), ref.getVarRefName().getLastSegment()));
			res.addUnclockedStatement(pid, ws, function);
			return res;
		}
		case pulse: {
			ArrayList<HDLExpression> params = function.getParams();
			HDLVariableRef ref = getVarRef(params.get(0));
			VHDLContext res = new VHDLContext();
			res.setNoSensitivity(pid);
			IHDLObject container = function.getContainer();
			HDLAssignment ass = setValue(ref, 0, container);
			res.addUnclockedStatement(pid, VHDLStatementExtension.vhdlOf(ass, pid).getStatement(), ass);
			HDLFunctionCall wait = new HDLFunctionCall().setName(SimulationFunctions.waitFor.getName()).addParams(params.get(1)).addParams(params.get(2)).copyDeepFrozen(container);
			res.addUnclockedStatement(pid, VHDLStatementExtension.vhdlOf(wait, pid).getStatement(), wait);
			ass = setValue(ref, 1, container);
			res.addUnclockedStatement(pid, VHDLStatementExtension.vhdlOf(ass, pid).getStatement(), ass);
			wait = new HDLFunctionCall().setName(SimulationFunctions.waitFor.getName()).addParams(params.get(1)).addParams(params.get(2)).copyDeepFrozen(container);
			res.addUnclockedStatement(pid, VHDLStatementExtension.vhdlOf(wait, pid).getStatement(), wait);
			return res;
		}
		case waitUntil: {
			VHDLContext res = new VHDLContext();
			res.setNoSensitivity(pid);
			WaitStatement ws = new WaitStatement(VHDLExpressionExtension.vhdlOf(function.getParams().get(0)), null);
			res.addUnclockedStatement(pid, ws, function);
			return res;
		}
		}
		return null;
	}

	private HDLAssignment setValue(HDLVariableRef ref, int value, IHDLObject container) {
		HDLManip val = new HDLManip().setCastTo(HDLPrimitive.getBit()).setType(HDLManipType.CAST).setTarget(HDLLiteral.get(value));
		return new HDLAssignment().setLeft(ref).setRight(val).copyDeepFrozen(container);
	}

	private HDLVariableRef getVarRef(HDLExpression hdlExpression) {
		if (hdlExpression instanceof HDLVariableRef) {
			HDLVariableRef ref = (HDLVariableRef) hdlExpression;
			return ref;
		}
		if (hdlExpression instanceof HDLManip) {
			HDLManip manip = (HDLManip) hdlExpression;
			return getVarRef(manip.getTarget());
		}
		return null;
	}

	@Override
	public FunctionCall toVHDLExpression(HDLFunctionCall function) {
		return null;
	}

	@Override
	public FunctionInformation getFunctionInfo(String funcName) {
		return new FunctionInformation(funcName, VHDLSimulationFunctions.class.getSimpleName(), "", "does not return", true, FunctionType.NATIVE);
	}

}
