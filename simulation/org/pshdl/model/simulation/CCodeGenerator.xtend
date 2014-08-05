package org.pshdl.model.simulation

import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.VariableInformation
import java.math.BigInteger
import java.util.EnumSet
import org.pshdl.model.simulation.CommonCodeGenerator.Attributes
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.utils.Instruction

class CCodeGenerator extends CommonCodeGenerator implements ICodeGen {
	
	CommonCompilerExtension cce
	
	new(ExecutableModel em, int maxCosts) {
		super(em, 64, maxCosts)
		cce=new CommonCompilerExtension(em, 64)
	}
	
	override protected applyRegUpdates() '''updateRegs();'''
	
	override protected assignArrayInit(VariableInformation hvar, BigInteger initValue, EnumSet<Attributes> attributes) 
	'''«fieldName(hvar, attributes)»[«hvar.arraySize»];'''
	
	override protected arrayInit(VariableInformation varInfo, BigInteger zero, EnumSet<Attributes> attributes) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected callStage(int stage, boolean constant) '''«stageMethodName(stage, constant)»();
		'''
	
	override protected checkRegupdates() '''regUpdatePos!=0'''
	
	override protected clearRegUpdates() '''regUpdatePos=0;'''
	
	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(Attributes.baseType)) {
			if (isBoolean(varInfo, attributes))
				return "bool "
			return "uint64_t "
		} else {
			if (isBoolean(varInfo, attributes))
				return "bool "
			return "uint64_t "
		}
	}
	
	override protected footer() '''
	«helperMethods»
	'''
	
	override protected postFieldDeclarations() '''«IF hasClock»
	bool skipEdge(uint64_t local) {
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
		void «frame.frameName»() {
	'''
	
	override protected header() 
'''
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <stdarg.h>

«IF hasClock»
	typedef struct regUpdate {
		int internal;
		int offset;
		uint64_t fillValue;
	} regUpdate_t;
	
	regUpdate_t regUpdates[«maxRegUpdates»];
	int regUpdatePos=0;

«ENDIF»

'''

def copyRegs() '''
		void updateRegs() {
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
	
	override protected runMethodsFooter(boolean constant) '''}
	'''
	
	override protected runMethodsHeader(boolean constant) '''void «IF !constant»pshdl_sim_run«ELSE»pshdl_sim_initConstants«ENDIF»() {
		'''
	
	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset, boolean force, CharSequence fillValue)'''
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
	
	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) '''void «stageMethodName(
		stage, constant)»(){
		'''
	
	override compile(String packageName, String unitName) {
		return doGenerateMainUnit
	}
	
	override protected getCast(int targetSizeWithType) {
		if (isSignedType(targetSizeWithType))
			return "(int64_t)"
		return ""
	}
	
	override protected twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes) {
		if (fi.inst===Instruction.sra){
			return assignTempVar(targetSizeWithType, pos, attributes, '''((int64_t)«getTempName(leftOperand, NONE)») >> «getTempName(rightOperand, NONE)»''')
		}
		if (fi.inst===Instruction.srl){
			return assignTempVar(targetSizeWithType, pos, attributes, '''«getTempName(leftOperand, NONE)» >> «getTempName(rightOperand, NONE)»''')
		}
		super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes)
	}
	 
	override createChangeAdapter(String packageName, String unitName) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected copyArray(VariableInformation varInfo) '''memcpy(«varInfo.idName(true, EnumSet.of(Attributes.isPrev))», «varInfo.idName(true, NONE)», «varInfo.arraySize»);
	'''
	
	def helperMethods() '''
		void pshdl_sim_setInput(int idx, long value, int arrayIdx[]) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					«IF v.dimensions.length == 0»
						case «varIdx.get(v.name)»: 
							«assignVariable(v, '''«IF v.predicate»value!=0«ELSE»value«ENDIF»''', NONE, true, false)»
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							«idName(v, true, NONE)»[«v.calculateVariableAccessIndexArr»]=«IF v.predicate»value!=0«ELSE»value«ENDIF»;
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
		
		int pshdl_sim_getDeltaCycle(){
			return deltaCycle;
		}
		int pshdl_sim_getVarCount(){
			return «varIdx.size»;
		}
		void pshdl_sim_setDisableEdge(bool enable){
			«IF hasClock»
			«DISABLE_EDGES.name»=enable;
			«ENDIF»
		}
		void pshdl_sim_setDisabledRegOutputlogic(bool enable){
			«IF hasClock»
			«DISABLE_REG_OUTPUTLOGIC.name»=enable;
			«ENDIF»
		}
		
		uint64_t pshdl_sim_getOutput(int idx, int arrayIdx[]) {
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
	
	override protected barrier() 
	'''
	}
	#pragma omp section
	{
	'''
	
	override protected barrierBegin(int stage, int totalStageCosts, boolean createConstant) {
		indent+=2
		'''
		#pragma omp parallel sections
		«indent()»{
		«indent()»#pragma omp section
		«indent()»{
   		'''
   }

	override protected barrierEnd(int stage, int totalStageCosts, boolean createConstant) {
		indent-=2
		'''
			}
		«indent()»}
		'''
	}
	
}