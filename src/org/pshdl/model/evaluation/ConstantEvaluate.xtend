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
package org.pshdl.model.evaluation

import com.google.common.base.Optional
import java.math.BigInteger
import java.util.ArrayList
import java.util.LinkedList
import java.util.List
import org.pshdl.interpreter.frames.BigIntegerFrame
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLArrayInit
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLLiteral.HDLLiteralPresentation
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLObject.GenericMeta
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLType
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration.HDLDirection
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.extensions.TypeExtension
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import org.pshdl.model.utils.Insulin

import static org.pshdl.model.HDLArithOp.HDLArithOpType.*
import static org.pshdl.model.HDLBitOp.HDLBitOpType.*
import static org.pshdl.model.HDLEqualityOp.HDLEqualityOpType.*
import static org.pshdl.model.HDLManip.HDLManipType.*
import static org.pshdl.model.HDLShiftOp.HDLShiftOpType.*
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.*
import static org.pshdl.model.extensions.ProblemDescription.*

/**
 * This class allows to attempt to resolve a {@link java.math.BigInteger} value for any {@link org.pshdl.model.IHDLObject}. Of course
 * this only works when the given IHDLObject is truly constant. Parameters are not considered constant, unless
 * they can be found in the given {@link org.pshdl.model.evaluation.HDLEvaluationContext}.
 * 
 * @author Karsten Becker
 */
class ConstantEvaluate {
	private static ConstantEvaluate INST = new ConstantEvaluate

	/**
	 * Attempts to determine a constant that the given Expression can be replaced with. This method does not use parameters
	 * as their value depends on the context.
	 * 
	 * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription#DESCRIPTION} Meta annotations
	 */
	def static Optional<BigInteger> valueOf(HDLExpression exp) {
		return INST.constantEvaluate(exp, null)
	}

	/**
	 * Attempts to determine a constant that the given Expression can be replaced with. If parameter are encountered, 
	 * the provided context is used to retrieve a value for them.
	 * 
	 * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription.DESCRIPTION} Meta annotations
	 */
	def static Optional<BigInteger> valueOf(HDLExpression exp, HDLEvaluationContext context) {
		if (exp === null)
			return Optional.absent
		return INST.constantEvaluate(exp, context)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLUnresolvedFragment obj, HDLEvaluationContext context) {
		val type = Insulin.resolveFragment(obj)
		if (!type.present)
			return Optional.absent
		return type.get.obj.copyDeepFrozen(obj.container).constantEvaluate(context)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLArrayInit obj, HDLEvaluationContext context) {
		return Optional.absent
	}

	def dispatch Optional<BigInteger> constantEvaluate(IHDLObject obj, HDLEvaluationContext context) {
		throw new IllegalArgumentException("Did not implement constantEvaulate for type:" + obj.classType)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLTernary obj, HDLEvaluationContext context) {
		val Optional<BigInteger> res = obj.ifExpr.constantEvaluate(context)
		if (res.present) {
			if (0bi.equals(res.get)) {
				return obj.elseExpr.constantEvaluate(context)
			}
			return obj.thenExpr.constantEvaluate(context)
		}
		obj.addMeta(SOURCE, obj.ifExpr)
		obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
		return Optional.absent
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLLiteral obj, HDLEvaluationContext context) {
		switch (obj.presentation) {
			case HDLLiteral$HDLLiteralPresentation.STR:
				return Optional.absent
			case HDLLiteral$HDLLiteralPresentation.BOOL: {
				if (context!=null && context.boolAsInt){
					return boolInt(!obj.equals(HDLLiteral.^false))
				}
				return Optional.absent
			}
		}
		return Optional.of(obj.valueAsBigInt)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLManip obj, HDLEvaluationContext context) {
		val Optional<BigInteger> eval = subEvaluate(obj, obj.target, context)
		if (!eval.present) {
			return Optional.absent
		}
		switch (obj.type) {
			case ARITH_NEG:
				return Optional.of(eval.get.negate)
			case BIT_NEG:
				return Optional.of(eval.get.not)
			case LOGIC_NEG: {
				val const = obj.target.constantEvaluate(context)
				if (const.present)
					return boolInt(const.get.equals(0bi))
				return Optional.absent
			}
			case CAST: {
				val HDLType type = obj.castTo
				if (type instanceof HDLPrimitive) {
					val HDLPrimitive prim = type  as HDLPrimitive
					if (prim.width !== null) {
						val Optional<BigInteger> width = prim.width.constantEvaluate(context)
						if (width.present)
							return Optional.of(eval.get.mod(1bi.shiftLeft(width.get.intValue)))
						return Optional.absent
					}
					return eval
				}
				obj.addMeta(SOURCE, obj.target)
				obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
				return Optional.absent
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLConcat obj, HDLEvaluationContext context) {
		var BigInteger sum = 0bi
		for (HDLExpression cat : obj.cats) {
			val Optional<BigInteger> im = subEvaluate(obj, cat, context)
			if (!im.present) {
				return Optional.absent
			}
			val type = TypeExtension.typeOf(cat)
			if (!type.present) {
				obj.addMeta(SOURCE, cat)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return Optional.absent
			}
			val Optional<BigInteger> width = type.get.width.constantEvaluate(context)
			if (!width.present) {
				obj.addMeta(SOURCE, type.get.width)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE)
				return Optional.absent

			}
			sum = sum.shiftLeft(width.get.intValue).or(im.get)
		}
		return Optional.of(sum)
	}

	def Optional<BigInteger> subEvaluate(HDLExpression container, HDLExpression left, HDLEvaluationContext context) {
		if (left === null)
			throw new IllegalArgumentException("Container:" + container + " has null left expression");
		val Optional<BigInteger> leftVal = left.constantEvaluate(context)
		if (!leftVal.present) {
			container.addMeta(SOURCE, left)
			container.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE)
			return Optional.absent
		}
		return leftVal
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLArithOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional.absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional.absent
		switch (obj.type) {
			case DIV:
				return Optional.of(leftVal.get.divide(rightVal.get))
			case MUL:
				return Optional.of(leftVal.get.multiply(rightVal.get))
			case MINUS:
				return Optional.of(leftVal.get.subtract(rightVal.get))
			case PLUS:
				return Optional.of(leftVal.get.add(rightVal.get))
			case MOD:
				return Optional.of(leftVal.get.remainder(rightVal.get))
			case POW:
				return Optional.of(leftVal.get.pow(rightVal.get.intValue))
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLBitOp obj, HDLEvaluationContext context) {
		val Optional<BigInteger> leftVal = subEvaluate(obj, obj.left, context)
		if (!leftVal.present)
			return Optional.absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional.absent
		switch (obj.type) {
			case AND:
				return Optional.of(leftVal.get.and(rightVal.get))
			case OR:
				return Optional.of(leftVal.get.or(rightVal.get))
			case XOR:
				return Optional.of(leftVal.get.xor(rightVal.get))
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
			return Optional.absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional.absent
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
			return Optional.absent
		val Optional<BigInteger> rightVal = subEvaluate(obj, obj.right, context)
		if (!rightVal.present)
			return Optional.absent
		switch (obj.type) {
			case SLL:
				return Optional.of(leftVal.get.shiftLeft(rightVal.get.intValue))
			case SRA:
				return Optional.of(leftVal.get.shiftRight(rightVal.get.intValue))
			case SRL: {
				val l = leftVal.get
				if (l.signum < 0) {
					val t = TypeExtension.typeOf(obj.left)
					if (t.present) {
						val width = HDLPrimitives.getWidth(t.get, context)
						if (width !== null) {
							val shiftWidth = rightVal.get.intValue
							val res = BigIntegerFrame.srl(l, width, shiftWidth)
							return Optional.of(res)
						}
					}
					return Optional.absent
				}
				return Optional.of(l.shiftRight(rightVal.get.intValue))
			}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!")
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLFunctionCall obj, HDLEvaluationContext context) {
		val List<BigInteger> args = new LinkedList<BigInteger>
		for (HDLExpression arg : obj.params) {
			val Optional<BigInteger> bigVal = subEvaluate(obj, arg, context)
			if (!bigVal.present)
				return Optional.absent
			args.add(bigVal.get)
		}
		return HDLFunctions.constantEvaluate(obj, args, context)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLVariableRef obj, HDLEvaluationContext context) {
		if (obj.array.size != 0) {
			val hVarOpt = obj.resolveVar
			if (hVarOpt.present) {
				val hVar = hVarOpt.get
				if (hVar.direction == CONSTANT) {
					val defVal = hVar.defaultValue
					return arrayDefValue(defVal, obj.array, context)
				}
			}
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional.absent
		}
		if (obj.bits.size != 0) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional.absent
		}
		val Optional<? extends HDLType> type = TypeExtension.typeOf(obj)
		if (!type.present || !(type.get instanceof HDLPrimitive)) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
			return Optional.absent
		}
		val hVar = obj.resolveVar
		if (!hVar.present) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return Optional.absent
		}
		val HDLDirection dir = hVar.get.direction
		if (dir == CONSTANT)
			return subEvaluate(obj, hVar.get.defaultValue, context)

		if (dir == PARAMETER) {
			if (context === null) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, CAN_NOT_USE_PARAMETER)
				return Optional.absent
			}
			val HDLExpression cRef = context.get(hVar.get)
			if (cRef === null) {
				obj.addMeta(SOURCE, hVar.get)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT)
				return Optional.absent
			}
			val Optional<BigInteger> cRefEval = cRef.constantEvaluate(context)
			if (!cRefEval.present) {
				obj.addMeta(SOURCE, cRef)
				obj.addMeta(DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT)
				return Optional.absent
			}
			return cRefEval
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional.absent
	}

	def Optional<BigInteger> arrayDefValue(HDLExpression expression, List<HDLExpression> expressions,
		HDLEvaluationContext context) {
		if (expression instanceof HDLArrayInit) {
			if (expressions.empty) {
				return Optional.absent
			}
			val idx = valueOf(expressions.get(0), context)
			if (!idx.present) {
				return Optional.absent
			}
			val arr = expression as HDLArrayInit
			val idxValue = idx.get.intValue
			if (arr.exp.size < idxValue) {
				return Optional.of(0bi)
			}
			return arrayDefValue(arr.exp.get(idxValue), expressions.subList(1, expressions.size), context)
		}
		return constantEvaluate(expression, context)
	}

	def dispatch Optional<BigInteger> constantEvaluate(HDLEnumRef obj, HDLEvaluationContext context) {
		if (context != null && context.enumAsInt) {
			val hEnum = obj.resolveHEnum.get
			val hVar = obj.resolveVar.get
			return Optional.of(BigInteger.valueOf(hEnum.enums.indexOf(hVar)))
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional.absent
	}

	def static Optional<BigInteger> boolInt(boolean b) {
		return if(b) Optional.of(1bi) else Optional.of(0bi)
	}
}
