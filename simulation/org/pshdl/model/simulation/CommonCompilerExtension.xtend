package org.pshdl.model.simulation

import java.util.ArrayList
import java.util.List
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.VariableInformation
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame
import org.pshdl.interpreter.utils.Instruction
import java.util.Map
import java.util.HashMap
import java.math.BigInteger

class CommonCompilerExtension {
	public ExecutableModel em
	public Map<String, Integer> varIdx = new HashMap
	public Map<String, Integer> intIdx = new HashMap
	public Map<String, Boolean> prevMap = new HashMap
	public boolean hasClock

	new(ExecutableModel em) {
		this.em = em
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

	def InternalInformation asInternal(int id) {
		return em.internals.get(id)
	}

	def asMask(int width) {
		val mask = (1bi.shiftLeft(width)) - 1bi
		return mask.toHexString
	}

	def asMaskL(int width) {
		val mask = (1l << width) - 1
		if (width == 64)
			return "0xFFFFFFFFFFFFFFFFl";
		return mask.toHexStringL
	}

	def excludeNull(VariableInformation[] vars) {
		vars.filter[isNotNull]
	}

	def isNotNull(VariableInformation it) {
		name != '#null'
	}

	def isNull(VariableInformation it) {
		!isNotNull
	}

	def excludeNull(InternalInformation[] vars) {
		vars.filter[info.isNotNull]
	}

	def dimMask(InternalInformation info) {
		val size = info.info.totalSize
		val res = Long::highestOneBit(size)
		if (res == size)
			return res - 1;
		return (res << 1) - 1;
	}

	def arrayFixedOffset(InternalInformation v) {
		val dims = dimsLastOne(v.info)
		var off = 0;
		for (i : (0 ..< v.arrayIdx.length)) {
			val arr = v.arrayIdx.get(i)
			val dim = dims.get(i)
			off = off + (arr * dim)
		}
		return off
	}

	def dimsLastOne(VariableInformation v) {
		val dims = new ArrayList(v.dimensions)
		if (dims.size > 0) {
			dims.set(dims.size - 1, 1);
		}
		dims
	}

	def isArray(VariableInformation information) {
		information.dimensions.length != 0
	}

	def arrayAccess(VariableInformation v) {
		arrayAccess(v, null, "a")
	}

	def arrayAccess(VariableInformation v, List<Integer> arr) {
		arrayAccess(v, arr, "a")
	}

	def arrayAccessBracket(VariableInformation v, List<Integer> arr) {
		if (!v.array)
			return ""
		return '''[«arrayAccess(v, arr, "a")»]'''
	}

	def arrayAccessArrIdx(VariableInformation v) {
		val varAccess = new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0 ..< v.dimensions.length)) {
			val dim = dims.get(i)
			if (i != 0)
				varAccess.append('+')
			val idx = i
			if (dim != 1)
				varAccess.append('''arrayIdx[«idx»]*«dim»''')
			else
				varAccess.append('''arrayIdx[«idx»]''')
		}
		return varAccess
	}

	def arrayAccess(VariableInformation v, List<Integer> arr, String varName) {
		val varAccess = new StringBuilder
		val dims = dimsLastOne(v)
		for (i : (0 ..< v.dimensions.length)) {
			val dim = dims.get(i)
			if (i != 0)
				varAccess.append('+')
			val idx = if(arr === null) i else arr.get(i)
			if (dim != 1)
				varAccess.append('''«varName»«idx»*«dim»''')
			else
				varAccess.append('''«varName»«idx»''')
		}
		return varAccess
	}

	def toHexString(BigInteger value) '''«IF value.signum<0»-«ENDIF»0x«value.abs.toString(16)»'''

	def toHexStringL(long value) '''0x«Long::toHexString(value)»l'''
	def toHexStringI(Integer value) '''0x«Integer::toHexString(value)»'''

	def getFrameName(Frame f) '''s«String::format("%03d",Math::max(f.scheduleStage,0))»frame«String::format("%04X",f.uniqueID)»'''

	def constantL(int id, Frame f) '''«f.constants.get(id).longValue.toHexStringL»'''
	def constantI(int id, Frame f) '''«f.constants.get(id).intValue.toHexStringI»'''

	def totalSize(VariableInformation info) {
		var size = 1
		for (d : info.dimensions) {
			size = size * d
		}
		return size
	}

	def isPredicate(VariableInformation info) {
		info.name.startsWith(InternalInformation::PRED_PREFIX)
	}

	def idName(VariableInformation information, boolean prev, boolean field) {
		return information.name.idName(prev, field)
	}

	def idName(InternalInformation ii, boolean prev, boolean field) {
		if (ii.fixedArray)
			return ii.fullName.idName(prev, field)
		return ii.info.idName(prev, field)
	}

	def idName(String name, boolean prev, boolean field) {
		var res=name;
		val isReg=name.endsWith("$reg")
		if (isReg)
			res=name.substring(0,name.length-4)
		res = res.replaceAll("[\\.\\$\\@]+", "_").replaceAll('\\{', 'Bit').replaceAll('\\}', '').replaceAll(':', 'to').
			replaceAll('\\[', 'arr').replaceAll('\\]', '')
		if (res.startsWith("#"))
			res = res.substring(1)
		if (field)
			res = "_" + res
		if (isReg)
			res=res+"$reg"
		if (prev)
			return res + '_prev'
		return res
	}

	def maxRegUpdates(ExecutableModel em) {
		var maxUpdates = 0;
		for (f : em.frames) {
			val oi = f.outputId.asInternal
			if (!oi.info.notNull) {
				for (inst : f.instructions) {
					if (inst.inst === Instruction::writeInternal) {
						if (inst.arg1.asInternal.isShadowReg)
							maxUpdates = maxUpdates + 1;
					}
				}
			} else {
				if (oi.isShadowReg)
					maxUpdates = maxUpdates + 1;
			}
		}
		return maxUpdates
	}
	
	def twoOpValue(String op, String cast, int a,int b, int targetSizeWithType) {
		val targetSize=(targetSizeWithType>>1)
		val shift=64-targetSize
		if ((targetSizeWithType.bitwiseAnd(1))==1)
			return signExtend('''t«b» «op» t«a»''', cast, shift)
		return '''(t«b» «op» t«a») & «targetSize.asMaskL»'''
	}
	
	def singleOpValue(String op, String cast, int a,int targetSizeWithType) {
		val targetSize=(targetSizeWithType>>1)
		val shift=64-targetSize
		if ((targetSizeWithType.bitwiseAnd(1))==1)
			return signExtend('''«op» t«a»''', cast, shift)
		return '''(«op» t«a») & «targetSize.asMaskL»'''
	}
	
	def signExtend(CharSequence op, CharSequence cast, int shift) {
		if (shift==0)
			return op
		if (cast!==null && cast!="")
			return '''((«cast»(«op»)) << «shift») >> «shift»'''
		'''((«op») << «shift») >> «shift»'''
	}

}
