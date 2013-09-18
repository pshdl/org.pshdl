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
package org.pshdl.model.simulation;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.cli.*;
import org.pshdl.interpreter.*;
import org.pshdl.interpreter.utils.Graph.CycleException;
import org.pshdl.interpreter.utils.*;
import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class PStoEXCompiler extends PSAbstractCompiler implements IOutputProvider {

	private final Map<String, ITypeOuptutProvider> providers;

	public PStoEXCompiler(ExecutorService service) {
		super("CMDLINE", service);
		final Collection<ITypeOuptutProvider> allImplementations = HDLCore.getAllImplementations(ITypeOuptutProvider.class);
		providers = Maps.newTreeMap();
		for (final ITypeOuptutProvider ito : allImplementations) {
			providers.put(ito.getHookName().toLowerCase(), ito);
		}
	}

	@Override
	public String getHookName() {
		return "psex";
	}

	@SuppressWarnings("static-access")
	@Override
	public MultiOption getUsage() {
		final Options options = new Options();
		options.addOption(new Option("o", "outputDir", true, "Specify the directory to which the files will be written, default is: src-gen/psex/[type]"));
		options.addOption(new Option("noEm", "disable the output of the byte-code .em file"));
		options.addOption(OptionBuilder.withArgName("type")
				.withDescription("The output type to generate. Valid options are: " + listTypes() + ". Each type may require additional command line arguments").hasArg()
				.create("type"));
		final MultiOption mo = new MultiOption(getHookName() + " usage: [OPTIONS] MODULE <files>", null, options);
		for (final ITypeOuptutProvider ito : providers.values()) {
			mo.subs.add(ito.getUsage());
		}
		return mo;
	}

	private String listTypes() {
		return Joiner.on(", ").join(providers.keySet());
	}

	@Override
	public String invoke(CommandLine cli) throws Exception {
		final List<String> argList = cli.getArgList();
		if (argList.size() == 0)
			return "Missing module and file arguments, try help " + getHookName();
		if (argList.size() == 1)
			return "Missing file arguments, try help " + getHookName();
		final HDLQualifiedName unitName = new HDLQualifiedName(argList.get(0));
		System.out.println("Using module:\t" + unitName);
		final List<File> files = Lists.newLinkedList();
		final String type = cli.getOptionValue("type");
		if ((type != null) && !providers.containsKey(type.toLowerCase()))
			return "Invalid type argument, no such type " + type;
		System.out.println("Parsing files:");
		for (int i = 1; i < argList.size(); i++) {
			final File source = new File(argList.get(i));
			if (!source.exists())
				return "The file: " + source + " can not be found";
			files.add(source);
		}
		if (addFiles(files))
			return "Exiting because of syntax errors in the input";
		System.out.println("Validating:");
		if (validatePackages())
			return "Exiting because of errors in the input";
		final HDLUnit unit = findUnit(unitName);
		if (unit == null)
			return "Unit: " + unitName + " not found";
		final String src = unit.getLibrary().getSrc(unitName);
		String outDir = cli.getOptionValue("outputDir");
		if (outDir == null) {
			outDir = "src-gen/psex";
		}
		final File dir = new File(outDir);
		if (!dir.exists()) {
			System.out.println("Output directory " + dir + " does not exist, creating it");
			dir.mkdirs();
		}
		try {
			final ExecutableModel em = createExecutable(unit, src);
			if (!cli.hasOption("noEm")) {
				IOUtil.writeExecutableModel(new Date().getTime(), em, new File(dir, unitName.toString() + ".em"));
			}
			if (type != null) {
				final ITypeOuptutProvider ito = providers.get(type.toLowerCase());
				final List<CompileResult> results = ito.invoke(cli, em, null);
				for (final CompileResult cr : results) {
					writeFiles(dir, cr, true);
				}
			}
		} catch (final CycleException e) {
			e.explain(System.err);
		}
		close();
		return null;
	}

	public ExecutableModel createExecutable(HDLUnit unit, String src) throws CycleException {
		final HDLEvaluationContext context = HDLEvaluationContext.createDefault(unit);
		final HDLUnit simulationModel = HDLSimulator.createSimulationModel(unit, context, src, '_');
		final FluidFrame model = SimulationTransformationExtension.simulationModelOf(simulationModel, context);
		final HDLQualifiedName fqn = FullNameExtension.fullNameOf(simulationModel);
		final ExecutableModel em = model.getExecutable(fqn.toString(), src);
		try {
			em.sortTopological();
		} catch (final CycleException e) {
			e.model = em;
			throw e;
		}
		return em;
	}

	public static void main(String[] args) throws Exception {
		final PStoEXCompiler psx = new PStoEXCompiler(createExecutor());
		final MultiOption options = psx.getUsage();
		options.printHelp(System.out);
		final CommandLineParser clp = new PosixParser();
		final String invoke = psx.invoke(clp.parse(options.allOptions(), args));
		if (invoke != null) {
			System.err.println(invoke);
			System.exit(1);
		}
		System.exit(0);
	}

	public ExecutableModel createExecutable(HDLQualifiedName name, String src) throws CycleException {
		final HDLUnit findUnit = findUnit(name);
		if (findUnit == null)
			throw new IllegalArgumentException("No unit with name:" + name);
		return createExecutable(findUnit, src);
	}

}
