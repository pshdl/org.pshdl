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

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.simulation.SimulationTransformationExtension;
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class CCompiler implements ITypeOuptutProvider {
  @Extension
  private CommonCompilerExtension cce;
  
  private final int bitWidth;
  
  private final boolean jsonDescription;
  
  public CCompiler() {
    this.bitWidth = 64;
    this.jsonDescription = false;
  }
  
  public CCompiler(final ExecutableModel em, final boolean jsonDescription) {
    this.bitWidth = 64;
    this.jsonDescription = jsonDescription;
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em, this.bitWidth);
    this.cce = _commonCompilerExtension;
  }
  
  public static String doCompileMainC(final ExecutableModel em, final boolean jsonDescription) {
    CCompiler _cCompiler = new CCompiler(em, jsonDescription);
    CharSequence _compile = _cCompiler.compile();
    return _compile.toString();
  }
  
  public CharSequence compile() {
    CharSequence _xblockexpression = null;
    {
      final Set<Integer> handled = new HashSet<Integer>();
      final Set<Integer> handledPosEdge = new HashSet<Integer>();
      final Set<Integer> handledNegEdge = new HashSet<Integer>();
      handled.add(Integer.valueOf((-1)));
      handledPosEdge.add(Integer.valueOf((-1)));
      handledNegEdge.add(Integer.valueOf((-1)));
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _imports = this.getImports();
      _builder.append(_imports, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      {
        if (this.cce.hasClock) {
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
          _builder.append("regUpdate_t regUpdates[");
          int _maxRegUpdates = this.cce.maxRegUpdates(this.cce.em);
          _builder.append(_maxRegUpdates, "\t");
          _builder.append("];");
          _builder.newLineIfNotEmpty();
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
        Iterable<VariableInformation> _excludeNull = this.cce.excludeNull(this.cce.em.variables);
        for(final VariableInformation v : _excludeNull) {
          _builder.append("\t");
          Boolean _get = this.cce.prevMap.get(v.name);
          CharSequence _decl = this.decl(v, _get);
          _builder.append(_decl, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("int epsCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int deltaCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      {
        for(final Frame f : this.cce.em.frames) {
          _builder.append("\t");
          CharSequence _method = this.method(f);
          _builder.append(_method, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (this.cce.hasClock) {
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
        if (this.cce.hasClock) {
          _builder.append("\t");
          CharSequence _copyRegs = this.copyRegs();
          _builder.append(_copyRegs, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("void pshdl_sim_run(){");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("deltaCycle++;");
      _builder.newLine();
      {
        if (this.cce.hasClock) {
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
        for(final Frame f_1 : this.cce.em.frames) {
          {
            boolean _and = false;
            boolean _and_1 = false;
            if (!((f_1.edgeNegDepRes == (-1)) && (f_1.edgePosDepRes == (-1)))) {
              _and_1 = false;
            } else {
              boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f_1.predNegDepRes)));
              _and_1 = _isNullOrEmpty;
            }
            if (!_and_1) {
              _and = false;
            } else {
              boolean _isNullOrEmpty_1 = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f_1.predPosDepRes)));
              _and = _isNullOrEmpty_1;
            }
            if (_and) {
              _builder.append("\t\t");
              CharSequence _frameName = this.cce.getFrameName(f_1);
              _builder.append(_frameName, "\t\t");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t\t");
              CharSequence _createNegEdge = this.createNegEdge(f_1.edgeNegDepRes, handledPosEdge);
              _builder.append(_createNegEdge, "\t\t");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              CharSequence _createPosEdge = this.createPosEdge(f_1.edgePosDepRes, handledNegEdge);
              _builder.append(_createPosEdge, "\t\t");
              _builder.newLineIfNotEmpty();
              {
                boolean _isNullOrEmpty_2 = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f_1.predNegDepRes)));
                boolean _not = (!_isNullOrEmpty_2);
                if (_not) {
                  {
                    for(final int p : f_1.predNegDepRes) {
                      _builder.append("\t\t");
                      CharSequence _createboolPred = this.createboolPred(p, handled);
                      _builder.append(_createboolPred, "\t\t");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
              {
                boolean _isNullOrEmpty_3 = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f_1.predPosDepRes)));
                boolean _not_1 = (!_isNullOrEmpty_3);
                if (_not_1) {
                  {
                    for(final int p_1 : f_1.predPosDepRes) {
                      _builder.append("\t\t");
                      CharSequence _createboolPred_1 = this.createboolPred(p_1, handled);
                      _builder.append(_createboolPred_1, "\t\t");
                      _builder.newLineIfNotEmpty();
                    }
                  }
                }
              }
              _builder.append("\t\t");
              _builder.append("if (");
              String _predicates = this.predicates(f_1);
              _builder.append(_predicates, "\t\t");
              _builder.append(")");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              _builder.append("\t");
              CharSequence _frameName_1 = this.cce.getFrameName(f_1);
              _builder.append(_frameName_1, "\t\t\t");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      {
        if (this.cce.hasClock) {
          _builder.append("\t\t");
          _builder.append("updateRegs();");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("epsCycle++;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("} while (regUpdatePos!=0 && !disabledRegOutputlogic);");
          _builder.newLine();
        }
      }
      {
        Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
        final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
          public Boolean apply(final VariableInformation it) {
            Boolean _get = CCompiler.this.cce.prevMap.get(it.name);
            return Boolean.valueOf((_get != null));
          }
        };
        Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull_1, _function);
        for(final VariableInformation v_1 : _filter) {
          _builder.append("\t\t");
          String _copyPrev = this.copyPrev(v_1);
          _builder.append(_copyPrev, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      CharSequence _helperMethods = this.helperMethods();
      _builder.append(_helperMethods, "");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence helperMethods() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void pshdl_sim_setInput(int idx, long value, ...) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("va_list va_arrayIdx;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("(void)va_arrayIdx;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v : _excludeNull) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get = this.cce.varIdx.get(v.name);
        _builder.append(_get, "\t\t");
        _builder.append(": ");
        _builder.newLineIfNotEmpty();
        {
          if ((v.width != this.bitWidth)) {
            _builder.append("\t\t");
            _builder.append("\t");
            {
              boolean _tripleEquals = (v.type == VariableInformation.Type.INT);
              if (_tripleEquals) {
                _builder.append("value=(value<<");
                _builder.append((this.bitWidth - v.width), "\t\t\t");
                _builder.append(")>>");
                _builder.append((this.bitWidth - v.width), "\t\t\t");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("\t");
              } else {
                boolean _isPredicate = this.cce.isPredicate(v);
                boolean _not = (!_isPredicate);
                if (_not) {
                  _builder.append("value&=");
                  CharSequence _asMaskL = this.cce.asMaskL(v.width);
                  _builder.append(_asMaskL, "\t\t\t");
                  _builder.append(";");
                }
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
        {
          int _length = v.dimensions.length;
          boolean _equals = (_length == 0);
          if (_equals) {
            _builder.append("\t\t");
            _builder.append("\t");
            String _idName = this.cce.idName(v, false, false);
            _builder.append(_idName, "\t\t\t");
            _builder.append("=value");
            {
              boolean _isPredicate_1 = this.cce.isPredicate(v);
              if (_isPredicate_1) {
                _builder.append("==0?false:true");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("va_start(va_arrayIdx, value);");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("\t");
            String _idName_1 = this.cce.idName(v, false, false);
            _builder.append(_idName_1, "\t\t\t");
            _builder.append("[");
            StringBuilder _arrayVarArgAccessArrIdx = this.arrayVarArgAccessArrIdx(v);
            _builder.append(_arrayVarArgAccessArrIdx, "\t\t\t");
            _builder.append("]=value;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("va_end(va_arrayIdx);");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("break;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("char* pshdl_sim_getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v_1 : _excludeNull_1) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get_1 = this.cce.varIdx.get(v_1.name);
        _builder.append(_get_1, "\t\t");
        _builder.append(": return \"");
        _builder.append(v_1.name, "\t\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    {
      if (this.jsonDescription) {
        _builder.append("static char* jsonDesc=\"");
        String _jSONDescription = this.cce.getJSONDescription();
        _builder.append(_jSONDescription, "");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
        _builder.append("char* pshdl_sim_getJsonDesc(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return jsonDesc;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("int pshdl_sim_getDeltaCycle(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return deltaCycle;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("int pshdl_sim_getVarCount(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        int _size = this.cce.varIdx.size();
        _builder.append(_size, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.append("void pshdl_sim_setDisableEdge(bool enable){");
        _builder.newLine();
        {
          if (this.cce.hasClock) {
            _builder.append("\t");
            _builder.append("disableEdges=enable;");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.append("void pshdl_sim_setDisabledRegOutputlogic(bool enable){");
        _builder.newLine();
        {
          if (this.cce.hasClock) {
            _builder.append("\t");
            _builder.append("disabledRegOutputlogic=enable;");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    CharSequence _uint_t = this.uint_t();
    _builder.append(_uint_t, "");
    _builder.append(" pshdl_sim_getOutput(int idx, ...) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("va_list va_arrayIdx;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("(void)va_arrayIdx;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_2 = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v_2 : _excludeNull_2) {
        {
          int _length_1 = v_2.dimensions.length;
          boolean _equals_1 = (_length_1 == 0);
          if (_equals_1) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_2 = this.cce.varIdx.get(v_2.name);
            _builder.append(_get_2, "\t\t");
            _builder.append(": return ");
            String _idName_2 = this.cce.idName(v_2, false, false);
            _builder.append(_idName_2, "\t\t");
            {
              boolean _isPredicate_2 = this.cce.isPredicate(v_2);
              if (_isPredicate_2) {
                _builder.append("?1:0");
              } else {
                if ((v_2.width != this.bitWidth)) {
                  _builder.append(" & ");
                  CharSequence _asMaskL_1 = this.cce.asMaskL(v_2.width);
                  _builder.append(_asMaskL_1, "\t\t");
                }
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_3 = this.cce.varIdx.get(v_2.name);
            _builder.append(_get_3, "\t\t");
            _builder.append(": {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("va_start(va_arrayIdx, idx);");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("\t");
            CharSequence _uint_t_1 = this.uint_t();
            _builder.append(_uint_t_1, "\t\t\t");
            _builder.append(" res=");
            String _idName_3 = this.cce.idName(v_2, false, false);
            _builder.append(_idName_3, "\t\t\t");
            _builder.append("[");
            StringBuilder _arrayVarArgAccessArrIdx_1 = this.arrayVarArgAccessArrIdx(v_2);
            _builder.append(_arrayVarArgAccessArrIdx_1, "\t\t\t");
            _builder.append("]");
            {
              boolean _and = false;
              if (!(v_2.width != this.bitWidth)) {
                _and = false;
              } else {
                boolean _isPredicate_3 = this.cce.isPredicate(v_2);
                boolean _not_1 = (!_isPredicate_3);
                _and = _not_1;
              }
              if (_and) {
                _builder.append(" & ");
                CharSequence _asMaskL_2 = this.cce.asMaskL(v_2.width);
                _builder.append(_asMaskL_2, "\t\t\t");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("va_end(va_arrayIdx);");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("return res;");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  public StringBuilder arrayVarArgAccessArrIdx(final VariableInformation v) {
    final StringBuilder varAccess = new StringBuilder();
    final ArrayList<Integer> dims = this.cce.dimsLastOne(v);
    int _length = v.dimensions.length;
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _length, true);
    for (final Integer i : _doubleDotLessThan) {
      {
        final Integer dim = dims.get((i).intValue());
        if (((i).intValue() != 0)) {
          varAccess.append("+");
        }
        if (((dim).intValue() != 1)) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("va_arg(va_arrayIdx, int) *");
          _builder.append(dim, "");
          varAccess.append(_builder);
        } else {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("va_arg(va_arrayIdx, int)");
          varAccess.append(_builder_1);
        }
      }
    }
    return varAccess;
  }
  
  public String predicates(final Frame f) {
    final StringBuilder sb = new StringBuilder();
    boolean first = true;
    if ((f.edgeNegDepRes != (-1))) {
      StringConcatenation _builder = new StringConcatenation();
      InternalInformation _asInternal = this.cce.asInternal(f.edgeNegDepRes);
      String _idName = this.cce.idName(_asInternal, false, false);
      _builder.append(_idName, "");
      _builder.append("_isFalling && !");
      InternalInformation _asInternal_1 = this.cce.asInternal(f.edgeNegDepRes);
      String _idName_1 = this.cce.idName(_asInternal_1, false, false);
      _builder.append(_idName_1, "");
      _builder.append("_fallingIsHandled");
      sb.append(_builder);
      first = false;
    }
    if ((f.edgePosDepRes != (-1))) {
      if ((!first)) {
        sb.append(" && ");
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      InternalInformation _asInternal_2 = this.cce.asInternal(f.edgePosDepRes);
      String _idName_2 = this.cce.idName(_asInternal_2, false, false);
      _builder_1.append(_idName_2, "");
      _builder_1.append("_isRising&& !");
      InternalInformation _asInternal_3 = this.cce.asInternal(f.edgePosDepRes);
      String _idName_3 = this.cce.idName(_asInternal_3, false, false);
      _builder_1.append(_idName_3, "");
      _builder_1.append("_risingIsHandled");
      sb.append(_builder_1);
      first = false;
    }
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f.predNegDepRes)));
    boolean _not = (!_isNullOrEmpty);
    if (_not) {
      for (final int p : f.predNegDepRes) {
        {
          if ((!first)) {
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
    }
    boolean _isNullOrEmpty_1 = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(f.predPosDepRes)));
    boolean _not_1 = (!_isNullOrEmpty_1);
    if (_not_1) {
      for (final int p_1 : f.predPosDepRes) {
        {
          if ((!first)) {
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
      InternalInformation _asInternal = this.cce.asInternal(id);
      CharSequence _ter = this.getter(_asInternal, false, id, (-1));
      _builder_1.append(_ter, "");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool p");
      _builder_1.append(id, "");
      _builder_1.append("_fresh=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("uint64_t up");
      _builder_1.append(id, "");
      _builder_1.append("=");
      InternalInformation _asInternal_1 = this.cce.asInternal(id);
      String _idName = this.cce.idName(_asInternal_1.info, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_update;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if ((up");
      _builder_1.append(id, "");
      _builder_1.append(">>16 != deltaCycle) || ((up");
      _builder_1.append(id, "");
      _builder_1.append("&0xFFFF) != epsCycle)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("p");
      _builder_1.append(id, "\t");
      _builder_1.append("_fresh=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = _builder_1;
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
      final InternalInformation internal = this.cce.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _idName = this.cce.idName(internal, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_isRising=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _idName_1 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_1, "");
      _builder_1.append("_risingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!disableEdges){");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal = this.cce.asInternal(id);
      CharSequence _ter = this.getter(_asInternal, false, id, (-1));
      _builder_1.append(_ter, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      InternalInformation _asInternal_1 = this.cce.asInternal(id);
      CharSequence _ter_1 = this.getter(_asInternal_1, true, id, (-1));
      _builder_1.append(_ter_1, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("if ((t");
      _builder_1.append(id, "\t");
      _builder_1.append("_prev!=0) || (t");
      _builder_1.append(id, "\t");
      _builder_1.append("!=1)) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      String _idName_2 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_2, "\t\t");
      _builder_1.append("_isRising=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("} else {");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal_2 = this.cce.asInternal(id);
      CharSequence _ter_2 = this.getter(_asInternal_2, false, id, (-1));
      _builder_1.append(_ter_2, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_3 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_3, "\t");
      _builder_1.append("_isRising=t");
      _builder_1.append(id, "\t");
      _builder_1.append("==1;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _idName_4 = this.cce.idName(internal.info, false, false);
      _builder_1.append(_idName_4, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_5 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_5, "\t");
      _builder_1.append("_risingIsHandled=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = _builder_1;
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
      final InternalInformation internal = this.cce.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _idName = this.cce.idName(internal, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_isFalling=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _idName_1 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_1, "");
      _builder_1.append("_fallingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!disableEdges){");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal = this.cce.asInternal(id);
      CharSequence _ter = this.getter(_asInternal, false, id, (-1));
      _builder_1.append(_ter, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      InternalInformation _asInternal_1 = this.cce.asInternal(id);
      CharSequence _ter_1 = this.getter(_asInternal_1, true, id, (-1));
      _builder_1.append(_ter_1, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("if ((t");
      _builder_1.append(id, "\t");
      _builder_1.append("_prev!=1) || (t");
      _builder_1.append(id, "\t");
      _builder_1.append("!=0)) {");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      String _idName_2 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_2, "\t\t");
      _builder_1.append("_isFalling=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("} else {");
      _builder_1.newLine();
      _builder_1.append("\t");
      InternalInformation _asInternal_2 = this.cce.asInternal(id);
      CharSequence _ter_2 = this.getter(_asInternal_2, false, id, (-1));
      _builder_1.append(_ter_2, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_3 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_3, "\t");
      _builder_1.append("_isFalling=t");
      _builder_1.append(id, "\t");
      _builder_1.append("==0;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _idName_4 = this.cce.idName(internal.info, false, false);
      _builder_1.append(_idName_4, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_5 = this.cce.idName(internal, false, false);
      _builder_1.append(_idName_5, "\t");
      _builder_1.append("_fallingIsHandled=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  public CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int i;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (i=0;i<regUpdatePos; i++) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("regUpdate_t reg=regUpdates[i];");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (reg.internal) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.cce.em.variables) {
        {
          if (v.isRegister) {
            _builder.append("\t\t\t");
            _builder.append("case ");
            Integer _get = this.cce.varIdx.get(v.name);
            _builder.append(_get, "\t\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            {
              int _length = v.dimensions.length;
              boolean _equals = (_length == 0);
              if (_equals) {
                _builder.append("\t\t\t");
                String _idName = this.cce.idName(v, false, false);
                _builder.append(_idName, "\t\t\t");
                _builder.append(" = ");
                String _idName_1 = this.cce.idName(v, false, false);
                _builder.append(_idName_1, "\t\t\t");
                _builder.append("$reg; break;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t\t");
                _builder.append("if (reg.offset==-1)");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                _builder.append("memcpy(");
                String _idName_2 = this.cce.idName(v, false, false);
                _builder.append(_idName_2, "\t\t\t\t");
                _builder.append(", ");
                String _idName_3 = this.cce.idName(v, false, false);
                _builder.append(_idName_3, "\t\t\t\t");
                _builder.append("$reg, ");
                int _talSize = this.cce.totalSize(v);
                _builder.append(_talSize, "\t\t\t\t");
                _builder.append(");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                String _idName_4 = this.cce.idName(v, false, false);
                _builder.append(_idName_4, "\t\t\t\t");
                _builder.append("[reg.offset] = ");
                String _idName_5 = this.cce.idName(v, false, false);
                _builder.append(_idName_5, "\t\t\t\t");
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
      String _idName = this.cce.idName(info, true, false);
      _builder.append(_idName, "");
      _builder.append("=");
      String _idName_1 = this.cce.idName(info, false, false);
      _builder.append(_idName_1, "");
      _builder.append(";");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("memcpy(");
    String _idName_2 = this.cce.idName(info, true, false);
    _builder_1.append(_idName_2, "");
    _builder_1.append(",");
    String _idName_3 = this.cce.idName(info, false, false);
    _builder_1.append(_idName_3, "");
    _builder_1.append(", ");
    int _talSize = this.cce.totalSize(info);
    _builder_1.append(_talSize, "");
    _builder_1.append(");");
    return _builder_1.toString();
  }
  
  public CharSequence getter(final InternalInformation info, final boolean prev, final int pos, final int frameID) {
    CharSequence _xblockexpression = null;
    {
      final StringBuilder sb = new StringBuilder();
      final CharSequence mask = this.cce.asMaskL(info.actualWidth);
      for (final int arr : info.arrayIdx) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[");
        _builder.append(arr, "");
        _builder.append("]");
        sb.append(_builder);
      }
      String _xifexpression = null;
      int _length = info.info.dimensions.length;
      boolean _equals = (_length == 0);
      if (_equals) {
        _xifexpression = "";
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("[");
        StringBuilder _arrayAccess = this.cce.arrayAccess(info.info, null);
        _builder_1.append(_arrayAccess, "");
        _builder_1.append(" & ");
        long _dimMask = this.cce.dimMask(info);
        CharSequence _hexStringL = this.cce.toHexStringL(_dimMask);
        _builder_1.append(_hexStringL, "");
        _builder_1.append("l]");
        _xifexpression = _builder_1.toString();
      }
      final String arrAcc = _xifexpression;
      String varName = ("t" + Integer.valueOf(pos));
      if (info.isPred) {
        varName = ("p" + Integer.valueOf(pos));
      }
      if (prev) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append(varName, "");
        _builder_2.append("_prev");
        varName = _builder_2.toString();
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_3 = new StringConcatenation();
        {
          if ((info.actualWidth == info.info.width)) {
            CharSequence _cType = this.cType(info);
            _builder_3.append(_cType, "");
            _builder_3.append(" ");
            _builder_3.append(varName, "");
            _builder_3.append("=");
            String _idName = this.cce.idName(info.info, prev, false);
            _builder_3.append(_idName, "");
            _builder_3.append(sb, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              CharSequence _cType_1 = this.cType(info);
              _builder_3.append(_cType_1, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("=(");
              String _idName_1 = this.cce.idName(info.info, prev, false);
              _builder_3.append(_idName_1, "");
              _builder_3.append(sb, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitStart, "");
              _builder_3.append(") & 1;");
              _builder_3.newLineIfNotEmpty();
            } else {
              CharSequence _cType_2 = this.cType(info);
              _builder_3.append(_cType_2, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("=(");
              String _idName_2 = this.cce.idName(info.info, prev, false);
              _builder_3.append(_idName_2, "");
              _builder_3.append(sb, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitEnd, "");
              _builder_3.append(") & ");
              _builder_3.append(mask, "");
              _builder_3.append(";");
              _builder_3.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression_1 = _builder_3;
      } else {
        StringConcatenation _builder_4 = new StringConcatenation();
        {
          if ((info.actualWidth == info.info.width)) {
            CharSequence _cType_3 = this.cType(info);
            _builder_4.append(_cType_3, "");
            _builder_4.append(" ");
            _builder_4.append(varName, "");
            _builder_4.append("= ");
            String _idName_3 = this.cce.idName(info.info, prev, false);
            _builder_4.append(_idName_3, "");
            _builder_4.append(arrAcc, "");
            _builder_4.append(";");
            _builder_4.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              CharSequence _cType_4 = this.cType(info);
              _builder_4.append(_cType_4, "");
              _builder_4.append(" ");
              _builder_4.append(varName, "");
              _builder_4.append("= (");
              String _idName_4 = this.cce.idName(info.info, prev, false);
              _builder_4.append(_idName_4, "");
              _builder_4.append(arrAcc, "");
              _builder_4.append(" >> ");
              _builder_4.append(info.bitStart, "");
              _builder_4.append(") & 1;");
              _builder_4.newLineIfNotEmpty();
            } else {
              CharSequence _cType_5 = this.cType(info);
              _builder_4.append(_cType_5, "");
              _builder_4.append(" ");
              _builder_4.append(varName, "");
              _builder_4.append("= (");
              String _idName_5 = this.cce.idName(info.info, prev, false);
              _builder_4.append(_idName_5, "");
              _builder_4.append(arrAcc, "");
              _builder_4.append(" >> ");
              _builder_4.append(info.bitEnd, "");
              _builder_4.append(") & ");
              CharSequence _asMaskL = this.cce.asMaskL(info.actualWidth);
              _builder_4.append(_asMaskL, "");
              _builder_4.append("l;");
              _builder_4.newLineIfNotEmpty();
            }
          }
        }
        _xifexpression_1 = _builder_4;
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  public CharSequence setter(final InternalInformation info, final String value) {
    CharSequence _xblockexpression = null;
    {
      long _doubleLessThan = (1l << info.actualWidth);
      final long mask = (_doubleLessThan - 1);
      final CharSequence maskString = this.cce.toHexStringL(mask);
      long _doubleLessThan_1 = (mask << info.bitEnd);
      long _bitwiseNot = (~_doubleLessThan_1);
      final CharSequence writeMask = this.cce.toHexStringL(_bitwiseNot);
      final StringBuilder varAccess = this.cce.arrayAccess(info.info, null);
      final int off = this.cce.arrayFixedOffset(info);
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
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("$reg");
        _builder_2.append(fixedAccess, "");
        fixedAccess = _builder_2.toString();
        regSuffix = "$reg";
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_3 = new StringConcatenation();
        {
          if (info.isShadowReg) {
            CharSequence _cType = this.cType(info.info);
            _builder_3.append(_cType, "");
            _builder_3.append(" prev=");
            String _idName = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName, "");
            _builder_3.append(fixedAccess, "");
            _builder_3.append(";");
          }
        }
        _builder_3.newLineIfNotEmpty();
        {
          if ((info.actualWidth == info.info.width)) {
            {
              if (info.isShadowReg) {
                CharSequence _cType_1 = this.cType(info.info);
                _builder_3.append(_cType_1, "");
                _builder_3.append(" current=");
                String _idName_1 = this.cce.idName(info.info, false, false);
                _builder_3.append(_idName_1, "");
                _builder_3.append(fixedAccess, "");
                _builder_3.append(";");
              }
            }
            _builder_3.newLineIfNotEmpty();
            String _idName_2 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_2, "");
            _builder_3.append(fixedAccess, "");
            _builder_3.append("=");
            _builder_3.append(value, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            CharSequence _cType_2 = this.cType(info.info);
            _builder_3.append(_cType_2, "");
            _builder_3.append(" current=");
            String _idName_3 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_3, "");
            _builder_3.append(fixedAccess, "");
            _builder_3.append(" & ");
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
            _builder_3.append(");");
            _builder_3.newLineIfNotEmpty();
            String _idName_4 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_4, "");
            _builder_3.append(fixedAccess, "");
            _builder_3.append("=current|");
            _builder_3.append(value, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_3.append("static regUpdate_t reg;");
            _builder_3.newLine();
            _builder_3.append("if (prev!=");
            _builder_3.append(value, "");
            _builder_3.append("){");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("reg.internal=");
            Integer _get = this.cce.varIdx.get(info.info.name);
            _builder_3.append(_get, "\t");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            _builder_3.append("\t");
            _builder_3.append("reg.offset=");
            _builder_3.append(off, "\t");
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
            String _idName_5 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_5, "");
            _builder_3.append("_update=((uint64_t) deltaCycle << 16ll) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_3.newLineIfNotEmpty();
        _xifexpression_1 = _builder_3;
      } else {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("int offset=(int)");
        _builder_4.append(varAccess, "");
        _builder_4.append(";");
        _builder_4.newLineIfNotEmpty();
        _builder_4.append("offset&=");
        long _dimMask = this.cce.dimMask(info);
        CharSequence _hexStringL = this.cce.toHexStringL(_dimMask);
        _builder_4.append(_hexStringL, "");
        _builder_4.append("l;");
        _builder_4.newLineIfNotEmpty();
        {
          if (info.isShadowReg) {
            CharSequence _cType_3 = this.cType(info.info);
            _builder_4.append(_cType_3, "");
            _builder_4.append(" prev=");
            String _idName_6 = this.cce.idName(info.info, false, false);
            _builder_4.append(_idName_6, "");
            _builder_4.append(regSuffix, "");
            _builder_4.append("[offset];");
          }
        }
        _builder_4.newLineIfNotEmpty();
        {
          if ((info.actualWidth == info.info.width)) {
            {
              if (info.isShadowReg) {
                CharSequence _cType_4 = this.cType(info.info);
                _builder_4.append(_cType_4, "");
                _builder_4.append(" current=");
                String _idName_7 = this.cce.idName(info.info, false, false);
                _builder_4.append(_idName_7, "");
                _builder_4.append(regSuffix, "");
                _builder_4.append("[offset];");
              }
            }
            _builder_4.newLineIfNotEmpty();
            String _idName_8 = this.cce.idName(info.info, false, false);
            _builder_4.append(_idName_8, "");
            _builder_4.append(regSuffix, "");
            _builder_4.append("[offset]=");
            _builder_4.append(value, "");
            _builder_4.append(";");
            _builder_4.newLineIfNotEmpty();
          } else {
            CharSequence _cType_5 = this.cType(info.info);
            _builder_4.append(_cType_5, "");
            _builder_4.append(" current=");
            String _idName_9 = this.cce.idName(info.info, false, false);
            _builder_4.append(_idName_9, "");
            _builder_4.append(regSuffix, "");
            _builder_4.append("[offset] & ");
            _builder_4.append(writeMask, "");
            _builder_4.append(";");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append(value, "");
            _builder_4.append("=((");
            _builder_4.append(value, "");
            _builder_4.append(" & ");
            _builder_4.append(maskString, "");
            _builder_4.append(") << ");
            _builder_4.append(info.bitEnd, "");
            _builder_4.append(";");
            _builder_4.newLineIfNotEmpty();
            String _idName_10 = this.cce.idName(info.info, false, false);
            _builder_4.append(_idName_10, "");
            _builder_4.append(regSuffix, "");
            _builder_4.append("[offset]=current|");
            _builder_4.append(value, "");
            _builder_4.append(");");
            _builder_4.newLineIfNotEmpty();
          }
        }
        {
          if (info.isShadowReg) {
            _builder_4.append("static regUpdate_t reg;");
            _builder_4.newLine();
            _builder_4.append("if (prev!=");
            _builder_4.append(value, "");
            _builder_4.append("){");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("\t");
            _builder_4.append("reg.internal=");
            Integer _get_1 = this.cce.varIdx.get(info.info.name);
            _builder_4.append(_get_1, "\t");
            _builder_4.append(";");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("\t");
            _builder_4.append("reg.offset=offset;");
            _builder_4.newLine();
            _builder_4.append("\t");
            _builder_4.append("regUpdates[regUpdatePos++]=reg;");
            _builder_4.newLine();
            _builder_4.append("}");
            _builder_4.newLine();
          }
        }
        {
          if (info.isPred) {
            String _idName_11 = this.cce.idName(info.info, false, false);
            _builder_4.append(_idName_11, "");
            _builder_4.append("_update=((uint64_t) deltaCycle << 16ll) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_4.newLineIfNotEmpty();
        _xifexpression_1 = _builder_4;
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  public CharSequence method(final Frame frame) {
    CharSequence _xblockexpression = null;
    {
      int pos = 0;
      int arrPos = 0;
      final Stack<Integer> stack = new Stack<Integer>();
      final List<Integer> arr = new LinkedList<Integer>();
      final StringBuilder func = new StringBuilder();
      for (final Frame.FastInstruction i : frame.instructions) {
        {
          int a = 0;
          int b = 0;
          if ((i.inst.pop > 0)) {
            Integer _pop = stack.pop();
            a = (_pop).intValue();
          }
          if ((i.inst.pop > 1)) {
            Integer _pop_1 = stack.pop();
            b = (_pop_1).intValue();
          }
          if ((i.inst.push > 0)) {
            stack.push(Integer.valueOf(pos));
          }
          boolean _tripleEquals = (i.inst == Instruction.pushAddIndex);
          if (_tripleEquals) {
            arr.add(Integer.valueOf(arrPos));
            arrPos = (arrPos + 1);
          }
          this.toExpression(i, frame, func, pos, a, b, arr, arrPos);
          boolean _tripleNotEquals = (i.inst != Instruction.pushAddIndex);
          if (_tripleNotEquals) {
            pos = (pos + 1);
          }
        }
      }
      Integer _pop = stack.pop();
      final String last = ("t" + _pop);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("void ");
      CharSequence _frameName = this.cce.getFrameName(frame);
      _builder.append(_frameName, "");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append(func, "\t");
      _builder.newLineIfNotEmpty();
      {
        InternalInformation _asInternal = this.cce.asInternal(frame.outputId);
        boolean _notEquals = (!Objects.equal(_asInternal.info.name, "#null"));
        if (_notEquals) {
          _builder.append("\t");
          InternalInformation _asInternal_1 = this.cce.asInternal(frame.outputId);
          CharSequence _setter = this.setter(_asInternal_1, last);
          _builder.append(_setter, "\t");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("\t");
          _builder.append("//Write to #null ");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("(void)");
          _builder.append(last, "\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public StringBuilder toExpression(final Frame.FastInstruction inst, final Frame f, final StringBuilder sb, final int pos, final int a, final int b, final List<Integer> arr, final int arrPos) {
    StringBuilder _xblockexpression = null;
    {
      final Instruction _switchValue = inst.inst;
      if (_switchValue != null) {
        switch (_switchValue) {
          case pushAddIndex:
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("int a");
            Integer _last = IterableExtensions.<Integer>last(arr);
            _builder.append(_last, "");
            _builder.append("=(int)t");
            _builder.append(a, "");
            _builder.append(";");
            sb.append(_builder);
            break;
          case writeInternal:
            final InternalInformation internal = this.cce.asInternal(inst.arg1);
            final VariableInformation info = internal.info;
            int _size = arr.size();
            int _length = info.dimensions.length;
            final boolean isDynMem = (_size < _length);
            if (isDynMem) {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("memset(");
              String _idName = this.cce.idName(internal, false, false);
              _builder_1.append(_idName, "");
              _builder_1.append(", t");
              _builder_1.append(a, "");
              _builder_1.append(", ");
              int _talSize = this.cce.totalSize(info);
              _builder_1.append(_talSize, "");
              _builder_1.append(");");
              sb.append(_builder_1);
            } else {
              StringConcatenation _builder_2 = new StringConcatenation();
              String _idName_1 = this.cce.idName(internal, false, false);
              _builder_2.append(_idName_1, "");
              {
                int _length_1 = info.dimensions.length;
                boolean _greaterThan = (_length_1 > 0);
                if (_greaterThan) {
                  _builder_2.append("[");
                  StringBuilder _arrayAccess = this.cce.arrayAccess(info, arr);
                  _builder_2.append(_arrayAccess, "");
                  _builder_2.append("]");
                }
              }
              _builder_2.append("=t");
              _builder_2.append(a, "");
              _builder_2.append(";");
              sb.append(_builder_2);
            }
            if (internal.isShadowReg) {
              StringConcatenation _builder_3 = new StringConcatenation();
              _builder_3.append("{");
              _builder_3.newLine();
              _builder_3.append("\t");
              _builder_3.append("static regUpdate_t reg;");
              _builder_3.newLine();
              _builder_3.append("\t");
              _builder_3.append("reg.internal=");
              Integer _get = this.cce.varIdx.get(info.name);
              _builder_3.append(_get, "\t");
              _builder_3.append(";");
              _builder_3.newLineIfNotEmpty();
              _builder_3.append("\t");
              _builder_3.append("reg.offset=");
              {
                boolean _and = false;
                if (!(!isDynMem)) {
                  _and = false;
                } else {
                  boolean _isArray = this.cce.isArray(internal.info);
                  _and = _isArray;
                }
                if (_and) {
                  StringBuilder _arrayAccess_1 = this.cce.arrayAccess(internal.info, arr);
                  _builder_3.append(_arrayAccess_1, "\t");
                } else {
                  _builder_3.append("-1");
                }
              }
              _builder_3.append(";");
              _builder_3.newLineIfNotEmpty();
              _builder_3.append("\t");
              _builder_3.append("regUpdates[regUpdatePos++]=reg;");
              _builder_3.newLine();
              _builder_3.append("}");
              _builder_3.newLine();
              String _plus = ("\n" + _builder_3);
              sb.append(_plus);
            }
            arr.clear();
            break;
          case noop:
            sb.append("//Do nothing");
            break;
          case bitAccessSingle:
            StringConcatenation _builder_4 = new StringConcatenation();
            CharSequence _uTemp = this.uTemp(pos, "t");
            _builder_4.append(_uTemp, "");
            _builder_4.append("=(t");
            _builder_4.append(a, "");
            _builder_4.append(" >> ");
            _builder_4.append(inst.arg1, "");
            _builder_4.append(") & 1;");
            sb.append(_builder_4);
            break;
          case bitAccessSingleRange:
            final int highBit = inst.arg1;
            final int lowBit = inst.arg2;
            long _doubleLessThan = (1l << ((highBit - lowBit) + 1));
            final long mask = (_doubleLessThan - 1);
            StringConcatenation _builder_5 = new StringConcatenation();
            CharSequence _uTemp_1 = this.uTemp(pos, "t");
            _builder_5.append(_uTemp_1, "");
            _builder_5.append("=(t");
            _builder_5.append(a, "");
            _builder_5.append(" >> ");
            _builder_5.append(lowBit, "");
            _builder_5.append(") & ");
            CharSequence _hexStringL = this.cce.toHexStringL(mask);
            _builder_5.append(_hexStringL, "");
            _builder_5.append("l;");
            sb.append(_builder_5);
            break;
          case cast_int:
            if ((inst.arg1 != this.bitWidth)) {
              int _min = Math.min(inst.arg1, inst.arg2);
              final int shiftWidth = (this.bitWidth - _min);
              StringConcatenation _builder_6 = new StringConcatenation();
              CharSequence _sTemp = this.sTemp(pos, "c");
              _builder_6.append(_sTemp, "");
              _builder_6.append("=t");
              _builder_6.append(a, "");
              _builder_6.append(" << ");
              _builder_6.append(shiftWidth, "");
              _builder_6.append(";");
              _builder_6.newLineIfNotEmpty();
              CharSequence _uTemp_2 = this.uTemp(pos, "t");
              _builder_6.append(_uTemp_2, "");
              _builder_6.append("=c");
              _builder_6.append(pos, "");
              _builder_6.append(" >> ");
              _builder_6.append(shiftWidth, "");
              _builder_6.append(";");
              _builder_6.newLineIfNotEmpty();
              sb.append(_builder_6);
            } else {
              StringConcatenation _builder_7 = new StringConcatenation();
              CharSequence _uTemp_3 = this.uTemp(pos, "t");
              _builder_7.append(_uTemp_3, "");
              _builder_7.append("=t");
              _builder_7.append(a, "");
              _builder_7.append(";");
              sb.append(_builder_7);
            }
            break;
          case cast_uint:
            final int castSize = Math.min(inst.arg1, inst.arg2);
            if ((castSize != this.bitWidth)) {
              StringConcatenation _builder_8 = new StringConcatenation();
              CharSequence _uTemp_4 = this.uTemp(pos, "t");
              _builder_8.append(_uTemp_4, "");
              _builder_8.append("=t");
              _builder_8.append(a, "");
              _builder_8.append(" & ");
              CharSequence _asMaskL = this.cce.asMaskL(castSize);
              _builder_8.append(_asMaskL, "");
              _builder_8.append("l;");
              sb.append(_builder_8);
            } else {
              StringConcatenation _builder_9 = new StringConcatenation();
              CharSequence _uTemp_5 = this.uTemp(pos, "t");
              _builder_9.append(_uTemp_5, "");
              _builder_9.append("=t");
              _builder_9.append(a, "");
              _builder_9.append(";");
              sb.append(_builder_9);
            }
            break;
          case logiNeg:
            StringConcatenation _builder_10 = new StringConcatenation();
            _builder_10.append("bool t");
            _builder_10.append(pos, "");
            _builder_10.append("=!t");
            _builder_10.append(a, "");
            _builder_10.append(";");
            sb.append(_builder_10);
            break;
          case logiAnd:
            StringConcatenation _builder_11 = new StringConcatenation();
            _builder_11.append("bool t");
            _builder_11.append(pos, "");
            _builder_11.append("=t");
            _builder_11.append(a, "");
            _builder_11.append(" && t");
            _builder_11.append(b, "");
            _builder_11.append(";");
            sb.append(_builder_11);
            break;
          case logiOr:
            StringConcatenation _builder_12 = new StringConcatenation();
            _builder_12.append("bool t");
            _builder_12.append(pos, "");
            _builder_12.append("=t");
            _builder_12.append(a, "");
            _builder_12.append(" || t");
            _builder_12.append(b, "");
            _builder_12.append(";");
            sb.append(_builder_12);
            break;
          case const0:
            StringConcatenation _builder_13 = new StringConcatenation();
            CharSequence _uTemp_6 = this.uTemp(pos, "t");
            _builder_13.append(_uTemp_6, "");
            _builder_13.append("=0;");
            sb.append(_builder_13);
            break;
          case const1:
            StringConcatenation _builder_14 = new StringConcatenation();
            CharSequence _uTemp_7 = this.uTemp(pos, "t");
            _builder_14.append(_uTemp_7, "");
            _builder_14.append("=1;");
            sb.append(_builder_14);
            break;
          case const2:
            StringConcatenation _builder_15 = new StringConcatenation();
            CharSequence _uTemp_8 = this.uTemp(pos, "t");
            _builder_15.append(_uTemp_8, "");
            _builder_15.append("=2;");
            sb.append(_builder_15);
            break;
          case constAll1:
            StringConcatenation _builder_16 = new StringConcatenation();
            CharSequence _uTemp_9 = this.uTemp(pos, "t");
            _builder_16.append(_uTemp_9, "");
            _builder_16.append("=");
            CharSequence _asMaskL_1 = this.cce.asMaskL(inst.arg1);
            _builder_16.append(_asMaskL_1, "");
            _builder_16.append("l;");
            sb.append(_builder_16);
            break;
          case concat:
            StringConcatenation _builder_17 = new StringConcatenation();
            CharSequence _uTemp_10 = this.uTemp(pos, "t");
            _builder_17.append(_uTemp_10, "");
            _builder_17.append("=(t");
            _builder_17.append(b, "");
            _builder_17.append(" << ");
            _builder_17.append(inst.arg2, "");
            _builder_17.append(") | t");
            _builder_17.append(a, "");
            _builder_17.append(";");
            sb.append(_builder_17);
            break;
          case loadConstant:
            if ((this.bitWidth == 32)) {
              StringConcatenation _builder_18 = new StringConcatenation();
              CharSequence _uTemp_11 = this.uTemp(pos, "t");
              _builder_18.append(_uTemp_11, "");
              _builder_18.append("=");
              CharSequence _constantI = this.cce.constantI(inst.arg1, f);
              _builder_18.append(_constantI, "");
              _builder_18.append(";");
              sb.append(_builder_18);
            } else {
              StringConcatenation _builder_19 = new StringConcatenation();
              CharSequence _uTemp_12 = this.uTemp(pos, "t");
              _builder_19.append(_uTemp_12, "");
              _builder_19.append("=");
              CharSequence _constantL = this.cce.constantL(inst.arg1, f);
              _builder_19.append(_constantL, "");
              _builder_19.append("l;");
              sb.append(_builder_19);
            }
            break;
          case loadInternal:
            final InternalInformation internal_1 = this.cce.asInternal(inst.arg1);
            CharSequence _ter = this.getter(internal_1, false, pos, f.uniqueID);
            sb.append(_ter);
            arr.clear();
            break;
          case arith_neg:
            StringConcatenation _builder_20 = new StringConcatenation();
            CharSequence _uTemp_13 = this.uTemp(pos, "t");
            _builder_20.append(_uTemp_13, "");
            _builder_20.append("=");
            StringConcatenation _builder_21 = new StringConcatenation();
            _builder_21.append("(");
            CharSequence _int_t = this.int_t();
            _builder_21.append(_int_t, "");
            _builder_21.append(")");
            CharSequence _singleOpValue = this.cce.singleOpValue("-", _builder_21.toString(), a, inst.arg1);
            _builder_20.append(_singleOpValue, "");
            _builder_20.append(";");
            sb.append(_builder_20);
            break;
          case bit_neg:
            StringConcatenation _builder_22 = new StringConcatenation();
            CharSequence _uTemp_14 = this.uTemp(pos, "t");
            _builder_22.append(_uTemp_14, "");
            _builder_22.append("=");
            StringConcatenation _builder_23 = new StringConcatenation();
            _builder_23.append("(");
            CharSequence _int_t_1 = this.int_t();
            _builder_23.append(_int_t_1, "");
            _builder_23.append(")");
            CharSequence _singleOpValue_1 = this.cce.singleOpValue("~", _builder_23.toString(), a, inst.arg1);
            _builder_22.append(_singleOpValue_1, "");
            _builder_22.append(";");
            sb.append(_builder_22);
            break;
          case and:
            String _twoOp = this.twoOp("&", inst.arg1, pos, a, b);
            sb.append(_twoOp);
            break;
          case or:
            String _twoOp_1 = this.twoOp("|", inst.arg1, pos, a, b);
            sb.append(_twoOp_1);
            break;
          case xor:
            String _twoOp_2 = this.twoOp("^", inst.arg1, pos, a, b);
            sb.append(_twoOp_2);
            break;
          case plus:
            String _twoOp_3 = this.twoOp("+", inst.arg1, pos, a, b);
            sb.append(_twoOp_3);
            break;
          case minus:
            String _twoOp_4 = this.twoOp("-", inst.arg1, pos, a, b);
            sb.append(_twoOp_4);
            break;
          case mul:
            String _twoOp_5 = this.twoOp("*", inst.arg1, pos, a, b);
            sb.append(_twoOp_5);
            break;
          case div:
            String _twoOp_6 = this.twoOp("/", inst.arg1, pos, a, b);
            sb.append(_twoOp_6);
            break;
          case sll:
            String _twoOp_7 = this.twoOp("<<", inst.arg1, pos, a, b);
            sb.append(_twoOp_7);
            break;
          case srl:
            String _twoOp_8 = this.twoOp(">>", inst.arg1, pos, a, b);
            sb.append(_twoOp_8);
            break;
          case sra:
            StringConcatenation _builder_24 = new StringConcatenation();
            CharSequence _uTemp_15 = this.uTemp(pos, "t");
            _builder_24.append(_uTemp_15, "");
            _builder_24.append("=");
            CharSequence _sra = this.sra(inst.arg1, a, b);
            _builder_24.append(_sra, "");
            _builder_24.append(";");
            sb.append(_builder_24);
            break;
          case eq:
            StringConcatenation _builder_25 = new StringConcatenation();
            _builder_25.append("bool t");
            _builder_25.append(pos, "");
            _builder_25.append("=t");
            _builder_25.append(b, "");
            _builder_25.append(" == t");
            _builder_25.append(a, "");
            _builder_25.append(";");
            sb.append(_builder_25);
            break;
          case not_eq:
            StringConcatenation _builder_26 = new StringConcatenation();
            _builder_26.append("bool t");
            _builder_26.append(pos, "");
            _builder_26.append("=t");
            _builder_26.append(b, "");
            _builder_26.append(" != t");
            _builder_26.append(a, "");
            _builder_26.append(";");
            sb.append(_builder_26);
            break;
          case less:
            StringConcatenation _builder_27 = new StringConcatenation();
            _builder_27.append("bool t");
            _builder_27.append(pos, "");
            _builder_27.append("=t");
            _builder_27.append(b, "");
            _builder_27.append(" < t");
            _builder_27.append(a, "");
            _builder_27.append(";");
            sb.append(_builder_27);
            break;
          case less_eq:
            StringConcatenation _builder_28 = new StringConcatenation();
            _builder_28.append("bool t");
            _builder_28.append(pos, "");
            _builder_28.append("=t");
            _builder_28.append(b, "");
            _builder_28.append(" <= t");
            _builder_28.append(a, "");
            _builder_28.append(";");
            sb.append(_builder_28);
            break;
          case greater:
            StringConcatenation _builder_29 = new StringConcatenation();
            _builder_29.append("bool t");
            _builder_29.append(pos, "");
            _builder_29.append("=t");
            _builder_29.append(b, "");
            _builder_29.append(" > t");
            _builder_29.append(a, "");
            _builder_29.append(";");
            sb.append(_builder_29);
            break;
          case greater_eq:
            StringConcatenation _builder_30 = new StringConcatenation();
            _builder_30.append("bool t");
            _builder_30.append(pos, "");
            _builder_30.append("=t");
            _builder_30.append(b, "");
            _builder_30.append(" >= t");
            _builder_30.append(a, "");
            _builder_30.append(";");
            sb.append(_builder_30);
            break;
          case isRisingEdge:
            StringConcatenation _builder_31 = new StringConcatenation();
            InternalInformation _asInternal = this.cce.asInternal(inst.arg1);
            String _idName_2 = this.cce.idName(_asInternal.info, false, false);
            _builder_31.append(_idName_2, "");
            _builder_31.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
            sb.append(_builder_31);
            break;
          case isFallingEdge:
            StringConcatenation _builder_32 = new StringConcatenation();
            InternalInformation _asInternal_1 = this.cce.asInternal(inst.arg1);
            String _idName_3 = this.cce.idName(_asInternal_1.info, false, false);
            _builder_32.append(_idName_3, "");
            _builder_32.append("_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
            sb.append(_builder_32);
            break;
          default:
            break;
        }
      }
      StringConcatenation _builder_33 = new StringConcatenation();
      _builder_33.append("//");
      _builder_33.append(inst, "");
      _builder_33.newLineIfNotEmpty();
      _xblockexpression = sb.append(_builder_33);
    }
    return _xblockexpression;
  }
  
  public CharSequence uTemp(final int pos, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _uint_t = this.uint_t();
    _builder.append(_uint_t, "");
    _builder.append(" ");
    _builder.append(name, "");
    _builder.append(pos, "");
    return _builder;
  }
  
  public CharSequence sTemp(final int pos, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _int_t = this.int_t();
    _builder.append(_int_t, "");
    _builder.append(" ");
    _builder.append(name, "");
    _builder.append(pos, "");
    return _builder;
  }
  
  public CharSequence uint_t() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("uint");
    _builder.append(this.bitWidth, "");
    _builder.append("_t");
    return _builder;
  }
  
  public CharSequence int_t() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int");
    _builder.append(this.bitWidth, "");
    _builder.append("_t");
    return _builder;
  }
  
  public CharSequence sra(final int targetSizeWithType, final int a, final int b) {
    final int targetSize = (targetSizeWithType >> 1);
    final int shift = (this.bitWidth - targetSize);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _equals = (_bitwiseAnd == 1);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("((");
      CharSequence _int_t = this.int_t();
      _builder.append(_int_t, "");
      _builder.append(")t");
      _builder.append(b, "");
      _builder.append(") >> t");
      _builder.append(a, "");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("(");
      CharSequence _int_t_1 = this.int_t();
      _builder_1.append(_int_t_1, "");
      _builder_1.append(")");
      return this.cce.signExtend(_builder, _builder_1, shift);
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("(((");
    CharSequence _int_t_2 = this.int_t();
    _builder_2.append(_int_t_2, "");
    _builder_2.append(")t");
    _builder_2.append(b, "");
    _builder_2.append(") >> t");
    _builder_2.append(a, "");
    _builder_2.append(") & ");
    CharSequence _asMaskL = this.cce.asMaskL(targetSize);
    _builder_2.append(_asMaskL, "");
    _builder_2.append("l");
    return _builder_2.toString();
  }
  
  public String twoOp(final String op, final int targetSizeWithType, final int pos, final int a, final int b) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _uTemp = this.uTemp(pos, "t");
    _builder.append(_uTemp, "");
    _builder.append("=");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(");
    CharSequence _int_t = this.int_t();
    _builder_1.append(_int_t, "");
    _builder_1.append(")");
    CharSequence _twoOpValue = this.cce.twoOpValue(op, _builder_1.toString(), a, b, targetSizeWithType);
    _builder.append(_twoOpValue, "");
    _builder.append(";");
    return _builder.toString();
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
        size = (size * d);
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      String _idName = this.cce.idName(info, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("=new ");
      CharSequence _cType = this.cType(info);
      _builder_1.append(_cType, "");
      _builder_1.append("[");
      _builder_1.append(size, "");
      _builder_1.append("];");
      _builder_1.newLineIfNotEmpty();
      {
        if (info.isRegister) {
          String _idName_1 = this.cce.idName(info, false, false);
          _builder_1.append(_idName_1, "");
          _builder_1.append("$reg=new ");
          CharSequence _cType_1 = this.cType(info);
          _builder_1.append(_cType_1, "");
          _builder_1.append("[");
          _builder_1.append(size, "");
          _builder_1.append("];");
        }
      }
      _builder_1.newLineIfNotEmpty();
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  public CharSequence cType(final InternalInformation ii) {
    final CharSequence jt = this.cType(ii.info);
    int _length = ii.arrayIdx.length;
    int _length_1 = ii.info.dimensions.length;
    boolean _notEquals = (_length != _length_1);
    if (_notEquals) {
      return (jt + "[]");
    }
    return jt;
  }
  
  public CharSequence cType(final VariableInformation information) {
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      return "bool";
    }
    return this.uint_t();
  }
  
  public CharSequence decl(final VariableInformation info, final Boolean includePrev) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _or = false;
      boolean _isPredicate = this.cce.isPredicate(info);
      if (_isPredicate) {
        _or = true;
      } else {
        boolean _and = false;
        Boolean _get = this.cce.prevMap.get(info.name);
        boolean _tripleNotEquals = (_get != null);
        if (!_tripleNotEquals) {
          _and = false;
        } else {
          Boolean _get_1 = this.cce.prevMap.get(info.name);
          _and = (_get_1).booleanValue();
        }
        _or = _and;
      }
      if (_or) {
        _builder.append("uint64_t ");
        String _idName = this.cce.idName(info, false, 
          false);
        _builder.append(_idName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _cType = this.cType(info);
    _builder.append(_cType, "");
    _builder.append(" ");
    String _idName_1 = this.cce.idName(info, false, false);
    _builder.append(_idName_1, "");
    {
      boolean _isEmpty = IterableExtensions.isEmpty(((Iterable<?>)Conversions.doWrapArray(info.dimensions)));
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("[");
        int _talSize = this.cce.totalSize(info);
        _builder.append(_talSize, "");
        _builder.append("]");
      }
    }
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("#define PSHDL_SIM_");
    String _idName_2 = this.cce.idName(info, false, false);
    String _upperCase = _idName_2.toUpperCase();
    _builder.append(_upperCase, "");
    _builder.append(" ");
    Integer _get_2 = this.cce.varIdx.get(info.name);
    _builder.append(_get_2, "");
    _builder.newLineIfNotEmpty();
    {
      boolean _and_1 = false;
      boolean _tripleNotEquals_1 = (includePrev != null);
      if (!_tripleNotEquals_1) {
        _and_1 = false;
      } else {
        _and_1 = (includePrev).booleanValue();
      }
      if (_and_1) {
        CharSequence _cType_1 = this.cType(info);
        _builder.append(_cType_1, "");
        _builder.append(" ");
        String _idName_3 = this.cce.idName(info, true, false);
        _builder.append(_idName_3, "");
        {
          boolean _isEmpty_1 = IterableExtensions.isEmpty(((Iterable<?>)Conversions.doWrapArray(info.dimensions)));
          boolean _not_1 = (!_isEmpty_1);
          if (_not_1) {
            _builder.append("[");
            int _talSize_1 = this.cce.totalSize(info);
            _builder.append(_talSize_1, "");
            _builder.append("]");
          }
        }
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        CharSequence _cType_2 = this.cType(info);
        _builder.append(_cType_2, "");
        _builder.append(" ");
        String _idName_4 = this.cce.idName(info, false, false);
        _builder.append(_idName_4, "");
        _builder.append("$reg");
        {
          boolean _isEmpty_2 = IterableExtensions.isEmpty(((Iterable<?>)Conversions.doWrapArray(info.dimensions)));
          boolean _not_2 = (!_isEmpty_2);
          if (_not_2) {
            _builder.append("[");
            int _talSize_2 = this.cce.totalSize(info);
            _builder.append(_talSize_2, "");
            _builder.append("]");
          }
        }
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
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
    _builder.append("#include <stdarg.h>");
    _builder.newLine();
    return _builder;
  }
  
  public String getHookName() {
    return "C";
  }
  
  public IOutputProvider.MultiOption getUsage() {
    final Options options = new Options();
    options.addOption("j", "jsonDescription", false, "Generates a string that can be parsed as json that descripes the module");
    return new IOutputProvider.MultiOption(null, null, options);
  }
  
  public static List<PSAbstractCompiler.CompileResult> doCompile(final ExecutableModel em, final boolean withJSON, final Set<Problem> syntaxProblems) {
    final CCompiler comp = new CCompiler(em, withJSON);
    final List<IHDLGenerator.SideFile> sideFiles = Lists.<IHDLGenerator.SideFile>newLinkedList();
    final String simFile = comp.generateSimEncapsuation();
    boolean _tripleNotEquals = (simFile != null);
    if (_tripleNotEquals) {
      byte[] _bytes = simFile.getBytes(Charsets.UTF_8);
      IHDLGenerator.SideFile _sideFile = new IHDLGenerator.SideFile("simEncapsulation.c", _bytes, true);
      sideFiles.add(_sideFile);
    }
    CharSequence _compile = comp.compile();
    String _string = _compile.toString();
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, _string, em.moduleName, sideFiles, em.source, _hookName, 
      true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
    boolean _hasOption = cli.hasOption("jsonDescription");
    return CCompiler.doCompile(em, _hasOption, syntaxProblems);
  }
  
  public String generateSimEncapsuation() {
    final Unit unit = this.getUnit(this.cce.em);
    boolean _tripleEquals = (unit == null);
    if (_tripleEquals) {
      return null;
    }
    List<Row> _buildRows = MemoryModel.buildRows(unit);
    return this.generateSimEncapsuation(unit, _buildRows);
  }
  
  public Unit getUnit(final ExecutableModel model) {
    try {
      Unit unit = null;
      final Splitter annoSplitter = Splitter.on(SimulationTransformationExtension.ANNO_VALUE_SEP);
      boolean _tripleNotEquals = (this.cce.em.annotations != null);
      if (_tripleNotEquals) {
        for (final String a : this.cce.em.annotations) {
          boolean _startsWith = a.startsWith("busDescription");
          if (_startsWith) {
            Splitter _limit = annoSplitter.limit(2);
            Iterable<String> _split = _limit.split(a);
            final String value = IterableExtensions.<String>last(_split);
            HashSet<Problem> _hashSet = new HashSet<Problem>();
            Unit _parseUnit = MemoryModelAST.parseUnit(value, _hashSet, 0);
            unit = _parseUnit;
          }
        }
      }
      return unit;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Extension
  private BusAccess ba = new BusAccess();
  
  private String generateSimEncapsuation(final Unit unit, final Iterable<Row> rows) {
    final Set<String> varNames = new HashSet<String>();
    final Procedure1<Row> _function = new Procedure1<Row>() {
      public void apply(final Row it) {
        List<Definition> _allDefs = CCompiler.this.ba.allDefs(it);
        final Function1<Definition, Boolean> _function = new Function1<Definition, Boolean>() {
          public Boolean apply(final Definition it) {
            return Boolean.valueOf((it.type != Definition.Type.UNUSED));
          }
        };
        Iterable<Definition> _filter = IterableExtensions.<Definition>filter(_allDefs, _function);
        final Procedure1<Definition> _function_1 = new Procedure1<Definition>() {
          public void apply(final Definition it) {
            String _name = it.getName();
            varNames.add(_name);
          }
        };
        IterableExtensions.<Definition>forEach(_filter, _function_1);
      }
    };
    IterableExtensions.<Row>forEach(rows, _function);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//  BusAccessSim.c");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <stdint.h>");
    _builder.newLine();
    _builder.append("#include <stdbool.h>");
    _builder.newLine();
    _builder.append("#include \"BusAccess.h\"");
    _builder.newLine();
    _builder.append("#include \"BusStdDefinitions.h\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("static void defaultWarn(warningType_t t, int value, char *def, char *row, char *msg){");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("warnFunc_p warn=defaultWarn;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void setWarn(warnFunc_p warnFunction){");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("warn=warnFunction;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("extern uint64_t pshdl_sim_getOutput(int idx, ...);");
    _builder.newLine();
    _builder.append("extern void pshdl_sim_setInput(int idx, long value, ...);");
    _builder.newLine();
    _builder.append("extern void pshdl_sim_run();");
    _builder.newLine();
    _builder.append("extern bool disableEdges;");
    _builder.newLine();
    _builder.newLine();
    {
      for(final String v : varNames) {
        _builder.append("#define ");
        String _defineName = this.getDefineName(v);
        _builder.append(_defineName, "");
        _builder.append(" ");
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append(this.cce.em.moduleName, "");
        _builder_1.append(".");
        _builder_1.append(v, "");
        String _string = _builder_1.toString();
        Integer _get = this.cce.varIdx.get(_string);
        _builder.append(_get, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("#define ");
    String _defineName_1 = this.getDefineName("Bus2IP_Clk");
    _builder.append(_defineName_1, "");
    _builder.append(" ");
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append(this.cce.em.moduleName, "");
    _builder_2.append(".Bus2IP_Clk");
    String _string_1 = _builder_2.toString();
    Integer _get_1 = this.cce.varIdx.get(_string_1);
    _builder.append(_get_1, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String res = _builder.toString();
    final HashSet<String> checkedRows = new HashSet<String>();
    for (final Row row : rows) {
      boolean _contains = checkedRows.contains(row.name);
      boolean _not = (!_contains);
      if (_not) {
        boolean _hasWriteDefs = this.ba.hasWriteDefs(row);
        if (_hasWriteDefs) {
          CharSequence _simSetter = this.simSetter(row);
          String _plus = (res + _simSetter);
          res = _plus;
        }
        CharSequence _simGetter = this.simGetter(row);
        String _plus_1 = (res + _simGetter);
        res = _plus_1;
        checkedRows.add(row.name);
      }
    }
    return res;
  }
  
  public String getDefineName(final String v) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(this.cce.em.moduleName, "");
    _builder.append(".");
    _builder.append(v, "");
    String _string = _builder.toString();
    return this.cce.idName(_string, false, false);
  }
  
  public CharSequence simGetter(final Row row) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//Getter");
    _builder.newLine();
    _builder.append("int get");
    String _firstUpper = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper, "");
    _builder.append("Direct(uint32_t *base, int index");
    {
      List<Definition> _allDefs = this.ba.allDefs(row);
      for(final Definition definition : _allDefs) {
        String _parameter = this.ba.getParameter(row, definition, true);
        _builder.append(_parameter, "");
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _allDefs_1 = this.ba.allDefs(row);
      for(final Definition d : _allDefs_1) {
        _builder.append("\t");
        _builder.append("*");
        String _varName = this.ba.getVarName(row, d);
        _builder.append(_varName, "\t");
        _builder.append("=pshdl_sim_getOutput(");
        String _defineName = this.getDefineName(d.name);
        _builder.append(_defineName, "\t");
        _builder.append(", index);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return 1;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int get");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_1, "");
    _builder.append("(uint32_t *base, int index, ");
    _builder.append(row.name, "");
    _builder.append("_t *result){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return get");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_2, "\t");
    _builder.append("Direct(base, index");
    {
      List<Definition> _allDefs_2 = this.ba.allDefs(row);
      for(final Definition d_1 : _allDefs_2) {
        _builder.append(", &result->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_1);
        _builder.append(_varNameIndex, "\t");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence simSetter(final Row row) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("// Setter");
    _builder.newLine();
    _builder.append("int set");
    String _firstUpper = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper, "");
    _builder.append("Direct(uint32_t *base, int index");
    {
      List<Definition> _writeDefs = this.ba.writeDefs(row);
      for(final Definition definition : _writeDefs) {
        String _parameter = this.ba.getParameter(row, definition, false);
        _builder.append(_parameter, "");
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _writeDefs_1 = this.ba.writeDefs(row);
      for(final Definition ne : _writeDefs_1) {
        _builder.append("\t");
        CharSequence _generateConditions = this.ba.generateConditions(row, ne);
        _builder.append(_generateConditions, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Definition> _writeDefs_2 = this.ba.writeDefs(row);
      for(final Definition d : _writeDefs_2) {
        _builder.append("\t");
        _builder.append("pshdl_sim_setInput(");
        String _defineName = this.getDefineName(d.name);
        _builder.append(_defineName, "\t");
        _builder.append(", ");
        _builder.append(d.name, "\t");
        _builder.append(", index);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("if (!disableEdges) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_setInput(");
    String _defineName_1 = this.getDefineName("Bus2IP_Clk");
    _builder.append(_defineName_1, "\t\t");
    _builder.append(", 0, 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_setInput(");
    String _defineName_2 = this.getDefineName("Bus2IP_Clk");
    _builder.append(_defineName_2, "\t");
    _builder.append(", 1, 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//warn(invalidIndex, index, \"\", \"");
    _builder.append(row.name, "\t");
    _builder.append("\", \"\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int set");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_1, "");
    _builder.append("(uint32_t *base, int index, ");
    _builder.append(row.name, "");
    _builder.append("_t *newVal) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return set");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_2, "\t");
    _builder.append("Direct(base, index");
    {
      List<Definition> _writeDefs_3 = this.ba.writeDefs(row);
      for(final Definition d_1 : _writeDefs_3) {
        _builder.append(", newVal->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_1);
        _builder.append(_varNameIndex, "\t");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
