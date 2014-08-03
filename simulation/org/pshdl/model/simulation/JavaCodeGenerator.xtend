package org.pshdl.model.simulation

import java.math.BigInteger
import java.util.EnumSet
import java.util.List
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation

import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.*

class JavaCodeGenerator extends CommonCodeGenerator implements ICodeGen {

	String packageName
	String unitName

	new(ExecutableModel em, String packageName, String unitName, int maxCosts) {
		super(em, 64, maxCosts)
		this.packageName = packageName
		this.unitName = unitName
	}

	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(baseType)) {
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

	override protected footer() '''
	«hdlInterpreter»
}'''

	def protected hdlInterpreter() '''
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
							«assignVariable(v, '''«IF v.predicate»value!=0«ELSE»value«ENDIF»''', NONE, true, false)»
							break;
					«ELSE»
						case «varIdx.get(v.name)»: 
							«idName(v, true, NONE)»[«v.calculateVariableAccessIndexArr»]=«IF v.predicate»value!=0«ELSE»value«ENDIF»;
							break;
					«ENDIF»
				«ENDFOR»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public int getIndex(String name) {
			Integer idx=varIdx.get(name);
			if (idx==null)
				throw new IllegalArgumentException("The name:"+name+" is not a valid index");
			return idx;
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
						case «varIdx.get(v.name)»: return «v.idName(false, NONE)»«IF v.predicate»?1:0«ELSEIF v.width != 64» & «v.width.calcMask.constant»«ENDIF»;
					«ELSE»
						case «varIdx.get(v.name)»: return «v.idName(false, NONE)»[«v.calculateVariableAccessIndexArr»]«IF v.width != 64 &&
			!v.predicate» & «v.width.calcMask.constant»«ENDIF»;
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
				«DISABLE_REG_OUTPUTLOGIC.name» = (boolean) value;
			«ENDIF»
				break;
			case disableEdges:
			«IF hasClock»
				«DISABLE_EDGES.name» = (boolean) value;
			«ENDIF»
				break;
			}
		}
	'''

	override protected header() '''
		«IF packageName !== null»package «packageName»;«ENDIF»
		«imports»
		
		public class «unitName»
		 implements «IF !em.annotations.nullOrEmpty && em.annotations.contains(HDLSimulator.TB_UNIT.name.substring(1))»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»
		{
			private Map<String, Integer> varIdx=new HashMap<String, Integer>();
	'''

	protected override postFieldDeclarations() '''	«IF maxCosts != Integer.MAX_VALUE»public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());«ENDIF»
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
			public «unitName»(boolean «DISABLE_EDGES.name», boolean «DISABLE_REG_OUTPUTLOGIC.name») {
				this();
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

	def protected copyRegs() '''
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

	def  protected getImports() '''
		import java.util.*;
		import java.math.*;
		import org.pshdl.interpreter.*;
		import java.util.concurrent.*;
	'''

	override protected calculateVariableAccessIndex(List<Integer> arr, VariableInformation varInfo) {
		val res = super.calculateVariableAccessIndex(arr, varInfo)
		if (res.length === 0)
			return res
		return "(int)(" + res + ")"
	}

	def protected CharSequence calculateVariableAccessIndexArr(VariableInformation varInfo) {
		val int lastIndex = varInfo.dimensions.size() - 1;
		val StringBuilder arrayAccess = new StringBuilder();
		for (i : 0 ..< lastIndex) {
			if (i != 0) {
				arrayAccess.append(" + ");
			}
			arrayAccess.append(Integer.toString(varInfo.dimensions.get(i)) + ''' * arrayIdx[«i»]''');
		}
		if (lastIndex != 0) {
			arrayAccess.append(" + ");
		}
		arrayAccess.append('''arrayIdx[«lastIndex»]''');
		return arrayAccess;
	}

	override protected arrayInit(VariableInformation varInfo, BigInteger zero, EnumSet<Attributes> attributes) {
		val attrClone = attributes.clone
		attrClone.add(baseType)
		return '''new «varInfo.fieldType(attrClone)»[«varInfo.arraySize»]'''
	}

	override protected functionFooter(Frame frame) '''}
		'''

	override protected functionHeader(Frame frame) '''
		private final void «frame.frameName»() {
	'''

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean forceRegUpdate) '''
	«IF !forceRegUpdate»if («cpyName»!=«last»)
	«indent()»	«ENDIF»regUpdates.add(new RegUpdate(«varIdx.get(outputInternal.info.name)», «offset»));'''

	override protected runMethodsHeader(boolean constant) '''public void «IF !constant»run«ELSE»initConstants«ENDIF»() {
		'''

	override protected runMethodsFooter(boolean constant) '''}
		'''

	override protected callStage(int stage, boolean constant) '''«stageMethodName(stage, constant)»();
		'''

	override protected stageMethodsFooter(int stage, int stageCosts, boolean constant) '''}
		'''

	override protected barrierBegin(int stage, int totalStageCosts, boolean constant) {
		val res = '''List<Callable<Void>> calls=new ArrayList<Callable<Void>>();
	«indent()»calls.add(new Callable<Void>() {
	«indent()»@Override public Void call() {
	'''
		indent += 2;
		return res
	}

	override protected barrierEnd(int stage, int totalStageCosts, boolean constant) {
		indent -= 2;
		val res = '''return null;
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
		indent -= 2
		val res = '''
				return null;
			«indent()»	}
			«indent()»});
			calls.add(new Callable<Void>() {
			«indent()»@Override public Void call() {
		'''
		indent += 2
		return res
	}

	override protected stageMethodsHeader(int stage, int stageCosts, boolean constant) '''public void «stageMethodName(
		stage, constant)»(){
		'''

	override protected applyRegUpdates() {
		return "updateRegs();"
	}

	override protected checkRegupdates() {
		return "!regUpdates.isEmpty()"
	}
	
	override CharSequence compile(String packageName, String unitName){
		return doGenerateMainUnit();
	}
	override CharSequence createChangeAdapter(String packageName, String unitName){
		return createChangeAdapter(false)
	}
	def CharSequence createChangeAdapter(boolean useInterface)
'''«IF packageName !== null»package «packageName»;«ENDIF»

import org.pshdl.interpreter.IChangeListener;
import org.pshdl.interpreter.IHDLInterpreter;
import java.math.BigInteger;

public class «IF useInterface»Generic«ENDIF»ChangeAdapter«unitName» implements IHDLInterpreter{
	«fieldDeclarations(false)»
	«IF useInterface»
		«FOR varInfo:em.variables.excludeNull»
			int «varInfo.idName(true, NONE)»_idx;
		«ENDFOR»
	«ENDIF»
	
	private «IF useInterface»IHDLInterpreter«ELSE»«unitName»«ENDIF» module;
	private IChangeListener[] listeners;
	public «IF useInterface»Generic«ENDIF»ChangeAdapter«unitName»(«IF useInterface»IHDLInterpreter«ELSE»«unitName»«ENDIF» module, IChangeListener ... listeners) {
		this.module=module;
		this.listeners=listeners;
		«IF useInterface»
		«FOR varInfo:em.variables.excludeNull»
			«varInfo.idName(true, NONE)»_idx=module.getIndex("«varInfo.name»");
		«ENDFOR»
	«ENDIF»
	}
	
	@Override
	public void run() {
		module.run();
		«FOR varInfo:em.variables.excludeNull»
			«val CharSequence varName=varInfo.idName(true, NONE)»
			«IF useInterface»
				«varInfo.changedNotificationInterface»
				«IF varInfo.array»
					«IF varInfo.predicate»
						for (int i=0;i<«varInfo.arraySize»;i++)
							«varName»[i]=module.getOutputLong(«varName»_idx, i)!=0;
					«ELSE»
						for (int i=0;i<«varInfo.arraySize»;i++)
							«varName»[i]=module.getOutputLong(«varName»_idx, i);
					«ENDIF»
				«ELSE»
					«IF varInfo.predicate»
						«varName»=module.getOutputLong(«varName»_idx)!=0;
					«ELSE»
						«varName»=module.getOutputLong(«varName»_idx);
					«ENDIF»
				«ENDIF»
			«ELSE»
				«varInfo.changedNotification»
				«varName»=module.«varName»;
			«ENDIF»
		«ENDFOR»
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
	public void initConstants() {
		module.initConstants();
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
	
	def protected changedNotificationInterface(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array){
			if (vi.predicate){
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
				return '''if ((module.getOutputLong(«varName»_idx)!=0) != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), "«vi.name»", «varName», (module.getOutputLong(«varName»_idx)!=0), «varNameUpdate», -1);
'''
			} else {
				return '''if (module.getOutputLong(«varName»_idx) != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), "«vi.name»", «varName», module.getOutputLong(«varName»_idx));
'''
			}
		} else {
			if (vi.predicate){
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
				return '''{
boolean[] tempArr=new boolean[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx)!=0;
if (!tempArr.equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicateArray(getDeltaCycle(), "«vi.name»", «varName», tempArr, «varNameUpdate», -1);
}
'''
			} else {
				return '''{
long[] tempArr=new long[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx, i);
if (!tempArr.equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedLongArray(getDeltaCycle(), "«vi.name»", «varName», tempArr);
}
'''
			}
		}
	}
	def protected changedNotification(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array){
			if (vi.predicate){
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), "«vi.name»", «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), "«vi.name»", «varName»«IF vi.width != 64» & «vi.width.calcMask.constant»«ENDIF», module.«varName»«IF vi.width != 64» & «vi.width.calcMask.constant»«ENDIF»);
				'''
			}
		} else {
			if (vi.predicate){
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
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
	
	override protected clearRegUpdates() '''regUpdates.clear();
	'''

}
