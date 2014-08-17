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
package org.pshdl.model.simulation;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.IHDLInterpreterFactory;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.NativeRunner;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class DartCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
  private String unitName;
  
  private String library;
  
  private boolean usePackageImport;
  
  private final static int epsWidth = 16;
  
  public static String TESTRUNNER_DIR = "/Users/karstenbecker/GDrive/DartTestRunner/";
  
  public static String DART_EXEC = "/Applications/dart/dart-sdk/bin/dart";
  
  public DartCodeGenerator() {
  }
  
  public DartCodeGenerator(final ExecutableModel model, final String unitName, final String library, final boolean usePackageImport, final int maxCosts) {
    super(model, (-1), maxCosts);
    this.unitName = unitName;
    this.library = library;
    this.usePackageImport = usePackageImport;
  }
  
  public IHDLInterpreterFactory<NativeRunner> createInterpreter(final File tempDir) {
    try {
      IHDLInterpreterFactory<NativeRunner> _xblockexpression = null;
      {
        final DartCodeGenerator dc = new DartCodeGenerator(this.em, this.unitName, this.library, true, Integer.MAX_VALUE);
        final String dartCode = dc.generateMainCode();
        final File binDir = new File(tempDir, "bin");
        boolean _mkdirs = binDir.mkdirs();
        boolean _not = (!_mkdirs);
        if (_not) {
          throw new IllegalArgumentException(("Failed to create Directory " + binDir));
        }
        File _file = new File(binDir, "dut.dart");
        Files.write(dartCode, _file, StandardCharsets.UTF_8);
        final File testRunnerDir = new File(DartCodeGenerator.TESTRUNNER_DIR);
        final File testRunner = new File(testRunnerDir, "bin/darttestrunner.dart");
        String _name = testRunner.getName();
        File _file_1 = new File(binDir, _name);
        Files.copy(testRunner, _file_1);
        final File yaml = new File(testRunnerDir, "pubspec.yaml");
        String _name_1 = yaml.getName();
        File _file_2 = new File(tempDir, _name_1);
        Files.copy(yaml, _file_2);
        File _file_3 = new File(binDir, "packages");
        Path _path = _file_3.toPath();
        File _file_4 = new File(testRunnerDir, "packages");
        Path _path_1 = _file_4.toPath();
        java.nio.file.Files.createSymbolicLink(_path, _path_1);
        File _file_5 = new File(tempDir, "packages");
        Path _path_2 = _file_5.toPath();
        File _file_6 = new File(testRunnerDir, "packages");
        Path _path_3 = _file_6.toPath();
        java.nio.file.Files.createSymbolicLink(_path_2, _path_3);
        _xblockexpression = new IHDLInterpreterFactory<NativeRunner>() {
          public NativeRunner newInstance() {
            try {
              String _name = testRunner.getName();
              String _plus = ("bin/" + _name);
              ProcessBuilder _processBuilder = new ProcessBuilder(DartCodeGenerator.DART_EXEC, _plus, DartCodeGenerator.this.unitName, DartCodeGenerator.this.library);
              ProcessBuilder _directory = _processBuilder.directory(tempDir);
              ProcessBuilder _redirectErrorStream = _directory.redirectErrorStream(true);
              final Process dartRunner = _redirectErrorStream.start();
              InputStream _inputStream = dartRunner.getInputStream();
              OutputStream _outputStream = dartRunner.getOutputStream();
              return new NativeRunner(_inputStream, _outputStream, DartCodeGenerator.this.em, dartRunner, 5);
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
  
  protected String constantSuffix() {
    return "";
  }
  
  protected String fieldPrefix() {
    return "_";
  }
  
  protected void postBody() {
    this.indent--;
  }
  
  protected void preBody() {
    this.indent++;
  }
  
  protected CharSequence applyRegUpdates() {
    return "_updateRegs();";
  }
  
  protected CharSequence checkRegupdates() {
    return "!_regUpdates.isEmpty";
  }
  
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_regUpdates.clear();");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("new ");
    CharSequence _fieldType = this.fieldType(varInfo, attributes);
    _builder.append(_fieldType, "");
    _builder.append("(");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append(")");
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
    _builder.append("void _");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset, final boolean forceRegUpdate, final CharSequence fillValue) {
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
    _builder.append("_regUpdates.add(new RegUpdate(");
    int _varIdx = this.getVarIdx(outputInternal);
    _builder.append(_varIdx, "");
    _builder.append(", ");
    _builder.append(offset, "");
    _builder.append(", ");
    _builder.append(fillValue, "");
    _builder.append("));");
    return _builder;
  }
  
  protected CharSequence runMethodsHeader(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void ");
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
    _builder.append("_");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence stageMethodsFooter(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence copyArray(final VariableInformation varInfo) {
    final CharSequence type = this.fieldType(varInfo, CommonCodeGenerator.NONE);
    StringConcatenation _builder = new StringConcatenation();
    EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isPrev);
    CharSequence _idName = this.idName(varInfo, true, _of);
    _builder.append(_idName, "");
    _builder.append(" = new ");
    _builder.append(type, "");
    _builder.append(".from");
    {
      boolean _notEquals = (!Objects.equal(type, 
        "List< "));
      if (_notEquals) {
        _builder.append("List");
      }
    }
    _builder.append("(");
    CharSequence _idName_1 = this.idName(varInfo, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1, "");
    _builder.append(");");
    return _builder;
  }
  
  protected CharSequence fieldType(final VariableInformation information, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    String jt = "int";
    boolean _isBoolean = this.isBoolean(information, attributes);
    if (_isBoolean) {
      jt = "bool";
    }
    boolean _and = false;
    boolean _isArray = this.isArray(information);
    if (!_isArray) {
      _and = false;
    } else {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.baseType);
      boolean _not = (!_contains);
      _and = _not;
    }
    if (_and) {
      boolean _equals = Objects.equal(jt, "bool");
      if (_equals) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("List<");
        _builder.append(jt, "");
        _builder.append(">");
        return _builder;
      }
      boolean _and_1 = false;
      if (!(information.width <= 8)) {
        _and_1 = false;
      } else {
        boolean _tripleEquals = (information.type == VariableInformation.Type.INT);
        _and_1 = _tripleEquals;
      }
      if (_and_1) {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("Int8List");
        return _builder_1;
      }
      if ((information.width <= 8)) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("Uint8List");
        return _builder_2;
      }
      boolean _and_2 = false;
      if (!(information.width <= 16)) {
        _and_2 = false;
      } else {
        boolean _tripleEquals_1 = (information.type == VariableInformation.Type.INT);
        _and_2 = _tripleEquals_1;
      }
      if (_and_2) {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("Int16List");
        return _builder_3;
      }
      if ((information.width <= 16)) {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("Uint16List");
        return _builder_4;
      }
      boolean _and_3 = false;
      if (!(information.width <= 32)) {
        _and_3 = false;
      } else {
        boolean _tripleEquals_2 = (information.type == VariableInformation.Type.INT);
        _and_3 = _tripleEquals_2;
      }
      if (_and_3) {
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("Int32List");
        return _builder_5;
      }
      if ((information.width <= 32)) {
        StringConcatenation _builder_6 = new StringConcatenation();
        _builder_6.append("Uint32List");
        return _builder_6;
      }
      boolean _and_4 = false;
      if (!(information.width <= 64)) {
        _and_4 = false;
      } else {
        boolean _tripleEquals_3 = (information.type == VariableInformation.Type.INT);
        _and_4 = _tripleEquals_3;
      }
      if (_and_4) {
        StringConcatenation _builder_7 = new StringConcatenation();
        _builder_7.append("Int64List");
        return _builder_7;
      }
      if ((information.width <= 64)) {
        StringConcatenation _builder_8 = new StringConcatenation();
        _builder_8.append("Uint64List");
        return _builder_8;
      }
      StringConcatenation _builder_9 = new StringConcatenation();
      _builder_9.append("List<");
      _builder_9.append(jt, "");
      _builder_9.append(">");
      return _builder_9;
    }
    return jt;
  }
  
  protected CharSequence calculateVariableAccessIndexArr(final VariableInformation varInfo) {
    return "offset";
  }
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("void setVar(int idx, dynamic value, {int offset}) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    CharSequence _setInputCases = this.setInputCases("value", null);
    _builder.append(_setInputCases, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return _varIdx[name];");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.em.variables) {
        _builder.append("\t\t\t");
        _builder.append("case ");
        Integer _get = this.varIdx.get(v.name);
        _builder.append(_get, "\t\t\t");
        _builder.append(": return \"");
        String _replaceAll = v.name.replaceAll("[\\$]", "\\\\\\$");
        _builder.append(_replaceAll, "\t\t\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("dynamic getVar(int idx, {int offset}) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    CharSequence _outputCases = this.getOutputCases(null);
    _builder.append(_outputCases, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("throw new ArgumentError(\"Not a valid index: $idx\");");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int get deltaCycle =>_deltaCycle;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int get varNum => ");
    int _size = this.varIdx.size();
    _builder.append(_size, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("bool get ");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
    _builder.append(" => ");
    CharSequence _idName = this.idName(CommonCodeGenerator.DISABLE_EDGES, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("set ");
    _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
    _builder.append("(bool newVal) => ");
    CharSequence _idName_1 = this.idName(CommonCodeGenerator.DISABLE_EDGES, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_1, "\t");
    _builder.append("=newVal;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("bool get ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
    _builder.append(" => ");
    CharSequence _idName_2 = this.idName(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_2, "\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("set ");
    _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
    _builder.append("(bool newVal) => ");
    CharSequence _idName_3 = this.idName(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC, true, CommonCodeGenerator.NONE);
    _builder.append(_idName_3, "\t");
    _builder.append("=newVal;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _description = this.getDescription();
    _builder.append(_description, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getDescription() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Description get description=>new Description(");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir == VariableInformation.Direction.IN));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function);
      boolean _hasElements = false;
      for(final VariableInformation v : _filter) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _asPort = this.asPort(v);
        _builder.append(_asPort, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("],");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation, Boolean> _function_1 = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir == VariableInformation.Direction.INOUT));
        }
      };
      Iterable<VariableInformation> _filter_1 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_1);
      boolean _hasElements_1 = false;
      for(final VariableInformation v_1 : _filter_1) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _asPort_1 = this.asPort(v_1);
        _builder.append(_asPort_1, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("],");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation, Boolean> _function_2 = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir == VariableInformation.Direction.OUT));
        }
      };
      Iterable<VariableInformation> _filter_2 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_2);
      boolean _hasElements_2 = false;
      for(final VariableInformation v_2 : _filter_2) {
        if (!_hasElements_2) {
          _hasElements_2 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _asPort_2 = this.asPort(v_2);
        _builder.append(_asPort_2, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("],");
    _builder.newLine();
    _builder.append("[");
    _builder.newLine();
    {
      final Function1<VariableInformation, Boolean> _function_3 = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir == VariableInformation.Direction.INTERNAL));
        }
      };
      Iterable<VariableInformation> _filter_3 = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), _function_3);
      boolean _hasElements_3 = false;
      for(final VariableInformation v_3 : _filter_3) {
        if (!_hasElements_3) {
          _hasElements_3 = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _asPort_3 = this.asPort(v_3);
        _builder.append(_asPort_3, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("], _varIdx, \"");
    _builder.append(this.em.moduleName, "");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence asPort(final VariableInformation v) {
    CharSequence _xblockexpression = null;
    {
      String dims = "";
      boolean _isArray = this.isArray(v);
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
      if (_switchValue != null) {
        switch (_switchValue) {
          case BIT:
            type = "Port.TYPE_BIT";
            break;
          case INT:
            type = "Port.TYPE_INT";
            break;
          case UINT:
            type = "Port.TYPE_UINT";
            break;
          case BOOL:
            type = "Port.TYPE_BOOL";
            break;
          default:
            break;
        }
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("new Port(");
      Integer _get = this.varIdx.get(v.name);
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
      _xblockexpression = _builder_1;
    }
    return _xblockexpression;
  }
  
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (this.library != null);
      if (_tripleNotEquals) {
        _builder.append("library ");
        _builder.append(this.library, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _imports = this.getImports(this.usePackageImport);
    _builder.append(_imports, "");
    _builder.newLineIfNotEmpty();
    _builder.append("void main(List<String> args, SendPort replyTo){");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("handleReceive((e,l) => new ");
    _builder.append(this.unitName, "  \t");
    _builder.append("(e,l), replyTo);");
    _builder.newLineIfNotEmpty();
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
        _builder.append("final int fillValue;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("RegUpdate(this.internalID, this.offset, this.fillValue);");
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
        _builder.append("result = (prime * result) + fillValue;");
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
        _builder.append("if (fillValue != other.fillValue)");
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
    _builder.append(this.unitName, "");
    _builder.append(" extends DartInterpreter{");
    _builder.newLineIfNotEmpty();
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append("List<RegUpdate> _regUpdates=[];");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Map<String, int> _varIdx={");
    _builder.newLine();
    {
      boolean _hasElements = false;
      for(final VariableInformation v : this.em.variables) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "\t\t");
        }
        _builder.append("\t\t");
        _builder.append("\"");
        String _replaceAll = v.name.replaceAll("[\\$]", "\\\\\\$");
        _builder.append(_replaceAll, "\t\t");
        _builder.append("\": ");
        Integer _get = this.varIdx.get(v.name);
        _builder.append(_get, "\t\t");
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
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append(this.unitName, "\t");
        _builder.append("(this._");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append(", this._");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append(this.unitName, "\t");
        _builder.append("(bool disableEdges, bool disabledRegOutputlogic) {}");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (this.hasClock) {
        _builder.append("\t");
        _builder.append("bool _skipEdge(int local) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("int dc = local >> ");
        _builder.append(DartCodeGenerator.epsWidth, "\t\t");
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
        BigInteger _calcMask = this.calcMask(DartCodeGenerator.epsWidth);
        CharSequence _constant = this.constant(_calcMask, true);
        _builder.append(_constant, "\t\t");
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
        _builder.append("\t");
        _builder.append("void _updateRegs() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("for (RegUpdate reg in _regUpdates) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("switch (reg.internalID) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        CharSequence _updateRegCases = this.updateRegCases();
        _builder.append(_updateRegCases, "\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
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
    _builder.append("int _signExtend(int val, int width) {");
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
    return _builder;
  }
  
  public CharSequence getImports(final boolean usePackageImport) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (this.hasClock) {
        _builder.append("import \'dart:collection\';");
        _builder.newLine();
      }
    }
    _builder.append("import \'dart:typed_data\';");
    _builder.newLine();
    _builder.append("import \'dart:isolate\';");
    _builder.newLine();
    {
      if (usePackageImport) {
        _builder.append("import \'package:pshdl_api/simulation_comm.dart\';");
        _builder.newLine();
      } else {
        _builder.append("import \'../simulation_comm.dart\';");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  protected CharSequence stageMethodsHeader(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("void _");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence assignNextTime(final VariableInformation nextTime, final CharSequence currentProcessTime) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(nextTime.name, "");
    _builder.append("=Math.min(");
    _builder.append(nextTime.name, "");
    _builder.append(", ");
    _builder.append(currentProcessTime, "");
    _builder.append(");");
    return _builder;
  }
  
  protected CharSequence callMethod(final CharSequence methodName, final CharSequence... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("_");
    _builder.append(methodName, "");
    _builder.append("(");
    {
      boolean _tripleNotEquals = (args != null);
      if (_tripleNotEquals) {
        {
          boolean _hasElements = false;
          for(final CharSequence arg : args) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(",", "");
            }
            _builder.append(arg, "");
          }
        }
      }
    }
    _builder.append(")");
    return _builder;
  }
  
  protected CharSequence callRunMethod() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("run();");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence checkTestbenchListener() {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _indent = this.indent();
    _builder.append(_indent, "");
    _builder.append("if (listener!=null && !listener.nextStep(");
    VariableInformation _varByName = this.varByName("$time");
    CharSequence _idName = this.idName(_varByName, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(", stepCount))");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("\tbreak;");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    CharSequence _xblockexpression = null;
    {
      this.indent++;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("bool _");
      String _processMethodName = this.processMethodName(pd);
      _builder.append(_processMethodName, "");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence runTestbenchHeader() {
    CharSequence _xblockexpression = null;
    {
      this.indent++;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("void runTestbench(int maxTime, int maxSteps, ITestbenchStepListener listener) {");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(".fillRange(0, ");
    int _arraySize = this.getArraySize(vi);
    _builder.append(_arraySize, "");
    _builder.append(", ");
    _builder.append(regFillValue, "");
    _builder.append(");");
    return _builder;
  }
  
  protected CharSequence signExtend(final CharSequence op, final int targetSizeWithType) {
    boolean _isSignedType = this.isSignedType(targetSizeWithType);
    if (_isSignedType) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("_signExtend(");
      _builder.append(op, "");
      _builder.append(", ");
      int _doubleGreaterThan = (targetSizeWithType >> 1);
      _builder.append(_doubleGreaterThan, "");
      _builder.append(")");
      return _builder;
    }
    return op;
  }
  
  protected CharSequence twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    boolean _tripleEquals = (fi.inst == Instruction.srl);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("_srl(");
      String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
      _builder.append(_tempName, "");
      _builder.append(", ");
      String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
      _builder.append(_tempName_1, "");
      _builder.append(", ");
      _builder.append(fi.arg1, "");
      _builder.append(")");
      return this.assignTempVar(targetSizeWithType, pos, CommonCodeGenerator.NONE, _builder, true);
    }
    boolean _tripleEquals_1 = (fi.inst == Instruction.div);
    if (_tripleEquals_1) {
      CharSequence _cast = this.getCast(targetSizeWithType);
      final CharSequence assignValue = this.twoOpValue("~/", _cast, leftOperand, rightOperand, targetSizeWithType, attributes);
      return this.assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
    }
    return super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask);
  }
  
  protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pow(");
    String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName, "");
    _builder.append(", ");
    String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName_1, "");
    _builder.append(")");
    return this.assignTempVar(targetSizeWithType, pos, CommonCodeGenerator.NONE, _builder, true);
  }
  
  public String getHookName() {
    return "Dart";
  }
  
  public IOutputProvider.MultiOption getUsage() {
    final Options options = new Options();
    options.addOption("p", "pkg", true, 
      "The package the generated source will use. If non is specified the package from the module is used");
    String _hookName = this.getHookName();
    String _plus = ("Options for the " + _hookName);
    String _plus_1 = (_plus + " type:");
    return new IOutputProvider.MultiOption(_plus_1, null, options);
  }
  
  public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
    ArrayList<PSAbstractCompiler.CompileResult> _xblockexpression = null;
    {
      final String moduleName = em.moduleName;
      final int li = moduleName.lastIndexOf(".");
      String pkg = null;
      final String optionPkg = cli.getOptionValue("pkg");
      boolean _tripleNotEquals = (optionPkg != null);
      if (_tripleNotEquals) {
        pkg = optionPkg;
      } else {
        if ((li != (-1))) {
          String _substring = moduleName.substring(0, (li - 1));
          pkg = _substring;
        }
      }
      int _length = moduleName.length();
      final String unitName = moduleName.substring((li + 1), _length);
      _xblockexpression = DartCodeGenerator.doCompile(syntaxProblems, em, pkg, unitName, true);
    }
    return _xblockexpression;
  }
  
  public static ArrayList<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final ExecutableModel em, final String pkg, final String unitName, final boolean usePackageImport) {
    final DartCodeGenerator comp = new DartCodeGenerator(em, unitName, pkg, usePackageImport, Integer.MAX_VALUE);
    final String code = comp.generateMainCode();
    final ArrayList<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent>newArrayList();
    Iterable<AuxiliaryContent> _auxiliaryContent = comp.getAuxiliaryContent();
    Iterables.<AuxiliaryContent>addAll(sideFiles, _auxiliaryContent);
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, sideFiles, em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
}
