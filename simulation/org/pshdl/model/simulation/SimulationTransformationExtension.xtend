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
package org.pshdl.model.simulation

import org.pshdl.interpreter.utils.FluidFrame
import org.pshdl.interpreter.utils.FluidFrame$ArgumentedInstruction
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLAssignment
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLReference
import org.pshdl.model.HDLRegisterConfig
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableDeclaration$HDLDirection
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.evaluation.HDLEvaluationContext
import static org.pshdl.model.evaluation.ConstantEvaluate.*
import static org.pshdl.model.extensions.FullNameExtension.*
import static org.pshdl.model.extensions.TypeExtension.*
import org.pshdl.model.types.builtIn.HDLPrimitives
import java.math.BigInteger
import java.util.ArrayList
import java.util.Iterator
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLResolvedRef

import static org.pshdl.interpreter.utils.FluidFrame$Instruction.*
import static org.pshdl.model.HDLArithOp$HDLArithOpType.*
import static org.pshdl.model.HDLEqualityOp$HDLEqualityOpType.*
import static org.pshdl.model.HDLBitOp$HDLBitOpType.*
import static org.pshdl.model.HDLClass.*
import static org.pshdl.model.HDLManip$HDLManipType.*
import static org.pshdl.model.HDLPrimitive$HDLPrimitiveType.*
import static org.pshdl.model.HDLRegisterConfig$HDLRegClockType.*
import static org.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static org.pshdl.model.HDLVariableDeclaration$HDLDirection.*
import static org.pshdl.model.simulation.SimulationTransformationExtension.*
import com.google.common.base.Optional
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLSwitchStatement
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEnumDeclaration
import org.pshdl.model.HDLInterfaceDeclaration
import org.pshdl.model.evaluation.ConstantEvaluate

class SimulationTransformationExtension {
	private static SimulationTransformationExtension INST = new SimulationTransformationExtension

	def static FluidFrame simulationModelOf(IHDLObject obj, HDLEvaluationContext context) {
		return INST.toSimulationModel(obj, context)
	}

	def dispatch FluidFrame toSimulationModel(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLStatement obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLInterfaceDeclaration obj, HDLEvaluationContext context) {
		return new FluidFrame
	}

	def dispatch FluidFrame toSimulationModel(HDLEnumDeclaration obj, HDLEvaluationContext context) {
		return new FluidFrame
	}

	def dispatch FluidFrame toSimulationModel(HDLVariableDeclaration obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		val type = obj.resolveType.get
		val width = if(type.classType === HDLPrimitive) HDLPrimitives::getWidth(type, context) else 32
		for (HDLVariable hVar : obj.variables) {
			res.addWith(fullNameOf(hVar).toString, width)
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLSwitchStatement obj, HDLEvaluationContext context) {
		val name= fullNameOf(obj).toString;
		val res = obj.caseExp.toSimulationModel(context)
		res.setName(name)
		val type= typeOf(obj.caseExp).get
		val width = if(type.classType === HDLPrimitive) HDLPrimitives::getWidth(type, context) else 32
		res.addWith(name, width)
		for (HDLSwitchCaseStatement c : obj.cases) {
			val cName = fullNameOf(c).toString
			val defaultFrame = new FluidFrame(FluidFrame::PRED_PREFIX + cName)
			if (c.label == null) {
				for (cSub : obj.cases) {
					if (cSub != c)
						defaultFrame.add(
							new ArgumentedInstruction(negPredicate, fullNameOf(cSub).toString))
				}
				defaultFrame.add(const1)
			} else {
				val const = valueOf(c.label)
				var int l
				if (const.present)
					l=const.get.intValue
				else {
					if (c.label.classType === HDLEnumRef) {
						val HDLEnumRef ref = c.label as HDLEnumRef
						val hEnum = ref.resolveHEnum.get
						val hVar = ref.resolveVar.get
						l = hEnum.enums.indexOf(hVar)
					} else {
						throw new IllegalArgumentException("Unsupported label type");
					}
				}
				defaultFrame.add(new ArgumentedInstruction(loadInternal, name))
				defaultFrame.addConstant("label", BigInteger::valueOf(l))
				defaultFrame.add(eq)
			}
			for (d : c.dos) {
				val subDo = d.toSimulationModel(context)
				if (subDo.hasInstructions)
					subDo.instructions.addFirst(new ArgumentedInstruction(posPredicate, cName))
				defaultFrame.addReferencedFrame(subDo)
			}
			res.addReferencedFrame(defaultFrame)
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLIfStatement obj, HDLEvaluationContext context) {
		val name = fullNameOf(obj).toString
		val ifModel = obj.ifExp.toSimulationModel(context)
		ifModel.setName(FluidFrame::PRED_PREFIX + name)
		for (s : obj.thenDo) {
			val thenDo = s.toSimulationModel(context)
			if (thenDo.hasInstructions)
				thenDo.instructions.addFirst(new ArgumentedInstruction(posPredicate, name))
			ifModel.addReferencedFrame(thenDo)
		}
		for (s : obj.elseDo) {
			val elseDo = s.toSimulationModel(context)
			if (elseDo.hasInstructions)
				elseDo.instructions.addFirst(new ArgumentedInstruction(negPredicate, name))
			ifModel.addReferencedFrame(elseDo)
		}
		return ifModel
	}

	def dispatch FluidFrame toSimulationModel(HDLAssignment obj, HDLEvaluationContext context) {
		val HDLReference left = obj.left
		val HDLVariable hVar = left.resolveVar
		var HDLRegisterConfig config = hVar.registerConfig
		var FluidFrame res
		if (config !== null)
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true) + FluidFrame::REG_POSTFIX)
		else
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true))
		if (config !== null) {
			config = config.normalize
			val HDLVariable clk = config.resolveClk.get
			val String name = fullNameOf(clk).toString
			if (config.clockType == RISING)
				res.add(new ArgumentedInstruction(isRisingEdge, name))
			else
				res.add(new ArgumentedInstruction(isFallingEdge, name))
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
		sb.append(fullNameOf(hVar.resolveVar.get))
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
			val int width = HDLPrimitives::getWidth(typeOf(exp).get, context)
			res.add(new ArgumentedInstruction(concat, width.toString))
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLUnit obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		for (HDLStatement stmnt : obj.inits) {
			res.addReferencedFrame(stmnt.toSimulationModel(context))
		}
		for (HDLStatement stmnt : obj.statements) {
			res.addReferencedFrame(stmnt.toSimulationModel(context))
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLManip obj, HDLEvaluationContext context) {
		val FluidFrame res = obj.target.toSimulationModel(context)
		switch (obj.type) {
			case ARITH_NEG:
				res.add(arith_neg)
			case BIT_NEG:
				res.add(bit_neg)
			case LOGIC_NEG:
				res.add(logiNeg)
			case CAST: {
				val HDLPrimitive prim = obj.castTo as HDLPrimitive
				val HDLPrimitive current = typeOf(obj.target).get as HDLPrimitive
				val String currentWidth = getWidth(current, context)
				val String primWidth = getWidth(prim, context)
				switch (prim.type) {
					case prim.type === INTEGER || prim.type === INT:
						res.instructions.add(new ArgumentedInstruction(cast_int, primWidth, currentWidth))
					case prim.type === UINT || prim.type === NATURAL:
						res.instructions.add(new ArgumentedInstruction(cast_uint, primWidth, currentWidth))
					case prim.type === BIT || prim.type === BITVECTOR: {
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
			case current.type === INTEGER || current.type === NATURAL:
				return "32"
			case current.type === INT || current.type === UINT || current.type === BITVECTOR: {
				val res = valueOf(current.width, context)
				if (res.present)
					return res.get.toString
			}
		}
		throw new IllegalArgumentException(current + " is not a valid type");
	}

	def dispatch FluidFrame toSimulationModel(HDLEnumRef obj, HDLEvaluationContext context) {
		val res = new FluidFrame
		val hEnum = obj.resolveHEnum.get
		val hVar = obj.resolveVar.get
		val idx = hEnum.enums.indexOf(hVar)
		res.addConstant(fullNameOf(hVar).toString, BigInteger::valueOf(idx))
		return res
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
				res.instructions.add(new ArgumentedInstruction(loadInternal, arrBits))
			case dir === PARAMETER || dir === CONSTANT: {
				val Optional<BigInteger> bVal = valueOf(obj, context)
				if (!bVal.present)
					throw new IllegalArgumentException("Const/param should be constant")
				res.addConstant(refName, bVal.get)
			}
			case IN: {
				res.instructions.add(new ArgumentedInstruction(loadInternal, arrBits))
			}
			case dir === OUT || dir === INOUT: {
				res.instructions.add(new ArgumentedInstruction(loadInternal, arrBits))
			}
			default:
				throw new IllegalArgumentException("Did not expect obj here" + dir)
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLLiteral obj, HDLEvaluationContext context) {
		val BigInteger value = obj.valueAsBigInt

		val FluidFrame res = new FluidFrame
		if (0bi.equals(value)) {
			res.add(const0)
			return res
		}
		if (1bi.equals(value)) {
			res.add(const1)
			return res
		}
		if (2bi.equals(value)) {
			res.add(const2)
			return res
		}

		//		if (value.xor(value).equals(0bi)) {
		//			res.add(new ArgumentedInstruction(constAll1, value.bitLength.toString))
		//			return res
		//		}
		val String key = value.toString
		res.constants.put(key, value)
		res.add(new ArgumentedInstruction(loadConstant, key))
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLEqualityOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		switch (obj.type) {
			case EQ:
				res.add(eq)
			case NOT_EQ:
				res.add(not_eq)
			case GREATER:
				res.add(greater)
			case GREATER_EQ:
				res.add(greater_eq)
			case LESS:
				res.add(less)
			case LESS_EQ:
				res.add(less_eq)
		}
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
