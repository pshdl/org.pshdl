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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.types.builtIn.busses.memorymodel.Column;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.types.builtIn.busses.memorymodel.Memory;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.NamedElement;
import org.pshdl.model.types.builtIn.busses.memorymodel.Reference;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.collect.Maps;

public class UserLogicCodeGen extends CommonBusCode {
	public static HDLUnit get(String name, Unit unit, List<Row> rows) {
		final HDLInterface hdi = MemoryModel.buildHDLInterface(unit, rows);
		final Map<String, Boolean> isArray = buildArrayMap(hdi);
		final int regCount = rows.size();
		final HDLVariableDeclaration Bus2IP_RdCE = new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit<" + regCount + ">"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("Bus2IP_RdCE"));
		final HDLVariableDeclaration Bus2IP_WrCE = new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit<" + regCount + ">"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("Bus2IP_WrCE"));
		final HDLVariableDeclaration Bus2IP_Clk = new HDLVariableDeclaration().setDirection(HDLDirection.IN).addAnnotations(new HDLAnnotation().setName("@clock"))
				.addAnnotations(new HDLAnnotation().setName("@VHDLAttribute").setValue("SIGIS=CLK")).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("Bus2IP_Clk"));
		final HDLVariableDeclaration Bus2IP_Reset = new HDLVariableDeclaration().setDirection(HDLDirection.IN).addAnnotations(new HDLAnnotation().setName("@reset"))
				.addAnnotations(new HDLAnnotation().setName("@VHDLAttribute").setValue("SIGIS=RST")).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("Bus2IP_Reset"));
		final HDLVariableDeclaration IP2Bus_RdAck = new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(new HDLVariable().setName("IP2Bus_RdAck")
						.setDefaultValue(new HDLManip().setType(HDLManipType.CAST).setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_RdCE")))
								.setCastTo(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.UINT).setWidth(HDLLiteral.get(regCount)))));
		final HDLVariableDeclaration IP2Bus_WrAck = new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(new HDLVariable().setName("IP2Bus_WrAck")
						.setDefaultValue(new HDLManip().setType(HDLManipType.CAST).setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_WrCE")))
								.setCastTo(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.UINT).setWidth(HDLLiteral.get(regCount)))));
		final HDLVariableDeclaration IP2Bus_Error = new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(new HDLVariable().setName("IP2Bus_Error").setDefaultValue(new HDLLiteral().setVal("0")));
		final HDLVariableDeclaration IP2Bus_Data = new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit<32>"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(HDLLiteral.get(32)))
				.addVariables(new HDLVariable().setName("IP2Bus_Data").setDefaultValue(new HDLLiteral().setVal("0")));
		final HDLVariableDeclaration slv_reg_read_sel = new HDLVariableDeclaration().setDirection(HDLDirection.INTERNAL)
				.setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("slv_reg_read_sel").setDefaultValue(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_RdCE"))));
		final HDLVariableDeclaration Bus2IP_Data = new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit<32>"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(HDLLiteral.get(32)))
				.addVariables(new HDLVariable().setName("Bus2IP_Data"));
		final HDLVariableDeclaration slv_reg_write_sel = new HDLVariableDeclaration().setDirection(HDLDirection.INTERNAL)
				.setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("slv_reg_write_sel").setDefaultValue(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_WrCE"))));
		final HDLVariableDeclaration Bus2IP_BE = new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLQualifiedName.create("#bit<4>"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(HDLLiteral.get(4)))
				.addVariables(new HDLVariable().setName("Bus2IP_BE"));
		final HDLSwitchStatement writeSwitch = createWriteSwitch(rows, isArray);
		final HDLSwitchStatement readSwitch = createReadSwitch(rows, isArray);
		HDLUnit hUnit = new HDLUnit().setSimulation(false).setName(name).addStatements(Bus2IP_RdCE).addStatements(Bus2IP_WrCE).addStatements(Bus2IP_Clk).addStatements(Bus2IP_Reset)
				.addStatements(IP2Bus_RdAck).addStatements(IP2Bus_WrAck).addStatements(IP2Bus_Error).addStatements(IP2Bus_Data).addStatements(slv_reg_read_sel)
				.addStatements(readSwitch).addStatements(Bus2IP_Data).addStatements(slv_reg_write_sel).addStatements(Bus2IP_BE).addStatements(writeSwitch);
		for (final HDLVariableDeclaration port : hdi.getPorts()) {
			hUnit = hUnit.addStatements(port.setDirection(HDLDirection.INTERNAL));
		}
		return hUnit;
	}

	private static HDLSwitchStatement createWriteSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement hsl = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_write_sel")));
		int pos = 0;
		final Map<String, Integer> intPos = Maps.newLinkedHashMap();
		for (final Row row : rows) {
			hsl = hsl.addCases(createWriteCase(row, rows.size(), pos++, intPos, isArray));
		}
		hsl = hsl.addCases(new HDLSwitchCaseStatement().setLabel(null));
		return hsl;
	}

	private static HDLSwitchCaseStatement createWriteCase(Row row, int rowCount, int pos, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(BigInteger.ONE.shiftLeft((rowCount - 1) - pos)));
		final HDLVariableRef busData = new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_Data"));
		int bitPos = 31;
		final HDLVariableRef be = new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_BE"));
		HDLIfStatement ifStatement = null;
		int ifBitPos = 31;
		for (final NamedElement ne : row.definitions) {
			final Definition def = (Definition) ne;
			if ((def.rw == RWType.rw) || (def.rw == RWType.w)) {
				final int size = MemoryModel.getSize(def);
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.getName(row)));
				target = createRef(intPos, isArray, def, target, row);
				boolean done = false;
				int targetIdx = size - 1;
				// a[i]{16:9}=Bus2IP_Data{31:24};
				// a[i]{8:1}=Bus2IP_Data{23:16};
				// a[i]{0}=Bus2IP_Data{15};
				// b[i]=Bus2IP_Data{14:10};
				// c[i]{7:6}=Bus2IP_Data{9:8};
				// c[i]{5:0}=Bus2IP_Data{7:2}; //6bit
				// d[i]=Bus2IP_Data{1:0}; //2bit
				if (((bitPos / 8) != (ifBitPos / 8)) && (ifStatement != null)) {
					label = label.addDos(ifStatement);
					ifStatement = new HDLIfStatement().setIfExp(be.addBits(getRange(bitPos / 8, 1)));
					ifBitPos = bitPos;
				}
				while (!done) {
					final int remainingBits = bitPos % 8;
					if (targetIdx > remainingBits) {
						HDLReference temp;
						if (def.width == -1) {
							temp = target;
						} else {
							temp = target.addBits(getRange(targetIdx, remainingBits + 1));
						}
						final HDLRange busRange = getRange(bitPos, remainingBits + 1);
						if (ifStatement == null) {
							ifStatement = new HDLIfStatement().setIfExp(be.addBits(getRange(bitPos / 8, 1)));
						}
						ifStatement = ifStatement.addThenDo(new HDLAssignment().setLeft(temp).setType(HDLAssignmentType.ASSGN).setRight(busData.addBits(busRange)));
						bitPos -= remainingBits + 1;
						targetIdx -= remainingBits + 1;
						if ((bitPos / 8) != (ifBitPos / 8)) {
							label = label.addDos(ifStatement);
							ifStatement = new HDLIfStatement().setIfExp(be.addBits(getRange(bitPos / 8, 1)));
							ifBitPos = bitPos;
						}
					} else {
						HDLReference temp;
						if (def.width == -1) {
							temp = target;
						} else {
							temp = target.addBits(getRange(targetIdx, targetIdx + 1));
						}
						final HDLRange busRange = getRange(bitPos, targetIdx + 1);
						if (ifStatement == null) {
							ifStatement = new HDLIfStatement().setIfExp(be.addBits(getRange(bitPos / 8, 1)));
							ifBitPos = bitPos;
						}
						ifStatement = ifStatement.addThenDo(new HDLAssignment().setLeft(temp).setType(HDLAssignmentType.ASSGN).setRight(busData.addBits(busRange)));
						bitPos -= targetIdx + 1;
						done = true;
					}
				}
			} else {
				bitPos -= MemoryModel.getSize(def);
			}
		}
		if ((ifStatement != null) && !ifStatement.getThenDo().isEmpty()) {
			label = label.addDos(ifStatement);
		}
		return label;
	}

	private static HDLSwitchStatement createReadSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		final HDLSwitchCaseStatement defaultCase = new HDLSwitchCaseStatement().addDos(new HDLAssignment()
				.setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("IP2Bus_Data"))).setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0")));
		HDLSwitchStatement hdlSwitchStatement = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_read_sel")));
		int pos = 0;
		final Map<String, Integer> intPos = Maps.newLinkedHashMap();
		final int regCount = rows.size();
		for (final Row row : rows) {
			final int reg = pos++;
			hdlSwitchStatement = hdlSwitchStatement
					.addCases(createReadCase(row, reg, intPos, isArray, "IP2Bus_Data", HDLLiteral.get(BigInteger.ONE.shiftLeft(regCount - reg - 1))));
		}
		hdlSwitchStatement = hdlSwitchStatement.addCases(defaultCase);
		return hdlSwitchStatement;
	}

	public static Unit createAdderBus(int regCount) {
		final Definition a = new Definition("a", true, RWType.rw, Type.UINT, 16);
		final Definition b = new Definition("b", true, RWType.rw, Type.UINT, 16);
		final Definition result = new Definition("result", true, RWType.rw, Type.UINT, 16);
		final Row rowIn = new Row("input", null, a, b);
		final Row rowOut = new Row("output", null, new Reference("fill"), result);
		final Column col = new Column("adder");
		col.rows.add(rowIn);
		col.rows.add(rowOut);
		final Memory mem = new Memory(new Reference("adder", regCount));
		final Unit unit = new Unit();
		unit.declarations.put("input", rowIn);
		unit.declarations.put("output", rowOut);
		unit.declarations.put("adder", col);
		unit.memory = mem;
		return unit;
	}

	public static Unit create(int regCount) {
		final Definition a = new Definition("a", true, RWType.rw, Type.BIT, 17);
		final Definition b = new Definition("b", true, RWType.rw, Type.BIT, 5);
		final Definition c = new Definition("c", true, RWType.rw, Type.BIT, 8);
		final Definition d = new Definition("d", true, RWType.rw, Type.BIT, 2);
		final Row row = new Row("reg", null, a, b, c, d);
		final Memory mem = new Memory(new Reference("reg", regCount));
		final Unit unit = new Unit();
		unit.declarations.put("reg", row);
		unit.memory = mem;
		return unit;
	}

	public static void main(String[] args) {
		// Unit defaultUnit = BusGenerator.createDefaultUnit(5);
		final Unit defaultUnit = createAdderBus(3);
		final HDLUnit unit = get("Bla", defaultUnit, MemoryModel.buildRows(defaultUnit));
		System.out.println(unit);
	}
}
