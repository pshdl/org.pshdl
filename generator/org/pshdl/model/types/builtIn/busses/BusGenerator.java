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
package org.pshdl.model.types.builtIn.busses;

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.parser.SourceInfo;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.types.builtIn.busses.memorymodel.Memory;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModelSideFiles;
import org.pshdl.model.types.builtIn.busses.memorymodel.NamedElement;
import org.pshdl.model.types.builtIn.busses.memorymodel.Reference;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.ModificationSet;
import org.pshdl.model.utils.services.CompilerInformation.GeneratorInformation;
import org.pshdl.model.utils.services.IHDLAnnotation;
import org.pshdl.model.utils.services.IHDLAnnotationProvider;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.Problem.ProblemSeverity;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.TreeMultimap;

public class BusGenerator implements IHDLGenerator, IHDLAnnotationProvider {

	public static final char ROW_SEPARTOR = ';';
	private static final IHDLAnnotation.AbstractAnnotation busTagetSignal = new IHDLAnnotation.AbstractAnnotation("busTargetSignal", BusGenerator.class,
			"Annotation used by the Busgenerator to annotate variables that can be modified", "A list of rows that use this signal separated by:'" + ROW_SEPARTOR + "'") {

		@Override
		public String validate(HDLAnnotation annotation) {
			return null;
		}
	};
	private static final IHDLAnnotation.AbstractAnnotation busDescription = new IHDLAnnotation.AbstractAnnotation("busDescription", BusGenerator.class,
			"Annotation used by the Busgenerator to give a full representation of the bus system", "A list of rows that use this signal separated by:'" + ROW_SEPARTOR + "'") {

		@Override
		public String validate(HDLAnnotation annotation) {
			return null;
		}
	};

	public static enum BusErrors implements IErrorCode {
		invalid_reference, underfilled_row, overfilled_row;

		@Override
		public ProblemSeverity getSeverity() {
			return ProblemSeverity.ERROR;
		}

	}

	@Override
	public Optional<HDLInterface> getInterface(HDLDirectGeneration hdl) {
		final String name = hdl.getIfName();
		if (hdl.getGeneratorContent().length() != 0) {
			try {
				return createInterface(hdl, name);
			} catch (final Exception e) {
				return Optional.absent();
			}
		}
		try {
			final Unit unit = createDefaultUnit(getRegCount(hdl));
			final List<Row> rows = MemoryModel.buildRows(unit);
			final HDLInterface bId = MemoryModel.buildHDLInterface(unit, rows).setContainer(hdl).setName(name);
			return Optional.of(bId);
		} catch (final Exception e) {
		}
		return Optional.absent();
	}

	private Optional<HDLInterface> createInterface(HDLDirectGeneration hdl, String name) {
		final Optional<Unit> unitOpt = parse(hdl, Sets.<Problem> newHashSet());
		if (unitOpt.isPresent()) {
			final Unit unit = unitOpt.get();
			final List<Row> rows = MemoryModel.buildRows(unit);
			final HDLInterface bId = MemoryModel.buildHDLInterface(unit, rows).setContainer(hdl).setName(name);
			return Optional.of(bId);
		}
		return Optional.absent();
	}

	private Optional<Unit> parse(HDLDirectGeneration hdl, final Set<Problem> problems) {
		Unit unit;
		final int lineOffset = getLineOffset(hdl);
		try {
			unit = MemoryModelAST.parseUnit(getContentStream(hdl), problems, lineOffset);
			if (!validate(unit, problems, lineOffset))
				return Optional.absent();
		} catch (final IOException e) {
			return Optional.absent();
		}
		return Optional.of(unit);
	}

	private int getLineOffset(HDLDirectGeneration hdl) {
		int lineOffset = 0;
		final SourceInfo si = Problem.findMeta(hdl);
		if (si != null) {
			lineOffset = si.startLine;
		}
		return lineOffset;
	}

	private boolean validate(Unit unit, Set<Problem> problems, int lineOffset) {
		if (unit == null)
			return false;
		for (final Problem problem : problems)
			if (problem.severity == ProblemSeverity.ERROR)
				return false;
		boolean hasError = false;
		for (final Entry<String, NamedElement> entry : unit.declarations.entrySet())
			if (entry.getValue() instanceof Reference) {
				final Reference ref = (Reference) entry.getValue();
				final NamedElement decl = unit.declarations.get(ref.getName());
				if ((decl == null) && !"fill".equals(ref.name)) {
					final String message = "Can not resolve the reference to object:" + ref.name;
					problems.add(new Problem(BusErrors.invalid_reference, message, ref.token.getLine() + lineOffset, ref.token.getCharPositionInLine(), ref.token.getText()
							.length(), ref.token.getStartIndex()));
					hasError = true;
				}
			}
		return !hasError;
	}

	private String getContentStream(HDLDirectGeneration hdl) {
		final String generatorContent = hdl.getGeneratorContent();
		final String substring = generatorContent.substring(2, generatorContent.length() - 2);
		return substring;
	}

	private int getMemCount(HDLDirectGeneration hdl) {
		final ArrayList<HDLArgument> args = hdl.getArguments();
		int memCount = 0;
		for (final HDLArgument arg : args)
			if ("memCount".equals(arg.getName())) {
				HDLExpression expression = arg.getExpression();
				expression = expression.copyDeepFrozen(expression.getContainer());
				final Optional<BigInteger> regVal = ConstantEvaluate.valueOf(expression);
				if (regVal.isPresent()) {
					memCount = regVal.get().intValue();
				} else if (expression instanceof HDLLiteral) {
					final HDLLiteral lit = (HDLLiteral) expression;
					// String literals are allowed as well...
					if (lit.getStr()) {
						try {
							memCount = Integer.parseInt(lit.getVal());
						} catch (final NumberFormatException e) {
							throw new IllegalArgumentException("The value of the parameter regCount is not valid! It is not a valid integer.");
						}
					}
				} else
					throw new IllegalArgumentException("The value of the parameter regCount is not valid! It probably is not constant.");
			}
		return memCount;
	}

	private int getRegCount(HDLDirectGeneration hdl) {
		final ArrayList<HDLArgument> args = hdl.getArguments();
		int regCount = -1;
		for (final HDLArgument arg : args)
			if ("regCount".equals(arg.getName())) {
				HDLExpression expression = arg.getExpression();
				expression = expression.copyDeepFrozen(expression.getContainer());
				final Optional<BigInteger> regVal = ConstantEvaluate.valueOf(expression);
				if (regVal.isPresent()) {
					regCount = regVal.get().intValue();
				} else if (expression instanceof HDLLiteral) {
					final HDLLiteral lit = (HDLLiteral) expression;
					// String literals are allowed as well...
					if (lit.getStr()) {
						try {
							regCount = Integer.parseInt(lit.getVal());
						} catch (final NumberFormatException e) {
							throw new IllegalArgumentException("The value of the parameter regCount is not valid! It is not a valid integer.");
						}
					}
				} else
					throw new IllegalArgumentException("The value of the parameter regCount is not valid! It probably is not constant.");
			}
		if (regCount == -1)
			throw new IllegalArgumentException("The parameter regCount is not present!");
		return regCount;
	}

	@Override
	public Optional<HDLGenerationInfo> getImplementation(HDLDirectGeneration hdl) {
		try {
			Unit unit;
			final int memCount = getMemCount(hdl);
			if (hdl.getGeneratorContent().length() == 0) {
				unit = createDefaultUnit(getRegCount(hdl));
			} else {
				try {
					final Set<Problem> problems = Sets.newHashSet();
					unit = MemoryModelAST.parseUnit(getContentStream(hdl), problems, 0);
					if (!validate(unit, problems, 0))
						return Optional.absent();
				} catch (final Exception e) {
					throw new IllegalArgumentException("Invalid input:" + hdl.getGeneratorContent());
				}
			}
			final String version = getVersion(hdl);
			final List<Row> rows = MemoryModel.buildRows(unit);
			final byte[] html = MemoryModelSideFiles.builtHTML(unit, rows, true);
			final List<AuxiliaryContent> sideFiles = new LinkedList<AuxiliaryContent>();
			sideFiles.add(new AuxiliaryContent(hdl.getVar().getName() + "Map.html", html, true));
			final HDLUnit containerUnit = hdl.getContainer(HDLUnit.class);
			sideFiles.addAll(MemoryModelSideFiles.getSideFiles(containerUnit, unit, rows, version, true));
			if (hdl.getGeneratorID().equalsIgnoreCase("plb")) {
				HDLGenerationInfo hdgi = new HDLGenerationInfo(UserLogicCodeGen.get("org.plb", unit, rows));
				hdgi = annotateSignals(hdgi, unit);
				sideFiles.addAll(BusGenSideFiles.getSideFiles(containerUnit, rows.size(), memCount, version, false, true));
				hdgi.files.addAll(sideFiles);
				return Optional.of(hdgi);
			}
			if (hdl.getGeneratorID().equalsIgnoreCase("axi")) {
				HDLGenerationInfo hdgi = new HDLGenerationInfo(UserLogicCodeGen.get("org.axi", unit, rows));
				hdgi = annotateSignals(hdgi, unit);
				sideFiles.addAll(BusGenSideFiles.getSideFiles(containerUnit, rows.size(), memCount, version, true, true));
				hdgi.files.addAll(sideFiles);
				return Optional.of(hdgi);
			}
			if (hdl.getGeneratorID().equalsIgnoreCase("apb")) {
				HDLGenerationInfo hdgi = new HDLGenerationInfo(ABP3BusCodeGen.get("org.apb", unit, rows));
				hdgi = annotateSignals(hdgi, unit);
				hdgi.files.addAll(sideFiles);
				return Optional.of(hdgi);
			}
		} catch (final Exception e) {
			return Optional.absent();
		}
		throw new IllegalArgumentException("Can not handle generator ID:" + hdl.getGeneratorID());
	}

	private HDLGenerationInfo annotateSignals(HDLGenerationInfo hdgi, Unit unit) {
		final HDLUnit hdlUnit = hdgi.unit;
		final ModificationSet ms = new ModificationSet();
		ms.addTo(hdlUnit, HDLUnit.fAnnotations, busDescription.create(unit.toCompactString()));
		final Multimap<String, String> mm = TreeMultimap.create();
		for (final Entry<String, NamedElement> e : unit.declarations.entrySet()) {
			final NamedElement value = e.getValue();
			if (value instanceof Row) {
				final Row row = (Row) value;
				for (final NamedElement def : row.definitions) {
					final String dName = def.getName();
					mm.put(dName, row.name);
				}
			}
		}
		for (final Entry<String, Collection<String>> e : mm.asMap().entrySet()) {
			final String dName = e.getKey();
			final String row = Joiner.on(ROW_SEPARTOR).join(e.getValue());
			final Collection<HDLVariable> lastSegmentIs = HDLQuery.select(HDLVariable.class).from(hdlUnit).where(HDLVariable.fName).lastSegmentIs(dName).getAll();
			for (final HDLVariable hdlVariable : lastSegmentIs) {
				ms.replace(hdlVariable, hdlVariable.addAnnotations(busTagetSignal.create(row)));
			}
		}
		final HDLGenerationInfo hdgiRef = new HDLGenerationInfo(ms.apply(hdlUnit));
		hdgiRef.files = hdgi.files;
		return hdgiRef;
	}

	public static Unit createDefaultUnit(int regCount) {
		final Definition def = new Definition("regs", true, RWType.rw, Type.BIT, 32);
		final Row row = new Row("reg", null, def);
		final Memory mem = new Memory(new Reference("reg", regCount));
		final Unit unit = new Unit();
		unit.declarations.put("reg", row);
		unit.memory = mem;
		return unit;
	}

	private String getVersion(HDLDirectGeneration hdl) {
		for (final HDLArgument arg : hdl.getArguments())
			if ("version".equals(arg.getName()))
				return ((HDLLiteral) arg.getExpression()).getVal();
		return "v1_00_a";
	}

	@Override
	public String[] getNames() {
		return new String[] { "plb", "axi", "apb" };
	}

	@Override
	public void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context) {
		if (!hdg.getInclude()) {
			problems.add(new Problem(ErrorCode.GENERATOR_WARNING, hdg, "The " + hdg.getGeneratorID()
					+ " generator assumes to be included. Not including it means that all ports need to be exported manually."));
		}
		if (hdg.getGeneratorContent().length() == 0) {
			try {
				getRegCount(hdg);
			} catch (final Exception e) {
				problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, e.getMessage()));
			}
		} else {
			parse(hdg, problems);
			if (problems.size() == 0) {
				final String name = fullNameOf(hdg).append(hdg.getIfName()).toString();
				try {
					final Optional<HDLInterface> intf = createInterface(hdg, name);
					if (!intf.isPresent()) {
						problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, "Failed to generate interface from description"));
					}
				} catch (final Exception e) {
					problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, e.getMessage()));
				}
			}
		}
		final HDLUnit unit = hdg.getContainer(HDLUnit.class);
		if (unit == null) {
			problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, "Generator needs to be included in a module"));
		} else {
			final ArrayList<HDLVariableDeclaration> ports = unit.asInterface().getPorts();
			for (final HDLVariableDeclaration port : ports) {
				if (port.getDirection() == HDLDirection.INOUT) {
					problems.add(new Problem(ErrorCode.GENERATOR_ERROR, port, "The generator " + hdg.getGeneratorID()
							+ " does not support generating the required files for ports with inout direction"));
				}
			}
		}
		final String version = getVersion(hdg);
		if (!version.matches("v\\d_\\d\\d_[a-z]")) {
			problems.add(new Problem(ErrorCode.GENERATOR_ERROR, hdg, "The version string:" + version + " is not valid. It has to be of the format v[0-9]_[0-9][0-9]_[a-z]"));
		}
	}

	@Override
	public List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl) {
		final Optional<HDLGenerationInfo> opt = getImplementation(hdl);
		if (opt.isPresent()) {
			final List<HDLVariableDeclaration> res = new LinkedList<HDLVariableDeclaration>();
			final HDLVariableDeclaration[] hvd = opt.get().unit.getAllObjectsOf(HDLVariableDeclaration.class, true);
			for (final HDLVariableDeclaration hdlVariableDeclaration : hvd) {
				switch (hdlVariableDeclaration.getDirection()) {
				case CONSTANT:
				case PARAMETER:
				case IN:
				case INOUT:
				case OUT:
					res.add(hdlVariableDeclaration.addAnnotations(HDLBuiltInAnnotations.genSignal.create(hdl.getIfName().toString())));
					break;
				case HIDDEN:
				case INTERNAL:
					break;
				}
			}
			return res;
		}
		return Lists.newLinkedList();
	}

	@Override
	public GeneratorInformation getGeneratorInfo(String name) {
		final GeneratorInformation gi = new GeneratorInformation(BusGenerator.class.getSimpleName(), name, "Generate the infrastructure to create a pcore for the " + name
				+ " bus. This generator should always be included.");
		gi.arguments.put("regCount",
				"This parameter is mandatory. It indicates how many sw registers should be accessible in the ip core. The number can be a constant or a string");
		gi.arguments.put("version", "The version ID to use for the generated pcore");
		return gi;
	}

	@Override
	public IHDLAnnotation[] getAnnotations() {
		return new IHDLAnnotation[] { busTagetSignal };
	}

}
