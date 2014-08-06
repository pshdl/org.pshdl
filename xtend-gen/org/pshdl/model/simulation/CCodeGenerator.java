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

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.SimulationTransformationExtension;
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class CCodeGenerator extends CommonCodeGenerator {
  private CommonCompilerExtension cce;
  
  public CCodeGenerator(final ExecutableModel em, final int maxCosts) {
    super(em, 64, maxCosts);
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em, 64);
    this.cce = _commonCompilerExtension;
  }
  
  protected CharSequence applyRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("updateRegs();");
    return _builder;
  }
  
  protected CharSequence assignArrayInit(final VariableInformation hvar, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _fieldName = this.fieldName(hvar, attributes);
    _builder.append(_fieldName, "");
    _builder.append("[");
    int _arraySize = this.getArraySize(hvar);
    _builder.append(_arraySize, "");
    _builder.append("];");
    return _builder;
  }
  
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger zero, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence checkRegupdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdatePos!=0");
    return _builder;
  }
  
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdatePos=0;");
    return _builder;
  }
  
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    boolean _isBoolean = this.isBoolean(varInfo, attributes);
    if (_isBoolean) {
      return "bool ";
    }
    return "uint64_t ";
  }
  
  protected CharSequence justDeclare(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _fieldName = this.fieldName(varInfo, attributes);
    _builder.append(_fieldName, "");
    {
      boolean _isArray = this.isArray(varInfo);
      if (_isArray) {
        _builder.append("[");
        int _arraySize = this.getArraySize(varInfo);
        _builder.append(_arraySize, "");
        _builder.append("]");
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _helperMethods = this.helperMethods();
    _builder.append(_helperMethods, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence postFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (this.hasClock) {
        _builder.newLineIfNotEmpty();
        _builder.append("static bool skipEdge(uint64_t local) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("uint64_t dc = local >> 16l;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// Register was updated in previous delta cylce, that is ok");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (dc < deltaCycle)");
        _builder.newLine();
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
        _builder.append("if ((dc == deltaCycle) && ((local & 0xFFFF) == epsCycle))");
        _builder.newLine();
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
        CharSequence _copyRegs = this.copyRegs();
        _builder.append(_copyRegs, "");
        _builder.newLineIfNotEmpty();
      }
    }
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
    _builder.append("static void ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#include <stdint.h>");
    _builder.newLine();
    _builder.append("#include <stdbool.h>");
    _builder.newLine();
    _builder.append("#include <string.h>");
    _builder.newLine();
    _builder.append("#include \"pshdl_generic_sim.h\"");
    _builder.newLine();
    _builder.append("#include \"");
    String _headerName = this.headerName();
    _builder.append(_headerName, "");
    _builder.append(".h\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("typedef struct regUpdate {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int internal;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int offset;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("uint64_t fillValue;");
        _builder.newLine();
        _builder.append("} regUpdate_t;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("static regUpdate_t regUpdates[");
        int _maxRegUpdates = this.maxRegUpdates();
        _builder.append(_maxRegUpdates, "");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("static int regUpdatePos=0;");
        _builder.newLine();
        _builder.newLine();
      }
    }
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("static void updateRegs() {");
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
                _builder.append("memset(");
                CharSequence _idName_4 = this.idName(v, true, CommonCodeGenerator.NONE);
                _builder.append(_idName_4, "\t\t\t\t");
                _builder.append(", reg.fillValue, ");
                int _arraySize = this.getArraySize(v);
                _builder.append(_arraySize, "\t\t\t\t");
                _builder.append(");");
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
  
  protected CharSequence writeToNull(final String last) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(void)");
    _builder.append(last, "");
    _builder.append("; //Write to #null");
    _builder.newLineIfNotEmpty();
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
    _builder.append("void ");
    {
      if ((!constant)) {
        _builder.append("pshdl_sim_run");
      } else {
        _builder.append("pshdl_sim_initConstants");
      }
    }
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
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
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("\t\tstatic regUpdate_t reg;");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("\t\treg.internal=");
    int _varIdx = this.getVarIdx(outputInternal);
    _builder.append(_varIdx, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3, "");
    _builder.append("\t\treg.offset=");
    _builder.append(offset, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_4 = this.indent();
    _builder.append(_indent_4, "");
    _builder.append("\t\treg.fillValue=");
    _builder.append(fillValue, "");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_5 = this.indent();
    _builder.append(_indent_5, "");
    _builder.append("\t\tregUpdates[regUpdatePos++]=reg;");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_6 = this.indent();
    _builder.append(_indent_6, "");
    _builder.append("\t}");
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
    _builder.append("static void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence getCast(final int targetSizeWithType) {
    boolean _isSignedType = this.isSignedType(targetSizeWithType);
    if (_isSignedType) {
      return "(int64_t)";
    }
    return "";
  }
  
  protected StringBuilder twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringBuilder _xblockexpression = null;
    {
      boolean _tripleEquals = (fi.inst == Instruction.sra);
      if (_tripleEquals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("((int64_t)");
        String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        _builder.append(_tempName, "");
        _builder.append(") >> ");
        String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
        _builder.append(_tempName_1, "");
        return this.assignTempVar(targetSizeWithType, pos, attributes, _builder);
      }
      boolean _tripleEquals_1 = (fi.inst == Instruction.srl);
      if (_tripleEquals_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        String _tempName_2 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        _builder_1.append(_tempName_2, "");
        _builder_1.append(" >> ");
        String _tempName_3 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
        _builder_1.append(_tempName_3, "");
        return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_1);
      }
      _xblockexpression = super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes);
    }
    return _xblockexpression;
  }
  
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("memcpy(");
    EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isPrev);
    CharSequence _idName = this.idName(varInfo, true, _of);
    _builder.append(_idName, "");
    _builder.append(", ");
    CharSequence _idName_1 = this.idName(varInfo, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1, "");
    _builder.append(", ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence preField(final VariableInformation x, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.isPublic);
      boolean _not = (!_contains);
      if (_not) {
        _builder.append("static");
      }
    }
    _builder.append(" ");
    return _builder;
  }
  
  protected CharSequence helperMethods() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void pshdl_sim_setInput(int idx, uint64_t value) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_setInputArray(idx, value, ((void *)0));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("void pshdl_sim_setInputArray(int idx, uint64_t value, int arrayIdx[]) {");
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
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
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
    _builder.append("uint64_t pshdl_sim_getDeltaCycle(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return deltaCycle;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int pshdl_sim_getVarCount(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return ");
    int _size = this.varIdx.size();
    _builder.append(_size, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void pshdl_sim_setDisableEdges(bool enable){");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append("=enable;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void pshdl_sim_setDisableRegOutputlogic(bool enable){");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append("=enable;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("uint64_t pshdl_sim_getOutput(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return pshdl_sim_getOutputArray(idx, ((void *)0));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("uint64_t pshdl_sim_getOutputArray(int idx, int arrayIdx[]) {");
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
  
  protected CharSequence barrier() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    _builder.append("#pragma omp section");
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean createConstant) {
    CharSequence _xblockexpression = null;
    {
      int _indent = this.indent;
      indent = (_indent + 2);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("#pragma omp parallel sections");
      _builder.newLine();
      CharSequence _indent_1 = this.indent();
      _builder.append(_indent_1, "");
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      CharSequence _indent_2 = this.indent();
      _builder.append(_indent_2, "");
      _builder.append("#pragma omp section");
      _builder.newLineIfNotEmpty();
      CharSequence _indent_3 = this.indent();
      _builder.append(_indent_3, "");
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean createConstant) {
    CharSequence _xblockexpression = null;
    {
      int _indent = this.indent;
      indent = (_indent - 2);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      CharSequence _indent_1 = this.indent();
      _builder.append(_indent_1, "");
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public Iterable<AuxiliaryContent> getAuxiliaryContent() {
    try {
      InputStream _resourceAsStream = CCodeGenerator.class.getResourceAsStream("/org/pshdl/model/simulation/includes/pshdl_generic_sim.h");
      final AuxiliaryContent generic_h = new AuxiliaryContent("pshdl_generic_sim.h", _resourceAsStream, true);
      String _headerName = this.headerName();
      String _plus = (_headerName + ".h");
      CharSequence _specificHeader = this.getSpecificHeader();
      String _string = _specificHeader.toString();
      final AuxiliaryContent specific_h = new AuxiliaryContent(_plus, _string);
      final ArrayList<AuxiliaryContent> res = Lists.<AuxiliaryContent>newArrayList(generic_h, specific_h);
      final String simEncapsulation = this.generateSimEncapsuation();
      boolean _tripleNotEquals = (simEncapsulation != null);
      if (_tripleNotEquals) {
        AuxiliaryContent _auxiliaryContent = new AuxiliaryContent("simEncapsulation.c", simEncapsulation);
        res.add(_auxiliaryContent);
      }
      return res;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected String headerName() {
    CharSequence _idName = this.idName(this.em.moduleName, false, CommonCodeGenerator.NONE);
    String _plus = ("pshdl_" + _idName);
    return (_plus + "_sim");
  }
  
  protected CharSequence getSpecificHeader() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("#ifndef _");
    String _headerName = this.headerName();
    _builder.append(_headerName, "");
    _builder.append("_h_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define _");
    String _headerName_1 = this.headerName();
    _builder.append(_headerName_1, "");
    _builder.append("_h_");
    _builder.newLineIfNotEmpty();
    _builder.append("#include \"pshdl_generic_sim.h\"");
    _builder.newLine();
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation vi : _excludeNull) {
        _builder.append("#define ");
        CharSequence _defineName = this.getDefineName(vi);
        _builder.append(_defineName, "");
        _builder.append(" ");
        int _varIdx = this.getVarIdx(vi);
        _builder.append(_varIdx, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    CharSequence _fieldDeclarations = this.fieldDeclarations(false, false);
    String _string = _fieldDeclarations.toString();
    String[] _split = _string.split("\n");
    final Function1<String, String> _function = new Function1<String, String>() {
      public String apply(final String it) {
        return ("extern" + it);
      }
    };
    List<String> _map = ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(_split)), _function);
    String _join = IterableExtensions.join(_map, "\n");
    _builder.append(_join, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif");
    _builder.newLine();
    return _builder;
  }
  
  public String generateSimEncapsuation() {
    final Unit unit = this.getUnit(this.em);
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
      boolean _tripleNotEquals = (this.em.annotations != null);
      if (_tripleNotEquals) {
        for (final String a : this.em.annotations) {
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
        List<Definition> _allDefs = CCodeGenerator.this.ba.allDefs(it);
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
    _builder.append("#include \"pshdl_generic_sim.h\"");
    _builder.newLine();
    _builder.append("#include \"");
    String _headerName = this.headerName();
    _builder.append(_headerName, "");
    _builder.append(".h\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("static void defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){");
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
    _builder.append("#define ");
    _builder.append("busclk_idx", "");
    _builder.append(" ");
    int _busIndex = this.getBusIndex();
    _builder.append(_busIndex, "");
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
  
  protected int getBusIndex() {
    final Integer pclk = this.varIdx.get((this.em.moduleName + ".PCLK"));
    boolean _tripleEquals = (pclk == null);
    if (_tripleEquals) {
      return (this.varIdx.get((this.em.moduleName + ".Bus2IP_Clk"))).intValue();
    }
    return 0;
  }
  
  protected CharSequence getDefineName(final VariableInformation vi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("PSHDL_SIM_");
    CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
    String _string = _idName.toString();
    String _upperCase = _string.toUpperCase();
    _builder.append(_upperCase, "");
    return _builder;
  }
  
  protected CharSequence getDefineNameString(final String s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("PSHDL_SIM_");
    CharSequence _idName = this.idName(((this.em.moduleName + ".") + s), true, CommonCodeGenerator.NONE);
    String _string = _idName.toString();
    String _upperCase = _string.toUpperCase();
    _builder.append(_upperCase, "");
    return _builder;
  }
  
  protected CharSequence simGetter(final Row row) {
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
    _builder.append("\t");
    _builder.append("int offset[1]={index};");
    _builder.newLine();
    {
      List<Definition> _allDefs_1 = this.ba.allDefs(row);
      for(final Definition d : _allDefs_1) {
        _builder.append("\t");
        _builder.append("*");
        String _varName = this.ba.getVarName(row, d);
        _builder.append(_varName, "\t");
        _builder.append("=(");
        CharSequence _busType = this.ba.getBusType(d);
        _builder.append(_busType, "\t");
        _builder.append(")pshdl_sim_getOutputArray(");
        CharSequence _defineNameString = this.getDefineNameString(d.name);
        _builder.append(_defineNameString, "\t");
        _builder.append(", offset);");
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
  
  protected CharSequence simSetter(final Row row) {
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
    _builder.append("\t");
    _builder.append("int offset[1]={index};");
    _builder.newLine();
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
        _builder.append("pshdl_sim_setInputArray(");
        CharSequence _defineNameString = this.getDefineNameString(d.name);
        _builder.append(_defineNameString, "\t");
        _builder.append(", ");
        _builder.append(d.name, "\t");
        _builder.append(", offset);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("if (!");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_setInput(busclk_idx, 0);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_setInput(busclk_idx, 1);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
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
  
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence callMethod(final String methodName, final String... args) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence callRunMethod() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence checkTestbenchListener() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  protected CharSequence runTestbenchHeader() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
}
