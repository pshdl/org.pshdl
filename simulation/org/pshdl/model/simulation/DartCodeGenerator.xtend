/*******************************************************************************
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
 ******************************************************************************/
package org.pshdl.model.simulation

import com.google.common.collect.Lists
import com.google.common.io.Files
import java.io.File
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.EnumSet
import java.util.Set
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.IHDLInterpreterFactory
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.NativeRunner
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation.Direction
import org.pshdl.interpreter.utils.Instruction
import org.pshdl.model.utils.PSAbstractCompiler
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem

class DartCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {
	String unitName
	String library
	boolean usePackageImport
	static final int epsWidth = 16;
	public static String TESTRUNNER_DIR = "/Users/karstenbecker/GDrive/DartTestRunner/"
	public static String DART_EXEC = "/Applications/dart/dart-sdk/bin/dart"

	new() {
	}

	new(ExecutableModel model, String unitName, String library, boolean usePackageImport, int maxCosts) {
		super(model, -1, maxCosts)
		this.unitName = unitName
		this.library = library
		this.usePackageImport = usePackageImport
	}

	def IHDLInterpreterFactory<NativeRunner> createInterpreter(File tempDir) {
		val dc = new DartCodeGenerator(em, unitName, library, true, Integer.MAX_VALUE)
		val dartCode = dc.generateMainCode()
		val File binDir = new File(tempDir, "bin")
		if (!binDir.mkdirs())
			throw new IllegalArgumentException("Failed to create Directory " + binDir)
		Files.write(dartCode, new File(binDir, "dut.dart"), StandardCharsets.UTF_8)
		val testRunnerDir = new File(TESTRUNNER_DIR)
		val testRunner = new File(testRunnerDir, "bin/darttestrunner.dart")
		Files.copy(testRunner, new File(binDir, testRunner.getName()))
		val yaml = new File(testRunnerDir, "pubspec.yaml")
		Files.copy(yaml, new File(tempDir, yaml.getName()))
		java.nio.file.Files.createSymbolicLink(new File(binDir, "packages").toPath(),
			new File(testRunnerDir, "packages").toPath())
		java.nio.file.Files.createSymbolicLink(new File(tempDir, "packages").toPath(),
			new File(testRunnerDir, "packages").toPath())
		new IHDLInterpreterFactory<NativeRunner>() {

			override newInstance() {
				val Process dartRunner = new ProcessBuilder(DART_EXEC, "bin/" + testRunner.getName(), unitName, library).
					directory(tempDir).redirectErrorStream(true).start()
				return new NativeRunner(dartRunner.getInputStream(), dartRunner.getOutputStream(), em, dartRunner, 5, "Dart "+library+"."+unitName)
			}

		}
	}

	override protected constantSuffix() {
		return ""
	}

	override protected fieldPrefix() {
		return '_'
	}

	override protected void postBody() {
		indent--;
	}

	override protected void preBody() {
		indent++;
	}

	override protected applyRegUpdates() {
		return "_updateRegs();"
	}

	override protected checkRegupdates() {
		return "!_regUpdates.isEmpty"
	}

	override protected clearRegUpdates() '''_regUpdates.clear();
		'''

	override protected arrayInit(VariableInformation varInfo, BigInteger initValue,
		EnumSet<CommonCodeGenerator.Attributes> attributes) '''new «varInfo.fieldType(attributes)»(«varInfo.arraySize»)'''

	override protected functionFooter(Frame frame) '''}
		'''

	override protected functionHeader(Frame frame) '''
		void _«frame.frameName»() {
	'''

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean forceRegUpdate, CharSequence fillValue) '''
	«IF !forceRegUpdate»if («cpyName»!=«last»)
	«indent()»	«ENDIF»_regUpdates.add(new RegUpdate(«outputInternal.varIdx», «offset», «fillValue»));'''

	override protected runMethodsHeader(boolean constant) '''void «IF !constant»run«ELSE»initConstants«ENDIF»() {
		'''

	override protected runMethodsFooter(boolean constant) '''}
		'''

	override protected callStage(int stage, boolean constant) '''_«stageMethodName(stage, constant)»();
		'''

	override protected stageMethodsFooter(int stage, int stageCosts, boolean constant) '''}
		'''

	override protected copyArray(VariableInformation varInfo) {
		val type = varInfo.fieldType(NONE)
		return '''«varInfo.idName(true, EnumSet.of(CommonCodeGenerator.Attributes.isPrev))» = new «type».from«IF type !=
			"List< "»List«ENDIF»(«varInfo.idName(true, NONE)»);'''
	}

	override protected fieldType(VariableInformation information, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		var jt = "int"
		if (information.isBoolean(attributes))
			jt = "bool"
		if (information.array && !attributes.contains(CommonCodeGenerator.Attributes.baseType)) {
			if (jt == "bool")
				return '''List<«jt»>'''
			if (information.width <= 8 && information.type === VariableInformation.Type.INT)
				return '''Int8List'''
			if (information.width <= 8)
				return '''Uint8List'''
			if (information.width <= 16 && information.type === VariableInformation.Type.INT)
				return '''Int16List'''
			if (information.width <= 16)
				return '''Uint16List'''
			if (information.width <= 32 && information.type === VariableInformation.Type.INT)
				return '''Int32List'''
			if (information.width <= 32)
				return '''Uint32List'''
			if (information.width <= 64 && information.type === VariableInformation.Type.INT)
				return '''Int64List'''
			if (information.width <= 64)
				return '''Uint64List'''
			return '''List<«jt»>'''
		}
		return jt
	}

	override protected calculateVariableAccessIndexArr(VariableInformation varInfo) {
		return "offset"
	}

	override protected footer() '''	void setVar(int idx, dynamic value, {int offset}) {
		switch (idx) {
			«setInputCases("value", null)»
			default:
				throw new ArgumentError("Not a valid index: $idx");
		}
	}
	
	int getIndex(String name) {
		return _varIdx[name];
	}
	
	String getName(int idx) {
		switch (idx) {
			«FOR v : em.variables»
				case «varIdx.get(v.name)»: return "«v.name.replaceAll("[\\$]", "\\\\\\$")»";
			«ENDFOR»
			default:
				throw new ArgumentError("Not a valid index: $idx");
		}
	}
	
	dynamic getVar(int idx, {int offset}) {
		switch (idx) {
			«getOutputCases(null)»
			default:
				throw new ArgumentError("Not a valid index: $idx");
		}
	}
	
	int get deltaCycle =>_deltaCycle;
	
	int get varNum => «varIdx.size»;
	
	bool get «DISABLE_EDGES.name» => «DISABLE_EDGES.idName(true, NONE)»;
	set «DISABLE_EDGES.name»(bool newVal) => «DISABLE_EDGES.idName(true, NONE)»=newVal;
	bool get «DISABLE_REG_OUTPUTLOGIC.name» => «DISABLE_REG_OUTPUTLOGIC.idName(true, NONE)»;
	set «DISABLE_REG_OUTPUTLOGIC.name»(bool newVal) => «DISABLE_REG_OUTPUTLOGIC.idName(true, NONE)»=newVal;
	
	«description»
}
'''

	def getDescription() '''	
Description get description=>new Description(
[
«FOR v : em.variables.filter[dir === Direction.IN] SEPARATOR ','»
	«v.asPort»
«ENDFOR»
],
[
«FOR v : em.variables.filter[dir === Direction.INOUT] SEPARATOR ','»
	«v.asPort»
«ENDFOR»
],
[
«FOR v : em.variables.filter[dir === Direction.OUT] SEPARATOR ','»
	«v.asPort»
«ENDFOR»
],
[
«FOR v : em.variables.filter[dir === Direction.INTERNAL] SEPARATOR ','»
	«v.asPort»
«ENDFOR»
], _varIdx, "«em.moduleName»");
'''

	def asPort(VariableInformation v) {
		var dims = ""
		if (v.array) {
			dims = ''', dimensions: [«FOR i : v.dimensions SEPARATOR ','»«i»«ENDFOR»]'''
		}
		val clock = if(v.isClock) ", clock:true" else ""
		val reset = if(v.isReset) ", reset:true" else ""
		var type = "INVALID"
		switch (v.type) {
			case VariableInformation.Type.BIT: type = "Port.TYPE_BIT"
			case VariableInformation.Type.INT: type = "Port.TYPE_INT"
			case VariableInformation.Type.UINT: type = "Port.TYPE_UINT"
			case VariableInformation.Type.BOOL: type = "Port.TYPE_BOOL"
		}
		'''new Port(«varIdx.get(v.name)», "«v.name.replaceAll("[\\$]", "\\\\\\$")»", «v.width», «type»«dims»«clock»«reset»)'''
	}

	override protected header() '''«IF library !== null»library «library»;«ENDIF»
«getImports(usePackageImport)»
void main(List<String> args, SendPort replyTo){
  	handleReceive((e,l) => new «unitName»(e,l), replyTo);
}
«IF hasClock»
	class RegUpdate {
		final int internalID;
		final int offset;
		final int fillValue;
		
		RegUpdate(this.internalID, this.offset, this.fillValue);
		
		int get hashCode {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + internalID;
			result = (prime * result) + offset;
			result = (prime * result) + fillValue;
			return result;
		}
		
		operator ==(RegUpdate other) {
			if (identical(this, other))
				return true;
			if (other == null)
				return false;
			if (internalID != other.internalID)
				return false;
			if (offset != other.offset)
				return false;
			if (fillValue != other.fillValue)
				return false;
			return true;
		}
	}
	
«ENDIF»
class «unitName» extends DartInterpreter{
	«IF hasClock»
		List<RegUpdate> _regUpdates=[];
	«ENDIF»
	
	Map<String, int> _varIdx={
		«FOR v : em.variables SEPARATOR ','»
			"«v.name.replaceAll("[\\$]", "\\\\\\$")»": «varIdx.get(v.name)»
		«ENDFOR»
	};
	
	List<String> get names=>_varIdx.keys.toList();
	
	«IF hasClock»
	«unitName»(this._«DISABLE_EDGES.name», this._«DISABLE_REG_OUTPUTLOGIC.name»);
	«ELSE»
	«unitName»(bool disableEdges, bool disabledRegOutputlogic) {}
	«ENDIF»
	«IF hasClock»
	bool _skipEdge(int local) {
		int dc = local >> «epsWidth»;
		// Register was updated in previous delta cylce, that is ok
		if (dc < deltaCycle)
			return false;
		// Register was updated in this delta cycle but it is the same eps,
		// that is ok as well
		if ((dc == _deltaCycle) && ((local & «epsWidth.calcMask.constant(true)») == _epsCycle))
			return false;
		// Don't update
		return true;
	}
	void _updateRegs() {
		for (RegUpdate reg in _regUpdates) {
			switch (reg.internalID) {
				«updateRegCases»
			}
		}
	}
	«ENDIF»
	int _srl(int val, int shiftBy, int width){
	  if (val>=0)
	    return val>>shiftBy;
	  int opener=1<<(width);
	  int opened=(val - opener) & (opener - 1);
	  return (opened>>shiftBy);
	}
	int _signExtend(int val, int width) {
	  var msb=(1<<(width-1));
	  var mask=(1<<width)-1;
	  var twoComplement = -val;
	  if ((val&msb)==0){
	    //The MSB is not set, but the stored sign is negative
	    if (val>=0)
	      return val;
	    return twoComplement&mask;
	  }
	  if (val<0)
	    return val;
	  return -(twoComplement&mask);
	}
	'''

	def getImports(boolean usePackageImport) '''
«IF hasClock»	
import 'dart:collection';
«ENDIF»
import 'dart:typed_data';
import 'dart:isolate';
«IF usePackageImport»
import 'package:pshdl_api/simulation_comm.dart';
«ELSE»
import '../simulation_comm.dart';
«ENDIF»
	'''

	override protected stageMethodsHeader(int stage, int stageCosts, boolean constant) '''void _«stageMethodName(
		stage, constant)»(){
		'''

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) '''«nextTime.name»=Math.min(«nextTime.
		name», «currentProcessTime»);'''

	override protected callMethod(CharSequence methodName, CharSequence... args) '''_«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''

	override protected callRunMethod() '''run();
		'''

	override protected checkTestbenchListener() '''«indent()»if (listener!=null && !listener.nextStep(«varByName("$time").
		idName(true, NONE)», stepCount))
«indent()»	break;
'''

	override protected runProcessHeader(CommonCodeGenerator.ProcessData pd) {
		indent++
		'''bool _«processMethodName(pd)»() {
			'''
	}

	override protected runTestbenchHeader() {
		indent++
		'''void runTestbench(int maxTime, int maxSteps, ITestbenchStepListener listener) {
			'''
	}

	override protected fillArray(VariableInformation vi, CharSequence regFillValue) '''«vi.idName(true, NONE)».fillRange(0, «vi.
		arraySize», «regFillValue»);'''

	override protected signExtend(CharSequence op, int targetSizeWithType) {
		if (targetSizeWithType.signedType)
			return '''_signExtend(«op», «targetSizeWithType >> 1»)'''
		return op
	}

	override protected twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
		if (fi.inst === Instruction.srl) {
			return assignTempVar(targetSizeWithType, pos, NONE,
				'''_srl(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)», «fi.arg1»)''', true)
		}
		if (fi.inst === Instruction.div) {
			val CharSequence assignValue = twoOpValue("~/", getCast(targetSizeWithType), leftOperand, rightOperand,
				targetSizeWithType, attributes);
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
		}
		return super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask)
	}

	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<Attributes> attributes, boolean doMask) {
		return assignTempVar(targetSizeWithType, pos, NONE,
			'''pow(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)»)''', true)
	}

	override getHookName() {
		return "Dart"
	}

	override getUsage() {
		val options = new Options;
		options.addOption('p', "pkg", true,
			"The package the generated source will use. If non is specified the package from the module is used")
		return new MultiOption("Options for the " + hookName + " type:", null, options)
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		val moduleName = em.moduleName
		val li = moduleName.lastIndexOf('.')
		var String pkg = null
		val optionPkg = cli.getOptionValue("pkg")
		if (optionPkg !== null) {
			pkg = optionPkg
		} else if (li != -1) {
			pkg = moduleName.substring(0, li - 1)
		}
		val unitName = moduleName.substring(li + 1, moduleName.length);
		doCompile(syntaxProblems, em, pkg, unitName, true);
	}

	def static doCompile(Set<Problem> syntaxProblems, ExecutableModel em, String pkg, String unitName,
		boolean usePackageImport) {
		val comp = new DartCodeGenerator(em, unitName, pkg, usePackageImport, Integer.MAX_VALUE)
		val code = comp.generateMainCode
		val sideFiles = Lists.newArrayList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, sideFiles, em.source,
				comp.hookName, true))
	}

}
