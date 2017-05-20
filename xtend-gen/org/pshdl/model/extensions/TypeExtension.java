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
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
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
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

@SuppressWarnings("all")
public class TypeExtension {
  private static TypeExtension INST = new TypeExtension();
  
  public static HDLType typeOfForced(final IHDLObject obj, final String stage) {
    final Optional<? extends HDLType> res = TypeExtension.typeOf(obj);
    boolean _isPresent = res.isPresent();
    if (_isPresent) {
      return res.get();
    }
    throw new HDLCodeGenerationException(obj, ("Failed to resolve type of:" + obj), stage);
  }
  
  public static Optional<? extends HDLType> typeOf(final IHDLObject obj) {
    Optional<? extends HDLType> _xblockexpression = null;
    {
      boolean _isFrozen = obj.isFrozen();
      boolean _not = (!_isFrozen);
      if (_not) {
        throw new HDLCodeGenerationException(obj, "The object is not frozen", "TYPE");
      }
      _xblockexpression = TypeExtension.cachedType(obj);
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
      return Optional.<HDLPrimitive>of(HDLPrimitive.getBit());
    }
    String _name_1 = hVar.getName();
    boolean _equals_1 = Objects.equal(HDLRegisterConfig.DEF_RST, _name_1);
    if (_equals_1) {
      return Optional.<HDLPrimitive>of(HDLPrimitive.getBit());
    }
    final IHDLObject container = hVar.getContainer();
    if ((container == null)) {
      return Optional.<HDLType>absent();
    }
    HDLClass _classType = container.getClassType();
    if (_classType != null) {
      switch (_classType) {
        case HDLVariableDeclaration:
          return this.attachDim(TypeExtension.cachedType(((HDLVariableDeclaration) container)), hVar.getDimensions());
        case HDLDirectGeneration:
          return this.attachDim(Optional.<HDLInterface>fromNullable(((HDLDirectGeneration) container).getHIf()), hVar.getDimensions());
        case HDLInterfaceInstantiation:
          return this.attachDim(((HDLInterfaceInstantiation) container).resolveHIf(), hVar.getDimensions());
        case HDLForLoop:
          return Optional.<HDLPrimitive>of(HDLPrimitive.getNatural());
        case HDLInlineFunction:
          return Optional.<HDLType>absent();
        case HDLSubstituteFunction:
          return Optional.<HDLType>absent();
        default:
          break;
      }
    }
    return Optional.<HDLType>absent();
  }
  
  public Optional<? extends HDLType> attachDim(final Optional<? extends HDLType> optional, final ArrayList<HDLExpression> expressions) {
    boolean _isPresent = optional.isPresent();
    if (_isPresent) {
      return Optional.<HDLType>of(optional.get().setDim(expressions));
    }
    return optional;
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLVariableDeclaration hvd) {
    HDLPrimitive _primitive = hvd.getPrimitive();
    boolean _tripleNotEquals = (_primitive != null);
    if (_tripleNotEquals) {
      return Optional.<HDLPrimitive>of(hvd.getPrimitive());
    }
    return hvd.resolveType();
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLArrayInit ai) {
    int _size = ai.getExp().size();
    boolean _equals = (_size == 1);
    if (_equals) {
      return TypeExtension.cachedType(ai.getExp().get(0));
    }
    HDLPrimitive res = HDLPrimitive.getNatural();
    ArrayList<HDLExpression> _exp = ai.getExp();
    for (final HDLExpression exp : _exp) {
      {
        final Optional<? extends HDLType> sub = TypeExtension.cachedType(exp);
        if ((sub.isPresent() && (!sub.get().equals(res)))) {
          return sub;
        }
      }
    }
    return Optional.<HDLPrimitive>of(res);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLExpression cat) {
    HDLClass _classType = cat.getClassType();
    String _plus = ("Did not correctly implement determineType for:" + _classType);
    throw new RuntimeException(_plus);
  }
  
  private static HDLObject.GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new HDLObject.GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", 
    false);
  
  protected Optional<? extends HDLType> _determineType(final HDLUnresolvedFragment cat) {
    boolean _hasMeta = cat.hasMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    if (_hasMeta) {
      return Optional.<HDLType>absent();
    }
    cat.setMeta(TypeExtension.DETERMINE_TYPE_RESOLVE);
    Optional<Insulin.ResolvedPart> resolved = Insulin.resolveFragment(cat);
    boolean _isPresent = resolved.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    return TypeExtension.cachedType(resolved.get().obj.copyDeepFrozen(cat.getContainer()));
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLConcat cat) {
    final Iterator<HDLExpression> iter = cat.getCats().iterator();
    final Optional<? extends HDLType> nextType = TypeExtension.cachedType(iter.next());
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
    while (iter.hasNext()) {
      {
        if ((width == null)) {
          return Optional.<HDLType>absent();
        }
        final Optional<? extends HDLType> nextCatType = TypeExtension.cachedType(iter.next());
        boolean _isPresent_1 = nextCatType.isPresent();
        boolean _not_1 = (!_isPresent_1);
        if (_not_1) {
          return Optional.<HDLType>absent();
        }
        type = nextCatType.get();
        if ((!(type instanceof HDLPrimitive))) {
          return Optional.<HDLType>absent();
        }
        final HDLExpression tWidth = TypeExtension.getWidth(type);
        if ((tWidth == null)) {
          return Optional.<HDLType>absent();
        }
        width = HDLArithOp.add(width, tWidth);
        width = HDLPrimitives.simplifyWidth(cat, width, null);
      }
    }
    return Optional.<HDLPrimitive>of(HDLPrimitive.getBitvector().setWidth(width).setContainer(cat));
  }
  
  protected static HDLExpression _getWidth(final IHDLObject obj) {
    final Optional<? extends HDLType> type = TypeExtension.INST.determineType(obj);
    boolean _isPresent = type.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return null;
    }
    return TypeExtension.getWidth(type.get());
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
    return Optional.<HDLType>fromNullable(HDLPrimitives.getInstance().getManipOpType(manip).result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLFunctionCall call) {
    final Optional<HDLFunction> funcOpt = call.resolveFunction();
    boolean _isPresent = funcOpt.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    final HDLFunction func = funcOpt.get();
    HDLFunctionParameter _returnType = func.getReturnType();
    boolean _tripleEquals = (_returnType == null);
    if (_tripleEquals) {
      return Optional.<HDLType>absent();
    }
    final Optional<? extends HDLType> returnType = this.determineType(func.getReturnType());
    if ((returnType.isPresent() && (returnType.get() instanceof HDLPrimitive.AnyPrimitive))) {
      final Optional<? extends HDLType> specified = HDLFunctions.specifyReturnType(func, call, null);
      boolean _isPresent_1 = specified.isPresent();
      if (_isPresent_1) {
        return specified;
      }
    }
    return returnType;
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLFunctionParameter param) {
    HDLFunctionParameter.Type _type = param.getType();
    if (_type != null) {
      switch (_type) {
        case PARAM_ANY_BIT:
          return Optional.<HDLPrimitive.AnyPrimitive>of(HDLPrimitive.anyBit());
        case PARAM_ANY_INT:
          return Optional.<HDLPrimitive.AnyPrimitive>of(HDLPrimitive.anyInt());
        case PARAM_ANY_UINT:
          return Optional.<HDLPrimitive.AnyPrimitive>of(HDLPrimitive.anyUint());
        case PARAM_ANY_ENUM:
          return Optional.<HDLEnum.AnyEnum>of(HDLEnum.anyEnum());
        case PARAM_ANY_IF:
          return Optional.<HDLInterface.AnyInterface>of(HDLInterface.anyInterface());
        case PARAM_BOOL:
          return Optional.<HDLPrimitive>of(HDLPrimitive.getBool());
        case PARAM_ENUM:
          return param.resolveEnumSpec();
        case PARAM_FUNCTION:
          return Optional.<HDLType>absent();
        case PARAM_IF:
          return param.resolveIfSpec();
        case PARAM_BIT:
          HDLExpression _width = param.getWidth();
          boolean _tripleEquals = (_width == null);
          if (_tripleEquals) {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getBit());
          } else {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getBitvector().setWidth(param.getWidth()));
          }
        case PARAM_INT:
          HDLExpression _width_1 = param.getWidth();
          boolean _tripleEquals_1 = (_width_1 == null);
          if (_tripleEquals_1) {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getInteger());
          } else {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getInt().setWidth(param.getWidth()));
          }
        case PARAM_UINT:
          HDLExpression _width_2 = param.getWidth();
          boolean _tripleEquals_2 = (_width_2 == null);
          if (_tripleEquals_2) {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getNatural());
          } else {
            return Optional.<HDLPrimitive>of(HDLPrimitive.getUint().setWidth(param.getWidth()));
          }
        case PARAM_STRING:
          return Optional.<HDLPrimitive>of(HDLPrimitive.getString());
        default:
          break;
      }
    }
    return Optional.<HDLType>absent();
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLLiteral lit) {
    Boolean _str = lit.getStr();
    if ((_str).booleanValue()) {
      return Optional.<HDLPrimitive>of(HDLPrimitive.getString());
    }
    HDLLiteral.HDLLiteralPresentation _presentation = lit.getPresentation();
    boolean _tripleEquals = (_presentation == HDLLiteral.HDLLiteralPresentation.BOOL);
    if (_tripleEquals) {
      return Optional.<HDLPrimitive>of(HDLPrimitive.getBool());
    }
    final BigInteger bigVal = lit.getValueAsBigInt();
    int _bitLength = bigVal.bitLength();
    boolean _greaterThan = (_bitLength > 31);
    if (_greaterThan) {
      boolean _isNegative = lit.isNegative();
      if (_isNegative) {
        return Optional.<HDLPrimitive.AnyPrimitive>of(HDLPrimitive.anyInt());
      }
      return Optional.<HDLPrimitive.AnyPrimitive>of(HDLPrimitive.anyUint());
    }
    boolean _isNegative_1 = lit.isNegative();
    if (_isNegative_1) {
      return Optional.<HDLPrimitive>of(HDLPrimitive.getInteger());
    }
    return Optional.<HDLPrimitive>of(HDLPrimitive.getNatural());
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
        final Optional<? extends HDLType> opType = TypeExtension.cachedType(res.get());
        boolean _isPresent_1 = opType.isPresent();
        if (_isPresent_1) {
          HDLType type = opType.get();
          final ArrayList<HDLExpression> dim = type.getDim();
          int _length = ((Object[])Conversions.unwrapArray(arr, Object.class)).length;
          int _length_1 = ((Object[])Conversions.unwrapArray(dim, Object.class)).length;
          boolean _greaterThan = (_length > _length_1);
          if (_greaterThan) {
            return opType;
          }
          return Optional.<HDLType>of(type.setDim(dim.subList(((Object[])Conversions.unwrapArray(arr, Object.class)).length, ((Object[])Conversions.unwrapArray(dim, Object.class)).length)));
        }
        return Optional.<HDLType>absent();
      } else {
        return Optional.<HDLType>absent();
      }
    }
    if (((bits.size() == 1) && bits.get(0).isBit())) {
      return Optional.<HDLPrimitive>of(HDLPrimitive.getBit());
    }
    final Iterator<HDLRange> iter = bits.iterator();
    HDLExpression width = HDLPrimitives.simplifyWidth(ref, iter.next().getWidth(), null);
    while (iter.hasNext()) {
      {
        width = HDLArithOp.add(width, iter.next().getWidth());
        width = HDLPrimitives.simplifyWidth(ref, width, null);
      }
    }
    final Optional<HDLVariable> hVar = ref.resolveVar();
    boolean _isPresent_2 = hVar.isPresent();
    boolean _not = (!_isPresent_2);
    if (_not) {
      return Optional.<HDLType>absent();
    }
    final Optional<? extends HDLType> type_1 = TypeExtension.cachedType(hVar.get());
    boolean _isPresent_3 = type_1.isPresent();
    boolean _not_1 = (!_isPresent_3);
    if (_not_1) {
      return Optional.<HDLType>absent();
    }
    final HDLType pType = type_1.get();
    if ((pType instanceof HDLPrimitive)) {
      return Optional.<HDLPrimitive>of(((HDLPrimitive)pType).setWidth(width));
    }
    return type_1;
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLArithOp aop) {
    return Optional.<HDLType>fromNullable(HDLPrimitives.getInstance().getArithOpType(aop).result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLBitOp bop) {
    return Optional.<HDLType>fromNullable(HDLPrimitives.getInstance().getBitOpType(bop).result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLShiftOp sop) {
    return Optional.<HDLType>fromNullable(HDLPrimitives.getInstance().getShiftOpType(sop).result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLEqualityOp eop) {
    return Optional.<HDLType>fromNullable(HDLPrimitives.getInstance().getEqualityOpType(eop).result);
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLTernary tern) {
    return TypeExtension.cachedType(tern.getThenExpr());
  }
  
  protected Optional<? extends HDLType> _determineType(final HDLInlineFunction func) {
    Problem _problem = new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, func);
    throw new HDLProblemException(_problem);
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
    } else if (ref instanceof HDLFunctionParameter) {
      return _determineType((HDLFunctionParameter)ref);
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
