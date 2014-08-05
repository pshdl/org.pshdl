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
import org.pshdl.model.simulation.HDLSimulator;
import org.pshdl.model.simulation.ICodeGen;

@SuppressWarnings("all")
public class JavaCodeGenerator extends CommonCodeGenerator implements ICodeGen {
  private String packageName;
  
  private String unitName;
  
  public JavaCodeGenerator(final ExecutableModel em, final String packageName, final String unitName, final int maxCosts) {
    super(em, 64, maxCosts);
    this.packageName = packageName;
    this.unitName = unitName;
  }
  
  protected void postBody() {
    this.indent--;
  }
  
  protected void preBody() {
    this.indent++;
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
        return "boolean ";
      }
      return "long ";
    } else {
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "boolean[] ";
      }
      return "long[] ";
    }
  }
  
  protected CharSequence preField(final VariableInformation x, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.isPublic);
      if (_contains) {
        _builder.append("public");
      } else {
        _builder.append("private");
      }
    }
    _builder.append(" ");
    return _builder;
  }
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    CharSequence _hdlInterpreter = this.hdlInterpreter();
    _builder.append(_hdlInterpreter, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence hdlInterpreter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(String name, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setInput(getIndex(name), value, arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(int idx, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation v : _excludeNull) {
        {
          int _length = v.dimensions.length;
          boolean _equals = (_length == 0);
          if (_equals) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
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
            _builder.append(_assignVariable, "\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("break;");
            _builder.newLine();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_1 = this.varIdx.get(v.name);
            _builder.append(_get_1, "\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            CharSequence _idName = this.idName(v, true, CommonCodeGenerator.NONE);
            _builder.append(_idName, "\t\t\t");
            _builder.append("[");
            CharSequence _calculateVariableAccessIndexArr = this.calculateVariableAccessIndexArr(v);
            _builder.append(_calculateVariableAccessIndexArr, "\t\t\t");
            _builder.append("]=");
            {
              boolean _isPredicate_1 = this.isPredicate(v);
              if (_isPredicate_1) {
                _builder.append("value!=0");
              } else {
                _builder.append("value");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("break;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("throw new IllegalArgumentException(\"Not a valid index:\" + idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Integer idx=varIdx.get(name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (idx==null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("throw new IllegalArgumentException(\"The name:\"+name+\" is not a valid index\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return idx;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
      for(final VariableInformation v_1 : _excludeNull_1) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get_2 = this.varIdx.get(v_1.name);
        _builder.append(_get_2, "\t\t");
        _builder.append(": return \"");
        _builder.append(v_1.name, "\t\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("throw new IllegalArgumentException(\"Not a valid index:\" + idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public long getOutputLong(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return getOutputLong(getIndex(name), arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public long getOutputLong(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_2 = this.excludeNull(this.em.variables);
      for(final VariableInformation v_2 : _excludeNull_2) {
        {
          int _length_1 = v_2.dimensions.length;
          boolean _equals_1 = (_length_1 == 0);
          if (_equals_1) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_3 = this.varIdx.get(v_2.name);
            _builder.append(_get_3, "\t\t");
            _builder.append(": return ");
            CharSequence _idName_1 = this.idName(v_2, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_1, "\t\t");
            {
              boolean _isPredicate_2 = this.isPredicate(v_2);
              if (_isPredicate_2) {
                _builder.append("?1:0");
              } else {
                if ((v_2.width != 64)) {
                  _builder.append(" & ");
                  BigInteger _calcMask = this.calcMask(v_2.width);
                  CharSequence _constant = this.constant(_calcMask);
                  _builder.append(_constant, "\t\t");
                }
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_4 = this.varIdx.get(v_2.name);
            _builder.append(_get_4, "\t\t");
            _builder.append(": return ");
            CharSequence _idName_2 = this.idName(v_2, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_2, "\t\t");
            _builder.append("[");
            CharSequence _calculateVariableAccessIndexArr_1 = this.calculateVariableAccessIndexArr(v_2);
            _builder.append(_calculateVariableAccessIndexArr_1, "\t\t");
            _builder.append("]");
            {
              boolean _and = false;
              if (!(v_2.width != 64)) {
                _and = false;
              } else {
                boolean _isPredicate_3 = this.isPredicate(v_2);
                boolean _not = (!_isPredicate_3);
                _and = _not;
              }
              if (_and) {
                _builder.append(" & ");
                BigInteger _calcMask_1 = this.calcMask(v_2.width);
                CharSequence _constant_1 = this.constant(_calcMask_1);
                _builder.append(_constant_1, "\t\t");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("throw new IllegalArgumentException(\"Not a valid index:\" + idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public long getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return deltaCycle;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void close() throws Exception{");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setFeature(Feature feature, Object value) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (feature) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("case disableOutputRegs:");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append(" = (boolean) value;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("case disableEdges:");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append(" = (boolean) value;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (this.packageName != null);
      if (_tripleNotEquals) {
        _builder.append("package ");
        _builder.append(this.packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _imports = this.getImports();
    _builder.append(_imports, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class ");
    _builder.append(this.unitName, "");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("implements ");
    {
      boolean _and = false;
      boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(this.em.annotations)));
      boolean _not = (!_isNullOrEmpty);
      if (!_not) {
        _and = false;
      } else {
        String _name = HDLSimulator.TB_UNIT.getName();
        String _substring = _name.substring(1);
        boolean _contains = ((List<String>)Conversions.doWrapArray(this.em.annotations)).contains(_substring);
        _and = _contains;
      }
      if (_and) {
        _builder.append("IHDLTestbenchInterpreter");
      } else {
        _builder.append("IHDLInterpreter");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Map<String, Integer> varIdx=new HashMap<String, Integer>();");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence postFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.maxCosts != Integer.MAX_VALUE)) {
        _builder.append("public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (this.hasClock) {
        _builder.append("private List<RegUpdate> regUpdates=new ArrayList<RegUpdate>();");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append("* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false");
        _builder.newLine();
        _builder.append("*/");
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this(");
        {
          if (this.hasClock) {
            _builder.append("false, false");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("(boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "");
        _builder.append(", boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
          for(final VariableInformation v : _excludeNull) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v.name, "\t");
            _builder.append("\", ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        {
          Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
          for(final VariableInformation v_1 : _excludeNull_1) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v_1.name, "\t");
            _builder.append("\", ");
            Integer _get_1 = this.varIdx.get(v_1.name);
            _builder.append(_get_1, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("(boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "");
        _builder.append(", boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this();");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      if (this.hasClock) {
        _builder.append("public boolean skipEdge(long local) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("long dc = local >>> 16l;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// Register was updated in previous delta cylce, that is ok");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (dc < ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// Register was updated in this delta cycle but it is the same eps,");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// that is ok as well");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if ((dc == ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
        _builder.append(") && ((local & 0xFFFF) == ");
        _builder.append(CommonCodeGenerator.EPS_CYCLE.name, "\t");
        _builder.append("))");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// Don\'t update");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return true;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        CharSequence _copyRegs = this.copyRegs();
        _builder.append(_copyRegs, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private void updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (RegUpdate reg : regUpdates) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (reg.internalID) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.em.variables) {
        {
          if (v.isRegister) {
            _builder.append("\t\t\t");
            _builder.append("case ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "\t\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            {
              int _length = v.dimensions.length;
              boolean _equals = (_length == 0);
              if (_equals) {
                _builder.append("\t\t\t");
                CharSequence _idName = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName, "\t\t\t");
                _builder.append(" = ");
                CharSequence _idName_1 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_1, "\t\t\t");
                _builder.append("$reg; break;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t\t");
                _builder.append("if (reg.offset!=-1)");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                CharSequence _idName_2 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_2, "\t\t\t\t");
                _builder.append("[reg.offset] = ");
                CharSequence _idName_3 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_3, "\t\t\t\t");
                _builder.append("$reg[reg.offset];");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                _builder.append("Arrays.fill(");
                CharSequence _idName_4 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_4, "\t\t\t\t");
                _builder.append(", reg.fillValue); ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t");
                _builder.append("break;");
                _builder.newLine();
              }
            }
          }
        }
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
  
  protected CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.*;");
    _builder.newLine();
    _builder.append("import java.math.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.*;");
    _builder.newLine();
    _builder.append("import java.util.concurrent.*;");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence calculateVariableAccessIndex(final List<Integer> arr, final VariableInformation varInfo) {
    final CharSequence res = super.calculateVariableAccessIndex(arr, varInfo);
    int _length = res.length();
    boolean _tripleEquals = (_length == 0);
    if (_tripleEquals) {
      return res;
    }
    return (("(int)(" + res) + ")");
  }
  
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger zero, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    final EnumSet<CommonCodeGenerator.Attributes> attrClone = attributes.clone();
    attrClone.add(CommonCodeGenerator.Attributes.baseType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("new ");
    CharSequence _fieldType = this.fieldType(varInfo, attrClone);
    _builder.append(_fieldType, "");
    _builder.append("[");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append("]");
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
    _builder.append("private final void ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean forceRegUpdate, final CharSequence fillValue) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((!forceRegUpdate)) {
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
    _builder.append("regUpdates.add(new RegUpdate(");
    int _varIdx = this.getVarIdx(outputInternal);
    _builder.append(_varIdx, "");
    _builder.append(", ");
    _builder.append(offset, "");
    _builder.append(", ");
    _builder.append(fillValue, "");
    _builder.append("));");
    return _builder;
  }
  
  protected CharSequence runMethodsHeader(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void ");
    {
      if ((!constant)) {
        _builder.append("run");
      } else {
        _builder.append("initConstants");
      }
    }
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence runMethodsFooter(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence stageMethodsFooter(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("List<Callable<Void>> calls=new ArrayList<Callable<Void>>();");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _indent = this.indent();
    _builder.append(_indent, "\t");
    _builder.append("calls.add(new Callable<Void>() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "\t");
    _builder.append("@Override public Void call() {");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    int _indent_2 = this.indent;
    indent = (_indent_2 + 2);
    return res;
  }
  
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean constant) {
    int _indent = this.indent;
    indent = (_indent - 2);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return null;");
    _builder.newLine();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("});");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3, "");
    _builder.append("try {");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_4 = this.indent();
    _builder.append(_indent_4, "");
    _builder.append("\tmainPool.invokeAll(calls);");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_5 = this.indent();
    _builder.append(_indent_5, "");
    _builder.append("} catch (final InterruptedException e) {");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_6 = this.indent();
    _builder.append(_indent_6, "");
    _builder.append("\tnew RuntimeException(e);");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_7 = this.indent();
    _builder.append(_indent_7, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    return res;
  }
  
  protected CharSequence barrier() {
    int _indent = this.indent;
    indent = (_indent - 2);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("return null;");
    _builder.newLine();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("\t}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("});");
    _builder.newLineIfNotEmpty();
    _builder.append("calls.add(new Callable<Void>() {");
    _builder.newLine();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3, "");
    _builder.append("@Override public Void call() {");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    int _indent_4 = this.indent;
    indent = (_indent_4 + 2);
    return res;
  }
  
  protected CharSequence stageMethodsHeader(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence applyRegUpdates() {
    return "updateRegs();";
  }
  
  protected CharSequence checkRegupdates() {
    return "!regUpdates.isEmpty()";
  }
  
  public CharSequence compile(final String packageName, final String unitName) {
    return this.doGenerateMainUnit();
  }
  
  public CharSequence createChangeAdapter(final String packageName, final String unitName) {
    return this.createChangeAdapter(false);
  }
  
  public CharSequence createChangeAdapter(final boolean useInterface) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (this.packageName != null);
      if (_tripleNotEquals) {
        _builder.append("package ");
        _builder.append(this.packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.IChangeListener;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.IHDLInterpreter;");
    _builder.newLine();
    _builder.append("import java.math.BigInteger;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class ");
    {
      if (useInterface) {
        _builder.append("Generic");
      }
    }
    _builder.append("ChangeAdapter");
    _builder.append(this.unitName, "");
    _builder.append(" implements IHDLInterpreter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _fieldDeclarations = this.fieldDeclarations(false);
    _builder.append(_fieldDeclarations, "\t");
    _builder.newLineIfNotEmpty();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
          for(final VariableInformation varInfo : _excludeNull) {
            _builder.append("\t");
            _builder.append("int ");
            CharSequence _idName = this.idName(varInfo, true, CommonCodeGenerator.NONE);
            _builder.append(_idName, "\t");
            _builder.append("_idx;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    {
      if (useInterface) {
        _builder.append("IHDLInterpreter");
      } else {
        _builder.append(this.unitName, "\t");
      }
    }
    _builder.append(" module;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private IChangeListener[] listeners;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    {
      if (useInterface) {
        _builder.append("Generic");
      }
    }
    _builder.append("ChangeAdapter");
    _builder.append(this.unitName, "\t");
    _builder.append("(");
    {
      if (useInterface) {
        _builder.append("IHDLInterpreter");
      } else {
        _builder.append(this.unitName, "\t");
      }
    }
    _builder.append(" module, IChangeListener ... listeners) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this.module=module;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.listeners=listeners;");
    _builder.newLine();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
          for(final VariableInformation varInfo_1 : _excludeNull_1) {
            _builder.append("\t\t");
            CharSequence _idName_1 = this.idName(varInfo_1, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_1, "\t\t");
            _builder.append("_idx=module.getIndex(\"");
            _builder.append(varInfo_1.name, "\t\t");
            _builder.append("\");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void run() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.run();");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_2 = this.excludeNull(this.em.variables);
      for(final VariableInformation varInfo_2 : _excludeNull_2) {
        _builder.append("\t\t");
        final CharSequence varName = this.idName(varInfo_2, true, CommonCodeGenerator.NONE);
        _builder.newLineIfNotEmpty();
        {
          if (useInterface) {
            _builder.append("\t\t");
            String _changedNotificationInterface = this.changedNotificationInterface(varInfo_2);
            _builder.append(_changedNotificationInterface, "\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _isArray = this.isArray(varInfo_2);
              if (_isArray) {
                {
                  boolean _isPredicate = this.isPredicate(varInfo_2);
                  if (_isPredicate) {
                    _builder.append("\t\t");
                    _builder.append("for (int i=0;i<");
                    int _arraySize = this.getArraySize(varInfo_2);
                    _builder.append(_arraySize, "\t\t");
                    _builder.append(";i++)");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t\t");
                    _builder.append("\t");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("[i]=module.getOutputLong(");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("_idx, i)!=0;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append("for (int i=0;i<");
                    int _arraySize_1 = this.getArraySize(varInfo_2);
                    _builder.append(_arraySize_1, "\t\t");
                    _builder.append(";i++)");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t\t");
                    _builder.append("\t");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("[i]=module.getOutputLong(");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("_idx, i);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  boolean _isPredicate_1 = this.isPredicate(varInfo_2);
                  if (_isPredicate_1) {
                    _builder.append("\t\t");
                    _builder.append(varName, "\t\t");
                    _builder.append("=module.getOutputLong(");
                    _builder.append(varName, "\t\t");
                    _builder.append("_idx)!=0;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append(varName, "\t\t");
                    _builder.append("=module.getOutputLong(");
                    _builder.append(varName, "\t\t");
                    _builder.append("_idx);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          } else {
            _builder.append("\t\t");
            String _changedNotification = this.changedNotification(varInfo_2);
            _builder.append(_changedNotification, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append(varName, "\t\t");
            _builder.append("=module.");
            _builder.append(varName, "\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setInput(String name, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setInput(name, value, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setInput(int idx, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setInput(idx, value, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getIndex(name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getName(idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getOutputLong(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputLong(name, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getOutputLong(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputLong(idx, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getDeltaCycle();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void initConstants() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.initConstants();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void close() throws Exception{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.close();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setFeature(Feature feature, Object value) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setFeature(feature, value);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected String changedNotificationInterface(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, true, _of);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if ((module.getOutputLong(");
        _builder.append(varName, "");
        _builder.append("_idx)!=0) != ");
        _builder.append(varName, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (IChangeListener listener:listeners)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\", ");
        _builder.append(varName, "\t\t");
        _builder.append(", (module.getOutputLong(");
        _builder.append(varName, "\t\t");
        _builder.append("_idx)!=0), ");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(", -1);");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (module.getOutputLong(");
        _builder_1.append(varName, "");
        _builder_1.append("_idx) != ");
        _builder_1.append(varName, "");
        _builder_1.append(")");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("for (IChangeListener listener:listeners)");
        _builder_1.newLine();
        _builder_1.append("\t\t");
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), \"");
        _builder_1.append(vi.name, "\t\t");
        _builder_1.append("\", ");
        _builder_1.append(varName, "\t\t");
        _builder_1.append(", module.getOutputLong(");
        _builder_1.append(varName, "\t\t");
        _builder_1.append("_idx));");
        _builder_1.newLineIfNotEmpty();
        return _builder_1.toString();
      }
    } else {
      boolean _isPredicate_1 = this.isPredicate(vi);
      if (_isPredicate_1) {
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, true, _of_1);
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("{");
        _builder_2.newLine();
        _builder_2.append("boolean[] tempArr=new boolean[");
        int _arraySize = this.getArraySize(vi);
        _builder_2.append(_arraySize, "");
        _builder_2.append("];");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("for (int i=0;i<");
        int _arraySize_1 = this.getArraySize(vi);
        _builder_2.append(_arraySize_1, "");
        _builder_2.append(";i++)");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("tempArr[i]=module.getOutputLong(");
        _builder_2.append(varName, "\t");
        _builder_2.append("_idx)!=0;");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("if (!tempArr.equals(");
        _builder_2.append(varName, "");
        _builder_2.append("))");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("for (IChangeListener listener:listeners)");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("listener.valueChangedPredicateArray(getDeltaCycle(), \"");
        _builder_2.append(vi.name, "\t\t");
        _builder_2.append("\", ");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", tempArr, ");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(", -1);");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("}");
        _builder_2.newLine();
        return _builder_2.toString();
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("{");
        _builder_3.newLine();
        _builder_3.append("long[] tempArr=new long[");
        int _arraySize_2 = this.getArraySize(vi);
        _builder_3.append(_arraySize_2, "");
        _builder_3.append("];");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("for (int i=0;i<");
        int _arraySize_3 = this.getArraySize(vi);
        _builder_3.append(_arraySize_3, "");
        _builder_3.append(";i++)");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("tempArr[i]=module.getOutputLong(");
        _builder_3.append(varName, "\t");
        _builder_3.append("_idx, i);");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("if (!tempArr.equals(");
        _builder_3.append(varName, "");
        _builder_3.append("))");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("for (IChangeListener listener:listeners)");
        _builder_3.newLine();
        _builder_3.append("\t\t");
        _builder_3.append("listener.valueChangedLongArray(getDeltaCycle(), \"");
        _builder_3.append(vi.name, "\t\t");
        _builder_3.append("\", ");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(", tempArr);");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("}");
        _builder_3.newLine();
        return _builder_3.toString();
      }
    }
  }
  
  protected String changedNotification(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, true, _of);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if (module.");
        _builder.append(varName, "");
        _builder.append(" != ");
        _builder.append(varName, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (IChangeListener listener:listeners)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\", ");
        _builder.append(varName, "\t\t");
        _builder.append(", module.");
        _builder.append(varName, "\t\t");
        _builder.append(", ");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(", module.");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (module.");
        _builder_1.append(varName, "");
        _builder_1.append(" != ");
        _builder_1.append(varName, "");
        _builder_1.append(")");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("for (IChangeListener listener:listeners)");
        _builder_1.newLine();
        _builder_1.append("\t\t");
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), \"");
        _builder_1.append(vi.name, "\t\t");
        _builder_1.append("\", ");
        _builder_1.append(varName, "\t\t");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            BigInteger _calcMask = this.calcMask(vi.width);
            CharSequence _constant = this.constant(_calcMask);
            _builder_1.append(_constant, "\t\t");
          }
        }
        _builder_1.append(", module.");
        _builder_1.append(varName, "\t\t");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            BigInteger _calcMask_1 = this.calcMask(vi.width);
            CharSequence _constant_1 = this.constant(_calcMask_1);
            _builder_1.append(_constant_1, "\t\t");
          }
        }
        _builder_1.append(");");
        _builder_1.newLineIfNotEmpty();
        return _builder_1.toString();
      }
    } else {
      boolean _isPredicate_1 = this.isPredicate(vi);
      if (_isPredicate_1) {
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, true, _of_1);
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("if (!module.");
        _builder_2.append(varName, "");
        _builder_2.append(".equals(");
        _builder_2.append(varName, "");
        _builder_2.append("))");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("for (IChangeListener listener:listeners)");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("listener.valueChangedPredicateArray(getDeltaCycle(), \"");
        _builder_2.append(vi.name, "\t\t");
        _builder_2.append("\", ");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", module.");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", ");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(", module.");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(");");
        _builder_2.newLineIfNotEmpty();
        return _builder_2.toString();
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("if (!module.");
        _builder_3.append(varName, "");
        _builder_3.append(".equals(");
        _builder_3.append(varName, "");
        _builder_3.append("))");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("for (IChangeListener listener:listeners)");
        _builder_3.newLine();
        _builder_3.append("\t\t");
        _builder_3.append("listener.valueChangedLongArray(getDeltaCycle(), \"");
        _builder_3.append(vi.name, "\t\t");
        _builder_3.append("\", ");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(", module.");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(");");
        _builder_3.newLineIfNotEmpty();
        return _builder_3.toString();
      }
    }
  }
  
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdates.clear();");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("System.arraycopy(");
    CharSequence _idName = this.idName(varInfo, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(", 0, ");
    EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isPrev);
    CharSequence _idName_1 = this.idName(varInfo, true, _of);
    _builder.append(_idName_1, "");
    _builder.append(", 0, ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
