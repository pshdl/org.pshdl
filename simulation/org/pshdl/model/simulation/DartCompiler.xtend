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
import org.pshdl.interpreter.VariableInformation$Direction
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
import java.math.BigInteger

class DartCompiler {

	private extension CommonCompilerExtension cce

	new(ExecutableModel em) {
		this.cce = new CommonCompilerExtension(em)
	}

	def static String doCompile(ExecutableModel em, String unitName) {
		return new DartCompiler(em).compile(unitName).toString
	}

	def compile(String unitName) {
		val Set<Integer> handled = new HashSet
		handled.add(-1)
		'''
			«imports»
			void main(){
			  «IF hasClock»
			  	handleReceive((e,l) => new «unitName»(e,l));
			  «ELSE»
			  	handleReceive((e,l) => new «unitName»());
			  «ENDIF»
			}
			«IF hasClock»
				class RegUpdate {
					final int internalID;
					final int offset;
					
					RegUpdate(this.internalID, this.offset);
					
					int get hashCode {
						final int prime = 31;
						int result = 1;
						result = (prime * result) + internalID;
						result = (prime * result) + offset;
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
						return true;
					}
				}
				
			«ENDIF»
			class «unitName» implements DartInterpreter{
				«IF hasClock»
					Set<RegUpdate> _regUpdates=new HashSet<RegUpdate>();
					final bool _disableEdges;
					final bool _disabledRegOutputlogic;
				«ENDIF»
				«FOR v : em.variables.excludeNull»
					«v.decl(prevMap.get(v.name))»
				«ENDFOR»
				int _epsCycle=0;
				int _deltaCycle=0;
				int get updateStamp=>(_deltaCycle << 16) | (_epsCycle & 0xFFFF);
				Map<String, int> _varIdx={
					«FOR v : em.variables SEPARATOR ','»
						"«v.name.replaceAll("[\\$]", "\\\\\\$")»": «varIdx.get(v.name)»
					«ENDFOR»
				};
				
				List<String> get names=>_varIdx.keys.toList();
				
				«unitName»(«IF hasClock»this._disableEdges, this._disabledRegOutputlogic«ENDIF»);
				
				«FOR v : em.variables.excludeNull»
					set «v.idName(false, false)»(«v.dartType(true)» value) =>
						«v.idName(false, true)»=value «IF !v.predicate && !v.array»& «v.width.asMask»«ENDIF»;
					
					«v.dartType(true)» get «v.idName(false, false)» =>
						«v.idName(false, true)» «IF !v.predicate && !v.array»& «v.width.asMask»«ENDIF»;
					
					«IF v.array»
						void set «v.idName(false, false)»(«v.dartType(false)» value«FOR i : (0 ..< v.dimensions.size)», int a«i»«ENDFOR») {
							«v.idName(false, true)»«v.arrayAccessBracket(null)»=value & «v.width.asMask»;
						}
						
						«v.dartType(false)» get «v.idName(false, false)»(«FOR i : (0 ..< v.dimensions.size) SEPARATOR ','»int a«i»«ENDFOR») {
							return «v.idName(false, true)»«v.arrayAccessBracket(null)» & «v.width.asMask»;
						}
						
					«ENDIF»
				«ENDFOR»
				«FOR f : em.frames»
					«f.method»
				«ENDFOR»
				«IF hasClock»
					bool skipEdge(int local) {
						int dc = local >> 16;
						// Register was updated in previous delta cylce, that is ok
						if (dc < deltaCycle)
							return false;
						// Register was updated in this delta cycle but it is the same eps,
						// that is ok as well
						if ((dc == _deltaCycle) && ((local & 0xFFFF) == _epsCycle))
							return false;
						// Don't update
						return true;
					}
				«ENDIF»
				void run(){
					_deltaCycle++;
					«IF hasClock»
						_epsCycle=0;
						do {
							_regUpdates.clear();
					«ENDIF»
					«FOR f : em.frames»
						«IF f.edgeNegDepRes === -1 && f.edgePosDepRes === -1 && f.predNegDepRes.length === 0 &&
				f.predPosDepRes.length === 0»
							_frame«f.uniqueID»();
						«ELSE»
							«f.edgeNegDepRes.createNegEdge(handled)»
							«f.edgePosDepRes.createPosEdge(handled)»
							«FOR p : f.predNegDepRes»
								«p.createBooleanPred(handled)»
							«ENDFOR»
							«FOR p : f.predPosDepRes»
								«p.createBooleanPred(handled)»
							«ENDFOR»
							if («f.predicates»)
								_frame«f.uniqueID»();
						«ENDIF»
					«ENDFOR»
					«IF hasClock»
						_updateRegs();
						_epsCycle++;
						} while (!_regUpdates.isEmpty && !_disabledRegOutputlogic);
					«ENDIF»
					«FOR v : em.variables.excludeNull.filter[prevMap.get(it.name) != null]»
						«v.copyPrev»
					«ENDFOR»
				}
				«IF hasClock»
					«copyRegs»
				«ENDIF»
				«hdlInterpreter»
			}
		'''
	}

	def predicates(Frame f) {
		val sb = new StringBuilder
		var first = true;
		if (f.edgeNegDepRes !== -1) {
			sb.append(
				'''«f.edgeNegDepRes.asInternal.idName(false, true)»_isFalling && !«f.edgeNegDepRes.asInternal.
					idName(false, true)»_fallingIsHandled''')
			first = false
		}
		if (f.edgePosDepRes !== -1) {
			if (!first)
				sb.append(' && ')
			sb.append(
				'''«f.edgePosDepRes.asInternal.idName(false, true)»_isRising&& !«f.edgePosDepRes.asInternal.
					idName(false, true)»_risingIsHandled''')
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

	def createBooleanPred(int id, Set<Integer> handled) {
		if (handled.contains(id))
			return ''''''
		handled.add(id)
		'''
			«id.asInternal.getter(false, id, -1)»
			bool p«id»_fresh=true;
			int up«id»=«id.asInternal.info.idName(false, true)»_update;
			if ((up«id»>>16 != _deltaCycle) || ((up«id»&0xFFFF) != _epsCycle)){
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
			bool «internal.idName(false, true)»_isRising=true;
			bool «internal.idName(false, true)»_risingIsHandled=false;
			if (!_disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=0) || (t«id»!=1)) {
					«internal.idName(false, true)»_isRising=false;
				}
			}
			if (skipEdge(«internal.info.idName(false, true)»_update)){
				«internal.idName(false, true)»_risingIsHandled=true;
			}
		'''
	}

	def createNegEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			bool «internal.idName(false, true)»_isFalling=true;
			bool «internal.idName(false, true)»_fallingIsHandled=false;
			if (!_disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=1) || (t«id»!=0)) {
					«internal.idName(false, true)»_isFalling=false;
				}
			}
			if (skipEdge(«internal.info.idName(false, true)»_update)){
				«internal.idName(false, true)»_fallingIsHandled=true;
			}
		'''
	}

	def hdlInterpreter() '''
		
		void setVar(int idx, dynamic value) {
			switch (idx) {
				«FOR v : em.variables»
					«IF !v.^null»
						case «varIdx.get(v.name)»: 
							«v.idName(false, false)»=value«IF v.predicate»==0?false:true«ENDIF»;
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							break;
					«ENDIF»
				«ENDFOR»
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
		
		dynamic getVar(int idx) {
			switch (idx) {
				«FOR v : em.variables»
					«IF v.predicate»
						case «varIdx.get(v.name)»: return «v.idName(false, false)»?1:0;
					«ELSEIF v.isNull»
						case «varIdx.get(v.name)»: return 0;
					«ELSE»
						case «varIdx.get(v.name)»: return «v.idName(false, false)»;
					«ENDIF»
				«ENDFOR»
				default:
					throw new ArgumentError("Not a valid index: $idx");
			}
		}
		
		int get deltaCycle =>_deltaCycle;
		
		int get varNum => «varIdx.size»;
		
		«description»
	'''

	def getDescription() {

		'''
			Description get description=>new Description(
				[
				«FOR v : em.variables.filter[dir === Direction::IN] SEPARATOR ','»
					«v.asPort»
				«ENDFOR»
				],
				[
				«FOR v : em.variables.filter[dir === Direction::INOUT] SEPARATOR ','»
					«v.asPort»
				«ENDFOR»
				],
				[
				«FOR v : em.variables.filter[dir === Direction::OUT] SEPARATOR ','»
					«v.asPort»
				«ENDFOR»
				],
				[
				«FOR v : em.variables.filter[dir === Direction::INTERNAL] SEPARATOR ','»
					«v.asPort»
				«ENDFOR»
				], _varIdx);
		'''
	}

	def asPort(VariableInformation v) {
		var dims = ""
		if (v.array) {
			dims = ''', dimensions: [«FOR i : v.dimensions SEPARATOR ','»«i»«ENDFOR»]'''
		}
		val clock = if(v.isClock) ", clock:true" else ""
		val reset = if(v.isReset) ", reset:true" else ""
		'''new Port(«varIdx.get(v.name)», "«v.name.replaceAll("[\\$]", "\\\\\\$")»", «v.width»«dims»«clock»«reset»)'''
	}

	def copyRegs() '''
		void _updateRegs() {
			for (RegUpdate reg in _regUpdates) {
				switch (reg.internalID) {
					«FOR v : em.variables»
						«IF v.isRegister»
							case «varIdx.get(v.name)»: 
							«IF !v.array»
								«v.idName(false, true)» = «v.idName(false, true)»$reg; break;
							«ELSE»
								«v.idName(false, true)»[reg.offset] = «v.idName(false, true)»$reg[reg.offset]; break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''

	def copyPrev(VariableInformation info) {
		if (!info.array)
			return '''«info.idName(true, true)»=«info.idName(false, true)»;'''
		return '''System.arraycopy(«info.idName(false, true)»,0,«info.idName(true, true)», 0, «info.idName(false,
			true)».length);'''
	}

	def getter(InternalInformation info, boolean prev, int pos, int frameID) {
		val sb = new StringBuilder
		val mask = info.actualWidth.asMask
		for (arr : info.arrayIdx)
			sb.append('''[«arr»]''')
		val arrAcc = '''«FOR int i : (0 ..< info.arrayIdx.length) BEFORE '[' SEPARATOR '][' AFTER ']'»a«i»«ENDFOR»'''
		var varName = 't' + pos
		if (info.isPred)
			varName = 'p' + pos
		if (prev)
			varName = varName + "_prev"
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«info.dartType» «varName»=«info.info.idName(prev, true)»«sb»;
			«ELSEIF info.actualWidth == 1»
				«info.dartType» «varName»=(«info.info.idName(prev,true)»«sb» >> «info.bitStart») & 1;
			«ELSE»
				«info.dartType» «varName»=(«info.info.idName(prev, true)»«sb» >> «info.bitEnd») & «mask»;
			«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«info.dartType» «varName»= «info.info.idName(prev, true)»«arrAcc»;
			«ELSEIF info.actualWidth == 1»
				«info.dartType» «varName»= («info.info.idName(prev,true)»«arrAcc» >> «info.bitStart») & 1;
			«ELSE»
				«info.dartType» «varName»= («info.info.idName(prev, true)»«arrAcc» >> «info.bitEnd») & «info.actualWidth.asMask»;
			«ENDIF»
		'''
	}

	def setter(InternalInformation info, String value) {

		//TODO Fix this!
		val mask = ((1bi.shiftLeft(info.actualWidth)) - 1bi)
		val maskString = mask.toHexString
		val subMask = mask.shiftLeft(info.bitEnd);
		val fullMask = ((1bi.shiftLeft(info.info.width)) - 1bi)
		val writeMask = fullMask.xor(subMask).toHexString
		val off = info.arrayFixedOffset
		var fixedAccess = if (info.arrayIdx.length > 0) '''[«off»]''' else ''''''
		var regSuffix = ''
		if (info.isShadowReg) {
			fixedAccess = "$reg" + fixedAccess
			regSuffix = "$reg"
		}
		if (info.fixedArray) '''
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.dartType(false)» current=«info.info.idName(false, true)»«fixedAccess»;«ENDIF»
				«info.info.idName(false, true)»«fixedAccess»=«value»;
			«ELSE»
				«info.info.dartType(false)» current=«info.info.idName(false, true)»«fixedAccess» & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»);
				«info.info.idName(false, true)»«fixedAccess»=current|«value»;
			«ENDIF»
			«IF info.isShadowReg»
				if (current!=«value»)
					_regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», «off»));
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, true)»_update=updateStamp;«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.dartType(false)» current=«info.info.idName(false, true)»«regSuffix»«info.info.
				arrayAccessBracket(null)»;«ENDIF»
				«info.info.idName(false, true)»«regSuffix»«info.info.arrayAccessBracket(null)»=«value»;
			«ELSE»
				«info.info.dartType(false)» current=«info.info.idName(false, true)»«regSuffix»«info.info.
				arrayAccessBracket(null)» & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»;
				«info.info.idName(false, true)»«regSuffix»«info.info.arrayAccessBracket(null)»=current|«value»);
			«ENDIF»
			«IF info.isShadowReg»
				if (current!=«value»)
					_regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», «info.info.arrayAccess(null)»));
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, true)»_update=updateStamp;«ENDIF»
		'''
	}

	def method(Frame frame) {
		val StringBuilder sb = new StringBuilder
		sb.append(
			'''
				void _frame«frame.uniqueID»() {
			''')

		var pos = 0
		var arrPos = 0
		val Stack<Integer> stack = new Stack
		val List<Integer> arr = new LinkedList
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
			i.toExpression(frame, sb, pos, a, b, arr, arrPos)
			if (i.inst !== Instruction::pushAddIndex)
				pos = pos + 1
		}
		val last = "t" + stack.pop
		if (frame.outputId.asInternal.info.name != "#null")
			sb.append(frame.outputId.asInternal.setter(last))
		else
			sb.append(
				'''//Write to #null
					''')
		sb.append(
			'''
				}
			''')
		return sb.toString
	}

	def toExpression(FastInstruction inst, Frame f, StringBuilder sb, int pos, int a, int b, List<Integer> arr,
		int arrPos) {
		switch (inst.inst) {
			case Instruction::pushAddIndex:
				sb.append('''int a«arr.last»=t«a»;''')
			case Instruction::writeInternal: {
				val internal = inst.arg1.asInternal
				var name = (internal).idName(false, true);
				if (arr.size < inst.arg1.asInternal.info.dimensions.length) {
					sb.append(
						'''
							«name».fillRange(0, «name».length, t«a»);
							_regUpdates.add(new RegUpdate(«varIdx.get(internal.info.name)», -1));
						''')
				} else {
					sb.append(
						'''
							«name»«internal.info.arrayAccessBracket(arr)»=t«a»;
							_regUpdates.add(new RegUpdate(«varIdx.get(internal.info.name)», «internal.info.arrayAccess(arr)»));
						''')
					arr.clear
				}
			}
			case Instruction::noop:
				sb.append("//Do nothing")
			case Instruction::and:
				sb.append('''int t«pos»=t«b» & t«a»;''')
			case Instruction::arith_neg:
				sb.append('''int t«pos»=-t«a»;''')
			case Instruction::bit_neg:
				sb.append('''int t«pos»=~t«a»;''')
			case Instruction::bitAccessSingle:
				sb.append('''int t«pos»=(t«a» >> «inst.arg1») & 1;''')
			case Instruction::bitAccessSingleRange: {
				val highBit = inst.arg1
				val lowBit = inst.arg2
				val mask = ((highBit - lowBit) + 1).asMask
				sb.append('''int t«pos»=(t«a» >> «lowBit») & «mask»;''')
			}
			case Instruction::cast_int: {
				val targetWidth = inst.arg1;
				val currWidth = inst.arg2;
				if (targetWidth >= currWidth) {
					sb.append('''int t«pos»=t«a»;''')
				} else {
					val mask = BigInteger.ONE.shiftLeft(targetWidth).subtract(BigInteger.ONE);
					sb.append(
						'''
							int u«pos» = t«a» & «mask»;
							if ((u«pos» & «1bi.shiftLeft(targetWidth - 1).toHexString») != 0)) { // MSB is set
								if (u«pos» > 0) {
									u«pos» = -u«pos»;
								}
							} else {
								if (u«pos» < 0) {
									u«pos» = -u«pos»;
								}
							}
							t«pos» = u«pos»;
						''')
				}
			}
			case Instruction::cast_uint: {
				sb.append('''int t«pos»=t«a» & «inst.arg1.asMask»;''')
			}
			case Instruction::logiNeg:
				sb.append('''bool t«pos»=!t«a»;''')
			case Instruction::logiAnd:
				sb.append('''bool t«pos»=t«a» && t«b»;''')
			case Instruction::logiOr:
				sb.append('''bool t«pos»=t«a» || t«b»;''')
			case Instruction::const0:
				sb.append('''int t«pos»=0;''')
			case Instruction::const1:
				sb.append('''int t«pos»=1;''')
			case Instruction::const2:
				sb.append('''int t«pos»=2;''')
			case Instruction::constAll1:
				sb.append('''int t«pos»=«inst.arg1.asMask»;''')
			case Instruction::concat:
				sb.append('''int t«pos»=(t«b» << «inst.arg2») | t«a»;''')
			case Instruction::loadConstant:
				sb.append('''int t«pos»=«inst.arg1.constant(f)»;''')
			case Instruction::loadInternal: {
				val internal = inst.arg1.asInternal
				sb.append(internal.getter(false, pos, f.uniqueID))
				arr.clear
			}
			case Instruction::and:
				sb.append('''int t«pos»=t«b» & t«a»;''')
			case Instruction::or:
				sb.append('''int t«pos»=t«b» | t«a»;''')
			case Instruction::xor:
				sb.append('''int t«pos»=t«b» ^ t«a»;''')
			case Instruction::plus:
				sb.append('''int t«pos»=t«b» + t«a»;''')
			case Instruction::minus:
				sb.append('''int t«pos»=t«b» - t«a»;''')
			case Instruction::mul:
				sb.append('''int t«pos»=t«b» * t«a»;''')
			case Instruction::div:
				sb.append('''int t«pos»=t«b» / t«a»;''')
			case Instruction::sll:
				sb.append('''int t«pos»=t«b» << t«a»;''')
			case Instruction::srl:
				sb.append('''int t«pos»=t«b» >>> t«a»;''')
			case Instruction::sra:
				sb.append('''int t«pos»=t«b» >> t«a»;''')
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
				sb.append('''«inst.arg1.asInternal.info.idName(false, true)»_update=updateStamp;''')
			case Instruction::isFallingEdge:
				sb.append('''«inst.arg1.asInternal.info.idName(false, true)»_update=updateStamp;''')
		}
		sb.append(
			'''//«inst»
				''')
	}

	def constant(int id, Frame f) '''«f.constants.get(id).toHexString»'''

	def dartType(InternalInformation ii) {
		return ii.info.dartType(false)
	}

	def dartType(VariableInformation information, boolean withArray) {
		var jt = "int"
		if (information.name.startsWith(InternalInformation::PRED_PREFIX))
			jt = "bool"
		if (information.array && withArray)
			return '''List<«jt»>'''
		return jt
	}

	def decl(VariableInformation info, Boolean includePrev) {
		'''
			«IF info.isPredicate || (prevMap.get(info.name) != null && prevMap.get(info.name))»int «info.idName(false, true)»_update=0;«ENDIF»
			«info.dartType(true)» «info.idName(false, true)»=«initValue(info)»
			«IF includePrev != null && includePrev»«info.dartType(true)» «info.idName(true, true)»=0;«ENDIF»
			«IF info.isRegister»«info.dartType(true)» «info.idName(false, true)»$reg=«initValue(info)»«ENDIF»
		'''
	}

	def initValue(VariableInformation info) {
		'''«IF info.predicate»false«ELSEIF info.array»new «info.dartType(true)»(«info.totalSize»)«ELSE»0«ENDIF»;'''
	}

	def getImports() '''
import 'dart:collection';
import '../simulation_comm.dart';
	'''

}
