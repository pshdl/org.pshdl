package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.parser.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;
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
		PSHDLCompiler setup = setup();
		for (String string : args) {
			CompileResult vhdl = setup.compileToVHDL(new File(string));
			if (vhdl != null)
				System.out.println(vhdl.vhdlCode);
		}
	}
}
