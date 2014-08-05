package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.EnumSet;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.ICodeGen;

@SuppressWarnings("all")
public class CCodeGenerator extends CommonCodeGenerator implements ICodeGen {
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
      return "uint64_t ";
    } else {
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "bool ";
      }
      return "uint64_t ";
    }
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
        _builder.append("bool skipEdge(uint64_t local) {");
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
    _builder.append("void ");
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
    _builder.append("#include <stdio.h>");
    _builder.newLine();
    _builder.append("#include <time.h>");
    _builder.newLine();
    _builder.append("#include <stdlib.h>");
    _builder.newLine();
    _builder.append("#include <stdarg.h>");
    _builder.newLine();
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
        _builder.append("regUpdate_t regUpdates[");
        int _maxRegUpdates = this.maxRegUpdates();
        _builder.append(_maxRegUpdates, "");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("int regUpdatePos=0;");
        _builder.newLine();
        _builder.newLine();
      }
    }
    _builder.newLine();
    return _builder;
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
    _builder.append("void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence compile(final String packageName, final String unitName) {
    return this.doGenerateMainUnit();
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
  
  public CharSequence createChangeAdapter(final String packageName, final String unitName) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
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
  
  public CharSequence helperMethods() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void pshdl_sim_setInput(int idx, long value, int arrayIdx[]) {");
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
    int _size = this.varIdx.size();
    _builder.append(_size, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("void pshdl_sim_setDisableEdge(bool enable){");
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
    _builder.append("void pshdl_sim_setDisabledRegOutputlogic(bool enable){");
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
    _builder.append("uint64_t pshdl_sim_getOutput(int idx, int arrayIdx[]) {");
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
}
