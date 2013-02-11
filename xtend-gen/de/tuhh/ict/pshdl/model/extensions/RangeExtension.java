package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import de.tuhh.ict.pshdl.model.HDLAnnotation;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLEnumRef;
import de.tuhh.ict.pshdl.model.HDLEqualityOp;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext;
import de.tuhh.ict.pshdl.model.extensions.ConstantEvaluate;
import de.tuhh.ict.pshdl.model.extensions.ProblemDescription;
import de.tuhh.ict.pshdl.model.extensions.TypeExtension;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives;
import de.tuhh.ict.pshdl.model.utils.services.IHDLPrimitive;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class RangeExtension {
  public static RangeExtension INST = new Function0<RangeExtension>() {
    public RangeExtension apply() {
      RangeExtension _rangeExtension = new RangeExtension();
      return _rangeExtension;
    }
  }.apply();
  
  public static Range<BigInteger> rangeOf(final IHDLObject obj) {
    return RangeExtension.INST.determineRange(obj, null);
  }
  
  public static Range<BigInteger> rangeOf(final IHDLObject obj, final HDLEvaluationContext context) {
    return RangeExtension.INST.determineRange(obj, context);
  }
  
  public static GenericMeta<IHDLObject> SOURCE = new Function0<GenericMeta<IHDLObject>>() {
    public GenericMeta<IHDLObject> apply() {
      GenericMeta<IHDLObject> _genericMeta = new GenericMeta<IHDLObject>("SOURCE", true);
      return _genericMeta;
    }
  }.apply();
  
  protected Range<BigInteger> _determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
    throw _runtimeException;
  }
  
  protected Range<BigInteger> _determineRange(final HDLLiteral obj, final HDLEvaluationContext context) {
    BigInteger _valueAsBigInt = obj.getValueAsBigInt();
    BigInteger _valueAsBigInt_1 = obj.getValueAsBigInt();
    return Ranges.<BigInteger>closed(_valueAsBigInt, _valueAsBigInt_1);
  }
  
  protected Range<BigInteger> _determineRange(final HDLVariableRef obj, final HDLEvaluationContext context) {
    final BigInteger bigVal = ConstantEvaluate.valueOf(obj, context);
    boolean _notEquals = (!Objects.equal(bigVal, null));
    if (_notEquals) {
      return Ranges.<BigInteger>closed(bigVal, bigVal);
    }
    final HDLVariable hVar = obj.resolveVar();
    boolean _equals = Objects.equal(hVar, null);
    if (_equals) {
      obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return null;
    }
    HDLAnnotation range = hVar.getAnnotation(HDLBuiltInAnnotations.range);
    boolean _notEquals_1 = (!Objects.equal(range, null));
    if (_notEquals_1) {
      String _value = range.getValue();
      final String[] value = _value.split(";");
      String _get = ((List<String>)Conversions.doWrapArray(value)).get(0);
      BigInteger _bigInteger = new BigInteger(_get);
      String _get_1 = ((List<String>)Conversions.doWrapArray(value)).get(1);
      BigInteger _bigInteger_1 = new BigInteger(_get_1);
      return Ranges.<BigInteger>closed(_bigInteger, _bigInteger_1);
    }
    IHDLObject _container = hVar.getContainer();
    boolean _notEquals_2 = (!Objects.equal(_container, null));
    if (_notEquals_2) {
      IHDLObject _container_1 = hVar.getContainer();
      if ((_container_1 instanceof HDLVariableDeclaration)) {
        IHDLObject _container_2 = hVar.getContainer();
        final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) _container_2);
        HDLAnnotation _annotation = hvd.getAnnotation(HDLBuiltInAnnotations.range);
        range = _annotation;
        boolean _notEquals_3 = (!Objects.equal(range, null));
        if (_notEquals_3) {
          String _value_1 = range.getValue();
          final String[] value_1 = _value_1.split(";");
          String _get_2 = ((List<String>)Conversions.doWrapArray(value_1)).get(0);
          BigInteger _bigInteger_2 = new BigInteger(_get_2);
          String _get_3 = ((List<String>)Conversions.doWrapArray(value_1)).get(1);
          BigInteger _bigInteger_3 = new BigInteger(_get_3);
          return Ranges.<BigInteger>closed(_bigInteger_2, _bigInteger_3);
        }
      }
      IHDLObject _container_3 = hVar.getContainer();
      if ((_container_3 instanceof HDLForLoop)) {
        IHDLObject _container_4 = hVar.getContainer();
        final HDLForLoop loop = ((HDLForLoop) _container_4);
        ArrayList<HDLRange> _range = loop.getRange();
        HDLRange _get_4 = _range.get(0);
        Range<BigInteger> res = this.determineRange(_get_4, context);
        ArrayList<HDLRange> _range_1 = loop.getRange();
        for (final HDLRange r : _range_1) {
          Range<BigInteger> _determineRange = this.determineRange(r, context);
          Range<BigInteger> _span = res.span(_determineRange);
          res = _span;
        }
        return res;
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
          BigInteger cw = ConstantEvaluate.valueOf(width, context);
          boolean _equals_1 = Objects.equal(cw, null);
          if (_equals_1) {
            bitWidth = null;
          }
          BigInteger _add = bitWidth.add(cw);
          bitWidth = _add;
        }
      }
      boolean _notEquals_4 = (!Objects.equal(bitWidth, null));
      if (_notEquals_4) {
        int _intValue = bitWidth.intValue();
        BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
        BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, _subtract);
      }
    }
    final HDLType type = TypeExtension.typeOf(hVar);
    if ((type instanceof HDLPrimitive)) {
      IHDLPrimitive _instance = HDLPrimitives.getInstance();
      return _instance.getValueRange(((HDLPrimitive) type), context);
    }
    obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
    return null;
  }
  
  protected Range<BigInteger> _determineRange(final HDLRange obj, final HDLEvaluationContext context) {
    HDLExpression _to = obj.getTo();
    final BigInteger to = ConstantEvaluate.valueOf(_to, context);
    HDLExpression _from = obj.getFrom();
    boolean _notEquals = (!Objects.equal(_from, null));
    if (_notEquals) {
      HDLExpression _from_1 = obj.getFrom();
      final BigInteger from = ConstantEvaluate.valueOf(_from_1, context);
      int _compareTo = from.compareTo(to);
      boolean _greaterThan = (_compareTo > 0);
      if (_greaterThan) {
        return Ranges.<BigInteger>closed(to, from);
      }
      return Ranges.<BigInteger>closed(from, to);
    }
    return Ranges.<BigInteger>closed(to, to);
  }
  
  protected Range<BigInteger> _determineRange(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
    return Ranges.<BigInteger>closed(BigInteger.ZERO, BigInteger.ONE);
  }
  
  protected Range<BigInteger> _determineRange(final HDLShiftOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Range<BigInteger> leftRange = this.determineRange(_left, context);
    HDLExpression _right = obj.getRight();
    final Range<BigInteger> rightRange = this.determineRange(_right, context);
    HDLShiftOpType _type = obj.getType();
    final HDLShiftOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SLL)) {
        _matched=true;
        BigInteger _lowerEndpoint = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_1 = rightRange.lowerEndpoint();
        int _intValue = _lowerEndpoint_1.intValue();
        final BigInteger ff = _lowerEndpoint.shiftLeft(_intValue);
        BigInteger _lowerEndpoint_2 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint = rightRange.upperEndpoint();
        int _intValue_1 = _upperEndpoint.intValue();
        final BigInteger ft = _lowerEndpoint_2.shiftLeft(_intValue_1);
        BigInteger _upperEndpoint_1 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_3 = rightRange.lowerEndpoint();
        int _intValue_2 = _lowerEndpoint_3.intValue();
        final BigInteger tf = _upperEndpoint_1.shiftLeft(_intValue_2);
        BigInteger _upperEndpoint_2 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_3 = rightRange.upperEndpoint();
        int _intValue_3 = _upperEndpoint_3.intValue();
        final BigInteger tt = _upperEndpoint_2.shiftLeft(_intValue_3);
        BigInteger _min = ff.min(ft);
        BigInteger _min_1 = _min.min(tf);
        BigInteger _min_2 = _min_1.min(tt);
        BigInteger _max = ff.max(ft);
        BigInteger _max_1 = _max.max(tf);
        BigInteger _max_2 = _max_1.max(tt);
        return Ranges.<BigInteger>closed(_min_2, _max_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRA)) {
        _matched=true;
        BigInteger _lowerEndpoint_4 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_5 = rightRange.lowerEndpoint();
        int _intValue_4 = _lowerEndpoint_5.intValue();
        final BigInteger ff_1 = _lowerEndpoint_4.shiftRight(_intValue_4);
        BigInteger _lowerEndpoint_6 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint_4 = rightRange.upperEndpoint();
        int _intValue_5 = _upperEndpoint_4.intValue();
        final BigInteger ft_1 = _lowerEndpoint_6.shiftRight(_intValue_5);
        BigInteger _upperEndpoint_5 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_7 = rightRange.lowerEndpoint();
        int _intValue_6 = _lowerEndpoint_7.intValue();
        final BigInteger tf_1 = _upperEndpoint_5.shiftRight(_intValue_6);
        BigInteger _upperEndpoint_6 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_7 = rightRange.upperEndpoint();
        int _intValue_7 = _upperEndpoint_7.intValue();
        final BigInteger tt_1 = _upperEndpoint_6.shiftRight(_intValue_7);
        BigInteger _min_3 = ff_1.min(ft_1);
        BigInteger _min_4 = _min_3.min(tf_1);
        BigInteger _min_5 = _min_4.min(tt_1);
        BigInteger _max_3 = ff_1.max(ft_1);
        BigInteger _max_4 = _max_3.max(tf_1);
        BigInteger _max_5 = _max_4.max(tt_1);
        return Ranges.<BigInteger>closed(_min_5, _max_5);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRL)) {
        _matched=true;
        BigInteger _lowerEndpoint_8 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_9 = rightRange.lowerEndpoint();
        final BigInteger ff_2 = RangeExtension.srl(_lowerEndpoint_8, _lowerEndpoint_9);
        BigInteger _lowerEndpoint_10 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint_8 = rightRange.upperEndpoint();
        final BigInteger ft_2 = RangeExtension.srl(_lowerEndpoint_10, _upperEndpoint_8);
        BigInteger _upperEndpoint_9 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_11 = rightRange.lowerEndpoint();
        final BigInteger tf_2 = RangeExtension.srl(_upperEndpoint_9, _lowerEndpoint_11);
        BigInteger _upperEndpoint_10 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_11 = rightRange.upperEndpoint();
        final BigInteger tt_2 = RangeExtension.srl(_upperEndpoint_10, _upperEndpoint_11);
        BigInteger _min_6 = ff_2.min(ft_2);
        BigInteger _min_7 = _min_6.min(tf_2);
        BigInteger _min_8 = _min_7.min(tt_2);
        BigInteger _max_6 = ff_2.max(ft_2);
        BigInteger _max_7 = _max_6.max(tf_2);
        BigInteger _max_8 = _max_7.max(tt_2);
        return Ranges.<BigInteger>closed(_min_8, _max_8);
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
    if (_lessThan) {
      return res.negate();
    }
    return res;
  }
  
  protected Range<BigInteger> _determineRange(final HDLBitOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Range<BigInteger> leftRange = this.determineRange(_left, context);
    HDLExpression _right = obj.getRight();
    final Range<BigInteger> rightRange = this.determineRange(_right, context);
    HDLBitOpType _type = obj.getType();
    final HDLBitOpType type = _type;
    boolean _matched = false;
    if (!_matched) {
      boolean _or = false;
      boolean _equals = Objects.equal(type, HDLBitOpType.OR);
      if (_equals) {
        _or = true;
      } else {
        boolean _equals_1 = Objects.equal(type, HDLBitOpType.XOR);
        _or = (_equals || _equals_1);
      }
      if (_or) {
        _matched=true;
        obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
        BigInteger _upperEndpoint = leftRange.upperEndpoint();
        int _bitLength = _upperEndpoint.bitLength();
        BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
        BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, _subtract);
      }
    }
    if (!_matched) {
      if (Objects.equal(type,HDLBitOpType.AND)) {
        _matched=true;
        obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
        BigInteger _upperEndpoint_1 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_2 = rightRange.upperEndpoint();
        int _bitLength_1 = _upperEndpoint_2.bitLength();
        BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(_bitLength_1);
        BigInteger _subtract_1 = _shiftLeft_1.subtract(BigInteger.ONE);
        BigInteger _min = _upperEndpoint_1.min(_subtract_1);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, _min);
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _equals_2 = Objects.equal(type, HDLBitOpType.LOGI_AND);
      if (_equals_2) {
        _or_1 = true;
      } else {
        boolean _equals_3 = Objects.equal(type, HDLBitOpType.LOGI_OR);
        _or_1 = (_equals_2 || _equals_3);
      }
      if (_or_1) {
        _matched=true;
        obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, BigInteger.ONE);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
    throw _runtimeException;
  }
  
  protected Range<BigInteger> _determineRange(final HDLArithOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Range<BigInteger> leftRange = this.determineRange(_left, context);
    HDLExpression _right = obj.getRight();
    Range<BigInteger> rightRange = this.determineRange(_right, context);
    HDLArithOpType _type = obj.getType();
    final HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.PLUS)) {
        _matched=true;
        BigInteger _lowerEndpoint = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_1 = rightRange.lowerEndpoint();
        BigInteger _add = _lowerEndpoint.add(_lowerEndpoint_1);
        BigInteger _upperEndpoint = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_1 = rightRange.upperEndpoint();
        BigInteger _add_1 = _upperEndpoint.add(_upperEndpoint_1);
        return Ranges.<BigInteger>closed(_add, _add_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MINUS)) {
        _matched=true;
        BigInteger _lowerEndpoint_2 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_3 = rightRange.lowerEndpoint();
        BigInteger _subtract = _lowerEndpoint_2.subtract(_lowerEndpoint_3);
        BigInteger _upperEndpoint_2 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_3 = rightRange.upperEndpoint();
        BigInteger _subtract_1 = _upperEndpoint_2.subtract(_upperEndpoint_3);
        return Ranges.<BigInteger>closed(_subtract, _subtract_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.DIV)) {
        _matched=true;
        boolean _or = false;
        BigInteger _lowerEndpoint_4 = rightRange.lowerEndpoint();
        boolean _equals = _lowerEndpoint_4.equals(BigInteger.ZERO);
        if (_equals) {
          _or = true;
        } else {
          BigInteger _upperEndpoint_4 = rightRange.upperEndpoint();
          boolean _equals_1 = _upperEndpoint_4.equals(BigInteger.ZERO);
          _or = (_equals || _equals_1);
        }
        if (_or) {
          obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
          return null;
        }
        boolean _or_1 = false;
        BigInteger _lowerEndpoint_5 = rightRange.lowerEndpoint();
        int _signum = _lowerEndpoint_5.signum();
        BigInteger _upperEndpoint_5 = rightRange.upperEndpoint();
        int _signum_1 = _upperEndpoint_5.signum();
        int _multiply = (_signum * _signum_1);
        boolean _lessThan = (_multiply < 0);
        if (_lessThan) {
          _or_1 = true;
        } else {
          BigInteger _upperEndpoint_6 = rightRange.upperEndpoint();
          int _signum_2 = _upperEndpoint_6.signum();
          boolean _equals_2 = (_signum_2 == 0);
          _or_1 = (_lessThan || _equals_2);
        }
        if (_or_1) {
          obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
        }
        BigInteger _lowerEndpoint_6 = rightRange.lowerEndpoint();
        BigInteger _divide = BigInteger.ONE.divide(_lowerEndpoint_6);
        BigInteger _upperEndpoint_7 = rightRange.upperEndpoint();
        BigInteger _divide_1 = BigInteger.ONE.divide(_upperEndpoint_7);
        Range<BigInteger> _closed = Ranges.<BigInteger>closed(_divide, _divide_1);
        rightRange = _closed;
        BigInteger _lowerEndpoint_7 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_8 = rightRange.lowerEndpoint();
        final BigInteger ff = _lowerEndpoint_7.multiply(_lowerEndpoint_8);
        BigInteger _lowerEndpoint_9 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint_8 = rightRange.upperEndpoint();
        final BigInteger ft = _lowerEndpoint_9.multiply(_upperEndpoint_8);
        BigInteger _upperEndpoint_9 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_10 = rightRange.lowerEndpoint();
        final BigInteger tf = _upperEndpoint_9.multiply(_lowerEndpoint_10);
        BigInteger _upperEndpoint_10 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_11 = rightRange.upperEndpoint();
        final BigInteger tt = _upperEndpoint_10.multiply(_upperEndpoint_11);
        BigInteger _min = ff.min(ft);
        BigInteger _min_1 = _min.min(tf);
        BigInteger _min_2 = _min_1.min(tt);
        BigInteger _max = ff.max(ft);
        BigInteger _max_1 = _max.max(tf);
        BigInteger _max_2 = _max_1.max(tt);
        return Ranges.<BigInteger>closed(_min_2, _max_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MUL)) {
        _matched=true;
        BigInteger _lowerEndpoint_11 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_12 = rightRange.lowerEndpoint();
        final BigInteger ff_1 = _lowerEndpoint_11.multiply(_lowerEndpoint_12);
        BigInteger _lowerEndpoint_13 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint_12 = rightRange.upperEndpoint();
        final BigInteger ft_1 = _lowerEndpoint_13.multiply(_upperEndpoint_12);
        BigInteger _upperEndpoint_13 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_14 = rightRange.lowerEndpoint();
        final BigInteger tf_1 = _upperEndpoint_13.multiply(_lowerEndpoint_14);
        BigInteger _upperEndpoint_14 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_15 = rightRange.upperEndpoint();
        final BigInteger tt_1 = _upperEndpoint_14.multiply(_upperEndpoint_15);
        BigInteger _min_3 = ff_1.min(ft_1);
        BigInteger _min_4 = _min_3.min(tf_1);
        BigInteger _min_5 = _min_4.min(tt_1);
        BigInteger _max_3 = ff_1.max(ft_1);
        BigInteger _max_4 = _max_3.max(tf_1);
        BigInteger _max_5 = _max_4.max(tt_1);
        return Ranges.<BigInteger>closed(_min_5, _max_5);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MOD)) {
        _matched=true;
        BigInteger _upperEndpoint_16 = rightRange.upperEndpoint();
        BigInteger _subtract_2 = _upperEndpoint_16.subtract(BigInteger.ONE);
        BigInteger _upperEndpoint_17 = leftRange.upperEndpoint();
        BigInteger _min_6 = _subtract_2.min(_upperEndpoint_17);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, _min_6);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.POW)) {
        _matched=true;
        BigInteger _lowerEndpoint_15 = leftRange.lowerEndpoint();
        BigInteger _lowerEndpoint_16 = rightRange.lowerEndpoint();
        int _intValue = _lowerEndpoint_16.intValue();
        final BigInteger ff_2 = _lowerEndpoint_15.pow(_intValue);
        BigInteger _lowerEndpoint_17 = leftRange.lowerEndpoint();
        BigInteger _upperEndpoint_18 = rightRange.upperEndpoint();
        int _intValue_1 = _upperEndpoint_18.intValue();
        final BigInteger ft_2 = _lowerEndpoint_17.pow(_intValue_1);
        BigInteger _upperEndpoint_19 = leftRange.upperEndpoint();
        BigInteger _lowerEndpoint_18 = rightRange.lowerEndpoint();
        int _intValue_2 = _lowerEndpoint_18.intValue();
        final BigInteger tf_2 = _upperEndpoint_19.pow(_intValue_2);
        BigInteger _upperEndpoint_20 = leftRange.upperEndpoint();
        BigInteger _upperEndpoint_21 = rightRange.upperEndpoint();
        int _intValue_3 = _upperEndpoint_21.intValue();
        final BigInteger tt_2 = _upperEndpoint_20.pow(_intValue_3);
        BigInteger _min_7 = ff_2.min(ft_2);
        BigInteger _min_8 = _min_7.min(tf_2);
        BigInteger _min_9 = _min_8.min(tt_2);
        BigInteger _max_6 = ff_2.max(ft_2);
        BigInteger _max_7 = _max_6.max(tf_2);
        BigInteger _max_8 = _max_7.max(tt_2);
        return Ranges.<BigInteger>closed(_min_9, _max_8);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
    throw _runtimeException;
  }
  
  protected Range<BigInteger> _determineRange(final HDLEnumRef obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
    return null;
  }
  
  protected Range<BigInteger> _determineRange(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final Range<BigInteger> right = this.determineRange(_target, context);
    HDLManipType _type = obj.getType();
    final HDLManipType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.CAST)) {
        _matched=true;
        final HDLType type = obj.getCastTo();
        if ((type instanceof HDLPrimitive)) {
          IHDLPrimitive _instance = HDLPrimitives.getInstance();
          final Range<BigInteger> castRange = _instance.getValueRange(((HDLPrimitive) type), context);
          return castRange.intersection(right);
        }
        obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
        return null;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.ARITH_NEG)) {
        _matched=true;
        BigInteger _upperEndpoint = right.upperEndpoint();
        BigInteger _negate = _upperEndpoint.negate();
        BigInteger _lowerEndpoint = right.lowerEndpoint();
        BigInteger _negate_1 = _lowerEndpoint.negate();
        return Ranges.<BigInteger>closed(_negate, _negate_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.BIT_NEG)) {
        _matched=true;
        BigInteger _upperEndpoint_1 = right.upperEndpoint();
        int _bitLength = _upperEndpoint_1.bitLength();
        BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
        BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, _subtract);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.LOGIC_NEG)) {
        _matched=true;
        obj.<IHDLObject>addMeta(RangeExtension.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
        return Ranges.<BigInteger>closed(BigInteger.ZERO, BigInteger.ONE);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented obj op");
    throw _runtimeException;
  }
  
  protected Range<BigInteger> _determineRange(final HDLFunctionCall obj, final HDLEvaluationContext context) {
    return HDLFunctions.determineRange(obj, context);
  }
  
  protected Range<BigInteger> _determineRange(final HDLConcat obj, final HDLEvaluationContext context) {
    HDLType _typeOf = TypeExtension.typeOf(obj);
    final HDLPrimitive type = ((HDLPrimitive) _typeOf);
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    return _instance.getValueRange(type, context);
  }
  
  public Range<BigInteger> determineRange(final IHDLObject obj, final HDLEvaluationContext context) {
    if (obj instanceof HDLEnumRef) {
      return _determineRange((HDLEnumRef)obj, context);
    } else if (obj instanceof HDLVariableRef) {
      return _determineRange((HDLVariableRef)obj, context);
    } else if (obj instanceof HDLArithOp) {
      return _determineRange((HDLArithOp)obj, context);
    } else if (obj instanceof HDLBitOp) {
      return _determineRange((HDLBitOp)obj, context);
    } else if (obj instanceof HDLEqualityOp) {
      return _determineRange((HDLEqualityOp)obj, context);
    } else if (obj instanceof HDLShiftOp) {
      return _determineRange((HDLShiftOp)obj, context);
    } else if (obj instanceof HDLConcat) {
      return _determineRange((HDLConcat)obj, context);
    } else if (obj instanceof HDLFunctionCall) {
      return _determineRange((HDLFunctionCall)obj, context);
    } else if (obj instanceof HDLLiteral) {
      return _determineRange((HDLLiteral)obj, context);
    } else if (obj instanceof HDLManip) {
      return _determineRange((HDLManip)obj, context);
    } else if (obj instanceof HDLRange) {
      return _determineRange((HDLRange)obj, context);
    } else if (obj instanceof HDLExpression) {
      return _determineRange((HDLExpression)obj, context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context).toString());
    }
  }
}
