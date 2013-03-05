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
import com.google.common.base.Optional

class ConstantEvaluate {
	public static ConstantEvaluate INST=new ConstantEvaluate
	
	/**
	 * Attempts to determine a constant that the given Expression can be replaced with. This method does not use parameters
	 * as their value depends on the context.
	 * 
	 * @return an absent Optional if not successful
	 */
	def static Optional<BigInteger> valueOf(HDLExpression exp){
		return INST.constantEvaluate(exp, null)
	}
	
	/**
	 * Attempts to determine a constant that the given Expression can be replaced with. If parameter are encountered, 
	 * the provided context is used to retrieve a value for them.
	 * 
	 * @return an absent Optional if not successful
	 */
	def static Optional<BigInteger> valueOf(HDLExpression exp, HDLEvaluationContext context){
		return INST.constantEvaluate(exp, context)
	}
	
	/**
	 * This annotation can be used to find out what caused the evaluation to fail
	 */
	public static GenericMeta<IHDLObject> SOURCE=new GenericMeta("SOURCE", true)
	
	def dispatch Optional<BigInteger> constantEvaluate(HDLUnresolvedFragment obj, HDLEvaluationContext context) {
		return Insulin::resolveFragment(obj)?.copyDeepFrozen(obj.container)?.constantEvaluate(context)
	}
	def dispatch Optional<BigInteger> constantEvaluate(IHDLObject obj, HDLEvaluationContext context) {
		throw new IllegalArgumentException("Did not implement constantEvaulate for type:"+obj.classType)
	}
	def dispatch Optional<BigInteger> constantEvaluate(HDLTernary obj, HDLEvaluationContext context) {
		val Optional<BigInteger> res=obj.ifExpr.constantEvaluate(context)
		if (res.present){
			if (0bi.equals(res.get)){
				return obj.elseExpr.constantEvaluate(context)
			}
			return obj.thenExpr.constantEvaluate(context)
		}
		obj.addMeta(SOURCE, obj.ifExpr)
		obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
		return Optional::absent
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLLiteral obj, HDLEvaluationContext context) {
		switch (obj.presentation){
		case HDLLiteral$HDLLiteralPresentation::STR:
			return Optional::absent
		case HDLLiteral$HDLLiteralPresentation::BOOL:
			return Optional::absent
		}
		return Optional::of(obj.valueAsBigInt)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLManip obj, HDLEvaluationContext context) {
		val Optional<BigInteger> eval = subEvaluate(obj, obj.target, context)
		if (!eval.present) {
			return Optional::absent
		}
		switch (obj.type) {
		case ARITH_NEG:
			return Optional::of(eval.get.negate)
		case BIT_NEG:
			return Optional::of(eval.get.not)
		case LOGIC_NEG:
			return boolInt(obj.target.constantEvaluate(context).equals(0bi))
		case CAST:{
			val HDLType type = obj.castTo
			if (type instanceof HDLPrimitive) {
				val HDLPrimitive prim = type  as HDLPrimitive
				if (prim.width != null) {
					val Optional<BigInteger> width = prim.width.constantEvaluate(context)
					if (width!=null)
						return Optional::of(eval.get.mod(1bi.shiftLeft(width.get.intValue)))
					return Optional::absent
				}
				return eval
			}
			obj.addMeta(SOURCE, obj.target)
			obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
			return Optional::absent
		}

		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLConcat obj, HDLEvaluationContext context) {
		var BigInteger sum = 0bi
		for (HDLExpression cat : obj.cats) {
			val Optional<BigInteger> im = subEvaluate(obj, cat, context)
			if (!im.present) {
				return Optional::absent
			}
			val type=TypeExtension::typeOf(cat)
			if (!type.present){
				obj.addMeta(SOURCE, cat)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return Optional::absent
			}
			val Optional<BigInteger> width = type.get.width.constantEvaluate(context)
			if (!width.present) {
				obj.addMeta(SOURCE, type.get.width)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return Optional::absent

			}
			sum = sum.shiftLeft(width.get.intValue).or(im.get)
		}
		return Optional::of(sum)
	}

	def Optional<BigInteger> subEvaluate(HDLExpression container, HDLExpression left, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = left.constantEvaluate(context)
		if (!leftVal.present) {
			container.addMeta(SOURCE, left)
			container.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE)
			return Optional::absent
		}
		return leftVal
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLArithOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional::absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional::absent
		switch (obj.type) {
		case DIV:
			return Optional::of(leftVal.get.divide(rightVal.get))
		case MUL:
			return Optional::of(leftVal.get.multiply(rightVal.get))
		case MINUS:
			return Optional::of(leftVal.get.subtract(rightVal.get))
		case PLUS:
			return Optional::of(leftVal.get.add(rightVal.get))
		case MOD:
			return Optional::of(leftVal.get.remainder(rightVal.get))
		case POW:
			return Optional::of(leftVal.get.pow(rightVal.get.intValue))
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLBitOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional::absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional::absent
		switch (obj.type) {
		case AND:
			return Optional::of(leftVal.get.and(rightVal.get))
		case OR:
			return Optional::of(leftVal.get.or(rightVal.get))
		case XOR:
			return Optional::of(leftVal.get.xor(rightVal.get))
		case LOGI_AND: {
			val boolean l = !0bi.equals(leftVal.get)
			val boolean r = !0bi.equals(rightVal.get)
			return boolInt(l && r)
		}
		case LOGI_OR: {
			val boolean l = !0bi.equals(leftVal.get)
			val boolean r = !0bi.equals(rightVal.get)
			return boolInt(l || r)
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLEqualityOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional::absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional::absent
		switch (obj.type) {
		case EQ:
			return boolInt(leftVal.get.equals(rightVal.get))
		case NOT_EQ:
			return boolInt(!leftVal.get.equals(rightVal.get))
		case GREATER:
			return boolInt(leftVal.get.compareTo(rightVal.get) > 0)
		case GREATER_EQ:
			return boolInt(leftVal.get.compareTo(rightVal.get) >= 0)
		case LESS:
			return boolInt(leftVal.get.compareTo(rightVal.get) < 0)
		case LESS_EQ:
			return boolInt(leftVal.get.compareTo(rightVal.get) <= 0)
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLShiftOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional::absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional::absent
		switch (obj.type) {
		case SLL:
			return Optional::of(leftVal.get.shiftLeft(rightVal.get.intValue))
		case SRA:
			return Optional::of(leftVal.get.shiftRight(rightVal.get.intValue))
		case SRL:{
			val BigInteger shiftRight = leftVal.get.shiftRight(rightVal.get.intValue)
			if (shiftRight.signum < 0)
				//XXX This is incorrect. We have to know the width of the
				return Optional::of(shiftRight.negate)
			return Optional::of(shiftRight)
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLFunctionCall obj, HDLEvaluationContext context) {
		val List<BigInteger> args = new LinkedList<BigInteger>
		for (HDLExpression arg : obj.params) {
			val Optional<BigInteger> bigVal = subEvaluate(obj, arg, context)
			if (!bigVal.present)
				return Optional::absent
			args.add(bigVal.get)
		}
		return HDLFunctions::constantEvaluate(obj, args, context)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLVariableRef obj, HDLEvaluationContext context) {
		if (obj.array.size != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional::absent
		}
		if (obj.bits.size != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional::absent
		}
		val Optional<? extends HDLType> type = TypeExtension::typeOf(obj)
		if (!type.present || !(type.get instanceof HDLPrimitive)) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional::absent
		}
		val HDLVariable hVar = obj.resolveVar
		if (hVar == null) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return Optional::absent
		}
		val HDLDirection dir = hVar.direction
		if (dir == CONSTANT)
			return subEvaluate(obj, hVar.defaultValue, context)

		if (dir == PARAMETER) {
			if (context == null) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, CAN_NOT_USE_PARAMETER)
				return Optional::absent
			}
			val HDLExpression cRef = context.get(hVar)
			val Optional<BigInteger> cRefEval = cRef.constantEvaluate(context)
			if (!cRefEval.present) {
				obj.addMeta(SOURCE, cRef)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT)
				return Optional::absent
			}
			return cRefEval
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional::absent
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional::absent
	}

	def static Optional<BigInteger> boolInt(boolean b) {
		return if (b) Optional::of(1bi) else Optional::of(0bi)
	}
}
