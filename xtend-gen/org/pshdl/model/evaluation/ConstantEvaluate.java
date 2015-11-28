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
package org.pshdl.model.evaluation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.pshdl.interpreter.frames.BigIntegerFrame;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.ProblemDescription;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.Insulin;

/**
 * This class allows to attempt to resolve a {@link java.math.BigInteger} value for any {@link org.pshdl.model.IHDLObject}. Of course
 * this only works when the given IHDLObject is truly constant. Parameters are not considered constant, unless
 * they can be found in the given {@link org.pshdl.model.evaluation.HDLEvaluationContext}.
 * 
 * @author Karsten Becker
 */
@SuppressWarnings("all")
public class ConstantEvaluate {
  private static ConstantEvaluate INST = new ConstantEvaluate();
  
  /**
   * Attempts to determine a constant that the given Expression can be replaced with. This method does not use parameters
   * as their value depends on the context.
   * 
   * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription#DESCRIPTION} Meta annotations
   */
  public static Optional<BigInteger> valueOf(final HDLExpression exp) {
    HashSet<HDLQualifiedName> _newHashSet = Sets.<HDLQualifiedName>newHashSet();
    return ConstantEvaluate.INST.constantEvaluate(exp, null, _newHashSet);
  }
  
  /**
   * Attempts to determine a constant that the given Expression can be replaced with. If parameter are encountered,
   * the provided context is used to retrieve a value for them.
   * 
   * @return an absent {@link Optional} if not successful check the SOURCE and {@link ProblemDescription#DESCRIPTION} Meta annotations
   */
  public static BigInteger valueOfForced(final HDLExpression exp, final HDLEvaluationContext context, final String stage) {
    final Optional<BigInteger> opt = ConstantEvaluate.valueOf(exp, context);
    boolean _isPresent = opt.isPresent();
    if (_isPresent) {
      return opt.get();
    }
    throw new HDLCodeGenerationException(exp, (("Failed to evaluate \'" + exp) + "\' to a constant"), stage);
  }
  
  public static Optional<BigInteger> valueOf(final HDLExpression exp, final HDLEvaluationContext context) {
    if ((exp == null)) {
      return Optional.<BigInteger>absent();
    }
    HashSet<HDLQualifiedName> _newHashSet = Sets.<HDLQualifiedName>newHashSet();
    return ConstantEvaluate.INST.constantEvaluate(exp, context, _newHashSet);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLUnresolvedFragment obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<Insulin.ResolvedPart> type = Insulin.resolveFragment(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    Insulin.ResolvedPart _get = type.get();
    IHDLObject _container = obj.getContainer();
    IHDLObject _copyDeepFrozen = _get.obj.copyDeepFrozen(_container);
    return this.constantEvaluate(_copyDeepFrozen, context, evaled);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLArrayInit obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    return Optional.<BigInteger>absent();
  }
  
  protected Optional<BigInteger> _constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Did not implement constantEvaulate for type:" + _classType);
    throw new IllegalArgumentException(_plus);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLTernary obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _ifExpr = obj.getIfExpr();
    final Optional<BigInteger> res = this.constantEvaluate(_ifExpr, context, evaled);
    boolean _isPresent = res.isPresent();
    if (_isPresent) {
      BigInteger _get = res.get();
      boolean _equals = BigInteger.ZERO.equals(_get);
      if (_equals) {
        HDLExpression _elseExpr = obj.getElseExpr();
        return this.constantEvaluate(_elseExpr, context, evaled);
      }
      HDLExpression _thenExpr = obj.getThenExpr();
      return this.constantEvaluate(_thenExpr, context, evaled);
    }
    HDLExpression _ifExpr_1 = obj.getIfExpr();
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _ifExpr_1);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
    return Optional.<BigInteger>absent();
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLLiteral obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLLiteral.HDLLiteralPresentation _presentation = obj.getPresentation();
    if (_presentation != null) {
      switch (_presentation) {
        case STR:
          return Optional.<BigInteger>absent();
        case BOOL:
          boolean _and = false;
          if (!(context != null)) {
            _and = false;
          } else {
            _and = context.boolAsInt;
          }
          if (_and) {
            HDLLiteral _false = HDLLiteral.getFalse();
            boolean _equals = obj.equals(_false);
            boolean _not = (!_equals);
            return ConstantEvaluate.boolInt(_not);
          }
          return Optional.<BigInteger>absent();
        default:
          break;
      }
    }
    BigInteger _valueAsBigInt = obj.getValueAsBigInt();
    return Optional.<BigInteger>of(_valueAsBigInt);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLManip obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _target = obj.getTarget();
    final Optional<BigInteger> eval = this.subEvaluate(obj, _target, context, evaled);
    boolean _isPresent = eval.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLManip.HDLManipType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case ARITH_NEG:
          BigInteger _get = eval.get();
          BigInteger _negate = _get.negate();
          return Optional.<BigInteger>of(_negate);
        case BIT_NEG:
          BigInteger _get_1 = eval.get();
          BigInteger _not_1 = _get_1.not();
          return Optional.<BigInteger>of(_not_1);
        case LOGIC_NEG:
          HDLExpression _target_1 = obj.getTarget();
          final Optional<BigInteger> const_ = this.constantEvaluate(_target_1, context, evaled);
          boolean _isPresent_1 = const_.isPresent();
          if (_isPresent_1) {
            BigInteger _get_2 = const_.get();
            boolean _equals = _get_2.equals(BigInteger.ZERO);
            return ConstantEvaluate.boolInt(_equals);
          }
          return Optional.<BigInteger>absent();
        case CAST:
          final HDLType type = obj.getCastTo();
          if ((type instanceof HDLPrimitive)) {
            final HDLPrimitive prim = ((HDLPrimitive) type);
            HDLExpression _width = prim.getWidth();
            boolean _tripleNotEquals = (_width != null);
            if (_tripleNotEquals) {
              HDLExpression _width_1 = prim.getWidth();
              final Optional<BigInteger> width = this.constantEvaluate(_width_1, context, evaled);
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
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLConcat obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    BigInteger sum = BigInteger.ZERO;
    ArrayList<HDLExpression> _cats = obj.getCats();
    for (final HDLExpression cat : _cats) {
      {
        final Optional<BigInteger> im = this.subEvaluate(obj, cat, context, evaled);
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
        final Optional<BigInteger> width = this.constantEvaluate(_width, context, evaled);
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
  
  public Optional<BigInteger> subEvaluate(final HDLExpression container, final HDLExpression left, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    if ((left == null)) {
      throw new IllegalArgumentException((("Container:" + container) + " has null left expression"));
    }
    final Optional<BigInteger> leftVal = this.constantEvaluate(left, context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      container.<IHDLObject>addMeta(ProblemDescription.SOURCE, left);
      container.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE);
      return Optional.<BigInteger>absent();
    }
    return leftVal;
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLArithOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLArithOp.HDLArithOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case DIV:
          BigInteger _get = leftVal.get();
          BigInteger _get_1 = rightVal.get();
          BigInteger _divide = _get.divide(_get_1);
          return Optional.<BigInteger>of(_divide);
        case MUL:
          BigInteger _get_2 = leftVal.get();
          BigInteger _get_3 = rightVal.get();
          BigInteger _multiply = _get_2.multiply(_get_3);
          return Optional.<BigInteger>of(_multiply);
        case MINUS:
          BigInteger _get_4 = leftVal.get();
          BigInteger _get_5 = rightVal.get();
          BigInteger _subtract = _get_4.subtract(_get_5);
          return Optional.<BigInteger>of(_subtract);
        case PLUS:
          BigInteger _get_6 = leftVal.get();
          BigInteger _get_7 = rightVal.get();
          BigInteger _add = _get_6.add(_get_7);
          return Optional.<BigInteger>of(_add);
        case MOD:
          BigInteger _get_8 = leftVal.get();
          BigInteger _get_9 = rightVal.get();
          BigInteger _remainder = _get_8.remainder(_get_9);
          return Optional.<BigInteger>of(_remainder);
        case POW:
          BigInteger _get_10 = leftVal.get();
          BigInteger _get_11 = rightVal.get();
          int _intValue = _get_11.intValue();
          BigInteger _pow = _get_10.pow(_intValue);
          return Optional.<BigInteger>of(_pow);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLBitOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLBitOp.HDLBitOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case AND:
          BigInteger _get = leftVal.get();
          BigInteger _get_1 = rightVal.get();
          BigInteger _and = _get.and(_get_1);
          return Optional.<BigInteger>of(_and);
        case OR:
          BigInteger _get_2 = leftVal.get();
          BigInteger _get_3 = rightVal.get();
          BigInteger _or = _get_2.or(_get_3);
          return Optional.<BigInteger>of(_or);
        case XOR:
          BigInteger _get_4 = leftVal.get();
          BigInteger _get_5 = rightVal.get();
          BigInteger _xor = _get_4.xor(_get_5);
          return Optional.<BigInteger>of(_xor);
        case LOGI_AND:
          BigInteger _get_6 = leftVal.get();
          boolean _equals = BigInteger.ZERO.equals(_get_6);
          final boolean l = (!_equals);
          BigInteger _get_7 = rightVal.get();
          boolean _equals_1 = BigInteger.ZERO.equals(_get_7);
          final boolean r = (!_equals_1);
          return ConstantEvaluate.boolInt((l && r));
        case LOGI_OR:
          BigInteger _get_8 = leftVal.get();
          boolean _equals_2 = BigInteger.ZERO.equals(_get_8);
          final boolean l_1 = (!_equals_2);
          BigInteger _get_9 = rightVal.get();
          boolean _equals_3 = BigInteger.ZERO.equals(_get_9);
          final boolean r_1 = (!_equals_3);
          return ConstantEvaluate.boolInt((l_1 || r_1));
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEqualityOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLEqualityOp.HDLEqualityOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case EQ:
          BigInteger _get = leftVal.get();
          BigInteger _get_1 = rightVal.get();
          boolean _equals = _get.equals(_get_1);
          return ConstantEvaluate.boolInt(_equals);
        case NOT_EQ:
          BigInteger _get_2 = leftVal.get();
          BigInteger _get_3 = rightVal.get();
          boolean _equals_1 = _get_2.equals(_get_3);
          boolean _not_2 = (!_equals_1);
          return ConstantEvaluate.boolInt(_not_2);
        case GREATER:
          BigInteger _get_4 = leftVal.get();
          BigInteger _get_5 = rightVal.get();
          int _compareTo = _get_4.compareTo(_get_5);
          boolean _greaterThan = (_compareTo > 0);
          return ConstantEvaluate.boolInt(_greaterThan);
        case GREATER_EQ:
          BigInteger _get_6 = leftVal.get();
          BigInteger _get_7 = rightVal.get();
          int _compareTo_1 = _get_6.compareTo(_get_7);
          boolean _greaterEqualsThan = (_compareTo_1 >= 0);
          return ConstantEvaluate.boolInt(_greaterEqualsThan);
        case LESS:
          BigInteger _get_8 = leftVal.get();
          BigInteger _get_9 = rightVal.get();
          int _compareTo_2 = _get_8.compareTo(_get_9);
          boolean _lessThan = (_compareTo_2 < 0);
          return ConstantEvaluate.boolInt(_lessThan);
        case LESS_EQ:
          BigInteger _get_10 = leftVal.get();
          BigInteger _get_11 = rightVal.get();
          int _compareTo_3 = _get_10.compareTo(_get_11);
          boolean _lessEqualsThan = (_compareTo_3 <= 0);
          return ConstantEvaluate.boolInt(_lessEqualsThan);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLShiftOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    HDLExpression _left = obj.getLeft();
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, _left, context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLExpression _right = obj.getRight();
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, _right, context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLShiftOp.HDLShiftOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case SLL:
          BigInteger _get = leftVal.get();
          BigInteger _get_1 = rightVal.get();
          int _intValue = _get_1.intValue();
          BigInteger _shiftLeft = _get.shiftLeft(_intValue);
          return Optional.<BigInteger>of(_shiftLeft);
        case SRA:
          BigInteger _get_2 = leftVal.get();
          BigInteger _get_3 = rightVal.get();
          int _intValue_1 = _get_3.intValue();
          BigInteger _shiftRight = _get_2.shiftRight(_intValue_1);
          return Optional.<BigInteger>of(_shiftRight);
        case SRL:
          final BigInteger l = leftVal.get();
          int _signum = l.signum();
          boolean _lessThan = (_signum < 0);
          if (_lessThan) {
            HDLExpression _left_1 = obj.getLeft();
            final Optional<? extends HDLType> t = TypeExtension.typeOf(_left_1);
            boolean _isPresent_2 = t.isPresent();
            if (_isPresent_2) {
              HDLType _get_4 = t.get();
              final Integer width = HDLPrimitives.getWidth(_get_4, context);
              if ((width != null)) {
                BigInteger _get_5 = rightVal.get();
                final int shiftWidth = _get_5.intValue();
                final BigInteger res = BigIntegerFrame.srl(l, (width).intValue(), shiftWidth);
                return Optional.<BigInteger>of(res);
              }
            }
            return Optional.<BigInteger>absent();
          }
          BigInteger _get_6 = rightVal.get();
          int _intValue_2 = _get_6.intValue();
          BigInteger _shiftRight_1 = l.shiftRight(_intValue_2);
          return Optional.<BigInteger>of(_shiftRight_1);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLFunctionCall obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    return HDLFunctions.constantEvaluate(obj, context);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLVariableRef obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    ArrayList<HDLExpression> _array = obj.getArray();
    int _size = _array.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      final Optional<HDLVariable> hVarOpt = obj.resolveVar();
      boolean _isPresent = hVarOpt.isPresent();
      if (_isPresent) {
        final HDLVariable hVar = hVarOpt.get();
        HDLVariableDeclaration.HDLDirection _direction = hVar.getDirection();
        boolean _equals = Objects.equal(_direction, HDLVariableDeclaration.HDLDirection.CONSTANT);
        if (_equals) {
          boolean _and = false;
          if (!(context != null)) {
            _and = false;
          } else {
            _and = context.ignoreConstantRefs;
          }
          if (_and) {
            return Optional.<BigInteger>absent();
          }
          final HDLExpression defVal = hVar.getDefaultValue();
          ArrayList<HDLExpression> _array_1 = obj.getArray();
          return this.arrayDefValue(defVal, _array_1, context, evaled);
        }
      }
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
    boolean _isPresent_1 = type.isPresent();
    boolean _not = (!_isPresent_1);
    if (_not) {
      _or = true;
    } else {
      HDLType _get = type.get();
      boolean _not_1 = (!(_get instanceof HDLPrimitive));
      _or = _not_1;
    }
    if (_or) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    final Optional<HDLVariable> hVar_1 = obj.resolveVar();
    boolean _isPresent_2 = hVar_1.isPresent();
    boolean _not_2 = (!_isPresent_2);
    if (_not_2) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return Optional.<BigInteger>absent();
    }
    HDLVariable _get_1 = hVar_1.get();
    final HDLQualifiedName fqn = FullNameExtension.fullNameOf(_get_1);
    boolean _contains = evaled.contains(fqn);
    if (_contains) {
      HDLVariable _get_2 = hVar_1.get();
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _get_2);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CONSTANT_EVAL_LOOP);
      return Optional.<BigInteger>absent();
    }
    evaled.add(fqn);
    HDLVariable _get_3 = hVar_1.get();
    final HDLVariableDeclaration.HDLDirection dir = _get_3.getDirection();
    boolean _equals_1 = Objects.equal(dir, HDLVariableDeclaration.HDLDirection.CONSTANT);
    if (_equals_1) {
      boolean _and_1 = false;
      if (!(context != null)) {
        _and_1 = false;
      } else {
        _and_1 = context.ignoreConstantRefs;
      }
      if (_and_1) {
        return Optional.<BigInteger>absent();
      }
      HDLVariable _get_4 = hVar_1.get();
      HDLExpression _defaultValue = _get_4.getDefaultValue();
      final Optional<BigInteger> subEval = this.subEvaluate(obj, _defaultValue, context, evaled);
      boolean _isPresent_3 = subEval.isPresent();
      if (_isPresent_3) {
        evaled.remove(fqn);
      }
      return subEval;
    }
    boolean _equals_2 = Objects.equal(dir, HDLVariableDeclaration.HDLDirection.PARAMETER);
    if (_equals_2) {
      if ((context == null)) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
        return Optional.<BigInteger>absent();
      }
      if (context.ignoreParameterRefs) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
        return Optional.<BigInteger>absent();
      }
      HDLVariable _get_5 = hVar_1.get();
      final HDLExpression cRef = context.get(_get_5);
      if ((cRef == null)) {
        HDLVariable _get_6 = hVar_1.get();
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, _get_6);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return Optional.<BigInteger>absent();
      }
      final Optional<BigInteger> cRefEval = this.constantEvaluate(cRef, context, evaled);
      boolean _isPresent_4 = cRefEval.isPresent();
      boolean _not_3 = (!_isPresent_4);
      if (_not_3) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, cRef);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return Optional.<BigInteger>absent();
      }
      boolean _isPresent_5 = cRefEval.isPresent();
      if (_isPresent_5) {
        evaled.remove(fqn);
      }
      return cRefEval;
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
    return Optional.<BigInteger>absent();
  }
  
  public Optional<BigInteger> arrayDefValue(final HDLExpression expression, final List<HDLExpression> expressions, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    if ((expression instanceof HDLArrayInit)) {
      boolean _isEmpty = expressions.isEmpty();
      if (_isEmpty) {
        return Optional.<BigInteger>absent();
      }
      HDLExpression _get = expressions.get(0);
      final Optional<BigInteger> idx = ConstantEvaluate.valueOf(_get, context);
      boolean _isPresent = idx.isPresent();
      boolean _not = (!_isPresent);
      if (_not) {
        return Optional.<BigInteger>absent();
      }
      final HDLArrayInit arr = ((HDLArrayInit) expression);
      BigInteger _get_1 = idx.get();
      final int idxValue = _get_1.intValue();
      ArrayList<HDLExpression> _exp = arr.getExp();
      int _size = _exp.size();
      boolean _lessThan = (_size < idxValue);
      if (_lessThan) {
        return Optional.<BigInteger>of(BigInteger.ZERO);
      }
      ArrayList<HDLExpression> _exp_1 = arr.getExp();
      HDLExpression _get_2 = _exp_1.get(idxValue);
      int _size_1 = expressions.size();
      List<HDLExpression> _subList = expressions.subList(1, _size_1);
      return this.arrayDefValue(_get_2, _subList, context, evaled);
    }
    return this.constantEvaluate(expression, context, evaled);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEnumRef obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    boolean _and = false;
    if (!(context != null)) {
      _and = false;
    } else {
      _and = context.enumAsInt;
    }
    if (_and) {
      final Optional<HDLEnum> resolveHEnum = obj.resolveHEnum();
      final Optional<HDLVariable> resolveVar = obj.resolveVar();
      boolean _or = false;
      boolean _isPresent = resolveHEnum.isPresent();
      boolean _not = (!_isPresent);
      if (_not) {
        _or = true;
      } else {
        boolean _isPresent_1 = resolveVar.isPresent();
        boolean _not_1 = (!_isPresent_1);
        _or = _not_1;
      }
      if (_or) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.FAILED_TO_RESOLVE_ENUM);
        return Optional.<BigInteger>absent();
      }
      final HDLEnum hEnum = resolveHEnum.get();
      final HDLVariable hVar = resolveVar.get();
      ArrayList<HDLVariable> _enums = hEnum.getEnums();
      int _indexOf = _enums.indexOf(hVar);
      BigInteger _valueOf = BigInteger.valueOf(_indexOf);
      return Optional.<BigInteger>of(_valueOf);
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
    return Optional.<BigInteger>absent();
  }
  
  public static Optional<BigInteger> boolInt(final boolean b) {
    Optional<BigInteger> _xifexpression = null;
    if (b) {
      _xifexpression = Optional.<BigInteger>of(BigInteger.ONE);
    } else {
      _xifexpression = Optional.<BigInteger>of(BigInteger.ZERO);
    }
    return _xifexpression;
  }
  
  public Optional<BigInteger> constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    if (obj instanceof HDLEnumRef) {
      return _constantEvaluate((HDLEnumRef)obj, context, evaled);
    } else if (obj instanceof HDLVariableRef) {
      return _constantEvaluate((HDLVariableRef)obj, context, evaled);
    } else if (obj instanceof HDLArithOp) {
      return _constantEvaluate((HDLArithOp)obj, context, evaled);
    } else if (obj instanceof HDLBitOp) {
      return _constantEvaluate((HDLBitOp)obj, context, evaled);
    } else if (obj instanceof HDLEqualityOp) {
      return _constantEvaluate((HDLEqualityOp)obj, context, evaled);
    } else if (obj instanceof HDLShiftOp) {
      return _constantEvaluate((HDLShiftOp)obj, context, evaled);
    } else if (obj instanceof HDLUnresolvedFragment) {
      return _constantEvaluate((HDLUnresolvedFragment)obj, context, evaled);
    } else if (obj instanceof HDLArrayInit) {
      return _constantEvaluate((HDLArrayInit)obj, context, evaled);
    } else if (obj instanceof HDLConcat) {
      return _constantEvaluate((HDLConcat)obj, context, evaled);
    } else if (obj instanceof HDLFunctionCall) {
      return _constantEvaluate((HDLFunctionCall)obj, context, evaled);
    } else if (obj instanceof HDLLiteral) {
      return _constantEvaluate((HDLLiteral)obj, context, evaled);
    } else if (obj instanceof HDLManip) {
      return _constantEvaluate((HDLManip)obj, context, evaled);
    } else if (obj instanceof HDLTernary) {
      return _constantEvaluate((HDLTernary)obj, context, evaled);
    } else if (obj != null) {
      return _constantEvaluate(obj, context, evaled);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context, evaled).toString());
    }
  }
}
