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
package org.pshdl.model.extensions

import com.google.common.collect.Range
import com.google.common.collect.Ranges
import org.pshdl.model.HDLAnnotation
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLObject$GenericMeta
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLType
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.evaluation.HDLEvaluationContext
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import org.pshdl.model.evaluation.ConstantEvaluate
import java.math.BigInteger

import static org.pshdl.model.HDLArithOp$HDLArithOpType.*
import static org.pshdl.model.HDLBitOp$HDLBitOpType.*
import static org.pshdl.model.HDLManip$HDLManipType.*
import static org.pshdl.model.HDLShiftOp$HDLShiftOpType.*
import static org.pshdl.model.extensions.ProblemDescription.*
import static org.pshdl.model.extensions.RangeExtension.*
import static java.math.BigInteger.*
import java.math.BigDecimal
import com.google.common.base.Optional
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.interpreter.frames.BigIntegerFrame

/**
 * The RangeExtensions can determine what values an expression can possible have. This is useful for detecting
 * code that will likely cause problems. For example when one parameter specifies the size of an array and 
 * another specifies the upper bound for the range of a for loop.
 * 
 * @author Karsten Becker
 */
class RangeExtension {

	private static RangeExtension INST = new RangeExtension

	/**
	 * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription 
	 * Meta for information.
	 */
	def static Optional<Range<BigInteger>> rangeOf(HDLExpression obj) {
		return INST.determineRange(obj, null)
	}

	/**
	 * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription 
	 * Meta for information.
	 */
	def static Optional<Range<BigInteger>> rangeOf(HDLExpression obj, HDLEvaluationContext context) {
		return INST.determineRange(obj, context)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented obj op:" + obj.classType + " " + obj)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLUnresolvedFragment obj, HDLEvaluationContext context) {
		return Optional::absent
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLLiteral obj, HDLEvaluationContext context) {
		return Optional::of(Ranges::closed(obj.valueAsBigInt, obj.valueAsBigInt))
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLVariableRef obj, HDLEvaluationContext context) {
		val Optional<BigInteger> bigVal = ConstantEvaluate::valueOf(obj, context)
		if (bigVal.present)
			return Optional::of(Ranges::closed(bigVal.get, bigVal.get))
		val hVar = obj.resolveVar
		if (!hVar.present) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return Optional::absent
		}
		var HDLAnnotation range = hVar.get.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
		if (range !== null) {
			val value = range.value.split(";")

			//TODO Allow simple references
			return Optional::of(Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1))))
		}
		val container = hVar.get.container
		if (container !== null) {
			if (container instanceof HDLVariableDeclaration) {
				val HDLVariableDeclaration hvd = container as HDLVariableDeclaration
				range = hvd.getAnnotation(HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations::range)
				if (range !== null) {
					val String[] value = range.value.split(";")

					//TODO Allow simple references
					return Optional::of(Ranges::closed(new BigInteger(value.get(0)), new BigInteger(value.get(1))))
				}
			}
			if (container instanceof HDLForLoop) {
				val HDLForLoop loop = container as HDLForLoop
				val zeroR = loop.range.get(0).rangeOf(context)
				if (zeroR.present) {
					var Range<BigInteger> res = zeroR.get
					for (HDLRange r : loop.range) {
						val rRange = r.rangeOf(context)
						if (rRange.present)
							res = res.span(rRange.get)
						else
							Optional::absent
					}
					return Optional::of(res)
				} else {
					return Optional::absent
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
			if (bitWidth !== null) {
				return Optional::of(Ranges::closed(0bi, 1bi.shiftLeft(bitWidth.intValue).subtract(1bi)))
			}
		}
		val Optional<? extends HDLType> type = TypeExtension::typeOf(hVar.get)
		if (type.present && type.get instanceof HDLPrimitive) {
			return HDLPrimitives::instance.getValueRange(type.get as HDLPrimitive, context)
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
		return Optional::absent
	}

	def static Optional<Range<BigInteger>> rangeOf(HDLRange obj) {
		return rangeOf(obj, null)
	}
	def static Optional<Range<BigInteger>> rangeOf(HDLRange obj, HDLEvaluationContext context) {
		val Optional<BigInteger> to = ConstantEvaluate::valueOf(obj.to, context)
		if (!to.present)
			return Optional::absent;
		if (obj.from !== null) {
			val Optional<BigInteger> from = ConstantEvaluate::valueOf(obj.from, context)
			if (!from.present)
				return Optional::absent;
			if (from.get.compareTo(to.get) > 0)
				return Optional::of(Ranges::closed(to.get, from.get))
			return Optional::of(Ranges::closed(from.get, to.get))
		}
		return Optional::of(Ranges::closed(to.get, to.get))
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLEqualityOp obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
		return Optional::of(Ranges::closed(0bi, 1bi))
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLShiftOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional::absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional::absent
		switch (obj.type) {
			case SLL: {
				val BigInteger ff = leftRange.get.lowerEndpoint.shiftLeft(rightRange.get.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.get.lowerEndpoint.shiftLeft(rightRange.get.upperEndpoint.intValue)
				val BigInteger tf = leftRange.get.upperEndpoint.shiftLeft(rightRange.get.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.get.upperEndpoint.shiftLeft(rightRange.get.upperEndpoint.intValue)
				return Optional::of(Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case SRA: {
				val BigInteger ff = leftRange.get.lowerEndpoint.shiftRight(rightRange.get.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.get.lowerEndpoint.shiftRight(rightRange.get.upperEndpoint.intValue)
				val BigInteger tf = leftRange.get.upperEndpoint.shiftRight(rightRange.get.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.get.upperEndpoint.shiftRight(rightRange.get.upperEndpoint.intValue)
				return Optional::of(Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case SRL: {
				val BigInteger ff = srl(leftRange.get.lowerEndpoint, rightRange.get.lowerEndpoint)
				val BigInteger ft = srl(leftRange.get.lowerEndpoint, rightRange.get.upperEndpoint)
				val BigInteger tf = srl(leftRange.get.upperEndpoint, rightRange.get.lowerEndpoint)
				val BigInteger tt = srl(leftRange.get.upperEndpoint, rightRange.get.upperEndpoint)
				return Optional::of(Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def private static BigInteger srl(BigInteger a, BigInteger b) {
		BigIntegerFrame::srl(a, 1024, b.intValue);
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLBitOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional::absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional::absent
		switch (type: obj.type) {
			case type == OR || type == XOR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Optional::of(
					Ranges::closed(0bi, 1bi.shiftLeft(leftRange.get.upperEndpoint.bitLength).subtract(1bi)))
			}
			case AND: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Optional::of(
					Ranges::closed(0bi,
						leftRange.get.upperEndpoint.min(
							1bi.shiftLeft(rightRange.get.upperEndpoint.bitLength).subtract(1bi))))
			}
			case type == LOGI_AND || type == LOGI_OR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Optional::of(Ranges::closed(0bi, 1bi))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	// See http://en.wikipedia.org/wiki/Interval_arithmetic#Simple_arithmetic
	def dispatch Optional<Range<BigInteger>> determineRange(HDLArithOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional::absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional::absent
		switch (obj.type) {
			case PLUS:
				return Optional::of(
					Ranges::closed(leftRange.get.lowerEndpoint.add(rightRange.get.lowerEndpoint),
						leftRange.get.upperEndpoint.add(rightRange.get.upperEndpoint)))
			case MINUS:
				return Optional::of(
					Ranges::closed(leftRange.get.lowerEndpoint.subtract(rightRange.get.lowerEndpoint),
						leftRange.get.upperEndpoint.subtract(rightRange.get.upperEndpoint)))
			case DIV: {
				if (rightRange.get.lowerEndpoint.equals(0bi) || rightRange.get.upperEndpoint.equals(0bi)) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, ZERO_DIVIDE)
					return Optional::absent
				}
				if (rightRange.get.lowerEndpoint.signum * rightRange.get.upperEndpoint.signum < 0 ||
					rightRange.get.upperEndpoint.signum == 0) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, POSSIBLY_ZERO_DIVIDE)
				}
				val mulRange = Ranges::closed(1bd.divide(new BigDecimal(rightRange.get.lowerEndpoint)),
					1bd.divide(new BigDecimal(rightRange.get.upperEndpoint)))
				val BigDecimal ff = new BigDecimal(leftRange.get.lowerEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal ft = new BigDecimal(leftRange.get.lowerEndpoint).multiply(mulRange.upperEndpoint)
				val BigDecimal tf = new BigDecimal(leftRange.get.upperEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal tt = new BigDecimal(leftRange.get.upperEndpoint).multiply(mulRange.upperEndpoint)
				return Optional::of(
					Ranges::closed(ff.min(ft).min(tf).min(tt).toBigInteger, ff.max(ft).max(tf).max(tt).toBigInteger))
			}
			case MUL: {
				val BigInteger ff = leftRange.get.lowerEndpoint.multiply(rightRange.get.lowerEndpoint)
				val BigInteger ft = leftRange.get.lowerEndpoint.multiply(rightRange.get.upperEndpoint)
				val BigInteger tf = leftRange.get.upperEndpoint.multiply(rightRange.get.lowerEndpoint)
				val BigInteger tt = leftRange.get.upperEndpoint.multiply(rightRange.get.upperEndpoint)
				return Optional::of(Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case MOD:
				return Optional::of(
					Ranges::closed(0bi, rightRange.get.upperEndpoint.subtract(1bi).min(leftRange.get.upperEndpoint)))
			case POW: {
				val BigInteger ff = leftRange.get.lowerEndpoint.pow(rightRange.get.lowerEndpoint.intValue)
				val BigInteger ft = leftRange.get.lowerEndpoint.pow(rightRange.get.upperEndpoint.intValue)
				val BigInteger tf = leftRange.get.upperEndpoint.pow(rightRange.get.lowerEndpoint.intValue)
				val BigInteger tt = leftRange.get.upperEndpoint.pow(rightRange.get.upperEndpoint.intValue)
				return Optional::of(Ranges::closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional::absent
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLManip obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> right = obj.target.determineRange(context)
		if (!right.present)
			return Optional::absent
		switch (obj.type) {
			case CAST: {
				val HDLType type = obj.castTo
				if (type instanceof HDLPrimitive) {
					val Optional<Range<BigInteger>> castRange = HDLPrimitives::getInstance.getValueRange(
						type as HDLPrimitive, context)
					if (!castRange.present)
						return Optional::absent
					if (!right.present)
						return Optional::absent
					return Optional::of(castRange.get.intersection(right.get))
				}
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
				return Optional::absent
			}
			case ARITH_NEG:
				return Optional::of(Ranges::closed(right.get.upperEndpoint.negate, right.get.lowerEndpoint.negate))
			case BIT_NEG:
				return Optional::of(Ranges::closed(0bi, 1bi.shiftLeft(right.get.upperEndpoint.bitLength).subtract(1bi)))
			case LOGIC_NEG: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Optional::of(Ranges::closed(0bi, 1bi))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLFunctionCall obj, HDLEvaluationContext context) {
		return HDLFunctions::determineRange(obj, context)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLConcat obj, HDLEvaluationContext context) {
		val type = TypeExtension::typeOf(obj)
		if (!type.present)
			return Optional::absent
		return HDLPrimitives::instance.getValueRange(type.get as HDLPrimitive, context)
	}
}
