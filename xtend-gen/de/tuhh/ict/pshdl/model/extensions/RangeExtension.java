package de.tuhh.ict.pshdl.model.extensions;

import java.math.*;
import java.util.*;

import org.eclipse.xtext.xbase.lib.*;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

import com.google.common.base.*;
import com.google.common.collect.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.types.builtIn.*;

@SuppressWarnings("all")
public class RangeExtension {
	public static RangeExtension INST = new Function0<RangeExtension>() {
		@Override
		public RangeExtension apply() {
			RangeExtension _rangeExtension = new RangeExtension();
			return _rangeExtension;
		}
	}.apply();

	public static Optional<Range<BigInteger>> rangeOf(final IHDLObject obj) {
		return RangeExtension.INST.determineRange(obj, null);
	}

	public static Optional<Range<BigInteger>> rangeOf(final IHDLObject obj, final HDLEvaluationContext context) {
		return RangeExtension.INST.determineRange(obj, context);
	}

	public static GenericMeta<IHDLObject> SOURCE = new Function0<GenericMeta<IHDLObject>>() {
		@Override
		public GenericMeta<IHDLObject> apply() {
			GenericMeta<IHDLObject> _genericMeta = new GenericMeta<IHDLObject>("SOURCE", true);
			return _genericMeta;
		}
	}.apply();

	protected Optional<Range<BigInteger>> _determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
		RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
		throw _runtimeException;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLLiteral obj, final HDLEvaluationContext context) {
		BigInteger _valueAsBigInt = obj.getValueAsBigInt();
		BigInteger _valueAsBigInt_1 = obj.getValueAsBigInt();
		Range<BigInteger> _closed = Ranges.<BigInteger> closed(_valueAsBigInt, _valueAsBigInt_1);
		return Optional.<Range<BigInteger>> of(_closed);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLVariableRef obj, final HDLEvaluationContext context) {
		final Optional<BigInteger> bigVal = ConstantEvaluate.valueOf(obj, context);
		boolean _isPresent = bigVal.isPresent();
		if (_isPresent) {
			BigInteger _get = bigVal.get();
			BigInteger _get_1 = bigVal.get();
			Range<BigInteger> _closed = Ranges.<BigInteger> closed(_get, _get_1);
			return Optional.<Range<BigInteger>> of(_closed);
		}
		final Optional<HDLVariable> hVar = obj.resolveVar();
		boolean _isPresent_1 = hVar.isPresent();
		boolean _not = (!_isPresent_1);
		if (_not) {
			obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
			obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
			return Optional.<Range<BigInteger>> absent();
		}
		HDLVariable _get_2 = hVar.get();
		HDLAnnotation range = _get_2.getAnnotation(HDLBuiltInAnnotations.range);
		boolean _notEquals = ObjectExtensions.operator_notEquals(range, null);
		if (_notEquals) {
			String _value = range.getValue();
			final String[] value = _value.split(";");
			String _get_3 = ((List<String>) Conversions.doWrapArray(value)).get(0);
			BigInteger _bigInteger = new BigInteger(_get_3);
			String _get_4 = ((List<String>) Conversions.doWrapArray(value)).get(1);
			BigInteger _bigInteger_1 = new BigInteger(_get_4);
			Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(_bigInteger, _bigInteger_1);
			return Optional.<Range<BigInteger>> of(_closed_1);
		}
		HDLVariable _get_5 = hVar.get();
		final IHDLObject container = _get_5.getContainer();
		boolean _notEquals_1 = ObjectExtensions.operator_notEquals(container, null);
		if (_notEquals_1) {
			if ((container instanceof HDLVariableDeclaration)) {
				final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) container);
				HDLAnnotation _annotation = hvd.getAnnotation(HDLBuiltInAnnotations.range);
				range = _annotation;
				boolean _notEquals_2 = ObjectExtensions.operator_notEquals(range, null);
				if (_notEquals_2) {
					String _value_1 = range.getValue();
					final String[] value_1 = _value_1.split(";");
					String _get_6 = ((List<String>) Conversions.doWrapArray(value_1)).get(0);
					BigInteger _bigInteger_2 = new BigInteger(_get_6);
					String _get_7 = ((List<String>) Conversions.doWrapArray(value_1)).get(1);
					BigInteger _bigInteger_3 = new BigInteger(_get_7);
					Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(_bigInteger_2, _bigInteger_3);
					return Optional.<Range<BigInteger>> of(_closed_2);
				}
			}
			if ((container instanceof HDLForLoop)) {
				final HDLForLoop loop = ((HDLForLoop) container);
				ArrayList<HDLRange> _range = loop.getRange();
				HDLRange _get_8 = _range.get(0);
				final Optional<Range<BigInteger>> zeroR = this.determineRange(_get_8, context);
				boolean _isPresent_2 = zeroR.isPresent();
				if (_isPresent_2) {
					Range<BigInteger> res = zeroR.get();
					ArrayList<HDLRange> _range_1 = loop.getRange();
					for (final HDLRange r : _range_1) {
						{
							final Optional<Range<BigInteger>> rRange = this.determineRange(r, context);
							boolean _isPresent_3 = rRange.isPresent();
							if (_isPresent_3) {
								Range<BigInteger> _get_9 = rRange.get();
								Range<BigInteger> _span = res.span(_get_9);
								res = _span;
							} else {
								Optional.absent();
							}
						}
					}
					return Optional.<Range<BigInteger>> of(res);
				} else
					return Optional.<Range<BigInteger>> absent();
			}
		}
		ArrayList<HDLRange> _bits = obj.getBits();
		int _size = _bits.size();
		boolean _greaterThan = (_size > 0);
		if (_greaterThan) {
			BigInteger bitWidth = BigInteger.ZERO;
			ArrayList<HDLRange> _bits_1 = obj.getBits();
			for (final HDLRange r_1 : _bits_1) {
				{
					HDLExpression width = r_1.getWidth();
					HDLExpression _copyDeepFrozen = width.copyDeepFrozen(r_1);
					width = _copyDeepFrozen;
					Optional<BigInteger> cw = ConstantEvaluate.valueOf(width, context);
					boolean _isPresent_3 = cw.isPresent();
					boolean _not_1 = (!_isPresent_3);
					if (_not_1) {
						bitWidth = null;
					} else {
						BigInteger _get_9 = cw.get();
						BigInteger _add = bitWidth.add(_get_9);
						bitWidth = _add;
					}
				}
			}
			boolean _notEquals_3 = ObjectExtensions.operator_notEquals(bitWidth, null);
			if (_notEquals_3) {
				int _intValue = bitWidth.intValue();
				BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
				BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
				Range<BigInteger> _closed_3 = Ranges.<BigInteger> closed(BigInteger.ZERO, _subtract);
				return Optional.<Range<BigInteger>> of(_closed_3);
			}
		}
		HDLVariable _get_9 = hVar.get();
		final Optional<? extends HDLType> type = TypeExtension.typeOf(_get_9);
		boolean _and = false;
		boolean _isPresent_3 = type.isPresent();
		if (!_isPresent_3) {
			_and = false;
		} else {
			HDLType _get_10 = type.get();
			_and = (_isPresent_3 && (_get_10 instanceof HDLPrimitive));
		}
		if (_and) {
			HDLPrimitives _instance = HDLPrimitives.getInstance();
			HDLType _get_11 = type.get();
			return _instance.getValueRange(((HDLPrimitive) _get_11), context);
		}
		obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
		return Optional.<Range<BigInteger>> absent();
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLRange obj, final HDLEvaluationContext context) {
		HDLExpression _to = obj.getTo();
		final Optional<BigInteger> to = ConstantEvaluate.valueOf(_to, context);
		boolean _isPresent = to.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLExpression _from = obj.getFrom();
		boolean _notEquals = ObjectExtensions.operator_notEquals(_from, null);
		if (_notEquals) {
			HDLExpression _from_1 = obj.getFrom();
			final Optional<BigInteger> from = ConstantEvaluate.valueOf(_from_1, context);
			boolean _isPresent_1 = from.isPresent();
			boolean _not_1 = (!_isPresent_1);
			if (_not_1)
				return Optional.<Range<BigInteger>> absent();
			BigInteger _get = from.get();
			BigInteger _get_1 = to.get();
			int _compareTo = _get.compareTo(_get_1);
			boolean _greaterThan = (_compareTo > 0);
			if (_greaterThan) {
				BigInteger _get_2 = to.get();
				BigInteger _get_3 = from.get();
				Range<BigInteger> _closed = Ranges.<BigInteger> closed(_get_2, _get_3);
				return Optional.<Range<BigInteger>> of(_closed);
			}
			BigInteger _get_4 = from.get();
			BigInteger _get_5 = to.get();
			Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(_get_4, _get_5);
			return Optional.<Range<BigInteger>> of(_closed_1);
		}
		BigInteger _get_6 = to.get();
		BigInteger _get_7 = to.get();
		Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(_get_6, _get_7);
		return Optional.<Range<BigInteger>> of(_closed_2);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLEqualityOp obj, final HDLEvaluationContext context) {
		obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
		Range<BigInteger> _closed = Ranges.<BigInteger> closed(BigInteger.ZERO, BigInteger.ONE);
		return Optional.<Range<BigInteger>> of(_closed);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLShiftOp obj, final HDLEvaluationContext context) {
		HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		boolean _isPresent = leftRange.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		boolean _isPresent_1 = rightRange.isPresent();
		boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		HDLShiftOpType _type = obj.getType();
		final HDLShiftOpType _switchValue = _type;
		boolean _matched = false;
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLShiftOpType.SLL)) {
				_matched = true;
				Range<BigInteger> _get = leftRange.get();
				BigInteger _lowerEndpoint = _get.lowerEndpoint();
				Range<BigInteger> _get_1 = rightRange.get();
				BigInteger _lowerEndpoint_1 = _get_1.lowerEndpoint();
				int _intValue = _lowerEndpoint_1.intValue();
				final BigInteger ff = _lowerEndpoint.shiftLeft(_intValue);
				Range<BigInteger> _get_2 = leftRange.get();
				BigInteger _lowerEndpoint_2 = _get_2.lowerEndpoint();
				Range<BigInteger> _get_3 = rightRange.get();
				BigInteger _upperEndpoint = _get_3.upperEndpoint();
				int _intValue_1 = _upperEndpoint.intValue();
				final BigInteger ft = _lowerEndpoint_2.shiftLeft(_intValue_1);
				Range<BigInteger> _get_4 = leftRange.get();
				BigInteger _upperEndpoint_1 = _get_4.upperEndpoint();
				Range<BigInteger> _get_5 = rightRange.get();
				BigInteger _lowerEndpoint_3 = _get_5.lowerEndpoint();
				int _intValue_2 = _lowerEndpoint_3.intValue();
				final BigInteger tf = _upperEndpoint_1.shiftLeft(_intValue_2);
				Range<BigInteger> _get_6 = leftRange.get();
				BigInteger _upperEndpoint_2 = _get_6.upperEndpoint();
				Range<BigInteger> _get_7 = rightRange.get();
				BigInteger _upperEndpoint_3 = _get_7.upperEndpoint();
				int _intValue_3 = _upperEndpoint_3.intValue();
				final BigInteger tt = _upperEndpoint_2.shiftLeft(_intValue_3);
				BigInteger _min = ff.min(ft);
				BigInteger _min_1 = _min.min(tf);
				BigInteger _min_2 = _min_1.min(tt);
				BigInteger _max = ff.max(ft);
				BigInteger _max_1 = _max.max(tf);
				BigInteger _max_2 = _max_1.max(tt);
				Range<BigInteger> _closed = Ranges.<BigInteger> closed(_min_2, _max_2);
				return Optional.<Range<BigInteger>> of(_closed);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLShiftOpType.SRA)) {
				_matched = true;
				Range<BigInteger> _get_8 = leftRange.get();
				BigInteger _lowerEndpoint_4 = _get_8.lowerEndpoint();
				Range<BigInteger> _get_9 = rightRange.get();
				BigInteger _lowerEndpoint_5 = _get_9.lowerEndpoint();
				int _intValue_4 = _lowerEndpoint_5.intValue();
				final BigInteger ff_1 = _lowerEndpoint_4.shiftRight(_intValue_4);
				Range<BigInteger> _get_10 = leftRange.get();
				BigInteger _lowerEndpoint_6 = _get_10.lowerEndpoint();
				Range<BigInteger> _get_11 = rightRange.get();
				BigInteger _upperEndpoint_4 = _get_11.upperEndpoint();
				int _intValue_5 = _upperEndpoint_4.intValue();
				final BigInteger ft_1 = _lowerEndpoint_6.shiftRight(_intValue_5);
				Range<BigInteger> _get_12 = leftRange.get();
				BigInteger _upperEndpoint_5 = _get_12.upperEndpoint();
				Range<BigInteger> _get_13 = rightRange.get();
				BigInteger _lowerEndpoint_7 = _get_13.lowerEndpoint();
				int _intValue_6 = _lowerEndpoint_7.intValue();
				final BigInteger tf_1 = _upperEndpoint_5.shiftRight(_intValue_6);
				Range<BigInteger> _get_14 = leftRange.get();
				BigInteger _upperEndpoint_6 = _get_14.upperEndpoint();
				Range<BigInteger> _get_15 = rightRange.get();
				BigInteger _upperEndpoint_7 = _get_15.upperEndpoint();
				int _intValue_7 = _upperEndpoint_7.intValue();
				final BigInteger tt_1 = _upperEndpoint_6.shiftRight(_intValue_7);
				BigInteger _min_3 = ff_1.min(ft_1);
				BigInteger _min_4 = _min_3.min(tf_1);
				BigInteger _min_5 = _min_4.min(tt_1);
				BigInteger _max_3 = ff_1.max(ft_1);
				BigInteger _max_4 = _max_3.max(tf_1);
				BigInteger _max_5 = _max_4.max(tt_1);
				Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(_min_5, _max_5);
				return Optional.<Range<BigInteger>> of(_closed_1);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLShiftOpType.SRL)) {
				_matched = true;
				Range<BigInteger> _get_16 = leftRange.get();
				BigInteger _lowerEndpoint_8 = _get_16.lowerEndpoint();
				Range<BigInteger> _get_17 = rightRange.get();
				BigInteger _lowerEndpoint_9 = _get_17.lowerEndpoint();
				final BigInteger ff_2 = RangeExtension.srl(_lowerEndpoint_8, _lowerEndpoint_9);
				Range<BigInteger> _get_18 = leftRange.get();
				BigInteger _lowerEndpoint_10 = _get_18.lowerEndpoint();
				Range<BigInteger> _get_19 = rightRange.get();
				BigInteger _upperEndpoint_8 = _get_19.upperEndpoint();
				final BigInteger ft_2 = RangeExtension.srl(_lowerEndpoint_10, _upperEndpoint_8);
				Range<BigInteger> _get_20 = leftRange.get();
				BigInteger _upperEndpoint_9 = _get_20.upperEndpoint();
				Range<BigInteger> _get_21 = rightRange.get();
				BigInteger _lowerEndpoint_11 = _get_21.lowerEndpoint();
				final BigInteger tf_2 = RangeExtension.srl(_upperEndpoint_9, _lowerEndpoint_11);
				Range<BigInteger> _get_22 = leftRange.get();
				BigInteger _upperEndpoint_10 = _get_22.upperEndpoint();
				Range<BigInteger> _get_23 = rightRange.get();
				BigInteger _upperEndpoint_11 = _get_23.upperEndpoint();
				final BigInteger tt_2 = RangeExtension.srl(_upperEndpoint_10, _upperEndpoint_11);
				BigInteger _min_6 = ff_2.min(ft_2);
				BigInteger _min_7 = _min_6.min(tf_2);
				BigInteger _min_8 = _min_7.min(tt_2);
				BigInteger _max_6 = ff_2.max(ft_2);
				BigInteger _max_7 = _max_6.max(tf_2);
				BigInteger _max_8 = _max_7.max(tt_2);
				Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(_min_8, _max_8);
				return Optional.<Range<BigInteger>> of(_closed_2);
			}
		}
		RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
		throw _runtimeException;
	}

	private static BigInteger srl(final BigInteger a, final BigInteger b) {
		int _intValue = b.intValue();
		final BigInteger res = a.shiftRight(_intValue);
		int _signum = res.signum();
		boolean _lessThan = (_signum < 0);
		if (_lessThan)
			return res.negate();
		return res;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLBitOp obj, final HDLEvaluationContext context) {
		HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		boolean _isPresent = leftRange.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		boolean _isPresent_1 = rightRange.isPresent();
		boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		HDLBitOpType _type = obj.getType();
		final HDLBitOpType type = _type;
		boolean _matched = false;
		if (!_matched) {
			boolean _or = false;
			boolean _equals = ObjectExtensions.operator_equals(type, HDLBitOpType.OR);
			if (_equals) {
				_or = true;
			} else {
				boolean _equals_1 = ObjectExtensions.operator_equals(type, HDLBitOpType.XOR);
				_or = (_equals || _equals_1);
			}
			if (_or) {
				_matched = true;
				obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
				Range<BigInteger> _get = leftRange.get();
				BigInteger _upperEndpoint = _get.upperEndpoint();
				int _bitLength = _upperEndpoint.bitLength();
				BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
				BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
				Range<BigInteger> _closed = Ranges.<BigInteger> closed(BigInteger.ZERO, _subtract);
				return Optional.<Range<BigInteger>> of(_closed);
			}
		}
		if (!_matched) {
			if (Objects.equal(type, HDLBitOpType.AND)) {
				_matched = true;
				obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
				Range<BigInteger> _get_1 = leftRange.get();
				BigInteger _upperEndpoint_1 = _get_1.upperEndpoint();
				Range<BigInteger> _get_2 = rightRange.get();
				BigInteger _upperEndpoint_2 = _get_2.upperEndpoint();
				int _bitLength_1 = _upperEndpoint_2.bitLength();
				BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(_bitLength_1);
				BigInteger _subtract_1 = _shiftLeft_1.subtract(BigInteger.ONE);
				BigInteger _min = _upperEndpoint_1.min(_subtract_1);
				Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(BigInteger.ZERO, _min);
				return Optional.<Range<BigInteger>> of(_closed_1);
			}
		}
		if (!_matched) {
			boolean _or_1 = false;
			boolean _equals_2 = ObjectExtensions.operator_equals(type, HDLBitOpType.LOGI_AND);
			if (_equals_2) {
				_or_1 = true;
			} else {
				boolean _equals_3 = ObjectExtensions.operator_equals(type, HDLBitOpType.LOGI_OR);
				_or_1 = (_equals_2 || _equals_3);
			}
			if (_or_1) {
				_matched = true;
				obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
				Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(BigInteger.ZERO, BigInteger.ONE);
				return Optional.<Range<BigInteger>> of(_closed_2);
			}
		}
		RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
		throw _runtimeException;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLArithOp obj, final HDLEvaluationContext context) {
		HDLExpression _left = obj.getLeft();
		final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
		boolean _isPresent = leftRange.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLExpression _right = obj.getRight();
		final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
		boolean _isPresent_1 = rightRange.isPresent();
		boolean _not_1 = (!_isPresent_1);
		if (_not_1)
			return Optional.<Range<BigInteger>> absent();
		HDLArithOpType _type = obj.getType();
		final HDLArithOpType _switchValue = _type;
		boolean _matched = false;
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.PLUS)) {
				_matched = true;
				Range<BigInteger> _get = leftRange.get();
				BigInteger _lowerEndpoint = _get.lowerEndpoint();
				Range<BigInteger> _get_1 = rightRange.get();
				BigInteger _lowerEndpoint_1 = _get_1.lowerEndpoint();
				BigInteger _add = _lowerEndpoint.add(_lowerEndpoint_1);
				Range<BigInteger> _get_2 = leftRange.get();
				BigInteger _upperEndpoint = _get_2.upperEndpoint();
				Range<BigInteger> _get_3 = rightRange.get();
				BigInteger _upperEndpoint_1 = _get_3.upperEndpoint();
				BigInteger _add_1 = _upperEndpoint.add(_upperEndpoint_1);
				Range<BigInteger> _closed = Ranges.<BigInteger> closed(_add, _add_1);
				return Optional.<Range<BigInteger>> of(_closed);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.MINUS)) {
				_matched = true;
				Range<BigInteger> _get_4 = leftRange.get();
				BigInteger _lowerEndpoint_2 = _get_4.lowerEndpoint();
				Range<BigInteger> _get_5 = rightRange.get();
				BigInteger _lowerEndpoint_3 = _get_5.lowerEndpoint();
				BigInteger _subtract = _lowerEndpoint_2.subtract(_lowerEndpoint_3);
				Range<BigInteger> _get_6 = leftRange.get();
				BigInteger _upperEndpoint_2 = _get_6.upperEndpoint();
				Range<BigInteger> _get_7 = rightRange.get();
				BigInteger _upperEndpoint_3 = _get_7.upperEndpoint();
				BigInteger _subtract_1 = _upperEndpoint_2.subtract(_upperEndpoint_3);
				Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(_subtract, _subtract_1);
				return Optional.<Range<BigInteger>> of(_closed_1);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.DIV)) {
				_matched = true;
				boolean _or = false;
				Range<BigInteger> _get_8 = rightRange.get();
				BigInteger _lowerEndpoint_4 = _get_8.lowerEndpoint();
				boolean _equals = _lowerEndpoint_4.equals(BigInteger.ZERO);
				if (_equals) {
					_or = true;
				} else {
					Range<BigInteger> _get_9 = rightRange.get();
					BigInteger _upperEndpoint_4 = _get_9.upperEndpoint();
					boolean _equals_1 = _upperEndpoint_4.equals(BigInteger.ZERO);
					_or = (_equals || _equals_1);
				}
				if (_or) {
					obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
					return Optional.<Range<BigInteger>> absent();
				}
				boolean _or_1 = false;
				Range<BigInteger> _get_10 = rightRange.get();
				BigInteger _lowerEndpoint_5 = _get_10.lowerEndpoint();
				int _signum = _lowerEndpoint_5.signum();
				Range<BigInteger> _get_11 = rightRange.get();
				BigInteger _upperEndpoint_5 = _get_11.upperEndpoint();
				int _signum_1 = _upperEndpoint_5.signum();
				int _multiply = (_signum * _signum_1);
				boolean _lessThan = (_multiply < 0);
				if (_lessThan) {
					_or_1 = true;
				} else {
					Range<BigInteger> _get_12 = rightRange.get();
					BigInteger _upperEndpoint_6 = _get_12.upperEndpoint();
					int _signum_2 = _upperEndpoint_6.signum();
					boolean _equals_2 = (_signum_2 == 0);
					_or_1 = (_lessThan || _equals_2);
				}
				if (_or_1) {
					obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
					obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
				}
				Range<BigInteger> _get_13 = rightRange.get();
				BigInteger _lowerEndpoint_6 = _get_13.lowerEndpoint();
				BigDecimal _bigDecimal = new BigDecimal(_lowerEndpoint_6);
				BigDecimal _divide = BigDecimal.ONE.divide(_bigDecimal);
				Range<BigInteger> _get_14 = rightRange.get();
				BigInteger _upperEndpoint_7 = _get_14.upperEndpoint();
				BigDecimal _bigDecimal_1 = new BigDecimal(_upperEndpoint_7);
				BigDecimal _divide_1 = BigDecimal.ONE.divide(_bigDecimal_1);
				final Range<BigDecimal> mulRange = Ranges.<BigDecimal> closed(_divide, _divide_1);
				Range<BigInteger> _get_15 = leftRange.get();
				BigInteger _lowerEndpoint_7 = _get_15.lowerEndpoint();
				BigDecimal _bigDecimal_2 = new BigDecimal(_lowerEndpoint_7);
				BigDecimal _lowerEndpoint_8 = mulRange.lowerEndpoint();
				final BigDecimal ff = _bigDecimal_2.multiply(_lowerEndpoint_8);
				Range<BigInteger> _get_16 = leftRange.get();
				BigInteger _lowerEndpoint_9 = _get_16.lowerEndpoint();
				BigDecimal _bigDecimal_3 = new BigDecimal(_lowerEndpoint_9);
				BigDecimal _upperEndpoint_8 = mulRange.upperEndpoint();
				final BigDecimal ft = _bigDecimal_3.multiply(_upperEndpoint_8);
				Range<BigInteger> _get_17 = leftRange.get();
				BigInteger _upperEndpoint_9 = _get_17.upperEndpoint();
				BigDecimal _bigDecimal_4 = new BigDecimal(_upperEndpoint_9);
				BigDecimal _lowerEndpoint_10 = mulRange.lowerEndpoint();
				final BigDecimal tf = _bigDecimal_4.multiply(_lowerEndpoint_10);
				Range<BigInteger> _get_18 = leftRange.get();
				BigInteger _upperEndpoint_10 = _get_18.upperEndpoint();
				BigDecimal _bigDecimal_5 = new BigDecimal(_upperEndpoint_10);
				BigDecimal _upperEndpoint_11 = mulRange.upperEndpoint();
				final BigDecimal tt = _bigDecimal_5.multiply(_upperEndpoint_11);
				BigDecimal _min = ff.min(ft);
				BigDecimal _min_1 = _min.min(tf);
				BigDecimal _min_2 = _min_1.min(tt);
				BigInteger _bigInteger = _min_2.toBigInteger();
				BigDecimal _max = ff.max(ft);
				BigDecimal _max_1 = _max.max(tf);
				BigDecimal _max_2 = _max_1.max(tt);
				BigInteger _bigInteger_1 = _max_2.toBigInteger();
				Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(_bigInteger, _bigInteger_1);
				return Optional.<Range<BigInteger>> of(_closed_2);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.MUL)) {
				_matched = true;
				Range<BigInteger> _get_19 = leftRange.get();
				BigInteger _lowerEndpoint_11 = _get_19.lowerEndpoint();
				Range<BigInteger> _get_20 = rightRange.get();
				BigInteger _lowerEndpoint_12 = _get_20.lowerEndpoint();
				final BigInteger ff_1 = _lowerEndpoint_11.multiply(_lowerEndpoint_12);
				Range<BigInteger> _get_21 = leftRange.get();
				BigInteger _lowerEndpoint_13 = _get_21.lowerEndpoint();
				Range<BigInteger> _get_22 = rightRange.get();
				BigInteger _upperEndpoint_12 = _get_22.upperEndpoint();
				final BigInteger ft_1 = _lowerEndpoint_13.multiply(_upperEndpoint_12);
				Range<BigInteger> _get_23 = leftRange.get();
				BigInteger _upperEndpoint_13 = _get_23.upperEndpoint();
				Range<BigInteger> _get_24 = rightRange.get();
				BigInteger _lowerEndpoint_14 = _get_24.lowerEndpoint();
				final BigInteger tf_1 = _upperEndpoint_13.multiply(_lowerEndpoint_14);
				Range<BigInteger> _get_25 = leftRange.get();
				BigInteger _upperEndpoint_14 = _get_25.upperEndpoint();
				Range<BigInteger> _get_26 = rightRange.get();
				BigInteger _upperEndpoint_15 = _get_26.upperEndpoint();
				final BigInteger tt_1 = _upperEndpoint_14.multiply(_upperEndpoint_15);
				BigInteger _min_3 = ff_1.min(ft_1);
				BigInteger _min_4 = _min_3.min(tf_1);
				BigInteger _min_5 = _min_4.min(tt_1);
				BigInteger _max_3 = ff_1.max(ft_1);
				BigInteger _max_4 = _max_3.max(tf_1);
				BigInteger _max_5 = _max_4.max(tt_1);
				Range<BigInteger> _closed_3 = Ranges.<BigInteger> closed(_min_5, _max_5);
				return Optional.<Range<BigInteger>> of(_closed_3);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.MOD)) {
				_matched = true;
				Range<BigInteger> _get_27 = rightRange.get();
				BigInteger _upperEndpoint_16 = _get_27.upperEndpoint();
				BigInteger _subtract_2 = _upperEndpoint_16.subtract(BigInteger.ONE);
				Range<BigInteger> _get_28 = leftRange.get();
				BigInteger _upperEndpoint_17 = _get_28.upperEndpoint();
				BigInteger _min_6 = _subtract_2.min(_upperEndpoint_17);
				Range<BigInteger> _closed_4 = Ranges.<BigInteger> closed(BigInteger.ZERO, _min_6);
				return Optional.<Range<BigInteger>> of(_closed_4);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLArithOpType.POW)) {
				_matched = true;
				Range<BigInteger> _get_29 = leftRange.get();
				BigInteger _lowerEndpoint_15 = _get_29.lowerEndpoint();
				Range<BigInteger> _get_30 = rightRange.get();
				BigInteger _lowerEndpoint_16 = _get_30.lowerEndpoint();
				int _intValue = _lowerEndpoint_16.intValue();
				final BigInteger ff_2 = _lowerEndpoint_15.pow(_intValue);
				Range<BigInteger> _get_31 = leftRange.get();
				BigInteger _lowerEndpoint_17 = _get_31.lowerEndpoint();
				Range<BigInteger> _get_32 = rightRange.get();
				BigInteger _upperEndpoint_18 = _get_32.upperEndpoint();
				int _intValue_1 = _upperEndpoint_18.intValue();
				final BigInteger ft_2 = _lowerEndpoint_17.pow(_intValue_1);
				Range<BigInteger> _get_33 = leftRange.get();
				BigInteger _upperEndpoint_19 = _get_33.upperEndpoint();
				Range<BigInteger> _get_34 = rightRange.get();
				BigInteger _lowerEndpoint_18 = _get_34.lowerEndpoint();
				int _intValue_2 = _lowerEndpoint_18.intValue();
				final BigInteger tf_2 = _upperEndpoint_19.pow(_intValue_2);
				Range<BigInteger> _get_35 = leftRange.get();
				BigInteger _upperEndpoint_20 = _get_35.upperEndpoint();
				Range<BigInteger> _get_36 = rightRange.get();
				BigInteger _upperEndpoint_21 = _get_36.upperEndpoint();
				int _intValue_3 = _upperEndpoint_21.intValue();
				final BigInteger tt_2 = _upperEndpoint_20.pow(_intValue_3);
				BigInteger _min_7 = ff_2.min(ft_2);
				BigInteger _min_8 = _min_7.min(tf_2);
				BigInteger _min_9 = _min_8.min(tt_2);
				BigInteger _max_6 = ff_2.max(ft_2);
				BigInteger _max_7 = _max_6.max(tf_2);
				BigInteger _max_8 = _max_7.max(tt_2);
				Range<BigInteger> _closed_5 = Ranges.<BigInteger> closed(_min_9, _max_8);
				return Optional.<Range<BigInteger>> of(_closed_5);
			}
		}
		RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
		throw _runtimeException;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLEnumRef obj, final HDLEvaluationContext context) {
		obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
		obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
		return Optional.<Range<BigInteger>> absent();
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLManip obj, final HDLEvaluationContext context) {
		HDLExpression _target = obj.getTarget();
		final Optional<Range<BigInteger>> right = this.determineRange(_target, context);
		boolean _isPresent = right.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLManipType _type = obj.getType();
		final HDLManipType _switchValue = _type;
		boolean _matched = false;
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLManipType.CAST)) {
				_matched = true;
				final HDLType type = obj.getCastTo();
				if ((type instanceof HDLPrimitive)) {
					HDLPrimitives _instance = HDLPrimitives.getInstance();
					final Optional<Range<BigInteger>> castRange = _instance.getValueRange(((HDLPrimitive) type), context);
					boolean _isPresent_1 = castRange.isPresent();
					boolean _not_1 = (!_isPresent_1);
					if (_not_1)
						return Optional.<Range<BigInteger>> absent();
					boolean _isPresent_2 = right.isPresent();
					boolean _not_2 = (!_isPresent_2);
					if (_not_2)
						return Optional.<Range<BigInteger>> absent();
					Range<BigInteger> _get = castRange.get();
					Range<BigInteger> _get_1 = right.get();
					Range<BigInteger> _intersection = _get.intersection(_get_1);
					return Optional.<Range<BigInteger>> of(_intersection);
				}
				obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
				return Optional.<Range<BigInteger>> absent();
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLManipType.ARITH_NEG)) {
				_matched = true;
				Range<BigInteger> _get_2 = right.get();
				BigInteger _upperEndpoint = _get_2.upperEndpoint();
				BigInteger _negate = _upperEndpoint.negate();
				Range<BigInteger> _get_3 = right.get();
				BigInteger _lowerEndpoint = _get_3.lowerEndpoint();
				BigInteger _negate_1 = _lowerEndpoint.negate();
				Range<BigInteger> _closed = Ranges.<BigInteger> closed(_negate, _negate_1);
				return Optional.<Range<BigInteger>> of(_closed);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLManipType.BIT_NEG)) {
				_matched = true;
				Range<BigInteger> _get_4 = right.get();
				BigInteger _upperEndpoint_1 = _get_4.upperEndpoint();
				int _bitLength = _upperEndpoint_1.bitLength();
				BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
				BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
				Range<BigInteger> _closed_1 = Ranges.<BigInteger> closed(BigInteger.ZERO, _subtract);
				return Optional.<Range<BigInteger>> of(_closed_1);
			}
		}
		if (!_matched) {
			if (Objects.equal(_switchValue, HDLManipType.LOGIC_NEG)) {
				_matched = true;
				obj.<IHDLObject> addMeta(RangeExtension.SOURCE, obj);
				obj.<ProblemDescription> addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
				Range<BigInteger> _closed_2 = Ranges.<BigInteger> closed(BigInteger.ZERO, BigInteger.ONE);
				return Optional.<Range<BigInteger>> of(_closed_2);
			}
		}
		RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
		throw _runtimeException;
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLFunctionCall obj, final HDLEvaluationContext context) {
		return HDLFunctions.determineRange(obj, context);
	}

	protected Optional<Range<BigInteger>> _determineRange(final HDLConcat obj, final HDLEvaluationContext context) {
		final Optional<? extends HDLType> type = TypeExtension.typeOf(obj);
		boolean _isPresent = type.isPresent();
		boolean _not = (!_isPresent);
		if (_not)
			return Optional.<Range<BigInteger>> absent();
		HDLPrimitives _instance = HDLPrimitives.getInstance();
		HDLType _get = type.get();
		return _instance.getValueRange(((HDLPrimitive) _get), context);
	}

	public Optional<Range<BigInteger>> determineRange(final IHDLObject obj, final HDLEvaluationContext context) {
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
		else if (obj instanceof HDLConcat)
			return _determineRange((HDLConcat) obj, context);
		else if (obj instanceof HDLFunctionCall)
			return _determineRange((HDLFunctionCall) obj, context);
		else if (obj instanceof HDLLiteral)
			return _determineRange((HDLLiteral) obj, context);
		else if (obj instanceof HDLManip)
			return _determineRange((HDLManip) obj, context);
		else if (obj instanceof HDLRange)
			return _determineRange((HDLRange) obj, context);
		else if (obj instanceof HDLExpression)
			return _determineRange((HDLExpression) obj, context);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, context).toString());
	}
}
