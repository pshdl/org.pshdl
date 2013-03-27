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
package de.tuhh.ict.pshdl.model.evaluation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLArrayInit;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLEnumRef;
import de.tuhh.ict.pshdl.model.HDLEqualityOp;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLLiteral.HDLLiteralPresentation;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLTernary;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext;
import de.tuhh.ict.pshdl.model.extensions.ProblemDescription;
import de.tuhh.ict.pshdl.model.extensions.TypeExtension;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions;
import de.tuhh.ict.pshdl.model.utils.Insulin;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

/**
 * This class allows to attempt to resolve a {@link java.math.BigInteger} value for any {@link de.tuhh.ict.pshdl.model.IHDLObject}. Of course
 * this only works when the given IHDLObject is truly constant. Parameters are not considered constant, unless
 * they can be found in the given {@link de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext}.
 * 
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class ConstantEvaluate {
  private static ConstantEvaluate INST = new Function0<ConstantEvaluate>() {
    public ConstantEvaluate apply() {
      ConstantEvaluate _constantEvaluate = new ConstantEvaluate();
      return _constantEvaluate;
    }
  }.apply();
  
  /**
   * Attempts to determine a constant that the given Expression can be replaced with. This method does not use parameters
   * as their value depends on the context.
   * 
   * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription#DESCRIPTION} Meta annotations
   */
  public static Optional<BigInteger> valueOf(final HDLExpression exp) {
    return ConstantEvaluate.INST.constantEvaluate(exp, null);
  }
  
  /**
   * Attempts to determine a constant that the given Expression can be replaced with. If parameter are encountered,
   * the provided context is used to retrieve a value for them.
   * 
   * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription.DESCRIPTION} Meta annotations
   */
  public static Optional<BigInteger> valueOf(final HDLExpression exp, final HDLEvaluationContext context) {
    return ConstantEvaluate.INST.constantEvaluate(exp, context);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLUnresolvedFragment obj, final HDLEvaluationContext context) {
    final Optional<? extends IHDLObject> type = Insulin.resolveFragment(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    IHDLObject _get = type.get();
    IHDLObject _container = obj.getContainer();
    IHDLObject _copyDeepFrozen = _get.copyDeepFrozen(_container);
    return this.constantEvaluate(_copyDeepFrozen, context);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLArrayInit obj, final HDLEvaluationContext context) {
    return Optional.<BigInteger>absent();
  }
  
  protected Optional<BigInteger> _constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Did not implement constantEvaulate for type:" + _classType);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLTernary obj, final HDLEvaluationContext context) {
    HDLExpression _ifExpr = obj.getIfExpr();
    final Optional<BigInteger> res = this.constantEvaluate(_ifExpr, context);
    boolean _isPresent = res.isPresent();
    if (_isPresent) {
      BigInteger _get = res.get();
      boolean _equals = BigInteger.ZERO.equals(_get);
      if (_equals) {
        HDLExpression _elseExpr = obj.getElseExpr();
        return this.constantEvaluate(_elseExpr, context);
      }
      HDLExpression _thenExpr = obj.getThenExpr();
      return this.constantEvaluate(_thenExpr, context);
    }
    HDLExpression _ifExpr_1 = obj.getIfExpr();
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _ifExpr_1);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
    return Optional.<BigInteger>absent();
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLLiteral obj, final HDLEvaluationContext context) {
    HDLLiteralPresentation _presentation = obj.getPresentation();
    final HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.STR)) {
        _matched=true;
        return Optional.<BigInteger>absent();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BOOL)) {
        _matched=true;
        return Optional.<BigInteger>absent();
      }
    }
    BigInteger _valueAsBigInt = obj.getValueAsBigInt();
    return Optional.<BigInteger>of(_valueAsBigInt);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final Optional<BigInteger> eval = this.subEvaluate(obj, _target, context);
    boolean _isPresent = eval.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLManipType _type = obj.getType();
    final HDLManipType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.ARITH_NEG)) {
        _matched=true;
        BigInteger _get = eval.get();
        BigInteger _negate = _get.negate();
        return Optional.<BigInteger>of(_negate);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.BIT_NEG)) {
        _matched=true;
        BigInteger _get_1 = eval.get();
        BigInteger _not_1 = _get_1.not();
        return Optional.<BigInteger>of(_not_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.LOGIC_NEG)) {
        _matched=true;
        HDLExpression _target_1 = obj.getTarget();
        final Optional<BigInteger> const_ = this.constantEvaluate(_target_1, context);
        boolean _isPresent_1 = const_.isPresent();
        if (_isPresent_1) {
          BigInteger _get_2 = const_.get();
          boolean _equals = _get_2.equals(BigInteger.ZERO);
          return ConstantEvaluate.boolInt(_equals);
        }
        return Optional.<BigInteger>absent();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.CAST)) {
        _matched=true;
        final HDLType type = obj.getCastTo();
        if ((type instanceof HDLPrimitive)) {
          final HDLPrimitive prim = ((HDLPrimitive) type);
          HDLExpression _width = prim.getWidth();
          boolean _tripleNotEquals = (_width != null);
          if (_tripleNotEquals) {
            HDLExpression _width_1 = prim.getWidth();
            final Optional<BigInteger> width = this.constantEvaluate(_width_1, context);
            boolean _isPresent_2 = width.isPresent();
            if (_isPresent_2) {
              BigInteger _get_3 = eval.get();
              BigInteger _get_4 = width.get();
              int _intValue = _get_4.intValue();
              BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
              BigInteger _mod = _get_3.mod(_shiftLeft);
              return Optional.<BigInteger>of(_mod);
            }
            return Optional.<BigInteger>absent();
          }
          return eval;
        }
        HDLExpression _target_2 = obj.getTarget();
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _target_2);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
        return Optional.<BigInteger>absent();
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLConcat obj, final HDLEvaluationContext context) {
    BigInteger sum = BigInteger.ZERO;
    ArrayList<HDLExpression> _cats = obj.getCats();
    for (final HDLExpression cat : _cats) {
      {
        final Optional<BigInteger> im = this.subEvaluate(obj, cat, context);
        boolean _isPresent = im.isPresent();
        boolean _not = (!_isPresent);
        if (_not) {
          return Optional.<BigInteger>absent();
        }
        final Optional<? extends HDLType> type = TypeExtension.typeOf(cat);
        boolean _isPresent_1 = type.isPresent();
        boolean _not_1 = (!_isPresent_1);
        if (_not_1) {
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, cat);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
          return Optional.<BigInteger>absent();
        }
        HDLType _get = type.get();
        HDLExpression _width = _get.getWidth();
        final Optional<BigInteger> width = this.constantEvaluate(_width, context);
        boolean _isPresent_2 = width.isPresent();
        boolean _not_2 = (!_isPresent_2);
        if (_not_2) {
          HDLType _get_1 = type.get();
          HDLExpression _width_1 = _get_1.getWidth();
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _width_1);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
          return Optional.<BigInteger>absent();
        }
        BigInteger _get_2 = width.get();
        int _intValue = _get_2.intValue();
        BigInteger _shiftLeft = sum.shiftLeft(_intValue);
        BigInteger _get_3 = im.get();
        BigInteger _or = _shiftLeft.or(_get_3);
        sum = _or;
      }
    }
    return Optional.<BigInteger>of(sum);
  }
  
  public Optional<BigInteger> subEvaluate(final HDLExpression container, final HDLExpression left, final HDLEvaluationContext context) {
    final Optional<BigInteger> leftVal = this.constantEvaluate(left, context);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      container.<IHDLObject>addMeta(ProblemDescription.SOURCE, left);
      container.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE);
      return Optional.<BigInteger>absent();
    }
    return leftVal;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLArithOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLArithOpType _type = obj.getType();
    final HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.DIV)) {
        _matched=true;
        BigInteger _get = leftVal.get();
        BigInteger _get_1 = rightVal.get();
        BigInteger _divide = _get.divide(_get_1);
        return Optional.<BigInteger>of(_divide);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MUL)) {
        _matched=true;
        BigInteger _get_2 = leftVal.get();
        BigInteger _get_3 = rightVal.get();
        BigInteger _multiply = _get_2.multiply(_get_3);
        return Optional.<BigInteger>of(_multiply);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MINUS)) {
        _matched=true;
        BigInteger _get_4 = leftVal.get();
        BigInteger _get_5 = rightVal.get();
        BigInteger _subtract = _get_4.subtract(_get_5);
        return Optional.<BigInteger>of(_subtract);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.PLUS)) {
        _matched=true;
        BigInteger _get_6 = leftVal.get();
        BigInteger _get_7 = rightVal.get();
        BigInteger _add = _get_6.add(_get_7);
        return Optional.<BigInteger>of(_add);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MOD)) {
        _matched=true;
        BigInteger _get_8 = leftVal.get();
        BigInteger _get_9 = rightVal.get();
        BigInteger _remainder = _get_8.remainder(_get_9);
        return Optional.<BigInteger>of(_remainder);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.POW)) {
        _matched=true;
        BigInteger _get_10 = leftVal.get();
        BigInteger _get_11 = rightVal.get();
        int _intValue = _get_11.intValue();
        BigInteger _pow = _get_10.pow(_intValue);
        return Optional.<BigInteger>of(_pow);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLBitOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLBitOpType _type = obj.getType();
    final HDLBitOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.AND)) {
        _matched=true;
        BigInteger _get = leftVal.get();
        BigInteger _get_1 = rightVal.get();
        BigInteger _and = _get.and(_get_1);
        return Optional.<BigInteger>of(_and);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.OR)) {
        _matched=true;
        BigInteger _get_2 = leftVal.get();
        BigInteger _get_3 = rightVal.get();
        BigInteger _or = _get_2.or(_get_3);
        return Optional.<BigInteger>of(_or);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.XOR)) {
        _matched=true;
        BigInteger _get_4 = leftVal.get();
        BigInteger _get_5 = rightVal.get();
        BigInteger _xor = _get_4.xor(_get_5);
        return Optional.<BigInteger>of(_xor);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_AND)) {
        _matched=true;
        BigInteger _get_6 = leftVal.get();
        boolean _equals = BigInteger.ZERO.equals(_get_6);
        final boolean l = (!_equals);
        BigInteger _get_7 = rightVal.get();
        boolean _equals_1 = BigInteger.ZERO.equals(_get_7);
        final boolean r = (!_equals_1);
        boolean _and_1 = false;
        if (!l) {
          _and_1 = false;
        } else {
          _and_1 = (l && r);
        }
        return ConstantEvaluate.boolInt(_and_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_OR)) {
        _matched=true;
        BigInteger _get_8 = leftVal.get();
        boolean _equals_2 = BigInteger.ZERO.equals(_get_8);
        final boolean l_1 = (!_equals_2);
        BigInteger _get_9 = rightVal.get();
        boolean _equals_3 = BigInteger.ZERO.equals(_get_9);
        final boolean r_1 = (!_equals_3);
        boolean _or_1 = false;
        if (l_1) {
          _or_1 = true;
        } else {
          _or_1 = (l_1 || r_1);
        }
        return ConstantEvaluate.boolInt(_or_1);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLEqualityOpType _type = obj.getType();
    final HDLEqualityOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.EQ)) {
        _matched=true;
        BigInteger _get = leftVal.get();
        BigInteger _get_1 = rightVal.get();
        boolean _equals = _get.equals(_get_1);
        return ConstantEvaluate.boolInt(_equals);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.NOT_EQ)) {
        _matched=true;
        BigInteger _get_2 = leftVal.get();
        BigInteger _get_3 = rightVal.get();
        boolean _equals_1 = _get_2.equals(_get_3);
        boolean _not_2 = (!_equals_1);
        return ConstantEvaluate.boolInt(_not_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER)) {
        _matched=true;
        BigInteger _get_4 = leftVal.get();
        BigInteger _get_5 = rightVal.get();
        int _compareTo = _get_4.compareTo(_get_5);
        boolean _greaterThan = (_compareTo > 0);
        return ConstantEvaluate.boolInt(_greaterThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER_EQ)) {
        _matched=true;
        BigInteger _get_6 = leftVal.get();
        BigInteger _get_7 = rightVal.get();
        int _compareTo_1 = _get_6.compareTo(_get_7);
        boolean _greaterEqualsThan = (_compareTo_1 >= 0);
        return ConstantEvaluate.boolInt(_greaterEqualsThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS)) {
        _matched=true;
        BigInteger _get_8 = leftVal.get();
        BigInteger _get_9 = rightVal.get();
        int _compareTo_2 = _get_8.compareTo(_get_9);
        boolean _lessThan = (_compareTo_2 < 0);
        return ConstantEvaluate.boolInt(_lessThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS_EQ)) {
        _matched=true;
        BigInteger _get_10 = leftVal.get();
        BigInteger _get_11 = rightVal.get();
        int _compareTo_3 = _get_10.compareTo(_get_11);
        boolean _lessEqualsThan = (_compareTo_3 <= 0);
        return ConstantEvaluate.boolInt(_lessEqualsThan);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLShiftOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLShiftOpType _type = obj.getType();
    final HDLShiftOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SLL)) {
        _matched=true;
        BigInteger _get = leftVal.get();
        BigInteger _get_1 = rightVal.get();
        int _intValue = _get_1.intValue();
        BigInteger _shiftLeft = _get.shiftLeft(_intValue);
        return Optional.<BigInteger>of(_shiftLeft);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRA)) {
        _matched=true;
        BigInteger _get_2 = leftVal.get();
        BigInteger _get_3 = rightVal.get();
        int _intValue_1 = _get_3.intValue();
        BigInteger _shiftRight = _get_2.shiftRight(_intValue_1);
        return Optional.<BigInteger>of(_shiftRight);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRL)) {
        _matched=true;
        BigInteger _get_4 = leftVal.get();
        BigInteger _get_5 = rightVal.get();
        int _intValue_2 = _get_5.intValue();
        final BigInteger shiftRight = _get_4.shiftRight(_intValue_2);
        int _signum = shiftRight.signum();
        boolean _lessThan = (_signum < 0);
        if (_lessThan) {
          BigInteger _negate = shiftRight.negate();
          return Optional.<BigInteger>of(_negate);
        }
        return Optional.<BigInteger>of(shiftRight);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLFunctionCall obj, final HDLEvaluationContext context) {
    LinkedList<BigInteger> _linkedList = new LinkedList<BigInteger>();
    final List<BigInteger> args = _linkedList;
    ArrayList<HDLExpression> _params = obj.getParams();
    for (final HDLExpression arg : _params) {
      {
        final Optional<BigInteger> bigVal = this.subEvaluate(obj, arg, context);
        boolean _isPresent = bigVal.isPresent();
        boolean _not = (!_isPresent);
        if (_not) {
          return Optional.<BigInteger>absent();
        }
        BigInteger _get = bigVal.get();
        args.add(_get);
      }
    }
    return HDLFunctions.constantEvaluate(obj, args, context);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLVariableRef obj, final HDLEvaluationContext context) {
    ArrayList<HDLExpression> _array = obj.getArray();
    int _size = _array.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    ArrayList<HDLRange> _bits = obj.getBits();
    int _size_1 = _bits.size();
    boolean _notEquals_1 = (_size_1 != 0);
    if (_notEquals_1) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    final Optional<? extends HDLType> type = TypeExtension.typeOf(obj);
    boolean _or = false;
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      _or = true;
    } else {
      HDLType _get = type.get();
      boolean _not_1 = (!(_get instanceof HDLPrimitive));
      _or = (_not || _not_1);
    }
    if (_or) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    final Optional<HDLVariable> hVar = obj.resolveVar();
    boolean _isPresent_1 = hVar.isPresent();
    boolean _not_2 = (!_isPresent_1);
    if (_not_2) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return Optional.<BigInteger>absent();
    }
    HDLVariable _get_1 = hVar.get();
    final HDLDirection dir = _get_1.getDirection();
    boolean _equals = Objects.equal(dir, HDLDirection.CONSTANT);
    if (_equals) {
      HDLVariable _get_2 = hVar.get();
      HDLExpression _defaultValue = _get_2.getDefaultValue();
      return this.subEvaluate(obj, _defaultValue, context);
    }
    boolean _equals_1 = Objects.equal(dir, HDLDirection.PARAMETER);
    if (_equals_1) {
      boolean _tripleEquals = (context == null);
      if (_tripleEquals) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
        return Optional.<BigInteger>absent();
      }
      HDLVariable _get_3 = hVar.get();
      final HDLExpression cRef = context.get(_get_3);
      final Optional<BigInteger> cRefEval = this.constantEvaluate(cRef, context);
      boolean _isPresent_2 = cRefEval.isPresent();
      boolean _not_3 = (!_isPresent_2);
      if (_not_3) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, cRef);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return Optional.<BigInteger>absent();
      }
      return cRefEval;
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
    return Optional.<BigInteger>absent();
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEnumRef obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
    return Optional.<BigInteger>absent();
  }
  
  public static Optional<BigInteger> boolInt(final boolean b) {
    Optional<BigInteger> _xifexpression = null;
    if (b) {
      Optional<BigInteger> _of = Optional.<BigInteger>of(BigInteger.ONE);
      _xifexpression = _of;
    } else {
      Optional<BigInteger> _of_1 = Optional.<BigInteger>of(BigInteger.ZERO);
      _xifexpression = _of_1;
    }
    return _xifexpression;
  }
  
  public Optional<BigInteger> constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context) {
    if (obj instanceof HDLEnumRef) {
      return _constantEvaluate((HDLEnumRef)obj, context);
    } else if (obj instanceof HDLVariableRef) {
      return _constantEvaluate((HDLVariableRef)obj, context);
    } else if (obj instanceof HDLArithOp) {
      return _constantEvaluate((HDLArithOp)obj, context);
    } else if (obj instanceof HDLBitOp) {
      return _constantEvaluate((HDLBitOp)obj, context);
    } else if (obj instanceof HDLEqualityOp) {
      return _constantEvaluate((HDLEqualityOp)obj, context);
    } else if (obj instanceof HDLShiftOp) {
      return _constantEvaluate((HDLShiftOp)obj, context);
    } else if (obj instanceof HDLUnresolvedFragment) {
      return _constantEvaluate((HDLUnresolvedFragment)obj, context);
    } else if (obj instanceof HDLArrayInit) {
      return _constantEvaluate((HDLArrayInit)obj, context);
    } else if (obj instanceof HDLConcat) {
      return _constantEvaluate((HDLConcat)obj, context);
    } else if (obj instanceof HDLFunctionCall) {
      return _constantEvaluate((HDLFunctionCall)obj, context);
    } else if (obj instanceof HDLLiteral) {
      return _constantEvaluate((HDLLiteral)obj, context);
    } else if (obj instanceof HDLManip) {
      return _constantEvaluate((HDLManip)obj, context);
    } else if (obj instanceof HDLTernary) {
      return _constantEvaluate((HDLTernary)obj, context);
    } else if (obj != null) {
      return _constantEvaluate(obj, context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context).toString());
    }
  }
}
