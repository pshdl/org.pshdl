package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation.*;
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
	public BigInteger evaluate(HDLFunctionCall function, List<BigInteger> args, HDLEvaluationContext context) {
		return null;
	}

	@Override
	public ValueRange range(HDLFunctionCall function, HDLEvaluationContext context) {
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
			BigInteger hdlExpression = function.getParams().get(0).constantEvaluate(null);
			WaitStatement ws = new WaitStatement(new PhysicalLiteral(hdlExpression.toString(), ref.getVarRefName().getLastSegment()));
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
			res.addUnclockedStatement(pid, ass.toVHDL(pid).getStatement(), ass);
			HDLFunctionCall wait = new HDLFunctionCall().setName(SimulationFunctions.waitFor.getName()).addParams(params.get(1)).addParams(params.get(2)).copyDeepFrozen(container);
			res.addUnclockedStatement(pid, wait.toVHDL(pid).getStatement(), wait);
			ass = setValue(ref, 1, container);
			res.addUnclockedStatement(pid, ass.toVHDL(pid).getStatement(), ass);
			wait = new HDLFunctionCall().setName(SimulationFunctions.waitFor.getName()).addParams(params.get(1)).addParams(params.get(2)).copyDeepFrozen(container);
			res.addUnclockedStatement(pid, wait.toVHDL(pid).getStatement(), wait);
			return res;
		}
		case waitUntil: {
			VHDLContext res = new VHDLContext();
			res.setNoSensitivity(pid);
			WaitStatement ws = new WaitStatement(function.getParams().get(0).toVHDL(), null);
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
