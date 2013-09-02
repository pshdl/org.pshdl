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

import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame$FastInstruction
import org.pshdl.interpreter.utils.Instruction
import java.util.Stack
import java.util.Set
import java.util.List
import java.util.LinkedList
import java.util.HashMap
import java.util.Map
import java.util.ArrayList
import java.util.HashSet

class CCompiler {
	private extension CommonCompilerExtension cce
	private final int bitWidth;

	new(ExecutableModel em, boolean includeDebug) {
		this.cce = new CommonCompilerExtension(em)
		if (em.maxDataWidth<=32) {
			bitWidth=32;
		} else {
			bitWidth=64;
		} 
	}

	def static String doCompile(ExecutableModel em, boolean includeDebugListener) {
		return new CCompiler(em, includeDebugListener).compile.toString
	}

	def compile() {
		val Set<Integer> handled = new HashSet
		handled.add(-1)
		'''
			«imports»
			
				«IF hasClock»
					typedef struct regUpdate {
						int internal;
						int offset;
					} regUpdate_t;
					
					regUpdate_t regUpdates[«em.maxRegUpdates»];
					int regUpdatePos=0;
					bool disableEdges;
					bool disabledRegOutputlogic;
				«ENDIF»
				«FOR v : em.variables.excludeNull»
					«v.decl(prevMap.get(v.name))»
				«ENDFOR»
				int epsCycle=0;
				int deltaCycle=0;
				«FOR f : em.frames»
					«f.method»
				«ENDFOR»
				«IF hasClock»
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
				«ENDIF»
				«IF hasClock»
					«copyRegs»
				«ENDIF»
				void run(){
					deltaCycle++;
					«IF hasClock»
						epsCycle=0;
						do {
							regUpdatePos=0;
					«ENDIF»
					«FOR f : em.frames»
						«IF f.edgeNegDepRes === -1 && f.edgePosDepRes === -1 && f.predNegDepRes.length === 0 &&
				f.predPosDepRes.length === 0»
							«f.frameName»();
						«ELSE»
							«f.edgeNegDepRes.createNegEdge(handled)»
							«f.edgePosDepRes.createPosEdge(handled)»
							«FOR p : f.predNegDepRes»
								«p.createboolPred(handled)»
							«ENDFOR»
							«FOR p : f.predPosDepRes»
								«p.createboolPred(handled)»
							«ENDFOR»
							if («f.predicates»)
								«f.frameName»();
						«ENDIF»
					«ENDFOR»
					«IF hasClock»
						updateRegs();
						epsCycle++;
						} while (regUpdatePos!=0 && !disabledRegOutputlogic);
					«ENDIF»
					«FOR v : em.variables.excludeNull.filter[prevMap.get(it.name) != null]»
						«v.copyPrev»
					«ENDFOR»
				}
			
		'''
	}

	def predicates(Frame f) {
		val sb = new StringBuilder
		var first = true;
		if (f.edgeNegDepRes !== -1) {
			sb.append(
				'''«f.edgeNegDepRes.asInternal.idName(false, false)»_isFalling && !«f.edgeNegDepRes.asInternal.
					idName(false, false)»_fallingIsHandled''')
			first = false
		}
		if (f.edgePosDepRes !== -1) {
			if (!first)
				sb.append(' && ')
			sb.append(
				'''«f.edgePosDepRes.asInternal.idName(false, false)»_isRising&& !«f.edgePosDepRes.asInternal.
					idName(false, false)»_risingIsHandled''')
			first = false
		}
		for (p : f.predNegDepRes) {
			if (!first)
				sb.append(' && ')
			sb.append('''!p«p» && p«p»_fresh''')
			first = false
		}
		for (p : f.predPosDepRes) {
			if (!first)
				sb.append(' && ')
			sb.append('''p«p» && p«p»_fresh''')
			first = false
		}
		return sb.toString
	}

	def createboolPred(int id, Set<Integer> handled) {
		if (handled.contains(id))
			return ''''''
		handled.add(id)
		'''
			«id.asInternal.getter(false, id, -1)»
			bool p«id»_fresh=true;
			uint64_t up«id»=«id.asInternal.info.idName(false, false)»_update;
			if ((up«id»>>16 != deltaCycle) || ((up«id»&0xFFFF) != epsCycle)){
				p«id»_fresh=false;
			}
		'''
	}

	def createPosEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			bool «internal.idName(false, false)»_isRising=true;
			bool «internal.idName(false, false)»_risingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=0) || (t«id»!=1)) {
					«internal.idName(false, false)»_isRising=false;
				}
			} else {
				«id.asInternal.getter(false, id, -1)»
				«internal.idName(false, false)»_isRising=t«id»==1;
			}
			if (skipEdge(«internal.info.idName(false, false)»_update)){
				«internal.idName(false, false)»_risingIsHandled=true;
			}
		'''
	}

	def createNegEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			bool «internal.idName(false, false)»_isFalling=true;
			bool «internal.idName(false, false)»_fallingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=1) || (t«id»!=0)) {
					«internal.idName(false, false)»_isFalling=false;
				}
			} else {
				«id.asInternal.getter(false, id, -1)»
				«internal.idName(false, false)»_isFalling=t«id»==0;
			}
			if (skipEdge(«internal.info.idName(false, false)»_update)){
				«internal.idName(false, false)»_fallingIsHandled=true;
			}
		'''
	}

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
								«v.idName(false, false)» = «v.idName(false, false)»$reg; break;
							«ELSE»
								if (reg.offset==-1)
									memcpy(«v.idName(false, false)», «v.idName(false, false)»$reg, «v.totalSize»);
								else
									«v.idName(false, false)»[reg.offset] = «v.idName(false, false)»$reg[reg.offset]; break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''

	def copyPrev(VariableInformation info) {
		if (info.dimensions.length == 0)
			return '''«info.idName(true, false)»=«info.idName(false, false)»;'''
		return '''System.arraycopy(«info.idName(false, false)»,0,«info.idName(true, false)», 0, «info.
			idName(false, false)».length);'''
	}

	def getter(InternalInformation info, boolean prev, int pos, int frameID) {
		val sb = new StringBuilder
		val mask = info.actualWidth.asMaskL
		for (arr : info.arrayIdx)
			sb.append('''[«arr»]''')
		val arrAcc = if (info.info.dimensions.length == 0)
				''
			else '''[«info.info.arrayAccess(null)» & «info.dimMask.toHexStringL»l]'''
		var varName = 't' + pos
		if (info.isPred)
			varName = 'p' + pos
		if (prev)
			varName = varName + "_prev"
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«info.cType» «varName»=«info.info.idName(prev, false)»«sb»;
			«ELSEIF info.actualWidth == 1»
				«info.cType» «varName»=(«info.info.idName(prev, false)»«sb» >> «info.bitStart») & 1;
			«ELSE»
				«info.cType» «varName»=(«info.info.idName(prev, false)»«sb» >> «info.bitEnd») & «mask»;
			«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«info.cType» «varName»= «info.info.idName(prev, false)»«arrAcc»;
			«ELSEIF info.actualWidth == 1»
				«info.cType» «varName»= («info.info.idName(prev, false)»«arrAcc» >> «info.bitStart») & 1;
			«ELSE»
				«info.cType» «varName»= («info.info.idName(prev, false)»«arrAcc» >> «info.bitEnd») & «info.actualWidth.asMaskL»l;
			«ENDIF»
		'''
	}

	def setter(InternalInformation info, String value) {
		val mask = ((1l << info.actualWidth) - 1)
		val maskString = mask.toHexStringL
		val writeMask = (mask << (info.bitEnd)).bitwiseNot.toHexStringL
		val varAccess = info.info.arrayAccess(null)
		val off = info.arrayFixedOffset
		var fixedAccess = if (info.arrayIdx.length > 0) '''[«off»]''' else ''''''
		var regSuffix = ''
		if (info.isShadowReg) {
			fixedAccess = "$reg" + fixedAccess
			regSuffix = "$reg"
		}
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.cType» current=«info.info.idName(false, false)»«fixedAccess»;«ENDIF»
				«info.info.idName(false, false)»«fixedAccess»=«value»;
			«ELSE»
				«info.info.cType» current=«info.info.idName(false, false)»«fixedAccess» & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»);
				«info.info.idName(false, false)»«fixedAccess»=current|«value»;
			«ENDIF»
			«IF info.isShadowReg»
				static regUpdate_t reg;
				if (current!=«value»){
					reg.internal=«varIdx.get(info.info.name)»;
					reg.offset=«off»;
					regUpdates[regUpdatePos++]=reg;
				}
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, false)»_update=((uint64_t) deltaCycle << 16ll) | (epsCycle & 0xFFFF);«ENDIF»
		''' else '''
			int offset=(int)«varAccess»;
			offset&=«dimMask(info).toHexStringL»l;
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.cType» current=«info.info.idName(false, false)»«regSuffix»[offset];«ENDIF»
				«info.info.idName(false, false)»«regSuffix»[offset]=«value»;
			«ELSE»
				«info.info.cType» current=«info.info.idName(false, false)»«regSuffix»[offset] & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»;
				«info.info.idName(false, false)»«regSuffix»[offset]=current|«value»);
			«ENDIF»
			«IF info.isShadowReg»
				static regUpdate_t reg;
				if (current!=«value»){
					reg.internal=«varIdx.get(info.info.name)»;
					reg.offset=offset;
					regUpdates[regUpdatePos++]=reg;
				}
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, false)»_update=((uint64_t) deltaCycle << 16ll) | (epsCycle & 0xFFFF);«ENDIF»
		'''
	}

	def method(Frame frame) {
		var pos = 0
		var arrPos = 0
		val Stack<Integer> stack = new Stack
		val List<Integer> arr = new LinkedList
		val StringBuilder func = new StringBuilder
		for (i : frame.instructions) {
			var int a = 0
			var int b = 0
			if (i.inst.pop > 0)
				a = stack.pop
			if (i.inst.pop > 1)
				b = stack.pop
			if (i.inst.push > 0)
				stack.push(pos)
			if (i.inst === Instruction::pushAddIndex) {
				arr.add(arrPos)
				arrPos = arrPos + 1
			}
			i.toExpression(frame, func, pos, a, b, arr, arrPos)
			if (i.inst !== Instruction::pushAddIndex)
				pos = pos + 1
		}
		val last = "t" + stack.pop
		'''
			void «frame.frameName»() {
				«func»
				«IF (frame.outputId.asInternal.info.name != "#null")»
					«frame.outputId.asInternal.setter(last)»
				«ELSE»
					//Write to #null 
					(void)«last»;
				«ENDIF»
			}
		'''
	}

	def toExpression(FastInstruction inst, Frame f, StringBuilder sb, int pos, int a, int b, List<Integer> arr,
		int arrPos) {
		switch (inst.inst) {
			case Instruction::pushAddIndex:
				sb.append('''int a«arr.last»=(int)t«a»;''')
			case Instruction::writeInternal: {
				val internal = inst.arg1.asInternal
				val info = internal.info
				val isDynMem = arr.size < info.dimensions.length
				if (isDynMem) {
					sb.append('''memset(«internal.idName(false, false)», t«a», «info.totalSize»);''')
				} else {
					sb.append(
						'''«internal.idName(false, false)»«IF info.dimensions.length > 0»[«info.arrayAccess(arr)»]«ENDIF»=t«a»;''')
				}
				if (internal.isShadowReg) {
					sb.append(
						'\n' + '''
							{
								static regUpdate_t reg;
								reg.internal=«varIdx.get(info.name)»;
								reg.offset=«IF !isDynMem && internal.info.array»«internal.info.arrayAccess(arr)»«ELSE»-1«ENDIF»;
								regUpdates[regUpdatePos++]=reg;
							}
						''')
				}
				arr.clear
			}
			case Instruction::noop:
				sb.append("//Do nothing")
			case Instruction::bitAccessSingle:
				sb.append('''«pos.uTemp('t')»=(t«a» >> «inst.arg1») & 1;''')
			case Instruction::bitAccessSingleRange: {
				val highBit = inst.arg1
				val lowBit = inst.arg2
				val long mask = (1l << ((highBit - lowBit) + 1)) - 1
				sb.append('''«pos.uTemp('t')»=(t«a» >> «lowBit») & «mask.toHexStringL»l;''')
			}
			case Instruction::cast_int: {
				if (inst.arg1 != bitWidth) {
					val shiftWidth = bitWidth - Math::min(inst.arg1, inst.arg2);
					sb.append(
						'''
							«pos.sTemp('c')»=t«a» << «shiftWidth»;
							«pos.uTemp('t')»=c«pos» >> «shiftWidth»;
						''')
				} else {
					sb.append('''«pos.uTemp('t')»=t«a»;''')
				}
			}
			case Instruction::cast_uint: {
				if (inst.arg1 != bitWidth) {
					sb.append('''«pos.uTemp('t')»=t«a» & «inst.arg1.asMaskL»l;''')
				} else {
					sb.append('''«pos.uTemp('t')»=t«a»;''')
				}
			}
			case Instruction::logiNeg:
				sb.append('''bool t«pos»=!t«a»;''')
			case Instruction::logiAnd:
				sb.append('''bool t«pos»=t«a» && t«b»;''')
			case Instruction::logiOr:
				sb.append('''bool t«pos»=t«a» || t«b»;''')
			case Instruction::const0:
				sb.append('''«pos.uTemp('t')»=0;''')
			case Instruction::const1:
				sb.append('''«pos.uTemp('t')»=1;''')
			case Instruction::const2:
				sb.append('''«pos.uTemp('t')»=2;''')
			case Instruction::constAll1:
				sb.append('''«pos.uTemp('t')»=«inst.arg1.asMaskL»l;''')
			case Instruction::concat:
				sb.append('''«pos.uTemp('t')»=(t«b» << «inst.arg2») | t«a»;''')
			case Instruction::loadConstant:
				if (bitWidth==32)
					sb.append('''«pos.uTemp('t')»=«inst.arg1.constantI(f)»;''')
				else
					sb.append('''«pos.uTemp('t')»=«inst.arg1.constantL(f)»l;''')
			case Instruction::loadInternal: {
				val internal = inst.arg1.asInternal
				sb.append(internal.getter(false, pos, f.uniqueID))
				arr.clear
			}
			case Instruction::arith_neg:
				sb.append('''«pos.uTemp('t')»=«singleOpValue('-', '''(«int_t()»)''', a,inst.arg1)»;''')
			case Instruction::bit_neg:
				sb.append('''«pos.uTemp('t')»=«singleOpValue('~', '''(«int_t()»)''', a,inst.arg1)»;''')
			case Instruction::and:
				sb.append(twoOp('&', inst.arg1, pos, a, b))
			case Instruction::or:
				sb.append(twoOp('|', inst.arg1, pos, a, b))
			case Instruction::xor:
				sb.append(twoOp('^', inst.arg1, pos, a, b))
			case Instruction::plus:
				sb.append(twoOp('+', inst.arg1, pos, a, b))
			case Instruction::minus:
				sb.append(twoOp('-', inst.arg1, pos, a, b))
			case Instruction::mul:
				sb.append(twoOp('*', inst.arg1, pos, a, b))
			case Instruction::div:
				sb.append(twoOp('/', inst.arg1, pos, a, b))
			case Instruction::sll:
				sb.append(twoOp('<<', inst.arg1, pos, a, b))
			case Instruction::srl:
				sb.append(twoOp('>>', inst.arg1, pos, a, b))
			case Instruction::sra:
				sb.append('''«pos.uTemp('t')»=«sra(inst.arg1, a,b)»;''')
			case Instruction::eq:
				sb.append('''bool t«pos»=t«b» == t«a»;''')
			case Instruction::not_eq:
				sb.append('''bool t«pos»=t«b» != t«a»;''')
			case Instruction::less:
				sb.append('''bool t«pos»=t«b» < t«a»;''')
			case Instruction::less_eq:
				sb.append('''bool t«pos»=t«b» <= t«a»;''')
			case Instruction::greater:
				sb.append('''bool t«pos»=t«b» > t«a»;''')
			case Instruction::greater_eq:
				sb.append('''bool t«pos»=t«b» >= t«a»;''')
			case Instruction::isRisingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.idName(false, false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
			case Instruction::isFallingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.idName(false, false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
		}
		sb.append(
			'''//«inst»
				''')
	}
	
	def uTemp(int pos, String name) '''«uint_t()» «name»«pos»'''
	def sTemp(int pos, String name) '''«int_t()» «name»«pos»'''
	def uint_t() '''uint«bitWidth»_t'''
	def int_t() '''int«bitWidth»_t'''
	
	def sra(int targetSizeWithType, int a, int b) {
		val targetSize=(targetSizeWithType>>1)
		val shift=bitWidth-targetSize
		if ((targetSizeWithType.bitwiseAnd(1))===1)
			return signExtend('''((«int_t()»)t«b») >> t«a»''',  '''(«int_t()»)''', shift)
		return '''(((«int_t»)t«b») >> t«a») & «targetSize.asMaskL»l'''
	}
	
	def String twoOp(String op, int targetSizeWithType, int pos, int a, int b) 
		'''«pos.uTemp('t')»=«twoOpValue(op,  '''(«int_t()»)''', a, b, targetSizeWithType)»;'''

	def init(VariableInformation info) {
		if (info.dimensions.length == 0)
			return ''''''
		var size = 1
		for (d : info.dimensions) {
			size = size * d
		}
		'''
			«info.idName(false, false)»=new «info.cType»[«size»];
			«IF info.isRegister»«info.idName(false, false)»$reg=new «info.cType»[«size»];«ENDIF»
		'''
	}

	def cType(InternalInformation ii) {
		val jt = ii.info.cType
		if (ii.arrayIdx.length != ii.info.dimensions.length)
			return jt + "[]"
		return jt
	}

	def cType(VariableInformation information) {
		if (information.name.startsWith(InternalInformation::PRED_PREFIX))
			return "bool"
		return uint_t()
	}

	def decl(VariableInformation info, Boolean includePrev) '''
		«IF info.isPredicate || (prevMap.get(info.name) != null && prevMap.get(info.name))»uint64_t «info.idName(false,
			false)»_update=0;«ENDIF»
		«info.cType» «info.idName(false, false)»«IF !info.dimensions.empty»[«info.totalSize»]«ENDIF»;
		«IF includePrev != null && includePrev»«info.cType» «info.idName(true, false)»«IF !info.dimensions.empty»[«info.
			totalSize»]«ENDIF»;«ENDIF»
		«IF info.isRegister»«info.cType» «info.idName(false, false)»$reg«IF !info.dimensions.empty»[«info.totalSize»]«ENDIF»;«ENDIF»
	'''

	def getImports() '''
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
	'''

}
