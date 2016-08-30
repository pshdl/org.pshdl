/**
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
 */
package org.pshdl.model.extensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.pshdl.interpreter.frames.BigIntegerFrame;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.validation.Problem;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Range;

/**
 * The RangeExtensions can determine what values an expression can possible
 * have. This is useful for detecting code that will likely cause problems. For
 * example when one parameter specifies the size of an array and another
 * specifies the upper bound for the range of a for loop.
 *
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class RangeExtension {
	private static RangeExtension INST = new RangeExtension();

	/**
	 * Attempts to determine the range of an {@link HDLExpression}. If not
	 * successful check ProblemDescription Meta for information.
	 */
	public static Optional<Range<BigInteger>> rangeOf(final HDLExpression obj) {
		return RangeExtension.rangeOf(obj, null);
	}

	public static Range<BigInteger> rangeOfForced(final HDLExpression obj, final HDLEvaluationContext context, final String stage) {
		final Optional<Range<BigInteger>> opt = RangeExtension.rangeOf(obj, context);
		final boolean _isPresent = opt.isPresent();
		if (_isPresent)
			return opt.get();
		throw new HDLCodeGenerationException(obj, ("Unable to determine value range of " + obj), stage);
	}

	/**
	 * Attempts to determine the range of an {@link HDLExpression}. If not
	 * successful check ProblemDescription Meta for information.
	 */
	public static Optional<Range<BigInteger>> rangeOf(final HDLExpression obj, final HDLEvaluationContext context) {
		if ((obj == null))
			throw new NullPointerException();
		final Optional<Range<BigInteger>> range = RangeExtension.INST.determineRange(obj, context);
		if ((range == null)) {
			final String _string = obj.toString();
			throw new NullPointerException(_string);
		}
		return range;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
		final HDLClass _classType = obj.getClassType();
		final String _plus = ("Incorrectly implemented obj op:" + _classType);
		final String _plus_1 = (_plus + " ");
		final String _plus_2 = (_plus_1 + obj);
		throw new RuntimeException(_plus_2);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLUnresolvedFragment obj, final HDLEvaluationContext context) {
		final Optional<Insulin.ResolvedPart> type = Insulin.resolveFragment(obj);
		final boolean _isPresent = type.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final Insulin.ResolvedPart _get = type.get();
		final IHDLObject _container = obj.getContainer();
		final IHDLObject _copyDeepFrozen = _get.obj.copyDeepFrozen(_container);
		final HDLExpression copyDeepFrozen = ((HDLExpression) _copyDeepFrozen);
		return this.determineRange(copyDeepFrozen, context);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLLiteral obj, final HDLEvaluationContext context) {
		final BigInteger bigVal = obj.getValueAsBigInt();
		if ((bigVal == null))
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(bigVal, bigVal);
		return Optional.<Range<BigInteger>> of(_createRange);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLVariableRef obj, final HDLEvaluationContext context) {
		final Optional<BigInteger> bigVal = ConstantEvaluate.valueOf(obj, context);
		final boolean _isPresent = bigVal.isPresent();
		if (_isPresent) {
			final BigInteger _get = bigVal.get();
			final BigInteger _get_1 = bigVal.get();
			final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(_get, _get_1);
			return Optional.<Range<BigInteger>> of(_createRange);
		}
		final Optional<HDLVariable> hVar = obj.resolveVar();
		final boolean _isPresent_1 = hVar.isPresent();
		final boolean _not = (!_isPresent_1);
		if (_not) {
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
			return Optional.<Range<BigInteger>> absent();
		}
		final HDLVariable _get_2 = hVar.get();
		final HDLAnnotation _annotation = _get_2.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.range);
		final LinkedHashSet<Problem> _linkedHashSet = new LinkedHashSet<Problem>();
		final Optional<Range<BigInteger>> annoCheck = HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.checkRangeAnnotation(_annotation, _linkedHashSet);
		final boolean _isPresent_2 = annoCheck.isPresent();
		if (_isPresent_2)
			return annoCheck;
		final HDLVariable _get_3 = hVar.get();
		final IHDLObject container = _get_3.getContainer();
		if ((container != null)) {
			if ((container instanceof HDLVariableDeclaration)) {
				final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) container);
				final HDLAnnotation _annotation_1 = hvd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.range);
				final LinkedHashSet<Problem> _linkedHashSet_1 = new LinkedHashSet<Problem>();
				final Optional<Range<BigInteger>> subAnnoCheck = HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.checkRangeAnnotation(_annotation_1, _linkedHashSet_1);
				final boolean _isPresent_3 = subAnnoCheck.isPresent();
				if (_isPresent_3)
					return subAnnoCheck;
			}
			if ((container instanceof HDLForLoop)) {
				final HDLForLoop loop = ((HDLForLoop) container);
				final ArrayList<HDLRange> _range = loop.getRange();
				final HDLRange _get_4 = _range.get(0);
				final Optional<Range<BigInteger>> zeroR = RangeExtension.rangeOf(_get_4, context);
				final boolean _isPresent_4 = zeroR.isPresent();
				if (_isPresent_4) {
					Range<BigInteger> res = zeroR.get();
					final ArrayList<HDLRange> _range_1 = loop.getRange();
					for (final HDLRange r : _range_1) {
						{
							final Optional<Range<BigInteger>> rRange = RangeExtension.rangeOf(r, context);
							final boolean _isPresent_5 = rRange.isPresent();
							if (_isPresent_5) {
								final Range<BigInteger> _get_5 = rRange.get();
								final Range<BigInteger> _span = res.span(_get_5);
								res = _span;
							} else {
								Optional.<Object> absent();
							}
						}
					}
					return Optional.<Range<BigInteger>> of(res);
				} else
					return Optional.<Range<BigInteger>> absent();
			}
		}
		final ArrayList<HDLRange> _bits = obj.getBits();
		final int _size = _bits.size();
		final boolean _greaterThan = (_size > 0);
		if (_greaterThan) {
			BigInteger bitWidth = BigInteger.ZERO;
			final ArrayList<HDLRange> _bits_1 = obj.getBits();
			for (final HDLRange r_1 : _bits_1) {
				{
					HDLExpression width = r_1.getWidth();
					final HDLExpression _copyDeepFrozen = width.copyDeepFrozen(r_1);
					width = _copyDeepFrozen;
					final Optional<BigInteger> cw = ConstantEvaluate.valueOf(width, context);
					final boolean _isPresent_5 = cw.isPresent();
					final boolean _not_1 = (!_isPresent_5);
					if (_not_1) {
						bitWidth = null;
					} else {
						if ((bitWidth != null)) {
							final BigInteger _get_5 = cw.get();
							final BigInteger _add = bitWidth.add(_get_5);
							bitWidth = _add;
						}
					}
				}
			}
			if ((bitWidth != null)) {
				final int _intValue = bitWidth.intValue();
				final BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
				final BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
				final Range<BigInteger> _createRange_1 = RangeTool.<BigInteger> createRange(BigInteger.ZERO, _subtract);
				return Optional.<Range<BigInteger>> of(_createRange_1);
			}
		}
		final HDLVariable _get_5 = hVar.get();
		final Optional<? extends HDLType> type = TypeExtension.typeOf(_get_5);
		if ((type.isPresent() && (type.get() instanceof HDLPrimitive))) {
			final HDLPrimitives _instance = HDLPrimitives.getInstance();
			final HDLType _get_6 = type.get();
			return _instance.getValueRange(((HDLPrimitive) _get_6), context);
		}
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
		return Optional.<Range<BigInteger>> absent();
	}

	public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj) {
		return RangeExtension.rangeOf(obj, null);
	}

	public static Range<BigInteger> rangeOfForced(final HDLRange obj, final HDLEvaluationContext context, final String stage) {
		final Optional<Range<BigInteger>> opt = RangeExtension.rangeOf(obj, context);
		final boolean _isPresent = opt.isPresent();
		if (_isPresent)
			return opt.get();
		throw new HDLCodeGenerationException(obj, ("Unable to determine value range of " + obj), stage);
	}

	public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj, final HDLEvaluationContext context) {
		final HDLExpression _to = obj.getTo();
		final Optional<Range<BigInteger>> to = RangeExtension.rangeOf(_to, context);
		final boolean _isPresent = to.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final HDLExpression _from = obj.getFrom();
		final boolean _tripleNotEquals = (_from != null);
		if (_tripleNotEquals) {
			final HDLExpression _from_1 = obj.getFrom();
			final Optional<Range<BigInteger>> from = RangeExtension.rangeOf(_from_1, context);
			final boolean _isPresent_1 = from.isPresent();
			final boolean _not_1 = (!_isPresent_1);
			if (_not_1)
				return Optional.<Range<BigInteger>> absent();
			final Range<BigInteger> _get = from.get();
			final Range<BigInteger> _get_1 = to.get();
			final Range<BigInteger> _span = _get.span(_get_1);
			return Optional.<Range<BigInteger>> of(_span);
		}
		final HDLExpression _dec = obj.getDec();
		final boolean _tripleNotEquals_1 = (_dec != null);
		if (_tripleNotEquals_1) {
			final HDLExpression _dec_1 = obj.getDec();
			final Optional<Range<BigInteger>> decVal = RangeExtension.rangeOf(_dec_1, context);
			final boolean _isPresent_2 = decVal.isPresent();
			final boolean _not_2 = (!_isPresent_2);
			if (_not_2)
				return Optional.<Range<BigInteger>> absent();
			final Range<BigInteger> _get_2 = to.get();
			final Range<BigInteger> _get_3 = decVal.get();
			final Range<BigInteger> _span_1 = _get_2.span(_get_3);
			return Optional.<Range<BigInteger>> of(_span_1);
		}
		final HDLExpression _inc = obj.getInc();
		final boolean _tripleNotEquals_2 = (_inc != null);
		if (_tripleNotEquals_2) {
			final HDLExpression _inc_1 = obj.getInc();
			final Optional<Range<BigInteger>> incVal = RangeExtension.rangeOf(_inc_1, context);
			final boolean _isPresent_3 = incVal.isPresent();
			final boolean _not_3 = (!_isPresent_3);
			if (_not_3)
				return Optional.<Range<BigInteger>> absent();
			final Range<BigInteger> _get_4 = to.get();
			final Range<BigInteger> _get_5 = incVal.get();
			final Range<BigInteger> _span_2 = _get_4.span(_get_5);
			return Optional.<Range<BigInteger>> of(_span_2);
		}
		return to;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLEqualityOp obj, final HDLEvaluationContext context) {
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
		final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(BigInteger.ZERO, BigInteger.ONE);
		return Optional.<Range<BigInteger>> of(_createRange);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLShiftOp obj, final HDLEvaluationContext context) {
		final HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		final boolean _isPresent = leftRange.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> lrVal = leftRange.get();
		if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		final boolean _isPresent_1 = rightRange.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> rrVal = rightRange.get();
		if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLShiftOp.HDLShiftOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case SLL:
				final BigInteger _lowerEndpoint = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_1 = rrVal.lowerEndpoint();
				final int _intValue = _lowerEndpoint_1.intValue();
				final BigInteger ff = _lowerEndpoint.shiftLeft(_intValue);
				final BigInteger _lowerEndpoint_2 = lrVal.lowerEndpoint();
				final BigInteger _upperEndpoint = rrVal.upperEndpoint();
				final int _intValue_1 = _upperEndpoint.intValue();
				final BigInteger ft = _lowerEndpoint_2.shiftLeft(_intValue_1);
				final BigInteger _upperEndpoint_1 = lrVal.upperEndpoint();
				final BigInteger _lowerEndpoint_3 = rrVal.lowerEndpoint();
				final int _intValue_2 = _lowerEndpoint_3.intValue();
				final BigInteger tf = _upperEndpoint_1.shiftLeft(_intValue_2);
				final BigInteger _upperEndpoint_2 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_3 = rrVal.upperEndpoint();
				final int _intValue_3 = _upperEndpoint_3.intValue();
				final BigInteger tt = _upperEndpoint_2.shiftLeft(_intValue_3);
				final BigInteger _min = ff.min(ft);
				final BigInteger _min_1 = _min.min(tf);
				final BigInteger _min_2 = _min_1.min(tt);
				final BigInteger _max = ff.max(ft);
				final BigInteger _max_1 = _max.max(tf);
				final BigInteger _max_2 = _max_1.max(tt);
				final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(_min_2, _max_2);
				return Optional.<Range<BigInteger>> of(_createRange);
			case SRA:
				final BigInteger _lowerEndpoint_4 = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_5 = rrVal.lowerEndpoint();
				final int _intValue_4 = _lowerEndpoint_5.intValue();
				final BigInteger ff_1 = _lowerEndpoint_4.shiftRight(_intValue_4);
				final BigInteger _lowerEndpoint_6 = lrVal.lowerEndpoint();
				final BigInteger _upperEndpoint_4 = rrVal.upperEndpoint();
				final int _intValue_5 = _upperEndpoint_4.intValue();
				final BigInteger ft_1 = _lowerEndpoint_6.shiftRight(_intValue_5);
				final BigInteger _upperEndpoint_5 = lrVal.upperEndpoint();
				final BigInteger _lowerEndpoint_7 = rrVal.lowerEndpoint();
				final int _intValue_6 = _lowerEndpoint_7.intValue();
				final BigInteger tf_1 = _upperEndpoint_5.shiftRight(_intValue_6);
				final BigInteger _upperEndpoint_6 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_7 = rrVal.upperEndpoint();
				final int _intValue_7 = _upperEndpoint_7.intValue();
				final BigInteger tt_1 = _upperEndpoint_6.shiftRight(_intValue_7);
				final BigInteger _min_3 = ff_1.min(ft_1);
				final BigInteger _min_4 = _min_3.min(tf_1);
				final BigInteger _min_5 = _min_4.min(tt_1);
				final BigInteger _max_3 = ff_1.max(ft_1);
				final BigInteger _max_4 = _max_3.max(tf_1);
				final BigInteger _max_5 = _max_4.max(tt_1);
				final Range<BigInteger> _createRange_1 = RangeTool.<BigInteger> createRange(_min_5, _max_5);
				return Optional.<Range<BigInteger>> of(_createRange_1);
			case SRL:
				final BigInteger _lowerEndpoint_8 = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_9 = rrVal.lowerEndpoint();
				final BigInteger ff_2 = RangeExtension.srl(_lowerEndpoint_8, _lowerEndpoint_9);
				final BigInteger _lowerEndpoint_10 = lrVal.lowerEndpoint();
				final BigInteger _upperEndpoint_8 = rrVal.upperEndpoint();
				final BigInteger ft_2 = RangeExtension.srl(_lowerEndpoint_10, _upperEndpoint_8);
				final BigInteger _upperEndpoint_9 = lrVal.upperEndpoint();
				final BigInteger _lowerEndpoint_11 = rrVal.lowerEndpoint();
				final BigInteger tf_2 = RangeExtension.srl(_upperEndpoint_9, _lowerEndpoint_11);
				final BigInteger _upperEndpoint_10 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_11 = rrVal.upperEndpoint();
				final BigInteger tt_2 = RangeExtension.srl(_upperEndpoint_10, _upperEndpoint_11);
				final BigInteger _min_6 = ff_2.min(ft_2);
				final BigInteger _min_7 = _min_6.min(tf_2);
				final BigInteger _min_8 = _min_7.min(tt_2);
				final BigInteger _max_6 = ff_2.max(ft_2);
				final BigInteger _max_7 = _max_6.max(tf_2);
				final BigInteger _max_8 = _max_7.max(tt_2);
				final Range<BigInteger> _createRange_2 = RangeTool.<BigInteger> createRange(_min_8, _max_8);
				return Optional.<Range<BigInteger>> of(_createRange_2);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op");
	}

	private static BigInteger srl(final BigInteger a, final BigInteger b) {
		final int _intValue = b.intValue();
		return BigIntegerFrame.srl(a, 1024, _intValue);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLBitOp obj, final HDLEvaluationContext context) {
		final HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		final boolean _isPresent = leftRange.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> lrVal = leftRange.get();
		if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		final boolean _isPresent_1 = rightRange.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> rrVal = rightRange.get();
		if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLBitOp.HDLBitOpType _type = obj.getType();
		final HDLBitOp.HDLBitOpType type = _type;
		boolean _matched = false;
		if ((Objects.equal(type, HDLBitOp.HDLBitOpType.OR) || Objects.equal(type, HDLBitOp.HDLBitOpType.XOR))) {
			_matched = true;
			obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
			final BigInteger _upperEndpoint = lrVal.upperEndpoint();
			final int _bitLength = _upperEndpoint.bitLength();
			final BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
			final BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
			final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(BigInteger.ZERO, _subtract);
			return Optional.<Range<BigInteger>> of(_createRange);
		}
		if (!_matched) {
			if (Objects.equal(type, HDLBitOp.HDLBitOpType.AND)) {
				_matched = true;
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
				final BigInteger _upperEndpoint_1 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_2 = rrVal.upperEndpoint();
				final int _bitLength_1 = _upperEndpoint_2.bitLength();
				final BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(_bitLength_1);
				final BigInteger _subtract_1 = _shiftLeft_1.subtract(BigInteger.ONE);
				final BigInteger _min = _upperEndpoint_1.min(_subtract_1);
				final Range<BigInteger> _createRange_1 = RangeTool.<BigInteger> createRange(BigInteger.ZERO, _min);
				return Optional.<Range<BigInteger>> of(_createRange_1);
			}
		}
		if (!_matched) {
			if ((Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_AND) || Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_OR))) {
				_matched = true;
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
				final Range<BigInteger> _createRange_2 = RangeTool.<BigInteger> createRange(BigInteger.ZERO, BigInteger.ONE);
				return Optional.<Range<BigInteger>> of(_createRange_2);
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op");
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLArithOp obj, final HDLEvaluationContext context) {
		final HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		final boolean _isPresent = leftRange.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> lrVal = leftRange.get();
		if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		final boolean _isPresent_1 = rightRange.isPresent();
		final boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		final Range<BigInteger> rrVal = rightRange.get();
		if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound())))
			return Optional.<Range<BigInteger>> absent();
		final HDLArithOp.HDLArithOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case PLUS:
				final BigInteger _lowerEndpoint = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_1 = rrVal.lowerEndpoint();
				final BigInteger _add = _lowerEndpoint.add(_lowerEndpoint_1);
				final BigInteger _upperEndpoint = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_1 = rrVal.upperEndpoint();
				final BigInteger _add_1 = _upperEndpoint.add(_upperEndpoint_1);
				final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(_add, _add_1);
				return Optional.<Range<BigInteger>> of(_createRange);
			case MINUS:
				final BigInteger _lowerEndpoint_2 = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_3 = rrVal.lowerEndpoint();
				final BigInteger _subtract = _lowerEndpoint_2.subtract(_lowerEndpoint_3);
				final BigInteger _upperEndpoint_2 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_3 = rrVal.upperEndpoint();
				final BigInteger _subtract_1 = _upperEndpoint_2.subtract(_upperEndpoint_3);
				final Range<BigInteger> _createRange_1 = RangeTool.<BigInteger> createRange(_subtract, _subtract_1);
				return Optional.<Range<BigInteger>> of(_createRange_1);
			case DIV:
				if ((rrVal.lowerEndpoint().equals(BigInteger.ZERO) || rrVal.upperEndpoint().equals(BigInteger.ZERO))) {
					obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
					return Optional.<Range<BigInteger>> absent();
				}
				if ((((rrVal.lowerEndpoint().signum() * rrVal.upperEndpoint().signum()) < 0) || (rrVal.upperEndpoint().signum() == 0))) {
					obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
				}
				final BigInteger _lowerEndpoint_4 = rrVal.lowerEndpoint();
				final BigDecimal _bigDecimal = new BigDecimal(_lowerEndpoint_4);
				final BigDecimal _divide = BigDecimal.ONE.divide(_bigDecimal);
				final BigInteger _upperEndpoint_4 = rrVal.upperEndpoint();
				final BigDecimal _bigDecimal_1 = new BigDecimal(_upperEndpoint_4);
				final BigDecimal _divide_1 = BigDecimal.ONE.divide(_bigDecimal_1);
				final Range<BigDecimal> mulRange = RangeTool.<BigDecimal> createRange(_divide, _divide_1);
				final BigInteger _lowerEndpoint_5 = lrVal.lowerEndpoint();
				final BigDecimal _bigDecimal_2 = new BigDecimal(_lowerEndpoint_5);
				final BigDecimal _lowerEndpoint_6 = mulRange.lowerEndpoint();
				final BigDecimal ff = _bigDecimal_2.multiply(_lowerEndpoint_6);
				final BigInteger _lowerEndpoint_7 = lrVal.lowerEndpoint();
				final BigDecimal _bigDecimal_3 = new BigDecimal(_lowerEndpoint_7);
				final BigDecimal _upperEndpoint_5 = mulRange.upperEndpoint();
				final BigDecimal ft = _bigDecimal_3.multiply(_upperEndpoint_5);
				final BigInteger _upperEndpoint_6 = lrVal.upperEndpoint();
				final BigDecimal _bigDecimal_4 = new BigDecimal(_upperEndpoint_6);
				final BigDecimal _lowerEndpoint_8 = mulRange.lowerEndpoint();
				final BigDecimal tf = _bigDecimal_4.multiply(_lowerEndpoint_8);
				final BigInteger _upperEndpoint_7 = lrVal.upperEndpoint();
				final BigDecimal _bigDecimal_5 = new BigDecimal(_upperEndpoint_7);
				final BigDecimal _upperEndpoint_8 = mulRange.upperEndpoint();
				final BigDecimal tt = _bigDecimal_5.multiply(_upperEndpoint_8);
				final BigDecimal _min = ff.min(ft);
				final BigDecimal _min_1 = _min.min(tf);
				final BigDecimal _min_2 = _min_1.min(tt);
				final BigInteger _bigInteger = _min_2.toBigInteger();
				final BigDecimal _max = ff.max(ft);
				final BigDecimal _max_1 = _max.max(tf);
				final BigDecimal _max_2 = _max_1.max(tt);
				final BigInteger _bigInteger_1 = _max_2.toBigInteger();
				final Range<BigInteger> _createRange_2 = RangeTool.<BigInteger> createRange(_bigInteger, _bigInteger_1);
				return Optional.<Range<BigInteger>> of(_createRange_2);
			case MUL:
				final BigInteger _lowerEndpoint_9 = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_10 = rrVal.lowerEndpoint();
				final BigInteger ff_1 = _lowerEndpoint_9.multiply(_lowerEndpoint_10);
				final BigInteger _lowerEndpoint_11 = lrVal.lowerEndpoint();
				final BigInteger _upperEndpoint_9 = rrVal.upperEndpoint();
				final BigInteger ft_1 = _lowerEndpoint_11.multiply(_upperEndpoint_9);
				final BigInteger _upperEndpoint_10 = lrVal.upperEndpoint();
				final BigInteger _lowerEndpoint_12 = rrVal.lowerEndpoint();
				final BigInteger tf_1 = _upperEndpoint_10.multiply(_lowerEndpoint_12);
				final BigInteger _upperEndpoint_11 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_12 = rrVal.upperEndpoint();
				final BigInteger tt_1 = _upperEndpoint_11.multiply(_upperEndpoint_12);
				final BigInteger _min_3 = ff_1.min(ft_1);
				final BigInteger _min_4 = _min_3.min(tf_1);
				final BigInteger _min_5 = _min_4.min(tt_1);
				final BigInteger _max_3 = ff_1.max(ft_1);
				final BigInteger _max_4 = _max_3.max(tf_1);
				final BigInteger _max_5 = _max_4.max(tt_1);
				final Range<BigInteger> _createRange_3 = RangeTool.<BigInteger> createRange(_min_5, _max_5);
				return Optional.<Range<BigInteger>> of(_createRange_3);
			case MOD:
				final BigInteger rle = rrVal.lowerEndpoint();
				final BigInteger leftBound = rle.min(BigInteger.ZERO);
				final BigInteger _upperEndpoint_13 = rrVal.upperEndpoint();
				final BigInteger rue = _upperEndpoint_13.subtract(BigInteger.ONE);
				final BigInteger rightBound = rue.max(BigInteger.ZERO);
				final Range<BigInteger> _createRange_4 = RangeTool.<BigInteger> createRange(leftBound, rightBound);
				return Optional.<Range<BigInteger>> of(_createRange_4);
			case POW:
				final BigInteger _lowerEndpoint_13 = lrVal.lowerEndpoint();
				final BigInteger _lowerEndpoint_14 = rrVal.lowerEndpoint();
				final int _intValue = _lowerEndpoint_14.intValue();
				final BigInteger ff_2 = _lowerEndpoint_13.pow(_intValue);
				final BigInteger _lowerEndpoint_15 = lrVal.lowerEndpoint();
				final BigInteger _upperEndpoint_14 = rrVal.upperEndpoint();
				final int _intValue_1 = _upperEndpoint_14.intValue();
				final BigInteger ft_2 = _lowerEndpoint_15.pow(_intValue_1);
				final BigInteger _upperEndpoint_15 = lrVal.upperEndpoint();
				final BigInteger _lowerEndpoint_16 = rrVal.lowerEndpoint();
				final int _intValue_2 = _lowerEndpoint_16.intValue();
				final BigInteger tf_2 = _upperEndpoint_15.pow(_intValue_2);
				final BigInteger _upperEndpoint_16 = lrVal.upperEndpoint();
				final BigInteger _upperEndpoint_17 = rrVal.upperEndpoint();
				final int _intValue_3 = _upperEndpoint_17.intValue();
				final BigInteger tt_2 = _upperEndpoint_16.pow(_intValue_3);
				final BigInteger _min_6 = ff_2.min(ft_2);
				final BigInteger _min_7 = _min_6.min(tf_2);
				final BigInteger _min_8 = _min_7.min(tt_2);
				final BigInteger _max_6 = ff_2.max(ft_2);
				final BigInteger _max_7 = _max_6.max(tf_2);
				final BigInteger _max_8 = _max_7.max(tt_2);
				final Range<BigInteger> _createRange_5 = RangeTool.<BigInteger> createRange(_min_8, _max_8);
				return Optional.<Range<BigInteger>> of(_createRange_5);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op");
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLEnumRef obj, final HDLEvaluationContext context) {
		obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
		return Optional.<Range<BigInteger>> absent();
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLManip obj, final HDLEvaluationContext context) {
		final HDLExpression _target = obj.getTarget();
		final Optional<Range<BigInteger>> right = this.determineRange(_target, context);
		final boolean _isPresent = right.isPresent();
		final boolean _not = (!_isPresent);
		if (_not) {
			final HDLManip.HDLManipType _type = obj.getType();
			final boolean _tripleEquals = (_type == HDLManip.HDLManipType.CAST);
			if (_tripleEquals) {
				final Optional<? extends HDLType> newTypeOpt = TypeExtension.typeOf(obj);
				if ((newTypeOpt.isPresent() && (newTypeOpt.get() instanceof HDLPrimitive))) {
					final HDLType _get = newTypeOpt.get();
					HDLPrimitive prim = ((HDLPrimitive) _get);
					final boolean _isAny = prim.isAny();
					if (_isAny) {
						final HDLExpression _target_1 = obj.getTarget();
						final HDLPrimitive _anyCastType = Insulin.anyCastType(prim, _target_1);
						prim = _anyCastType;
					}
					final HDLPrimitives _instance = HDLPrimitives.getInstance();
					return _instance.getValueRange(prim, context);
				}
			}
			return Optional.<Range<BigInteger>> absent();
		}
		final HDLManip.HDLManipType _type_1 = obj.getType();
		if (_type_1 != null) {
			switch (_type_1) {
			case CAST:
				final HDLType type = obj.getCastTo();
				if ((type instanceof HDLPrimitive)) {
					final HDLPrimitives _instance_1 = HDLPrimitives.getInstance();
					final Optional<Range<BigInteger>> castRange = _instance_1.getValueRange(((HDLPrimitive) type), context);
					final HDLPrimitive.HDLPrimitiveType _type_2 = ((HDLPrimitive) type).getType();
					final boolean _equals = Objects.equal(_type_2, HDLPrimitive.HDLPrimitiveType.INTEGER);
					if (_equals) {
						final Range<BigInteger> _intRange = HDLPrimitives.intRange(BigInteger.valueOf(32L));
						final Range<BigInteger> _get_1 = right.get();
						final Range<BigInteger> _intersection = _intRange.intersection(_get_1);
						return Optional.<Range<BigInteger>> of(_intersection);
					}
					final HDLPrimitive.HDLPrimitiveType _type_3 = ((HDLPrimitive) type).getType();
					final boolean _equals_1 = Objects.equal(_type_3, HDLPrimitive.HDLPrimitiveType.NATURAL);
					if (_equals_1) {
						final Range<BigInteger> _uintRange = HDLPrimitives.uintRange(BigInteger.valueOf(32L));
						final Range<BigInteger> _get_2 = right.get();
						final Range<BigInteger> _intersection_1 = _uintRange.intersection(_get_2);
						return Optional.<Range<BigInteger>> of(_intersection_1);
					}
					final HDLPrimitive.HDLPrimitiveType _type_4 = ((HDLPrimitive) type).getType();
					final boolean _equals_2 = Objects.equal(_type_4, HDLPrimitive.HDLPrimitiveType.BIT);
					if (_equals_2) {
						final Range<BigInteger> _createRange = RangeTool.<BigInteger> createRange(BigInteger.ZERO, BigInteger.ONE);
						final Range<BigInteger> _get_3 = right.get();
						final Range<BigInteger> _intersection_2 = _createRange.intersection(_get_3);
						return Optional.<Range<BigInteger>> of(_intersection_2);
					}
					final boolean _isPresent_1 = castRange.isPresent();
					final boolean _not_1 = (!_isPresent_1);
					if (_not_1)
						return Optional.<Range<BigInteger>> absent();
					final Range<BigInteger> _get_4 = castRange.get();
					final Range<BigInteger> _get_5 = right.get();
					final Range<BigInteger> _intersection_3 = _get_4.intersection(_get_5);
					return Optional.<Range<BigInteger>> of(_intersection_3);
				}
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
				return Optional.<Range<BigInteger>> absent();
			case ARITH_NEG:
				final Range<BigInteger> _get_6 = right.get();
				final BigInteger _upperEndpoint = _get_6.upperEndpoint();
				final BigInteger _negate = _upperEndpoint.negate();
				final Range<BigInteger> _get_7 = right.get();
				final BigInteger _lowerEndpoint = _get_7.lowerEndpoint();
				final BigInteger _negate_1 = _lowerEndpoint.negate();
				final Range<BigInteger> _createRange_1 = RangeTool.<BigInteger> createRange(_negate, _negate_1);
				return Optional.<Range<BigInteger>> of(_createRange_1);
			case BIT_NEG:
				final Range<BigInteger> _get_8 = right.get();
				final BigInteger _upperEndpoint_1 = _get_8.upperEndpoint();
				final int _bitLength = _upperEndpoint_1.bitLength();
				final BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
				final BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
				final Range<BigInteger> _createRange_2 = RangeTool.<BigInteger> createRange(BigInteger.ZERO, _subtract);
				return Optional.<Range<BigInteger>> of(_createRange_2);
			case LOGIC_NEG:
				obj.<IHDLObject> addMeta(ProblemDescription.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
				final Range<BigInteger> _createRange_3 = RangeTool.<BigInteger> createRange(BigInteger.ZERO, BigInteger.ONE);
				return Optional.<Range<BigInteger>> of(_createRange_3);
			default:
				break;
			}
		}
		throw new RuntimeException("Incorrectly implemented obj op");
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLFunctionCall obj, final HDLEvaluationContext context) {
		return HDLFunctions.determineRange(obj, context);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLConcat obj, final HDLEvaluationContext context) {
		final Optional<? extends HDLType> type = TypeExtension.typeOf(obj);
		final boolean _isPresent = type.isPresent();
		final boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		final HDLPrimitives _instance = HDLPrimitives.getInstance();
		final HDLType _get = type.get();
		return _instance.getValueRange(((HDLPrimitive) _get), context);
	}

	public Optional<Range<BigInteger>> determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
		if (obj instanceof HDLEnumRef)
			return _determineRange((HDLEnumRef) obj, context);
		else if (obj instanceof HDLVariableRef)
			return _determineRange((HDLVariableRef) obj, context);
		else if (obj instanceof HDLArithOp)
			return _determineRange((HDLArithOp) obj, context);
		else if (obj instanceof HDLBitOp)
			return _determineRange((HDLBitOp) obj, context);
		else if (obj instanceof HDLEqualityOp)
			return _determineRange((HDLEqualityOp) obj, context);
		else if (obj instanceof HDLShiftOp)
			return _determineRange((HDLShiftOp) obj, context);
		else if (obj instanceof HDLUnresolvedFragment)
			return _determineRange((HDLUnresolvedFragment) obj, context);
		else if (obj instanceof HDLConcat)
			return _determineRange((HDLConcat) obj, context);
		else if (obj instanceof HDLFunctionCall)
			return _determineRange((HDLFunctionCall) obj, context);
		else if (obj instanceof HDLLiteral)
			return _determineRange((HDLLiteral) obj, context);
		else if (obj instanceof HDLManip)
			return _determineRange((HDLManip) obj, context);
		else if (obj != null)
			return _determineRange(obj, context);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, context).toString());
	}
}
