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
import org.pshdl.interpreter.utils.Instruction;

@SuppressWarnings("all")
public class CCompiler {
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
  
  private boolean debug;
  
  private boolean hasClock;
  
  public CCompiler(final ExecutableModel em, final boolean includeDebug) {
    this.em = em;
    this.debug = includeDebug;
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
  
  public static String doCompile(final ExecutableModel em, final boolean includeDebugListener) {
    CCompiler _cCompiler = new CCompiler(em, includeDebugListener);
    CharSequence _compile = _cCompiler.compile();
    return _compile.toString();
  }
  
  public InternalInformation asInternal(final int id) {
    return this.em.internals[id];
  }
  
  public CharSequence compile() {
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
      _builder.newLine();
      {
        if (this.hasClock) {
          _builder.append("\t");
          _builder.append("typedef struct regUpdate {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("int internal;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("int offset;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("} regUpdate_t;");
          _builder.newLine();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("regUpdate_t regUpdates[20];");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("int regUpdatePos=0;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("bool disableEdges;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("bool disabledRegOutputlogic;");
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
      _builder.append("int epsCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int deltaCycle=0;");
      _builder.newLine();
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
          _builder.append("bool skipEdge(uint64_t local) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("uint64_t dc = local >> 16l;");
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
          _builder.append("if ((dc == deltaCycle) && ((local & 0xFFFF) == epsCycle))");
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
      {
        if (this.hasClock) {
          _builder.append("\t");
          CharSequence _copyRegs = this.copyRegs();
          _builder.append(_copyRegs, "	");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("void run(){");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("deltaCycle++;");
      _builder.newLine();
      {
        if (this.hasClock) {
          _builder.append("\t\t");
          _builder.append("epsCycle=0;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("do {");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("\t");
          _builder.append("regUpdatePos=0;");
          _builder.newLine();
        }
      }
      {
        if (this.debug) {
          _builder.append("\t\t");
          _builder.append("if (listener!=null)");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("\t");
          _builder.append("listener.startCycle(deltaCycle, epsCycle, this);");
          _builder.newLine();
        }
      }
      {
        for(final Frame f_1 : this.em.frames) {
          {
            boolean _and = false;
            boolean _and_1 = false;
            boolean _and_2 = false;
            int _minus_1 = (-1);
            boolean _tripleEquals = (Integer.valueOf(f_1.edgeNegDepRes) == Integer.valueOf(_minus_1));
            if (!_tripleEquals) {
              _and_2 = false;
            } else {
              int _minus_2 = (-1);
              boolean _tripleEquals_1 = (Integer.valueOf(f_1.edgePosDepRes) == Integer.valueOf(_minus_2));
              _and_2 = (_tripleEquals && _tripleEquals_1);
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              int _length = f_1.predNegDepRes.length;
              boolean _tripleEquals_2 = (Integer.valueOf(_length) == Integer.valueOf(0));
              _and_1 = (_and_2 && _tripleEquals_2);
            }
            if (!_and_1) {
              _and = false;
            } else {
              int _length_1 = f_1.predPosDepRes.length;
              boolean _tripleEquals_3 = (Integer.valueOf(_length_1) == Integer.valueOf(0));
              _and = (_and_1 && _tripleEquals_3);
            }
            if (_and) {
              _builder.append("\t\t");
              _builder.append("frame");
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
                  CharSequence _createboolPred = this.createboolPred(p, handled);
                  _builder.append(_createboolPred, "		");
                  _builder.newLineIfNotEmpty();
                }
              }
              {
                for(final int p_1 : f_1.predPosDepRes) {
                  _builder.append("\t\t");
                  CharSequence _createboolPred_1 = this.createboolPred(p_1, handled);
                  _builder.append(_createboolPred_1, "		");
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
              _builder.append("frame");
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
          _builder.append("updateRegs();");
          _builder.newLine();
          {
            if (this.debug) {
              _builder.append("\t\t");
              _builder.append("if (listener!=null && !regUpdates.isEmpty())");
              _builder.newLine();
              _builder.append("\t\t");
              _builder.append("\t");
              _builder.append("listener.copyingRegisterValues(this);");
              _builder.newLine();
            }
          }
          _builder.append("\t\t");
          _builder.append("epsCycle++;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("} while (regUpdatePos!=0 && !disabledRegOutputlogic);");
          _builder.newLine();
        }
      }
      {
        Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
        final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
            public Boolean apply(final VariableInformation it) {
              Boolean _get = CCompiler.this.prevMap.get(it.name);
              boolean _notEquals = (!Objects.equal(_get, null));
              return Boolean.valueOf(_notEquals);
            }
          };
        Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull_1, _function);
        for(final VariableInformation v_1 : _filter) {
          _builder.append("\t\t");
          String _copyPrev = this.copyPrev(v_1);
          _builder.append(_copyPrev, "		");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (this.debug) {
          _builder.append("\t\t");
          _builder.append("if (listener!=null)");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("\t");
          _builder.append("listener.doneCycle(deltaCycle, this);");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
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
  
  public CharSequence createboolPred(final int id, final Set<Integer> handled) {
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
      _builder_1.append("uint64_t up");
      _builder_1.append(id, "");
      _builder_1.append("=");
      InternalInformation _asInternal_1 = this.asInternal(id);
      String _javaName = this.javaName(_asInternal_1.info, false);
      _builder_1.append(_javaName, "");
      _builder_1.append("_update;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if ((up");
      _builder_1.append(id, "");
      _builder_1.append(">>16 != deltaCycle) || ((up");
      _builder_1.append(id, "");
      _builder_1.append("&0xFFFF) != epsCycle)){");
      _builder_1.newLineIfNotEmpty();
      {
        if (this.debug) {
          _builder_1.append("\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append(" \t");
          _builder_1.append("listener.skippingPredicateNotFresh(-1, em.internals[");
          _builder_1.append(id, "	 	");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      _builder_1.append("if (!disableEdges){");
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
      {
        if (this.debug) {
          _builder_1.append("\t\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t\t");
          _builder_1.append("\t");
          _builder_1.append("listener.skippingNotAnEdge(-1, em.internals[");
          _builder_1.append(id, "			");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      {
        if (this.debug) {
          _builder_1.append("\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append("\t");
          _builder_1.append("listener.skippingHandledEdge(-1, em.internals[");
          _builder_1.append(id, "		");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      _builder_1.append("if (!disableEdges){");
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
      {
        if (this.debug) {
          _builder_1.append("\t\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t\t");
          _builder_1.append(" \t");
          _builder_1.append("listener.skippingNotAnEdge(-1, em.internals[");
          _builder_1.append(id, "		 	");
          _builder_1.append("], false, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      {
        if (this.debug) {
          _builder_1.append("\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append(" \t");
          _builder_1.append("listener.skippingHandledEdge(-1, em.internals[");
          _builder_1.append(id, "	 	");
          _builder_1.append("], false, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
    long _doubleLessThan = (1l << width);
    final long mask = (_doubleLessThan - 1);
    boolean _equals = (width == 64);
    if (_equals) {
      return "0xFFFFFFFFFFFFFFFFl";
    }
    return this.toHexString(mask);
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
    _builder.append("void updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (int i=0;i<regUpdatePos; i++) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("regUpdate_t reg=regUpdates[i];");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (reg.internal) {");
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
        {
          int _length_1 = info.arrayIdx.length;
          int _length_2 = info.info.dimensions.length;
          boolean _tripleEquals = (Integer.valueOf(_length_1) == Integer.valueOf(_length_2));
          if (_tripleEquals) {
            {
              if (this.debug) {
                _builder_2.append("if (listener!=null)");
                _builder_2.newLine();
                _builder_2.append("\t");
                _builder_2.append("listener.loadingInternal(");
                _builder_2.append(frameID, "	");
                _builder_2.append(", em.internals[");
                Integer _get = this.intIdx.get(info.fullName);
                _builder_2.append(_get, "	");
                _builder_2.append("], ");
                {
                  if (info.isPred) {
                    _builder_2.append(varName, "	");
                    _builder_2.append("?BigInteger.ONE:BigInteger.ZERO");
                  } else {
                    _builder_2.append("BigInteger.valueOf(");
                    _builder_2.append(varName, "	");
                    _builder_2.append(")");
                  }
                }
                _builder_2.append(", null);");
                _builder_2.newLineIfNotEmpty();
              }
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
        {
          int _length_3 = info.arrayIdx.length;
          int _length_4 = info.info.dimensions.length;
          boolean _tripleEquals_1 = (Integer.valueOf(_length_3) == Integer.valueOf(_length_4));
          if (_tripleEquals_1) {
            {
              if (this.debug) {
                _builder_3.append("if (listener!=null)");
                _builder_3.newLine();
                _builder_3.append("\t");
                _builder_3.append("listener.loadingInternal(");
                _builder_3.append(frameID, "	");
                _builder_3.append(", em.internals[");
                Integer _get_1 = this.intIdx.get(info.fullName);
                _builder_3.append(_get_1, "	");
                _builder_3.append("], ");
                {
                  if (info.isPred) {
                    _builder_3.append(varName, "	");
                    _builder_3.append("?BigInteger.ONE:BigInteger.ZERO");
                  } else {
                    _builder_3.append("BigInteger.valueOf(");
                    _builder_3.append(varName, "	");
                    _builder_3.append(")");
                  }
                }
                _builder_3.append(", null);");
                _builder_3.newLineIfNotEmpty();
              }
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
      String regSuffix = "";
      if (info.isShadowReg) {
        String _plus = ("$reg" + fixedAccess);
        fixedAccess = _plus;
        regSuffix = "$reg";
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          boolean _equals = (info.actualWidth == info.info.width);
          if (_equals) {
            {
              if (info.isShadowReg) {
                String _javaType = this.getJavaType(info.info);
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
            String _javaType_1 = this.getJavaType(info.info);
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
            _builder_2.append("static regUpdate_t reg;");
            _builder_2.newLine();
            _builder_2.append("if (current!=");
            _builder_2.append(value, "");
            _builder_2.append("){");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            _builder_2.append("reg.internal=");
            Integer _get = this.varIdx.get(info.info.name);
            _builder_2.append(_get, "	");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            _builder_2.append("reg.offset=");
            _builder_2.append(off, "	");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("\t");
            _builder_2.append("regUpdates[regUpdatePos++]=reg;");
            _builder_2.newLine();
            _builder_2.append("}");
            _builder_2.newLine();
          }
        }
        {
          if (info.isPred) {
            String _javaName_4 = this.javaName(info.info, false);
            _builder_2.append(_javaName_4, "");
            _builder_2.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_2.newLineIfNotEmpty();
        _xifexpression_1 = _builder_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("int offset=(int)");
        _builder_3.append(varAccess, "");
        _builder_3.append(";");
        _builder_3.newLineIfNotEmpty();
        {
          boolean _equals_1 = (info.actualWidth == info.info.width);
          if (_equals_1) {
            {
              if (info.isShadowReg) {
                String _javaType_2 = this.getJavaType(info.info);
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
            String _javaType_3 = this.getJavaType(info.info);
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
            _builder_3.append("static regUpdate_t reg;");
            _builder_3.newLine();
            _builder_3.append("if (current!=");
            _builder_3.append(value, "");
            _builder_3.append("){");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("reg.internal=");
            Integer _get_1 = this.varIdx.get(info.info.name);
            _builder_3.append(_get_1, "	");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("reg.offset=");
            _builder_3.append(off, "	");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("regUpdates[regUpdatePos++]=reg;");
            _builder_3.newLine();
            _builder_3.append("}");
            _builder_3.newLine();
          }
        }
        {
          if (info.isPred) {
            String _javaName_9 = this.javaName(info.info, false);
            _builder_3.append(_javaName_9, "");
            _builder_3.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
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
    _builder.append("void frame");
    _builder.append(frame.uniqueID, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    {
      if (this.debug) {
        _builder.append("\t");
        _builder.append("if (listener!=null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("listener.startFrame(");
        _builder.append(frame.uniqueID, "		");
        _builder.append(", deltaCycle, epsCycle, null);");
        _builder.newLineIfNotEmpty();
      }
    }
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
    {
      if (this.debug) {
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
          InternalInformation _asInternal_2 = this.asInternal(frame.outputId);
          if (_asInternal_2.isPred) {
            _builder_2.append("?1:0");
          }
        }
        _builder_2.append("), null);");
        _builder_2.newLineIfNotEmpty();
      }
    }
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
        if (Objects.equal(_switchValue,Instruction.writeInternal)) {
          _matched=true;
          InternalInformation _asInternal = this.asInternal(inst.arg1);
          final VariableInformation info = _asInternal.info;
          int _size = arr.size();
          InternalInformation _asInternal_1 = this.asInternal(inst.arg1);
          int _length = _asInternal_1.info.dimensions.length;
          boolean _lessThan = (_size < _length);
          if (_lessThan) {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append("memset(");
            InternalInformation _asInternal_2 = this.asInternal(inst.arg1);
            String _javaName = this.javaName(_asInternal_2, false);
            _builder_1.append(_javaName, "");
            _builder_1.append(", t");
            _builder_1.append(a, "");
            _builder_1.append(", ");
            int _totalSize = this.getTotalSize(info);
            _builder_1.append(_totalSize, "");
            _builder_1.append(");");
            sb.append(_builder_1);
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            InternalInformation _asInternal_3 = this.asInternal(inst.arg1);
            String _javaName_1 = this.javaName(_asInternal_3, false);
            _builder_2.append(_javaName_1, "");
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
          _builder_3.append("uint64_t t");
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
          _builder_4.append("uint64_t t");
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
          _builder_5.append("uint64_t t");
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
          _builder_6.append("uint64_t t");
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
          long _doubleLessThan = (1l << _plus);
          final long mask = (_doubleLessThan - 1);
          StringConcatenation _builder_7 = new StringConcatenation();
          _builder_7.append("uint64_t t");
          _builder_7.append(pos, "");
          _builder_7.append("=(t");
          _builder_7.append(a, "");
          _builder_7.append(" >> ");
          _builder_7.append(lowBit, "");
          _builder_7.append(") & ");
          CharSequence _hexString = this.toHexString(mask);
          _builder_7.append(_hexString, "");
          _builder_7.append(";");
          sb.append(_builder_7);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_int)) {
          _matched=true;
          boolean _notEquals = (inst.arg1 != 64);
          if (_notEquals) {
            int currentSize = inst.arg2;
            int targetSize = inst.arg1;
            long _doubleLessThan_1 = (1l << targetSize);
            long _minus_1 = (_doubleLessThan_1 - 1);
            long orMask = (~_minus_1);
            StringConcatenation _builder_8 = new StringConcatenation();
            _builder_8.append("//Target size ");
            _builder_8.append(targetSize, "");
            _builder_8.append(" currentSize ");
            _builder_8.append(currentSize, "");
            _builder_8.newLineIfNotEmpty();
            _builder_8.append("int64_t c");
            _builder_8.append(pos, "");
            _builder_8.append("=t");
            _builder_8.append(a, "");
            _builder_8.append(" << ");
            int _minus_2 = (64 - currentSize);
            _builder_8.append(_minus_2, "");
            _builder_8.append(";");
            _builder_8.newLineIfNotEmpty();
            sb.append(_builder_8);
            boolean _lessThan_1 = (targetSize < currentSize);
            if (_lessThan_1) {
              StringConcatenation _builder_9 = new StringConcatenation();
              _builder_9.append("uint64_t t");
              _builder_9.append(pos, "");
              _builder_9.append("=(c");
              _builder_9.append(pos, "");
              _builder_9.append(" >> ");
              int _minus_3 = (64 - currentSize);
              _builder_9.append(_minus_3, "");
              _builder_9.append(") | ");
              CharSequence _hexString_1 = this.toHexString(orMask);
              _builder_9.append(_hexString_1, "");
              _builder_9.append(";");
              _builder_9.newLineIfNotEmpty();
              sb.append(_builder_9);
            } else {
              StringConcatenation _builder_10 = new StringConcatenation();
              _builder_10.append("uint64_t t");
              _builder_10.append(pos, "");
              _builder_10.append("=c");
              _builder_10.append(pos, "");
              _builder_10.append(" >> ");
              int _minus_4 = (64 - currentSize);
              _builder_10.append(_minus_4, "");
              _builder_10.append(";");
              _builder_10.newLineIfNotEmpty();
              sb.append(_builder_10);
            }
          } else {
            StringConcatenation _builder_11 = new StringConcatenation();
            _builder_11.append("uint64_t t");
            _builder_11.append(pos, "");
            _builder_11.append("=t");
            _builder_11.append(a, "");
            _builder_11.append(";");
            sb.append(_builder_11);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_uint)) {
          _matched=true;
          boolean _notEquals_1 = (inst.arg1 != 64);
          if (_notEquals_1) {
            StringConcatenation _builder_12 = new StringConcatenation();
            _builder_12.append("uint64_t t");
            _builder_12.append(pos, "");
            _builder_12.append("=t");
            _builder_12.append(a, "");
            _builder_12.append(" & ");
            CharSequence _asMask = this.asMask(inst.arg1);
            _builder_12.append(_asMask, "");
            _builder_12.append(";");
            sb.append(_builder_12);
          } else {
            StringConcatenation _builder_13 = new StringConcatenation();
            _builder_13.append("uint64_t t");
            _builder_13.append(pos, "");
            _builder_13.append("=t");
            _builder_13.append(a, "");
            _builder_13.append(";");
            sb.append(_builder_13);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiNeg)) {
          _matched=true;
          StringConcatenation _builder_14 = new StringConcatenation();
          _builder_14.append("bool t");
          _builder_14.append(pos, "");
          _builder_14.append("=!t");
          _builder_14.append(a, "");
          _builder_14.append(";");
          sb.append(_builder_14);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiAnd)) {
          _matched=true;
          StringConcatenation _builder_15 = new StringConcatenation();
          _builder_15.append("bool t");
          _builder_15.append(pos, "");
          _builder_15.append("=t");
          _builder_15.append(a, "");
          _builder_15.append(" && t");
          _builder_15.append(b, "");
          _builder_15.append(";");
          sb.append(_builder_15);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiOr)) {
          _matched=true;
          StringConcatenation _builder_16 = new StringConcatenation();
          _builder_16.append("bool t");
          _builder_16.append(pos, "");
          _builder_16.append("=t");
          _builder_16.append(a, "");
          _builder_16.append(" || t");
          _builder_16.append(b, "");
          _builder_16.append(";");
          sb.append(_builder_16);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const0)) {
          _matched=true;
          StringConcatenation _builder_17 = new StringConcatenation();
          _builder_17.append("uint64_t t");
          _builder_17.append(pos, "");
          _builder_17.append("=0;");
          sb.append(_builder_17);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const1)) {
          _matched=true;
          StringConcatenation _builder_18 = new StringConcatenation();
          _builder_18.append("uint64_t t");
          _builder_18.append(pos, "");
          _builder_18.append("=1;");
          sb.append(_builder_18);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const2)) {
          _matched=true;
          StringConcatenation _builder_19 = new StringConcatenation();
          _builder_19.append("uint64_t t");
          _builder_19.append(pos, "");
          _builder_19.append("=2;");
          sb.append(_builder_19);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.constAll1)) {
          _matched=true;
          StringConcatenation _builder_20 = new StringConcatenation();
          _builder_20.append("uint64_t t");
          _builder_20.append(pos, "");
          _builder_20.append("=");
          CharSequence _asMask_1 = this.asMask(inst.arg1);
          _builder_20.append(_asMask_1, "");
          _builder_20.append(";");
          sb.append(_builder_20);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.concat)) {
          _matched=true;
          StringConcatenation _builder_21 = new StringConcatenation();
          _builder_21.append("uint64_t t");
          _builder_21.append(pos, "");
          _builder_21.append("=(t");
          _builder_21.append(b, "");
          _builder_21.append(" << ");
          _builder_21.append(inst.arg2, "");
          _builder_21.append(") | t");
          _builder_21.append(a, "");
          _builder_21.append(";");
          sb.append(_builder_21);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadConstant)) {
          _matched=true;
          StringConcatenation _builder_22 = new StringConcatenation();
          _builder_22.append("uint64_t t");
          _builder_22.append(pos, "");
          _builder_22.append("=");
          CharSequence _constant = this.constant(inst.arg1, f);
          _builder_22.append(_constant, "");
          _builder_22.append(";");
          sb.append(_builder_22);
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
          StringConcatenation _builder_23 = new StringConcatenation();
          _builder_23.append("uint64_t t");
          _builder_23.append(pos, "");
          _builder_23.append("=t");
          _builder_23.append(b, "");
          _builder_23.append(" & t");
          _builder_23.append(a, "");
          _builder_23.append(";");
          sb.append(_builder_23);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.or)) {
          _matched=true;
          StringConcatenation _builder_24 = new StringConcatenation();
          _builder_24.append("uint64_t t");
          _builder_24.append(pos, "");
          _builder_24.append("=t");
          _builder_24.append(b, "");
          _builder_24.append(" | t");
          _builder_24.append(a, "");
          _builder_24.append(";");
          sb.append(_builder_24);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.xor)) {
          _matched=true;
          StringConcatenation _builder_25 = new StringConcatenation();
          _builder_25.append("uint64_t t");
          _builder_25.append(pos, "");
          _builder_25.append("=t");
          _builder_25.append(b, "");
          _builder_25.append(" ^ t");
          _builder_25.append(a, "");
          _builder_25.append(";");
          sb.append(_builder_25);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.plus)) {
          _matched=true;
          StringConcatenation _builder_26 = new StringConcatenation();
          _builder_26.append("uint64_t t");
          _builder_26.append(pos, "");
          _builder_26.append("=t");
          _builder_26.append(b, "");
          _builder_26.append(" + t");
          _builder_26.append(a, "");
          _builder_26.append(";");
          sb.append(_builder_26);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.minus)) {
          _matched=true;
          StringConcatenation _builder_27 = new StringConcatenation();
          _builder_27.append("uint64_t t");
          _builder_27.append(pos, "");
          _builder_27.append("=t");
          _builder_27.append(b, "");
          _builder_27.append(" - t");
          _builder_27.append(a, "");
          _builder_27.append(";");
          sb.append(_builder_27);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.mul)) {
          _matched=true;
          StringConcatenation _builder_28 = new StringConcatenation();
          _builder_28.append("uint64_t t");
          _builder_28.append(pos, "");
          _builder_28.append("=t");
          _builder_28.append(b, "");
          _builder_28.append(" * t");
          _builder_28.append(a, "");
          _builder_28.append(";");
          sb.append(_builder_28);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.div)) {
          _matched=true;
          StringConcatenation _builder_29 = new StringConcatenation();
          _builder_29.append("uint64_t t");
          _builder_29.append(pos, "");
          _builder_29.append("=t");
          _builder_29.append(b, "");
          _builder_29.append(" / t");
          _builder_29.append(a, "");
          _builder_29.append(";");
          sb.append(_builder_29);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sll)) {
          _matched=true;
          StringConcatenation _builder_30 = new StringConcatenation();
          _builder_30.append("uint64_t t");
          _builder_30.append(pos, "");
          _builder_30.append("=t");
          _builder_30.append(b, "");
          _builder_30.append(" << t");
          _builder_30.append(a, "");
          _builder_30.append(";");
          sb.append(_builder_30);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.srl)) {
          _matched=true;
          StringConcatenation _builder_31 = new StringConcatenation();
          _builder_31.append("uint64_t t");
          _builder_31.append(pos, "");
          _builder_31.append("=t");
          _builder_31.append(b, "");
          _builder_31.append(" >> t");
          _builder_31.append(a, "");
          _builder_31.append(";");
          sb.append(_builder_31);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sra)) {
          _matched=true;
          StringConcatenation _builder_32 = new StringConcatenation();
          _builder_32.append("uint64_t t");
          _builder_32.append(pos, "");
          _builder_32.append("=((int64_t)t");
          _builder_32.append(b, "");
          _builder_32.append(") >> t");
          _builder_32.append(a, "");
          _builder_32.append(";");
          sb.append(_builder_32);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.eq)) {
          _matched=true;
          StringConcatenation _builder_33 = new StringConcatenation();
          _builder_33.append("bool t");
          _builder_33.append(pos, "");
          _builder_33.append("=t");
          _builder_33.append(b, "");
          _builder_33.append(" == t");
          _builder_33.append(a, "");
          _builder_33.append(";");
          sb.append(_builder_33);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.not_eq)) {
          _matched=true;
          StringConcatenation _builder_34 = new StringConcatenation();
          _builder_34.append("bool t");
          _builder_34.append(pos, "");
          _builder_34.append("=t");
          _builder_34.append(b, "");
          _builder_34.append(" != t");
          _builder_34.append(a, "");
          _builder_34.append(";");
          sb.append(_builder_34);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less)) {
          _matched=true;
          StringConcatenation _builder_35 = new StringConcatenation();
          _builder_35.append("bool t");
          _builder_35.append(pos, "");
          _builder_35.append("=t");
          _builder_35.append(b, "");
          _builder_35.append(" < t");
          _builder_35.append(a, "");
          _builder_35.append(";");
          sb.append(_builder_35);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less_eq)) {
          _matched=true;
          StringConcatenation _builder_36 = new StringConcatenation();
          _builder_36.append("bool t");
          _builder_36.append(pos, "");
          _builder_36.append("=t");
          _builder_36.append(b, "");
          _builder_36.append(" <= t");
          _builder_36.append(a, "");
          _builder_36.append(";");
          sb.append(_builder_36);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater)) {
          _matched=true;
          StringConcatenation _builder_37 = new StringConcatenation();
          _builder_37.append("bool t");
          _builder_37.append(pos, "");
          _builder_37.append("=t");
          _builder_37.append(b, "");
          _builder_37.append(" > t");
          _builder_37.append(a, "");
          _builder_37.append(";");
          sb.append(_builder_37);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater_eq)) {
          _matched=true;
          StringConcatenation _builder_38 = new StringConcatenation();
          _builder_38.append("bool t");
          _builder_38.append(pos, "");
          _builder_38.append("=t");
          _builder_38.append(b, "");
          _builder_38.append(" >= t");
          _builder_38.append(a, "");
          _builder_38.append(";");
          sb.append(_builder_38);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isRisingEdge)) {
          _matched=true;
          StringConcatenation _builder_39 = new StringConcatenation();
          InternalInformation _asInternal_4 = this.asInternal(inst.arg1);
          String _javaName_2 = this.javaName(_asInternal_4.info, false);
          _builder_39.append(_javaName_2, "");
          _builder_39.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          sb.append(_builder_39);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isFallingEdge)) {
          _matched=true;
          StringConcatenation _builder_40 = new StringConcatenation();
          InternalInformation _asInternal_5 = this.asInternal(inst.arg1);
          String _javaName_3 = this.javaName(_asInternal_5.info, false);
          _builder_40.append(_javaName_3, "");
          _builder_40.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          sb.append(_builder_40);
        }
      }
      StringConcatenation _builder_41 = new StringConcatenation();
      _builder_41.append("//");
      _builder_41.append(inst, "");
      _builder_41.newLineIfNotEmpty();
      StringBuilder _append = sb.append(_builder_41);
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
  
  public String getJavaType(final InternalInformation ii) {
    final String jt = this.getJavaType(ii.info);
    int _length = ii.arrayIdx.length;
    int _length_1 = ii.info.dimensions.length;
    boolean _notEquals = (_length != _length_1);
    if (_notEquals) {
      return (jt + "[]");
    }
    return jt;
  }
  
  public String getJavaType(final VariableInformation information) {
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      return "bool";
    }
    return "uint64_t";
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
    String res = _replaceAll_4.replaceAll("\\]", "");
    boolean _startsWith = res.startsWith("$");
    if (_startsWith) {
      String _substring = res.substring(1);
      res = _substring;
    }
    boolean _startsWith_1 = res.startsWith("#");
    if (_startsWith_1) {
      String _substring_1 = res.substring(1);
      res = _substring_1;
    }
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
        _builder.append("uint64_t ");
        String _javaName = this.javaName(info, false);
        _builder.append(_javaName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    String _javaType = this.getJavaType(info);
    _builder.append(_javaType, "");
    _builder.append(" ");
    String _javaName_1 = this.javaName(info, false);
    _builder.append(_javaName_1, "");
    {
      boolean _isEmpty = IterableExtensions.isEmpty(((Iterable<? extends Object>)Conversions.doWrapArray(info.dimensions)));
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("[");
        int _totalSize = this.getTotalSize(info);
        _builder.append(_totalSize, "");
        _builder.append("]");
      }
    }
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
        String _javaType_1 = this.getJavaType(info);
        _builder.append(_javaType_1, "");
        _builder.append(" ");
        String _javaName_2 = this.javaName(info, true);
        _builder.append(_javaName_2, "");
        {
          boolean _isEmpty_1 = IterableExtensions.isEmpty(((Iterable<? extends Object>)Conversions.doWrapArray(info.dimensions)));
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            _builder.append("[");
            int _totalSize_1 = this.getTotalSize(info);
            _builder.append(_totalSize_1, "");
            _builder.append("]");
          }
        }
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        String _javaType_2 = this.getJavaType(info);
        _builder.append(_javaType_2, "");
        _builder.append(" ");
        String _javaName_3 = this.javaName(info, false);
        _builder.append(_javaName_3, "");
        _builder.append("$reg");
        {
          boolean _isEmpty_2 = IterableExtensions.isEmpty(((Iterable<? extends Object>)Conversions.doWrapArray(info.dimensions)));
          boolean _not_2 = (!_isEmpty_2);
          if (_not_2) {
            _builder.append("[");
            int _totalSize_2 = this.getTotalSize(info);
            _builder.append(_totalSize_2, "");
            _builder.append("]");
          }
        }
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public int getTotalSize(final VariableInformation info) {
    int size = 1;
    for (final int d : info.dimensions) {
      int _multiply = (size * d);
      size = _multiply;
    }
    return size;
  }
  
  public boolean isPredicate(final VariableInformation info) {
    boolean _startsWith = info.name.startsWith(InternalInformation.PRED_PREFIX);
    return _startsWith;
  }
  
  public CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include <stdint.h>");
    _builder.newLine();
    _builder.append("#include <stdbool.h>");
    _builder.newLine();
    _builder.append("#include <string.h>");
    _builder.newLine();
    _builder.append("#include <stdio.h>");
    _builder.newLine();
    _builder.append("#include <time.h>");
    _builder.newLine();
    _builder.append("#include <stdlib.h>");
    _builder.newLine();
    return _builder;
  }
}
