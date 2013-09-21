package org.pshdl.model.utils

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import com.google.common.collect.Sets
import java.util.LinkedHashMap
import java.util.Map
import java.util.Set
import org.pshdl.model.HDLRegisterConfig
import org.pshdl.model.HDLStatement
import org.pshdl.model.HDLAssignment
import org.pshdl.model.HDLReference
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.HDLResolvedRef
import org.pshdl.model.HDLBlock
import java.util.concurrent.atomic.AtomicInteger
import org.pshdl.model.HDLForLoop
import org.pshdl.model.HDLSwitchCaseStatement
import org.pshdl.model.HDLSwitchStatement
import java.util.LinkedList
import java.util.HashSet
import com.google.common.collect.Maps
import com.google.common.collect.Lists
import java.util.List
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess
import java.util.ArrayList
import org.pshdl.model.IHDLObject
import java.util.Collection
import org.pshdl.model.HDLIfStatement
import org.pshdl.model.HDLUnit
import org.pshdl.model.HDLDeclaration
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.extensions.StringWriteExtension
import org.pshdl.model.HDLInstantiation

class ProcessModel {

	private static AtomicInteger ai = new AtomicInteger();
	public Multimap<Integer, HDLStatement> unclockedStatements = LinkedListMultimap.create();
	public Multimap<HDLRegisterConfig, HDLStatement> clockedStatements = LinkedListMultimap.create();

	public static final int DEF_PROCESS = -1

	def static ProcessModel toProcessModel(HDLUnit stmnt) {
		val pm = new ProcessModel()
		stmnt.getAllObjectsOf(typeof(HDLStatement), false).forEach[s|pm.merge(s.toProcessModel(DEF_PROCESS))]
		return pm
	}

	dispatch static def ProcessModel toProcessModel(HDLFunctionCall stmnt, int pid) {
		new ProcessModel().addUnclocked(pid,stmnt)
	}

	dispatch static def ProcessModel toProcessModel(HDLInstantiation stmnt, int pid) {
	}
	dispatch static def ProcessModel toProcessModel(HDLDeclaration stmnt, int pid) {
	}

	dispatch static def ProcessModel toProcessModel(HDLStatement stmnt, int pid) {
		throw new RuntimeException("Not implemented for statement:" + stmnt)
	}

	dispatch static def ProcessModel toProcessModel(HDLBlock stmnt, int pid) {
		val newPid = if(stmnt.process) ai.andIncrement else pid
		return stmnt.toProcessModel(HDLBlock.fStatements, newPid)
	}

	dispatch static def ProcessModel toProcessModel(HDLForLoop stmnt, int pid) {
		return stmnt.toProcessModel(HDLForLoop.fDos, pid)
	}

	dispatch static def ProcessModel toProcessModel(HDLSwitchCaseStatement stmnt, int pid) {
		return stmnt.toProcessModel(HDLSwitchCaseStatement.fDos, pid)
	}

	private static def <T extends HDLStatement, V extends HDLStatement, C extends Collection<V>> ProcessModel toProcessModel(
		T obj, HDLFieldAccess<T, C> field, int pid) {
		val pm = new ProcessModel
		for (V subStmnt : field.getValue(obj)) {
			pm.merge(subStmnt.toProcessModel(pid))
		}
		val res = new ProcessModel
		pm.unclockedStatements.asMap.forEach[subPid, stmnts|
			res.unclockedStatements.put(subPid, field.setValue(obj, new ArrayList(stmnts) as C))]
		pm.clockedStatements.asMap.forEach[reg, stmnts|
			res.clockedStatements.put(reg, field.setValue(obj, new ArrayList(stmnts) as C))]
		return res
	}

	dispatch static def ProcessModel toProcessModel(HDLIfStatement stmnt, int pid) {
		val thenPM = new ProcessModel
		stmnt.thenDo.forEach([s|thenPM.merge(s.toProcessModel(pid))])
		val elsePM = new ProcessModel
		stmnt.elseDo.forEach([s|elsePM.merge(s.toProcessModel(pid))])
		val clocks = new HashSet<HDLRegisterConfig>
		clocks.addAll(thenPM.clockedStatements.keySet)
		clocks.addAll(elsePM.clockedStatements.keySet)
		val res = new ProcessModel
		if (!thenPM.unclockedStatements.empty || !elsePM.unclockedStatements.empty) {
			res.addUnclocked(pid, stmnt.setThenDo(thenPM.getUnclocked(pid)).setElseDo(elsePM.getUnclocked(pid)))
		}
		for (HDLRegisterConfig reg : clocks) {
			res.addClocked(reg, stmnt.setThenDo(thenPM.getClocked(reg)).setElseDo(elsePM.getClocked(reg)))
		}
		return res
	}

	dispatch static def ProcessModel toProcessModel(HDLSwitchStatement stmnt, int pid) {
		val Map<HDLSwitchCaseStatement, ProcessModel> pms = Maps::newLinkedHashMap
		val clocks = new HashSet<HDLRegisterConfig>
		var hasUnclocked = false
		for (HDLSwitchCaseStatement caze : stmnt.cases) {
			val casePM = caze.toProcessModel(pid)
			if (!casePM.unclockedStatements.empty)
				hasUnclocked = true
			clocks.addAll(casePM.clockedStatements.keySet)
			pms.put(caze, casePM)
		}
		val res = new ProcessModel
		if (hasUnclocked) {
			val List<HDLSwitchCaseStatement> newCases = Lists.newLinkedList()
			pms.forEach[caze, caseStatements|newCases.add(caze.setDos(caseStatements.unclockedStatements.get(pid)))]
			res.addUnclocked(pid, stmnt.setCases(newCases))
		}
		for (HDLRegisterConfig reg : clocks) {
			val List<HDLSwitchCaseStatement> newCases = Lists.newLinkedList()
			pms.forEach[caze, caseStatements|newCases.add(caze.setDos(caseStatements.clockedStatements.get(reg)))]
			res.addClocked(reg, stmnt.setCases(newCases))
		}
		return res
	}

	dispatch static def ProcessModel toProcessModel(HDLAssignment stmnt, int pid) {
		val HDLReference ref = stmnt.left
		if (ref instanceof HDLUnresolvedFragment)
			throw new RuntimeException("Not implemented for HDLUnresolvedFragment:" + stmnt)
		val HDLResolvedRef rRef = ref as HDLResolvedRef;
		val hVar = rRef.resolveVar.get
		val regConfig = hVar.registerConfig
		if (hVar.registerConfig !== null)
			return new ProcessModel().addClocked(regConfig, stmnt)
		return new ProcessModel().addUnclocked(pid, stmnt)
	}

	def merge(ProcessModel model) {
		if (model !== null) {
			unclockedStatements.putAll(model.unclockedStatements)
			clockedStatements.putAll(model.clockedStatements)
		}
		return this
	}

	def getClocked(HDLRegisterConfig reg) {
		return clockedStatements.get(reg)
	}

	def getUnclocked(int pid) {
		return unclockedStatements.get(pid)
	}

	def addUnclocked(int pid, HDLStatement stmnt) {
		unclockedStatements.put(pid, stmnt)
		return this
	}

	def addClocked(HDLRegisterConfig config, HDLStatement stmnt) {
		clockedStatements.put(config, stmnt)
		return this
	}

	override toString() '''
<------------------>
Unclocked Statements:
«FOR e : unclockedStatements.asMap.entrySet»
	Process:«e.key»
	«FOR s : e.value»
		«StringWriteExtension.asString(s, SyntaxHighlighter.none)»
	«ENDFOR»
«ENDFOR»

<------------------>
Clocked Statements:
«FOR e : clockedStatements.asMap.entrySet»
	Process:«e.key»
	«FOR s : e.value»
		«StringWriteExtension.asString(s, SyntaxHighlighter.none)»
	«ENDFOR»
«ENDFOR»
	'''

}
