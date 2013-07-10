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
	private ExecutableModel em
	private Map<String, Integer> varIdx = new HashMap
	private Map<String, Integer> intIdx = new HashMap
	private Map<String, Boolean> prevMap = new HashMap
	private boolean hasClock

	new(ExecutableModel em, boolean includeDebug) {
		this.em = em
		for (i : 0 ..< em.variables.length) {
			varIdx.put(em.variables.get(i).name, i)
		}
		for (i : 0 ..< em.internals.length) {
			intIdx.put(em.internals.get(i).fullName, i)
		}
		for (f : em.frames) {
			if (f.edgeNegDepRes != -1)
				prevMap.put(f.edgeNegDepRes.asInternal.info.name, true)
			if (f.edgePosDepRes != -1)
				prevMap.put(f.edgePosDepRes.asInternal.info.name, true)
		}
		this.hasClock = !prevMap.empty
	}

	def static String doCompile(ExecutableModel em, boolean includeDebugListener) {
		return new CCompiler(em, includeDebugListener).compile.toString
	}

	def InternalInformation asInternal(int id) {
		return em.internals.get(id)
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

	def maxRegUpdates(ExecutableModel em) {
		var maxUpdates = 0;
		for (f : em.frames) {
			val oi = f.outputId.asInternal
			if (!oi.info.notNull) {
				for (inst : f.instructions) {
					if (inst.inst === Instruction::writeInternal) {
						if (inst.arg1.asInternal.isShadowReg)
							maxUpdates = maxUpdates + 1;
					}
				}
			} else {
				if (oi.isShadowReg)
					maxUpdates = maxUpdates + 1;
			}
		}
		return maxUpdates
	}

	def predicates(Frame f) {
		val sb = new StringBuilder
		var first = true;
		if (f.edgeNegDepRes !== -1) {
			sb.append(
				'''«f.edgeNegDepRes.asInternal.javaName(false)»_isFalling && !«f.edgeNegDepRes.asInternal.
					javaName(false)»_fallingIsHandled''')
			first = false
		}
		if (f.edgePosDepRes !== -1) {
			if (!first)
				sb.append(' && ')
			sb.append(
				'''«f.edgePosDepRes.asInternal.javaName(false)»_isRising&& !«f.edgePosDepRes.asInternal.javaName(false)»_risingIsHandled''')
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
			uint64_t up«id»=«id.asInternal.info.javaName(false)»_update;
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
			bool «internal.javaName(false)»_isRising=true;
			bool «internal.javaName(false)»_risingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=0) || (t«id»!=1)) {
					«internal.javaName(false)»_isRising=false;
				}
			}
			if (skipEdge(«internal.info.javaName(false)»_update)){
				«internal.javaName(false)»_risingIsHandled=true;
			}
		'''
	}

	def createNegEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			bool «internal.javaName(false)»_isFalling=true;
			bool «internal.javaName(false)»_fallingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=1) || (t«id»!=0)) {
					«internal.javaName(false)»_isFalling=false;
				}
			}
			if (skipEdge(«internal.info.javaName(false)»_update)){
				«internal.javaName(false)»_fallingIsHandled=true;
			}
		'''
	}

	def asMask(int width) {
		val mask = (1l << width) - 1
		if (width == 64)
			return "0xFFFFFFFFFFFFFFFFl";
		return mask.toHexString
	}

	def excludeNull(VariableInformation[] vars) {
		vars.filter[isNotNull]
	}

	def isNotNull(VariableInformation it) {
		name != '#null'
	}

	def excludeNull(InternalInformation[] vars) {
		vars.filter[info.isNotNull]
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
								«v.javaName(false)» = «v.javaName(false)»$reg; break;
							«ELSE»
								if (reg.offset==-1)
									memcpy(«v.javaName(false)», «v.javaName(false)»$reg, «v.totalSize»);
								else
									«v.javaName(false)»[reg.offset] = «v.javaName(false)»$reg[reg.offset]; break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''

	def copyPrev(VariableInformation info) {
		if (info.dimensions.length == 0)
			return '''«info.javaName(true)»=«info.javaName(false)»;'''
		return '''System.arraycopy(«info.javaName(false)»,0,«info.javaName(true)», 0, «info.javaName(false)».length);'''
	}

	def getter(InternalInformation info, boolean prev, int pos, int frameID) {
		val sb = new StringBuilder
		val mask = info.actualWidth.asMask
		for (arr : info.arrayIdx)
			sb.append('''[«arr»]''')
		val arrAcc = if(info.info.dimensions.length == 0) '' else '''[«info.info.arrayAccess(null)» & «info.dimMask.
				toHexString»]'''
		var varName = 't' + pos
		if (info.isPred)
			varName = 'p' + pos
		if (prev)
			varName = varName + "_prev"
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«info.javaType» «varName»=«info.info.javaName(prev)»«sb»;
			«ELSEIF info.actualWidth == 1»
				«info.javaType» «varName»=(«info.info.javaName(prev)»«sb» >> «info.bitStart») & 1;
			«ELSE»
				«info.javaType» «varName»=(«info.info.javaName(prev)»«sb» >> «info.bitEnd») & «mask»;
			«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«info.javaType» «varName»= «info.info.javaName(prev)»«arrAcc»;
			«ELSEIF info.actualWidth == 1»
				«info.javaType» «varName»= («info.info.javaName(prev)»«arrAcc» >> «info.bitStart») & 1;
			«ELSE»
				«info.javaType» «varName»= («info.info.javaName(prev)»«arrAcc» >> «info.bitEnd») & «info.actualWidth.asMask»;
			«ENDIF»
		'''
	}

	def setter(InternalInformation info, String value) {
		val mask = ((1l << info.actualWidth) - 1)
		val maskString = mask.toHexString
		val writeMask = (mask << (info.bitEnd)).bitwiseNot.toHexString
		val varAccess = info.info.arrayAccess(null)
		val off = info.arrayAccess
		var fixedAccess = if (info.arrayIdx.length > 0) '''[«off»]''' else ''''''
		var regSuffix = ''
		if (info.isShadowReg) {
			fixedAccess = "$reg" + fixedAccess
			regSuffix = "$reg"
		}
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.javaType» current=«info.info.javaName(false)»«fixedAccess»;«ENDIF»
				«info.info.javaName(false)»«fixedAccess»=«value»;
			«ELSE»
				«info.info.javaType» current=«info.info.javaName(false)»«fixedAccess» & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»);
				«info.info.javaName(false)»«fixedAccess»=current|«value»;
			«ENDIF»
			«IF info.isShadowReg»
				static regUpdate_t reg;
				if (current!=«value»){
					reg.internal=«varIdx.get(info.info.name)»;
					reg.offset=«off»;
					regUpdates[regUpdatePos++]=reg;
				}
			«ENDIF»
			«IF info.isPred»«info.info.javaName(false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		''' else '''
			int offset=(int)«varAccess»;
			offset&=«dimMask(info).toHexString»;
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.javaType» current=«info.info.javaName(false)»«regSuffix»[offset];«ENDIF»
				«info.info.javaName(false)»«regSuffix»[offset]=«value»;
			«ELSE»
				«info.info.javaType» current=«info.info.javaName(false)»«regSuffix»[offset] & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»;
				«info.info.javaName(false)»«regSuffix»[offset]=current|«value»);
			«ENDIF»
			«IF info.isShadowReg»
				static regUpdate_t reg;
				if (current!=«value»){
					reg.internal=«varIdx.get(info.info.name)»;
					reg.offset=offset;
					regUpdates[regUpdatePos++]=reg;
				}
			«ENDIF»
			«IF info.isPred»«info.info.javaName(false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		'''
	}

	def dimMask(InternalInformation info) {
		val size = info.info.totalSize
		val res = Long::highestOneBit(size)
		if (res == size)
			return res - 1;
		return (res << 1) - 1;
	}

	def arrayAccess(InternalInformation v) {
		val dims = dimsLastOne(v.info)
		var off = 0;
		for (i : (0 ..< v.arrayIdx.length)) {
			val arr = v.arrayIdx.get(i)
			val dim = dims.get(i)
			off = off + (arr * dim)
		}
		return off
	}

	def arrayAccessArrIdx(VariableInformation v) {
		val varAccess = new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0 ..< v.dimensions.length)) {
			val dim = dims.get(i)
			if (dim != 1)
				varAccess.append('''arrayIdx[«i»]*«dim»''')
			else
				varAccess.append('''arrayIdx[«i»]''')
		}
		return varAccess
	}

	def dimsLastOne(VariableInformation v) {
		val dims = new ArrayList(v.dimensions)
		if (dims.size > 0) {
			dims.set(dims.size - 1, 1);
		}
		dims
	}

	def arrayAccess(VariableInformation v, List<Integer> arr) {
		val varAccess = new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0 ..< v.dimensions.length)) {
			val dim = dims.get(i)
			if (i != 0)
				varAccess.append('+')
			val idx=if(arr==null) i else arr.get(i)
			if (dim != 1)
				varAccess.append('''a«idx»*«dim»''')
			else
				varAccess.append('''a«idx»''')
		}
		return varAccess
	}

	def toHexString(long value) '''0x«Long::toHexString(value)»l'''

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

	def getFrameName(Frame f) '''frame«Integer.toHexString(f.uniqueID)»'''

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
					sb.append('''memset(«internal.javaName(false)», t«a», «info.totalSize»);''')
				} else {
					sb.append(
						'''«internal.javaName(false)»«IF info.dimensions.length > 0»[«info.arrayAccess(arr)»]«ENDIF»=t«a»;''')
				}
				arr.clear
				if (internal.isShadowReg) {
					sb.append(
						'\n' + '''
							{
								static regUpdate_t reg;
								reg.internal=«varIdx.get(info.name)»;
								reg.offset=«IF isDynMem»«internal.arrayAccess»«ELSE»-1«ENDIF»;
								regUpdates[regUpdatePos++]=reg;
							}
						''')
				}
			}
			case Instruction::noop:
				sb.append("//Do nothing")
			case Instruction::and:
				sb.append('''uint64_t t«pos»=t«b» & t«a»;''')
			case Instruction::arith_neg:
				sb.append('''uint64_t t«pos»=-t«a»;''')
			case Instruction::bit_neg:
				sb.append('''uint64_t t«pos»=~t«a»;''')
			case Instruction::bitAccessSingle:
				sb.append('''uint64_t t«pos»=(t«a» >> «inst.arg1») & 1;''')
			case Instruction::bitAccessSingleRange: {
				val highBit = inst.arg1
				val lowBit = inst.arg2
				val long mask = (1l << ((highBit - lowBit) + 1)) - 1
				sb.append('''uint64_t t«pos»=(t«a» >> «lowBit») & «mask.toHexString»;''')
			}
			case Instruction::cast_int: {
				if (inst.arg1 != 64) {
					val shiftWidth = 64 - Math::min(inst.arg1, inst.arg2);
					sb.append(
						'''
							int64_t c«pos»=t«a» << «shiftWidth»;
							uint64_t t«pos»=c«pos» >> «shiftWidth»;
						''')
				} else {
					sb.append('''uint64_t t«pos»=t«a»;''')
				}
			}
			case Instruction::cast_uint: {
				if (inst.arg1 != 64) {
					sb.append('''uint64_t t«pos»=t«a» & «inst.arg1.asMask»;''')
				} else {
					sb.append('''uint64_t t«pos»=t«a»;''')
				}
			}
			case Instruction::logiNeg:
				sb.append('''bool t«pos»=!t«a»;''')
			case Instruction::logiAnd:
				sb.append('''bool t«pos»=t«a» && t«b»;''')
			case Instruction::logiOr:
				sb.append('''bool t«pos»=t«a» || t«b»;''')
			case Instruction::const0:
				sb.append('''uint64_t t«pos»=0;''')
			case Instruction::const1:
				sb.append('''uint64_t t«pos»=1;''')
			case Instruction::const2:
				sb.append('''uint64_t t«pos»=2;''')
			case Instruction::constAll1:
				sb.append('''uint64_t t«pos»=«inst.arg1.asMask»;''')
			case Instruction::concat:
				sb.append('''uint64_t t«pos»=(t«b» << «inst.arg2») | t«a»;''')
			case Instruction::loadConstant:
				sb.append('''uint64_t t«pos»=«inst.arg1.constant(f)»;''')
			case Instruction::loadInternal: {
				val internal = inst.arg1.asInternal
				sb.append(internal.getter(false, pos, f.uniqueID))
				arr.clear
			}
			case Instruction::and:
				sb.append('''uint64_t t«pos»=t«b» & t«a»;''')
			case Instruction::or:
				sb.append('''uint64_t t«pos»=t«b» | t«a»;''')
			case Instruction::xor:
				sb.append('''uint64_t t«pos»=t«b» ^ t«a»;''')
			case Instruction::plus:
				sb.append('''uint64_t t«pos»=t«b» + t«a»;''')
			case Instruction::minus:
				sb.append('''uint64_t t«pos»=t«b» - t«a»;''')
			case Instruction::mul:
				sb.append('''uint64_t t«pos»=t«b» * t«a»;''')
			case Instruction::div:
				sb.append('''uint64_t t«pos»=t«b» / t«a»;''')
			case Instruction::sll:
				sb.append('''uint64_t t«pos»=t«b» << t«a»;''')
			case Instruction::srl:
				sb.append('''uint64_t t«pos»=t«b» >> t«a»;''')
			case Instruction::sra:
				sb.append('''uint64_t t«pos»=((int64_t)t«b») >> t«a»;''')
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
					'''«inst.arg1.asInternal.info.javaName(false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
			case Instruction::isFallingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.javaName(false)»_update=((uint64_t) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
		}
		sb.append(
			'''//«inst»
				''')
	}

	def constant(int id, Frame f) '''«f.constants.get(id).longValue.toHexString»'''

	def init(VariableInformation info) {
		if (info.dimensions.length == 0)
			return ''''''
		var size = 1
		for (d : info.dimensions) {
			size = size * d
		}
		'''
			«info.javaName(false)»=new «info.javaType»[«size»];
			«IF info.isRegister»«info.javaName(false)»$reg=new «info.javaType»[«size»];«ENDIF»
		'''
	}

	def getJavaType(InternalInformation ii) {
		val jt = ii.info.javaType
		if (ii.arrayIdx.length != ii.info.dimensions.length)
			return jt + "[]"
		return jt
	}

	def getJavaType(VariableInformation information) {
		if (information.name.startsWith(InternalInformation::PRED_PREFIX))
			return "bool"
		return "uint64_t"
	}

	def javaName(VariableInformation information, boolean prev) {
		return information.name.javaName(prev)
	}

	def javaName(InternalInformation ii, boolean prev) {
		if (ii.fixedArray)
			return ii.fullName.javaName(prev)
		return ii.info.javaName(prev)
	}

	def javaName(String name, boolean prev) {
		var res = name.replaceAll("\\.", "_").replaceAll('\\{', 'Bit').replaceAll('\\}', '').replaceAll(':', 'to').
			replaceAll('\\[', 'arr').replaceAll('\\]', '')
		if (res.startsWith("$"))
			res = res.substring(1)
		if (res.startsWith("#"))
			res = res.substring(1)
		if (prev)
			return res + '_prev'
		return res
	}

	def decl(VariableInformation info, Boolean includePrev) '''
		«IF info.isPredicate || (prevMap.get(info.name) != null && prevMap.get(info.name))»uint64_t «info.javaName(false)»_update=0;«ENDIF»
		«info.javaType» «info.javaName(false)»«IF !info.dimensions.empty»[«info.totalSize»]«ENDIF»;
		«IF includePrev != null && includePrev»«info.javaType» «info.javaName(true)»«IF !info.dimensions.empty»[«info.
			totalSize»]«ENDIF»;«ENDIF»
		«IF info.isRegister»«info.javaType» «info.javaName(false)»$reg«IF !info.dimensions.empty»[«info.totalSize»]«ENDIF»;«ENDIF»
	'''

	def getTotalSize(VariableInformation info) {
		var size = 1
		for (d : info.dimensions) {
			size = size * d
		}
		return size
	}

	def isPredicate(VariableInformation info) {
		info.name.startsWith(InternalInformation::PRED_PREFIX)
	}

	def getImports() '''
#include <stdint.h>
#include <stdbool.h>
#include <string.h>
#include <stdio.h>
#include <time.h>
#include <stdlib.h>
	'''

}
