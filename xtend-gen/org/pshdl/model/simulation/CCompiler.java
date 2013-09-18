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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.model.simulation.CommonCompilerExtension;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import org.pshdl.model.types.builtIn.busses.memorymodel.Row;
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import org.pshdl.model.utils.PSAbstractCompiler.CompileResult;
import org.pshdl.model.utils.services.IOutputProvider.MultiOption;
import org.pshdl.model.validation.Problem;

@SuppressWarnings("all")
public class CCompiler implements ITypeOuptutProvider {
  @Extension
  private CommonCompilerExtension cce;
  
  private final int bitWidth;
  
  public CCompiler() {
    int _minus = (-1);
    this.bitWidth = _minus;
  }
  
  public CCompiler(final /* ExecutableModel */Object em) {
    throw new Error("Unresolved compilation problems:"
      + "\nmaxDataWidth cannot be resolved"
      + "\n<= cannot be resolved");
  }
  
  public static String doCompileMainC(final /* ExecutableModel */Object em) {
    CCompiler _cCompiler = new CCompiler(em);
    CharSequence _compile = _cCompiler.compile();
    return _compile.toString();
  }
  
  public CharSequence compile() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type CCompiler"
      + "\nThe method edgePosDepRes is undefined for the type CCompiler"
      + "\nThe method predNegDepRes is undefined for the type CCompiler"
      + "\nThe method predPosDepRes is undefined for the type CCompiler"
      + "\nThe method edgeNegDepRes is undefined for the type CCompiler"
      + "\nThe method edgePosDepRes is undefined for the type CCompiler"
      + "\nThe method predNegDepRes is undefined for the type CCompiler"
      + "\nThe method predPosDepRes is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nmaxRegUpdates cannot be resolved"
      + "\nvariables cannot be resolved"
      + "\nexcludeNull cannot be resolved"
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
  
  public CharSequence helperMethods() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method dimensions is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method dimensions is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
      + "\nThe method width is undefined for the type CCompiler"
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
      + "\nvariables cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n- cannot be resolved"
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
  
  public StringBuilder arrayVarArgAccessArrIdx(final /* VariableInformation */Object v) {
    throw new Error("Unresolved compilation problems:"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved");
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
  
  public CharSequence createboolPred(final int id, final Set<Integer> handled) {
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
  
  public CharSequence copyRegs() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method isRegister is undefined for the type CCompiler"
      + "\nThe method name is undefined for the type CCompiler"
      + "\nThe method dimensions is undefined for the type CCompiler"
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
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\ndimMask cannot be resolved"
      + "\ntoHexStringL cannot be resolved"
      + "\nisPred cannot be resolved"
      + "\nfixedArray cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nwidth cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\n== cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitStart cannot be resolved"
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\nbitEnd cannot be resolved"
      + "\nactualWidth cannot be resolved"
      + "\nasMaskL cannot be resolved");
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
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ncType cannot be resolved"
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
      + "\ncType cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ncType cannot be resolved"
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
  
  public CharSequence method(final /* Frame */Object frame) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method inst is undefined for the type CCompiler"
      + "\nThe method inst is undefined for the type CCompiler"
      + "\nThe method inst is undefined for the type CCompiler"
      + "\nThe method inst is undefined for the type CCompiler"
      + "\nInstruction cannot be resolved to a type."
      + "\nThe method inst is undefined for the type CCompiler"
      + "\nInstruction cannot be resolved to a type."
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
      + "\nframeName cannot be resolved"
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
      + "\ninst cannot be resolved"
      + "\npushAddIndex cannot be resolved"
      + "\nwriteInternal cannot be resolved"
      + "\narg1 cannot be resolved"
      + "\nasInternal cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ntotalSize cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n> cannot be resolved"
      + "\narrayAccess cannot be resolved"
      + "\nisShadowReg cannot be resolved"
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
      + "\n!= cannot be resolved"
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
      + "\nconstantI cannot be resolved"
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
  
  public CharSequence uTemp(final int pos, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _uint_t = this.uint_t();
    _builder.append(_uint_t, "");
    _builder.append(" ");
    _builder.append(name, "");
    _builder.append(pos, "");
    return _builder;
  }
  
  public CharSequence sTemp(final int pos, final String name) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _int_t = this.int_t();
    _builder.append(_int_t, "");
    _builder.append(" ");
    _builder.append(name, "");
    _builder.append(pos, "");
    return _builder;
  }
  
  public CharSequence uint_t() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("uint");
    _builder.append(this.bitWidth, "");
    _builder.append("_t");
    return _builder;
  }
  
  public CharSequence int_t() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int");
    _builder.append(this.bitWidth, "");
    _builder.append("_t");
    return _builder;
  }
  
  public CharSequence sra(final int targetSizeWithType, final int a, final int b) {
    final int targetSize = (targetSizeWithType >> 1);
    final int shift = (this.bitWidth - targetSize);
    int _bitwiseAnd = (targetSizeWithType & 1);
    boolean _tripleEquals = (Integer.valueOf(_bitwiseAnd) == Integer.valueOf(1));
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("((");
      CharSequence _int_t = this.int_t();
      _builder.append(_int_t, "");
      _builder.append(")t");
      _builder.append(b, "");
      _builder.append(") >> t");
      _builder.append(a, "");
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("(");
      CharSequence _int_t_1 = this.int_t();
      _builder_1.append(_int_t_1, "");
      _builder_1.append(")");
      return this.cce.signExtend(_builder, _builder_1, shift);
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append("(((");
    CharSequence _int_t_2 = this.int_t();
    _builder_2.append(_int_t_2, "");
    _builder_2.append(")t");
    _builder_2.append(b, "");
    _builder_2.append(") >> t");
    _builder_2.append(a, "");
    _builder_2.append(") & ");
    CharSequence _asMaskL = this.cce.asMaskL(targetSize);
    _builder_2.append(_asMaskL, "");
    _builder_2.append("l");
    return _builder_2.toString();
  }
  
  public String twoOp(final String op, final int targetSizeWithType, final int pos, final int a, final int b) {
    StringConcatenation _builder = new StringConcatenation();
    CharSequence _uTemp = this.uTemp(pos, "t");
    _builder.append(_uTemp, "");
    _builder.append("=");
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("(");
    CharSequence _int_t = this.int_t();
    _builder_1.append(_int_t, "");
    _builder_1.append(")");
    CharSequence _twoOpValue = this.cce.twoOpValue(op, _builder_1.toString(), a, b, targetSizeWithType);
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
      + "\ncType cannot be resolved"
      + "\nisRegister cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ncType cannot be resolved");
  }
  
  public Object cType(final /* InternalInformation */Object ii) {
    throw new Error("Unresolved compilation problems:"
      + "\ninfo cannot be resolved"
      + "\ncType cannot be resolved"
      + "\narrayIdx cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n!= cannot be resolved"
      + "\ninfo cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nlength cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  public CharSequence cType(final /* VariableInformation */Object information) {
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
      + "\ncType cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nempty cannot be resolved"
      + "\n! cannot be resolved"
      + "\ntotalSize cannot be resolved"
      + "\ncType cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nempty cannot be resolved"
      + "\n! cannot be resolved"
      + "\ntotalSize cannot be resolved"
      + "\nisRegister cannot be resolved"
      + "\ncType cannot be resolved"
      + "\nidName cannot be resolved"
      + "\ndimensions cannot be resolved"
      + "\nempty cannot be resolved"
      + "\n! cannot be resolved"
      + "\ntotalSize cannot be resolved");
  }
  
  public CharSequence getImports() {
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
    return _builder;
  }
  
  public String getHookName() {
    return "C";
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
      + "\nsource cannot be resolved");
  }
  
  public String generateSimEncapsuation() {
    final Unit unit = this.getUnit(this.cce.em);
    boolean _equals = Objects.equal(unit, null);
    if (_equals) {
      return null;
    }
    List<Row> _buildRows = MemoryModel.buildRows(unit);
    return this.generateSimEncapsuation(unit, _buildRows);
  }
  
  public Unit getUnit(final /* ExecutableModel */Object model) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method startsWith is undefined for the type CCompiler"
      + "\nannotations cannot be resolved"
      + "\n!== cannot be resolved"
      + "\nannotations cannot be resolved");
  }
  
  @Extension
  private BusAccess ba = new Function0<BusAccess>() {
    public BusAccess apply() {
      BusAccess _busAccess = new BusAccess();
      return _busAccess;
    }
  }.apply();
  
  private String generateSimEncapsuation(final Unit unit, final Iterable<Row> rows) {
    throw new Error("Unresolved compilation problems:"
      + "\nmoduleName cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nmoduleName cannot be resolved"
      + "\n+ cannot be resolved");
  }
  
  public Object getDefineName(final String v) {
    throw new Error("Unresolved compilation problems:"
      + "\nmoduleName cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nidName cannot be resolved");
  }
  
  public CharSequence simGetter(final Row row) {
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
    {
      List<Definition> _allDefs_1 = this.ba.allDefs(row);
      for(final Definition d : _allDefs_1) {
        _builder.append("\t");
        _builder.append("*");
        String _varName = this.ba.getVarName(row, d);
        _builder.append(_varName, "	");
        _builder.append("=pshdl_sim_getOutput(");
        Object _defineName = this.getDefineName(d.name);
        _builder.append(_defineName, "	");
        _builder.append(", index);");
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
    _builder.append(_firstUpper_2, "	");
    _builder.append("Direct(base, index");
    {
      List<Definition> _allDefs_2 = this.ba.allDefs(row);
      for(final Definition d_1 : _allDefs_2) {
        _builder.append(", &result->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_1);
        _builder.append(_varNameIndex, "	");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence simSetter(final Row row) {
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
    {
      List<Definition> _writeDefs_1 = this.ba.writeDefs(row);
      for(final Definition ne : _writeDefs_1) {
        _builder.append("\t");
        CharSequence _generateConditions = this.ba.generateConditions(row, ne);
        _builder.append(_generateConditions, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      List<Definition> _writeDefs_2 = this.ba.writeDefs(row);
      for(final Definition d : _writeDefs_2) {
        _builder.append("\t");
        _builder.append("pshdl_sim_setInput(");
        Object _defineName = this.getDefineName(d.name);
        _builder.append(_defineName, "	");
        _builder.append(", ");
        _builder.append(d.name, "	");
        _builder.append(", index);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("if (disableEdges) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_setInput(");
    Object _defineName_1 = this.getDefineName("Bus2IP_Clk");
    _builder.append(_defineName_1, "		");
    _builder.append(", 0, 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("pshdl_sim_setInput(");
    Object _defineName_2 = this.getDefineName("Bus2IP_Clk");
    _builder.append(_defineName_2, "	");
    _builder.append(", 1, 0);");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("pshdl_sim_run();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//warn(invalidIndex, index, \"\", \"");
    _builder.append(row.name, "	");
    _builder.append("\", \"\");");
    _builder.newLineIfNotEmpty();
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
    _builder.append(_firstUpper_2, "	");
    _builder.append("Direct(base, index");
    {
      List<Definition> _writeDefs_3 = this.ba.writeDefs(row);
      for(final Definition d_1 : _writeDefs_3) {
        _builder.append(", newVal->");
        String _varNameIndex = this.ba.getVarNameIndex(row, d_1);
        _builder.append(_varNameIndex, "	");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
