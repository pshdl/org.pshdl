package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.Frame.FastInstruction;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;

@SuppressWarnings("all")
public class JavaCompiler {
  private ExecutableModel em;
  
  private Map<String,Integer> varIdx = new Function0<Map<String,Integer>>() {
    public Map<String,Integer> apply() {
      HashMap<String,Integer> _hashMap = new HashMap<String,Integer>();
      return _hashMap;
    }
  }.apply();
  
  private Map<String,Boolean> prevMap = new Function0<Map<String,Boolean>>() {
    public Map<String,Boolean> apply() {
      HashMap<String,Boolean> _hashMap = new HashMap<String,Boolean>();
      return _hashMap;
    }
  }.apply();
  
  public JavaCompiler(final ExecutableModel em) {
    this.em = em;
    int _length = em.variables.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      VariableInformation _get = em.variables[(i).intValue()];
      this.varIdx.put(_get.name, i);
    }
    for (final Frame f : em.frames) {
      {
        int _minus = (-1);
        boolean _notEquals = (f.edgeNegDepRes != _minus);
        if (_notEquals) {
          InternalInformation _get_1 = em.internals[f.edgeNegDepRes];
          this.prevMap.put(_get_1.info.name, Boolean.valueOf(true));
        }
        int _minus_1 = (-1);
        boolean _notEquals_1 = (f.edgePosDepRes != _minus_1);
        if (_notEquals_1) {
          InternalInformation _get_2 = em.internals[f.edgePosDepRes];
          this.prevMap.put(_get_2.info.name, Boolean.valueOf(true));
        }
      }
    }
  }
  
  public static String doCompile(final ExecutableModel em, final String packageName, final String unitName) {
    JavaCompiler _javaCompiler = new JavaCompiler(em);
    CharSequence _compile = _javaCompiler.compile(packageName, unitName);
    return _compile.toString();
  }
  
  public CharSequence compile(final String packageName, final String unitName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(packageName, null));
      if (_notEquals) {
        _builder.append("package ");
        _builder.append(packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _imports = this.getImports();
    _builder.append(_imports, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class ");
    _builder.append(unitName, "");
    _builder.append(" implements IHDLInterpreter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private static class RegUpdate {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public final int internalID;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public final int offset;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public RegUpdate(int internalID, int offset) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("super();");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("this.internalID = internalID;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("this.offset = offset;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public int hashCode() {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("final int prime = 31;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("int result = 1;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("result = (prime * result) + internalID;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("result = (prime * result) + offset;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return result;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public boolean equals(Object obj) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (this == obj)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (obj == null)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (getClass() != obj.getClass())");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("RegUpdate other = (RegUpdate) obj;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (internalID != other.internalID)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (offset != other.offset)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation v : _excludeNull) {
        _builder.append("\t");
        Boolean _get = this.prevMap.get(v.name);
        CharSequence _decl = this.decl(v, _get);
        _builder.append(_decl, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("private int deltaCycle=0, epsCycle=0;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Set<RegUpdate> regUpdates=new HashSet<RegUpdate>();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Map<String, Integer> varIdx=new HashMap<String, Integer>();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private final IDebugListener listener;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private final ExecutableModel em;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private final boolean disableEdges;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    _builder.append(unitName, "	");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this(false, null, null);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    _builder.append(unitName, "	");
    _builder.append("(boolean disableEdges, IDebugListener listener, ExecutableModel em) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this.disableEdges=disableEdges;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.listener=listener;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.em=em;");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
      for(final VariableInformation v_1 : _excludeNull_1) {
        _builder.append("\t\t");
        CharSequence _init = this.init(v_1);
        _builder.append(_init, "		");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("varIdx.put(\"");
        _builder.append(v_1.name, "		");
        _builder.append("\", ");
        Integer _get_1 = this.varIdx.get(v_1.name);
        _builder.append(_get_1, "		");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      Iterable<InternalInformation> _excludeNull_2 = this.excludeNull(this.em.internals);
      for(final InternalInformation i : _excludeNull_2) {
        {
          boolean _not = (!i.isShadowReg);
          if (_not) {
            {
              Boolean _get_2 = this.prevMap.get(i.info.name);
              boolean _tripleNotEquals = (_get_2 != null);
              if (_tripleNotEquals) {
                _builder.append("\t");
                CharSequence _ter = this.getter(i, true);
                _builder.append(_ter, "	");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            CharSequence _ter_1 = this.getter(i, false);
            _builder.append(_ter_1, "	");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          boolean _or = false;
          boolean _not_1 = (!i.info.isRegister);
          if (_not_1) {
            _or = true;
          } else {
            _or = (_not_1 || i.isShadowReg);
          }
          if (_or) {
            _builder.append("\t");
            CharSequence _setter = this.setter(i);
            _builder.append(_setter, "	");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      for(final Frame f : this.em.frames) {
        _builder.append("\t");
        String _method = this.method(f);
        _builder.append(_method, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public boolean skipEdge(long local) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("long dc = local >>> 16l;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// Register was updated in previous delta cylce, that is ok");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (dc < deltaCycle)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// Register was updated in this delta cycle but it is the same eps,");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// that is ok as well");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if ((dc == deltaCycle) && ((local & 0xFFFF) == epsCycle))");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return false;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("// Don\'t update");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void run(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("do {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (listener!=null)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("listener.startCycle(deltaCycle, epsCycle, this);");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("regUpdates.clear();");
    _builder.newLine();
    {
      for(final Frame f_1 : this.em.frames) {
        _builder.append("\t\t\t");
        _builder.append("frame");
        _builder.append(f_1.uniqueID, "			");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Iterable<VariableInformation> _excludeNull_3 = this.excludeNull(this.em.variables);
      final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            Boolean _get = JavaCompiler.this.prevMap.get(it.name);
            boolean _notEquals = (!Objects.equal(_get, null));
            return Boolean.valueOf(_notEquals);
          }
        };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull_3, _function);
      for(final VariableInformation v_2 : _filter) {
        _builder.append("\t\t\t");
        String _copyPrev = this.copyPrev(v_2);
        _builder.append(_copyPrev, "			");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("updateRegs();");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (listener!=null && !regUpdates.isEmpty())");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("listener.copyingRegisterValues(this);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("} while (!regUpdates.isEmpty());");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (listener!=null)");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("listener.doneCycle(deltaCycle, this);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("deltaCycle++;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _copyRegs = this.copyRegs();
    _builder.append(_copyRegs, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _hdlInterpreter = this.hdlInterpreter();
    _builder.append(_hdlInterpreter, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
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
            _builder.append(_get, "		");
            _builder.append(": ");
            String _javaName = this.javaName(v, false);
            _builder.append(_javaName, "		");
            _builder.append("=value");
            {
              boolean _isPredicate = this.isPredicate(v);
              if (_isPredicate) {
                _builder.append("==0?false:true");
              }
            }
            _builder.append(";break;");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_1 = this.varIdx.get(v.name);
            _builder.append(_get_1, "		");
            _builder.append(": ");
            String _javaName_1 = this.javaName(v, false);
            _builder.append(_javaName_1, "		");
            _builder.append("[");
            StringBuilder _arrayAccessArrIdx = this.arrayAccessArrIdx(v);
            _builder.append(_arrayAccessArrIdx, "		");
            _builder.append("]=value;break;");
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
    _builder.append("public int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return varIdx.get(name);");
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
        _builder.append(_get_2, "		");
        _builder.append(": return \"");
        _builder.append(v_1.name, "		");
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
            _builder.append(_get_3, "		");
            _builder.append(": return ");
            String _javaName_2 = this.javaName(v_2, false);
            _builder.append(_javaName_2, "		");
            {
              boolean _isPredicate_1 = this.isPredicate(v_2);
              if (_isPredicate_1) {
                _builder.append("?1:0");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_4 = this.varIdx.get(v_2.name);
            _builder.append(_get_4, "		");
            _builder.append(": return ");
            String _javaName_3 = this.javaName(v_2, false);
            _builder.append(_javaName_3, "		");
            _builder.append("[");
            StringBuilder _arrayAccessArrIdx_1 = this.arrayAccessArrIdx(v_2);
            _builder.append(_arrayAccessArrIdx_1, "		");
            _builder.append("];");
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
    _builder.append("return deltaCycle;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public Iterable<VariableInformation> excludeNull(final VariableInformation[] vars) {
    final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
        public Boolean apply(final VariableInformation it) {
          boolean _notEquals = (!Objects.equal(it.name, "#null"));
          return Boolean.valueOf(_notEquals);
        }
      };
    Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
  }
  
  public Iterable<InternalInformation> excludeNull(final InternalInformation[] vars) {
    final Function1<InternalInformation,Boolean> _function = new Function1<InternalInformation,Boolean>() {
        public Boolean apply(final InternalInformation it) {
          boolean _notEquals = (!Objects.equal(it.info.name, "#null"));
          return Boolean.valueOf(_notEquals);
        }
      };
    Iterable<InternalInformation> _filter = IterableExtensions.<InternalInformation>filter(((Iterable<InternalInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
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
            _builder.append(_get, "			");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            {
              int _length = v.dimensions.length;
              boolean _equals = (_length == 0);
              if (_equals) {
                _builder.append("\t\t\t");
                String _javaName = this.javaName(v, false);
                _builder.append(_javaName, "			");
                _builder.append(" = ");
                String _javaName_1 = this.javaName(v, false);
                _builder.append(_javaName_1, "			");
                _builder.append("$reg; break;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t\t");
                String _javaName_2 = this.javaName(v, false);
                _builder.append(_javaName_2, "			");
                _builder.append("[reg.offset] = ");
                String _javaName_3 = this.javaName(v, false);
                _builder.append(_javaName_3, "			");
                _builder.append("$reg[reg.offset]; break;");
                _builder.newLineIfNotEmpty();
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
  
  public String copyPrev(final VariableInformation info) {
    int _length = info.dimensions.length;
    boolean _equals = (_length == 0);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      String _javaName = this.javaName(info, true);
      _builder.append(_javaName, "");
      _builder.append("=");
      String _javaName_1 = this.javaName(info, false);
      _builder.append(_javaName_1, "");
      _builder.append(";");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("System.arraycopy(");
    String _javaName_2 = this.javaName(info, false);
    _builder_1.append(_javaName_2, "");
    _builder_1.append(",0,");
    String _javaName_3 = this.javaName(info, true);
    _builder_1.append(_javaName_3, "");
    _builder_1.append(", 0, ");
    String _javaName_4 = this.javaName(info, false);
    _builder_1.append(_javaName_4, "");
    _builder_1.append(".length);");
    return _builder_1.toString();
  }
  
  public CharSequence getter(final InternalInformation info, final boolean prev) {
    CharSequence _xblockexpression = null;
    {
      StringBuilder _stringBuilder = new StringBuilder();
      final StringBuilder sb = _stringBuilder;
      long _doubleLessThan = (1l << info.actualWidth);
      final long mask = (_doubleLessThan - 1);
      for (final int arr : info.arrayIdx) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[");
        _builder.append(arr, "");
        _builder.append("]");
        sb.append(_builder);
      }
      CharSequence _xifexpression = null;
      if (info.fixedArray) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("//");
        _builder_1.append(info.fullName, "");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("public ");
        String _javaType = this.getJavaType(info.info);
        _builder_1.append(_javaType, "");
        _builder_1.append(" ");
        String _javaName = this.javaName(info, prev);
        _builder_1.append(_javaName, "");
        _builder_1.append("(){");
        _builder_1.newLineIfNotEmpty();
        {
          boolean _equals = (info.actualWidth == info.info.width);
          if (_equals) {
            _builder_1.append("\t");
            _builder_1.append("return ");
            String _javaName_1 = this.javaName(info.info, prev);
            _builder_1.append(_javaName_1, "	");
            _builder_1.append(sb, "	");
            _builder_1.append(";");
            _builder_1.newLineIfNotEmpty();
          } else {
            boolean _equals_1 = (info.actualWidth == 1);
            if (_equals_1) {
              _builder_1.append("\t");
              _builder_1.append("return (");
              String _javaName_2 = this.javaName(info.info, prev);
              _builder_1.append(_javaName_2, "	");
              _builder_1.append(sb, "	");
              _builder_1.append(" >> ");
              _builder_1.append(info.bitStart, "	");
              _builder_1.append(") & 1;");
              _builder_1.newLineIfNotEmpty();
            } else {
              _builder_1.append("\t");
              _builder_1.append("return (");
              String _javaName_3 = this.javaName(info.info, prev);
              _builder_1.append(_javaName_3, "	");
              _builder_1.append(sb, "	");
              _builder_1.append(" >> ");
              _builder_1.append(info.bitEnd, "	");
              _builder_1.append(") & ");
              CharSequence _hexString = this.toHexString(mask);
              _builder_1.append(_hexString, "	");
              _builder_1.append(";");
              _builder_1.newLineIfNotEmpty();
            }
          }
        }
        _builder_1.append("}");
        _builder_1.newLine();
        _xifexpression = _builder_1;
      } else {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("public ");
        String _javaType_1 = this.getJavaType(info.info);
        _builder_2.append(_javaType_1, "");
        _builder_2.append(" ");
        String _javaName_4 = this.javaName(info, prev);
        _builder_2.append(_javaName_4, "");
        _builder_2.append("(");
        {
          int _length = info.arrayIdx.length;
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
          boolean _hasElements = false;
          for(final int i : _doubleDotLessThan) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder_2.appendImmediate(",", "");
            }
            _builder_2.append("int a");
            _builder_2.append(i, "");
          }
        }
        _builder_2.append("){");
        _builder_2.newLineIfNotEmpty();
        {
          boolean _equals_2 = (info.actualWidth == info.info.width);
          if (_equals_2) {
            _builder_2.append("\t");
            _builder_2.append("return ");
            String _javaName_5 = this.javaName(info.info, prev);
            _builder_2.append(_javaName_5, "	");
            {
              int _length_1 = info.arrayIdx.length;
              ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _length_1, true);
              boolean _hasElements_1 = false;
              for(final int i_1 : _doubleDotLessThan_1) {
                if (!_hasElements_1) {
                  _hasElements_1 = true;
                  _builder_2.append("[", "	");
                } else {
                  _builder_2.appendImmediate("][", "	");
                }
                _builder_2.append("a");
                _builder_2.append(i_1, "	");
              }
              if (_hasElements_1) {
                _builder_2.append("]", "	");
              }
            }
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            boolean _equals_3 = (info.actualWidth == 1);
            if (_equals_3) {
              _builder_2.append("\t");
              _builder_2.append("return (");
              String _javaName_6 = this.javaName(info.info, prev);
              _builder_2.append(_javaName_6, "	");
              {
                int _length_2 = info.arrayIdx.length;
                ExclusiveRange _doubleDotLessThan_2 = new ExclusiveRange(0, _length_2, true);
                boolean _hasElements_2 = false;
                for(final int i_2 : _doubleDotLessThan_2) {
                  if (!_hasElements_2) {
                    _hasElements_2 = true;
                    _builder_2.append("[", "	");
                  } else {
                    _builder_2.appendImmediate("][", "	");
                  }
                  _builder_2.append("a");
                  _builder_2.append(i_2, "	");
                }
                if (_hasElements_2) {
                  _builder_2.append("]", "	");
                }
              }
              _builder_2.append(" >> ");
              _builder_2.append(info.bitStart, "	");
              _builder_2.append(") & 1;");
              _builder_2.newLineIfNotEmpty();
            } else {
              _builder_2.append("\t");
              _builder_2.append("return (");
              String _javaName_7 = this.javaName(info.info, prev);
              _builder_2.append(_javaName_7, "	");
              {
                int _length_3 = info.arrayIdx.length;
                ExclusiveRange _doubleDotLessThan_3 = new ExclusiveRange(0, _length_3, true);
                boolean _hasElements_3 = false;
                for(final int i_3 : _doubleDotLessThan_3) {
                  if (!_hasElements_3) {
                    _hasElements_3 = true;
                    _builder_2.append("[", "	");
                  } else {
                    _builder_2.appendImmediate("][", "	");
                  }
                  _builder_2.append("a");
                  _builder_2.append(i_3, "	");
                }
                if (_hasElements_3) {
                  _builder_2.append("]", "	");
                }
              }
              _builder_2.append(" >> ");
              _builder_2.append(info.bitEnd, "	");
              _builder_2.append(") & ");
              long _doubleLessThan_1 = (1l << info.actualWidth);
              long _minus = (_doubleLessThan_1 - 1);
              _builder_2.append(_minus, "	");
              _builder_2.append(";");
              _builder_2.newLineIfNotEmpty();
            }
          }
        }
        _builder_2.append("}");
        _builder_2.newLine();
        _xifexpression = _builder_2;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public CharSequence setter(final InternalInformation info) {
    CharSequence _xblockexpression = null;
    {
      long _doubleLessThan = (1l << info.actualWidth);
      final long mask = (_doubleLessThan - 1);
      final CharSequence maskString = this.toHexString(mask);
      long _doubleLessThan_1 = (mask << info.bitEnd);
      long _bitwiseNot = (~_doubleLessThan_1);
      final CharSequence writeMask = this.toHexString(_bitwiseNot);
      final StringBuilder varAccess = this.arrayAccess(info.info);
      final int off = this.arrayAccess(info);
      String _xifexpression = null;
      int _length = info.arrayIdx.length;
      boolean _greaterThan = (_length > 0);
      if (_greaterThan) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[");
        _builder.append(off, "");
        _builder.append("]");
        _xifexpression = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _xifexpression = _builder_1.toString();
      }
      String fixedAccess = _xifexpression;
      if (info.isShadowReg) {
        String _plus = ("$reg" + fixedAccess);
        fixedAccess = _plus;
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("public void ");
        String _javaName = this.javaName(info, false);
        _builder_2.append(_javaName, "");
        _builder_2.append("(");
        String _javaType = this.getJavaType(info.info);
        _builder_2.append(_javaType, "");
        _builder_2.append(" value){");
        _builder_2.newLineIfNotEmpty();
        {
          boolean _equals = (info.actualWidth == info.info.width);
          if (_equals) {
            _builder_2.append("\t");
            String _javaType_1 = this.getJavaType(info.info);
            _builder_2.append(_javaType_1, "	");
            _builder_2.append(" current=");
            String _javaName_1 = this.javaName(info.info, false);
            _builder_2.append(_javaName_1, "	");
            _builder_2.append(fixedAccess, "	");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            String _javaName_2 = this.javaName(info.info, false);
            _builder_2.append(_javaName_2, "	");
            _builder_2.append(fixedAccess, "	");
            _builder_2.append("=value;");
            _builder_2.newLineIfNotEmpty();
          } else {
            _builder_2.append("\t");
            String _javaType_2 = this.getJavaType(info.info);
            _builder_2.append(_javaType_2, "	");
            _builder_2.append(" current=");
            String _javaName_3 = this.javaName(info.info, false);
            _builder_2.append(_javaName_3, "	");
            _builder_2.append(fixedAccess, "	");
            _builder_2.append(" & ");
            _builder_2.append(writeMask, "	");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            _builder_2.append("value=((value & ");
            _builder_2.append(maskString, "	");
            _builder_2.append(") << ");
            _builder_2.append(info.bitEnd, "	");
            _builder_2.append(");");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            String _javaName_4 = this.javaName(info.info, false);
            _builder_2.append(_javaName_4, "	");
            _builder_2.append(fixedAccess, "	");
            _builder_2.append("=current|value;");
            _builder_2.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_2.append("\t");
            _builder_2.append("if (current!=value)");
            _builder_2.newLine();
            _builder_2.append("\t");
            _builder_2.append("\t");
            _builder_2.append("regUpdates.add(new RegUpdate(");
            Integer _get = this.varIdx.get(info.info.name);
            _builder_2.append(_get, "		");
            _builder_2.append(", ");
            _builder_2.append(off, "		");
            _builder_2.append("));");
            _builder_2.newLineIfNotEmpty();
          }
        }
        _builder_2.append("\t");
        {
          if (info.isPred) {
            String _javaName_5 = this.javaName(info.info, false);
            _builder_2.append(_javaName_5, "	");
            _builder_2.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("}");
        _builder_2.newLine();
        _xifexpression_1 = _builder_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("public void ");
        String _javaName_6 = this.javaName(info, false);
        _builder_3.append(_javaName_6, "");
        _builder_3.append("(");
        String _javaType_3 = this.getJavaType(info.info);
        _builder_3.append(_javaType_3, "");
        _builder_3.append(" value,");
        {
          int _length_1 = info.arrayIdx.length;
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length_1, true);
          boolean _hasElements = false;
          for(final int i : _doubleDotLessThan) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder_3.appendImmediate(",", "");
            }
            _builder_3.append("int a");
            _builder_3.append(i, "");
          }
        }
        _builder_3.append("){");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("int offset=(int)");
        _builder_3.append(varAccess, "	");
        _builder_3.append(";");
        _builder_3.newLineIfNotEmpty();
        {
          boolean _equals_1 = (info.actualWidth == info.info.width);
          if (_equals_1) {
            _builder_3.append("\t");
            String _javaType_4 = this.getJavaType(info.info);
            _builder_3.append(_javaType_4, "	");
            _builder_3.append(" current=");
            String _javaName_7 = this.javaName(info.info, false);
            _builder_3.append(_javaName_7, "	");
            _builder_3.append("[offset];");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            String _javaName_8 = this.javaName(info.info, false);
            _builder_3.append(_javaName_8, "	");
            _builder_3.append("[offset]=value;");
            _builder_3.newLineIfNotEmpty();
          } else {
            _builder_3.append("\t");
            String _javaType_5 = this.getJavaType(info.info);
            _builder_3.append(_javaType_5, "	");
            _builder_3.append(" current=");
            String _javaName_9 = this.javaName(info.info, false);
            _builder_3.append(_javaName_9, "	");
            _builder_3.append("[offset] & ");
            _builder_3.append(writeMask, "	");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("value=((value & ");
            _builder_3.append(maskString, "	");
            _builder_3.append(") << ");
            _builder_3.append(info.bitEnd, "	");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            String _javaName_10 = this.javaName(info.info, false);
            _builder_3.append(_javaName_10, "	");
            _builder_3.append("[offset]=current|value);");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_3.append("\t");
            _builder_3.append("if (current!=value)");
            _builder_3.newLine();
            _builder_3.append("\t");
            _builder_3.append("\t");
            _builder_3.append("regUpdates.add(new RegUpdate(");
            Integer _get_1 = this.varIdx.get(info.info.name);
            _builder_3.append(_get_1, "		");
            _builder_3.append(", offset));");
            _builder_3.newLineIfNotEmpty();
          }
        }
        _builder_3.append("\t");
        {
          if (info.isPred) {
            String _javaName_11 = this.javaName(info.info, false);
            _builder_3.append(_javaName_11, "	");
            _builder_3.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("}");
        _builder_3.newLine();
        _xifexpression_1 = _builder_3;
      }
      _xblockexpression = (_xifexpression_1);
    }
    return _xblockexpression;
  }
  
  public int arrayAccess(final InternalInformation v) {
    final ArrayList<Integer> dims = this.dimsLastOne(v.info);
    int off = 0;
    int _length = v.arrayIdx.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final int arr = v.arrayIdx[(i).intValue()];
        final Integer dim = dims.get((i).intValue());
        int _multiply = (arr * (dim).intValue());
        int _plus = (off + _multiply);
        off = _plus;
      }
    }
    return off;
  }
  
  public StringBuilder arrayAccessArrIdx(final VariableInformation v) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder varAccess = _stringBuilder;
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        boolean _notEquals = ((dim).intValue() != 1);
        if (_notEquals) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("arrayIdx[");
          _builder.append(i, "");
          _builder.append("]*");
          _builder.append(dim, "");
          varAccess.append(_builder);
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("arrayIdx[");
          _builder_1.append(i, "");
          _builder_1.append("]");
          varAccess.append(_builder_1);
        }
      }
    }
    return varAccess;
  }
  
  public ArrayList<Integer> dimsLastOne(final VariableInformation v) {
    ArrayList<Integer> _xblockexpression = null;
    {
      ArrayList<Integer> _arrayList = new ArrayList<Integer>(((Collection<? extends Integer>)Conversions.doWrapArray(v.dimensions)));
      final ArrayList<Integer> dims = _arrayList;
      int _size = dims.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        int _size_1 = dims.size();
        int _minus = (_size_1 - 1);
        dims.set(_minus, Integer.valueOf(1));
      }
      _xblockexpression = (dims);
    }
    return _xblockexpression;
  }
  
  public StringBuilder arrayAccess(final VariableInformation v) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder varAccess = _stringBuilder;
    final ArrayList<Integer> dims = this.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        boolean _notEquals = ((dim).intValue() != 1);
        if (_notEquals) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("a");
          _builder.append(i, "");
          _builder.append("*");
          _builder.append(dim, "");
          varAccess.append(_builder);
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("a");
          _builder_1.append(i, "");
          varAccess.append(_builder_1);
        }
      }
    }
    return varAccess;
  }
  
  public CharSequence toHexString(final long value) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("0x");
    String _hexString = Long.toHexString(value);
    _builder.append(_hexString, "");
    _builder.append("l");
    return _builder;
  }
  
  public String method(final Frame frame) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private final void frame");
    _builder.append(frame.uniqueID, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (listener!=null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("listener.startFrame(");
    _builder.append(frame.uniqueID, "		");
    _builder.append(", deltaCycle, epsCycle, null);");
    _builder.newLineIfNotEmpty();
    sb.append(_builder);
    int pos = 0;
    int arrPos = 0;
    Stack<Integer> _stack = new Stack<Integer>();
    final Stack<Integer> stack = _stack;
    LinkedList<Integer> _linkedList = new LinkedList<Integer>();
    final List<Integer> arr = _linkedList;
    for (final FastInstruction i : frame.instructions) {
      {
        int a = 0;
        int b = 0;
        boolean _greaterThan = (i.inst.pop > 0);
        if (_greaterThan) {
          Integer _pop = stack.pop();
          a = (_pop).intValue();
        }
        boolean _greaterThan_1 = (i.inst.pop > 1);
        if (_greaterThan_1) {
          Integer _pop_1 = stack.pop();
          b = (_pop_1).intValue();
        }
        boolean _greaterThan_2 = (i.inst.push > 0);
        if (_greaterThan_2) {
          stack.push(Integer.valueOf(pos));
        }
        boolean _tripleEquals = (i.inst == Instruction.pushAddIndex);
        if (_tripleEquals) {
          arr.add(Integer.valueOf(arrPos));
          int _plus = (arrPos + 1);
          arrPos = _plus;
        }
        this.toExpression(i, frame, sb, pos, a, b, arr, arrPos);
        boolean _tripleNotEquals = (i.inst != Instruction.pushAddIndex);
        if (_tripleNotEquals) {
          int _plus_1 = (pos + 1);
          pos = _plus_1;
        }
      }
    }
    Integer _pop = stack.pop();
    final String last = ("t" + _pop);
    InternalInformation _get = this.em.internals[frame.outputId];
    boolean _notEquals = (!Objects.equal(_get.info.name, "#null"));
    if (_notEquals) {
      String _setInternal = this.setInternal(frame.outputId, last, false, arr);
      sb.append(_setInternal);
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("//Write to #null");
      _builder_1.newLine();
      sb.append(_builder_1);
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("if (listener!=null)");
    _builder_2.newLine();
    _builder_2.append("\t");
    _builder_2.append("listener.writingResult(");
    _builder_2.append(frame.uniqueID, "	");
    _builder_2.append(", em.internals[");
    _builder_2.append(frame.outputId, "	");
    _builder_2.append("], BigInteger.valueOf(");
    _builder_2.append(last, "	");
    {
      InternalInformation _get_1 = this.em.internals[frame.outputId];
      if (_get_1.isPred) {
        _builder_2.append("?1:0");
      }
    }
    _builder_2.append("), null);");
    _builder_2.newLineIfNotEmpty();
    _builder_2.append("\t");
    _builder_2.append("}");
    _builder_2.newLine();
    sb.append(_builder_2);
    return sb.toString();
  }
  
  public StringBuilder toExpression(final FastInstruction inst, final Frame f, final StringBuilder sb, final int pos, final int a, final int b, final List<Integer> arr, final int arrPos) {
    StringBuilder _xblockexpression = null;
    {
      final Instruction _switchValue = inst.inst;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.pushAddIndex)) {
          _matched=true;
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("int a");
          Integer _last = IterableExtensions.<Integer>last(arr);
          _builder.append(_last, "");
          _builder.append("=(int)t");
          _builder.append(a, "");
          _builder.append(";");
          sb.append(_builder);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.noop)) {
          _matched=true;
          sb.append("//Do nothing");
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.and)) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("long t");
          _builder_1.append(pos, "");
          _builder_1.append("=t");
          _builder_1.append(b, "");
          _builder_1.append(" & t");
          _builder_1.append(a, "");
          _builder_1.append(";");
          sb.append(_builder_1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.arith_neg)) {
          _matched=true;
          StringConcatenation _builder_2 = new StringConcatenation();
          _builder_2.append("long t");
          _builder_2.append(pos, "");
          _builder_2.append("=-t");
          _builder_2.append(a, "");
          _builder_2.append(";");
          sb.append(_builder_2);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bit_neg)) {
          _matched=true;
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("long t");
          _builder_3.append(pos, "");
          _builder_3.append("=~t");
          _builder_3.append(a, "");
          _builder_3.append(";");
          sb.append(_builder_3);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bitAccessSingle)) {
          _matched=true;
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("long t");
          _builder_4.append(pos, "");
          _builder_4.append("=(t");
          _builder_4.append(a, "");
          _builder_4.append(" >> ");
          _builder_4.append(inst.arg1, "");
          _builder_4.append(") & 1;");
          sb.append(_builder_4);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bitAccessSingleRange)) {
          _matched=true;
          final int highBit = inst.arg1;
          final int lowBit = inst.arg2;
          int _minus = (highBit - lowBit);
          int _plus = (_minus + 1);
          long _doubleLessThan = (1l << _plus);
          final long mask = (_doubleLessThan - 1);
          StringConcatenation _builder_5 = new StringConcatenation();
          _builder_5.append("long t");
          _builder_5.append(pos, "");
          _builder_5.append("=(t");
          _builder_5.append(a, "");
          _builder_5.append(" >> ");
          _builder_5.append(lowBit, "");
          _builder_5.append(") & ");
          CharSequence _hexString = this.toHexString(mask);
          _builder_5.append(_hexString, "");
          _builder_5.append(";");
          sb.append(_builder_5);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_int)) {
          _matched=true;
          boolean _notEquals = (inst.arg1 != 64);
          if (_notEquals) {
            StringConcatenation _builder_6 = new StringConcatenation();
            _builder_6.append("long c");
            _builder_6.append(pos, "");
            _builder_6.append("=t");
            _builder_6.append(a, "");
            _builder_6.append(" << ");
            int _minus_1 = (64 - inst.arg1);
            _builder_6.append(_minus_1, "");
            _builder_6.append(";");
            _builder_6.newLineIfNotEmpty();
            _builder_6.append("long t");
            _builder_6.append(pos, "");
            _builder_6.append("=c");
            _builder_6.append(pos, "");
            _builder_6.append(" >> ");
            int _minus_2 = (64 - inst.arg1);
            _builder_6.append(_minus_2, "");
            _builder_6.append(";");
            _builder_6.newLineIfNotEmpty();
            sb.append(_builder_6);
          } else {
            StringConcatenation _builder_7 = new StringConcatenation();
            _builder_7.append("long t");
            _builder_7.append(pos, "");
            _builder_7.append("=t");
            _builder_7.append(a, "");
            _builder_7.append(";");
            sb.append(_builder_7);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_uint)) {
          _matched=true;
          boolean _notEquals_1 = (inst.arg1 != 64);
          if (_notEquals_1) {
            long _doubleLessThan_1 = (1l << inst.arg1);
            final long mask_1 = (_doubleLessThan_1 - 1);
            StringConcatenation _builder_8 = new StringConcatenation();
            _builder_8.append("long t");
            _builder_8.append(pos, "");
            _builder_8.append("=t");
            _builder_8.append(a, "");
            _builder_8.append(" & ");
            CharSequence _hexString_1 = this.toHexString(mask_1);
            _builder_8.append(_hexString_1, "");
            _builder_8.append(";");
            sb.append(_builder_8);
          } else {
            StringConcatenation _builder_9 = new StringConcatenation();
            _builder_9.append("long t");
            _builder_9.append(pos, "");
            _builder_9.append("=t");
            _builder_9.append(a, "");
            _builder_9.append(";");
            sb.append(_builder_9);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiNeg)) {
          _matched=true;
          StringConcatenation _builder_10 = new StringConcatenation();
          _builder_10.append("boolean t");
          _builder_10.append(pos, "");
          _builder_10.append("=!t");
          _builder_10.append(a, "");
          _builder_10.append(";");
          sb.append(_builder_10);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiAnd)) {
          _matched=true;
          StringConcatenation _builder_11 = new StringConcatenation();
          _builder_11.append("boolean t");
          _builder_11.append(pos, "");
          _builder_11.append("=t");
          _builder_11.append(a, "");
          _builder_11.append(" && t");
          _builder_11.append(b, "");
          _builder_11.append(";");
          sb.append(_builder_11);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiOr)) {
          _matched=true;
          StringConcatenation _builder_12 = new StringConcatenation();
          _builder_12.append("boolean t");
          _builder_12.append(pos, "");
          _builder_12.append("=t");
          _builder_12.append(a, "");
          _builder_12.append(" || t");
          _builder_12.append(b, "");
          _builder_12.append(";");
          sb.append(_builder_12);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const0)) {
          _matched=true;
          StringConcatenation _builder_13 = new StringConcatenation();
          _builder_13.append("long t");
          _builder_13.append(pos, "");
          _builder_13.append("=0;");
          sb.append(_builder_13);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const1)) {
          _matched=true;
          StringConcatenation _builder_14 = new StringConcatenation();
          _builder_14.append("long t");
          _builder_14.append(pos, "");
          _builder_14.append("=1;");
          sb.append(_builder_14);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const2)) {
          _matched=true;
          StringConcatenation _builder_15 = new StringConcatenation();
          _builder_15.append("long t");
          _builder_15.append(pos, "");
          _builder_15.append("=2;");
          sb.append(_builder_15);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.concat)) {
          _matched=true;
          StringConcatenation _builder_16 = new StringConcatenation();
          _builder_16.append("long t");
          _builder_16.append(pos, "");
          _builder_16.append("=(t");
          _builder_16.append(b, "");
          _builder_16.append(" << ");
          _builder_16.append(inst.arg2, "");
          _builder_16.append(") | t");
          _builder_16.append(a, "");
          _builder_16.append(";");
          sb.append(_builder_16);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadConstant)) {
          _matched=true;
          StringConcatenation _builder_17 = new StringConcatenation();
          _builder_17.append("long t");
          _builder_17.append(pos, "");
          _builder_17.append("=");
          CharSequence _constant = this.constant(inst.arg1, f);
          _builder_17.append(_constant, "");
          _builder_17.append(";");
          sb.append(_builder_17);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadInternal)) {
          _matched=true;
          final InternalInformation internal = this.em.internals[inst.arg1];
          StringConcatenation _builder_18 = new StringConcatenation();
          String _javaType = this.getJavaType(internal.info);
          _builder_18.append(_javaType, "");
          _builder_18.append(" t");
          _builder_18.append(pos, "");
          _builder_18.append("=");
          String _internal = this.internal(inst.arg1, false, arr);
          _builder_18.append(_internal, "");
          _builder_18.append(";");
          _builder_18.newLineIfNotEmpty();
          _builder_18.append("if (listener!=null)");
          _builder_18.newLine();
          _builder_18.append("\t");
          _builder_18.append("listener.loadingInternal(");
          _builder_18.append(f.uniqueID, "	");
          _builder_18.append(", em.internals[");
          _builder_18.append(inst.arg1, "	");
          _builder_18.append("], BigInteger.valueOf(t");
          _builder_18.append(pos, "	");
          _builder_18.append("), null);");
          _builder_18.newLineIfNotEmpty();
          sb.append(_builder_18);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.and)) {
          _matched=true;
          StringConcatenation _builder_19 = new StringConcatenation();
          _builder_19.append("long t");
          _builder_19.append(pos, "");
          _builder_19.append("=t");
          _builder_19.append(b, "");
          _builder_19.append(" & t");
          _builder_19.append(a, "");
          _builder_19.append(";");
          sb.append(_builder_19);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.xor)) {
          _matched=true;
          StringConcatenation _builder_20 = new StringConcatenation();
          _builder_20.append("long t");
          _builder_20.append(pos, "");
          _builder_20.append("=t");
          _builder_20.append(b, "");
          _builder_20.append(" ^ t");
          _builder_20.append(a, "");
          _builder_20.append(";");
          sb.append(_builder_20);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.plus)) {
          _matched=true;
          StringConcatenation _builder_21 = new StringConcatenation();
          _builder_21.append("long t");
          _builder_21.append(pos, "");
          _builder_21.append("=t");
          _builder_21.append(b, "");
          _builder_21.append(" + t");
          _builder_21.append(a, "");
          _builder_21.append(";");
          sb.append(_builder_21);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.minus)) {
          _matched=true;
          StringConcatenation _builder_22 = new StringConcatenation();
          _builder_22.append("long t");
          _builder_22.append(pos, "");
          _builder_22.append("=t");
          _builder_22.append(b, "");
          _builder_22.append(" - t");
          _builder_22.append(a, "");
          _builder_22.append(";");
          sb.append(_builder_22);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.mul)) {
          _matched=true;
          StringConcatenation _builder_23 = new StringConcatenation();
          _builder_23.append("long t");
          _builder_23.append(pos, "");
          _builder_23.append("=t");
          _builder_23.append(b, "");
          _builder_23.append(" * t");
          _builder_23.append(a, "");
          _builder_23.append(";");
          sb.append(_builder_23);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.div)) {
          _matched=true;
          StringConcatenation _builder_24 = new StringConcatenation();
          _builder_24.append("long t");
          _builder_24.append(pos, "");
          _builder_24.append("=t");
          _builder_24.append(b, "");
          _builder_24.append(" / t");
          _builder_24.append(a, "");
          _builder_24.append(";");
          sb.append(_builder_24);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sll)) {
          _matched=true;
          StringConcatenation _builder_25 = new StringConcatenation();
          _builder_25.append("long t");
          _builder_25.append(pos, "");
          _builder_25.append("=t");
          _builder_25.append(b, "");
          _builder_25.append(" << t");
          _builder_25.append(a, "");
          _builder_25.append(";");
          sb.append(_builder_25);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.srl)) {
          _matched=true;
          StringConcatenation _builder_26 = new StringConcatenation();
          _builder_26.append("long t");
          _builder_26.append(pos, "");
          _builder_26.append("=t");
          _builder_26.append(b, "");
          _builder_26.append(" >>> t");
          _builder_26.append(a, "");
          _builder_26.append(";");
          sb.append(_builder_26);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sra)) {
          _matched=true;
          StringConcatenation _builder_27 = new StringConcatenation();
          _builder_27.append("long t");
          _builder_27.append(pos, "");
          _builder_27.append("=t");
          _builder_27.append(b, "");
          _builder_27.append(" >> t");
          _builder_27.append(a, "");
          _builder_27.append(";");
          sb.append(_builder_27);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.eq)) {
          _matched=true;
          StringConcatenation _builder_28 = new StringConcatenation();
          _builder_28.append("boolean t");
          _builder_28.append(pos, "");
          _builder_28.append("=t");
          _builder_28.append(b, "");
          _builder_28.append(" == t");
          _builder_28.append(a, "");
          _builder_28.append(";");
          sb.append(_builder_28);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.not_eq)) {
          _matched=true;
          StringConcatenation _builder_29 = new StringConcatenation();
          _builder_29.append("boolean t");
          _builder_29.append(pos, "");
          _builder_29.append("=t");
          _builder_29.append(b, "");
          _builder_29.append(" != t");
          _builder_29.append(a, "");
          _builder_29.append(";");
          sb.append(_builder_29);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less)) {
          _matched=true;
          StringConcatenation _builder_30 = new StringConcatenation();
          _builder_30.append("boolean t");
          _builder_30.append(pos, "");
          _builder_30.append("=t");
          _builder_30.append(b, "");
          _builder_30.append(" < t");
          _builder_30.append(a, "");
          _builder_30.append(";");
          sb.append(_builder_30);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less_eq)) {
          _matched=true;
          StringConcatenation _builder_31 = new StringConcatenation();
          _builder_31.append("boolean t");
          _builder_31.append(pos, "");
          _builder_31.append("=t");
          _builder_31.append(b, "");
          _builder_31.append(" <= t");
          _builder_31.append(a, "");
          _builder_31.append(";");
          sb.append(_builder_31);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater)) {
          _matched=true;
          StringConcatenation _builder_32 = new StringConcatenation();
          _builder_32.append("boolean t");
          _builder_32.append(pos, "");
          _builder_32.append("=t");
          _builder_32.append(b, "");
          _builder_32.append(" > t");
          _builder_32.append(a, "");
          _builder_32.append(";");
          sb.append(_builder_32);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater_eq)) {
          _matched=true;
          StringConcatenation _builder_33 = new StringConcatenation();
          _builder_33.append("boolean t");
          _builder_33.append(pos, "");
          _builder_33.append("=t");
          _builder_33.append(b, "");
          _builder_33.append(" >= t");
          _builder_33.append(a, "");
          _builder_33.append(";");
          sb.append(_builder_33);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.negPredicate)) {
          _matched=true;
          StringConcatenation _builder_34 = new StringConcatenation();
          _builder_34.append("if (");
          String _internal_1 = this.internal(inst.arg1, false, arr);
          _builder_34.append(_internal_1, "");
          _builder_34.append(") {");
          _builder_34.newLineIfNotEmpty();
          _builder_34.append("\t");
          _builder_34.append("if (listener!=null)");
          _builder_34.newLine();
          _builder_34.append("\t\t");
          _builder_34.append("listener.skippingPredicateNotMet(");
          _builder_34.append(f.uniqueID, "		");
          _builder_34.append(", em.internals[");
          _builder_34.append(inst.arg1, "		");
          _builder_34.append("], false, ");
          String _internal_2 = this.internal(inst.arg1, false, arr);
          _builder_34.append(_internal_2, "		");
          _builder_34.append("?BigInteger.ONE:BigInteger.ZERO,null); ");
          _builder_34.newLineIfNotEmpty();
          _builder_34.append("\t");
          _builder_34.append("return;");
          _builder_34.newLine();
          _builder_34.append("}");
          _builder_34.newLine();
          _builder_34.append("long p");
          _builder_34.append(pos, "");
          _builder_34.append("=");
          InternalInformation _get = this.em.internals[inst.arg1];
          String _javaName = this.javaName(_get.info, false);
          _builder_34.append(_javaName, "");
          _builder_34.append("_update;");
          _builder_34.newLineIfNotEmpty();
          _builder_34.append("if ((p");
          _builder_34.append(pos, "");
          _builder_34.append(">>>16 != deltaCycle) || ((p");
          _builder_34.append(pos, "");
          _builder_34.append("&0xFFFF) != epsCycle)){");
          _builder_34.newLineIfNotEmpty();
          _builder_34.append("\t");
          _builder_34.append("if (listener!=null)");
          _builder_34.newLine();
          _builder_34.append("\t \t");
          _builder_34.append("listener.skippingPredicateNotFresh(");
          _builder_34.append(f.uniqueID, "	 	");
          _builder_34.append(", em.internals[");
          _builder_34.append(inst.arg1, "	 	");
          _builder_34.append("], false, null);");
          _builder_34.newLineIfNotEmpty();
          _builder_34.append("\t");
          _builder_34.append("return;");
          _builder_34.newLine();
          _builder_34.append("}");
          _builder_34.newLine();
          sb.append(_builder_34);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.posPredicate)) {
          _matched=true;
          StringConcatenation _builder_35 = new StringConcatenation();
          _builder_35.append("if (!");
          String _internal_3 = this.internal(inst.arg1, false, arr);
          _builder_35.append(_internal_3, "");
          _builder_35.append(") {");
          _builder_35.newLineIfNotEmpty();
          _builder_35.append("\t");
          _builder_35.append("if (listener!=null)");
          _builder_35.newLine();
          _builder_35.append("\t\t");
          _builder_35.append("listener.skippingPredicateNotMet(");
          _builder_35.append(f.uniqueID, "		");
          _builder_35.append(", em.internals[");
          _builder_35.append(inst.arg1, "		");
          _builder_35.append("], true, ");
          String _internal_4 = this.internal(inst.arg1, false, arr);
          _builder_35.append(_internal_4, "		");
          _builder_35.append("?BigInteger.ONE:BigInteger.ZERO,null); ");
          _builder_35.newLineIfNotEmpty();
          _builder_35.append("\t");
          _builder_35.append("return;");
          _builder_35.newLine();
          _builder_35.append("}");
          _builder_35.newLine();
          _builder_35.append("long p");
          _builder_35.append(pos, "");
          _builder_35.append("=");
          InternalInformation _get_1 = this.em.internals[inst.arg1];
          String _javaName_1 = this.javaName(_get_1.info, false);
          _builder_35.append(_javaName_1, "");
          _builder_35.append("_update;");
          _builder_35.newLineIfNotEmpty();
          _builder_35.append("if ((p");
          _builder_35.append(pos, "");
          _builder_35.append(">>>16 != deltaCycle) || ((p");
          _builder_35.append(pos, "");
          _builder_35.append("&0xFFFF) != epsCycle)){");
          _builder_35.newLineIfNotEmpty();
          _builder_35.append("\t");
          _builder_35.append("if (listener!=null)");
          _builder_35.newLine();
          _builder_35.append("\t \t");
          _builder_35.append("listener.skippingPredicateNotFresh(");
          _builder_35.append(f.uniqueID, "	 	");
          _builder_35.append(", em.internals[");
          _builder_35.append(inst.arg1, "	 	");
          _builder_35.append("], true, null);");
          _builder_35.newLineIfNotEmpty();
          _builder_35.append("\t");
          _builder_35.append("return;");
          _builder_35.newLine();
          _builder_35.append("}");
          _builder_35.newLine();
          sb.append(_builder_35);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isRisingEdge)) {
          _matched=true;
          StringConcatenation _builder_36 = new StringConcatenation();
          _builder_36.append("if (!disableEdges){");
          _builder_36.newLine();
          _builder_36.append("\t");
          _builder_36.append("if ((");
          String _internal_5 = this.internal(inst.arg1, true, arr);
          _builder_36.append(_internal_5, "	");
          _builder_36.append("!=0) || (");
          String _internal_6 = this.internal(inst.arg1, false, arr);
          _builder_36.append(_internal_6, "	");
          _builder_36.append("!=1)) {");
          _builder_36.newLineIfNotEmpty();
          _builder_36.append("\t\t");
          _builder_36.append("if (listener!=null)");
          _builder_36.newLine();
          _builder_36.append("\t\t \t");
          _builder_36.append("listener.skippingNotAnEdge(");
          _builder_36.append(f.uniqueID, "		 	");
          _builder_36.append(", em.internals[");
          _builder_36.append(inst.arg1, "		 	");
          _builder_36.append("], true, null);");
          _builder_36.newLineIfNotEmpty();
          _builder_36.append("\t\t");
          _builder_36.append("return;");
          _builder_36.newLine();
          _builder_36.append("\t");
          _builder_36.append("}");
          _builder_36.newLine();
          _builder_36.append("\t");
          _builder_36.append("long p");
          _builder_36.append(pos, "	");
          _builder_36.append("=");
          InternalInformation _get_2 = this.em.internals[inst.arg1];
          String _javaName_2 = this.javaName(_get_2.info, false);
          _builder_36.append(_javaName_2, "	");
          _builder_36.append("_update;");
          _builder_36.newLineIfNotEmpty();
          _builder_36.append("\t");
          _builder_36.append("if (skipEdge(p");
          _builder_36.append(pos, "	");
          _builder_36.append(")){");
          _builder_36.newLineIfNotEmpty();
          _builder_36.append("\t\t");
          _builder_36.append("if (listener!=null)");
          _builder_36.newLine();
          _builder_36.append("\t\t \t");
          _builder_36.append("listener.skippingHandledEdge(");
          _builder_36.append(f.uniqueID, "		 	");
          _builder_36.append(", em.internals[");
          _builder_36.append(inst.arg1, "		 	");
          _builder_36.append("], true, null);");
          _builder_36.newLineIfNotEmpty();
          _builder_36.append("\t\t");
          _builder_36.append("return;");
          _builder_36.newLine();
          _builder_36.append("\t");
          _builder_36.append("}");
          _builder_36.newLine();
          _builder_36.append("}");
          _builder_36.newLine();
          InternalInformation _get_3 = this.em.internals[inst.arg1];
          String _javaName_3 = this.javaName(_get_3.info, false);
          _builder_36.append(_javaName_3, "");
          _builder_36.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          _builder_36.newLineIfNotEmpty();
          sb.append(_builder_36);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isFallingEdge)) {
          _matched=true;
          StringConcatenation _builder_37 = new StringConcatenation();
          _builder_37.append("if (!disableEdges){");
          _builder_37.newLine();
          _builder_37.append("\t");
          _builder_37.append("if ((");
          String _internal_7 = this.internal(inst.arg1, true, arr);
          _builder_37.append(_internal_7, "	");
          _builder_37.append("!=1) || (");
          String _internal_8 = this.internal(inst.arg1, false, arr);
          _builder_37.append(_internal_8, "	");
          _builder_37.append("!=0)) {");
          _builder_37.newLineIfNotEmpty();
          _builder_37.append("\t\t");
          _builder_37.append("if (listener!=null)");
          _builder_37.newLine();
          _builder_37.append("\t\t \t");
          _builder_37.append("listener.skippingNotAnEdge(");
          _builder_37.append(f.uniqueID, "		 	");
          _builder_37.append(", em.internals[");
          _builder_37.append(inst.arg1, "		 	");
          _builder_37.append("], false, null);");
          _builder_37.newLineIfNotEmpty();
          _builder_37.append("\t\t");
          _builder_37.append("return;");
          _builder_37.newLine();
          _builder_37.append("\t");
          _builder_37.append("}");
          _builder_37.newLine();
          _builder_37.append("\t");
          _builder_37.append("long p");
          _builder_37.append(pos, "	");
          _builder_37.append("=");
          InternalInformation _get_4 = this.em.internals[inst.arg1];
          String _javaName_4 = this.javaName(_get_4.info, false);
          _builder_37.append(_javaName_4, "	");
          _builder_37.append("_update;");
          _builder_37.newLineIfNotEmpty();
          _builder_37.append("\t");
          _builder_37.append("if (skipEdge(p");
          _builder_37.append(pos, "	");
          _builder_37.append(")){");
          _builder_37.newLineIfNotEmpty();
          _builder_37.append("\t\t");
          _builder_37.append("if (listener!=null)");
          _builder_37.newLine();
          _builder_37.append("\t\t \t");
          _builder_37.append("listener.skippingHandledEdge(");
          _builder_37.append(f.uniqueID, "		 	");
          _builder_37.append(", em.internals[");
          _builder_37.append(inst.arg1, "		 	");
          _builder_37.append("], false, null);");
          _builder_37.newLineIfNotEmpty();
          _builder_37.append("\t\t");
          _builder_37.append("return;");
          _builder_37.newLine();
          _builder_37.append("\t");
          _builder_37.append("}");
          _builder_37.newLine();
          _builder_37.append("}");
          _builder_37.newLine();
          InternalInformation _get_5 = this.em.internals[inst.arg1];
          String _javaName_5 = this.javaName(_get_5.info, false);
          _builder_37.append(_javaName_5, "");
          _builder_37.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          _builder_37.newLineIfNotEmpty();
          sb.append(_builder_37);
        }
      }
      StringConcatenation _builder_38 = new StringConcatenation();
      _builder_38.append("//");
      _builder_38.append(inst, "");
      _builder_38.newLineIfNotEmpty();
      StringBuilder _append = sb.append(_builder_38);
      _xblockexpression = (_append);
    }
    return _xblockexpression;
  }
  
  public CharSequence constant(final int id, final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    BigInteger _get = f.constants[id];
    long _longValue = _get.longValue();
    CharSequence _hexString = this.toHexString(_longValue);
    _builder.append(_hexString, "");
    return _builder;
  }
  
  public String internal(final int id, final boolean prev, final List<Integer> arr) {
    StringConcatenation _builder = new StringConcatenation();
    InternalInformation _get = this.em.internals[id];
    String _javaName = this.javaName(_get, prev);
    _builder.append(_javaName, "");
    _builder.append("(");
    {
      boolean _hasElements = false;
      for(final Integer ai : arr) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append("a");
        _builder.append(ai, "");
      }
    }
    _builder.append(")");
    final String res = _builder.toString();
    arr.clear();
    return res;
  }
  
  public String setInternal(final int id, final String value, final boolean prev, final List<Integer> arr) {
    StringConcatenation _builder = new StringConcatenation();
    InternalInformation _get = this.em.internals[id];
    String _javaName = this.javaName(_get, prev);
    _builder.append(_javaName, "");
    _builder.append("(");
    _builder.append(value, "");
    {
      boolean _hasElements = false;
      for(final Integer ai : arr) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append(",", "");
        } else {
          _builder.appendImmediate(",", "");
        }
        _builder.append("a");
        _builder.append(ai, "");
      }
    }
    _builder.append(");");
    final String res = _builder.toString();
    arr.clear();
    return res;
  }
  
  public CharSequence init(final VariableInformation info) {
    CharSequence _xblockexpression = null;
    {
      int _length = info.dimensions.length;
      boolean _equals = (_length == 0);
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder.toString();
      }
      int size = 1;
      for (final int d : info.dimensions) {
        int _multiply = (size * d);
        size = _multiply;
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      String _javaName = this.javaName(info, false);
      _builder_1.append(_javaName, "");
      _builder_1.append("=new ");
      String _javaType = this.getJavaType(info);
      _builder_1.append(_javaType, "");
      _builder_1.append("[");
      _builder_1.append(size, "");
      _builder_1.append("];");
      _builder_1.newLineIfNotEmpty();
      {
        if (info.isRegister) {
          String _javaName_1 = this.javaName(info, false);
          _builder_1.append(_javaName_1, "");
          _builder_1.append("$reg=new ");
          String _javaType_1 = this.getJavaType(info);
          _builder_1.append(_javaType_1, "");
          _builder_1.append("[");
          _builder_1.append(size, "");
          _builder_1.append("];");
        }
      }
      _builder_1.newLineIfNotEmpty();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
  
  public String getJavaType(final VariableInformation information) {
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      return "boolean";
    }
    return "long";
  }
  
  public String javaName(final VariableInformation information, final boolean prev) {
    return this.javaName(information.name, prev);
  }
  
  public String javaName(final InternalInformation ii, final boolean prev) {
    if (ii.fixedArray) {
      return this.javaName(ii.fullName, prev);
    }
    return this.javaName(ii.info, prev);
  }
  
  public String javaName(final String name, final boolean prev) {
    String _replaceAll = name.replaceAll("\\.", "_");
    String _replaceAll_1 = _replaceAll.replaceAll("\\{", "Bit");
    String _replaceAll_2 = _replaceAll_1.replaceAll("\\}", "");
    String _replaceAll_3 = _replaceAll_2.replaceAll(":", "to");
    String _replaceAll_4 = _replaceAll_3.replaceAll("\\[", "arr");
    final String res = _replaceAll_4.replaceAll("\\]", "");
    if (prev) {
      return (res + "_prev");
    }
    return res;
  }
  
  public CharSequence decl(final VariableInformation info, final Boolean includePrev) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _or = false;
      boolean _isPredicate = this.isPredicate(info);
      if (_isPredicate) {
        _or = true;
      } else {
        boolean _and = false;
        Boolean _get = this.prevMap.get(info.name);
        boolean _notEquals = (!Objects.equal(_get, null));
        if (!_notEquals) {
          _and = false;
        } else {
          Boolean _get_1 = this.prevMap.get(info.name);
          _and = (_notEquals && (_get_1).booleanValue());
        }
        _or = (_isPredicate || _and);
      }
      if (_or) {
        _builder.append("private long ");
        String _javaName = this.javaName(info, false);
        _builder.append(_javaName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("public ");
    String _javaType = this.getJavaType(info);
    _builder.append(_javaType, "");
    {
      for(final int d : info.dimensions) {
        _builder.append("[]");
      }
    }
    _builder.append(" ");
    String _javaName_1 = this.javaName(info, false);
    _builder.append(_javaName_1, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    {
      boolean _and_1 = false;
      boolean _notEquals_1 = (!Objects.equal(includePrev, null));
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        _and_1 = (_notEquals_1 && (includePrev).booleanValue());
      }
      if (_and_1) {
        _builder.append("private ");
        String _javaType_1 = this.getJavaType(info);
        _builder.append(_javaType_1, "");
        {
          for(final int d_1 : info.dimensions) {
            _builder.append("[]");
          }
        }
        _builder.append(" ");
        String _javaName_2 = this.javaName(info, true);
        _builder.append(_javaName_2, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        _builder.append("private ");
        String _javaType_2 = this.getJavaType(info);
        _builder.append(_javaType_2, "");
        {
          for(final int d_2 : info.dimensions) {
            _builder.append("[]");
          }
        }
        _builder.append(" ");
        String _javaName_3 = this.javaName(info, false);
        _builder.append(_javaName_3, "");
        _builder.append("$reg;");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public boolean isPredicate(final VariableInformation info) {
    boolean _startsWith = info.name.startsWith(InternalInformation.PRED_PREFIX);
    return _startsWith;
  }
  
  public CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.*;");
    _builder.newLine();
    _builder.append("import java.math.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.frames.*;");
    _builder.newLine();
    return _builder;
  }
}
