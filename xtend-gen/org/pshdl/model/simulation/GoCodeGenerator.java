package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.model.simulation.CommonCodeGenerator;

@SuppressWarnings("all")
public class GoCodeGenerator extends CommonCodeGenerator {
  private String pkg;
  
  private String unit;
  
  public GoCodeGenerator(final ExecutableModel em, final int maxCosts, final String pkg, final String unit) {
    super(em, 64, maxCosts);
    this.pkg = pkg;
    this.unit = unit;
  }
  
  protected CharSequence preFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type ");
    _builder.append(this.unit, "");
    _builder.append(" struct {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("varIdx map[string]int");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("regUpdates   []regUpdate");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("regUpdatePos int");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence postFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence applyRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") updateRegs() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("for _, r := range s.regUpdates {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch r.internal {");
    _builder.newLine();
    {
      for(final VariableInformation vi : this.em.variables) {
        _builder.append("\t\t");
        _builder.append("case ");
        int _varIdx = this.getVarIdx(vi);
        _builder.append(_varIdx, "\t\t");
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("s.");
        CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
        _builder.append(_idName, "\t\t\t");
        _builder.append(" = s.");
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isShadowReg);
        CharSequence _idName_1 = this.idName(vi, true, _of);
        _builder.append(_idName_1, "\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("make([]");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    _builder.append(_fieldType, "");
    _builder.append(", ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append(")");
    return _builder;
  }
  
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence callMethod(final String methodName, final String... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    _builder.append(methodName, "");
    _builder.append("((");
    {
      boolean _tripleNotEquals = (args != null);
      if (_tripleNotEquals) {
        {
          boolean _hasElements = false;
          for(final String arg : args) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(arg, "");
          }
        }
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  protected CharSequence callRunMethod() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.Run()");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence checkRegupdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if len(s.regUpdates) > 0 && !s.disableRegOutputLogic {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("break");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence checkTestbenchListener() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.regUpdatePos = 0");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/* copy array */");
    return _builder;
  }
  
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    boolean _or = false;
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(varInfo.dimensions)));
    if (_isNullOrEmpty) {
      _or = true;
    } else {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.baseType);
      _or = _contains;
    }
    if (_or) {
      boolean _isBoolean = this.isBoolean(varInfo, attributes);
      if (_isBoolean) {
        return "bool ";
      }
      return "uint64 ";
    } else {
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "bool[] ";
      }
      return "uint64[] ";
    }
  }
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *SimpleAluGenerator) SetInputWithName(name string, value int64, arrayIdx ...int) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("s.SetInput(s.GetIndex(name), value, arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) SetInput(idx int, value int64, arrayIdx ...int) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation v : _excludeNull) {
        {
          int _length = v.dimensions.length;
          boolean _equals = (_length == 0);
          if (_equals) {
            _builder.append("case ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            StringConcatenation _builder_1 = new StringConcatenation();
            {
              boolean _isPredicate = this.isPredicate(v);
              if (_isPredicate) {
                _builder_1.append("value!=0");
              } else {
                _builder_1.append("value");
              }
            }
            StringBuilder _assignVariable = this.assignVariable(v, _builder_1, CommonCodeGenerator.NONE, true, false);
            _builder.append(_assignVariable, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("break");
            _builder.newLine();
          } else {
            _builder.append("case ");
            Integer _get_1 = this.varIdx.get(v.name);
            _builder.append(_get_1, "");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            CharSequence _idName = this.idName(v, true, CommonCodeGenerator.NONE);
            _builder.append(_idName, "\t");
            _builder.append("[");
            CharSequence _calculateVariableAccessIndexArr = this.calculateVariableAccessIndexArr(v);
            _builder.append(_calculateVariableAccessIndexArr, "\t");
            _builder.append("]=");
            {
              boolean _isPredicate_1 = this.isPredicate(v);
              if (_isPredicate_1) {
                _builder.append("value!=0");
              } else {
                _builder.append("value");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("break");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) GetIndex(name string) int {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("idx, ok := s.varIdx[name]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if !ok {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"The name:\" + name + \" is not a valid index\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return idx");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) GetName(idx int) string {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("case 1:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return \"$Pred_alu.@if0\"");
    _builder.newLine();
    {
      for(final VariableInformation vi : this.em.variables) {
        _builder.append("\t");
        _builder.append("case ");
        int _varIdx = this.getVarIdx(vi);
        _builder.append(_varIdx, "\t");
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index:\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) GetOutputWithName(name string, arrayIdx ...int) int64 {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return s.GetOutput(s.GetIndex(name), arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) GetOutput(idx int, arrayIdx ...int) int64 {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
      for(final VariableInformation v_1 : _excludeNull_1) {
        {
          int _length_1 = v_1.dimensions.length;
          boolean _equals_1 = (_length_1 == 0);
          if (_equals_1) {
            _builder.append("case ");
            Integer _get_2 = this.varIdx.get(v_1.name);
            _builder.append(_get_2, "");
            _builder.append(":");
            _builder.newLineIfNotEmpty();
            {
              boolean _isPredicate_2 = this.isPredicate(v_1);
              if (_isPredicate_2) {
                _builder.append("\t");
                _builder.append("if ");
                CharSequence _idName_1 = this.idName(v_1, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_1, "\t");
                _builder.append(" {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return 1");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("else {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return 0");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("return ");
                CharSequence _idName_2 = this.idName(v_1, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_2, "\t");
                {
                  if ((v_1.width != 64)) {
                    _builder.append(" & ");
                    BigInteger _calcMask = this.calcMask(v_1.width);
                    CharSequence _constant = this.constant(_calcMask);
                    _builder.append(_constant, "\t");
                  }
                }
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("case ");
            Integer _get_3 = this.varIdx.get(v_1.name);
            _builder.append(_get_3, "");
            _builder.append(":");
            _builder.newLineIfNotEmpty();
            {
              boolean _isPredicate_3 = this.isPredicate(v_1);
              if (_isPredicate_3) {
                _builder.append("if ");
                CharSequence _idName_3 = this.idName(v_1, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_3, "");
                _builder.append("[");
                CharSequence _calculateVariableAccessIndexArr_1 = this.calculateVariableAccessIndexArr(v_1);
                _builder.append(_calculateVariableAccessIndexArr_1, "");
                _builder.append("] {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("return 1");
                _builder.newLine();
                _builder.append("else {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("return 0");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("return ");
                CharSequence _idName_4 = this.idName(v_1, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_4, "");
                _builder.append("[");
                CharSequence _calculateVariableAccessIndexArr_2 = this.calculateVariableAccessIndexArr(v_1);
                _builder.append(_calculateVariableAccessIndexArr_2, "");
                _builder.append("]");
                {
                  boolean _and = false;
                  if (!(v_1.width != 64)) {
                    _and = false;
                  } else {
                    boolean _isPredicate_4 = this.isPredicate(v_1);
                    boolean _not = (!_isPredicate_4);
                    _and = _not;
                  }
                  if (_and) {
                    _builder.append(" & ");
                    BigInteger _calcMask_1 = this.calcMask(v_1.width);
                    CharSequence _constant_1 = this.constant(_calcMask_1);
                    _builder.append(_constant_1, "");
                  }
                }
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index:\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *SimpleAluGenerator) getDeltaCycle() int64 {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return s.deltaCycle");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence functionFooter(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence functionHeader(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *SimpleAluGenerator) ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName, "");
    _builder.append(" (){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(this.pkg, "");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("type regUpdate struct {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("internal, offset, fillValue int");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func New");
    _builder.append(this.unit, "");
    _builder.append("() *");
    _builder.append(this.unit, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return NewSimpleAluGeneratorWithArgs(false, false)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func New");
    _builder.append(this.unit, "");
    _builder.append("WithArgs(disableEdge, disableRegOutputLogic bool) *");
    _builder.append(this.unit, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var s = ");
    _builder.append(this.unit, "\t");
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("disableEdge:           disableEdge,");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("disableRegOutputLogic: disableRegOutputLogic,");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("s.regUpdates = make([]regUpdate, ");
    int _maxRegUpdates = this.maxRegUpdates();
    _builder.append(_maxRegUpdates, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.varIdx = make(map[string]int, ");
    int _size = ((List<VariableInformation>)Conversions.doWrapArray(this.em.variables)).size();
    int _minus = (_size - 1);
    _builder.append(_minus, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation v : _excludeNull) {
        _builder.append("s.varIdx[\"");
        _builder.append(v.name, "");
        _builder.append("\"] =  ");
        Integer _get = this.varIdx.get(v.name);
        _builder.append(_get, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return &s");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") skipEdge(local uint64) bool {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var dc = local >> 16 // zero-extended shift");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if dc < s.deltaCycle {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return false");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (dc == s.deltaCycle) && ((local & 0xFFFF) == s.epsCycle) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return false");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return true");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence runMethodsFooter(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence runMethodsHeader(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *SimpleAluGenerator) ");
    {
      if ((!constant)) {
        _builder.append("Run");
      } else {
        _builder.append("InitConstants");
      }
    }
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence runTestbenchHeader() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean force, final CharSequence fillValue) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((!force)) {
        _builder.append("if (");
        _builder.append(cpyName, "");
        _builder.append("!=");
        _builder.append(last, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        CharSequence _indent = this.indent();
        _builder.append(_indent, "");
        _builder.append("\t");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence stageMethodsFooter(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence stageMethodsHeader(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *SimpleAluGenerator) ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("() {");
    return _builder;
  }
}
