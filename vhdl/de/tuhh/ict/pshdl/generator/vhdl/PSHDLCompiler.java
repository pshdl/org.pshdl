package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.parser.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;
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
public class PSHDLCompiler {

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

	private PSHDLCompiler(String libURI) {
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
	 * @return the {@link PSHDLCompiler}
	 */
	public static PSHDLCompiler setup(String libURI) {
		if (!HDLCore.isInitialized())
			throw new RuntimeException("The HDLCore needs to be initialized first!");
		return new PSHDLCompiler(libURI);
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
		public Set<Problem> syntaxProblems;
		/**
		 * The generated VHDL code if the compilation was successful
		 */
		public String vhdlCode;
		/**
		 * The name of the first entity encountered for display purposes. Will
		 * be &lt;ERROR&gt; if not successful
		 */
		public String entityName;
		/**
		 * All additional files that have been generated
		 */
		public Set<SideFile> sideFiles;
		/**
		 * The src for which this code was generated
		 */
		public String src;

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

	}

	/**
	 * Compiles all added files. Files that had syntax errors are ignored. When
	 * a validator is detecting an error, that file will produce an empty error
	 * {@link CompileResult}
	 * 
	 * @param listener
	 *            a call back interface for reporting progress on the
	 *            compilation
	 * @return a list of {@link CompileResult}s. If a file contained an error,
	 *         the field VHDLCode will be <code>null</code>
	 * @throws IOException
	 */
	public List<CompileResult> compileToVHDL(ICompilationListener listener) throws IOException {
		List<CompileResult> res = Lists.newArrayListWithCapacity(parsedContent.size());
		Set<Problem> syntaxProblems = new HashSet<Problem>();
		for (Entry<String, HDLPackage> e : parsedContent.entrySet()) {
			HDLPackage parse = e.getValue();
			String src = e.getKey();
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
	public static void main(String[] args) throws FileNotFoundException, IOException {
		if (args.length == 0) {
			System.out.println("Arguments are: outputDir <Files>");
			return;
		}
		Stopwatch sw = new Stopwatch().start();
		HDLCore.defaultInit();
		PSHDLCompiler compiler = setup("CMDLINE");
		CompilerInformation information = HDLCore.getCompilerInformation();
		System.out.println("PSHDLCompiler.main()" + information);
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
			return;
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
			} else {
				System.out.println("Failed to generate code for:" + result.src);
			}
		}
		System.out.println("Done! Total time:" + sw);
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
		HDLPackage pkg = PSHDLParser.parseString(contents, libURI, problems, src);
		if (pkg != null) {
			parsedContent.put(src, pkg);
		}
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
}
