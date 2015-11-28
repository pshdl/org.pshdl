/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 * 
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model.simulation.codegenerator

import com.google.common.base.Predicate
import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Lists
import com.google.common.collect.Multimap
import com.google.common.collect.Sets
import java.math.BigInteger
import java.util.Collections
import java.util.EnumSet
import java.util.Iterator
import java.util.List
import java.util.Random
import java.util.Set
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.FunctionInformation
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.ParameterInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation.Type
import org.pshdl.model.simulation.HDLSimulator
import org.pshdl.model.simulation.ITypeOuptutProvider
import org.pshdl.model.simulation.SimulationTransformationExtension
import org.pshdl.model.utils.PSAbstractCompiler
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem

import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.*
import static org.pshdl.model.simulation.codegenerator.CommonCodeGenerator.Attributes.*

class JavaCodeGeneratorParameter extends CommonCodeGeneratorParameter {
	@Option(description="The name of the package that should be declared. If unspecified, the package of the module will be used", optionName="pkg", hasArg=true)
	public String packageName;
	@Option(description="The name of the java class. If not specified, the name of the module will be used", optionName="pkg", hasArg=true)
	public String unitName;
	@Option(description="Generate a JUnit based test-bench", optionName="junit", hasArg=false)
	public boolean junit = false;

	public int executionCores = 0

	new(ExecutableModel em) {
		super(em, 64)
		val moduleName = em.moduleName
		val li = moduleName.lastIndexOf('.')
		this.packageName = null
		if (li != -1) {
			this.packageName = moduleName.substring(0, li)
		}
		this.unitName = moduleName.substring(li + 1, moduleName.length);
	}

	public def JavaCodeGeneratorParameter setPackageName(String packageName) {
		this.packageName = packageName
		return this
	}

	public def JavaCodeGeneratorParameter setJUnitGeneration(boolean generateJUnit) {
		this.junit = generateJUnit
		return this
	}

	public def JavaCodeGeneratorParameter setUnitName(String unitName) {
		this.unitName = unitName
		return this
	}

	public def String javaChangeAdapterName(boolean useInterface) {
		if (packageName == null)
			return changeAdapterName(useInterface)
		return '''«packageName».«changeAdapterName(useInterface)»'''
	}

	public def String changeAdapterName(boolean useInterface) {
		return (if(useInterface) "GenericChangeAdapter" else "ChangeAdapter") + unitName
	}

	public def String javaClassName() {
		if (packageName == null)
			return unitName
		return '''«packageName».«unitName»'''
	}

}

class JavaCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {

	String packageName
	String unitName
	int executionCores = 0
	boolean junit = false
	boolean hasPow = false

	new() {
	}

	new(JavaCodeGeneratorParameter parameter) {
		super(parameter)
		this.packageName = parameter.packageName
		this.unitName = parameter.unitName
		this.executionCores = parameter.executionCores
		this.junit = parameter.junit
	}

	override JavaCodeGeneratorParameter getParameter() {
		return super.parameter as JavaCodeGeneratorParameter
	}

	override protected void postBody() {
		indent--;
	}

	override protected void preBody() {
		indent++;
	}

	override protected fieldType(VariableInformation varInfo, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		var res = "long"
		if (varInfo.type === Type.STRING)
			res = "String"
		if (isBoolean(varInfo, attributes))
			res = "boolean"
		if (varInfo.array && !attributes.contains(baseType)) {
			return res + "[]"
		}
		return res
	}

	override protected preField(VariableInformation x, EnumSet<CommonCodeGenerator.Attributes> attributes) '''«IF attributes.
		contains(CommonCodeGenerator.Attributes.isPublic)»public«ELSE»private«ENDIF» '''

	override protected footer() '''
		@Override
		public void setInput(String name, long value, int... arrayIdx) {
			setInput(getIndex(name), value, arrayIdx);
		}
		
		@Override
		public void setInput(int idx, long value, int... arrayIdx) {
			switch (idx) {
				«setInputCases("value", null, EnumSet.of(Attributes.isArrayArg))»
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
		
		«««		«FOR v: em.variables»«getterSetter(v, if (v.aliasVar!==null) v.aliaVVrr else v)»«ENDFOR»
		
		@Override
		public String getName(int idx) {
			switch (idx) {
			«FOR v : em.variables»
			case «v.getVarIdx(false)»: return "«v.name»";
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
				«getOutputCases(null, EnumSet.of(Attributes.isArrayArg))»
				default:
					throw new IllegalArgumentException("Not a valid index:" + idx);
			}
		}
		
		@Override
		public long getDeltaCycle() {
			return deltaCycle;
		}
		
		@Override
		public void close(){
		}
		
		@Override
		public VariableInformation[] getVariableInformation(){
			VariableInformation[] res=new VariableInformation[«em.variables.length»];
			«var int count=0»
			«FOR v : em.variables»
				res[«count++»]=new VariableInformation(VariableInformation.Direction.«v.dir.name», "«v.name»", «v.width», VariableInformation.Type.«v.type.name», «v.isRegister», «v.isClock», «v.isReset», new String[]{«v.annotations?.join('"',",",'"',[it])»}, new int[]{«v.dimensions?.join(",")»});
			«ENDFOR»
			return res;
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
		«IF executionCores!=0»
			«makeThreads»
		«ENDIF»
		«IF hasPow»
			private static long pow(long a, long n) {
				long result = 1;
				long p = a;
				while (n > 0) {
					if ((n % 2) != 0) {
						result = result * p;
					}
					p = p * p;
					n = n / 2;
				}
				return result;
			}
		«ENDIF»
		
		«IF !em.functions.empty»
			public JavaPSHDLLib pshdl=new JavaPSHDLLib(this);
			«generateInlineMethods»
		«ENDIF»
		}
	'''

	def generateInlineMethods() {
		val StringBuilder sb = new StringBuilder
		for (FunctionInformation fi : em.functions) {
			if (fi.name.startsWith("pshdl.")) {
				sb.
					append('''
					private «fi.returnType.toJava» «fi.signature»(«FOR ParameterInformation pi : fi.parameter SEPARATOR ', '»«pi.toJava» p«pi.name.toFirstUpper»«ENDFOR»){
						«IF fi.returnType!==null»return «ENDIF»pshdl.«fi.name.substring(fi.name.lastIndexOf('.')+1)»(«FOR ParameterInformation pi : fi.parameter SEPARATOR ', '»p«pi.name.toFirstUpper»«ENDFOR»);
					}''')
			}
		}
		return sb
	}

	def toJava(ParameterInformation information) {
		if (information === null)
			return "void"
		if (information.type === ParameterInformation.Type.PARAM_BOOL)
			return "boolean"
		if (information.type === ParameterInformation.Type.PARAM_STRING)
			return "String"
		return "long"
	}

	protected def getterSetter(VariableInformation v, VariableInformation aliased) '''
		public «v.fieldType(NONE)» get«v.idName(false, NONE).toString.toFirstUpper»(){
			«IF aliased.predicate || aliased.array»
				return «aliased.idName(true, NONE)»;
			«ELSE»
				return «aliased.idName(true, NONE).fixupValue(aliased.targetSizeWithType, true)»;
			«ENDIF»
		}
		
		public void set«v.idName(false, NONE).toString.toFirstUpper»(«v.fieldType(NONE)» newVal){
			«IF aliased.predicate || aliased.array»
				«aliased.idName(true, NONE)»=newVal;
			«ELSE»
				«aliased.idName(true, NONE)»=«"newVal".fixupValue(aliased.targetSizeWithType, true)»;
			«ENDIF»
		}
	'''

	static class ExecutionPhase {
		static int globalID = 0
		public final int id = globalID++
		public final StringBuilder executionCore = new StringBuilder

		public int stage

		new(int stage) {
			this.stage = stage
		}

		public def declare() '''
			«field»
			public final void «methodName»(){
				«executionCore»
				execPhase«id» = «TIMESTAMP.name»;
			}
		'''

		public def methodName() '''stage«String.format("%03x", stage)»phase«String.format("%04x", id)»'''

		public def call() '''
			if (execPhase«id» != «TIMESTAMP.name»){
				«methodName»();
			}
		'''

		public def field() '''long execPhase«id»=-1;
		'''
	}

	Random r = new Random()

	def makeThreads() {
		val Set<Integer> handledNegEdges = Sets.newLinkedHashSet
		val Set<Integer> handledPosEdges = Sets.newLinkedHashSet
		val Set<Integer> handledPredicates = Sets.newLinkedHashSet
		val Multimap<Integer, Frame> stageFrames = LinkedHashMultimap.create
		em.frames.filter[!constant].forEach[stageFrames.put(it.scheduleStage, it)]
		var int maxStage = stageFrames.keySet.sort.last
		val ctrlThread = new StringBuilder
		val phaseMethods = new StringBuilder
		val List<StringBuilder> execution = Lists.newArrayListWithCapacity(executionCores)
		for (int i : 0 ..< executionCores) {
			execution.add(new StringBuilder)
		}
		for (int stage : 0 ..< maxStage) {
			val matchingFrames = stageFrames.get(stage)
			if (!matchingFrames.nullOrEmpty) {
				for (Frame frame : matchingFrames) {
					val negEdge = handleEdge(handledNegEdges, false, frame.edgeNegDepRes)
					if (!negEdge.toString.empty) {
						ctrlThread.append(negEdge)
					}
					val posEdge = handleEdge(handledPosEdges, true, frame.edgePosDepRes)
					if (!posEdge.toString.empty) {
						ctrlThread.append(posEdge)
					}
					val negPred = handlePredicates(handledPredicates, false, frame.predNegDepRes)
					if (!negPred.toString.empty) {
						ctrlThread.append(negPred)
					}
					val posPred = handlePredicates(handledPredicates, true, frame.predPosDepRes)
					if (!posPred.toString.empty) {
						ctrlThread.append(posPred)
					}
				}
				addBarrier(ctrlThread, execution, "Predicates for stage:" + stage, stage)
				var int totalCosts = 0
				var ExecutionPhase current = new ExecutionPhase(stage)
				val List<ExecutionPhase> phases = Lists.newArrayList(current)
				for (val Iterator<Frame> iterator = matchingFrames.iterator(); iterator.hasNext();) {
					val Frame frame = iterator.next()
					if (!(purgeAliases && frame.isRename(em))) {
						totalCosts += estimateFrameCosts(frame)
						val call = predicateCheckedFrameCall(frame)
						current.executionCore.append(call)
						if (totalCosts > 10 && iterator.hasNext) {
							totalCosts = 0
							current = new ExecutionPhase(stage)
							phases.add(current)
						}
					}
				}
				phaseMethods.append(phases.map[declare].join)
				ctrlThread.append(phases.map[call].join)
				for (int i : 0 ..< executionCores) {
					Collections.shuffle(phases, r)
					execution.get(i).append(phases.map[call].join)
				}
			}
		}

		val StringBuilder res = new StringBuilder
		for (int tid : 0 ..< executionCores) {
			res.append(
				'''
					private final Thread thread«tid»=new Thread(){
						public void run(){
							long startingTimeStamp;
							while(true){
								while (phase==-1){}
								startingTimeStamp=timeStamp;
								«execution.get(tid)»
								//System.out.println("Execution of thread «tid» done");
							}
						}
					};
				'''
			)
		}
		res.append(
			'''
				«phaseMethods»
			
				private volatile int phase=-1;
				public void initParallel() {
					phase=-1;
					«FOR int tid : 0 ..< executionCores»
						thread«tid».setDaemon(true);
						thread«tid».start();
					«ENDFOR»
				}
				public void parallelExecution(){
					phase=-1;
					«ctrlThread»
					phase=-1;
					//System.out.println("Done");
				}
		''')
	}

	def addBarrier(StringBuilder master, List<StringBuilder> slaves, String stageName, int stage) {
		for (i : 0 ..< slaves.size) {
			val slave = slaves.get(i)
			slave.append(
				'''
				//«stageName»
				//System.out.println("Thread «i» waiting for next phase «stageName»");
				while (phase < «stage») {
				}
				if (startingTimeStamp!=timeStamp)
					break;
			''')
		}
		master.append(
			'''
			//System.out.println("Entering stage: «stageName»");
			try{
				Thread.sleep(1);
			} catch(Exception e){
			}
			phase=«stage»;
		''')
	}

	def schedule(List<Integer> threadLoad, List<StringBuilder> threadExecution, int costs, CharSequence execution) {
		var min = Integer.MAX_VALUE
		var tid = -1
		for (Integer i : 0 ..< threadLoad.size) {
			val load = threadLoad.get(i)
			if (load <= min) {
				min = load
				tid = i
			}
		}
		threadLoad.set(tid, threadLoad.get(tid) + costs);
		threadExecution.get(tid).append(execution)
	}

	override protected header() '''
		«IF packageName !== null»package «packageName»;«ENDIF»
		«imports»
		
		public final class «unitName»
		 implements «IF isTestbench()»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»
		{
			private Map<String, Integer> varIdx=new HashMap<String, Integer>();
	'''

	protected def isTestbench() {
		!em.annotations.nullOrEmpty && em.annotations.contains(HDLSimulator.TB_UNIT.name.substring(1))
	}

	protected override postFieldDeclarations() '''
		«IF maxCosts != Integer.MAX_VALUE»public static ExecutorService mainPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());«ENDIF»
		«IF hasClock»
			private List<RegUpdate> regUpdates=new ArrayList<RegUpdate>();
			
			/**
			* Constructs an instance with no debugging and disabledEdge as well as disabledRegOutputlogic are false
			*/
			public «unitName»() {
				this(«IF hasClock»false, false«ENDIF»);
			}
			
			public «unitName»(boolean «DISABLE_EDGES.name», boolean «DISABLE_REG_OUTPUTLOGIC.name») {
				this.«DISABLE_EDGES.name»=«DISABLE_EDGES.name»;
				this.«DISABLE_REG_OUTPUTLOGIC.name»=«DISABLE_REG_OUTPUTLOGIC.name»;
				«FOR v : em.variables»
					varIdx.put("«v.name»", «v.getVarIdx(false)»);
				«ENDFOR»
			}
		«ELSE»
			public «unitName»() {
				«FOR v : em.variables»
					varIdx.put("«v.name»", «v.getVarIdx(false)»);
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

	override protected makeCase(CharSequence caseLabel, CharSequence value, boolean includeBreak) {
		super.makeCase(caseLabel.toString.replaceAll("L", ""), value, includeBreak)
	}

	def protected copyRegs() '''
		private void updateRegs() {
			for (RegUpdate reg : regUpdates) {
				switch (reg.internalID) {
					«updateRegCases»
				}
			}
		}
	'''

	def protected getImports() '''
		import java.util.*;
		import org.pshdl.interpreter.*;
		import org.pshdl.interpreter.JavaPSHDLLib.TimeUnit;
		import java.util.concurrent.*;
		import java.util.concurrent.locks.*;
	'''

	override protected calculateVariableAccessIndex(VariableInformation varInfo,
		EnumSet<CommonCodeGenerator.Attributes> attributes) {
		val res = super.calculateVariableAccessIndex(varInfo, attributes)
		if (res.length === 0)
			return res
		return "(int)(" + res + ")"
	}

	override protected arrayInit(VariableInformation varInfo, BigInteger zero,
		EnumSet<CommonCodeGenerator.Attributes> attributes) {
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
		CharSequence offset, boolean forceRegUpdate, CharSequence fillValue) '''
	«IF !forceRegUpdate»if («cpyName»!=«last»)
	«indent()»	«ENDIF»regUpdates.add(new RegUpdate(«outputInternal.regIdx», «offset», «fillValue»));'''

	def regIdx(InternalInformation information) {
		regIdx.get(information.info.name)
	}

	override protected runMethodsHeader(boolean constant) '''public void «IF !constant»run«ELSE»initConstants«ENDIF»() {
	'''

	override protected runMethodsFooter(boolean constant) '''}
	'''

	override protected callStage(int stage, boolean constant) '''«stageMethodName(stage, constant)»();
	'''

	override protected stageMethodsFooter(int stage, int stageCosts, boolean constant) '''}
	'''

	int threadID = 0
	int currentStage = 0

	override protected barrierBegin(int stage, int totalStageCosts, boolean constant) {
		threadID = 0
		currentStage = stage
		val res = '''}
		public void t«threadID»s_«stage»(){
	'''
		return res
	}

	override protected barrierEnd(int stage, int totalStageCosts, boolean constant) {
		val res = '''}
		volatile int phase_«stage»=0;
		«FOR int i : 0 ..< threadID»
		volatile int thread_«i»_«stage»=0;
		volatile int thread_sl_«i»_«stage»=0;
		«ENDFOR»
		public void executeStage«stage»(){
			«FOR int i : 0 ..< threadID»
			Thread t«i»=new Thread(){
				public void run(){
					while(true){
						t«i»s_«stage»();
						thread_«i»_«stage»++;
						while(thread_«i»_«stage» != phase_«stage»){
							thread_sl_«i»_«stage»++;
						}
					}
				}
			};
			t«i».start();
			«ENDFOR»
			while (true){
				final long start = System.nanoTime();
				for (int i=0;i<1000;i++){
					«FOR int i : 0 ..< threadID»
					while(thread_«i»_«stage»==phase_«stage»){}
					«ENDFOR»
					phase_«stage»++;
				}
				final long end = System.nanoTime();
				System.out.println("Took: "+(end-start)/1000.+" ns "+«FOR int i : 0 ..< threadID SEPARATOR '+" "+'»thread_sl_«i»_«stage»«ENDFOR»);
				«FOR int i : 0 ..< threadID»
					thread_sl_«i»_«stage»=0;
				«ENDFOR»
			}
'''
		return res
	}

	override protected barrier() {
		threadID++
		return '''}
		public void t«threadID»s_«currentStage»(){
		'''
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

	def CharSequence createChangeAdapter(
		boolean useInterface,
		Predicate<VariableInformation> filter
	) '''«IF packageName !== null»package «packageName»;«ENDIF»
import org.pshdl.interpreter.*;
import org.pshdl.interpreter.JavaPSHDLLib.TimeUnit;
import java.util.Arrays;

/**
«FOR anno: em.annotations»
 * «anno»
«ENDFOR»
*/

public class «(parameter as JavaCodeGeneratorParameter).changeAdapterName(useInterface)» implements «IF isTestbench()»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»{
	«fieldDeclarations(false, true, filter)»
	«IF useInterface»
		«FOR varInfo : em.variables.filter(filter)»
			int «varInfo.idName(true, NONE)»_idx;
		«ENDFOR»
	«ENDIF»
	
	private «getInterpreterClassName(useInterface)» module;
	private IChangeListener[] listeners;
	private VariableInformation[] varInfos;
	public «IF useInterface»Generic«ENDIF»ChangeAdapter«unitName»(«getInterpreterClassName(useInterface)» module, IChangeListener ... listeners) {
		this.module=module;
		this.listeners=listeners;
		this.varInfos=module.getVariableInformation();
		«IF useInterface»
		«FOR varInfo : em.variables.excludeNullAndAlias.filter(filter)»
			«varInfo.idName(true, NONE)»_idx=module.getIndex("«varInfo.name»");
		«ENDFOR»
	«ENDIF»
	}
	
	@Override
	public void run() {
		module.run();
		«FOR varInfo : em.variables.excludeNullAndAlias.filter(filter)»
			«val CharSequence varName = varInfo.idName(true, NONE)»
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
	
	@Override
	public VariableInformation[] getVariableInformation() {
		return module.getVariableInformation();
	}
	
	«IF isTestbench»
		«timeMethods»
		@Override
		public void runTestbench(long maxTime, long maxSteps, ITestbenchStepListener listener, Runnable main){
			if (main == null)
				module.runTestbench(maxTime, maxSteps, listener, this);
			else
				module.runTestbench(maxTime, maxSteps, listener, main);
		}
	«ENDIF»
}
'''
	
	def getInterpreterClassName(boolean useInterface) '''«IF useInterface»«IF isTestbench»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»«ELSE»«(parameter as JavaCodeGeneratorParameter).javaClassName»«ENDIF»'''
	def protected changedNotificationInterface(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array) {
			if (vi.predicate) {
				val varNameUpdate = vi.idName(
					true,
					EnumSet.of(
						isUpdate
					)
				)
				return '''if ((module.getOutputLong(«varName»_idx)!=0) != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», (module.getOutputLong(«varName»_idx)!=0), «varNameUpdate», -1);
'''
			} else {
				return '''if (module.getOutputLong(«varName»_idx) != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», module.getOutputLong(«varName»_idx));
'''
			}
		} else {
			if (vi.predicate) {
				val varNameUpdate = vi.idName(
					true,
					EnumSet.of(
						isUpdate
					)
				)
				return '''{
boolean[] tempArr=new boolean[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx)!=0;
if (!Arrays.equals(tempArr,«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicateArray(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», tempArr, «varNameUpdate», -1);
}
'''
			} else {
				return '''{
long[] tempArr=new long[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx, i);
if (!Arrays.equals(tempArr,«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedLongArray(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», tempArr);
}
'''
			}
		}
	}

	def getIdx(VariableInformation information) {
		return getVarIdx(information, false)
	}

	def protected changedNotification(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array) {
			if (vi.predicate) {
				val varNameUpdate = vi.idName(
					true,
					EnumSet.of(
						isUpdate
					)
				)
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName»«IF vi.width != 64» & «vi.width.calcMask.
					constant(true)»«ENDIF», module.«varName»«IF vi.width != 64» & «vi.width.calcMask.constant(true)»«ENDIF»);
				'''
			}
		} else {
			if (vi.predicate) {
				val varNameUpdate = vi.idName(
					true,
					EnumSet.of(
						isUpdate
					)
				)
				return '''if (!module.«varName».equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicateArray(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (!module.«varName».equals(«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedLongArray(getDeltaCycle(), varInfos[«vi.idx»], «vi.idx», «varName», module.«varName»);
				'''
			}
		}
	}

	override protected clearRegUpdates() '''regUpdates.clear();
	'''

	override protected copyArray(VariableInformation varInfo) '''System.arraycopy(«varInfo.idName(true, NONE)», 0, «varInfo.
		idName(true, EnumSet.of(CommonCodeGenerator.Attributes.isPrev))», 0, «varInfo.arraySize»);
	'''

	override protected assignNextTime(VariableInformation nextTime,
		CharSequence currentProcessTime) '''«nextTime.name»=Math.min(«nextTime.
		name», «currentProcessTime»);'''

	override protected callMethod(
		CharSequence methodName,
		CharSequence... args
	) '''«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''

	override protected callRunMethod() '''doRun.run();
	'''

	override protected checkTestbenchListener() '''«indent()»if (listener!=null && !listener.nextStep(«varByName("$time").
		idName(true, NONE)», stepCount))
«indent()»	break;
'''

	override protected runProcessHeader(CommonCodeGenerator.ProcessData pd) {
		indent++
		'''private boolean «processMethodName(pd)»() {
		'''
	}

	override protected runTestbenchHeader() {
		indent++
		'''«timeMethods»
		@Override
		public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener, Runnable main) {
			Runnable doRun=(main==null?this:main);
			'''
	}
	
	def timeMethods() '''		@Override
		public TimeUnit getTimeBase(){
			return TimeUnit.«getAnnoValue("TimeBase")»;
		}
		
		@Override
		public long getTime(){
			return «fieldName(timeName, NONE)»;
		}
	'''
	
	protected def getAnnoValue(String anno) {
		val foundAnno = em.annotations.findFirst[startsWith(anno)]
		return foundAnno.substring(foundAnno.indexOf(SimulationTransformationExtension.ANNO_VALUE_SEP)+1)
	}

	override getHookName() {
		return "Java"
	}

	override getUsage() {
		val options = new Options;
		options.addOption('p', "pkg", true,
			"The package the generated source will use. If non is specified the package from the module is used")
		return new MultiOption("Options for the " + hookName + " type:", null, options)
	}

	override invoke(CommandLine cli, ExecutableModel em, Set<Problem> syntaxProblems) throws Exception {
		val javaParam = new JavaCodeGeneratorParameter(em)
		val optionPkg = cli.getOptionValue("pkg")
		if (optionPkg !== null) {
			javaParam.packageName = optionPkg
		}
		doCompile(syntaxProblems, javaParam);
	}

	def static doCompile(Set<Problem> syntaxProblems, JavaCodeGeneratorParameter parameter) {
		val comp = new JavaCodeGenerator(parameter)
		val code = comp.generateMainCode
		val sideFiles = Lists.newArrayList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new PSAbstractCompiler.CompileResult(syntaxProblems, code, parameter.em.moduleName, sideFiles,
				parameter.em.source, comp.hookName, true))
	}

	override protected fillArray(VariableInformation vi,
		CharSequence regFillValue) '''Arrays.fill(«vi.idName(true, NONE)», «regFillValue»);'''

	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<Attributes> attributes, boolean doMask) {
		hasPow = true
		return assignTempVar(typeFromTargetSize(targetSizeWithType), targetSizeWithType, pos,
			NONE, '''pow(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)»)''', true)
	}

}
