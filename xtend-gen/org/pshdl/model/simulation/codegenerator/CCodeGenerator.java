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
package org.pshdl.model.simulation.codegenerator;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.FunctionInformation;
import org.pshdl.interpreter.IHDLInterpreterFactory;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.NativeRunner;
import org.pshdl.interpreter.ParameterInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.simulation.SimulationTransformationExtension;
import org.pshdl.model.simulation.codegenerator.CCodeGeneratorParameter;
import org.pshdl.model.simulation.codegenerator.CommonCodeGenerator;
import org.pshdl.model.simulation.codegenerator.CommonCompilerExtension;
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class CCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
  private CommonCompilerExtension cce;
  
  private boolean hasPow = false;
  
  public static String COMPILER = "/usr/bin/clang";
  
  public CCodeGenerator() {
  }
  
  public CCodeGenerator(final CCodeGeneratorParameter parameter) {
    super(parameter);
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(this.em, 64);
    this.cce = _commonCompilerExtension;
  }
  
  public IHDLInterpreterFactory<NativeRunner> createInterpreter(final File tempDir, final NativeRunner.IRunListener listener) {
    try {
      final File testCFile = new File(tempDir, "test.c");
      Files.write(this.generateMainCode(), testCFile, StandardCharsets.UTF_8);
      final File testRunner = new File(tempDir, "runner.c");
      this.copyFile("/org/pshdl/model/simulation/includes/runner.c", testRunner);
      final File testGenericLibHeader = new File(tempDir, "pshdl_generic_sim.h");
      this.copyFile("/org/pshdl/model/simulation/includes/pshdl_generic_sim.h", testGenericLibHeader);
      final File executable = new File(tempDir, "testExec");
      this.writeAuxiliaryContents(tempDir);
      String _absolutePath = tempDir.getAbsolutePath();
      String _absolutePath_1 = testCFile.getAbsolutePath();
      String _absolutePath_2 = testRunner.getAbsolutePath();
      String _absolutePath_3 = executable.getAbsolutePath();
      final ProcessBuilder builder = new ProcessBuilder(CCodeGenerator.COMPILER, "-I", _absolutePath, "-O3", _absolutePath_1, _absolutePath_2, "-o", _absolutePath_3);
      final Process process = builder.directory(tempDir).inheritIO().start();
      process.waitFor();
      final int exitValue = process.exitValue();
      if ((exitValue != 0)) {
        throw new RuntimeException(("Process did not terminate with 0, was " + Integer.valueOf(exitValue)));
      }
      return new IHDLInterpreterFactory<NativeRunner>() {
        @Override
        public NativeRunner newInstance() {
          try {
            String _absolutePath = executable.getAbsolutePath();
            final ProcessBuilder execBuilder = new ProcessBuilder(_absolutePath);
            final Process testExec = execBuilder.directory(tempDir).redirectErrorStream(true).start();
            InputStream _inputStream = testExec.getInputStream();
            OutputStream _outputStream = testExec.getOutputStream();
            String _absolutePath_1 = executable.getAbsolutePath();
            return new NativeRunner(_inputStream, _outputStream, CCodeGenerator.this.em, testExec, 5, _absolutePath_1, listener);
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public long copyFile(final String fileToCopy, final File testRunner) {
    try {
      long _xblockexpression = (long) 0;
      {
        final InputStream runnerStream = CCodeGenerator.class.getResourceAsStream(fileToCopy);
        final FileOutputStream fos = new FileOutputStream(testRunner);
        long _xtrycatchfinallyexpression = (long) 0;
        try {
          _xtrycatchfinallyexpression = ByteStreams.copy(runnerStream, fos);
        } finally {
          runnerStream.close();
          fos.close();
        }
        _xblockexpression = _xtrycatchfinallyexpression;
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  protected CharSequence applyRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("updateRegs();");
    return _builder;
  }
  
  @Override
  protected CharSequence assignArrayInit(final VariableInformation hvar, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _fieldName = this.fieldName(hvar, attributes);
    _builder.append(_fieldName);
    _builder.append("[");
    int _arraySize = this.getArraySize(hvar);
    _builder.append(_arraySize);
    _builder.append("];");
    return _builder;
  }
  
  @Override
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger zero, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName);
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence checkRegupdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdatePos!=0");
    return _builder;
  }
  
  @Override
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdatePos=0;");
    return _builder;
  }
  
  @Override
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    boolean _equals = Objects.equal(varInfo.type, VariableInformation.Type.STRING);
    if (_equals) {
      return "char*";
    }
    boolean _isBoolean = this.isBoolean(varInfo, attributes);
    if (_isBoolean) {
      return "bool";
    }
    return "uint64_t";
  }
  
  @Override
  protected CharSequence justDeclare(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _fieldName = this.fieldName(varInfo, attributes);
    _builder.append(_fieldName);
    {
      boolean _isArray = this.isArray(varInfo);
      if (_isArray) {
        _builder.append("[");
        int _arraySize = this.getArraySize(varInfo);
        _builder.append(_arraySize);
        _builder.append("]");
      }
    }
    _builder.append(";");
    return _builder;
  }
  
  @Override
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _helperMethods = this.helperMethods();
    _builder.append(_helperMethods);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
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
        _builder.append(_copyRegs);
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  @Override
  protected CharSequence functionFooter(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence functionHeader(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("static void ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName);
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
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
    _builder.append(_headerName);
    _builder.append(".h\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    StringBuilder _generateInlineMethods = this.generateInlineMethods();
    _builder.append(_generateInlineMethods);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("/// Don\'t use this");
        _builder.newLine();
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
        _builder.append(_maxRegUpdates);
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
  
  public StringBuilder generateInlineMethods() {
    final StringBuilder sb = new StringBuilder();
    for (final FunctionInformation fi : this.em.functions) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("extern ");
      String _c = this.toC(fi.returnType);
      _builder.append(_c);
      _builder.append(" ");
      String _signature = fi.signature();
      _builder.append(_signature);
      _builder.append("(");
      {
        boolean _hasElements = false;
        for(final ParameterInformation pi : fi.parameter) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(", ", "");
          }
          String _c_1 = this.toC(pi);
          _builder.append(_c_1);
          _builder.append(" p");
          String _firstUpper = StringExtensions.toFirstUpper(pi.name);
          _builder.append(_firstUpper);
        }
      }
      _builder.append(");");
      sb.append(_builder);
    }
    return sb;
  }
  
  public String toC(final ParameterInformation information) {
    if ((information == null)) {
      return "void";
    }
    if ((information.type == ParameterInformation.Type.PARAM_BOOL)) {
      return "bool";
    }
    if ((information.type == ParameterInformation.Type.PARAM_STRING)) {
      return "const char*";
    }
    return "uint64_t";
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
    _builder.append("\t\t\t");
    CharSequence _updateRegCases = this.updateRegCases();
    _builder.append(_updateRegCases, "\t\t\t");
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
  
  public static int hash(final String str) {
    int hash = (-2128831035);
    final byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
    for (final byte b : bytes) {
      {
        hash = (hash ^ b);
        hash = (hash * 16777619);
      }
    }
    return hash;
  }
  
  @Override
  protected CharSequence runMethodsFooter(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
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
  
  @Override
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean force, final CharSequence fillValue) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((!force)) {
        _builder.append("if (");
        _builder.append(cpyName);
        _builder.append("!=");
        _builder.append(last);
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        CharSequence _indent = this.indent();
        _builder.append(_indent);
        _builder.append("\t");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1);
    _builder.append("\t\tstatic regUpdate_t reg;");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2);
    _builder.append("\t\treg.internal=");
    Integer _regIdx = this.regIdx(outputInternal);
    _builder.append(_regIdx);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3);
    _builder.append("\t\treg.offset=(int)(");
    _builder.append(offset);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_4 = this.indent();
    _builder.append(_indent_4);
    _builder.append("\t\treg.fillValue=");
    _builder.append(fillValue);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_5 = this.indent();
    _builder.append(_indent_5);
    _builder.append("\t\tregUpdates[regUpdatePos++]=reg;");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_6 = this.indent();
    _builder.append(_indent_6);
    _builder.append("\t}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public Integer regIdx(final InternalInformation information) {
    return this.regIdx.get(information.info.name);
  }
  
  @Override
  protected CharSequence stageMethodsFooter(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence stageMethodsHeader(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("static void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName);
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence getCast(final int targetSizeWithType) {
    boolean _isSignedType = this.isSignedType(targetSizeWithType);
    if (_isSignedType) {
      return "(int64_t)";
    }
    return "";
  }
  
  @Override
  protected CharSequence twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    CharSequence _xblockexpression = null;
    {
      final VariableInformation.Type type = this.typeFromTargetSize(targetSizeWithType);
      final Instruction _switchValue = fi.inst;
      if (_switchValue != null) {
        switch (_switchValue) {
          case sra:
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("((int64_t)");
            String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder.append(_tempName);
            _builder.append(") >> ");
            String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder.append(_tempName_1);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder, true);
          case srl:
            StringConcatenation _builder_1 = new StringConcatenation();
            String _tempName_2 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder_1.append(_tempName_2);
            _builder_1.append(" >> ");
            String _tempName_3 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder_1.append(_tempName_3);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder_1, true);
          case less:
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append("(int64_t)");
            String _tempName_4 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder_2.append(_tempName_4);
            _builder_2.append(" < (int64_t)");
            String _tempName_5 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder_2.append(_tempName_5);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder_2, true);
          case less_eq:
            StringConcatenation _builder_3 = new StringConcatenation();
            _builder_3.append("(int64_t)");
            String _tempName_6 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder_3.append(_tempName_6);
            _builder_3.append(" <= (int64_t)");
            String _tempName_7 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder_3.append(_tempName_7);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder_3, true);
          case greater:
            StringConcatenation _builder_4 = new StringConcatenation();
            _builder_4.append("(int64_t)");
            String _tempName_8 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder_4.append(_tempName_8);
            _builder_4.append(" > (int64_t)");
            String _tempName_9 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder_4.append(_tempName_9);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder_4, true);
          case greater_eq:
            StringConcatenation _builder_5 = new StringConcatenation();
            _builder_5.append("(int64_t)");
            String _tempName_10 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
            _builder_5.append(_tempName_10);
            _builder_5.append(" >= (int64_t)");
            String _tempName_11 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
            _builder_5.append(_tempName_11);
            return this.assignTempVar(type, targetSizeWithType, pos, attributes, _builder_5, true);
          default:
            break;
        }
      } else {
      }
      _xblockexpression = super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask);
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("memcpy(");
    CharSequence _idName = this.idName(varInfo, true, 
      EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isPrev));
    _builder.append(_idName);
    _builder.append(", ");
    CharSequence _idName_1 = this.idName(varInfo, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1);
    _builder.append(", ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize);
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
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
    _builder.append("void pshdl_sim_setInput(uint32_t idx, uint64_t value) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_setInputArray(idx, value, 0);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("void pshdl_sim_setInputArray(uint32_t idx, uint64_t value, uint32_t offset) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _setInputCases = this.setInputCases("value", null, EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.useArrayOffset));
    _builder.append(_setInputCases, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("char* pshdl_sim_getName(uint32_t idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.em.variables) {
        _builder.append("\t\t");
        _builder.append("case ");
        int _varIdx = this.getVarIdx(v, false);
        _builder.append(_varIdx, "\t\t");
        _builder.append(": return \"");
        _builder.append(v.name, "\t\t");
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
    _builder.append(_jSONDescription);
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
    _builder.append("uint32_t pshdl_sim_getVarCount(){");
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
    _builder.append("static uint32_t hash(char* str){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("size_t len=strlen(str);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("uint32_t hash = 2166136261;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int i;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (i=0;i<len;i++){");
    _builder.newLine();
    _builder.append("\t   \t");
    _builder.append("hash = hash ^ str[i];");
    _builder.newLine();
    _builder.append("\t   \t");
    _builder.append("hash = hash * 16777619;");
    _builder.newLine();
    _builder.append("\t   ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return hash;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("int pshdl_sim_getIndex(char* name) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("uint32_t hashName=hash(name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (hashName) {");
    _builder.newLine();
    {
      Set<Map.Entry<Integer, List<VariableInformation>>> _entrySet = this.getHashed(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables))).entrySet();
      for(final Map.Entry<Integer, List<VariableInformation>> e : _entrySet) {
        _builder.append("\t\t");
        _builder.append("case ");
        CharSequence _constant32Bit = this.constant32Bit((e.getKey()).intValue());
        _builder.append(_constant32Bit, "\t\t");
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        {
          List<VariableInformation> _value = e.getValue();
          for(final VariableInformation vi : _value) {
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("if (strcmp(name, \"");
            _builder.append(vi.name, "\t\t\t");
            _builder.append("\") == 0)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return ");
            int _varIdx_1 = this.getVarIdx(vi, this.purgeAliases);
            _builder.append(_varIdx_1, "\t\t\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("return -1; //so close...");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return -1;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("uint64_t pshdl_sim_getOutput(uint32_t idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return pshdl_sim_getOutputArray(idx, 0);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("uint64_t pshdl_sim_getOutputArray(uint32_t idx, uint32_t offset) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _outputCases = this.getOutputCases(null, EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.useArrayOffset));
    _builder.append(_outputCases, "\t\t");
    _builder.newLineIfNotEmpty();
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
      if (this.hasPow) {
        _builder.append("static uint64_t pshdl_sim_pow(uint64_t a, uint64_t n){");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("uint64_t result = 1;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("uint64_t p = a;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("while (n > 0){");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("if ((n % 2) != 0)");
        _builder.newLine();
        _builder.append("            ");
        _builder.append("result = result * p;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("p = p * p;");
        _builder.newLine();
        _builder.append("        ");
        _builder.append("n = n / 2;");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("}");
        _builder.newLine();
        _builder.append("    ");
        _builder.append("return result;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public Map<Integer, List<VariableInformation>> getHashed(final Iterable<VariableInformation> informations) {
    final Map<Integer, List<VariableInformation>> res = Maps.<Integer, List<VariableInformation>>newLinkedHashMap();
    for (final VariableInformation vi : this.em.variables) {
      {
        final int hashVal = CCodeGenerator.hash(vi.name);
        final List<VariableInformation> list = res.get(Integer.valueOf(hashVal));
        if ((list == null)) {
          res.put(Integer.valueOf(hashVal), Lists.<VariableInformation>newArrayList(vi));
        } else {
          list.add(vi);
        }
      }
    }
    return res;
  }
  
  @Override
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
  
  @Override
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean createConstant) {
    CharSequence _xblockexpression = null;
    {
      int _indent = this.indent;
      this.indent = (_indent + 2);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("#pragma omp parallel sections");
      _builder.newLine();
      CharSequence _indent_1 = this.indent();
      _builder.append(_indent_1);
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      CharSequence _indent_2 = this.indent();
      _builder.append(_indent_2);
      _builder.append("#pragma omp section");
      _builder.newLineIfNotEmpty();
      CharSequence _indent_3 = this.indent();
      _builder.append(_indent_3);
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean createConstant) {
    CharSequence _xblockexpression = null;
    {
      int _indent = this.indent;
      this.indent = (_indent - 2);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      CharSequence _indent_1 = this.indent();
      _builder.append(_indent_1);
      _builder.append("}");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  @Override
  public Iterable<AuxiliaryContent> getAuxiliaryContent() {
    try {
      final InputStream generic_hStream = CCodeGenerator.class.getResourceAsStream(
        "/org/pshdl/model/simulation/includes/pshdl_generic_sim.h");
      try {
        final AuxiliaryContent generic_h = new AuxiliaryContent("pshdl_generic_sim.h", generic_hStream, true);
        String _headerName = this.headerName();
        String _plus = (_headerName + ".h");
        String _string = this.getSpecificHeader().toString();
        final AuxiliaryContent specific_h = new AuxiliaryContent(_plus, _string);
        final ArrayList<AuxiliaryContent> res = Lists.<AuxiliaryContent>newArrayList(generic_h, specific_h);
        final String simEncapsulation = this.generateSimEncapsuation();
        if ((simEncapsulation != null)) {
          AuxiliaryContent _auxiliaryContent = new AuxiliaryContent("simEncapsulation.c", simEncapsulation);
          res.add(_auxiliaryContent);
        }
        return res;
      } finally {
        generic_hStream.close();
      }
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
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @file");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @brief Provides access to all fields and their index.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#ifndef _");
    String _headerName = this.headerName();
    _builder.append(_headerName);
    _builder.append("_h_");
    _builder.newLineIfNotEmpty();
    _builder.append("#define _");
    String _headerName_1 = this.headerName();
    _builder.append(_headerName_1);
    _builder.append("_h_");
    _builder.newLineIfNotEmpty();
    _builder.append("#include \"pshdl_generic_sim.h\"");
    _builder.newLine();
    _builder.newLine();
    {
      for(final VariableInformation vi : this.em.variables) {
        _builder.append("///Use this index define to access <tt> ");
        String _replaceAll = vi.name.replaceAll("\\@", "\\\\@");
        _builder.append(_replaceAll);
        _builder.append(" </tt> via getOutput/setInput methods");
        _builder.newLineIfNotEmpty();
        _builder.append("#define ");
        CharSequence _defineName = this.getDefineName(vi);
        _builder.append(_defineName);
        _builder.append(" ");
        int _varIdx = this.getVarIdx(vi, this.purgeAliases);
        _builder.append(_varIdx);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    final Function1<String, String> _function = new Function1<String, String>() {
      @Override
      public String apply(final String it) {
        return ("extern" + it);
      }
    };
    String _join = IterableExtensions.join(ListExtensions.<String, String>map(((List<String>)Conversions.doWrapArray(this.fieldDeclarations(false, false).toString().split("\n"))), _function), "\n");
    _builder.append(_join);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif");
    _builder.newLine();
    return _builder;
  }
  
  public String generateSimEncapsuation() {
    final Unit unit = this.getUnit(this.em);
    if ((unit == null)) {
      return null;
    }
    return this.generateSimEncapsuation(unit, MemoryModel.buildRows(unit));
  }
  
  public Unit getUnit(final ExecutableModel model) {
    try {
      Unit unit = null;
      final Splitter annoSplitter = Splitter.on(SimulationTransformationExtension.ANNO_VALUE_SEP);
      if ((this.em.annotations != null)) {
        for (final String a : this.em.annotations) {
          boolean _startsWith = a.startsWith("busDescription");
          if (_startsWith) {
            final String value = IterableExtensions.<String>last(annoSplitter.limit(2).split(a));
            LinkedHashSet<Problem> _linkedHashSet = new LinkedHashSet<Problem>();
            unit = MemoryModelAST.parseUnit(value, _linkedHashSet, 0);
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
    final Set<String> varNames = new LinkedHashSet<String>();
    final Consumer<Row> _function = new Consumer<Row>() {
      @Override
      public void accept(final Row it) {
        final Function1<Definition, Boolean> _function = new Function1<Definition, Boolean>() {
          @Override
          public Boolean apply(final Definition it) {
            return Boolean.valueOf((it.type != Definition.Type.UNUSED));
          }
        };
        final Consumer<Definition> _function_1 = new Consumer<Definition>() {
          @Override
          public void accept(final Definition it) {
            varNames.add(it.getName());
          }
        };
        IterableExtensions.<Definition>filter(CCodeGenerator.this.ba.allDefs(it), _function).forEach(_function_1);
      }
    };
    rows.forEach(_function);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @file");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @brief  Provides methods for simulating accessing to the memory registers");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This file is a substitue for the BusAccess.c file that is used to access real memory.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* For each type of row there are methods for setting/getting the values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* either directly, or as a struct. A memory map overview has been");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* generated into BusMap.html.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
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
    _builder.append("#include \"");
    String _headerName = this.headerName();
    _builder.append(_headerName);
    _builder.append(".h\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This method provides a null implementation of the warning functionality. You");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* can use it to provide your own error handling, or you can use the implementation");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* provided in BusPrint.h");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("static void defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("warnFunc_p warn=defaultWarn;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This methods allows the user to set a custom warning function. Usually this is used");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* in conjunction with the implementation provided in BusPrint.h.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param warnFunction the new function to use for error reporting");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Example Usage:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @code");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*    #include \"BusPrint.h\"");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*    setWarn(defaultPrintfWarn);");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @endcode");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("void setWarn(warnFunc_p warnFunction){");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("warn=warnFunction;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("///The index of the Clock that is toggled for each setting");
    _builder.newLine();
    _builder.append("#define ");
    _builder.append("busclk_idx");
    _builder.append(" ");
    int _busIndex = this.getBusIndex();
    _builder.append(_busIndex);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    String res = _builder.toString();
    final LinkedHashSet<String> checkedRows = new LinkedHashSet<String>();
    final LinkedHashMap<String, Integer> rowCounts = new LinkedHashMap<String, Integer>();
    for (final Row row : rows) {
      {
        final Integer idx = rowCounts.get(row.getName());
        if ((idx == null)) {
          rowCounts.put(row.getName(), Integer.valueOf(1));
        } else {
          rowCounts.put(row.getName(), Integer.valueOf(((idx).intValue() + 1)));
        }
      }
    }
    for (final Row row_1 : rows) {
      boolean _contains = checkedRows.contains(row_1.getName());
      boolean _not = (!_contains);
      if (_not) {
        boolean _hasWriteDefs = this.ba.hasWriteDefs(row_1);
        if (_hasWriteDefs) {
          CharSequence _simSetter = this.simSetter(row_1, (rowCounts.get(row_1.getName())).intValue());
          String _plus = (res + _simSetter);
          res = _plus;
        }
        CharSequence _simGetter = this.simGetter(row_1, (rowCounts.get(row_1.getName())).intValue());
        String _plus_1 = (res + _simGetter);
        res = _plus_1;
        checkedRows.add(row_1.getName());
      }
    }
    return res;
  }
  
  protected int getBusIndex() {
    final Integer pclk = this.varIdx.get((this.em.moduleName + ".PCLK"));
    if ((pclk == null)) {
      return (this.varIdx.get((this.em.moduleName + ".Bus2IP_Clk"))).intValue();
    }
    return (pclk).intValue();
  }
  
  protected CharSequence getDefineName(final VariableInformation vi) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("PSHDL_SIM_");
    String _upperCase = this.idName(vi, true, CommonCodeGenerator.NONE).toString().toUpperCase();
    _builder.append(_upperCase);
    return _builder;
  }
  
  protected CharSequence getDefineNameString(final String s) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("PSHDL_SIM_");
    String _upperCase = this.idName(((this.em.moduleName + ".") + s), true, CommonCodeGenerator.NONE).toString().toUpperCase();
    _builder.append(_upperCase);
    return _builder;
  }
  
  protected CharSequence simGetter(final Row row, final int rowCount) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Directly retrieve the fields of row ");
    String _name = row.getName();
    _builder.append(_name, " ");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param index the row that you want to access. ");
    {
      if ((rowCount == 1)) {
        _builder.append("The only valid index is 0");
      } else {
        _builder.append("Valid values are 0..");
        _builder.append((rowCount - 
          1), " ");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _allDefs = this.ba.allDefs(row);
      for(final Definition d : _allDefs) {
        _builder.append(" ");
        _builder.append("* @param ");
        _builder.append(d.name, " ");
        _builder.append(" the value of ");
        _builder.append(d.name, " ");
        _builder.append(" will be written into the memory of this pointer.");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 1  Successfully retrieved the values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 0  Something went wrong (invalid index for example)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("int get");
    String _firstUpper = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper);
    _builder.append("Direct(uint32_t *base, uint32_t index");
    {
      List<Definition> _allDefs_1 = this.ba.allDefs(row);
      for(final Definition definition : _allDefs_1) {
        String _parameter = this.ba.getParameter(row, definition, true);
        _builder.append(_parameter);
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("uint32_t offset[1]={index};");
    _builder.newLine();
    {
      List<Definition> _allDefs_2 = this.ba.allDefs(row);
      for(final Definition d_1 : _allDefs_2) {
        _builder.append("\t");
        _builder.append("*");
        String _varName = this.ba.getVarName(row, d_1);
        _builder.append(_varName, "\t");
        _builder.append("=(");
        CharSequence _busType = this.ba.getBusType(d_1);
        _builder.append(_busType, "\t");
        _builder.append(")pshdl_sim_getOutputArray(");
        CharSequence _defineNameString = this.getDefineNameString(d_1.name);
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
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Retrieve the fields of row ");
    String _name_1 = row.getName();
    _builder.append(_name_1, " ");
    _builder.append(" into the struct.");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param index the row that you want to access. ");
    {
      if ((rowCount == 1)) {
        _builder.append("The only valid index is 0");
      } else {
        _builder.append("Valid values are 0..");
        _builder.append((rowCount - 
          1), " ");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* @param result the values of this row will be written into the struct");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 1  Successfully retrieved the values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 0  Something went wrong (invalid index for example)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("int get");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper_1);
    _builder.append("(uint32_t *base, uint32_t index, ");
    String _name_2 = row.getName();
    _builder.append(_name_2);
    _builder.append("_t *result){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return get");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper_2, "\t");
    _builder.append("Direct(base, index");
    {
      List<Definition> _allDefs_3 = this.ba.allDefs(row);
      for(final Definition d_2 : _allDefs_3) {
        _builder.append(", &result->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_2);
        _builder.append(_varNameIndex, "\t");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence simSetter(final Row row, final int rowCount) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Updates the values in memory from the struct. This also advances the simulation by one clock cycle, ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* unless PSHDL_SIM_NO_BUSCLK_TOGGLE is defined.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param index the row that you want to access. ");
    {
      if ((rowCount == 1)) {
        _builder.append("The only valid index is 0");
      } else {
        _builder.append("Valid values are 0..");
        _builder.append((rowCount - 
          1), " ");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _allDefs = this.ba.allDefs(row);
      for(final Definition d : _allDefs) {
        _builder.append(" ");
        _builder.append("* @param ");
        _builder.append(d.name, " ");
        _builder.append(" the value of ");
        _builder.append(d.name, " ");
        _builder.append(" will be written into the register. ");
        StringBuilder _explain = this.ba.explain(d);
        _builder.append(_explain, " ");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 1  Successfully updated the values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 0  Something went wrong (invalid index or value exceeds its range for example)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("int set");
    String _firstUpper = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper);
    _builder.append("Direct(uint32_t *base, uint32_t index");
    {
      List<Definition> _writeDefs = this.ba.writeDefs(row);
      for(final Definition definition : _writeDefs) {
        String _parameter = this.ba.getParameter(row, definition, false);
        _builder.append(_parameter);
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (index>");
    _builder.append((rowCount - 1), "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("uint32_t offset[1]={index};");
    _builder.newLine();
    {
      List<Definition> _writeDefs_1 = this.ba.writeDefs(row);
      for(final Definition ne : _writeDefs_1) {
        _builder.append("\t");
        CharSequence _generateConditions = this.ba.generateConditions(row, "", ne);
        _builder.append(_generateConditions, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Definition> _writeDefs_2 = this.ba.writeDefs(row);
      for(final Definition d_1 : _writeDefs_2) {
        _builder.append("\t");
        _builder.append("pshdl_sim_setInputArray(");
        CharSequence _defineNameString = this.getDefineNameString(d_1.name);
        _builder.append(_defineNameString, "\t");
        _builder.append(", ");
        _builder.append(d_1.name, "\t");
        _builder.append(", offset);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("#ifndef PSHDL_SIM_NO_BUSCLK_TOGGLE");
    _builder.newLine();
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
    _builder.append("#endif");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return 1;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Updates the values in memory from the struct. This also advances the simulation by one clock cycle, ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* unless PSHDL_SIM_NO_BUSCLK_TOGGLE is defined.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @param index the row that you want to access. ");
    {
      if ((rowCount == 1)) {
        _builder.append("The only valid index is 0");
      } else {
        _builder.append("Valid values are 0..");
        _builder.append((rowCount - 
          1), " ");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("* @param newVal the values of this row will be written into the struct");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 1  Successfully updated the values");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* @retval 0  Something went wrong (invalid index or value exceeds range for example)");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("int set");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper_1);
    _builder.append("(uint32_t *base, uint32_t index, ");
    String _name = row.getName();
    _builder.append(_name);
    _builder.append("_t *newVal) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return set");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.getName());
    _builder.append(_firstUpper_2, "\t");
    _builder.append("Direct(base, index");
    {
      List<Definition> _writeDefs_3 = this.ba.writeDefs(row);
      for(final Definition d_2 : _writeDefs_3) {
        _builder.append(", newVal->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_2);
        _builder.append(_varNameIndex, "\t");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callMethod(final boolean pshdlFunction, final CharSequence methodName, final CharSequence... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(methodName);
    _builder.append("(");
    {
      if ((args != null)) {
        {
          boolean _hasElements = false;
          for(final CharSequence arg : args) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(arg);
          }
        }
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  @Override
  protected CharSequence callRunMethod() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence checkTestbenchListener() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence runTestbenchHeader() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  public String getHookName() {
    return "C";
  }
  
  @Override
  public IOutputProvider.MultiOption getUsage() {
    final Options options = new Options();
    return new IOutputProvider.MultiOption(null, null, options);
  }
  
  public static List<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final CCodeGeneratorParameter parameter) {
    final CCodeGenerator comp = new CCodeGenerator(parameter);
    final List<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent>newLinkedList();
    Iterables.<AuxiliaryContent>addAll(sideFiles, comp.getAuxiliaryContent());
    String _string = comp.generateMainCode().toString();
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, _string, parameter.em.moduleName, sideFiles, 
      parameter.em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  @Override
  public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
    CCodeGeneratorParameter _cCodeGeneratorParameter = new CCodeGeneratorParameter(em);
    return CCodeGenerator.doCompile(syntaxProblems, _cCodeGeneratorParameter);
  }
  
  @Override
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("memset(");
    CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName);
    _builder.append(", ");
    _builder.append(regFillValue);
    _builder.append(", ");
    int _arraySize = this.getArraySize(vi);
    _builder.append(_arraySize);
    _builder.append(");");
    return _builder;
  }
  
  @Override
  protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    this.hasPow = true;
    VariableInformation.Type _typeFromTargetSize = this.typeFromTargetSize(targetSizeWithType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pshdl_sim_pow(");
    String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName);
    _builder.append(", ");
    String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName_1);
    _builder.append(")");
    return this.assignTempVar(_typeFromTargetSize, targetSizeWithType, pos, CommonCodeGenerator.NONE, _builder, true);
  }
}
