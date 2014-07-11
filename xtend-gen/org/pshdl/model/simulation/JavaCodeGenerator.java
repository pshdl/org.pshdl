package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.EnumSet;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.HDLSimulator;

@SuppressWarnings("all")
public class JavaCodeGenerator extends CommonCodeGenerator {
  private String packageName;
  
  private String unitName;
  
  public JavaCodeGenerator(final ExecutableModel em, final String packageName, final String unitName, final int maxCosts) {
    super(em, 64, maxCosts);
    this.packageName = packageName;
    this.unitName = unitName;
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
  
  public CharSequence hdlInterpreter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(String name, BigInteger value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setInput(getIndex(name), value.longValue(), arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(int idx, BigInteger value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setInput(idx, value.longValue(), arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
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
            CharSequence _idName_1 = this.idName(v_2, false, CommonCodeGenerator.NONE);
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
            CharSequence _idName_2 = this.idName(v_2, false, CommonCodeGenerator.NONE);
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
    _builder.append("public BigInteger getOutputBig(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return BigInteger.valueOf(getOutputLong(getIndex(name), arrayIdx));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public BigInteger getOutputBig(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return BigInteger.valueOf(getOutputLong(idx, arrayIdx));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public int getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return (int)deltaCycle;");
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
      String _name = HDLSimulator.TB_UNIT.getName();
      String _substring = _name.substring(1);
      boolean _contains = ((List<String>)Conversions.doWrapArray(this.em.annotations)).contains(_substring);
      if (_contains) {
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
    _builder.append("\t");
    {
      if ((this.maxCosts != Integer.MAX_VALUE)) {
        _builder.append("public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append("private Set<RegUpdate> regUpdates=new LinkedHashSet<RegUpdate>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("/**");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ");
        _builder.append(this.unitName, "\t");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("this(");
        {
          if (this.hasClock) {
            _builder.append("false, false");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ");
        _builder.append(this.unitName, "\t");
        _builder.append("(boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append(", boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
          for(final VariableInformation v : _excludeNull) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v.name, "\t\t");
            _builder.append("\", ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("public ");
        _builder.append(this.unitName, "\t");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        {
          Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
          for(final VariableInformation v_1 : _excludeNull_1) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v_1.name, "\t\t");
            _builder.append("\", ");
            Integer _get_1 = this.varIdx.get(v_1.name);
            _builder.append(_get_1, "\t\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append("public boolean skipEdge(long local) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("long dc = local >>> 16l;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("// Register was updated in previous delta cylce, that is ok");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (dc < ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("// Register was updated in this delta cycle but it is the same eps,");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("// that is ok as well");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if ((dc == ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t\t");
        _builder.append(") && ((local & 0xFFFF) == ");
        _builder.append(CommonCodeGenerator.EPS_CYCLE.name, "\t\t");
        _builder.append("))");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("// Don\'t update");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return true;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _copyRegs = this.copyRegs();
        _builder.append(_copyRegs, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence copyRegs() {
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
                _builder.append(", ");
                CharSequence _idName_5 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_5, "\t\t\t\t");
                _builder.append("$reg[0]); ");
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
    _builder.append("\t");
    _builder.append("regUpdates.clear();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getImports() {
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
  
  public CharSequence calculateVariableAccessIndexArr(final VariableInformation varInfo) {
    int _size = IterableExtensions.size(((Iterable<?>)Conversions.doWrapArray(varInfo.dimensions)));
    final int lastIndex = (_size - 1);
    final StringBuilder arrayAccess = new StringBuilder();
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, lastIndex, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        if (((i).intValue() != 0)) {
          arrayAccess.append(" + ");
        }
        int _get = varInfo.dimensions[(i).intValue()];
        String _string = Integer.toString(_get);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(" ");
        _builder.append("* arrayIdx[");
        _builder.append(i, " ");
        _builder.append("]");
        String _plus = (_string + _builder);
        arrayAccess.append(_plus);
      }
    }
    if ((lastIndex != 0)) {
      arrayAccess.append(" + ");
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("arrayIdx[");
    _builder.append(lastIndex, "");
    _builder.append("]");
    arrayAccess.append(_builder);
    return arrayAccess;
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
  
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean forceRegUpdate) {
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
    Integer _get = this.varIdx.get(outputInternal.info.name);
    _builder.append(_get, "");
    _builder.append(", ");
    _builder.append(offset, "");
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
    CharSequence _stageMethodName = this.stageMethodName(constant, stage);
    _builder.append(_stageMethodName, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence stageMethodName(final boolean constant, final int stage) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (constant) {
        _builder.append("const_");
      }
    }
    _builder.append("stage");
    String _format = String.format("%04d", Integer.valueOf(stage));
    _builder.append(_format, "");
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
    CharSequence _stageMethodName = this.stageMethodName(constant, stage);
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
}
