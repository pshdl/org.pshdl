package org.pshdl.model.simulation.codegenerator

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import java.math.BigInteger
import java.util.EnumSet
import java.util.Set
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.VariableInformation.Direction
import org.pshdl.interpreter.VariableInformation.Type

class VerilogCodeGenerator extends CommonCodeGenerator {

	Multimap<Integer, Frame> writes = LinkedListMultimap.create
	Multimap<Integer, VariableInformation> posClocks = LinkedHashMultimap.create
	Multimap<Integer, VariableInformation> negClocks = LinkedHashMultimap.create

	new(ExecutableModel model) {
		super(new CommonCodeGeneratorParameter(model, -1, Integer.MAX_VALUE, false))
		for (Frame f : em.frames) {
			for (int outputId : f.outputIds) {
				val internal = em.internals.get(outputId)
				if (f.edgeNegDepRes !== -1)
					negClocks.put(f.edgeNegDepRes, internal.info)
				if (f.edgePosDepRes !== -1)
					posClocks.put(f.edgePosDepRes, internal.info)
				writes.put(outputId, f)
			}
		}
	}

	override generateMainCode() '''`timescale 1ns/1ps
module «em.moduleName.idName(false, NONE)»(
	«FOR VariableInformation vi : em.variables.filter[dir !== Direction.INTERNAL] SEPARATOR ','»
	«vi.idName(true, NONE)»
	«ENDFOR»
);
«FOR VariableInformation vi : em.variables»
«vi.declaration(NONE)»
«IF vi.isRegister»
«vi.declaration(SHADOWREG)»
«ENDIF»

«ENDFOR»

«FOR Integer ii : posClocks.keySet»
always @(posedge «em.internals.get(ii).info.idName(true, NONE)»)
begin
	«FOR VariableInformation vi : posClocks.get(ii)»
		«vi.idName(true, NONE)» <= «vi.idName(true, SHADOWREG)»;
	«ENDFOR»
end
«ENDFOR»

«FOR Integer ii : negClocks.keySet»
always @(negedge «em.internals.get(ii).info.idName(true, NONE)»)
begin
	«FOR VariableInformation vi : negClocks.get(ii)»
		«vi.idName(true, NONE)» <= «vi.idName(true, SHADOWREG)»;
	«ENDFOR»
end
«ENDFOR»

«FOR Integer ii : writes.keySet»
«ii.generateWriteProcess»
«ENDFOR»

endmodule;
	'''

	def generateWriteProcess(Integer internal) '''
		always @(«writes.get(internal).map[internalDependencies.map[sensitiviyName].toSet].flatten.join(' or ')»)
		begin
			«FOR Frame f : writes.get(internal)»
				«val predSet = f.predNegDepRes.map[sensitiviyName + " == 0"] + f.predPosDepRes.map[sensitiviyName]»
				«IF !predSet.empty»
					if («predSet.join(" && ")»)
					  begin
					«f.frameExecution»
					 end
				«ELSE»
					«f.frameExecution»
				«ENDIF»
			«ENDFOR»
		end
	'''

	override protected updatePrediateTimestamp(InternalInformation outputInternal) ''''''

	override protected fixupValue(CharSequence assignValue, int targetSizeWithType, boolean doMask) {
		return assignValue
	}

	def protected toCastExpression(FastInstruction exec, Frame frame, int pos, int a, int b,
		int arrPos) {
		val StringBuilder sb = new StringBuilder();
		val String tempName = getTempName(a, NONE);
		switch (exec.inst) {
			case bitAccessSingle: {
				sb.append(assignTempVar(Type.BIT, 2, pos, NONE, '''«tempName»[«exec.arg1»]''', false));
			}
			case bitAccessSingleRange: {
				val int highBit = exec.arg1;
				val int lowBit = exec.arg2;
				val int targetSize = (highBit - lowBit) + 1;
				sb.append(assignTempVar(Type.BIT, targetSize << 1, pos, NONE, '''«tempName»[«exec.arg1»:«exec.arg2»]''', false));
			}
			case cast_int: {
				sb.append(
					assignTempVar(Type.INT, (Math.min(exec.arg1, exec.arg2) << 1).bitwiseOr(1), pos, NONE,
						'''«tempName»[«Math.min(exec.arg1, exec.arg2) - 1»:0]''', false));
			}
			case cast_uint: {
				sb.append(
					assignTempVar(Type.UINT, Math.min(exec.arg1, exec.arg2) << 1, pos, NONE,
						'''«tempName»[«Math.min(exec.arg1, exec.arg2) - 1»:0]''', false));
			}
			default: {
				throw new IllegalArgumentException("Did not instruction:" + exec + " here");
			}
		}
		return sb;
	}

	def protected toEdgeExpression(FastInstruction exec, Frame frame, int pos, int a, int b, 
		int arrPos) ''''''

	override protected handlePredicates(Set<Integer> handledPredicates, boolean positive, int[] predicates) ''''''

	override protected updatePredicateFreshness(int pred, boolean positive) ''''''

	override protected updateHandledClk(int edgeDepRes, boolean posEdge) ''''''

	override protected updateEdge(int edgeDepRes, boolean posEdge) ''''''

	def sensitiviyName(Integer internal) {
		val vi = em.internals.get(internal).info
		var attributes = NONE
		if (vi.isRegister)
			attributes = SHADOWREG
		return vi.idName(true, attributes)
	}

	def declaration(VariableInformation vi, EnumSet<Attributes> attributes) {
		var CharSequence type;
		switch (vi.dir) {
			case IN: type = "input"
			case INOUT: type = "inout"
			case OUT: type = "output"
			case INTERNAL: type = "reg"
		}
		if (attributes.contains(Attributes.isShadowReg))
			type = "reg"
		var CharSequence width = '''[«vi.width - 1»:0]'''
		if (vi.width === 1) {
			if (vi.array)
				width = '''[0:«vi.arraySize - 1»]'''
			else
				width = ""
		}
		var CharSequence dim = ''''''
		if (vi.array) {
			if (vi.width !== 1)
				dim = '''[0:«vi.arraySize - 1»]'''
		}
		return '''«type» «width» «vi.idName(true, attributes)» «dim»;'''
	}

	override protected idName(String name, boolean field, EnumSet<Attributes> attributes) {
		super.idName(name, field, attributes).toString.replace("$", "__")
	}

	override protected applyRegUpdates() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected arrayInit(VariableInformation varInfo, BigInteger initValue, EnumSet<Attributes> attributes) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callMethod(CharSequence methodName, CharSequence... args) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callRunMethod() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected callStage(int stage, boolean constant) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected checkRegupdates() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected checkTestbenchListener() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected clearRegUpdates() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected copyArray(VariableInformation varInfo) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		var type = "reg"
		var width = ""
		if (varInfo.width > 1)
			width = '''[«varInfo.width - 1»:0]'''
		if (varInfo.width <= 0)
			width = "[31:0]"
		return '''«type»«width»'''
	}

	override protected fillArray(VariableInformation vi, CharSequence regFillValue) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected footer() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected functionFooter(Frame frame) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected functionHeader(Frame frame) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected header() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected pow(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand,
		int rightOperand, EnumSet<Attributes> attributes, boolean doMask) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runMethodsFooter(boolean constant) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runMethodsHeader(boolean constant) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runProcessHeader(ProcessData pd) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected runTestbenchHeader() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName,
		CharSequence offset, boolean force, CharSequence fillValue) {
		'''''';
	}

	override protected stageMethodsFooter(int stage, int totalStageCosts, boolean constant) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

}
