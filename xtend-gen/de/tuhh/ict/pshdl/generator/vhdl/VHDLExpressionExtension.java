package de.tuhh.ict.pshdl.generator.vhdl;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLUtils;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLCastsLibrary;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLCastsLibrary.TargetType;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLShiftLibrary;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLTypesLibrary;
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
import de.tuhh.ict.pshdl.model.HDLFunction;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLInterfaceRef;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLLiteral.HDLLiteralPresentation;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLReference;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLTernary;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.extensions.TypeExtension;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.Range;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.Standard;
import de.upb.hni.vmagic.expression.Add;
import de.upb.hni.vmagic.expression.Aggregate;
import de.upb.hni.vmagic.expression.And;
import de.upb.hni.vmagic.expression.Concatenate;
import de.upb.hni.vmagic.expression.Divide;
import de.upb.hni.vmagic.expression.Equals;
import de.upb.hni.vmagic.expression.Expression;
import de.upb.hni.vmagic.expression.FunctionCall;
import de.upb.hni.vmagic.expression.GreaterEquals;
import de.upb.hni.vmagic.expression.GreaterThan;
import de.upb.hni.vmagic.expression.LessEquals;
import de.upb.hni.vmagic.expression.LessThan;
import de.upb.hni.vmagic.expression.Literal;
import de.upb.hni.vmagic.expression.Minus;
import de.upb.hni.vmagic.expression.Multiply;
import de.upb.hni.vmagic.expression.Name;
import de.upb.hni.vmagic.expression.Not;
import de.upb.hni.vmagic.expression.NotEquals;
import de.upb.hni.vmagic.expression.Or;
import de.upb.hni.vmagic.expression.Parentheses;
import de.upb.hni.vmagic.expression.Pow;
import de.upb.hni.vmagic.expression.Rem;
import de.upb.hni.vmagic.expression.Subtract;
import de.upb.hni.vmagic.expression.Xor;
import de.upb.hni.vmagic.literal.BasedLiteral;
import de.upb.hni.vmagic.literal.DecimalLiteral;
import de.upb.hni.vmagic.literal.StringLiteral;
import de.upb.hni.vmagic.object.ArrayElement;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.Slice;
import de.upb.hni.vmagic.type.UnresolvedType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class VHDLExpressionExtension {
  public static VHDLExpressionExtension INST = new Function0<VHDLExpressionExtension>() {
    public VHDLExpressionExtension apply() {
      VHDLExpressionExtension _vHDLExpressionExtension = new VHDLExpressionExtension();
      return _vHDLExpressionExtension;
    }
  }.apply();
  
  public static Expression<? extends Object> vhdlOf(final HDLExpression exp) {
    return VHDLExpressionExtension.INST.toVHDL(exp);
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLExpression exp) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Not implemented for type:" + _classType);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected Name<? extends Object> _toVHDL(final HDLReference ref) {
    HDLClass _classType = ref.getClassType();
    String _plus = ("Not implemented for type:" + _classType);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected String _getVHDLName(final HDLVariableRef obj) {
    HDLQualifiedName _varRefName = obj.getVarRefName();
    return _varRefName.getLastSegment();
  }
  
  protected String _getVHDLName(final HDLInterfaceRef obj) {
    HDLQualifiedName _hIfRefName = obj.getHIfRefName();
    String _lastSegment = _hIfRefName.getLastSegment();
    String _plus = (_lastSegment + "_");
    HDLQualifiedName _varRefName = obj.getVarRefName();
    String _lastSegment_1 = _varRefName.getLastSegment();
    return (_plus + _lastSegment_1);
  }
  
  protected Name<? extends Object> _toVHDL(final HDLVariableRef obj) {
    String _vHDLName = this.getVHDLName(obj);
    Signal _signal = new Signal(_vHDLName, UnresolvedType.NO_NAME);
    Name<?> result = _signal;
    Name<? extends Object> _ref = this.getRef(result, obj);
    result = _ref;
    return result;
  }
  
  private Name<? extends Object> getRef(final Name<? extends Object> name, final HDLVariableRef ref) {
    Name<? extends Object> result = name;
    ArrayList<HDLExpression> _array = ref.getArray();
    int _size = _array.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      LinkedList<Expression> _linkedList = new LinkedList<Expression>();
      final List<Expression> indices = _linkedList;
      ArrayList<HDLExpression> _array_1 = ref.getArray();
      for (final HDLExpression arr : _array_1) {
        Expression<?> _vHDL = this.toVHDL(arr);
        indices.add(_vHDL);
      }
      ArrayElement<Name<?>> _arrayElement = new ArrayElement<Name<?>>(name, indices);
      result = _arrayElement;
    }
    ArrayList<HDLRange> _bits = ref.getBits();
    int _size_1 = _bits.size();
    boolean _greaterThan = (_size_1 > 0);
    if (_greaterThan) {
      ArrayList<HDLRange> _bits_1 = ref.getBits();
      int _size_2 = _bits_1.size();
      boolean _greaterThan_1 = (_size_2 > 1);
      if (_greaterThan_1) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Multi bit access not supported");
        throw _illegalArgumentException;
      }
      ArrayList<HDLRange> _bits_2 = ref.getBits();
      final HDLRange r = _bits_2.get(0);
      HDLExpression _from = r.getFrom();
      boolean _equals = ObjectExtensions.operator_equals(_from, null);
      if (_equals) {
        HDLExpression _to = r.getTo();
        Expression<?> _vHDL_1 = this.toVHDL(_to);
        ArrayElement<Name<?>> _arrayElement_1 = new ArrayElement<Name<?>>(result, _vHDL_1);
        result = _arrayElement_1;
      } else {
        Range _vHDL_2 = this.toVHDL(r, Direction.DOWNTO);
        Slice<Name<?>> _slice = new Slice<Name<?>>(result, _vHDL_2);
        result = _slice;
      }
    }
    return result;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLArrayInit obj) {
    ArrayList<HDLExpression> _exp = obj.getExp();
    LinkedList<Expression<?>> _linkedList = new LinkedList<Expression<?>>();
    final Function2<LinkedList<Expression<?>>,HDLExpression,LinkedList<Expression<?>>> _function = new Function2<LinkedList<Expression<?>>,HDLExpression,LinkedList<Expression<?>>>() {
        public LinkedList<Expression<?>> apply(final LinkedList<Expression<?>> l, final HDLExpression e) {
          LinkedList<Expression<?>> _xblockexpression = null;
          {
            Expression<?> _vHDL = VHDLExpressionExtension.this.toVHDL(e);
            l.add(_vHDL);
            _xblockexpression = (l);
          }
          return _xblockexpression;
        }
      };
    LinkedList<Expression<?>> _fold = IterableExtensions.<HDLExpression, LinkedList<Expression<?>>>fold(_exp, _linkedList, _function);
    Aggregate _aggregate = new Aggregate(_fold);
    return _aggregate;
  }
  
  protected Name<? extends Object> _toVHDL(final HDLInterfaceRef obj) {
    String _vHDLName = this.getVHDLName(obj);
    Signal _signal = new Signal(_vHDLName, UnresolvedType.NO_NAME);
    Name<?> result = _signal;
    ArrayList<HDLExpression> _ifArray = obj.getIfArray();
    int _size = _ifArray.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      ArrayList<HDLExpression> _ifArray_1 = obj.getIfArray();
      LinkedList<Expression> _linkedList = new LinkedList<Expression>();
      final Function2<LinkedList<Expression>,HDLExpression,LinkedList<Expression>> _function = new Function2<LinkedList<Expression>,HDLExpression,LinkedList<Expression>>() {
          public LinkedList<Expression> apply(final LinkedList<Expression> l, final HDLExpression e) {
            LinkedList<Expression> _xblockexpression = null;
            {
              Expression<?> _vHDL = VHDLExpressionExtension.this.toVHDL(e);
              l.add(_vHDL);
              _xblockexpression = (l);
            }
            return _xblockexpression;
          }
        };
      LinkedList<Expression> _fold = IterableExtensions.<HDLExpression, LinkedList<Expression>>fold(_ifArray_1, _linkedList, _function);
      ArrayElement<Name<?>> _arrayElement = new ArrayElement<Name<?>>(result, _fold);
      result = _arrayElement;
    }
    return this.getRef(result, obj);
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLFunctionCall obj) {
    return HDLFunctions.toVHDLExpression(obj);
  }
  
  protected Signal _toVHDL(final HDLEnumRef obj) {
    HDLQualifiedName _varRefName = obj.getVarRefName();
    String _lastSegment = _varRefName.getLastSegment();
    Signal _signal = new Signal(_lastSegment, UnresolvedType.NO_NAME);
    return _signal;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLConcat obj) {
    final List<HDLExpression> cats = obj.getCats();
    HDLExpression _get = cats.get(0);
    Expression<?> res = this.toVHDL(_get);
    cats.remove(0);
    for (final HDLExpression cat : cats) {
      Expression<?> _vHDL = this.toVHDL(cat);
      Concatenate _concatenate = new Concatenate(res, _vHDL);
      res = _concatenate;
    }
    return res;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLManip obj) {
    HDLManipType _type = obj.getType();
    final HDLManipType type = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(type,HDLManipType.ARITH_NEG)) {
        _matched=true;
        HDLExpression _target = obj.getTarget();
        Expression<?> _vHDL = this.toVHDL(_target);
        Minus _minus = new Minus(_vHDL);
        return _minus;
      }
    }
    if (!_matched) {
      boolean _or = false;
      boolean _equals = ObjectExtensions.operator_equals(type, HDLManipType.LOGIC_NEG);
      if (_equals) {
        _or = true;
      } else {
        boolean _equals_1 = ObjectExtensions.operator_equals(type, HDLManipType.BIT_NEG);
        _or = (_equals || _equals_1);
      }
      if (_or) {
        _matched=true;
        HDLExpression _target_1 = obj.getTarget();
        Expression<?> _vHDL_1 = this.toVHDL(_target_1);
        Not _not = new Not(_vHDL_1);
        return _not;
      }
    }
    if (!_matched) {
      if (Objects.equal(type,HDLManipType.CAST)) {
        _matched=true;
        HDLType _castTo = obj.getCastTo();
        final HDLPrimitive targetType = ((HDLPrimitive) _castTo);
        boolean _equals_2 = ObjectExtensions.operator_equals(targetType, HDLPrimitiveType.STRING);
        if (_equals_2) {
          HDLExpression _target_2 = obj.getTarget();
          return this.toVHDL(_target_2);
        }
        final HDLExpression tWidth = targetType.getWidth();
        HDLExpression _target_3 = obj.getTarget();
        HDLClass _classType = _target_3.getClassType();
        boolean _equals_3 = ObjectExtensions.operator_equals(_classType, HDLClass.HDLLiteral);
        if (_equals_3) {
          IHDLObject _container = obj.getContainer();
          HDLExpression _target_4 = obj.getTarget();
          return VHDLCastsLibrary.handleLiteral(_container, ((HDLLiteral) _target_4), targetType, tWidth);
        }
        HDLExpression _target_5 = obj.getTarget();
        Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(_target_5);
        HDLType _get = _typeOf.get();
        final HDLPrimitive t = ((HDLPrimitive) _get);
        HDLExpression _target_6 = obj.getTarget();
        Expression<?> exp = this.toVHDL(_target_6);
        HDLPrimitiveType actualType = t.getType();
        boolean _notEquals = ObjectExtensions.operator_notEquals(tWidth, null);
        if (_notEquals) {
          final TargetType resized = VHDLCastsLibrary.getResize(exp, t, tWidth);
          exp = resized.resized;
          actualType = resized.newType;
        }
        HDLPrimitiveType _type_1 = targetType.getType();
        return VHDLCastsLibrary.cast(exp, actualType, _type_1);
      }
    }
    String _plus = ("Not supported:" + obj);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public Range toVHDL(final HDLRange obj, final Direction dir) {
    HDLExpression _to = obj.getTo();
    HDLExpression _simplifyWidth = HDLPrimitives.simplifyWidth(obj, _to);
    final Expression<?> to = this.toVHDL(_simplifyWidth);
    HDLExpression _from = obj.getFrom();
    boolean _equals = ObjectExtensions.operator_equals(_from, null);
    if (_equals) {
      Range _range = new Range(to, dir, to);
      return _range;
    }
    HDLExpression _from_1 = obj.getFrom();
    HDLExpression _simplifyWidth_1 = HDLPrimitives.simplifyWidth(obj, _from_1);
    Expression<?> _vHDL = this.toVHDL(_simplifyWidth_1);
    Range _range_1 = new Range(_vHDL, dir, to);
    return _range_1;
  }
  
  protected Literal<? extends Object> _toVHDL(final HDLLiteral obj) {
    int length = (-1);
    BigInteger _valueAsBigInt = obj.getValueAsBigInt();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_valueAsBigInt, null);
    if (_notEquals) {
      BigInteger _valueAsBigInt_1 = obj.getValueAsBigInt();
      int _bitLength = _valueAsBigInt_1.bitLength();
      length = _bitLength;
    }
    return this.toVHDL(obj, length, false);
  }
  
  public Literal<? extends Object> toVHDL(final HDLLiteral obj, final int length, final boolean asString) {
    int l = length;
    String sVal = obj.getVal();
    boolean _equals = (l == 0);
    if (_equals) {
      l = 1;
    }
    final BigInteger dec = obj.getValueAsBigInt();
    HDLLiteralPresentation _presentation = obj.getPresentation();
    final HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.STR)) {
        _matched=true;
        StringLiteral _stringLiteral = new StringLiteral(sVal);
        return _stringLiteral;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BOOL)) {
        _matched=true;
        boolean _equals_1 = "true".equals(sVal);
        if (_equals_1) {
          return Standard.BOOLEAN_TRUE;
        }
        return Standard.BOOLEAN_FALSE;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.HEX)) {
        _matched=true;
        if (asString) {
          return VHDLUtils.toHexLiteral(l, dec);
        }
        String _substring = sVal.substring(2);
        String _plus = ("16#" + _substring);
        String _plus_1 = (_plus + "#");
        BasedLiteral _basedLiteral = new BasedLiteral(_plus_1);
        return _basedLiteral;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BIN)) {
        _matched=true;
        if (asString) {
          return VHDLUtils.toBinaryLiteral(l, dec);
        }
        String _substring_1 = sVal.substring(2);
        String _plus_2 = ("2#" + _substring_1);
        String _plus_3 = (_plus_2 + "#");
        BasedLiteral _basedLiteral_1 = new BasedLiteral(_plus_3);
        return _basedLiteral_1;
      }
    }
    boolean _or = false;
    int _bitLength = dec.bitLength();
    boolean _greaterThan = (_bitLength > 31);
    if (_greaterThan) {
      _or = true;
    } else {
      _or = (_greaterThan || asString);
    }
    if (_or) {
      return VHDLUtils.toBinaryLiteral(l, dec);
    }
    DecimalLiteral _decimalLiteral = new DecimalLiteral(sVal);
    return _decimalLiteral;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLShiftOp obj) {
    HDLExpression _left = obj.getLeft();
    Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(_left);
    HDLType _get = _typeOf.get();
    final HDLPrimitive type = ((HDLPrimitive) _get);
    HDLExpression _left_1 = obj.getLeft();
    Expression<?> _vHDL = this.toVHDL(_left_1);
    HDLExpression _right = obj.getRight();
    Expression<?> _vHDL_1 = this.toVHDL(_right);
    HDLPrimitiveType _type = type.getType();
    HDLShiftOpType _type_1 = obj.getType();
    return VHDLShiftLibrary.shift(_vHDL, _vHDL_1, _type, _type_1);
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLEqualityOp obj) {
    HDLEqualityOpType _type = obj.getType();
    final HDLEqualityOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.EQ)) {
        _matched=true;
        HDLExpression _left = obj.getLeft();
        Expression<?> _vHDL = this.toVHDL(_left);
        HDLExpression _right = obj.getRight();
        Expression<?> _vHDL_1 = this.toVHDL(_right);
        Equals _equals = new Equals(_vHDL, _vHDL_1);
        Parentheses _parentheses = new Parentheses(_equals);
        return _parentheses;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER_EQ)) {
        _matched=true;
        HDLExpression _left_1 = obj.getLeft();
        Expression<?> _vHDL_2 = this.toVHDL(_left_1);
        HDLExpression _right_1 = obj.getRight();
        Expression<?> _vHDL_3 = this.toVHDL(_right_1);
        GreaterEquals _greaterEquals = new GreaterEquals(_vHDL_2, _vHDL_3);
        Parentheses _parentheses_1 = new Parentheses(_greaterEquals);
        return _parentheses_1;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER)) {
        _matched=true;
        HDLExpression _left_2 = obj.getLeft();
        Expression<?> _vHDL_4 = this.toVHDL(_left_2);
        HDLExpression _right_2 = obj.getRight();
        Expression<?> _vHDL_5 = this.toVHDL(_right_2);
        GreaterThan _greaterThan = new GreaterThan(_vHDL_4, _vHDL_5);
        Parentheses _parentheses_2 = new Parentheses(_greaterThan);
        return _parentheses_2;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS_EQ)) {
        _matched=true;
        HDLExpression _left_3 = obj.getLeft();
        Expression<?> _vHDL_6 = this.toVHDL(_left_3);
        HDLExpression _right_3 = obj.getRight();
        Expression<?> _vHDL_7 = this.toVHDL(_right_3);
        LessEquals _lessEquals = new LessEquals(_vHDL_6, _vHDL_7);
        Parentheses _parentheses_3 = new Parentheses(_lessEquals);
        return _parentheses_3;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS)) {
        _matched=true;
        HDLExpression _left_4 = obj.getLeft();
        Expression<?> _vHDL_8 = this.toVHDL(_left_4);
        HDLExpression _right_4 = obj.getRight();
        Expression<?> _vHDL_9 = this.toVHDL(_right_4);
        LessThan _lessThan = new LessThan(_vHDL_8, _vHDL_9);
        Parentheses _parentheses_4 = new Parentheses(_lessThan);
        return _parentheses_4;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.NOT_EQ)) {
        _matched=true;
        HDLExpression _left_5 = obj.getLeft();
        Expression<?> _vHDL_10 = this.toVHDL(_left_5);
        HDLExpression _right_5 = obj.getRight();
        Expression<?> _vHDL_11 = this.toVHDL(_right_5);
        NotEquals _notEquals = new NotEquals(_vHDL_10, _vHDL_11);
        Parentheses _parentheses_5 = new Parentheses(_notEquals);
        return _parentheses_5;
      }
    }
    String _plus = ("Not supported:" + obj);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLBitOp obj) {
    HDLBitOpType _type = obj.getType();
    final HDLBitOpType type = _type;
    boolean _matched = false;
    if (!_matched) {
      boolean _or = false;
      boolean _equals = ObjectExtensions.operator_equals(type, HDLBitOpType.AND);
      if (_equals) {
        _or = true;
      } else {
        boolean _equals_1 = ObjectExtensions.operator_equals(type, HDLBitOpType.LOGI_AND);
        _or = (_equals || _equals_1);
      }
      if (_or) {
        _matched=true;
        HDLExpression _left = obj.getLeft();
        Expression<?> _vHDL = this.toVHDL(_left);
        HDLExpression _right = obj.getRight();
        Expression<?> _vHDL_1 = this.toVHDL(_right);
        And _and = new And(_vHDL, _vHDL_1);
        Parentheses _parentheses = new Parentheses(_and);
        return _parentheses;
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _equals_2 = ObjectExtensions.operator_equals(type, HDLBitOpType.OR);
      if (_equals_2) {
        _or_1 = true;
      } else {
        boolean _equals_3 = ObjectExtensions.operator_equals(type, HDLBitOpType.LOGI_OR);
        _or_1 = (_equals_2 || _equals_3);
      }
      if (_or_1) {
        _matched=true;
        HDLExpression _left_1 = obj.getLeft();
        Expression<?> _vHDL_2 = this.toVHDL(_left_1);
        HDLExpression _right_1 = obj.getRight();
        Expression<?> _vHDL_3 = this.toVHDL(_right_1);
        Or _or_2 = new Or(_vHDL_2, _vHDL_3);
        Parentheses _parentheses_1 = new Parentheses(_or_2);
        return _parentheses_1;
      }
    }
    if (!_matched) {
      if (Objects.equal(type,HDLBitOpType.XOR)) {
        _matched=true;
        HDLExpression _left_2 = obj.getLeft();
        Expression<?> _vHDL_4 = this.toVHDL(_left_2);
        HDLExpression _right_2 = obj.getRight();
        Expression<?> _vHDL_5 = this.toVHDL(_right_2);
        Xor _xor = new Xor(_vHDL_4, _vHDL_5);
        Parentheses _parentheses_2 = new Parentheses(_xor);
        return _parentheses_2;
      }
    }
    String _plus = ("Not supported:" + obj);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLArithOp obj) {
    HDLArithOpType _type = obj.getType();
    final HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.PLUS)) {
        _matched=true;
        HDLExpression _left = obj.getLeft();
        Expression<?> _vHDL = this.toVHDL(_left);
        HDLExpression _right = obj.getRight();
        Expression<?> _vHDL_1 = this.toVHDL(_right);
        Add _add = new Add(_vHDL, _vHDL_1);
        Parentheses _parentheses = new Parentheses(_add);
        return _parentheses;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MINUS)) {
        _matched=true;
        HDLExpression _left_1 = obj.getLeft();
        Expression<?> _vHDL_2 = this.toVHDL(_left_1);
        HDLExpression _right_1 = obj.getRight();
        Expression<?> _vHDL_3 = this.toVHDL(_right_1);
        Subtract _subtract = new Subtract(_vHDL_2, _vHDL_3);
        Parentheses _parentheses_1 = new Parentheses(_subtract);
        return _parentheses_1;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.DIV)) {
        _matched=true;
        HDLExpression _left_2 = obj.getLeft();
        Expression<?> _vHDL_4 = this.toVHDL(_left_2);
        HDLExpression _right_2 = obj.getRight();
        Expression<?> _vHDL_5 = this.toVHDL(_right_2);
        Divide _divide = new Divide(_vHDL_4, _vHDL_5);
        Parentheses _parentheses_2 = new Parentheses(_divide);
        return _parentheses_2;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MUL)) {
        _matched=true;
        HDLExpression _left_3 = obj.getLeft();
        Expression<?> _vHDL_6 = this.toVHDL(_left_3);
        HDLExpression _right_3 = obj.getRight();
        Expression<?> _vHDL_7 = this.toVHDL(_right_3);
        Multiply _multiply = new Multiply(_vHDL_6, _vHDL_7);
        Parentheses _parentheses_3 = new Parentheses(_multiply);
        return _parentheses_3;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MOD)) {
        _matched=true;
        HDLExpression _left_4 = obj.getLeft();
        Expression<?> _vHDL_8 = this.toVHDL(_left_4);
        HDLExpression _right_4 = obj.getRight();
        Expression<?> _vHDL_9 = this.toVHDL(_right_4);
        Rem _rem = new Rem(_vHDL_8, _vHDL_9);
        Parentheses _parentheses_4 = new Parentheses(_rem);
        return _parentheses_4;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.POW)) {
        _matched=true;
        HDLExpression _left_5 = obj.getLeft();
        Expression<?> _vHDL_10 = this.toVHDL(_left_5);
        HDLExpression _right_5 = obj.getRight();
        Expression<?> _vHDL_11 = this.toVHDL(_right_5);
        Pow _pow = new Pow(_vHDL_10, _vHDL_11);
        Parentheses _parentheses_5 = new Parentheses(_pow);
        return _parentheses_5;
      }
    }
    String _plus = ("Not supported:" + obj);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLTernary obj) {
    FunctionCall _functionCall = new FunctionCall(VHDLTypesLibrary.TERNARY_SLV);
    final FunctionCall fc = _functionCall;
    final List<AssociationElement> parameters = fc.getParameters();
    HDLExpression _ifExpr = obj.getIfExpr();
    Expression<?> _vHDL = this.toVHDL(_ifExpr);
    AssociationElement _associationElement = new AssociationElement(_vHDL);
    parameters.add(_associationElement);
    HDLExpression _thenExpr = obj.getThenExpr();
    Expression<?> _vHDL_1 = this.toVHDL(_thenExpr);
    AssociationElement _associationElement_1 = new AssociationElement(_vHDL_1);
    parameters.add(_associationElement_1);
    HDLExpression _elseExpr = obj.getElseExpr();
    Expression<?> _vHDL_2 = this.toVHDL(_elseExpr);
    AssociationElement _associationElement_2 = new AssociationElement(_vHDL_2);
    parameters.add(_associationElement_2);
    return fc;
  }
  
  protected Expression<? extends Object> _toVHDL(final HDLFunction obj) {
    String _plus = ("Not supported:" + obj);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public Expression<?> toVHDL(final IHDLObject obj) {
    if (obj instanceof HDLInterfaceRef) {
      return _toVHDL((HDLInterfaceRef)obj);
    } else if (obj instanceof HDLEnumRef) {
      return _toVHDL((HDLEnumRef)obj);
    } else if (obj instanceof HDLVariableRef) {
      return _toVHDL((HDLVariableRef)obj);
    } else if (obj instanceof HDLArithOp) {
      return _toVHDL((HDLArithOp)obj);
    } else if (obj instanceof HDLBitOp) {
      return _toVHDL((HDLBitOp)obj);
    } else if (obj instanceof HDLEqualityOp) {
      return _toVHDL((HDLEqualityOp)obj);
    } else if (obj instanceof HDLFunction) {
      return _toVHDL((HDLFunction)obj);
    } else if (obj instanceof HDLShiftOp) {
      return _toVHDL((HDLShiftOp)obj);
    } else if (obj instanceof HDLArrayInit) {
      return _toVHDL((HDLArrayInit)obj);
    } else if (obj instanceof HDLConcat) {
      return _toVHDL((HDLConcat)obj);
    } else if (obj instanceof HDLFunctionCall) {
      return _toVHDL((HDLFunctionCall)obj);
    } else if (obj instanceof HDLLiteral) {
      return _toVHDL((HDLLiteral)obj);
    } else if (obj instanceof HDLManip) {
      return _toVHDL((HDLManip)obj);
    } else if (obj instanceof HDLReference) {
      return _toVHDL((HDLReference)obj);
    } else if (obj instanceof HDLTernary) {
      return _toVHDL((HDLTernary)obj);
    } else if (obj instanceof HDLExpression) {
      return _toVHDL((HDLExpression)obj);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj).toString());
    }
  }
  
  public String getVHDLName(final HDLVariableRef obj) {
    if (obj instanceof HDLInterfaceRef) {
      return _getVHDLName((HDLInterfaceRef)obj);
    } else if (obj != null) {
      return _getVHDLName(obj);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj).toString());
    }
  }
}
