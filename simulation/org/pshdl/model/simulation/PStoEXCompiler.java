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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.utils.Graph.CycleException;
import org.pshdl.interpreter.utils.IOUtil;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.utils.HDLCore;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.IOutputProvider;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class PStoEXCompiler extends PSAbstractCompiler implements IOutputProvider {

	private final Map<String, ITypeOuptutProvider> providers;

	public PStoEXCompiler() {
		this(null, null);
	}

	public PStoEXCompiler(String uri, ExecutorService service) {
		super(uri, service);
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
		options.addOption(new Option("em", "enable the output of the byte-code .em file"));
		options.addOption(new Option("allSignals", "disable the removal of redundant variables"));
		options.addOption(new Option("ir", "dump the intermediate PSHDL code"));
		options.addOption(OptionBuilder.withArgName(listTypes("|"))
				.withDescription("The output type to generate. Valid options are: " + listTypes(", ") + ". Each type may require additional command line arguments").hasArg()
				.create("type"));
		final MultiOption mo = new MultiOption(getHookName() + " usage: [OPTIONS] MODULE <files>", null, options);
		for (final ITypeOuptutProvider ito : providers.values()) {
			mo.subs.add(ito.getUsage());
		}
		return mo;
	}

	private String listTypes(String sep) {
		return Joiner.on(sep).join(providers.keySet());
	}

	@Override
	public String invoke(CommandLine cli) throws Exception {
		@SuppressWarnings("unchecked")
		final List<String> argList = cli.getArgList();
		if (argList.size() == 0) {
			getUsage().printHelp(System.out);
			return "Missing module and file arguments";
		}
		if (argList.size() == 1) {
			getUsage().printHelp(System.out);
			return "Missing file arguments";
		}
		final HDLQualifiedName unitName = new HDLQualifiedName(argList.get(0));
		System.out.println("Using module:\t" + unitName);
		final List<File> files = Lists.newLinkedList();
		final String type = cli.getOptionValue("type");
		if ((type != null) && !providers.containsKey(type.toLowerCase()))
			return "Invalid type argument, no such type " + type;
		System.out.println("Parsing files:");
		for (int i = 1; i < argList.size(); i++) {
			final File source = new File(argList.get(i));
			System.out.println("\t" + source.getAbsolutePath());
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
			if (!dir.mkdirs())
				throw new IllegalArgumentException("Failed to create direcory:" + dir);
		}
		final ExecutableModel em = createExecutable(unit, src, cli.hasOption("ir"), !cli.hasOption("allSignals"));
		if (cli.hasOption("em")) {
			IOUtil.writeExecutableModel(System.currentTimeMillis(), em, new File(dir, unitName.toString() + ".em"));
		}
		if (type != null) {
			final ITypeOuptutProvider ito = providers.get(type.toLowerCase());
			final List<CompileResult> results = ito.invoke(cli, em, null);
			for (final CompileResult cr : results) {
				writeFiles(dir, cr);
			}
		}
		return null;
	}

	public static ExecutableModel createExecutable(HDLUnit unit, String src, boolean dumpIntermediate, boolean purgeAliases) throws CycleException {
		final HDLEvaluationContext context = HDLEvaluationContext.createDefault(unit);
		final HDLUnit simulationModel = HDLSimulator.createSimulationModel(unit, context, src, '_');
		if (dumpIntermediate) {
			try {
				Files.write(simulationModel.toString(), new File(src + "_ir.pshdl"), Charsets.UTF_8);
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		}
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

	public ExecutableModel createExecutable(HDLQualifiedName name, String src) throws CycleException {
		final HDLUnit findUnit = findUnit(name);
		if (findUnit == null)
			throw new IllegalArgumentException("No unit with name:" + name);
		return createExecutable(findUnit, src, false, true);
	}

}
