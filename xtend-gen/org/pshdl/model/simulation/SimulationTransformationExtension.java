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
package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.simulation.FluidFrame;
import org.pshdl.model.simulation.FluidFrame.ArgumentedInstruction;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLQualifiedName;

@SuppressWarnings("all")
public class SimulationTransformationExtension {
  private static SimulationTransformationExtension INST = new Function0<SimulationTransformationExtension>() {
    public SimulationTransformationExtension apply() {
      SimulationTransformationExtension _simulationTransformationExtension = new SimulationTransformationExtension();
      return _simulationTransformationExtension;
    }
  }.apply();
  
  public final static char ANNO_VALUE_SEP = '|';
  
  public static FluidFrame simulationModelOf(final IHDLObject obj, final HDLEvaluationContext context) {
    return SimulationTransformationExtension.INST.toSimulationModel(obj, context);
  }
  
  protected FluidFrame _toSimulationModel(final HDLExpression obj, final HDLEvaluationContext context) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Not implemented! " + _classType);
    String _plus_1 = (_plus + " ");
    String _plus_2 = (_plus_1 + obj);
    RuntimeException _runtimeException = new RuntimeException(_plus_2);
    throw _runtimeException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLStatement obj, final HDLEvaluationContext context) {
    HDLClass _classType = obj.getClassType();
    String _plus = ("Not implemented! " + _classType);
    String _plus_1 = (_plus + " ");
    String _plus_2 = (_plus_1 + obj);
    RuntimeException _runtimeException = new RuntimeException(_plus_2);
    throw _runtimeException;
  }
  
  protected FluidFrame _toSimulationModelPred(final HDLStatement obj, final ArgumentedInstruction predicate, final HDLEvaluationContext context) {
    FluidFrame res = this.toSimulationModel(obj, context);
    boolean _hasInstructions = res.hasInstructions();
    if (_hasInstructions) {
      res.instructions.addFirst(predicate);
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLInterfaceDeclaration obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    return _fluidFrame;
  }
  
  protected FluidFrame _toSimulationModel(final HDLEnumDeclaration obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    return _fluidFrame;
  }
  
  protected FluidFrame _toSimulationModel(final HDLExpression obj, final HDLEvaluationContext context, final String varName) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field writeInternal is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLArrayInit obj, final HDLEvaluationContext context, final String varName) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field pushAddIndex is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLVariableDeclaration obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nDirection cannot be resolved to a type."
      + "\nThe method addVar is undefined for the type SimulationTransformationExtension"
      + "\nVariableInformation cannot be resolved."
      + "\nDirection cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nThe method addVar is undefined for the type SimulationTransformationExtension"
      + "\nVariableInformation cannot be resolved."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field posPredicate is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field negPredicate is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field isRisingEdge is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field isFallingEdge is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const0 is undefined for the type SimulationTransformationExtension"
      + "\nINTERNAL cannot be resolved"
      + "\nBIT cannot be resolved"
      + "\nIN cannot be resolved"
      + "\nOUT cannot be resolved"
      + "\nINOUT cannot be resolved"
      + "\nINTERNAL cannot be resolved"
      + "\nBIT cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nUINT cannot be resolved"
      + "\nUINT cannot be resolved");
  }
  
  public String[] toAnnoString(final Iterable<HDLAnnotation> annotations) {
    final Function1<HDLAnnotation,String> _function = new Function1<HDLAnnotation,String>() {
        public String apply(final HDLAnnotation it) {
          String _xifexpression = null;
          String _value = it.getValue();
          boolean _equals = Objects.equal(_value, null);
          if (_equals) {
            String _name = it.getName();
            String _substring = _name.substring(1);
            _xifexpression = _substring;
          } else {
            String _name_1 = it.getName();
            String _substring_1 = _name_1.substring(1);
            String _plus = (_substring_1 + Character.valueOf(SimulationTransformationExtension.ANNO_VALUE_SEP));
            String _value_1 = it.getValue();
            String _plus_1 = (_plus + _value_1);
            _xifexpression = _plus_1;
          }
          return _xifexpression;
        }
      };
    Iterable<String> _map = IterableExtensions.<HDLAnnotation, String>map(annotations, _function);
    return ((String[])Conversions.unwrapArray(_map, String.class));
  }
  
  public void createInit(final HDLRegisterConfig config, final HDLVariableDeclaration obj, final HDLEvaluationContext context, final FluidFrame res, final boolean toReg) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field const0 is undefined for the type SimulationTransformationExtension"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field writeInternal is undefined for the type SimulationTransformationExtension"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field writeInternal is undefined for the type SimulationTransformationExtension"
      + "\nREG_POSTFIX cannot be resolved"
      + "\nREG_POSTFIX cannot be resolved");
  }
  
  protected FluidFrame _toSimulationModel(final HDLSwitchStatement obj, final HDLEvaluationContext context) {
    FluidFrame _simulationModelPred = this.toSimulationModelPred(obj, null, context);
    return _simulationModelPred;
  }
  
  protected FluidFrame _toSimulationModelPred(final HDLSwitchStatement obj, final ArgumentedInstruction predicate, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method addVar is undefined for the type SimulationTransformationExtension"
      + "\nVariableInformation cannot be resolved."
      + "\nDirection cannot be resolved to a type."
      + "\nType cannot be resolved to a type."
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field negPredicate is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const1 is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const1 is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field eq is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field eq is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field posPredicate is undefined for the type SimulationTransformationExtension"
      + "\nINTERNAL cannot be resolved"
      + "\nBIT cannot be resolved"
      + "\nPRED_PREFIX cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  protected FluidFrame _toSimulationModel(final HDLIfStatement obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field posPredicate is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field negPredicate is undefined for the type SimulationTransformationExtension"
      + "\nPRED_PREFIX cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  protected FluidFrame _toSimulationModel(final HDLAssignment obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field isRisingEdge is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field isFallingEdge is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field negPredicate is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field posPredicate is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field pushAddIndex is undefined for the type SimulationTransformationExtension"
      + "\nREG_POSTFIX cannot be resolved");
  }
  
  public HDLVariable resolveVar(final HDLReference reference) {
    if ((reference instanceof HDLUnresolvedFragment)) {
      RuntimeException _runtimeException = new RuntimeException("Can not use unresolved fragments");
      throw _runtimeException;
    }
    Optional<HDLVariable> _resolveVar = ((HDLResolvedRef) reference).resolveVar();
    return _resolveVar.get();
  }
  
  public static String getVarName(final HDLVariableRef hVar, final boolean withBits) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    Optional<HDLVariable> _resolveVar = hVar.resolveVar();
    HDLVariable _get = _resolveVar.get();
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(_get);
    sb.append(_fullNameOf);
    ArrayList<HDLExpression> _array = hVar.getArray();
    for (final HDLExpression exp : _array) {
      {
        final Optional<BigInteger> s = ConstantEvaluate.valueOf(exp);
        boolean _isPresent = s.isPresent();
        if (_isPresent) {
          StringBuilder _append = sb.append("[");
          BigInteger _get_1 = s.get();
          StringBuilder _append_1 = _append.append(_get_1);
          _append_1.append("]");
        } else {
          sb.append("[-1]");
        }
      }
    }
    if (withBits) {
      ArrayList<HDLRange> _bits = hVar.getBits();
      for (final HDLRange exp_1 : _bits) {
        StringBuilder _append = sb.append("{");
        StringBuilder _append_1 = _append.append(exp_1);
        _append_1.append("}");
      }
    }
    return sb.toString();
  }
  
  protected FluidFrame _toSimulationModel(final HDLConcat obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field concat is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLUnit obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const0 is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field not_eq is undefined for the type SimulationTransformationExtension"
      + "\nPRED_PREFIX cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  protected FluidFrame _toSimulationModel(final HDLManip obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field arith_neg is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field bit_neg is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field logiNeg is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field cast_int is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field cast_uint is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field cast_uint is undefined for the type SimulationTransformationExtension");
  }
  
  private int getWidth(final HDLPrimitive current, final HDLEvaluationContext context) {
    HDLPrimitiveType _type = current.getType();
    final HDLPrimitiveType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLPrimitiveType.BIT)) {
        _matched=true;
        return 1;
      }
    }
    if (!_matched) {
      boolean _or = false;
      HDLPrimitiveType _type_1 = current.getType();
      boolean _tripleEquals = (_type_1 == HDLPrimitiveType.INTEGER);
      if (_tripleEquals) {
        _or = true;
      } else {
        HDLPrimitiveType _type_2 = current.getType();
        boolean _tripleEquals_1 = (_type_2 == HDLPrimitiveType.NATURAL);
        _or = (_tripleEquals || _tripleEquals_1);
      }
      if (_or) {
        _matched=true;
        return 32;
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _or_2 = false;
      HDLPrimitiveType _type_3 = current.getType();
      boolean _tripleEquals_2 = (_type_3 == HDLPrimitiveType.INT);
      if (_tripleEquals_2) {
        _or_2 = true;
      } else {
        HDLPrimitiveType _type_4 = current.getType();
        boolean _tripleEquals_3 = (_type_4 == HDLPrimitiveType.UINT);
        _or_2 = (_tripleEquals_2 || _tripleEquals_3);
      }
      if (_or_2) {
        _or_1 = true;
      } else {
        HDLPrimitiveType _type_5 = current.getType();
        boolean _tripleEquals_4 = (_type_5 == HDLPrimitiveType.BITVECTOR);
        _or_1 = (_or_2 || _tripleEquals_4);
      }
      if (_or_1) {
        _matched=true;
        HDLExpression _width = current.getWidth();
        final Optional<BigInteger> res = ConstantEvaluate.valueOf(_width, context);
        boolean _isPresent = res.isPresent();
        if (_isPresent) {
          BigInteger _get = res.get();
          return _get.intValue();
        }
      }
    }
    String _plus = (current + " is not a valid type");
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLEnumRef obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    Optional<HDLEnum> _resolveHEnum = obj.resolveHEnum();
    final HDLEnum hEnum = _resolveHEnum.get();
    Optional<HDLVariable> _resolveVar = obj.resolveVar();
    final HDLVariable hVar = _resolveVar.get();
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    final int idx = _enums.indexOf(hVar);
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
    String _string = _fullNameOf.toString();
    BigInteger _valueOf = BigInteger.valueOf(idx);
    res.addConstant(_string, _valueOf);
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLVariableRef obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field pushAddIndex is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadInternal is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLLiteral obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field const0 is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const1 is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field const2 is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field loadConstant is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field eq is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field not_eq is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field greater is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field greater_eq is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field less is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field less_eq is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLBitOp obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field and is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field logiAnd is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field or is undefined for the type SimulationTransformationExtension"
      + "\nThe method or field logiOr is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field xor is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLArithOp obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field div is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field minus is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field mul is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field plus is undefined for the type SimulationTransformationExtension");
  }
  
  protected FluidFrame _toSimulationModel(final HDLShiftOp obj, final HDLEvaluationContext context) {
    throw new Error("Unresolved compilation problems:"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field sll is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field sra is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field srl is undefined for the type SimulationTransformationExtension"
      + "\nArgumentedInstruction cannot be resolved."
      + "\nThe method or field srl is undefined for the type SimulationTransformationExtension");
  }
  
  public int targetSizeWithType(final HDLExpression op, final HDLEvaluationContext context) {
    Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(op);
    HDLType _get = _typeOf.get();
    final HDLPrimitive type = ((HDLPrimitive) _get);
    Optional<? extends HDLType> _typeOf_1 = TypeExtension.typeOf(op);
    HDLType _get_1 = _typeOf_1.get();
    final Integer width = HDLPrimitives.getWidth(_get_1, context);
    boolean _or = false;
    HDLPrimitiveType _type = type.getType();
    boolean _tripleEquals = (_type == HDLPrimitiveType.INT);
    if (_tripleEquals) {
      _or = true;
    } else {
      HDLPrimitiveType _type_1 = type.getType();
      boolean _tripleEquals_1 = (_type_1 == HDLPrimitiveType.INTEGER);
      _or = (_tripleEquals || _tripleEquals_1);
    }
    if (_or) {
      int _doubleLessThan = ((width).intValue() << 1);
      return (_doubleLessThan | 1);
    }
    return ((width).intValue() << 1);
  }
  
  public FluidFrame toSimulationModel(final IHDLObject obj, final HDLEvaluationContext context) {
    if (obj instanceof HDLEnumRef) {
      return _toSimulationModel((HDLEnumRef)obj, context);
    } else if (obj instanceof HDLVariableRef) {
      return _toSimulationModel((HDLVariableRef)obj, context);
    } else if (obj instanceof HDLArithOp) {
      return _toSimulationModel((HDLArithOp)obj, context);
    } else if (obj instanceof HDLBitOp) {
      return _toSimulationModel((HDLBitOp)obj, context);
    } else if (obj instanceof HDLEnumDeclaration) {
      return _toSimulationModel((HDLEnumDeclaration)obj, context);
    } else if (obj instanceof HDLEqualityOp) {
      return _toSimulationModel((HDLEqualityOp)obj, context);
    } else if (obj instanceof HDLIfStatement) {
      return _toSimulationModel((HDLIfStatement)obj, context);
    } else if (obj instanceof HDLInterfaceDeclaration) {
      return _toSimulationModel((HDLInterfaceDeclaration)obj, context);
    } else if (obj instanceof HDLShiftOp) {
      return _toSimulationModel((HDLShiftOp)obj, context);
    } else if (obj instanceof HDLSwitchStatement) {
      return _toSimulationModel((HDLSwitchStatement)obj, context);
    } else if (obj instanceof HDLVariableDeclaration) {
      return _toSimulationModel((HDLVariableDeclaration)obj, context);
    } else if (obj instanceof HDLAssignment) {
      return _toSimulationModel((HDLAssignment)obj, context);
    } else if (obj instanceof HDLConcat) {
      return _toSimulationModel((HDLConcat)obj, context);
    } else if (obj instanceof HDLLiteral) {
      return _toSimulationModel((HDLLiteral)obj, context);
    } else if (obj instanceof HDLManip) {
      return _toSimulationModel((HDLManip)obj, context);
    } else if (obj instanceof HDLUnit) {
      return _toSimulationModel((HDLUnit)obj, context);
    } else if (obj instanceof HDLExpression) {
      return _toSimulationModel((HDLExpression)obj, context);
    } else if (obj instanceof HDLStatement) {
      return _toSimulationModel((HDLStatement)obj, context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context).toString());
    }
  }
  
  public FluidFrame toSimulationModelPred(final HDLStatement obj, final ArgumentedInstruction predicate, final HDLEvaluationContext context) {
    if (obj instanceof HDLSwitchStatement) {
      return _toSimulationModelPred((HDLSwitchStatement)obj, predicate, context);
    } else if (obj != null) {
      return _toSimulationModelPred(obj, predicate, context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, predicate, context).toString());
    }
  }
  
  public FluidFrame toSimulationModel(final HDLExpression obj, final HDLEvaluationContext context, final String varName) {
    if (obj instanceof HDLArrayInit) {
      return _toSimulationModel((HDLArrayInit)obj, context, varName);
    } else if (obj != null) {
      return _toSimulationModel(obj, context, varName);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, context, varName).toString());
    }
  }
}
