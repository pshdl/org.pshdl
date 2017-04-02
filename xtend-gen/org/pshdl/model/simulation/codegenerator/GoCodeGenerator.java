/**
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 * 
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.IHDLInterpreterFactory;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.NativeRunner;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.simulation.codegenerator.CCodeGenerator;
import org.pshdl.model.simulation.codegenerator.CommonCodeGenerator;
import org.pshdl.model.simulation.codegenerator.CommonCompilerExtension;
import org.pshdl.model.simulation.codegenerator.GoCodeGeneratorParameter;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class GoCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
  private String pkg;
  
  private String unit;
  
  private CommonCompilerExtension cce;
  
  public GoCodeGenerator() {
  }
  
  public GoCodeGenerator(final GoCodeGeneratorParameter parameter) {
    super(parameter);
    this.pkg = parameter.packageName;
    this.unit = StringExtensions.toFirstUpper(parameter.unitName);
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(this.em, 64);
    this.cce = _commonCompilerExtension;
  }
  
  public IHDLInterpreterFactory<NativeRunner> createInterpreter(final File tempDir, final NativeRunner.IRunListener listener) {
    try {
      IHDLInterpreterFactory<NativeRunner> _xblockexpression = null;
      {
        final CharSequence dartCode = this.generateMainCode();
        final File dutFile = new File(tempDir, "TestUnit.go");
        Files.createParentDirs(dutFile);
        Files.write(dartCode, dutFile, StandardCharsets.UTF_8);
        final File testRunner = new File(tempDir, "runner.go");
        final InputStream runnerStream = CCodeGenerator.class.getResourceAsStream("/org/pshdl/model/simulation/includes/runner.go");
        final FileOutputStream fos = new FileOutputStream(testRunner);
        try {
          ByteStreams.copy(runnerStream, fos);
        } finally {
          fos.close();
        }
        String _absolutePath = testRunner.getAbsolutePath();
        String _absolutePath_1 = dutFile.getAbsolutePath();
        final ProcessBuilder goBuilder = new ProcessBuilder("/usr/local/bin/go", "build", _absolutePath, _absolutePath_1).directory(tempDir).redirectErrorStream(true).inheritIO();
        final Process goCompiler = goBuilder.start();
        int _waitFor = goCompiler.waitFor();
        boolean _notEquals = (_waitFor != 0);
        if (_notEquals) {
          throw new RuntimeException("Compilation of Go Program failed");
        }
        _xblockexpression = new IHDLInterpreterFactory<NativeRunner>() {
          @Override
          public NativeRunner newInstance() {
            try {
              final File runnerExecutable = new File(tempDir, "runner");
              String _absolutePath = runnerExecutable.getAbsolutePath();
              final ProcessBuilder goBuilder = new ProcessBuilder(_absolutePath).directory(tempDir).redirectErrorStream(true);
              final Process goRunner = goBuilder.start();
              InputStream _inputStream = goRunner.getInputStream();
              OutputStream _outputStream = goRunner.getOutputStream();
              String _absolutePath_1 = runnerExecutable.getAbsolutePath();
              return new NativeRunner(_inputStream, _outputStream, GoCodeGenerator.this.em, goRunner, 5, _absolutePath_1, listener);
            } catch (Throwable _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  protected CharSequence preFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("type ");
    _builder.append(this.unit);
    _builder.append(" struct {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("varIdx map[string]int");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("regUpdates   [");
    int _maxRegUpdates = this.maxRegUpdates();
    _builder.append(_maxRegUpdates, "\t");
    _builder.append("]regUpdate");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("regUpdatePos int");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence postFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") updateRegs() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("for i:=0; i<s.regUpdatePos; i++ {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("reg:=s.regUpdates[i]");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch reg.internal {");
    _builder.newLine();
    _builder.append("\t\t");
    CharSequence _updateRegCases = this.updateRegCases();
    _builder.append(_updateRegCases, "\t\t");
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
  
  @Override
  protected String dynamicMask(final Integer idx) {
    String _shiftLeftDynamic = this.shiftLeftDynamic(this.constant(1, true), idx);
    String _plus = ("((^(" + _shiftLeftDynamic);
    return (_plus + "))-1)");
  }
  
  @Override
  protected String shiftRightDynamic(final CharSequence tempName, final Integer idx) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(tempName);
    _builder.append(" >> uint(");
    String _tempName = this.getTempName((idx).intValue(), EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayIndex));
    _builder.append(_tempName);
    _builder.append(")");
    return _builder.toString();
  }
  
  @Override
  protected String shiftLeftDynamic(final CharSequence tempName, final Integer idx) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(tempName);
    _builder.append(" << uint(");
    String _tempName = this.getTempName((idx).intValue(), EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayIndex));
    _builder.append(_tempName);
    _builder.append(")");
    return _builder.toString();
  }
  
  @Override
  protected CharSequence doLoopStart() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for {");
    return _builder;
  }
  
  @Override
  protected CharSequence doLoopEnd(final CharSequence condition) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("if (!(");
    _builder.append(condition, "\t");
    _builder.append(")) { break }");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence applyRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.updateRegs()");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("make([]");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    _builder.append(_fieldType);
    _builder.append(", ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize);
    _builder.append(")");
    return _builder;
  }
  
  @Override
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence callMethod(final boolean pshdlFunction, final CharSequence methodName, final CharSequence... args) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (this.inBarrier) {
        _builder.append("wg.Add(1)");
        CharSequence _indent = this.indent();
        _builder.append(_indent);
        _builder.append("go ");
      }
    }
    {
      if ((!pshdlFunction)) {
        _builder.append("s.");
      }
    }
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
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.Run()");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName);
    _builder.append("()");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence checkRegupdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.regUpdatePos != 0");
    return _builder;
  }
  
  @Override
  protected CharSequence checkTestbenchListener() {
    throw new UnsupportedOperationException("TODO: auto-generated method stub");
  }
  
  @Override
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.regUpdatePos = 0");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/* copy array */");
    return _builder;
  }
  
  @Override
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    if ((IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(varInfo.dimensions))) || attributes.contains(CommonCodeGenerator.Attributes.baseType))) {
      boolean _equals = Objects.equal(varInfo.type, VariableInformation.Type.STRING);
      if (_equals) {
        return "string";
      }
      boolean _isBoolean = this.isBoolean(varInfo, attributes);
      if (_isBoolean) {
        return "bool";
      }
      return "int64";
    } else {
      boolean _equals_1 = Objects.equal(varInfo.type, VariableInformation.Type.STRING);
      if (_equals_1) {
        return "[]string";
      }
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "[]bool";
      }
      return "[]int64";
    }
  }
  
  @Override
  protected CharSequence doCast(final CharSequence cast, final CharSequence assignValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(cast);
    _builder.append("(");
    _builder.append(assignValue);
    _builder.append(")");
    return _builder;
  }
  
  @Override
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") SetInputWithName(name string, value int64, arrayIdx ...int) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.SetInput(s.GetIndex(name), value, arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") SetInput(idx int, value int64, arrayIdx ...int) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _setInputCases = this.setInputCases("value", null, EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayArg));
    _builder.append(_setInputCases, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetIndex(name string) int {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("idx, ok := s.varIdx[name]");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if !ok {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"The name:\" + name + \" is not a valid index\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return idx");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetName(idx int) string {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    {
      for(final VariableInformation vi : this.em.variables) {
        _builder.append("\t");
        _builder.append("case ");
        int _varIdx = this.getVarIdx(vi, false);
        _builder.append(_varIdx, "\t");
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\"");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index:\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetOutputWithName(name string, arrayIdx ...int) int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return s.GetOutput(s.GetIndex(name), arrayIdx...)");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetOutput(idx int, arrayIdx ...int) int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("switch idx {");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _outputCases = this.getOutputCases(null, EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayArg));
    _builder.append(_outputCases, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("panic(\"Not a valid index:\")");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetDeltaCycle() int64 {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return s.deltaCycle");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") GetJsonDesc() string {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return \"");
    String _jSONDescription = this.cce.getJSONDescription();
    _builder.append(_jSONDescription, "\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") SetDisableEdge(enable bool) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
    _builder.append(" = enable");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") SetDisableRegOutputLogic(enable bool) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("s.");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
    _builder.append(" = enable");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
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
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName);
    _builder.append(" (){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package ");
    _builder.append(this.pkg);
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("type regUpdate struct {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("internal, offset int");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fillValue int64");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func New");
    _builder.append(this.unit);
    _builder.append("() *");
    _builder.append(this.unit);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return New");
    _builder.append(this.unit, "\t");
    _builder.append("WithArgs(false, false)");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("func New");
    _builder.append(this.unit);
    _builder.append("WithArgs(");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name);
    _builder.append(", ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name);
    _builder.append(" bool) *");
    _builder.append(this.unit);
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var s = ");
    _builder.append(this.unit, "\t");
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
    _builder.append(":           ");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
    _builder.append(": ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t");
    _builder.append(",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("s.varIdx = make(map[string]int, ");
    int _size = ((List<VariableInformation>)Conversions.doWrapArray(this.em.variables)).size();
    _builder.append(_size, "\t");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    {
      for(final VariableInformation v : this.em.variables) {
        _builder.append("\t");
        _builder.append("s.varIdx[\"");
        _builder.append(v.name, "\t");
        _builder.append("\"] =  ");
        int _varIdx = this.getVarIdx(v, this.purgeAliases);
        _builder.append(_varIdx, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        @Override
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf(GoCodeGenerator.this.isArray(it));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function);
      for(final VariableInformation v_1 : _filter) {
        _builder.append("\t");
        CharSequence _idName = this.idName(v_1, true, CommonCodeGenerator.NONE);
        _builder.append(_idName, "\t");
        _builder.append(" = make([]int64, ");
        int _arraySize = this.getArraySize(v_1);
        _builder.append(_arraySize, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        {
          if (v_1.isRegister) {
            _builder.append("\t");
            CharSequence _idName_1 = this.idName(v_1, true, CommonCodeGenerator.SHADOWREG);
            _builder.append(_idName_1, "\t");
            _builder.append(" = make([]int64, ");
            int _arraySize_1 = this.getArraySize(v_1);
            _builder.append(_arraySize_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return &s");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") skipEdge(local int64) bool {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("var dc = int64(uint64(local) >> 16) // zero-extended shift");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if dc < s.");
    _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
    _builder.append(" {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return false");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (dc == s.");
    _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
    _builder.append(") && ((local & 0xFFFF) == s.");
    _builder.append(CommonCodeGenerator.EPS_CYCLE.name, "\t");
    _builder.append(") {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return false");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return true");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("func pow(a int64, n int64) int64 {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("var result int64 = 1;");
    _builder.newLine();
    _builder.append("    \t");
    _builder.append("var p int64 = a;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (n<=0) {break}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if ((n % 2) != 0) {");
    _builder.newLine();
    _builder.append("        \t    ");
    _builder.append("result = result * p;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("p = p * p;");
    _builder.newLine();
    _builder.append("\t        ");
    _builder.append("n = n / 2;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return result");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence idName(final String name, final boolean field, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    final String superVal = super.idName(name, field, attributes).toString().replace("$", "__");
    return superVal;
  }
  
  @Override
  protected String fieldPrefix() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("s.");
    return _builder.toString();
  }
  
  @Override
  protected CharSequence createVarDeclaration(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean initialize) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.preField(varInfo, attributes));
    this.indent++;
    sb.append(this.indent());
    this.indent--;
    sb.append(this.idName(varInfo, false, attributes)).append(" ");
    sb.append(this.fieldType(varInfo, attributes));
    sb.append(this.postField(varInfo));
    return sb;
  }
  
  @Override
  protected String constantSuffix() {
    StringConcatenation _builder = new StringConcatenation();
    return _builder.toString();
  }
  
  @Override
  protected CharSequence inlineVarDecl(final VariableInformation varInfo, final boolean field, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("var ");
    CharSequence _idName = this.idName(varInfo, field, attributes);
    _builder.append(_idName);
    _builder.append(" ");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    _builder.append(_fieldType);
    return _builder;
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
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") ");
    {
      if ((!constant)) {
        _builder.append("Run");
      } else {
        _builder.append("InitConstants");
      }
    }
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
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
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean force, final CharSequence fillValue) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((!force)) {
        _builder.append("if (");
        _builder.append(cpyName);
        _builder.append("!=");
        _builder.append(last);
        _builder.append(") ");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    CharSequence _indent = this.indent();
    _builder.append(_indent);
    _builder.append("\ts.regUpdates[s.regUpdatePos] = regUpdate{");
    Integer _regIdx = this.regIdx(outputInternal);
    _builder.append(_regIdx);
    _builder.append(", int(");
    _builder.append(offset);
    _builder.append("), ");
    _builder.append(fillValue);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1);
    _builder.append("\ts.regUpdatePos++");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2);
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public Integer regIdx(final InternalInformation information) {
    return this.regIdx.get(information.info.name);
  }
  
  @Override
  protected CharSequence doMask(final CharSequence currentValue, final CharSequence writeMask) {
    return this.doCast("int64", super.doMask(this.doCast("uint64", currentValue), writeMask));
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
    _builder.append("func (s *");
    _builder.append(this.unit);
    _builder.append(") ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName);
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("for i := range ");
    CharSequence _idName = this.idName(vi, true, 
      CommonCodeGenerator.NONE);
    _builder.append(_idName);
    _builder.append(" { ");
    CharSequence _idName_1 = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1);
    _builder.append("[i] = ");
    _builder.append(regFillValue);
    _builder.append(" } ");
    return _builder;
  }
  
  @Override
  protected CharSequence singleOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int a, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    CharSequence _xblockexpression = null;
    {
      if ((fi.inst == Instruction.bit_neg)) {
        final CharSequence assignValue = this.singleOpValue("^", this.getCast(targetSizeWithType), a, targetSizeWithType, attributes);
        return this.assignTempVar(this.typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos, attributes, assignValue, true);
      }
      _xblockexpression = super.singleOp(fi, op, targetSizeWithType, pos, a, attributes, doMask);
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    CharSequence _xblockexpression = null;
    {
      if ((fi.inst == Instruction.srl)) {
        CharSequence _doCast = this.doCast("uint64", this.getTempName(leftOperand, CommonCodeGenerator.NONE));
        String _plus = (_doCast + ">>");
        CharSequence _doCast_1 = this.doCast("uint64", this.getTempName(rightOperand, CommonCodeGenerator.NONE));
        String _plus_1 = (_plus + _doCast_1);
        final CharSequence assignValue = this.doCast("int64", _plus_1);
        return this.assignTempVar(this.typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos, attributes, assignValue, true);
      }
      if ((fi.inst == Instruction.sra)) {
        String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        String _plus_2 = (_tempName + ">>");
        CharSequence _doCast_2 = this.doCast("uint64", this.getTempName(rightOperand, CommonCodeGenerator.NONE));
        final CharSequence assignValue_1 = (_plus_2 + _doCast_2);
        return this.assignTempVar(this.typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos, attributes, assignValue_1, true);
      }
      if ((fi.inst == Instruction.sll)) {
        String _tempName_1 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
        String _plus_3 = (_tempName_1 + "<<");
        CharSequence _doCast_3 = this.doCast("uint64", this.getTempName(rightOperand, CommonCodeGenerator.NONE));
        String _plus_4 = (_plus_3 + _doCast_3);
        final CharSequence assignValue_2 = this.doCast("int64", _plus_4);
        return this.assignTempVar(this.typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos, attributes, assignValue_2, true);
      }
      _xblockexpression = super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask);
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    VariableInformation.Type _typeFromTargetSize = this.typeFromTargetSize(targetSizeWithType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pow(");
    String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName);
    _builder.append(", ");
    String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName_1);
    _builder.append(")");
    return this.assignTempVar(_typeFromTargetSize, targetSizeWithType, pos, CommonCodeGenerator.NONE, _builder, true);
  }
  
  @Override
  public String getHookName() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Go");
    return _builder.toString();
  }
  
  @Override
  public IOutputProvider.MultiOption getUsage() {
    final Options options = new Options();
    options.addOption("p", "pkg", true, 
      "The package the generated source will use. If non is specified the package from the module is used");
    String _hookName = this.getHookName();
    String _plus = ("Options for the " + _hookName);
    String _plus_1 = (_plus + " type:");
    return new IOutputProvider.MultiOption(_plus_1, null, options);
  }
  
  @Override
  public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
    GoCodeGeneratorParameter _goCodeGeneratorParameter = new GoCodeGeneratorParameter(em);
    return GoCodeGenerator.doCompile(syntaxProblems, _goCodeGeneratorParameter);
  }
  
  public static ArrayList<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final GoCodeGeneratorParameter parameter) {
    final GoCodeGenerator comp = new GoCodeGenerator(parameter);
    final String code = comp.generateMainCode();
    final ArrayList<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent>newArrayList();
    Iterables.<AuxiliaryContent>addAll(sideFiles, comp.getAuxiliaryContent());
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, code, parameter.em.moduleName, sideFiles, 
      parameter.em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  private boolean inBarrier = false;
  
  @Override
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean createConstant) {
    this.inBarrier = true;
    return "var wg sync.WaitGroup\n";
  }
  
  @Override
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean createConstant) {
    this.inBarrier = false;
    return "wg.Wait";
  }
}
