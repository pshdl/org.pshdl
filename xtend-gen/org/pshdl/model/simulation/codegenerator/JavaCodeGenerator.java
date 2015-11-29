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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.FunctionInformation;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.ParameterInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.model.simulation.HDLSimulator;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.simulation.SimulationTransformationExtension;
import org.pshdl.model.simulation.codegenerator.CommonCodeGenerator;
import org.pshdl.model.simulation.codegenerator.JavaCodeGeneratorParameter;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class JavaCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
  public static class ExecutionPhase {
    private static int globalID = 0;
    
    public final int id = JavaCodeGenerator.ExecutionPhase.globalID++;
    
    public final StringBuilder executionCore = new StringBuilder();
    
    public int stage;
    
    public ExecutionPhase(final int stage) {
      this.stage = stage;
    }
    
    public CharSequence declare() {
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _field = this.field();
      _builder.append(_field, "");
      _builder.newLineIfNotEmpty();
      _builder.append("public final void ");
      CharSequence _methodName = this.methodName();
      _builder.append(_methodName, "");
      _builder.append("(){");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append(this.executionCore, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("execPhase");
      _builder.append(this.id, "\t");
      _builder.append(" = ");
      _builder.append(CommonCodeGenerator.TIMESTAMP.name, "\t");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      return _builder;
    }
    
    public CharSequence methodName() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("stage");
      String _format = String.format("%03x", Integer.valueOf(this.stage));
      _builder.append(_format, "");
      _builder.append("phase");
      String _format_1 = String.format("%04x", Integer.valueOf(this.id));
      _builder.append(_format_1, "");
      return _builder;
    }
    
    public CharSequence call() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("if (execPhase");
      _builder.append(this.id, "");
      _builder.append(" != ");
      _builder.append(CommonCodeGenerator.TIMESTAMP.name, "");
      _builder.append("){");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      CharSequence _methodName = this.methodName();
      _builder.append(_methodName, "\t");
      _builder.append("();");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      return _builder;
    }
    
    public CharSequence field() {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("long execPhase");
      _builder.append(this.id, "");
      _builder.append("=-1;");
      _builder.newLineIfNotEmpty();
      return _builder;
    }
  }
  
  private String packageName;
  
  private String unitName;
  
  private int executionCores = 0;
  
  private boolean junit = false;
  
  private boolean hasPow = false;
  
  public JavaCodeGenerator() {
  }
  
  public JavaCodeGenerator(final JavaCodeGeneratorParameter parameter) {
    super(parameter);
    this.packageName = parameter.packageName;
    this.unitName = parameter.unitName;
    this.executionCores = parameter.executionCores;
    this.junit = parameter.junit;
  }
  
  @Override
  public JavaCodeGeneratorParameter getParameter() {
    return ((JavaCodeGeneratorParameter) super.parameter);
  }
  
  @Override
  protected void postBody() {
    this.indent--;
  }
  
  @Override
  protected void preBody() {
    this.indent++;
  }
  
  @Override
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    String res = "long";
    if ((varInfo.type == VariableInformation.Type.STRING)) {
      res = "String";
    }
    boolean _isBoolean = this.isBoolean(varInfo, attributes);
    if (_isBoolean) {
      res = "boolean";
    }
    boolean _and = false;
    boolean _isArray = this.isArray(varInfo);
    if (!_isArray) {
      _and = false;
    } else {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.baseType);
      boolean _not = (!_contains);
      _and = _not;
    }
    if (_and) {
      return (res + "[]");
    }
    return res;
  }
  
  @Override
  protected CharSequence preField(final VariableInformation x, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.isPublic);
      if (_contains) {
        _builder.append("public");
      } else {
        _builder.append("private");
      }
    }
    _builder.append(" ");
    return _builder;
  }
  
  @Override
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
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
    _builder.append("\t\t");
    EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayArg);
    CharSequence _setInputCases = this.setInputCases("value", null, _of);
    _builder.append(_setInputCases, "\t\t");
    _builder.newLineIfNotEmpty();
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
    _builder.append("Integer idx=varIdx.get(name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (idx==null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("throw new IllegalArgumentException(\"The name:\"+name+\" is not a valid index\");");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return idx;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      for(final VariableInformation v : this.em.variables) {
        _builder.append("\t");
        _builder.append("case ");
        int _varIdx = this.getVarIdx(v, false);
        _builder.append(_varIdx, "\t");
        _builder.append(": return \"");
        _builder.append(v.name, "\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t");
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
    _builder.append("\t\t");
    EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isArrayArg);
    CharSequence _outputCases = this.getOutputCases(null, _of_1);
    _builder.append(_outputCases, "\t\t");
    _builder.newLineIfNotEmpty();
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
    _builder.append("public long getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return deltaCycle;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void close(){");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public VariableInformation[] getVariableInformation(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("VariableInformation[] res=new VariableInformation[");
    int _length = this.em.variables.length;
    _builder.append(_length, "\t");
    _builder.append("];");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    int count = 0;
    _builder.newLineIfNotEmpty();
    {
      for(final VariableInformation v_1 : this.em.variables) {
        _builder.append("\t");
        _builder.append("res[");
        int _plusPlus = count++;
        _builder.append(_plusPlus, "\t");
        _builder.append("]=new VariableInformation(VariableInformation.Direction.");
        String _name = v_1.dir.name();
        _builder.append(_name, "\t");
        _builder.append(", \"");
        _builder.append(v_1.name, "\t");
        _builder.append("\", ");
        _builder.append(v_1.width, "\t");
        _builder.append(", VariableInformation.Type.");
        String _name_1 = v_1.type.name();
        _builder.append(_name_1, "\t");
        _builder.append(", ");
        _builder.append(v_1.isRegister, "\t");
        _builder.append(", ");
        _builder.append(v_1.isClock, "\t");
        _builder.append(", ");
        _builder.append(v_1.isReset, "\t");
        _builder.append(", new String[]{");
        String[] _annotations = v_1.annotations;
        String _join = null;
        if (((Iterable<String>)Conversions.doWrapArray(_annotations))!=null) {
          final Function1<String, CharSequence> _function = new Function1<String, CharSequence>() {
            @Override
            public CharSequence apply(final String it) {
              return it;
            }
          };
          _join=IterableExtensions.<String>join(((Iterable<String>)Conversions.doWrapArray(_annotations)), "\"", ",", "\"", _function);
        }
        _builder.append(_join, "\t");
        _builder.append("}, new int[]{");
        int[] _dimensions = v_1.dimensions;
        String _join_1 = null;
        if (((Iterable<?>)Conversions.doWrapArray(_dimensions))!=null) {
          _join_1=IterableExtensions.join(((Iterable<?>)Conversions.doWrapArray(_dimensions)), ",");
        }
        _builder.append(_join_1, "\t");
        _builder.append("});");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return res;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setFeature(Feature feature, Object value) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (feature) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("case disableOutputRegs:");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t\t\t");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t\t\t");
        _builder.append(" = (boolean) value;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("case disableEdges:");
    _builder.newLine();
    {
      if (this.hasClock) {
        _builder.append("\t\t\t");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t\t\t");
        _builder.append(" = (boolean) value;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      if ((this.executionCores != 0)) {
        StringBuilder _makeThreads = this.makeThreads();
        _builder.append(_makeThreads, "");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (this.hasPow) {
        _builder.append("private static long pow(long a, long n) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("long result = 1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("long p = a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("while (n > 0) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if ((n % 2) != 0) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("result = result * p;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("p = p * p;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("n = n / 2;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return result;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    {
      boolean _isEmpty = ((List<FunctionInformation>)Conversions.doWrapArray(this.em.functions)).isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        _builder.append("public JavaPSHDLLib pshdl=new JavaPSHDLLib(this);");
        _builder.newLine();
        StringBuilder _generateInlineMethods = this.generateInlineMethods();
        _builder.append(_generateInlineMethods, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public StringBuilder generateInlineMethods() {
    final StringBuilder sb = new StringBuilder();
    for (final FunctionInformation fi : this.em.functions) {
      boolean _startsWith = fi.name.startsWith("pshdl.");
      if (_startsWith) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("private ");
        String _java = this.toJava(fi.returnType);
        _builder.append(_java, "");
        _builder.append(" ");
        String _signature = fi.signature();
        _builder.append(_signature, "");
        _builder.append("(");
        {
          boolean _hasElements = false;
          for(final ParameterInformation pi : fi.parameter) {
            if (!_hasElements) {
              _hasElements = true;
            } else {
              _builder.appendImmediate(", ", "");
            }
            String _java_1 = this.toJava(pi);
            _builder.append(_java_1, "");
            _builder.append(" p");
            String _firstUpper = StringExtensions.toFirstUpper(pi.name);
            _builder.append(_firstUpper, "");
          }
        }
        _builder.append("){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        {
          if ((fi.returnType != null)) {
            _builder.append("return ");
          }
        }
        _builder.append("pshdl.");
        int _lastIndexOf = fi.name.lastIndexOf(".");
        int _plus = (_lastIndexOf + 1);
        String _substring = fi.name.substring(_plus);
        _builder.append(_substring, "\t");
        _builder.append("(");
        {
          boolean _hasElements_1 = false;
          for(final ParameterInformation pi_1 : fi.parameter) {
            if (!_hasElements_1) {
              _hasElements_1 = true;
            } else {
              _builder.appendImmediate(", ", "\t");
            }
            _builder.append("p");
            String _firstUpper_1 = StringExtensions.toFirstUpper(pi_1.name);
            _builder.append(_firstUpper_1, "\t");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        sb.append(_builder);
      }
    }
    return sb;
  }
  
  public String toJava(final ParameterInformation information) {
    if ((information == null)) {
      return "void";
    }
    if ((information.type == ParameterInformation.Type.PARAM_BOOL)) {
      return "boolean";
    }
    if ((information.type == ParameterInformation.Type.PARAM_STRING)) {
      return "String";
    }
    return "long";
  }
  
  protected CharSequence getterSetter(final VariableInformation v, final VariableInformation aliased) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public ");
    CharSequence _fieldType = this.fieldType(v, CommonCodeGenerator.NONE);
    _builder.append(_fieldType, "");
    _builder.append(" get");
    CharSequence _idName = this.idName(v, false, CommonCodeGenerator.NONE);
    String _string = _idName.toString();
    String _firstUpper = StringExtensions.toFirstUpper(_string);
    _builder.append(_firstUpper, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    {
      boolean _or = false;
      boolean _isPredicate = this.isPredicate(aliased);
      if (_isPredicate) {
        _or = true;
      } else {
        boolean _isArray = this.isArray(aliased);
        _or = _isArray;
      }
      if (_or) {
        _builder.append("\t");
        _builder.append("return ");
        CharSequence _idName_1 = this.idName(aliased, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_1, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("return ");
        CharSequence _idName_2 = this.idName(aliased, true, CommonCodeGenerator.NONE);
        int _targetSizeWithType = this.getTargetSizeWithType(aliased);
        CharSequence _fixupValue = this.fixupValue(_idName_2, _targetSizeWithType, true);
        _builder.append(_fixupValue, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public void set");
    CharSequence _idName_3 = this.idName(v, false, CommonCodeGenerator.NONE);
    String _string_1 = _idName_3.toString();
    String _firstUpper_1 = StringExtensions.toFirstUpper(_string_1);
    _builder.append(_firstUpper_1, "");
    _builder.append("(");
    CharSequence _fieldType_1 = this.fieldType(v, CommonCodeGenerator.NONE);
    _builder.append(_fieldType_1, "");
    _builder.append(" newVal){");
    _builder.newLineIfNotEmpty();
    {
      boolean _or_1 = false;
      boolean _isPredicate_1 = this.isPredicate(aliased);
      if (_isPredicate_1) {
        _or_1 = true;
      } else {
        boolean _isArray_1 = this.isArray(aliased);
        _or_1 = _isArray_1;
      }
      if (_or_1) {
        _builder.append("\t");
        CharSequence _idName_4 = this.idName(aliased, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_4, "\t");
        _builder.append("=newVal;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        CharSequence _idName_5 = this.idName(aliased, true, CommonCodeGenerator.NONE);
        _builder.append(_idName_5, "\t");
        _builder.append("=");
        int _targetSizeWithType_1 = this.getTargetSizeWithType(aliased);
        CharSequence _fixupValue_1 = this.fixupValue("newVal", _targetSizeWithType_1, true);
        _builder.append(_fixupValue_1, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private Random r = new Random();
  
  public StringBuilder makeThreads() {
    StringBuilder _xblockexpression = null;
    {
      final Set<Integer> handledNegEdges = Sets.<Integer>newLinkedHashSet();
      final Set<Integer> handledPosEdges = Sets.<Integer>newLinkedHashSet();
      final Set<Integer> handledPredicates = Sets.<Integer>newLinkedHashSet();
      final Multimap<Integer, Frame> stageFrames = LinkedHashMultimap.<Integer, Frame>create();
      final Function1<Frame, Boolean> _function = new Function1<Frame, Boolean>() {
        @Override
        public Boolean apply(final Frame it) {
          return Boolean.valueOf((!it.constant));
        }
      };
      Iterable<Frame> _filter = IterableExtensions.<Frame>filter(((Iterable<Frame>)Conversions.doWrapArray(this.em.frames)), _function);
      final Procedure1<Frame> _function_1 = new Procedure1<Frame>() {
        @Override
        public void apply(final Frame it) {
          stageFrames.put(Integer.valueOf(it.scheduleStage), it);
        }
      };
      IterableExtensions.<Frame>forEach(_filter, _function_1);
      Set<Integer> _keySet = stageFrames.keySet();
      List<Integer> _sort = IterableExtensions.<Integer>sort(_keySet);
      int maxStage = (int) IterableExtensions.<Integer>last(_sort);
      final StringBuilder ctrlThread = new StringBuilder();
      final StringBuilder phaseMethods = new StringBuilder();
      final List<StringBuilder> execution = Lists.<StringBuilder>newArrayListWithCapacity(this.executionCores);
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, this.executionCores, true);
      for (final int i : _doubleDotLessThan) {
        StringBuilder _stringBuilder = new StringBuilder();
        execution.add(_stringBuilder);
      }
      ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, maxStage, true);
      for (final int stage : _doubleDotLessThan_1) {
        {
          final Collection<Frame> matchingFrames = stageFrames.get(Integer.valueOf(stage));
          boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(matchingFrames);
          boolean _not = (!_isNullOrEmpty);
          if (_not) {
            for (final Frame frame : matchingFrames) {
              {
                final CharSequence negEdge = this.handleEdge(handledNegEdges, false, frame.edgeNegDepRes);
                String _string = negEdge.toString();
                boolean _isEmpty = _string.isEmpty();
                boolean _not_1 = (!_isEmpty);
                if (_not_1) {
                  ctrlThread.append(negEdge);
                }
                final CharSequence posEdge = this.handleEdge(handledPosEdges, true, frame.edgePosDepRes);
                String _string_1 = posEdge.toString();
                boolean _isEmpty_1 = _string_1.isEmpty();
                boolean _not_2 = (!_isEmpty_1);
                if (_not_2) {
                  ctrlThread.append(posEdge);
                }
                final CharSequence negPred = this.handlePredicates(handledPredicates, false, frame.predNegDepRes);
                String _string_2 = negPred.toString();
                boolean _isEmpty_2 = _string_2.isEmpty();
                boolean _not_3 = (!_isEmpty_2);
                if (_not_3) {
                  ctrlThread.append(negPred);
                }
                final CharSequence posPred = this.handlePredicates(handledPredicates, true, frame.predPosDepRes);
                String _string_3 = posPred.toString();
                boolean _isEmpty_3 = _string_3.isEmpty();
                boolean _not_4 = (!_isEmpty_3);
                if (_not_4) {
                  ctrlThread.append(posPred);
                }
              }
            }
            this.addBarrier(ctrlThread, execution, ("Predicates for stage:" + Integer.valueOf(stage)), stage);
            int totalCosts = 0;
            JavaCodeGenerator.ExecutionPhase current = new JavaCodeGenerator.ExecutionPhase(stage);
            final List<JavaCodeGenerator.ExecutionPhase> phases = Lists.<JavaCodeGenerator.ExecutionPhase>newArrayList(current);
            for (final Iterator<Frame> iterator = matchingFrames.iterator(); iterator.hasNext();) {
              {
                final Frame frame_1 = iterator.next();
                boolean _and = false;
                if (!this.purgeAliases) {
                  _and = false;
                } else {
                  boolean _isRename = frame_1.isRename(this.em);
                  _and = _isRename;
                }
                boolean _not_1 = (!_and);
                if (_not_1) {
                  int _talCosts = totalCosts;
                  int _estimateFrameCosts = this.estimateFrameCosts(frame_1);
                  totalCosts = (_talCosts + _estimateFrameCosts);
                  final CharSequence call = this.predicateCheckedFrameCall(frame_1);
                  current.executionCore.append(call);
                  boolean _and_1 = false;
                  if (!(totalCosts > 10)) {
                    _and_1 = false;
                  } else {
                    boolean _hasNext = iterator.hasNext();
                    _and_1 = _hasNext;
                  }
                  if (_and_1) {
                    totalCosts = 0;
                    JavaCodeGenerator.ExecutionPhase _executionPhase = new JavaCodeGenerator.ExecutionPhase(stage);
                    current = _executionPhase;
                    phases.add(current);
                  }
                }
              }
            }
            final Function1<JavaCodeGenerator.ExecutionPhase, CharSequence> _function_2 = new Function1<JavaCodeGenerator.ExecutionPhase, CharSequence>() {
              @Override
              public CharSequence apply(final JavaCodeGenerator.ExecutionPhase it) {
                return it.declare();
              }
            };
            List<CharSequence> _map = ListExtensions.<JavaCodeGenerator.ExecutionPhase, CharSequence>map(phases, _function_2);
            String _join = IterableExtensions.join(_map);
            phaseMethods.append(_join);
            final Function1<JavaCodeGenerator.ExecutionPhase, CharSequence> _function_3 = new Function1<JavaCodeGenerator.ExecutionPhase, CharSequence>() {
              @Override
              public CharSequence apply(final JavaCodeGenerator.ExecutionPhase it) {
                return it.call();
              }
            };
            List<CharSequence> _map_1 = ListExtensions.<JavaCodeGenerator.ExecutionPhase, CharSequence>map(phases, _function_3);
            String _join_1 = IterableExtensions.join(_map_1);
            ctrlThread.append(_join_1);
            ExclusiveRange _doubleDotLessThan_2 = new ExclusiveRange(0, this.executionCores, true);
            for (final int i_1 : _doubleDotLessThan_2) {
              {
                Collections.shuffle(phases, this.r);
                StringBuilder _get = execution.get(i_1);
                final Function1<JavaCodeGenerator.ExecutionPhase, CharSequence> _function_4 = new Function1<JavaCodeGenerator.ExecutionPhase, CharSequence>() {
                  @Override
                  public CharSequence apply(final JavaCodeGenerator.ExecutionPhase it) {
                    return it.call();
                  }
                };
                List<CharSequence> _map_2 = ListExtensions.<JavaCodeGenerator.ExecutionPhase, CharSequence>map(phases, _function_4);
                String _join_2 = IterableExtensions.join(_map_2);
                _get.append(_join_2);
              }
            }
          }
        }
      }
      final StringBuilder res = new StringBuilder();
      ExclusiveRange _doubleDotLessThan_2 = new ExclusiveRange(0, this.executionCores, true);
      for (final int tid : _doubleDotLessThan_2) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("private final Thread thread");
        _builder.append(tid, "");
        _builder.append("=new Thread(){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public void run(){");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("long startingTimeStamp;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("while(true){");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("while (phase==-1){}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("startingTimeStamp=timeStamp;");
        _builder.newLine();
        _builder.append("\t\t\t");
        StringBuilder _get = execution.get(tid);
        _builder.append(_get, "\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("//System.out.println(\"Execution of thread ");
        _builder.append(tid, "\t\t\t");
        _builder.append(" done\");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        res.append(_builder);
      }
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("\t");
      _builder_1.append(phaseMethods, "\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("private volatile int phase=-1;");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("public void initParallel() {");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("phase=-1;");
      _builder_1.newLine();
      {
        ExclusiveRange _doubleDotLessThan_3 = new ExclusiveRange(0, this.executionCores, true);
        for(final int tid_1 : _doubleDotLessThan_3) {
          _builder_1.append("\t\t");
          _builder_1.append("thread");
          _builder_1.append(tid_1, "\t\t");
          _builder_1.append(".setDaemon(true);");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("\t\t");
          _builder_1.append("thread");
          _builder_1.append(tid_1, "\t\t");
          _builder_1.append(".start();");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("public void parallelExecution(){");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("phase=-1;");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append(ctrlThread, "\t\t");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("\t\t");
      _builder_1.append("phase=-1;");
      _builder_1.newLine();
      _builder_1.append("\t\t");
      _builder_1.append("//System.out.println(\"Done\");");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _xblockexpression = res.append(_builder_1);
    }
    return _xblockexpression;
  }
  
  public StringBuilder addBarrier(final StringBuilder master, final List<StringBuilder> slaves, final String stageName, final int stage) {
    StringBuilder _xblockexpression = null;
    {
      int _size = slaves.size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final StringBuilder slave = slaves.get((i).intValue());
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("//");
          _builder.append(stageName, "");
          _builder.newLineIfNotEmpty();
          _builder.append("//System.out.println(\"Thread ");
          _builder.append(i, "");
          _builder.append(" waiting for next phase ");
          _builder.append(stageName, "");
          _builder.append("\");");
          _builder.newLineIfNotEmpty();
          _builder.append("while (phase < ");
          _builder.append(stage, "");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          _builder.append("if (startingTimeStamp!=timeStamp)");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("break;");
          _builder.newLine();
          slave.append(_builder);
        }
      }
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("//System.out.println(\"Entering stage: ");
      _builder.append(stageName, "");
      _builder.append("\");");
      _builder.newLineIfNotEmpty();
      _builder.append("try{");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Thread.sleep(1);");
      _builder.newLine();
      _builder.append("} catch(Exception e){");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.append("phase=");
      _builder.append(stage, "");
      _builder.append(";");
      _builder.newLineIfNotEmpty();
      _xblockexpression = master.append(_builder);
    }
    return _xblockexpression;
  }
  
  public StringBuilder schedule(final List<Integer> threadLoad, final List<StringBuilder> threadExecution, final int costs, final CharSequence execution) {
    StringBuilder _xblockexpression = null;
    {
      int min = Integer.MAX_VALUE;
      int tid = (-1);
      int _size = threadLoad.size();
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size, true);
      for (final Integer i : _doubleDotLessThan) {
        {
          final Integer load = threadLoad.get((i).intValue());
          if (((load).intValue() <= min)) {
            min = (load).intValue();
            tid = (i).intValue();
          }
        }
      }
      Integer _get = threadLoad.get(tid);
      int _plus = ((_get).intValue() + costs);
      threadLoad.set(tid, Integer.valueOf(_plus));
      StringBuilder _get_1 = threadExecution.get(tid);
      _xblockexpression = _get_1.append(execution);
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.packageName != null)) {
        _builder.append("package ");
        _builder.append(this.packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _imports = this.getImports();
    _builder.append(_imports, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public final class ");
    _builder.append(this.unitName, "");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("implements ");
    {
      boolean _isTestbench = this.isTestbench();
      if (_isTestbench) {
        _builder.append("IHDLTestbenchInterpreter");
      } else {
        _builder.append("IHDLInterpreter");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private Map<String, Integer> varIdx=new HashMap<String, Integer>();");
    _builder.newLine();
    return _builder;
  }
  
  protected boolean isTestbench() {
    boolean _and = false;
    boolean _isNullOrEmpty = IterableExtensions.isNullOrEmpty(((Iterable<?>)Conversions.doWrapArray(this.em.annotations)));
    boolean _not = (!_isNullOrEmpty);
    if (!_not) {
      _and = false;
    } else {
      String _name = HDLSimulator.TB_UNIT.getName();
      String _substring = _name.substring(1);
      boolean _contains = ((List<String>)Conversions.doWrapArray(this.em.annotations)).contains(_substring);
      _and = _contains;
    }
    return _and;
  }
  
  @Override
  protected CharSequence postFieldDeclarations() {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.maxCosts != Integer.MAX_VALUE)) {
        _builder.append("public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (this.hasClock) {
        _builder.append("private List<RegUpdate> regUpdates=new ArrayList<RegUpdate>();");
        _builder.newLine();
        _builder.newLine();
        _builder.append("/**");
        _builder.newLine();
        _builder.append("* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false");
        _builder.newLine();
        _builder.append("*/");
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this(");
        {
          if (this.hasClock) {
            _builder.append("false, false");
          }
        }
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("(boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "");
        _builder.append(", boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this.");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append("=");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        {
          for(final VariableInformation v : this.em.variables) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v.name, "\t");
            _builder.append("\", ");
            int _varIdx = this.getVarIdx(v, false);
            _builder.append(_varIdx, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("() {");
        _builder.newLineIfNotEmpty();
        {
          for(final VariableInformation v_1 : this.em.variables) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v_1.name, "\t");
            _builder.append("\", ");
            int _varIdx_1 = this.getVarIdx(v_1, false);
            _builder.append(_varIdx_1, "\t");
            _builder.append(");");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.append("public ");
        _builder.append(this.unitName, "");
        _builder.append("(boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_EDGES.name, "");
        _builder.append(", boolean ");
        _builder.append(CommonCodeGenerator.DISABLE_REG_OUTPUTLOGIC.name, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("this();");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      if (this.hasClock) {
        _builder.append("public boolean skipEdge(long local) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("long dc = local >>> 16l;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// Register was updated in previous delta cylce, that is ok");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (dc < ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
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
        _builder.append("if ((dc == ");
        _builder.append(CommonCodeGenerator.DELTA_CYCLE.name, "\t");
        _builder.append(") && ((local & 0xFFFF) == ");
        _builder.append(CommonCodeGenerator.EPS_CYCLE.name, "\t");
        _builder.append("))");
        _builder.newLineIfNotEmpty();
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
        _builder.newLine();
        CharSequence _copyRegs = this.copyRegs();
        _builder.append(_copyRegs, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  @Override
  protected CharSequence makeCase(final CharSequence caseLabel, final CharSequence value, final boolean includeBreak) {
    String _string = caseLabel.toString();
    String _replaceAll = _string.replaceAll("L", "");
    return super.makeCase(_replaceAll, value, includeBreak);
  }
  
  protected CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private void updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (RegUpdate reg : regUpdates) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (reg.internalID) {");
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
  
  protected CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.JavaPSHDLLib.TimeUnit;");
    _builder.newLine();
    _builder.append("import java.util.concurrent.*;");
    _builder.newLine();
    _builder.append("import java.util.concurrent.locks.*;");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence calculateVariableAccessIndex(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    final CharSequence res = super.calculateVariableAccessIndex(varInfo, attributes);
    int _length = res.length();
    boolean _tripleEquals = (_length == 0);
    if (_tripleEquals) {
      return res;
    }
    return (("(int)(" + res) + ")");
  }
  
  @Override
  protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger zero, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    final EnumSet<CommonCodeGenerator.Attributes> attrClone = attributes.clone();
    attrClone.add(CommonCodeGenerator.Attributes.baseType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("new ");
    CharSequence _fieldType = this.fieldType(varInfo, attrClone);
    _builder.append(_fieldType, "");
    _builder.append("[");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append("]");
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
    _builder.append("private final void ");
    CharSequence _frameName = this.getFrameName(frame);
    _builder.append(_frameName, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
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
    _builder.append("regUpdates.add(new RegUpdate(");
    Integer _regIdx = this.regIdx(outputInternal);
    _builder.append(_regIdx, "");
    _builder.append(", ");
    _builder.append(offset, "");
    _builder.append(", ");
    _builder.append(fillValue, "");
    _builder.append("));");
    return _builder;
  }
  
  public Integer regIdx(final InternalInformation information) {
    return this.regIdx.get(information.info.name);
  }
  
  @Override
  protected CharSequence runMethodsHeader(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void ");
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
  
  @Override
  protected CharSequence runMethodsFooter(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("();");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence stageMethodsFooter(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  private int threadID = 0;
  
  private int currentStage = 0;
  
  @Override
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean constant) {
    this.threadID = 0;
    this.currentStage = stage;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public void t");
    _builder.append(this.threadID, "\t\t");
    _builder.append("s_");
    _builder.append(stage, "\t\t");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    return res;
  }
  
  @Override
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("volatile int phase_");
    _builder.append(stage, "\t\t");
    _builder.append("=0;");
    _builder.newLineIfNotEmpty();
    {
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, this.threadID, true);
      for(final int i : _doubleDotLessThan) {
        _builder.append("\t\t");
        _builder.append("volatile int thread_");
        _builder.append(i, "\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t");
        _builder.append("=0;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("volatile int thread_sl_");
        _builder.append(i, "\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t");
        _builder.append("=0;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("public void executeStage");
    _builder.append(stage, "\t\t");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    {
      ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, this.threadID, true);
      for(final int i_1 : _doubleDotLessThan_1) {
        _builder.append("\t\t\t");
        _builder.append("Thread t");
        _builder.append(i_1, "\t\t\t");
        _builder.append("=new Thread(){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("public void run(){");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        _builder.append("while(true){");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("t");
        _builder.append(i_1, "\t\t\t\t\t\t");
        _builder.append("s_");
        _builder.append(stage, "\t\t\t\t\t\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("thread_");
        _builder.append(i_1, "\t\t\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t\t\t");
        _builder.append("++;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("while(thread_");
        _builder.append(i_1, "\t\t\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t\t\t");
        _builder.append(" != phase_");
        _builder.append(stage, "\t\t\t\t\t\t");
        _builder.append("){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t\t");
        _builder.append("thread_sl_");
        _builder.append(i_1, "\t\t\t\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t\t\t\t");
        _builder.append("++;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("t");
        _builder.append(i_1, "\t\t\t");
        _builder.append(".start();");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("while (true){");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("final long start = System.nanoTime();");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("for (int i=0;i<1000;i++){");
    _builder.newLine();
    {
      ExclusiveRange _doubleDotLessThan_2 = new ExclusiveRange(0, this.threadID, true);
      for(final int i_2 : _doubleDotLessThan_2) {
        _builder.append("\t\t\t\t\t");
        _builder.append("while(thread_");
        _builder.append(i_2, "\t\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t\t");
        _builder.append("==phase_");
        _builder.append(stage, "\t\t\t\t\t");
        _builder.append("){}");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t\t\t");
    _builder.append("phase_");
    _builder.append(stage, "\t\t\t\t\t");
    _builder.append("++;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("final long end = System.nanoTime();");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("System.out.println(\"Took: \"+(end-start)/1000.+\" ns \"+");
    {
      ExclusiveRange _doubleDotLessThan_3 = new ExclusiveRange(0, this.threadID, true);
      boolean _hasElements = false;
      for(final int i_3 : _doubleDotLessThan_3) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate("+\" \"+", "\t\t\t\t");
        }
        _builder.append("thread_sl_");
        _builder.append(i_3, "\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    {
      ExclusiveRange _doubleDotLessThan_4 = new ExclusiveRange(0, this.threadID, true);
      for(final int i_4 : _doubleDotLessThan_4) {
        _builder.append("\t\t\t\t");
        _builder.append("thread_sl_");
        _builder.append(i_4, "\t\t\t\t");
        _builder.append("_");
        _builder.append(stage, "\t\t\t\t");
        _builder.append("=0;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    final String res = _builder.toString();
    return res;
  }
  
  @Override
  protected CharSequence barrier() {
    this.threadID++;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public void t");
    _builder.append(this.threadID, "\t\t");
    _builder.append("s_");
    _builder.append(this.currentStage, "\t\t");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence stageMethodsHeader(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
  protected CharSequence applyRegUpdates() {
    return "updateRegs();";
  }
  
  @Override
  protected CharSequence checkRegupdates() {
    return "!regUpdates.isEmpty()";
  }
  
  public CharSequence createChangeAdapter(final boolean useInterface, final Predicate<VariableInformation> filter) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((this.packageName != null)) {
        _builder.append("package ");
        _builder.append(this.packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("import org.pshdl.interpreter.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.JavaPSHDLLib.TimeUnit;");
    _builder.newLine();
    _builder.append("import java.util.Arrays;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/**");
    _builder.newLine();
    {
      for(final String anno : this.em.annotations) {
        _builder.append("* ");
        _builder.append(anno, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class ");
    String _changeAdapterName = ((JavaCodeGeneratorParameter) this.parameter).changeAdapterName(useInterface);
    _builder.append(_changeAdapterName, "");
    _builder.append(" implements ");
    {
      boolean _isTestbench = this.isTestbench();
      if (_isTestbench) {
        _builder.append("IHDLTestbenchInterpreter");
      } else {
        _builder.append("IHDLInterpreter");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _fieldDeclarations = this.fieldDeclarations(false, true, filter);
    _builder.append(_fieldDeclarations, "\t");
    _builder.newLineIfNotEmpty();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)), new Function1<VariableInformation, Boolean>() {
              public Boolean apply(VariableInformation arg0) {
                return filter.apply(arg0);
              }
          });
          for(final VariableInformation varInfo : _filter) {
            _builder.append("\t");
            _builder.append("int ");
            CharSequence _idName = this.idName(varInfo, true, CommonCodeGenerator.NONE);
            _builder.append(_idName, "\t");
            _builder.append("_idx;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    CharSequence _interpreterClassName = this.getInterpreterClassName(useInterface);
    _builder.append(_interpreterClassName, "\t");
    _builder.append(" module;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private IChangeListener[] listeners;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private VariableInformation[] varInfos;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    {
      if (useInterface) {
        _builder.append("Generic");
      }
    }
    _builder.append("ChangeAdapter");
    _builder.append(this.unitName, "\t");
    _builder.append("(");
    CharSequence _interpreterClassName_1 = this.getInterpreterClassName(useInterface);
    _builder.append(_interpreterClassName_1, "\t");
    _builder.append(" module, IChangeListener ... listeners) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this.module=module;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.listeners=listeners;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.varInfos=module.getVariableInformation();");
    _builder.newLine();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _excludeNullAndAlias = this.excludeNullAndAlias(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)));
          Iterable<VariableInformation> _filter_1 = IterableExtensions.<VariableInformation>filter(_excludeNullAndAlias, new Function1<VariableInformation, Boolean>() {
              public Boolean apply(VariableInformation arg0) {
                return filter.apply(arg0);
              }
          });
          for(final VariableInformation varInfo_1 : _filter_1) {
            _builder.append("\t\t");
            CharSequence _idName_1 = this.idName(varInfo_1, true, CommonCodeGenerator.NONE);
            _builder.append(_idName_1, "\t\t");
            _builder.append("_idx=module.getIndex(\"");
            _builder.append(varInfo_1.name, "\t\t");
            _builder.append("\");");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void run() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.run();");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNullAndAlias_1 = this.excludeNullAndAlias(((Iterable<VariableInformation>)Conversions.doWrapArray(this.em.variables)));
      Iterable<VariableInformation> _filter_2 = IterableExtensions.<VariableInformation>filter(_excludeNullAndAlias_1, new Function1<VariableInformation, Boolean>() {
          public Boolean apply(VariableInformation arg0) {
            return filter.apply(arg0);
          }
      });
      for(final VariableInformation varInfo_2 : _filter_2) {
        _builder.append("\t\t");
        final CharSequence varName = this.idName(varInfo_2, true, CommonCodeGenerator.NONE);
        _builder.newLineIfNotEmpty();
        {
          if (useInterface) {
            _builder.append("\t\t");
            String _changedNotificationInterface = this.changedNotificationInterface(varInfo_2);
            _builder.append(_changedNotificationInterface, "\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _isArray = this.isArray(varInfo_2);
              if (_isArray) {
                {
                  boolean _isPredicate = this.isPredicate(varInfo_2);
                  if (_isPredicate) {
                    _builder.append("\t\t");
                    _builder.append("for (int i=0;i<");
                    int _arraySize = this.getArraySize(varInfo_2);
                    _builder.append(_arraySize, "\t\t");
                    _builder.append(";i++)");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t\t");
                    _builder.append("\t");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("[i]=module.getOutputLong(");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("_idx, i)!=0;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append("for (int i=0;i<");
                    int _arraySize_1 = this.getArraySize(varInfo_2);
                    _builder.append(_arraySize_1, "\t\t");
                    _builder.append(";i++)");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t\t");
                    _builder.append("\t");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("[i]=module.getOutputLong(");
                    _builder.append(varName, "\t\t\t");
                    _builder.append("_idx, i);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  boolean _isPredicate_1 = this.isPredicate(varInfo_2);
                  if (_isPredicate_1) {
                    _builder.append("\t\t");
                    _builder.append(varName, "\t\t");
                    _builder.append("=module.getOutputLong(");
                    _builder.append(varName, "\t\t");
                    _builder.append("_idx)!=0;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append(varName, "\t\t");
                    _builder.append("=module.getOutputLong(");
                    _builder.append(varName, "\t\t");
                    _builder.append("_idx);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          } else {
            _builder.append("\t\t");
            String _changedNotification = this.changedNotification(varInfo_2);
            _builder.append(_changedNotification, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append(varName, "\t\t");
            _builder.append("=module.");
            _builder.append(varName, "\t\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setInput(String name, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setInput(name, value, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setInput(int idx, long value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setInput(idx, value, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public int getIndex(String name) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getIndex(name);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getName(idx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getOutputLong(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputLong(name, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getOutputLong(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputLong(idx, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public long getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getDeltaCycle();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void initConstants() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.initConstants();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void close() throws Exception{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.close();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setFeature(Feature feature, Object value) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.setFeature(feature, value);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public VariableInformation[] getVariableInformation() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getVariableInformation();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      boolean _isTestbench_1 = this.isTestbench();
      if (_isTestbench_1) {
        _builder.append("\t");
        CharSequence _timeMethods = this.timeMethods();
        _builder.append(_timeMethods, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("@Override");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void runTestbench(long maxTime, long maxSteps, ITestbenchStepListener listener, Runnable main){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (main == null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("module.runTestbench(maxTime, maxSteps, listener, this);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("module.runTestbench(maxTime, maxSteps, listener, main);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence getInterpreterClassName(final boolean useInterface) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if (useInterface) {
        {
          boolean _isTestbench = this.isTestbench();
          if (_isTestbench) {
            _builder.append("IHDLTestbenchInterpreter");
          } else {
            _builder.append("IHDLInterpreter");
          }
        }
      } else {
        String _javaClassName = ((JavaCodeGeneratorParameter) this.parameter).javaClassName();
        _builder.append(_javaClassName, "");
      }
    }
    return _builder;
  }
  
  protected String changedNotificationInterface(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(
          CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, 
          true, _of);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if ((module.getOutputLong(");
        _builder.append(varName, "");
        _builder.append("_idx)!=0) != ");
        _builder.append(varName, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (IChangeListener listener:listeners)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), varInfos[");
        int _idx = this.getIdx(vi);
        _builder.append(_idx, "\t\t");
        _builder.append("], ");
        int _idx_1 = this.getIdx(vi);
        _builder.append(_idx_1, "\t\t");
        _builder.append(", ");
        _builder.append(varName, "\t\t");
        _builder.append(", (module.getOutputLong(");
        _builder.append(varName, "\t\t");
        _builder.append("_idx)!=0), ");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(", -1);");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (module.getOutputLong(");
        _builder_1.append(varName, "");
        _builder_1.append("_idx) != ");
        _builder_1.append(varName, "");
        _builder_1.append(")");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("for (IChangeListener listener:listeners)");
        _builder_1.newLine();
        _builder_1.append("\t\t");
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), varInfos[");
        int _idx_2 = this.getIdx(vi);
        _builder_1.append(_idx_2, "\t\t");
        _builder_1.append("], ");
        int _idx_3 = this.getIdx(vi);
        _builder_1.append(_idx_3, "\t\t");
        _builder_1.append(", ");
        _builder_1.append(varName, "\t\t");
        _builder_1.append(", module.getOutputLong(");
        _builder_1.append(varName, "\t\t");
        _builder_1.append("_idx));");
        _builder_1.newLineIfNotEmpty();
        return _builder_1.toString();
      }
    } else {
      boolean _isPredicate_1 = this.isPredicate(vi);
      if (_isPredicate_1) {
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(
          CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, 
          true, _of_1);
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("{");
        _builder_2.newLine();
        _builder_2.append("boolean[] tempArr=new boolean[");
        int _arraySize = this.getArraySize(vi);
        _builder_2.append(_arraySize, "");
        _builder_2.append("];");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("for (int i=0;i<");
        int _arraySize_1 = this.getArraySize(vi);
        _builder_2.append(_arraySize_1, "");
        _builder_2.append(";i++)");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("tempArr[i]=module.getOutputLong(");
        _builder_2.append(varName, "\t");
        _builder_2.append("_idx)!=0;");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("if (!Arrays.equals(tempArr,");
        _builder_2.append(varName, "");
        _builder_2.append("))");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("for (IChangeListener listener:listeners)");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("listener.valueChangedPredicateArray(getDeltaCycle(), varInfos[");
        int _idx_4 = this.getIdx(vi);
        _builder_2.append(_idx_4, "\t\t");
        _builder_2.append("], ");
        int _idx_5 = this.getIdx(vi);
        _builder_2.append(_idx_5, "\t\t");
        _builder_2.append(", ");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", tempArr, ");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(", -1);");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("}");
        _builder_2.newLine();
        return _builder_2.toString();
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("{");
        _builder_3.newLine();
        _builder_3.append("long[] tempArr=new long[");
        int _arraySize_2 = this.getArraySize(vi);
        _builder_3.append(_arraySize_2, "");
        _builder_3.append("];");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("for (int i=0;i<");
        int _arraySize_3 = this.getArraySize(vi);
        _builder_3.append(_arraySize_3, "");
        _builder_3.append(";i++)");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("tempArr[i]=module.getOutputLong(");
        _builder_3.append(varName, "\t");
        _builder_3.append("_idx, i);");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("if (!Arrays.equals(tempArr,");
        _builder_3.append(varName, "");
        _builder_3.append("))");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("for (IChangeListener listener:listeners)");
        _builder_3.newLine();
        _builder_3.append("\t\t");
        _builder_3.append("listener.valueChangedLongArray(getDeltaCycle(), varInfos[");
        int _idx_6 = this.getIdx(vi);
        _builder_3.append(_idx_6, "\t\t");
        _builder_3.append("], ");
        int _idx_7 = this.getIdx(vi);
        _builder_3.append(_idx_7, "\t\t");
        _builder_3.append(", ");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(", tempArr);");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("}");
        _builder_3.newLine();
        return _builder_3.toString();
      }
    }
  }
  
  public int getIdx(final VariableInformation information) {
    return this.getVarIdx(information, false);
  }
  
  protected String changedNotification(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(
          CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, 
          true, _of);
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("if (module.");
        _builder.append(varName, "");
        _builder.append(" != ");
        _builder.append(varName, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("for (IChangeListener listener:listeners)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), varInfos[");
        int _idx = this.getIdx(vi);
        _builder.append(_idx, "\t\t");
        _builder.append("], ");
        int _idx_1 = this.getIdx(vi);
        _builder.append(_idx_1, "\t\t");
        _builder.append(", ");
        _builder.append(varName, "\t\t");
        _builder.append(", module.");
        _builder.append(varName, "\t\t");
        _builder.append(", ");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(", module.");
        _builder.append(varNameUpdate, "\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        return _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("if (module.");
        _builder_1.append(varName, "");
        _builder_1.append(" != ");
        _builder_1.append(varName, "");
        _builder_1.append(")");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("\t");
        _builder_1.append("for (IChangeListener listener:listeners)");
        _builder_1.newLine();
        _builder_1.append("\t\t");
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), varInfos[");
        int _idx_2 = this.getIdx(vi);
        _builder_1.append(_idx_2, "\t\t");
        _builder_1.append("], ");
        int _idx_3 = this.getIdx(vi);
        _builder_1.append(_idx_3, "\t\t");
        _builder_1.append(", ");
        _builder_1.append(varName, "\t\t");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            BigInteger _calcMask = this.calcMask(vi.width);
            CharSequence _constant = this.constant(_calcMask, true);
            _builder_1.append(_constant, "\t\t");
          }
        }
        _builder_1.append(", module.");
        _builder_1.append(varName, "\t\t");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            BigInteger _calcMask_1 = this.calcMask(vi.width);
            CharSequence _constant_1 = this.constant(_calcMask_1, true);
            _builder_1.append(_constant_1, "\t\t");
          }
        }
        _builder_1.append(");");
        _builder_1.newLineIfNotEmpty();
        return _builder_1.toString();
      }
    } else {
      boolean _isPredicate_1 = this.isPredicate(vi);
      if (_isPredicate_1) {
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(
          CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, 
          true, _of_1);
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("if (!module.");
        _builder_2.append(varName, "");
        _builder_2.append(".equals(");
        _builder_2.append(varName, "");
        _builder_2.append("))");
        _builder_2.newLineIfNotEmpty();
        _builder_2.append("\t");
        _builder_2.append("for (IChangeListener listener:listeners)");
        _builder_2.newLine();
        _builder_2.append("\t\t");
        _builder_2.append("listener.valueChangedPredicateArray(getDeltaCycle(), varInfos[");
        int _idx_4 = this.getIdx(vi);
        _builder_2.append(_idx_4, "\t\t");
        _builder_2.append("], ");
        int _idx_5 = this.getIdx(vi);
        _builder_2.append(_idx_5, "\t\t");
        _builder_2.append(", ");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", module.");
        _builder_2.append(varName, "\t\t");
        _builder_2.append(", ");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(", module.");
        _builder_2.append(varNameUpdate_1, "\t\t");
        _builder_2.append(");");
        _builder_2.newLineIfNotEmpty();
        return _builder_2.toString();
      } else {
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("if (!module.");
        _builder_3.append(varName, "");
        _builder_3.append(".equals(");
        _builder_3.append(varName, "");
        _builder_3.append("))");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("\t");
        _builder_3.append("for (IChangeListener listener:listeners)");
        _builder_3.newLine();
        _builder_3.append("\t\t");
        _builder_3.append("listener.valueChangedLongArray(getDeltaCycle(), varInfos[");
        int _idx_6 = this.getIdx(vi);
        _builder_3.append(_idx_6, "\t\t");
        _builder_3.append("], ");
        int _idx_7 = this.getIdx(vi);
        _builder_3.append(_idx_7, "\t\t");
        _builder_3.append(", ");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(", module.");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(");");
        _builder_3.newLineIfNotEmpty();
        return _builder_3.toString();
      }
    }
  }
  
  @Override
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdates.clear();");
    _builder.newLine();
    return _builder;
  }
  
  @Override
  protected CharSequence copyArray(final VariableInformation varInfo) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("System.arraycopy(");
    CharSequence _idName = this.idName(varInfo, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(", 0, ");
    EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isPrev);
    CharSequence _idName_1 = this.idName(varInfo, true, _of);
    _builder.append(_idName_1, "");
    _builder.append(", 0, ");
    int _arraySize = this.getArraySize(varInfo);
    _builder.append(_arraySize, "");
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  @Override
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
  
  @Override
  protected CharSequence callMethod(final CharSequence methodName, final CharSequence... args) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(methodName, "");
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
            _builder.append(arg, "");
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
    _builder.append("doRun.run();");
    _builder.newLine();
    return _builder;
  }
  
  @Override
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
  
  @Override
  protected CharSequence runProcessHeader(final CommonCodeGenerator.ProcessData pd) {
    CharSequence _xblockexpression = null;
    {
      this.indent++;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("private boolean ");
      String _processMethodName = this.processMethodName(pd);
      _builder.append(_processMethodName, "");
      _builder.append("() {");
      _builder.newLineIfNotEmpty();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  @Override
  protected CharSequence runTestbenchHeader() {
    CharSequence _xblockexpression = null;
    {
      this.indent++;
      StringConcatenation _builder = new StringConcatenation();
      CharSequence _timeMethods = this.timeMethods();
      _builder.append(_timeMethods, "");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("@Override");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener, Runnable main) {");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("Runnable doRun=(main==null?this:main);");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence timeMethods() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public TimeUnit getTimeBase(){");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return TimeUnit.");
    String _annoValue = this.getAnnoValue("TimeBase");
    _builder.append(_annoValue, "\t\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("public long getTime(){");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return ");
    VariableInformation _timeName = this.timeName();
    CharSequence _fieldName = this.fieldName(_timeName, CommonCodeGenerator.NONE);
    _builder.append(_fieldName, "\t\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected String getAnnoValue(final String anno) {
    final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
      @Override
      public Boolean apply(final String it) {
        return Boolean.valueOf(it.startsWith(anno));
      }
    };
    final String foundAnno = IterableExtensions.<String>findFirst(((Iterable<String>)Conversions.doWrapArray(this.em.annotations)), _function);
    int _indexOf = foundAnno.indexOf(SimulationTransformationExtension.ANNO_VALUE_SEP);
    int _plus = (_indexOf + 1);
    return foundAnno.substring(_plus);
  }
  
  @Override
  public String getHookName() {
    return "Java";
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
    ArrayList<PSAbstractCompiler.CompileResult> _xblockexpression = null;
    {
      final JavaCodeGeneratorParameter javaParam = new JavaCodeGeneratorParameter(em);
      final String optionPkg = cli.getOptionValue("pkg");
      if ((optionPkg != null)) {
        javaParam.packageName = optionPkg;
      }
      _xblockexpression = JavaCodeGenerator.doCompile(syntaxProblems, javaParam);
    }
    return _xblockexpression;
  }
  
  public static ArrayList<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final JavaCodeGeneratorParameter parameter) {
    final JavaCodeGenerator comp = new JavaCodeGenerator(parameter);
    final String code = comp.generateMainCode();
    final ArrayList<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent>newArrayList();
    Iterable<AuxiliaryContent> _auxiliaryContent = comp.getAuxiliaryContent();
    Iterables.<AuxiliaryContent>addAll(sideFiles, _auxiliaryContent);
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, code, parameter.em.moduleName, sideFiles, 
      parameter.em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  @Override
  protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Arrays.fill(");
    CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
    _builder.append(_idName, "");
    _builder.append(", ");
    _builder.append(regFillValue, "");
    _builder.append(");");
    return _builder;
  }
  
  @Override
  protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand, final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
    this.hasPow = true;
    VariableInformation.Type _typeFromTargetSize = this.typeFromTargetSize(targetSizeWithType);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("pow(");
    String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName, "");
    _builder.append(", ");
    String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
    _builder.append(_tempName_1, "");
    _builder.append(")");
    return this.assignTempVar(_typeFromTargetSize, targetSizeWithType, pos, 
      CommonCodeGenerator.NONE, _builder, true);
  }
}
