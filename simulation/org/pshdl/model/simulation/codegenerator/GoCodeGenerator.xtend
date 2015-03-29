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
package org.pshdl.model.simulation.codegenerator

import com.google.common.collect.Lists
import com.google.common.io.ByteStreams
import com.google.common.io.Files
import java.io.File
import java.io.FileOutputStream
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
import org.pshdl.interpreter.utils.Instruction
import org.pshdl.model.simulation.ITypeOuptutProvider
import org.pshdl.model.utils.PSAbstractCompiler
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem

class GoCodeGeneratorParameter extends CommonCodeGeneratorParameter {
	@Option(description="The name of the library that should be declared. If unspecified, the package of the module will be used", optionName="pkg", hasArg=true)
	public String packageName="main";
	@Option(description="The name of the struct. If not specified, the name of the module will be used", optionName="pkg", hasArg=true)
	public String unitName="TestUnit";
	
	public def static nativeRunner(ExecutableModel em){
		new GoCodeGeneratorParameter(em).setPackageName("main").setUnitName("TestUnit")
	}

	new(ExecutableModel em) {
		super(em, 64)
		val moduleName = em.moduleName
		val li = moduleName.lastIndexOf('.')
		this.packageName = null
		if (li != -1) {
			this.packageName = moduleName.substring(0, li - 1)
		}
		this.unitName = moduleName.substring(li + 1, moduleName.length);
	}

	public def GoCodeGeneratorParameter setPackageName(String packageName) {
		this.packageName = packageName
		return this
	}

	public def GoCodeGeneratorParameter setUnitName(String unitName) {
		this.unitName = unitName
		return this
	}

}

class GoCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {

	String pkg
	String unit
	CommonCompilerExtension cce;

	new() {
	}

	new(GoCodeGeneratorParameter parameter) {
		super(parameter)
		this.pkg = parameter.packageName
		this.unit = parameter.unitName.toFirstUpper
		cce = new CommonCompilerExtension(em, 64)
	}

	def public IHDLInterpreterFactory<NativeRunner> createInterpreter(File tempDir) {
		val CharSequence dartCode = generateMainCode();
		val File dutFile = new File(tempDir, "TestUnit.go");
		Files.createParentDirs(dutFile);
		Files.write(dartCode, dutFile, StandardCharsets.UTF_8);
		val File testRunner = new File(tempDir, "runner.go")
		val runnerStream = typeof(CCodeGenerator).getResourceAsStream("/org/pshdl/model/simulation/includes/runner.go")
		val fos = new FileOutputStream(testRunner)
		try {
			ByteStreams.copy(runnerStream, fos)
		} finally {
			fos.close
		}
		val ProcessBuilder goBuilder = new ProcessBuilder("/usr/local/bin/go", "build",
			testRunner.getAbsolutePath(), dutFile.getAbsolutePath()).directory(tempDir).redirectErrorStream(true).inheritIO;
		val Process goCompiler = goBuilder.start();
		if (goCompiler.waitFor != 0) {
			throw new RuntimeException("Compilation of Go Program failed")
		}
		new IHDLInterpreterFactory<NativeRunner>() {
			override newInstance() {
				val runnerExecutable = new File(tempDir, "runner")
				val ProcessBuilder goBuilder = new ProcessBuilder(runnerExecutable.absolutePath).directory(tempDir).
					redirectErrorStream(true);
				val Process goRunner = goBuilder.start();
				return new NativeRunner(goRunner.getInputStream(), goRunner.getOutputStream(), em, goRunner, 5,
					runnerExecutable.absolutePath);
			}
		}
	}

	override protected preFieldDeclarations() '''type «unit» struct {
	varIdx map[string]int

	regUpdates   [«maxRegUpdates»]regUpdate
	regUpdatePos int
	'''

	override protected postFieldDeclarations() '''}
func (s *«unit») updateRegs() {
	for i:=0; i<s.regUpdatePos; i++ {
		reg:=s.regUpdates[i]
		switch reg.internal {
		«updateRegCases»
		}
	}
}
	'''

	override protected String dynamicMask(Integer idx) {
		return "((^(" + shiftLeftDynamic(constant(1, true), idx) + "))-1)";
	}
	override protected shiftRightDynamic(CharSequence tempName, Integer idx) '''«tempName» >> uint(«getTempName(idx, EnumSet.of(Attributes.isArrayIndex))»)'''
	override protected shiftLeftDynamic(CharSequence tempName, Integer idx) '''«tempName» << uint(«getTempName(idx, EnumSet.of(Attributes.isArrayIndex))»)'''

	override protected doLoopStart() '''for {'''

	override protected doLoopEnd(CharSequence condition) '''	if (!(«condition»)) { break }
	}
	'''

	override protected applyRegUpdates() '''s.updateRegs()
		'''

	override protected arrayInit(VariableInformation varInfo, BigInteger initValue,
		EnumSet<CommonCodeGenerator.Attributes> attributes) '''make([]«fieldType(varInfo, attributes)», «varInfo.arraySize»)'''

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callMethod(CharSequence methodName, CharSequence... args) '''«IF inBarrier»wg.Add(1)
«indent()»go «ENDIF»s.«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''

	override protected callRunMethod() '''s.Run()
		'''

	override protected callStage(int stage, boolean constant) '''s.«stageMethodName(stage, constant)»()
		'''

	override protected checkRegupdates() '''s.regUpdatePos != 0'''

	override protected checkTestbenchListener() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected clearRegUpdates() '''s.regUpdatePos = 0
'''

	override protected copyArray(VariableInformation varInfo) '''/* copy array */'''

	override protected fieldType(VariableInformation varInfo, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(CommonCodeGenerator.Attributes.baseType)) {
			if (isBoolean(varInfo, attributes))
				return "bool"
			return "int64"
		} else {
			if (isBoolean(varInfo, attributes))
				return "[]bool"
			return "[]int64"
		}
	}

	override protected doCast(CharSequence cast, CharSequence assignValue) '''«cast»(«assignValue»)'''

	override protected footer() '''
func (s *«unit») SetInputWithName(name string, value int64, arrayIdx ...int) {
	s.SetInput(s.GetIndex(name), value, arrayIdx...)
}
 
func (s *«unit») SetInput(idx int, value int64, arrayIdx ...int) {
	switch idx {
	«setInputCases("value", null, EnumSet.of(Attributes.isArrayArg))»
	default:
		panic("Not a valid index")
	}
}
 
func (s *«unit») GetIndex(name string) int {
	idx, ok := s.varIdx[name]
	if !ok {
		panic("The name:" + name + " is not a valid index")
	}
	return idx
}
 
func (s *«unit») GetName(idx int) string {
	switch idx {
	«FOR VariableInformation vi : em.variables»
	case «vi.getVarIdx(false)»:
		return "«vi.name»"
	«ENDFOR»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *«unit») GetOutputWithName(name string, arrayIdx ...int) int64 {
	return s.GetOutput(s.GetIndex(name), arrayIdx...)
}
 
func (s *«unit») GetOutput(idx int, arrayIdx ...int) int64 {
	switch idx {
	«getOutputCases(null, EnumSet.of(Attributes.isArrayArg))»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *«unit») GetDeltaCycle() int64 {
	return s.deltaCycle
}

func (s *«unit») GetJsonDesc() string {
	return "«cce.JSONDescription»"
}

func (s *«unit») SetDisableEdge(enable bool) {
	s.«DISABLE_EDGES.name» = enable
}
 
func (s *«unit») SetDisableRegOutputLogic(enable bool) {
	s.«DISABLE_REG_OUTPUTLOGIC.name» = enable
}

	'''

	override protected functionFooter(Frame frame) '''}
		'''

	override protected functionHeader(Frame frame) '''func (s *«unit») «frame.frameName» (){
'''

	override protected header() '''package «pkg»
 
type regUpdate struct {
	internal, offset int
	fillValue int64
}
 
func New«unit»() *«unit» {
	return New«unit»WithArgs(false, false)
}
 
func New«unit»WithArgs(«DISABLE_EDGES.name», «DISABLE_REG_OUTPUTLOGIC.name» bool) *«unit» {
	var s = «unit»{
		«DISABLE_EDGES.name»:           «DISABLE_EDGES.name»,
		«DISABLE_REG_OUTPUTLOGIC.name»: «DISABLE_REG_OUTPUTLOGIC.name»,
	}
 
	s.varIdx = make(map[string]int, «em.variables.size»)
	«FOR v : em.variables»
		s.varIdx["«v.name»"] =  «v.getVarIdx(purgeAliases)»
	«ENDFOR»
	«FOR v : em.variables.filter[array]»
		«v.idName(true, NONE)» = make([]int64, «v.arraySize»)
		«IF v.isRegister»
			«v.idName(true, SHADOWREG)» = make([]int64, «v.arraySize»)
		«ENDIF»
 	«ENDFOR»
	return &s
}

func (s *«unit») skipEdge(local int64) bool {
	var dc = int64(uint64(local) >> 16) // zero-extended shift
	if dc < s.«DELTA_CYCLE.name» {
		return false
	}
 
	if (dc == s.«DELTA_CYCLE.name») && ((local & 0xFFFF) == s.«EPS_CYCLE.name») {
		return false
	}
 
	return true
}

func pow(a int64, n int64) int64 {
	var result int64 = 1;
    	var p int64 = a;
	for {
		if (n<=0) {break}
		if ((n % 2) != 0) {
        	    result = result * p;
		}
	        p = p * p;
	        n = n / 2;
	}
	return result
}
	'''

	override protected idName(String name, boolean field, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		val superVal = super.idName(name, field, attributes).toString.replace('$', '__')
		return superVal
	}

	override protected fieldPrefix() '''s.'''

	override protected CharSequence createVarDeclaration(VariableInformation varInfo,
		EnumSet<CommonCodeGenerator.Attributes> attributes, boolean initialize) {
		val StringBuilder sb = new StringBuilder()
		sb.append(preField(varInfo, attributes))
		indent++;
		sb.append(indent())
		indent--;
		sb.append(idName(varInfo, false, attributes)).append(" ")
		sb.append(fieldType(varInfo, attributes))
		sb.append(postField(varInfo))
		return sb;
	}

	override protected constantSuffix() ''''''

	override protected CharSequence inlineVarDecl(VariableInformation varInfo, boolean field,
		EnumSet<CommonCodeGenerator.Attributes> attributes) '''var «idName(varInfo, field, attributes)» «fieldType(varInfo,
		attributes)»'''

	override protected runMethodsFooter(boolean constant) '''}
		'''

	override protected runMethodsHeader(boolean constant) '''func (s *«unit») «IF !constant»Run«ELSE»InitConstants«ENDIF»() {
		'''

	override protected runProcessHeader(CommonCodeGenerator.ProcessData pd) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runTestbenchHeader() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean force, CharSequence fillValue) '''
		«IF !force»if («cpyName»!=«last») «ENDIF»{
		«indent()»	s.regUpdates[s.regUpdatePos] = regUpdate{«outputInternal.varIdx», int(«offset»), «fillValue»}
		«indent()»	s.regUpdatePos++
		«indent()»}
	'''

	override protected doMask(CharSequence currentValue, CharSequence writeMask) {
		return doCast("int64", super.doMask(doCast("uint64", currentValue), writeMask))
	}

	override protected stageMethodsFooter(int stage, int totalStageCosts, boolean constant) '''}
		'''

	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) '''func (s *«unit») «stageMethodName(
		stage, constant)»() {
		'''

	override protected fillArray(VariableInformation vi, CharSequence regFillValue) '''for i := range «idName(vi, true,
		NONE)» { «idName(vi, true, NONE)»[i] = «regFillValue» } '''

	override protected singleOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int a,
		EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
		if (fi.inst === Instruction.bit_neg) {
			val CharSequence assignValue = singleOpValue("^", getCast(targetSizeWithType), a, targetSizeWithType,
				attributes);
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
		}
		super.singleOp(fi, op, targetSizeWithType, pos, a, attributes, doMask)
	}

	override protected twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
		if (fi.inst === Instruction.srl) {
			val CharSequence assignValue = doCast("int64",
				doCast("uint64", getTempName(leftOperand, NONE)) + ">>" +
					doCast("uint64", getTempName(rightOperand, NONE)))
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
		}
		if (fi.inst === Instruction.sra) {
			val CharSequence assignValue = getTempName(leftOperand, NONE) + ">>" +
				doCast("uint64", getTempName(rightOperand, NONE))
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
		}
		if (fi.inst === Instruction.sll) {
			val CharSequence assignValue = doCast("int64",
				getTempName(leftOperand, NONE) + "<<" + doCast("uint64", getTempName(rightOperand, NONE)))
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue, true);
		}
		super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask)
	}

	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
		return assignTempVar(targetSizeWithType, pos, NONE,
			'''pow(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)»)''', true)
	}

	override getHookName() '''Go'''

	override getUsage() {
		val options = new Options;
		options.addOption('p', "pkg", true,
			"The package the generated source will use. If non is specified the package from the module is used")
		return new MultiOption("Options for the " + hookName + " type:", null, options)
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		doCompile(syntaxProblems, new GoCodeGeneratorParameter(em));
	}

	def static doCompile(Set<Problem> syntaxProblems, GoCodeGeneratorParameter parameter) {
		val comp = new GoCodeGenerator(parameter)
		val code = comp.generateMainCode
		val sideFiles = Lists.newArrayList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new PSAbstractCompiler.CompileResult(syntaxProblems, code, parameter.em.moduleName, sideFiles,
				parameter.em.source, comp.hookName, true))
	}

	boolean inBarrier = false

	override protected barrierBegin(int stage, int totalStageCosts, boolean createConstant) {
		inBarrier = true
		return "var wg sync.WaitGroup\n"
	}

	override protected barrierEnd(int stage, int totalStageCosts, boolean createConstant) {
		inBarrier = false
		return "wg.Wait"
	}

}
