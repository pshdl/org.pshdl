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
package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;

import com.google.common.base.*;
import com.google.common.io.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;

public class ABP3BusCodeGen extends CommonBusCode {

	public static HDLUnit get(String name, Unit unit, List<Row> rows) {
		HDLInterface hdi = MemoryModel.buildHDLInterface(unit, rows);
		Map<String, Boolean> isArray = buildArrayMap(hdi);
		int addrWidth = ((int) Math.ceil(Math.log10(rows.size() * 4) / Math.log10(2)));
		HDLUnit res = new HDLUnit()
				.setName(name)
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).addAnnotations(new HDLAnnotation().setName("@clock")).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("PCLK")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("PRESETn")))
				.addStatements(
						new HDLVariableDeclaration()
								.setDirection(HDLDirection.INTERNAL)
								.addAnnotations(new HDLAnnotation().setName("@reset"))
								.setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
								.addVariables(
										new HDLVariable().setName("rst").setDefaultValue(
												new HDLManip().setType(HDLManipType.BIT_NEG).setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("PRESETn"))))))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("PENABLE")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("PSEL")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(addrWidth)))
								.addVariables(new HDLVariable().setName("PADDR")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("PWRITE")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit<32>"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLLiteral().setVal("32")))
								.addVariables(new HDLVariable().setName("PWDATA")))
				.addStatements(
						new HDLVariableDeclaration()
								.setRegister(
										new HDLRegisterConfig().setClk(HDLQualifiedName.create("$clk")).setRst(HDLQualifiedName.create("$rst"))
												.setClockType(HDLRegClockType.FALLING).setResetValue(new HDLLiteral().setVal("0"))).setDirection(HDLDirection.OUT)
								.setType(HDLQualifiedName.create("#bit<32>"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLLiteral().setVal("32")))
								.addVariables(new HDLVariable().setName("PRDATA")))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
								.addVariables(new HDLVariable().setName("PREADY").setDefaultValue(new HDLLiteral().setVal("1"))))
				.addStatements(
						new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
								.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
								.addVariables(new HDLVariable().setName("PSLVERR").setDefaultValue(new HDLLiteral().setVal("0"))))
				.addStatements(
						new HDLIfStatement().setIfExp(
								new HDLBitOp()
										.setLeft(
												new HDLBitOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PWRITE")))
														.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("PSEL"))).setType(HDLBitOpType.LOGI_AND))
										.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("PENABLE"))).setType(HDLBitOpType.LOGI_AND)).addThenDo(
								createWriteSwitch(rows, isArray)))
				.addStatements(
						new HDLIfStatement()
								.setIfExp(
										new HDLBitOp()
												.setLeft(
														new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PWRITE")))
																.setRight(new HDLLiteral().setVal("0")).setType(HDLEqualityOpType.EQ))
												.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("PSEL"))).setType(HDLBitOpType.LOGI_AND))
								.addThenDo(createReadSwitch(rows, isArray))
								.addElseDo(
										new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PRDATA"))).setType(HDLAssignmentType.ASSGN)
												.setRight(new HDLLiteral().setVal("0")))).setSimulation(false);
		for (HDLVariableDeclaration port : hdi.getPorts()) {
			res = res.addStatements(port.setDirection(HDLDirection.INTERNAL));
		}
		return res;
	}

	private static HDLSwitchStatement createWriteSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement hsl = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("PADDR")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		for (Row row : rows) {
			hsl = hsl.addCases(createWriteCase(row, pos++, intPos, isArray));
		}
		hsl = hsl.addCases(new HDLSwitchCaseStatement().setLabel(null));
		return hsl;
	}

	private static HDLSwitchCaseStatement createWriteCase(Row row, int pos, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(pos * 4));
		HDLVariableRef busData = new HDLVariableRef().setVar(HDLQualifiedName.create("PWDATA"));
		int bitPos = 31;
		for (NamedElement ne : row.definitions) {
			Definition def = (Definition) ne;
			int size = MemoryModel.getSize(def);
			if ((def.rw == RWType.rw) || (def.rw == RWType.w)) {
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.name));
				target = createRef(intPos, isArray, def, target);
				HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target).setType(HDLAssignmentType.ASSGN).setRight(busData.addBits(range)));
			}
			bitPos -= size;
		}
		return label;
	}

	private static HDLSwitchStatement createReadSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement res = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("PADDR")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		for (Row row : rows) {
			int reg = pos++;
			res = res.addCases(createReadCase(row, reg, intPos, isArray, "PRDATA", HDLLiteral.get(reg * 4)));
		}
		res = res.addCases(new HDLSwitchCaseStatement().addDos(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("PRDATA")))
				.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0"))));
		return res;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, RecognitionException {
		Unit unit = MemoryModelAST.parseUnit(Files.toString(new File(args[0]), Charsets.UTF_8), new HashSet<Problem>());
		System.out.println(unit);
		System.out.println(get("Bla", unit, MemoryModel.buildRows(unit)));
	}
}
