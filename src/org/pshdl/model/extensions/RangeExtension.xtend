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

import com.google.common.base.Optional
import com.google.common.collect.Range
import java.math.BigDecimal
import java.math.BigInteger
import java.util.HashSet
import java.util.LinkedHashSet
import org.pshdl.interpreter.frames.BigIntegerFrame
import org.pshdl.model.HDLAnnotation
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLArithOp.HDLArithOpType
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLObject.GenericMeta
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLType
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.evaluation.ConstantEvaluate
import org.pshdl.model.evaluation.HDLEvaluationContext
import org.pshdl.model.parser.PSHDLParser
import org.pshdl.model.simulation.RangeTool
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import org.pshdl.model.utils.Insulin
import org.pshdl.model.validation.Problem

import static java.math.BigInteger.*
import static org.pshdl.model.HDLArithOp.HDLArithOpType.*
import static org.pshdl.model.HDLBitOp.HDLBitOpType.*
import static org.pshdl.model.HDLManip.HDLManipType.*
import static org.pshdl.model.HDLShiftOp.HDLShiftOpType.*
import static org.pshdl.model.extensions.ProblemDescription.*
import static org.pshdl.model.extensions.RangeExtension.*
import org.pshdl.model.utils.HDLCodeGenerationException

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
		return rangeOf(obj, null)
	}

	def static Range<BigInteger> rangeOfForced(HDLExpression obj, HDLEvaluationContext context, String stage) {
		val opt=rangeOf(obj, context)
		if (opt.present)
			return opt.get
		throw new HDLCodeGenerationException(obj, "Unable to determine value range of "+obj, stage)
	}
	/**
	 * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription 
	 * Meta for information.
	 */
	def static Optional<Range<BigInteger>> rangeOf(HDLExpression obj, HDLEvaluationContext context) {
		if (obj === null)
			throw new NullPointerException()
		val range = INST.determineRange(obj, context)
		if (range === null)
			throw new NullPointerException(obj.toString)
		return range
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLExpression obj, HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented obj op:" + obj.classType + " " + obj)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLUnresolvedFragment obj, HDLEvaluationContext context) {
		val type = Insulin.resolveFragment(obj)
		if (!type.present)
			return Optional.absent
		val copyDeepFrozen = type.get.obj.copyDeepFrozen(obj.container) as HDLExpression
		return (copyDeepFrozen).determineRange(context)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLLiteral obj, HDLEvaluationContext context) {
		val bigVal = obj.valueAsBigInt
		if (bigVal === null)
			return Optional.absent
		return Optional.of(RangeTool.createRange(bigVal, bigVal))
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLVariableRef obj, HDLEvaluationContext context) {
		val Optional<BigInteger> bigVal = ConstantEvaluate.valueOf(obj, context)
		if (bigVal.present)
			return Optional.of(RangeTool.createRange(bigVal.get, bigVal.get))
		val hVar = obj.resolveVar
		if (!hVar.present) {
			obj.addMeta(SOURCE, obj)
			obj.addMeta(DESCRIPTION, VARIABLE_NOT_RESOLVED)
			return Optional.absent
		}
		val annoCheck = HDLBuiltInAnnotations.checkRangeAnnotation(hVar.get.getAnnotation(HDLBuiltInAnnotations.range), new LinkedHashSet<Problem>())
		if (annoCheck.present)
			return annoCheck
		val container = hVar.get.container
		if (container !== null) {
			if (container instanceof HDLVariableDeclaration) {
				val HDLVariableDeclaration hvd = container as HDLVariableDeclaration
				val subAnnoCheck = HDLBuiltInAnnotations.checkRangeAnnotation(hvd.getAnnotation(HDLBuiltInAnnotations.range), new LinkedHashSet<Problem>())
				if (subAnnoCheck.present)
					return subAnnoCheck
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
							Optional.absent
					}
					return Optional.of(res)
				} else {
					return Optional.absent
				}
			}
		}
		if (obj.bits.size > 0) {
			var BigInteger bitWidth = 0bi
			for (HDLRange r : obj.bits) {
				var HDLExpression width = r.width
				width = width.copyDeepFrozen(r)
				var Optional<BigInteger> cw = ConstantEvaluate.valueOf(width, context)
				if (!cw.present) {
					bitWidth = null
				} else {
					if (bitWidth !== null)
						bitWidth = bitWidth.add(cw.get)
				}
			}
			if (bitWidth !== null) {
				return Optional.of(RangeTool.createRange(0bi, 1bi.shiftLeft(bitWidth.intValue).subtract(1bi)))
			}
		}
		val Optional<? extends HDLType> type = TypeExtension.typeOf(hVar.get)
		if (type.present && type.get instanceof HDLPrimitive) {
			return HDLPrimitives.instance.getValueRange(type.get as HDLPrimitive, context)
		}
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, NON_PRIMITVE_TYPE_NOT_EVALUATED)
		return Optional.absent
	}


	def static Optional<Range<BigInteger>> rangeOf(HDLRange obj) {
		return rangeOf(obj, null)
	}

	def static Range<BigInteger> rangeOfForced(HDLRange obj, HDLEvaluationContext context, String stage) {
		val opt=rangeOf(obj, context)
		if (opt.present)
			return opt.get
		throw new HDLCodeGenerationException(obj, "Unable to determine value range of "+obj, stage)
	}
	def static Optional<Range<BigInteger>> rangeOf(HDLRange obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> to = rangeOf(obj.to, context)
		if (!to.present)
			return Optional.absent;
		if (obj.from !== null) {
			val from = rangeOf(obj.from, context)
			if (!from.present)
				return Optional.absent;
			return Optional.of(from.get.span(to.get))
		}
		if (obj.dec !== null) {
			val decVal = rangeOf(obj.dec, context)
			if (!decVal.present)
				return Optional.absent
			return Optional.of(to.get.span(decVal.get))
		}
		if (obj.inc !== null) {
			val incVal = rangeOf(obj.inc, context)
			if (!incVal.present)
				return Optional.absent
			return Optional.of(to.get.span(incVal.get))
		}
		return to
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLEqualityOp obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
		return Optional.of(RangeTool.createRange(0bi, 1bi))
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLShiftOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional.absent
		val lrVal=leftRange.get
		if (!lrVal.hasLowerBound || !lrVal.hasUpperBound)
			return Optional.absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional.absent
		val rrVal = rightRange.get
		if (!rrVal.hasLowerBound || !rrVal.hasUpperBound)
			return Optional.absent
		switch (obj.type) {
			case SLL: {
				val BigInteger ff = lrVal.lowerEndpoint.shiftLeft(rrVal.lowerEndpoint.intValue)
				val BigInteger ft = lrVal.lowerEndpoint.shiftLeft(rrVal.upperEndpoint.intValue)
				val BigInteger tf = lrVal.upperEndpoint.shiftLeft(rrVal.lowerEndpoint.intValue)
				val BigInteger tt = lrVal.upperEndpoint.shiftLeft(rrVal.upperEndpoint.intValue)
				return Optional.of(RangeTool.createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case SRA: {
				val BigInteger ff = lrVal.lowerEndpoint.shiftRight(rrVal.lowerEndpoint.intValue)
				val BigInteger ft = lrVal.lowerEndpoint.shiftRight(rrVal.upperEndpoint.intValue)
				val BigInteger tf = lrVal.upperEndpoint.shiftRight(rrVal.lowerEndpoint.intValue)
				val BigInteger tt = lrVal.upperEndpoint.shiftRight(rrVal.upperEndpoint.intValue)
				return Optional.of(RangeTool.createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case SRL: {
				val BigInteger ff = srl(lrVal.lowerEndpoint, rrVal.lowerEndpoint)
				val BigInteger ft = srl(lrVal.lowerEndpoint, rrVal.upperEndpoint)
				val BigInteger tf = srl(lrVal.upperEndpoint, rrVal.lowerEndpoint)
				val BigInteger tt = srl(lrVal.upperEndpoint, rrVal.upperEndpoint)
				return Optional.of(RangeTool.createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def private static BigInteger srl(BigInteger a, BigInteger b) {
		BigIntegerFrame.srl(a, 1024, b.intValue);
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLBitOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional.absent
		val lrVal=leftRange.get
		if (!lrVal.hasLowerBound || !lrVal.hasUpperBound)
			return Optional.absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional.absent
		val rrVal = rightRange.get
		if (!rrVal.hasLowerBound || !rrVal.hasUpperBound)
			return Optional.absent
		switch (type: obj.type) {
			case type == OR || type == XOR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Optional.of(
					RangeTool.createRange(0bi, 1bi.shiftLeft(lrVal.upperEndpoint.bitLength).subtract(1bi)))
			}
			case AND: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BIT_NOT_SUPPORTED_FOR_RANGES)
				return Optional.of(
					RangeTool.createRange(0bi,
						lrVal.upperEndpoint.min(
							1bi.shiftLeft(rrVal.upperEndpoint.bitLength).subtract(1bi))))
			}
			case type == LOGI_AND || type == LOGI_OR: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Optional.of(RangeTool.createRange(0bi, 1bi))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	// See http://en.wikipedia.org/wiki/Interval_arithmetic#Simple_arithmetic
	def dispatch Optional<Range<BigInteger>> determineRange(HDLArithOp obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> leftRange = obj.left.determineRange(context)
		if (!leftRange.present)
			return Optional.absent
		val lrVal=leftRange.get
		if (!lrVal.hasLowerBound || !lrVal.hasUpperBound)
			return Optional.absent
		val Optional<Range<BigInteger>> rightRange = obj.right.determineRange(context)
		if (!rightRange.present)
			return Optional.absent
		val rrVal = rightRange.get
		if (!rrVal.hasLowerBound || !rrVal.hasUpperBound)
			return Optional.absent
		switch (obj.type) {
			case PLUS:
				return Optional.of(
					RangeTool.createRange(lrVal.lowerEndpoint.add(rrVal.lowerEndpoint),
						lrVal.upperEndpoint.add(rrVal.upperEndpoint)))
			case MINUS:
				return Optional.of(
					RangeTool.createRange(lrVal.lowerEndpoint.subtract(rrVal.lowerEndpoint),
						lrVal.upperEndpoint.subtract(rrVal.upperEndpoint)))
			case DIV: {
				if (rrVal.lowerEndpoint.equals(0bi) || rrVal.upperEndpoint.equals(0bi)) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, ZERO_DIVIDE)
					return Optional.absent
				}
				if (rrVal.lowerEndpoint.signum * rrVal.upperEndpoint.signum < 0 ||
					rrVal.upperEndpoint.signum == 0) {
					obj.addMeta(SOURCE, obj)
					obj.addMeta(DESCRIPTION, POSSIBLY_ZERO_DIVIDE)
				}
				val mulRange = RangeTool.createRange(1bd.divide(new BigDecimal(rrVal.lowerEndpoint)),
					1bd.divide(new BigDecimal(rrVal.upperEndpoint)))
				val BigDecimal ff = new BigDecimal(lrVal.lowerEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal ft = new BigDecimal(lrVal.lowerEndpoint).multiply(mulRange.upperEndpoint)
				val BigDecimal tf = new BigDecimal(lrVal.upperEndpoint).multiply(mulRange.lowerEndpoint)
				val BigDecimal tt = new BigDecimal(lrVal.upperEndpoint).multiply(mulRange.upperEndpoint)
				return Optional.of(
					RangeTool.createRange(ff.min(ft).min(tf).min(tt).toBigInteger,
						ff.max(ft).max(tf).max(tt).toBigInteger))
			}
			case MUL: {
				val BigInteger ff = lrVal.lowerEndpoint.multiply(rrVal.lowerEndpoint)
				val BigInteger ft = lrVal.lowerEndpoint.multiply(rrVal.upperEndpoint)
				val BigInteger tf = lrVal.upperEndpoint.multiply(rrVal.lowerEndpoint)
				val BigInteger tt = lrVal.upperEndpoint.multiply(rrVal.upperEndpoint)
				return Optional.of(RangeTool.createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
			case MOD: {
				val rle = rrVal.lowerEndpoint
				val leftBound = rle.min(0bi)
				val rue = rrVal.upperEndpoint.subtract(BigInteger.ONE)
				val rightBound = rue.max(0bi)
				return Optional.of(RangeTool.createRange(leftBound, rightBound))
			}
			case POW: {
				val BigInteger ff = lrVal.lowerEndpoint.pow(rrVal.lowerEndpoint.intValue)
				val BigInteger ft = lrVal.lowerEndpoint.pow(rrVal.upperEndpoint.intValue)
				val BigInteger tf = lrVal.upperEndpoint.pow(rrVal.lowerEndpoint.intValue)
				val BigInteger tt = lrVal.upperEndpoint.pow(rrVal.upperEndpoint.intValue)
				return Optional.of(RangeTool.createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLEnumRef obj, HDLEvaluationContext context) {
		obj.addMeta(SOURCE, obj)
		obj.addMeta(DESCRIPTION, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS)
		return Optional.absent
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLManip obj, HDLEvaluationContext context) {
		val Optional<Range<BigInteger>> right = obj.target.determineRange(context)
		if (!right.present)
			return Optional.absent
		switch (obj.type) {
			case CAST: {
				val HDLType type = obj.castTo
				if (type instanceof HDLPrimitive) {
					val Optional<Range<BigInteger>> castRange = HDLPrimitives.getInstance.getValueRange(
						type as HDLPrimitive, context)
					if (type.type == HDLPrimitiveType.INTEGER) {
						return Optional.of(HDLPrimitives.intRange(32bi).intersection(right.get))
					}
					if (type.type == HDLPrimitiveType.NATURAL) {
						return Optional.of(HDLPrimitives.uintRange(32bi).intersection(right.get))
					}
					if (type.type == HDLPrimitiveType.BIT) {
						return Optional.of(RangeTool.createRange(0bi, 1bi).intersection(right.get))
					}
					if (!castRange.present)
						return Optional.absent
					return Optional.of(castRange.get.intersection(right.get))
				}
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, TYPE_NOT_SUPPORTED_FOR_CONSTANTS)
				return Optional.absent
			}
			case ARITH_NEG:
				return Optional.of(RangeTool.createRange(right.get.upperEndpoint.negate, right.get.lowerEndpoint.negate))
			case BIT_NEG:
				return Optional.of(
					RangeTool.createRange(0bi, 1bi.shiftLeft(right.get.upperEndpoint.bitLength).subtract(1bi)))
			case LOGIC_NEG: {
				obj.addMeta(SOURCE, obj)
				obj.addMeta(DESCRIPTION, BOOLEAN_NOT_SUPPORTED_FOR_RANGES)
				return Optional.of(RangeTool.createRange(0bi, 1bi))
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op")
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLFunctionCall obj, HDLEvaluationContext context) {
		return HDLFunctions.determineRange(obj, context)
	}

	def dispatch Optional<Range<BigInteger>> determineRange(HDLConcat obj, HDLEvaluationContext context) {
		val type = TypeExtension.typeOf(obj)
		if (!type.present)
			return Optional.absent
		return HDLPrimitives.instance.getValueRange(type.get as HDLPrimitive, context)
	}
}
