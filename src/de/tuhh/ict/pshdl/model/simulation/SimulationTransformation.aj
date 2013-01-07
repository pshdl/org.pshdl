package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.simulation.FluidFrame.ArgumentedInstruction;
import de.tuhh.ict.pshdl.model.simulation.FluidFrame.Instruction;

public aspect SimulationTransformation {
	public FluidFrame HDLExpression.toSimulationModel() {
		throw new RuntimeException("Not implemented!");
	}

	public FluidFrame HDLStatement.toSimulationModel() {
		throw new RuntimeException("Not implemented!");
	}

	public FluidFrame HDLAssignment.toSimulationModel() {
		FluidFrame res = new FluidFrame(getLeft().toString());
		res.append(getRight().toSimulationModel());
		return res;
	}

	public FluidFrame HDLUnit.toSimulationModel() {
		FluidFrame res = new FluidFrame();
		for (HDLStatement stmnt : getStatements()) {
			switch (stmnt.getClassType()) {
			case HDLAssignment:
				FluidFrame sFrame = stmnt.toSimulationModel();
				res.addReferencedFrame(sFrame);
				res.instructions.add(new ArgumentedInstruction(Instruction.callFrame, Integer.toString(sFrame.id)));
				break;
			case HDLVariableDeclaration:
				break;
			}
		}
		return res;
	}
	
	
	public FluidFrame HDLManip.toSimulationModel(){
		FluidFrame res=getTarget().toSimulationModel();
		switch (getType()){
		case ARITH_NEG: res.add(Instruction.arith_neg);break; 
		case BIT_NEG: res.add(Instruction.bit_neg);break; 
		case LOGIC_NEG: res.add(Instruction.logic_neg);break; 
		case CAST:
			HDLPrimitive prim=(HDLPrimitive)getCastTo();
			switch (prim.getType()){
			case INT:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_int, prim.getWidth().toString()));break;
			case INTEGER:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_int, "32"));break;
			case UINT:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_uint, prim.getWidth().toString()));break;
			case NATURAL:	
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_uint, "32"));break;
			default:
				throw new IllegalArgumentException("Cast to type:"+prim.getType()+" not supported");
			}
			break;
		}
		return res;
	}

	public FluidFrame HDLVariableRef.toSimulationModel(){
		FluidFrame res=new FluidFrame();
		String refName=getVarRefName().toString();
		res.addInput(refName);
		String[] bits=new String[getBits().size()+1];
		bits[0]=refName;
		if (getBits().size()!=0){
			for (int i = 0; i < bits.length-1; i++) {
				bits[i+1]=getBits().get(i).toString();
			}
//			res.instructions.add(new ArgumentedInstruction(Instruction.bitAccess, bits));
		}
		res.instructions.add(new ArgumentedInstruction(Instruction.loadInput, bits));
		return res;
	}
	
	public FluidFrame HDLTernary.toSimulationModel() {
		FluidFrame res = new FluidFrame();
		res.append(getIfExpr().toSimulationModel());
		FluidFrame thenFrame = getThenExpr().toSimulationModel();
		res.addReferencedFrame(thenFrame);
		FluidFrame elseFrame = getThenExpr().toSimulationModel();
		res.addReferencedFrame(elseFrame);
		res.instructions.add(new ArgumentedInstruction(Instruction.ifCall, Integer.toString(thenFrame.id), Integer.toString(elseFrame.id)));
		return res;
	}

	public FluidFrame HDLLiteral.toSimulationModel() {
		BigInteger value = getValueAsBigInt();
		FluidFrame res = new FluidFrame();
		String key = value.toString();
		res.store.put(key, value);
		res.instructions.add(new ArgumentedInstruction(Instruction.loadStore, key));
		return res;
	}

	public FluidFrame HDLBitOp.toSimulationModel() {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel());
		res.append(getRight().toSimulationModel());
		switch (getType()) {
		case AND:
			res.add(Instruction.and);
			break;
		case LOGI_AND:
			res.add(Instruction.and);
			break;
		case OR:
			res.add(Instruction.and);
			break;
		case LOGI_OR:
			res.add(Instruction.and);
			break;
		case XOR:
			res.add(Instruction.and);
			break;
		}
		return res;
	}

	public FluidFrame HDLArithOp.toSimulationModel() {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel());
		res.append(getRight().toSimulationModel());
		switch (getType()) {
		case DIV:
			res.add(Instruction.div);
			break;
		case MINUS:
			res.add(Instruction.minus);
			break;
		case MOD:
			res.add(Instruction.mod);
			break;
		case MUL:
			res.add(Instruction.mul);
			break;
		case PLUS:
			res.add(Instruction.plus);
			break;
		case POW:
			res.add(Instruction.pow);
			break;
		}
		return res;
	}

	public FluidFrame HDLShiftOp.toSimulationModel() {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel());
		res.append(getRight().toSimulationModel());
		switch (getType()) {
		case SLL:
			res.add(Instruction.sll);
			break;
		case SRA:
			res.add(Instruction.sra);
			break;
		case SRL:
			res.add(Instruction.srl);
			break;
		}
		return res;
	}
}
