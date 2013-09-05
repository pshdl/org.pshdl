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
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.IHDLGenerator.SideFile;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;

import com.google.common.base.*;
import com.google.common.collect.*;

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
public class PStoVHDLCompiler extends PSAbstractCompiler implements IOutputProvider {

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

	public PStoVHDLCompiler() {
		super();
	}

	public PStoVHDLCompiler(String uri) {
		super(uri);
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
		final List<CompileResult> res = Lists.newArrayListWithCapacity(pkgs.size());
		for (final Entry<String, HDLPackage> e : pkgs.entrySet()) {
			final String src = e.getKey();
			final Set<Problem> syntaxProblems = new HashSet<Problem>(issues.get(src));
			final HDLPackage parse = e.getValue();
			if (listener.startVHDL(src, parse)) {
				final HDLPackage transform = Insulin.transform(parse, src);
				final HDLUnresolvedFragment[] fragments = transform.getAllObjectsOf(HDLUnresolvedFragment.class, true);
				if (fragments.length > 0) {
					System.out.println("PStoVHDLCompiler.compileToVHDL()Fragments found:" + fragments);
				}
				final String vhdlCode = VhdlOutput.toVhdlString(VHDLPackageExtension.INST.toVHDL(transform));
				final HDLUnit[] units = parse.getAllObjectsOf(HDLUnit.class, false);
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

	public static void main(String[] args) throws IOException {
		new PStoVHDLCompiler().invoke(args);
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
		final Stopwatch sw = Stopwatch.createStarted();
		HDLCore.defaultInit();
		final PStoVHDLCompiler compiler = setup("CMDLINE");
		System.out.println("Init: " + sw);
		final File outDir = new File(args[0]);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		boolean hasSyntaxErrors = false;
		for (int i = 1; i < args.length; i++) {
			final File f = new File(args[i]);
			System.out.println("Adding file:" + f);
			final Set<Problem> problems = compiler.add(f);
			if (!problems.isEmpty()) {
				System.out.println("Found the following syntax problems in file " + f.getName() + ":");
				hasSyntaxErrors = true;
			}
			for (final Problem problem : problems) {
				System.out.println("\t" + problem);
			}
		}
		if (hasSyntaxErrors) {
			System.out.println("Exiting");
			return "Found synax errors";
		}
		System.out.println("Compiling files");
		final List<CompileResult> results = compiler.compileToVHDL(new ICompilationListener() {

			@Override
			public boolean startVHDL(String src, HDLPackage parse) {
				System.out.println("Compiling:" + new File(src).getName());
				return true;
			}

			@Override
			public boolean continueWith(String src, HDLPackage parse, Set<Problem> syntaxProblems) {
				final String newName = new File(src).getName();
				if (!syntaxProblems.isEmpty()) {
					System.out.println("Found the following problems in file " + newName + ":");
				}
				for (final Problem p : syntaxProblems) {
					System.out.println("\t" + p.toString());
				}
				return true;
			}
		});
		for (final CompileResult result : results) {
			if (result.vhdlCode != null) {
				writeFiles(outDir, result);
			} else {
				System.out.println("Failed to generate code for:" + result.src);
			}
		}
		System.out.println("Done! Total time:" + sw);
		return null;
	}

	public static File[] writeFiles(File outDir, CompileResult result) throws FileNotFoundException, IOException {
		if (result.hasError())
			return new File[0];
		final List<File> res = new LinkedList<File>();
		String newName = new File(result.src).getName();
		newName = newName.substring(0, newName.length() - 5) + "vhd";
		final File target = new File(outDir, newName);
		res.add(target);
		FileOutputStream fos = new FileOutputStream(target);
		fos.write(result.vhdlCode.getBytes(Charsets.UTF_8));
		fos.close();
		if (result.sideFiles != null) {
			for (final SideFile sd : result.sideFiles) {
				final File file = new File(outDir + "/" + sd.relPath);
				res.add(file);
				final File parentFile = file.getParentFile();
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
		return res.toArray(new File[res.size()]);
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
		VHDLImporter.importFile(HDLQualifiedName.create("VHDL", "work"), contents, lib, asSrc);
	}

	@Override
	public String getHookName() {
		return "vhdl";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

}
