package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
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
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
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

@SuppressWarnings("all")
public class ConstantEvaluate {
  public static ConstantEvaluate INST = new Function0<ConstantEvaluate>() {
    public ConstantEvaluate apply() {
      ConstantEvaluate _constantEvaluate = new ConstantEvaluate();
      return _constantEvaluate;
    }
  }.apply();
  
  public static BigInteger valueOf(final HDLExpression exp) {
    return ConstantEvaluate.INST.constantEvaluate(exp, null);
  }
  
  public static BigInteger valueOf(final HDLExpression exp, final HDLEvaluationContext context) {
    return ConstantEvaluate.INST.constantEvaluate(exp, context);
  }
  
  public static GenericMeta<IHDLObject> SOURCE = new Function0<GenericMeta<IHDLObject>>() {
    public GenericMeta<IHDLObject> apply() {
      GenericMeta<IHDLObject> _genericMeta = new GenericMeta<IHDLObject>("SOURCE", true);
      return _genericMeta;
    }
  }.apply();
  
  protected BigInteger _constantEvaluate(final HDLUnresolvedFragment obj, final HDLEvaluationContext context) {
    IHDLObject _resolveFragment = Insulin.resolveFragment(obj);
    IHDLObject _container = obj.getContainer();
    IHDLObject _copyDeepFrozen = _resolveFragment==null?(IHDLObject)null:_resolveFragment.copyDeepFrozen(_container);
    return _copyDeepFrozen==null?(BigInteger)null:this.constantEvaluate(_copyDeepFrozen, context);
  }
  
  protected BigInteger _constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Did not implement constantEvaulate for type:" + _classType);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected BigInteger _constantEvaluate(final HDLTernary obj, final HDLEvaluationContext context) {
    HDLExpression _ifExpr = obj.getIfExpr();
    final BigInteger res = this.constantEvaluate(_ifExpr, context);
    boolean _notEquals = (!Objects.equal(res, null));
    if (_notEquals) {
      boolean _equals = BigInteger.ZERO.equals(res);
      if (_equals) {
        HDLExpression _elseExpr = obj.getElseExpr();
        return this.constantEvaluate(_elseExpr, context);
      }
      HDLExpression _thenExpr = obj.getThenExpr();
      return this.constantEvaluate(_thenExpr, context);
    }
    HDLExpression _ifExpr_1 = obj.getIfExpr();
    obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, _ifExpr_1);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
    return null;
  }
  
  protected BigInteger _constantEvaluate(final HDLLiteral obj, final HDLEvaluationContext context) {
    HDLLiteralPresentation _presentation = obj.getPresentation();
    final HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.STR)) {
        _matched=true;
        return null;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BOOL)) {
        _matched=true;
        return null;
      }
    }
    return obj.getValueAsBigInt();
  }
  
  protected BigInteger _constantEvaluate(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final BigInteger eval = this.subEvaluate(obj, _target, context);
    boolean _equals = Objects.equal(eval, null);
    if (_equals) {
      return null;
    }
    HDLManipType _type = obj.getType();
    final HDLManipType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.ARITH_NEG)) {
        _matched=true;
        return eval.negate();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.BIT_NEG)) {
        _matched=true;
        return eval.not();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.LOGIC_NEG)) {
        _matched=true;
        HDLExpression _target_1 = obj.getTarget();
        BigInteger _constantEvaluate = this.constantEvaluate(_target_1, context);
        boolean _equals_1 = _constantEvaluate.equals(BigInteger.ZERO);
        return ConstantEvaluate.boolInt(_equals_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.CAST)) {
        _matched=true;
        final HDLType type = obj.getCastTo();
        if ((type instanceof HDLPrimitive)) {
          final HDLPrimitive prim = ((HDLPrimitive) type);
          HDLExpression _width = prim.getWidth();
          boolean _notEquals = (!Objects.equal(_width, null));
          if (_notEquals) {
            HDLExpression _width_1 = prim.getWidth();
            final BigInteger width = this.constantEvaluate(_width_1, context);
            boolean _notEquals_1 = (!Objects.equal(width, null));
            if (_notEquals_1) {
              int _intValue = width.intValue();
              BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(_intValue);
              return eval.mod(_shiftLeft);
            }
            return null;
          }
          return eval;
        }
        HDLExpression _target_2 = obj.getTarget();
        obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, _target_2);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
        return null;
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected BigInteger _constantEvaluate(final HDLConcat obj, final HDLEvaluationContext context) {
    BigInteger sum = BigInteger.ZERO;
    ArrayList<HDLExpression> _cats = obj.getCats();
    for (final HDLExpression cat : _cats) {
      {
        final BigInteger im = this.subEvaluate(obj, cat, context);
        boolean _equals = Objects.equal(im, null);
        if (_equals) {
          return null;
        }
        HDLType _typeOf = TypeExtension.typeOf(cat);
        HDLExpression _width = _typeOf.getWidth();
        final BigInteger width = this.constantEvaluate(_width, context);
        boolean _equals_1 = Objects.equal(width, null);
        if (_equals_1) {
          HDLType _typeOf_1 = TypeExtension.typeOf(cat);
          HDLExpression _width_1 = _typeOf_1.getWidth();
          obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, _width_1);
          obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
          return null;
        }
        int _intValue = width.intValue();
        BigInteger _shiftLeft = sum.shiftLeft(_intValue);
        BigInteger _or = _shiftLeft.or(im);
        sum = _or;
      }
    }
    return sum;
  }
  
  public BigInteger subEvaluate(final HDLExpression container, final HDLExpression left, final HDLEvaluationContext context) {
    final BigInteger leftVal = this.constantEvaluate(left, context);
    boolean _equals = Objects.equal(leftVal, null);
    if (_equals) {
      container.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, left);
      container.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE);
      return null;
    }
    return leftVal;
  }
  
  protected BigInteger _constantEvaluate(final HDLArithOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final BigInteger leftVal = this.subEvaluate(obj, _left, context);
    boolean _equals = Objects.equal(leftVal, null);
    if (_equals) {
      return null;
    }
    HDLExpression _right = obj.getRight();
    final BigInteger rightVal = this.subEvaluate(obj, _right, context);
    boolean _equals_1 = Objects.equal(rightVal, null);
    if (_equals_1) {
      return null;
    }
    HDLArithOpType _type = obj.getType();
    final HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.DIV)) {
        _matched=true;
        return leftVal.divide(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MUL)) {
        _matched=true;
        return leftVal.multiply(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MINUS)) {
        _matched=true;
        return leftVal.subtract(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.PLUS)) {
        _matched=true;
        return leftVal.add(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MOD)) {
        _matched=true;
        return leftVal.remainder(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.POW)) {
        _matched=true;
        int _intValue = rightVal.intValue();
        return leftVal.pow(_intValue);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected BigInteger _constantEvaluate(final HDLBitOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final BigInteger leftVal = this.subEvaluate(obj, _left, context);
    boolean _equals = Objects.equal(leftVal, null);
    if (_equals) {
      return null;
    }
    HDLExpression _right = obj.getRight();
    final BigInteger rightVal = this.subEvaluate(obj, _right, context);
    boolean _equals_1 = Objects.equal(rightVal, null);
    if (_equals_1) {
      return null;
    }
    HDLBitOpType _type = obj.getType();
    final HDLBitOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.AND)) {
        _matched=true;
        return leftVal.and(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.OR)) {
        _matched=true;
        return leftVal.or(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.XOR)) {
        _matched=true;
        return leftVal.xor(rightVal);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_AND)) {
        _matched=true;
        boolean _equals_2 = BigInteger.ZERO.equals(leftVal);
        final boolean l = (!_equals_2);
        boolean _equals_3 = BigInteger.ZERO.equals(rightVal);
        final boolean r = (!_equals_3);
        boolean _and = false;
        if (!l) {
          _and = false;
        } else {
          _and = (l && r);
        }
        return ConstantEvaluate.boolInt(_and);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_OR)) {
        _matched=true;
        boolean _equals_4 = BigInteger.ZERO.equals(leftVal);
        final boolean l_1 = (!_equals_4);
        boolean _equals_5 = BigInteger.ZERO.equals(rightVal);
        final boolean r_1 = (!_equals_5);
        boolean _or = false;
        if (l_1) {
          _or = true;
        } else {
          _or = (l_1 || r_1);
        }
        return ConstantEvaluate.boolInt(_or);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected BigInteger _constantEvaluate(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final BigInteger leftVal = this.subEvaluate(obj, _left, context);
    boolean _equals = Objects.equal(leftVal, null);
    if (_equals) {
      return null;
    }
    HDLExpression _right = obj.getRight();
    final BigInteger rightVal = this.subEvaluate(obj, _right, context);
    boolean _equals_1 = Objects.equal(rightVal, null);
    if (_equals_1) {
      return null;
    }
    HDLEqualityOpType _type = obj.getType();
    final HDLEqualityOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.EQ)) {
        _matched=true;
        boolean _equals_2 = leftVal.equals(rightVal);
        return ConstantEvaluate.boolInt(_equals_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.NOT_EQ)) {
        _matched=true;
        boolean _equals_3 = leftVal.equals(rightVal);
        boolean _not = (!_equals_3);
        return ConstantEvaluate.boolInt(_not);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER)) {
        _matched=true;
        int _compareTo = leftVal.compareTo(rightVal);
        boolean _greaterThan = (_compareTo > 0);
        return ConstantEvaluate.boolInt(_greaterThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER_EQ)) {
        _matched=true;
        int _compareTo_1 = leftVal.compareTo(rightVal);
        boolean _greaterEqualsThan = (_compareTo_1 >= 0);
        return ConstantEvaluate.boolInt(_greaterEqualsThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS)) {
        _matched=true;
        int _compareTo_2 = leftVal.compareTo(rightVal);
        boolean _lessThan = (_compareTo_2 < 0);
        return ConstantEvaluate.boolInt(_lessThan);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS_EQ)) {
        _matched=true;
        int _compareTo_3 = leftVal.compareTo(rightVal);
        boolean _lessEqualsThan = (_compareTo_3 <= 0);
        return ConstantEvaluate.boolInt(_lessEqualsThan);
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected BigInteger _constantEvaluate(final HDLShiftOp obj, final HDLEvaluationContext context) {
    HDLExpression _left = obj.getLeft();
    final BigInteger leftVal = this.subEvaluate(obj, _left, context);
    boolean _equals = Objects.equal(leftVal, null);
    if (_equals) {
      return null;
    }
    HDLExpression _right = obj.getRight();
    final BigInteger rightVal = this.subEvaluate(obj, _right, context);
    boolean _equals_1 = Objects.equal(rightVal, null);
    if (_equals_1) {
      return null;
    }
    HDLShiftOpType _type = obj.getType();
    final HDLShiftOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SLL)) {
        _matched=true;
        int _intValue = rightVal.intValue();
        return leftVal.shiftLeft(_intValue);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRA)) {
        _matched=true;
        int _intValue_1 = rightVal.intValue();
        return leftVal.shiftRight(_intValue_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRL)) {
        _matched=true;
        int _intValue_2 = rightVal.intValue();
        final BigInteger shiftRight = leftVal.shiftRight(_intValue_2);
        int _signum = shiftRight.signum();
        boolean _lessThan = (_signum < 0);
        if (_lessThan) {
          return shiftRight.negate();
        }
        return shiftRight;
      }
    }
    RuntimeException _runtimeException = new RuntimeException("Incorrectly implemented constant evaluation!");
    throw _runtimeException;
  }
  
  protected BigInteger _constantEvaluate(final HDLFunctionCall obj, final HDLEvaluationContext context) {
    LinkedList<BigInteger> _linkedList = new LinkedList<BigInteger>();
    final List<BigInteger> args = _linkedList;
    ArrayList<HDLExpression> _params = obj.getParams();
    for (final HDLExpression arg : _params) {
      {
        final BigInteger bigVal = this.subEvaluate(obj, arg, context);
        boolean _equals = Objects.equal(bigVal, null);
        if (_equals) {
          return null;
        }
        args.add(bigVal);
      }
    }
    return HDLFunctions.constantEvaluate(obj, args, context);
  }
  
  protected BigInteger _constantEvaluate(final HDLVariableRef obj, final HDLEvaluationContext context) {
    ArrayList<HDLExpression> _array = obj.getArray();
    int _size = _array.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return null;
    }
    ArrayList<HDLRange> _bits = obj.getBits();
    int _size_1 = _bits.size();
    boolean _notEquals_1 = (_size_1 != 0);
    if (_notEquals_1) {
      obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
      return null;
    }
    final HDLType type = TypeExtension.typeOf(obj);
    boolean _not = (!(type instanceof HDLPrimitive));
    if (_not) {
      obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
      return null;
    }
    final HDLVariable hVar = obj.resolveVar();
    boolean _equals = Objects.equal(hVar, null);
    if (_equals) {
      obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
      obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.VARIABLE_NOT_RESOLVED);
      return null;
    }
    final HDLDirection dir = hVar.getDirection();
    boolean _equals_1 = Objects.equal(dir, HDLDirection.CONSTANT);
    if (_equals_1) {
      HDLExpression _defaultValue = hVar.getDefaultValue();
      return this.subEvaluate(obj, _defaultValue, context);
    }
    boolean _equals_2 = Objects.equal(dir, HDLDirection.PARAMETER);
    if (_equals_2) {
      boolean _equals_3 = Objects.equal(context, null);
      if (_equals_3) {
        obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
        return null;
      }
      final HDLExpression cRef = context.get(hVar);
      final BigInteger cRefEval = this.constantEvaluate(cRef, context);
      boolean _equals_4 = Objects.equal(cRefEval, null);
      if (_equals_4) {
        obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, cRef);
        obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
        return null;
      }
      return cRefEval;
    }
    obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
    return null;
  }
  
  protected BigInteger _constantEvaluate(final HDLEnumRef obj, final HDLEvaluationContext context) {
    obj.<IHDLObject>addMeta(ConstantEvaluate.SOURCE, obj);
    obj.<ProblemDescription>addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
    return null;
  }
  
  public static BigInteger boolInt(final boolean b) {
    BigInteger _xifexpression = null;
    if (b) {
      _xifexpression = BigInteger.ONE;
    } else {
      _xifexpression = BigInteger.ZERO;
    }
    return _xifexpression;
  }
  
  public BigInteger constantEvaluate(final IHDLObject obj, final HDLEvaluationContext context) {
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
