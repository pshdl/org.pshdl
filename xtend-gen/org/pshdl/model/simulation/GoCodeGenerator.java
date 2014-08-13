package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.CommonCompilerExtension;

@SuppressWarnings("all")
public class GoCodeGenerator extends CommonCodeGenerator {
  private String pkg;
  
  private String unit;
  
  private CommonCompilerExtension cce;
  
  public GoCodeGenerator(final ExecutableModel em, final int maxCosts, final String pkg, final String unit) {
    super(em, 64, maxCosts);
    this.pkg = pkg;
    this.unit = unit;
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em, 64);
    this.cce = _commonCompilerExtension;
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") updateRegs() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("for _, reg := range s.regUpdates {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch reg.internal {");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _updateRegCases = this.updateRegCases();
    _builder.append(_updateRegCases, "\t\t");
    _builder.newLineIfNotEmpty();
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
  
  protected CharSequence doLoopStart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for {");
    return _builder;
  }
  
  protected CharSequence doLoopEnd(final CharSequence condition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("if (");
    _builder.append(condition, "\t");
    _builder.append(") { break }");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence applyRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.updateRegs()");
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
  
  protected CharSequence callMethod(final CharSequence methodName, final CharSequence... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    _builder.append(methodName, "");
    _builder.append("(");
    {
      boolean _tripleNotEquals = (args != null);
      if (_tripleNotEquals) {
        {
          boolean _hasElements = false;
          for(final CharSequence arg : args) {
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
    _builder.append("s.regUpdatePos == 0");
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
        return "bool";
      }
      return "int64";
    } else {
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "[]bool";
      }
      return "[]int64";
    }
  }
  
  protected CharSequence doCast(final CharSequence cast, final CharSequence assignValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(cast, "");
    _builder.append("(");
    _builder.append(assignValue, "");
    _builder.append(")");
    return _builder;
  }
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") SetInputWithName(name string, value int64, arrayIdx ...int) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.SetInput(s.GetIndex(name), value, arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") SetInput(idx int, value int64, arrayIdx ...int) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _setInputCases = this.setInputCases("value", null);
    _builder.append(_setInputCases, "\t");
    _builder.newLineIfNotEmpty();
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetIndex(name string) int {");
    _builder.newLineIfNotEmpty();
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetName(idx int) string {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetOutputWithName(name string, arrayIdx ...int) int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return s.GetOutput(s.GetIndex(name), arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetOutput(idx int, arrayIdx ...int) int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _outputCases = this.getOutputCases(null);
    _builder.append(_outputCases, "\t");
    _builder.newLineIfNotEmpty();
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetDeltaCycle() int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return s.deltaCycle");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") GetJsonDesc() string {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return \"");
    String _jSONDescription = this.cce.getJSONDescription();
    _builder.append(_jSONDescription, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") SetDisableEdge(enable bool) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
    _builder.append(" = enable");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") SetDisableRegOutputLogic(enable bool) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
    _builder.append(" = enable");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") ");
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
    _builder.append("internal, offset int");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fillValue int64");
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
    _builder.append("return New");
    _builder.append(this.unit, "\t");
    _builder.append("WithArgs(false, false)");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func New");
    _builder.append(this.unit, "");
    _builder.append("WithArgs(");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "");
    _builder.append(", ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "");
    _builder.append(" bool) *");
    _builder.append(this.unit, "");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var s = ");
    _builder.append(this.unit, "\t");
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
    _builder.append(":           ");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
    _builder.append(": ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
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
        _builder.append("\t");
        _builder.append("s.varIdx[\"");
        _builder.append(v.name, "\t");
        _builder.append("\"] =  ");
        Integer _get = this.varIdx.get(v.name);
        _builder.append(_get, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf(GoCodeGenerator.this.isArray(it));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function);
      for(final VariableInformation v_1 : _filter) {
        _builder.append("\t");
        CharSequence _idName = this.idName(v_1, true, CommonCodeGenerator.NONE);
        _builder.append(_idName, "\t");
        _builder.append(" = make([]int64, ");
        int _arraySize = this.getArraySize(v_1);
        _builder.append(_arraySize, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        {
          if (v_1.isRegister) {
            _builder.append("\t");
            CharSequence _idName_1 = this.idName(v_1, true, CommonCodeGenerator.SHADOWREG);
            _builder.append(_idName_1, "\t");
            _builder.append(" = make([]int64, ");
            int _arraySize_1 = this.getArraySize(v_1);
            _builder.append(_arraySize_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return &s");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") skipEdge(local int64) bool {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var dc = int64(uint64(local) >> 16) // zero-extended shift");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if dc < s.");
    _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return false");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (dc == s.");
    _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
    _builder.append(") && ((local & 0xFFFF) == s.");
    _builder.append(CommonCodeGenerator.EPS_CYCLE.name, "\t");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
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
  
  protected CharSequence idName(final String name, final boolean field, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    CharSequence _idName = super.idName(name, field, attributes);
    String _string = _idName.toString();
    final String superVal = _string.replace("$", "__");
    return superVal;
  }
  
  protected String fieldPrefix() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    return _builder.toString();
  }
  
  protected CharSequence createVarDeclaration(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean initialize) {
    final StringBuilder sb = new StringBuilder();
    CharSequence _preField = this.preField(varInfo, attributes);
    sb.append(_preField);
    this.indent++;
    CharSequence _indent = this.indent();
    sb.append(_indent);
    this.indent--;
    CharSequence _idName = this.idName(varInfo, false, attributes);
    StringBuilder _append = sb.append(_idName);
    _append.append(" ");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    sb.append(_fieldType);
    CharSequence _postField = this.postField(varInfo);
    sb.append(_postField);
    return sb;
  }
  
  protected String constantSuffix() {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  protected CharSequence inlineVarDecl(final VariableInformation varInfo, final boolean field, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("var ");
    CharSequence _idName = this.idName(varInfo, field, attributes);
    _builder.append(_idName, "");
    _builder.append(" ");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    _builder.append(_fieldType, "");
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") ");
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
        _builder.append(") ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    CharSequence _indent = this.indent();
    _builder.append(_indent, "");
    _builder.append("\ts.regUpdates[s.regUpdatePos] = regUpdate{");
    int _varIdx = this.getVarIdx(outputInternal);
    _builder.append(_varIdx, "");
    _builder.append(", int(");
    _builder.append(offset, "");
    _builder.append("), ");
    _builder.append(fillValue, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("\ts.regUpdatePos++");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("}");
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
    _builder.append("func (s *");
    _builder.append(this.unit, "");
    _builder.append(") ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for i := range ");
    CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(" { ");
    CharSequence _idName_1 = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1, "");
    _builder.append("[i] = ");
    _builder.append(regFillValue, "");
    _builder.append(" } ");
    return _builder;
  }
  
  protected CharSequence singleOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int a, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    CharSequence _xblockexpression = null;
    {
      boolean _tripleEquals = (fi.inst == Instruction.bit_neg);
      if (_tripleEquals) {
        CharSequence _cast = this.getCast(targetSizeWithType);
        final CharSequence assignValue = this.singleOpValue("^", _cast, a, targetSizeWithType, attributes);
        return this.assignTempVar(targetSizeWithType, pos, attributes, assignValue);
      }
      _xblockexpression = super.singleOp(fi, op, targetSizeWithType, pos, a, attributes);
    }
    return _xblockexpression;
  }
  
  protected CharSequence twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    CharSequence _xblockexpression = null;
    {
      boolean _tripleEquals = (fi.inst == Instruction.srl);
      if (_tripleEquals) {
        String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        CharSequence _doCast = this.doCast("uint64", _tempName);
        String _plus = (_doCast + ">>");
        String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
        CharSequence _doCast_1 = this.doCast("uint64", _tempName_1);
        String _plus_1 = (_plus + _doCast_1);
        final CharSequence assignValue = this.doCast("int64", _plus_1);
        return this.assignTempVar(targetSizeWithType, pos, attributes, assignValue);
      }
      boolean _tripleEquals_1 = (fi.inst == Instruction.sll);
      if (_tripleEquals_1) {
        String _tempName_2 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        String _plus_2 = (_tempName_2 + "<<");
        String _tempName_3 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
        CharSequence _doCast_2 = this.doCast("uint64", _tempName_3);
        String _plus_3 = (_plus_2 + _doCast_2);
        final CharSequence assignValue_1 = this.doCast("int64", _plus_3);
        return this.assignTempVar(targetSizeWithType, pos, attributes, assignValue_1);
      }
      _xblockexpression = super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes);
    }
    return _xblockexpression;
  }
  
  protected CharSequence writeToNull(final String last) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("var _ = ");
    _builder.append(last, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
