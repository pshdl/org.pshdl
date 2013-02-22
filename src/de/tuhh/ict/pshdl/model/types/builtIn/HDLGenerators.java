package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.HDLGenerationInfo;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

public class HDLGenerators {
	private static Map<String, IHDLGenerator> generators;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		generators = new HashMap<String, IHDLGenerator>();
		for (IHDLGenerator gen : sp.getAllGenerators()) {
			for (String name : gen.getNames()) {
				generators.put(name, gen);
				info.registeredGenerators.put(name, gen.getGeneratorInfo(name));
			}
		}
	}

	public static List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null)
			return generator.getPortAdditions(hdl);
		return null;
	}

	public static HDLInterface getInterface(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null)
			return generator.getInterface(hdl).copyDeepFrozen(hdl);
		return null;
	}

	public static HDLGenerationInfo getImplementation(HDLDirectGeneration hdl) {
		IHDLGenerator generator = generators.get(hdl.getGeneratorID());
		if (generator != null)
			return generator.getImplementation(hdl);
		return null;
	}

	public static void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context) {
		IHDLGenerator generator = generators.get(hdg.getGeneratorID());
		if (generator != null) {
			generator.validate(hdg, problems, context);
		} else {
			problems.add(new Problem(ErrorCode.GENERATOR_NOT_KNOWN, hdg));
		}
	}

	public static Collection<IHDLGenerator> getAllGenerators() {
		return generators.values();
	}

}
