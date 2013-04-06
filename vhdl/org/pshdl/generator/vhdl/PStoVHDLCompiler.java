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

import org.pshdl.model.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.IHDLGenerator.SideFile;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.Problem.ProblemSeverity;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

import de.upb.hni.vmagic.output.*;

/**
 * This compiler is the central place for generating VHDL output and auxiliary
 * files for PSHDL. The basic operation is like this:
 * 
 * <ol>
 * <li>{@link #setup(String)}</li>
 * <li>{@link #add(File)} Add as many files as you want. You can also add VHDL
 * files with {@link #addVHDL(InputStream, String, HashSet)}</li>
 * <li>{@link #compileToVHDL(ICompilationListener)} generates all VHDL code and
 * auxiliary files</li>
 * </ol>
 * 
 * @author Karsten Becker
 * 
 */
public class PStoVHDLCompiler implements IOutputProvider {

	/**
	 * Do not report any progress and proceed whenever possible
	 * 
	 * @author Karsten Becker
	 * 
	 */
	public static final class NullListener implements ICompilationListener {
		@Override
		public boolean startVHDL(String src, HDLPackage parse) {
			return true;
		}

		@Override
		public boolean continueWith(String src, HDLPackage parse, Set<Problem> validate) {
			return true;
		}
	}

	public interface ICompilationListener {

		/**
		 * Check whether you want to continue compiling this source
		 * 
		 * @param src
		 *            the src location of this file
		 * @param parse
		 *            the model before running {@link Insulin}
		 * @return <code>true</code> to continue with the compilation,
		 *         <code>false</code> otherwise
		 */
		boolean startVHDL(String src, HDLPackage parse);

		/**
		 * Check whether you want to continue compiling this source.
		 * <b>Note:</b> if there are errors reported, compilation will not
		 * continue and this method will not be called
		 * 
		 * @param src
		 *            the src location of this file
		 * @param parse
		 *            the model after {@link Insulin}
		 * @param validate
		 *            the problems that the validators reported
		 * @return <code>true</code> to continue with the compilation,
		 *         <code>false</code> otherwise
		 */
		boolean continueWith(String src, HDLPackage parse, Set<Problem> validate);

	}

	private HDLLibrary lib;
	private String libURI;
	private Map<String, HDLPackage> parsedContent = Maps.newLinkedHashMap();
	private Multimap<String, Problem> issues = LinkedHashMultimap.create();

	private PStoVHDLCompiler(String libURI) {
		lib = new HDLLibrary();
		this.libURI = libURI;
		HDLLibrary.registerLibrary(libURI, lib);
	}

	/**
	 * Call this method to get an instance of the compiler. It also checks
	 * whether the {@link HDLCore} was correctly initialized
	 * 
	 * @param libURI
	 *            a unique libURI for registering the {@link HDLLibrary}
	 * @return the {@link PStoVHDLCompiler}
	 */
	public static PStoVHDLCompiler setup(String libURI) {
		if (!HDLCore.isInitialized())
			throw new RuntimeException("The HDLCore needs to be initialized first!");
		return new PStoVHDLCompiler(libURI);
	}

	/**
	 * A container for the results of the compilation
	 * 
	 * @author Karsten Becker
	 * 
	 */
	public static class CompileResult {
		/**
		 * Problems that occurred during validation
		 */
		public final Set<Problem> syntaxProblems;
		/**
		 * The generated VHDL code if the compilation was successful,
		 * <code>null</code> otherwise
		 */
		public final String vhdlCode;
		/**
		 * The name of the first entity encountered for display purposes. Will
		 * be &lt;ERROR&gt; if not successful
		 */
		public final String entityName;
		/**
		 * All additional files that have been generated
		 */
		public final Set<SideFile> sideFiles;
		/**
		 * The src for which this code was generated
		 */
		public final String src;

		public CompileResult(Set<Problem> syntaxProblems, String vhdlCode, String entityName, Collection<SideFile> sideFiles, String src) {
			super();
			this.syntaxProblems = syntaxProblems;
			this.vhdlCode = vhdlCode;
			this.entityName = entityName;
			this.src = src;
			if (sideFiles != null) {
				this.sideFiles = new HashSet<SideFile>(sideFiles);
			} else {
				this.sideFiles = new HashSet<SideFile>();
			}
		}

		public boolean hasError() {
			return vhdlCode == null;
		}

	}

	/**
	 * Compiles all added files. Files that had syntax errors are ignored. When
	 * a validator is detecting an error, that file will produce an empty error
	 * {@link CompileResult}
	 * 
	 * @param listener
	 *            a call back interface for reporting progress on the
	 *            compilation. If <code>null</code> {@link NullListener} will be
	 *            used.
	 * @return a list of {@link CompileResult}s. If a file contained an error,
	 *         the field VHDLCode will be <code>null</code>
	 */
	public List<CompileResult> compileToVHDL(ICompilationListener listener) {
		if (listener == null) {
			listener = new NullListener();
		}
		List<CompileResult> res = Lists.newArrayListWithCapacity(parsedContent.size());
		for (Entry<String, HDLPackage> e : parsedContent.entrySet()) {
			String src = e.getKey();
			Set<Problem> syntaxProblems = new HashSet<Problem>(issues.get(src));
			HDLPackage parse = e.getValue();
			if (parse == null) {
				res.add(new CompileResult(syntaxProblems, null, "<ERROR>", null, src));
				continue;
			}
			if (listener.startVHDL(src, parse)) {
				parse = Insulin.resolveFragments(parse);
				Set<Problem> validate = HDLValidator.validate(parse, null);
				boolean hasValidationError = false;
				for (Problem issue : validate) {
					if (issue.severity == ProblemSeverity.ERROR) {
						hasValidationError = true;
					}
					syntaxProblems.add(issue);
				}
				if (hasValidationError || !listener.continueWith(src, parse, validate)) {
					res.add(new CompileResult(syntaxProblems, null, "<ERROR>", null, src));
					continue;
				}
				HDLPackage hdlPackage = Insulin.transform(parse, src);
				String vhdlCode = VhdlOutput.toVhdlString(VHDLPackageExtension.INST.toVHDL(hdlPackage));
				HDLUnit[] units = parse.getAllObjectsOf(HDLUnit.class, false);
				String name = "<emptyFile>";
				if (units.length != 0) {
					name = units[0].getName();
				}
				res.add(new CompileResult(syntaxProblems, vhdlCode, name, lib.sideFiles.values(), src));
				lib.sideFiles.clear();
			}
		}
		return res;
	}

	/**
	 * This is the command line version of the compiler
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Override
	public String invoke(String[] args) throws IOException {
		if (args.length == 1)
			return "Invalid arguments. Try help " + getHookName();
		Stopwatch sw = new Stopwatch().start();
		HDLCore.defaultInit();
		PStoVHDLCompiler compiler = setup("CMDLINE");
		System.out.println("Init: " + sw);
		File outDir = new File(args[0]);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		boolean hasSyntaxErrors = false;
		for (int i = 1; i < args.length; i++) {
			File f = new File(args[i]);
			System.out.println("Adding file:" + f);
			Set<Problem> problems = compiler.add(f);
			if (!problems.isEmpty()) {
				System.out.println("Found the following syntax problems in file " + f.getName() + ":");
				hasSyntaxErrors = true;
			}
			for (Problem problem : problems) {
				System.out.println("\t" + problem);
			}
		}
		if (hasSyntaxErrors) {
			System.out.println("Exiting");
			return "Found synax errors";
		}
		System.out.println("Compiling files");
		List<CompileResult> results = compiler.compileToVHDL(new ICompilationListener() {

			@Override
			public boolean startVHDL(String src, HDLPackage parse) {
				System.out.println("Compiling:" + new File(src).getName());
				return true;
			}

			@Override
			public boolean continueWith(String src, HDLPackage parse, Set<Problem> syntaxProblems) {
				String newName = new File(src).getName();
				if (!syntaxProblems.isEmpty()) {
					System.out.println("Found the following problems in file " + newName + ":");
				}
				for (Problem p : syntaxProblems) {
					System.out.println("\t" + p.toString());
				}
				return true;
			}
		});
		for (CompileResult result : results) {
			if (result.vhdlCode != null) {
				writeFiles(outDir, result);
			} else {
				System.out.println("Failed to generate code for:" + result.src);
			}
		}
		System.out.println("Done! Total time:" + sw);
		return null;
	}

	public static void writeFiles(File outDir, CompileResult result) throws FileNotFoundException, IOException {
		if (result.hasError())
			return;
		String newName = new File(result.src).getName();
		newName = newName.substring(0, newName.length() - 5) + "vhd";
		FileOutputStream fos = new FileOutputStream(new File(outDir, newName));
		fos.write(result.vhdlCode.getBytes(Charsets.UTF_8));
		fos.close();
		if (result.sideFiles != null) {
			for (SideFile sd : result.sideFiles) {
				File file = new File(outDir + "/" + sd.relPath);
				File parentFile = file.getParentFile();
				if ((parentFile != null) && !parentFile.exists()) {
					parentFile.mkdirs();
				}
				fos = new FileOutputStream(file);
				if (sd.contents == SideFile.THIS) {
					fos.write(result.vhdlCode.getBytes(Charsets.UTF_8));
				} else {
					fos.write(sd.contents);
				}
				fos.close();
			}
		}
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param absolutePath
	 *            a source file name for this module
	 * @throws IOException
	 */
	public Set<Problem> add(File f) throws IOException {
		return add(Files.toString(f, Charsets.UTF_8), f.getAbsolutePath());
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param src
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 * @throws IOException
	 */
	public Set<Problem> add(InputStream contents, String src) throws IOException {
		InputStreamReader r = new InputStreamReader(contents, Charsets.UTF_8);
		String text = CharStreams.toString(r);
		r.close();
		return add(text, src);
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param src
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 */
	public Set<Problem> add(String contents, String src) {
		Set<Problem> problems = Sets.newHashSet();
		issues.removeAll(src);
		HDLPackage pkg = PSHDLParser.parseString(contents, libURI, problems, src);
		parsedContent.put(src, pkg);
		issues.putAll(src, problems);
		return problems;
	}

	/**
	 * Removes all resources related to this src. This can be important for
	 * incremental compilation.
	 * 
	 * @param src
	 *            the src name that was used to register a added input
	 */
	public void remove(String src) {
		lib.removeAllSrc(src);
		parsedContent.remove(src);
	}

	/**
	 * Removes the generated HDLLibrary
	 */
	public void close() {
		lib.unregister();
		lib = null;
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
		FileInputStream fis = new FileInputStream(file);
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
		VHDLImporter.importFile(HDLQualifiedName.create("VHDL", "work"), contents, lib, asSrc);
	}

	@Override
	public String getHookName() {
		return "vhdl";
	}

	@Override
	public String[] getUsage() {
		// TODO Auto-generated method stub
		return null;
	}

}
