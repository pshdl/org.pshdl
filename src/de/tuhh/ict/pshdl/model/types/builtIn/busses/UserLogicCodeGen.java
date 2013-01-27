package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class UserLogicCodeGen extends CommonBusCode {
	public static HDLUnit get(String name, Unit unit, List<Row> rows) {
		HDLInterface hdi = MemoryModel.buildHDLInterface(unit, rows);
		Map<String, Boolean> isArray = buildArrayMap(hdi);
		int regCount = rows.size();
		HDLVariableDeclaration C_SLV_WIDTH = new HDLVariableDeclaration().setDirection(HDLDirection.PARAMETER).setType(HDLQualifiedName.create("#uint"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.NATURAL))
				.addVariables(new HDLVariable().setName("C_SLV_DWIDTH").setDefaultValue(new HDLLiteral().setVal("32")));
		HDLVariableDeclaration C_NUM_REG = new HDLVariableDeclaration().setDirection(HDLDirection.CONSTANT).setType(HDLQualifiedName.create("#uint"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.NATURAL))
				.addVariables(new HDLVariable().setName("C_NUM_REG").setDefaultValue(HDLLiteral.get(regCount)));
		HDLVariableDeclaration Bus2IP_RdCE = new HDLVariableDeclaration()
				.setDirection(HDLDirection.IN)
				.setType(HDLQualifiedName.create("#bit<C_NUM_REG>"))
				.setPrimitive(
						new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_NUM_REG"))))
				.addVariables(new HDLVariable().setName("Bus2IP_RdCE"));
		HDLVariableDeclaration Bus2IP_WrCE = new HDLVariableDeclaration()
				.setDirection(HDLDirection.IN)
				.setType(HDLQualifiedName.create("#bit<C_NUM_REG>"))
				.setPrimitive(
						new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_NUM_REG"))))
				.addVariables(new HDLVariable().setName("Bus2IP_WrCE"));
		HDLVariableDeclaration Bus2IP_Clk = new HDLVariableDeclaration().setDirection(HDLDirection.IN).addAnnotations(new HDLAnnotation().setName("@clock"))
				.addAnnotations(new HDLAnnotation().setName("@VHDLAttribute").setValue("SIGIS=CLK")).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("Bus2IP_Clk"));
		HDLVariableDeclaration Bus2IP_Reset = new HDLVariableDeclaration().setDirection(HDLDirection.IN).addAnnotations(new HDLAnnotation().setName("@reset"))
				.addAnnotations(new HDLAnnotation().setName("@VHDLAttribute").setValue("SIGIS=RST")).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT)).addVariables(new HDLVariable().setName("Bus2IP_Reset"));
		HDLVariableDeclaration IP2Bus_RdAck = new HDLVariableDeclaration()
				.setDirection(HDLDirection.OUT)
				.setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(
						new HDLVariable().setName("IP2Bus_RdAck").setDefaultValue(
								new HDLManip()
										.setType(HDLManipType.CAST)
										.setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_RdCE")))
										.setCastTo(
												new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.UINT)
														.setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_NUM_REG"))))));
		HDLVariableDeclaration IP2Bus_WrAck = new HDLVariableDeclaration()
				.setDirection(HDLDirection.OUT)
				.setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(
						new HDLVariable().setName("IP2Bus_WrAck").setDefaultValue(
								new HDLManip()
										.setType(HDLManipType.CAST)
										.setTarget(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_WrCE")))
										.setCastTo(
												new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.UINT)
														.setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_NUM_REG"))))));
		HDLVariableDeclaration IP2Bus_Error = new HDLVariableDeclaration().setDirection(HDLDirection.OUT).setType(HDLQualifiedName.create("#bit"))
				.setPrimitive(new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BIT))
				.addVariables(new HDLVariable().setName("IP2Bus_Error").setDefaultValue(new HDLLiteral().setVal("0")));
		HDLVariableDeclaration IP2Bus_Data = new HDLVariableDeclaration()
				.setDirection(HDLDirection.OUT)
				.setType(HDLQualifiedName.create("#bit<C_SLV_DWIDTH>"))
				.setPrimitive(
						new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_SLV_DWIDTH"))))
				.addVariables(new HDLVariable().setName("IP2Bus_Data").setDefaultValue(new HDLLiteral().setVal("0")));
		HDLVariableDeclaration slv_reg_read_sel = new HDLVariableDeclaration().setDirection(HDLDirection.INTERNAL)
				.setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("slv_reg_read_sel").setDefaultValue(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_RdCE"))));
		HDLVariableDeclaration Bus2IP_Data = new HDLVariableDeclaration()
				.setDirection(HDLDirection.IN)
				.setType(HDLQualifiedName.create("#bit<C_SLV_DWIDTH>"))
				.setPrimitive(
						new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_SLV_DWIDTH"))))
				.addVariables(new HDLVariable().setName("Bus2IP_Data"));
		HDLVariableDeclaration slv_reg_write_sel = new HDLVariableDeclaration().setDirection(HDLDirection.INTERNAL)
				.setType(HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(regCount)))
				.addVariables(new HDLVariable().setName("slv_reg_write_sel").setDefaultValue(new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_WrCE"))));
		HDLVariableDeclaration Bus2IP_BE = new HDLVariableDeclaration()
				.setDirection(HDLDirection.IN)
				.setType(HDLQualifiedName.create("#bit<(C_SLV_DWIDTH/8)>"))
				.setPrimitive(
						new HDLPrimitive()
								.setName("#primitive")
								.setType(HDLPrimitiveType.BITVECTOR)
								.setWidth(
										new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("C_SLV_DWIDTH"))).setRight(new HDLLiteral().setVal("8"))
												.setType(HDLArithOpType.DIV))).addVariables(new HDLVariable().setName("Bus2IP_BE"));
		HDLSwitchStatement writeSwitch = createWriteSwitch(rows, isArray);
		HDLSwitchStatement readSwitch = createReadSwitch(rows, isArray);
		HDLUnit hUnit = new HDLUnit().setSimulation(false).setName(name).addStatements(C_SLV_WIDTH).addStatements(C_NUM_REG).addStatements(Bus2IP_RdCE).addStatements(Bus2IP_WrCE)
				.addStatements(Bus2IP_Clk).addStatements(Bus2IP_Reset).addStatements(IP2Bus_RdAck).addStatements(IP2Bus_WrAck).addStatements(IP2Bus_Error)
				.addStatements(IP2Bus_Data).addStatements(slv_reg_read_sel).addStatements(readSwitch).addStatements(Bus2IP_Data).addStatements(slv_reg_write_sel)
				.addStatements(Bus2IP_BE).addStatements(writeSwitch);
		for (HDLVariableDeclaration port : hdi.getPorts()) {
			hUnit = hUnit.addStatements(port.setDirection(HDLDirection.INTERNAL));
		}
		return hUnit;
	}

	private static HDLSwitchStatement createWriteSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement hsl = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_write_sel")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		for (Row row : rows) {
			hsl = hsl.addCases(createWriteCase(row, rows.size(), pos++, intPos, isArray));
		}
		hsl = hsl.addCases(new HDLSwitchCaseStatement().setLabel(null));
		return hsl;
	}

	private static HDLSwitchCaseStatement createWriteCase(Row row, int rowCount, int pos, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(BigInteger.ONE.shiftLeft((rowCount - 1) - pos)));
		HDLVariableRef busData = new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_Data"));
		int bitPos = 31;
		HDLVariableRef be = new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_BE"));
		HDLIfStatement ifStatement = null;
		int ifBitPos = 31;
		for (NamedElement ne : row.definitions) {
			Definition def = (Definition) ne;
			if ((def.rw == RWType.rw) || (def.rw == RWType.w)) {
				int size = MemoryModel.getSize(def);
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.name));
				target = createRef(intPos, isArray, def, target);
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
					int remainingBits = bitPos % 8;
					if (targetIdx > remainingBits) {
						HDLReference temp;
						if (def.width == -1)
							temp = target;
						else
							temp = target.addBits(getRange(targetIdx, remainingBits + 1));
						HDLRange busRange = getRange(bitPos, remainingBits + 1);
						if (ifStatement == null)
							ifStatement = new HDLIfStatement().setIfExp(be.addBits(getRange(bitPos / 8, 1)));
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
						if (def.width == -1)
							temp = target;
						else
							temp = target.addBits(getRange(targetIdx, targetIdx + 1));
						HDLRange busRange = getRange(bitPos, targetIdx + 1);
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
		if ((ifStatement != null) && !ifStatement.getThenDo().isEmpty())
			label = label.addDos(ifStatement);
		return label;
	}

	private static HDLSwitchStatement createReadSwitch(List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement defaultCase = new HDLSwitchCaseStatement().addDos(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("IP2Bus_Data")))
				.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0")));
		HDLSwitchStatement hdlSwitchStatement = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_read_sel")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		int regCount = rows.size();
		for (Row row : rows) {
			int reg = pos++;
			hdlSwitchStatement = hdlSwitchStatement
					.addCases(createReadCase(row, reg, intPos, isArray, "IP2Bus_Data", HDLLiteral.get(BigInteger.ONE.shiftLeft(regCount - reg - 1))));
		}
		hdlSwitchStatement = hdlSwitchStatement.addCases(defaultCase);
		return hdlSwitchStatement;
	}

	public static Unit createAdderBus(int regCount) {
		Definition a = new Definition("a", true, RWType.rw, Type.UINT, 16);
		Definition b = new Definition("b", true, RWType.rw, Type.UINT, 16);
		Definition result = new Definition("result", true, RWType.rw, Type.UINT, 16);
		Row rowIn = new Row("input", null, a, b);
		Row rowOut = new Row("output", null, new Reference("fill"), result);
		Column col = new Column("adder");
		col.rows.add(rowIn);
		col.rows.add(rowOut);
		Memory mem = new Memory(new Reference("adder", regCount));
		Unit unit = new Unit();
		unit.declarations.put("input", rowIn);
		unit.declarations.put("output", rowOut);
		unit.declarations.put("adder", col);
		unit.memory = mem;
		return unit;
	}

	public static Unit create(int regCount) {
		Definition a = new Definition("a", true, RWType.rw, Type.BIT, 17);
		Definition b = new Definition("b", true, RWType.rw, Type.BIT, 5);
		Definition c = new Definition("c", true, RWType.rw, Type.BIT, 8);
		Definition d = new Definition("d", true, RWType.rw, Type.BIT, 2);
		Row row = new Row("reg", null, a, b, c, d);
		Memory mem = new Memory(new Reference("reg", regCount));
		Unit unit = new Unit();
		unit.declarations.put("reg", row);
		unit.memory = mem;
		return unit;
	}

	public static void main(String[] args) {
		// Unit defaultUnit = BusGenerator.createDefaultUnit(5);
		Unit defaultUnit = createAdderBus(3);
		HDLUnit unit = get("Bla", defaultUnit, MemoryModel.buildRows(defaultUnit));
		System.out.println(unit);
	}
}
