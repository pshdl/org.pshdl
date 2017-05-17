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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.RecognitionException;
import org.pshdl.model.*;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLBitOp.HDLBitOpType;
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLRegisterConfig.*;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.NamedElement;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.utils.*;
import org.pshdl.model.validation.Problem;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class ABP3BusCodeGen extends CommonBusCode {

	public static HDLUnit get(String name, Unit unit, List<Row> rows, boolean defaultClock, boolean defaultReset) {
		final HDLInterface hdi = MemoryModel.buildHDLInterface(unit, rows);
		final Map<String, Boolean> isArray = buildArrayMap(hdi);
		final int addrWidth = ((int) Math.ceil(Math.log10(rows.size() * 4) / Math.log10(2)));
		HDLVariable pclkVar = new HDLVariable().setName("PCLK");
		HDLVariableDeclaration pclk = new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()).addVariables(pclkVar);
		if (defaultClock)
			pclk = pclk.addAnnotations(HDLBuiltInAnnotations.clock.create(null));
		HDLVariableDeclaration preset = new HDLVariableDeclaration().setDirection(HDLDirection.INTERNAL).setType(HDLPrimitive.getBit()).addVariables(new HDLVariable()
				.setName("rst").setDefaultValue(new HDLManip().setType(HDLManipType.BIT_NEG).setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("PRESETn")))));
		if (defaultReset)
			preset = preset.addAnnotations(HDLBuiltInAnnotations.reset.create(null));
		HDLVariable presetVar = new HDLVariable().setName("PRESETn");
		HDLUnit res = new HDLUnit().setName(name).addStatements(pclk)
				.addStatements(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()).addVariables(presetVar))
				.addStatements(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()).addVariables(new HDLVariable().setName("PENABLE")))
				.addStatements(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()).addVariables(new HDLVariable().setName("PSEL")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBitvector(addrWidth)).addVariables(new HDLVariable().setName("PADDR")))
				.addStatements(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()).addVariables(new HDLVariable().setName("PWRITE")))
				.addStatements(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBitvector(32)).addVariables(new HDLVariable().setName("PWDATA")))
				.addStatements(new HDLVariableDeclaration().setRegister(createRegister(defaultClock, pclkVar, defaultReset, presetVar)).setDirection(HDLDirection.OUT)
						.setType(HDLPrimitive.getBitvector(32)).addVariables(new HDLVariable().setName("PRDATA")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLPrimitive.getBit())
								.addVariables(
										new HDLVariable().setName("PREADY").setDefaultValue(new HDLLiteral().setVal("1"))))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.OUT)
								.setType(
										HDLPrimitive.getBit())
								.addVariables(
										new HDLVariable()
												.setName(
														"PSLVERR")
												.setDefaultValue(
														new HDLLiteral()
																.setVal("0"))))
				.addStatements(
						new HDLIfStatement()
								.setIfExp(
										new HDLBitOp()
												.setLeft(
														new HDLBitOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PWRITE")))
																.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("PSEL"))).setType(HDLBitOpType.LOGI_AND))
												.setRight(
														new HDLVariableRef()
																.setVar(HDLQualifiedName
																		.create("PENABLE")))
												.setType(
														HDLBitOpType.LOGI_AND))
								.addThenDo(createWriteSwitch(rows, isArray)))
				.addStatements(
						new HDLIfStatement()
								.setIfExp(new HDLBitOp()
										.setLeft(new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PWRITE"))).setRight(new HDLLiteral().setVal("0"))
												.setType(HDLEqualityOpType.EQ))
										.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("PSEL"))).setType(HDLBitOpType.LOGI_AND))
								.addThenDo(createReadSwitch(rows, isArray)).addElseDo(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PRDATA")))
										.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0"))))
				.setSimulation(false);
		for (final HDLVariableDeclaration port : hdi.getPorts()) {
			res = res.addStatements(port.setDirection(HDLDirection.INTERNAL).setRegister(createRegister(defaultClock, pclkVar, defaultReset, presetVar)));
		}
		return res;
	}

	private static HDLRegisterConfig createRegister(boolean defaultClock, HDLVariable clockVar, boolean defaultReset, HDLVariable resetVar) {
		HDLRegisterConfig config = HDLRegisterConfig.defaultConfig().setClockType(HDLRegClockType.RISING);
		if (!defaultClock)
			config = config.setClk(clockVar.asHDLRef());
		if (!defaultReset)
			config = config.setRst(resetVar.asHDLRef()).setResetType(HDLRegResetActiveType.LOW);
		return config;
	}

	private static HDLSwitchStatement createWriteSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement hsl = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("PADDR")));
		int pos = 0;
		final Map<String, Integer> intPos = Maps.newLinkedHashMap();
		for (final Row row : rows) {
			hsl = hsl.addCases(createWriteCase(row, pos++, intPos, isArray));
		}
		hsl = hsl.addCases(new HDLSwitchCaseStatement().setLabel(null));
		return hsl;
	}

	private static HDLSwitchCaseStatement createWriteCase(Row row, int pos, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(pos * 4l));
		final HDLVariableRef busData = new HDLVariableRef().setVar(HDLQualifiedName.create("PWDATA"));
		int bitPos = 31;
		for (final NamedElement ne : row.definitions) {
			final Definition def = (Definition) ne;
			final int size = MemoryModel.getSize(def);
			if ((def.rw == RWType.rw) || (def.rw == RWType.w)) {
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.getName(row)));
				target = createRef(intPos, isArray, def, target, row);
				final HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target).setType(HDLAssignmentType.ASSGN).setRight(busData.addBits(range)));
			}
			bitPos -= size;
		}
		return label;
	}

	private static HDLSwitchStatement createReadSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement res = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("PADDR")));
		int pos = 0;
		final Map<String, Integer> intPos = Maps.newLinkedHashMap();
		for (final Row row : rows) {
			final int reg = pos++;
			res = res.addCases(createReadCase(row, reg, intPos, isArray, "PRDATA", HDLLiteral.get(reg * 4)));
		}
		res = res.addCases(new HDLSwitchCaseStatement().addDos(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PRDATA")))
				.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0"))));
		return res;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, RecognitionException {
		final Unit unit = MemoryModelAST.parseUnit(Files.toString(new File(args[0]), Charsets.UTF_8), new LinkedHashSet<Problem>(), 0);
		System.out.println(unit);
		System.out.println(get("Bla", unit, MemoryModel.buildRows(unit), true, false));
	}
}
