package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.pshdl.interpreter.VariableInformation.Direction;
import org.pshdl.interpreter.utils.Instruction;

@SuppressWarnings("all")
public class DartCompiler {
  private ExecutableModel em;
  
  private Map<String,Integer> varIdx = new Function0<Map<String,Integer>>() {
    public Map<String,Integer> apply() {
      HashMap<String,Integer> _hashMap = new HashMap<String,Integer>();
      return _hashMap;
    }
  }.apply();
  
  private Map<String,Integer> intIdx = new Function0<Map<String,Integer>>() {
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
  
  private boolean hasClock;
  
  public DartCompiler(final ExecutableModel em) {
    this.em = em;
    int _length = em.variables.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      VariableInformation _get = em.variables[(i).intValue()];
      this.varIdx.put(_get.name, i);
    }
    int _length_1 = em.internals.length;
    ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _length_1, true);
    for (final Integer i_1 : _doubleDotLessThan_1) {
      InternalInformation _get_1 = em.internals[(i_1).intValue()];
      this.intIdx.put(_get_1.fullName, i_1);
    }
    for (final Frame f : em.frames) {
      {
        int _minus = (-1);
        boolean _notEquals = (f.edgeNegDepRes != _minus);
        if (_notEquals) {
          InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
          this.prevMap.put(_asInternal.info.name, Boolean.valueOf(true));
        }
        int _minus_1 = (-1);
        boolean _notEquals_1 = (f.edgePosDepRes != _minus_1);
        if (_notEquals_1) {
          InternalInformation _asInternal_1 = this.asInternal(f.edgePosDepRes);
          this.prevMap.put(_asInternal_1.info.name, Boolean.valueOf(true));
        }
      }
    }
    boolean _isEmpty = this.prevMap.isEmpty();
    boolean _not = (!_isEmpty);
    this.hasClock = _not;
  }
  
  public static String doCompile(final ExecutableModel em, final String unitName) {
    DartCompiler _dartCompiler = new DartCompiler(em);
    CharSequence _compile = _dartCompiler.compile(unitName);
    return _compile.toString();
  }
  
  public InternalInformation asInternal(final int id) {
    return this.em.internals[id];
  }
  
  public CharSequence compile(final String unitName) {
    CharSequence _xblockexpression = null;
    {
      HashSet<Integer> _hashSet = new HashSet<Integer>();
      final Set<Integer> handled = _hashSet;
      int _minus = (-1);
      handled.add(Integer.valueOf(_minus));
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _imports = this.getImports();
      _builder.append(_imports, "");
      _builder.newLineIfNotEmpty();
      _builder.append("void main(){");
      _builder.newLine();
      _builder.append("  ");
      _builder.append(unitName, "  ");
      _builder.append(" object=new ");
      _builder.append(unitName, "  ");
      _builder.append("();");
      _builder.newLineIfNotEmpty();
      _builder.append("  ");
      _builder.append("handleReceive(object);");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      {
        if (this.hasClock) {
          _builder.append("class RegUpdate {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("final int internalID;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("final int offset;");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("RegUpdate(this.internalID, this.offset);");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("int get hashCode {");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("final int prime = 31;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("int result = 1;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("result = (prime * result) + internalID;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("result = (prime * result) + offset;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("return result;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("operator ==(RegUpdate other) {");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("if (identical(this, other))");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("return true;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("if (other == null)");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("return false;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("if (internalID != other.internalID)");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("return false;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("if (offset != other.offset)");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("return false;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("return true;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
          _builder.newLine();
        }
      }
      _builder.append("class ");
      _builder.append(unitName, "");
      _builder.append(" implements DartInterpreter{");
      _builder.newLineIfNotEmpty();
      {
        if (this.hasClock) {
          _builder.append("\t");
          _builder.append("Set<RegUpdate> _regUpdates=new HashSet<RegUpdate>();");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("final bool _disableEdges;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("final bool _disabledRegOutputlogic;");
          _builder.newLine();
        }
      }
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
      _builder.append("int _epsCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int _deltaCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int get updateStamp=>(_deltaCycle << 16) | (_epsCycle & 0xFFFF);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Map<String, int> _varIdx={");
      _builder.newLine();
      {
        boolean _hasElements = false;
        for(final VariableInformation v_1 : this.em.variables) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "	");
          }
          _builder.append("\t");
          _builder.append("\"");
          String _replaceAll = v_1.name.replaceAll("[\\$]", "\\\\\\$");
          _builder.append(_replaceAll, "	");
          _builder.append("\": ");
          Integer _get_1 = this.varIdx.get(v_1.name);
          _builder.append(_get_1, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("};");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("List<String> get names=>_varIdx.keys.toList();");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append(unitName, "	");
      _builder.append("(");
      {
        if (this.hasClock) {
          _builder.append("this._disableEdges, this._disabledRegOutputlogic");
        }
      }
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      {
        Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
        for(final VariableInformation v_2 : _excludeNull_1) {
          _builder.append("\t");
          _builder.append("set ");
          String _javaName = this.javaName(v_2, false);
          String _substring = _javaName.substring(1);
          _builder.append(_substring, "	");
          _builder.append("(");
          String _javaType = this.getJavaType(v_2, true);
          _builder.append(_javaType, "	");
          _builder.append(" value) =>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _javaName_1 = this.javaName(v_2, false);
          _builder.append(_javaName_1, "		");
          _builder.append("=value ");
          {
            boolean _and = false;
            boolean _isPredicate = this.isPredicate(v_2);
            boolean _not = (!_isPredicate);
            if (!_not) {
              _and = false;
            } else {
              int _length = v_2.dimensions.length;
              boolean _equals = (_length == 0);
              _and = (_not && _equals);
            }
            if (_and) {
              _builder.append("& ");
              CharSequence _asMask = this.asMask(v_2.width);
              _builder.append(_asMask, "		");
            }
          }
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          String _javaType_1 = this.getJavaType(v_2, true);
          _builder.append(_javaType_1, "	");
          _builder.append(" get ");
          String _javaName_2 = this.javaName(v_2, false);
          String _substring_1 = _javaName_2.substring(1);
          _builder.append(_substring_1, "	");
          _builder.append(" =>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _javaName_3 = this.javaName(v_2, false);
          _builder.append(_javaName_3, "		");
          _builder.append(" ");
          {
            boolean _and_1 = false;
            boolean _isPredicate_1 = this.isPredicate(v_2);
            boolean _not_1 = (!_isPredicate_1);
            if (!_not_1) {
              _and_1 = false;
            } else {
              int _length_1 = v_2.dimensions.length;
              boolean _equals_1 = (_length_1 == 0);
              _and_1 = (_not_1 && _equals_1);
            }
            if (_and_1) {
              _builder.append("& ");
              CharSequence _asMask_1 = this.asMask(v_2.width);
              _builder.append(_asMask_1, "		");
            }
          }
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
          {
            int _size = IterableExtensions.size(((Iterable<? extends Object>)Conversions.doWrapArray(v_2.dimensions)));
            boolean _notEquals = (_size != 0);
            if (_notEquals) {
              _builder.append("\t");
              _builder.append("void set");
              String _javaName_4 = this.javaName(v_2, false);
              String _substring_2 = _javaName_4.substring(1);
              _builder.append(_substring_2, "	");
              _builder.append("(");
              String _javaType_2 = this.getJavaType(v_2, false);
              _builder.append(_javaType_2, "	");
              _builder.append(" value");
              {
                int _size_1 = IterableExtensions.size(((Iterable<? extends Object>)Conversions.doWrapArray(v_2.dimensions)));
                ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
                for(final Integer i : _doubleDotLessThan) {
                  _builder.append(", int a");
                  _builder.append(i, "	");
                }
              }
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              String _javaName_5 = this.javaName(v_2, false);
              _builder.append(_javaName_5, "		");
              _builder.append("[");
              StringBuilder _arrayAccess = this.arrayAccess(v_2);
              _builder.append(_arrayAccess, "		");
              _builder.append("]=value & ");
              CharSequence _asMask_2 = this.asMask(v_2.width);
              _builder.append(_asMask_2, "		");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("\t");
              _builder.newLine();
              _builder.append("\t");
              String _javaType_3 = this.getJavaType(v_2, false);
              _builder.append(_javaType_3, "	");
              _builder.append(" get");
              String _javaName_6 = this.javaName(v_2, false);
              String _substring_3 = _javaName_6.substring(1);
              _builder.append(_substring_3, "	");
              _builder.append("(");
              {
                int _size_2 = IterableExtensions.size(((Iterable<? extends Object>)Conversions.doWrapArray(v_2.dimensions)));
                ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _size_2, true);
                boolean _hasElements_1 = false;
                for(final Integer i_1 : _doubleDotLessThan_1) {
                  if (!_hasElements_1) {
                    _hasElements_1 = true;
                  } else {
                    _builder.appendImmediate(",", "	");
                  }
                  _builder.append("int a");
                  _builder.append(i_1, "	");
                }
              }
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("return ");
              String _javaName_7 = this.javaName(v_2, false);
              _builder.append(_javaName_7, "		");
              _builder.append("[");
              StringBuilder _arrayAccess_1 = this.arrayAccess(v_2);
              _builder.append(_arrayAccess_1, "		");
              _builder.append("] & ");
              CharSequence _asMask_3 = this.asMask(v_2.width);
              _builder.append(_asMask_3, "		");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("\t");
              _builder.newLine();
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
      {
        if (this.hasClock) {
          _builder.append("\t");
          _builder.append("bool skipEdge(int local) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("int dc = local >> 16;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("// Register was updated in previous delta cylce, that is ok");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("if (dc < deltaCycle)");
          _builder.newLine();
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
          _builder.append("if ((dc == _deltaCycle) && ((local & 0xFFFF) == _epsCycle))");
          _builder.newLine();
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
        }
      }
      _builder.append("\t");
      _builder.append("void run(){");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("_deltaCycle++;");
      _builder.newLine();
      {
        if (this.hasClock) {
          _builder.append("\t\t");
          _builder.append("_epsCycle=0;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("do {");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("\t");
          _builder.append("_regUpdates.clear();");
          _builder.newLine();
        }
      }
      {
        for(final Frame f_1 : this.em.frames) {
          {
            boolean _and_2 = false;
            boolean _and_3 = false;
            boolean _and_4 = false;
            int _minus_1 = (-1);
            boolean _tripleEquals = (Integer.valueOf(f_1.edgeNegDepRes) == Integer.valueOf(_minus_1));
            if (!_tripleEquals) {
              _and_4 = false;
            } else {
              int _minus_2 = (-1);
              boolean _tripleEquals_1 = (Integer.valueOf(f_1.edgePosDepRes) == Integer.valueOf(_minus_2));
              _and_4 = (_tripleEquals && _tripleEquals_1);
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              int _length_2 = f_1.predNegDepRes.length;
              boolean _tripleEquals_2 = (Integer.valueOf(_length_2) == Integer.valueOf(0));
              _and_3 = (_and_4 && _tripleEquals_2);
            }
            if (!_and_3) {
              _and_2 = false;
            } else {
              int _length_3 = f_1.predPosDepRes.length;
              boolean _tripleEquals_3 = (Integer.valueOf(_length_3) == Integer.valueOf(0));
              _and_2 = (_and_3 && _tripleEquals_3);
            }
            if (_and_2) {
              _builder.append("\t\t");
              _builder.append("_frame");
              _builder.append(f_1.uniqueID, "		");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t\t");
              CharSequence _createNegEdge = this.createNegEdge(f_1.edgeNegDepRes, handled);
              _builder.append(_createNegEdge, "		");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              CharSequence _createPosEdge = this.createPosEdge(f_1.edgePosDepRes, handled);
              _builder.append(_createPosEdge, "		");
              _builder.newLineIfNotEmpty();
              {
                for(final int p : f_1.predNegDepRes) {
                  _builder.append("\t\t");
                  CharSequence _createBooleanPred = this.createBooleanPred(p, handled);
                  _builder.append(_createBooleanPred, "		");
                  _builder.newLineIfNotEmpty();
                }
              }
              {
                for(final int p_1 : f_1.predPosDepRes) {
                  _builder.append("\t\t");
                  CharSequence _createBooleanPred_1 = this.createBooleanPred(p_1, handled);
                  _builder.append(_createBooleanPred_1, "		");
                  _builder.newLineIfNotEmpty();
                }
              }
              _builder.append("\t\t");
              _builder.append("if (");
              String _predicates = this.predicates(f_1);
              _builder.append(_predicates, "		");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("\t");
              _builder.append("_frame");
              _builder.append(f_1.uniqueID, "			");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      {
        if (this.hasClock) {
          _builder.append("\t\t");
          _builder.append("_updateRegs();");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("_epsCycle++;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("} while (!_regUpdates.isEmpty && !_disabledRegOutputlogic);");
          _builder.newLine();
        }
      }
      {
        Iterable<VariableInformation> _excludeNull_2 = this.excludeNull(this.em.variables);
        final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
            public Boolean apply(final VariableInformation it) {
              Boolean _get = DartCompiler.this.prevMap.get(it.name);
              boolean _notEquals = (!Objects.equal(_get, null));
              return Boolean.valueOf(_notEquals);
            }
          };
        Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull_2, _function);
        for(final VariableInformation v_3 : _filter) {
          _builder.append("\t\t");
          String _copyPrev = this.copyPrev(v_3);
          _builder.append(_copyPrev, "		");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      {
        if (this.hasClock) {
          _builder.append("\t");
          CharSequence _copyRegs = this.copyRegs();
          _builder.append(_copyRegs, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      CharSequence _hdlInterpreter = this.hdlInterpreter();
      _builder.append(_hdlInterpreter, "	");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public String predicates(final Frame f) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    boolean first = true;
    int _minus = (-1);
    boolean _tripleNotEquals = (Integer.valueOf(f.edgeNegDepRes) != Integer.valueOf(_minus));
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      InternalInformation _asInternal = this.asInternal(f.edgeNegDepRes);
      String _javaName = this.javaName(_asInternal, false);
      _builder.append(_javaName, "");
      _builder.append("_isFalling && !");
      InternalInformation _asInternal_1 = this.asInternal(f.edgeNegDepRes);
      String _javaName_1 = this.javaName(_asInternal_1, false);
      _builder.append(_javaName_1, "");
      _builder.append("_fallingIsHandled");
      sb.append(_builder);
      first = false;
    }
    int _minus_1 = (-1);
    boolean _tripleNotEquals_1 = (Integer.valueOf(f.edgePosDepRes) != Integer.valueOf(_minus_1));
    if (_tripleNotEquals_1) {
      boolean _not = (!first);
      if (_not) {
        sb.append(" && ");
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      InternalInformation _asInternal_2 = this.asInternal(f.edgePosDepRes);
      String _javaName_2 = this.javaName(_asInternal_2, false);
      _builder_1.append(_javaName_2, "");
      _builder_1.append("_isRising&& !");
      InternalInformation _asInternal_3 = this.asInternal(f.edgePosDepRes);
      String _javaName_3 = this.javaName(_asInternal_3, false);
      _builder_1.append(_javaName_3, "");
      _builder_1.append("_risingIsHandled");
      sb.append(_builder_1);
      first = false;
    }
    for (final int p : f.predNegDepRes) {
      {
        boolean _not_1 = (!first);
        if (_not_1) {
          sb.append(" && ");
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("!p");
        _builder_2.append(p, "");
        _builder_2.append(" && p");
        _builder_2.append(p, "");
        _builder_2.append("_fresh");
        sb.append(_builder_2);
        first = false;
      }
    }
    for (final int p_1 : f.predPosDepRes) {
      {
        boolean _not_1 = (!first);
        if (_not_1) {
          sb.append(" && ");
        }
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("p");
        _builder_2.append(p_1, "");
        _builder_2.append(" && p");
        _builder_2.append(p_1, "");
        _builder_2.append("_fresh");
        sb.append(_builder_2);
        first = false;
      }
    }
    return sb.toString();
  }
  
  public CharSequence createBooleanPred(final int id, final Set<Integer> handled) {
    CharSequence _xblockexpression = null;
    {
      boolean _contains = handled.contains(Integer.valueOf(id));
      if (_contains) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder.toString();
      }
      handled.add(Integer.valueOf(id));
      StringConcatenation _builder_1 = new StringConcatenation();
      InternalInformation _asInternal = this.asInternal(id);
      int _minus = (-1);
      CharSequence _ter = this.getter(_asInternal, false, id, _minus);
      _builder_1.append(_ter, "");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool p");
      _builder_1.append(id, "");
      _builder_1.append("_fresh=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("int up");
      _builder_1.append(id, "");
      _builder_1.append("=");
      InternalInformation _asInternal_1 = this.asInternal(id);
      String _javaName = this.javaName(_asInternal_1.info, false);
      _builder_1.append(_javaName, "");
      _builder_1.append("_update;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if ((up");
      _builder_1.append(id, "");
      _builder_1.append(">>16 != _deltaCycle) || ((up");
      _builder_1.append(id, "");
      _builder_1.append("&0xFFFF) != _epsCycle)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("p");
      _builder_1.append(id, "	");
      _builder_1.append("_fresh=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
  
  public CharSequence createPosEdge(final int id, final Set<Integer> handledEdges) {
    CharSequence _xblockexpression = null;
    {
      boolean _contains = handledEdges.contains(Integer.valueOf(id));
      if (_contains) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder.toString();
      }
      handledEdges.add(Integer.valueOf(id));
      final InternalInformation internal = this.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _javaName = this.javaName(internal, false);
      _builder_1.append(_javaName, "");
      _builder_1.append("_isRising=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _javaName_1 = this.javaName(internal, false);
      _builder_1.append(_javaName_1, "");
      _builder_1.append("_risingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!_disableEdges){");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal = this.asInternal(id);
      int _minus = (-1);
      CharSequence _ter = this.getter(_asInternal, false, id, _minus);
      _builder_1.append(_ter, "	");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      InternalInformation _asInternal_1 = this.asInternal(id);
      int _minus_1 = (-1);
      CharSequence _ter_1 = this.getter(_asInternal_1, true, id, _minus_1);
      _builder_1.append(_ter_1, "	");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("if ((t");
      _builder_1.append(id, "	");
      _builder_1.append("_prev!=0) || (t");
      _builder_1.append(id, "	");
      _builder_1.append("!=1)) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      String _javaName_2 = this.javaName(internal, false);
      _builder_1.append(_javaName_2, "		");
      _builder_1.append("_isRising=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _javaName_3 = this.javaName(internal.info, false);
      _builder_1.append(_javaName_3, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _javaName_4 = this.javaName(internal, false);
      _builder_1.append(_javaName_4, "	");
      _builder_1.append("_risingIsHandled=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
  
  public CharSequence createNegEdge(final int id, final Set<Integer> handledEdges) {
    CharSequence _xblockexpression = null;
    {
      boolean _contains = handledEdges.contains(Integer.valueOf(id));
      if (_contains) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder.toString();
      }
      handledEdges.add(Integer.valueOf(id));
      final InternalInformation internal = this.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _javaName = this.javaName(internal, false);
      _builder_1.append(_javaName, "");
      _builder_1.append("_isFalling=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _javaName_1 = this.javaName(internal, false);
      _builder_1.append(_javaName_1, "");
      _builder_1.append("_fallingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!_disableEdges){");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal = this.asInternal(id);
      int _minus = (-1);
      CharSequence _ter = this.getter(_asInternal, false, id, _minus);
      _builder_1.append(_ter, "	");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      InternalInformation _asInternal_1 = this.asInternal(id);
      int _minus_1 = (-1);
      CharSequence _ter_1 = this.getter(_asInternal_1, true, id, _minus_1);
      _builder_1.append(_ter_1, "	");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("if ((t");
      _builder_1.append(id, "	");
      _builder_1.append("_prev!=1) || (t");
      _builder_1.append(id, "	");
      _builder_1.append("!=0)) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      String _javaName_2 = this.javaName(internal, false);
      _builder_1.append(_javaName_2, "		");
      _builder_1.append("_isFalling=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _javaName_3 = this.javaName(internal.info, false);
      _builder_1.append(_javaName_3, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _javaName_4 = this.javaName(internal, false);
      _builder_1.append(_javaName_4, "	");
      _builder_1.append("_fallingIsHandled=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
  
  public CharSequence asMask(final int width) {
    BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(width);
    final BigInteger mask = _shiftLeft.subtract(BigInteger.ONE);
    return this.toHexString(mask);
  }
  
  public CharSequence hdlInterpreter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("void setVar(int idx, dynamic value) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.em.variables) {
        {
          boolean _isNull = this.isNull(v);
          boolean _not = (!_isNull);
          if (_not) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "		");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            String _javaName = this.javaName(v, false);
            String _substring = _javaName.substring(1);
            _builder.append(_substring, "			");
            _builder.append("=value");
            {
              boolean _isPredicate = this.isPredicate(v);
              if (_isPredicate) {
                _builder.append("==0?false:true");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("break;");
            _builder.newLine();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_1 = this.varIdx.get(v.name);
            _builder.append(_get_1, "		");
            _builder.append(": ");
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
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return _varIdx[name];");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v_1 : this.em.variables) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get_2 = this.varIdx.get(v_1.name);
        _builder.append(_get_2, "		");
        _builder.append(": return \"");
        String _replaceAll = v_1.name.replaceAll("[\\$]", "\\\\\\$");
        _builder.append(_replaceAll, "		");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("dynamic getVar(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v_2 : this.em.variables) {
        {
          boolean _isPredicate_1 = this.isPredicate(v_2);
          if (_isPredicate_1) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_3 = this.varIdx.get(v_2.name);
            _builder.append(_get_3, "		");
            _builder.append(": return ");
            String _javaName_1 = this.javaName(v_2, false);
            String _substring_1 = _javaName_1.substring(1);
            _builder.append(_substring_1, "		");
            _builder.append("?1:0;");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isNull_1 = this.isNull(v_2);
            if (_isNull_1) {
              _builder.append("\t\t");
              _builder.append("case ");
              Integer _get_4 = this.varIdx.get(v_2.name);
              _builder.append(_get_4, "		");
              _builder.append(": return 0;");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t\t");
              _builder.append("case ");
              Integer _get_5 = this.varIdx.get(v_2.name);
              _builder.append(_get_5, "		");
              _builder.append(": return ");
              String _javaName_2 = this.javaName(v_2, false);
              String _substring_2 = _javaName_2.substring(1);
              _builder.append(_substring_2, "		");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int get deltaCycle =>_deltaCycle;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int get varNum => ");
    int _size = this.varIdx.size();
    _builder.append(_size, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    CharSequence _description = this.getDescription();
    _builder.append(_description, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence getDescription() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Description get description=>new Description(");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            boolean _tripleEquals = (it.dir == Direction.IN);
            return Boolean.valueOf(_tripleEquals);
          }
        };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function);
      boolean _hasElements = false;
      for(final VariableInformation v : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "	");
        }
        _builder.append("\t");
        CharSequence _asPort = this.asPort(v);
        _builder.append(_asPort, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("],");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation,Boolean> _function_1 = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            boolean _tripleEquals = (it.dir == Direction.INOUT);
            return Boolean.valueOf(_tripleEquals);
          }
        };
      Iterable<VariableInformation> _filter_1 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_1);
      boolean _hasElements_1 = false;
      for(final VariableInformation v_1 : _filter_1) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder.appendImmediate(",", "	");
        }
        _builder.append("\t");
        CharSequence _asPort_1 = this.asPort(v_1);
        _builder.append(_asPort_1, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("],");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation,Boolean> _function_2 = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            boolean _tripleEquals = (it.dir == Direction.OUT);
            return Boolean.valueOf(_tripleEquals);
          }
        };
      Iterable<VariableInformation> _filter_2 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_2);
      boolean _hasElements_2 = false;
      for(final VariableInformation v_2 : _filter_2) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
        } else {
          _builder.appendImmediate(",", "	");
        }
        _builder.append("\t");
        CharSequence _asPort_2 = this.asPort(v_2);
        _builder.append(_asPort_2, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("],");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation,Boolean> _function_3 = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            boolean _tripleEquals = (it.dir == Direction.INTERNAL);
            return Boolean.valueOf(_tripleEquals);
          }
        };
      Iterable<VariableInformation> _filter_3 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_3);
      boolean _hasElements_3 = false;
      for(final VariableInformation v_3 : _filter_3) {
        if (!_hasElements_3) {
          _hasElements_3 = true;
        } else {
          _builder.appendImmediate(",", "	");
        }
        _builder.append("\t");
        CharSequence _asPort_3 = this.asPort(v_3);
        _builder.append(_asPort_3, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("], _varIdx);");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence asPort(final VariableInformation v) {
    CharSequence _xblockexpression = null;
    {
      String dims = "";
      int _length = v.dimensions.length;
      boolean _notEquals = (_length != 0);
      if (_notEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append(", dimensions: [");
        {
          boolean _hasElements = false;
          for(final int i : v.dimensions) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append("i");
          }
        }
        _builder.append("]");
        dims = _builder.toString();
      }
      String _xifexpression = null;
      if (v.isClock) {
        _xifexpression = ", clock:true";
      } else {
        _xifexpression = "";
      }
      final String clock = _xifexpression;
      String _xifexpression_1 = null;
      if (v.isReset) {
        _xifexpression_1 = ", reset:true";
      } else {
        _xifexpression_1 = "";
      }
      final String reset = _xifexpression_1;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("new Port(");
      Integer _get = this.varIdx.get(v.name);
      _builder_1.append(_get, "");
      _builder_1.append(", \"");
      String _replaceAll = v.name.replaceAll("[\\$]", "\\\\\\$");
      _builder_1.append(_replaceAll, "");
      _builder_1.append("\", ");
      _builder_1.append(v.width, "");
      _builder_1.append(dims, "");
      _builder_1.append(clock, "");
      _builder_1.append(reset, "");
      _builder_1.append(")");
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
  }
  
  public boolean isNull(final VariableInformation information) {
    boolean _equals = Objects.equal(information.name, "#null");
    return _equals;
  }
  
  public Iterable<VariableInformation> excludeNull(final VariableInformation[] vars) {
    final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
        public Boolean apply(final VariableInformation it) {
          boolean _isNull = DartCompiler.this.isNull(it);
          boolean _not = (!_isNull);
          return Boolean.valueOf(_not);
        }
      };
    Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
  }
  
  public Iterable<InternalInformation> excludeNull(final InternalInformation[] vars) {
    final Function1<InternalInformation,Boolean> _function = new Function1<InternalInformation,Boolean>() {
        public Boolean apply(final InternalInformation it) {
          boolean _isNull = DartCompiler.this.isNull(it.info);
          boolean _not = (!_isNull);
          return Boolean.valueOf(_not);
        }
      };
    Iterable<InternalInformation> _filter = IterableExtensions.<InternalInformation>filter(((Iterable<InternalInformation>)Conversions.doWrapArray(vars)), _function);
    return _filter;
  }
  
  public CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void _updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (RegUpdate reg in _regUpdates) {");
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
  
  public CharSequence getter(final InternalInformation info, final boolean prev, final int pos, final int frameID) {
    CharSequence _xblockexpression = null;
    {
      StringBuilder _stringBuilder = new StringBuilder();
      final StringBuilder sb = _stringBuilder;
      final CharSequence mask = this.asMask(info.actualWidth);
      for (final int arr : info.arrayIdx) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[");
        _builder.append(arr, "");
        _builder.append("]");
        sb.append(_builder);
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      {
        int _length = info.arrayIdx.length;
        ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
        boolean _hasElements = false;
        for(final int i : _doubleDotLessThan) {
          if (!_hasElements) {
            _hasElements = true;
            _builder_1.append("[", "");
          } else {
            _builder_1.appendImmediate("][", "");
          }
          _builder_1.append("a");
          _builder_1.append(i, "");
        }
        if (_hasElements) {
          _builder_1.append("]", "");
        }
      }
      final String arrAcc = _builder_1.toString();
      String varName = ("t" + Integer.valueOf(pos));
      if (info.isPred) {
        String _plus = ("p" + Integer.valueOf(pos));
        varName = _plus;
      }
      if (prev) {
        String _plus_1 = (varName + "_prev");
        varName = _plus_1;
      }
      CharSequence _xifexpression = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          boolean _equals = (info.actualWidth == info.info.width);
          if (_equals) {
            String _javaType = this.getJavaType(info);
            _builder_2.append(_javaType, "");
            _builder_2.append(" ");
            _builder_2.append(varName, "");
            _builder_2.append("=");
            String _javaName = this.javaName(info.info, prev);
            _builder_2.append(_javaName, "");
            _builder_2.append(sb, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            boolean _equals_1 = (info.actualWidth == 1);
            if (_equals_1) {
              String _javaType_1 = this.getJavaType(info);
              _builder_2.append(_javaType_1, "");
              _builder_2.append(" ");
              _builder_2.append(varName, "");
              _builder_2.append("=(");
              String _javaName_1 = this.javaName(info.info, prev);
              _builder_2.append(_javaName_1, "");
              _builder_2.append(sb, "");
              _builder_2.append(" >> ");
              _builder_2.append(info.bitStart, "");
              _builder_2.append(") & 1;");
              _builder_2.newLineIfNotEmpty();
            } else {
              String _javaType_2 = this.getJavaType(info);
              _builder_2.append(_javaType_2, "");
              _builder_2.append(" ");
              _builder_2.append(varName, "");
              _builder_2.append("=(");
              String _javaName_2 = this.javaName(info.info, prev);
              _builder_2.append(_javaName_2, "");
              _builder_2.append(sb, "");
              _builder_2.append(" >> ");
              _builder_2.append(info.bitEnd, "");
              _builder_2.append(") & ");
              _builder_2.append(mask, "");
              _builder_2.append(";");
              _builder_2.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression = _builder_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        {
          boolean _equals_2 = (info.actualWidth == info.info.width);
          if (_equals_2) {
            String _javaType_3 = this.getJavaType(info);
            _builder_3.append(_javaType_3, "");
            _builder_3.append(" ");
            _builder_3.append(varName, "");
            _builder_3.append("= ");
            String _javaName_3 = this.javaName(info.info, prev);
            _builder_3.append(_javaName_3, "");
            _builder_3.append(arrAcc, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            boolean _equals_3 = (info.actualWidth == 1);
            if (_equals_3) {
              String _javaType_4 = this.getJavaType(info);
              _builder_3.append(_javaType_4, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("= (");
              String _javaName_4 = this.javaName(info.info, prev);
              _builder_3.append(_javaName_4, "");
              _builder_3.append(arrAcc, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitStart, "");
              _builder_3.append(") & 1;");
              _builder_3.newLineIfNotEmpty();
            } else {
              String _javaType_5 = this.getJavaType(info);
              _builder_3.append(_javaType_5, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("= (");
              String _javaName_5 = this.javaName(info.info, prev);
              _builder_3.append(_javaName_5, "");
              _builder_3.append(arrAcc, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitEnd, "");
              _builder_3.append(") & ");
              CharSequence _asMask = this.asMask(info.actualWidth);
              _builder_3.append(_asMask, "");
              _builder_3.append(";");
              _builder_3.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression = _builder_3;
      }
      _xblockexpression = (_xifexpression);
    }
    return _xblockexpression;
  }
  
  public CharSequence setter(final InternalInformation info, final String value) {
    CharSequence _xblockexpression = null;
    {
      BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(info.actualWidth);
      final BigInteger mask = _shiftLeft.subtract(BigInteger.ONE);
      final CharSequence maskString = this.toHexString(mask);
      final BigInteger subMask = mask.shiftLeft(info.bitEnd);
      BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(info.info.width);
      final BigInteger fullMask = _shiftLeft_1.subtract(BigInteger.ONE);
      BigInteger _xor = fullMask.xor(subMask);
      final CharSequence writeMask = this.toHexString(_xor);
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
      String regSuffix = "";
      if (info.isShadowReg) {
        String _plus = ("$reg" + fixedAccess);
        fixedAccess = _plus;
        regSuffix = "$reg";
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("//Actual Width: ");
        _builder_2.append(info.actualWidth, "");
        _builder_2.append(" Bit End: ");
        _builder_2.append(info.bitEnd, "");
        _builder_2.append(" subMask: ");
        CharSequence _hexString = this.toHexString(subMask);
        _builder_2.append(_hexString, "");
        _builder_2.append(" fullMask: ");
        CharSequence _hexString_1 = this.toHexString(fullMask);
        _builder_2.append(_hexString_1, "");
        _builder_2.newLineIfNotEmpty();
        {
          boolean _equals = (info.actualWidth == info.info.width);
          if (_equals) {
            {
              if (info.isShadowReg) {
                String _javaType = this.getJavaType(info.info, false);
                _builder_2.append(_javaType, "");
                _builder_2.append(" current=");
                String _javaName = this.javaName(info.info, false);
                _builder_2.append(_javaName, "");
                _builder_2.append(fixedAccess, "");
                _builder_2.append(";");
              }
            }
            _builder_2.newLineIfNotEmpty();
            String _javaName_1 = this.javaName(info.info, false);
            _builder_2.append(_javaName_1, "");
            _builder_2.append(fixedAccess, "");
            _builder_2.append("=");
            _builder_2.append(value, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            String _javaType_1 = this.getJavaType(info.info, false);
            _builder_2.append(_javaType_1, "");
            _builder_2.append(" current=");
            String _javaName_2 = this.javaName(info.info, false);
            _builder_2.append(_javaName_2, "");
            _builder_2.append(fixedAccess, "");
            _builder_2.append(" & ");
            _builder_2.append(writeMask, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append(value, "");
            _builder_2.append("=((");
            _builder_2.append(value, "");
            _builder_2.append(" & ");
            _builder_2.append(maskString, "");
            _builder_2.append(") << ");
            _builder_2.append(info.bitEnd, "");
            _builder_2.append(");");
            _builder_2.newLineIfNotEmpty();
            String _javaName_3 = this.javaName(info.info, false);
            _builder_2.append(_javaName_3, "");
            _builder_2.append(fixedAccess, "");
            _builder_2.append("=current|");
            _builder_2.append(value, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_2.append("if (current!=");
            _builder_2.append(value, "");
            _builder_2.append(")");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            _builder_2.append("_regUpdates.add(new RegUpdate(");
            Integer _get = this.varIdx.get(info.info.name);
            _builder_2.append(_get, "	");
            _builder_2.append(", ");
            _builder_2.append(off, "	");
            _builder_2.append("));");
            _builder_2.newLineIfNotEmpty();
          }
        }
        {
          if (info.isPred) {
            String _javaName_4 = this.javaName(info.info, false);
            _builder_2.append(_javaName_4, "");
            _builder_2.append("_update=updateStamp;");
          }
        }
        _builder_2.newLineIfNotEmpty();
        _xifexpression_1 = _builder_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("int offset=");
        _builder_3.append(varAccess, "");
        _builder_3.append(";");
        _builder_3.newLineIfNotEmpty();
        {
          boolean _equals_1 = (info.actualWidth == info.info.width);
          if (_equals_1) {
            {
              if (info.isShadowReg) {
                String _javaType_2 = this.getJavaType(info.info, false);
                _builder_3.append(_javaType_2, "");
                _builder_3.append(" current=");
                String _javaName_5 = this.javaName(info.info, false);
                _builder_3.append(_javaName_5, "");
                _builder_3.append(regSuffix, "");
                _builder_3.append("[offset];");
              }
            }
            _builder_3.newLineIfNotEmpty();
            String _javaName_6 = this.javaName(info.info, false);
            _builder_3.append(_javaName_6, "");
            _builder_3.append(regSuffix, "");
            _builder_3.append("[offset]=");
            _builder_3.append(value, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            String _javaType_3 = this.getJavaType(info.info, false);
            _builder_3.append(_javaType_3, "");
            _builder_3.append(" current=");
            String _javaName_7 = this.javaName(info.info, false);
            _builder_3.append(_javaName_7, "");
            _builder_3.append(regSuffix, "");
            _builder_3.append("[offset] & ");
            _builder_3.append(writeMask, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append(value, "");
            _builder_3.append("=((");
            _builder_3.append(value, "");
            _builder_3.append(" & ");
            _builder_3.append(maskString, "");
            _builder_3.append(") << ");
            _builder_3.append(info.bitEnd, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            String _javaName_8 = this.javaName(info.info, false);
            _builder_3.append(_javaName_8, "");
            _builder_3.append(regSuffix, "");
            _builder_3.append("[offset]=current|");
            _builder_3.append(value, "");
            _builder_3.append(");");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_3.append("if (current!=");
            _builder_3.append(value, "");
            _builder_3.append(")");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("_regUpdates.add(new RegUpdate(");
            Integer _get_1 = this.varIdx.get(info.info.name);
            _builder_3.append(_get_1, "	");
            _builder_3.append(", offset));");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isPred) {
            String _javaName_9 = this.javaName(info.info, false);
            _builder_3.append(_javaName_9, "");
            _builder_3.append("_update=updateStamp;");
          }
        }
        _builder_3.newLineIfNotEmpty();
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
  
  public CharSequence toHexString(final BigInteger value) {
    CharSequence _xblockexpression = null;
    {
      int _signum = value.signum();
      boolean _lessThan = (_signum < 0);
      if (_lessThan) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Mask can not be negative");
        throw _illegalArgumentException;
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("0x");
      String _string = value.toString(16);
      _builder.append(_string, "");
      _xblockexpression = (_builder);
    }
    return _xblockexpression;
  }
  
  public String method(final Frame frame) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void _frame");
    _builder.append(frame.uniqueID, "");
    _builder.append("() {");
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
    InternalInformation _asInternal = this.asInternal(frame.outputId);
    boolean _notEquals = (!Objects.equal(_asInternal.info.name, "#null"));
    if (_notEquals) {
      InternalInformation _asInternal_1 = this.asInternal(frame.outputId);
      CharSequence _setter = this.setter(_asInternal_1, last);
      sb.append(_setter);
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("//Write to #null");
      _builder_1.newLine();
      sb.append(_builder_1);
    }
    StringConcatenation _builder_2 = new StringConcatenation();
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
          _builder.append("=t");
          _builder.append(a, "");
          _builder.append(";");
          sb.append(_builder);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.writeInternal)) {
          _matched=true;
          int _size = arr.size();
          InternalInformation _asInternal = this.asInternal(inst.arg1);
          int _length = _asInternal.info.dimensions.length;
          boolean _lessThan = (_size < _length);
          if (_lessThan) {
            InternalInformation _asInternal_1 = this.asInternal(inst.arg1);
            String name = this.javaName(_asInternal_1, false);
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(name, "");
            _builder_1.append(".fillRange(0, ");
            _builder_1.append(name, "");
            _builder_1.append(".length, t");
            _builder_1.append(a, "");
            _builder_1.append(");");
            sb.append(_builder_1);
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            InternalInformation _asInternal_2 = this.asInternal(inst.arg1);
            String _javaName = this.javaName(_asInternal_2, false);
            _builder_2.append(_javaName, "");
            {
              boolean _hasElements = false;
              for(final Integer ai : arr) {
                if (!_hasElements) {
                  _hasElements = true;
                  _builder_2.append("[", "");
                } else {
                  _builder_2.appendImmediate("][", "");
                }
                _builder_2.append("a");
                _builder_2.append(ai, "");
              }
              if (_hasElements) {
                _builder_2.append("]", "");
              }
            }
            _builder_2.append("=t");
            _builder_2.append(a, "");
            _builder_2.append(";");
            sb.append(_builder_2);
            arr.clear();
          }
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
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("int t");
          _builder_3.append(pos, "");
          _builder_3.append("=t");
          _builder_3.append(b, "");
          _builder_3.append(" & t");
          _builder_3.append(a, "");
          _builder_3.append(";");
          sb.append(_builder_3);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.arith_neg)) {
          _matched=true;
          StringConcatenation _builder_4 = new StringConcatenation();
          _builder_4.append("int t");
          _builder_4.append(pos, "");
          _builder_4.append("=-t");
          _builder_4.append(a, "");
          _builder_4.append(";");
          sb.append(_builder_4);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bit_neg)) {
          _matched=true;
          StringConcatenation _builder_5 = new StringConcatenation();
          _builder_5.append("int t");
          _builder_5.append(pos, "");
          _builder_5.append("=~t");
          _builder_5.append(a, "");
          _builder_5.append(";");
          sb.append(_builder_5);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bitAccessSingle)) {
          _matched=true;
          StringConcatenation _builder_6 = new StringConcatenation();
          _builder_6.append("int t");
          _builder_6.append(pos, "");
          _builder_6.append("=(t");
          _builder_6.append(a, "");
          _builder_6.append(" >> ");
          _builder_6.append(inst.arg1, "");
          _builder_6.append(") & 1;");
          sb.append(_builder_6);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.bitAccessSingleRange)) {
          _matched=true;
          final int highBit = inst.arg1;
          final int lowBit = inst.arg2;
          int _minus = (highBit - lowBit);
          int _plus = (_minus + 1);
          final CharSequence mask = this.asMask(_plus);
          StringConcatenation _builder_7 = new StringConcatenation();
          _builder_7.append("int t");
          _builder_7.append(pos, "");
          _builder_7.append("=(t");
          _builder_7.append(a, "");
          _builder_7.append(" >> ");
          _builder_7.append(lowBit, "");
          _builder_7.append(") & ");
          _builder_7.append(mask, "");
          _builder_7.append(";");
          sb.append(_builder_7);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_int)) {
          _matched=true;
          final int targetWidth = inst.arg1;
          final int currWidth = inst.arg2;
          boolean _greaterEqualsThan = (targetWidth >= currWidth);
          if (_greaterEqualsThan) {
          } else {
            BigInteger _shiftLeft = BigInteger.ONE.shiftLeft(targetWidth);
            final BigInteger mask_1 = _shiftLeft.subtract(BigInteger.ONE);
            StringConcatenation _builder_8 = new StringConcatenation();
            _builder_8.append("int u");
            _builder_8.append(pos, "");
            _builder_8.append(" = t");
            _builder_8.append(a, "");
            _builder_8.append(" & ");
            _builder_8.append(mask_1, "");
            _builder_8.append(";");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("if ((u");
            _builder_8.append(pos, "");
            _builder_8.append(" & ");
            int _minus_1 = (targetWidth - 1);
            BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(_minus_1);
            CharSequence _hexString = this.toHexString(_shiftLeft_1);
            _builder_8.append(_hexString, "");
            _builder_8.append(") != 0)) { // MSB is set");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("\t");
            _builder_8.append("if (u");
            _builder_8.append(pos, "	");
            _builder_8.append(" > 0) {");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("\t\t");
            _builder_8.append("u");
            _builder_8.append(pos, "		");
            _builder_8.append(" = -u");
            _builder_8.append(pos, "		");
            _builder_8.append(";");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("\t");
            _builder_8.append("}");
            _builder_8.newLine();
            _builder_8.append("} else {");
            _builder_8.newLine();
            _builder_8.append("\t");
            _builder_8.append("if (u");
            _builder_8.append(pos, "	");
            _builder_8.append(" < 0) {");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("\t\t");
            _builder_8.append("u");
            _builder_8.append(pos, "		");
            _builder_8.append(" = -u");
            _builder_8.append(pos, "		");
            _builder_8.append(";");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("\t");
            _builder_8.append("}");
            _builder_8.newLine();
            _builder_8.append("}");
            _builder_8.newLine();
            _builder_8.append("t");
            _builder_8.append(pos, "");
            _builder_8.append(" = u");
            _builder_8.append(pos, "");
            _builder_8.append(";");
            _builder_8.newLineIfNotEmpty();
            sb.append(_builder_8);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_uint)) {
          _matched=true;
          StringConcatenation _builder_9 = new StringConcatenation();
          _builder_9.append("int t");
          _builder_9.append(pos, "");
          _builder_9.append("=t");
          _builder_9.append(a, "");
          _builder_9.append(" & ");
          CharSequence _asMask = this.asMask(inst.arg1);
          _builder_9.append(_asMask, "");
          _builder_9.append(";");
          sb.append(_builder_9);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiNeg)) {
          _matched=true;
          StringConcatenation _builder_10 = new StringConcatenation();
          _builder_10.append("bool t");
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
          _builder_11.append("bool t");
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
          _builder_12.append("bool t");
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
          _builder_13.append("int t");
          _builder_13.append(pos, "");
          _builder_13.append("=0;");
          sb.append(_builder_13);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const1)) {
          _matched=true;
          StringConcatenation _builder_14 = new StringConcatenation();
          _builder_14.append("int t");
          _builder_14.append(pos, "");
          _builder_14.append("=1;");
          sb.append(_builder_14);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const2)) {
          _matched=true;
          StringConcatenation _builder_15 = new StringConcatenation();
          _builder_15.append("int t");
          _builder_15.append(pos, "");
          _builder_15.append("=2;");
          sb.append(_builder_15);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.constAll1)) {
          _matched=true;
          StringConcatenation _builder_16 = new StringConcatenation();
          _builder_16.append("int t");
          _builder_16.append(pos, "");
          _builder_16.append("=");
          CharSequence _asMask_1 = this.asMask(inst.arg1);
          _builder_16.append(_asMask_1, "");
          _builder_16.append(";");
          sb.append(_builder_16);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.concat)) {
          _matched=true;
          StringConcatenation _builder_17 = new StringConcatenation();
          _builder_17.append("int t");
          _builder_17.append(pos, "");
          _builder_17.append("=(t");
          _builder_17.append(b, "");
          _builder_17.append(" << ");
          _builder_17.append(inst.arg2, "");
          _builder_17.append(") | t");
          _builder_17.append(a, "");
          _builder_17.append(";");
          sb.append(_builder_17);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadConstant)) {
          _matched=true;
          StringConcatenation _builder_18 = new StringConcatenation();
          _builder_18.append("int t");
          _builder_18.append(pos, "");
          _builder_18.append("=");
          CharSequence _constant = this.constant(inst.arg1, f);
          _builder_18.append(_constant, "");
          _builder_18.append(";");
          sb.append(_builder_18);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadInternal)) {
          _matched=true;
          final InternalInformation internal = this.asInternal(inst.arg1);
          CharSequence _ter = this.getter(internal, false, pos, f.uniqueID);
          sb.append(_ter);
          arr.clear();
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.and)) {
          _matched=true;
          StringConcatenation _builder_19 = new StringConcatenation();
          _builder_19.append("int t");
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
        if (Objects.equal(_switchValue,Instruction.or)) {
          _matched=true;
          StringConcatenation _builder_20 = new StringConcatenation();
          _builder_20.append("int t");
          _builder_20.append(pos, "");
          _builder_20.append("=t");
          _builder_20.append(b, "");
          _builder_20.append(" | t");
          _builder_20.append(a, "");
          _builder_20.append(";");
          sb.append(_builder_20);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.xor)) {
          _matched=true;
          StringConcatenation _builder_21 = new StringConcatenation();
          _builder_21.append("int t");
          _builder_21.append(pos, "");
          _builder_21.append("=t");
          _builder_21.append(b, "");
          _builder_21.append(" ^ t");
          _builder_21.append(a, "");
          _builder_21.append(";");
          sb.append(_builder_21);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.plus)) {
          _matched=true;
          StringConcatenation _builder_22 = new StringConcatenation();
          _builder_22.append("int t");
          _builder_22.append(pos, "");
          _builder_22.append("=t");
          _builder_22.append(b, "");
          _builder_22.append(" + t");
          _builder_22.append(a, "");
          _builder_22.append(";");
          sb.append(_builder_22);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.minus)) {
          _matched=true;
          StringConcatenation _builder_23 = new StringConcatenation();
          _builder_23.append("int t");
          _builder_23.append(pos, "");
          _builder_23.append("=t");
          _builder_23.append(b, "");
          _builder_23.append(" - t");
          _builder_23.append(a, "");
          _builder_23.append(";");
          sb.append(_builder_23);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.mul)) {
          _matched=true;
          StringConcatenation _builder_24 = new StringConcatenation();
          _builder_24.append("int t");
          _builder_24.append(pos, "");
          _builder_24.append("=t");
          _builder_24.append(b, "");
          _builder_24.append(" * t");
          _builder_24.append(a, "");
          _builder_24.append(";");
          sb.append(_builder_24);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.div)) {
          _matched=true;
          StringConcatenation _builder_25 = new StringConcatenation();
          _builder_25.append("int t");
          _builder_25.append(pos, "");
          _builder_25.append("=t");
          _builder_25.append(b, "");
          _builder_25.append(" / t");
          _builder_25.append(a, "");
          _builder_25.append(";");
          sb.append(_builder_25);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sll)) {
          _matched=true;
          StringConcatenation _builder_26 = new StringConcatenation();
          _builder_26.append("int t");
          _builder_26.append(pos, "");
          _builder_26.append("=t");
          _builder_26.append(b, "");
          _builder_26.append(" << t");
          _builder_26.append(a, "");
          _builder_26.append(";");
          sb.append(_builder_26);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.srl)) {
          _matched=true;
          StringConcatenation _builder_27 = new StringConcatenation();
          _builder_27.append("int t");
          _builder_27.append(pos, "");
          _builder_27.append("=t");
          _builder_27.append(b, "");
          _builder_27.append(" >>> t");
          _builder_27.append(a, "");
          _builder_27.append(";");
          sb.append(_builder_27);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sra)) {
          _matched=true;
          StringConcatenation _builder_28 = new StringConcatenation();
          _builder_28.append("int t");
          _builder_28.append(pos, "");
          _builder_28.append("=t");
          _builder_28.append(b, "");
          _builder_28.append(" >> t");
          _builder_28.append(a, "");
          _builder_28.append(";");
          sb.append(_builder_28);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.eq)) {
          _matched=true;
          StringConcatenation _builder_29 = new StringConcatenation();
          _builder_29.append("bool t");
          _builder_29.append(pos, "");
          _builder_29.append("=t");
          _builder_29.append(b, "");
          _builder_29.append(" == t");
          _builder_29.append(a, "");
          _builder_29.append(";");
          sb.append(_builder_29);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.not_eq)) {
          _matched=true;
          StringConcatenation _builder_30 = new StringConcatenation();
          _builder_30.append("bool t");
          _builder_30.append(pos, "");
          _builder_30.append("=t");
          _builder_30.append(b, "");
          _builder_30.append(" != t");
          _builder_30.append(a, "");
          _builder_30.append(";");
          sb.append(_builder_30);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less)) {
          _matched=true;
          StringConcatenation _builder_31 = new StringConcatenation();
          _builder_31.append("bool t");
          _builder_31.append(pos, "");
          _builder_31.append("=t");
          _builder_31.append(b, "");
          _builder_31.append(" < t");
          _builder_31.append(a, "");
          _builder_31.append(";");
          sb.append(_builder_31);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less_eq)) {
          _matched=true;
          StringConcatenation _builder_32 = new StringConcatenation();
          _builder_32.append("bool t");
          _builder_32.append(pos, "");
          _builder_32.append("=t");
          _builder_32.append(b, "");
          _builder_32.append(" <= t");
          _builder_32.append(a, "");
          _builder_32.append(";");
          sb.append(_builder_32);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater)) {
          _matched=true;
          StringConcatenation _builder_33 = new StringConcatenation();
          _builder_33.append("bool t");
          _builder_33.append(pos, "");
          _builder_33.append("=t");
          _builder_33.append(b, "");
          _builder_33.append(" > t");
          _builder_33.append(a, "");
          _builder_33.append(";");
          sb.append(_builder_33);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater_eq)) {
          _matched=true;
          StringConcatenation _builder_34 = new StringConcatenation();
          _builder_34.append("bool t");
          _builder_34.append(pos, "");
          _builder_34.append("=t");
          _builder_34.append(b, "");
          _builder_34.append(" >= t");
          _builder_34.append(a, "");
          _builder_34.append(";");
          sb.append(_builder_34);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isRisingEdge)) {
          _matched=true;
          StringConcatenation _builder_35 = new StringConcatenation();
          InternalInformation _asInternal_3 = this.asInternal(inst.arg1);
          String _javaName_1 = this.javaName(_asInternal_3.info, false);
          _builder_35.append(_javaName_1, "");
          _builder_35.append("_update=updateStamp;");
          sb.append(_builder_35);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isFallingEdge)) {
          _matched=true;
          StringConcatenation _builder_36 = new StringConcatenation();
          InternalInformation _asInternal_4 = this.asInternal(inst.arg1);
          String _javaName_2 = this.javaName(_asInternal_4.info, false);
          _builder_36.append(_javaName_2, "");
          _builder_36.append("_update=updateStamp;");
          sb.append(_builder_36);
        }
      }
      StringConcatenation _builder_37 = new StringConcatenation();
      _builder_37.append("//");
      _builder_37.append(inst, "");
      _builder_37.newLineIfNotEmpty();
      StringBuilder _append = sb.append(_builder_37);
      _xblockexpression = (_append);
    }
    return _xblockexpression;
  }
  
  public CharSequence constant(final int id, final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    BigInteger _get = f.constants[id];
    CharSequence _hexString = this.toHexString(_get);
    _builder.append(_hexString, "");
    return _builder;
  }
  
  public int calcSize(final VariableInformation info) {
    int size = 1;
    for (final int d : info.dimensions) {
      int _multiply = (size * d);
      size = _multiply;
    }
    return size;
  }
  
  public String getJavaType(final InternalInformation ii) {
    return this.getJavaType(ii.info, false);
  }
  
  public String getJavaType(final VariableInformation information, final boolean withArray) {
    String jt = "int";
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      jt = "bool";
    }
    boolean _and = false;
    int _length = information.dimensions.length;
    boolean _notEquals = (_length != 0);
    if (!_notEquals) {
      _and = false;
    } else {
      _and = (_notEquals && withArray);
    }
    if (_and) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("List<");
      _builder.append(jt, "");
      _builder.append(">");
      return _builder.toString();
    }
    return jt;
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
      String _plus = ("_" + res);
      return (_plus + "_prev");
    }
    return ("_" + res);
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
        _builder.append("int ");
        String _javaName = this.javaName(info, false);
        _builder.append(_javaName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    String _javaType = this.getJavaType(info, true);
    _builder.append(_javaType, "");
    _builder.append(" ");
    String _javaName_1 = this.javaName(info, false);
    _builder.append(_javaName_1, "");
    _builder.append("=");
    CharSequence _initValue = this.initValue(info);
    _builder.append(_initValue, "");
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
        String _javaType_1 = this.getJavaType(info, true);
        _builder.append(_javaType_1, "");
        _builder.append(" ");
        String _javaName_2 = this.javaName(info, true);
        _builder.append(_javaName_2, "");
        _builder.append("=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        String _javaType_2 = this.getJavaType(info, true);
        _builder.append(_javaType_2, "");
        _builder.append(" ");
        String _javaName_3 = this.javaName(info, false);
        _builder.append(_javaName_3, "");
        _builder.append("$reg=");
        CharSequence _initValue_1 = this.initValue(info);
        _builder.append(_initValue_1, "");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence initValue(final VariableInformation info) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isPredicate = this.isPredicate(info);
      if (_isPredicate) {
        _builder.append("false");
      } else {
        int _length = info.dimensions.length;
        boolean _notEquals = (_length != 0);
        if (_notEquals) {
          _builder.append("new ");
          String _javaType = this.getJavaType(info, true);
          _builder.append(_javaType, "");
          _builder.append("(");
          int _calcSize = this.calcSize(info);
          _builder.append(_calcSize, "");
          _builder.append(")");
        } else {
          _builder.append("0");
        }
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  public boolean isPredicate(final VariableInformation info) {
    boolean _startsWith = info.name.startsWith(InternalInformation.PRED_PREFIX);
    return _startsWith;
  }
  
  public CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import \'dart:collection\';");
    _builder.newLine();
    _builder.append("import \'simulation_comm.dart\';");
    _builder.newLine();
    return _builder;
  }
}
