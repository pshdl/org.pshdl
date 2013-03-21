package de.tuhh.ict.pshdl.model.simulation

import de.tuhh.ict.pshdl.interpreter.utils.FluidFrame
import de.tuhh.ict.pshdl.interpreter.utils.FluidFrame$ArgumentedInstruction
import de.tuhh.ict.pshdl.model.HDLArithOp
import de.tuhh.ict.pshdl.model.HDLAssignment
import de.tuhh.ict.pshdl.model.HDLBitOp
import de.tuhh.ict.pshdl.model.HDLConcat
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLManip
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLRange
import de.tuhh.ict.pshdl.model.HDLReference
import de.tuhh.ict.pshdl.model.HDLRegisterConfig
import de.tuhh.ict.pshdl.model.HDLShiftOp
import de.tuhh.ict.pshdl.model.HDLStatement
import de.tuhh.ict.pshdl.model.HDLTernary
import de.tuhh.ict.pshdl.model.HDLUnit
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext
import de.tuhh.ict.pshdl.model.evaluation.ConstantEvaluate
import de.tuhh.ict.pshdl.model.extensions.FullNameExtension
import de.tuhh.ict.pshdl.model.extensions.TypeExtension
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives
import java.math.BigInteger
import java.util.ArrayList
import java.util.Iterator
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment
import de.tuhh.ict.pshdl.model.HDLResolvedRef

import static de.tuhh.ict.pshdl.interpreter.utils.FluidFrame$Instruction.*
import static de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType.*
import static de.tuhh.ict.pshdl.model.HDLBitOp$HDLBitOpType.*
import static de.tuhh.ict.pshdl.model.HDLClass.*
import static de.tuhh.ict.pshdl.model.HDLManip$HDLManipType.*
import static de.tuhh.ict.pshdl.model.HDLPrimitive$HDLPrimitiveType.*
import static de.tuhh.ict.pshdl.model.HDLRegisterConfig$HDLRegClockType.*
import static de.tuhh.ict.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection.*
import static de.tuhh.ict.pshdl.model.simulation.SimulationTransformationExtension.*
import com.google.common.base.Optional

class SimulationTransformationExtension {
	public static SimulationTransformationExtension INST = new SimulationTransformationExtension

	def static FluidFrame simulationModelOf(IHDLObject obj, HDLEvaluationContext context) {
		return INST.toSimulationModel(obj, context)
	}

	def dispatch FluidFrame toSimulationModel(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented!")
	}

	def dispatch FluidFrame toSimulationModel(HDLStatement obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented!")
	}

	def dispatch FluidFrame toSimulationModel(HDLAssignment obj, HDLEvaluationContext context) {
		val HDLReference left = obj.left
		val HDLVariable hVar = left.resolveVar
		var HDLRegisterConfig config = hVar.registerConfig
		var FluidFrame res
		if (config !== null)
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true) + "$reg")
		else
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true))
		if (config !== null) {
			config = config.normalize
			val HDLVariable clk = config.resolveClk.get
			val String name = FullNameExtension::fullNameOf(clk).toString
			if (config.clockType == RISING)
				res.add(new ArgumentedInstruction(isRisingEdgeInternal2, name))
			else
				res.add(new ArgumentedInstruction(isFallingEdgeInternal2, name))
		}
		res.append(obj.right.toSimulationModel(context))
		var boolean hasBits = false
		if (left instanceof HDLVariableRef) {
			val HDLVariableRef variableRef = left as HDLVariableRef
			if (!variableRef.bits.isEmpty)
				hasBits = true
		}
		return res
	}

	def HDLVariable resolveVar(HDLReference reference) {
		if (reference instanceof HDLUnresolvedFragment)
			throw new RuntimeException("Can not use unresolved fragments")
		return (reference as HDLResolvedRef).resolveVar.get
	}

	def static String getVarName(HDLVariableRef hVar, boolean withBits) {
		val StringBuilder sb = new StringBuilder
		sb.append(FullNameExtension::fullNameOf(hVar.resolveVar.get))
		for (HDLExpression exp : hVar.array) {
			sb.append('[').append(exp).append(']')
		}
		if (withBits) {
			for (HDLRange exp : hVar.bits) {
				sb.append('{').append(exp).append('}')
			}
		}
		return sb.toString
	}

	def dispatch FluidFrame toSimulationModel(HDLConcat obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		val Iterator<HDLExpression> iter = obj.cats.iterator
		res.append(iter.next.toSimulationModel(context))
		while (iter.hasNext) {
			val HDLExpression exp = iter.next
			res.append(exp.toSimulationModel(context))
			val int width = HDLPrimitives::getWidth(TypeExtension::typeOf(exp).get, context)
			res.add(new ArgumentedInstruction(concat, width.toString))
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLUnit obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		for (HDLStatement stmnt : obj.inits) {
			handleStatement(context, res, stmnt)
		}

		for (HDLStatement stmnt : obj.statements) {
			handleStatement(context, res, stmnt)
		}
		return res
	}

	def private handleStatement(HDLEvaluationContext context, FluidFrame res, HDLStatement stmnt) {
		switch (stmnt.classType) {
			case HDLAssignment: {
				val FluidFrame sFrame = stmnt.toSimulationModel(context)
				res.addReferencedFrame(sFrame)
				res.instructions.add(new ArgumentedInstruction(callFrame, sFrame.id.toString))
			}
			case HDLVariableDeclaration: {
				val HDLVariableDeclaration hvd = stmnt as HDLVariableDeclaration
				for (HDLVariable hVar : hvd.variables) {
					res.addWith(FullNameExtension::fullNameOf(hVar).toString,
						HDLPrimitives::getWidth(TypeExtension::typeOf(hVar).get, context))
				}
			}
		}
	}

	def dispatch FluidFrame toSimulationModel(HDLManip obj, HDLEvaluationContext context) {
		val FluidFrame res = obj.target.toSimulationModel(context)
		switch (obj.type) {
			case ARITH_NEG:
				res.add(arith_neg)
			case BIT_NEG:
				res.add(bit_neg)
			case LOGIC_NEG:
				res.add(logic_neg)
			case CAST: {
				val HDLPrimitive prim = obj.castTo as HDLPrimitive
				val HDLPrimitive current = TypeExtension::typeOf(obj.target).get as HDLPrimitive
				val String currentWidth = getWidth(current, context)
				val String primWidth = getWidth(prim, context)
				switch (prim.type) {
					case prim.type == INTEGER || prim.type == INT:
						res.instructions.add(new ArgumentedInstruction(cast_int, primWidth, currentWidth))
					case prim.type == UINT || prim.type == NATURAL:
						res.instructions.add(new ArgumentedInstruction(cast_uint, primWidth, currentWidth))
					case prim.type == BIT || prim.type == BITVECTOR: {
					}
					default:
						throw new IllegalArgumentException("Cast to type:" + prim.type + " not supported")
				}
			}
		}
		return res
	}

	def private String getWidth(HDLPrimitive current, HDLEvaluationContext context) {
		switch (current.type) {
			case BIT:
				return "1"
			case current.type == INTEGER || current.type == NATURAL:
				return "32"
			case current.type == INT || current.type == UINT || current.type == BITVECTOR: {
				val res = ConstantEvaluate::valueOf(current.width, context)
				if (res.present)
					return res.get.toString
			}
		}
		throw new IllegalArgumentException(current + " is not a valid type");
	}

	def dispatch FluidFrame toSimulationModel(HDLVariableRef obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		var hVar = obj.resolveVar
		val String refName = hVar.get.asRef.toString
		val bits = new ArrayList<String>(obj.bits.size + 1)
		bits.add(refName)
		if (!obj.bits.isEmpty) {
			for (HDLRange  r : obj.bits) {
				bits.add(r.toString)
			}
		}
		val String[] arrBits = bits
		val HDLDirection dir = hVar.get.direction
		switch (dir) {
			case INTERNAL:
				res.instructions.add(new ArgumentedInstruction(loadInternal2, arrBits))
			case dir == PARAMETER || dir == CONSTANT: {
				val Optional<BigInteger> bVal = ConstantEvaluate::valueOf(obj, context)
				if (!bVal.present)
					throw new IllegalArgumentException("Const/param should be constant")
				res.constants.put(refName, bVal.get)
				res.instructions.add(new ArgumentedInstruction(loadConstant, refName))
			}
			case IN: {
				res.instructions.add(new ArgumentedInstruction(loadInternal2, arrBits))
			}
			case dir == OUT || dir == INOUT: {
				res.instructions.add(new ArgumentedInstruction(loadInternal2, arrBits))
			}
			default:
				throw new IllegalArgumentException("Did not expect obj here" + dir)
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLTernary obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.setPredicate(true)
		res.append(obj.ifExpr.toSimulationModel(context))
		val FluidFrame thenFrame = obj.thenExpr.toSimulationModel(context)
		thenFrame.addPredicate(res.id, true)
		res.addReferencedFrame(thenFrame)
		val FluidFrame elseFrame = obj.thenExpr.toSimulationModel(context)
		elseFrame.addPredicate(res.id, false)
		res.addReferencedFrame(elseFrame)
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLLiteral obj, HDLEvaluationContext context) {
		val BigInteger value = obj.valueAsBigInt

		val FluidFrame res = new FluidFrame
		if (BigInteger::ZERO.equals(value)) {
			res.add(const0)
			return res
		}
		val String key = value.toString
		res.constants.put(key, value)
		res.instructions.add(new ArgumentedInstruction(loadConstant, key))
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLBitOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		switch (obj.type) {
			case AND:
				res.add(and)
			case LOGI_AND:
				res.add(logiAnd)
			case OR:
				res.add(or)
			case LOGI_OR:
				res.add(logiOr)
			case XOR:
				res.add(xor)
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLArithOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		switch (obj.type) {
			case DIV:
				res.add(div)
			case MINUS:
				res.add(minus)
			case MOD:
				throw new IllegalArgumentException("Mod is not supported as Instruction")
			case MUL:
				res.add(mul)
			case PLUS:
				res.add(plus)
			case POW:
				throw new IllegalArgumentException("Pow is not supported as Instruction")
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLShiftOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		switch (obj.type) {
			case SLL:
				res.add(sll)
			case SRA:
				res.add(sra)
			case SRL:
				res.add(srl)
		}
		return res
	}
}
