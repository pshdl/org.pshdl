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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.model.simulation.CommonCodeGenerator;
import org.pshdl.model.simulation.HDLSimulator;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.utils.PSAbstractCompiler;
import org.pshdl.model.utils.services.AuxiliaryContent;
import org.pshdl.model.utils.services.IOutputProvider;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class JavaCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
  private String packageName;
  
  private String unitName;
  
  public JavaCodeGenerator() {
  }
  
  public JavaCodeGenerator(final ExecutableModel em, final String packageName, final String unitName, final int maxCosts) {
    super(em, 64, maxCosts);
    this.packageName = packageName;
    this.unitName = unitName;
  }
  
  protected void postBody() {
    this.indent--;
  }
  
  protected void preBody() {
    this.indent++;
  }
  
  protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
    boolean _or = false;
    boolean _isArray = this.isArray(varInfo);
    boolean _not = (!_isArray);
    if (_not) {
      _or = true;
    } else {
      boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.baseType);
      _or = _contains;
    }
    if (_or) {
      boolean _isBoolean = this.isBoolean(varInfo, attributes);
      if (_isBoolean) {
        return "boolean";
      }
      return "long";
    } else {
      boolean _isBoolean_1 = this.isBoolean(varInfo, attributes);
      if (_isBoolean_1) {
        return "boolean[]";
      }
      return "long[]";
    }
  }
  
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
  
  protected CharSequence footer() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    CharSequence _hdlInterpreter = this.hdlInterpreter();
    _builder.append(_hdlInterpreter, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    return _builder;
  }
  
  protected CharSequence hdlInterpreter() {
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
    CharSequence _setInputCases = this.setInputCases("value", null);
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
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("public String getName(int idx) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("switch (idx) {");
    _builder.newLine();
    {
      Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
      for(final VariableInformation v : _excludeNull) {
        _builder.append("\t\t");
        _builder.append("case ");
        Integer _get = this.varIdx.get(v.name);
        _builder.append(_get, "\t\t");
        _builder.append(": return \"");
        _builder.append(v.name, "\t\t");
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
    _builder.append("\t\t");
    CharSequence _outputCases = this.getOutputCases(null);
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
    _builder.append("private long pow(long a, long n) {");
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
    return _builder;
  }
  
  protected CharSequence header() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (this.packageName != null);
      if (_tripleNotEquals) {
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
    _builder.append("public class ");
    _builder.append(this.unitName, "");
    _builder.newLineIfNotEmpty();
    _builder.append(" ");
    _builder.append("implements ");
    {
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
      if (_and) {
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
          Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
          for(final VariableInformation v : _excludeNull) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v.name, "\t");
            _builder.append("\", ");
            Integer _get = this.varIdx.get(v.name);
            _builder.append(_get, "\t");
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
          Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
          for(final VariableInformation v_1 : _excludeNull_1) {
            _builder.append("\t");
            _builder.append("varIdx.put(\"");
            _builder.append(v_1.name, "\t");
            _builder.append("\", ");
            Integer _get_1 = this.varIdx.get(v_1.name);
            _builder.append(_get_1, "\t");
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
    _builder.append("import java.util.concurrent.*;");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence calculateVariableAccessIndex(final List<Integer> arr, final VariableInformation varInfo) {
    final CharSequence res = super.calculateVariableAccessIndex(arr, varInfo);
    int _length = res.length();
    boolean _tripleEquals = (_length == 0);
    if (_tripleEquals) {
      return res;
    }
    return (("(int)(" + res) + ")");
  }
  
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
  
  protected CharSequence functionFooter(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence functionHeader(final Frame frame) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("private final void ");
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
    _builder.append("regUpdates.add(new RegUpdate(");
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
  
  protected CharSequence runMethodsFooter(final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence callStage(final int stage, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
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
  
  protected CharSequence barrierBegin(final int stage, final int totalStageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("List<Callable<Void>> calls=new ArrayList<Callable<Void>>();");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _indent = this.indent();
    _builder.append(_indent, "\t");
    _builder.append("calls.add(new Callable<Void>() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "\t");
    _builder.append("@Override public Void call() {");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    int _indent_2 = this.indent;
    indent = (_indent_2 + 2);
    return res;
  }
  
  protected CharSequence barrierEnd(final int stage, final int totalStageCosts, final boolean constant) {
    int _indent = this.indent;
    indent = (_indent - 2);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("return null;");
    _builder.newLine();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("});");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3, "");
    _builder.append("try {");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_4 = this.indent();
    _builder.append(_indent_4, "");
    _builder.append("\tmainPool.invokeAll(calls);");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_5 = this.indent();
    _builder.append(_indent_5, "");
    _builder.append("} catch (final InterruptedException e) {");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_6 = this.indent();
    _builder.append(_indent_6, "");
    _builder.append("\tnew RuntimeException(e);");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_7 = this.indent();
    _builder.append(_indent_7, "");
    _builder.append("}");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    return res;
  }
  
  protected CharSequence barrier() {
    int _indent = this.indent;
    indent = (_indent - 2);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("return null;");
    _builder.newLine();
    CharSequence _indent_1 = this.indent();
    _builder.append(_indent_1, "");
    _builder.append("\t}");
    _builder.newLineIfNotEmpty();
    CharSequence _indent_2 = this.indent();
    _builder.append(_indent_2, "");
    _builder.append("});");
    _builder.newLineIfNotEmpty();
    _builder.append("calls.add(new Callable<Void>() {");
    _builder.newLine();
    CharSequence _indent_3 = this.indent();
    _builder.append(_indent_3, "");
    _builder.append("@Override public Void call() {");
    _builder.newLineIfNotEmpty();
    final String res = _builder.toString();
    int _indent_4 = this.indent;
    indent = (_indent_4 + 2);
    return res;
  }
  
  protected CharSequence stageMethodsHeader(final int stage, final int stageCosts, final boolean constant) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void ");
    CharSequence _stageMethodName = this.stageMethodName(stage, constant);
    _builder.append(_stageMethodName, "");
    _builder.append("(){");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence applyRegUpdates() {
    return "updateRegs();";
  }
  
  protected CharSequence checkRegupdates() {
    return "!regUpdates.isEmpty()";
  }
  
  public CharSequence createChangeAdapter(final boolean useInterface) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _tripleNotEquals = (this.packageName != null);
      if (_tripleNotEquals) {
        _builder.append("package ");
        _builder.append(this.packageName, "");
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
    _builder.append("public class ");
    {
      if (useInterface) {
        _builder.append("Generic");
      }
    }
    _builder.append("ChangeAdapter");
    _builder.append(this.unitName, "");
    _builder.append(" implements IHDLInterpreter{");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _fieldDeclarations = this.fieldDeclarations(false, true);
    _builder.append(_fieldDeclarations, "\t");
    _builder.newLineIfNotEmpty();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
          for(final VariableInformation varInfo : _excludeNull) {
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
    {
      if (useInterface) {
        _builder.append("IHDLInterpreter");
      } else {
        _builder.append(this.unitName, "\t");
      }
    }
    _builder.append(" module;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("private IChangeListener[] listeners;");
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
    {
      if (useInterface) {
        _builder.append("IHDLInterpreter");
      } else {
        _builder.append(this.unitName, "\t");
      }
    }
    _builder.append(" module, IChangeListener ... listeners) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this.module=module;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.listeners=listeners;");
    _builder.newLine();
    {
      if (useInterface) {
        {
          Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
          for(final VariableInformation varInfo_1 : _excludeNull_1) {
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
      Iterable<VariableInformation> _excludeNull_2 = this.excludeNull(this.em.variables);
      for(final VariableInformation varInfo_2 : _excludeNull_2) {
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
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected String changedNotificationInterface(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, true, _of);
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
        _builder.append("listener.valueChangedPredicate(getDeltaCycle(), \"");
        _builder.append(vi.name, "\t\t");
        _builder.append("\", ");
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
        _builder_1.append("listener.valueChangedLong(getDeltaCycle(), \"");
        _builder_1.append(vi.name, "\t\t");
        _builder_1.append("\", ");
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
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, true, _of_1);
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
        _builder_2.append("if (!tempArr.equals(");
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
        _builder_3.append("if (!tempArr.equals(");
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
        _builder_3.append(", tempArr);");
        _builder_3.newLineIfNotEmpty();
        _builder_3.append("}");
        _builder_3.newLine();
        return _builder_3.toString();
      }
    }
  }
  
  protected String changedNotification(final VariableInformation vi) {
    final CharSequence varName = this.idName(vi, true, CommonCodeGenerator.NONE);
    boolean _isArray = this.isArray(vi);
    boolean _not = (!_isArray);
    if (_not) {
      boolean _isPredicate = this.isPredicate(vi);
      if (_isPredicate) {
        EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate = this.idName(vi, true, _of);
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
        EnumSet<CommonCodeGenerator.Attributes> _of_1 = EnumSet.<CommonCodeGenerator.Attributes>of(CommonCodeGenerator.Attributes.isUpdate);
        final CharSequence varNameUpdate_1 = this.idName(vi, true, _of_1);
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
  
  protected CharSequence clearRegUpdates() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("regUpdates.clear();");
    _builder.newLine();
    return _builder;
  }
  
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
      _builder.append("private boolean ");
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
      _builder.append("public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener) {");
      _builder.newLine();
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
  
  public String getHookName() {
    return "Java";
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
      _xblockexpression = JavaCodeGenerator.doCompile(syntaxProblems, em, pkg, unitName);
    }
    return _xblockexpression;
  }
  
  public static ArrayList<PSAbstractCompiler.CompileResult> doCompile(final Set<Problem> syntaxProblems, final ExecutableModel em, final String pkg, final String unitName) {
    final JavaCodeGenerator comp = new JavaCodeGenerator(em, pkg, unitName, Integer.MAX_VALUE);
    final String code = comp.generateMainCode();
    final ArrayList<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent>newArrayList();
    Iterable<AuxiliaryContent> _auxiliaryContent = comp.getAuxiliaryContent();
    Iterables.<AuxiliaryContent>addAll(sideFiles, _auxiliaryContent);
    String _hookName = comp.getHookName();
    PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, sideFiles, em.source, _hookName, true);
    return Lists.<PSAbstractCompiler.CompileResult>newArrayList(_compileResult);
  }
  
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
}
