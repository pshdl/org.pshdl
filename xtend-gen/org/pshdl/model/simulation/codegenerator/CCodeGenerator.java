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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.pshdl.interpreter.ExecutableModel;
import org.pshdl.interpreter.Frame;
import org.pshdl.interpreter.IHDLInterpreterFactory;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.NativeRunner;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.simulation.ITypeOuptutProvider;
import org.pshdl.model.simulation.SimulationTransformationExtension;
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

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

@SuppressWarnings("all")
public class CCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
	private CommonCompilerExtension cce;

	private boolean hasPow = false;

	public static String COMPILER = "/usr/bin/clang";

	public CCodeGenerator() {
	}

	public CCodeGenerator(final CCodeGeneratorParameter parameter) {
		super(parameter);
		final CommonCompilerExtension _commonCompilerExtension = new CommonCompilerExtension(this.em, 64);
		this.cce = _commonCompilerExtension;
	}

	public IHDLInterpreterFactory<NativeRunner> createInterpreter(final File tempDir) {
		try {
			final File testCFile = new File(tempDir, "test.c");
			final String _generateMainCode = this.generateMainCode();
			Files.write(_generateMainCode, testCFile, StandardCharsets.UTF_8);
			final File testRunner = new File(tempDir, "runner.c");
			final InputStream runnerStream = CCodeGenerator.class.getResourceAsStream("/org/pshdl/model/simulation/includes/runner.c");
			final FileOutputStream fos = new FileOutputStream(testRunner);
			try {
				ByteStreams.copy(runnerStream, fos);
			} finally {
				runnerStream.close();
				fos.close();
			}
			final File executable = new File(tempDir, "testExec");
			this.writeAuxiliaryContents(tempDir);
			final String _absolutePath = tempDir.getAbsolutePath();
			final String _absolutePath_1 = testCFile.getAbsolutePath();
			final String _absolutePath_2 = testRunner.getAbsolutePath();
			final String _absolutePath_3 = executable.getAbsolutePath();
			final ProcessBuilder builder = new ProcessBuilder(CCodeGenerator.COMPILER, "-I", _absolutePath, "-O3", _absolutePath_1, _absolutePath_2, "-o", _absolutePath_3);
			final ProcessBuilder _directory = builder.directory(tempDir);
			final ProcessBuilder _inheritIO = _directory.inheritIO();
			final Process process = _inheritIO.start();
			process.waitFor();
			final int _exitValue = process.exitValue();
			final boolean _notEquals = (_exitValue != 0);
			if (_notEquals)
				throw new RuntimeException("Process did not terminate with 0");
			return new IHDLInterpreterFactory<NativeRunner>() {
				@Override
				public NativeRunner newInstance() {
					try {
						final String _absolutePath = executable.getAbsolutePath();
						final ProcessBuilder execBuilder = new ProcessBuilder(_absolutePath);
						final ProcessBuilder _directory = execBuilder.directory(tempDir);
						final ProcessBuilder _redirectErrorStream = _directory.redirectErrorStream(true);
						final Process testExec = _redirectErrorStream.start();
						final InputStream _inputStream = testExec.getInputStream();
						final OutputStream _outputStream = testExec.getOutputStream();
						final String _absolutePath_1 = executable.getAbsolutePath();
						return new NativeRunner(_inputStream, _outputStream, CCodeGenerator.this.em, testExec, 5, _absolutePath_1);
					} catch (final Throwable _e) {
						throw Exceptions.sneakyThrow(_e);
					}
				}
			};
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@Override
	protected CharSequence applyRegUpdates() {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("updateRegs();");
		return _builder;
	}

	@Override
	protected CharSequence assignArrayInit(final VariableInformation hvar, final BigInteger initValue, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _fieldName = this.fieldName(hvar, attributes);
		_builder.append(_fieldName, "");
		_builder.append("[");
		final int _arraySize = this.getArraySize(hvar);
		_builder.append(_arraySize, "");
		_builder.append("];");
		return _builder;
	}

	@Override
	protected CharSequence arrayInit(final VariableInformation varInfo, final BigInteger zero, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub");
	}

	@Override
	protected CharSequence callStage(final int stage, final boolean constant) {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _stageMethodName = this.stageMethodName(stage, constant);
		_builder.append(_stageMethodName, "");
		_builder.append("();");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence checkRegupdates() {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("regUpdatePos!=0");
		return _builder;
	}

	@Override
	protected CharSequence clearRegUpdates() {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("regUpdatePos=0;");
		return _builder;
	}

	@Override
	protected CharSequence fieldType(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
		final boolean _isBoolean = this.isBoolean(varInfo, attributes);
		if (_isBoolean)
			return "bool";
		return "uint64_t";
	}

	@Override
	protected CharSequence justDeclare(final VariableInformation varInfo, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _fieldName = this.fieldName(varInfo, attributes);
		_builder.append(_fieldName, "");
		{
			final boolean _isArray = this.isArray(varInfo);
			if (_isArray) {
				_builder.append("[");
				final int _arraySize = this.getArraySize(varInfo);
				_builder.append(_arraySize, "");
				_builder.append("]");
			}
		}
		_builder.append(";");
		return _builder;
	}

	@Override
	protected CharSequence footer() {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _helperMethods = this.helperMethods();
		_builder.append(_helperMethods, "");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence postFieldDeclarations() {
		final StringConcatenation _builder = new StringConcatenation();
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
				final CharSequence _copyRegs = this.copyRegs();
				_builder.append(_copyRegs, "");
				_builder.newLineIfNotEmpty();
			}
		}
		return _builder;
	}

	@Override
	protected CharSequence functionFooter(final Frame frame) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("}");
		_builder.newLine();
		return _builder;
	}

	@Override
	protected CharSequence functionHeader(final Frame frame) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("static void ");
		final CharSequence _frameName = this.getFrameName(frame);
		_builder.append(_frameName, "");
		_builder.append("() {");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence header() {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("#include <stdint.h>");
		_builder.newLine();
		_builder.append("#include <stdbool.h>");
		_builder.newLine();
		_builder.append("#include <string.h>");
		_builder.newLine();
		_builder.append("#include \"pshdl_generic_sim.h\"");
		_builder.newLine();
		_builder.append("#include \"");
		final String _headerName = this.headerName();
		_builder.append(_headerName, "");
		_builder.append(".h\"");
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
				final int _maxRegUpdates = this.maxRegUpdates();
				_builder.append(_maxRegUpdates, "");
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

	protected CharSequence copyRegs() {
		final StringConcatenation _builder = new StringConcatenation();
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
		final CharSequence _updateRegCases = this.updateRegCases();
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
				final int _bitwiseXor = (hash ^ b);
				hash = _bitwiseXor;
				hash = (hash * 16777619);
			}
		}
		return hash;
	}

	@Override
	protected CharSequence writeToNull(final String last) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("(void)");
		_builder.append(last, "");
		_builder.append("; //Write to #null");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence runMethodsFooter(final boolean constant) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("}");
		_builder.newLine();
		return _builder;
	}

	@Override
	protected CharSequence runMethodsHeader(final boolean constant) {
		final StringConcatenation _builder = new StringConcatenation();
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
	protected CharSequence scheduleShadowReg(final InternalInformation outputInternal, final CharSequence last, final CharSequence cpyName, final CharSequence offset,
			final boolean force, final CharSequence fillValue) {
		final StringConcatenation _builder = new StringConcatenation();
		{
			if ((!force)) {
				_builder.append("if (");
				_builder.append(cpyName, "");
				_builder.append("!=");
				_builder.append(last, "");
				_builder.append(")");
				_builder.newLineIfNotEmpty();
				final CharSequence _indent = this.indent();
				_builder.append(_indent, "");
				_builder.append("\t");
			}
		}
		_builder.append("{");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_1 = this.indent();
		_builder.append(_indent_1, "");
		_builder.append("\t\tstatic regUpdate_t reg;");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_2 = this.indent();
		_builder.append(_indent_2, "");
		_builder.append("\t\treg.internal=");
		final int _varIdx = this.getVarIdx(outputInternal);
		_builder.append(_varIdx, "");
		_builder.append(";");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_3 = this.indent();
		_builder.append(_indent_3, "");
		_builder.append("\t\treg.offset=(int)");
		_builder.append(offset, "");
		_builder.append(";");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_4 = this.indent();
		_builder.append(_indent_4, "");
		_builder.append("\t\treg.fillValue=");
		_builder.append(fillValue, "");
		_builder.append(";");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_5 = this.indent();
		_builder.append(_indent_5, "");
		_builder.append("\t\tregUpdates[regUpdatePos++]=reg;");
		_builder.newLineIfNotEmpty();
		final CharSequence _indent_6 = this.indent();
		_builder.append(_indent_6, "");
		_builder.append("\t}");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence stageMethodsFooter(final int stage, final int totalStageCosts, final boolean constant) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("}");
		_builder.newLine();
		return _builder;
	}

	@Override
	protected CharSequence stageMethodsHeader(final int stage, final int totalStageCosts, final boolean constant) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("static void ");
		final CharSequence _stageMethodName = this.stageMethodName(stage, constant);
		_builder.append(_stageMethodName, "");
		_builder.append("(){");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence getCast(final int targetSizeWithType) {
		final boolean _isSignedType = this.isSignedType(targetSizeWithType);
		if (_isSignedType)
			return "(int64_t)";
		return "";
	}

	@Override
	protected CharSequence twoOp(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand,
			final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
		CharSequence _xblockexpression = null;
		{
			final Instruction _switchValue = fi.inst;
			if (_switchValue != null) {
				switch (_switchValue) {
				case sra:
					final StringConcatenation _builder = new StringConcatenation();
					_builder.append("((int64_t)");
					final String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder.append(_tempName, "");
					_builder.append(") >> ");
					final String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder.append(_tempName_1, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder, true);
				case srl:
					final StringConcatenation _builder_1 = new StringConcatenation();
					final String _tempName_2 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder_1.append(_tempName_2, "");
					_builder_1.append(" >> ");
					final String _tempName_3 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder_1.append(_tempName_3, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_1, true);
				case less:
					final StringConcatenation _builder_2 = new StringConcatenation();
					_builder_2.append("(int64_t)");
					final String _tempName_4 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder_2.append(_tempName_4, "");
					_builder_2.append(" < (int64_t)");
					final String _tempName_5 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder_2.append(_tempName_5, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_2, true);
				case less_eq:
					final StringConcatenation _builder_3 = new StringConcatenation();
					_builder_3.append("(int64_t)");
					final String _tempName_6 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder_3.append(_tempName_6, "");
					_builder_3.append(" <= (int64_t)");
					final String _tempName_7 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder_3.append(_tempName_7, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_3, true);
				case greater:
					final StringConcatenation _builder_4 = new StringConcatenation();
					_builder_4.append("(int64_t)");
					final String _tempName_8 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder_4.append(_tempName_8, "");
					_builder_4.append(" > (int64_t)");
					final String _tempName_9 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder_4.append(_tempName_9, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_4, true);
				case greater_eq:
					final StringConcatenation _builder_5 = new StringConcatenation();
					_builder_5.append("(int64_t)");
					final String _tempName_10 = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
					_builder_5.append(_tempName_10, "");
					_builder_5.append(" >= (int64_t)");
					final String _tempName_11 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
					_builder_5.append(_tempName_11, "");
					return this.assignTempVar(targetSizeWithType, pos, attributes, _builder_5, true);
				default:
					break;
				}
			}
			_xblockexpression = super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask);
		}
		return _xblockexpression;
	}

	@Override
	protected CharSequence copyArray(final VariableInformation varInfo) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("memcpy(");
		final EnumSet<CommonCodeGenerator.Attributes> _of = EnumSet.<CommonCodeGenerator.Attributes> of(CommonCodeGenerator.Attributes.isPrev);
		final CharSequence _idName = this.idName(varInfo, true, _of);
		_builder.append(_idName, "");
		_builder.append(", ");
		final CharSequence _idName_1 = this.idName(varInfo, true, CommonCodeGenerator.NONE);
		_builder.append(_idName_1, "");
		_builder.append(", ");
		final int _arraySize = this.getArraySize(varInfo);
		_builder.append(_arraySize, "");
		_builder.append(");");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	@Override
	protected CharSequence preField(final VariableInformation x, final EnumSet<CommonCodeGenerator.Attributes> attributes) {
		final StringConcatenation _builder = new StringConcatenation();
		{
			final boolean _contains = attributes.contains(CommonCodeGenerator.Attributes.isPublic);
			final boolean _not = (!_contains);
			if (_not) {
				_builder.append("static");
			}
		}
		_builder.append(" ");
		return _builder;
	}

	protected CharSequence helperMethods() {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("void pshdl_sim_setInput(uint32_t idx, uint64_t value) {");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("pshdl_sim_setInputArray(idx, value, ((void *)0));");
		_builder.newLine();
		_builder.append("}");
		_builder.newLine();
		_builder.append("void pshdl_sim_setInputArray(uint32_t idx, uint64_t value, uint32_t arrayIdx[]) {");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("switch (idx) {");
		_builder.newLine();
		_builder.append("\t\t");
		final CharSequence _setInputCases = this.setInputCases("value", null);
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
			final Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
			for (final VariableInformation v : _excludeNull) {
				_builder.append("\t\t");
				_builder.append("case ");
				final int _varIdx = this.getVarIdx(v, false);
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
		final String _jSONDescription = this.cce.getJSONDescription();
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
		final int _size = this.varIdx.size();
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
			final Iterable<VariableInformation> _excludeNull_1 = this.excludeNull(this.em.variables);
			final Map<Integer, List<VariableInformation>> _hashed = this.getHashed(_excludeNull_1);
			final Set<Map.Entry<Integer, List<VariableInformation>>> _entrySet = _hashed.entrySet();
			for (final Map.Entry<Integer, List<VariableInformation>> e : _entrySet) {
				_builder.append("\t\t");
				_builder.append("case ");
				final Integer _key = e.getKey();
				final CharSequence _constant32Bit = this.constant32Bit((_key).intValue());
				_builder.append(_constant32Bit, "\t\t");
				_builder.append(":");
				_builder.newLineIfNotEmpty();
				{
					final List<VariableInformation> _value = e.getValue();
					for (final VariableInformation vi : _value) {
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
						final int _varIdx_1 = this.getVarIdx(vi, this.purgeAliases);
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
		_builder.append("return pshdl_sim_getOutputArray(idx, ((void *)0));");
		_builder.newLine();
		_builder.append("}");
		_builder.newLine();
		_builder.newLine();
		_builder.append("uint64_t pshdl_sim_getOutputArray(uint32_t idx, uint32_t arrayIdx[]) {");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("switch (idx) {");
		_builder.newLine();
		_builder.append("\t\t");
		final CharSequence _outputCases = this.getOutputCases(null);
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
		final Map<Integer, List<VariableInformation>> res = Maps.<Integer, List<VariableInformation>> newLinkedHashMap();
		for (final VariableInformation vi : this.em.variables) {
			{
				final int hashVal = CCodeGenerator.hash(vi.name);
				final List<VariableInformation> list = res.get(Integer.valueOf(hashVal));
				final boolean _tripleEquals = (list == null);
				if (_tripleEquals) {
					final ArrayList<VariableInformation> _newArrayList = Lists.<VariableInformation> newArrayList(vi);
					res.put(Integer.valueOf(hashVal), _newArrayList);
				} else {
					list.add(vi);
				}
			}
		}
		return res;
	}

	@Override
	protected CharSequence barrier() {
		final StringConcatenation _builder = new StringConcatenation();
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
			final int _indent = this.indent;
			indent = (_indent + 2);
			final StringConcatenation _builder = new StringConcatenation();
			_builder.append("#pragma omp parallel sections");
			_builder.newLine();
			final CharSequence _indent_1 = this.indent();
			_builder.append(_indent_1, "");
			_builder.append("{");
			_builder.newLineIfNotEmpty();
			final CharSequence _indent_2 = this.indent();
			_builder.append(_indent_2, "");
			_builder.append("#pragma omp section");
			_builder.newLineIfNotEmpty();
			final CharSequence _indent_3 = this.indent();
			_builder.append(_indent_3, "");
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
			final int _indent = this.indent;
			indent = (_indent - 2);
			final StringConcatenation _builder = new StringConcatenation();
			_builder.append("\t");
			_builder.append("}");
			_builder.newLine();
			final CharSequence _indent_1 = this.indent();
			_builder.append(_indent_1, "");
			_builder.append("}");
			_builder.newLineIfNotEmpty();
			_xblockexpression = _builder;
		}
		return _xblockexpression;
	}

	@Override
	public Iterable<AuxiliaryContent> getAuxiliaryContent() {
		try {
			final InputStream _resourceAsStream = CCodeGenerator.class.getResourceAsStream("/org/pshdl/model/simulation/includes/pshdl_generic_sim.h");
			final AuxiliaryContent generic_h = new AuxiliaryContent("pshdl_generic_sim.h", _resourceAsStream, true);
			final String _headerName = this.headerName();
			final String _plus = (_headerName + ".h");
			final CharSequence _specificHeader = this.getSpecificHeader();
			final String _string = _specificHeader.toString();
			final AuxiliaryContent specific_h = new AuxiliaryContent(_plus, _string);
			final ArrayList<AuxiliaryContent> res = Lists.<AuxiliaryContent> newArrayList(generic_h, specific_h);
			final String simEncapsulation = this.generateSimEncapsuation();
			final boolean _tripleNotEquals = (simEncapsulation != null);
			if (_tripleNotEquals) {
				final AuxiliaryContent _auxiliaryContent = new AuxiliaryContent("simEncapsulation.c", simEncapsulation);
				res.add(_auxiliaryContent);
			}
			return res;
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	protected String headerName() {
		final CharSequence _idName = this.idName(this.em.moduleName, false, CommonCodeGenerator.NONE);
		final String _plus = ("pshdl_" + _idName);
		return (_plus + "_sim");
	}

	protected CharSequence getSpecificHeader() {
		final StringConcatenation _builder = new StringConcatenation();
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
		final String _headerName = this.headerName();
		_builder.append(_headerName, "");
		_builder.append("_h_");
		_builder.newLineIfNotEmpty();
		_builder.append("#define _");
		final String _headerName_1 = this.headerName();
		_builder.append(_headerName_1, "");
		_builder.append("_h_");
		_builder.newLineIfNotEmpty();
		_builder.append("#include \"pshdl_generic_sim.h\"");
		_builder.newLine();
		_builder.newLine();
		{
			final Iterable<VariableInformation> _excludeNull = this.excludeNull(this.em.variables);
			for (final VariableInformation vi : _excludeNull) {
				_builder.append("///Use this index define to access <tt> ");
				final String _replaceAll = vi.name.replaceAll("\\@", "\\\\@");
				_builder.append(_replaceAll, "");
				_builder.append(" </tt> via getOutput/setInput methods");
				_builder.newLineIfNotEmpty();
				_builder.append("#define ");
				final CharSequence _defineName = this.getDefineName(vi);
				_builder.append(_defineName, "");
				_builder.append(" ");
				final int _varIdx = this.getVarIdx(vi, this.purgeAliases);
				_builder.append(_varIdx, "");
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.newLine();
		final CharSequence _fieldDeclarations = this.fieldDeclarations(false, false);
		final String _string = _fieldDeclarations.toString();
		final String[] _split = _string.split("\n");
		final Function1<String, String> _function = new Function1<String, String>() {
			@Override
			public String apply(final String it) {
				return ("extern" + it);
			}
		};
		final List<String> _map = ListExtensions.<String, String> map(((List<String>) Conversions.doWrapArray(_split)), _function);
		final String _join = IterableExtensions.join(_map, "\n");
		_builder.append(_join, "");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("#endif");
		_builder.newLine();
		return _builder;
	}

	public String generateSimEncapsuation() {
		final Unit unit = this.getUnit(this.em);
		final boolean _tripleEquals = (unit == null);
		if (_tripleEquals)
			return null;
		final List<Row> _buildRows = MemoryModel.buildRows(unit);
		return this.generateSimEncapsuation(unit, _buildRows);
	}

	public Unit getUnit(final ExecutableModel model) {
		try {
			Unit unit = null;
			final Splitter annoSplitter = Splitter.on(SimulationTransformationExtension.ANNO_VALUE_SEP);
			final boolean _tripleNotEquals = (this.em.annotations != null);
			if (_tripleNotEquals) {
				for (final String a : this.em.annotations) {
					final boolean _startsWith = a.startsWith("busDescription");
					if (_startsWith) {
						final Splitter _limit = annoSplitter.limit(2);
						final Iterable<String> _split = _limit.split(a);
						final String value = IterableExtensions.<String> last(_split);
						final LinkedHashSet<Problem> _linkedHashSet = new LinkedHashSet<Problem>();
						final Unit _parseUnit = MemoryModelAST.parseUnit(value, _linkedHashSet, 0);
						unit = _parseUnit;
					}
				}
			}
			return unit;
		} catch (final Throwable _e) {
			throw Exceptions.sneakyThrow(_e);
		}
	}

	@Extension
	private final BusAccess ba = new BusAccess();

	private String generateSimEncapsuation(final Unit unit, final Iterable<Row> rows) {
		final Set<String> varNames = new LinkedHashSet<String>();
		final Procedure1<Row> _function = new Procedure1<Row>() {
			@Override
			public void apply(final Row it) {
				final List<Definition> _allDefs = CCodeGenerator.this.ba.allDefs(it);
				final Function1<Definition, Boolean> _function = new Function1<Definition, Boolean>() {
					@Override
					public Boolean apply(final Definition it) {
						return Boolean.valueOf((it.type != Definition.Type.UNUSED));
					}
				};
				final Iterable<Definition> _filter = IterableExtensions.<Definition> filter(_allDefs, _function);
				final Procedure1<Definition> _function_1 = new Procedure1<Definition>() {
					@Override
					public void apply(final Definition it) {
						final String _name = it.getName();
						varNames.add(_name);
					}
				};
				IterableExtensions.<Definition> forEach(_filter, _function_1);
			}
		};
		IterableExtensions.<Row> forEach(rows, _function);
		final StringConcatenation _builder = new StringConcatenation();
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
		final String _headerName = this.headerName();
		_builder.append(_headerName, "");
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
		_builder.append("busclk_idx", "");
		_builder.append(" ");
		final int _busIndex = this.getBusIndex();
		_builder.append(_busIndex, "");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		String res = _builder.toString();
		final LinkedHashSet<String> checkedRows = new LinkedHashSet<String>();
		final LinkedHashMap<String, Integer> rowCounts = new LinkedHashMap<String, Integer>();
		for (final Row row : rows) {
			{
				final Integer idx = rowCounts.get(row.name);
				final boolean _tripleEquals = (idx == null);
				if (_tripleEquals) {
					rowCounts.put(row.name, Integer.valueOf(1));
				} else {
					rowCounts.put(row.name, Integer.valueOf(((idx).intValue() + 1)));
				}
			}
		}
		for (final Row row_1 : rows) {
			final boolean _contains = checkedRows.contains(row_1.name);
			final boolean _not = (!_contains);
			if (_not) {
				final boolean _hasWriteDefs = this.ba.hasWriteDefs(row_1);
				if (_hasWriteDefs) {
					final Integer _get = rowCounts.get(row_1.name);
					final CharSequence _simSetter = this.simSetter(row_1, (_get).intValue());
					final String _plus = (res + _simSetter);
					res = _plus;
				}
				final Integer _get_1 = rowCounts.get(row_1.name);
				final CharSequence _simGetter = this.simGetter(row_1, (_get_1).intValue());
				final String _plus_1 = (res + _simGetter);
				res = _plus_1;
				checkedRows.add(row_1.name);
			}
		}
		return res;
	}

	protected int getBusIndex() {
		final Integer pclk = this.varIdx.get((this.em.moduleName + ".PCLK"));
		final boolean _tripleEquals = (pclk == null);
		if (_tripleEquals)
			return (this.varIdx.get((this.em.moduleName + ".Bus2IP_Clk"))).intValue();
		return (pclk).intValue();
	}

	protected CharSequence getDefineName(final VariableInformation vi) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("PSHDL_SIM_");
		final CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
		final String _string = _idName.toString();
		final String _upperCase = _string.toUpperCase();
		_builder.append(_upperCase, "");
		return _builder;
	}

	protected CharSequence getDefineNameString(final String s) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("PSHDL_SIM_");
		final CharSequence _idName = this.idName(((this.em.moduleName + ".") + s), true, CommonCodeGenerator.NONE);
		final String _string = _idName.toString();
		final String _upperCase = _string.toUpperCase();
		_builder.append(_upperCase, "");
		return _builder;
	}

	protected CharSequence simGetter(final Row row, final int rowCount) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* Directly retrieve the fields of row ");
		_builder.append(row.name, " ");
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
				_builder.append((rowCount - 1), " ");
			}
		}
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _allDefs = this.ba.allDefs(row);
			for (final Definition d : _allDefs) {
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
		final String _firstUpper = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper, "");
		_builder.append("Direct(uint32_t *base, uint32_t index");
		{
			final List<Definition> _allDefs_1 = this.ba.allDefs(row);
			for (final Definition definition : _allDefs_1) {
				final String _parameter = this.ba.getParameter(row, definition, true);
				_builder.append(_parameter, "");
			}
		}
		_builder.append("){");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("uint32_t offset[1]={index};");
		_builder.newLine();
		{
			final List<Definition> _allDefs_2 = this.ba.allDefs(row);
			for (final Definition d_1 : _allDefs_2) {
				_builder.append("\t");
				_builder.append("*");
				final String _varName = this.ba.getVarName(row, d_1);
				_builder.append(_varName, "\t");
				_builder.append("=(");
				final CharSequence _busType = this.ba.getBusType(d_1);
				_builder.append(_busType, "\t");
				_builder.append(")pshdl_sim_getOutputArray(");
				final CharSequence _defineNameString = this.getDefineNameString(d_1.name);
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
		_builder.append(row.name, " ");
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
				_builder.append((rowCount - 1), " ");
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
		final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_1, "");
		_builder.append("(uint32_t *base, uint32_t index, ");
		_builder.append(row.name, "");
		_builder.append("_t *result){");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("return get");
		final String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_2, "\t");
		_builder.append("Direct(base, index");
		{
			final List<Definition> _allDefs_3 = this.ba.allDefs(row);
			for (final Definition d_2 : _allDefs_3) {
				_builder.append(", &result->");
				final String _varNameIndex = this.ba.getVarNameIndex(row, d_2);
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
		final StringConcatenation _builder = new StringConcatenation();
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
				_builder.append((rowCount - 1), " ");
			}
		}
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _allDefs = this.ba.allDefs(row);
			for (final Definition d : _allDefs) {
				_builder.append(" ");
				_builder.append("* @param ");
				_builder.append(d.name, " ");
				_builder.append(" the value of ");
				_builder.append(d.name, " ");
				_builder.append(" will be written into the register. ");
				final StringBuilder _explain = this.ba.explain(d);
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
		final String _firstUpper = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper, "");
		_builder.append("Direct(uint32_t *base, uint32_t index");
		{
			final List<Definition> _writeDefs = this.ba.writeDefs(row);
			for (final Definition definition : _writeDefs) {
				final String _parameter = this.ba.getParameter(row, definition, false);
				_builder.append(_parameter, "");
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
			final List<Definition> _writeDefs_1 = this.ba.writeDefs(row);
			for (final Definition ne : _writeDefs_1) {
				_builder.append("\t");
				final CharSequence _generateConditions = this.ba.generateConditions(row, ne);
				_builder.append(_generateConditions, "\t");
				_builder.newLineIfNotEmpty();
			}
		}
		{
			final List<Definition> _writeDefs_2 = this.ba.writeDefs(row);
			for (final Definition d_1 : _writeDefs_2) {
				_builder.append("\t");
				_builder.append("pshdl_sim_setInputArray(");
				final CharSequence _defineNameString = this.getDefineNameString(d_1.name);
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
				_builder.append((rowCount - 1), " ");
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
		final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_1, "");
		_builder.append("(uint32_t *base, uint32_t index, ");
		_builder.append(row.name, "");
		_builder.append("_t *newVal) {");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("return set");
		final String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_2, "\t");
		_builder.append("Direct(base, index");
		{
			final List<Definition> _writeDefs_3 = this.ba.writeDefs(row);
			for (final Definition d_2 : _writeDefs_3) {
				_builder.append(", newVal->");
				final String _varNameIndex = this.ba.getVarNameIndex(row, d_2);
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
	protected CharSequence callMethod(final CharSequence methodName, final CharSequence... args) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append(methodName, "");
		_builder.append("(");
		{
			final boolean _tripleNotEquals = (args != null);
			if (_tripleNotEquals) {
				{
					boolean _hasElements = false;
					for (final CharSequence arg : args) {
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
		final List<AuxiliaryContent> sideFiles = Lists.<AuxiliaryContent> newLinkedList();
		final Iterable<AuxiliaryContent> _auxiliaryContent = comp.getAuxiliaryContent();
		Iterables.<AuxiliaryContent> addAll(sideFiles, _auxiliaryContent);
		final String _generateMainCode = comp.generateMainCode();
		final String _string = _generateMainCode.toString();
		final String _hookName = comp.getHookName();
		final PSAbstractCompiler.CompileResult _compileResult = new PSAbstractCompiler.CompileResult(syntaxProblems, _string, parameter.em.moduleName, sideFiles,
				parameter.em.source, _hookName, true);
		return Lists.<PSAbstractCompiler.CompileResult> newArrayList(_compileResult);
	}

	@Override
	public List<PSAbstractCompiler.CompileResult> invoke(final CommandLine cli, final ExecutableModel em, final Set<Problem> syntaxProblems) throws Exception {
		final CCodeGeneratorParameter _cCodeGeneratorParameter = new CCodeGeneratorParameter(em);
		return CCodeGenerator.doCompile(syntaxProblems, _cCodeGeneratorParameter);
	}

	@Override
	protected CharSequence fillArray(final VariableInformation vi, final CharSequence regFillValue) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("memset(");
		final CharSequence _idName = this.idName(vi, true, CommonCodeGenerator.NONE);
		_builder.append(_idName, "");
		_builder.append(", ");
		_builder.append(regFillValue, "");
		_builder.append(", ");
		final int _arraySize = this.getArraySize(vi);
		_builder.append(_arraySize, "");
		_builder.append(");");
		return _builder;
	}

	@Override
	protected CharSequence pow(final Frame.FastInstruction fi, final String op, final int targetSizeWithType, final int pos, final int leftOperand, final int rightOperand,
			final EnumSet<CommonCodeGenerator.Attributes> attributes, final boolean doMask) {
		this.hasPow = true;
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("pshdl_sim_pow(");
		final String _tempName = this.getTempName(leftOperand, CommonCodeGenerator.NONE);
		_builder.append(_tempName, "");
		_builder.append(", ");
		final String _tempName_1 = this.getTempName(rightOperand, CommonCodeGenerator.NONE);
		_builder.append(_tempName_1, "");
		_builder.append(")");
		return this.assignTempVar(targetSizeWithType, pos, CommonCodeGenerator.NONE, _builder, true);
	}
}
