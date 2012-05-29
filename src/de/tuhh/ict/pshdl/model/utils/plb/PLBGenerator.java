package de.tuhh.ict.pshdl.model.utils.plb;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.*;

public class PLBGenerator implements IHDLGenerator {

	@Override
	public HDLInterface getInterface(HDLDirectGeneration hdl) {
		HDLInterface hIf = new HDLInterface().setName(hdl.getIfName().getLastSegment());
		ArrayList<HDLGeneratorArgument> args = hdl.getArguments();
		int regCount = -1;
		for (HDLGeneratorArgument arg : args) {
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
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(32)));
		hvd = hvd.setDirection(HDLDirection.INOUT).addVariables(new HDLVariable().setName("regs").addDimensions(HDLLiteral.get(regCount)));
		hIf = hIf.addPorts(hvd);
		// System.out.println("PLBGenerator.getInterface()" + hIf);
		return hIf;
	}

}
