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

import static org.pshdl.model.extensions.FullNameExtension.*;
import static org.pshdl.model.validation.Problem.ProblemSeverity.*;

import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.HDLValidator.*;
import org.pshdl.model.validation.Problem.*;

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
				return new HDLAdvise(problem, "The used name is a keyword or an extended identifier in VHDL",
						"Keywords will be escaped using the extended identifier convention, this may look strange, but is fully working",
						"Don't use a VHDL keyword or don't end with an underscore as identifier");
			case KEYWORD_TYPE:
				return new HDLAdvise(problem, "The used type name is a keyword in VHDL", "Keywords are not supported for type names", "Don't use a VHDL keyword as identifier");

			}
		}
		return null;
	}

	@Override
	public boolean check(HDLPackage unit, Set<Problem> problems, Map<HDLQualifiedName, HDLEvaluationContext> context) {
		checkNames(unit, problems);
		return true;
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
