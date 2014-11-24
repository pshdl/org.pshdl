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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Range;
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
import org.pshdl.model.extensions.ProblemDescription;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.simulation.RangeTool;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.validation.Problem;

/**
 * The RangeExtensions can determine what values an expression can possible have. This is useful for detecting
 * code that will likely cause problems. For example when one parameter specifies the size of an array and
 * another specifies the upper bound for the range of a for loop.
 * 
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class RangeExtension {
  private static RangeExtension INST = new RangeExtension();
  
  /**
   * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription
   * Meta for information.
   */
  public static Optional<Range<BigInteger>> rangeOf(final HDLExpression obj) {
    return RangeExtension.rangeOf(obj, null);
  }
  
  /**
   * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription
   * Meta for information.
   */
  public static Optional<Range<BigInteger>> rangeOf(final HDLExpression obj, final HDLEvaluationContext context) {
    boolean _tripleEquals = (obj == null);
    if (_tripleEquals) {
      throw new NullPointerException();
    }
    final Optional<Range<BigInteger>> range = RangeExtension.INST.determineRange(obj, context);
    boolean _tripleEquals_1 = (range == null);
    if (_tripleEquals_1) {
      String _string = obj.toString();
      throw new NullPointerException(_string);
    }
    return range;
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Incorrectly implemented obj op:" + _classType);
    String _plus_1 = (_plus + " ");
    String _plus_2 = (_plus_1 + obj);
    throw new RuntimeException(_plus_2);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLUnresolvedFragment obj, final HDLEvaluationContext context) {
    final Optional<Insulin.ResolvedPart> type = Insulin.resolveFragment(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    Insulin.ResolvedPart _get = type.get();
    IHDLObject _container = obj.getContainer();
    IHDLObject _copyDeepFrozen = _get.obj.copyDeepFrozen(_container);
    final HDLExpression copyDeepFrozen = ((HDLExpression) _copyDeepFrozen);
    return this.determineRange(copyDeepFrozen, context);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLLiteral obj, final HDLEvaluationContext context) {
    final BigInteger bigVal = obj.getValueAsBigInt();
    boolean _tripleEquals = (bigVal == null);
    if (_tripleEquals) {
      return Optional.<Range<BigInteger>>absent();
    }
    Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(bigVal, bigVal);
    return Optional.<Range<BigInteger>>of(_createRange);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLVariableRef obj, final HDLEvaluationContext context) {
    final Optional<BigInteger> bigVal = ConstantEvaluate.valueOf(obj, context);
    boolean _isPresent = bigVal.isPresent();
    if (_isPresent) {
      BigInteger _get = bigVal.get();
      BigInteger _get_1 = bigVal.get();
      Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(_get, _get_1);
      return Optional.<Range<BigInteger>>of(_createRange);
    }
    final Optional<HDLVariable> hVar = obj.resolveVar();
    boolean _isPresent_1 = hVar.isPresent();
    boolean _not = (!_isPresent_1);
    if (_not) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return Optional.<Range<BigInteger>>absent();
    }
    HDLVariable _get_2 = hVar.get();
    HDLAnnotation _annotation = _get_2.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.range);
    LinkedHashSet<Problem> _linkedHashSet = new LinkedHashSet<Problem>();
    final Optional<Range<BigInteger>> annoCheck = HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.checkRangeAnnotation(_annotation, _linkedHashSet);
    boolean _isPresent_2 = annoCheck.isPresent();
    if (_isPresent_2) {
      return annoCheck;
    }
    HDLVariable _get_3 = hVar.get();
    final IHDLObject container = _get_3.getContainer();
    boolean _tripleNotEquals = (container != null);
    if (_tripleNotEquals) {
      if ((container instanceof HDLVariableDeclaration)) {
        final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) container);
        HDLAnnotation _annotation_1 = hvd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.range);
        LinkedHashSet<Problem> _linkedHashSet_1 = new LinkedHashSet<Problem>();
        final Optional<Range<BigInteger>> subAnnoCheck = HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.checkRangeAnnotation(_annotation_1, _linkedHashSet_1);
        boolean _isPresent_3 = subAnnoCheck.isPresent();
        if (_isPresent_3) {
          return subAnnoCheck;
        }
      }
      if ((container instanceof HDLForLoop)) {
        final HDLForLoop loop = ((HDLForLoop) container);
        ArrayList<HDLRange> _range = loop.getRange();
        HDLRange _get_4 = _range.get(0);
        final Optional<Range<BigInteger>> zeroR = RangeExtension.rangeOf(_get_4, context);
        boolean _isPresent_4 = zeroR.isPresent();
        if (_isPresent_4) {
          Range<BigInteger> res = zeroR.get();
          ArrayList<HDLRange> _range_1 = loop.getRange();
          for (final HDLRange r : _range_1) {
            {
              final Optional<Range<BigInteger>> rRange = RangeExtension.rangeOf(r, context);
              boolean _isPresent_5 = rRange.isPresent();
              if (_isPresent_5) {
                Range<BigInteger> _get_5 = rRange.get();
                Range<BigInteger> _span = res.span(_get_5);
                res = _span;
              } else {
                Optional.<Object>absent();
              }
            }
          }
          return Optional.<Range<BigInteger>>of(res);
        } else {
          return Optional.<Range<BigInteger>>absent();
        }
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
          boolean _isPresent_5 = cw.isPresent();
          boolean _not_1 = (!_isPresent_5);
          if (_not_1) {
            bitWidth = null;
          } else {
            boolean _tripleNotEquals_1 = (bitWidth != null);
            if (_tripleNotEquals_1) {
              BigInteger _get_5 = cw.get();
              BigInteger _add = bitWidth.add(_get_5);
              bitWidth = _add;
            }
          }
        }
      }
      boolean _tripleNotEquals_1 = (bitWidth != null);
      if (_tripleNotEquals_1) {
        int _intValue = bitWidth.intValue();
        BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
        BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
        Range<BigInteger> _createRange_1 = RangeTool.<BigInteger>createRange(BigInteger.ZERO, _subtract);
        return Optional.<Range<BigInteger>>of(_createRange_1);
      }
    }
    HDLVariable _get_5 = hVar.get();
    final Optional<? extends HDLType> type = TypeExtension.typeOf(_get_5);
    boolean _and = false;
    boolean _isPresent_5 = type.isPresent();
    if (!_isPresent_5) {
      _and = false;
    } else {
      HDLType _get_6 = type.get();
      _and = (_get_6 instanceof HDLPrimitive);
    }
    if (_and) {
      HDLPrimitives _instance = HDLPrimitives.getInstance();
      HDLType _get_7 = type.get();
      return _instance.getValueRange(((HDLPrimitive) _get_7), context);
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
    return Optional.<Range<BigInteger>>absent();
  }
  
  public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj) {
    return RangeExtension.rangeOf(obj, null);
  }
  
  public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj, final HDLEvaluationContext context) {
    HDLExpression _to = obj.getTo();
    final Optional<Range<BigInteger>> to = RangeExtension.rangeOf(_to, context);
    boolean _isPresent = to.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLExpression _from = obj.getFrom();
    boolean _tripleNotEquals = (_from != null);
    if (_tripleNotEquals) {
      HDLExpression _from_1 = obj.getFrom();
      final Optional<Range<BigInteger>> from = RangeExtension.rangeOf(_from_1, context);
      boolean _isPresent_1 = from.isPresent();
      boolean _not_1 = (!_isPresent_1);
      if (_not_1) {
        return Optional.<Range<BigInteger>>absent();
      }
      Range<BigInteger> _get = from.get();
      Range<BigInteger> _get_1 = to.get();
      Range<BigInteger> _span = _get.span(_get_1);
      return Optional.<Range<BigInteger>>of(_span);
    }
    HDLExpression _dec = obj.getDec();
    boolean _tripleNotEquals_1 = (_dec != null);
    if (_tripleNotEquals_1) {
      HDLExpression _dec_1 = obj.getDec();
      final Optional<Range<BigInteger>> decVal = RangeExtension.rangeOf(_dec_1, context);
      boolean _isPresent_2 = decVal.isPresent();
      boolean _not_2 = (!_isPresent_2);
      if (_not_2) {
        return Optional.<Range<BigInteger>>absent();
      }
      Range<BigInteger> _get_2 = to.get();
      Range<BigInteger> _get_3 = decVal.get();
      Range<BigInteger> _span_1 = _get_2.span(_get_3);
      return Optional.<Range<BigInteger>>of(_span_1);
    }
    HDLExpression _inc = obj.getInc();
    boolean _tripleNotEquals_2 = (_inc != null);
    if (_tripleNotEquals_2) {
      HDLExpression _inc_1 = obj.getInc();
      final Optional<Range<BigInteger>> incVal = RangeExtension.rangeOf(_inc_1, context);
      boolean _isPresent_3 = incVal.isPresent();
      boolean _not_3 = (!_isPresent_3);
      if (_not_3) {
        return Optional.<Range<BigInteger>>absent();
      }
      Range<BigInteger> _get_4 = to.get();
      Range<BigInteger> _get_5 = incVal.get();
      Range<BigInteger> _span_2 = _get_4.span(_get_5);
      return Optional.<Range<BigInteger>>of(_span_2);
    }
    return to;
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
    Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE);
    return Optional.<Range<BigInteger>>of(_createRange);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLShiftOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    boolean _or = false;
    boolean _hasLowerBound = lrVal.hasLowerBound();
    boolean _not_1 = (!_hasLowerBound);
    if (_not_1) {
      _or = true;
    } else {
      boolean _hasUpperBound = lrVal.hasUpperBound();
      boolean _not_2 = (!_hasUpperBound);
      _or = _not_2;
    }
    if (_or) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_3 = (!_isPresent_1);
    if (_not_3) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    boolean _or_1 = false;
    boolean _hasLowerBound_1 = rrVal.hasLowerBound();
    boolean _not_4 = (!_hasLowerBound_1);
    if (_not_4) {
      _or_1 = true;
    } else {
      boolean _hasUpperBound_1 = rrVal.hasUpperBound();
      boolean _not_5 = (!_hasUpperBound_1);
      _or_1 = _not_5;
    }
    if (_or_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLShiftOp.HDLShiftOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case SLL:
          BigInteger _lowerEndpoint = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_1 = rrVal.lowerEndpoint();
          int _intValue = _lowerEndpoint_1.intValue();
          final BigInteger ff = _lowerEndpoint.shiftLeft(_intValue);
          BigInteger _lowerEndpoint_2 = lrVal.lowerEndpoint();
          BigInteger _upperEndpoint = rrVal.upperEndpoint();
          int _intValue_1 = _upperEndpoint.intValue();
          final BigInteger ft = _lowerEndpoint_2.shiftLeft(_intValue_1);
          BigInteger _upperEndpoint_1 = lrVal.upperEndpoint();
          BigInteger _lowerEndpoint_3 = rrVal.lowerEndpoint();
          int _intValue_2 = _lowerEndpoint_3.intValue();
          final BigInteger tf = _upperEndpoint_1.shiftLeft(_intValue_2);
          BigInteger _upperEndpoint_2 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_3 = rrVal.upperEndpoint();
          int _intValue_3 = _upperEndpoint_3.intValue();
          final BigInteger tt = _upperEndpoint_2.shiftLeft(_intValue_3);
          BigInteger _min = ff.min(ft);
          BigInteger _min_1 = _min.min(tf);
          BigInteger _min_2 = _min_1.min(tt);
          BigInteger _max = ff.max(ft);
          BigInteger _max_1 = _max.max(tf);
          BigInteger _max_2 = _max_1.max(tt);
          Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(_min_2, _max_2);
          return Optional.<Range<BigInteger>>of(_createRange);
        case SRA:
          BigInteger _lowerEndpoint_4 = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_5 = rrVal.lowerEndpoint();
          int _intValue_4 = _lowerEndpoint_5.intValue();
          final BigInteger ff_1 = _lowerEndpoint_4.shiftRight(_intValue_4);
          BigInteger _lowerEndpoint_6 = lrVal.lowerEndpoint();
          BigInteger _upperEndpoint_4 = rrVal.upperEndpoint();
          int _intValue_5 = _upperEndpoint_4.intValue();
          final BigInteger ft_1 = _lowerEndpoint_6.shiftRight(_intValue_5);
          BigInteger _upperEndpoint_5 = lrVal.upperEndpoint();
          BigInteger _lowerEndpoint_7 = rrVal.lowerEndpoint();
          int _intValue_6 = _lowerEndpoint_7.intValue();
          final BigInteger tf_1 = _upperEndpoint_5.shiftRight(_intValue_6);
          BigInteger _upperEndpoint_6 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_7 = rrVal.upperEndpoint();
          int _intValue_7 = _upperEndpoint_7.intValue();
          final BigInteger tt_1 = _upperEndpoint_6.shiftRight(_intValue_7);
          BigInteger _min_3 = ff_1.min(ft_1);
          BigInteger _min_4 = _min_3.min(tf_1);
          BigInteger _min_5 = _min_4.min(tt_1);
          BigInteger _max_3 = ff_1.max(ft_1);
          BigInteger _max_4 = _max_3.max(tf_1);
          BigInteger _max_5 = _max_4.max(tt_1);
          Range<BigInteger> _createRange_1 = RangeTool.<BigInteger>createRange(_min_5, _max_5);
          return Optional.<Range<BigInteger>>of(_createRange_1);
        case SRL:
          BigInteger _lowerEndpoint_8 = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_9 = rrVal.lowerEndpoint();
          final BigInteger ff_2 = RangeExtension.srl(_lowerEndpoint_8, _lowerEndpoint_9);
          BigInteger _lowerEndpoint_10 = lrVal.lowerEndpoint();
          BigInteger _upperEndpoint_8 = rrVal.upperEndpoint();
          final BigInteger ft_2 = RangeExtension.srl(_lowerEndpoint_10, _upperEndpoint_8);
          BigInteger _upperEndpoint_9 = lrVal.upperEndpoint();
          BigInteger _lowerEndpoint_11 = rrVal.lowerEndpoint();
          final BigInteger tf_2 = RangeExtension.srl(_upperEndpoint_9, _lowerEndpoint_11);
          BigInteger _upperEndpoint_10 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_11 = rrVal.upperEndpoint();
          final BigInteger tt_2 = RangeExtension.srl(_upperEndpoint_10, _upperEndpoint_11);
          BigInteger _min_6 = ff_2.min(ft_2);
          BigInteger _min_7 = _min_6.min(tf_2);
          BigInteger _min_8 = _min_7.min(tt_2);
          BigInteger _max_6 = ff_2.max(ft_2);
          BigInteger _max_7 = _max_6.max(tf_2);
          BigInteger _max_8 = _max_7.max(tt_2);
          Range<BigInteger> _createRange_2 = RangeTool.<BigInteger>createRange(_min_8, _max_8);
          return Optional.<Range<BigInteger>>of(_createRange_2);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented obj op");
  }
  
  private static BigInteger srl(final BigInteger a, final BigInteger b) {
    int _intValue = b.intValue();
    return BigIntegerFrame.srl(a, 1024, _intValue);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLBitOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    boolean _or = false;
    boolean _hasLowerBound = lrVal.hasLowerBound();
    boolean _not_1 = (!_hasLowerBound);
    if (_not_1) {
      _or = true;
    } else {
      boolean _hasUpperBound = lrVal.hasUpperBound();
      boolean _not_2 = (!_hasUpperBound);
      _or = _not_2;
    }
    if (_or) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_3 = (!_isPresent_1);
    if (_not_3) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    boolean _or_1 = false;
    boolean _hasLowerBound_1 = rrVal.hasLowerBound();
    boolean _not_4 = (!_hasLowerBound_1);
    if (_not_4) {
      _or_1 = true;
    } else {
      boolean _hasUpperBound_1 = rrVal.hasUpperBound();
      boolean _not_5 = (!_hasUpperBound_1);
      _or_1 = _not_5;
    }
    if (_or_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLBitOp.HDLBitOpType _type = obj.getType();
    final HDLBitOp.HDLBitOpType type = _type;
    boolean _matched = false;
    if (!_matched) {
      boolean _or_2 = false;
      boolean _equals = Objects.equal(type, HDLBitOp.HDLBitOpType.OR);
      if (_equals) {
        _or_2 = true;
      } else {
        boolean _equals_1 = Objects.equal(type, HDLBitOp.HDLBitOpType.XOR);
        _or_2 = _equals_1;
      }
      if (_or_2) {
        _matched=true;
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
        BigInteger _upperEndpoint = lrVal.upperEndpoint();
        int _bitLength = _upperEndpoint.bitLength();
        BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
        BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
        Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(BigInteger.ZERO, _subtract);
        return Optional.<Range<BigInteger>>of(_createRange);
      }
    }
    if (!_matched) {
      if (Objects.equal(type, HDLBitOp.HDLBitOpType.AND)) {
        _matched=true;
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
        BigInteger _upperEndpoint_1 = lrVal.upperEndpoint();
        BigInteger _upperEndpoint_2 = rrVal.upperEndpoint();
        int _bitLength_1 = _upperEndpoint_2.bitLength();
        BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(_bitLength_1);
        BigInteger _subtract_1 = _shiftLeft_1.subtract(BigInteger.ONE);
        BigInteger _min = _upperEndpoint_1.min(_subtract_1);
        Range<BigInteger> _createRange_1 = RangeTool.<BigInteger>createRange(BigInteger.ZERO, _min);
        return Optional.<Range<BigInteger>>of(_createRange_1);
      }
    }
    if (!_matched) {
      boolean _or_3 = false;
      boolean _equals_2 = Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_AND);
      if (_equals_2) {
        _or_3 = true;
      } else {
        boolean _equals_3 = Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_OR);
        _or_3 = _equals_3;
      }
      if (_or_3) {
        _matched=true;
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
        Range<BigInteger> _createRange_2 = RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE);
        return Optional.<Range<BigInteger>>of(_createRange_2);
      }
    }
    throw new RuntimeException("Incorrectly implemented obj op");
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLArithOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<Range<BigInteger>> leftRange = this.determineRange(_left, context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    boolean _or = false;
    boolean _hasLowerBound = lrVal.hasLowerBound();
    boolean _not_1 = (!_hasLowerBound);
    if (_not_1) {
      _or = true;
    } else {
      boolean _hasUpperBound = lrVal.hasUpperBound();
      boolean _not_2 = (!_hasUpperBound);
      _or = _not_2;
    }
    if (_or) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<Range<BigInteger>> rightRange = this.determineRange(_right, context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_3 = (!_isPresent_1);
    if (_not_3) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    boolean _or_1 = false;
    boolean _hasLowerBound_1 = rrVal.hasLowerBound();
    boolean _not_4 = (!_hasLowerBound_1);
    if (_not_4) {
      _or_1 = true;
    } else {
      boolean _hasUpperBound_1 = rrVal.hasUpperBound();
      boolean _not_5 = (!_hasUpperBound_1);
      _or_1 = _not_5;
    }
    if (_or_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLArithOp.HDLArithOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case PLUS:
          BigInteger _lowerEndpoint = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_1 = rrVal.lowerEndpoint();
          BigInteger _add = _lowerEndpoint.add(_lowerEndpoint_1);
          BigInteger _upperEndpoint = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_1 = rrVal.upperEndpoint();
          BigInteger _add_1 = _upperEndpoint.add(_upperEndpoint_1);
          Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(_add, _add_1);
          return Optional.<Range<BigInteger>>of(_createRange);
        case MINUS:
          BigInteger _lowerEndpoint_2 = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_3 = rrVal.lowerEndpoint();
          BigInteger _subtract = _lowerEndpoint_2.subtract(_lowerEndpoint_3);
          BigInteger _upperEndpoint_2 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_3 = rrVal.upperEndpoint();
          BigInteger _subtract_1 = _upperEndpoint_2.subtract(_upperEndpoint_3);
          Range<BigInteger> _createRange_1 = RangeTool.<BigInteger>createRange(_subtract, _subtract_1);
          return Optional.<Range<BigInteger>>of(_createRange_1);
        case DIV:
          boolean _or_2 = false;
          BigInteger _lowerEndpoint_4 = rrVal.lowerEndpoint();
          boolean _equals = _lowerEndpoint_4.equals(BigInteger.ZERO);
          if (_equals) {
            _or_2 = true;
          } else {
            BigInteger _upperEndpoint_4 = rrVal.upperEndpoint();
            boolean _equals_1 = _upperEndpoint_4.equals(BigInteger.ZERO);
            _or_2 = _equals_1;
          }
          if (_or_2) {
            obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
            obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
            return Optional.<Range<BigInteger>>absent();
          }
          boolean _or_3 = false;
          BigInteger _lowerEndpoint_5 = rrVal.lowerEndpoint();
          int _signum = _lowerEndpoint_5.signum();
          BigInteger _upperEndpoint_5 = rrVal.upperEndpoint();
          int _signum_1 = _upperEndpoint_5.signum();
          int _multiply = (_signum * _signum_1);
          boolean _lessThan = (_multiply < 0);
          if (_lessThan) {
            _or_3 = true;
          } else {
            BigInteger _upperEndpoint_6 = rrVal.upperEndpoint();
            int _signum_2 = _upperEndpoint_6.signum();
            boolean _equals_2 = (_signum_2 == 0);
            _or_3 = _equals_2;
          }
          if (_or_3) {
            obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
            obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
          }
          BigInteger _lowerEndpoint_6 = rrVal.lowerEndpoint();
          BigDecimal _bigDecimal = new BigDecimal(_lowerEndpoint_6);
          BigDecimal _divide = BigDecimal.ONE.divide(_bigDecimal);
          BigInteger _upperEndpoint_7 = rrVal.upperEndpoint();
          BigDecimal _bigDecimal_1 = new BigDecimal(_upperEndpoint_7);
          BigDecimal _divide_1 = BigDecimal.ONE.divide(_bigDecimal_1);
          final Range<BigDecimal> mulRange = RangeTool.<BigDecimal>createRange(_divide, _divide_1);
          BigInteger _lowerEndpoint_7 = lrVal.lowerEndpoint();
          BigDecimal _bigDecimal_2 = new BigDecimal(_lowerEndpoint_7);
          BigDecimal _lowerEndpoint_8 = mulRange.lowerEndpoint();
          final BigDecimal ff = _bigDecimal_2.multiply(_lowerEndpoint_8);
          BigInteger _lowerEndpoint_9 = lrVal.lowerEndpoint();
          BigDecimal _bigDecimal_3 = new BigDecimal(_lowerEndpoint_9);
          BigDecimal _upperEndpoint_8 = mulRange.upperEndpoint();
          final BigDecimal ft = _bigDecimal_3.multiply(_upperEndpoint_8);
          BigInteger _upperEndpoint_9 = lrVal.upperEndpoint();
          BigDecimal _bigDecimal_4 = new BigDecimal(_upperEndpoint_9);
          BigDecimal _lowerEndpoint_10 = mulRange.lowerEndpoint();
          final BigDecimal tf = _bigDecimal_4.multiply(_lowerEndpoint_10);
          BigInteger _upperEndpoint_10 = lrVal.upperEndpoint();
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
          Range<BigInteger> _createRange_2 = RangeTool.<BigInteger>createRange(_bigInteger, _bigInteger_1);
          return Optional.<Range<BigInteger>>of(_createRange_2);
        case MUL:
          BigInteger _lowerEndpoint_11 = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_12 = rrVal.lowerEndpoint();
          final BigInteger ff_1 = _lowerEndpoint_11.multiply(_lowerEndpoint_12);
          BigInteger _lowerEndpoint_13 = lrVal.lowerEndpoint();
          BigInteger _upperEndpoint_12 = rrVal.upperEndpoint();
          final BigInteger ft_1 = _lowerEndpoint_13.multiply(_upperEndpoint_12);
          BigInteger _upperEndpoint_13 = lrVal.upperEndpoint();
          BigInteger _lowerEndpoint_14 = rrVal.lowerEndpoint();
          final BigInteger tf_1 = _upperEndpoint_13.multiply(_lowerEndpoint_14);
          BigInteger _upperEndpoint_14 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_15 = rrVal.upperEndpoint();
          final BigInteger tt_1 = _upperEndpoint_14.multiply(_upperEndpoint_15);
          BigInteger _min_3 = ff_1.min(ft_1);
          BigInteger _min_4 = _min_3.min(tf_1);
          BigInteger _min_5 = _min_4.min(tt_1);
          BigInteger _max_3 = ff_1.max(ft_1);
          BigInteger _max_4 = _max_3.max(tf_1);
          BigInteger _max_5 = _max_4.max(tt_1);
          Range<BigInteger> _createRange_3 = RangeTool.<BigInteger>createRange(_min_5, _max_5);
          return Optional.<Range<BigInteger>>of(_createRange_3);
        case MOD:
          final BigInteger rle = rrVal.lowerEndpoint();
          final BigInteger leftBound = rle.min(BigInteger.ZERO);
          BigInteger _upperEndpoint_16 = rrVal.upperEndpoint();
          final BigInteger rue = _upperEndpoint_16.subtract(BigInteger.ONE);
          final BigInteger rightBound = rue.max(BigInteger.ZERO);
          Range<BigInteger> _createRange_4 = RangeTool.<BigInteger>createRange(leftBound, rightBound);
          return Optional.<Range<BigInteger>>of(_createRange_4);
        case POW:
          BigInteger _lowerEndpoint_15 = lrVal.lowerEndpoint();
          BigInteger _lowerEndpoint_16 = rrVal.lowerEndpoint();
          int _intValue = _lowerEndpoint_16.intValue();
          final BigInteger ff_2 = _lowerEndpoint_15.pow(_intValue);
          BigInteger _lowerEndpoint_17 = lrVal.lowerEndpoint();
          BigInteger _upperEndpoint_17 = rrVal.upperEndpoint();
          int _intValue_1 = _upperEndpoint_17.intValue();
          final BigInteger ft_2 = _lowerEndpoint_17.pow(_intValue_1);
          BigInteger _upperEndpoint_18 = lrVal.upperEndpoint();
          BigInteger _lowerEndpoint_18 = rrVal.lowerEndpoint();
          int _intValue_2 = _lowerEndpoint_18.intValue();
          final BigInteger tf_2 = _upperEndpoint_18.pow(_intValue_2);
          BigInteger _upperEndpoint_19 = lrVal.upperEndpoint();
          BigInteger _upperEndpoint_20 = rrVal.upperEndpoint();
          int _intValue_3 = _upperEndpoint_20.intValue();
          final BigInteger tt_2 = _upperEndpoint_19.pow(_intValue_3);
          BigInteger _min_6 = ff_2.min(ft_2);
          BigInteger _min_7 = _min_6.min(tf_2);
          BigInteger _min_8 = _min_7.min(tt_2);
          BigInteger _max_6 = ff_2.max(ft_2);
          BigInteger _max_7 = _max_6.max(tf_2);
          BigInteger _max_8 = _max_7.max(tt_2);
          Range<BigInteger> _createRange_5 = RangeTool.<BigInteger>createRange(_min_8, _max_8);
          return Optional.<Range<BigInteger>>of(_createRange_5);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented obj op");
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLEnumRef obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
    return Optional.<Range<BigInteger>>absent();
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final Optional<Range<BigInteger>> right = this.determineRange(_target, context);
    boolean _isPresent = right.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLManip.HDLManipType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case CAST:
          final HDLType type = obj.getCastTo();
          if ((type instanceof HDLPrimitive)) {
            HDLPrimitives _instance = HDLPrimitives.getInstance();
            final Optional<Range<BigInteger>> castRange = _instance.getValueRange(
              ((HDLPrimitive) type), context);
            boolean _isPresent_1 = right.isPresent();
            boolean _not_1 = (!_isPresent_1);
            if (_not_1) {
              return Optional.<Range<BigInteger>>absent();
            }
            HDLPrimitive.HDLPrimitiveType _type_1 = ((HDLPrimitive)type).getType();
            boolean _equals = Objects.equal(_type_1, HDLPrimitive.HDLPrimitiveType.INTEGER);
            if (_equals) {
              Range<BigInteger> _intRange = HDLPrimitives.intRange(BigInteger.valueOf(32L));
              Range<BigInteger> _get = right.get();
              Range<BigInteger> _intersection = _intRange.intersection(_get);
              return Optional.<Range<BigInteger>>of(_intersection);
            }
            HDLPrimitive.HDLPrimitiveType _type_2 = ((HDLPrimitive)type).getType();
            boolean _equals_1 = Objects.equal(_type_2, HDLPrimitive.HDLPrimitiveType.NATURAL);
            if (_equals_1) {
              Range<BigInteger> _uintRange = HDLPrimitives.uintRange(BigInteger.valueOf(32L));
              Range<BigInteger> _get_1 = right.get();
              Range<BigInteger> _intersection_1 = _uintRange.intersection(_get_1);
              return Optional.<Range<BigInteger>>of(_intersection_1);
            }
            HDLPrimitive.HDLPrimitiveType _type_3 = ((HDLPrimitive)type).getType();
            boolean _equals_2 = Objects.equal(_type_3, HDLPrimitive.HDLPrimitiveType.BIT);
            if (_equals_2) {
              Range<BigInteger> _createRange = RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE);
              Range<BigInteger> _get_2 = right.get();
              Range<BigInteger> _intersection_2 = _createRange.intersection(_get_2);
              return Optional.<Range<BigInteger>>of(_intersection_2);
            }
            boolean _isPresent_2 = castRange.isPresent();
            boolean _not_2 = (!_isPresent_2);
            if (_not_2) {
              return Optional.<Range<BigInteger>>absent();
            }
            Range<BigInteger> _get_3 = castRange.get();
            Range<BigInteger> _get_4 = right.get();
            Range<BigInteger> _intersection_3 = _get_3.intersection(_get_4);
            return Optional.<Range<BigInteger>>of(_intersection_3);
          }
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
          return Optional.<Range<BigInteger>>absent();
        case ARITH_NEG:
          Range<BigInteger> _get_5 = right.get();
          BigInteger _upperEndpoint = _get_5.upperEndpoint();
          BigInteger _negate = _upperEndpoint.negate();
          Range<BigInteger> _get_6 = right.get();
          BigInteger _lowerEndpoint = _get_6.lowerEndpoint();
          BigInteger _negate_1 = _lowerEndpoint.negate();
          Range<BigInteger> _createRange_1 = RangeTool.<BigInteger>createRange(_negate, _negate_1);
          return Optional.<Range<BigInteger>>of(_createRange_1);
        case BIT_NEG:
          Range<BigInteger> _get_7 = right.get();
          BigInteger _upperEndpoint_1 = _get_7.upperEndpoint();
          int _bitLength = _upperEndpoint_1.bitLength();
          BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_bitLength);
          BigInteger _subtract = _shiftLeft.subtract(BigInteger.ONE);
          Range<BigInteger> _createRange_2 = RangeTool.<BigInteger>createRange(BigInteger.ZERO, _subtract);
          return Optional.<Range<BigInteger>>of(_createRange_2);
        case LOGIC_NEG:
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
          Range<BigInteger> _createRange_3 = RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE);
          return Optional.<Range<BigInteger>>of(_createRange_3);
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
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLType _get = type.get();
    return _instance.getValueRange(((HDLPrimitive) _get), context);
  }
  
  public Optional<Range<BigInteger>> determineRange(final HDLExpression obj, final HDLEvaluationContext context) {
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
    } else if (obj instanceof HDLUnresolvedFragment) {
      return _determineRange((HDLUnresolvedFragment)obj, context);
    } else if (obj instanceof HDLConcat) {
      return _determineRange((HDLConcat)obj, context);
    } else if (obj instanceof HDLFunctionCall) {
      return _determineRange((HDLFunctionCall)obj, context);
    } else if (obj instanceof HDLLiteral) {
      return _determineRange((HDLLiteral)obj, context);
    } else if (obj instanceof HDLManip) {
      return _determineRange((HDLManip)obj, context);
    } else if (obj != null) {
      return _determineRange(obj, context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context).toString());
    }
  }
}
