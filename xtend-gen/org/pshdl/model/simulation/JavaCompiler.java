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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.utils.PSAbstractCompiler.CompileResult;
import org.pshdl.model.utils.services.IOutputProvider.MultiOption;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class JavaCompiler implements ITypeOuptutProvider {
  private boolean debug;
  
  private int cores;
  
  @Extension
  private CommonCompilerExtension cce;
  
  public JavaCompiler() {
  }
  
  public JavaCompiler(final /* ExecutableModel */Object em, final boolean includeDebug, final int cores) {
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em);
    this.cce = _commonCompilerExtension;
    this.debug = includeDebug;
    this.cores = cores;
  }
  
  public static String doCompile(final /* ExecutableModel */Object em, final String packageName, final String unitName, final boolean includeDebugListener, final int cores) {
    JavaCompiler _javaCompiler = new JavaCompiler(em, includeDebugListener, cores);
    CharSequence _compile = _javaCompiler.compile(packageName, unitName);
    return _compile.toString();
  }
  
  public CharSequence compile(final String packageName, final String unitName) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method or field dir is undefined for the type JavaCompiler"
      + "\nVariableInformation$Direction cannot be resolved to a type."
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type JavaCompiler"
      + "\nThe method edgePosDepRes is undefined for the type JavaCompiler"
      + "\nThe method predNegDepRes is undefined for the type JavaCompiler"
      + "\nThe method predPosDepRes is undefined for the type JavaCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type JavaCompiler"
      + "\nThe method edgePosDepRes is undefined for the type JavaCompiler"
      + "\nThe method predNegDepRes is undefined for the type JavaCompiler"
      + "\nThe method predPosDepRes is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nfilter cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nINTERNAL cannot be resolved"
      + "\nsize cannot be resolved"
      + "\n== cannot be resolved"
      + "\ntoFirstUpper cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\ntoFirstUpper cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\ntoFirstUpper cannot be resolved"
      + "\nsize cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\ntoFirstUpper cannot be resolved"
      + "\nsize cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\nframes cannot be resolved"
      + "\nframes cannot be resolved"
      + "\n=== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n=== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n=== cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n=== cannot be resolved"
      + "\ncreateNegEdge cannot be resolved"
      + "\ncreatePosEdge cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nfilter cannot be resolved");
  }
  
  public String predicates(final /* Frame */Object f) {
    throw new Error("Unresolved compilation problems:"
      + "\nedgeNegDepRes cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nedgeNegDepRes cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nedgeNegDepRes cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nedgePosDepRes cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nedgePosDepRes cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nedgePosDepRes cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nidName cannot be resolved"
      + "\npredNegDepRes cannot be resolved"
      + "\npredPosDepRes cannot be resolved");
  }
  
  public CharSequence createBooleanPred(final int id, final Set<Integer> handled) {
    throw new Error("Unresolved compilation problems:"
      + "\ngetter cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence createPosEdge(final int id, final Set<Integer> handledEdges) {
    throw new Error("Unresolved compilation problems:"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence createNegEdge(final int id, final Set<Integer> handledEdges) {
    throw new Error("Unresolved compilation problems:"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence hdlInterpreter() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nThe method width is undefined for the type JavaCompiler"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved"
      + "\n!= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\n!= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved"
      + "\n!= cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\n!= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nasMaskL cannot be resolved");
  }
  
  public CharSequence copyRegs() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method isRegister is undefined for the type JavaCompiler"
      + "\nThe method name is undefined for the type JavaCompiler"
      + "\nThe method dimensions is undefined for the type JavaCompiler"
      + "\nvariables cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved");
  }
  
  public String copyPrev(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence getter(final /* InternalInformation */Object info, final boolean prev, final int pos, final int frameID) {
    throw new Error("Unresolved compilation problems:"
      + "\nactualWidth cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\nfixedArray cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n=== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nfullName cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n=== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nfullName cannot be resolved"
      + "\nisPred cannot be resolved");
  }
  
  public CharSequence setter(final /* InternalInformation */Object info, final String value) {
    throw new Error("Unresolved compilation problems:"
      + "\nactualWidth cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\narrayFixedOffset cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n> cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\nfixedArray cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public String method(final /* Frame */Object frame) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method inst is undefined for the type JavaCompiler"
      + "\nThe method inst is undefined for the type JavaCompiler"
      + "\nThe method inst is undefined for the type JavaCompiler"
      + "\nThe method inst is undefined for the type JavaCompiler"
      + "\nInstruction cannot be resolved to a type."
      + "\nThe method inst is undefined for the type JavaCompiler"
      + "\nInstruction cannot be resolved to a type."
      + "\nframeName cannot be resolved"
      + "\nuniqueID cannot be resolved"
      + "\ninstructions cannot be resolved"
      + "\npop cannot be resolved"
      + "\n> cannot be resolved"
      + "\npop cannot be resolved"
      + "\n> cannot be resolved"
      + "\npush cannot be resolved"
      + "\n> cannot be resolved"
      + "\n=== cannot be resolved"
      + "\npushAddIndex cannot be resolved"
      + "\n!== cannot be resolved"
      + "\npushAddIndex cannot be resolved"
      + "\noutputId cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\n!= cannot be resolved"
      + "\noutputId cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nsetter cannot be resolved"
      + "\nuniqueID cannot be resolved"
      + "\noutputId cannot be resolved"
      + "\noutputId cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nisPred cannot be resolved");
  }
  
  public StringBuilder toExpression(final /* FastInstruction */Object inst, final /* Frame */Object f, final StringBuilder sb, final int pos, final int a, final int b, final List<Integer> arr, final int arrPos) {
    throw new Error("Unresolved compilation problems:"
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\nInstruction cannot be resolved to a type."
      + "\ninst cannot be resolved"
      + "\npushAddIndex cannot be resolved"
      + "\nwriteInternal cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n> cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narray cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\nnoop cannot be resolved"
      + "\nbitAccessSingle cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nbitAccessSingleRange cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\n- cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ncast_int cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\ncast_uint cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\n!= cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\nlogiNeg cannot be resolved"
      + "\nlogiAnd cannot be resolved"
      + "\nlogiOr cannot be resolved"
      + "\nconst0 cannot be resolved"
      + "\nconst1 cannot be resolved"
      + "\nconst2 cannot be resolved"
      + "\nconstAll1 cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasMaskL cannot be resolved"
      + "\nconcat cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\nloadConstant cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nconstantL cannot be resolved"
      + "\nloadInternal cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nuniqueID cannot be resolved"
      + "\narith_neg cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nbit_neg cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nand cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nor cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nxor cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nplus cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nminus cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nmul cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\ndiv cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nsll cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nsrl cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nsra cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\neq cannot be resolved"
      + "\nnot_eq cannot be resolved"
      + "\nless cannot be resolved"
      + "\nless_eq cannot be resolved"
      + "\ngreater cannot be resolved"
      + "\ngreater_eq cannot be resolved"
      + "\nisRisingEdge cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nisFallingEdge cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved");
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
  
  public CharSequence init(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nidName cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\nisRegister cannot be resolved"
      + "\nidName cannot be resolved"
      + "\njavaType cannot be resolved");
  }
  
  public Object getJavaType(final /* InternalInformation */Object ii) {
    throw new Error("Unresolved compilation problems:"
      + "\ninfo cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n!= cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  public String getJavaType(final /* VariableInformation */Object information) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nname cannot be resolved"
      + "\nstartsWith cannot be resolved"
      + "\nPRED_PREFIX cannot be resolved");
  }
  
  public CharSequence decl(final /* VariableInformation */Object info, final Boolean includePrev) {
    throw new Error("Unresolved compilation problems:"
      + "\nisPredicate cannot be resolved"
      + "\n|| cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nidName cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nidName cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nisRegister cannot be resolved"
      + "\njavaType cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nidName cannot be resolved");
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
  
  public MultiOption getUsage() {
    Options _options = new Options();
    final Options options = _options;
    options.addOption("p", "pkg", true, "The package the generated source will use. If non is specified the package from the module is used");
    options.addOption("d", "debug", false, "If debug is specified, the source will contain support for a IDebugListener");
    String _hookName = this.getHookName();
    String _plus = ("Options for the " + _hookName);
    String _plus_1 = (_plus + " type:");
    MultiOption _multiOption = new MultiOption(_plus_1, null, options);
    return _multiOption;
  }
  
  public ArrayList<CompileResult> invoke(final CommandLine cli, final /* ExecutableModel */Object em, final Set<Problem> syntaxProblems) throws Exception {
    throw new Error("Unresolved compilation problems:"
      + "\nmoduleName cannot be resolved"
      + "\nlastIndexOf cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nsubstring cannot be resolved"
      + "\n- cannot be resolved"
      + "\nsubstring cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nsource cannot be resolved");
  }
}
