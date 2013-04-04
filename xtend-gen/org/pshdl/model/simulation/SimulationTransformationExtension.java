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
import java.util.Iterator;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.pshdl.interpreter.utils.FluidFrame;
import org.pshdl.interpreter.utils.FluidFrame.ArgumentedInstruction;
import org.pshdl.interpreter.utils.FluidFrame.Instruction;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBitOp.HDLBitOpType;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLShiftOp.HDLShiftOpType;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.TypeExtension;
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
  
  protected FluidFrame _toSimulationModel(final HDLVariableDeclaration obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    ArrayList<HDLVariable> _variables = obj.getVariables();
    for (final HDLVariable hVar : _variables) {
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
      String _string = _fullNameOf.toString();
      Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(hVar);
      HDLType _get = _typeOf.get();
      Integer _width = HDLPrimitives.getWidth(_get, context);
      res.addWith(_string, _width);
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLIfStatement obj, final HDLEvaluationContext context) {
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    final String name = _fullNameOf.toString();
    HDLExpression _ifExp = obj.getIfExp();
    final FluidFrame ifModel = this.toSimulationModel(_ifExp, context);
    String _plus = (FluidFrame.PRED_PREFIX + name);
    ifModel.setName(_plus);
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    for (final HDLStatement s : _thenDo) {
      {
        final FluidFrame thenDo = this.toSimulationModel(s, context);
        boolean _hasInstructions = thenDo.hasInstructions();
        if (_hasInstructions) {
          ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.posPredicate, name);
          thenDo.instructions.addFirst(_argumentedInstruction);
        }
        ifModel.addReferencedFrame(thenDo);
      }
    }
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    for (final HDLStatement s_1 : _elseDo) {
      {
        final FluidFrame elseDo = this.toSimulationModel(s_1, context);
        boolean _hasInstructions = elseDo.hasInstructions();
        if (_hasInstructions) {
          ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.negPredicate, name);
          elseDo.instructions.addFirst(_argumentedInstruction);
        }
        ifModel.addReferencedFrame(elseDo);
      }
    }
    return ifModel;
  }
  
  protected FluidFrame _toSimulationModel(final HDLAssignment obj, final HDLEvaluationContext context) {
    final HDLReference left = obj.getLeft();
    final HDLVariable hVar = this.resolveVar(left);
    HDLRegisterConfig config = hVar.getRegisterConfig();
    FluidFrame res = null;
    boolean _tripleNotEquals = (config != null);
    if (_tripleNotEquals) {
      HDLReference _left = obj.getLeft();
      String _varName = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left), true);
      String _plus = (_varName + FluidFrame.REG_POSTFIX);
      FluidFrame _fluidFrame = new FluidFrame(_plus);
      res = _fluidFrame;
    } else {
      HDLReference _left_1 = obj.getLeft();
      String _varName_1 = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left_1), true);
      FluidFrame _fluidFrame_1 = new FluidFrame(_varName_1);
      res = _fluidFrame_1;
    }
    boolean _tripleNotEquals_1 = (config != null);
    if (_tripleNotEquals_1) {
      HDLRegisterConfig _normalize = config.normalize();
      config = _normalize;
      Optional<HDLVariable> _resolveClk = config.resolveClk();
      final HDLVariable clk = _resolveClk.get();
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(clk);
      final String name = _fullNameOf.toString();
      HDLRegClockType _clockType = config.getClockType();
      boolean _equals = Objects.equal(_clockType, HDLRegClockType.RISING);
      if (_equals) {
        ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.isRisingEdge, name);
        res.add(_argumentedInstruction);
      } else {
        ArgumentedInstruction _argumentedInstruction_1 = new ArgumentedInstruction(Instruction.isFallingEdge, name);
        res.add(_argumentedInstruction_1);
      }
    }
    HDLExpression _right = obj.getRight();
    FluidFrame _simulationModel = this.toSimulationModel(_right, context);
    res.append(_simulationModel);
    boolean hasBits = false;
    if ((left instanceof HDLVariableRef)) {
      final HDLVariableRef variableRef = ((HDLVariableRef) left);
      ArrayList<HDLRange> _bits = variableRef.getBits();
      boolean _isEmpty = _bits.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        hasBits = true;
      }
    }
    return res;
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
      StringBuilder _append = sb.append("[");
      StringBuilder _append_1 = _append.append(exp);
      _append_1.append("]");
    }
    if (withBits) {
      ArrayList<HDLRange> _bits = hVar.getBits();
      for (final HDLRange exp_1 : _bits) {
        StringBuilder _append_2 = sb.append("{");
        StringBuilder _append_3 = _append_2.append(exp_1);
        _append_3.append("}");
      }
    }
    return sb.toString();
  }
  
  protected FluidFrame _toSimulationModel(final HDLConcat obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    ArrayList<HDLExpression> _cats = obj.getCats();
    final Iterator<HDLExpression> iter = _cats.iterator();
    HDLExpression _next = iter.next();
    FluidFrame _simulationModel = this.toSimulationModel(_next, context);
    res.append(_simulationModel);
    boolean _hasNext = iter.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        final HDLExpression exp = iter.next();
        FluidFrame _simulationModel_1 = this.toSimulationModel(exp, context);
        res.append(_simulationModel_1);
        Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(exp);
        HDLType _get = _typeOf.get();
        final int width = (HDLPrimitives.getWidth(_get, context)).intValue();
        String _string = Integer.valueOf(width).toString();
        ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.concat, _string);
        res.add(_argumentedInstruction);
      }
      boolean _hasNext_1 = iter.hasNext();
      _while = _hasNext_1;
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLUnit obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    ArrayList<HDLStatement> _inits = obj.getInits();
    for (final HDLStatement stmnt : _inits) {
      FluidFrame _simulationModel = this.toSimulationModel(stmnt, context);
      res.addReferencedFrame(_simulationModel);
    }
    ArrayList<HDLStatement> _statements = obj.getStatements();
    for (final HDLStatement stmnt_1 : _statements) {
      FluidFrame _simulationModel_1 = this.toSimulationModel(stmnt_1, context);
      res.addReferencedFrame(_simulationModel_1);
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final FluidFrame res = this.toSimulationModel(_target, context);
    HDLManipType _type = obj.getType();
    final HDLManipType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.ARITH_NEG)) {
        _matched=true;
        res.add(Instruction.arith_neg);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.BIT_NEG)) {
        _matched=true;
        res.add(Instruction.bit_neg);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.LOGIC_NEG)) {
        _matched=true;
        res.add(Instruction.logiNeg);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.CAST)) {
        _matched=true;
        HDLType _castTo = obj.getCastTo();
        final HDLPrimitive prim = ((HDLPrimitive) _castTo);
        HDLExpression _target_1 = obj.getTarget();
        Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(_target_1);
        HDLType _get = _typeOf.get();
        final HDLPrimitive current = ((HDLPrimitive) _get);
        final String currentWidth = this.getWidth(current, context);
        final String primWidth = this.getWidth(prim, context);
        HDLPrimitiveType _type_1 = prim.getType();
        final HDLPrimitiveType _switchValue_1 = _type_1;
        boolean _matched_1 = false;
        if (!_matched_1) {
          boolean _or = false;
          HDLPrimitiveType _type_2 = prim.getType();
          boolean _tripleEquals = (_type_2 == HDLPrimitiveType.INTEGER);
          if (_tripleEquals) {
            _or = true;
          } else {
            HDLPrimitiveType _type_3 = prim.getType();
            boolean _tripleEquals_1 = (_type_3 == HDLPrimitiveType.INT);
            _or = (_tripleEquals || _tripleEquals_1);
          }
          if (_or) {
            _matched_1=true;
            ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.cast_int, primWidth, currentWidth);
            res.instructions.add(_argumentedInstruction);
          }
        }
        if (!_matched_1) {
          boolean _or_1 = false;
          HDLPrimitiveType _type_4 = prim.getType();
          boolean _tripleEquals_2 = (_type_4 == HDLPrimitiveType.UINT);
          if (_tripleEquals_2) {
            _or_1 = true;
          } else {
            HDLPrimitiveType _type_5 = prim.getType();
            boolean _tripleEquals_3 = (_type_5 == HDLPrimitiveType.NATURAL);
            _or_1 = (_tripleEquals_2 || _tripleEquals_3);
          }
          if (_or_1) {
            _matched_1=true;
            ArgumentedInstruction _argumentedInstruction_1 = new ArgumentedInstruction(Instruction.cast_uint, primWidth, currentWidth);
            res.instructions.add(_argumentedInstruction_1);
          }
        }
        if (!_matched_1) {
          boolean _or_2 = false;
          HDLPrimitiveType _type_6 = prim.getType();
          boolean _tripleEquals_4 = (_type_6 == HDLPrimitiveType.BIT);
          if (_tripleEquals_4) {
            _or_2 = true;
          } else {
            HDLPrimitiveType _type_7 = prim.getType();
            boolean _tripleEquals_5 = (_type_7 == HDLPrimitiveType.BITVECTOR);
            _or_2 = (_tripleEquals_4 || _tripleEquals_5);
          }
          if (_or_2) {
            _matched_1=true;
          }
        }
        if (!_matched_1) {
          HDLPrimitiveType _type_8 = prim.getType();
          String _plus = ("Cast to type:" + _type_8);
          String _plus_1 = (_plus + " not supported");
          IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus_1);
          throw _illegalArgumentException;
        }
      }
    }
    return res;
  }
  
  private String getWidth(final HDLPrimitive current, final HDLEvaluationContext context) {
    HDLPrimitiveType _type = current.getType();
    final HDLPrimitiveType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLPrimitiveType.BIT)) {
        _matched=true;
        return "1";
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
        return "32";
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
          return _get.toString();
        }
      }
    }
    String _plus = (current + " is not a valid type");
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLVariableRef obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    Optional<HDLVariable> hVar = obj.resolveVar();
    HDLVariable _get = hVar.get();
    HDLQualifiedName _asRef = _get.asRef();
    final String refName = _asRef.toString();
    ArrayList<HDLRange> _bits = obj.getBits();
    int _size = _bits.size();
    int _plus = (_size + 1);
    ArrayList<String> _arrayList = new ArrayList<String>(_plus);
    final ArrayList<String> bits = _arrayList;
    bits.add(refName);
    ArrayList<HDLRange> _bits_1 = obj.getBits();
    boolean _isEmpty = _bits_1.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      ArrayList<HDLRange> _bits_2 = obj.getBits();
      for (final HDLRange r : _bits_2) {
        String _string = r.toString();
        bits.add(_string);
      }
    }
    final String[] arrBits = ((String[])Conversions.unwrapArray(bits, String.class));
    HDLVariable _get_1 = hVar.get();
    final HDLDirection dir = _get_1.getDirection();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(dir,HDLDirection.INTERNAL)) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.loadInternal, arrBits);
        res.instructions.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      boolean _or = false;
      boolean _tripleEquals = (dir == HDLDirection.PARAMETER);
      if (_tripleEquals) {
        _or = true;
      } else {
        boolean _tripleEquals_1 = (dir == HDLDirection.CONSTANT);
        _or = (_tripleEquals || _tripleEquals_1);
      }
      if (_or) {
        _matched=true;
        final Optional<BigInteger> bVal = ConstantEvaluate.valueOf(obj, context);
        boolean _isPresent = bVal.isPresent();
        boolean _not_1 = (!_isPresent);
        if (_not_1) {
          IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Const/param should be constant");
          throw _illegalArgumentException;
        }
        BigInteger _get_2 = bVal.get();
        res.constants.put(refName, _get_2);
        ArgumentedInstruction _argumentedInstruction_1 = new ArgumentedInstruction(Instruction.loadConstant, refName);
        res.instructions.add(_argumentedInstruction_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(dir,HDLDirection.IN)) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction_2 = new ArgumentedInstruction(Instruction.loadInternal, arrBits);
        res.instructions.add(_argumentedInstruction_2);
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _tripleEquals_2 = (dir == HDLDirection.OUT);
      if (_tripleEquals_2) {
        _or_1 = true;
      } else {
        boolean _tripleEquals_3 = (dir == HDLDirection.INOUT);
        _or_1 = (_tripleEquals_2 || _tripleEquals_3);
      }
      if (_or_1) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction_3 = new ArgumentedInstruction(Instruction.loadInternal, arrBits);
        res.instructions.add(_argumentedInstruction_3);
      }
    }
    if (!_matched) {
      String _plus_1 = ("Did not expect obj here" + dir);
      IllegalArgumentException _illegalArgumentException_1 = new IllegalArgumentException(_plus_1);
      throw _illegalArgumentException_1;
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLLiteral obj, final HDLEvaluationContext context) {
    final BigInteger value = obj.getValueAsBigInt();
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    boolean _equals = BigInteger.ZERO.equals(value);
    if (_equals) {
      res.add(Instruction.const0);
      return res;
    }
    final String key = value.toString();
    res.constants.put(key, value);
    ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.loadConstant, key);
    res.instructions.add(_argumentedInstruction);
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLEqualityOp obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    HDLExpression _left = obj.getLeft();
    FluidFrame _simulationModel = this.toSimulationModel(_left, context);
    res.append(_simulationModel);
    HDLExpression _right = obj.getRight();
    FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context);
    res.append(_simulationModel_1);
    HDLEqualityOpType _type = obj.getType();
    final HDLEqualityOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.EQ)) {
        _matched=true;
        res.add(Instruction.eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.NOT_EQ)) {
        _matched=true;
        res.add(Instruction.not_eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER)) {
        _matched=true;
        res.add(Instruction.greater);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.GREATER_EQ)) {
        _matched=true;
        res.add(Instruction.greater_eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS)) {
        _matched=true;
        res.add(Instruction.less);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOpType.LESS_EQ)) {
        _matched=true;
        res.add(Instruction.less_eq);
      }
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLBitOp obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    HDLExpression _left = obj.getLeft();
    FluidFrame _simulationModel = this.toSimulationModel(_left, context);
    res.append(_simulationModel);
    HDLExpression _right = obj.getRight();
    FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context);
    res.append(_simulationModel_1);
    HDLBitOpType _type = obj.getType();
    final HDLBitOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.AND)) {
        _matched=true;
        res.add(Instruction.and);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_AND)) {
        _matched=true;
        res.add(Instruction.logiAnd);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.OR)) {
        _matched=true;
        res.add(Instruction.or);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.LOGI_OR)) {
        _matched=true;
        res.add(Instruction.logiOr);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOpType.XOR)) {
        _matched=true;
        res.add(Instruction.xor);
      }
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLArithOp obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    HDLExpression _left = obj.getLeft();
    FluidFrame _simulationModel = this.toSimulationModel(_left, context);
    res.append(_simulationModel);
    HDLExpression _right = obj.getRight();
    FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context);
    res.append(_simulationModel_1);
    HDLArithOpType _type = obj.getType();
    final HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.DIV)) {
        _matched=true;
        res.add(Instruction.div);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MINUS)) {
        _matched=true;
        res.add(Instruction.minus);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MOD)) {
        _matched=true;
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Mod is not supported as Instruction");
        throw _illegalArgumentException;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.MUL)) {
        _matched=true;
        res.add(Instruction.mul);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.PLUS)) {
        _matched=true;
        res.add(Instruction.plus);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOpType.POW)) {
        _matched=true;
        IllegalArgumentException _illegalArgumentException_1 = new IllegalArgumentException("Pow is not supported as Instruction");
        throw _illegalArgumentException_1;
      }
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLShiftOp obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    HDLExpression _left = obj.getLeft();
    FluidFrame _simulationModel = this.toSimulationModel(_left, context);
    res.append(_simulationModel);
    HDLExpression _right = obj.getRight();
    FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context);
    res.append(_simulationModel_1);
    HDLShiftOpType _type = obj.getType();
    final HDLShiftOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SLL)) {
        _matched=true;
        res.add(Instruction.sll);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRA)) {
        _matched=true;
        res.add(Instruction.sra);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOpType.SRL)) {
        _matched=true;
        res.add(Instruction.srl);
      }
    }
    return res;
  }
  
  public FluidFrame toSimulationModel(final IHDLObject obj, final HDLEvaluationContext context) {
    if (obj instanceof HDLVariableRef) {
      return _toSimulationModel((HDLVariableRef)obj, context);
    } else if (obj instanceof HDLArithOp) {
      return _toSimulationModel((HDLArithOp)obj, context);
    } else if (obj instanceof HDLBitOp) {
      return _toSimulationModel((HDLBitOp)obj, context);
    } else if (obj instanceof HDLEqualityOp) {
      return _toSimulationModel((HDLEqualityOp)obj, context);
    } else if (obj instanceof HDLIfStatement) {
      return _toSimulationModel((HDLIfStatement)obj, context);
    } else if (obj instanceof HDLShiftOp) {
      return _toSimulationModel((HDLShiftOp)obj, context);
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
}
