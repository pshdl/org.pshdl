package org.pshdl.model.simulation.codegenerator;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.math.BigInteger;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.codegenerator.CommonCodeGenerator;
import org.pshdl.model.simulation.codegenerator.CommonCodeGeneratorParameter;

@SuppressWarnings("all")
public class VerilogCodeGenerator extends CommonCodeGenerator {
  private Multimap<Integer, Frame> writes = LinkedListMultimap.<Integer, Frame>create();
  
  private Multimap<Integer, VariableInformation> posClocks = LinkedHashMultimap.<Integer, VariableInformation>create();
  
  private Multimap<Integer, VariableInformation> negClocks = LinkedHashMultimap.<Integer, VariableInformation>create();
  
  public VerilogCodeGenerator(final ExecutableModel model) {
    super(new CommonCodeGeneratorParameter(model, (-1), Integer.MAX_VALUE, false));
    for (final Frame f : this.em.frames) {
      for (final int outputId : f.outputIds) {
        {
          final InternalInformation internal = this.em.internals[outputId];
          if ((f.edgeNegDepRes != (-1))) {
            this.negClocks.put(Integer.valueOf(f.edgeNegDepRes), internal.info);
          }
          if ((f.edgePosDepRes != (-1))) {
            this.posClocks.put(Integer.valueOf(f.edgePosDepRes), internal.info);
          }
          this.writes.put(Integer.valueOf(outputId), f);
        }
      }
    }
  }
  
  @Override
  public String generateMainCode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("`timescale 1ns/1ps");
    _builder.newLine();
    _builder.append("module ");
    CharSequence _idName = this.idName(this.em.moduleName, false, CommonCodeGenerator.NONE);
    _builder.append(_idName);
    _builder.append("(");
    _builder.newLineIfNotEmpty();
    {
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        @Override
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir != VariableInformation.Direction.INTERNAL));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function);
      boolean _hasElements = false;
      for(final VariableInformation vi : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        CharSequence _idName_1 = this.idName(vi, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(");");
    _builder.newLine();
    {
      for(final VariableInformation vi_1 : this.em.variables) {
        String _declaration = this.declaration(vi_1, CommonCodeGenerator.NONE);
        _builder.append(_declaration);
        _builder.newLineIfNotEmpty();
        {
          if (vi_1.isRegister) {
            String _declaration_1 = this.declaration(vi_1, CommonCodeGenerator.SHADOWREG);
            _builder.append(_declaration_1);
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      Set<Integer> _keySet = this.posClocks.keySet();
      for(final Integer ii : _keySet) {
        _builder.append("always @(posedge ");
        CharSequence _idName_2 = this.idName(this.em.internals[(ii).intValue()].info, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_2);
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("begin");
        _builder.newLine();
        {
          Collection<VariableInformation> _get = this.posClocks.get(ii);
          for(final VariableInformation vi_2 : _get) {
            _builder.append("\t");
            CharSequence _idName_3 = this.idName(vi_2, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_3, "\t");
            _builder.append(" <= ");
            CharSequence _idName_4 = this.idName(vi_2, true, CommonCodeGenerator.SHADOWREG);
            _builder.append(_idName_4, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("end");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      Set<Integer> _keySet_1 = this.negClocks.keySet();
      for(final Integer ii_1 : _keySet_1) {
        _builder.append("always @(negedge ");
        CharSequence _idName_5 = this.idName(this.em.internals[(ii_1).intValue()].info, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_5);
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("begin");
        _builder.newLine();
        {
          Collection<VariableInformation> _get_1 = this.negClocks.get(ii_1);
          for(final VariableInformation vi_3 : _get_1) {
            _builder.append("\t");
            CharSequence _idName_6 = this.idName(vi_3, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_6, "\t");
            _builder.append(" <= ");
            CharSequence _idName_7 = this.idName(vi_3, true, CommonCodeGenerator.SHADOWREG);
            _builder.append(_idName_7, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("end");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      Set<Integer> _keySet_2 = this.writes.keySet();
      for(final Integer ii_2 : _keySet_2) {
        CharSequence _generateWriteProcess = this.generateWriteProcess(ii_2);
        _builder.append(_generateWriteProcess);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("endmodule;");
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence generateWriteProcess(final Integer internal) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("always @(");
    final Function1<Frame, Set<CharSequence>> _function = new Function1<Frame, Set<CharSequence>>() {
      @Override
      public Set<CharSequence> apply(final Frame it) {
        final Function1<Integer, CharSequence> _function = new Function1<Integer, CharSequence>() {
          @Override
          public CharSequence apply(final Integer it) {
            return VerilogCodeGenerator.this.sensitiviyName(it);
          }
        };
        return IterableExtensions.<CharSequence>toSet(ListExtensions.<Integer, CharSequence>map(((List<Integer>)Conversions.doWrapArray(it.internalDependencies)), _function));
      }
    };
    String _join = IterableExtensions.join(Iterables.<CharSequence>concat(IterableExtensions.<Frame, Set<CharSequence>>map(this.writes.get(internal), _function)), " or ");
    _builder.append(_join);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("begin");
    _builder.newLine();
    {
      Collection<Frame> _get = this.writes.get(internal);
      for(final Frame f : _get) {
        _builder.append("\t");
        final Function1<Integer, String> _function_1 = new Function1<Integer, String>() {
          @Override
          public String apply(final Integer it) {
            CharSequence _sensitiviyName = VerilogCodeGenerator.this.sensitiviyName(it);
            return (_sensitiviyName + " == 0");
          }
        };
        List<String> _map = ListExtensions.<Integer, String>map(((List<Integer>)Conversions.doWrapArray(f.predNegDepRes)), _function_1);
        final Function1<Integer, CharSequence> _function_2 = new Function1<Integer, CharSequence>() {
          @Override
          public CharSequence apply(final Integer it) {
            return VerilogCodeGenerator.this.sensitiviyName(it);
          }
        };
        List<CharSequence> _map_1 = ListExtensions.<Integer, CharSequence>map(((List<Integer>)Conversions.doWrapArray(f.predPosDepRes)), _function_2);
        final Iterable<CharSequence> predSet = Iterables.<CharSequence>concat(_map, _map_1);
        _builder.newLineIfNotEmpty();
        {
          boolean _isEmpty = IterableExtensions.isEmpty(predSet);
          boolean _not = (!_isEmpty);
          if (_not) {
            _builder.append("\t");
            _builder.append("if (");
            String _join_1 = IterableExtensions.join(predSet, " && ");
            _builder.append(_join_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("  ");
            _builder.append("begin");
            _builder.newLine();
            _builder.append("\t");
            CharSequence _frameExecution = this.frameExecution(f);
            _builder.append(_frameExecution, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(" ");
            _builder.append("end");
            _builder.newLine();
          } else {
            _builder.append("\t");
            CharSequence _frameExecution_1 = this.frameExecution(f);
            _builder.append(_frameExecution_1, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("end");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence updatePrediateTimestamp(final InternalInformation outputInternal) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence fixupValue(final CharSequence assignValue, final int targetSizeWithType, final boolean doMask) {
    return assignValue;
  }
  
  protected StringBuilder toCastExpression(final Frame.FastInstruction exec, final Frame frame, final int pos, final int a, final int b, final int arrPos) {
    final StringBuilder sb = new StringBuilder();
    final String tempName = this.getTempName(a, CommonCodeGenerator.NONE);
    final Instruction _switchValue = exec.inst;
    if (_switchValue != null) {
      switch (_switchValue) {
        case bitAccessSingle:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append(tempName);
          _builder.append("[");
          _builder.append(exec.arg1);
          _builder.append("]");
          sb.append(this.assignTempVar(VariableInformation.Type.BIT, 2, pos, CommonCodeGenerator.NONE, _builder, false));
          break;
        case bitAccessSingleRange:
          final int highBit = exec.arg1;
          final int lowBit = exec.arg2;
          final int targetSize = ((highBit - lowBit) + 1);
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append(tempName);
          _builder_1.append("[");
          _builder_1.append(exec.arg1);
          _builder_1.append(":");
          _builder_1.append(exec.arg2);
          _builder_1.append("]");
          sb.append(this.assignTempVar(VariableInformation.Type.BIT, (targetSize << 1), pos, CommonCodeGenerator.NONE, _builder_1, false));
          break;
        case cast_int:
          int _min = Math.min(exec.arg1, exec.arg2);
          int _bitwiseOr = ((_min << 1) | 1);
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append(tempName);
          _builder_2.append("[");
          int _min_1 = Math.min(exec.arg1, exec.arg2);
          int _minus = (_min_1 - 1);
          _builder_2.append(_minus);
          _builder_2.append(":0]");
          sb.append(
            this.assignTempVar(VariableInformation.Type.INT, _bitwiseOr, pos, CommonCodeGenerator.NONE, _builder_2, false));
          break;
        case cast_uint:
          int _min_2 = Math.min(exec.arg1, exec.arg2);
          int _doubleLessThan = (_min_2 << 1);
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append(tempName);
          _builder_3.append("[");
          int _min_3 = Math.min(exec.arg1, exec.arg2);
          int _minus_1 = (_min_3 - 1);
          _builder_3.append(_minus_1);
          _builder_3.append(":0]");
          sb.append(
            this.assignTempVar(VariableInformation.Type.UINT, _doubleLessThan, pos, CommonCodeGenerator.NONE, _builder_3, false));
          break;
        default:
          throw new IllegalArgumentException((("Did not instruction:" + exec) + " here"));
      }
    } else {
      throw new IllegalArgumentException((("Did not instruction:" + exec) + " here"));
    }
    return sb;
  }
  
  protected CharSequence toEdgeExpression(final Frame.FastInstruction exec, final Frame frame, final int pos, final int a, final int b, final int arrPos) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence handlePredicates(final Set<Integer> handledPredicates, final boolean positive, final int[] predicates) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence updatePredicateFreshness(final int pred, final boolean positive) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence updateHandledClk(final int edgeDepRes, final boolean posEdge) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence updateEdge(final int edgeDepRes, final boolean posEdge) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence sensitiviyName(final Integer internal) {
    final VariableInformation vi = this.em.internals[(internal).intValue()].info;
    EnumSet<CommonCodeGenerator.Attributes> attributes = CommonCodeGenerator.NONE;
    if (vi.isRegister) {
      attributes = CommonCodeGenerator.SHADOWREG;
    }
    return this.idName(vi, true, attributes);
  }
  
  public String declaration(final VariableInformation vi, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    CharSequence type = null;
    final VariableInformation.Direction _switchValue = vi.dir;
    if (_switchValue != null) {
      switch (_switchValue) {
        case IN:
          type = "input";
          break;
        case INOUT:
          type = "inout";
          break;
        case OUT:
          type = "output";
          break;
        case INTERNAL:
          type = "reg";
          break;
        default:
          break;
      }
    }
    boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.isShadowReg);
    if (_contains) {
      type = "reg";
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    _builder.append((vi.width - 1));
    _builder.append(":0]");
    CharSequence width = _builder;
    if ((vi.width == 1)) {
      boolean _isArray = this.isArray(vi);
      if (_isArray) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("[0:");
        int _arraySize = this.getArraySize(vi);
        int _minus = (_arraySize - 1);
        _builder_1.append(_minus);
        _builder_1.append("]");
        width = _builder_1;
      } else {
        width = "";
      }
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    CharSequence dim = _builder_2;
    boolean _isArray_1 = this.isArray(vi);
    if (_isArray_1) {
      if ((vi.width != 1)) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("[0:");
        int _arraySize_1 = this.getArraySize(vi);
        int _minus_1 = (_arraySize_1 - 1);
        _builder_3.append(_minus_1);
        _builder_3.append("]");
        dim = _builder_3;
      }
    }
    StringConcatenation _builder_4 = new StringConcatenation();
    _builder_4.append(type);
    _builder_4.append(" ");
    _builder_4.append(width);
    _builder_4.append(" ");
    CharSequence _idName = this.idName(vi, true, attributes);
    _builder_4.append(_idName);
    _builder_4.append(" ");
    _builder_4.append(dim);
    _builder_4.append(";");
    return _builder_4.toString();
  }
  
  @Override
  protected CharSequence idName(final String name, final boolean field, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    return super.idName(name, field, attributes).toString().replace("$", "__");
  }
  
  @Override
  protected CharSequence applyRegUpdates() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callMethod(final boolean pshdlFunction, final CharSequence methodName, final CharSequence... args) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callRunMethod() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callStage(final int stage, final boolean constant) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence checkRegupdates() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence checkTestbenchListener() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence clearRegUpdates() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence copyArray(final VariableInformation varInfo) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    String type = "reg";
    String width = "";
    if ((varInfo.width > 1)) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[");
      _builder.append((varInfo.width - 1));
      _builder.append(":0]");
      width = _builder.toString();
    }
    if ((varInfo.width <= 0)) {
      width = "[31:0]";
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append(type);
    _builder_1.append(width);
    return _builder_1;
  }
  
  @Override
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence footer() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence functionFooter(final Frame frame) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence functionHeader(final Frame frame) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence header() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runMethodsFooter(final boolean constant) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runMethodsHeader(final boolean constant) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runTestbenchHeader() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean force, final CharSequence fillValue) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  @Override
  protected CharSequence stageMethodsFooter(final int stage, final int totalStageCosts, final boolean constant) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence stageMethodsHeader(final int stage, final int totalStageCosts, final boolean constant) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
