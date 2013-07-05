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

class JavaCompiler {
	private ExecutableModel em
	private Map<String, Integer> varIdx = new HashMap
	private Map<String, Integer> intIdx = new HashMap
	private Map<String, Boolean> prevMap = new HashMap
	private boolean debug
	private boolean hasClock

	new(ExecutableModel em, boolean includeDebug) {
		this.em = em
		this.debug = includeDebug
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

	def static String doCompile(ExecutableModel em, String packageName, String unitName, boolean includeDebugListener) {
		return new JavaCompiler(em, includeDebugListener).compile(packageName, unitName).toString
	}

	def InternalInformation asInternal(int id) {
		return em.internals.get(id)
	}

	def compile(String packageName, String unitName) {
		val Set<Integer> handled = new HashSet
		handled.add(-1)
		'''
			«IF packageName != null»package «packageName»;«ENDIF»
			«imports»
			
			public class «unitName» implements IHDLInterpreter{
				«IF hasClock»
					private static class RegUpdate {
						public final int internalID;
						public final int offset;
						
						public RegUpdate(int internalID, int offset) {
							super();
							this.internalID = internalID;
							this.offset = offset;
						}
						
						@Override
						public int hashCode() {
							final int prime = 31;
							int result = 1;
							result = (prime * result) + internalID;
							result = (prime * result) + offset;
							return result;
						}
						
						@Override
						public boolean equals(Object obj) {
							if (this == obj)
								return true;
							if (obj == null)
								return false;
							if (getClass() != obj.getClass())
								return false;
							RegUpdate other = (RegUpdate) obj;
							if (internalID != other.internalID)
								return false;
							if (offset != other.offset)
								return false;
							return true;
						}
					}
					
					private Set<RegUpdate> regUpdates=new HashSet<RegUpdate>();
					private final boolean disableEdges;
					private final boolean disabledRegOutputlogic;
				«ENDIF»
				«FOR v : em.variables.excludeNull»
					«v.decl(prevMap.get(v.name))»
				«ENDFOR»
				private int epsCycle=0;
				private int deltaCycle=0;
				private Map<String, Integer> varIdx=new HashMap<String, Integer>();
				«IF debug»
					private final IDebugListener listener;
					private final ExecutableModel em;
				«ENDIF»
				«IF hasClock || debug»
					/**
					* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false
					*/
					public «unitName»() {
						this(«IF hasClock»false, false«ENDIF»«IF hasClock && debug»,«ENDIF»«IF debug» null, null«ENDIF»);
					}
				«ENDIF»
				
				public «unitName»(«IF hasClock»boolean disableEdges, boolean disabledRegOutputlogic«ENDIF»«IF hasClock && debug»,«ENDIF»«IF debug» IDebugListener listener, ExecutableModel em«ENDIF») {
					«IF hasClock»
						this.disableEdges=disableEdges;
						this.disabledRegOutputlogic=disabledRegOutputlogic;
					«ENDIF»
					«IF debug»
						this.listener=listener;
						this.em=em;
					«ENDIF»
					«FOR v : em.variables.excludeNull»
						«v.init»
						varIdx.put("«v.name»", «varIdx.get(v.name)»);
					«ENDFOR»
				}
				«FOR v : em.variables.filter[dir !== VariableInformation$Direction::INTERNAL]»
					«IF v.dimensions.size==0»
					public void set«v.javaName(false).toFirstUpper»(«v.javaType» value) {
						«v.javaName(false)»=value & «v.width.asMask»;
					}
					
					public «v.javaType» get«v.javaName(false).toFirstUpper»() {
						return «v.javaName(false)» & «v.width.asMask»;
					}
					«ELSE»
					public void set«v.javaName(false).toFirstUpper»(«v.javaType» value«FOR i:(0..<v.dimensions.size)», int a«i»«ENDFOR») {
						«v.javaName(false)»[«v.arrayAccess»]=value & «v.width.asMask»;
					}
					
					public «v.javaType» get«v.javaName(false).toFirstUpper»(«FOR i:(0..<v.dimensions.size) SEPARATOR ','»int a«i»«ENDFOR») {
						return «v.javaName(false)»[«v.arrayAccess»] & «v.width.asMask»;
					}
					«ENDIF»
				«ENDFOR»
				«FOR f : em.frames»
					«f.method»
				«ENDFOR»
				«IF hasClock»
					public boolean skipEdge(long local) {
						long dc = local >>> 16l;
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
				public void run(){
					deltaCycle++;
					«IF hasClock»
						epsCycle=0;
						do {
							regUpdates.clear();
					«ENDIF»
					«IF debug»
						if (listener!=null)
							listener.startCycle(deltaCycle, epsCycle, this);
					«ENDIF»
					«FOR f : em.frames»
						«IF f.edgeNegDepRes === -1 && f.edgePosDepRes === -1 && f.predNegDepRes.length === 0 &&
				f.predPosDepRes.length === 0»
							frame«f.uniqueID»();
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
								frame«f.uniqueID»();
						«ENDIF»
					«ENDFOR»
					«IF hasClock»
						updateRegs();
						«IF debug»
							if (listener!=null && !regUpdates.isEmpty())
								listener.copyingRegisterValues(this);
						«ENDIF»
						epsCycle++;
						} while (!regUpdates.isEmpty() && !disabledRegOutputlogic);
					«ENDIF»
					«FOR v : em.variables.excludeNull.filter[prevMap.get(it.name) != null]»
						«v.copyPrev»
					«ENDFOR»
					«IF debug»
						if (listener!=null)
							listener.doneCycle(deltaCycle, this);
					«ENDIF»
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

	def createBooleanPred(int id, Set<Integer> handled) {
		if (handled.contains(id))
			return ''''''
		handled.add(id)
		'''
			«id.asInternal.getter(false, id, -1)»
			boolean p«id»_fresh=true;
			long up«id»=«id.asInternal.info.javaName(false)»_update;
			if ((up«id»>>>16 != deltaCycle) || ((up«id»&0xFFFF) != epsCycle)){
				«IF debug»
					if (listener!=null)
					 	listener.skippingPredicateNotFresh(-1, em.internals[«id»], true, null);
				«ENDIF»
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
			boolean «internal.javaName(false)»_isRising=true;
			boolean «internal.javaName(false)»_risingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=0) || (t«id»!=1)) {
					«IF debug»
						if (listener!=null)
							listener.skippingNotAnEdge(-1, em.internals[«id»], true, null);
					«ENDIF»
					«internal.javaName(false)»_isRising=false;
				}
			}
			if (skipEdge(«internal.info.javaName(false)»_update)){
				«IF debug»
					if (listener!=null)
						listener.skippingHandledEdge(-1, em.internals[«id»], true, null);
				«ENDIF»
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
			boolean «internal.javaName(false)»_isFalling=true;
			boolean «internal.javaName(false)»_fallingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=1) || (t«id»!=0)) {
					«IF debug»
						if (listener!=null)
						 	listener.skippingNotAnEdge(-1, em.internals[«id»], false, null);
					«ENDIF»
					«internal.javaName(false)»_isFalling=false;
				}
			}
			if (skipEdge(«internal.info.javaName(false)»_update)){
				«IF debug»
					if (listener!=null)
					 	listener.skippingHandledEdge(-1, em.internals[«id»], false, null);
				«ENDIF»
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

	def hdlInterpreter() '''
		@Override
		public void setInput(String name, BigInteger value, int... arrayIdx) {
			setInput(getIndex(name), value.longValue(), arrayIdx);
		}
		
		@Override
		public void setInput(int idx, BigInteger value, int... arrayIdx) {
			setInput(idx, value.longValue(), arrayIdx);
		}
		
		@Override
		public void setInput(String name, long value, int... arrayIdx) {
			setInput(getIndex(name), value, arrayIdx);
		}
		
		@Override
		public void setInput(int idx, long value, int... arrayIdx) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					«IF v.dimensions.length == 0»
						case «varIdx.get(v.name)»: 
							«IF v.width != 64 && !v.predicate»value&=«v.width.asMask»;«ENDIF»
							«v.javaName(false)»=value«IF v.predicate»==0?false:true«ENDIF»;
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							«IF v.width != 64 && !v.predicate»value&=«v.width.asMask»;«ENDIF»
							«v.javaName(false)»[«v.arrayAccessArrIdx»]=value;
							break;
					«ENDIF»
				«ENDFOR»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public int getIndex(String name) {
			return varIdx.get(name);
		}
		
		@Override
		public String getName(int idx) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					case «varIdx.get(v.name)»: return "«v.name»";
				«ENDFOR»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public long getOutputLong(String name, int... arrayIdx) {
			return getOutputLong(getIndex(name), arrayIdx);
		}
		
		@Override
		public long getOutputLong(int idx, int... arrayIdx) {
			switch (idx) {
				«FOR v : em.variables.excludeNull»
					«IF v.dimensions.length == 0»
						case «varIdx.get(v.name)»: return «v.javaName(false)»«IF v.predicate»?1:0«ELSEIF v.width != 64» & «v.width.asMask»«ENDIF»;
					«ELSE»
						case «varIdx.get(v.name)»: return «v.javaName(false)»[«v.arrayAccessArrIdx»]«IF v.width != 64 && !v.predicate» & «v.
			width.asMask»«ENDIF»;
					«ENDIF»
				«ENDFOR»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public BigInteger getOutputBig(String name, int... arrayIdx) {
			return BigInteger.valueOf(getOutputLong(getIndex(name), arrayIdx));
		}
		
		@Override
		public BigInteger getOutputBig(int idx, int... arrayIdx) {
			return BigInteger.valueOf(getOutputLong(idx, arrayIdx));
		}
		
		@Override
		public int getDeltaCycle() {
			return deltaCycle;
		}
	'''

	def excludeNull(VariableInformation[] vars) {
		vars.filter[name != '#null']
	}

	def excludeNull(InternalInformation[] vars) {
		vars.filter[info.name != '#null']
	}

	def copyRegs() '''
		private void updateRegs() {
			for (RegUpdate reg : regUpdates) {
				switch (reg.internalID) {
					«FOR v : em.variables»
						«IF v.isRegister»
							case «varIdx.get(v.name)»: 
							«IF v.dimensions.length == 0»
								«v.javaName(false)» = «v.javaName(false)»$reg; break;
							«ELSE»
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
		val arrAcc = '''«FOR int i : (0 ..< info.arrayIdx.length) BEFORE '[' SEPARATOR '][' AFTER ']'»a«i»«ENDFOR»'''
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
			«IF info.arrayIdx.length === info.info.dimensions.length»
				«IF debug»
					if (listener!=null)
						listener.loadingInternal(«frameID», em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»«varName»?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(«varName»)«ENDIF», null);
				«ENDIF»
			«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«info.javaType» «varName»= «info.info.javaName(prev)»«arrAcc»;
			«ELSEIF info.actualWidth == 1»
				«info.javaType» «varName»= («info.info.javaName(prev)»«arrAcc» >> «info.bitStart») & 1;
			«ELSE»
				«info.javaType» «varName»= («info.info.javaName(prev)»«arrAcc» >> «info.bitEnd») & «info.actualWidth.asMask»;
			«ENDIF»
			«IF info.arrayIdx.length === info.info.dimensions.length»
				«IF debug»
					if (listener!=null)
						listener.loadingInternal(«frameID», em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»«varName»?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(«varName»)«ENDIF», null);
				«ENDIF»
			«ENDIF»
		'''
	}

	def setter(InternalInformation info, String value) {
		val mask = ((1l << info.actualWidth) - 1)
		val maskString = mask.toHexString
		val writeMask = (mask << (info.bitEnd)).bitwiseNot.toHexString
		val varAccess = info.info.arrayAccess
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
				if (current!=«value»)
					regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», «off»));
			«ENDIF»
			«IF info.isPred»«info.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		''' else '''
			int offset=(int)«varAccess»;
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.javaType» current=«info.info.javaName(false)»«regSuffix»[offset];«ENDIF»
				«info.info.javaName(false)»«regSuffix»[offset]=«value»;
			«ELSE»
				«info.info.javaType» current=«info.info.javaName(false)»«regSuffix»[offset] & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»;
				«info.info.javaName(false)»«regSuffix»[offset]=current|«value»);
			«ENDIF»
			«IF info.isShadowReg»
				if (current!=«value»)
					regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», offset));
			«ENDIF»
			«IF info.isPred»«info.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		'''
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

	def arrayAccess(VariableInformation v) {
		val varAccess = new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0 ..< v.dimensions.length)) {
			val dim = dims.get(i)
			if (dim != 1)
				varAccess.append('''a«i»*«dim»''')
			else
				varAccess.append('''a«i»''')
		}
		return varAccess
	}

	def toHexString(long value) '''0x«Long::toHexString(value)»l'''

	def method(Frame frame) {
		val StringBuilder sb = new StringBuilder
		sb.append(
			'''
				private final void frame«frame.uniqueID»() {
					«IF debug»
						if (listener!=null)
							listener.startFrame(«frame.uniqueID», deltaCycle, epsCycle, null);
					«ENDIF»
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
				«IF debug»
					if (listener!=null)
						listener.writingResult(«frame.uniqueID», em.internals[«frame.outputId»], BigInteger.valueOf(«last»«IF frame.
					outputId.asInternal.isPred»?1:0«ENDIF»), null);
				«ENDIF»
				}
			''')
		return sb.toString
	}

	def toExpression(FastInstruction inst, Frame f, StringBuilder sb, int pos, int a, int b, List<Integer> arr,
		int arrPos) {
		switch (inst.inst) {
			case Instruction::pushAddIndex:
				sb.append('''int a«arr.last»=(int)t«a»;''')
			case Instruction::writeInternal: {
				if (arr.size < inst.arg1.asInternal.info.dimensions.length) {
					sb.append('''Arrays.fill(«inst.arg1.asInternal.javaName(false)», t«a»);''')
				} else {
					sb.append(
						'''«inst.arg1.asInternal.javaName(false)»«FOR ai : arr BEFORE '[' SEPARATOR '][' AFTER ']'»a«ai»«ENDFOR»=t«a»;''')
					arr.clear
				}
			}
			case Instruction::noop:
				sb.append("//Do nothing")
			case Instruction::and:
				sb.append('''long t«pos»=t«b» & t«a»;''')
			case Instruction::arith_neg:
				sb.append('''long t«pos»=-t«a»;''')
			case Instruction::bit_neg:
				sb.append('''long t«pos»=~t«a»;''')
			case Instruction::bitAccessSingle:
				sb.append('''long t«pos»=(t«a» >> «inst.arg1») & 1;''')
			case Instruction::bitAccessSingleRange: {
				val highBit = inst.arg1
				val lowBit = inst.arg2
				val long mask = (1l << ((highBit - lowBit) + 1)) - 1
				sb.append('''long t«pos»=(t«a» >> «lowBit») & «mask.toHexString»;''')
			}
			case Instruction::cast_int: {
				if (inst.arg1 != 64) {
					var currentSize=inst.arg2;
					var targetSize=inst.arg1;
					var orMask=((1l<<targetSize)-1).bitwiseNot;
					sb.append(
						'''
							//Target size «targetSize» currentSize «currentSize»
							long c«pos»=t«a» << «64 - currentSize»;
						''')
					if (targetSize<currentSize)
						sb.append(
							'''long t«pos»=(c«pos» >> «64 - currentSize») | «orMask.toHexString»;
							''')
					else
						sb.append(
							'''long t«pos»=c«pos» >> «64 - currentSize»;
							''')
				} else {
					sb.append('''long t«pos»=t«a»;''')
				}
			}
			case Instruction::cast_uint: {
				if (inst.arg1 != 64) {
					sb.append('''long t«pos»=t«a» & «inst.arg1.asMask»;''')
				} else {
					sb.append('''long t«pos»=t«a»;''')
				}
			}
			case Instruction::logiNeg:
				sb.append('''boolean t«pos»=!t«a»;''')
			case Instruction::logiAnd:
				sb.append('''boolean t«pos»=t«a» && t«b»;''')
			case Instruction::logiOr:
				sb.append('''boolean t«pos»=t«a» || t«b»;''')
			case Instruction::const0:
				sb.append('''long t«pos»=0;''')
			case Instruction::const1:
				sb.append('''long t«pos»=1;''')
			case Instruction::const2:
				sb.append('''long t«pos»=2;''')
			case Instruction::constAll1:
				sb.append('''long t«pos»=«inst.arg1.asMask»;''')
			case Instruction::concat:
				sb.append('''long t«pos»=(t«b» << «inst.arg2») | t«a»;''')
			case Instruction::loadConstant:
				sb.append('''long t«pos»=«inst.arg1.constant(f)»;''')
			case Instruction::loadInternal: {
				val internal = inst.arg1.asInternal
				sb.append(internal.getter(false, pos, f.uniqueID))
				arr.clear
			}
			case Instruction::and:
				sb.append('''long t«pos»=t«b» & t«a»;''')
			case Instruction::or:
				sb.append('''long t«pos»=t«b» | t«a»;''')
			case Instruction::xor:
				sb.append('''long t«pos»=t«b» ^ t«a»;''')
			case Instruction::plus:
				sb.append('''long t«pos»=t«b» + t«a»;''')
			case Instruction::minus:
				sb.append('''long t«pos»=t«b» - t«a»;''')
			case Instruction::mul:
				sb.append('''long t«pos»=t«b» * t«a»;''')
			case Instruction::div:
				sb.append('''long t«pos»=t«b» / t«a»;''')
			case Instruction::sll:
				sb.append('''long t«pos»=t«b» << t«a»;''')
			case Instruction::srl:
				sb.append('''long t«pos»=t«b» >>> t«a»;''')
			case Instruction::sra:
				sb.append('''long t«pos»=t«b» >> t«a»;''')
			case Instruction::eq:
				sb.append('''boolean t«pos»=t«b» == t«a»;''')
			case Instruction::not_eq:
				sb.append('''boolean t«pos»=t«b» != t«a»;''')
			case Instruction::less:
				sb.append('''boolean t«pos»=t«b» < t«a»;''')
			case Instruction::less_eq:
				sb.append('''boolean t«pos»=t«b» <= t«a»;''')
			case Instruction::greater:
				sb.append('''boolean t«pos»=t«b» > t«a»;''')
			case Instruction::greater_eq:
				sb.append('''boolean t«pos»=t«b» >= t«a»;''')
			case Instruction::isRisingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
			case Instruction::isFallingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
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
			return "boolean"
		return "long"
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
		val res = name.replaceAll("\\.", "_").replaceAll('\\{', 'Bit').replaceAll('\\}', '').replaceAll(':', 'to').
			replaceAll('\\[', 'arr').replaceAll('\\]', '')
		if (prev)
			return res + '_prev'
		return res
	}

	def decl(VariableInformation info, Boolean includePrev) '''
		«IF info.isPredicate || (prevMap.get(info.name) != null && prevMap.get(info.name))»private long «info.javaName(false)»_update=0;«ENDIF»
		public «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.javaName(false)»;
		«IF includePrev != null && includePrev»private «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.
			javaName(true)»;«ENDIF»
		«IF info.isRegister»private «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.javaName(false)»$reg;«ENDIF»
	'''

	def isPredicate(VariableInformation info) {
		info.name.startsWith(InternalInformation::PRED_PREFIX)
	}

	def getImports() '''
		import java.util.*;
		import java.math.*;
		import org.pshdl.interpreter.*;
		«IF debug»
			import org.pshdl.interpreter.frames.*;
		«ENDIF»
	'''

}
