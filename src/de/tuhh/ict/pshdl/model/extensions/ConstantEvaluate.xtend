package de.tuhh.ict.pshdl.model.extensions

import java.math.BigInteger
import de.tuhh.ict.pshdl.model.*
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import java.util.List
import java.util.LinkedList
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection

class ConstantEvaluate {
	public static ConstantEvaluate INST=new ConstantEvaluate
	
	def static BigInteger valueOf(HDLExpression exp){
		return INST.constantEvaluate(exp, null)
	}
	def static BigInteger valueOf(HDLExpression exp, HDLEvaluationContext context){
		return INST.constantEvaluate(exp, context)
	}
	
	public static GenericMeta<IHDLObject> SOURCE=new GenericMeta("SOURCE", true)
	
	def dispatch BigInteger constantEvaluate(HDLTernary obj, HDLEvaluationContext context) {
		val BigInteger res=obj.ifExpr.constantEvaluate(context)
		if (res!=null){
			if (BigInteger::ZERO.equals(res)){
				return obj.elseExpr.constantEvaluate(context)
			}
			return obj.thenExpr.constantEvaluate(context)
		}
		obj.addMeta(SOURCE, obj.ifExpr)
		obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
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
		case HDLManip$HDLManipType::ARITH_NEG:
			return eval.negate()
		case HDLManip$HDLManipType::BIT_NEG:
			return eval.not()
		case HDLManip$HDLManipType::LOGIC_NEG:
			return boolInt(obj.target.constantEvaluate(context).equals(BigInteger::ZERO))
		case HDLManip$HDLManipType::CAST:{
			val HDLType type = obj.castTo
			if (type instanceof HDLPrimitive) {
				val HDLPrimitive prim = type  as HDLPrimitive
				if (prim.width != null) {
					val BigInteger width = prim.width.constantEvaluate(context)
					if (width!=null)
						return eval.mod(BigInteger::ONE.shiftLeft(width.intValue()))
					return null
				}
				return eval
			}
			obj.addMeta(SOURCE, obj.target)
			obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::NON_PRIMITVE_TYPE_NOT_EVALUATED)
			return null
		}

		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLConcat obj, HDLEvaluationContext context) {
		var BigInteger sum = BigInteger::ZERO
		for (HDLExpression cat : obj.cats) {
			val BigInteger im = subEvaluate(obj, cat, context)
			if (im == null) {
				return null
			}
			val BigInteger width = TypeExtension::typeOf(cat).width.constantEvaluate(context)
			if (width == null) {
				obj.addMeta(SOURCE, TypeExtension::typeOf(cat).width)
				obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return null

			}
			sum = sum.shiftLeft(width.intValue()).or(im)
		}
		return sum
	}

	def BigInteger subEvaluate(HDLExpression container, HDLExpression left, HDLEvaluationContext context) {
		val BigInteger leftVal = left.constantEvaluate(context)
		if (leftVal == null) {
			container.addMeta(SOURCE, left)
			container.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::SUBEXPRESSION_DID_NOT_EVALUATE)
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
		case HDLArithOp$HDLArithOpType::DIV:
			return leftVal.divide(rightVal)
		case HDLArithOp$HDLArithOpType::MUL:
			return leftVal.multiply(rightVal)
		case HDLArithOp$HDLArithOpType::MINUS:
			return leftVal.subtract(rightVal)
		case HDLArithOp$HDLArithOpType::PLUS:
			return leftVal.add(rightVal)
		case HDLArithOp$HDLArithOpType::MOD:
			return leftVal.remainder(rightVal)
		case HDLArithOp$HDLArithOpType::POW:
			return leftVal.pow(rightVal.intValue())
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
		case HDLBitOp$HDLBitOpType::AND:
			return leftVal.and(rightVal)
		case HDLBitOp$HDLBitOpType::OR:
			return leftVal.or(rightVal)
		case HDLBitOp$HDLBitOpType::XOR:
			return leftVal.xor(rightVal)
		case HDLBitOp$HDLBitOpType::LOGI_AND: {
			val boolean l = !BigInteger::ZERO.equals(leftVal)
			val boolean r = !BigInteger::ZERO.equals(rightVal)
			return boolInt(l && r)
		}
		case HDLBitOp$HDLBitOpType::LOGI_OR: {
			val boolean l = !BigInteger::ZERO.equals(leftVal)
			val boolean r = !BigInteger::ZERO.equals(rightVal)
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
		case HDLEqualityOp$HDLEqualityOpType::EQ:
			return boolInt(leftVal.equals(rightVal))
		case HDLEqualityOp$HDLEqualityOpType::NOT_EQ:
			return boolInt(!leftVal.equals(rightVal))
		case HDLEqualityOp$HDLEqualityOpType::GREATER:
			return boolInt(leftVal.compareTo(rightVal) > 0)
		case HDLEqualityOp$HDLEqualityOpType::GREATER_EQ:
			return boolInt(leftVal.compareTo(rightVal) >= 0)
		case HDLEqualityOp$HDLEqualityOpType::LESS:
			return boolInt(leftVal.compareTo(rightVal) < 0)
		case HDLEqualityOp$HDLEqualityOpType::LESS_EQ:
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
		case HDLShiftOp$HDLShiftOpType::SLL:
			return leftVal.shiftLeft(rightVal.intValue())
		case HDLShiftOp$HDLShiftOpType::SRA:
			return leftVal.shiftRight(rightVal.intValue())
		case HDLShiftOp$HDLShiftOpType::SRL:{
			val BigInteger shiftRight = leftVal.shiftRight(rightVal.intValue())
			if (shiftRight.signum() < 0)
				//XXX This is incorrect. We have to know the width of the
				return shiftRight.negate()
			return shiftRight
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch BigInteger constantEvaluate(HDLFunctionCall obj, HDLEvaluationContext context) {
		val List<BigInteger> args = new LinkedList<BigInteger>()
		for (HDLExpression arg : obj.params) {
			val BigInteger bigVal = subEvaluate(obj, arg, context)
			if (bigVal == null)
				return null
			args.add(bigVal)
		}
		return HDLFunctions::constantEvaluate(obj, args, context)
	}

	def dispatch BigInteger constantEvaluate(HDLVariableRef obj, HDLEvaluationContext context) {
		if (obj.array.size() != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		if (obj.bits.size() != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		val HDLType type = TypeExtension::typeOf(obj)
		if (!(type instanceof HDLPrimitive)) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		val HDLVariable hVar = obj.resolveVar()
		val HDLDirection dir = hVar.direction
		if (dir == HDLDirection::CONSTANT)
			return subEvaluate(obj, hVar.defaultValue, context)

		if (dir == HDLDirection::PARAMETER) {
			if (context == null) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::CAN_NOT_USE_PARAMETER)
				return null
			}
			val HDLExpression cRef = context.get(hVar)
			val BigInteger cRefEval = cRef.constantEvaluate(context)
			if (cRefEval == null) {
				obj.addMeta(SOURCE, cRef)
				obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT)
				return null
			}
			return cRefEval
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
		return null
	}

	def dispatch BigInteger constantEvaluate(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(ProblemDescription::DESCRIPTION, ProblemDescription::ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return null
	}

	def static BigInteger boolInt(boolean b) {
		return if (b) BigInteger::ONE else BigInteger::ZERO
	}
}
