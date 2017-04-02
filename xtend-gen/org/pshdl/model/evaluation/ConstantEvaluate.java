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
 * This class allows to attempt to resolve a {@link BigInteger} value for any {@link IHDLObject}. Of course
 * this only works when the given IHDLObject is truly constant. Parameters are not considered constant, unless
 * they can be found in the given {@link HDLEvaluationContext}.
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
    return ConstantEvaluate.INST.constantEvaluate(exp, null, Sets.<HDLQualifiedName>newHashSet());
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
    return ConstantEvaluate.INST.constantEvaluate(exp, context, Sets.<HDLQualifiedName>newHashSet());
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLUnresolvedFragment obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<Insulin.ResolvedPart> type = Insulin.resolveFragment(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    return this.constantEvaluate(type.get().obj.copyDeepFrozen(obj.getContainer()), context, evaled);
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
    final Optional<BigInteger> res = this.constantEvaluate(obj.getIfExpr(), context, evaled);
    boolean _isPresent = res.isPresent();
    if (_isPresent) {
      boolean _equals = BigInteger.ZERO.equals(res.get());
      if (_equals) {
        return this.constantEvaluate(obj.getElseExpr(), context, evaled);
      }
      return this.constantEvaluate(obj.getThenExpr(), context, evaled);
    }
    obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj.getIfExpr());
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
          if (((context != null) && context.boolAsInt)) {
            boolean _equals = obj.equals(HDLLiteral.getFalse());
            boolean _not = (!_equals);
            return ConstantEvaluate.boolInt(_not);
          }
          return Optional.<BigInteger>absent();
        default:
          break;
      }
    }
    return Optional.<BigInteger>of(obj.getValueAsBigInt());
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLManip obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<BigInteger> eval = this.subEvaluate(obj, obj.getTarget(), context, evaled);
    boolean _isPresent = eval.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    HDLManip.HDLManipType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case ARITH_NEG:
          return Optional.<BigInteger>of(eval.get().negate());
        case BIT_NEG:
          return Optional.<BigInteger>of(eval.get().not());
        case LOGIC_NEG:
          final Optional<BigInteger> const_ = this.constantEvaluate(obj.getTarget(), context, evaled);
          boolean _isPresent_1 = const_.isPresent();
          if (_isPresent_1) {
            return ConstantEvaluate.boolInt(const_.get().equals(BigInteger.ZERO));
          }
          return Optional.<BigInteger>absent();
        case CAST:
          final HDLType type = obj.getCastTo();
          if ((type instanceof HDLPrimitive)) {
            final HDLPrimitive prim = ((HDLPrimitive) type);
            HDLExpression _width = prim.getWidth();
            boolean _tripleNotEquals = (_width != null);
            if (_tripleNotEquals) {
              final Optional<BigInteger> width = this.constantEvaluate(prim.getWidth(), context, evaled);
              boolean _isPresent_2 = width.isPresent();
              if (_isPresent_2) {
                return Optional.<BigInteger>of(eval.get().mod(BigInteger.ONE.shiftLeft(width.get().intValue())));
              }
              return Optional.<BigInteger>absent();
            }
            return eval;
          }
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj.getTarget());
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
        final Optional<BigInteger> width = this.constantEvaluate(type.get().getWidth(), context, evaled);
        boolean _isPresent_2 = width.isPresent();
        boolean _not_2 = (!_isPresent_2);
        if (_not_2) {
          obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, type.get().getWidth());
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
          return Optional.<BigInteger>absent();
        }
        sum = sum.shiftLeft(width.get().intValue()).or(im.get());
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
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, obj.getLeft(), context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, obj.getRight(), context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLArithOp.HDLArithOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case DIV:
          return Optional.<BigInteger>of(leftVal.get().divide(rightVal.get()));
        case MUL:
          return Optional.<BigInteger>of(leftVal.get().multiply(rightVal.get()));
        case MINUS:
          return Optional.<BigInteger>of(leftVal.get().subtract(rightVal.get()));
        case PLUS:
          return Optional.<BigInteger>of(leftVal.get().add(rightVal.get()));
        case MOD:
          return Optional.<BigInteger>of(leftVal.get().remainder(rightVal.get()));
        case POW:
          return Optional.<BigInteger>of(leftVal.get().pow(rightVal.get().intValue()));
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLBitOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, obj.getLeft(), context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, obj.getRight(), context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLBitOp.HDLBitOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case AND:
          return Optional.<BigInteger>of(leftVal.get().and(rightVal.get()));
        case OR:
          return Optional.<BigInteger>of(leftVal.get().or(rightVal.get()));
        case XOR:
          return Optional.<BigInteger>of(leftVal.get().xor(rightVal.get()));
        case LOGI_AND:
          boolean _equals = BigInteger.ZERO.equals(leftVal.get());
          final boolean l = (!_equals);
          boolean _equals_1 = BigInteger.ZERO.equals(rightVal.get());
          final boolean r = (!_equals_1);
          return ConstantEvaluate.boolInt((l && r));
        case LOGI_OR:
          boolean _equals_2 = BigInteger.ZERO.equals(leftVal.get());
          final boolean l_1 = (!_equals_2);
          boolean _equals_3 = BigInteger.ZERO.equals(rightVal.get());
          final boolean r_1 = (!_equals_3);
          return ConstantEvaluate.boolInt((l_1 || r_1));
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEqualityOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, obj.getLeft(), context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, obj.getRight(), context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLEqualityOp.HDLEqualityOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case EQ:
          return ConstantEvaluate.boolInt(leftVal.get().equals(rightVal.get()));
        case NOT_EQ:
          boolean _equals = leftVal.get().equals(rightVal.get());
          boolean _not_2 = (!_equals);
          return ConstantEvaluate.boolInt(_not_2);
        case GREATER:
          int _compareTo = leftVal.get().compareTo(rightVal.get());
          boolean _greaterThan = (_compareTo > 0);
          return ConstantEvaluate.boolInt(_greaterThan);
        case GREATER_EQ:
          int _compareTo_1 = leftVal.get().compareTo(rightVal.get());
          boolean _greaterEqualsThan = (_compareTo_1 >= 0);
          return ConstantEvaluate.boolInt(_greaterEqualsThan);
        case LESS:
          int _compareTo_2 = leftVal.get().compareTo(rightVal.get());
          boolean _lessThan = (_compareTo_2 < 0);
          return ConstantEvaluate.boolInt(_lessThan);
        case LESS_EQ:
          int _compareTo_3 = leftVal.get().compareTo(rightVal.get());
          boolean _lessEqualsThan = (_compareTo_3 <= 0);
          return ConstantEvaluate.boolInt(_lessEqualsThan);
        default:
          break;
      }
    }
    throw new RuntimeException("Incorrectly implemented constant evaluation!");
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLShiftOp obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    final Optional<BigInteger> leftVal = this.subEvaluate(obj, obj.getLeft(), context, evaled);
    boolean _isPresent = leftVal.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<BigInteger>absent();
    }
    final Optional<BigInteger> rightVal = this.subEvaluate(obj, obj.getRight(), context, evaled);
    boolean _isPresent_1 = rightVal.isPresent();
    boolean _not_1 = (!_isPresent_1);
    if (_not_1) {
      return Optional.<BigInteger>absent();
    }
    HDLShiftOp.HDLShiftOpType _type = obj.getType();
    if (_type != null) {
      switch (_type) {
        case SLL:
          return Optional.<BigInteger>of(leftVal.get().shiftLeft(rightVal.get().intValue()));
        case SRA:
          return Optional.<BigInteger>of(leftVal.get().shiftRight(rightVal.get().intValue()));
        case SRL:
          final BigInteger l = leftVal.get();
          int _signum = l.signum();
          boolean _lessThan = (_signum < 0);
          if (_lessThan) {
            final Optional<? extends HDLType> t = TypeExtension.typeOf(obj.getLeft());
            boolean _isPresent_2 = t.isPresent();
            if (_isPresent_2) {
              final Integer width = HDLPrimitives.getWidth(t.get(), context);
              if ((width != null)) {
                final int shiftWidth = rightVal.get().intValue();
                final BigInteger res = BigIntegerFrame.srl(l, (width).intValue(), shiftWidth);
                return Optional.<BigInteger>of(res);
              }
            }
            return Optional.<BigInteger>absent();
          }
          return Optional.<BigInteger>of(l.shiftRight(rightVal.get().intValue()));
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
    int _size = obj.getArray().size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      final Optional<HDLVariable> hVarOpt = obj.resolveVar();
      boolean _isPresent = hVarOpt.isPresent();
      if (_isPresent) {
        final HDLVariable hVar = hVarOpt.get();
        HDLVariableDeclaration.HDLDirection _direction = hVar.getDirection();
        boolean _equals = Objects.equal(_direction, HDLVariableDeclaration.HDLDirection.CONSTANT);
        if (_equals) {
          if (((context != null) && context.ignoreConstantRefs)) {
            return Optional.<BigInteger>absent();
          }
          final HDLExpression defVal = hVar.getDefaultValue();
          return this.arrayDefValue(defVal, obj.getArray(), context, evaled);
        }
      }
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    int _size_1 = obj.getBits().size();
    boolean _notEquals_1 = (_size_1 != 0);
    if (_notEquals_1) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    final Optional<? extends HDLType> type = TypeExtension.typeOf(obj);
    if (((!type.isPresent()) || (!(type.get() instanceof HDLPrimitive)))) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
      return Optional.<BigInteger>absent();
    }
    final Optional<HDLVariable> hVar_1 = obj.resolveVar();
    boolean _isPresent_1 = hVar_1.isPresent();
    boolean _not = (!_isPresent_1);
    if (_not) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return Optional.<BigInteger>absent();
    }
    final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hVar_1.get());
    boolean _contains = evaled.contains(fqn);
    if (_contains) {
      obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, hVar_1.get());
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CONSTANT_EVAL_LOOP);
      return Optional.<BigInteger>absent();
    }
    evaled.add(fqn);
    final HDLVariableDeclaration.HDLDirection dir = hVar_1.get().getDirection();
    boolean _equals_1 = Objects.equal(dir, HDLVariableDeclaration.HDLDirection.CONSTANT);
    if (_equals_1) {
      if (((context != null) && context.ignoreConstantRefs)) {
        return Optional.<BigInteger>absent();
      }
      final Optional<BigInteger> subEval = this.subEvaluate(obj, hVar_1.get().getDefaultValue(), context, evaled);
      boolean _isPresent_2 = subEval.isPresent();
      if (_isPresent_2) {
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
      final HDLExpression cRef = context.get(hVar_1.get());
      if ((cRef == null)) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, hVar_1.get());
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return Optional.<BigInteger>absent();
      }
      final Optional<BigInteger> cRefEval = this.constantEvaluate(cRef, context, evaled);
      boolean _isPresent_3 = cRefEval.isPresent();
      boolean _not_1 = (!_isPresent_3);
      if (_not_1) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, cRef);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return Optional.<BigInteger>absent();
      }
      boolean _isPresent_4 = cRefEval.isPresent();
      if (_isPresent_4) {
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
      final Optional<BigInteger> idx = ConstantEvaluate.valueOf(expressions.get(0), context);
      boolean _isPresent = idx.isPresent();
      boolean _not = (!_isPresent);
      if (_not) {
        return Optional.<BigInteger>absent();
      }
      final HDLArrayInit arr = ((HDLArrayInit) expression);
      final int idxValue = idx.get().intValue();
      int _size = arr.getExp().size();
      boolean _lessThan = (_size < idxValue);
      if (_lessThan) {
        return Optional.<BigInteger>of(BigInteger.ZERO);
      }
      return this.arrayDefValue(arr.getExp().get(idxValue), expressions.subList(1, expressions.size()), context, evaled);
    }
    return this.constantEvaluate(expression, context, evaled);
  }
  
  protected Optional<BigInteger> _constantEvaluate(final HDLEnumRef obj, final HDLEvaluationContext context, final Set<HDLQualifiedName> evaled) {
    if (((context != null) && context.enumAsInt)) {
      final Optional<HDLEnum> resolveHEnum = obj.resolveHEnum();
      final Optional<HDLVariable> resolveVar = obj.resolveVar();
      if (((!resolveHEnum.isPresent()) || (!resolveVar.isPresent()))) {
        obj.<IHDLObject>addMeta(ProblemDescription.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.FAILED_TO_RESOLVE_ENUM);
        return Optional.<BigInteger>absent();
      }
      final HDLEnum hEnum = resolveHEnum.get();
      final HDLVariable hVar = resolveVar.get();
      return Optional.<BigInteger>of(BigInteger.valueOf(hEnum.getEnums().indexOf(hVar)));
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
