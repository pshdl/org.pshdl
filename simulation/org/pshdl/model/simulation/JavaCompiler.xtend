package org.pshdl.model.simulation

import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame$FastInstruction
import org.pshdl.interpreter.utils.Instruction
import java.util.Stack
import java.util.List
import java.util.LinkedList
import java.util.HashMap
import java.util.Map
import java.util.ArrayList

class JavaCompiler {
	private ExecutableModel em
	private Map<String,Integer> varIdx=new HashMap
	private Map<String,Integer> intIdx=new HashMap
	private Map<String, Boolean> prevMap = new HashMap

	new(ExecutableModel em) {
		this.em = em
		for (i:0 ..< em.variables.length){
			varIdx.put(em.variables.get(i).name,i)
		}
		for (i:0 ..< em.internals.length){
			intIdx.put(em.internals.get(i).fullName,i)
		}
		for (f : em.frames) {
			if (f.edgeNegDepRes != -1)
				prevMap.put(f.edgeNegDepRes.asInternal.info.name, true)
			if (f.edgePosDepRes != -1)
				prevMap.put(f.edgePosDepRes.asInternal.info.name, true)
		}
	}

	def static String doCompile(ExecutableModel em, String packageName, String unitName) {
		return new JavaCompiler(em).compile(packageName, unitName).toString
	}
	
	def InternalInformation asInternal(int id){
		return em.internals.get(id)
	}

	def compile(String packageName, String unitName) {

		'''
			«IF packageName!=null»package «packageName»;«ENDIF»
			«imports»
			
			public class «unitName» implements IHDLInterpreter{
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
				«FOR v : em.variables.excludeNull»
					«v.decl(prevMap.get(v.name))»
				«ENDFOR»
				private int deltaCycle=0, epsCycle=0;
				private Set<RegUpdate> regUpdates=new HashSet<RegUpdate>();
				private Map<String, Integer> varIdx=new HashMap<String, Integer>();
				private final IDebugListener listener;
				private final ExecutableModel em;
				private final boolean disableEdges;
				
				public «unitName»() {
					this(false, null, null);
				}
				
				public «unitName»(boolean disableEdges, IDebugListener listener, ExecutableModel em) {
					this.disableEdges=disableEdges;
					this.listener=listener;
					this.em=em;
					«FOR v : em.variables.excludeNull»
						«v.init»
						varIdx.put("«v.name»", «varIdx.get(v.name)»);
					«ENDFOR»
				}
				«FOR i : em.internals.excludeNull»
					«IF !i.isShadowReg»
						«IF prevMap.get(i.info.name) !== null»
							«i.getter(true)»
						«ENDIF»
						«i.getter(false)»
					«ENDIF»
					«IF !i.info.isRegister || i.isShadowReg»
						«i.setter»
					«ENDIF»
				«ENDFOR»
				«FOR f : em.frames»
					«f.method»
				«ENDFOR»
				
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
				
				public void run(){
					deltaCycle++;
					epsCycle=0;
					do {
						if (listener!=null)
							listener.startCycle(deltaCycle, epsCycle, this);
						regUpdates.clear();
						«FOR f : em.frames»
							frame«f.uniqueID»();
						«ENDFOR»
						«FOR v : em.variables.excludeNull.filter[prevMap.get(it.name)!=null]»
							«v.copyPrev»
						«ENDFOR»
						updateRegs();
						if (listener!=null && !regUpdates.isEmpty())
							listener.copyingRegisterValues(this);
						epsCycle++;
					} while (!regUpdates.isEmpty());
					if (listener!=null)
						listener.doneCycle(deltaCycle, this);
				}
				«copyRegs»
				«hdlInterpreter»
			}
		'''
	}
	
	def asMask(int width){
		val mask=(1l<<width) - 1
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
			«FOR v:em.variables.excludeNull»
				«IF v.dimensions.length==0»
				case «varIdx.get(v.name)»: 
					«IF false&&v.width!=64 && !v.predicate»value&=«v.width.asMask»;«ENDIF»
					«v.javaName(false)»=value«IF v.predicate»==0?false:true«ENDIF»;
					break;
				«ELSE»
				case «varIdx.get(v.name)»: 
					«IF false&&v.width!=64 && !v.predicate»value&=«v.width.asMask»;«ENDIF»
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
			«FOR v:em.variables.excludeNull»
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
			«FOR v:em.variables.excludeNull»
				«IF v.dimensions.length==0»
				case «varIdx.get(v.name)»: return «v.javaName(false)»«IF v.predicate»?1:0«ELSEIF false && v.width!=64» & «v.width.asMask»«ENDIF»;
				«ELSE»
				case «varIdx.get(v.name)»: return «v.javaName(false)»[«v.arrayAccessArrIdx»]«IF false && v.width!=64 && !v.predicate» & «v.width.asMask»«ENDIF»;
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

	def excludeNull(VariableInformation[] vars){
		vars.filter[name!='#null']
	}
	def excludeNull(InternalInformation[] vars){
		vars.filter[info.name!='#null']
	}

	def copyRegs() '''
				private void updateRegs() {
					for (RegUpdate reg : regUpdates) {
						switch (reg.internalID) {
							«FOR v:em.variables»
								«IF v.isRegister»
									case «varIdx.get(v.name)»: 
									«IF v.dimensions.length==0»
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

	def getter(InternalInformation info, boolean prev) {
		val sb = new StringBuilder
		val mask=info.actualWidth.asMask
		for (arr : info.arrayIdx)
			sb.append('''[«arr»]''')
		if (info.fixedArray) '''
			public «info.javaType» «info.javaName(prev)»(int frameID){
				«IF info.actualWidth == info.info.width»
					«info.javaType» val=«info.info.javaName(prev)»«sb»;
				«ELSEIF info.actualWidth == 1»
					«info.javaType» val=(«info.info.javaName(prev)»«sb» >> «info.bitStart») & 1;
				«ELSE»
					«info.javaType» val=(«info.info.javaName(prev)»«sb» >> «info.bitEnd») & «mask»;
				«ENDIF»
				«IF info.arrayIdx.length===info.info.dimensions.length»
					if (listener!=null)
						listener.loadingInternal(frameID, em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»val?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(val)«ENDIF», null);
				«ENDIF»
				return val;
			}
		''' else '''
			public «info.javaType» «info.javaName(prev)»(int frameID, «FOR int i : (0 ..< info.arrayIdx.length) SEPARATOR ','»int a«i»«ENDFOR»){
				«IF info.actualWidth == info.info.width»
					«info.javaType» val= «info.info.javaName(prev)»«FOR int i : (0 ..< info.arrayIdx.length) BEFORE '[' SEPARATOR '][' AFTER ']'»a«i»«ENDFOR»;
				«ELSEIF info.actualWidth == 1»
					«info.javaType» val= («info.info.javaName(prev)»«FOR int i:(0 ..< info.arrayIdx.length) BEFORE '[' SEPARATOR '][' AFTER ']'»a«i»«ENDFOR» >> «info.bitStart») & 1;
				«ELSE»
					«info.javaType» val= («info.info.javaName(prev)»«FOR int i : (0 ..< info.arrayIdx.length) BEFORE '[' SEPARATOR '][' AFTER ']'»a«i»«ENDFOR» >> «info.
				bitEnd») & «info.actualWidth.asMask»;
				«ENDIF»
				«IF info.arrayIdx.length===info.info.dimensions.length»
					if (listener!=null)
						listener.loadingInternal(frameID, em.internals[«intIdx.get(info.fullName)»], «IF info.isPred»val?BigInteger.ONE:BigInteger.ZERO«ELSE»BigInteger.valueOf(val)«ENDIF», null);
				«ENDIF»
				return val;
				
			}
		'''
	}

	def setter(InternalInformation info) {
		val mask = ((1l << info.actualWidth) - 1)
		val maskString = mask.toHexString
		val writeMask = (mask << (info.bitEnd)).bitwiseNot.toHexString
		val varAccess = info.info.arrayAccess
		val off=info.arrayAccess
		var fixedAccess = if (info.arrayIdx.length>0) '''[«off»]''' else ''''''
		if (info.isShadowReg)
			fixedAccess="$reg"+fixedAccess
		if (info.fixedArray) '''
			public void «info.javaName(false)»(«info.info.javaType» value){
				«IF info.actualWidth == info.info.width»
					«info.info.javaType» current=«info.info.javaName(false)»«fixedAccess»;
					«info.info.javaName(false)»«fixedAccess»=value;
				«ELSE»
					«info.info.javaType» current=«info.info.javaName(false)»«fixedAccess» & «writeMask»;
					value=((value & «maskString») << «info.bitEnd»);
					«info.info.javaName(false)»«fixedAccess»=current|value;
				«ENDIF»
				«IF info.isShadowReg»
					if (current!=value)
						regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», «off»));
				«ENDIF»
				«IF info.isPred»«info.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
			}
		''' else '''
			public void «info.javaName(false)»(«info.info.javaType» value,«FOR int i : (0 ..< info.arrayIdx.length) SEPARATOR ','»int a«i»«ENDFOR»){
				int offset=(int)«varAccess»;
				«IF info.actualWidth == info.info.width»
					«info.info.javaType» current=«info.info.javaName(false)»[offset];
					«info.info.javaName(false)»[offset]=value;
				«ELSE»
					«info.info.javaType» current=«info.info.javaName(false)»[offset] & «writeMask»;
					value=((value & «maskString») << «info.bitEnd»;
					«info.info.javaName(false)»[offset]=current|value);
				«ENDIF»
				«IF info.isShadowReg»
					if (current!=value)
						regUpdates.add(new RegUpdate(«varIdx.get(info.info.name)», offset));
				«ENDIF»
				«IF info.isPred»«info.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);«ENDIF»
			}
		'''
	}

	def arrayAccess(InternalInformation v){
		val dims = dimsLastOne(v.info)
		var off=0;
		for (i : (0..<v.arrayIdx.length)) {
			val arr=v.arrayIdx.get(i)
			val dim = dims.get(i)
			off=off+(arr*dim)
		}
		return off
	}
	def arrayAccessArrIdx(VariableInformation v){
		val varAccess=new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0..<v.dimensions.length)) {
			val dim = dims.get(i)
			if (dim!=1)
				varAccess.append('''arrayIdx[«i»]*«dim»''')
			else
				varAccess.append('''arrayIdx[«i»]''')
		}
		return varAccess
	}

	def dimsLastOne(VariableInformation v) {
		val dims = new ArrayList(v.dimensions)
		if (dims.size > 0) {
			dims.set(dims.size - 1,1);
		}
		dims
	}
	def arrayAccess(VariableInformation v){
		val varAccess=new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0..<v.dimensions.length)) {
			val dim = dims.get(i)
			if (dim!=1)
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
				if (listener!=null)
					listener.startFrame(«frame.uniqueID», deltaCycle, epsCycle, null);
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
		val last="t" + stack.pop
		if (frame.outputId.asInternal.info.name != "#null")
			sb.append(frame.outputId.setInternal(last, false, arr))
		else
			sb.append('''//Write to #null
			''')
		sb.append(
		'''
			if (listener!=null)
				listener.writingResult(«frame.uniqueID», em.internals[«frame.outputId»], BigInteger.valueOf(«last»«IF frame.outputId.asInternal.isPred»?1:0«ENDIF»), null);
	}
		''')
		return sb.toString
	}

	def toExpression(FastInstruction inst, Frame f, StringBuilder sb, int pos, int a, int b, List<Integer> arr,
		int arrPos) {
		switch (inst.inst) {
			case Instruction::pushAddIndex:
				sb.append('''int a«arr.last»=(int)t«a»;''')
			case Instruction::writeInternal:{
				if (arr.size<inst.arg1.asInternal.info.dimensions.length){
					sb.append('''Arrays.fill(«inst.arg1.asInternal.javaName(false)», t«a»);''')
				} else {
					sb.append('''«inst.arg1.asInternal.javaName(false)»«FOR ai : arr BEFORE '[' SEPARATOR '][' AFTER ']'»a«ai»«ENDFOR»=t«a»;''')
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
				val mask = (highBit - lowBit).asMask
				sb.append('''long t«pos»=(t«a» >> «lowBit») & «mask»;''')
			}
			case Instruction::cast_int: {
				if (inst.arg1 != 64) {
					sb.append(
					'''
					long c«pos»=t«a» << «64-inst.arg1»;
					long t«pos»=c«pos» >> «64-inst.arg1»;
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
			case Instruction::loadInternal:{
				val internal=inst.arg1.asInternal
				sb.append('''«internal.info.javaType» t«pos»=«inst.arg1.internal(f.uniqueID, false, arr)»;''')
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
			case Instruction::negPredicate:
				sb.append(
					'''
					«inst.arg1.asInternal.javaType» p«pos»=«inst.arg1.internal(f.uniqueID, false, arr)»;
					long up«pos»=«inst.arg1.asInternal.info.javaName(false)»_update;
					if ((up«pos»>>>16 != deltaCycle) || ((up«pos»&0xFFFF) != epsCycle)){
						if (listener!=null)
						 	listener.skippingPredicateNotFresh(«f.uniqueID», em.internals[«inst.arg1»], false, null);
						return;
					}
					if (p«pos») {
						if (listener!=null)
							listener.skippingPredicateNotMet(«f.uniqueID», em.internals[«inst.arg1»], false, p«pos»?BigInteger.ONE:BigInteger.ZERO,null); 
						return;
					}
					''')			
			case Instruction::posPredicate:
				sb.append(
					'''
					«inst.arg1.asInternal.javaType» p«pos»=«inst.arg1.internal(f.uniqueID, false, arr)»;
					long up«pos»=«inst.arg1.asInternal.info.javaName(false)»_update;
					if ((up«pos»>>>16 != deltaCycle) || ((up«pos»&0xFFFF) != epsCycle)){
						if (listener!=null)
						 	listener.skippingPredicateNotFresh(«f.uniqueID», em.internals[«inst.arg1»], true, null);
						return;
					}
					if (!p«pos») {
						if (listener!=null)
							listener.skippingPredicateNotMet(«f.uniqueID», em.internals[«inst.arg1»], true, p«pos»?BigInteger.ONE:BigInteger.ZERO,null); 
						return;
					}
					''')
			case Instruction::isRisingEdge:
				sb.append(
					'''
					if (!disableEdges){
						if ((«inst.arg1.internal(f.uniqueID, true, arr)»!=0) || («inst.arg1.internal(f.uniqueID, false, arr)»!=1)) {
							if (listener!=null)
							 	listener.skippingNotAnEdge(«f.uniqueID», em.internals[«inst.arg1»], true, null);
							return;
						}
						long p«pos»=«inst.arg1.asInternal.info.javaName(false)»_update;
						if (skipEdge(p«pos»)){
							if (listener!=null)
							 	listener.skippingHandledEdge(«f.uniqueID», em.internals[«inst.arg1»], true, null);
							return;
						}
					}
					«inst.arg1.asInternal.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);
					''')
			case Instruction::isFallingEdge:
				sb.append(
					'''
					if (!disableEdges){
						if ((«inst.arg1.internal(f.uniqueID, true, arr)»!=1) || («inst.arg1.internal(f.uniqueID, false, arr)»!=0)) {
							if (listener!=null)
							 	listener.skippingNotAnEdge(«f.uniqueID», em.internals[«inst.arg1»], false, null);
							return;
						}
						long p«pos»=«inst.arg1.asInternal.info.javaName(false)»_update;
						if (skipEdge(p«pos»)){
							if (listener!=null)
							 	listener.skippingHandledEdge(«f.uniqueID», em.internals[«inst.arg1»], false, null);
							return;
						}
					}
					«inst.arg1.asInternal.info.javaName(false)»_update=((long) deltaCycle << 16l) | (epsCycle & 0xFFFF);
					''')
		}
		sb.append(
			'''//«inst»
				''')
	}

	def constant(int id, Frame f) '''«f.constants.get(id).longValue.toHexString»'''

	def internal(int id, int uniqueID, boolean prev, List<Integer> arr) {
		val res = '''«id.asInternal.javaName(prev)»(«uniqueID»«FOR ai : arr BEFORE ',' SEPARATOR ','»a«ai»«ENDFOR»)'''
		arr.clear
		return res
	}

	def setInternal(int id, String value, boolean prev, List<Integer> arr) {
		val res = '''«id.asInternal.javaName(prev)»(«value»«FOR ai : arr BEFORE ',' SEPARATOR ','»a«ai»«ENDFOR»);'''
		arr.clear
		return res
	}

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
		val jt=ii.info.javaType
		if (ii.arrayIdx.length!=ii.info.dimensions.length)
			return jt+"[]"
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
		«IF info.isPredicate || (prevMap.get(info.name)!=null && prevMap.get(info.name))»private long «info.javaName(false)»_update=0;«ENDIF»
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
	import org.pshdl.interpreter.frames.*;
	'''

}
