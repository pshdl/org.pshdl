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

import com.google.common.base.Optional
import com.google.common.collect.Lists
import java.math.BigInteger
import java.util.ArrayList
import java.util.HashSet
import java.util.Iterator
import java.util.LinkedHashSet
import java.util.LinkedList
import java.util.List
import java.util.Set
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation.Direction
import org.pshdl.interpreter.VariableInformation.Type
import org.pshdl.model.HDLAnnotation
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLArrayInit
import org.pshdl.model.HDLAssignment
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLBlock
import org.pshdl.model.HDLClass
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLEnum
import org.pshdl.model.HDLEnumDeclaration
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLInterfaceDeclaration
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLReference
import org.pshdl.model.HDLRegisterConfig
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType
import org.pshdl.model.HDLResolvedRef
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLSwitchStatement
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableDeclaration.HDLDirection
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.evaluation.ConstantEvaluate
import org.pshdl.model.evaluation.HDLEvaluationContext
import org.pshdl.model.extensions.FullNameExtension
import org.pshdl.model.simulation.FluidFrame
import org.pshdl.model.simulation.FluidFrame.ArgumentedInstruction
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import org.pshdl.model.utils.HDLQualifiedName

import static org.pshdl.interpreter.utils.Instruction.*
import static org.pshdl.model.HDLArithOp.HDLArithOpType.*
import static org.pshdl.model.HDLBitOp.HDLBitOpType.*
import static org.pshdl.model.HDLEqualityOp.HDLEqualityOpType.*
import static org.pshdl.model.HDLManip.HDLManipType.*
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.*
import static org.pshdl.model.HDLRegisterConfig.HDLRegClockType.*
import static org.pshdl.model.HDLShiftOp.HDLShiftOpType.*
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.*
import static org.pshdl.model.evaluation.ConstantEvaluate.*
import static org.pshdl.model.extensions.FullNameExtension.*
import static org.pshdl.model.extensions.TypeExtension.*
import static org.pshdl.model.simulation.SimulationTransformationExtension.*
import org.pshdl.interpreter.FunctionInformation
import org.pshdl.model.HDLFunctionParameter
import org.pshdl.interpreter.ParameterInformation
import org.pshdl.interpreter.ParameterInformation.RWType
import org.pshdl.model.HDLType
import org.pshdl.model.extensions.TypeExtension
import org.pshdl.model.utils.HDLCodeGenerationException

class SimulationTransformationExtension {
	private static SimulationTransformationExtension INST = new SimulationTransformationExtension

	public static final char ANNO_VALUE_SEP = '|'
	private static final String PSEX_STAGE = "PSEX"

	def static FluidFrame simulationModelOf(HDLUnit obj, HDLEvaluationContext context) {
		return INST.toSimulationModelUnit(obj, context.withEnumAndBool(true, true))
	}

	def dispatch FluidFrame toSimulationModel(IHDLObject obj, HDLEvaluationContext context, String process) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLExpression obj, HDLEvaluationContext context, String process) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLStatement obj, HDLEvaluationContext context, String process) {
		throw new RuntimeException("Not implemented! " + obj.classType + " " + obj)
	}

	def dispatch FluidFrame toSimulationModel(HDLBlock obj, HDLEvaluationContext context, String process) {
		var newProcess = process
		if (obj.process)
			newProcess = FullNameExtension.fullNameOf(obj).lastSegment.replaceAll('@', '')
		val frame = new FluidFrame(obj, null, false, newProcess)
		for (HDLStatement stmnt : obj.statements) {
			frame.addReferencedFrame(stmnt.toSimulationModel(context, frame.simProcess))
		}
		return frame
	}

	def dispatch FluidFrame toSimulationModelPred(HDLBlock obj, ArgumentedInstruction predicate,
		HDLEvaluationContext context, String process) {
		var newProcess = process
		if (obj.process)
			newProcess = FullNameExtension.fullNameOf(obj).lastSegment.replaceAll('@', '')
		val frame = new FluidFrame(obj, null, false, newProcess)
		for (HDLStatement stmnt : obj.statements) {
			frame.addReferencedFrame(stmnt.toSimulationModelPred(predicate, context, frame.simProcess))
		}
		return frame
	}

	def dispatch FluidFrame toSimulationModelPred(HDLStatement obj, ArgumentedInstruction predicate,
		HDLEvaluationContext context, String process) {
		var res = obj.toSimulationModel(context, process)
		if (res.hasInstructions)
			res.instructions.addFirst(predicate)
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLInterfaceDeclaration obj, HDLEvaluationContext context,
		String process) {
		return new FluidFrame(obj, null)
	}

	def dispatch FluidFrame toSimulationModel(HDLFunctionCall obj, HDLEvaluationContext context, String process) {
		val constVal = ConstantEvaluate.valueOf(obj, context)
		if (constVal.present) {
			return HDLLiteral.get(constVal.get).toSimulationModel(context, process)
		}
		val res = new FluidFrame(obj, process)
		for (HDLExpression e : obj.params) {
			res.append(e.toSimulationModel(context, process))
		}
		val func = obj.resolveFunctionForced(PSEX_STAGE)
		val returnType = func.returnType.toParameterInformation(context)
		val args = func.args?.map[toParameterInformation(context)]
		val isStatement = obj.getContainer(HDLStatement) === null
		val fi = new FunctionInformation(func.name, isStatement, returnType, args, func.annotations.toAnnoString)
		val sig = fi.signature
		res.funcs.put(sig, fi)
		res.add(new ArgumentedInstruction(invokeFunction, sig, if(args === null) "0" else args.length.toString))
		return res
	}

	def ParameterInformation toParameterInformation(HDLFunctionParameter parameter, HDLEvaluationContext context) {
		if (parameter === null)
			return null
		val rwType = RWType.getOp(parameter.rw.toString)
		val type = ParameterInformation.Type.getOp(parameter.type.toString)
		val enumSpecRefName = parameter.enumSpecRefName?.toString
		val ifSpec = parameter.ifSpecRefName?.toString
		val funcSpec = parameter.funcSpec?.map[toParameterInformation(context)]
		val funcReturnSpec = parameter.funcReturnSpec?.toParameterInformation(context)
		val name = parameter.name.name
		val widthOpt = valueOf(parameter.width, context)
		val width = if(widthOpt.present) widthOpt.get.intValue else -1
		val dims = parameter.dim?.map[valueOfForced(it, context, PSEX_STAGE).intValue]
		val boolean const = parameter.constant
		return new ParameterInformation(rwType, type, enumSpecRefName, ifSpec, funcSpec, funcReturnSpec, name, width,
			dims, const)
	}

	def dispatch FluidFrame toSimulationModel(HDLEnumDeclaration obj, HDLEvaluationContext context, String process) {
		return new FluidFrame(obj, null)
	}

	def dispatch FluidFrame toSimulationModelInit(HDLExpression obj, HDLEvaluationContext context, String varName,
		String process) {
		val res = new FluidFrame(obj, process)
		res.append(obj.toSimulationModel(context, process))
		res.add(new ArgumentedInstruction(writeInternal, varName))
		return res
	}

	def dispatch FluidFrame toSimulationModelInit(HDLArrayInit obj, HDLEvaluationContext context, String varName,
		String process) {
		val res = new FluidFrame(obj, process)
		var pos = 0
		for (HDLExpression exp : obj.exp) {
			res.append(HDLLiteral.get(pos).toSimulationModel(context, process))
			res.add(new ArgumentedInstruction(pushAddIndex, varName, "0"))
			res.append(exp.toSimulationModelInit(context, varName.addDynamicIdx, process))
			pos = pos + 1
		}
		return res
	}

	def addDynamicIdx(String string) {
		if (string.endsWith(InternalInformation.REG_POSTFIX)) {
			return string.replace(InternalInformation.REG_POSTFIX, "[-1]" + InternalInformation.REG_POSTFIX)
		}
		return string + "[-1]"
	}

	def dispatch FluidFrame toSimulationModel(HDLVariableDeclaration obj, HDLEvaluationContext context,
		String process) {
		val type = obj.resolveTypeForced(PSEX_STAGE)
		var width = if(type.classType === HDLClass.HDLPrimitive) HDLPrimitives.getWidth(type, context) else 32
		val isReg = obj.register !== null
		val simAnno = obj.getAnnotation(HDLSimulator.TB_VAR.name)
		var newProcess = process
		if (newProcess === null && simAnno !== null)
			newProcess = "ONCE"
		val FluidFrame res = new FluidFrame(obj, null, false, newProcess)

		// res.addVar(new VariableInformation(Direction.INTERNAL, "#null", 1, Type.BIT, false, false, false, null))
		var Direction dir
		switch (obj.direction) {
			case IN: dir = Direction.IN
			case OUT: dir = Direction.OUT
			case INOUT: dir = Direction.INOUT
			default: dir = Direction.INTERNAL
		}
		for (HDLVariable hVar : obj.variables) {
			var clock = hVar.getAnnotation(HDLBuiltInAnnotations.clock) !== null
			var reset = hVar.getAnnotation(HDLBuiltInAnnotations.reset) !== null
			val varName = fullNameOf(hVar).toString
			val dims = new LinkedList<Integer>()
			for (HDLExpression dim : hVar.dimensions)
				dims.add(valueOfForced(dim, context, PSEX_STAGE).intValue)
			var vType = asType(type)
			val allAnnos = Lists.newArrayList(hVar.annotations + obj.annotations)
			if (type.classType === HDLClass.HDLEnum) {
				val HDLEnum hEnum = type as HDLEnum
				val enumAnno = new HDLAnnotation().setName("@enumNames").setValue(hEnum.enums.map[name].join(";"))
				allAnnos.add(enumAnno)
			}
			res.addVar(
				new VariableInformation(dir, varName, width, vType, isReg, clock, reset, allAnnos.toAnnoString, dims))
		}
		if (isReg) {
			val config = obj.register.normalize
			val rst = config.rst
			val String rstName = fullNameOf(rst).toString
			if (config.resetType === HDLRegisterConfig$HDLRegResetActiveType.HIGH)
				res.add(new ArgumentedInstruction(posPredicate, rstName))
			else
				res.add(new ArgumentedInstruction(negPredicate, rstName))
			if (config.syncType === HDLRegisterConfig$HDLRegSyncType.SYNC) {
				val HDLExpression clk = config.clk
				val String name = fullNameOf(clk).toString
				if (config.clockType === RISING)
					res.add(new ArgumentedInstruction(isRisingEdge, name))
				else
					res.add(new ArgumentedInstruction(isFallingEdge, name))
			}
			createInit(config, obj, context, res, true, process);
			if (config.syncType === HDLRegSyncType.ASYNC)
				createInit(config, obj, context, res, false, process);

		// res.add(const0)
		}
		return res
	}

	def asType(HDLType type) {
		var Type vType = Type.BIT
		if (type.classType === HDLClass.HDLPrimitive) {
			switch ((type as HDLPrimitive).type) {
				case ANY_INT: vType = Type.INT
				case INT: vType = Type.INT
				case INTEGER: vType = Type.INT
				case ANY_UINT: vType = Type.UINT
				case UINT: vType = Type.UINT
				case NATURAL: vType = Type.UINT
				case ANY_BIT: vType = Type.BIT
				case BIT: vType = Type.BIT
				case BITVECTOR: vType = Type.BIT
				case BOOL: vType = Type.BOOL
				case STRING: vType = Type.STRING
			}
		}
		if (type.classType === HDLClass.HDLEnum) {
			vType = Type.ENUM
		}
		return vType
	}

	def String[] toAnnoString(Iterable<HDLAnnotation> annotations) {
		annotations.map [
			if(it.value === null) it.name.substring(1) else it.name.substring(1) + ANNO_VALUE_SEP + it.value
		]
	}

	def void createInit(HDLRegisterConfig config, HDLVariableDeclaration obj, HDLEvaluationContext context,
		FluidFrame res, boolean toReg, String process) {
		if (config.resetValue instanceof HDLArrayInit) {
			for (HDLVariable hVar : obj.variables) {
				res.add(const0)
				var varName = fullNameOf(hVar).toString
				if (toReg)
					varName = varName + InternalInformation.REG_POSTFIX
				res.add(new ArgumentedInstruction(writeInternal, varName))
				val HDLArrayInit arr = config.resetValue as HDLArrayInit
				res.append(arr.toSimulationModelInit(context, varName, process))
			}
		} else {
			val resetFrame = config.resetValue.toSimulationModel(context, process)
			for (HDLVariable hVar : obj.variables) {
				var varName = fullNameOf(hVar).toString
				if (toReg)
					varName = varName + InternalInformation.REG_POSTFIX
				res.append(resetFrame)
				res.add(new ArgumentedInstruction(writeInternal, varName))
			}
		}
	}

	def dispatch FluidFrame toSimulationModel(HDLSwitchStatement obj, HDLEvaluationContext context, String process) {
		obj.toSimulationModelPred(null, context, process)
	}

	def dispatch FluidFrame toSimulationModelPred(HDLSwitchStatement obj, ArgumentedInstruction predicate,
		HDLEvaluationContext context, String process) {
		val name = fullNameOf(obj).toString;
		val res = obj.caseExp.toSimulationModel(context, process)
		res.setName(name)
		val type = typeOfForced(obj.caseExp, PSEX_STAGE)
		val width = if(type.classType === HDLClass.HDLPrimitive) HDLPrimitives.getWidth(type, context) else 32
		res.addVar(new VariableInformation(Direction.INTERNAL, name, width, Type.BIT, false, false, false, null))
		for (HDLSwitchCaseStatement c : obj.cases) {
			val cName = fullNameOf(c).toString
			val caseFrame = new FluidFrame(obj, InternalInformation.PRED_PREFIX + cName, false, process)
			if (predicate !== null)
				caseFrame.add(predicate)
			caseFrame.createPredVar
			if (c.label === null) {
				for (cSub : obj.cases) {
					if (cSub != c)
						caseFrame.add(new ArgumentedInstruction(negPredicate, fullNameOf(cSub).toString))
				}
				caseFrame.addConstant("true", BigInteger.ONE, Type.BOOL);
			} else {
				val const = valueOf(c.label, context)
				var int l
				if (const.present)
					l = const.get.intValue
				else {
					if (c.label.classType === HDLClass.HDLEnumRef) {
						val HDLEnumRef ref = c.label as HDLEnumRef
						l = ref.asInt
					} else {
						throw new IllegalArgumentException("Unsupported label type");
					}
				}
				caseFrame.add(new ArgumentedInstruction(loadInternal, name))
				caseFrame.addConstant("label", BigInteger.valueOf(l), Type.UINT)
				caseFrame.add(eq)
			}
			for (d : c.dos) {
				val subDo = d.toSimulationModelPred(new ArgumentedInstruction(posPredicate, cName), context, process)
				caseFrame.addReferencedFrame(subDo)
			}
			res.addReferencedFrame(caseFrame)
		}
		return res
	}

	def asInt(HDLEnumRef ref) {
		val hEnum = ref.resolveHEnumForced(PSEX_STAGE)
		val hVar = ref.resolveVarForced(PSEX_STAGE)
		return hEnum.enums.indexOf(hVar)
	}

	def dispatch FluidFrame toSimulationModel(HDLIfStatement obj, HDLEvaluationContext context, String process) {
		val name = fullNameOf(obj).toString
		val ifModel = obj.ifExp.toSimulationModel(context, process)
		ifModel.setName(InternalInformation.PRED_PREFIX + name)
		ifModel.createPredVar
		for (s : obj.thenDo) {
			val thenDo = s.toSimulationModelPred(new ArgumentedInstruction(posPredicate, name), context, process)
			ifModel.addReferencedFrame(thenDo)
		}
		for (s : obj.elseDo) {
			val elseDo = s.toSimulationModelPred(new ArgumentedInstruction(negPredicate, name), context, process)
			ifModel.addReferencedFrame(elseDo)
		}
		return ifModel
	}

	def dispatch FluidFrame toSimulationModel(HDLAssignment obj, HDLEvaluationContext context, String process) {
		if (obj.type !== HDLAssignment.HDLAssignmentType.ASSGN)
			throw new IllegalArgumentException("Did not expect a combined assignment")
		val HDLReference left = obj.left
		val HDLVariable hVar = left.resolveVar
		val constant = hVar.direction === CONSTANT
		var HDLRegisterConfig config = hVar.registerConfig
		var assignmentVarName = getVarName(obj.left as HDLVariableRef, true, context)
		if (config !== null)
			assignmentVarName = assignmentVarName + InternalInformation.REG_POSTFIX
		var res = new FluidFrame(obj, assignmentVarName, constant, process)
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
			if (config.resetType === HDLRegisterConfig$HDLRegResetActiveType.HIGH)
				res.add(new ArgumentedInstruction(negPredicate, rstName))
			else
				res.add(new ArgumentedInstruction(posPredicate, rstName))
		}
		res.append(obj.right.toSimulationModel(context, process))
		if (left instanceof HDLVariableRef) {
			val HDLVariableRef variableRef = left as HDLVariableRef
			createPushIndex(variableRef.array, context, res, process, assignmentVarName)
			createPushIndexBits(variableRef.bits, context, res, process, assignmentVarName)
		}
		return res
	}

	def createPushIndexBits(ArrayList<HDLRange> array, HDLEvaluationContext context, FluidFrame res, String process,
		String assignmentVarName) {
		var fixedBit = true
		for (HDLRange idx : array)
			if (!valueOf(idx.to, context).present)
				fixedBit = false
		if (!fixedBit)
			for (HDLRange idx : array) {
				res.append(idx.to.toSimulationModel(context, process));
				res.add(new ArgumentedInstruction(pushAddIndex, assignmentVarName, "1"))
			}
	}

	def createPushIndex(ArrayList<HDLExpression> array, HDLEvaluationContext context, FluidFrame res, String process,
		String assignmentVarName) {
		var fixedArray = true
		for (HDLExpression idx : array)
			if (!valueOf(idx, context).present)
				fixedArray = false
		if (!fixedArray)
			for (HDLExpression idx : array) {
				res.append(idx.toSimulationModel(context, process));
				res.add(new ArgumentedInstruction(pushAddIndex, assignmentVarName, "0"))
			}
	}

	def HDLVariable resolveVar(HDLReference reference) {
		if (reference instanceof HDLUnresolvedFragment)
			throw new RuntimeException("Can not use unresolved fragments")
		return (reference as HDLResolvedRef).resolveVar.get
	}

	def static String getVarName(HDLVariableRef hVar, boolean withBits, HDLEvaluationContext context) {
		val StringBuilder sb = new StringBuilder
		sb.append(fullNameOf(hVar.resolveVar.get))
		for (HDLExpression exp : hVar.array) {
			val s = valueOf(exp, context)
			if (s.present)
				sb.append('[').append(s.get).append(']')
			else
				sb.append('[-1]')

		}
		if (withBits) {
			for (HDLRange exp : hVar.bits) {
				val s = valueOf(exp.to, context)
				if (s.present)
					sb.append('{').append(exp).append('}')
				else
					sb.append('{-1}')
			}
		}
		return sb.toString
	}

	def dispatch FluidFrame toSimulationModel(HDLConcat obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		val Iterator<HDLExpression> iter = obj.cats.iterator
		val init = iter.next
		res.append(init.toSimulationModel(context, process))
		val initType=typeOfForced(init, PSEX_STAGE)
		var int owidth = HDLPrimitives.getWidth(initType, context)
		while (iter.hasNext) {
			val HDLExpression exp = iter.next
			res.append(exp.toSimulationModel(context, process))
			val expType=typeOfForced(exp, PSEX_STAGE)
			val int width = HDLPrimitives.getWidth(expType, context)
			res.add(new ArgumentedInstruction(concat, owidth.toString, width.toString))
			owidth = owidth + width
		}
		return res
	}

	def FluidFrame toSimulationModelUnit(HDLUnit obj, HDLEvaluationContext context) {
		val FluidFrame res = new FluidFrame(obj, null)
		for (HDLStatement stmnt : obj.inits) {
			res.addReferencedFrame(stmnt.toSimulationModel(context, null))
		}
		for (HDLStatement stmnt : obj.statements) {
			res.addReferencedFrame(stmnt.toSimulationModel(context, null))
		}
		res.annotations = obj.annotations.toAnnoString
		val regConfigs = obj.getAllObjectsOf(typeof(HDLRegisterConfig), true)
		val Set<HDLQualifiedName> lst = new LinkedHashSet
		for (HDLRegisterConfig reg : regConfigs) {
			val HDLQualifiedName rstVar = fullNameOf(reg.rst)
			if (!lst.contains(rstVar)) {
				lst.add(rstVar)
				val rstVarName = rstVar.toString
				val rstFrame = new FluidFrame(obj, InternalInformation.PRED_PREFIX + rstVarName, false, null)
				rstFrame.add(new ArgumentedInstruction(loadInternal, rstVarName))
				rstFrame.add(const0)
				rstFrame.add(not_eq)
				rstFrame.createPredVar
				res.addReferencedFrame(rstFrame)
			}
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLManip obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = obj.target.toSimulationModel(context, process)
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
				val HDLPrimitive current = typeOfForced(obj.target, PSEX_STAGE) as HDLPrimitive
				val HDLPrimitive target = obj.castTo as HDLPrimitive
				val isCurrentInt = current.type === ANY_INT || current.type === INT || current.type === INTEGER
				val isCurrentBit = current.type === ANY_BIT || current.type === BIT || current.type === BITVECTOR
				val isCurrentUint = current.type === ANY_UINT || current.type === UINT || current.type === NATURAL
				val isTargetInt = target.type === ANY_INT || target.type === INT || target.type === INTEGER
				val isTargetUint = target.type === ANY_UINT || target.type === UINT || target.type === NATURAL
				val isTargetBit = target.type === ANY_BIT || target.type === BIT || target.type === BITVECTOR
				val int currentWidth = getWidth(current, context)
				var int targetWidth = getWidth(target, context)
				if (isCurrentInt) {
					res.instructions.add(
						new ArgumentedInstruction(cast_int, Integer.toString(targetWidth),
							Integer.toString(currentWidth)))
					if (isTargetUint) {
						res.instructions.add(
							new ArgumentedInstruction(cast_uint, Integer.toString(targetWidth),
								Integer.toString(targetWidth)))
					}
				} else if (isCurrentUint) {
					res.instructions.add(
						new ArgumentedInstruction(cast_uint, Integer.toString(targetWidth),
							Integer.toString(currentWidth)))
					if (isTargetInt) {
						res.instructions.add(
							new ArgumentedInstruction(cast_int, Integer.toString(targetWidth),
								Integer.toString(targetWidth)))
					}
				} else if (isCurrentBit) {
					if (isTargetInt) {
						res.instructions.add(
							new ArgumentedInstruction(cast_int, Integer.toString(targetWidth),
								Integer.toString(currentWidth)))
					} else if (isTargetUint || isTargetBit) {
						res.instructions.add(
							new ArgumentedInstruction(cast_uint, Integer.toString(targetWidth),
								Integer.toString(currentWidth)))
					}
				} else
					throw new IllegalArgumentException("Cast to type:" + target.type + " not supported")
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
				return valueOfForced(current.width, context, "PSEX").intValue
			}
		}
		throw new HDLCodeGenerationException(current, current + " width is unknown", "PSEX");
	}

	def dispatch FluidFrame toSimulationModel(HDLEnumRef obj, HDLEvaluationContext context, String process) {
		val res = new FluidFrame(obj, process)
		val hEnum = obj.resolveHEnumForced(PSEX_STAGE)
		val hVar = obj.resolveVarForced(PSEX_STAGE)
		val idx = hEnum.enums.indexOf(hVar)
		res.addConstant(fullNameOf(hVar).toString, BigInteger.valueOf(idx), Type.ENUM)
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLVariableRef obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		val String refName = obj.getVarName(false, context)
		createPushIndex(obj.array, context, res, process, refName)
		createPushIndexBits(obj.bits, context, res, process, refName)
		var fixedArray = true
		for (HDLExpression idx : obj.array)
			if (!valueOf(idx, context).present)
				fixedArray = false
		val bits = new ArrayList<String>(obj.bits.size + 1)
		bits.add(refName)
		if (!obj.bits.isEmpty) {
			for (HDLRange  r : obj.bits) {
				bits.add(r.toString)
			}
		}
		var resVar = obj.resolveVarForced(PSEX_STAGE)
		val HDLDirection dir = resVar.direction
		switch (dir) {
			case INTERNAL:
				res.add(new ArgumentedInstruction(loadInternal, bits))
			case dir === PARAMETER || dir === CONSTANT: {
				if (!fixedArray)
					res.add(new ArgumentedInstruction(loadInternal, bits))
				else {
					val bVal = valueOfForced(obj, context, PSEX_STAGE)
					res.addConstant(refName, bVal, asType(TypeExtension.typeOfForced(resVar, PSEX_STAGE)))
				}
			}
			case IN: {
				res.add(new ArgumentedInstruction(loadInternal, bits))
			}
			case dir === OUT || dir === INOUT: {
				res.add(new ArgumentedInstruction(loadInternal, bits))
			}
			default:
				throw new HDLCodeGenerationException(obj, "Failed to resolve direction:" + dir, "PSEX")
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLLiteral obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		if (obj.str !== null) {
			if (obj.str) {
				res.add(new ArgumentedInstruction(loadConstantString, obj.^val))
				return res
			} else {
				if (HDLLiteral.FALSE == obj.^val){
					res.addConstant(obj.^val, 0bi, Type.BOOL)
					return res
				}
				if (HDLLiteral.TRUE == obj.^val) {
					res.addConstant(obj.^val, 1bi, Type.BOOL)
					return res						
				}
			}
		} 
		val BigInteger value = obj.valueAsBigInt
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
		res.addConstant(value.toString, value, Type.INT)
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLEqualityOp obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		res.append(obj.left.toSimulationModel(context, process))
		res.append(obj.right.toSimulationModel(context, process))
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

	def dispatch FluidFrame toSimulationModel(HDLBitOp obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		res.append(obj.left.toSimulationModel(context, process))
		res.append(obj.right.toSimulationModel(context, process))
		switch (obj.type) {
			case AND: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(and, Integer.toString(width)))
			}
			case LOGI_AND:
				res.add(logiAnd)
			case OR: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(or, Integer.toString(width)))
			}
			case LOGI_OR:
				res.add(logiOr)
			case XOR: {
				val width = obj.targetSizeWithType(context)
				res.add(new ArgumentedInstruction(xor, Integer.toString(width)))
			}
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLArithOp obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		res.append(obj.left.toSimulationModel(context, process))
		res.append(obj.right.toSimulationModel(context, process))
		val width = obj.targetSizeWithType(context)
		switch (obj.type) {
			case DIV:
				res.add(new ArgumentedInstruction(div, Integer.toString(width)))
			case MINUS:
				res.add(new ArgumentedInstruction(minus, Integer.toString(width)))
			case MOD:
				res.add(new ArgumentedInstruction(mod, Integer.toString(width)))
			case MUL:
				res.add(new ArgumentedInstruction(mul, Integer.toString(width)))
			case PLUS:
				res.add(new ArgumentedInstruction(plus, Integer.toString(width)))
			case POW:
				res.add(new ArgumentedInstruction(pow, Integer.toString(width)))
		}
		return res
	}

	def dispatch FluidFrame toSimulationModel(HDLShiftOp obj, HDLEvaluationContext context, String process) {
		val FluidFrame res = new FluidFrame(obj, process)
		res.append(obj.left.toSimulationModel(context, process))
		res.append(obj.right.toSimulationModel(context, process))
		val width = obj.targetSizeWithType(context)
		switch (obj.type) {
			case SLL:
				res.add(new ArgumentedInstruction(sll, width.toString))
			case SRA: {
				val type = typeOfForced(obj.left, PSEX_STAGE)
				val prim = type as HDLPrimitive
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
		val width = HDLPrimitives.getWidth(type, context)
		if (type.type === HDLPrimitiveType.INT || type.type === HDLPrimitiveType.INTEGER)
			return (width << 1).bitwiseOr(1)
		return ( width << 1)
	}

}
