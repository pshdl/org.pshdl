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

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

import org.apache.commons.cli.*;
import org.pshdl.model.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;

import com.google.common.collect.*;

import de.upb.hni.vmagic.output.*;

/**
 * This compiler is the central place for generating VHDL output and auxiliary
 * files for PSHDL. The basic operation is like this:
 * 
 * <ol>
 * <li>{@link #setup(String)}</li>
 * <li>{@link #add(File)} Add as many files as you want. You can also add VHDL
 * files with {@link #addVHDL(File)}</li>
 * <li>{@link #doCompile(ICompilationListener)} generates all VHDL code and
 * auxiliary files</li>
 * </ol>
 * 
 * @author Karsten Becker
 * 
 */
public class PStoVHDLCompiler extends PSAbstractCompiler implements IOutputProvider {

	public PStoVHDLCompiler(ExecutorService service) {
		super();
	}

	public PStoVHDLCompiler(String uri, ExecutorService service) {
		super(uri, service);
	}

	@Override
	protected CompileResult doCompile(final String src, final HDLPackage parse) {
		final HDLPackage transform = Insulin.transform(parse, src);
		final String vhdlCode = VhdlOutput.toVhdlString(VHDLPackageExtension.INST.toVHDL(transform));
		return createResult(src, vhdlCode, getHookName(), false);
	}

	public static void main(String[] args) throws Exception {
		final PStoVHDLCompiler compiler = new PStoVHDLCompiler(createExecutor());
		compiler.invoke(compiler.getUsage().parse(args));
		System.exit(0);
	}

	/**
	 * This is the command line version of the compiler
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public String invoke(CommandLine cli) throws IOException {
		final List<String> argList = cli.getArgList();
		if (argList.size() == 0)
			return "Missing file arguments, try help " + getHookName();
		final File outDir = new File(argList.get(0));
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		final List<File> files = Lists.newArrayListWithCapacity(argList.size());
		for (final String string : argList) {
			final File file = new File(string);
			if (!file.exists())
				return "File: " + file + " does not exist";
			files.add(file);
		}
		try {
			if (addFiles(files)) {
				printErrors();
				return "Found syntax errors";
			}
		} catch (final Exception e1) {
			e1.printStackTrace();
			return "An exception occured during file parsing, this should not happen";
		}
		System.out.println("Compiling files");
		try {
			validatePackages();
			printErrors();
		} catch (final Exception e) {
			e.printStackTrace();
			return "An exception occured during validation, this should not happen";
		}
		final List<CompileResult> res = Lists.newArrayListWithCapacity(pkgs.size());
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			final String src1 = e.getKey();
			final HDLPackage parse1 = e.getValue();
			if (new ICompilationListener() {

				@Override
				public boolean startModule(String src, HDLPackage parse) {
					System.out.println("Compiling:" + new File(src).getName());
					return true;
				}

				@Override
				public boolean useSource(String src, Collection<Problem> collection, boolean hasError) {
					if (hasError) {
						System.out.println("Skipping " + src + " because it has errors");
					}
					return hasError;
				}

			}.startModule(src1, parse1)) {
				res.add(doCompile(src1, parse1));
			}
		}
		final List<CompileResult> results = res;
		for (final CompileResult result : results) {
			if (!result.hasError()) {
				writeFiles(outDir, result, false);
			} else {
				System.out.println("Failed to generate code for:" + result.src);
			}
		}
		return null;
	}

	/**
	 * Adds a VHDL file to the {@link HDLLibrary} so that interfaces can be
	 * resolved
	 * 
	 * @param file
	 *            the VHDL file
	 * @throws IOException
	 */
	public void addVHDL(File file) throws IOException {
		final FileInputStream fis = new FileInputStream(file);
		addVHDL(fis, file.getAbsolutePath());
		fis.close();
	}

	/**
	 * Imports the given stream as HDLInterface. This allows it to be
	 * referenced. The generated interface can be found in package VHDL.work
	 * 
	 * @param contents
	 *            the contents of the VHDL file
	 * @param asSrc
	 *            a src id under which to register the {@link HDLInterface}
	 */
	public void addVHDL(InputStream contents, String asSrc) {
		validated = false;
		VHDLImporter.importFile(HDLQualifiedName.create("VHDL", "work"), contents, lib, asSrc);
	}

	@Override
	public String getHookName() {
		return "vhdl";
	}

	@Override
	public MultiOption getUsage() {
		final Options options = new Options();
		options.addOption(new Option("o", "outputDir", true, "Specify the directory to which the files will be written, default is: src-gen"));
		return new MultiOption(getHookName() + " usage: [OPTIONS] <files>", null, options);
	}

	public static PStoVHDLCompiler setup(String uri) {
		return new PStoVHDLCompiler(uri, null);
	}

}
