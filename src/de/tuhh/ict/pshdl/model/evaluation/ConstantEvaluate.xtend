package de.tuhh.ict.pshdl.model.evaluation

import de.tuhh.ict.pshdl.model.HDLArithOp
import de.tuhh.ict.pshdl.model.HDLBitOp
import de.tuhh.ict.pshdl.model.HDLConcat
import de.tuhh.ict.pshdl.model.HDLEnumRef
import de.tuhh.ict.pshdl.model.HDLEqualityOp
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLFunctionCall
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLLiteral$HDLLiteralPresentation
import de.tuhh.ict.pshdl.model.HDLManip
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLShiftOp
import de.tuhh.ict.pshdl.model.HDLTernary
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.extensions.TypeExtension
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions
import de.tuhh.ict.pshdl.model.utils.Insulin
import java.math.BigInteger
import java.util.LinkedList
import java.util.List

import static de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType.*
import static de.tuhh.ict.pshdl.model.HDLBitOp$HDLBitOpType.*
import static de.tuhh.ict.pshdl.model.HDLEqualityOp$HDLEqualityOpType.*
import static de.tuhh.ict.pshdl.model.HDLManip$HDLManipType.*
import static de.tuhh.ict.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection.*
import static de.tuhh.ict.pshdl.model.extensions.ProblemDescription.*

class ConstantEvaluate {
	public static ConstantEvaluate INST=new ConstantEvaluate
	
	def static BigInteger valueOf(HDLExpression exp){
		return INST.constantEvaluate(exp, null)
	}
	def static BigInteger valueOf(HDLExpression exp, HDLEvaluationContext context){
		return INST.constantEvaluate(exp, context)
	}
	public static GenericMeta<IHDLObject> SOURCE=new GenericMeta("SOURCE", true)
	
	def dispatch BigInteger constantEvaluate(HDLUnresolvedFragment obj, HDLEvaluationContext context) {
		return Insulin::resolveFragment(obj)?.copyDeepFrozen(obj.container)?.constantEvaluate(context)
	}
	def dispatch BigInteger constantEvaluate(IHDLObject obj, HDLEvaluationContext context) {
		throw new IllegalArgumentException("Did not implement constantEvaulate for type:"+obj.classType)
	}
	def dispatch BigInteger constantEvaluate(HDLTernary obj, HDLEvaluationContext context) {
		val BigInteger res=obj.ifExpr.constantEvaluate(context)
		if (res!=null){
			if (0bi.equals(res)){
				return obj.elseExpr.constantEvaluate(context)
			}
			return obj.thenExpr.constantEvaluate(context)
		}
		obj.addMeta(SOURCE, obj.ifExpr)
		obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
		return null
	}

	def dispatch BigInteger constantEvaluate(HDLLiteral obj, HDLEvaluationContext context) {
		switch (obj.presentation){
		case HDLLiteral$HDLLiteralPresentation::STR:
			return null
		case HDLLiteral$HDLLiteralPresentation::BOOL:
			return null
		}
		return obj.valueAsBigInt
	}

	def dispatch BigInteger constantEvaluate(HDLManip obj, HDLEvaluationContext context) {
		val BigInteger eval = subEvaluate(obj, obj.target, context)
		if (eval == null) {
			return null
		}
		switch (obj.type) {
		case ARITH_NEG:
			return eval.negate
		case BIT_NEG:
			return eval.not
		case LOGIC_NEG:
			return boolInt(obj.target.constantEvaluate(context).equals(0bi))
		case CAST:{
			val HDLType type = obj.castTo
			if (type instanceof HDLPrimitive) {
				val HDLPrimitive prim = type  as HDLPrimitive
				if (prim.width != null) {
					val BigInteger width = prim.width.constantEvaluate(context)
					if (width!=null)
						return eval.mod(1bi.shiftLeft(width.intValue))
					return null
				}
				return eval
			}
			obj.addMeta(SOURCE, obj.target)
			obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
			return null
		}

		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLConcat obj, HDLEvaluationContext context) {
		var BigInteger sum = 0bi
		for (HDLExpression cat : obj.cats) {
			val BigInteger im = subEvaluate(obj, cat, context)
			if (im == null) {
				return null
			}
			val BigInteger width = TypeExtension::typeOf(cat).width.constantEvaluate(context)
			if (width == null) {
				obj.addMeta(SOURCE, TypeExtension::typeOf(cat).width)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return null

			}
			sum = sum.shiftLeft(width.intValue).or(im)
		}
		return sum
	}

	def BigInteger subEvaluate(HDLExpression container, HDLExpression left, HDLEvaluationContext context) {
		val BigInteger leftVal = left.constantEvaluate(context)
		if (leftVal == null) {
			container.addMeta(SOURCE, left)
			container.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE)
			return null
		}
		return leftVal
	}

	def dispatch BigInteger constantEvaluate(HDLArithOp obj, HDLEvaluationContext context) {
		val BigInteger leftVal = subEvaluate(obj, obj.left, context)
		if (leftVal == null)
			return null
		val BigInteger rightVal = subEvaluate(obj, obj.right, context)
		if (rightVal == null)
			return null
		switch (obj.type) {
		case DIV:
			return leftVal.divide(rightVal)
		case MUL:
			return leftVal.multiply(rightVal)
		case MINUS:
			return leftVal.subtract(rightVal)
		case PLUS:
			return leftVal.add(rightVal)
		case MOD:
			return leftVal.remainder(rightVal)
		case POW:
			return leftVal.pow(rightVal.intValue)
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLBitOp obj, HDLEvaluationContext context) {
		val BigInteger leftVal = subEvaluate(obj, obj.left, context)
		if (leftVal == null)
			return null
		val BigInteger rightVal = subEvaluate(obj, obj.right, context)
		if (rightVal == null)
			return null
		switch (obj.type) {
		case AND:
			return leftVal.and(rightVal)
		case OR:
			return leftVal.or(rightVal)
		case XOR:
			return leftVal.xor(rightVal)
		case LOGI_AND: {
			val boolean l = !0bi.equals(leftVal)
			val boolean r = !0bi.equals(rightVal)
			return boolInt(l && r)
		}
		case LOGI_OR: {
			val boolean l = !0bi.equals(leftVal)
			val boolean r = !0bi.equals(rightVal)
			return boolInt(l || r)
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLEqualityOp obj, HDLEvaluationContext context) {
		val BigInteger leftVal = subEvaluate(obj, obj.left, context)
		if (leftVal == null)
			return null
		val BigInteger rightVal = subEvaluate(obj, obj.right, context)
		if (rightVal == null)
			return null
		switch (obj.type) {
		case EQ:
			return boolInt(leftVal.equals(rightVal))
		case NOT_EQ:
			return boolInt(!leftVal.equals(rightVal))
		case GREATER:
			return boolInt(leftVal.compareTo(rightVal) > 0)
		case GREATER_EQ:
			return boolInt(leftVal.compareTo(rightVal) >= 0)
		case LESS:
			return boolInt(leftVal.compareTo(rightVal) < 0)
		case LESS_EQ:
			return boolInt(leftVal.compareTo(rightVal) <= 0)
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLShiftOp obj, HDLEvaluationContext context) {
		val BigInteger leftVal = subEvaluate(obj, obj.left, context)
		if (leftVal == null)
			return null
		val BigInteger rightVal = subEvaluate(obj, obj.right, context)
		if (rightVal == null)
			return null
		switch (obj.type) {
		case SLL:
			return leftVal.shiftLeft(rightVal.intValue)
		case SRA:
			return leftVal.shiftRight(rightVal.intValue)
		case SRL:{
			val BigInteger shiftRight = leftVal.shiftRight(rightVal.intValue)
			if (shiftRight.signum < 0)
				//XXX This is incorrect. We have to know the width of the
				return shiftRight.negate
			return shiftRight
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLFunctionCall obj, HDLEvaluationContext context) {
		val List<BigInteger> args = new LinkedList<BigInteger>
		for (HDLExpression arg : obj.params) {
			val BigInteger bigVal = subEvaluate(obj, arg, context)
			if (bigVal == null)
				return null
			args.add(bigVal)
		}
		return HDLFunctions::constantEvaluate(obj, args, context)
	}

	def dispatch BigInteger constantEvaluate(HDLVariableRef obj, HDLEvaluationContext context) {
		if (obj.array.size != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		if (obj.bits.size != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		val HDLType type = TypeExtension::typeOf(obj)
		if (!(type instanceof HDLPrimitive)) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		val HDLVariable hVar = obj.resolveVar
		if (hVar == null) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return null
		}
		val HDLDirection dir = hVar.direction
		if (dir == CONSTANT)
			return subEvaluate(obj, hVar.defaultValue, context)

		if (dir == PARAMETER) {
			if (context == null) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, CAN_NOT_USE_PARAMETER)
				return null
			}
			val HDLExpression cRef = context.get(hVar)
			val BigInteger cRefEval = cRef.constantEvaluate(context)
			if (cRefEval == null) {
				obj.addMeta(SOURCE, cRef)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT)
				return null
			}
			return cRefEval
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
		return null
	}

	def dispatch BigInteger constantEvaluate(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return null
	}

	def static BigInteger boolInt(boolean b) {
		return if (b) 1bi else 0bi
	}
}
