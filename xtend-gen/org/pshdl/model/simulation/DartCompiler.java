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
public class DartCompiler implements ITypeOuptutProvider {
  @Extension
  private CommonCompilerExtension cce;
  
  private int epsWidth;
  
  public DartCompiler() {
  }
  
  public DartCompiler(final /* ExecutableModel */Object em) {
    CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(em);
    this.cce = _commonCompilerExtension;
    int _size = this.cce.prevMap.size();
    int _highestOneBit = Integer.highestOneBit(_size);
    int _plus = (_highestOneBit + 1);
    this.epsWidth = _plus;
  }
  
  public static String doCompile(final /* ExecutableModel */Object em, final String unitName) {
    DartCompiler _dartCompiler = new DartCompiler(em);
    CharSequence _compile = _dartCompiler.compile(unitName);
    return _compile.toString();
  }
  
  public CharSequence compile(final String unitName) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method width is undefined for the type DartCompiler"
      + "\nThe method width is undefined for the type DartCompiler"
      + "\nThe method dimensions is undefined for the type DartCompiler"
      + "\nThe method width is undefined for the type DartCompiler"
      + "\nThe method dimensions is undefined for the type DartCompiler"
      + "\nThe method width is undefined for the type DartCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type DartCompiler"
      + "\nThe method edgePosDepRes is undefined for the type DartCompiler"
      + "\nThe method predNegDepRes is undefined for the type DartCompiler"
      + "\nThe method predPosDepRes is undefined for the type DartCompiler"
      + "\nThe method uniqueID is undefined for the type DartCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type DartCompiler"
      + "\nThe method edgePosDepRes is undefined for the type DartCompiler"
      + "\nThe method predNegDepRes is undefined for the type DartCompiler"
      + "\nThe method predPosDepRes is undefined for the type DartCompiler"
      + "\nThe method uniqueID is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nreplaceAll cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
      + "\n! cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\n! cannot be resolved"
      + "\n&& cannot be resolved"
      + "\n! cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\nsize cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\nsize cannot be resolved"
      + "\nasMask cannot be resolved"
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
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nvariables cannot be resolved"
      + "\n! cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nreplaceAll cannot be resolved"
      + "\nvariables cannot be resolved");
  }
  
  public CharSequence getDescription() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field dir is undefined for the type DartCompiler"
      + "\nDirection cannot be resolved to a type."
      + "\nThe method or field dir is undefined for the type DartCompiler"
      + "\nDirection cannot be resolved to a type."
      + "\nThe method or field dir is undefined for the type DartCompiler"
      + "\nDirection cannot be resolved to a type."
      + "\nThe method or field dir is undefined for the type DartCompiler"
      + "\nDirection cannot be resolved to a type."
      + "\nvariables cannot be resolved"
      + "\nfilter cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nIN cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nfilter cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINOUT cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nfilter cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nOUT cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nfilter cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINTERNAL cannot be resolved");
  }
  
  public CharSequence asPort(final /* VariableInformation */Object v) {
    throw new Error("Unresolved compilation problems:"
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\narray cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nisClock cannot be resolved"
      + "\nisReset cannot be resolved"
      + "\ntype cannot be resolved"
      + "\nBIT cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nUINT cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nreplaceAll cannot be resolved"
      + "\nwidth cannot be resolved");
  }
  
  public CharSequence copyRegs() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method isRegister is undefined for the type DartCompiler"
      + "\nThe method name is undefined for the type DartCompiler"
      + "\nvariables cannot be resolved"
      + "\n! cannot be resolved");
  }
  
  public String copyPrev(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\narray cannot be resolved"
      + "\n! cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence getter(final /* InternalInformation */Object info, final boolean prev, final int pos, final int frameID) {
    throw new Error("Unresolved compilation problems:"
      + "\nactualWidth cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\nfixedArray cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\nasMask cannot be resolved");
  }
  
  public CharSequence setter(final /* InternalInformation */Object info, final String value) {
    throw new Error("Unresolved compilation problems:"
      + "\nactualWidth cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
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
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndartType cannot be resolved"
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
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccessBracket cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccessBracket cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccessBracket cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccessBracket cannot be resolved"
      + "\nisShadowReg cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public String method(final /* Frame */Object frame) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method inst is undefined for the type DartCompiler"
      + "\nThe method inst is undefined for the type DartCompiler"
      + "\nThe method inst is undefined for the type DartCompiler"
      + "\nThe method inst is undefined for the type DartCompiler"
      + "\nInstruction cannot be resolved to a type."
      + "\nThe method inst is undefined for the type DartCompiler"
      + "\nInstruction cannot be resolved to a type."
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
      + "\nsetter cannot be resolved");
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
      + "\nInstruction cannot be resolved to a type."
      + "\ninst cannot be resolved"
      + "\npushAddIndex cannot be resolved"
      + "\nwriteInternal cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\nidName cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccessBracket cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nname cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narray cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\nnoop cannot be resolved"
      + "\nand cannot be resolved"
      + "\narith_neg cannot be resolved"
      + "\nbit_neg cannot be resolved"
      + "\nbitAccessSingle cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nbitAccessSingleRange cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\n- cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\ncast_int cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\ncast_uint cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\nlogiNeg cannot be resolved"
      + "\nlogiAnd cannot be resolved"
      + "\nlogiOr cannot be resolved"
      + "\nconst0 cannot be resolved"
      + "\nconst1 cannot be resolved"
      + "\nconst2 cannot be resolved"
      + "\nconstAll1 cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasMask cannot be resolved"
      + "\nconcat cannot be resolved"
      + "\narg2 cannot be resolved"
      + "\nloadConstant cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nconstant cannot be resolved"
      + "\nloadInternal cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ngetter cannot be resolved"
      + "\nuniqueID cannot be resolved"
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
      + "\n>> cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nbitwiseAnd cannot be resolved"
      + "\n=== cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasMask cannot be resolved"
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
    boolean _tripleEquals = (Integer.valueOf(_bitwiseAnd) == Integer.valueOf(1));
    if (_tripleEquals) {
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
    boolean _tripleEquals = (Integer.valueOf(_bitwiseAnd) == Integer.valueOf(1));
    if (_tripleEquals) {
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
  
  public CharSequence constant(final int id, final /* Frame */Object f) {
    throw new Error("Unresolved compilation problems:"
      + "\nconstants cannot be resolved"
      + "\nget cannot be resolved"
      + "\ntoHexString cannot be resolved");
  }
  
  public Object dartType(final /* InternalInformation */Object ii) {
    throw new Error("Unresolved compilation problems:"
      + "\ninfo cannot be resolved"
      + "\ndartType cannot be resolved");
  }
  
  public String dartType(final /* VariableInformation */Object information, final boolean withArray) {
    throw new Error("Unresolved compilation problems:"
      + "\nInternalInformation cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nVariableInformation$Type cannot be resolved to a type."
      + "\nname cannot be resolved"
      + "\nstartsWith cannot be resolved"
      + "\nPRED_PREFIX cannot be resolved"
      + "\narray cannot be resolved"
      + "\n&& cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntype cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntype cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntype cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved"
      + "\n&& cannot be resolved"
      + "\ntype cannot be resolved"
      + "\n=== cannot be resolved"
      + "\nINT cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\n<= cannot be resolved");
  }
  
  public CharSequence decl(final /* VariableInformation */Object info, final Boolean includePrev) {
    throw new Error("Unresolved compilation problems:"
      + "\nisPredicate cannot be resolved"
      + "\n|| cannot be resolved"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nisRegister cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence initValue(final /* VariableInformation */Object info) {
    throw new Error("Unresolved compilation problems:"
      + "\npredicate cannot be resolved"
      + "\narray cannot be resolved"
      + "\ndartType cannot be resolved"
      + "\ntotalSize cannot be resolved");
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
    _builder.append("import \'../simulation_comm.dart\';");
    _builder.newLine();
    return _builder;
  }
  
  public String getHookName() {
    return "Dart";
  }
  
  public MultiOption getUsage() {
    Options _options = new Options();
    final Options options = _options;
    MultiOption _multiOption = new MultiOption(null, null, options);
    return _multiOption;
  }
  
  public ArrayList<CompileResult> invoke(final CommandLine cli, final /* ExecutableModel */Object em, final Set<Problem> syntaxProblems) throws Exception {
    throw new Error("Unresolved compilation problems:"
      + "\nmoduleName cannot be resolved"
      + "\nsubstring cannot be resolved"
      + "\nlastIndexOf cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n- cannot be resolved"
      + "\nsource cannot be resolved");
  }
}
