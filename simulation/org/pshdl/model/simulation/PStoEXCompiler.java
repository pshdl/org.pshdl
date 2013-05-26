package org.pshdl.model.simulation;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.pshdl.interpreter.*;
import org.pshdl.interpreter.utils.Graph.CycleException;
import org.pshdl.interpreter.utils.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.Problem.ProblemSeverity;

import com.google.common.collect.*;

public class PStoEXCompiler implements IOutputProvider {

	public PStoEXCompiler() {
		if (!HDLCore.isInitialized()) {
			HDLCore.defaultInit();
		}
		HDLLibrary.registerLibrary("PSHDLSim", new HDLLibrary());
	}

	@Override
	public String getHookName() {
		return "psex";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	private final Map<File, HDLPackage> pkgs = Maps.newLinkedHashMap();
	private HDLUnit unit;

	@Override
	public String invoke(String[] args) throws Exception {
		if (args.length == 0)
			return "Not enough arguments, try help:" + getHookName();
		final HDLQualifiedName unitName = new HDLQualifiedName(args[0]);
		System.out.println("Parsing files:");
		boolean syntaxerror = false;
		final Set<Problem> problems = new HashSet<Problem>();
		for (int i = 1; i < args.length; i++) {
			final File source = new File(args[i]);
			System.out.println("\t" + source);
			if (addFile(source, problems)) {
				syntaxerror = true;
			}
		}
		if (syntaxerror)
			return "Exiting because of syntax errors in the input";
		System.out.println("Validating:");
		if (validatePackages(problems))
			return "Exiting because of errors in the input";
		findUnit(unitName);
		if (unit == null)
			return "Unit:" + unitName + " not found";
		final String src = unit.getLibrary().getSrc(unitName);
		try {
			final ExecutableModel em = createExecutable(unit, src);
			IOUtil.writeExecutableModel(src, new Date().getTime(), em, new File("a.em"));
			// System.out.println(JavaCompiler.doCompile(em, "bla"));
		} catch (final CycleException e) {
			e.explain(System.err);
		}
		return "Success";
	}

	public ExecutableModel createExecutable(HDLUnit unit, String src) throws CycleException {
		final HDLEvaluationContext context = HDLEvaluationContext.createDefault(unit);
		final HDLUnit simulationModel = HDLSimulator.createSimulationModel(unit, context, src);
		// System.out.println("PStoEXCompiler.createExecutable()" +
		// simulationModel);
		// simulationModel.validateAllFields(simulationModel.getContainer(),
		// true);
		final FluidFrame model = SimulationTransformationExtension.simulationModelOf(simulationModel, context);
		final ExecutableModel em = model.getExecutable();
		try {
			em.sortTopological();
		} catch (final CycleException e) {
			e.model = em;
			throw e;
		}
		return em;
	}

	public HDLUnit findUnit(HDLQualifiedName unitName) {
		unit = null;
		for (final Entry<File, HDLPackage> e : pkgs.entrySet()) {
			final HDLPackage parse = e.getValue();
			final HDLUnit first = HDLQuery.select(HDLUnit.class).from(parse).whereObj().fullNameIs(unitName).getFirst();
			if (first != null) {
				unit = first;
				return first;
			}
		}
		return null;
	}

	public boolean validatePackages(Set<Problem> problems) throws IOException, FileNotFoundException {
		boolean validationError = false;
		for (final Entry<File, HDLPackage> e : pkgs.entrySet()) {
			final File source = e.getKey();
			System.out.println("\t" + source);
			if (validateFile(e.getValue(), problems)) {
				validationError = true;
			}
		}
		return validationError;
	}

	public static void main(String[] args) throws Exception {
		final String invoke = new PStoEXCompiler().invoke(args);
		if (invoke != null) {
			System.err.println(invoke);
		}
	}

	public boolean addFile(File source, Set<Problem> syntaxProblems) throws IOException, FileNotFoundException {
		final HDLPackage parse = PSHDLParser.parse(source, "PSHDLSim", syntaxProblems);
		pkgs.put(source, parse);
		return reportProblem(syntaxProblems);
	}

	public boolean reportProblem(Set<Problem> syntaxProblems) {
		boolean error = false;
		if (syntaxProblems.size() != 0) {
			System.err.println("The following syntax problems where found:");
			for (final Problem problem : syntaxProblems) {
				System.err.println(problem.line + ":" + problem);
				if (problem.severity == ProblemSeverity.ERROR) {
					error = true;
				}
			}
		}
		return error;
	}

	public boolean validateFile(HDLPackage parse, Set<Problem> problems) throws IOException, FileNotFoundException {
		final Set<Problem> validate = HDLValidator.validate(parse, null);
		problems.addAll(validate);
		return reportProblem(validate);
	}

	public ExecutableModel createExecutable(HDLQualifiedName name, String src) throws CycleException {
		final HDLUnit findUnit = findUnit(name);
		if (findUnit == null)
			throw new IllegalArgumentException("No unit with name:" + name);
		return createExecutable(findUnit, src);
	}

}
