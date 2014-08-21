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
package org.pshdl.model.simulation

import com.google.common.collect.Lists
import java.math.BigInteger
import java.util.Collections
import java.util.EnumSet
import java.util.List
import java.util.Set
import org.apache.commons.cli.CommandLine
import org.apache.commons.cli.Options
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.model.utils.PSAbstractCompiler
import org.pshdl.model.utils.services.IOutputProvider.MultiOption
import org.pshdl.model.validation.Problem

import static org.pshdl.model.simulation.CommonCodeGenerator.Attributes.*
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.model.simulation.CommonCodeGenerator.Attributes

class JavaCodeGenerator extends CommonCodeGenerator implements ITypeOuptutProvider {

	String packageName
	String unitName

	new(){
	}
	new(ExecutableModel em, String packageName, String unitName, int maxCosts, boolean purgeAlias) {
		super(em, 64, maxCosts, purgeAlias)
		this.packageName = packageName
		this.unitName = unitName
	}

	override protected void postBody() {
		indent--;
	}

	override protected void preBody() {
		indent++;
	}

	override protected fieldType(VariableInformation varInfo, EnumSet<CommonCodeGenerator.Attributes> attributes) {
		if (!varInfo.array || attributes.contains(baseType)) {
			if (isBoolean(varInfo, attributes))
				return "boolean"
			return "long"
		} else {
			if (isBoolean(varInfo, attributes))
				return "boolean[]"
			return "long[]"
		}
	}

	override protected preField(VariableInformation x, EnumSet<CommonCodeGenerator.Attributes> attributes) '''«IF attributes.
		contains(CommonCodeGenerator.Attributes.isPublic)»public«ELSE»private«ENDIF» '''

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
				«setInputCases("value", null)»
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
				«getOutputCases(null)»
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
		private long pow(long a, long n) {
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
	'''

	override protected header() '''
		«IF packageName !== null»package «packageName»;«ENDIF»
		«imports»
		
		public class «unitName»
		 implements «IF !em.annotations.nullOrEmpty && em.annotations.contains(HDLSimulator.TB_UNIT.name.substring(1))»IHDLTestbenchInterpreter«ELSE»IHDLInterpreter«ENDIF»
		{
			private Map<String, Integer> varIdx=new HashMap<String, Integer>();
	'''

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
				«FOR v : em.variables.excludeNull»
					varIdx.put("«v.name»", «v.getVarIdx(purgeAliases)»);
				«ENDFOR»
			}
		«ELSE»
			public «unitName»() {
				«FOR v : em.variables.excludeNull»
					varIdx.put("«v.name»", «v.getVarIdx(purgeAliases)»);
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
		super.makeCase(caseLabel.toString.replaceAll("L",""), value, includeBreak)
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
		import java.util.concurrent.*;
	'''

	override protected calculateVariableAccessIndex(List<Integer> arr, VariableInformation varInfo) {
		val res = super.calculateVariableAccessIndex(arr, varInfo)
		if (res.length === 0)
			return res
		return "(int)(" + res + ")"
	}

	override protected arrayInit(VariableInformation varInfo, BigInteger zero, EnumSet<CommonCodeGenerator.Attributes> attributes) {
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
	«indent()»	«ENDIF»regUpdates.add(new RegUpdate(«outputInternal.varIdx», «offset», «fillValue»));'''

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

	def CharSequence createChangeAdapter(boolean useInterface) '''«IF packageName !== null»package «packageName»;«ENDIF»

import org.pshdl.interpreter.IChangeListener;
import org.pshdl.interpreter.IHDLInterpreter;
import java.util.Arrays;

public class «IF useInterface»Generic«ENDIF»ChangeAdapter«unitName» implements IHDLInterpreter{
	«fieldDeclarations(false, true)»
	«IF useInterface»
		«FOR varInfo : em.variables.excludeNull»
			int «varInfo.idName(true, NONE)»_idx;
		«ENDFOR»
	«ENDIF»
	
	private «IF useInterface»IHDLInterpreter«ELSE»«unitName»«ENDIF» module;
	private IChangeListener[] listeners;
	public «IF useInterface»Generic«ENDIF»ChangeAdapter«unitName»(«IF useInterface»IHDLInterpreter«ELSE»«unitName»«ENDIF» module, IChangeListener ... listeners) {
		this.module=module;
		this.listeners=listeners;
		«IF useInterface»
		«FOR varInfo : em.variables.excludeNullAndAlias»
			«varInfo.idName(true, NONE)»_idx=module.getIndex("«varInfo.name»");
		«ENDFOR»
	«ENDIF»
	}
	
	@Override
	public void run() {
		module.run();
		«FOR varInfo : em.variables.excludeNullAndAlias»
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
}
'''

	def protected changedNotificationInterface(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array) {
			if (vi.predicate) {
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
			if (vi.predicate) {
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
				return '''{
boolean[] tempArr=new boolean[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx)!=0;
if (!Arrays.equals(tempArr,«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicateArray(getDeltaCycle(), "«vi.name»", «varName», tempArr, «varNameUpdate», -1);
}
'''
			} else {
				return '''{
long[] tempArr=new long[«vi.arraySize»];
for (int i=0;i<«vi.arraySize»;i++)
	tempArr[i]=module.getOutputLong(«varName»_idx, i);
if (!Arrays.equals(tempArr,«varName»))
	for (IChangeListener listener:listeners)
		listener.valueChangedLongArray(getDeltaCycle(), "«vi.name»", «varName», tempArr);
}
'''
			}
		}
	}

	def protected changedNotification(VariableInformation vi) {
		val varName = vi.idName(true, NONE)
		if (!vi.array) {
			if (vi.predicate) {
				val varNameUpdate = vi.idName(true, EnumSet.of(isUpdate))
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedPredicate(getDeltaCycle(), "«vi.name»", «varName», module.«varName», «varNameUpdate», module.«varNameUpdate»);
				'''
			} else {
				return '''if (module.«varName» != «varName»)
	for (IChangeListener listener:listeners)
		listener.valueChangedLong(getDeltaCycle(), "«vi.name»", «varName»«IF vi.width != 64» & «vi.width.calcMask.constant(true)»«ENDIF», module.«varName»«IF vi.
					width != 64» & «vi.width.calcMask.constant(true)»«ENDIF»);
				'''
			}
		} else {
			if (vi.predicate) {
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

	override protected copyArray(VariableInformation varInfo) '''System.arraycopy(«varInfo.idName(true, NONE)», 0, «varInfo.
		idName(true, EnumSet.of(CommonCodeGenerator.Attributes.isPrev))», 0, «varInfo.arraySize»);
		'''

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) '''«nextTime.name»=Math.min(«nextTime.
		name», «currentProcessTime»);'''

	override protected callMethod(CharSequence methodName, CharSequence... args) '''«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''

	override protected callRunMethod() '''run();
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
		'''public void runTestbench(long maxTime, long maxSteps, IHDLTestbenchInterpreter.ITestbenchStepListener listener) {
			'''
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
		val moduleName = em.moduleName
		val li = moduleName.lastIndexOf('.')
		var String pkg = null
		val optionPkg = cli.getOptionValue("pkg")
		if (optionPkg !== null) {
			pkg = optionPkg
		} else if (li != -1) {
			pkg = moduleName.substring(0, li - 1)
		}
		val unitName = moduleName.substring(li + 1, moduleName.length);
		doCompile(syntaxProblems, em, pkg, unitName, false);
	}
	
	def static doCompile(Set<Problem> syntaxProblems, ExecutableModel em, String pkg, String unitName, boolean purgeAlias) {
		val comp = new JavaCodeGenerator(em, pkg, unitName, Integer.MAX_VALUE, purgeAlias)
		val code = comp.generateMainCode
		val sideFiles=Lists.newArrayList
		sideFiles.addAll(comp.auxiliaryContent)
		return Lists.newArrayList(
			new PSAbstractCompiler.CompileResult(syntaxProblems, code, em.moduleName, sideFiles, em.source,
				comp.hookName, true))
	}
	
	override protected fillArray(VariableInformation vi, CharSequence regFillValue) '''Arrays.fill(«vi.idName(true, NONE)», «regFillValue»);'''
	
	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes, boolean doMask) {
		return assignTempVar(targetSizeWithType, pos, NONE,'''pow(«getTempName(leftOperand, NONE)», «getTempName(rightOperand, NONE)»)''' , true)
	}
	
}
