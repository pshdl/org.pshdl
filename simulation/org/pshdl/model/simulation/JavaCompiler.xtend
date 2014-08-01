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

import com.google.common.collect.Lists
import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Map.Entry
import java.util.Set
import java.util.Stack
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.utils.Instruction
import org.pshdl.model.utils.PSAbstractCompiler
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem

@Data
class JavaCompilerSpecification {
	ExecutableModel model
	String pkg
	String unitName
	boolean debug
	boolean createCoverage
}

class JavaCompiler implements ITypeOuptutProvider, ICodeGen {
	private boolean debug

	private extension CommonCompilerExtension cce

	new() {
	}

	new(ExecutableModel em, boolean includeDebug) {
		this.cce = new CommonCompilerExtension(em, 64)
		this.debug = includeDebug
	}

	def static doCompile(Set<Problem> syntaxProblems, ExecutableModel em, String pkg, String unitName, boolean debug) {
		val comp = new JavaCompiler(em, debug)
		val code = comp.compile(pkg, unitName).toString
		return Lists.newArrayList(
			new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, Collections.emptyList, em.source,
				comp.hookName, true))
	}
	
	def coverage(String packageName, String unitName) '''
		«IF packageName !== null»package «packageName»;«ENDIF»
		«imports»
		
		public class «unitName»Coverage {
			«FOR v : em.variables.excludeNull»
				«v.decl(prevMap.get(v.name))»
			«ENDFOR»
		}
	'''

	override compile(String packageName, String unitName) {
		val Set<Integer> handled = new HashSet
		val Set<Integer> handledPosEdge = new HashSet
		val Set<Integer> handledNegEdge = new HashSet
		handled.add(-1)
		handledPosEdge.add(-1)
		handledNegEdge.add(-1)
		'''
			«IF packageName !== null»package «packageName»;«ENDIF»
			«imports»
			
			public class «unitName» implements «IF !em.annotations.nullOrEmpty && em.annotations.contains(HDLSimulator.TB_UNIT.name.substring(1))»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»{
				«IF hasClock»
					private Set<RegUpdate> regUpdates=new LinkedHashSet<RegUpdate>();
					private boolean disableEdges;
					private boolean disabledRegOutputlogic;
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
						this(false, false«IF debug», null, null«ENDIF»);
					}
				«ENDIF»
				
				public «unitName»(boolean disableEdges, boolean disabledRegOutputlogic«IF debug», IDebugListener listener, ExecutableModel em«ENDIF») {
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
				«beanMethods()»
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
				«em.runMethod(handled, handledNegEdge, handledPosEdge)»
				«IF !em.processframes.empty»
					«em.testbenchMethod(handled, handledNegEdge, handledPosEdge)»
				«ENDIF»
			}
		'''
	}
	
	def protected beanMethods()
		'''«FOR v : em.variables.filter[dir !== VariableInformation$Direction.INTERNAL]»
			«IF v.dimensions.size == 0»
				public void set«v.idName(false, false).toFirstUpper»(«v.javaType» value) {
					«v.idName(false, false)»=value & «v.width.asMaskL»;
				}
				
				public «v.javaType» get«v.idName(false, false).toFirstUpper»() {
					return «v.idName(false, false)» & «v.width.asMaskL»;
				}
			«ELSE»
				public void set«v.idName(false, false).toFirstUpper»(«v.javaType» value«FOR i : (0 ..< v.dimensions.size)», int a«i»«ENDFOR») {
					«v.idName(false, false)»[«v.arrayAccess»]=value & «v.width.asMaskL»;
				}
				
				public «v.javaType» get«v.idName(false, false).toFirstUpper»(«FOR i : (0 ..< v.dimensions.size) SEPARATOR ','»int a«i»«ENDFOR») {
					return «v.idName(false, false)»[«v.arrayAccess»] & «v.width.asMaskL»;
				}
			«ENDIF»
		«ENDFOR»'''
	

	def protected  testbenchMethod(ExecutableModel model, Set<Integer> handled, Set<Integer> handledNegEdge, Set<Integer> handledPosEdge) {
		val Map<String, CharSequence> processes = model.processframes.map[process].toInvertedMap['''''']
		model.processframes.forEach[
			processes.put(it.process,
				processes.get(it.process) + '''
					«callFrame(it, handled, handledNegEdge, handledPosEdge)»
				''')]
		return '''
			«FOR Map.Entry<String, CharSequence> e : processes.entrySet»
				private boolean runProcess_«e.key»(){
					long oldTime=«processTime(model, e.key)»;
					long oldState=«processState(model, e.key)»;
					while («model.varByName("$time")»>=«processTime(model, e.key)» && 
							«processState(model, e.key)»>=0 &&
							«processState(model, e.key)»!= 0x7FFF_FFFF) {
						«e.value»
					}
					return (oldTime != «processTime(model, e.key)») || (oldState != «processState(model, e.key)»);
				}
			«ENDFOR»
			public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener) {
				long stepCount=0;
				while («model.varByName("$time")»<=maxTime && stepCount<maxSteps) {
					boolean modified=false;
					do {
						modified = false;
						«FOR CharSequence e : processes.keySet»
							if (runProcess_«e»())
								modified=true;
						«ENDFOR»
					} while (modified);
					run();
					stepCount++;
					long nextTime=Long.MAX_VALUE;
					«FOR CharSequence e : processes.keySet»
					if («processState(model, e)» >= 0 && «processState(model, e)» != 0x7FFF_FFFF)
						nextTime=Math.min(nextTime, «processTime(model, e)»);
					«ENDFOR»
					«model.varByName("$time")»=nextTime;
					if (listener!=null && !listener.nextStep(«model.varByName("$time")», stepCount))
						break;
				}
			}
		'''
	}

	def protected  runMethod(ExecutableModel model, Set<Integer> handled, Set<Integer> handledNegEdge, Set<Integer> handledPosEdge) '''
		public void initConstants(){}
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
			«FOR f : em.nonProcessframes»
				«callFrame(f, handled, handledNegEdge, handledPosEdge)»
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
			«FOR v : em.variables.excludeNull.filter[prevMap.get(it.name) !== null]»
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
	'''

	def protected  callFrame(Frame f, Set<Integer> handled, Set<Integer> handledNegEdge, Set<Integer> handledPosEdge) '''«IF f.edgeNegDepRes == -1 && f.edgePosDepRes == -1 &&
		f.predNegDepRes.nullOrEmpty && f.predPosDepRes.nullOrEmpty»
			«f.frameName»();
		«ELSE»
			«f.edgeNegDepRes.createNegEdge(handledNegEdge)»
			«f.edgePosDepRes.createPosEdge(handledPosEdge)»
			«IF !f.predNegDepRes.nullOrEmpty»
				«FOR p : f.predNegDepRes»
					«p.createBooleanPred(handled)»
				«ENDFOR»
			«ENDIF»
			«IF !f.predPosDepRes.nullOrEmpty»
				«FOR p : f.predPosDepRes»
					«p.createBooleanPred(handled)»
				«ENDFOR»
			«ENDIF»
			if («f.predicateConditions»)
				«f.frameName»();
		«ENDIF»'''


	def protected  createBooleanPred(int id, Set<Integer> handled) {
		if (handled.contains(id))
			return ''''''
		handled.add(id)
		'''
			«id.asInternal.getter(false, id, -1)»
			boolean p«id»_fresh=true;
			long up«id»=«id.asInternal.info.idName(false, false)»_update;
			if ((up«id»>>>16 != deltaCycle) || ((up«id»&0xFFFF) != epsCycle)){
				«IF debug»
					if (listener!=null)
					 	listener.skippingPredicateNotFresh(-1, em.internals[«id»], true, null);
				«ENDIF»
				p«id»_fresh=false;
			}
		'''
	}

	def protected  createPosEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			boolean «internal.idName(false, false)»_isRising=true;
			boolean «internal.idName(false, false)»_risingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=0) || (t«id»!=1)) {
					«IF debug»
						if (listener!=null)
							listener.skippingNotAnEdge(-1, em.internals[«id»], true, null);
					«ENDIF»
					«internal.idName(false, false)»_isRising=false;
				}
			} else {
				«id.asInternal.getter(false, id, -1)»
				«internal.idName(false, false)»_isRising=t«id»==1;
			}
			if (skipEdge(«internal.info.idName(false, false)»_update)){
				«IF debug»
					if (listener!=null)
						listener.skippingHandledEdge(-1, em.internals[«id»], true, null);
				«ENDIF»
				«internal.idName(false, false)»_risingIsHandled=true;
			}
		'''
	}

	def protected  createNegEdge(int id, Set<Integer> handledEdges) {
		if (handledEdges.contains(id))
			return ''''''
		handledEdges.add(id)
		val internal = id.asInternal
		'''
			boolean «internal.idName(false, false)»_isFalling=true;
			boolean «internal.idName(false, false)»_fallingIsHandled=false;
			if (!disableEdges){
				«id.asInternal.getter(false, id, -1)»
				«id.asInternal.getter(true, id, -1)»
				if ((t«id»_prev!=1) || (t«id»!=0)) {
					«IF debug»
						if (listener!=null)
						 	listener.skippingNotAnEdge(-1, em.internals[«id»], false, null);
					«ENDIF»
					«internal.idName(false, false)»_isFalling=false;
				}
			} else {
				«id.asInternal.getter(false, id, -1)»
				«internal.idName(false, false)»_isFalling=t«id»==0;
			}
			if (skipEdge(«internal.info.idName(false, false)»_update)){
				«IF debug»
					if (listener!=null)
					 	listener.skippingHandledEdge(-1, em.internals[«id»], false, null);
				«ENDIF»
				«internal.idName(false, false)»_fallingIsHandled=true;
			}
		'''
	}

	def protected  hdlInterpreter() '''

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
							«IF v.width != 64 && !v.predicate»value&=«v.width.asMaskL»;«ENDIF»
							«v.idName(false, false)»=value«IF v.predicate»==0?false:true«ENDIF»;
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							«IF v.width != 64 && !v.predicate»value&=«v.width.asMaskL»;«ENDIF»
							«v.idName(false, false)»[«v.arrayAccessArrIdx»]=value;
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
						case «varIdx.get(v.name)»: return «v.idName(false, false)»«IF v.predicate»?1:0«ELSEIF v.width != 64» & «v.width.asMaskL»«ENDIF»;
					«ELSE»
						case «varIdx.get(v.name)»: return «v.idName(false, false)»[«v.arrayAccessArrIdx»]«IF v.width != 64 &&
			!v.predicate» & «v.width.asMaskL»«ENDIF»;
					«ENDIF»
				«ENDFOR»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public long getDeltaCycle() {
			return deltaCycle;
		}
		
		@Override
		public void close() throws Exception{
		}
		@Override
		public void setFeature(Feature feature, Object value) {
			switch (feature) {
			case disableOutputRegs:
			«IF hasClock»
				disabledRegOutputlogic = (boolean) value;
			«ENDIF»
				break;
			case disableEdges:
			«IF hasClock»
				disableEdges = (boolean) value;
			«ENDIF»
				break;
			}
		}
	'''

	def protected  copyRegs() '''
		private void updateRegs() {
			for (RegUpdate reg : regUpdates) {
				switch (reg.internalID) {
					«FOR v : em.variables»
						«IF v.isRegister»
							case «varIdx.get(v.name)»: 
							«IF v.dimensions.length == 0»
								«v.idName(false, false)» = «v.idName(false, false)»$reg; break;
							«ELSE»
								if (reg.offset!=-1)
									«v.idName(false, false)»[reg.offset] = «v.idName(false, false)»$reg[reg.offset];
								else
									Arrays.fill(«v.idName(false, false)», «v.idName(false, false)»$reg[0]); 
								break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''

	def protected  copyPrev(VariableInformation info) {
		if (info.dimensions.length == 0)
			return '''«info.idName(true, false)»=«info.idName(false, false)»;'''
		return '''System.arraycopy(«info.idName(false, false)»,0,«info.idName(true, false)», 0, «info.idName(false,
			false)».length);'''
	}

	def protected  getter(InternalInformation info, boolean prev, int pos, int frameID) {
		val sb = new StringBuilder
		val mask = info.actualWidth.asMaskL
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
				«info.javaType» «varName»=«info.info.idName(prev, false)»«sb»;
			«ELSEIF info.actualWidth == 1»
				«info.javaType» «varName»=(«info.info.idName(prev, false)»«sb» >> «info.bitStart») & 1;
			«ELSE»
				«info.javaType» «varName»=(«info.info.idName(prev, false)»«sb» >> «info.bitEnd») & «mask»;
			«ENDIF»
			«IF info.arrayIdx.length == info.info.dimensions.length»
				«IF debug»
					if (listener!=null)
						listener.loadingInternal(«frameID», em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»«varName»?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(«varName»)«ENDIF», null);
				«ENDIF»
			«ENDIF»
		''' else '''
			«IF info.actualWidth == info.info.width»
				«info.javaType» «varName»= «info.info.idName(prev, false)»«arrAcc»;
			«ELSEIF info.actualWidth == 1»
				«info.javaType» «varName»= («info.info.idName(prev, false)»«arrAcc» >> «info.bitStart») & 1;
			«ELSE»
				«info.javaType» «varName»= («info.info.idName(prev, false)»«arrAcc» >> «info.bitEnd») & «info.actualWidth.asMaskL»;
			«ENDIF»
			«IF info.arrayIdx.length == info.info.dimensions.length»
				«IF debug»
					if (listener!=null)
						listener.loadingInternal(«frameID», em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»«varName»?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(«varName»)«ENDIF», null);
				«ENDIF»
			«ENDIF»
		'''
	}

	def protected  setter(InternalInformation info, String value) {
		val mask = ((1l << info.actualWidth) - 1)
		val maskString = mask.toHexStringL
		val writeMask = (mask << (info.bitEnd)).bitwiseNot.toHexStringL
		val varAccess = info.info.arrayAccess
		val off = info.arrayFixedOffset
		var fixedAccess = if (info.arrayIdx.length > 0) '''[«off»]''' else ''''''
		var regSuffix = ''
		if (info.isShadowReg) {
			fixedAccess = "$reg" + fixedAccess
			regSuffix = "$reg"
		}
		if (info.fixedArray) '''
			«IF info.isShadowReg»«info.info.javaType» prev=«info.info.idName(false, false)»«fixedAccess»;«ENDIF»
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.javaType» current=«info.info.idName(false, false)»«fixedAccess»;«ENDIF»
				«info.info.idName(false, false)»«fixedAccess»=«value»;
			«ELSE»
				«info.info.javaType» current=«info.info.idName(false, false)»«fixedAccess» & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»);
				«info.info.idName(false, false)»«fixedAccess»=current|«value»;
			«ENDIF»
			«IF info.isShadowReg»
				if (prev!=«value»)
					regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», «off»));
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		''' else '''
			int offset=(int)«varAccess»;
			«IF info.isShadowReg»«info.info.javaType» prev=«info.info.idName(false, false)»«regSuffix»[offset];«ENDIF»
			«IF info.actualWidth == info.info.width»
				«IF info.isShadowReg»«info.info.javaType» current=«info.info.idName(false, false)»«regSuffix»[offset];«ENDIF»
				«info.info.idName(false, false)»«regSuffix»[offset]=«value»;
			«ELSE»
				«info.info.javaType» current=«info.info.idName(false, false)»«regSuffix»[offset] & «writeMask»;
				«value»=((«value» & «maskString») << «info.bitEnd»;
				«info.info.idName(false, false)»«regSuffix»[offset]=current|«value»);
			«ENDIF»
			«IF info.isShadowReg»
				if (prev!=«value»)
					regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», offset));
			«ENDIF»
			«IF info.isPred»«info.info.idName(false, false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
		'''
	}

	def protected  method(Frame frame) {
		val StringBuilder sb = new StringBuilder
		sb.append(
			'''
				private final void «frame.frameName»() {
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
			if (i.inst === Instruction.pushAddIndex) {
				arr.add(arrPos)
				arrPos = arrPos + 1
			}
			i.toExpression(frame, sb, pos, a, b, arr, arrPos)
			if (i.inst !== Instruction.pushAddIndex)
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

	def protected  toExpression(FastInstruction inst, Frame f, StringBuilder sb, int pos, int a, int b, List<Integer> arr,
		int arrPos) {
		switch (inst.inst) {
			case Instruction.pushAddIndex:
				sb.append('''int a«arr.last»=(int)t«a»;''')
			case Instruction.writeInternal: {
				val internal = inst.arg1.asInternal
				val isDynMem = arr.size < internal.info.dimensions.length
				if (isDynMem) {
					sb.append('''Arrays.fill(«internal.idName(false, false)», t«a»);''')
				} else {
					sb.append(
						'''«internal.idName(false, false)»«IF internal.info.dimensions.length > 0»[«internal.info.
							arrayAccess(arr)»]«ENDIF»=t«a»;''')
				}
				if (internal.isShadowReg)
					sb.append(
						'''regUpdates.add(new RegUpdate(«varIdx.get(internal.info.name)», «IF !isDynMem &&
							internal.info.array»«internal.info.arrayAccess(arr)»«ELSE»-1«ENDIF»));''')
				arr.clear
			}
			case Instruction.noop:
				sb.append("//Do nothing")
			case Instruction.bitAccessSingle:
				sb.append('''long t«pos»=(t«a» >> «inst.arg1») & 1;''')
			case Instruction.bitAccessSingleRange: {
				val highBit = inst.arg1
				val lowBit = inst.arg2
				val long mask = (1l << ((highBit - lowBit) + 1)) - 1
				sb.append('''long t«pos»=(t«a» >> «lowBit») & «mask.toHexStringL»;''')
			}
			case Instruction.cast_int: {
				val shiftWidth = 64 - Math.min(inst.arg1, inst.arg2);
				sb.append('''long t«pos»=«signExtend('''t«a»''', null, shiftWidth)»;''')
			}
			case Instruction.cast_uint: {
				val castSize=Math.min(inst.arg1, inst.arg2)
				if (castSize != 64) {
					sb.append('''long t«pos»=t«a» & «castSize.asMaskL»;''')
				} else {
					sb.append('''long t«pos»=t«a»;''')
				}
			}
			case Instruction.logiNeg:
				sb.append('''boolean t«pos»=!t«a»;''')
			case Instruction.logiAnd:
				sb.append('''boolean t«pos»=t«a» && t«b»;''')
			case Instruction.logiOr:
				sb.append('''boolean t«pos»=t«a» || t«b»;''')
			case Instruction.const0:
				sb.append('''long t«pos»=0;''')
			case Instruction.const1:
				sb.append('''long t«pos»=1;''')
			case Instruction.const2:
				sb.append('''long t«pos»=2;''')
			case Instruction.constAll1:
				sb.append('''long t«pos»=«inst.arg1.asMaskL»;''')
			case Instruction.concat:
				sb.append('''long t«pos»=(t«b» << «inst.arg2») | t«a»;''')
			case Instruction.loadConstant:
				sb.append('''long t«pos»=«inst.arg1.constantL(f)»;''')
			case Instruction.loadInternal: {
				val internal = inst.arg1.asInternal
				sb.append(internal.getter(false, pos, f.uniqueID))
				arr.clear
			}
			case Instruction.arith_neg:
				sb.append('''long t«pos»=«singleOpValue('-', null, a, inst.arg1)»;''')
			case Instruction.bit_neg:
				sb.append('''long t«pos»=«singleOpValue('~', null, a, inst.arg1)»;''')
			case Instruction.and:
				sb.append(twoOp('&', inst.arg1, pos, a, b))
			case Instruction.or:
				sb.append(twoOp('|', inst.arg1, pos, a, b))
			case Instruction.xor:
				sb.append(twoOp('^', inst.arg1, pos, a, b))
			case Instruction.plus:
				sb.append(twoOp('+', inst.arg1, pos, a, b))
			case Instruction.minus:
				sb.append(twoOp('-', inst.arg1, pos, a, b))
			case Instruction.mul:
				sb.append(twoOp('*', inst.arg1, pos, a, b))
			case Instruction.mod:
				sb.append(twoOp('%', inst.arg1, pos, a, b))
			case Instruction.pow:
			//it is always 2**x which is the same as 1<<(x-1)
				sb.append('''long t«pos»=1 << (t«a»-1);''')
			case Instruction.div:
				sb.append(twoOp('/', inst.arg1, pos, a, b))
			case Instruction.sll:
				sb.append(twoOp('<<', inst.arg1, pos, a, b))
			case Instruction.srl:
				sb.append(twoOp('>>>', inst.arg1, pos, a, b))
			case Instruction.sra:
				sb.append(twoOp('>>', inst.arg1, pos, a, b))
			case Instruction.eq:
				sb.append('''boolean t«pos»=t«b» == t«a»;''')
			case Instruction.not_eq:
				sb.append('''boolean t«pos»=t«b» != t«a»;''')
			case Instruction.less:
				sb.append('''boolean t«pos»=t«b» < t«a»;''')
			case Instruction.less_eq:
				sb.append('''boolean t«pos»=t«b» <= t«a»;''')
			case Instruction.greater:
				sb.append('''boolean t«pos»=t«b» > t«a»;''')
			case Instruction.greater_eq:
				sb.append('''boolean t«pos»=t«b» >= t«a»;''')
			case Instruction.isRisingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.idName(false, false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
			case Instruction.isFallingEdge:
				sb.append(
					'''«inst.arg1.asInternal.info.idName(false, false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);''')
		}
		sb.append(
			'''//«inst»
				''')
	}

	def protected  String twoOp(String op, int targetSizeWithType, int pos, int a, int b) '''long t«pos»=«twoOpValue(op, null, a, b,
		targetSizeWithType)»;'''

	def protected  init(VariableInformation info) {
		if (info.dimensions.length == 0)
			return ''''''
		var size = 1
		for (d : info.dimensions) {
			size = size * d
		}
		'''
			«info.idName(false, false)»=new «info.javaType»[«size»];
			«IF info.isRegister»«info.idName(false, false)»$reg=new «info.javaType»[«size»];«ENDIF»
		'''
	}

	def protected  getJavaType(InternalInformation ii) {
		val jt = ii.info.javaType
		if (ii.arrayIdx.length != ii.info.dimensions.length)
			return jt + "[]"
		return jt
	}

	def protected  getJavaType(VariableInformation information) {
		if (information.name.startsWith(InternalInformation.PRED_PREFIX) || information.type===VariableInformation.Type.BOOL)
			return "boolean"
		return "long"
	}

	def protected  decl(VariableInformation info, Boolean includePrev) '''
		«IF info.isPredicate || (prevMap.get(info.name) !== null && prevMap.get(info.name))»public long «info.idName(false,
			false)»_update=0;«ENDIF»
		public «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.idName(false, false)»;
		«IF includePrev !== null && includePrev»private «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.idName(true,
			false)»;«ENDIF»
		«IF info.isRegister»private «info.javaType»«FOR d : info.dimensions»[]«ENDFOR» «info.idName(false, false)»$reg;«ENDIF»
	'''

	def protected  getImports() '''
		import java.util.*;
		import java.math.*;
		import org.pshdl.interpreter.*;
		«IF debug»
			import org.pshdl.interpreter.frames.*;
		«ENDIF»
	'''

	override getHookName() {
		return "Java"
	}

	override getUsage() {
		val options = new Options;
		options.addOption('p', "pkg", true,
			"The package the generated source will use. If non is specified the package from the module is used")
		options.addOption('d', "debug", false,
			"If debug is specified, the source will contain support for a IDebugListener")
		return new MultiOption("Options for the " + hookName + " type:", null, options)
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		val moduleName = em.moduleName
		val li = moduleName.lastIndexOf('.')
		var String pkg = null
		val optionPkg = cli.getOptionValue("pkg")
		var boolean debug = cli.hasOption("debug")
		if (optionPkg !== null) {
			pkg = optionPkg
		} else if (li != -1) {
			pkg = moduleName.substring(0, li - 1)
		}
		val unitName = moduleName.substring(li + 1, moduleName.length);
		doCompile(syntaxProblems, em, pkg, unitName, debug);
	}
	
	override CharSequence createChangeAdapter(String packageName, String unitName)
'''«IF packageName !== null»package «packageName»;«ENDIF»

import org.pshdl.interpreter.IChangeListener;
import org.pshdl.interpreter.IHDLInterpreter;
import java.math.BigInteger;

public class ChangeAdapter«unitName» implements IHDLInterpreter{
	«FOR v : em.variables.excludeNull»
		«v.decl(prevMap.get(v.name))»
	«ENDFOR»
	
	private «unitName» module;
	private IChangeListener[] listeners;
	public ChangeAdapter«unitName»(«unitName» module, IChangeListener ... listeners) {
		this.module=module;
		this.listeners=listeners;
	}
	
	@Override
	public void run() {
		module.run();
		«FOR varInfo:em.variables.excludeNull»
			«varInfo.changedNotification»
			«varInfo.idName(false, false)»=module.«varInfo.idName(false, false)»;
		«ENDFOR»
	}
	
	@Override
	public void initConstants() {
		module.initConstants();
	}

	@Override
	public void setInput(String name, long value, int... arrayIdx) {
		module.setInput(name, value, arrayIdx);
	}

	@Override
	public void setInput(int idx, long value, int... arrayIdx) {
		module.setInput(idx, value, arrayIdx);
	}

	@Override
	public int getIndex(String name) {
		return module.getIndex(name);
	}

	@Override
	public String getName(int idx) {
		return module.getName(idx);
	}

	@Override
	public long getOutputLong(String name, int... arrayIdx) {
		return module.getOutputLong(name, arrayIdx);
	}

	@Override
	public long getOutputLong(int idx, int... arrayIdx) {
		return module.getOutputLong(idx, arrayIdx);
	}

	@Override
	public long getDeltaCycle() {
		return module.getDeltaCycle();
	}
	@Override
	public void close() throws Exception{
		module.close();
	}
	
	@Override
	public void setFeature(Feature feature, Object value) {
		module.setFeature(feature, value);
	}
}
'''
	
	def protected  changedNotification(VariableInformation vi) {
		val varName = vi.idName(false, false)
		if (!vi.array){
			if (vi.predicate){
				val varNameUpdate = vi.idName(false, false)+"_update"
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), "«vi.name»", «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), "«vi.name»", «varName» «IF vi.width != 64» & «vi.width.asMaskL»«ENDIF», module.«varName» «IF vi.width != 64» & «vi.width.asMaskL»«ENDIF»);
				'''
			}
		} else {
			if (vi.predicate){
				val varNameUpdate = vi.idName(false, false)+"_update"
				return '''if (!module.«varName».equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicateArray(getDeltaCycle(), "«vi.name»", «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (!module.«varName».equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedLongArray(getDeltaCycle(), "«vi.name»", «varName», module.«varName»);
				'''
			}
		}
	}
}
