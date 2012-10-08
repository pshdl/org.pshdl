package de.tuhh.ict.pshdl.model.simulation;
import java.math.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;

public aspect SimulationTransformation {
	public String HDLExpression.toSimulationModel(){
		throw new RuntimeException("Not implemented!");
	}
	
	public String HDLBitOp.toSimulationModel() {
		String leftVal="("+getLeft().toSimulationModel()+")";
		String rightVal="("+getRight().toSimulationModel()+")";
		switch (getType()) {
		case AND:
			return leftVal+".and"+rightVal;
		case OR:
			return leftVal+".or"+rightVal;
		case XOR:
			return leftVal+".xor"+rightVal;
		case LOGI_AND: {
			return "!BigInteger.ZERO.equals"+leftVal+" && !BigInteger.ZERO.equals"+rightVal;
		}
		case LOGI_OR: {
			return "!BigInteger.ZERO.equals"+leftVal+" || !BigInteger.ZERO.equals"+rightVal;
		}
		}
		throw new RuntimeException("Not implemented!");
	}
	public String HDLArithOp.toSimulationModel() {
		String leftVal="("+getLeft().toSimulationModel()+")";
		String rightVal="("+getRight().toSimulationModel()+")";
		switch (getType()) {
		case DIV:
			return leftVal+".divide"+rightVal;
		case MUL:
			return leftVal+".multiply"+rightVal;
		case MINUS:
			return leftVal+".subtract"+rightVal;
		case PLUS:
			return leftVal+".add"+rightVal;
		case MOD:
			return leftVal+".remainder"+rightVal;
		case POW:
			return leftVal+".pow("+rightVal+".intValue())";
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}
	
	public String HDLEqualityOp.toSimulationModel() {
		String leftVal="("+getLeft().toSimulationModel()+")";
		String rightVal="("+getRight().toSimulationModel()+")";
		switch (getType()) {
		case EQ:
			return leftVal+".equals"+rightVal;
		case NOT_EQ:
			return "!"+leftVal+".equals"+rightVal;
		case GREATER:
			return leftVal+".compareTo"+rightVal+" > 0";
		case GREATER_EQ:
			return leftVal+".compareTo"+rightVal+" >= 0";
		case LESS:
			return leftVal+".compareTo"+rightVal+" < 0";
		case LESS_EQ:
			return leftVal+".compareTo"+rightVal+" <= 0";
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}
}
