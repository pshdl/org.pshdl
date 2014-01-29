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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.types.builtIn.HDLFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.utils.services.HDLTypeInferenceInfo;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

@SuppressWarnings("all")
public class TypeExtension {
  private static TypeExtension INST = new Function0<TypeExtension>() {
    public TypeExtension apply() {
      TypeExtension _typeExtension = new TypeExtension();
      return _typeExtension;
    }
  }.apply();
  
  public static Optional<? extends HDLType> typeOf(final IHDLObject obj) {
    Optional<? extends HDLType> _xblockexpression = null;
    {
      boolean _isFrozen = obj.isFrozen();
      boolean _not = (!_isFrozen);
      if (_not) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Target needs to be frozen");
        throw _illegalArgumentException;
      }
      Optional<? extends HDLType> _cachedType = TypeExtension.cachedType(obj);
      _xblockexpression = (_cachedType);
    }
    return _xblockexpression;
  }
  
  private static Optional<? extends HDLType> cachedType(final IHDLObject obj) {
    final Optional<? extends HDLType> type = TypeExtension.INST.determineType(obj);
    return type;
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
        Optional<? extends HDLType> _cachedType = TypeExtension.cachedType(((HDLVariableDeclaration) container));
        ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
        return this.attachDim(_cachedType, _dimensions);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLDirectGeneration)) {
        _matched=true;
        HDLInterface _hIf = ((HDLDirectGeneration) container).getHIf();
        Optional<HDLInterface> _fromNullable = Optional.<HDLInterface>fromNullable(_hIf);
        ArrayList<HDLExpression> _dimensions_1 = hVar.getDimensions();
        return this.attachDim(_fromNullable, _dimensions_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLInterfaceInstantiation)) {
        _matched=true;
        Optional<HDLInterface> _resolveHIf = ((HDLInterfaceInstantiation) container).resolveHIf();
        ArrayList<HDLExpression> _dimensions_2 = hVar.getDimensions();
        return this.attachDim(_resolveHIf, _dimensions_2);
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
  
  public Optional<? extends HDLType> attachDim(final Optional<? extends HDLType> optional, final ArrayList<HDLExpression> expressions) {
    boolean _isPresent = optional.isPresent();
    if (_isPresent) {
      HDLType _get = optional.get();
      HDLType _setDim = _get.setDim(expressions);
      return Optional.<HDLType>of(_setDim);
    }
    return optional;
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
      return TypeExtension.cachedType(_get);
    }
    HDLPrimitive res = HDLPrimitive.getNatural();
    ArrayList<HDLExpression> _exp_2 = ai.getExp();
    for (final HDLExpression exp : _exp_2) {
      {
        final Optional<? extends HDLType> sub = TypeExtension.cachedType(exp);
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
  
  private static HDLObject.GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new Function0<HDLObject.GenericMeta<Boolean>>() {
    public HDLObject.GenericMeta<Boolean> apply() {
      HDLObject.GenericMeta<Boolean> _genericMeta = new HDLObject.GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", 
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
    return TypeExtension.cachedType(_copyDeepFrozen);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLConcat cat) {
    ArrayList<HDLExpression> _cats = cat.getCats();
    final Iterator<HDLExpression> iter = _cats.iterator();
    HDLExpression _next = iter.next();
    final Optional<? extends HDLType> nextType = TypeExtension.cachedType(_next);
    boolean _isPresent = nextType.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    HDLType type = nextType.get();
    if ((!(type instanceof HDLPrimitive))) {
      return Optional.<HDLType>absent();
    }
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
        final Optional<? extends HDLType> nextCatType = TypeExtension.cachedType(_next_1);
        boolean _isPresent_1 = nextCatType.isPresent();
        boolean _not_1 = (!_isPresent_1);
        if (_not_1) {
          return Optional.<HDLType>absent();
        }
        HDLType _get = nextCatType.get();
        type = _get;
        if ((!(type instanceof HDLPrimitive))) {
          return Optional.<HDLType>absent();
        }
        final HDLExpression tWidth = TypeExtension.getWidth(type);
        boolean _tripleEquals_1 = (tWidth == null);
        if (_tripleEquals_1) {
          return Optional.<HDLType>absent();
        }
        HDLArithOp _hDLArithOp = new HDLArithOp();
        HDLArithOp _setLeft = _hDLArithOp.setLeft(width);
        HDLArithOp _setType = _setLeft.setType(HDLArithOp.HDLArithOpType.PLUS);
        HDLArithOp _setRight = _setType.setRight(tWidth);
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
  
  protected static HDLExpression _getWidth(final HDLEnum type) {
    return null;
  }
  
  protected static HDLExpression _getWidth(final HDLInterface type) {
    return null;
  }
  
  protected static HDLExpression _getWidth(final HDLPrimitive type) {
    final HDLExpression width = type.getWidth();
    HDLPrimitive.HDLPrimitiveType _type = type.getType();
    boolean _equals = Objects.equal(_type, HDLPrimitive.HDLPrimitiveType.BIT);
    if (_equals) {
      return HDLLiteral.get(1);
    }
    return width;
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLEnum ref) {
    return Optional.<HDLEnum>of(ref);
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
    HDLType _result = null;
    if (_inferenceInfo!=null) {
      _result=_inferenceInfo.result;
    }
    return Optional.<HDLType>fromNullable(_result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLLiteral lit) {
    HDLLiteral.HDLLiteralPresentation _presentation = lit.getPresentation();
    final HDLLiteral.HDLLiteralPresentation _switchValue = _presentation;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteral.HDLLiteralPresentation.STR)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive = new HDLPrimitive();
        HDLPrimitive _setType = _hDLPrimitive.setType(HDLPrimitive.HDLPrimitiveType.STRING);
        return Optional.<HDLPrimitive>of(_setType);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLLiteral.HDLLiteralPresentation.BOOL)) {
        _matched=true;
        HDLPrimitive _hDLPrimitive_1 = new HDLPrimitive();
        HDLPrimitive _setType_1 = _hDLPrimitive_1.setType(HDLPrimitive.HDLPrimitiveType.BOOL);
        return Optional.<HDLPrimitive>of(_setType_1);
      }
    }
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
    boolean _isNegative = lit.isNegative();
    boolean _not = (!_isNegative);
    HDLPrimitive _target = HDLPrimitive.target(_not);
    return Optional.<HDLPrimitive>of(_target);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLVariableRef ref) {
    final List<HDLRange> bits = ref.getBits();
    int _size = bits.size();
    boolean _equals = (_size == 0);
    if (_equals) {
      Optional<HDLVariable> res = ref.resolveVar();
      final ArrayList<HDLExpression> arr = ref.getArray();
      boolean _isPresent = res.isPresent();
      if (_isPresent) {
        HDLVariable _get = res.get();
        final Optional<? extends HDLType> opType = TypeExtension.cachedType(_get);
        boolean _isPresent_1 = opType.isPresent();
        if (_isPresent_1) {
          final HDLType type = opType.get();
          final ArrayList<HDLExpression> dim = type.getDim();
          int _length = ((Object[])Conversions.unwrapArray(arr, Object.class)).length;
          int _length_1 = ((Object[])Conversions.unwrapArray(dim, Object.class)).length;
          boolean _greaterThan = (_length > _length_1);
          if (_greaterThan) {
            return opType;
          }
          int _length_2 = ((Object[])Conversions.unwrapArray(arr, Object.class)).length;
          int _length_3 = ((Object[])Conversions.unwrapArray(dim, Object.class)).length;
          List<HDLExpression> _subList = dim.subList(_length_2, _length_3);
          HDLType _setDim = type.setDim(_subList);
          return Optional.<HDLType>of(_setDim);
        }
        return Optional.<HDLType>absent();
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
        HDLArithOp _setType = _setLeft.setType(HDLArithOp.HDLArithOpType.PLUS);
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
    boolean _isPresent_2 = hVar.isPresent();
    boolean _not = (!_isPresent_2);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    HDLVariable _get_2 = hVar.get();
    final Optional<? extends HDLType> type_1 = TypeExtension.cachedType(_get_2);
    boolean _isPresent_3 = type_1.isPresent();
    boolean _not_1 = (!_isPresent_3);
    if (_not_1) {
      return Optional.<HDLType>absent();
    }
    HDLType _get_3 = type_1.get();
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
    return TypeExtension.cachedType(_thenExpr);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLInlineFunction func) {
    Problem _problem = new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, func);
    HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
    throw _hDLProblemException;
  }
  
  public Optional<? extends HDLType> determineType(final IHDLObject ref) {
    if (ref instanceof HDLEnum) {
      return _determineType((HDLEnum)ref);
    } else if (ref instanceof HDLEnumRef) {
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
    if (type instanceof HDLEnum) {
      return _getWidth((HDLEnum)type);
    } else if (type instanceof HDLPrimitive) {
      return _getWidth((HDLPrimitive)type);
    } else if (type instanceof HDLInterface) {
      return _getWidth((HDLInterface)type);
    } else if (type != null) {
      return _getWidth(type);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(type).toString());
    }
  }
}
