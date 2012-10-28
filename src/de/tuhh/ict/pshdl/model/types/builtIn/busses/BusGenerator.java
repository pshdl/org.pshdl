package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.io.*;
import java.math.*;
import java.util.*;

import org.antlr.runtime.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.GeneratorInformation;
import de.tuhh.ict.pshdl.model.validation.*;

public class BusGenerator implements IHDLGenerator {

	@Override
	public HDLInterface getInterface(HDLDirectGeneration hdl) {
		String name = hdl.getFullName().append(hdl.getIfName()).toString();
		if (hdl.getGeneratorContent().length() != 0) {
			Unit unit = null;
			try {
				unit = MemoryModel.parseUnit(getContentStream(hdl));
				List<Row> rows = MemoryModel.buildRows(unit);
				HDLInterface bId = MemoryModel.buildHDLInterface(unit, rows).setContainer(hdl).setName(name);
				return bId;
			} catch (Exception e) {
				throw new HDLProblemException("An unexpected exception occured while parsing the generator content:" + e, new Problem(ErrorCode.GENERATOR_ERROR, hdl));
			}
		}
		HDLInterface hIf = new HDLInterface().setName(name);
		HDLExpression regCount = getRegCountLiteral(hdl);
		HDLVariableDeclaration hvd = new HDLVariableDeclaration().setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(32)));
		hvd = hvd.setDirection(HDLDirection.INOUT).addVariables(new HDLVariable().setName("regs").addDimensions(regCount));
		hvd = hvd.addAnnotations(HDLBuiltInAnnotations.genSignal.create(null));
		hIf = hIf.addPorts(hvd);
		hIf.setContainer(hdl);
		// System.out.println("PLBGenerator.getInterface()" + hIf);
		return hIf;
	}

	private InputStream getContentStream(HDLDirectGeneration hdl) {
		String generatorContent = hdl.getGeneratorContent();
		String substring = generatorContent.substring(2, generatorContent.length() - 2);
		return new ByteArrayInputStream(substring.getBytes());
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
		int regCount = 0;
		if (hdl.getGeneratorContent().length() == 0)
			regCount = getRegCount(hdl);
		String version = getVersion(hdl);
		if (hdl.getGeneratorID().equalsIgnoreCase("plb")) {
			HDLGenerationInfo hdgi = new HDLGenerationInfo(UserLogicCodeGen.get(regCount));
			List<SideFile> sideFiles = BusGenSideFiles.getSideFiles(hdl.getContainer(HDLUnit.class), regCount, version, false);
			hdgi.files.addAll(sideFiles);
			return hdgi;
		}
		if (hdl.getGeneratorID().equalsIgnoreCase("axi")) {
			HDLGenerationInfo hdgi = new HDLGenerationInfo(UserLogicCodeGen.get(regCount));
			List<SideFile> sideFiles = BusGenSideFiles.getSideFiles(hdl.getContainer(HDLUnit.class), regCount, version, true);
			hdgi.files.addAll(sideFiles);
			return hdgi;
		}
		if (hdl.getGeneratorID().equalsIgnoreCase("apb")) {
			if (hdl.getGeneratorContent().length() != 0) {
				Unit unit = null;
				try {
					unit = MemoryModel.parseUnit(getContentStream(hdl));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RecognitionException e) {
					e.printStackTrace();
				}
				return new HDLGenerationInfo(ABP3BusCodeGen.get("de.tuhh.ict.apb", unit));
			}
			Unit unit = createDefaultUnit(regCount);
			return new HDLGenerationInfo(ABP3BusCodeGen.get("de.tuhh.ict.apb", unit));
		}
		throw new IllegalArgumentException("Can not handle generator ID:" + hdl.getGeneratorID());
	}

	private Unit createDefaultUnit(int regCount) {
		Unit unit = new Unit();
		Row row = new Row("reg");
		Definition def = new Definition();
		def.name = "regs";
		def.register = true;
		def.type = Type.BIT;
		def.rw = RWType.rw;
		def.width = 32;
		row.definitions.add(def);
		unit.declarations.put("reg", row);
		Memory mem = new Memory();
		Reference regRow = new Reference("reg");
		regRow.dimensions.add(regCount);
		mem.references.add(regRow);
		unit.memory = mem;
		return unit;
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
		return new String[] { "plb", "axi", "apb" };
	}

	@Override
	public void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context) {
		if (!hdg.getInclude()) {
			problems.add(new Problem(ErrorCode.GENERATOR_WARNING, hdg, "The " + hdg.getGeneratorID()
					+ " generator assumes to be included. Not including it means that all ports need to be exported manually."));
		}
		if (hdg.getGeneratorContent().length() == 0) {
			try {
				getRegCount(hdg);
			} catch (Exception e) {
				problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, e.getMessage()));
			}
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
				res.add(hdlVariableDeclaration.addAnnotations(HDLBuiltInAnnotations.genSignal.create(hdl.getIfName().toString())));
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
