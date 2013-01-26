package de.tuhh.ict.pshdl.model.simulation;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.simulation.FluidFrame.ArgumentedInstruction;
import de.tuhh.ict.pshdl.model.simulation.FluidFrame.Instruction;
import de.tuhh.ict.pshdl.model.types.builtIn.*;

public aspect SimulationTransformation {
	public FluidFrame HDLExpression.toSimulationModel(HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented!");
	}

	public FluidFrame HDLStatement.toSimulationModel(HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented!");
	}

	public FluidFrame HDLAssignment.toSimulationModel(HDLEvaluationContext context) {
		HDLReference left = getLeft();
		HDLVariable var = left.resolveVar();
		HDLRegisterConfig config = var.getRegisterConfig();
		FluidFrame res;
		if (config != null)
			res = new FluidFrame(getVarName(getLeft(), true) + "$reg");
		else
			res = new FluidFrame(getVarName(getLeft(), true));
		if (config != null) {
			config = config.normalize();
			HDLVariable clk = config.resolveClk();
			String name = clk.getFullName().toString();
			if (clk.getDirection() == HDLDirection.IN) {
				res.addInput(name);
				if (config.getClockType() == HDLRegClockType.RISING)
					res.add(new ArgumentedInstruction(Instruction.isRisingEdgeInput, name));
				else
					res.add(new ArgumentedInstruction(Instruction.isFallingEdgeInput, name));
			} else {
				if (config.getClockType() == HDLRegClockType.RISING)
					res.add(new ArgumentedInstruction(Instruction.isRisingEdgeInternal, name));
				else
					res.add(new ArgumentedInstruction(Instruction.isFallingEdgeInternal, name));
			}
		}
		res.append(getRight().toSimulationModel(context));
		HDLDirection dir = var.getDirection();
		boolean hasBits = false;
		if (left instanceof HDLVariableRef) {
			HDLVariableRef variableRef = (HDLVariableRef) left;
			if (!variableRef.getBits().isEmpty())
				hasBits = true;
		}
		res.setInternal(dir == HDLDirection.INTERNAL || hasBits);
		return res;
	}

	public static String getVarName(HDLReference var, boolean withBits) {
		HDLVariableRef varRef = (HDLVariableRef) var;
		StringBuilder sb = new StringBuilder();
		sb.append(var.resolveVar().getFullName());
		for (HDLExpression exp : varRef.getArray()) {
			sb.append('[').append(exp).append(']');
		}
		if (withBits) {
			for (HDLRange exp : varRef.getBits()) {
				sb.append('{').append(exp).append('}');
			}
		}
		return sb.toString();
	}

	public FluidFrame HDLConcat.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		Iterator<HDLExpression> iter = getCats().iterator();
		res.append(iter.next().toSimulationModel(context));
		while (iter.hasNext()) {
			HDLExpression exp = iter.next();
			res.append(exp.toSimulationModel(context));
			Integer width=HDLPrimitives.getWidth(exp.determineType(), context);
			res.add(new ArgumentedInstruction(Instruction.concat, width.toString()));
		}
		return res;
	}

	public FluidFrame HDLUnit.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		for (HDLStatement stmnt : getInits()) {
			switch (stmnt.getClassType()) {
			case HDLAssignment:
				FluidFrame sFrame = stmnt.toSimulationModel(context);
				res.addReferencedFrame(sFrame);
				res.instructions.add(new ArgumentedInstruction(Instruction.callFrame, Integer.toString(sFrame.id)));
				break;
			case HDLVariableDeclaration:
				HDLVariableDeclaration hvd=(HDLVariableDeclaration)stmnt;
				for(HDLVariable var: hvd.getVariables()){
					res.addWith(var.getFullName().toString(),HDLPrimitives.getWidth(var.determineType(), context));
				}
				break;
			default:
				break;
			}
		}

		for (HDLStatement stmnt : getStatements()) {
			switch (stmnt.getClassType()) {
			case HDLAssignment:
				FluidFrame sFrame = stmnt.toSimulationModel(context);
				res.addReferencedFrame(sFrame);
				res.instructions.add(new ArgumentedInstruction(Instruction.callFrame, Integer.toString(sFrame.id)));
				break;
			case HDLVariableDeclaration:
				HDLVariableDeclaration hvd=(HDLVariableDeclaration)stmnt;
				for(HDLVariable var: hvd.getVariables()){
					res.addWith(var.getFullName().toString(),HDLPrimitives.getWidth(var.determineType(), context));
				}
				break;
			default:
				break;
			}
		}
		return res;
	}

	public FluidFrame HDLManip.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = getTarget().toSimulationModel(context);
		switch (getType()) {
		case ARITH_NEG:
			res.add(Instruction.arith_neg);
			break;
		case BIT_NEG:
			res.add(Instruction.bit_neg);
			break;
		case LOGIC_NEG:
			res.add(Instruction.logic_neg);
			break;
		case CAST:
			HDLPrimitive prim = (HDLPrimitive) getCastTo();
			switch (prim.getType()) {
			case INT:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_int, prim.getWidth().constantEvaluate(context).toString()));
				break;
			case INTEGER:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_int, "32"));
				break;
			case UINT:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_uint, prim.getWidth().constantEvaluate(context).toString()));
				break;
			case NATURAL:
				res.instructions.add(new ArgumentedInstruction(Instruction.cast_uint, "32"));
				break;
			case BIT:
			case BITVECTOR:
				break;
			default:
				throw new IllegalArgumentException("Cast to type:" + prim.getType() + " not supported");
			}
			break;
		}
		return res;
	}

	public FluidFrame HDLVariableRef.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		String refName = getVarRefName().toString();
		String[] bits = new String[getBits().size() + 1];
		bits[0] = refName;
		if (!getBits().isEmpty()) {
			for (int i = 0; i < bits.length - 1; i++) {
				bits[i + 1] = getBits().get(i).toString();
			}
		}
		HDLVariable var = resolveVar();
		HDLDirection dir = var.getDirection();
		switch (dir) {
		case INTERNAL:
			res.instructions.add(new ArgumentedInstruction(Instruction.loadInternal, bits));
			break;
		case PARAMETER:
		case CONSTANT:
		case IN:
			res.addInput(refName);
			res.instructions.add(new ArgumentedInstruction(Instruction.loadInput, bits));
			break;
		case OUT:
		case INOUT:
			if (bits.length > 1) {
				res.instructions.add(new ArgumentedInstruction(Instruction.loadInternal, bits));
			} else {
				res.addInput(refName);
				res.instructions.add(new ArgumentedInstruction(Instruction.loadInput, bits));
			}
			break;
		default:
			throw new IllegalArgumentException("Did not expect this here" + dir);
		}
		return res;
	}

	public FluidFrame HDLTernary.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		res.setPredicate(true);
		res.append(getIfExpr().toSimulationModel(context));
		FluidFrame thenFrame = getThenExpr().toSimulationModel(context);
		thenFrame.addPredicate(res.id, true);
		res.addReferencedFrame(thenFrame);
		FluidFrame elseFrame = getThenExpr().toSimulationModel(context);
		elseFrame.addPredicate(res.id, false);
		res.addReferencedFrame(elseFrame);
		return res;
	}

	public FluidFrame HDLLiteral.toSimulationModel(HDLEvaluationContext context) {
		BigInteger value = getValueAsBigInt();

		FluidFrame res = new FluidFrame();
		if (BigInteger.ZERO.equals(value)) {
			res.add(Instruction.const0);
			return res;
		}
		String key = value.toString();
		res.constants.put(key, value);
		res.instructions.add(new ArgumentedInstruction(Instruction.loadConstant, key));
		return res;
	}

	public FluidFrame HDLBitOp.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel(context));
		res.append(getRight().toSimulationModel(context));
		switch (getType()) {
		case AND:
			res.add(Instruction.and);
			break;
		case LOGI_AND:
			res.add(Instruction.logiAnd);
			break;
		case OR:
			res.add(Instruction.or);
			break;
		case LOGI_OR:
			res.add(Instruction.logiOr);
			break;
		case XOR:
			res.add(Instruction.xor);
			break;
		}
		return res;
	}

	public FluidFrame HDLArithOp.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel(context));
		res.append(getRight().toSimulationModel(context));
		switch (getType()) {
		case DIV:
			res.add(Instruction.div);
			break;
		case MINUS:
			res.add(Instruction.minus);
			break;
		case MOD:
			throw new IllegalArgumentException("Mod is not supported as Instruction");
		case MUL:
			res.add(Instruction.mul);
			break;
		case PLUS:
			res.add(Instruction.plus);
			break;
		case POW:
			throw new IllegalArgumentException("Pow is not supported as Instruction");
		}
		return res;
	}

	public FluidFrame HDLShiftOp.toSimulationModel(HDLEvaluationContext context) {
		FluidFrame res = new FluidFrame();
		res.append(getLeft().toSimulationModel(context));
		res.append(getRight().toSimulationModel(context));
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
