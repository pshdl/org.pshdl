package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.GeneratorInformation;
import de.tuhh.ict.pshdl.model.validation.*;

public class BusGenerator implements IHDLGenerator {

	@Override
	public HDLInterface getInterface(HDLDirectGeneration hdl) {
		HDLInterface hIf = new HDLInterface().setName(hdl.getFullName().append(hdl.getIfName()).toString());
		HDLExpression regCount = getRegCountLiteral(hdl);
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(32)));
		hvd = hvd.setDirection(HDLDirection.INOUT).addVariables(new HDLVariable().setName("regs").addDimensions(regCount));
		hvd = hvd.addAnnotations(HDLBuiltInAnnotations.genSignal.create(null));
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
					BigInteger regVal = arg.getExpression().constantEvaluate(null);
					if (regVal != null)
						regCount = regVal.intValue();
					else
						throw new IllegalArgumentException("The value of the parameter regCount is not valid! It probably is not constant.");
				} else {
					try {
						regCount = Integer.parseInt(arg.getValue());
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("The value of the parameter regCount is not valid! It is not a valid integer.");
					}
				}
			}
		}
		if (regCount == -1)
			throw new IllegalArgumentException("The parameter regCount is not present!");
		return regCount;
	}

	@Override
	public HDLGenerationInfo getImplementation(HDLDirectGeneration hdl) {
		int regCount = getRegCount(hdl);
		String version = getVersion(hdl);
		HDLGenerationInfo hdgi = new HDLGenerationInfo(UserLogicCodeGen.get(regCount));
		if (hdl.getGeneratorID().equalsIgnoreCase("plb")) {
			List<SideFile> sideFiles = BusGenSideFiles.getSideFiles(hdl.getContainer(HDLUnit.class), regCount, version, false);
			hdgi.files.addAll(sideFiles);
			return hdgi;
		}
		if (hdl.getGeneratorID().equalsIgnoreCase("axi")) {
			List<SideFile> sideFiles = BusGenSideFiles.getSideFiles(hdl.getContainer(HDLUnit.class), regCount, version, true);
			hdgi.files.addAll(sideFiles);
			return hdgi;
		}
		throw new IllegalArgumentException("Can not handle generator ID:" + hdl.getGeneratorID());
	}

	private String getVersion(HDLDirectGeneration hdl) {
		for (HDLArgument arg : hdl.getArguments()) {
			if ("version".equals(arg.getName())) {
				return arg.getValue();
			}
		}
		return "v1_00_a";
	}

	@Override
	public String[] getNames() {
		return new String[] { "plb", "axi" };
	}

	@Override
	public void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context) {
		if (!hdg.getInclude()) {
			problems.add(new Problem(ErrorCode.GENERATOR_WARNING, hdg, "The " + hdg.getGeneratorID()
					+ " generator assumes to be included. Not including it means that all ports need to be exported manually."));
		}
		try {
			getRegCount(hdg);
		} catch (Exception e) {
			problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, e.getMessage()));
		}
		String version = getVersion(hdg);
		if (!version.matches("v\\d_\\d\\d_[a-z]")) {
			problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, "The version string:" + version + " is not valid. It has to be of the format v[0-9]_[0-9][0-9]_[a-z]"));
		}
	}

	@Override
	public List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl) {
		HDLGenerationInfo info = getImplementation(hdl);
		List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
		Set<HDLVariableDeclaration> hvd = info.unit.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hdlVariableDeclaration : hvd) {
			switch (hdlVariableDeclaration.getDirection()) {
			case CONSTANT:
			case PARAMETER:
			case IN:
			case INOUT:
			case OUT:
				res.add(hdlVariableDeclaration.addAnnotations(HDLBuiltInAnnotations.genSignal.create(null)));
				break;
			case HIDDEN:
			case INTERNAL:
				break;
			}
		}
		return res;
	}

	@Override
	public GeneratorInformation getGeneratorInfo(String name) {
		GeneratorInformation gi = new GeneratorInformation(BusGenerator.class.getSimpleName(), name, "Generate the infrastructure to create a pcore for the " + name
				+ " bus. This generator should always be included.");
		gi.arguments.put("regCount",
				"This parameter is mandatory. It indicates how many sw registers should be accessible in the ip core. The number can be a constant or a string");
		gi.arguments.put("version", "The version ID to use for the generated pcore");
		return gi;
	}

}
