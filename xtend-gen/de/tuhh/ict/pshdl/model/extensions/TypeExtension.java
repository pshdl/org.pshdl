package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLArrayInit;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLDirectGeneration;
import de.tuhh.ict.pshdl.model.HDLEnumRef;
import de.tuhh.ict.pshdl.model.HDLEqualityOp;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLInlineFunction;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLLiteral.HDLLiteralPresentation;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLTernary;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives;
import de.tuhh.ict.pshdl.model.utils.HDLProblemException;
import de.tuhh.ict.pshdl.model.utils.Insulin;
import de.tuhh.ict.pshdl.model.utils.services.HDLTypeInferenceInfo;
import de.tuhh.ict.pshdl.model.validation.Problem;
import de.tuhh.ict.pshdl.model.validation.builtin.ErrorCode;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class TypeExtension {
  public static TypeExtension INST = new Function0<TypeExtension>() {
    public TypeExtension apply() {
      TypeExtension _typeExtension = new TypeExtension();
      return _typeExtension;
    }
  }.apply();
  
  public static Optional<? extends HDLType> typeOf(final IHDLObject obj) {
    boolean _isFrozen = obj.isFrozen();
    boolean _not = (!_isFrozen);
    if (_not) {
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Target needs to be frozen");
      throw _illegalArgumentException;
    }
    Optional<? extends HDLType> res = TypeExtension.INST.determineType(obj);
    boolean _isPresent = res.isPresent();
    if (_isPresent) {
      HDLType _get = res.get();
      HDLType _copyDeepFrozen = _get.copyDeepFrozen(obj);
      return Optional.<HDLType>of(_copyDeepFrozen);
    }
    return Optional.<HDLType>absent();
  }
  
  /**
   * Attempt to determine the type of this HDLVariable. For this to work it
   * needs to have a valid container.
   * 
   * @return the HDLType if it could be determined, <code>null</code>
   *         otherwise.
   */
  protected Optional<? extends HDLType> _determineType(final HDLVariable hVar) {
    String _name = hVar.getName();
    boolean _equals = Objects.equal(HDLRegisterConfig.DEF_CLK, _name);
    if (_equals) {
      HDLPrimitive _bit = HDLPrimitive.getBit();
      return Optional.<HDLPrimitive>of(_bit);
    }
    String _name_1 = hVar.getName();
    boolean _equals_1 = Objects.equal(HDLRegisterConfig.DEF_RST, _name_1);
    if (_equals_1) {
      HDLPrimitive _bit_1 = HDLPrimitive.getBit();
      return Optional.<HDLPrimitive>of(_bit_1);
    }
    final IHDLObject container = hVar.getContainer();
    boolean _tripleEquals = (container == null);
    if (_tripleEquals) {
      return Optional.<HDLType>absent();
    }
    HDLClass _classType = container.getClassType();
    final HDLClass _switchValue = _classType;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLVariableDeclaration)) {
        _matched=true;
        return this.determineType(((HDLVariableDeclaration) container));
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLDirectGeneration)) {
        _matched=true;
        HDLInterface _hIf = ((HDLDirectGeneration) container).getHIf();
        return Optional.<HDLInterface>of(_hIf);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLInterfaceInstantiation)) {
        _matched=true;
        return ((HDLInterfaceInstantiation) container).resolveHIf();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLForLoop)) {
        _matched=true;
        HDLPrimitive _natural = HDLPrimitive.getNatural();
        return Optional.<HDLPrimitive>of(_natural);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLInlineFunction)) {
        _matched=true;
        return Optional.<HDLType>absent();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLSubstituteFunction)) {
        _matched=true;
        return Optional.<HDLType>absent();
      }
    }
    return Optional.<HDLType>absent();
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLVariableDeclaration hvd) {
    HDLPrimitive _primitive = hvd.getPrimitive();
    boolean _tripleNotEquals = (_primitive != null);
    if (_tripleNotEquals) {
      HDLPrimitive _primitive_1 = hvd.getPrimitive();
      return Optional.<HDLPrimitive>of(_primitive_1);
    }
    return hvd.resolveType();
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLArrayInit ai) {
    ArrayList<HDLExpression> _exp = ai.getExp();
    int _size = _exp.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      ArrayList<HDLExpression> _exp_1 = ai.getExp();
      HDLExpression _get = _exp_1.get(0);
      return this.determineType(_get);
    }
    HDLPrimitive res = HDLPrimitive.getNatural();
    ArrayList<HDLExpression> _exp_2 = ai.getExp();
    for (final HDLExpression exp : _exp_2) {
      {
        final Optional<? extends HDLType> sub = this.determineType(exp);
        boolean _and = false;
        boolean _isPresent = sub.isPresent();
        if (!_isPresent) {
          _and = false;
        } else {
          HDLType _get_1 = sub.get();
          boolean _equals_1 = _get_1.equals(res);
          boolean _not = (!_equals_1);
          _and = (_isPresent && _not);
        }
        if (_and) {
          return sub;
        }
      }
    }
    return Optional.<HDLPrimitive>of(res);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLExpression cat) {
    HDLClass _classType = cat.getClassType();
    String _plus = ("Did not correctly implement determineType for:" + _classType);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  private static GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new Function0<GenericMeta<Boolean>>() {
    public GenericMeta<Boolean> apply() {
      GenericMeta<Boolean> _genericMeta = new GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", 
        false);
      return _genericMeta;
    }
  }.apply();
  
  protected Optional<? extends HDLType> _determineType(final HDLUnresolvedFragment cat) {
    boolean _hasMeta = cat.hasMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    if (_hasMeta) {
      return Optional.<HDLType>absent();
    }
    cat.setMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    Optional<? extends IHDLObject> resolved = Insulin.resolveFragment(cat);
    boolean _isPresent = resolved.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    IHDLObject _get = resolved.get();
    IHDLObject _container = cat.getContainer();
    IHDLObject _copyDeepFrozen = _get.copyDeepFrozen(_container);
    return this.determineType(_copyDeepFrozen);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLConcat cat) {
    ArrayList<HDLExpression> _cats = cat.getCats();
    final Iterator<HDLExpression> iter = _cats.iterator();
    HDLExpression _next = iter.next();
    final Optional<? extends HDLType> nextType = this.determineType(_next);
    boolean _isPresent = nextType.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    HDLType _get = nextType.get();
    HDLPrimitive type = ((HDLPrimitive) _get);
    HDLExpression width = TypeExtension.getWidth(type);
    boolean _hasNext = iter.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        boolean _tripleEquals = (width == null);
        if (_tripleEquals) {
          return Optional.<HDLType>absent();
        }
        HDLExpression _next_1 = iter.next();
        final Optional<? extends HDLType> nextCatType = this.determineType(_next_1);
        boolean _isPresent_1 = nextCatType.isPresent();
        boolean _not_1 = (!_isPresent_1);
        if (_not_1) {
          return Optional.<HDLType>absent();
        }
        HDLType _get_1 = nextCatType.get();
        type = ((HDLPrimitive) _get_1);
        HDLArithOp _hDLArithOp = new HDLArithOp();
        HDLArithOp _setLeft = _hDLArithOp.setLeft(width);
        HDLArithOp _setType = _setLeft.setType(HDLArithOpType.PLUS);
        HDLExpression _width = TypeExtension.getWidth(type);
        HDLArithOp _setRight = _setType.setRight(_width);
        width = _setRight;
        HDLExpression _simplifyWidth = HDLPrimitives.simplifyWidth(cat, width);
        width = _simplifyWidth;
      }
      boolean _hasNext_1 = iter.hasNext();
      _while = _hasNext_1;
    }
    HDLPrimitive _bitvector = HDLPrimitive.getBitvector();
    HDLPrimitive _setWidth = _bitvector.setWidth(width);
    HDLPrimitive _setContainer = _setWidth.setContainer(cat);
    return Optional.<HDLPrimitive>of(_setContainer);
  }
  
  protected static HDLExpression _getWidth(final IHDLObject obj) {
    final Optional<? extends HDLType> type = TypeExtension.INST.determineType(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return null;
    }
    HDLType _get = type.get();
    return TypeExtension.getWidth(_get);
  }
  
  protected static HDLExpression _getWidth(final HDLPrimitive type) {
    final HDLExpression width = type.getWidth();
    HDLPrimitiveType _type = type.getType();
    boolean _equals = Objects.equal(_type, HDLPrimitiveType.BIT);
    if (_equals) {
      return HDLLiteral.get(1);
    }
    return width;
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLEnumRef ref) {
    return ref.resolveHEnum();
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLManip manip) {
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _manipOpType = _instance.getManipOpType(manip);
    return Optional.<HDLType>fromNullable(_manipOpType.result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLFunctionCall call) {
    HDLTypeInferenceInfo _inferenceInfo = HDLFunctions.getInferenceInfo(call);
    HDLType _result = _inferenceInfo==null?(HDLType)null:_inferenceInfo.result;
    return Optional.<HDLType>fromNullable(_result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLLiteral lit) {
    HDLLiteralPresentation _presentation = lit.getPresentation();
    final HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.STR)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive = new HDLPrimitive();
        HDLPrimitive _setType = _hDLPrimitive.setType(HDLPrimitiveType.STRING);
        return Optional.<HDLPrimitive>of(_setType);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BOOL)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive_1 = new HDLPrimitive();
        HDLPrimitive _setType_1 = _hDLPrimitive_1.setType(HDLPrimitiveType.BOOL);
        return Optional.<HDLPrimitive>of(_setType_1);
      }
    }
    String _val = lit.getVal();
    char _charAt = _val.charAt(0);
    final boolean isSigned = (!Objects.equal(Character.valueOf(_charAt), "-"));
    final BigInteger bigVal = lit.getValueAsBigInt();
    int _bitLength = bigVal.bitLength();
    boolean _greaterThan = (_bitLength > 31);
    if (_greaterThan) {
      HDLPrimitive _uint = HDLPrimitive.getUint();
      int _bitLength_1 = bigVal.bitLength();
      HDLLiteral _get = HDLLiteral.get(_bitLength_1);
      HDLPrimitive _setWidth = _uint.setWidth(_get);
      return Optional.<HDLPrimitive>of(_setWidth);
    }
    HDLPrimitive _target = HDLPrimitive.target(isSigned);
    return Optional.<HDLPrimitive>of(_target);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLVariableRef ref) {
    final List<HDLRange> bits = ref.getBits();
    int _size = bits.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      Optional<HDLVariable> res = ref.resolveVar();
      boolean _isPresent = res.isPresent();
      if (_isPresent) {
        HDLVariable _get = res.get();
        return this.determineType(_get);
      } else {
        return Optional.<HDLType>absent();
      }
    }
    boolean _and = false;
    int _size_1 = bits.size();
    boolean _equals_1 = (_size_1 == 1);
    if (!_equals_1) {
      _and = false;
    } else {
      HDLRange _get_1 = bits.get(0);
      HDLExpression _from = _get_1.getFrom();
      boolean _tripleEquals = (_from == null);
      _and = (_equals_1 && _tripleEquals);
    }
    if (_and) {
      HDLPrimitive _bit = HDLPrimitive.getBit();
      return Optional.<HDLPrimitive>of(_bit);
    }
    final Iterator<HDLRange> iter = bits.iterator();
    HDLRange _next = iter.next();
    HDLExpression _width = _next.getWidth();
    HDLExpression width = HDLPrimitives.simplifyWidth(ref, _width);
    boolean _hasNext = iter.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        HDLArithOp _hDLArithOp = new HDLArithOp();
        HDLArithOp _setLeft = _hDLArithOp.setLeft(width);
        HDLArithOp _setType = _setLeft.setType(HDLArithOpType.PLUS);
        HDLRange _next_1 = iter.next();
        HDLExpression _width_1 = _next_1.getWidth();
        HDLArithOp _setRight = _setType.setRight(_width_1);
        width = _setRight;
        HDLExpression _simplifyWidth = HDLPrimitives.simplifyWidth(ref, width);
        width = _simplifyWidth;
      }
      boolean _hasNext_1 = iter.hasNext();
      _while = _hasNext_1;
    }
    final Optional<HDLVariable> hVar = ref.resolveVar();
    boolean _isPresent_1 = hVar.isPresent();
    boolean _not = (!_isPresent_1);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    HDLVariable _get_2 = hVar.get();
    final Optional<? extends HDLType> type = this.determineType(_get_2);
    boolean _isPresent_2 = type.isPresent();
    boolean _not_1 = (!_isPresent_2);
    if (_not_1) {
      return Optional.<HDLType>absent();
    }
    HDLType _get_3 = type.get();
    HDLPrimitive _setWidth = ((HDLPrimitive) _get_3).setWidth(width);
    return Optional.<HDLPrimitive>of(_setWidth);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLArithOp aop) {
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _arithOpType = _instance.getArithOpType(aop);
    return Optional.<HDLType>fromNullable(_arithOpType.result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLBitOp bop) {
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _bitOpType = _instance.getBitOpType(bop);
    return Optional.<HDLType>fromNullable(_bitOpType.result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLShiftOp sop) {
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _shiftOpType = _instance.getShiftOpType(sop);
    return Optional.<HDLType>fromNullable(_shiftOpType.result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLEqualityOp eop) {
    HDLPrimitives _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _equalityOpType = _instance.getEqualityOpType(eop);
    return Optional.<HDLType>fromNullable(_equalityOpType.result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLTernary tern) {
    HDLExpression _thenExpr = tern.getThenExpr();
    return this.determineType(_thenExpr);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLInlineFunction func) {
    Problem _problem = new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, func);
    HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
    throw _hDLProblemException;
  }
  
  public Optional<? extends HDLType> determineType(final IHDLObject ref) {
    if (ref instanceof HDLEnumRef) {
      return _determineType((HDLEnumRef)ref);
    } else if (ref instanceof HDLInlineFunction) {
      return _determineType((HDLInlineFunction)ref);
    } else if (ref instanceof HDLVariableRef) {
      return _determineType((HDLVariableRef)ref);
    } else if (ref instanceof HDLArithOp) {
      return _determineType((HDLArithOp)ref);
    } else if (ref instanceof HDLBitOp) {
      return _determineType((HDLBitOp)ref);
    } else if (ref instanceof HDLEqualityOp) {
      return _determineType((HDLEqualityOp)ref);
    } else if (ref instanceof HDLShiftOp) {
      return _determineType((HDLShiftOp)ref);
    } else if (ref instanceof HDLUnresolvedFragment) {
      return _determineType((HDLUnresolvedFragment)ref);
    } else if (ref instanceof HDLVariableDeclaration) {
      return _determineType((HDLVariableDeclaration)ref);
    } else if (ref instanceof HDLArrayInit) {
      return _determineType((HDLArrayInit)ref);
    } else if (ref instanceof HDLConcat) {
      return _determineType((HDLConcat)ref);
    } else if (ref instanceof HDLFunctionCall) {
      return _determineType((HDLFunctionCall)ref);
    } else if (ref instanceof HDLLiteral) {
      return _determineType((HDLLiteral)ref);
    } else if (ref instanceof HDLManip) {
      return _determineType((HDLManip)ref);
    } else if (ref instanceof HDLTernary) {
      return _determineType((HDLTernary)ref);
    } else if (ref instanceof HDLVariable) {
      return _determineType((HDLVariable)ref);
    } else if (ref instanceof HDLExpression) {
      return _determineType((HDLExpression)ref);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref).toString());
    }
  }
  
  public static HDLExpression getWidth(final IHDLObject type) {
    if (type instanceof HDLPrimitive) {
      return _getWidth((HDLPrimitive)type);
    } else if (type != null) {
      return _getWidth(type);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(type).toString());
    }
  }
}
