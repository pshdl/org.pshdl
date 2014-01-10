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

import org.pshdl.model.simulation.FluidFrame
import org.pshdl.model.simulation.FluidFrame$ArgumentedInstruction
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

import static org.pshdl.interpreter.utils.Instruction.*
import static org.pshdl.model.HDLArithOp$HDLArithOpType.*
import static org.pshdl.model.HDLEqualityOp$HDLEqualityOpType.*
import static org.pshdl.model.HDLBitOp$HDLBitOpType.*
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
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation$Direction
import org.pshdl.interpreter.VariableInformation$Type
import java.util.LinkedList
import org.pshdl.interpreter.InternalInformation
import org.pshdl.model.HDLArrayInit
import org.pshdl.model.HDLClass
import java.util.List
import java.util.Set
import java.util.HashSet
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType
import org.pshdl.model.HDLAnnotation
import org.pshdl.model.utils.HDLQualifiedName

class SimulationTransformationExtension {
	private static SimulationTransformationExtension INST = new SimulationTransformationExtension

	public static final char ANNO_VALUE_SEP = '|'

	def static FluidFrame simulationModelOf(IHDLObject obj, HDLEvaluationContext context) {
		return INST.toSimulationModel(obj, context.withEnumAndBool(true, true))
	}

	def dispatch FluidFrame toSimulationModel(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLStatement obj, HDLEvaluationContext context) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModelPred(HDLStatement obj, ArgumentedInstruction predicate,
		HDLEvaluationContext context) {
		var res = obj.toSimulationModel(context)
		if (res.hasInstructions)
			res.instructions.addFirst(predicate)
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLInterfaceDeclaration obj, HDLEvaluationContext context) {
		return new FluidFrame
	}

	def dispatch FluidFrame toSimulationModel(HDLEnumDeclaration obj, HDLEvaluationContext context) {
		return new FluidFrame
	}

	def dispatch FluidFrame toSimulationModel(HDLExpression obj, HDLEvaluationContext context, String varName) {
		val res = new FluidFrame
		res.append(obj.toSimulationModel(context))
		res.add(new ArgumentedInstruction(writeInternal, varName))
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLArrayInit obj, HDLEvaluationContext context, String varName) {
		val res = new FluidFrame
		var pos = 0
		for (HDLExpression exp : obj.exp) {
			res.append(HDLLiteral::get(pos).toSimulationModel(context))
			res.add(pushAddIndex)
			res.append(exp.toSimulationModel(context, varName))
			pos = pos + 1
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLVariableDeclaration obj, HDLEvaluationContext context) {
		val type = obj.resolveType.get
		val width = if(type.classType === HDLClass::HDLPrimitive) HDLPrimitives::getWidth(type, context) else 32
		val isReg = obj.register !== null
		val FluidFrame res = new FluidFrame("#null", false)
		res.addVar(new VariableInformation(Direction::INTERNAL, "#null", 1, Type::BIT, false, false, false, null))
		var Direction dir
		switch (obj.direction) {
			case IN: dir = Direction::IN
			case OUT: dir = Direction::OUT
			case INOUT: dir = Direction::INOUT
			default: dir = Direction::INTERNAL
		}
		for (HDLVariable hVar : obj.variables) {
			var clock = hVar.getAnnotation(HDLBuiltInAnnotations::clock) !== null
			var reset = hVar.getAnnotation(HDLBuiltInAnnotations::reset) !== null
			val varName = fullNameOf(hVar).toString
			val dims = new LinkedList<Integer>()
			for (HDLExpression dim : hVar.dimensions)
				dims.add(valueOf(dim, context).get.intValue)
			var vType = Type::BIT
			if (type.classType === HDLClass::HDLPrimitive) {
				switch ((type as HDLPrimitive).type) {
					case INT: vType = Type::INT
					case INTEGER: vType = Type::INT
					case UINT: vType = Type::UINT
					case NATURAL: vType = Type::UINT
				}
			}
			val allAnnos = hVar.annotations + obj.annotations
			res.addVar(
				new VariableInformation(dir, varName, width, vType, isReg, clock, reset, allAnnos.toAnnoString, dims))
		}
		if (isReg) {
			val config = obj.register.normalize
			val rst = config.rst
			val String rstName = fullNameOf(rst).toString
			if (config.resetType === HDLRegisterConfig$HDLRegResetActiveType::HIGH)
				res.add(new ArgumentedInstruction(posPredicate, rstName))
			else
				res.add(new ArgumentedInstruction(negPredicate, rstName))
			if (config.syncType === HDLRegisterConfig$HDLRegSyncType::SYNC) {
				val HDLExpression clk = config.clk
				val String name = fullNameOf(clk).toString
				if (config.clockType === RISING)
					res.add(new ArgumentedInstruction(isRisingEdge, name))
				else
					res.add(new ArgumentedInstruction(isFallingEdge, name))
			}
			createInit(config, obj, context, res, true);
			if (config.syncType === HDLRegSyncType::ASYNC)
				createInit(config, obj, context, res, false);
			res.add(const0)
		}
		return res
	}

	def String[] toAnnoString(Iterable<HDLAnnotation> annotations) {
		annotations.map [
			if(it.value === null) it.name.substring(1) else it.name.substring(1) + ANNO_VALUE_SEP + it.value
		]
	}

	def void createInit(HDLRegisterConfig config, HDLVariableDeclaration obj, HDLEvaluationContext context,
		FluidFrame res, boolean toReg) {
		if (config.resetValue instanceof HDLArrayInit) {
			for (HDLVariable hVar : obj.variables) {
				res.add(const0)
				var varName = fullNameOf(hVar).toString
				if (toReg)
					varName = varName + InternalInformation::REG_POSTFIX
				res.add(new ArgumentedInstruction(writeInternal, varName))
				val HDLArrayInit arr = config.resetValue as HDLArrayInit
				res.append(arr.toSimulationModel(context, varName))
			}
		} else {
			val resetFrame = config.resetValue.toSimulationModel(context)
			for (HDLVariable hVar : obj.variables) {
				var varName = fullNameOf(hVar).toString
				if (toReg)
					varName = varName + InternalInformation::REG_POSTFIX
				res.append(resetFrame)
				res.add(new ArgumentedInstruction(writeInternal, varName))
			}
		}
	}

	def dispatch FluidFrame toSimulationModel(HDLSwitchStatement obj, HDLEvaluationContext context) {
		obj.toSimulationModelPred(null, context)
	}

	def dispatch FluidFrame toSimulationModelPred(HDLSwitchStatement obj, ArgumentedInstruction predicate,
		HDLEvaluationContext context) {
		val name = fullNameOf(obj).toString;
		val res = obj.caseExp.toSimulationModel(context)
		res.setName(name)
		val type = typeOf(obj.caseExp).get
		val width = if(type.classType === HDLClass::HDLPrimitive) HDLPrimitives::getWidth(type, context) else 32
		res.addVar(new VariableInformation(Direction::INTERNAL, name, width, Type::BIT, false, false, false, null))
		for (HDLSwitchCaseStatement c : obj.cases) {
			val cName = fullNameOf(c).toString
			val caseFrame = new FluidFrame(InternalInformation::PRED_PREFIX + cName, false)
			if (predicate !== null)
				caseFrame.add(predicate)
			caseFrame.createPredVar
			if (c.label === null) {
				for (cSub : obj.cases) {
					if (cSub != c)
						caseFrame.add(new ArgumentedInstruction(negPredicate, fullNameOf(cSub).toString))
				}
				caseFrame.add(const1)
				caseFrame.add(const1)
				caseFrame.add(eq)
			} else {
				val const = valueOf(c.label)
				var int l
				if (const.present)
					l = const.get.intValue
				else {
					if (c.label.classType === HDLClass::HDLEnumRef) {
						val HDLEnumRef ref = c.label as HDLEnumRef
						l = ref.asInt
					} else {
						throw new IllegalArgumentException("Unsupported label type");
					}
				}
				caseFrame.add(new ArgumentedInstruction(loadInternal, name))
				caseFrame.addConstant("label", BigInteger::valueOf(l))
				caseFrame.add(eq)
			}
			for (d : c.dos) {
				val subDo = d.toSimulationModelPred(new ArgumentedInstruction(posPredicate, cName), context)
				caseFrame.addReferencedFrame(subDo)
			}
			res.addReferencedFrame(caseFrame)
		}
		return res
	}

	def asInt(HDLEnumRef ref) {
		val hEnum = ref.resolveHEnum.get
		val hVar = ref.resolveVar.get
		return hEnum.enums.indexOf(hVar)
	}

	def dispatch FluidFrame toSimulationModel(HDLIfStatement obj, HDLEvaluationContext context) {
		val name = fullNameOf(obj).toString
		val ifModel = obj.ifExp.toSimulationModel(context)
		ifModel.setName(InternalInformation::PRED_PREFIX + name)
		ifModel.createPredVar
		for (s : obj.thenDo) {
			val thenDo = s.toSimulationModelPred(new ArgumentedInstruction(posPredicate, name), context)
			ifModel.addReferencedFrame(thenDo)
		}
		for (s : obj.elseDo) {
			val elseDo = s.toSimulationModelPred(new ArgumentedInstruction(negPredicate, name), context)
			ifModel.addReferencedFrame(elseDo)
		}
		return ifModel
	}

	def dispatch FluidFrame toSimulationModel(HDLAssignment obj, HDLEvaluationContext context) {
		val HDLReference left = obj.left
		val HDLVariable hVar = left.resolveVar
		val constant = hVar.direction === CONSTANT
		var HDLRegisterConfig config = hVar.registerConfig
		var FluidFrame res
		if (config !== null)
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true) + InternalInformation::REG_POSTFIX,
				constant)
		else
			res = new FluidFrame(getVarName(obj.left as HDLVariableRef, true), constant)
		if (config !== null) {
			config = config.normalize
			val clk = config.clk
			val String name = fullNameOf(clk).toString
			if (config.clockType == RISING)
				res.add(new ArgumentedInstruction(isRisingEdge, name))
			else
				res.add(new ArgumentedInstruction(isFallingEdge, name))
			val rst = config.rst
			val String rstName = fullNameOf(rst).toString
			if (config.resetType === HDLRegisterConfig$HDLRegResetActiveType::HIGH)
				res.add(new ArgumentedInstruction(negPredicate, rstName))
			else
				res.add(new ArgumentedInstruction(posPredicate, rstName))
		}
		res.append(obj.right.toSimulationModel(context))
		var boolean hasBits = false
		if (left instanceof HDLVariableRef) {
			val HDLVariableRef variableRef = left as HDLVariableRef
			if (!variableRef.bits.isEmpty)
				hasBits = true
			var fixedArray = true
			for (HDLExpression idx : variableRef.array)
				if (!valueOf(idx, context).present)
					fixedArray = false
			if (!fixedArray)
				for (HDLExpression idx : variableRef.array) {
					res.append(idx.toSimulationModel(context));
					res.add(pushAddIndex)
				}
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
			val s = valueOf(exp)
			if (s.present)
				sb.append('[').append(s.get).append(']')
			else
				sb.append('[-1]')

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
		val init = iter.next
		res.append(init.toSimulationModel(context))
		var int owidth = HDLPrimitives::getWidth(typeOf(init).get, context)
		while (iter.hasNext) {
			val HDLExpression exp = iter.next
			res.append(exp.toSimulationModel(context))
			val int width = HDLPrimitives::getWidth(typeOf(exp).get, context)
			res.add(new ArgumentedInstruction(concat, owidth.toString, width.toString))
			owidth = owidth + width
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
		res.annotations = obj.annotations.toAnnoString
		val regConfigs = obj.getAllObjectsOf(typeof(HDLRegisterConfig), true)
		val Set<HDLQualifiedName> lst = new HashSet
		for (HDLRegisterConfig reg : regConfigs) {
			val HDLQualifiedName rstVar = fullNameOf(reg.rst)
			if (!lst.contains(rstVar)) {
				lst.add(rstVar)
				val rstVarName = rstVar.toString
				val rstFrame = new FluidFrame(InternalInformation::PRED_PREFIX + rstVarName, false)
				rstFrame.add(new ArgumentedInstruction(loadInternal, rstVarName))
				rstFrame.add(const0)
				rstFrame.add(not_eq)
				rstFrame.createPredVar
				res.addReferencedFrame(rstFrame)
			}
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLManip obj, HDLEvaluationContext context) {
		val FluidFrame res = obj.target.toSimulationModel(context)
		switch (obj.type) {
			case ARITH_NEG: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(arith_neg, width.toString))
			}
			case BIT_NEG: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(bit_neg, width.toString))
			}
			case LOGIC_NEG:
				res.add(logiNeg)
			case CAST: {
				val HDLPrimitive prim = obj.castTo as HDLPrimitive
				val HDLPrimitive current = typeOf(obj.target).get as HDLPrimitive
				val int currentWidth = getWidth(current, context)
				var int primWidth = getWidth(prim, context)
				switch (prim.type) {
					case current.type === INTEGER || current.type === INT:
						res.instructions.add(
							new ArgumentedInstruction(cast_int, primWidth.toString, currentWidth.toString))
					case current.type === BIT || current.type === BITVECTOR:
						res.instructions.add(
							new ArgumentedInstruction(cast_uint, primWidth.toString, currentWidth.toString))
					case current.type === UINT || current.type === NATURAL:
						res.instructions.add(
							new ArgumentedInstruction(cast_uint, primWidth.toString, currentWidth.toString))
					default:
						throw new IllegalArgumentException("Cast to type:" + prim.type + " not supported")
				}
			}
		}
		return res
	}

	def private int getWidth(HDLPrimitive current, HDLEvaluationContext context) {
		switch (current.type) {
			case BIT:
				return 1
			case current.type === INTEGER || current.type === NATURAL:
				return 32
			case current.type === INT || current.type === UINT || current.type === BITVECTOR: {
				val res = valueOf(current.width, context)
				if (res.present)
					return res.get.intValue
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
		val String refName = obj.getVarName(false)
		var fixedArray = true
		for (HDLExpression idx : obj.array)
			if (!valueOf(idx, context).present)
				fixedArray = false
		if (!fixedArray)
			for (HDLExpression idx : obj.array) {
				res.append(idx.toSimulationModel(context));
				res.add(pushAddIndex)
			}
		val bits = new ArrayList<String>(obj.bits.size + 1)
		bits.add(refName)
		if (!obj.bits.isEmpty) {
			for (HDLRange  r : obj.bits) {
				bits.add(r.toString)
			}
		}
		val HDLDirection dir = hVar.get.direction
		switch (dir) {
			case INTERNAL:
				res.add(new ArgumentedInstruction(loadInternal, bits))
			case dir === PARAMETER || dir === CONSTANT: {
				if (!fixedArray)
					res.add(new ArgumentedInstruction(loadInternal, bits))
				else {
					val Optional<BigInteger> bVal = valueOf(obj, context)
					if (!bVal.present)
						throw new IllegalArgumentException("Const/param should be constant")
					else
						res.addConstant(refName, bVal.get)
				}
			}
			case IN: {
				res.add(new ArgumentedInstruction(loadInternal, bits))
			}
			case dir === OUT || dir === INOUT: {
				res.add(new ArgumentedInstruction(loadInternal, bits))
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

		//		val mask=1bi.shiftLeft(value.bitLength).subtract(1bi)
		//		if (value.not.and(mask).equals(0bi)) {
		//			val ai = new ArgumentedInstruction(constAll1, (value.bitLength).toString)
		//			res.add(ai)
		//			println('''«ai» for value «value»''')
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
			case AND: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(and, width.toString))
			}
			case LOGI_AND:
				res.add(logiAnd)
			case OR: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(or, width.toString))
			}
			case LOGI_OR:
				res.add(logiOr)
			case XOR: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(xor, width.toString))
			}
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLArithOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		val width = obj.targetSizeWithType(context)
		switch (obj.type) {
			case DIV:
				res.add(new ArgumentedInstruction(div, width.toString))
			case MINUS:
				res.add(new ArgumentedInstruction(minus, width.toString))
			case MOD:
				throw new IllegalArgumentException("Mod is not supported as Instruction")
			case MUL:
				res.add(new ArgumentedInstruction(mul, width.toString))
			case PLUS:
				res.add(new ArgumentedInstruction(plus, width.toString))
			case POW:
				throw new IllegalArgumentException("Pow is not supported as Instruction")
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLShiftOp obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame
		res.append(obj.left.toSimulationModel(context))
		res.append(obj.right.toSimulationModel(context))
		val width = obj.targetSizeWithType(context)
		switch (obj.type) {
			case SLL:
				res.add(new ArgumentedInstruction(sll, width.toString))
			case SRA: {
				val type = typeOf(obj.left)
				val prim = type.get as HDLPrimitive
				if (prim.type === INTEGER || prim.type === INT)
					res.add(new ArgumentedInstruction(sra, width.toString))
				else
					res.add(new ArgumentedInstruction(srl, width.toString))
			}
			case SRL:
				res.add(new ArgumentedInstruction(srl, width.toString))
		}
		return res
	}

	def targetSizeWithType(HDLExpression op, HDLEvaluationContext context) {
		val HDLPrimitive type = typeOf(op).get as HDLPrimitive
		val width = HDLPrimitives::getWidth(typeOf(op).get, context)
		if (type.type === HDLPrimitiveType::INT || type.type === HDLPrimitiveType::INTEGER)
			return (width << 1).bitwiseOr(1)
		return (width << 1)
	}

}
