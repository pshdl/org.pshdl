package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;
import java.util.*;

import com.google.common.base.*;
import com.google.common.io.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.parser.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.output.*;

public class PSHDLCompiler {

	private HDLLibrary lib;

	protected PSHDLCompiler() {
		lib = new HDLLibrary();
	}

	public static PSHDLCompiler setup() {
		return new PSHDLCompiler();
	}

	public static class CompileResult {

		public Set<Problem> syntaxProblems;
		public String vhdlCode;
		public String entityName;
		public Collection<SideFile> sideFiles;

		public CompileResult(Set<Problem> syntaxProblems, String vhdlCode, String entityName, Collection<SideFile> sideFiles) {
			super();
			this.syntaxProblems = syntaxProblems;
			this.vhdlCode = vhdlCode;
			this.entityName = entityName;
			this.sideFiles = sideFiles;
		}

	}

	public CompileResult compileToVHDL(File resource) throws FileNotFoundException, IOException {
		String libUri = resource.toURI().toString();
		HDLLibrary.registerLibrary(libUri, lib);
		Set<Problem> syntaxProblems = new HashSet<Problem>();
		HDLPackage parse = PSHDLParser.parse(resource, libUri, syntaxProblems);
		for (Problem issue : syntaxProblems)
			if (issue.severity == ProblemSeverity.ERROR)
				return new CompileResult(syntaxProblems, null, "<ERROR>", null);
		parse = Insulin.resolveFragments(parse);
		Set<Problem> validate = HDLValidator.validate(parse, null);
		boolean hasValidationError = false;
		for (Problem issue : validate) {
			if (issue.severity == ProblemSeverity.ERROR) {
				hasValidationError = true;
			}
			syntaxProblems.add(issue);
		}
		if (hasValidationError)
			return new CompileResult(syntaxProblems, null, "<ERROR>", null);
		if (parse.getUnits().size() > 0) {
			HDLPackage hdlPackage = Insulin.transform(parse, resource.getAbsolutePath());
			String vhdlCode = VhdlOutput.toVhdlString(VHDLPackageExtension.INST.toVHDL(hdlPackage));
			HDLUnit[] units = parse.getAllObjectsOf(HDLUnit.class, false);
			String name = "<Unknown>";
			if (units.length != 0) {
				name = units[0].getName();
			}
			return new CompileResult(syntaxProblems, vhdlCode, name, lib.sideFiles.values());
		}
		return new CompileResult(syntaxProblems, "", "<emptyFile>", lib.sideFiles.values());

	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Stopwatch sw = new Stopwatch().start();
		PSHDLCompiler setup = setup();
		HDLCore.defaultInit();
		System.out.println("Init: " + sw);
		for (String string : args) {
			System.out.print("Compiling:" + string);
			Stopwatch comp = new Stopwatch().start();
			HDLLibrary.registerLibrary(string, setup.lib);
			HDLPackage pkg = loadHDLUnit(new File(string), true, string);
			System.out.print(" loading:" + comp);
			comp.reset().start();
			if (!string.endsWith("vhdl") && !string.endsWith("vhd")) {
				pkg = Insulin.transform(pkg, new File(string).getAbsolutePath());
				System.out.print(" insulin:" + comp);
				comp.reset().start();
				VhdlFile vhdl = VHDLPackageExtension.INST.toVHDL(pkg);
				System.out.print(" vhdl:" + comp);
				comp.reset().start();
				if (vhdl != null) {
					String val = (VhdlOutput.toVhdlString(vhdl));
				}
			}
			System.out.println();
		}
		System.out.println("Done! Total time:" + sw);
	}

	protected static HDLPackage loadHDLUnit(File file, boolean validate, String libURI) {
		String fileName = file.getName();
		if (fileName.endsWith(".vhdl") || fileName.endsWith(".vhd")) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				List<HDLInterface> pkg = VHDLImporter.importFile(HDLQualifiedName.create("VHDL", "work"), fis, HDLLibrary.getLibrary(libURI), file.getAbsolutePath());
				HDLPackage res = new HDLPackage();
				for (HDLInterface hdlInterface : pkg) {
					res.addDeclarations(new HDLInterfaceDeclaration().setHIf(hdlInterface));
				}
				return res;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			Set<Problem> problems = new HashSet<Problem>();
			HDLPackage unit = PSHDLParser.parse(file, libURI, problems);
			if (validate) {
				problems.addAll(HDLValidator.validate(unit, null));
				boolean hasError = false;
				for (Problem problem : problems) {
					System.out.println(problem);
					if (problem.severity == ProblemSeverity.ERROR) {
						hasError = true;
					}
				}
				if (hasError)
					return null;
			}
			unit.validateAllFields(null, false);
			HDLPackage resolveFragments = Insulin.resolveFragments(unit);
			return resolveFragments;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param absolutePath
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 * @throws IOException
	 */
	public void add(File f, Set<Problem> problems) throws IOException {
		add(Files.toString(f, Charsets.UTF_8), f.getAbsolutePath(), problems);
	}

	/**
	 * Parse and add a unit to the HDLLibrary so that all references can be
	 * resolved later
	 * 
	 * @param contents
	 *            the PSHDL module to add
	 * @param absolutePath
	 *            a source file name for this module
	 * @param problems
	 *            syntax errors will be added to this Set when encountered
	 */
	public void add(String contents, String absolutePath, Set<Problem> problems) {
		HDLLibrary.registerLibrary(absolutePath, lib);
		PSHDLParser.parseString(contents, absolutePath, problems, absolutePath);
	}

	public void close() {
		lib.unregister();
		lib = null;
	}
}
