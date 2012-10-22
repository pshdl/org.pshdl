package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.math.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.*;

public class UserLogicCodeGen {
	public static HDLUnit get(int regCount) {
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
		HDLVariableDeclaration regs = new HDLVariableDeclaration()
				.setRegister(
						new HDLRegisterConfig().setClk(HDLQualifiedName.create("$clk")).setRst(HDLQualifiedName.create("$rst")).setClockType(HDLRegClockType.RISING)
								.setResetType(HDLRegResetActiveType.HIGH).setSyncType(HDLRegSyncType.SYNC).setResetValue(new HDLLiteral().setVal("0")))
				.setDirection(HDLDirection.INTERNAL)
				.setType(HDLQualifiedName.create("#bit<C_SLV_DWIDTH>"))
				.setPrimitive(
						new HDLPrimitive().setName("#primitive").setType(HDLPrimitiveType.BITVECTOR).setWidth(new HDLVariableRef().setVar(HDLQualifiedName.create("C_SLV_DWIDTH"))))
				.addVariables(new HDLVariable().setName("regs").addDimensions(new HDLVariableRef().setVar(HDLQualifiedName.create("C_NUM_REG"))));
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
		HDLSwitchStatement writeSwitch = createWriteSwitch(regCount);
		HDLSwitchStatement readSwitch = createReadSwitch(regCount);
		return new HDLUnit().setLibURI("Test1113812579:1761192476").setSimulation(false).setName("net.kbsvn.plbgen").addStatements(C_SLV_WIDTH).addStatements(C_NUM_REG)
				.addStatements(Bus2IP_RdCE).addStatements(Bus2IP_WrCE).addStatements(Bus2IP_Clk).addStatements(Bus2IP_Reset).addStatements(IP2Bus_RdAck)
				.addStatements(IP2Bus_WrAck).addStatements(IP2Bus_Error).addStatements(regs).addStatements(IP2Bus_Data).addStatements(slv_reg_read_sel).addStatements(readSwitch)
				.addStatements(Bus2IP_Data).addStatements(slv_reg_write_sel).addStatements(Bus2IP_BE).addStatements(writeSwitch);
	}

	private static HDLSwitchStatement createWriteSwitch(int regCount) {
		HDLSwitchStatement writeSwitch = new HDLSwitchStatement();
		for (int i = 0; i < regCount; i++) {
			writeSwitch = writeSwitch.addCases(switchCaseCaseWrite(BigInteger.ONE.shiftLeft(regCount - i - 1), i));
		}
		writeSwitch = writeSwitch.setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_write_sel"))).addCases(new HDLSwitchCaseStatement());
		return writeSwitch;
	}

	private static HDLSwitchCaseStatement switchCaseCaseWrite(BigInteger caseLabel, int reg) {
		HDLVariableRef regRef = new HDLVariableRef()
				.setVar(HDLQualifiedName.create("regs"))
				.addArray(new HDLLiteral().setVal(Integer.toString(reg)))
				.addBits(
						new HDLRange().setFrom(
								new HDLArithOp()
										.setLeft(
												new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("I"))).setRight(new HDLLiteral().setVal("8"))
														.setType(HDLArithOpType.MUL)).setRight(new HDLLiteral().setVal("7")).setType(HDLArithOpType.PLUS)).setTo(
								new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("I"))).setRight(new HDLLiteral().setVal("8"))
										.setType(HDLArithOpType.MUL)));
		HDLVariableRef data = new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_Data")).addBits(
				new HDLRange().setFrom(
						new HDLArithOp()
								.setLeft(
										new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("I"))).setRight(new HDLLiteral().setVal("8"))
												.setType(HDLArithOpType.MUL)).setRight(new HDLLiteral().setVal("7")).setType(HDLArithOpType.PLUS)).setTo(
						new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("I"))).setRight(new HDLLiteral().setVal("8")).setType(HDLArithOpType.MUL)));
		HDLRange range = new HDLRange().setFrom(new HDLLiteral().setVal("0")).setTo(
				new HDLArithOp()
						.setLeft(
								new HDLArithOp().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("C_SLV_DWIDTH"))).setRight(new HDLLiteral().setVal("8"))
										.setType(HDLArithOpType.DIV)).setRight(new HDLLiteral().setVal("1")).setType(HDLArithOpType.MINUS));
		return new HDLSwitchCaseStatement().setLabel(new HDLLiteral().setVal("0b" + caseLabel.toString(2))).addDos(
				new HDLForLoop()
						.addRange(range)
						.setParam(new HDLVariable().setName("I"))
						.addDos(new HDLIfStatement().setIfExp(
								new HDLVariableRef().setVar(HDLQualifiedName.create("Bus2IP_BE")).addBits(
										new HDLRange().setTo(new HDLVariableRef().setVar(HDLQualifiedName.create("I"))))).addThenDo(
								new HDLAssignment().setLeft(regRef).setType(HDLAssignmentType.ASSGN).setRight(data))));
	}

	private static HDLSwitchStatement createReadSwitch(int regCount) {
		HDLSwitchCaseStatement defaultCase = new HDLSwitchCaseStatement().addDos(new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("IP2Bus_Data")))
				.setType(HDLAssignmentType.ASSGN).setRight(new HDLLiteral().setVal("0")));
		HDLSwitchStatement hdlSwitchStatement = new HDLSwitchStatement().setCaseExp(new HDLVariableRef().setVar(HDLQualifiedName.create("slv_reg_read_sel")));
		for (int i = 0; i < regCount; i++) {
			hdlSwitchStatement = hdlSwitchStatement.addCases(createReadCase(BigInteger.ONE.shiftLeft(regCount - i - 1), i));
		}
		hdlSwitchStatement = hdlSwitchStatement.addCases(defaultCase);
		return hdlSwitchStatement;
	}

	private static HDLSwitchCaseStatement createReadCase(BigInteger caseLabel, int reg) {
		return new HDLSwitchCaseStatement().setLabel(new HDLLiteral().setVal("0b" + caseLabel.toString(2))).addDos(
				new HDLAssignment().setLeft(new HDLVariableRef().setVar(HDLQualifiedName.create("IP2Bus_Data"))).setType(HDLAssignmentType.ASSGN)
						.setRight(new HDLVariableRef().setVar(HDLQualifiedName.create("regs")).addArray(new HDLLiteral().setVal(Integer.toString(reg)))));
	}

	public static void main(String[] args) {
		HDLUnit hdlPackage = get(5);
		System.out.println(hdlPackage);
		hdlPackage.validateAllFields(null, true);
	}
}
