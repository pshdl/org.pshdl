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
import com.google.common.collect.Iterables;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
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
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchCaseStatement;
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
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider;
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
    HDLEvaluationContext _withEnumAndBool = context.withEnumAndBool(true, true);
    return SimulationTransformationExtension.INST.toSimulationModel(obj, _withEnumAndBool);
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
  
  protected FluidFrame _toSimulationModelPred(final HDLStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context) {
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
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    FluidFrame _simulationModel = this.toSimulationModel(obj, context);
    res.append(_simulationModel);
    FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
    res.add(_argumentedInstruction);
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLArrayInit obj, final HDLEvaluationContext context, final String varName) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    int pos = 0;
    ArrayList<HDLExpression> _exp = obj.getExp();
    for (final HDLExpression exp : _exp) {
      {
        HDLLiteral _get = HDLLiteral.get(pos);
        FluidFrame _simulationModel = this.toSimulationModel(_get, context);
        res.append(_simulationModel);
        res.add(Instruction.pushAddIndex);
        FluidFrame _simulationModel_1 = this.toSimulationModel(exp, context, varName);
        res.append(_simulationModel_1);
        pos = (pos + 1);
      }
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLVariableDeclaration obj, final HDLEvaluationContext context) {
    Optional<? extends HDLType> _resolveType = obj.resolveType();
    final HDLType type = _resolveType.get();
    Integer _xifexpression = null;
    HDLClass _classType = type.getClassType();
    boolean _tripleEquals = (_classType == HDLClass.HDLPrimitive);
    if (_tripleEquals) {
      Integer _width = HDLPrimitives.getWidth(type, context);
      _xifexpression = _width;
    } else {
      _xifexpression = Integer.valueOf(32);
    }
    final Integer width = _xifexpression;
    HDLRegisterConfig _register = obj.getRegister();
    final boolean isReg = (_register != null);
    FluidFrame _fluidFrame = new FluidFrame("#null", false);
    final FluidFrame res = _fluidFrame;
    VariableInformation _variableInformation = new VariableInformation(VariableInformation.Direction.INTERNAL, "#null", 1, VariableInformation.Type.BIT, false, false, false, null);
    res.addVar(_variableInformation);
    VariableInformation.Direction dir = null;
    HDLVariableDeclaration.HDLDirection _direction = obj.getDirection();
    final HDLVariableDeclaration.HDLDirection _switchValue = _direction;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLVariableDeclaration.HDLDirection.IN)) {
        _matched=true;
        dir = VariableInformation.Direction.IN;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLVariableDeclaration.HDLDirection.OUT)) {
        _matched=true;
        dir = VariableInformation.Direction.OUT;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLVariableDeclaration.HDLDirection.INOUT)) {
        _matched=true;
        dir = VariableInformation.Direction.INOUT;
      }
    }
    if (!_matched) {
      dir = VariableInformation.Direction.INTERNAL;
    }
    ArrayList<HDLVariable> _variables = obj.getVariables();
    for (final HDLVariable hVar : _variables) {
      {
        HDLAnnotation _annotation = hVar.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.clock);
        boolean clock = (_annotation != null);
        HDLAnnotation _annotation_1 = hVar.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.reset);
        boolean reset = (_annotation_1 != null);
        HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
        final String varName = _fullNameOf.toString();
        LinkedList<Integer> _linkedList = new LinkedList<Integer>();
        final LinkedList<Integer> dims = _linkedList;
        ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
        for (final HDLExpression dim : _dimensions) {
          Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(dim, context);
          BigInteger _get = _valueOf.get();
          int _intValue = _get.intValue();
          dims.add(Integer.valueOf(_intValue));
        }
        VariableInformation.Type vType = VariableInformation.Type.BIT;
        HDLClass _classType_1 = type.getClassType();
        boolean _tripleEquals_1 = (_classType_1 == HDLClass.HDLPrimitive);
        if (_tripleEquals_1) {
          HDLPrimitive.HDLPrimitiveType _type = ((HDLPrimitive) type).getType();
          final HDLPrimitive.HDLPrimitiveType _switchValue_1 = _type;
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (Objects.equal(_switchValue_1,HDLPrimitive.HDLPrimitiveType.INT)) {
              _matched_1=true;
              vType = VariableInformation.Type.INT;
            }
          }
          if (!_matched_1) {
            if (Objects.equal(_switchValue_1,HDLPrimitive.HDLPrimitiveType.INTEGER)) {
              _matched_1=true;
              vType = VariableInformation.Type.INT;
            }
          }
          if (!_matched_1) {
            if (Objects.equal(_switchValue_1,HDLPrimitive.HDLPrimitiveType.UINT)) {
              _matched_1=true;
              vType = VariableInformation.Type.UINT;
            }
          }
          if (!_matched_1) {
            if (Objects.equal(_switchValue_1,HDLPrimitive.HDLPrimitiveType.NATURAL)) {
              _matched_1=true;
              vType = VariableInformation.Type.UINT;
            }
          }
        }
        ArrayList<HDLAnnotation> _annotations = hVar.getAnnotations();
        ArrayList<HDLAnnotation> _annotations_1 = obj.getAnnotations();
        final Iterable<HDLAnnotation> allAnnos = Iterables.<HDLAnnotation>concat(_annotations, _annotations_1);
        String[] _annoString = this.toAnnoString(allAnnos);
        VariableInformation _variableInformation_1 = new VariableInformation(dir, varName, (width).intValue(), vType, isReg, clock, reset, _annoString, ((int[])Conversions.unwrapArray(dims, int.class)));
        res.addVar(_variableInformation_1);
      }
    }
    if (isReg) {
      HDLRegisterConfig _register_1 = obj.getRegister();
      final HDLRegisterConfig config = _register_1.normalize();
      final HDLExpression rst = config.getRst();
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(rst);
      final String rstName = _fullNameOf.toString();
      HDLRegisterConfig.HDLRegResetActiveType _resetType = config.getResetType();
      boolean _tripleEquals_1 = (_resetType == HDLRegisterConfig.HDLRegResetActiveType.HIGH);
      if (_tripleEquals_1) {
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, rstName);
        res.add(_argumentedInstruction);
      } else {
        FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, rstName);
        res.add(_argumentedInstruction_1);
      }
      HDLRegisterConfig.HDLRegSyncType _syncType = config.getSyncType();
      boolean _tripleEquals_2 = (_syncType == HDLRegisterConfig.HDLRegSyncType.SYNC);
      if (_tripleEquals_2) {
        final HDLExpression clk = config.getClk();
        HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(clk);
        final String name = _fullNameOf_1.toString();
        HDLRegisterConfig.HDLRegClockType _clockType = config.getClockType();
        boolean _tripleEquals_3 = (_clockType == HDLRegisterConfig.HDLRegClockType.RISING);
        if (_tripleEquals_3) {
          FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.isRisingEdge, name);
          res.add(_argumentedInstruction_2);
        } else {
          FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.isFallingEdge, name);
          res.add(_argumentedInstruction_3);
        }
      }
      this.createInit(config, obj, context, res, true);
      HDLRegisterConfig.HDLRegSyncType _syncType_1 = config.getSyncType();
      boolean _tripleEquals_4 = (_syncType_1 == HDLRegisterConfig.HDLRegSyncType.ASYNC);
      if (_tripleEquals_4) {
        this.createInit(config, obj, context, res, false);
      }
      res.add(Instruction.const0);
    }
    return res;
  }
  
  public String[] toAnnoString(final Iterable<HDLAnnotation> annotations) {
    final Function1<HDLAnnotation,String> _function = new Function1<HDLAnnotation,String>() {
      public String apply(final HDLAnnotation it) {
        String _xifexpression = null;
        String _value = it.getValue();
        boolean _tripleEquals = (_value == null);
        if (_tripleEquals) {
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
    HDLExpression _resetValue = config.getResetValue();
    if ((_resetValue instanceof HDLArrayInit)) {
      ArrayList<HDLVariable> _variables = obj.getVariables();
      for (final HDLVariable hVar : _variables) {
        {
          res.add(Instruction.const0);
          HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
          String varName = _fullNameOf.toString();
          if (toReg) {
            varName = (varName + InternalInformation.REG_POSTFIX);
          }
          FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
          res.add(_argumentedInstruction);
          HDLExpression _resetValue_1 = config.getResetValue();
          final HDLArrayInit arr = ((HDLArrayInit) _resetValue_1);
          FluidFrame _simulationModel = this.toSimulationModel(arr, context, varName);
          res.append(_simulationModel);
        }
      }
    } else {
      HDLExpression _resetValue_1 = config.getResetValue();
      final FluidFrame resetFrame = this.toSimulationModel(_resetValue_1, context);
      ArrayList<HDLVariable> _variables_1 = obj.getVariables();
      for (final HDLVariable hVar_1 : _variables_1) {
        {
          HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar_1);
          String varName = _fullNameOf.toString();
          if (toReg) {
            varName = (varName + InternalInformation.REG_POSTFIX);
          }
          res.append(resetFrame);
          FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
          res.add(_argumentedInstruction);
        }
      }
    }
  }
  
  protected FluidFrame _toSimulationModel(final HDLSwitchStatement obj, final HDLEvaluationContext context) {
    FluidFrame _simulationModelPred = this.toSimulationModelPred(obj, null, context);
    return _simulationModelPred;
  }
  
  protected FluidFrame _toSimulationModelPred(final HDLSwitchStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context) {
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    final String name = _fullNameOf.toString();
    HDLExpression _caseExp = obj.getCaseExp();
    final FluidFrame res = this.toSimulationModel(_caseExp, context);
    res.setName(name);
    HDLExpression _caseExp_1 = obj.getCaseExp();
    Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(_caseExp_1);
    final HDLType type = _typeOf.get();
    Integer _xifexpression = null;
    HDLClass _classType = type.getClassType();
    boolean _tripleEquals = (_classType == HDLClass.HDLPrimitive);
    if (_tripleEquals) {
      Integer _width = HDLPrimitives.getWidth(type, context);
      _xifexpression = _width;
    } else {
      _xifexpression = Integer.valueOf(32);
    }
    final Integer width = _xifexpression;
    VariableInformation _variableInformation = new VariableInformation(VariableInformation.Direction.INTERNAL, name, (width).intValue(), VariableInformation.Type.BIT, false, false, false, null);
    res.addVar(_variableInformation);
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      {
        HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(c);
        final String cName = _fullNameOf_1.toString();
        FluidFrame _fluidFrame = new FluidFrame((InternalInformation.PRED_PREFIX + cName), false);
        final FluidFrame caseFrame = _fluidFrame;
        boolean _tripleNotEquals = (predicate != null);
        if (_tripleNotEquals) {
          caseFrame.add(predicate);
        }
        caseFrame.createPredVar();
        HDLExpression _label = c.getLabel();
        boolean _tripleEquals_1 = (_label == null);
        if (_tripleEquals_1) {
          ArrayList<HDLSwitchCaseStatement> _cases_1 = obj.getCases();
          for (final HDLSwitchCaseStatement cSub : _cases_1) {
            boolean _notEquals = (!Objects.equal(cSub, c));
            if (_notEquals) {
              HDLQualifiedName _fullNameOf_2 = FullNameExtension.fullNameOf(cSub);
              String _string = _fullNameOf_2.toString();
              FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, _string);
              caseFrame.add(_argumentedInstruction);
            }
          }
          caseFrame.add(Instruction.const1);
          caseFrame.add(Instruction.const1);
          caseFrame.add(Instruction.eq);
        } else {
          HDLExpression _label_1 = c.getLabel();
          final Optional<BigInteger> const_ = ConstantEvaluate.valueOf(_label_1);
          int l = 0;
          boolean _isPresent = const_.isPresent();
          if (_isPresent) {
            BigInteger _get = const_.get();
            int _intValue = _get.intValue();
            l = _intValue;
          } else {
            HDLExpression _label_2 = c.getLabel();
            HDLClass _classType_1 = _label_2.getClassType();
            boolean _tripleEquals_2 = (_classType_1 == HDLClass.HDLEnumRef);
            if (_tripleEquals_2) {
              HDLExpression _label_3 = c.getLabel();
              final HDLEnumRef ref = ((HDLEnumRef) _label_3);
              int _asInt = this.asInt(ref);
              l = _asInt;
            } else {
              IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Unsupported label type");
              throw _illegalArgumentException;
            }
          }
          FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, name);
          caseFrame.add(_argumentedInstruction_1);
          BigInteger _valueOf = BigInteger.valueOf(l);
          caseFrame.addConstant("label", _valueOf);
          caseFrame.add(Instruction.eq);
        }
        ArrayList<HDLStatement> _dos = c.getDos();
        for (final HDLStatement d : _dos) {
          {
            FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, cName);
            final FluidFrame subDo = this.toSimulationModelPred(d, _argumentedInstruction_2, context);
            caseFrame.addReferencedFrame(subDo);
          }
        }
        res.addReferencedFrame(caseFrame);
      }
    }
    return res;
  }
  
  public int asInt(final HDLEnumRef ref) {
    Optional<HDLEnum> _resolveHEnum = ref.resolveHEnum();
    final HDLEnum hEnum = _resolveHEnum.get();
    Optional<HDLVariable> _resolveVar = ref.resolveVar();
    final HDLVariable hVar = _resolveVar.get();
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    return _enums.indexOf(hVar);
  }
  
  protected FluidFrame _toSimulationModel(final HDLIfStatement obj, final HDLEvaluationContext context) {
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    final String name = _fullNameOf.toString();
    HDLExpression _ifExp = obj.getIfExp();
    final FluidFrame ifModel = this.toSimulationModel(_ifExp, context);
    ifModel.setName((InternalInformation.PRED_PREFIX + name));
    ifModel.createPredVar();
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    for (final HDLStatement s : _thenDo) {
      {
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, name);
        final FluidFrame thenDo = this.toSimulationModelPred(s, _argumentedInstruction, context);
        ifModel.addReferencedFrame(thenDo);
      }
    }
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    for (final HDLStatement s_1 : _elseDo) {
      {
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, name);
        final FluidFrame elseDo = this.toSimulationModelPred(s_1, _argumentedInstruction, context);
        ifModel.addReferencedFrame(elseDo);
      }
    }
    return ifModel;
  }
  
  protected FluidFrame _toSimulationModel(final HDLAssignment obj, final HDLEvaluationContext context) {
    final HDLReference left = obj.getLeft();
    final HDLVariable hVar = this.resolveVar(left);
    HDLVariableDeclaration.HDLDirection _direction = hVar.getDirection();
    final boolean constant = (_direction == HDLVariableDeclaration.HDLDirection.CONSTANT);
    HDLRegisterConfig config = hVar.getRegisterConfig();
    FluidFrame res = null;
    boolean _tripleNotEquals = (config != null);
    if (_tripleNotEquals) {
      HDLReference _left = obj.getLeft();
      String _varName = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left), true);
      String _plus = (_varName + InternalInformation.REG_POSTFIX);
      FluidFrame _fluidFrame = new FluidFrame(_plus, constant);
      res = _fluidFrame;
    } else {
      HDLReference _left_1 = obj.getLeft();
      String _varName_1 = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left_1), true);
      FluidFrame _fluidFrame_1 = new FluidFrame(_varName_1, constant);
      res = _fluidFrame_1;
    }
    boolean _tripleNotEquals_1 = (config != null);
    if (_tripleNotEquals_1) {
      HDLRegisterConfig _normalize = config.normalize();
      config = _normalize;
      final HDLExpression clk = config.getClk();
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(clk);
      final String name = _fullNameOf.toString();
      HDLRegisterConfig.HDLRegClockType _clockType = config.getClockType();
      boolean _equals = Objects.equal(_clockType, HDLRegisterConfig.HDLRegClockType.RISING);
      if (_equals) {
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.isRisingEdge, name);
        res.add(_argumentedInstruction);
      } else {
        FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.isFallingEdge, name);
        res.add(_argumentedInstruction_1);
      }
      final HDLExpression rst = config.getRst();
      HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(rst);
      final String rstName = _fullNameOf_1.toString();
      HDLRegisterConfig.HDLRegResetActiveType _resetType = config.getResetType();
      boolean _tripleEquals = (_resetType == HDLRegisterConfig.HDLRegResetActiveType.HIGH);
      if (_tripleEquals) {
        FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, rstName);
        res.add(_argumentedInstruction_2);
      } else {
        FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, rstName);
        res.add(_argumentedInstruction_3);
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
      boolean fixedArray = true;
      ArrayList<HDLExpression> _array = variableRef.getArray();
      for (final HDLExpression idx : _array) {
        Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(idx, context);
        boolean _isPresent = _valueOf.isPresent();
        boolean _not_1 = (!_isPresent);
        if (_not_1) {
          fixedArray = false;
        }
      }
      if ((!fixedArray)) {
        ArrayList<HDLExpression> _array_1 = variableRef.getArray();
        for (final HDLExpression idx_1 : _array_1) {
          {
            FluidFrame _simulationModel_1 = this.toSimulationModel(idx_1, context);
            res.append(_simulationModel_1);
            res.add(Instruction.pushAddIndex);
          }
        }
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
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    ArrayList<HDLExpression> _cats = obj.getCats();
    final Iterator<HDLExpression> iter = _cats.iterator();
    final HDLExpression init = iter.next();
    FluidFrame _simulationModel = this.toSimulationModel(init, context);
    res.append(_simulationModel);
    Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(init);
    HDLType _get = _typeOf.get();
    int owidth = (HDLPrimitives.getWidth(_get, context)).intValue();
    boolean _hasNext = iter.hasNext();
    boolean _while = _hasNext;
    while (_while) {
      {
        final HDLExpression exp = iter.next();
        FluidFrame _simulationModel_1 = this.toSimulationModel(exp, context);
        res.append(_simulationModel_1);
        Optional<? extends HDLType> _typeOf_1 = TypeExtension.typeOf(exp);
        HDLType _get_1 = _typeOf_1.get();
        final int width = (HDLPrimitives.getWidth(_get_1, context)).intValue();
        String _string = Integer.valueOf(owidth).toString();
        String _string_1 = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.concat, _string, _string_1);
        res.add(_argumentedInstruction);
        owidth = (owidth + width);
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
    ArrayList<HDLAnnotation> _annotations = obj.getAnnotations();
    String[] _annoString = this.toAnnoString(_annotations);
    res.annotations = _annoString;
    final HDLRegisterConfig[] regConfigs = obj.<HDLRegisterConfig>getAllObjectsOf(HDLRegisterConfig.class, true);
    HashSet<HDLQualifiedName> _hashSet = new HashSet<HDLQualifiedName>();
    final Set<HDLQualifiedName> lst = _hashSet;
    for (final HDLRegisterConfig reg : regConfigs) {
      {
        HDLExpression _rst = reg.getRst();
        final HDLQualifiedName rstVar = FullNameExtension.fullNameOf(_rst);
        boolean _contains = lst.contains(rstVar);
        boolean _not = (!_contains);
        if (_not) {
          lst.add(rstVar);
          final String rstVarName = rstVar.toString();
          FluidFrame _fluidFrame_1 = new FluidFrame((InternalInformation.PRED_PREFIX + rstVarName), false);
          final FluidFrame rstFrame = _fluidFrame_1;
          FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, rstVarName);
          rstFrame.add(_argumentedInstruction);
          rstFrame.add(Instruction.const0);
          rstFrame.add(Instruction.not_eq);
          rstFrame.createPredVar();
          res.addReferencedFrame(rstFrame);
        }
      }
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLManip obj, final HDLEvaluationContext context) {
    HDLExpression _target = obj.getTarget();
    final FluidFrame res = this.toSimulationModel(_target, context);
    HDLManip.HDLManipType _type = obj.getType();
    final HDLManip.HDLManipType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManip.HDLManipType.ARITH_NEG)) {
        _matched=true;
        final int width = this.targetSizeWithType(obj, context);
        String _string = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.arith_neg, _string);
        res.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManip.HDLManipType.BIT_NEG)) {
        _matched=true;
        final int width_1 = this.targetSizeWithType(obj, context);
        String _string_1 = Integer.valueOf(width_1).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.bit_neg, _string_1);
        res.add(_argumentedInstruction_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManip.HDLManipType.LOGIC_NEG)) {
        _matched=true;
        res.add(Instruction.logiNeg);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManip.HDLManipType.CAST)) {
        _matched=true;
        HDLType _castTo = obj.getCastTo();
        final HDLPrimitive prim = ((HDLPrimitive) _castTo);
        HDLExpression _target_1 = obj.getTarget();
        Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(_target_1);
        HDLType _get = _typeOf.get();
        final HDLPrimitive current = ((HDLPrimitive) _get);
        final int currentWidth = this.getWidth(current, context);
        int primWidth = this.getWidth(prim, context);
        HDLPrimitive.HDLPrimitiveType _type_1 = prim.getType();
        final HDLPrimitive.HDLPrimitiveType _switchValue_1 = _type_1;
        boolean _matched_1 = false;
        if (!_matched_1) {
          boolean _or = false;
          HDLPrimitive.HDLPrimitiveType _type_2 = current.getType();
          boolean _tripleEquals = (_type_2 == HDLPrimitive.HDLPrimitiveType.INTEGER);
          if (_tripleEquals) {
            _or = true;
          } else {
            HDLPrimitive.HDLPrimitiveType _type_3 = current.getType();
            boolean _tripleEquals_1 = (_type_3 == HDLPrimitive.HDLPrimitiveType.INT);
            _or = (_tripleEquals || _tripleEquals_1);
          }
          if (_or) {
            _matched_1=true;
            String _string_2 = Integer.valueOf(primWidth).toString();
            String _string_3 = Integer.valueOf(currentWidth).toString();
            FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.cast_int, _string_2, _string_3);
            res.instructions.add(_argumentedInstruction_2);
          }
        }
        if (!_matched_1) {
          boolean _or_1 = false;
          HDLPrimitive.HDLPrimitiveType _type_4 = current.getType();
          boolean _tripleEquals_2 = (_type_4 == HDLPrimitive.HDLPrimitiveType.BIT);
          if (_tripleEquals_2) {
            _or_1 = true;
          } else {
            HDLPrimitive.HDLPrimitiveType _type_5 = current.getType();
            boolean _tripleEquals_3 = (_type_5 == HDLPrimitive.HDLPrimitiveType.BITVECTOR);
            _or_1 = (_tripleEquals_2 || _tripleEquals_3);
          }
          if (_or_1) {
            _matched_1=true;
            String _string_4 = Integer.valueOf(primWidth).toString();
            String _string_5 = Integer.valueOf(currentWidth).toString();
            FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.cast_uint, _string_4, _string_5);
            res.instructions.add(_argumentedInstruction_3);
          }
        }
        if (!_matched_1) {
          boolean _or_2 = false;
          HDLPrimitive.HDLPrimitiveType _type_6 = current.getType();
          boolean _tripleEquals_4 = (_type_6 == HDLPrimitive.HDLPrimitiveType.UINT);
          if (_tripleEquals_4) {
            _or_2 = true;
          } else {
            HDLPrimitive.HDLPrimitiveType _type_7 = current.getType();
            boolean _tripleEquals_5 = (_type_7 == HDLPrimitive.HDLPrimitiveType.NATURAL);
            _or_2 = (_tripleEquals_4 || _tripleEquals_5);
          }
          if (_or_2) {
            _matched_1=true;
            String _string_6 = Integer.valueOf(primWidth).toString();
            String _string_7 = Integer.valueOf(currentWidth).toString();
            FluidFrame.ArgumentedInstruction _argumentedInstruction_4 = new FluidFrame.ArgumentedInstruction(Instruction.cast_uint, _string_6, _string_7);
            res.instructions.add(_argumentedInstruction_4);
          }
        }
        if (!_matched_1) {
          HDLPrimitive.HDLPrimitiveType _type_8 = prim.getType();
          String _plus = ("Cast to type:" + _type_8);
          String _plus_1 = (_plus + " not supported");
          IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus_1);
          throw _illegalArgumentException;
        }
      }
    }
    return res;
  }
  
  private int getWidth(final HDLPrimitive current, final HDLEvaluationContext context) {
    HDLPrimitive.HDLPrimitiveType _type = current.getType();
    final HDLPrimitive.HDLPrimitiveType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLPrimitive.HDLPrimitiveType.BIT)) {
        _matched=true;
        return 1;
      }
    }
    if (!_matched) {
      boolean _or = false;
      HDLPrimitive.HDLPrimitiveType _type_1 = current.getType();
      boolean _tripleEquals = (_type_1 == HDLPrimitive.HDLPrimitiveType.INTEGER);
      if (_tripleEquals) {
        _or = true;
      } else {
        HDLPrimitive.HDLPrimitiveType _type_2 = current.getType();
        boolean _tripleEquals_1 = (_type_2 == HDLPrimitive.HDLPrimitiveType.NATURAL);
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
      HDLPrimitive.HDLPrimitiveType _type_3 = current.getType();
      boolean _tripleEquals_2 = (_type_3 == HDLPrimitive.HDLPrimitiveType.INT);
      if (_tripleEquals_2) {
        _or_2 = true;
      } else {
        HDLPrimitive.HDLPrimitiveType _type_4 = current.getType();
        boolean _tripleEquals_3 = (_type_4 == HDLPrimitive.HDLPrimitiveType.UINT);
        _or_2 = (_tripleEquals_2 || _tripleEquals_3);
      }
      if (_or_2) {
        _or_1 = true;
      } else {
        HDLPrimitive.HDLPrimitiveType _type_5 = current.getType();
        boolean _tripleEquals_4 = (_type_5 == HDLPrimitive.HDLPrimitiveType.BITVECTOR);
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
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    Optional<HDLVariable> hVar = obj.resolveVar();
    final String refName = SimulationTransformationExtension.getVarName(obj, false);
    boolean fixedArray = true;
    ArrayList<HDLExpression> _array = obj.getArray();
    for (final HDLExpression idx : _array) {
      Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(idx, context);
      boolean _isPresent = _valueOf.isPresent();
      boolean _not = (!_isPresent);
      if (_not) {
        fixedArray = false;
      }
    }
    if ((!fixedArray)) {
      ArrayList<HDLExpression> _array_1 = obj.getArray();
      for (final HDLExpression idx_1 : _array_1) {
        {
          FluidFrame _simulationModel = this.toSimulationModel(idx_1, context);
          res.append(_simulationModel);
          res.add(Instruction.pushAddIndex);
        }
      }
    }
    ArrayList<HDLRange> _bits = obj.getBits();
    int _size = _bits.size();
    int _plus = (_size + 1);
    ArrayList<String> _arrayList = new ArrayList<String>(_plus);
    final ArrayList<String> bits = _arrayList;
    bits.add(refName);
    ArrayList<HDLRange> _bits_1 = obj.getBits();
    boolean _isEmpty = _bits_1.isEmpty();
    boolean _not_1 = (!_isEmpty);
    if (_not_1) {
      ArrayList<HDLRange> _bits_2 = obj.getBits();
      for (final HDLRange r : _bits_2) {
        String _string = r.toString();
        bits.add(_string);
      }
    }
    HDLVariable _get = hVar.get();
    final HDLVariableDeclaration.HDLDirection dir = _get.getDirection();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(dir,HDLVariableDeclaration.HDLDirection.INTERNAL)) {
        _matched=true;
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, ((String[])Conversions.unwrapArray(bits, String.class)));
        res.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      boolean _or = false;
      boolean _tripleEquals = (dir == HDLVariableDeclaration.HDLDirection.PARAMETER);
      if (_tripleEquals) {
        _or = true;
      } else {
        boolean _tripleEquals_1 = (dir == HDLVariableDeclaration.HDLDirection.CONSTANT);
        _or = (_tripleEquals || _tripleEquals_1);
      }
      if (_or) {
        _matched=true;
        if ((!fixedArray)) {
          FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, ((String[])Conversions.unwrapArray(bits, String.class)));
          res.add(_argumentedInstruction_1);
        } else {
          final Optional<BigInteger> bVal = ConstantEvaluate.valueOf(obj, context);
          boolean _isPresent_1 = bVal.isPresent();
          boolean _not_2 = (!_isPresent_1);
          if (_not_2) {
            IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Const/param should be constant");
            throw _illegalArgumentException;
          } else {
            BigInteger _get_1 = bVal.get();
            res.addConstant(refName, _get_1);
          }
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(dir,HDLVariableDeclaration.HDLDirection.IN)) {
        _matched=true;
        FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, ((String[])Conversions.unwrapArray(bits, String.class)));
        res.add(_argumentedInstruction_2);
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _tripleEquals_2 = (dir == HDLVariableDeclaration.HDLDirection.OUT);
      if (_tripleEquals_2) {
        _or_1 = true;
      } else {
        boolean _tripleEquals_3 = (dir == HDLVariableDeclaration.HDLDirection.INOUT);
        _or_1 = (_tripleEquals_2 || _tripleEquals_3);
      }
      if (_or_1) {
        _matched=true;
        FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, ((String[])Conversions.unwrapArray(bits, String.class)));
        res.add(_argumentedInstruction_3);
      }
    }
    if (!_matched) {
      IllegalArgumentException _illegalArgumentException_1 = new IllegalArgumentException(("Did not expect obj here" + dir));
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
    boolean _equals_1 = BigInteger.ONE.equals(value);
    if (_equals_1) {
      res.add(Instruction.const1);
      return res;
    }
    boolean _equals_2 = BigInteger.valueOf(2L).equals(value);
    if (_equals_2) {
      res.add(Instruction.const2);
      return res;
    }
    final String key = value.toString();
    res.constants.put(key, value);
    FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadConstant, key);
    res.add(_argumentedInstruction);
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
    HDLEqualityOp.HDLEqualityOpType _type = obj.getType();
    final HDLEqualityOp.HDLEqualityOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.EQ)) {
        _matched=true;
        res.add(Instruction.eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.NOT_EQ)) {
        _matched=true;
        res.add(Instruction.not_eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.GREATER)) {
        _matched=true;
        res.add(Instruction.greater);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.GREATER_EQ)) {
        _matched=true;
        res.add(Instruction.greater_eq);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.LESS)) {
        _matched=true;
        res.add(Instruction.less);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLEqualityOp.HDLEqualityOpType.LESS_EQ)) {
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
    HDLBitOp.HDLBitOpType _type = obj.getType();
    final HDLBitOp.HDLBitOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOp.HDLBitOpType.AND)) {
        _matched=true;
        final int width = this.targetSizeWithType(obj, context);
        String _string = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.and, _string);
        res.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOp.HDLBitOpType.LOGI_AND)) {
        _matched=true;
        res.add(Instruction.logiAnd);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOp.HDLBitOpType.OR)) {
        _matched=true;
        final int width_1 = this.targetSizeWithType(obj, context);
        String _string_1 = Integer.valueOf(width_1).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.or, _string_1);
        res.add(_argumentedInstruction_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOp.HDLBitOpType.LOGI_OR)) {
        _matched=true;
        res.add(Instruction.logiOr);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLBitOp.HDLBitOpType.XOR)) {
        _matched=true;
        final int width_2 = this.targetSizeWithType(obj, context);
        String _string_2 = Integer.valueOf(width_2).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.xor, _string_2);
        res.add(_argumentedInstruction_2);
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
    final int width = this.targetSizeWithType(obj, context);
    HDLArithOp.HDLArithOpType _type = obj.getType();
    final HDLArithOp.HDLArithOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.DIV)) {
        _matched=true;
        String _string = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.div, _string);
        res.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.MINUS)) {
        _matched=true;
        String _string_1 = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.minus, _string_1);
        res.add(_argumentedInstruction_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.MOD)) {
        _matched=true;
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Mod is not supported as Instruction");
        throw _illegalArgumentException;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.MUL)) {
        _matched=true;
        String _string_2 = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.mul, _string_2);
        res.add(_argumentedInstruction_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.PLUS)) {
        _matched=true;
        String _string_3 = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.plus, _string_3);
        res.add(_argumentedInstruction_3);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLArithOp.HDLArithOpType.POW)) {
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
    final int width = this.targetSizeWithType(obj, context);
    HDLShiftOp.HDLShiftOpType _type = obj.getType();
    final HDLShiftOp.HDLShiftOpType _switchValue = _type;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOp.HDLShiftOpType.SLL)) {
        _matched=true;
        String _string = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.sll, _string);
        res.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOp.HDLShiftOpType.SRA)) {
        _matched=true;
        HDLExpression _left_1 = obj.getLeft();
        final Optional<? extends HDLType> type = TypeExtension.typeOf(_left_1);
        HDLType _get = type.get();
        final HDLPrimitive prim = ((HDLPrimitive) _get);
        boolean _or = false;
        HDLPrimitive.HDLPrimitiveType _type_1 = prim.getType();
        boolean _tripleEquals = (_type_1 == HDLPrimitive.HDLPrimitiveType.INTEGER);
        if (_tripleEquals) {
          _or = true;
        } else {
          HDLPrimitive.HDLPrimitiveType _type_2 = prim.getType();
          boolean _tripleEquals_1 = (_type_2 == HDLPrimitive.HDLPrimitiveType.INT);
          _or = (_tripleEquals || _tripleEquals_1);
        }
        if (_or) {
          String _string_1 = Integer.valueOf(width).toString();
          FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.sra, _string_1);
          res.add(_argumentedInstruction_1);
        } else {
          String _string_2 = Integer.valueOf(width).toString();
          FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.srl, _string_2);
          res.add(_argumentedInstruction_2);
        }
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLShiftOp.HDLShiftOpType.SRL)) {
        _matched=true;
        String _string_3 = Integer.valueOf(width).toString();
        FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.srl, _string_3);
        res.add(_argumentedInstruction_3);
      }
    }
    return res;
  }
  
  public int targetSizeWithType(final HDLExpression op, final HDLEvaluationContext context) {
    Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(op);
    HDLType _get = _typeOf.get();
    final HDLPrimitive type = ((HDLPrimitive) _get);
    Optional<? extends HDLType> _typeOf_1 = TypeExtension.typeOf(op);
    HDLType _get_1 = _typeOf_1.get();
    final Integer width = HDLPrimitives.getWidth(_get_1, context);
    boolean _or = false;
    HDLPrimitive.HDLPrimitiveType _type = type.getType();
    boolean _tripleEquals = (_type == HDLPrimitive.HDLPrimitiveType.INT);
    if (_tripleEquals) {
      _or = true;
    } else {
      HDLPrimitive.HDLPrimitiveType _type_1 = type.getType();
      boolean _tripleEquals_1 = (_type_1 == HDLPrimitive.HDLPrimitiveType.INTEGER);
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
  
  public FluidFrame toSimulationModelPred(final HDLStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context) {
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
