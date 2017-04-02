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
import org.pshdl.model.utils.HDLCodeGenerationException;
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
  
  public static Range<BigInteger> rangeOfForced(final HDLExpression obj, final HDLEvaluationContext context, final String stage) {
    final Optional<Range<BigInteger>> opt = RangeExtension.rangeOf(obj, context);
    boolean _isPresent = opt.isPresent();
    if (_isPresent) {
      return opt.get();
    }
    throw new HDLCodeGenerationException(obj, ("Unable to determine value range of " + obj), stage);
  }
  
  /**
   * Attempts to determine the range of an {@link HDLExpression}. If not successful check ProblemDescription
   * Meta for information.
   */
  public static Optional<Range<BigInteger>> rangeOf(final HDLExpression obj, final HDLEvaluationContext context) {
    if ((obj == null)) {
      throw new NullPointerException();
    }
    final Optional<Range<BigInteger>> range = RangeExtension.INST.determineRange(obj, context);
    if ((range == null)) {
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
    IHDLObject _copyDeepFrozen = type.get().obj.copyDeepFrozen(obj.getContainer());
    final HDLExpression copyDeepFrozen = ((HDLExpression) _copyDeepFrozen);
    return this.determineRange(copyDeepFrozen, context);
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLLiteral obj, final HDLEvaluationContext context) {
    final BigInteger bigVal = obj.getValueAsBigInt();
    if ((bigVal == null)) {
      return Optional.<Range<BigInteger>>absent();
    }
    return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(bigVal, bigVal));
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLVariableRef obj, final HDLEvaluationContext context) {
    final Optional<BigInteger> bigVal = ConstantEvaluate.valueOf(obj, context);
    boolean _isPresent = bigVal.isPresent();
    if (_isPresent) {
      return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(bigVal.get(), bigVal.get()));
    }
    final Optional<HDLVariable> hVar = obj.resolveVar();
    boolean _isPresent_1 = hVar.isPresent();
    boolean _not = (!_isPresent_1);
    if (_not) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return Optional.<Range<BigInteger>>absent();
    }
    HDLAnnotation _annotation = hVar.get().getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.range);
    LinkedHashSet<Problem> _linkedHashSet = new LinkedHashSet<Problem>();
    final Optional<Range<BigInteger>> annoCheck = HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.checkRangeAnnotation(_annotation, _linkedHashSet);
    boolean _isPresent_2 = annoCheck.isPresent();
    if (_isPresent_2) {
      return annoCheck;
    }
    final IHDLObject container = hVar.get().getContainer();
    if ((container != null)) {
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
        final Optional<Range<BigInteger>> zeroR = RangeExtension.rangeOf(loop.getRange().get(0), context);
        boolean _isPresent_4 = zeroR.isPresent();
        if (_isPresent_4) {
          Range<BigInteger> res = zeroR.get();
          ArrayList<HDLRange> _range = loop.getRange();
          for (final HDLRange r : _range) {
            {
              final Optional<Range<BigInteger>> rRange = RangeExtension.rangeOf(r, context);
              boolean _isPresent_5 = rRange.isPresent();
              if (_isPresent_5) {
                res = res.span(rRange.get());
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
    int _size = obj.getBits().size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      BigInteger bitWidth = BigInteger.ZERO;
      ArrayList<HDLRange> _bits = obj.getBits();
      for (final HDLRange r_1 : _bits) {
        {
          HDLExpression width = r_1.getWidth();
          width = width.copyDeepFrozen(r_1);
          Optional<BigInteger> cw = ConstantEvaluate.valueOf(width, context);
          boolean _isPresent_5 = cw.isPresent();
          boolean _not_1 = (!_isPresent_5);
          if (_not_1) {
            bitWidth = null;
          } else {
            if ((bitWidth != null)) {
              bitWidth = bitWidth.add(cw.get());
            }
          }
        }
      }
      if ((bitWidth != null)) {
        return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE)));
      }
    }
    final Optional<? extends HDLType> type = TypeExtension.typeOf(hVar.get());
    if ((type.isPresent() && (type.get() instanceof HDLPrimitive))) {
      HDLType _get = type.get();
      return HDLPrimitives.getInstance().getValueRange(((HDLPrimitive) _get), context);
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
    return Optional.<Range<BigInteger>>absent();
  }
  
  public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj) {
    return RangeExtension.rangeOf(obj, null);
  }
  
  public static Range<BigInteger> rangeOfForced(final HDLRange obj, final HDLEvaluationContext context, final String stage) {
    final Optional<Range<BigInteger>> opt = RangeExtension.rangeOf(obj, context);
    boolean _isPresent = opt.isPresent();
    if (_isPresent) {
      return opt.get();
    }
    throw new HDLCodeGenerationException(obj, ("Unable to determine value range of " + obj), stage);
  }
  
  public static Optional<Range<BigInteger>> rangeOf(final HDLRange obj, final HDLEvaluationContext context) {
    final Optional<Range<BigInteger>> to = RangeExtension.rangeOf(obj.getTo(), context);
    boolean _isPresent = to.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLExpression _from = obj.getFrom();
    boolean _tripleNotEquals = (_from != null);
    if (_tripleNotEquals) {
      final Optional<Range<BigInteger>> from = RangeExtension.rangeOf(obj.getFrom(), context);
      boolean _isPresent_1 = from.isPresent();
      boolean _not_1 = (!_isPresent_1);
      if (_not_1) {
        return Optional.<Range<BigInteger>>absent();
      }
      return Optional.<Range<BigInteger>>of(from.get().span(to.get()));
    }
    HDLExpression _dec = obj.getDec();
    boolean _tripleNotEquals_1 = (_dec != null);
    if (_tripleNotEquals_1) {
      final Optional<Range<BigInteger>> decVal = RangeExtension.rangeOf(obj.getDec(), context);
      boolean _isPresent_2 = decVal.isPresent();
      boolean _not_2 = (!_isPresent_2);
      if (_not_2) {
        return Optional.<Range<BigInteger>>absent();
      }
      return Optional.<Range<BigInteger>>of(to.get().span(decVal.get()));
    }
    HDLExpression _inc = obj.getInc();
    boolean _tripleNotEquals_2 = (_inc != null);
    if (_tripleNotEquals_2) {
      final Optional<Range<BigInteger>> incVal = RangeExtension.rangeOf(obj.getInc(), context);
      boolean _isPresent_3 = incVal.isPresent();
      boolean _not_3 = (!_isPresent_3);
      if (_not_3) {
        return Optional.<Range<BigInteger>>absent();
      }
      return Optional.<Range<BigInteger>>of(to.get().span(incVal.get()));
    }
    return to;
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
    return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE));
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLShiftOp obj, final HDLEvaluationContext context) {
    final Optional<Range<BigInteger>> leftRange = this.determineRange(obj.getLeft(), context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Optional<Range<BigInteger>> rightRange = this.determineRange(obj.getRight(), context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLShiftOp.HDLShiftOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case SLL:
          final BigInteger ff = lrVal.lowerEndpoint().shiftLeft(rrVal.lowerEndpoint().intValue());
          final BigInteger ft = lrVal.lowerEndpoint().shiftLeft(rrVal.upperEndpoint().intValue());
          final BigInteger tf = lrVal.upperEndpoint().shiftLeft(rrVal.lowerEndpoint().intValue());
          final BigInteger tt = lrVal.upperEndpoint().shiftLeft(rrVal.upperEndpoint().intValue());
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt)));
        case SRA:
          final BigInteger ff_1 = lrVal.lowerEndpoint().shiftRight(rrVal.lowerEndpoint().intValue());
          final BigInteger ft_1 = lrVal.lowerEndpoint().shiftRight(rrVal.upperEndpoint().intValue());
          final BigInteger tf_1 = lrVal.upperEndpoint().shiftRight(rrVal.lowerEndpoint().intValue());
          final BigInteger tt_1 = lrVal.upperEndpoint().shiftRight(rrVal.upperEndpoint().intValue());
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(ff_1.min(ft_1).min(tf_1).min(tt_1), ff_1.max(ft_1).max(tf_1).max(tt_1)));
        case SRL:
          final BigInteger ff_2 = RangeExtension.srl(lrVal.lowerEndpoint(), rrVal.lowerEndpoint());
          final BigInteger ft_2 = RangeExtension.srl(lrVal.lowerEndpoint(), rrVal.upperEndpoint());
          final BigInteger tf_2 = RangeExtension.srl(lrVal.upperEndpoint(), rrVal.lowerEndpoint());
          final BigInteger tt_2 = RangeExtension.srl(lrVal.upperEndpoint(), rrVal.upperEndpoint());
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(ff_2.min(ft_2).min(tf_2).min(tt_2), ff_2.max(ft_2).max(tf_2).max(tt_2)));
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented obj op");
  }
  
  private static BigInteger srl(final BigInteger a, final BigInteger b) {
    return BigIntegerFrame.srl(a, 1024, b.intValue());
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLBitOp obj, final HDLEvaluationContext context) {
    final Optional<Range<BigInteger>> leftRange = this.determineRange(obj.getLeft(), context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Optional<Range<BigInteger>> rightRange = this.determineRange(obj.getRight(), context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLBitOp.HDLBitOpType _type = obj.getType();
    final HDLBitOp.HDLBitOpType type = _type;
    boolean _matched = false;
    if ((Objects.equal(type, HDLBitOp.HDLBitOpType.OR) || Objects.equal(type, HDLBitOp.HDLBitOpType.XOR))) {
      _matched=true;
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
      return Optional.<Range<BigInteger>>of(
        RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(lrVal.upperEndpoint().bitLength()).subtract(BigInteger.ONE)));
    }
    if (!_matched) {
      if (Objects.equal(type, HDLBitOp.HDLBitOpType.AND)) {
        _matched=true;
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_NOT_SUPPORTED_FOR_RANGES);
        return Optional.<Range<BigInteger>>of(
          RangeTool.<BigInteger>createRange(BigInteger.ZERO, 
            lrVal.upperEndpoint().min(
              BigInteger.ONE.shiftLeft(rrVal.upperEndpoint().bitLength()).subtract(BigInteger.ONE))));
      }
    }
    if (!_matched) {
      if ((Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_AND) || Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_OR))) {
        _matched=true;
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
        return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE));
      }
    }
    throw new RuntimeException("Incorrectly implemented obj op");
  }
  
  protected Optional<Range<BigInteger>> _determineRange(final HDLArithOp obj, final HDLEvaluationContext context) {
    final Optional<Range<BigInteger>> leftRange = this.determineRange(obj.getLeft(), context);
    boolean _isPresent = leftRange.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> lrVal = leftRange.get();
    if (((!lrVal.hasLowerBound()) || (!lrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Optional<Range<BigInteger>> rightRange = this.determineRange(obj.getRight(), context);
    boolean _isPresent_1 = rightRange.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<Range<BigInteger>>absent();
    }
    final Range<BigInteger> rrVal = rightRange.get();
    if (((!rrVal.hasLowerBound()) || (!rrVal.hasUpperBound()))) {
      return Optional.<Range<BigInteger>>absent();
    }
    HDLArithOp.HDLArithOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case PLUS:
          return Optional.<Range<BigInteger>>of(
            RangeTool.<BigInteger>createRange(lrVal.lowerEndpoint().add(rrVal.lowerEndpoint()), 
              lrVal.upperEndpoint().add(rrVal.upperEndpoint())));
        case MINUS:
          return Optional.<Range<BigInteger>>of(
            RangeTool.<BigInteger>createRange(lrVal.lowerEndpoint().subtract(rrVal.lowerEndpoint()), 
              lrVal.upperEndpoint().subtract(rrVal.upperEndpoint())));
        case DIV:
          if ((rrVal.lowerEndpoint().equals(BigInteger.ZERO) || rrVal.upperEndpoint().equals(BigInteger.ZERO))) {
            obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
            obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
            return Optional.<Range<BigInteger>>absent();
          }
          if ((((rrVal.lowerEndpoint().signum() * rrVal.upperEndpoint().signum()) < 0) || 
            (rrVal.upperEndpoint().signum() == 0))) {
            obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
            obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
          }
          BigInteger _lowerEndpoint = rrVal.lowerEndpoint();
          BigDecimal _bigDecimal = new BigDecimal(_lowerEndpoint);
          BigInteger _upperEndpoint = rrVal.upperEndpoint();
          BigDecimal _bigDecimal_1 = new BigDecimal(_upperEndpoint);
          final Range<BigDecimal> mulRange = RangeTool.<BigDecimal>createRange(BigDecimal.ONE.divide(_bigDecimal), 
            BigDecimal.ONE.divide(_bigDecimal_1));
          BigInteger _lowerEndpoint_1 = lrVal.lowerEndpoint();
          final BigDecimal ff = new BigDecimal(_lowerEndpoint_1).multiply(mulRange.lowerEndpoint());
          BigInteger _lowerEndpoint_2 = lrVal.lowerEndpoint();
          final BigDecimal ft = new BigDecimal(_lowerEndpoint_2).multiply(mulRange.upperEndpoint());
          BigInteger _upperEndpoint_1 = lrVal.upperEndpoint();
          final BigDecimal tf = new BigDecimal(_upperEndpoint_1).multiply(mulRange.lowerEndpoint());
          BigInteger _upperEndpoint_2 = lrVal.upperEndpoint();
          final BigDecimal tt = new BigDecimal(_upperEndpoint_2).multiply(mulRange.upperEndpoint());
          return Optional.<Range<BigInteger>>of(
            RangeTool.<BigInteger>createRange(ff.min(ft).min(tf).min(tt).toBigInteger(), 
              ff.max(ft).max(tf).max(tt).toBigInteger()));
        case MUL:
          final BigInteger ff_1 = lrVal.lowerEndpoint().multiply(rrVal.lowerEndpoint());
          final BigInteger ft_1 = lrVal.lowerEndpoint().multiply(rrVal.upperEndpoint());
          final BigInteger tf_1 = lrVal.upperEndpoint().multiply(rrVal.lowerEndpoint());
          final BigInteger tt_1 = lrVal.upperEndpoint().multiply(rrVal.upperEndpoint());
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(ff_1.min(ft_1).min(tf_1).min(tt_1), ff_1.max(ft_1).max(tf_1).max(tt_1)));
        case MOD:
          final BigInteger rle = rrVal.lowerEndpoint();
          final BigInteger leftBound = rle.min(BigInteger.ZERO);
          final BigInteger rue = rrVal.upperEndpoint().subtract(BigInteger.ONE);
          final BigInteger rightBound = rue.max(BigInteger.ZERO);
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(leftBound, rightBound));
        case POW:
          final BigInteger ff_2 = lrVal.lowerEndpoint().pow(rrVal.lowerEndpoint().intValue());
          final BigInteger ft_2 = lrVal.lowerEndpoint().pow(rrVal.upperEndpoint().intValue());
          final BigInteger tf_2 = lrVal.upperEndpoint().pow(rrVal.lowerEndpoint().intValue());
          final BigInteger tt_2 = lrVal.upperEndpoint().pow(rrVal.upperEndpoint().intValue());
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(ff_2.min(ft_2).min(tf_2).min(tt_2), ff_2.max(ft_2).max(tf_2).max(tt_2)));
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
    final Optional<Range<BigInteger>> right = this.determineRange(obj.getTarget(), context);
    boolean _isPresent = right.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      HDLManip.HDLManipType _type = obj.getType();
      boolean _tripleEquals = (_type == HDLManip.HDLManipType.CAST);
      if (_tripleEquals) {
        final Optional<? extends HDLType> newTypeOpt = TypeExtension.typeOf(obj);
        if ((newTypeOpt.isPresent() && (newTypeOpt.get() instanceof HDLPrimitive))) {
          HDLType _get = newTypeOpt.get();
          HDLPrimitive prim = ((HDLPrimitive) _get);
          boolean _isAny = prim.isAny();
          if (_isAny) {
            prim = Insulin.anyCastType(prim, obj.getTarget());
          }
          return HDLPrimitives.getInstance().getValueRange(prim, context);
        }
      }
      return Optional.<Range<BigInteger>>absent();
    }
    HDLManip.HDLManipType _type_1 = obj.getType();
    if (_type_1 != null) {
      switch (_type_1) {
        case CAST:
          final HDLType type = obj.getCastTo();
          if ((type instanceof HDLPrimitive)) {
            final Optional<Range<BigInteger>> castRange = HDLPrimitives.getInstance().getValueRange(
              ((HDLPrimitive) type), context);
            HDLPrimitive.HDLPrimitiveType _type_2 = ((HDLPrimitive)type).getType();
            boolean _equals = Objects.equal(_type_2, HDLPrimitive.HDLPrimitiveType.INTEGER);
            if (_equals) {
              return Optional.<Range<BigInteger>>of(HDLPrimitives.intRange(BigInteger.valueOf(32L)).intersection(right.get()));
            }
            HDLPrimitive.HDLPrimitiveType _type_3 = ((HDLPrimitive)type).getType();
            boolean _equals_1 = Objects.equal(_type_3, HDLPrimitive.HDLPrimitiveType.NATURAL);
            if (_equals_1) {
              return Optional.<Range<BigInteger>>of(HDLPrimitives.uintRange(BigInteger.valueOf(32L)).intersection(right.get()));
            }
            HDLPrimitive.HDLPrimitiveType _type_4 = ((HDLPrimitive)type).getType();
            boolean _equals_2 = Objects.equal(_type_4, HDLPrimitive.HDLPrimitiveType.BIT);
            if (_equals_2) {
              return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE).intersection(right.get()));
            }
            boolean _isPresent_1 = castRange.isPresent();
            boolean _not_1 = (!_isPresent_1);
            if (_not_1) {
              return Optional.<Range<BigInteger>>absent();
            }
            return Optional.<Range<BigInteger>>of(castRange.get().intersection(right.get()));
          }
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
          return Optional.<Range<BigInteger>>absent();
        case ARITH_NEG:
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(right.get().upperEndpoint().negate(), right.get().lowerEndpoint().negate()));
        case BIT_NEG:
          return Optional.<Range<BigInteger>>of(
            RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(right.get().upperEndpoint().bitLength()).subtract(BigInteger.ONE)));
        case LOGIC_NEG:
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOLEAN_NOT_SUPPORTED_FOR_RANGES);
          return Optional.<Range<BigInteger>>of(RangeTool.<BigInteger>createRange(BigInteger.ZERO, BigInteger.ONE));
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
    HDLType _get = type.get();
    return HDLPrimitives.getInstance().getValueRange(((HDLPrimitive) _get), context);
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
