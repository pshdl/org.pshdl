package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;
import java.util.*;

import com.google.common.base.*;

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
		public List<SideFile> sideFiles;

		public CompileResult(Set<Problem> syntaxProblems, String vhdlCode, String entityName, List<SideFile> sideFiles) {
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
		for (Problem issue : syntaxProblems) {
			if (issue.severity == ProblemSeverity.ERROR)
				return new CompileResult(syntaxProblems, null, "<ERROR>", null);
		}
		Set<Problem> validate = HDLValidator.validate(parse, null);
		boolean hasValidationError = false;
		for (Problem issue : validate) {
			if (issue.severity == ProblemSeverity.ERROR)
				hasValidationError = true;
			syntaxProblems.add(issue);
		}
		if (hasValidationError)
			return new CompileResult(syntaxProblems, null, "<ERROR>", null);
		if (parse.getUnits().size() > 0) {
			HDLPackage hdlPackage = Insulin.transform(parse);
			String vhdlCode = VhdlOutput.toVhdlString(VHDLPackageExtension.INST.toVHDL(hdlPackage));
			HDLUnit[] units = parse.getAllObjectsOf(HDLUnit.class, false);
			String name = "<Unknown>";
			if (units.length != 0)
				name = units[0].getName();
			return new CompileResult(syntaxProblems, vhdlCode, name, lib.sideFiles);
		}
		return new CompileResult(syntaxProblems, "", "<emptyFile>", lib.sideFiles);

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
			HDLPackage pkg = loadHDLUnit(string, true, string);
			System.out.print(" loading:" + comp);
			comp.reset().start();
			if (!string.endsWith("vhdl") && !string.endsWith("vhd")) {
				pkg = Insulin.transform(pkg);
				System.out.print(" insulin:" + comp);
				comp.reset().start();
				VhdlFile vhdl = VHDLPackageExtension.INST.toVHDL(pkg);
				System.out.print(" vhdl:" + comp);
				comp.reset().start();
				if (vhdl != null) {
					String code = (VhdlOutput.toVhdlString(vhdl));
				}
			}
			System.out.println();
		}
		System.out.println("Done! Total time:" + sw);
	}

	protected static HDLPackage loadHDLUnit(String string, boolean validate, String libURI) {
		File file = new File(string);
		if (string.endsWith(".vhdl") || string.endsWith(".vhd")) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				List<HDLInterface> pkg = VHDLImporter.importFile(HDLQualifiedName.create("VHDL", "work"), fis, HDLLibrary.getLibrary(libURI));
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
					if (problem.severity == ProblemSeverity.ERROR)
						hasError = true;
				}
				if (hasError)
					return null;
			}
			unit.validateAllFields(null, false);
			return Insulin.resolveFragments(unit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
