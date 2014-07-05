package org.pshdl.model.simulation

import java.math.BigInteger
import java.util.EnumSet
import java.util.List
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation

import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.*

class JavaCodeGenerator extends CommonCodeGenerator {

	String packageName
	String unitName

	new(ExecutableModel em, String packageName, String unitName, int maxCosts) {
		super(em, 64, maxCosts)
		this.packageName = packageName
		this.unitName = unitName
	}

	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(baseType)){
			if (isBoolean(varInfo, attributes))
				return "boolean "
			return "long "
		} else {
			if (isBoolean(varInfo, attributes))
				return "boolean[] "
			return "long[] "
		}
	}

	override protected preField(VariableInformation x, EnumSet<Attributes> attributes) '''«IF attributes.
		contains(Attributes.isPublic)»public«ELSE»private«ENDIF» '''

	override protected footer() 
'''
	«hdlInterpreter»
}'''

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
«««					«IF v.dimensions.length == 0»
«««						case «varIdx.get(v.name)»: 
«««							«assignVariable(v, "value", NONE, true, false)»
«««							break;
«««					«ELSE»
«««						case «varIdx.get(v.name)»: 
«««							«idName(v, true, NONE)»[«calculateVariableAccessIndex((0..<v.dimensions.length).toList, v, EnumSet.of(Attributes.isArray))»]
«««							break;
«««					«ENDIF»
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
«««					«IF v.dimensions.length == 0»
«««						case «varIdx.get(v.name)»: return «v.idName(false, NONE)»«IF v.predicate»?1:0«ELSEIF v.width != 64» & «v.width.mask»«ENDIF»;
«««					«ELSE»
«««						case «varIdx.get(v.name)»: return «v.idName(false, false)»[«v.arrayAccessArrIdx»]«IF v.width != 64 &&
«««			!v.predicate» & «v.width.asMaskL»«ENDIF»;
«««					«ENDIF»
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
			return (int)deltaCycle;
		}
	'''


	override protected header() '''
		«IF packageName !== null»package «packageName»;«ENDIF»
		«imports»
		
		public class «unitName»
		 implements «IF em.annotations.contains(HDLSimulator.TB_UNIT.name.substring(1))»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»
		{
			private Map<String, Integer> varIdx=new HashMap<String, Integer>();
	'''

	protected override postFieldDeclarations() 
'''	«IF maxCosts!=Integer.MAX_VALUE»public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());«ENDIF»
	«IF hasClock»
			private Set<RegUpdate> regUpdates=new LinkedHashSet<RegUpdate>();
			
			/**
			* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false
			*/
			public «unitName»() {
				this(«IF hasClock»false, false«ENDIF»);
			}
			
			public «unitName»(boolean «DISABLE_EDGES.name», boolean «DISABLE_REG_OUTPUTLOGIC.name») {
				this.«DISABLE_EDGES.name»=«DISABLE_EDGES.name»;
				this.«DISABLE_REG_OUTPUTLOGIC.name»=«DISABLE_REG_OUTPUTLOGIC.name»;
				«FOR v : em.variables.excludeNull»
					varIdx.put("«v.name»", «varIdx.get(v.name)»);
				«ENDFOR»
			}
	«ELSE»
			public «unitName»() {
				«FOR v : em.variables.excludeNull»
					varIdx.put("«v.name»", «varIdx.get(v.name)»);
				«ENDFOR»
			}
	«ENDIF»
	«IF hasClock»
			public boolean skipEdge(long local) {
				long dc = local >>> 16l;
				// Register was updated in previous delta cylce, that is ok
				if (dc < «DELTA_CYCLE.name»)
					return false;
				// Register was updated in this delta cycle but it is the same eps,
				// that is ok as well
				if ((dc == «DELTA_CYCLE.name») && ((local & 0xFFFF) == «EPS_CYCLE.name»))
					return false;
				// Don't update
				return true;
			}
			
			«copyRegs»
	«ENDIF»
'''
	def copyRegs() '''
		private void updateRegs() {
			for (RegUpdate reg : regUpdates) {
				switch (reg.internalID) {
					«FOR v : em.variables»
						«IF v.isRegister»
							case «varIdx.get(v.name)»: 
							«IF v.dimensions.length == 0»
								«v.idName(true, NONE)» = «v.idName(true, NONE)»$reg; break;
							«ELSE»
								if (reg.offset!=-1)
									«v.idName(true, NONE)»[reg.offset] = «v.idName(true, NONE)»$reg[reg.offset];
								else
									Arrays.fill(«v.idName(true, NONE)», «v.idName(true, NONE)»$reg[0]); 
								break;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				}
			}
		}
	'''
	def getImports() '''
		import java.util.*;
		import java.math.*;
		import org.pshdl.interpreter.*;
		import java.util.concurrent.*;
	'''

	override protected calculateVariableAccessIndex(List<Integer> arr, VariableInformation varInfo) {
		val res = super.calculateVariableAccessIndex(arr, varInfo)
		if (res.length===0)
			return res
		return "(int)("+res+")"
	}

	override protected arrayInit(VariableInformation varInfo, BigInteger zero, EnumSet<Attributes> attributes) {
		val attrClone=attributes.clone
		attrClone.add(baseType)
		return '''new «varInfo.fieldType(attrClone)»[«varInfo.arraySize»]'''
	}

	override protected functionFooter(Frame frame) '''}
		'''

	override protected functionHeader(Frame frame) '''
		private final void «frame.frameName»() {
	'''
	
	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset) '''
	if («cpyName»!=«last»)
	«indent()»	regUpdates.add(new RegUpdate(«varIdx.get(outputInternal.info.name)», «offset»));'''
	
	override protected runMethodsHeader(boolean constant) '''public void «IF !constant»run«ELSE»initConstants«ENDIF»() {
	'''
		
	override protected runMethodsFooter(boolean constant) '''}
	'''
	
	override protected callStage(int stage, boolean constant) '''«stageMethodName(constant, stage)»();
	'''
	
	protected def stageMethodName(boolean constant, int stage)
		'''«IF constant»const_«ENDIF»stage«String.format("%04d", stage)»'''
	
	
	override protected stageMethodsFooter(int stage, int stageCosts, boolean constant)'''}
			'''
	
	override protected barrierBegin(int stage, int totalStageCosts, boolean constant) {
	val res='''List<Callable<Void>> calls=new ArrayList<Callable<Void>>();
	«indent()»calls.add(new Callable<Void>() {
	«indent()»@Override public Void call() {
	'''
	indent+=2;
	return res
}
	
	override protected barrierEnd(int stage, int totalStageCosts, boolean constant){
	indent-=2;
val res=
'''return null;
«indent()»}
«indent()»});
«indent()»try {
«indent()»	mainPool.invokeAll(calls);
«indent()»} catch (final InterruptedException e) {
«indent()»	new RuntimeException(e);
«indent()»}
'''
return res
	} 
	
	override protected barrier() {
		indent-=2
		val res='''
			return null;
		«indent()»	}
		«indent()»});
		calls.add(new Callable<Void>() {
		«indent()»@Override public Void call() {
		'''
		indent+=2
		return res
	}
	
	override protected stageMethodsHeader(int stage, int stageCosts, boolean constant) '''public void «stageMethodName(constant, stage)»(){
		'''
		
		override protected applyRegUpdates() {
			return "updateRegs();"
		}
		
		override protected checkRegupdates() {
			return "!regUpdates.isEmpty()"
		}
		
}
