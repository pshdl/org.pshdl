package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.io.*;
import java.util.*;

import org.antlr.runtime.*;

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
import de.tuhh.ict.pshdl.model.utils.*;

public class ABP3BusCodeGen {

	public static HDLUnit get(String name, Unit unit, List<Row> rows) {
		HDLInterface hdi = MemoryModel.buildHDLInterface(unit, rows);
		String UNIT_NAME = "";
		int addrWidth = ((int) Math.ceil(Math.log10(rows.size() * 4) / Math.log10(2)));
		Map<String, Boolean> isArray = new HashMap<String, Boolean>();
		for (HDLVariable var : hdi.getAllObjectsOf(HDLVariable.class, true)) {
			if (var.getDimensions().size() != 0)
				isArray.put(var.getName(), true);
			else
				isArray.put(var.getName(), false);
		}
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
										new HDLVariable().setName("rst")
												.setDefaultValue(
														new HDLManip().setType(HDLManipType.BIT_NEG).setTarget(
																new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PRESETn"))))))
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
				// .addStatements(
				// new
				// HDLVariableDeclaration().setRegister(HDLRegisterConfig.defaultConfig()).setDirection(HDLDirection.INTERNAL)
				// .setType(HDLQualifiedName.create("#bit<32>"))
				// .setPrimitive(new
				// HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new
				// HDLLiteral().setVal("32")))
				// .addVariables(new
				// HDLVariable().setName("regs").addDimensions(HDLLiteral.get(regCount))))
				.addStatements(
						new HDLIfStatement().setIfExp(
								new HDLBitOp()
										.setLeft(
												new HDLBitOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PWRITE")))
														.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PSEL"))).setType(HDLBitOpType.LOGI_AND))
										.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PENABLE"))).setType(HDLBitOpType.LOGI_AND)).addThenDo(
								createWriteSwitch(UNIT_NAME, rows, isArray)))
				.addStatements(
						new HDLIfStatement()
								.setIfExp(
										new HDLBitOp()
												.setLeft(
														new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PWRITE")))
																.setRight(new HDLLiteral().setVal("0")).setType(HDLEqualityOpType.EQ))
												.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PSEL"))).setType(HDLBitOpType.LOGI_AND))
								.addThenDo(createReadSwitch(UNIT_NAME, rows, isArray))
								.addElseDo(
										new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PRDATA"))).setType(HDLAssignmentType.ASSGN)
												.setRight(new HDLLiteral().setVal("0")))).setSimulation(false);
		for (HDLVariableDeclaration port : hdi.getPorts()) {
			res = res.addStatements(port.copy().setDirection(HDLDirection.INTERNAL));
		}
		return res;
	}

	private static HDLSwitchStatement createWriteSwitch(String UNIT_NAME, List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement hsl = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PADDR")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		for (Row row : rows) {
			hsl = hsl.addCases(createWriteCase(UNIT_NAME, row, pos++, intPos, isArray));
		}
		hsl = hsl.addCases(new HDLSwitchCaseStatement().setLabel(null));
		return hsl;
	}

	private static HDLSwitchCaseStatement createWriteCase(String UNIT_NAME, Row row, int pos, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(pos * 4));
		HDLVariableRef busData = new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PWDATA"));
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

	private static HDLRange getRange(int bitPos, int size) {
		HDLRange range = new HDLRange().setTo(HDLLiteral.get(bitPos - (size - 1)));
		if (size != 1)
			range = range.setFrom(HDLLiteral.get(bitPos));
		return range;
	}

	private static HDLSwitchStatement createReadSwitch(String UNIT_NAME, List<Row> rows, Map<String, Boolean> isArray) {
		HDLSwitchStatement res = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PADDR")));
		int pos = 0;
		Map<String, Integer> intPos = new HashMap<String, Integer>();
		for (Row row : rows) {
			res = res.addCases(createReadCase(UNIT_NAME, row, pos++, intPos, isArray));
		}
		res = res.addCases(new HDLSwitchCaseStatement().addDos(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PRDATA")))
				.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0"))));
		return res;
	}

	private static HDLSwitchCaseStatement createReadCase(String UNIT_NAME, Row row, int reg, Map<String, Integer> intPos, Map<String, Boolean> isArray) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(HDLLiteral.get(reg * 4));
		HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(UNIT_NAME + "PRDATA"));
		int bitPos = 31;
		for (NamedElement ne : row.definitions) {
			Definition def = (Definition) ne;
			int size = MemoryModel.getSize(def);
			if ((def.rw == RWType.rw) || (def.rw == RWType.r)) {
				HDLVariableRef source = new HDLVariableRef().setVar(HDLQualifiedName.create(def.name));
				source = createRef(intPos, isArray, def, source);
				HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target.addBits(range)).setType(HDLAssignmentType.ASSGN).setRight(source));
			}
			bitPos -= size;
		}
		return label;
	}

	private static HDLVariableRef createRef(Map<String, Integer> intPos, Map<String, Boolean> isArray, Definition def, HDLVariableRef source) {
		if (isArray.get(def.name)) {
			Integer integer = intPos.get(def.name);
			if (integer == null) {
				integer = 0;
			}
			source = source.addArray(HDLLiteral.get(integer));
			intPos.put(def.name, ++integer);
		}
		return source;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, RecognitionException {
		Unit unit = MemoryModel.parseUnit(new FileInputStream(args[0]));
		System.out.println(unit);
		System.out.println(get("Bla", unit, MemoryModel.buildRows(unit)));
	}
}
