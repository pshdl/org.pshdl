package de.tuhh.ict.pshdl.model.extensions

import com.google.common.collect.Range
import com.google.common.collect.Ranges
import de.tuhh.ict.pshdl.model.HDLAnnotation
import de.tuhh.ict.pshdl.model.HDLArithOp
import de.tuhh.ict.pshdl.model.HDLBitOp
import de.tuhh.ict.pshdl.model.HDLConcat
import de.tuhh.ict.pshdl.model.HDLEnumRef
import de.tuhh.ict.pshdl.model.HDLEqualityOp
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLForLoop
import de.tuhh.ict.pshdl.model.HDLFunctionCall
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLManip
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLRange
import de.tuhh.ict.pshdl.model.HDLShiftOp
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives
import de.tuhh.ict.pshdl.model.evaluation.ConstantEvaluate
import java.math.BigInteger

import static de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType.*
import static de.tuhh.ict.pshdl.model.HDLBitOp$HDLBitOpType.*
import static de.tuhh.ict.pshdl.model.HDLManip$HDLManipType.*
import static de.tuhh.ict.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static de.tuhh.ict.pshdl.model.extensions.ProblemDescription.*
import static de.tuhh.ict.pshdl.model.extensions.RangeExtension.*
import static java.math.BigInteger.*
import java.math.BigDecimal
import com.google.common.base.Optional

class RangeExtension {

	public static RangeExtension INST = new RangeExtension

	def static rangeOf(IHDLObject obj) {
		return INST.determineRange(obj, null)
	}

	def static rangeOf(IHDLObject obj, HDLEvaluationContext context) {
		return INST.determineRange(obj, context)
	}

	public static GenericMeta<IHDLObject> SOURCE = new GenericMeta("SOURCE", true)

	def dispatch Range<BigInteger> determineRange(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Range<BigInteger> determineRange(HDLLiteral obj, HDLEvaluationContext context) {
		return Ranges::closed(obj.valueAsBigInt, obj.valueAsBigInt)
	}

	def dispatch Range<BigInteger> determineRange(HDLVariableRef obj, HDLEvaluationContext context) {
		val Optional<BigInteger> bigVal = ConstantEvaluate::valueOf(obj, context)
		if (bigVal.present)
			return Ranges::closed(bigVal.get, bigVal.get)
		val HDLVariable hVar = obj.resolveVar
		if (hVar == null) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return null
		}
		var HDLAnnotation range = hVar.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
		if (range != null) {
			val value = range.value.split(";")

			//TODO Allow simple references
			return Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1)))
		}
		if (hVar.container != null) {
			if (hVar.container instanceof HDLVariableDeclaration) {
				val HDLVariableDeclaration hvd = hVar.container as HDLVariableDeclaration
				range = hvd.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
				if (range != null) {
					val String[] value = range.value.split(";")

					//TODO Allow simple references
					return Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1)))
				}
			}
			if (hVar.container instanceof HDLForLoop) {
				val HDLForLoop loop = hVar.container as HDLForLoop
				var Range<BigInteger> res = loop.range.get(0).determineRange(context)
				if (res != null) {
					for (HDLRange r : loop.range) {
						res = res.span(r.determineRange(context))
					}
					return res
				}
			}
		}
		if (obj.bits.size > 0) {
			var BigInteger bitWidth = 0bi
			for (HDLRange r : obj.bits) {
				var HDLExpression width = r.width
				width = width.copyDeepFrozen(r)
				var Optional<BigInteger> cw = ConstantEvaluate::valueOf(width, context)
				if (!cw.present) {
					bitWidth = null
				} else {
					bitWidth = bitWidth.add(cw.get)
				}
			}
			if (bitWidth != null) {
				return Ranges::closed(0bi, 1bi.shiftLeft(bitWidth.intValue).subtract(1bi))
			}
		}
		val HDLType type = TypeExtension::typeOf(hVar)
		if (type instanceof HDLPrimitive) {
			return HDLPrimitives::instance.getValueRange(type as HDLPrimitive, context)
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
		return null
	}

	def dispatch Range<BigInteger> determineRange(HDLRange obj, HDLEvaluationContext context) {
		val Optional<BigInteger> to = ConstantEvaluate::valueOf(obj.to, context)
		if (!to.present)
			return null;
		if (obj.from != null) {
			val Optional<BigInteger> from = ConstantEvaluate::valueOf(obj.from, context)
			if (!from.present)
				return null;
			if (from.get.compareTo(to.get) > 0)
				return Ranges::closed(to.get, from.get)
			return Ranges::closed(from.get, to.get)
		}
		return Ranges::closed(to.get, to.get)
	}

	def dispatch Range<BigInteger> determineRange(HDLEqualityOp obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
		return Ranges::closed(0bi, 1bi)
	}

	def dispatch Range<BigInteger> determineRange(HDLShiftOp obj, HDLEvaluationContext context) {
		val Range<BigInteger> leftRange = obj.left.determineRange(context)
		val Range<BigInteger> rightRange = obj.right.determineRange(context)
		switch (obj.type) {
			case SLL: {
				val BigInteger ff = leftRange.lowerEndpoint.shiftLeft(rightRange.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.lowerEndpoint.shiftLeft(rightRange.upperEndpoint.intValue)
				val BigInteger tf = leftRange.upperEndpoint.shiftLeft(rightRange.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.upperEndpoint.shiftLeft(rightRange.upperEndpoint.intValue)
				return Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt))
			}
			case SRA: {
				val BigInteger ff = leftRange.lowerEndpoint.shiftRight(rightRange.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.lowerEndpoint.shiftRight(rightRange.upperEndpoint.intValue)
				val BigInteger tf = leftRange.upperEndpoint.shiftRight(rightRange.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.upperEndpoint.shiftRight(rightRange.upperEndpoint.intValue)
				return Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt))
			}
			case SRL: {
				val BigInteger ff = srl(leftRange.lowerEndpoint, rightRange.lowerEndpoint)
				val BigInteger ft = srl(leftRange.lowerEndpoint, rightRange.upperEndpoint)
				val BigInteger tf = srl(leftRange.upperEndpoint, rightRange.lowerEndpoint)
				val BigInteger tt = srl(leftRange.upperEndpoint, rightRange.upperEndpoint)
				return Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def private static BigInteger srl(BigInteger a, BigInteger b) {
		val BigInteger res = a.shiftRight(b.intValue)
		if (res.signum < 0)
			return res.negate
		return res
	}

	def dispatch Range<BigInteger> determineRange(HDLBitOp obj, HDLEvaluationContext context) {
		val Range<BigInteger> leftRange = obj.left.determineRange(context)
		val Range<BigInteger> rightRange = obj.right.determineRange(context)
		switch (type: obj.type) {
			case type == OR || type == XOR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Ranges::closed(0bi, 1bi.shiftLeft(leftRange.upperEndpoint.bitLength).subtract(1bi))
			}
			case AND: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Ranges::closed(0bi,
					leftRange.upperEndpoint.min(1bi.shiftLeft(rightRange.upperEndpoint.bitLength).subtract(1bi)))
			}
			case type == LOGI_AND || type == LOGI_OR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Ranges::closed(0bi, 1bi)
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	// See http://en.wikipedia.org/wiki/Interval_arithmetic#Simple_arithmetic
	def dispatch Range<BigInteger> determineRange(HDLArithOp obj, HDLEvaluationContext context) {
		val Range<BigInteger> leftRange = obj.left.determineRange(context)
		var Range<BigInteger> rightRange = obj.right.determineRange(context)
		switch (obj.type) {
			case PLUS:
				return Ranges::closed(leftRange.lowerEndpoint.add(rightRange.lowerEndpoint),
					leftRange.upperEndpoint.add(rightRange.upperEndpoint))
			case MINUS:
				return Ranges::closed(leftRange.lowerEndpoint.subtract(rightRange.lowerEndpoint),
					leftRange.upperEndpoint.subtract(rightRange.upperEndpoint))
			case DIV: {
				if (rightRange.lowerEndpoint.equals(0bi) || rightRange.upperEndpoint.equals(0bi)) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, ZERO_DIVIDE)
					return null
				}
				if (rightRange.lowerEndpoint.signum * rightRange.upperEndpoint.signum < 0 ||
					rightRange.upperEndpoint.signum == 0) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, POSSIBLY_ZERO_DIVIDE)
				}
				val mulRange = Ranges::closed(1bd.divide(new BigDecimal(rightRange.lowerEndpoint)),
					1bd.divide(new BigDecimal(rightRange.upperEndpoint)))
				val BigDecimal ff = new BigDecimal(leftRange.lowerEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal ft = new BigDecimal(leftRange.lowerEndpoint).multiply(mulRange.upperEndpoint)
				val BigDecimal tf = new BigDecimal(leftRange.upperEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal tt = new BigDecimal(leftRange.upperEndpoint).multiply(mulRange.upperEndpoint)
				return Ranges::closed(ff.min(ft).min(tf).min(tt).toBigInteger, ff.max(ft).max(tf).max(tt).toBigInteger)
			}
			case MUL: {
				val BigInteger ff = leftRange.lowerEndpoint.multiply(rightRange.lowerEndpoint)
				val BigInteger ft = leftRange.lowerEndpoint.multiply(rightRange.upperEndpoint)
				val BigInteger tf = leftRange.upperEndpoint.multiply(rightRange.lowerEndpoint)
				val BigInteger tt = leftRange.upperEndpoint.multiply(rightRange.upperEndpoint)
				return Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt))
			}
			case MOD:
				return Ranges::closed(0bi, rightRange.upperEndpoint.subtract(1bi).min(leftRange.upperEndpoint))
			case POW: {
				val BigInteger ff = leftRange.lowerEndpoint.pow(rightRange.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.lowerEndpoint.pow(rightRange.upperEndpoint.intValue)
				val BigInteger tf = leftRange.upperEndpoint.pow(rightRange.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.upperEndpoint.pow(rightRange.upperEndpoint.intValue)
				return Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Range<BigInteger> determineRange(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return null
	}

	def dispatch Range<BigInteger> determineRange(HDLManip obj, HDLEvaluationContext context) {
		val Range<BigInteger> right = obj.target.determineRange(context)
		switch (obj.type) {
			case CAST: {
				val HDLType type = obj.castTo
				if (type instanceof HDLPrimitive) {
					val Range<BigInteger> castRange = HDLPrimitives::getInstance.getValueRange(type as HDLPrimitive,
						context)
					return castRange.intersection(right)
				}
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
				return null
			}
			case ARITH_NEG:
				return Ranges::closed(right.upperEndpoint.negate, right.lowerEndpoint.negate)
			case BIT_NEG:
				return Ranges::closed(0bi, 1bi.shiftLeft(right.upperEndpoint.bitLength).subtract(1bi))
			case LOGIC_NEG: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Ranges::closed(0bi, 1bi)
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Range<BigInteger> determineRange(HDLFunctionCall obj, HDLEvaluationContext context) {
		return HDLFunctions::determineRange(obj, context)
	}

	def dispatch Range<BigInteger> determineRange(HDLConcat obj, HDLEvaluationContext context) {
		val type = TypeExtension::typeOf(obj) as HDLPrimitive
		return HDLPrimitives::instance.getValueRange(type, context)
	}
}
