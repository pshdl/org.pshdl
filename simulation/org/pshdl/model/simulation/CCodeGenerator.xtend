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
package org.pshdl.model.simulation

import com.google.common.base.Splitter
import com.google.common.collect.Lists
import java.math.BigInteger
import java.util.EnumSet
import java.util.HashSet
import java.util.List
import java.util.Set
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.utils.Instruction
import org.pshdl.model.types.builtIn.busses.memorymodel.BusAccess
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition
import org.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel
import org.pshdl.model.types.builtIn.busses.memorymodel.Row
import org.pshdl.model.types.builtIn.busses.memorymodel.Unit
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST
import org.pshdl.model.utils.services.AuxiliaryContent
import org.pshdl.model.validation.Problem
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.utils.PSAbstractCompiler.CompileResult

class CCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {

	CommonCompilerExtension cce

	new() {
	}
	new(ExecutableModel em, int maxCosts) {
		super(em, 64, maxCosts)
		this.cce = new CommonCompilerExtension(em, 64)
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
		if (isBoolean(varInfo, attributes))
			return "bool "
		return "uint64_t "
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

«IF hasClock»
	typedef struct regUpdate {
		int internal;
		int offset;
		uint64_t fillValue;
	} regUpdate_t;
	
	static regUpdate_t regUpdates[«maxRegUpdates»];
	static int regUpdatePos=0;

«ENDIF»

'''

	def protected copyRegs() '''
		static void updateRegs() {
			int i;
			for (i=0;i<regUpdatePos; i++) {
				regUpdate_t reg=regUpdates[i];
				switch (reg.internal) {
					«FOR v : em.variables»
						«IF v.isRegister»
							case «varIdx.get(v.name)»: 
							«IF v.dimensions.length == 0»
								«v.idName(true, NONE)» = «v.idName(true, NONE)»$reg; break;
							«ELSE»
								if (reg.offset!=-1)
									«v.idName(true, NONE)»[reg.offset] = «v.idName(true, NONE)»$reg[reg.offset];
								else
									memset(«v.idName(true, NONE)», reg.fillValue, «v.arraySize»);
								break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''

	override protected writeToNull(String last) '''(void)«last»; //Write to #null
		'''

	override protected runMethodsFooter(boolean constant) '''}
		'''

	override protected runMethodsHeader(boolean constant) '''void «IF !constant»pshdl_sim_run«ELSE»pshdl_sim_initConstants«ENDIF»() {
		'''

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean force, CharSequence fillValue) '''
		«IF !force»if («cpyName»!=«last»)
		«indent()»	«ENDIF»{
		«indent()»		static regUpdate_t reg;
		«indent()»		reg.internal=«outputInternal.varIdx»;
		«indent()»		reg.offset=«offset»;
		«indent()»		reg.fillValue=«fillValue»;
		«indent()»		regUpdates[regUpdatePos++]=reg;
		«indent()»	}
	'''

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
		int rightOperand, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		if (fi.inst === Instruction.sra) {
			return assignTempVar(targetSizeWithType, pos, attributes,
				'''((int64_t)«getTempName(leftOperand, NONE)») >> «getTempName(rightOperand, NONE)»''')
		}
		if (fi.inst === Instruction.srl) {
			return assignTempVar(targetSizeWithType, pos, attributes,
				'''«getTempName(leftOperand, NONE)» >> «getTempName(rightOperand, NONE)»''')
		}
		super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes)
	}

	override protected copyArray(VariableInformation varInfo) '''memcpy(«varInfo.idName(true,
		EnumSet.of(CommonCodeGenerator.Attributes.isPrev))», «varInfo.idName(true, NONE)», «varInfo.arraySize»);
		'''

	override protected preField(VariableInformation x, EnumSet<CommonCodeGenerator.Attributes> attributes) '''«IF !attributes.
		contains(CommonCodeGenerator.Attributes.isPublic)»static«ENDIF» '''

	def protected helperMethods() '''
		void pshdl_sim_setInput(int idx, uint64_t value) {
			pshdl_sim_setInputArray(idx, value, ((void *)0));
		}
		void pshdl_sim_setInputArray(int idx, uint64_t value, int arrayIdx[]) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					«IF v.dimensions.length == 0»
						case «varIdx.get(v.name)»: 
							«assignVariable(v, '''«IF v.predicate»value!=0«ELSE»value«ENDIF»''', NONE, true, false)»
							«IF v.isRegister»
								«assignVariable(v, '''«IF v.predicate»value!=0«ELSE»value«ENDIF»''',
			EnumSet.of(CommonCodeGenerator.Attributes.isShadowReg), true, false)»
							«ENDIF»
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							«idName(v, true, NONE)»[«v.calculateVariableAccessIndexArr»]=«IF v.predicate»value!=0«ELSE»value«ENDIF»;
							«IF v.isRegister»
								«idName(v, true, EnumSet.of(CommonCodeGenerator.Attributes.isShadowReg))»[«v.calculateVariableAccessIndexArr»]=«IF v.
			predicate»value!=0«ELSE»value«ENDIF»;
							«ENDIF»
							break;
					«ENDIF»
				«ENDFOR»
			}
		}
		
		char* pshdl_sim_getName(int idx) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					case «varIdx.get(v.name)»: return "«v.name»";
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
		
		int pshdl_sim_getVarCount(){
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
		
		uint64_t pshdl_sim_getOutput(int idx) {
			return pshdl_sim_getOutputArray(idx, ((void *)0));
		}
		
		uint64_t pshdl_sim_getOutputArray(int idx, int arrayIdx[]) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					«IF v.dimensions.length == 0»
						case «varIdx.get(v.name)»: return «v.idName(true, NONE)»«IF v.predicate»?1:0«ELSEIF v.width != 64» & «v.width.calcMask.constant»«ENDIF»;
					«ELSE»
						case «varIdx.get(v.name)»: return «v.idName(true, NONE)»[«v.calculateVariableAccessIndexArr»]«IF v.width != 64 &&
			!v.predicate» & «v.width.calcMask.constant»«ENDIF»;
					«ENDIF»
				«ENDFOR»
			}
			return 0;
		}	
	'''

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
		val generic_h = new AuxiliaryContent("pshdl_generic_sim.h",
			typeof(CCodeGenerator).getResourceAsStream("/org/pshdl/model/simulation/includes/pshdl_generic_sim.h"), true)
		val specific_h = new AuxiliaryContent(headerName() + ".h", specificHeader.toString)
		val res = Lists.newArrayList(generic_h, specific_h)
		val simEncapsulation = generateSimEncapsuation
		if (simEncapsulation !== null)
			res.add(new AuxiliaryContent("simEncapsulation.c", simEncapsulation))
		return res
	}

	def protected headerName() {
		"pshdl_" + em.moduleName.idName(false, NONE) + "_sim"
	}

	def protected getSpecificHeader() '''
		#ifndef _«headerName»_h_
		#define _«headerName»_h_
		#include "pshdl_generic_sim.h"
		
		«FOR VariableInformation vi : em.variables.excludeNull»
			#define «vi.defineName» «vi.varIdx»
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
					unit = MemoryModelAST.parseUnit(value, new HashSet, 0)
				}
			}
		}
		return unit
	}

	extension BusAccess ba = new BusAccess

	private def generateSimEncapsuation(Unit unit, Iterable<Row> rows) {
		val Set<String> varNames = new HashSet
		rows.forEach[it.allDefs.filter[it.type !== Definition.Type.UNUSED].forEach[varNames.add(it.getName)]]
		var res = '''
//  BusAccessSim.c
//

#include <stdint.h>
#include <stdbool.h>
#include "BusAccess.h"
#include "BusStdDefinitions.h"
#include "pshdl_generic_sim.h"
#include "«headerName».h"

static void defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){
}

warnFunc_p warn=defaultWarn;

void setWarn(warnFunc_p warnFunction){
    warn=warnFunction;
}

#define «"busclk_idx"» «busIndex»

'''
		val checkedRows = new HashSet<String>()
		for (Row row : rows) {
			if (!checkedRows.contains(row.name)) {
				if (row.hasWriteDefs)
					res = res + row.simSetter
				res = res + row.simGetter
				checkedRows.add(row.name)
			}
		}
		return res
	}

	def protected int getBusIndex() {
		val pclk = varIdx.get(em.moduleName + ".PCLK")
		if (pclk === null)
			return varIdx.get(em.moduleName + ".Bus2IP_Clk")
	}

	def protected getDefineName(VariableInformation vi) '''PSHDL_SIM_«vi.idName(true, NONE).toString.toUpperCase»'''

	def protected getDefineNameString(String s) '''PSHDL_SIM_«(em.moduleName + "." + s).idName(true, NONE).toString.
		toUpperCase»'''

	def protected simGetter(Row row) '''
//Getter
int get«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition definition : row.allDefs»«getParameter(
		row, definition, true)»«ENDFOR»){
	int offset[1]={index};
	«FOR Definition d : row.allDefs»
	*«row.getVarName(d)»=(«d.busType»)pshdl_sim_getOutputArray(«d.name.getDefineNameString», offset);
	«ENDFOR»
	return 1;
}

int get«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *result){
	return get«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.allDefs», &result->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	def protected simSetter(Row row) '''
// Setter
int set«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition definition : row.writeDefs»«getParameter(
		row, definition, false)»«ENDFOR»){
	int offset[1]={index};
	«FOR Definition ne : row.writeDefs»
		«row.generateConditions(ne)»
	«ENDFOR»
	«FOR Definition d : row.writeDefs»
	pshdl_sim_setInputArray(«d.name.getDefineNameString», «d.name», offset);
	«ENDFOR»
	if (!«DISABLE_EDGES.name») {
		pshdl_sim_setInput(busclk_idx, 0);
		pshdl_sim_run();
	}
	pshdl_sim_setInput(busclk_idx, 1);
	pshdl_sim_run();
	return 0;
}

int set«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *newVal) {
	return set«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.writeDefs», newVal->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callMethod(String methodName, String... args) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

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

	static def List<CompileResult> doCompile(ExecutableModel em, Set<Problem> syntaxProblems) {
		val comp = new CCodeGenerator(em, Integer.MAX_VALUE)
		val List<AuxiliaryContent> sideFiles = Lists.newLinkedList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new CompileResult(syntaxProblems, comp.generateMainCode.toString, em.moduleName, sideFiles, em.source,
				comp.hookName, true));
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		doCompile(em, syntaxProblems)
	}

}
