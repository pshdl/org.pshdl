/*******************************************************************************
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
 ******************************************************************************/
package org.pshdl.model.simulation.codegenerator

import com.google.common.base.Splitter
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.google.common.io.ByteStreams
import com.google.common.io.Files
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.util.EnumSet
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import java.util.List
import java.util.Map
import java.util.Set
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.FunctionInformation
import org.pshdl.interpreter.IHDLInterpreterFactory
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.NativeRunner
import org.pshdl.interpreter.ParameterInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation.Type
import org.pshdl.interpreter.utils.Instruction
import org.pshdl.model.simulation.ITypeOuptutProvider
import org.pshdl.model.simulation.SimulationTransformationExtension
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel
import org.pshdl.model.types.builtIn.busses.memorymodel.Row
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST
import org.pshdl.model.utils.PSAbstractCompiler.CompileResult
import org.pshdl.model.utils.services.AuxiliaryContent
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem
import org.pshdl.interpreter.NativeRunner.IRunListener

class CCodeGeneratorParameter extends CommonCodeGeneratorParameter {
	new(ExecutableModel em) {
		super(em, 64)
	}
}

class CCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {

	private CommonCompilerExtension cce
	private boolean hasPow = false
	public static String COMPILER = "/usr/bin/clang"

	new() {
	}

	new(CCodeGeneratorParameter parameter) {
		super(parameter)
		this.cce = new CommonCompilerExtension(em, 64)
	}

	def IHDLInterpreterFactory<NativeRunner> createInterpreter(File tempDir, IRunListener listener) {
		val File testCFile = new File(tempDir, "test.c")
		Files.write(generateMainCode, testCFile, StandardCharsets.UTF_8)
		val File testRunner = new File(tempDir, "runner.c")
		copyFile("/org/pshdl/model/simulation/includes/runner.c", testRunner)
//		val File testGenericLib = new File(tempDir, "pshdl_generic_sim.c")
//		copyFile("/org/pshdl/model/simulation/includes/pshdl_generic_sim.c", testGenericLib)
		val File testGenericLibHeader = new File(tempDir, "pshdl_generic_sim.h")
		copyFile("/org/pshdl/model/simulation/includes/pshdl_generic_sim.h", testGenericLibHeader)
		val File executable = new File(tempDir, "testExec")
		writeAuxiliaryContents(tempDir)
		val ProcessBuilder builder = new ProcessBuilder(COMPILER, "-I", tempDir.absolutePath, "-O3",
			testCFile.absolutePath, testRunner.absolutePath, "-o", executable.absolutePath)
		val Process process = builder.directory(tempDir).inheritIO().start()
		process.waitFor()
		val exitValue = process.exitValue()
		if (exitValue != 0) {		
			throw new RuntimeException("Process did not terminate with 0, was "+exitValue)
		}
		return new IHDLInterpreterFactory<NativeRunner>() {
			override newInstance() {
				val ProcessBuilder execBuilder = new ProcessBuilder(executable.getAbsolutePath())
				val Process testExec = execBuilder.directory(tempDir).redirectErrorStream(true).start()
				return new NativeRunner(testExec.getInputStream(), testExec.getOutputStream(), em, testExec, 5,
					executable.getAbsolutePath(), listener)
			}
		}
	}
	
	def copyFile(String fileToCopy, File testRunner) {
		val runnerStream = typeof(CCodeGenerator).getResourceAsStream(fileToCopy)
		val fos = new FileOutputStream(testRunner)
		try {
			ByteStreams.copy(runnerStream, fos)
		} finally {
			runnerStream.close
			fos.close
		}
	}

	override protected applyRegUpdates() '''updateRegs();'''

	override protected assignArrayInit(VariableInformation hvar, BigInteger initValue,
		EnumSet<CommonCodeGenerator.Attributes> attributes) '''«fieldName(hvar, attributes)»[«hvar.arraySize»];'''

	override protected arrayInit(VariableInformation varInfo, BigInteger zero,
		EnumSet<CommonCodeGenerator.Attributes> attributes) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callStage(int stage, boolean constant) '''«stageMethodName(stage, constant)»();
		'''

	override protected checkRegupdates() '''regUpdatePos!=0'''

	override protected clearRegUpdates() '''regUpdatePos=0;'''

	override protected fieldType(VariableInformation varInfo, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		if (varInfo.type==Type.STRING)
			return "char*"
		if (isBoolean(varInfo, attributes))
			return "bool"
		return "uint64_t"
	}

	override protected justDeclare(VariableInformation varInfo, EnumSet<CommonCodeGenerator.Attributes> attributes) '''«fieldName(
		varInfo, attributes)»«IF varInfo.array»[«varInfo.arraySize»]«ENDIF»;'''

	override protected footer() '''
		«helperMethods»
	'''

	override protected postFieldDeclarations() '''«IF hasClock»
	static bool skipEdge(uint64_t local) {
		uint64_t dc = local >> 16l;
		// Register was updated in previous delta cylce, that is ok
		if (dc < deltaCycle)
			return false;
		// Register was updated in this delta cycle but it is the same eps,
		// that is ok as well
		if ((dc == deltaCycle) && ((local & 0xFFFF) == epsCycle))
			return false;
		// Don't update
		return true;
	}
	«copyRegs»
	«ENDIF»
'''

	override protected functionFooter(Frame frame) '''}
		'''

	override protected functionHeader(Frame frame) '''
		static void «frame.frameName»() {
	'''

	override protected header() '''
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include "pshdl_generic_sim.h"
#include "«headerName».h"

«generateInlineMethods»

«IF hasClock»
	/// Don't use this
	typedef struct regUpdate {
		int internal;
		int offset;
		uint64_t fillValue;
	} regUpdate_t;
	
	static regUpdate_t regUpdates[«maxRegUpdates»];
	static int regUpdatePos=0;

«ENDIF»

'''
	def generateInlineMethods() {
		val StringBuilder sb = new StringBuilder
		for (FunctionInformation fi : em.functions) {
				sb.
					append('''extern «fi.returnType.toC» «fi.signature»(«FOR ParameterInformation pi : fi.parameter SEPARATOR ', '»«pi.toC» p«pi.name.toFirstUpper»«ENDFOR»);''')
		}
		return sb
	}
	
	def toC(ParameterInformation information) {
		if (information === null)
			return "void"
		if (information.type === ParameterInformation.Type.PARAM_BOOL)
			return "bool"
		if (information.type === ParameterInformation.Type.PARAM_STRING)
			return "const char*"
		return "uint64_t"
	}
	
	def protected copyRegs() '''
		static void updateRegs() {
			int i;
			for (i=0;i<regUpdatePos; i++) {
				regUpdate_t reg=regUpdates[i];
				switch (reg.internal) {
					«updateRegCases»
				}
			}
		}
	'''

	def static int hash(String str) {
		var int hash = -2128831035;
		val byte[] bytes = str.getBytes(StandardCharsets.ISO_8859_1);
		for (byte b : bytes) {
			hash = hash.bitwiseXor(b);
			hash = hash * 16777619;
		}
		return hash;
	}

	override protected runMethodsFooter(boolean constant) '''}
		'''

	override protected runMethodsHeader(boolean constant) '''void «IF !constant»pshdl_sim_run«ELSE»pshdl_sim_initConstants«ENDIF»() {
		'''

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean force, CharSequence fillValue) '''
		«IF !force»if («cpyName»!=«last»)
		«indent()»	«ENDIF»{
		«indent()»		static regUpdate_t reg;
		«indent()»		reg.internal=«outputInternal.regIdx»;
		«indent()»		reg.offset=(int)(«offset»);
		«indent()»		reg.fillValue=«fillValue»;
		«indent()»		regUpdates[regUpdatePos++]=reg;
		«indent()»	}
	'''

	def regIdx(InternalInformation information) {
		regIdx.get(information.info.name)
	}

	override protected stageMethodsFooter(int stage, int totalStageCosts, boolean constant) '''}
		'''

	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) '''static void «stageMethodName(
		stage, constant)»(){
		'''

	override protected getCast(int targetSizeWithType) {
		if (isSignedType(targetSizeWithType))
			return "(int64_t)"
		return ""
	}

	override protected twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
			val type=typeFromTargetSize(targetSizeWithType)
		switch (fi.inst) {
			case Instruction.sra:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''((int64_t)«getTempName(leftOperand, NONE)») >> «getTempName(rightOperand, NONE)»''', true)
			case Instruction.srl:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''«getTempName(leftOperand, NONE)» >> «getTempName(rightOperand, NONE)»''', true)
			case Instruction.less:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''(int64_t)«getTempName(leftOperand, NONE)» < (int64_t)«getTempName(rightOperand, NONE)»''', true)
			case Instruction.less_eq:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''(int64_t)«getTempName(leftOperand, NONE)» <= (int64_t)«getTempName(rightOperand, NONE)»''', true)
			case Instruction.greater:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''(int64_t)«getTempName(leftOperand, NONE)» > (int64_t)«getTempName(rightOperand, NONE)»''', true)
			case Instruction.greater_eq:
				return assignTempVar(type, targetSizeWithType, pos, attributes,
					'''(int64_t)«getTempName(leftOperand, NONE)» >= (int64_t)«getTempName(rightOperand, NONE)»''', true)
			default: {
			}
		}
		super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes, doMask)
	}

	override protected copyArray(VariableInformation varInfo) '''memcpy(«varInfo.idName(true,
		EnumSet.of(CommonCodeGenerator.Attributes.isPrev))», «varInfo.idName(true, NONE)», «varInfo.arraySize»);
		'''

	override protected preField(VariableInformation x, EnumSet<CommonCodeGenerator.Attributes> attributes) '''«IF !attributes.
		contains(CommonCodeGenerator.Attributes.isPublic)»static«ENDIF» '''

	def protected helperMethods() '''
		void pshdl_sim_setInput(uint32_t idx, uint64_t value) {
			pshdl_sim_setInputArray(idx, value, 0);
		}
		void pshdl_sim_setInputArray(uint32_t idx, uint64_t value, uint32_t offset) {
			switch (idx) {
				«setInputCases("value", null, EnumSet.of(Attributes.useArrayOffset))»
			}
		}
		
		char* pshdl_sim_getName(uint32_t idx) {
			switch (idx) {
				«FOR v : em.variables»
					case «v.getVarIdx(false)»: return "«v.name»";
				«ENDFOR»
			}
			return 0;
		}
		
		static char* jsonDesc="«cce.JSONDescription»";
		char* pshdl_sim_getJsonDesc(){
			return jsonDesc;
		}
		
		uint64_t pshdl_sim_getDeltaCycle(){
			return deltaCycle;
		}
		
		uint32_t pshdl_sim_getVarCount(){
			return «varIdx.size»;
		}
		
		void pshdl_sim_setDisableEdges(bool enable){
			«IF hasClock»
				«DISABLE_EDGES.name»=enable;
			«ENDIF»
		}
		
		void pshdl_sim_setDisableRegOutputlogic(bool enable){
			«IF hasClock»
				«DISABLE_REG_OUTPUTLOGIC.name»=enable;
			«ENDIF»
		}
		
		static uint32_t hash(char* str){
			size_t len=strlen(str);
			uint32_t hash = 2166136261;
			int i;
			for (i=0;i<len;i++){
			   	hash = hash ^ str[i];
			   	hash = hash * 16777619;
			   }
			return hash;
		}
		
		int pshdl_sim_getIndex(char* name) {
			uint32_t hashName=hash(name);
			switch (hashName) {
				«FOR Map.Entry<Integer, List<VariableInformation>> e : em.variables.hashed.entrySet»
					case «constant32Bit(e.key)»:
						«FOR VariableInformation vi : e.value» 
							if (strcmp(name, "«vi.name»") == 0)
								return «vi.getVarIdx(purgeAliases)»;
						«ENDFOR»
						return -1; //so close...
				«ENDFOR»
			default:
				return -1;
			}
		}
		
		uint64_t pshdl_sim_getOutput(uint32_t idx) {
			return pshdl_sim_getOutputArray(idx, 0);
		}
		
		uint64_t pshdl_sim_getOutputArray(uint32_t idx, uint32_t offset) {
			switch (idx) {
				«getOutputCases(null, EnumSet.of(Attributes.useArrayOffset))»
			}
			return 0;
		}
		
		«IF hasPow»
			static uint64_t pshdl_sim_pow(uint64_t a, uint64_t n){
			    uint64_t result = 1;
			    uint64_t p = a;
			    while (n > 0){
			        if ((n % 2) != 0)
			            result = result * p;
			        p = p * p;
			        n = n / 2;
			    }
			    return result;
			}
		«ENDIF»
	'''

	def Map<Integer, List<VariableInformation>> getHashed(Iterable<VariableInformation> informations) {
		val Map<Integer, List<VariableInformation>> res = Maps.newLinkedHashMap()
		for (VariableInformation vi : em.variables) {
			val hashVal = hash(vi.name)
			val list = res.get(hashVal)
			if (list === null) {
				res.put(hashVal, Lists.newArrayList(vi))
			} else {
				list.add(vi)
			}
		}
		return res
	}

	override protected barrier() '''
		}
		#pragma omp section
		{
	'''

	override protected barrierBegin(int stage, int totalStageCosts, boolean createConstant) {
		indent += 2
		'''
			#pragma omp parallel sections
			«indent()»{
			«indent()»#pragma omp section
			«indent()»{
		'''
	}

	override protected barrierEnd(int stage, int totalStageCosts, boolean createConstant) {
		indent -= 2
		'''
				}
			«indent()»}
		'''
	}

	override getAuxiliaryContent() {
		val generic_hStream = typeof(CCodeGenerator).getResourceAsStream(
			"/org/pshdl/model/simulation/includes/pshdl_generic_sim.h")
		try {
			val generic_h = new AuxiliaryContent("pshdl_generic_sim.h", generic_hStream, true)

			val specific_h = new AuxiliaryContent(headerName() + ".h", specificHeader.toString)
			val res = Lists.newArrayList(generic_h, specific_h)
			val simEncapsulation = generateSimEncapsuation
			if (simEncapsulation !== null) {
				res.add(new AuxiliaryContent("simEncapsulation.c", simEncapsulation))
			}
			return res
		} finally {
			generic_hStream.close
		}
	}

	def protected headerName() {
		"pshdl_" + em.moduleName.idName(false, NONE) + "_sim"
	}

	def protected getSpecificHeader() '''/**
 * @file
 * @brief Provides access to all fields and their index.
 */

#ifndef _«headerName»_h_
#define _«headerName»_h_
#include "pshdl_generic_sim.h"

«FOR VariableInformation vi : em.variables»
	///Use this index define to access <tt> «vi.name.replaceAll("\\@", "\\\\@")» </tt> via getOutput/setInput methods
	#define «vi.defineName» «vi.getVarIdx(purgeAliases)»
«ENDFOR»

«fieldDeclarations(false, false).toString.split("\n").map["extern" + it].join("\n")»

#endif
	'''

	def String generateSimEncapsuation() {
		val Unit unit = getUnit(em)
		if (unit === null)
			return null
		return generateSimEncapsuation(unit, MemoryModel.buildRows(unit))
	}

	def getUnit(ExecutableModel model) {
		var Unit unit
		val annoSplitter = Splitter.on(SimulationTransformationExtension.ANNO_VALUE_SEP);
		if (em.annotations !== null) {
			for (a : em.annotations) {
				if (a.startsWith("busDescription")) {
					val value = annoSplitter.limit(2).split(a).last
					unit = MemoryModelAST.parseUnit(value, new LinkedHashSet, 0)
				}
			}
		}
		return unit
	}

	extension BusAccess ba = new BusAccess

	private def generateSimEncapsuation(Unit unit, Iterable<Row> rows) {
		val Set<String> varNames = new LinkedHashSet
		rows.forEach[it.allDefs.filter[it.type !== Definition.Type.UNUSED].forEach[varNames.add(it.getName)]]
		var res = '''
/**
 * @file
 * @brief  Provides methods for simulating accessing to the memory registers
 *
 * This file is a substitue for the BusAccess.c file that is used to access real memory.
 * For each type of row there are methods for setting/getting the values
 * either directly, or as a struct. A memory map overview has been
 * generated into BusMap.html.
 */

#include <stdint.h>
#include <stdbool.h>
#include "BusAccess.h"
#include "BusStdDefinitions.h"
#include "«headerName».h"

/**
 * This method provides a null implementation of the warning functionality. You
 * can use it to provide your own error handling, or you can use the implementation
 * provided in BusPrint.h
 */
static void defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){
}

warnFunc_p warn=defaultWarn;

/**
 * This methods allows the user to set a custom warning function. Usually this is used
 * in conjunction with the implementation provided in BusPrint.h.
 *
 * @param warnFunction the new function to use for error reporting
 *
 * Example Usage:
 * @code
 *    #include "BusPrint.h"
 *    setWarn(defaultPrintfWarn);
 * @endcode
 */
void setWarn(warnFunc_p warnFunction){
    warn=warnFunction;
}

///The index of the Clock that is toggled for each setting
#define «"busclk_idx"» «busIndex»

'''
		val checkedRows = new LinkedHashSet<String>()
		val rowCounts = new LinkedHashMap<String, Integer>()
		for (Row row : rows) {
			val idx = rowCounts.get(row.name);
			if (idx === null)
				rowCounts.put(row.name, 1)
			else
				rowCounts.put(row.name, idx + 1)
		}
		for (Row row : rows) {
			if (!checkedRows.contains(row.name)) {
				if (row.hasWriteDefs)
					res = res + row.simSetter(rowCounts.get(row.name))
				res = res + row.simGetter(rowCounts.get(row.name))
				checkedRows.add(row.name)
			}
		}
		return res
	}

	def protected int getBusIndex() {
		val pclk = varIdx.get(em.moduleName + ".PCLK")
		if (pclk === null)
			return varIdx.get(em.moduleName + ".Bus2IP_Clk")
		return pclk
	}

	def protected getDefineName(VariableInformation vi) '''PSHDL_SIM_«vi.idName(true, NONE).toString.toUpperCase»'''

	def protected getDefineNameString(String s) '''PSHDL_SIM_«(em.moduleName + "." + s).idName(true, NONE).toString.
		toUpperCase»'''

	def protected simGetter(Row row, int rowCount) '''
/**
 * Directly retrieve the fields of row «row.name».
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.
 * @param index the row that you want to access. «IF rowCount == 1»The only valid index is 0«ELSE»Valid values are 0..«rowCount -
		1»«ENDIF»
 «FOR Definition d : row.allDefs»
 * @param «d.name» the value of «d.name» will be written into the memory of this pointer.
 «ENDFOR»
 *
 * @retval 1  Successfully retrieved the values
 * @retval 0  Something went wrong (invalid index for example)
 *
 */
int get«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition definition : row.allDefs»«getParameter(
		row, definition, true)»«ENDFOR»){
	uint32_t offset[1]={index};
	«FOR Definition d : row.allDefs»
	*«row.getVarName(d)»=(«d.busType»)pshdl_sim_getOutputArray(«d.name.getDefineNameString», offset);
	«ENDFOR»
	return 1;
}

/**
 * Retrieve the fields of row «row.name» into the struct.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.
 * @param index the row that you want to access. «IF rowCount == 1»The only valid index is 0«ELSE»Valid values are 0..«rowCount -
		1»«ENDIF»
 * @param result the values of this row will be written into the struct
 *
 * @retval 1  Successfully retrieved the values
 * @retval 0  Something went wrong (invalid index for example)
 *
 */
int get«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *result){
	return get«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.allDefs», &result->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	def protected simSetter(Row row, int rowCount) '''
/**
 * Updates the values in memory from the struct. This also advances the simulation by one clock cycle, 
 * unless PSHDL_SIM_NO_BUSCLK_TOGGLE is defined.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.
 * @param index the row that you want to access. «IF rowCount == 1»The only valid index is 0«ELSE»Valid values are 0..«rowCount -
		1»«ENDIF»
 «FOR Definition d : row.allDefs»
 * @param «d.name» the value of «d.name» will be written into the register. «explain(d)»
 «ENDFOR»
 *
 * @retval 1  Successfully updated the values
 * @retval 0  Something went wrong (invalid index or value exceeds its range for example)
 *
 */
int set«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition definition : row.writeDefs»«getParameter(
		row, definition, false)»«ENDFOR»){
	if (index>«rowCount - 1»)
		return 0;
	uint32_t offset[1]={index};
	«FOR Definition ne : row.writeDefs»
		«row.generateConditions("", ne)»
	«ENDFOR»
	«FOR Definition d : row.writeDefs»
	pshdl_sim_setInputArray(«d.name.getDefineNameString», «d.name», offset);
	«ENDFOR»
	#ifndef PSHDL_SIM_NO_BUSCLK_TOGGLE
	if (!«DISABLE_EDGES.name») {
		pshdl_sim_setInput(busclk_idx, 0);
		pshdl_sim_run();
	}
	pshdl_sim_setInput(busclk_idx, 1);
	pshdl_sim_run();
	#endif
	return 1;
}

/**
 * Updates the values in memory from the struct. This also advances the simulation by one clock cycle, 
 * unless PSHDL_SIM_NO_BUSCLK_TOGGLE is defined.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory. For simulation this parameter is ignored.
 * @param index the row that you want to access. «IF rowCount == 1»The only valid index is 0«ELSE»Valid values are 0..«rowCount -
		1»«ENDIF»
 * @param newVal the values of this row will be written into the struct
 *
 * @retval 1  Successfully updated the values
 * @retval 0  Something went wrong (invalid index or value exceeds range for example)
 *
 */
int set«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *newVal) {
	return set«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.writeDefs», newVal->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callMethod(boolean pshdlFunction, CharSequence methodName, CharSequence... args) '''«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''

	override protected callRunMethod() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected checkTestbenchListener() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runProcessHeader(CommonCodeGenerator.ProcessData pd) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runTestbenchHeader() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override getHookName() {
		return "C"
	}

	override getUsage() {
		val options = new Options;
		return new MultiOption(null, null, options)
	}

	static def List<CompileResult> doCompile(Set<Problem> syntaxProblems, CCodeGeneratorParameter parameter) {
		val comp = new CCodeGenerator(parameter)
		val List<AuxiliaryContent> sideFiles = Lists.newLinkedList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new CompileResult(syntaxProblems, comp.generateMainCode.toString, parameter.em.moduleName, sideFiles,
				parameter.em.source, comp.hookName, true));
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		doCompile(syntaxProblems, new CCodeGeneratorParameter(em))
	}

	override protected fillArray(VariableInformation vi, CharSequence regFillValue) '''memset(«vi.idName(true, NONE)», «regFillValue», «vi.
		arraySize»);'''

	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes, boolean doMask) {
		hasPow = true
		return assignTempVar(typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos, NONE,
			'''pshdl_sim_pow(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)»)''', true)
	}

}
