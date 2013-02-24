package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
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
import de.tuhh.ict.pshdl.model.utils.services.IHDLPrimitive;
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
  
  public static HDLType typeOf(final IHDLObject obj) {
    boolean _isFrozen = obj.isFrozen();
    boolean _not = (!_isFrozen);
    if (_not) {
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Target needs to be frozen");
      throw _illegalArgumentException;
    }
    HDLType res = TypeExtension.INST.determineType(obj);
    boolean _notEquals = (!Objects.equal(res, null));
    if (_notEquals) {
      return res.copyDeepFrozen(obj);
    }
    return res;
  }
  
  /**
   * Attempt to determine the type of this HDLVariable. For this to work it
   * needs to have a valid container.
   * 
   * @return the HDLType if it could be determined, <code>null</code>
   *         otherwise.
   */
  protected HDLType _determineType(final HDLVariable hVar) {
    String _name = hVar.getName();
    boolean _equals = Objects.equal(HDLRegisterConfig.DEF_CLK, _name);
    if (_equals) {
      return HDLPrimitive.getBit();
    }
    String _name_1 = hVar.getName();
    boolean _equals_1 = Objects.equal(HDLRegisterConfig.DEF_RST, _name_1);
    if (_equals_1) {
      return HDLPrimitive.getBit();
    }
    final IHDLObject container = hVar.getContainer();
    boolean _equals_2 = Objects.equal(container, null);
    if (_equals_2) {
      return null;
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
        return ((HDLDirectGeneration) container).getHIf();
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
        return HDLPrimitive.getNatural();
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLInlineFunction)) {
        _matched=true;
        Problem _problem = new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, hVar);
        HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
        throw _hDLProblemException;
      }
    }
    String _plus = ("Failed to resolve type of " + hVar);
    String _plus_1 = (_plus + " caused by an unexpected container: ");
    String _plus_2 = (_plus_1 + container);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus_2);
    throw _illegalArgumentException;
  }
  
  protected HDLType _determineType(final HDLVariableDeclaration hvd) {
    HDLPrimitive _primitive = hvd.getPrimitive();
    boolean _notEquals = (!Objects.equal(_primitive, null));
    if (_notEquals) {
      return hvd.getPrimitive();
    }
    return hvd.resolveType();
  }
  
  protected HDLType _determineType(final HDLArrayInit ai) {
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
        final HDLType sub = this.determineType(exp);
        boolean _equals_1 = sub.equals(exp);
        boolean _not = (!_equals_1);
        if (_not) {
          return sub;
        }
      }
    }
    return res;
  }
  
  protected HDLType _determineType(final HDLExpression cat) {
    HDLClass _classType = cat.getClassType();
    String _plus = ("Did not correctly implement determineType for:" + _classType);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  private static GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new Function0<GenericMeta<Boolean>>() {
    public GenericMeta<Boolean> apply() {
      GenericMeta<Boolean> _genericMeta = new GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", false);
      return _genericMeta;
    }
  }.apply();
  
  protected HDLType _determineType(final HDLUnresolvedFragment cat) {
    boolean _hasMeta = cat.hasMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    if (_hasMeta) {
      return null;
    }
    cat.setMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    IHDLObject _resolveFragment = Insulin.resolveFragment(cat);
    IHDLObject _container = cat.getContainer();
    IHDLObject _copyDeepFrozen = _resolveFragment==null?(IHDLObject)null:_resolveFragment.copyDeepFrozen(_container);
    return _copyDeepFrozen==null?(HDLType)null:this.determineType(_copyDeepFrozen);
  }
  
  protected HDLType _determineType(final HDLConcat cat) {
    ArrayList<HDLExpression> _cats = cat.getCats();
    final Iterator<HDLExpression> iter = _cats.iterator();
    HDLExpression _next = iter.next();
    HDLType _determineType = this.determineType(_next);
    HDLPrimitive type = ((HDLPrimitive) _determineType);
    HDLExpression width = TypeExtension.getWidth(type);
    boolean _hasNext = iter.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        boolean _equals = Objects.equal(width, null);
        if (_equals) {
          return null;
        }
        HDLExpression _next_1 = iter.next();
        HDLType _determineType_1 = this.determineType(_next_1);
        type = ((HDLPrimitive) _determineType_1);
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
    return _setWidth.setContainer(cat);
  }
  
  protected static HDLExpression _getWidth(final IHDLObject obj) {
    final HDLType type = TypeExtension.INST.determineType(obj);
    return TypeExtension.getWidth(type);
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
  
  protected HDLType _determineType(final HDLEnumRef ref) {
    return ref.resolveHEnum();
  }
  
  protected HDLType _determineType(final HDLManip manip) {
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _manipOpType = _instance.getManipOpType(manip);
    return _manipOpType.result;
  }
  
  protected HDLType _determineType(final HDLFunctionCall call) {
    HDLTypeInferenceInfo _inferenceInfo = HDLFunctions.getInferenceInfo(call);
    return _inferenceInfo.result;
  }
  
  protected HDLType _determineType(final HDLLiteral lit) {
    HDLLiteralPresentation _presentation = lit.getPresentation();
    final HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.STR)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive = new HDLPrimitive();
        return _hDLPrimitive.setType(HDLPrimitiveType.STRING);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteralPresentation.BOOL)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive_1 = new HDLPrimitive();
        return _hDLPrimitive_1.setType(HDLPrimitiveType.BOOL);
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
      return _uint.setWidth(_get);
    }
    return HDLPrimitive.target(isSigned);
  }
  
  protected HDLType _determineType(final HDLVariableRef ref) {
    final List<HDLRange> bits = ref.getBits();
    int _size = bits.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      HDLVariable _resolveVar = ref.resolveVar();
      return _resolveVar==null?(HDLType)null:this.determineType(_resolveVar);
    }
    boolean _and = false;
    int _size_1 = bits.size();
    boolean _equals_1 = (_size_1 == 1);
    if (!_equals_1) {
      _and = false;
    } else {
      HDLRange _get = bits.get(0);
      HDLExpression _from = _get.getFrom();
      boolean _equals_2 = Objects.equal(_from, null);
      _and = (_equals_1 && _equals_2);
    }
    if (_and) {
      return HDLPrimitive.getBit();
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
    HDLVariable _resolveVar_1 = ref.resolveVar();
    HDLType _determineType = _resolveVar_1==null?(HDLType)null:this.determineType(_resolveVar_1);
    return ((HDLPrimitive) _determineType).setWidth(width);
  }
  
  protected HDLType _determineType(final HDLArithOp aop) {
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _arithOpType = _instance.getArithOpType(aop);
    return _arithOpType.result;
  }
  
  protected HDLType _determineType(final HDLBitOp bop) {
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _bitOpType = _instance.getBitOpType(bop);
    return _bitOpType.result;
  }
  
  protected HDLType _determineType(final HDLShiftOp sop) {
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _shiftOpType = _instance.getShiftOpType(sop);
    return _shiftOpType.result;
  }
  
  protected HDLType _determineType(final HDLEqualityOp eop) {
    IHDLPrimitive _instance = HDLPrimitives.getInstance();
    HDLTypeInferenceInfo _equalityOpType = _instance.getEqualityOpType(eop);
    return _equalityOpType.result;
  }
  
  protected HDLType _determineType(final HDLTernary tern) {
    HDLExpression _thenExpr = tern.getThenExpr();
    return this.determineType(_thenExpr);
  }
  
  protected HDLType _determineType(final HDLInlineFunction func) {
    Problem _problem = new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, func);
    HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
    throw _hDLProblemException;
  }
  
  public HDLType determineType(final IHDLObject ref) {
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
