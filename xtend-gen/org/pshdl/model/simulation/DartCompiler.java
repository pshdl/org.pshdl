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
import com.google.common.collect.Lists;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class DartCompiler implements ITypeOuptutProvider {
  @Extension
  private CommonCompilerExtension cce;
  
  private int epsWidth;
  
  public DartCompiler() {
  }
  
  public DartCompiler(final ExecutableModel em) {
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em, (-1));
    this.cce = _commonCompilerExtension;
    int _size = this.cce.prevMap.size();
    int _highestOneBit = Integer.highestOneBit(_size);
    int _plus = (_highestOneBit + 1);
    this.epsWidth = _plus;
  }
  
  public static List<PSAbstractCompiler.CompileResult> doCompile(final ExecutableModel em, final String unitName, final Set<Problem> syntaxProblems) {
    DartCompiler _dartCompiler = new DartCompiler(em);
    final DartCompiler comp = _dartCompiler;
    CharSequence _compile = comp.compile(unitName);
    String _string = _compile.toString();
    List<IHDLGenerator.SideFile> _emptyList = Collections.<IHDLGenerator.SideFile>emptyList();
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, _string, em.moduleName, _emptyList, em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  public CharSequence compile(final String unitName) {
    CharSequence _xblockexpression = null;
    {
      HashSet<Integer> _hashSet = new HashSet<Integer>();
      final Set<Integer> handled = _hashSet;
      handled.add(Integer.valueOf((-1)));
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _imports = this.getImports();
      _builder.append(_imports, "");
      _builder.newLineIfNotEmpty();
      _builder.append("void main(List<String> args, SendPort replyTo){");
      _builder.newLine();
      {
        if (this.cce.hasClock) {
          _builder.append("  ");
          _builder.append("handleReceive((e,l) => new ");
          _builder.append(unitName, "  ");
          _builder.append("(e,l), replyTo);");
          _builder.newLineIfNotEmpty();
        } else {
          _builder.append("  ");
          _builder.append("handleReceive((e,l) => new ");
          _builder.append(unitName, "  ");
          _builder.append("(), replyTo);");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      {
        if (this.cce.hasClock) {
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
        if (this.cce.hasClock) {
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
      _builder.append("int _epsCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int _deltaCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int get updateStamp=>(_deltaCycle << ");
      _builder.append(this.epsWidth, "\t");
      _builder.append(") | (_epsCycle & ");
      CharSequence _asMask = this.cce.asMask(this.epsWidth);
      _builder.append(_asMask, "\t");
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("Map<String, int> _varIdx={");
      _builder.newLine();
      {
        boolean _hasElements = false;
        for(final VariableInformation v_1 : this.cce.em.variables) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "\t\t");
          }
          _builder.append("\t\t");
          _builder.append("\"");
          String _replaceAll = v_1.name.replaceAll("[\\$]", "\\\\\\$");
          _builder.append(_replaceAll, "\t\t");
          _builder.append("\": ");
          Integer _get_1 = this.cce.varIdx.get(v_1.name);
          _builder.append(_get_1, "\t\t");
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
      _builder.append(unitName, "\t");
      _builder.append("(");
      {
        if (this.cce.hasClock) {
          _builder.append("this._disableEdges, this._disabledRegOutputlogic");
        }
      }
      _builder.append(");");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.newLine();
      {
        Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
        for(final VariableInformation v_2 : _excludeNull_1) {
          _builder.append("\t");
          _builder.append("set ");
          String _idName = this.cce.idName(v_2, false, false);
          _builder.append(_idName, "\t");
          _builder.append("(");
          String _dartType = this.dartType(v_2, true);
          _builder.append(_dartType, "\t");
          _builder.append(" value) =>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _idName_1 = this.cce.idName(v_2, false, true);
          _builder.append(_idName_1, "\t\t");
          _builder.append("=value ");
          {
            boolean _and = false;
            boolean _isPredicate = this.cce.isPredicate(v_2);
            boolean _not = (!_isPredicate);
            if (!_not) {
              _and = false;
            } else {
              boolean _isArray = this.cce.isArray(v_2);
              boolean _not_1 = (!_isArray);
              _and = (_not && _not_1);
            }
            if (_and) {
              _builder.append("& ");
              CharSequence _asMask_1 = this.cce.asMask(v_2.width);
              _builder.append(_asMask_1, "\t\t");
            }
          }
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
          _builder.append("\t");
          String _dartType_1 = this.dartType(v_2, true);
          _builder.append(_dartType_1, "\t");
          _builder.append(" get ");
          String _idName_2 = this.cce.idName(v_2, false, false);
          _builder.append(_idName_2, "\t");
          _builder.append(" =>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          String _idName_3 = this.cce.idName(v_2, false, true);
          _builder.append(_idName_3, "\t\t");
          _builder.append(" ");
          {
            boolean _and_1 = false;
            boolean _isPredicate_1 = this.cce.isPredicate(v_2);
            boolean _not_2 = (!_isPredicate_1);
            if (!_not_2) {
              _and_1 = false;
            } else {
              boolean _isArray_1 = this.cce.isArray(v_2);
              boolean _not_3 = (!_isArray_1);
              _and_1 = (_not_2 && _not_3);
            }
            if (_and_1) {
              _builder.append("& ");
              CharSequence _asMask_2 = this.cce.asMask(v_2.width);
              _builder.append(_asMask_2, "\t\t");
            }
          }
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
          {
            boolean _isArray_2 = this.cce.isArray(v_2);
            if (_isArray_2) {
              _builder.append("\t");
              _builder.append("void set");
              String _idName_4 = this.cce.idName(v_2, false, false);
              _builder.append(_idName_4, "\t");
              _builder.append("(");
              String _dartType_2 = this.dartType(v_2, false);
              _builder.append(_dartType_2, "\t");
              _builder.append(" value");
              {
                int _size = IterableExtensions.size(((Iterable<? extends Object>)Conversions.doWrapArray(v_2.dimensions)));
                ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
                for(final Integer i : _doubleDotLessThan) {
                  _builder.append(", int a");
                  _builder.append(i, "\t");
                }
              }
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              String _idName_5 = this.cce.idName(v_2, false, true);
              _builder.append(_idName_5, "\t\t");
              String _arrayAccessBracket = this.cce.arrayAccessBracket(v_2, null);
              _builder.append(_arrayAccessBracket, "\t\t");
              _builder.append("=value & ");
              CharSequence _asMask_3 = this.cce.asMask(v_2.width);
              _builder.append(_asMask_3, "\t\t");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("}");
              _builder.newLine();
              _builder.append("\t");
              _builder.newLine();
              _builder.append("\t");
              String _dartType_3 = this.dartType(v_2, false);
              _builder.append(_dartType_3, "\t");
              _builder.append(" get");
              String _idName_6 = this.cce.idName(v_2, false, false);
              _builder.append(_idName_6, "\t");
              _builder.append("(");
              {
                int _size_1 = IterableExtensions.size(((Iterable<? extends Object>)Conversions.doWrapArray(v_2.dimensions)));
                ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _size_1, true);
                boolean _hasElements_1 = false;
                for(final Integer i_1 : _doubleDotLessThan_1) {
                  if (!_hasElements_1) {
                    _hasElements_1 = true;
                  } else {
                    _builder.appendImmediate(",", "\t");
                  }
                  _builder.append("int a");
                  _builder.append(i_1, "\t");
                }
              }
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("return ");
              String _idName_7 = this.cce.idName(v_2, false, true);
              _builder.append(_idName_7, "\t\t");
              String _arrayAccessBracket_1 = this.cce.arrayAccessBracket(v_2, null);
              _builder.append(_arrayAccessBracket_1, "\t\t");
              _builder.append(" & ");
              CharSequence _asMask_4 = this.cce.asMask(v_2.width);
              _builder.append(_asMask_4, "\t\t");
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
        for(final Frame f : this.cce.em.frames) {
          _builder.append("\t");
          String _method = this.method(f);
          _builder.append(_method, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      {
        if (this.cce.hasClock) {
          _builder.append("\t");
          _builder.append("bool skipEdge(int local) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("int dc = local >> ");
          _builder.append(this.epsWidth, "\t\t");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
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
          _builder.append("if ((dc == _deltaCycle) && ((local & ");
          CharSequence _asMask_5 = this.cce.asMask(this.epsWidth);
          _builder.append(_asMask_5, "\t\t");
          _builder.append(") == _epsCycle))");
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
        }
      }
      _builder.append("\t");
      _builder.append("void run(){");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("_deltaCycle++;");
      _builder.newLine();
      {
        if (this.cce.hasClock) {
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
        for(final Frame f_1 : this.cce.em.frames) {
          {
            boolean _and_2 = false;
            boolean _and_3 = false;
            if (!((f_1.edgeNegDepRes == (-1)) && (f_1.edgePosDepRes == (-1)))) {
              _and_3 = false;
            } else {
              int _length = f_1.predNegDepRes.length;
              boolean _equals = (_length == 0);
              _and_3 = (((f_1.edgeNegDepRes == (-1)) && (f_1.edgePosDepRes == (-1))) && _equals);
            }
            if (!_and_3) {
              _and_2 = false;
            } else {
              int _length_1 = f_1.predPosDepRes.length;
              boolean _equals_1 = (_length_1 == 0);
              _and_2 = (_and_3 && _equals_1);
            }
            if (_and_2) {
              _builder.append("\t\t");
              _builder.append("_frame");
              _builder.append(f_1.uniqueID, "\t\t");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t\t");
              CharSequence _createNegEdge = this.createNegEdge(f_1.edgeNegDepRes, handled);
              _builder.append(_createNegEdge, "\t\t");
              _builder.newLineIfNotEmpty();
              _builder.append("\t\t");
              CharSequence _createPosEdge = this.createPosEdge(f_1.edgePosDepRes, handled);
              _builder.append(_createPosEdge, "\t\t");
              _builder.newLineIfNotEmpty();
              {
                for(final int p : f_1.predNegDepRes) {
                  _builder.append("\t\t");
                  CharSequence _createBooleanPred = this.createBooleanPred(p, handled);
                  _builder.append(_createBooleanPred, "\t\t");
                  _builder.newLineIfNotEmpty();
                }
              }
              {
                for(final int p_1 : f_1.predPosDepRes) {
                  _builder.append("\t\t");
                  CharSequence _createBooleanPred_1 = this.createBooleanPred(p_1, handled);
                  _builder.append(_createBooleanPred_1, "\t\t");
                  _builder.newLineIfNotEmpty();
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
              _builder.append("_frame");
              _builder.append(f_1.uniqueID, "\t\t\t");
              _builder.append("();");
              _builder.newLineIfNotEmpty();
            }
          }
        }
      }
      {
        if (this.cce.hasClock) {
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
        Iterable<VariableInformation> _excludeNull_2 = this.cce.excludeNull(this.cce.em.variables);
        final Function1<VariableInformation,Boolean> _function = new Function1<VariableInformation,Boolean>() {
          public Boolean apply(final VariableInformation it) {
            Boolean _get = DartCompiler.this.cce.prevMap.get(it.name);
            boolean _tripleNotEquals = (_get != null);
            return Boolean.valueOf(_tripleNotEquals);
          }
        };
        Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull_2, _function);
        for(final VariableInformation v_3 : _filter) {
          _builder.append("\t\t");
          String _copyPrev = this.copyPrev(v_3);
          _builder.append(_copyPrev, "\t\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      {
        if (this.cce.hasClock) {
          _builder.append("\t");
          CharSequence _copyRegs = this.copyRegs();
          _builder.append(_copyRegs, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int _srl(int val, int shiftBy, int width){");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("if (val>=0)");
      _builder.newLine();
      _builder.append("\t    ");
      _builder.append("return val>>shiftBy;");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("int opener=1<<(width);");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("int opened=(val - opener) & (opener - 1);");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("return (opened>>shiftBy);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("int signExtend(int val, int width) {");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("var msb=(1<<(width-1));");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("var mask=(1<<width)-1;");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("var twoComplement = -val;");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("if ((val&msb)==0){");
      _builder.newLine();
      _builder.append("\t    ");
      _builder.append("//The MSB is not set, but the stored sign is negative");
      _builder.newLine();
      _builder.append("\t    ");
      _builder.append("if (val>=0)");
      _builder.newLine();
      _builder.append("\t      ");
      _builder.append("return val;");
      _builder.newLine();
      _builder.append("\t    ");
      _builder.append("return twoComplement&mask;");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("if (val<0)");
      _builder.newLine();
      _builder.append("\t    ");
      _builder.append("return val;");
      _builder.newLine();
      _builder.append("\t  ");
      _builder.append("return -(twoComplement&mask);");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _hdlInterpreter = this.hdlInterpreter();
      _builder.append(_hdlInterpreter, "\t");
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
    if ((f.edgeNegDepRes != (-1))) {
      StringConcatenation _builder = new StringConcatenation();
      InternalInformation _asInternal = this.cce.asInternal(f.edgeNegDepRes);
      String _idName = this.cce.idName(_asInternal, false, true);
      _builder.append(_idName, "");
      _builder.append("_isFalling && !");
      InternalInformation _asInternal_1 = this.cce.asInternal(f.edgeNegDepRes);
      String _idName_1 = this.cce.idName(_asInternal_1, false, true);
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
      String _idName_2 = this.cce.idName(_asInternal_2, false, true);
      _builder_1.append(_idName_2, "");
      _builder_1.append("_isRising&& !");
      InternalInformation _asInternal_3 = this.cce.asInternal(f.edgePosDepRes);
      String _idName_3 = this.cce.idName(_asInternal_3, false, 
        true);
      _builder_1.append(_idName_3, "");
      _builder_1.append("_risingIsHandled");
      sb.append(_builder_1);
      first = false;
    }
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
      InternalInformation _asInternal = this.cce.asInternal(id);
      CharSequence _ter = this.getter(_asInternal, false, id, (-1));
      _builder_1.append(_ter, "");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool p");
      _builder_1.append(id, "");
      _builder_1.append("_fresh=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("int up");
      _builder_1.append(id, "");
      _builder_1.append("=");
      InternalInformation _asInternal_1 = this.cce.asInternal(id);
      String _idName = this.cce.idName(_asInternal_1.info, false, true);
      _builder_1.append(_idName, "");
      _builder_1.append("_update;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if ((up");
      _builder_1.append(id, "");
      _builder_1.append(">>");
      _builder_1.append(this.epsWidth, "");
      _builder_1.append(" != _deltaCycle) || ((up");
      _builder_1.append(id, "");
      _builder_1.append("&");
      CharSequence _asMask = this.cce.asMask(this.epsWidth);
      _builder_1.append(_asMask, "");
      _builder_1.append(") != _epsCycle)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      _builder_1.append("p");
      _builder_1.append(id, "\t");
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
      final InternalInformation internal = this.cce.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _idName = this.cce.idName(internal, false, true);
      _builder_1.append(_idName, "");
      _builder_1.append("_isRising=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _idName_1 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_1, "");
      _builder_1.append("_risingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!_disableEdges){");
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
      String _idName_2 = this.cce.idName(internal, false, true);
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
      String _idName_3 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_3, "\t");
      _builder_1.append("_isRising=t");
      _builder_1.append(id, "\t");
      _builder_1.append("==1;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _idName_4 = this.cce.idName(internal.info, false, true);
      _builder_1.append(_idName_4, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_5 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_5, "\t");
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
      final InternalInformation internal = this.cce.asInternal(id);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("bool ");
      String _idName = this.cce.idName(internal, false, true);
      _builder_1.append(_idName, "");
      _builder_1.append("_isFalling=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("bool ");
      String _idName_1 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_1, "");
      _builder_1.append("_fallingIsHandled=false;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if (!_disableEdges){");
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
      String _idName_2 = this.cce.idName(internal, false, true);
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
      String _idName_3 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_3, "\t");
      _builder_1.append("_isFalling=t");
      _builder_1.append(id, "\t");
      _builder_1.append("==0;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("if (skipEdge(");
      String _idName_4 = this.cce.idName(internal.info, false, true);
      _builder_1.append(_idName_4, "");
      _builder_1.append("_update)){");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t");
      String _idName_5 = this.cce.idName(internal, false, true);
      _builder_1.append(_idName_5, "\t");
      _builder_1.append("_fallingIsHandled=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
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
      for(final VariableInformation v : this.cce.em.variables) {
        {
          boolean _isNull = this.cce.isNull(v);
          boolean _not = (!_isNull);
          if (_not) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get = this.cce.varIdx.get(v.name);
            _builder.append(_get, "\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            String _idName = this.cce.idName(v, false, false);
            _builder.append(_idName, "\t\t\t");
            _builder.append("=value");
            {
              boolean _isPredicate = this.cce.isPredicate(v);
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
            Integer _get_1 = this.cce.varIdx.get(v.name);
            _builder.append(_get_1, "\t\t");
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
      for(final VariableInformation v_1 : this.cce.em.variables) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get_2 = this.cce.varIdx.get(v_1.name);
        _builder.append(_get_2, "\t\t");
        _builder.append(": return \"");
        String _replaceAll = v_1.name.replaceAll("[\\$]", "\\\\\\$");
        _builder.append(_replaceAll, "\t\t");
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
      for(final VariableInformation v_2 : this.cce.em.variables) {
        {
          boolean _isPredicate_1 = this.cce.isPredicate(v_2);
          if (_isPredicate_1) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_3 = this.cce.varIdx.get(v_2.name);
            _builder.append(_get_3, "\t\t");
            _builder.append(": return ");
            String _idName_1 = this.cce.idName(v_2, false, false);
            _builder.append(_idName_1, "\t\t");
            _builder.append("?1:0;");
            _builder.newLineIfNotEmpty();
          } else {
            boolean _isNull_1 = this.cce.isNull(v_2);
            if (_isNull_1) {
              _builder.append("\t\t");
              _builder.append("case ");
              Integer _get_4 = this.cce.varIdx.get(v_2.name);
              _builder.append(_get_4, "\t\t");
              _builder.append(": return 0;");
              _builder.newLineIfNotEmpty();
            } else {
              _builder.append("\t\t");
              _builder.append("case ");
              Integer _get_5 = this.cce.varIdx.get(v_2.name);
              _builder.append(_get_5, "\t\t");
              _builder.append(": return ");
              String _idName_2 = this.cce.idName(v_2, false, false);
              _builder.append(_idName_2, "\t\t");
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
    int _size = this.cce.varIdx.size();
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
          boolean _tripleEquals = (it.dir == VariableInformation.Direction.IN);
          return Boolean.valueOf(_tripleEquals);
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.cce.em.variables)), _function);
      boolean _hasElements = false;
      for(final VariableInformation v : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        CharSequence _asPort = this.asPort(v);
        _builder.append(_asPort, "\t");
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
          boolean _tripleEquals = (it.dir == VariableInformation.Direction.INOUT);
          return Boolean.valueOf(_tripleEquals);
        }
      };
      Iterable<VariableInformation> _filter_1 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.cce.em.variables)), _function_1);
      boolean _hasElements_1 = false;
      for(final VariableInformation v_1 : _filter_1) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        CharSequence _asPort_1 = this.asPort(v_1);
        _builder.append(_asPort_1, "\t");
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
          boolean _tripleEquals = (it.dir == VariableInformation.Direction.OUT);
          return Boolean.valueOf(_tripleEquals);
        }
      };
      Iterable<VariableInformation> _filter_2 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.cce.em.variables)), _function_2);
      boolean _hasElements_2 = false;
      for(final VariableInformation v_2 : _filter_2) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        CharSequence _asPort_2 = this.asPort(v_2);
        _builder.append(_asPort_2, "\t");
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
          boolean _tripleEquals = (it.dir == VariableInformation.Direction.INTERNAL);
          return Boolean.valueOf(_tripleEquals);
        }
      };
      Iterable<VariableInformation> _filter_3 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.cce.em.variables)), _function_3);
      boolean _hasElements_3 = false;
      for(final VariableInformation v_3 : _filter_3) {
        if (!_hasElements_3) {
          _hasElements_3 = true;
        } else {
          _builder.appendImmediate(",", "\t");
        }
        _builder.append("\t");
        CharSequence _asPort_3 = this.asPort(v_3);
        _builder.append(_asPort_3, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("], _varIdx, \"");
    _builder.append(this.cce.em.moduleName, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence asPort(final VariableInformation v) {
    CharSequence _xblockexpression = null;
    {
      String dims = "";
      boolean _isArray = this.cce.isArray(v);
      if (_isArray) {
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
            _builder.append(i, "");
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
      String type = "INVALID";
      final VariableInformation.Type _switchValue = v.type;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,VariableInformation.Type.BIT)) {
          _matched=true;
          type = "Port.TYPE_BIT";
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,VariableInformation.Type.INT)) {
          _matched=true;
          type = "Port.TYPE_INT";
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,VariableInformation.Type.UINT)) {
          _matched=true;
          type = "Port.TYPE_UINT";
        }
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("new Port(");
      Integer _get = this.cce.varIdx.get(v.name);
      _builder_1.append(_get, "");
      _builder_1.append(", \"");
      String _replaceAll = v.name.replaceAll("[\\$]", "\\\\\\$");
      _builder_1.append(_replaceAll, "");
      _builder_1.append("\", ");
      _builder_1.append(v.width, "");
      _builder_1.append(", ");
      _builder_1.append(type, "");
      _builder_1.append(dims, "");
      _builder_1.append(clock, "");
      _builder_1.append(reset, "");
      _builder_1.append(")");
      _xblockexpression = (_builder_1);
    }
    return _xblockexpression;
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
              boolean _isArray = this.cce.isArray(v);
              boolean _not = (!_isArray);
              if (_not) {
                _builder.append("\t\t\t");
                String _idName = this.cce.idName(v, false, true);
                _builder.append(_idName, "\t\t\t");
                _builder.append(" = ");
                String _idName_1 = this.cce.idName(v, false, true);
                _builder.append(_idName_1, "\t\t\t");
                _builder.append("$reg; break;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t\t");
                _builder.append("if (reg.offset==-1)");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                String _idName_2 = this.cce.idName(v, false, true);
                _builder.append(_idName_2, "\t\t\t\t");
                _builder.append(".fillRange(0, ");
                int _talSize = this.cce.totalSize(v);
                _builder.append(_talSize, "\t\t\t\t");
                _builder.append(", ");
                String _idName_3 = this.cce.idName(v, false, true);
                _builder.append(_idName_3, "\t\t\t\t");
                _builder.append("$reg[0]);");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                String _idName_4 = this.cce.idName(v, false, true);
                _builder.append(_idName_4, "\t\t\t\t");
                _builder.append("[reg.offset] = ");
                String _idName_5 = this.cce.idName(v, false, true);
                _builder.append(_idName_5, "\t\t\t\t");
                _builder.append("$reg[reg.offset]; ");
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
  
  public String copyPrev(final VariableInformation info) {
    boolean _isArray = this.cce.isArray(info);
    boolean _not = (!_isArray);
    if (_not) {
      StringConcatenation _builder = new StringConcatenation();
      String _idName = this.cce.idName(info, true, true);
      _builder.append(_idName, "");
      _builder.append("=");
      String _idName_1 = this.cce.idName(info, false, true);
      _builder.append(_idName_1, "");
      _builder.append(";");
      return _builder.toString();
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("System.arraycopy(");
    String _idName_2 = this.cce.idName(info, false, true);
    _builder_1.append(_idName_2, "");
    _builder_1.append(",0,");
    String _idName_3 = this.cce.idName(info, true, true);
    _builder_1.append(_idName_3, "");
    _builder_1.append(", 0, ");
    String _idName_4 = this.cce.idName(info, false, 
      true);
    _builder_1.append(_idName_4, "");
    _builder_1.append(".length);");
    return _builder_1.toString();
  }
  
  public CharSequence getter(final InternalInformation info, final boolean prev, final int pos, final int frameID) {
    CharSequence _xblockexpression = null;
    {
      StringBuilder _stringBuilder = new StringBuilder();
      final StringBuilder sb = _stringBuilder;
      final CharSequence mask = this.cce.asMask(info.actualWidth);
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
        varName = ("p" + Integer.valueOf(pos));
      }
      if (prev) {
        varName = (varName + "_prev");
      }
      CharSequence _xifexpression = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          if ((info.actualWidth == info.info.width)) {
            String _dartType = this.dartType(info);
            _builder_2.append(_dartType, "");
            _builder_2.append(" ");
            _builder_2.append(varName, "");
            _builder_2.append("=");
            String _idName = this.cce.idName(info.info, prev, true);
            _builder_2.append(_idName, "");
            _builder_2.append(sb, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              String _dartType_1 = this.dartType(info);
              _builder_2.append(_dartType_1, "");
              _builder_2.append(" ");
              _builder_2.append(varName, "");
              _builder_2.append("=(");
              String _idName_1 = this.cce.idName(info.info, prev, true);
              _builder_2.append(_idName_1, "");
              _builder_2.append(sb, "");
              _builder_2.append(" >> ");
              _builder_2.append(info.bitStart, "");
              _builder_2.append(") & 1;");
              _builder_2.newLineIfNotEmpty();
            } else {
              String _dartType_2 = this.dartType(info);
              _builder_2.append(_dartType_2, "");
              _builder_2.append(" ");
              _builder_2.append(varName, "");
              _builder_2.append("=(");
              String _idName_2 = this.cce.idName(info.info, prev, true);
              _builder_2.append(_idName_2, "");
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
          if ((info.actualWidth == info.info.width)) {
            String _dartType_3 = this.dartType(info);
            _builder_3.append(_dartType_3, "");
            _builder_3.append(" ");
            _builder_3.append(varName, "");
            _builder_3.append("= ");
            String _idName_3 = this.cce.idName(info.info, prev, true);
            _builder_3.append(_idName_3, "");
            _builder_3.append(arrAcc, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              String _dartType_4 = this.dartType(info);
              _builder_3.append(_dartType_4, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("= (");
              String _idName_4 = this.cce.idName(info.info, prev, true);
              _builder_3.append(_idName_4, "");
              _builder_3.append(arrAcc, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitStart, "");
              _builder_3.append(") & 1;");
              _builder_3.newLineIfNotEmpty();
            } else {
              String _dartType_5 = this.dartType(info);
              _builder_3.append(_dartType_5, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("= (");
              String _idName_5 = this.cce.idName(info.info, prev, true);
              _builder_3.append(_idName_5, "");
              _builder_3.append(arrAcc, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitEnd, "");
              _builder_3.append(") & ");
              CharSequence _asMask = this.cce.asMask(info.actualWidth);
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
      final CharSequence maskString = this.cce.toHexString(mask);
      final BigInteger subMask = mask.shiftLeft(info.bitEnd);
      BigInteger _shiftLeft_1 = BigInteger.ONE.shiftLeft(info.info.width);
      final BigInteger fullMask = _shiftLeft_1.subtract(BigInteger.ONE);
      BigInteger _xor = fullMask.xor(subMask);
      final CharSequence writeMask = this.cce.toHexString(_xor);
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
        fixedAccess = ("$reg" + fixedAccess);
        regSuffix = "$reg";
      }
      CharSequence _xifexpression_1 = null;
      if (info.fixedArray) {
        StringConcatenation _builder_2 = new StringConcatenation();
        {
          if ((info.actualWidth == info.info.width)) {
            {
              if (info.isShadowReg) {
                String _dartType = this.dartType(info.info, false);
                _builder_2.append(_dartType, "");
                _builder_2.append(" current=");
                String _idName = this.cce.idName(info.info, false, true);
                _builder_2.append(_idName, "");
                _builder_2.append(fixedAccess, "");
                _builder_2.append(";");
              }
            }
            _builder_2.newLineIfNotEmpty();
            String _idName_1 = this.cce.idName(info.info, false, true);
            _builder_2.append(_idName_1, "");
            _builder_2.append(fixedAccess, "");
            _builder_2.append("=");
            _builder_2.append(value, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            String _dartType_1 = this.dartType(info.info, false);
            _builder_2.append(_dartType_1, "");
            _builder_2.append(" current=");
            String _idName_2 = this.cce.idName(info.info, false, true);
            _builder_2.append(_idName_2, "");
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
            String _idName_3 = this.cce.idName(info.info, false, true);
            _builder_2.append(_idName_3, "");
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
            Integer _get = this.cce.varIdx.get(info.info.name);
            _builder_2.append(_get, "\t");
            _builder_2.append(", ");
            _builder_2.append(off, "\t");
            _builder_2.append("));");
            _builder_2.newLineIfNotEmpty();
          }
        }
        {
          if (info.isPred) {
            String _idName_4 = this.cce.idName(info.info, false, true);
            _builder_2.append(_idName_4, "");
            _builder_2.append("_update=updateStamp;");
          }
        }
        _builder_2.newLineIfNotEmpty();
        _xifexpression_1 = _builder_2;
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        {
          if ((info.actualWidth == info.info.width)) {
            {
              if (info.isShadowReg) {
                String _dartType_2 = this.dartType(info.info, false);
                _builder_3.append(_dartType_2, "");
                _builder_3.append(" current=");
                String _idName_5 = this.cce.idName(info.info, false, true);
                _builder_3.append(_idName_5, "");
                _builder_3.append(regSuffix, "");
                String _arrayAccessBracket = this.cce.arrayAccessBracket(info.info, null);
                _builder_3.append(_arrayAccessBracket, "");
                _builder_3.append(";");
              }
            }
            _builder_3.newLineIfNotEmpty();
            String _idName_6 = this.cce.idName(info.info, false, true);
            _builder_3.append(_idName_6, "");
            _builder_3.append(regSuffix, "");
            String _arrayAccessBracket_1 = this.cce.arrayAccessBracket(info.info, null);
            _builder_3.append(_arrayAccessBracket_1, "");
            _builder_3.append("=");
            _builder_3.append(value, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            String _dartType_3 = this.dartType(info.info, false);
            _builder_3.append(_dartType_3, "");
            _builder_3.append(" current=");
            String _idName_7 = this.cce.idName(info.info, false, true);
            _builder_3.append(_idName_7, "");
            _builder_3.append(regSuffix, "");
            String _arrayAccessBracket_2 = this.cce.arrayAccessBracket(info.info, null);
            _builder_3.append(_arrayAccessBracket_2, "");
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
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
            String _idName_8 = this.cce.idName(info.info, false, true);
            _builder_3.append(_idName_8, "");
            _builder_3.append(regSuffix, "");
            String _arrayAccessBracket_3 = this.cce.arrayAccessBracket(info.info, null);
            _builder_3.append(_arrayAccessBracket_3, "");
            _builder_3.append("=current|");
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
            Integer _get_1 = this.cce.varIdx.get(info.info.name);
            _builder_3.append(_get_1, "\t");
            _builder_3.append(", ");
            StringBuilder _arrayAccess = this.cce.arrayAccess(info.info, null);
            _builder_3.append(_arrayAccess, "\t");
            _builder_3.append("));");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isPred) {
            String _idName_9 = this.cce.idName(info.info, false, true);
            _builder_3.append(_idName_9, "");
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
        this.toExpression(i, frame, sb, pos, a, b, arr, arrPos);
        boolean _tripleNotEquals = (i.inst != Instruction.pushAddIndex);
        if (_tripleNotEquals) {
          pos = (pos + 1);
        }
      }
    }
    Integer _pop = stack.pop();
    final String last = ("t" + _pop);
    InternalInformation _asInternal = this.cce.asInternal(frame.outputId);
    boolean _notEquals = (!Objects.equal(_asInternal.info.name, "#null"));
    if (_notEquals) {
      InternalInformation _asInternal_1 = this.cce.asInternal(frame.outputId);
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
  
  public StringBuilder toExpression(final Frame.FastInstruction inst, final Frame f, final StringBuilder sb, final int pos, final int a, final int b, final List<Integer> arr, final int arrPos) {
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
          final InternalInformation internal = this.cce.asInternal(inst.arg1);
          String name = this.cce.idName(internal, false, true);
          int _size = arr.size();
          InternalInformation _asInternal = this.cce.asInternal(inst.arg1);
          int _length = _asInternal.info.dimensions.length;
          boolean _lessThan = (_size < _length);
          if (_lessThan) {
            StringConcatenation _builder_1 = new StringConcatenation();
            _builder_1.append(name, "");
            _builder_1.append(".fillRange(0, ");
            _builder_1.append(name, "");
            _builder_1.append(".length, t");
            _builder_1.append(a, "");
            _builder_1.append(");");
            _builder_1.newLineIfNotEmpty();
            _builder_1.append("_regUpdates.add(new RegUpdate(");
            Integer _get = this.cce.varIdx.get(internal.info.name);
            _builder_1.append(_get, "");
            _builder_1.append(", -1));");
            _builder_1.newLineIfNotEmpty();
            sb.append(_builder_1);
          } else {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append(name, "");
            String _arrayAccessBracket = this.cce.arrayAccessBracket(internal.info, arr);
            _builder_2.append(_arrayAccessBracket, "");
            _builder_2.append("=t");
            _builder_2.append(a, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("_regUpdates.add(new RegUpdate(");
            Integer _get_1 = this.cce.varIdx.get(internal.info.name);
            _builder_2.append(_get_1, "");
            _builder_2.append(", ");
            {
              boolean _isArray = this.cce.isArray(internal.info);
              if (_isArray) {
                StringBuilder _arrayAccess = this.cce.arrayAccess(internal.info, arr);
                _builder_2.append(_arrayAccess, "");
              } else {
                _builder_2.append("-1");
              }
            }
            _builder_2.append("));");
            _builder_2.newLineIfNotEmpty();
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
          final CharSequence mask = this.cce.asMask(((highBit - lowBit) + 1));
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
          StringConcatenation _builder_8 = new StringConcatenation();
          _builder_8.append("int t");
          _builder_8.append(pos, "");
          _builder_8.append("=");
          StringConcatenation _builder_9 = new StringConcatenation();
          _builder_9.append("t");
          _builder_9.append(a, "");
          int _min = Math.min(targetWidth, currWidth);
          CharSequence _signExtend = this.signExtend(_builder_9, _min);
          _builder_8.append(_signExtend, "");
          _builder_8.append(";");
          sb.append(_builder_8);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.cast_uint)) {
          _matched=true;
          StringConcatenation _builder_10 = new StringConcatenation();
          _builder_10.append("int t");
          _builder_10.append(pos, "");
          _builder_10.append("=t");
          _builder_10.append(a, "");
          _builder_10.append(" & ");
          CharSequence _asMask = this.cce.asMask(inst.arg1);
          _builder_10.append(_asMask, "");
          _builder_10.append(";");
          sb.append(_builder_10);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiNeg)) {
          _matched=true;
          StringConcatenation _builder_11 = new StringConcatenation();
          _builder_11.append("bool t");
          _builder_11.append(pos, "");
          _builder_11.append("=!t");
          _builder_11.append(a, "");
          _builder_11.append(";");
          sb.append(_builder_11);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiAnd)) {
          _matched=true;
          StringConcatenation _builder_12 = new StringConcatenation();
          _builder_12.append("bool t");
          _builder_12.append(pos, "");
          _builder_12.append("=t");
          _builder_12.append(a, "");
          _builder_12.append(" && t");
          _builder_12.append(b, "");
          _builder_12.append(";");
          sb.append(_builder_12);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.logiOr)) {
          _matched=true;
          StringConcatenation _builder_13 = new StringConcatenation();
          _builder_13.append("bool t");
          _builder_13.append(pos, "");
          _builder_13.append("=t");
          _builder_13.append(a, "");
          _builder_13.append(" || t");
          _builder_13.append(b, "");
          _builder_13.append(";");
          sb.append(_builder_13);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const0)) {
          _matched=true;
          StringConcatenation _builder_14 = new StringConcatenation();
          _builder_14.append("int t");
          _builder_14.append(pos, "");
          _builder_14.append("=0;");
          sb.append(_builder_14);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const1)) {
          _matched=true;
          StringConcatenation _builder_15 = new StringConcatenation();
          _builder_15.append("int t");
          _builder_15.append(pos, "");
          _builder_15.append("=1;");
          sb.append(_builder_15);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.const2)) {
          _matched=true;
          StringConcatenation _builder_16 = new StringConcatenation();
          _builder_16.append("int t");
          _builder_16.append(pos, "");
          _builder_16.append("=2;");
          sb.append(_builder_16);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.constAll1)) {
          _matched=true;
          StringConcatenation _builder_17 = new StringConcatenation();
          _builder_17.append("int t");
          _builder_17.append(pos, "");
          _builder_17.append("=");
          CharSequence _asMask_1 = this.cce.asMask(inst.arg1);
          _builder_17.append(_asMask_1, "");
          _builder_17.append(";");
          sb.append(_builder_17);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.concat)) {
          _matched=true;
          StringConcatenation _builder_18 = new StringConcatenation();
          _builder_18.append("int t");
          _builder_18.append(pos, "");
          _builder_18.append("=(t");
          _builder_18.append(b, "");
          _builder_18.append(" << ");
          _builder_18.append(inst.arg2, "");
          _builder_18.append(") | t");
          _builder_18.append(a, "");
          _builder_18.append(";");
          sb.append(_builder_18);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadConstant)) {
          _matched=true;
          StringConcatenation _builder_19 = new StringConcatenation();
          _builder_19.append("int t");
          _builder_19.append(pos, "");
          _builder_19.append("=");
          CharSequence _constant = this.constant(inst.arg1, f);
          _builder_19.append(_constant, "");
          _builder_19.append(";");
          sb.append(_builder_19);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.loadInternal)) {
          _matched=true;
          final InternalInformation internal_1 = this.cce.asInternal(inst.arg1);
          CharSequence _ter = this.getter(internal_1, false, pos, f.uniqueID);
          sb.append(_ter);
          arr.clear();
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.and)) {
          _matched=true;
          this.twoOp(sb, pos, "&", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.or)) {
          _matched=true;
          this.twoOp(sb, pos, "|", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.xor)) {
          _matched=true;
          this.twoOp(sb, pos, "^", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.plus)) {
          _matched=true;
          this.twoOp(sb, pos, "+", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.minus)) {
          _matched=true;
          this.twoOp(sb, pos, "-", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.mul)) {
          _matched=true;
          this.twoOp(sb, pos, "*", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.div)) {
          _matched=true;
          this.twoOp(sb, pos, "~/", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sll)) {
          _matched=true;
          this.twoOp(sb, pos, "<<", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.srl)) {
          _matched=true;
          final int targetSize = (inst.arg1 >> 1);
          int _bitwiseAnd = (inst.arg1 & 1);
          boolean _equals = (_bitwiseAnd == 1);
          if (_equals) {
            StringConcatenation _builder_20 = new StringConcatenation();
            _builder_20.append("int t");
            _builder_20.append(pos, "");
            _builder_20.append("=");
            StringConcatenation _builder_21 = new StringConcatenation();
            _builder_21.append("_srl(t");
            _builder_21.append(b, "");
            _builder_21.append(", t");
            _builder_21.append(a, "");
            _builder_21.append(", ");
            _builder_21.append(inst.arg1, "");
            _builder_21.append(")");
            CharSequence _signExtend_1 = this.signExtend(_builder_21, targetSize);
            _builder_20.append(_signExtend_1, "");
            _builder_20.append(";");
            sb.append(_builder_20);
          } else {
            StringConcatenation _builder_22 = new StringConcatenation();
            _builder_22.append("int t");
            _builder_22.append(pos, "");
            _builder_22.append("=(_srl(t");
            _builder_22.append(b, "");
            _builder_22.append(", t");
            _builder_22.append(a, "");
            _builder_22.append(", ");
            _builder_22.append(inst.arg1, "");
            _builder_22.append(")) & ");
            CharSequence _asMask_2 = this.cce.asMask(targetSize);
            _builder_22.append(_asMask_2, "");
            _builder_22.append(";");
            sb.append(_builder_22);
          }
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.sra)) {
          _matched=true;
          this.twoOp(sb, pos, ">>", a, b, inst.arg1);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.eq)) {
          _matched=true;
          StringConcatenation _builder_23 = new StringConcatenation();
          _builder_23.append("bool t");
          _builder_23.append(pos, "");
          _builder_23.append("=t");
          _builder_23.append(b, "");
          _builder_23.append(" == t");
          _builder_23.append(a, "");
          _builder_23.append(";");
          sb.append(_builder_23);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.not_eq)) {
          _matched=true;
          StringConcatenation _builder_24 = new StringConcatenation();
          _builder_24.append("bool t");
          _builder_24.append(pos, "");
          _builder_24.append("=t");
          _builder_24.append(b, "");
          _builder_24.append(" != t");
          _builder_24.append(a, "");
          _builder_24.append(";");
          sb.append(_builder_24);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less)) {
          _matched=true;
          StringConcatenation _builder_25 = new StringConcatenation();
          _builder_25.append("bool t");
          _builder_25.append(pos, "");
          _builder_25.append("=t");
          _builder_25.append(b, "");
          _builder_25.append(" < t");
          _builder_25.append(a, "");
          _builder_25.append(";");
          sb.append(_builder_25);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.less_eq)) {
          _matched=true;
          StringConcatenation _builder_26 = new StringConcatenation();
          _builder_26.append("bool t");
          _builder_26.append(pos, "");
          _builder_26.append("=t");
          _builder_26.append(b, "");
          _builder_26.append(" <= t");
          _builder_26.append(a, "");
          _builder_26.append(";");
          sb.append(_builder_26);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater)) {
          _matched=true;
          StringConcatenation _builder_27 = new StringConcatenation();
          _builder_27.append("bool t");
          _builder_27.append(pos, "");
          _builder_27.append("=t");
          _builder_27.append(b, "");
          _builder_27.append(" > t");
          _builder_27.append(a, "");
          _builder_27.append(";");
          sb.append(_builder_27);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.greater_eq)) {
          _matched=true;
          StringConcatenation _builder_28 = new StringConcatenation();
          _builder_28.append("bool t");
          _builder_28.append(pos, "");
          _builder_28.append("=t");
          _builder_28.append(b, "");
          _builder_28.append(" >= t");
          _builder_28.append(a, "");
          _builder_28.append(";");
          sb.append(_builder_28);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isRisingEdge)) {
          _matched=true;
          StringConcatenation _builder_29 = new StringConcatenation();
          InternalInformation _asInternal_1 = this.cce.asInternal(inst.arg1);
          String _idName = this.cce.idName(_asInternal_1.info, false, true);
          _builder_29.append(_idName, "");
          _builder_29.append("_update=updateStamp;");
          sb.append(_builder_29);
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,Instruction.isFallingEdge)) {
          _matched=true;
          StringConcatenation _builder_30 = new StringConcatenation();
          InternalInformation _asInternal_2 = this.cce.asInternal(inst.arg1);
          String _idName_1 = this.cce.idName(_asInternal_2.info, false, true);
          _builder_30.append(_idName_1, "");
          _builder_30.append("_update=updateStamp;");
          sb.append(_builder_30);
        }
      }
      StringConcatenation _builder_31 = new StringConcatenation();
      _builder_31.append("//");
      _builder_31.append(inst, "");
      _builder_31.newLineIfNotEmpty();
      StringBuilder _append = sb.append(_builder_31);
      _xblockexpression = (_append);
    }
    return _xblockexpression;
  }
  
  public StringBuilder twoOp(final StringBuilder sb, final int pos, final String op, final int a, final int b, final int targetSizeWithType) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int t");
    _builder.append(pos, "");
    _builder.append("=");
    CharSequence _twoOpValue = this.twoOpValue(op, a, b, targetSizeWithType);
    _builder.append(_twoOpValue, "");
    _builder.append(";");
    StringBuilder _append = sb.append(_builder);
    return _append;
  }
  
  public CharSequence twoOpValue(final String op, final int a, final int b, final int targetSizeWithType) {
    final int targetSize = (targetSizeWithType >> 1);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _equals = (_bitwiseAnd == 1);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("t");
      _builder.append(b, "");
      _builder.append(" ");
      _builder.append(op, "");
      _builder.append(" t");
      _builder.append(a, "");
      return this.signExtend(_builder, targetSize);
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(t");
    _builder_1.append(b, "");
    _builder_1.append(" ");
    _builder_1.append(op, "");
    _builder_1.append(" t");
    _builder_1.append(a, "");
    _builder_1.append(") & ");
    CharSequence _asMask = this.cce.asMask(targetSize);
    _builder_1.append(_asMask, "");
    return _builder_1.toString();
  }
  
  public CharSequence singleOpValue(final String op, final String cast, final int a, final int targetSizeWithType) {
    final int targetSize = (targetSizeWithType >> 1);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _equals = (_bitwiseAnd == 1);
    if (_equals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append(op, "");
      _builder.append(" t");
      _builder.append(a, "");
      return this.signExtend(_builder, targetSize);
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(");
    _builder_1.append(op, "");
    _builder_1.append(" t");
    _builder_1.append(a, "");
    _builder_1.append(") & ");
    CharSequence _asMask = this.cce.asMask(targetSize);
    _builder_1.append(_asMask, "");
    return _builder_1.toString();
  }
  
  public CharSequence signExtend(final CharSequence op, final int size) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("signExtend(");
    _builder.append(op, "");
    _builder.append(", ");
    _builder.append(size, "");
    _builder.append(")");
    return _builder;
  }
  
  public CharSequence constant(final int id, final Frame f) {
    StringConcatenation _builder = new StringConcatenation();
    BigInteger _get = f.constants[id];
    CharSequence _hexString = this.cce.toHexString(_get);
    _builder.append(_hexString, "");
    return _builder;
  }
  
  public String dartType(final InternalInformation ii) {
    return this.dartType(ii.info, false);
  }
  
  public String dartType(final VariableInformation information, final boolean withArray) {
    String jt = "int";
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      jt = "bool";
    }
    boolean _and = false;
    boolean _isArray = this.cce.isArray(information);
    if (!_isArray) {
      _and = false;
    } else {
      _and = (_isArray && withArray);
    }
    if (_and) {
      boolean _equals = Objects.equal(jt, "bool");
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("List<");
        _builder.append(jt, "");
        _builder.append(">");
        return _builder.toString();
      }
      boolean _and_1 = false;
      if (!(information.width <= 8)) {
        _and_1 = false;
      } else {
        boolean _tripleEquals = (information.type == VariableInformation.Type.INT);
        _and_1 = ((information.width <= 8) && _tripleEquals);
      }
      if (_and_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Int8List");
        return _builder_1.toString();
      }
      if ((information.width <= 8)) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("Uint8List");
        return _builder_2.toString();
      }
      boolean _and_2 = false;
      if (!(information.width <= 16)) {
        _and_2 = false;
      } else {
        boolean _tripleEquals_1 = (information.type == VariableInformation.Type.INT);
        _and_2 = ((information.width <= 16) && _tripleEquals_1);
      }
      if (_and_2) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("Int16List");
        return _builder_3.toString();
      }
      if ((information.width <= 16)) {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("Uint16List");
        return _builder_4.toString();
      }
      boolean _and_3 = false;
      if (!(information.width <= 32)) {
        _and_3 = false;
      } else {
        boolean _tripleEquals_2 = (information.type == VariableInformation.Type.INT);
        _and_3 = ((information.width <= 32) && _tripleEquals_2);
      }
      if (_and_3) {
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("Int32List");
        return _builder_5.toString();
      }
      if ((information.width <= 32)) {
        StringConcatenation _builder_6 = new StringConcatenation();
        _builder_6.append("Uint32List");
        return _builder_6.toString();
      }
      boolean _and_4 = false;
      if (!(information.width <= 64)) {
        _and_4 = false;
      } else {
        boolean _tripleEquals_3 = (information.type == VariableInformation.Type.INT);
        _and_4 = ((information.width <= 64) && _tripleEquals_3);
      }
      if (_and_4) {
        StringConcatenation _builder_7 = new StringConcatenation();
        _builder_7.append("Int64List");
        return _builder_7.toString();
      }
      if ((information.width <= 64)) {
        StringConcatenation _builder_8 = new StringConcatenation();
        _builder_8.append("Uint64List");
        return _builder_8.toString();
      }
      StringConcatenation _builder_9 = new StringConcatenation();
      _builder_9.append("List<");
      _builder_9.append(jt, "");
      _builder_9.append(">");
      return _builder_9.toString();
    }
    return jt;
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
          _and = (_tripleNotEquals && (_get_1).booleanValue());
        }
        _or = (_isPredicate || _and);
      }
      if (_or) {
        _builder.append("int ");
        String _idName = this.cce.idName(info, false, true);
        _builder.append(_idName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    String _dartType = this.dartType(info, true);
    _builder.append(_dartType, "");
    _builder.append(" ");
    String _idName_1 = this.cce.idName(info, false, true);
    _builder.append(_idName_1, "");
    _builder.append("=");
    CharSequence _initValue = this.initValue(info);
    _builder.append(_initValue, "");
    _builder.newLineIfNotEmpty();
    {
      boolean _and_1 = false;
      boolean _tripleNotEquals_1 = (includePrev != null);
      if (!_tripleNotEquals_1) {
        _and_1 = false;
      } else {
        _and_1 = (_tripleNotEquals_1 && (includePrev).booleanValue());
      }
      if (_and_1) {
        String _dartType_1 = this.dartType(info, true);
        _builder.append(_dartType_1, "");
        _builder.append(" ");
        String _idName_2 = this.cce.idName(info, true, true);
        _builder.append(_idName_2, "");
        _builder.append("=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        String _dartType_2 = this.dartType(info, true);
        _builder.append(_dartType_2, "");
        _builder.append(" ");
        String _idName_3 = this.cce.idName(info, false, true);
        _builder.append(_idName_3, "");
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
      boolean _isPredicate = this.cce.isPredicate(info);
      if (_isPredicate) {
        _builder.append("false");
      } else {
        boolean _isArray = this.cce.isArray(info);
        if (_isArray) {
          _builder.append("new ");
          String _dartType = this.dartType(info, true);
          _builder.append(_dartType, "");
          _builder.append("(");
          int _talSize = this.cce.totalSize(info);
          _builder.append(_talSize, "");
          _builder.append(")");
        } else {
          _builder.append("0");
        }
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  public CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (this.cce.hasClock) {
        _builder.append("import \'dart:collection\';");
        _builder.newLine();
      }
    }
    _builder.append("import \'dart:typed_data\';");
    _builder.newLine();
    _builder.append("import \'dart:isolate\';");
    _builder.newLine();
    _builder.append("import \'../simulation_comm.dart\';");
    _builder.newLine();
    return _builder;
  }
  
  public String getHookName() {
    return "Dart";
  }
  
  public IOutputProvider.MultiOption getUsage() {
    Options _options = new Options();
    final Options options = _options;
    IOutputProvider.MultiOption _multiOption = new IOutputProvider.MultiOption(null, null, options);
    return _multiOption;
  }
  
  public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
    List<PSAbstractCompiler.CompileResult> _xblockexpression = null;
    {
      final String moduleName = em.moduleName;
      int _lastIndexOf = moduleName.lastIndexOf(".");
      int _plus = (_lastIndexOf + 1);
      int _length = moduleName.length();
      int _minus = (_length - 1);
      final String unitName = moduleName.substring(_plus, _minus);
      List<PSAbstractCompiler.CompileResult> _doCompile = DartCompiler.doCompile(em, unitName, syntaxProblems);
      _xblockexpression = (_doCompile);
    }
    return _xblockexpression;
  }
}
