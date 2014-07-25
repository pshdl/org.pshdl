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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.HDLSimulator;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.IHDLGenerator;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class JavaCompiler implements ITypeOuptutProvider {
  private boolean debug;
  
  @Extension
  private CommonCompilerExtension cce;
  
  public JavaCompiler() {
  }
  
  public JavaCompiler(final ExecutableModel em, final boolean includeDebug) {
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em, 64);
    this.cce = _commonCompilerExtension;
    this.debug = includeDebug;
  }
  
  public static ArrayList<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final ExecutableModel em, final String pkg, final String unitName, final boolean debug) {
    final JavaCompiler comp = new JavaCompiler(em, debug);
    CharSequence _compile = comp.compile(pkg, unitName);
    final String code = _compile.toString();
    List<IHDLGenerator.SideFile> _emptyList = Collections.<IHDLGenerator.SideFile>emptyList();
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, _emptyList, em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
  public CharSequence coverage(final String packageName, final String unitName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (packageName != null);
      if (_tripleNotEquals) {
        _builder.append("package ");
        _builder.append(packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    CharSequence _imports = this.getImports();
    _builder.append(_imports, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("public class ");
    _builder.append(unitName, "");
    _builder.append("Coverage {");
    _builder.newLineIfNotEmpty();
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
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence compile(final String packageName, final String unitName) {
    CharSequence _xblockexpression = null;
    {
      final Set<Integer> handled = new HashSet<Integer>();
      final Set<Integer> handledPosEdge = new HashSet<Integer>();
      final Set<Integer> handledNegEdge = new HashSet<Integer>();
      handled.add(Integer.valueOf((-1)));
      handledPosEdge.add(Integer.valueOf((-1)));
      handledNegEdge.add(Integer.valueOf((-1)));
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _tripleNotEquals = (packageName != null);
        if (_tripleNotEquals) {
          _builder.append("package ");
          _builder.append(packageName, "");
          _builder.append(";");
        }
      }
      _builder.newLineIfNotEmpty();
      CharSequence _imports = this.getImports();
      _builder.append(_imports, "");
      _builder.newLineIfNotEmpty();
      _builder.newLine();
      _builder.append("public class ");
      _builder.append(unitName, "");
      _builder.append(" implements ");
      {
        String _name = HDLSimulator.TB_UNIT.getName();
        String _substring = _name.substring(1);
        boolean _contains = ((List<String>)Conversions.doWrapArray(this.cce.em.annotations)).contains(_substring);
        if (_contains) {
          _builder.append("IHDLTestbenchInterpreter");
        } else {
          _builder.append("IHDLInterpreter");
        }
      }
      _builder.append("{");
      _builder.newLineIfNotEmpty();
      {
        if (this.cce.hasClock) {
          _builder.append("\t");
          _builder.append("private Set<RegUpdate> regUpdates=new LinkedHashSet<RegUpdate>();");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("private final boolean disableEdges;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("private final boolean disabledRegOutputlogic;");
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
      _builder.append("private int epsCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("private int deltaCycle=0;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("private Map<String, Integer> varIdx=new HashMap<String, Integer>();");
      _builder.newLine();
      {
        if (this.debug) {
          _builder.append("\t");
          _builder.append("private final IDebugListener listener;");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("private final ExecutableModel em;");
          _builder.newLine();
        }
      }
      {
        boolean _or = false;
        if (this.cce.hasClock) {
          _or = true;
        } else {
          _or = this.debug;
        }
        if (_or) {
          _builder.append("\t");
          _builder.append("/**");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("*/");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("public ");
          _builder.append(unitName, "\t");
          _builder.append("() {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("this(false, false");
          {
            if (this.debug) {
              _builder.append(", null, null");
            }
          }
          _builder.append(");");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("public ");
      _builder.append(unitName, "\t");
      _builder.append("(boolean disableEdges, boolean disabledRegOutputlogic");
      {
        if (this.debug) {
          _builder.append(", IDebugListener listener, ExecutableModel em");
        }
      }
      _builder.append(") {");
      _builder.newLineIfNotEmpty();
      {
        if (this.cce.hasClock) {
          _builder.append("\t\t");
          _builder.append("this.disableEdges=disableEdges;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("this.disabledRegOutputlogic=disabledRegOutputlogic;");
          _builder.newLine();
        }
      }
      {
        if (this.debug) {
          _builder.append("\t\t");
          _builder.append("this.listener=listener;");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("this.em=em;");
          _builder.newLine();
        }
      }
      {
        Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
        for(final VariableInformation v_1 : _excludeNull_1) {
          _builder.append("\t\t");
          CharSequence _init = this.init(v_1);
          _builder.append(_init, "\t\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t");
          _builder.append("varIdx.put(\"");
          _builder.append(v_1.name, "\t\t");
          _builder.append("\", ");
          Integer _get_1 = this.cce.varIdx.get(v_1.name);
          _builder.append(_get_1, "\t\t");
          _builder.append(");");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _beanMethods = this.beanMethods();
      _builder.append(_beanMethods, "\t");
      _builder.newLineIfNotEmpty();
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
          _builder.append("public boolean skipEdge(long local) {");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("long dc = local >>> 16l;");
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
      _builder.append("\t");
      CharSequence _runMethod = this.runMethod(this.cce.em, handled, handledNegEdge, handledPosEdge);
      _builder.append(_runMethod, "\t");
      _builder.newLineIfNotEmpty();
      {
        Iterable<Frame> _processframes = this.cce.getProcessframes(this.cce.em);
        boolean _isEmpty = IterableExtensions.isEmpty(_processframes);
        boolean _not = (!_isEmpty);
        if (_not) {
          _builder.append("\t");
          String _testbenchMethod = this.testbenchMethod(this.cce.em, handled, handledNegEdge, handledPosEdge);
          _builder.append(_testbenchMethod, "\t");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("}");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public CharSequence beanMethods() {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          return Boolean.valueOf((it.dir != VariableInformation.Direction.INTERNAL));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(((Iterable<VariableInformation>)Conversions.doWrapArray(this.cce.em.variables)), _function);
      for(final VariableInformation v : _filter) {
        _builder.newLineIfNotEmpty();
        {
          int _size = IterableExtensions.size(((Iterable<?>)Conversions.doWrapArray(v.dimensions)));
          boolean _equals = (_size == 0);
          if (_equals) {
            _builder.append("public void set");
            String _idName = this.cce.idName(v, false, false);
            String _firstUpper = StringExtensions.toFirstUpper(_idName);
            _builder.append(_firstUpper, "");
            _builder.append("(");
            String _javaType = this.getJavaType(v);
            _builder.append(_javaType, "");
            _builder.append(" value) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _idName_1 = this.cce.idName(v, false, false);
            _builder.append(_idName_1, "\t");
            _builder.append("=value & ");
            CharSequence _asMaskL = this.cce.asMaskL(v.width);
            _builder.append(_asMaskL, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("public ");
            String _javaType_1 = this.getJavaType(v);
            _builder.append(_javaType_1, "");
            _builder.append(" get");
            String _idName_2 = this.cce.idName(v, false, false);
            String _firstUpper_1 = StringExtensions.toFirstUpper(_idName_2);
            _builder.append(_firstUpper_1, "");
            _builder.append("() {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("return ");
            String _idName_3 = this.cce.idName(v, false, false);
            _builder.append(_idName_3, "\t");
            _builder.append(" & ");
            CharSequence _asMaskL_1 = this.cce.asMaskL(v.width);
            _builder.append(_asMaskL_1, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("public void set");
            String _idName_4 = this.cce.idName(v, false, false);
            String _firstUpper_2 = StringExtensions.toFirstUpper(_idName_4);
            _builder.append(_firstUpper_2, "");
            _builder.append("(");
            String _javaType_2 = this.getJavaType(v);
            _builder.append(_javaType_2, "");
            _builder.append(" value");
            {
              int _size_1 = IterableExtensions.size(((Iterable<?>)Conversions.doWrapArray(v.dimensions)));
              ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, _size_1, true);
              for(final Integer i : _doubleDotLessThan) {
                _builder.append(", int a");
                _builder.append(i, "");
              }
            }
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _idName_5 = this.cce.idName(v, false, false);
            _builder.append(_idName_5, "\t");
            _builder.append("[");
            StringBuilder _arrayAccess = this.cce.arrayAccess(v);
            _builder.append(_arrayAccess, "\t");
            _builder.append("]=value & ");
            CharSequence _asMaskL_2 = this.cce.asMaskL(v.width);
            _builder.append(_asMaskL_2, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            _builder.newLine();
            _builder.append("public ");
            String _javaType_3 = this.getJavaType(v);
            _builder.append(_javaType_3, "");
            _builder.append(" get");
            String _idName_6 = this.cce.idName(v, false, false);
            String _firstUpper_3 = StringExtensions.toFirstUpper(_idName_6);
            _builder.append(_firstUpper_3, "");
            _builder.append("(");
            {
              int _size_2 = IterableExtensions.size(((Iterable<?>)Conversions.doWrapArray(v.dimensions)));
              ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, _size_2, true);
              boolean _hasElements = false;
              for(final Integer i_1 : _doubleDotLessThan_1) {
                if (!_hasElements) {
                  _hasElements = true;
                } else {
                  _builder.appendImmediate(",", "");
                }
                _builder.append("int a");
                _builder.append(i_1, "");
              }
            }
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("return ");
            String _idName_7 = this.cce.idName(v, false, false);
            _builder.append(_idName_7, "\t");
            _builder.append("[");
            StringBuilder _arrayAccess_1 = this.cce.arrayAccess(v);
            _builder.append(_arrayAccess_1, "\t");
            _builder.append("] & ");
            CharSequence _asMaskL_3 = this.cce.asMaskL(v.width);
            _builder.append(_asMaskL_3, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
      }
    }
    return _builder;
  }
  
  public String testbenchMethod(final ExecutableModel model, final Set<Integer> handled, final Set<Integer> handledNegEdge, final Set<Integer> handledPosEdge) {
    Iterable<Frame> _processframes = this.cce.getProcessframes(model);
    final Function1<Frame, String> _function = new Function1<Frame, String>() {
      public String apply(final Frame it) {
        return it.process;
      }
    };
    Iterable<String> _map = IterableExtensions.<Frame, String>map(_processframes, _function);
    final Function1<String, CharSequence> _function_1 = new Function1<String, CharSequence>() {
      public CharSequence apply(final String it) {
        StringConcatenation _builder = new StringConcatenation();
        return _builder.toString();
      }
    };
    final Map<String, CharSequence> processes = IterableExtensions.<String, CharSequence>toInvertedMap(_map, _function_1);
    Iterable<Frame> _processframes_1 = this.cce.getProcessframes(model);
    final Procedure1<Frame> _function_2 = new Procedure1<Frame>() {
      public void apply(final Frame it) {
        CharSequence _get = processes.get(it.process);
        StringConcatenation _builder = new StringConcatenation();
        CharSequence _callFrame = JavaCompiler.this.callFrame(it, handled, handledNegEdge, handledPosEdge);
        _builder.append(_callFrame, "");
        _builder.newLineIfNotEmpty();
        String _plus = (_get + _builder.toString());
        processes.put(it.process, _plus);
      }
    };
    IterableExtensions.<Frame>forEach(_processframes_1, _function_2);
    StringConcatenation _builder = new StringConcatenation();
    {
      Set<Map.Entry<String, CharSequence>> _entrySet = processes.entrySet();
      for(final Map.Entry<String, CharSequence> e : _entrySet) {
        _builder.append("private boolean runProcess_");
        String _key = e.getKey();
        _builder.append(_key, "");
        _builder.append("(){");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("long oldTime=");
        String _key_1 = e.getKey();
        String _processTime = this.cce.processTime(model, _key_1);
        _builder.append(_processTime, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("long oldState=");
        String _key_2 = e.getKey();
        String _processState = this.cce.processState(model, _key_2);
        _builder.append(_processState, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("while (");
        String _varByName = this.cce.varByName(model, "$time");
        _builder.append(_varByName, "\t");
        _builder.append(">=");
        String _key_3 = e.getKey();
        String _processTime_1 = this.cce.processTime(model, _key_3);
        _builder.append(_processTime_1, "\t");
        _builder.append(" && ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        String _key_4 = e.getKey();
        String _processState_1 = this.cce.processState(model, _key_4);
        _builder.append(_processState_1, "\t\t\t");
        _builder.append(">=0 &&");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        String _key_5 = e.getKey();
        String _processState_2 = this.cce.processState(model, _key_5);
        _builder.append(_processState_2, "\t\t\t");
        _builder.append("!= 0x7FFF_FFFF) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        CharSequence _value = e.getValue();
        _builder.append(_value, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return (oldTime != ");
        String _key_6 = e.getKey();
        String _processTime_2 = this.cce.processTime(model, _key_6);
        _builder.append(_processTime_2, "\t");
        _builder.append(") || (oldState != ");
        String _key_7 = e.getKey();
        String _processState_3 = this.cce.processState(model, _key_7);
        _builder.append(_processState_3, "\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("long stepCount=0;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("while (");
    String _varByName_1 = this.cce.varByName(model, "$time");
    _builder.append(_varByName_1, "\t");
    _builder.append("<=maxTime && stepCount<maxSteps) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("boolean modified=false;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("do {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("modified = false;");
    _builder.newLine();
    {
      Set<String> _keySet = processes.keySet();
      for(final CharSequence e_1 : _keySet) {
        _builder.append("\t\t\t");
        _builder.append("if (runProcess_");
        _builder.append(e_1, "\t\t\t");
        _builder.append("())");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("modified=true;");
        _builder.newLine();
      }
    }
    _builder.append("\t\t");
    _builder.append("} while (modified);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("run();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("stepCount++;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("long nextTime=Long.MAX_VALUE;");
    _builder.newLine();
    {
      Set<String> _keySet_1 = processes.keySet();
      for(final CharSequence e_2 : _keySet_1) {
        _builder.append("\t\t");
        _builder.append("if (");
        String _processState_4 = this.cce.processState(model, e_2);
        _builder.append(_processState_4, "\t\t");
        _builder.append(" >= 0 && ");
        String _processState_5 = this.cce.processState(model, e_2);
        _builder.append(_processState_5, "\t\t");
        _builder.append(" != 0x7FFF_FFFF)");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("nextTime=Math.min(nextTime, ");
        String _processTime_3 = this.cce.processTime(model, e_2);
        _builder.append(_processTime_3, "\t\t\t");
        _builder.append(");");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    String _varByName_2 = this.cce.varByName(model, "$time");
    _builder.append(_varByName_2, "\t\t");
    _builder.append("=nextTime;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("if (listener!=null && !listener.nextStep(");
    String _varByName_3 = this.cce.varByName(model, "$time");
    _builder.append(_varByName_3, "\t\t");
    _builder.append(", stepCount))");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public CharSequence runMethod(final ExecutableModel model, final Set<Integer> handled, final Set<Integer> handledNegEdge, final Set<Integer> handledPosEdge) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void run(){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("deltaCycle++;");
    _builder.newLine();
    {
      if (this.cce.hasClock) {
        _builder.append("\t");
        _builder.append("epsCycle=0;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("do {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("regUpdates.clear();");
        _builder.newLine();
      }
    }
    {
      if (this.debug) {
        _builder.append("\t");
        _builder.append("if (listener!=null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("listener.startCycle(deltaCycle, epsCycle, this);");
        _builder.newLine();
      }
    }
    {
      Iterable<Frame> _nonProcessframes = this.cce.getNonProcessframes(this.cce.em);
      for(final Frame f : _nonProcessframes) {
        _builder.append("\t");
        CharSequence _callFrame = this.callFrame(f, handled, handledNegEdge, handledPosEdge);
        _builder.append(_callFrame, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (this.cce.hasClock) {
        _builder.append("\t");
        _builder.append("updateRegs();");
        _builder.newLine();
        {
          if (this.debug) {
            _builder.append("\t");
            _builder.append("if (listener!=null && !regUpdates.isEmpty())");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("listener.copyingRegisterValues(this);");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("epsCycle++;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("} while (!regUpdates.isEmpty() && !disabledRegOutputlogic);");
        _builder.newLine();
      }
    }
    {
      Iterable<VariableInformation> _excludeNull = this.cce.excludeNull(this.cce.em.variables);
      final Function1<VariableInformation, Boolean> _function = new Function1<VariableInformation, Boolean>() {
        public Boolean apply(final VariableInformation it) {
          Boolean _get = JavaCompiler.this.cce.prevMap.get(it.name);
          return Boolean.valueOf((_get != null));
        }
      };
      Iterable<VariableInformation> _filter = IterableExtensions.<VariableInformation>filter(_excludeNull, _function);
      for(final VariableInformation v : _filter) {
        _builder.append("\t");
        String _copyPrev = this.copyPrev(v);
        _builder.append(_copyPrev, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      if (this.debug) {
        _builder.append("\t");
        _builder.append("if (listener!=null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("listener.doneCycle(deltaCycle, this);");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    {
      if (this.cce.hasClock) {
        CharSequence _copyRegs = this.copyRegs();
        _builder.append(_copyRegs, "");
        _builder.newLineIfNotEmpty();
      }
    }
    CharSequence _hdlInterpreter = this.hdlInterpreter();
    _builder.append(_hdlInterpreter, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence callFrame(final Frame f, final Set<Integer> handled, final Set<Integer> handledNegEdge, final Set<Integer> handledPosEdge) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _and = false;
      boolean _and_1 = false;
      if (!((f.edgeNegDepRes == (-1)) && (f.edgePosDepRes == (-1)))) {
        _and_1 = false;
      } else {
        int _length = f.predNegDepRes.length;
        boolean _equals = (_length == 0);
        _and_1 = _equals;
      }
      if (!_and_1) {
        _and = false;
      } else {
        int _length_1 = f.predPosDepRes.length;
        boolean _equals_1 = (_length_1 == 0);
        _and = _equals_1;
      }
      if (_and) {
        _builder.newLineIfNotEmpty();
        CharSequence _frameName = this.cce.getFrameName(f);
        _builder.append(_frameName, "");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
      } else {
        CharSequence _createNegEdge = this.createNegEdge(f.edgeNegDepRes, handledNegEdge);
        _builder.append(_createNegEdge, "");
        _builder.newLineIfNotEmpty();
        CharSequence _createPosEdge = this.createPosEdge(f.edgePosDepRes, handledPosEdge);
        _builder.append(_createPosEdge, "");
        _builder.newLineIfNotEmpty();
        {
          for(final int p : f.predNegDepRes) {
            CharSequence _createBooleanPred = this.createBooleanPred(p, handled);
            _builder.append(_createBooleanPred, "");
            _builder.newLineIfNotEmpty();
          }
        }
        {
          for(final int p_1 : f.predPosDepRes) {
            CharSequence _createBooleanPred_1 = this.createBooleanPred(p_1, handled);
            _builder.append(_createBooleanPred_1, "");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("if (");
        String _predicateConditions = this.cce.predicateConditions(f);
        _builder.append(_predicateConditions, "");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        CharSequence _frameName_1 = this.cce.getFrameName(f);
        _builder.append(_frameName_1, "\t");
        _builder.append("();");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
      }
    }
    return _builder;
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
      _builder_1.append("boolean p");
      _builder_1.append(id, "");
      _builder_1.append("_fresh=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("long up");
      _builder_1.append(id, "");
      _builder_1.append("=");
      InternalInformation _asInternal_1 = this.cce.asInternal(id);
      String _idName = this.cce.idName(_asInternal_1.info, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_update;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("if ((up");
      _builder_1.append(id, "");
      _builder_1.append(">>>16 != deltaCycle) || ((up");
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
          _builder_1.append(id, "\t \t");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      _builder_1.append("boolean ");
      String _idName = this.cce.idName(internal, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_isRising=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("boolean ");
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
      {
        if (this.debug) {
          _builder_1.append("\t\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t\t");
          _builder_1.append("\t");
          _builder_1.append("listener.skippingNotAnEdge(-1, em.internals[");
          _builder_1.append(id, "\t\t\t");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      {
        if (this.debug) {
          _builder_1.append("\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append("\t");
          _builder_1.append("listener.skippingHandledEdge(-1, em.internals[");
          _builder_1.append(id, "\t\t");
          _builder_1.append("], true, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      _builder_1.append("boolean ");
      String _idName = this.cce.idName(internal, false, false);
      _builder_1.append(_idName, "");
      _builder_1.append("_isFalling=true;");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("boolean ");
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
      {
        if (this.debug) {
          _builder_1.append("\t\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t\t");
          _builder_1.append(" \t");
          _builder_1.append("listener.skippingNotAnEdge(-1, em.internals[");
          _builder_1.append(id, "\t\t \t");
          _builder_1.append("], false, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
      {
        if (this.debug) {
          _builder_1.append("\t");
          _builder_1.append("if (listener!=null)");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append(" \t");
          _builder_1.append("listener.skippingHandledEdge(-1, em.internals[");
          _builder_1.append(id, "\t \t");
          _builder_1.append("], false, null);");
          _builder_1.newLineIfNotEmpty();
        }
      }
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
  
  public CharSequence hdlInterpreter() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(String name, BigInteger value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setInput(getIndex(name), value.longValue(), arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public void setInput(int idx, BigInteger value, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("setInput(idx, value.longValue(), arrayIdx);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
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
    {
      Iterable<VariableInformation> _excludeNull = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v : _excludeNull) {
        {
          int _length = v.dimensions.length;
          boolean _equals = (_length == 0);
          if (_equals) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get = this.cce.varIdx.get(v.name);
            _builder.append(_get, "\t\t");
            _builder.append(": ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            {
              boolean _and = false;
              if (!(v.width != 64)) {
                _and = false;
              } else {
                boolean _isPredicate = this.cce.isPredicate(v);
                boolean _not = (!_isPredicate);
                _and = _not;
              }
              if (_and) {
                _builder.append("value&=");
                CharSequence _asMaskL = this.cce.asMaskL(v.width);
                _builder.append(_asMaskL, "\t\t\t");
                _builder.append(";");
              }
            }
            _builder.newLineIfNotEmpty();
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
            {
              boolean _and_1 = false;
              if (!(v.width != 64)) {
                _and_1 = false;
              } else {
                boolean _isPredicate_2 = this.cce.isPredicate(v);
                boolean _not_1 = (!_isPredicate_2);
                _and_1 = _not_1;
              }
              if (_and_1) {
                _builder.append("value&=");
                CharSequence _asMaskL_1 = this.cce.asMaskL(v.width);
                _builder.append(_asMaskL_1, "\t\t\t");
                _builder.append(";");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            String _idName_1 = this.cce.idName(v, false, false);
            _builder.append(_idName_1, "\t\t\t");
            _builder.append("[");
            StringBuilder _arrayAccessArrIdx = this.cce.arrayAccessArrIdx(v);
            _builder.append(_arrayAccessArrIdx, "\t\t\t");
            _builder.append("]=value;");
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
    _builder.append("return varIdx.get(name);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v_1 : _excludeNull_1) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get_2 = this.cce.varIdx.get(v_1.name);
        _builder.append(_get_2, "\t\t");
        _builder.append(": return \"");
        _builder.append(v_1.name, "\t\t");
        _builder.append("\";");
        _builder.newLineIfNotEmpty();
      }
    }
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
    {
      Iterable<VariableInformation> _excludeNull_2 = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation v_2 : _excludeNull_2) {
        {
          int _length_1 = v_2.dimensions.length;
          boolean _equals_1 = (_length_1 == 0);
          if (_equals_1) {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_3 = this.cce.varIdx.get(v_2.name);
            _builder.append(_get_3, "\t\t");
            _builder.append(": return ");
            String _idName_2 = this.cce.idName(v_2, false, false);
            _builder.append(_idName_2, "\t\t");
            {
              boolean _isPredicate_3 = this.cce.isPredicate(v_2);
              if (_isPredicate_3) {
                _builder.append("?1:0");
              } else {
                if ((v_2.width != 64)) {
                  _builder.append(" & ");
                  CharSequence _asMaskL_2 = this.cce.asMaskL(v_2.width);
                  _builder.append(_asMaskL_2, "\t\t");
                }
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("case ");
            Integer _get_4 = this.cce.varIdx.get(v_2.name);
            _builder.append(_get_4, "\t\t");
            _builder.append(": return ");
            String _idName_3 = this.cce.idName(v_2, false, false);
            _builder.append(_idName_3, "\t\t");
            _builder.append("[");
            StringBuilder _arrayAccessArrIdx_1 = this.cce.arrayAccessArrIdx(v_2);
            _builder.append(_arrayAccessArrIdx_1, "\t\t");
            _builder.append("]");
            {
              boolean _and_2 = false;
              if (!(v_2.width != 64)) {
                _and_2 = false;
              } else {
                boolean _isPredicate_4 = this.cce.isPredicate(v_2);
                boolean _not_2 = (!_isPredicate_4);
                _and_2 = _not_2;
              }
              if (_and_2) {
                _builder.append(" & ");
                CharSequence _asMaskL_3 = this.cce.asMaskL(v_2.width);
                _builder.append(_asMaskL_3, "\t\t");
              }
            }
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
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
    _builder.append("public BigInteger getOutputBig(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return BigInteger.valueOf(getOutputLong(getIndex(name), arrayIdx));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public BigInteger getOutputBig(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return BigInteger.valueOf(getOutputLong(idx, arrayIdx));");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public int getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return deltaCycle;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence copyRegs() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private void updateRegs() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (RegUpdate reg : regUpdates) {");
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
                _builder.append("if (reg.offset!=-1)");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                String _idName_2 = this.cce.idName(v, false, false);
                _builder.append(_idName_2, "\t\t\t\t");
                _builder.append("[reg.offset] = ");
                String _idName_3 = this.cce.idName(v, false, false);
                _builder.append(_idName_3, "\t\t\t\t");
                _builder.append("$reg[reg.offset];");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t\t\t");
                _builder.append("\t");
                _builder.append("Arrays.fill(");
                String _idName_4 = this.cce.idName(v, false, false);
                _builder.append(_idName_4, "\t\t\t\t");
                _builder.append(", ");
                String _idName_5 = this.cce.idName(v, false, false);
                _builder.append(_idName_5, "\t\t\t\t");
                _builder.append("$reg[0]); ");
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
    _builder_1.append("System.arraycopy(");
    String _idName_2 = this.cce.idName(info, false, false);
    _builder_1.append(_idName_2, "");
    _builder_1.append(",0,");
    String _idName_3 = this.cce.idName(info, true, false);
    _builder_1.append(_idName_3, "");
    _builder_1.append(", 0, ");
    String _idName_4 = this.cce.idName(info, false, 
      false);
    _builder_1.append(_idName_4, "");
    _builder_1.append(".length);");
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
            String _javaType = this.getJavaType(info);
            _builder_2.append(_javaType, "");
            _builder_2.append(" ");
            _builder_2.append(varName, "");
            _builder_2.append("=");
            String _idName = this.cce.idName(info.info, prev, false);
            _builder_2.append(_idName, "");
            _builder_2.append(sb, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              String _javaType_1 = this.getJavaType(info);
              _builder_2.append(_javaType_1, "");
              _builder_2.append(" ");
              _builder_2.append(varName, "");
              _builder_2.append("=(");
              String _idName_1 = this.cce.idName(info.info, prev, false);
              _builder_2.append(_idName_1, "");
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
              String _idName_2 = this.cce.idName(info.info, prev, false);
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
        {
          int _length_1 = info.arrayIdx.length;
          int _length_2 = info.info.dimensions.length;
          boolean _equals = (_length_1 == _length_2);
          if (_equals) {
            {
              if (this.debug) {
                _builder_2.append("if (listener!=null)");
                _builder_2.newLine();
                _builder_2.append("\t");
                _builder_2.append("listener.loadingInternal(");
                _builder_2.append(frameID, "\t");
                _builder_2.append(", em.internals[");
                Integer _get = this.cce.intIdx.get(info.fullName);
                _builder_2.append(_get, "\t");
                _builder_2.append("], ");
                {
                  if (info.isPred) {
                    _builder_2.append(varName, "\t");
                    _builder_2.append("?BigInteger.ONE:BigInteger.ZERO");
                  } else {
                    _builder_2.append("BigInteger.valueOf(");
                    _builder_2.append(varName, "\t");
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
          if ((info.actualWidth == info.info.width)) {
            String _javaType_3 = this.getJavaType(info);
            _builder_3.append(_javaType_3, "");
            _builder_3.append(" ");
            _builder_3.append(varName, "");
            _builder_3.append("= ");
            String _idName_3 = this.cce.idName(info.info, prev, false);
            _builder_3.append(_idName_3, "");
            _builder_3.append(arrAcc, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            if ((info.actualWidth == 1)) {
              String _javaType_4 = this.getJavaType(info);
              _builder_3.append(_javaType_4, "");
              _builder_3.append(" ");
              _builder_3.append(varName, "");
              _builder_3.append("= (");
              String _idName_4 = this.cce.idName(info.info, prev, false);
              _builder_3.append(_idName_4, "");
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
              String _idName_5 = this.cce.idName(info.info, prev, false);
              _builder_3.append(_idName_5, "");
              _builder_3.append(arrAcc, "");
              _builder_3.append(" >> ");
              _builder_3.append(info.bitEnd, "");
              _builder_3.append(") & ");
              CharSequence _asMaskL = this.cce.asMaskL(info.actualWidth);
              _builder_3.append(_asMaskL, "");
              _builder_3.append(";");
              _builder_3.newLineIfNotEmpty();
            }
          }
        }
        {
          int _length_3 = info.arrayIdx.length;
          int _length_4 = info.info.dimensions.length;
          boolean _equals_1 = (_length_3 == _length_4);
          if (_equals_1) {
            {
              if (this.debug) {
                _builder_3.append("if (listener!=null)");
                _builder_3.newLine();
                _builder_3.append("\t");
                _builder_3.append("listener.loadingInternal(");
                _builder_3.append(frameID, "\t");
                _builder_3.append(", em.internals[");
                Integer _get_1 = this.cce.intIdx.get(info.fullName);
                _builder_3.append(_get_1, "\t");
                _builder_3.append("], ");
                {
                  if (info.isPred) {
                    _builder_3.append(varName, "\t");
                    _builder_3.append("?BigInteger.ONE:BigInteger.ZERO");
                  } else {
                    _builder_3.append("BigInteger.valueOf(");
                    _builder_3.append(varName, "\t");
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
      _xblockexpression = _xifexpression;
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
      final StringBuilder varAccess = this.cce.arrayAccess(info.info);
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
                String _javaType = this.getJavaType(info.info);
                _builder_2.append(_javaType, "");
                _builder_2.append(" current=");
                String _idName = this.cce.idName(info.info, false, false);
                _builder_2.append(_idName, "");
                _builder_2.append(fixedAccess, "");
                _builder_2.append(";");
              }
            }
            _builder_2.newLineIfNotEmpty();
            String _idName_1 = this.cce.idName(info.info, false, false);
            _builder_2.append(_idName_1, "");
            _builder_2.append(fixedAccess, "");
            _builder_2.append("=");
            _builder_2.append(value, "");
            _builder_2.append(";");
            _builder_2.newLineIfNotEmpty();
          } else {
            String _javaType_1 = this.getJavaType(info.info);
            _builder_2.append(_javaType_1, "");
            _builder_2.append(" current=");
            String _idName_2 = this.cce.idName(info.info, false, false);
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
            String _idName_3 = this.cce.idName(info.info, false, false);
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
            _builder_2.append("regUpdates.add(new RegUpdate(");
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
            String _idName_4 = this.cce.idName(info.info, false, false);
            _builder_2.append(_idName_4, "");
            _builder_2.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
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
          if ((info.actualWidth == info.info.width)) {
            {
              if (info.isShadowReg) {
                String _javaType_2 = this.getJavaType(info.info);
                _builder_3.append(_javaType_2, "");
                _builder_3.append(" current=");
                String _idName_5 = this.cce.idName(info.info, false, false);
                _builder_3.append(_idName_5, "");
                _builder_3.append(regSuffix, "");
                _builder_3.append("[offset];");
              }
            }
            _builder_3.newLineIfNotEmpty();
            String _idName_6 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_6, "");
            _builder_3.append(regSuffix, "");
            _builder_3.append("[offset]=");
            _builder_3.append(value, "");
            _builder_3.append(";");
            _builder_3.newLineIfNotEmpty();
          } else {
            String _javaType_3 = this.getJavaType(info.info);
            _builder_3.append(_javaType_3, "");
            _builder_3.append(" current=");
            String _idName_7 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_7, "");
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
            String _idName_8 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_8, "");
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
            _builder_3.append("regUpdates.add(new RegUpdate(");
            Integer _get_1 = this.cce.varIdx.get(info.info.name);
            _builder_3.append(_get_1, "\t");
            _builder_3.append(", offset));");
            _builder_3.newLineIfNotEmpty();
          }
        }
        {
          if (info.isPred) {
            String _idName_9 = this.cce.idName(info.info, false, false);
            _builder_3.append(_idName_9, "");
            _builder_3.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
          }
        }
        _builder_3.newLineIfNotEmpty();
        _xifexpression_1 = _builder_3;
      }
      _xblockexpression = _xifexpression_1;
    }
    return _xblockexpression;
  }
  
  public String method(final Frame frame) {
    final StringBuilder sb = new StringBuilder();
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private final void ");
    CharSequence _frameName = this.cce.getFrameName(frame);
    _builder.append(_frameName, "");
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
        _builder.append(frame.uniqueID, "\t\t");
        _builder.append(", deltaCycle, epsCycle, null);");
        _builder.newLineIfNotEmpty();
      }
    }
    sb.append(_builder);
    int pos = 0;
    int arrPos = 0;
    final Stack<Integer> stack = new Stack<Integer>();
    final List<Integer> arr = new LinkedList<Integer>();
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
    {
      if (this.debug) {
        _builder_2.append("if (listener!=null)");
        _builder_2.newLine();
        _builder_2.append("\t");
        _builder_2.append("listener.writingResult(");
        _builder_2.append(frame.uniqueID, "\t");
        _builder_2.append(", em.internals[");
        _builder_2.append(frame.outputId, "\t");
        _builder_2.append("], BigInteger.valueOf(");
        _builder_2.append(last, "\t");
        {
          InternalInformation _asInternal_2 = this.cce.asInternal(frame.outputId);
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
            int _size = arr.size();
            int _length = internal.info.dimensions.length;
            final boolean isDynMem = (_size < _length);
            if (isDynMem) {
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("Arrays.fill(");
              String _idName = this.cce.idName(internal, false, false);
              _builder_1.append(_idName, "");
              _builder_1.append(", t");
              _builder_1.append(a, "");
              _builder_1.append(");");
              sb.append(_builder_1);
            } else {
              StringConcatenation _builder_2 = new StringConcatenation();
              String _idName_1 = this.cce.idName(internal, false, false);
              _builder_2.append(_idName_1, "");
              {
                int _length_1 = internal.info.dimensions.length;
                boolean _greaterThan = (_length_1 > 0);
                if (_greaterThan) {
                  _builder_2.append("[");
                  StringBuilder _arrayAccess = this.cce.arrayAccess(internal.info, arr);
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
              _builder_3.append("regUpdates.add(new RegUpdate(");
              Integer _get = this.cce.varIdx.get(internal.info.name);
              _builder_3.append(_get, "");
              _builder_3.append(", ");
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
                  _builder_3.append(_arrayAccess_1, "");
                } else {
                  _builder_3.append("-1");
                }
              }
              _builder_3.append("));");
              sb.append(_builder_3);
            }
            arr.clear();
            break;
          case noop:
            sb.append("//Do nothing");
            break;
          case bitAccessSingle:
            StringConcatenation _builder_4 = new StringConcatenation();
            _builder_4.append("long t");
            _builder_4.append(pos, "");
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
            _builder_5.append("long t");
            _builder_5.append(pos, "");
            _builder_5.append("=(t");
            _builder_5.append(a, "");
            _builder_5.append(" >> ");
            _builder_5.append(lowBit, "");
            _builder_5.append(") & ");
            CharSequence _hexStringL = this.cce.toHexStringL(mask);
            _builder_5.append(_hexStringL, "");
            _builder_5.append(";");
            sb.append(_builder_5);
            break;
          case cast_int:
            int _min = Math.min(inst.arg1, inst.arg2);
            final int shiftWidth = (64 - _min);
            StringConcatenation _builder_6 = new StringConcatenation();
            _builder_6.append("long t");
            _builder_6.append(pos, "");
            _builder_6.append("=");
            StringConcatenation _builder_7 = new StringConcatenation();
            _builder_7.append("t");
            _builder_7.append(a, "");
            CharSequence _signExtend = this.cce.signExtend(_builder_7, null, shiftWidth);
            _builder_6.append(_signExtend, "");
            _builder_6.append(";");
            sb.append(_builder_6);
            break;
          case cast_uint:
            if ((inst.arg1 != 64)) {
              StringConcatenation _builder_8 = new StringConcatenation();
              _builder_8.append("long t");
              _builder_8.append(pos, "");
              _builder_8.append("=t");
              _builder_8.append(a, "");
              _builder_8.append(" & ");
              CharSequence _asMaskL = this.cce.asMaskL(inst.arg1);
              _builder_8.append(_asMaskL, "");
              _builder_8.append(";");
              sb.append(_builder_8);
            } else {
              StringConcatenation _builder_9 = new StringConcatenation();
              _builder_9.append("long t");
              _builder_9.append(pos, "");
              _builder_9.append("=t");
              _builder_9.append(a, "");
              _builder_9.append(";");
              sb.append(_builder_9);
            }
            break;
          case logiNeg:
            StringConcatenation _builder_10 = new StringConcatenation();
            _builder_10.append("boolean t");
            _builder_10.append(pos, "");
            _builder_10.append("=!t");
            _builder_10.append(a, "");
            _builder_10.append(";");
            sb.append(_builder_10);
            break;
          case logiAnd:
            StringConcatenation _builder_11 = new StringConcatenation();
            _builder_11.append("boolean t");
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
            _builder_12.append("boolean t");
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
            _builder_13.append("long t");
            _builder_13.append(pos, "");
            _builder_13.append("=0;");
            sb.append(_builder_13);
            break;
          case const1:
            StringConcatenation _builder_14 = new StringConcatenation();
            _builder_14.append("long t");
            _builder_14.append(pos, "");
            _builder_14.append("=1;");
            sb.append(_builder_14);
            break;
          case const2:
            StringConcatenation _builder_15 = new StringConcatenation();
            _builder_15.append("long t");
            _builder_15.append(pos, "");
            _builder_15.append("=2;");
            sb.append(_builder_15);
            break;
          case constAll1:
            StringConcatenation _builder_16 = new StringConcatenation();
            _builder_16.append("long t");
            _builder_16.append(pos, "");
            _builder_16.append("=");
            CharSequence _asMaskL_1 = this.cce.asMaskL(inst.arg1);
            _builder_16.append(_asMaskL_1, "");
            _builder_16.append(";");
            sb.append(_builder_16);
            break;
          case concat:
            StringConcatenation _builder_17 = new StringConcatenation();
            _builder_17.append("long t");
            _builder_17.append(pos, "");
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
            StringConcatenation _builder_18 = new StringConcatenation();
            _builder_18.append("long t");
            _builder_18.append(pos, "");
            _builder_18.append("=");
            CharSequence _constantL = this.cce.constantL(inst.arg1, f);
            _builder_18.append(_constantL, "");
            _builder_18.append(";");
            sb.append(_builder_18);
            break;
          case loadInternal:
            final InternalInformation internal_1 = this.cce.asInternal(inst.arg1);
            CharSequence _ter = this.getter(internal_1, false, pos, f.uniqueID);
            sb.append(_ter);
            arr.clear();
            break;
          case arith_neg:
            StringConcatenation _builder_19 = new StringConcatenation();
            _builder_19.append("long t");
            _builder_19.append(pos, "");
            _builder_19.append("=");
            CharSequence _singleOpValue = this.cce.singleOpValue("-", null, a, inst.arg1);
            _builder_19.append(_singleOpValue, "");
            _builder_19.append(";");
            sb.append(_builder_19);
            break;
          case bit_neg:
            StringConcatenation _builder_20 = new StringConcatenation();
            _builder_20.append("long t");
            _builder_20.append(pos, "");
            _builder_20.append("=");
            CharSequence _singleOpValue_1 = this.cce.singleOpValue("~", null, a, inst.arg1);
            _builder_20.append(_singleOpValue_1, "");
            _builder_20.append(";");
            sb.append(_builder_20);
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
          case mod:
            String _twoOp_6 = this.twoOp("%", inst.arg1, pos, a, b);
            sb.append(_twoOp_6);
            break;
          case pow:
            StringConcatenation _builder_21 = new StringConcatenation();
            _builder_21.append("long t");
            _builder_21.append(pos, "");
            _builder_21.append("=1 << (t");
            _builder_21.append(a, "");
            _builder_21.append("-1);");
            sb.append(_builder_21);
            break;
          case div:
            String _twoOp_7 = this.twoOp("/", inst.arg1, pos, a, b);
            sb.append(_twoOp_7);
            break;
          case sll:
            String _twoOp_8 = this.twoOp("<<", inst.arg1, pos, a, b);
            sb.append(_twoOp_8);
            break;
          case srl:
            String _twoOp_9 = this.twoOp(">>>", inst.arg1, pos, a, b);
            sb.append(_twoOp_9);
            break;
          case sra:
            String _twoOp_10 = this.twoOp(">>", inst.arg1, pos, a, b);
            sb.append(_twoOp_10);
            break;
          case eq:
            StringConcatenation _builder_22 = new StringConcatenation();
            _builder_22.append("boolean t");
            _builder_22.append(pos, "");
            _builder_22.append("=t");
            _builder_22.append(b, "");
            _builder_22.append(" == t");
            _builder_22.append(a, "");
            _builder_22.append(";");
            sb.append(_builder_22);
            break;
          case not_eq:
            StringConcatenation _builder_23 = new StringConcatenation();
            _builder_23.append("boolean t");
            _builder_23.append(pos, "");
            _builder_23.append("=t");
            _builder_23.append(b, "");
            _builder_23.append(" != t");
            _builder_23.append(a, "");
            _builder_23.append(";");
            sb.append(_builder_23);
            break;
          case less:
            StringConcatenation _builder_24 = new StringConcatenation();
            _builder_24.append("boolean t");
            _builder_24.append(pos, "");
            _builder_24.append("=t");
            _builder_24.append(b, "");
            _builder_24.append(" < t");
            _builder_24.append(a, "");
            _builder_24.append(";");
            sb.append(_builder_24);
            break;
          case less_eq:
            StringConcatenation _builder_25 = new StringConcatenation();
            _builder_25.append("boolean t");
            _builder_25.append(pos, "");
            _builder_25.append("=t");
            _builder_25.append(b, "");
            _builder_25.append(" <= t");
            _builder_25.append(a, "");
            _builder_25.append(";");
            sb.append(_builder_25);
            break;
          case greater:
            StringConcatenation _builder_26 = new StringConcatenation();
            _builder_26.append("boolean t");
            _builder_26.append(pos, "");
            _builder_26.append("=t");
            _builder_26.append(b, "");
            _builder_26.append(" > t");
            _builder_26.append(a, "");
            _builder_26.append(";");
            sb.append(_builder_26);
            break;
          case greater_eq:
            StringConcatenation _builder_27 = new StringConcatenation();
            _builder_27.append("boolean t");
            _builder_27.append(pos, "");
            _builder_27.append("=t");
            _builder_27.append(b, "");
            _builder_27.append(" >= t");
            _builder_27.append(a, "");
            _builder_27.append(";");
            sb.append(_builder_27);
            break;
          case isRisingEdge:
            StringConcatenation _builder_28 = new StringConcatenation();
            InternalInformation _asInternal = this.cce.asInternal(inst.arg1);
            String _idName_2 = this.cce.idName(_asInternal.info, false, false);
            _builder_28.append(_idName_2, "");
            _builder_28.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
            sb.append(_builder_28);
            break;
          case isFallingEdge:
            StringConcatenation _builder_29 = new StringConcatenation();
            InternalInformation _asInternal_1 = this.cce.asInternal(inst.arg1);
            String _idName_3 = this.cce.idName(_asInternal_1.info, false, false);
            _builder_29.append(_idName_3, "");
            _builder_29.append("_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);");
            sb.append(_builder_29);
            break;
          default:
            break;
        }
      }
      StringConcatenation _builder_30 = new StringConcatenation();
      _builder_30.append("//");
      _builder_30.append(inst, "");
      _builder_30.newLineIfNotEmpty();
      _xblockexpression = sb.append(_builder_30);
    }
    return _xblockexpression;
  }
  
  public String twoOp(final String op, final int targetSizeWithType, final int pos, final int a, final int b) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("long t");
    _builder.append(pos, "");
    _builder.append("=");
    CharSequence _twoOpValue = this.cce.twoOpValue(op, null, a, b, targetSizeWithType);
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
      String _javaType = this.getJavaType(info);
      _builder_1.append(_javaType, "");
      _builder_1.append("[");
      _builder_1.append(size, "");
      _builder_1.append("];");
      _builder_1.newLineIfNotEmpty();
      {
        if (info.isRegister) {
          String _idName_1 = this.cce.idName(info, false, false);
          _builder_1.append(_idName_1, "");
          _builder_1.append("$reg=new ");
          String _javaType_1 = this.getJavaType(info);
          _builder_1.append(_javaType_1, "");
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
    boolean _or = false;
    boolean _startsWith = information.name.startsWith(InternalInformation.PRED_PREFIX);
    if (_startsWith) {
      _or = true;
    } else {
      boolean _tripleEquals = (information.type == VariableInformation.Type.BOOL);
      _or = _tripleEquals;
    }
    if (_or) {
      return "boolean";
    }
    return "long";
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
        _builder.append("public long ");
        String _idName = this.cce.idName(info, false, 
          false);
        _builder.append(_idName, "");
        _builder.append("_update=0;");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("public ");
    String _javaType = this.getJavaType(info);
    _builder.append(_javaType, "");
    {
      for(final int d : info.dimensions) {
        _builder.append("[]");
      }
    }
    _builder.append(" ");
    String _idName_1 = this.cce.idName(info, false, false);
    _builder.append(_idName_1, "");
    _builder.append(";");
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
        _builder.append("private ");
        String _javaType_1 = this.getJavaType(info);
        _builder.append(_javaType_1, "");
        {
          for(final int d_1 : info.dimensions) {
            _builder.append("[]");
          }
        }
        _builder.append(" ");
        String _idName_2 = this.cce.idName(info, true, 
          false);
        _builder.append(_idName_2, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    {
      if (info.isRegister) {
        _builder.append("private ");
        String _javaType_2 = this.getJavaType(info);
        _builder.append(_javaType_2, "");
        {
          for(final int d_2 : info.dimensions) {
            _builder.append("[]");
          }
        }
        _builder.append(" ");
        String _idName_3 = this.cce.idName(info, false, false);
        _builder.append(_idName_3, "");
        _builder.append("$reg;");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence getImports() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("import java.util.*;");
    _builder.newLine();
    _builder.append("import java.math.*;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.*;");
    _builder.newLine();
    {
      if (this.debug) {
        _builder.append("import org.pshdl.interpreter.frames.*;");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public String getHookName() {
    return "Java";
  }
  
  public IOutputProvider.MultiOption getUsage() {
    final Options options = new Options();
    options.addOption("p", "pkg", true, 
      "The package the generated source will use. If non is specified the package from the module is used");
    options.addOption("d", "debug", false, 
      "If debug is specified, the source will contain support for a IDebugListener");
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
      boolean debug = cli.hasOption("debug");
      boolean coverage = cli.hasOption("coverage");
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
      _xblockexpression = JavaCompiler.doCompile(syntaxProblems, em, pkg, unitName, debug);
    }
    return _xblockexpression;
  }
  
  public CharSequence createChangeAdapter(final String packageName, final String unitName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (packageName != null);
      if (_tripleNotEquals) {
        _builder.append("package ");
        _builder.append(packageName, "");
        _builder.append(";");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.IChangeListener;");
    _builder.newLine();
    _builder.append("import org.pshdl.interpreter.IHDLInterpreter;");
    _builder.newLine();
    _builder.append("import java.math.BigInteger;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class ChangeAdapter");
    _builder.append(unitName, "");
    _builder.append(" implements IHDLInterpreter{");
    _builder.newLineIfNotEmpty();
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
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private ");
    _builder.append(unitName, "\t");
    _builder.append(" module;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private IChangeListener[] listeners;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ChangeAdapter");
    _builder.append(unitName, "\t");
    _builder.append("(");
    _builder.append(unitName, "\t");
    _builder.append(" module, IChangeListener ... listeners) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this.module=module;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.listeners=listeners;");
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
    _builder.append("public void run() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("module.run();");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull_1 = this.cce.excludeNull(this.cce.em.variables);
      for(final VariableInformation varInfo : _excludeNull_1) {
        _builder.append("\t\t");
        String _changedNotification = this.changedNotification(varInfo);
        _builder.append(_changedNotification, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        String _idName = this.cce.idName(varInfo, false, false);
        _builder.append(_idName, "\t\t");
        _builder.append("=module.");
        String _idName_1 = this.cce.idName(varInfo, false, false);
        _builder.append(_idName_1, "\t\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
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
    _builder.append("public void setInput(String name, BigInteger value, int... arrayIdx) {");
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
    _builder.append("public void setInput(int idx, BigInteger value, int... arrayIdx) {");
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
    _builder.append("public BigInteger getOutputBig(String name, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputBig(name, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public BigInteger getOutputBig(int idx, int... arrayIdx) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getOutputBig(idx, arrayIdx);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public int getDeltaCycle() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return module.getDeltaCycle();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String changedNotification(final VariableInformation vi) {
    final String varName = this.cce.idName(vi, false, false);
    boolean _isArray = this.cce.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.cce.isPredicate(vi);
      if (_isPredicate) {
        String _idName = this.cce.idName(vi, false, false);
        final String varNameUpdate = (_idName + "_update");
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
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\", ");
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
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), \"");
        _builder_1.append(vi.name, "\t\t");
        _builder_1.append("\", ");
        _builder_1.append(varName, "\t\t");
        _builder_1.append(" ");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            CharSequence _asMaskL = this.cce.asMaskL(vi.width);
            _builder_1.append(_asMaskL, "\t\t");
          }
        }
        _builder_1.append(", module.");
        _builder_1.append(varName, "\t\t");
        _builder_1.append(" ");
        {
          if ((vi.width != 64)) {
            _builder_1.append(" & ");
            CharSequence _asMaskL_1 = this.cce.asMaskL(vi.width);
            _builder_1.append(_asMaskL_1, "\t\t");
          }
        }
        _builder_1.append(");");
        _builder_1.newLineIfNotEmpty();
        return _builder_1.toString();
      }
    } else {
      boolean _isPredicate_1 = this.cce.isPredicate(vi);
      if (_isPredicate_1) {
        String _idName_1 = this.cce.idName(vi, false, false);
        final String varNameUpdate_1 = (_idName_1 + "_update");
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
        _builder_2.append("listener.valueChangedPredicateArray(getDeltaCycle(), \"");
        _builder_2.append(vi.name, "\t\t");
        _builder_2.append("\", ");
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
        _builder_3.append("listener.valueChangedLongArray(getDeltaCycle(), \"");
        _builder_3.append(vi.name, "\t\t");
        _builder_3.append("\", ");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(", module.");
        _builder_3.append(varName, "\t\t");
        _builder_3.append(");");
        _builder_3.newLineIfNotEmpty();
        return _builder_3.toString();
      }
    }
  }
}
