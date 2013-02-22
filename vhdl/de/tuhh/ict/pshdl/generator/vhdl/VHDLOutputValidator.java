package de.tuhh.ict.pshdl.generator.vhdl;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;
import static de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity.*;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.HDLAdvise;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.Problem.ProblemSeverity;

public class VHDLOutputValidator implements IHDLValidator {
	public final static String[] keywords = { "abs", "if", "access", "impure", "after", "in", "alias", "inertial", "all", "inout", "and", "is", "architecture", "label", "array",
			"library", "assert", "linkage", "attribute", "literal", "begin", "loop", "block", "map", "body", "mod", "buffer", "nand", "bus", "new", "case", "next", "component",
			"nor", "configuration", "not", "constant", "null", "disconnect", "of", "downto", "on", "else", "open", "elsif", "or", "end", "others", "entity", "out", "exit",
			"package", "file", "port", "for", "postponed", "function", "procedure", "generate", "process", "generic", "pure", "group", "range", "guarded", "record", "register",
			"reject", "rem", "report", "return", "rol", "ror", "select", "severity", "signal", "shared", "sla", "sll", "sra", "srl", "subtype", "then", "to", "transport", "type",
			"unaffected", "units", "until", "use", "variable", "wait", "when", "while", "with", "xnor", "xor" };

	public final static Set<String> keywordSet;
	static {
		keywordSet = new HashSet<String>();
		for (String keyword : keywords) {
			keywordSet.add(keyword);
		}
	}

	public static enum VHDLErrorCode implements IErrorCode {
		KEYWORD_NAME(WARNING), KEYWORD_TYPE(ERROR);

		public final ProblemSeverity severity;

		VHDLErrorCode(ProblemSeverity severity) {
			this.severity = severity;
		}

		@Override
		public ProblemSeverity getSeverity() {
			return severity;
		}

	}

	@Override
	public Class<?> getErrorClass() {
		return VHDLErrorCode.class;
	}

	@Override
	public HDLAdvise advise(Problem problem) {
		IErrorCode code = problem.code;
		if (code instanceof VHDLErrorCode) {
			VHDLErrorCode vCode = (VHDLErrorCode) code;
			switch (vCode) {
			case KEYWORD_NAME:
				break;
			case KEYWORD_TYPE:
				break;

			}
		}
		return null;
	}

	@Override
	public void check(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		checkNames(unit, problems);
	}

	private void checkNames(HDLPackage unit, Set<Problem> problems) {
		HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			String varName = hdlVariable.getName();
			if (!getVHDLName(varName).equals(varName)) {
				problems.add(new Problem(VHDLErrorCode.KEYWORD_NAME, hdlVariable));
			}
		}
		HDLUnit[] units = unit.getAllObjectsOf(HDLUnit.class, true);
		for (HDLUnit hdlUnit : units) {
			String name = fullNameOf(hdlUnit).toString('_');
			if (keywordSet.contains(name.toLowerCase())) {
				problems.add(new Problem(VHDLErrorCode.KEYWORD_TYPE, hdlUnit));
			}
		}
		HDLInterface[] ifs = unit.getAllObjectsOf(HDLInterface.class, true);
		for (HDLInterface hdlUnit : ifs) {
			String name = fullNameOf(hdlUnit).toString('_');
			if (keywordSet.contains(name.toLowerCase())) {
				problems.add(new Problem(VHDLErrorCode.KEYWORD_TYPE, hdlUnit));
			}
		}
		HDLEnum[] enums = unit.getAllObjectsOf(HDLEnum.class, true);
		for (HDLEnum hdlUnit : enums) {
			String name = fullNameOf(hdlUnit).toString('_');
			if (keywordSet.contains(name.toLowerCase())) {
				problems.add(new Problem(VHDLErrorCode.KEYWORD_TYPE, hdlUnit));
			}
		}
	}

	@Override
	public String getName() {
		return "VHDL Validator";
	}

	public static String getVHDLName(String name) {
		if (keywordSet.contains(name))
			return "\\" + name + "\\";
		if (name.startsWith("_") || name.endsWith("_"))
			return "\\" + name + "\\";
		return name;
	}
}
