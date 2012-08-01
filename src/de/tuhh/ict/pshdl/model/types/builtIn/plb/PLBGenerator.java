package de.tuhh.ict.pshdl.model.types.builtIn.plb;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public class PLBGenerator implements IHDLGenerator {

	@Override
	public HDLInterface getInterface(HDLDirectGeneration hdl) {
		HDLInterface hIf = new HDLInterface().setName(hdl.getFullName().append(hdl.getIfName()).toString());
		HDLExpression regCount = getRegCountLiteral(hdl);
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(32)));
		hvd = hvd.setDirection(HDLDirection.INOUT).addVariables(new HDLVariable().setName("regs").addDimensions(regCount));
		hIf = hIf.addPorts(hvd);
		hIf.setContainer(hdl);
		// System.out.println("PLBGenerator.getInterface()" + hIf);
		return hIf;
	}

	private HDLExpression getRegCountLiteral(HDLDirectGeneration hdl) {
		ArrayList<HDLArgument> args = hdl.getArguments();
		for (HDLArgument arg : args) {
			if ("regCount".equals(arg.getName())) {
				if (arg.getExpression() != null) {
					return arg.getExpression().copyFiltered(CopyFilter.DEEP);
				}
				return HDLLiteral.get(Integer.parseInt(arg.getValue()));
			}
		}
		throw new IllegalArgumentException("The parameter regCount is not valid!");
	}

	private int getRegCount(HDLDirectGeneration hdl) {
		ArrayList<HDLArgument> args = hdl.getArguments();
		int regCount = -1;
		for (HDLArgument arg : args) {
			if ("regCount".equals(arg.getName())) {
				if (arg.getExpression() != null) {
					regCount = arg.getExpression().constantEvaluate(null).intValue();
				} else {
					regCount = Integer.parseInt(arg.getValue());
				}
			}
		}
		if (regCount == -1)
			throw new IllegalArgumentException("The parameter regCount is not valid!");
		return regCount;
	}

	@Override
	public HDLGenerationInfo getImplementation(HDLDirectGeneration hdl) {
		int regCount = getRegCount(hdl);
		return new HDLGenerationInfo(true, PLBCodeGen.get(regCount));
	}

	@Override
	public String[] getNames() {
		return new String[] { "plb" };
	}

}
