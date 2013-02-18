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
import java.math.BigInteger

import static de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType.*
import static de.tuhh.ict.pshdl.model.HDLBitOp$HDLBitOpType.*
import static de.tuhh.ict.pshdl.model.HDLManip$HDLManipType.*
import static de.tuhh.ict.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static de.tuhh.ict.pshdl.model.extensions.ProblemDescription.*
import static de.tuhh.ict.pshdl.model.extensions.RangeExtension.*
import static java.math.BigInteger.*
import java.math.BigDecimal

class RangeExtension {
	
	public static RangeExtension INST=new RangeExtension
	
	def static rangeOf(IHDLObject obj){
		return INST.determineRange(obj, null)
	}
	
	def static rangeOf(IHDLObject obj, HDLEvaluationContext context){
		return INST.determineRange(obj, context)
	}
	
	public static GenericMeta<IHDLObject> SOURCE=new GenericMeta("SOURCE", true)
	
	def dispatch Range<BigInteger> determineRange(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Range<BigInteger> determineRange(HDLLiteral obj, HDLEvaluationContext context) {
		return Ranges::closed(obj.valueAsBigInt, obj.valueAsBigInt)
	}

	def dispatch Range<BigInteger> determineRange(HDLVariableRef obj, HDLEvaluationContext context) {
		val BigInteger bigVal = ConstantEvaluate::valueOf(obj,context)
		if (bigVal != null)
			return Ranges::closed(bigVal, bigVal)
		val HDLVariable hVar=obj.resolveVar
		if (hVar == null) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return null
		}
		var HDLAnnotation range=hVar.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
		if (range!=null){
			val value=range.value.split(";")
			//TODO Allow simple references
			return Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1)))
		}
		if (hVar.container!=null) {
			if (hVar.container instanceof HDLVariableDeclaration) {
				val HDLVariableDeclaration hvd=hVar.container as HDLVariableDeclaration
				range=hvd.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
				if (range!=null){
					val String[] value=range.value.split(";")
					//TODO Allow simple references
					return Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1)))
				}
			}
			if (hVar.container instanceof HDLForLoop){
				val HDLForLoop loop=hVar.container as HDLForLoop
				var Range<BigInteger> res=loop.range.get(0).determineRange(context)
				for (HDLRange r:loop.range){
					res=res.span(r.determineRange(context))
				}
				return res
			}
		}
		if (obj.bits.size>0){
			var BigInteger bitWidth=ZERO
			for (HDLRange r : obj.bits) {
				var HDLExpression width=r.width
				width=width.copyDeepFrozen(r)
				var BigInteger cw=ConstantEvaluate::valueOf(width,context)
				if (cw==null) {
					bitWidth=null
				}
				bitWidth=bitWidth.add(cw)
			}
			if (bitWidth!=null){
				return Ranges::closed(ZERO, ONE.shiftLeft(bitWidth.intValue).subtract(ONE))
			}
		}
		val HDLType type = TypeExtension::typeOf(hVar)
		if (type instanceof HDLPrimitive) {
			return HDLPrimitives::instance.getValueRange( type as HDLPrimitive, context)
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
		return null
	}

	def dispatch Range<BigInteger> determineRange(HDLRange obj, HDLEvaluationContext context) {
		val BigInteger to = ConstantEvaluate::valueOf(obj.to,context)
		if (obj.from!=null){
			val BigInteger from = ConstantEvaluate::valueOf(obj.from,context)
			if (from.compareTo(to)>0)
				return Ranges::closed(to, from)
			return Ranges::closed(from, to)
		}
		return Ranges::closed(to,to)
	}
	
	def dispatch Range<BigInteger> determineRange(HDLEqualityOp obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
		return Ranges::closed(ZERO, ONE)
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
		case type==OR || type==XOR:{
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
			return Ranges::closed(ZERO, ONE.shiftLeft(leftRange.upperEndpoint.bitLength).subtract(ONE))
		}
		case AND: {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
			return Ranges::closed(ZERO, leftRange.upperEndpoint.min(ONE.shiftLeft(rightRange.upperEndpoint.bitLength).subtract(ONE)))
		}
		case type==LOGI_AND || type==LOGI_OR: {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
			return Ranges::closed(ZERO, ONE)
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
			return Ranges::closed(leftRange.lowerEndpoint.add(rightRange.lowerEndpoint), leftRange.upperEndpoint.add(rightRange.upperEndpoint))
		case MINUS:
			return Ranges::closed(leftRange.lowerEndpoint.subtract(rightRange.lowerEndpoint), leftRange.upperEndpoint.subtract(rightRange.upperEndpoint))
		case DIV: {
			if (rightRange.lowerEndpoint.equals(ZERO) || rightRange.upperEndpoint.equals(ZERO)) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, ZERO_DIVIDE)
				return null
			}
			if (rightRange.lowerEndpoint.signum * rightRange.upperEndpoint.signum < 0 || rightRange.upperEndpoint.signum == 0) {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, POSSIBLY_ZERO_DIVIDE)
			}
			val mulRange = Ranges::closed(BigDecimal::ONE.divide(new BigDecimal(rightRange.lowerEndpoint)), BigDecimal::ONE.divide(new BigDecimal(rightRange.upperEndpoint)))
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
			return Ranges::closed(ZERO, rightRange.upperEndpoint.subtract(ONE).min(leftRange.upperEndpoint))
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
				val Range<BigInteger> castRange = HDLPrimitives::getInstance.getValueRange(type as HDLPrimitive, context)
				return castRange.intersection(right)
			}
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
			return null
		}
		case ARITH_NEG:
			return Ranges::closed(right.upperEndpoint.negate, right.lowerEndpoint.negate)
		case BIT_NEG:
			return Ranges::closed(ZERO, ONE.shiftLeft(right.upperEndpoint.bitLength).subtract(ONE))
		case LOGIC_NEG: {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
			return Ranges::closed(ZERO, ONE)
		}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Range<BigInteger> determineRange(HDLFunctionCall obj, HDLEvaluationContext context) {
		return HDLFunctions::determineRange(obj, context)
	}

	def dispatch Range<BigInteger> determineRange(HDLConcat obj, HDLEvaluationContext context) {
		val type=TypeExtension::typeOf(obj) as HDLPrimitive
		return HDLPrimitives::instance.getValueRange(type, context)
	}
}
