package de.tuhh.ict.pshdl.model.simulation;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.interpreter.utils.FluidFrame;
import de.tuhh.ict.pshdl.interpreter.utils.FluidFrame.ArgumentedInstruction;
import de.tuhh.ict.pshdl.interpreter.utils.FluidFrame.Instruction;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLReference;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLResolvedRef;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLStatement;
import de.tuhh.ict.pshdl.model.HDLTernary;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnit;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.evaluation.ConstantEvaluate;
import de.tuhh.ict.pshdl.model.evaluation.HDLEvaluationContext;
import de.tuhh.ict.pshdl.model.extensions.FullNameExtension;
import de.tuhh.ict.pshdl.model.extensions.TypeExtension;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class SimulationTransformationExtension {
  public static SimulationTransformationExtension INST = new Function0<SimulationTransformationExtension>() {
    public SimulationTransformationExtension apply() {
      SimulationTransformationExtension _simulationTransformationExtension = new SimulationTransformationExtension();
      return _simulationTransformationExtension;
    }
  }.apply();
  
  public static FluidFrame simulationModelOf(final IHDLObject obj, final HDLEvaluationContext context) {
    return SimulationTransformationExtension.INST.toSimulationModel(obj, context);
  }
  
  protected FluidFrame _toSimulationModel(final HDLExpression obj, final HDLEvaluationContext context) {
    RuntimeException _runtimeException = new RuntimeException("Not implemented!");
    throw _runtimeException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLStatement obj, final HDLEvaluationContext context) {
    RuntimeException _runtimeException = new RuntimeException("Not implemented!");
    throw _runtimeException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLAssignment obj, final HDLEvaluationContext context) {
    final HDLReference left = obj.getLeft();
    final HDLVariable hVar = this.resolveVar(left);
    HDLRegisterConfig config = hVar.getRegisterConfig();
    FluidFrame res = null;
    boolean _notEquals = (!Objects.equal(config, null));
    if (_notEquals) {
      HDLReference _left = obj.getLeft();
      String _varName = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left), true);
      String _plus = (_varName + "$reg");
      FluidFrame _fluidFrame = new FluidFrame(_plus);
      res = _fluidFrame;
    } else {
      HDLReference _left_1 = obj.getLeft();
      String _varName_1 = SimulationTransformationExtension.getVarName(((HDLVariableRef) _left_1), true);
      FluidFrame _fluidFrame_1 = new FluidFrame(_varName_1);
      res = _fluidFrame_1;
    }
    boolean _notEquals_1 = (!Objects.equal(config, null));
    if (_notEquals_1) {
      HDLRegisterConfig _normalize = config.normalize();
      config = _normalize;
      final HDLVariable clk = config.resolveClk();
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(clk);
      final String name = _fullNameOf.toString();
      HDLRegClockType _clockType = config.getClockType();
      boolean _equals = Objects.equal(_clockType, HDLRegClockType.RISING);
      if (_equals) {
        ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.isRisingEdgeInternal2, name);
        res.add(_argumentedInstruction);
      } else {
        ArgumentedInstruction _argumentedInstruction_1 = new ArgumentedInstruction(Instruction.isFallingEdgeInternal2, name);
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
    return ((HDLResolvedRef) reference).resolveVar();
  }
  
  public static String getVarName(final HDLVariableRef hVar, final boolean withBits) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    HDLVariable _resolveVar = hVar.resolveVar();
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(_resolveVar);
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
        HDLType _typeOf = TypeExtension.typeOf(exp);
        final int width = (HDLPrimitives.getWidth(_typeOf, context)).intValue();
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
      this.handleStatement(context, res, stmnt);
    }
    ArrayList<HDLStatement> _statements = obj.getStatements();
    for (final HDLStatement stmnt_1 : _statements) {
      this.handleStatement(context, res, stmnt_1);
    }
    return res;
  }
  
  private Boolean handleStatement(final HDLEvaluationContext context, final FluidFrame res, final HDLStatement stmnt) {
    Boolean _switchResult = null;
    HDLClass _classType = stmnt.getClassType();
    final HDLClass _switchValue = _classType;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLAssignment)) {
        _matched=true;
        boolean _xblockexpression = false;
        {
          final FluidFrame sFrame = this.toSimulationModel(stmnt, context);
          res.addReferencedFrame(sFrame);
          String _string = Integer.valueOf(sFrame.id).toString();
          ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.callFrame, _string);
          boolean _add = res.instructions.add(_argumentedInstruction);
          _xblockexpression = (_add);
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLClass.HDLVariableDeclaration)) {
        _matched=true;
        final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) stmnt);
        ArrayList<HDLVariable> _variables = hvd.getVariables();
        for (final HDLVariable hVar : _variables) {
          HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
          String _string = _fullNameOf.toString();
          HDLType _typeOf = TypeExtension.typeOf(hVar);
          Integer _width = HDLPrimitives.getWidth(_typeOf, context);
          res.addWith(_string, _width);
        }
      }
    }
    return _switchResult;
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
        res.add(Instruction.logic_neg);
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,HDLManipType.CAST)) {
        _matched=true;
        HDLType _castTo = obj.getCastTo();
        final HDLPrimitive prim = ((HDLPrimitive) _castTo);
        HDLExpression _target_1 = obj.getTarget();
        HDLType _typeOf = TypeExtension.typeOf(_target_1);
        final HDLPrimitive current = ((HDLPrimitive) _typeOf);
        final String currentWidth = this.getWidth(current, context);
        final String primWidth = this.getWidth(prim, context);
        HDLPrimitiveType _type_1 = prim.getType();
        final HDLPrimitiveType _switchValue_1 = _type_1;
        boolean _matched_1 = false;
        if (!_matched_1) {
          boolean _or = false;
          HDLPrimitiveType _type_2 = prim.getType();
          boolean _equals = Objects.equal(_type_2, HDLPrimitiveType.INTEGER);
          if (_equals) {
            _or = true;
          } else {
            HDLPrimitiveType _type_3 = prim.getType();
            boolean _equals_1 = Objects.equal(_type_3, HDLPrimitiveType.INT);
            _or = (_equals || _equals_1);
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
          boolean _equals_2 = Objects.equal(_type_4, HDLPrimitiveType.UINT);
          if (_equals_2) {
            _or_1 = true;
          } else {
            HDLPrimitiveType _type_5 = prim.getType();
            boolean _equals_3 = Objects.equal(_type_5, HDLPrimitiveType.NATURAL);
            _or_1 = (_equals_2 || _equals_3);
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
          boolean _equals_4 = Objects.equal(_type_6, HDLPrimitiveType.BIT);
          if (_equals_4) {
            _or_2 = true;
          } else {
            HDLPrimitiveType _type_7 = prim.getType();
            boolean _equals_5 = Objects.equal(_type_7, HDLPrimitiveType.BITVECTOR);
            _or_2 = (_equals_4 || _equals_5);
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
      boolean _equals = Objects.equal(_type_1, HDLPrimitiveType.INTEGER);
      if (_equals) {
        _or = true;
      } else {
        HDLPrimitiveType _type_2 = current.getType();
        boolean _equals_1 = Objects.equal(_type_2, HDLPrimitiveType.NATURAL);
        _or = (_equals || _equals_1);
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
      boolean _equals_2 = Objects.equal(_type_3, HDLPrimitiveType.INT);
      if (_equals_2) {
        _or_2 = true;
      } else {
        HDLPrimitiveType _type_4 = current.getType();
        boolean _equals_3 = Objects.equal(_type_4, HDLPrimitiveType.UINT);
        _or_2 = (_equals_2 || _equals_3);
      }
      if (_or_2) {
        _or_1 = true;
      } else {
        HDLPrimitiveType _type_5 = current.getType();
        boolean _equals_4 = Objects.equal(_type_5, HDLPrimitiveType.BITVECTOR);
        _or_1 = (_or_2 || _equals_4);
      }
      if (_or_1) {
        _matched=true;
        HDLExpression _width = current.getWidth();
        BigInteger _valueOf = ConstantEvaluate.valueOf(_width, context);
        return _valueOf.toString();
      }
    }
    String _plus = (current + " is not a valid type");
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected FluidFrame _toSimulationModel(final HDLVariableRef obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    HDLVariable hVar = obj.resolveVar();
    HDLQualifiedName _asRef = hVar.asRef();
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
    final HDLDirection dir = hVar.getDirection();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(dir,HDLDirection.INTERNAL)) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction = new ArgumentedInstruction(Instruction.loadInternal2, arrBits);
        res.instructions.add(_argumentedInstruction);
      }
    }
    if (!_matched) {
      boolean _or = false;
      boolean _equals = Objects.equal(dir, HDLDirection.PARAMETER);
      if (_equals) {
        _or = true;
      } else {
        boolean _equals_1 = Objects.equal(dir, HDLDirection.CONSTANT);
        _or = (_equals || _equals_1);
      }
      if (_or) {
        _matched=true;
        final BigInteger bVal = ConstantEvaluate.valueOf(obj, context);
        res.constants.put(refName, bVal);
        ArgumentedInstruction _argumentedInstruction_1 = new ArgumentedInstruction(Instruction.loadConstant, refName);
        res.instructions.add(_argumentedInstruction_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(dir,HDLDirection.IN)) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction_2 = new ArgumentedInstruction(Instruction.loadInternal2, arrBits);
        res.instructions.add(_argumentedInstruction_2);
      }
    }
    if (!_matched) {
      boolean _or_1 = false;
      boolean _equals_2 = Objects.equal(dir, HDLDirection.OUT);
      if (_equals_2) {
        _or_1 = true;
      } else {
        boolean _equals_3 = Objects.equal(dir, HDLDirection.INOUT);
        _or_1 = (_equals_2 || _equals_3);
      }
      if (_or_1) {
        _matched=true;
        ArgumentedInstruction _argumentedInstruction_3 = new ArgumentedInstruction(Instruction.loadInternal2, arrBits);
        res.instructions.add(_argumentedInstruction_3);
      }
    }
    if (!_matched) {
      String _plus_1 = ("Did not expect obj here" + dir);
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus_1);
      throw _illegalArgumentException;
    }
    return res;
  }
  
  protected FluidFrame _toSimulationModel(final HDLTernary obj, final HDLEvaluationContext context) {
    FluidFrame _fluidFrame = new FluidFrame();
    final FluidFrame res = _fluidFrame;
    res.setPredicate(true);
    HDLExpression _ifExpr = obj.getIfExpr();
    FluidFrame _simulationModel = this.toSimulationModel(_ifExpr, context);
    res.append(_simulationModel);
    HDLExpression _thenExpr = obj.getThenExpr();
    final FluidFrame thenFrame = this.toSimulationModel(_thenExpr, context);
    thenFrame.addPredicate(res.id, true);
    res.addReferencedFrame(thenFrame);
    HDLExpression _thenExpr_1 = obj.getThenExpr();
    final FluidFrame elseFrame = this.toSimulationModel(_thenExpr_1, context);
    elseFrame.addPredicate(res.id, false);
    res.addReferencedFrame(elseFrame);
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
    } else if (obj instanceof HDLShiftOp) {
      return _toSimulationModel((HDLShiftOp)obj, context);
    } else if (obj instanceof HDLAssignment) {
      return _toSimulationModel((HDLAssignment)obj, context);
    } else if (obj instanceof HDLConcat) {
      return _toSimulationModel((HDLConcat)obj, context);
    } else if (obj instanceof HDLLiteral) {
      return _toSimulationModel((HDLLiteral)obj, context);
    } else if (obj instanceof HDLManip) {
      return _toSimulationModel((HDLManip)obj, context);
    } else if (obj instanceof HDLTernary) {
      return _toSimulationModel((HDLTernary)obj, context);
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
