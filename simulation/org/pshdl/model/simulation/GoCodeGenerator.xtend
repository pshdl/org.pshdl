package org.pshdl.model.simulation

import org.pshdl.interpreter.VariableInformation
import java.math.BigInteger
import java.util.EnumSet
import org.pshdl.model.simulation.CommonCodeGenerator.Attributes
import org.pshdl.interpreter.Frame
import org.pshdl.model.simulation.CommonCodeGenerator.ProcessData
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.ExecutableModel
import org.pshdl.interpreter.Frame.FastInstruction
import org.pshdl.interpreter.utils.Instruction

class GoCodeGenerator extends CommonCodeGenerator{
	
	String pkg 
	String unit
	CommonCompilerExtension cce;
	
	new(ExecutableModel em, int maxCosts, String pkg, String unit) {
		super(em, 64, maxCosts)
		this.pkg=pkg
		this.unit=unit
		cce=new CommonCompilerExtension(em, 64)
	}
	
	override protected preFieldDeclarations() '''type «unit» struct {
	varIdx map[string]int

	regUpdates   []regUpdate
	regUpdatePos int
	'''
	
	override protected postFieldDeclarations()'''}
func (s *«unit») updateRegs() {
	for _, reg := range s.regUpdates {
		switch reg.internal {
		«updateRegCases»
		}
	}
}
	'''
	
	override protected doLoopStart() '''for {'''
	
	override protected doLoopEnd(CharSequence condition) '''	if («condition») { break }
	}
	'''
	
	override protected applyRegUpdates() '''s.updateRegs()
	'''
	
	override protected arrayInit(VariableInformation varInfo, BigInteger initValue, EnumSet<Attributes> attributes) '''make([]«fieldType(varInfo, attributes)», «varInfo.arraySize»)'''
	
	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected callMethod(CharSequence methodName, CharSequence... args) '''s.«methodName»(«IF args !== null»«FOR CharSequence arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''
	
	override protected callRunMethod() '''s.Run()
	'''
	
	override protected callStage(int stage, boolean constant) '''s.«stageMethodName(stage, constant)»()
		'''
	
	override protected checkRegupdates() '''s.regUpdatePos == 0'''
	
	override protected checkTestbenchListener() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected clearRegUpdates() '''s.regUpdatePos = 0
'''
	
	override protected copyArray(VariableInformation varInfo) '''/* copy array */'''
	
	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(Attributes.baseType)) {
			if (isBoolean(varInfo, attributes))
				return "bool"
			return "int64"
		} else {
			if (isBoolean(varInfo, attributes))
				return "[]bool"
			return "[]int64"
		}
	}

	override protected doCast(CharSequence cast, CharSequence assignValue) '''«cast»(«assignValue»)'''
	
	override protected footer() 
	'''
func (s *«unit») SetInputWithName(name string, value int64, arrayIdx ...int) {
	s.SetInput(s.GetIndex(name), value, arrayIdx...)
}
 
func (s *«unit») SetInput(idx int, value int64, arrayIdx ...int) {
	switch idx {
	«setInputCases("value", null)»
	default:
		panic("Not a valid index")
	}
}
 
func (s *«unit») GetIndex(name string) int {
	idx, ok := s.varIdx[name]
	if !ok {
		panic("The name:" + name + " is not a valid index")
	}
	return idx
}
 
func (s *«unit») GetName(idx int) string {
	switch idx {
	«FOR VariableInformation vi:em.variables»
	case «vi.varIdx»:
		return "«vi.name»"
	«ENDFOR»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *«unit») GetOutputWithName(name string, arrayIdx ...int) int64 {
	return s.GetOutput(s.GetIndex(name), arrayIdx...)
}
 
func (s *«unit») GetOutput(idx int, arrayIdx ...int) int64 {
	switch idx {
	«getOutputCases(null)»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *«unit») GetDeltaCycle() int64 {
	return s.deltaCycle
}

func (s *«unit») GetJsonDesc() string {
	return "«cce.JSONDescription»"
}

func (s *«unit») SetDisableEdge(enable bool) {
	s.«DISABLE_EDGES.name» = enable
}
 
func (s *«unit») SetDisableRegOutputLogic(enable bool) {
	s.«DISABLE_REG_OUTPUTLOGIC.name» = enable
}

	'''
	
	override protected functionFooter(Frame frame) 
	'''}
	'''
	
	override protected functionHeader(Frame frame) 
	'''func (s *«unit») «frame.frameName» (){
'''
	
	override protected header() 
	'''package «pkg»
 
type regUpdate struct {
	internal, offset int
	fillValue int64
}
 
func New«unit»() *«unit» {
	return New«unit»WithArgs(false, false)
}
 
func New«unit»WithArgs(«DISABLE_EDGES.name», «DISABLE_REG_OUTPUTLOGIC.name» bool) *«unit» {
	var s = «unit»{
		«DISABLE_EDGES.name»:           «DISABLE_EDGES.name»,
		«DISABLE_REG_OUTPUTLOGIC.name»: «DISABLE_REG_OUTPUTLOGIC.name»,
	}
 
	s.regUpdates = make([]regUpdate, «maxRegUpdates»)
	s.varIdx = make(map[string]int, «em.variables.size-1»)
	«FOR v : em.variables.excludeNull»
		s.varIdx["«v.name»"] =  «varIdx.get(v.name)»
	«ENDFOR»
	«FOR v : em.variables.filter[array]»
		«v.idName(true, NONE)» = make([]int64, «v.arraySize»)
		«IF v.isRegister»
			«v.idName(true, SHADOWREG)» = make([]int64, «v.arraySize»)
		«ENDIF»
 	«ENDFOR»
	return &s
}

func (s *«unit») skipEdge(local int64) bool {
	var dc = int64(uint64(local) >> 16) // zero-extended shift
	if dc < s.«DELTA_CYCLE.name» {
		return false
	}
 
	if (dc == s.«DELTA_CYCLE.name») && ((local & 0xFFFF) == s.«EPS_CYCLE.name») {
		return false
	}
 
	return true
}
	'''
	
	override protected idName(String name, boolean field, EnumSet<Attributes> attributes) {
		val superVal=super.idName(name, field, attributes).toString.replace('$','__')
		return superVal
	}
	
	override protected fieldPrefix() '''s.'''
	
	override protected CharSequence createVarDeclaration(VariableInformation varInfo, EnumSet<Attributes> attributes, boolean initialize) {
		val StringBuilder sb = new StringBuilder()
		sb.append(preField(varInfo, attributes))
		indent++;
		sb.append(indent())
		indent--;
		sb.append(idName(varInfo, false, attributes)).append(" ")
		sb.append(fieldType(varInfo, attributes))
		sb.append(postField(varInfo))
		return sb;
	}
	
	override protected constantSuffix() ''''''
	
	override protected CharSequence inlineVarDecl(VariableInformation varInfo, boolean field, EnumSet<Attributes> attributes) '''var «idName(varInfo, field, attributes)» «fieldType(varInfo, attributes)»'''
	
	override protected runMethodsFooter(boolean constant) '''}
	'''
	
	override protected runMethodsHeader(boolean constant) '''func (s *«unit») «IF !constant»Run«ELSE»InitConstants«ENDIF»() {
	'''
	
	override protected runProcessHeader(ProcessData pd) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected runTestbenchHeader() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset, boolean force, CharSequence fillValue)
	'''
	«IF !force»if («cpyName»!=«last») «ENDIF»{
	«indent()»	s.regUpdates[s.regUpdatePos] = regUpdate{«outputInternal.varIdx», int(«offset»), «fillValue»}
	«indent()»	s.regUpdatePos++
	«indent()»}
	'''
	
	override protected stageMethodsFooter(int stage, int totalStageCosts, boolean constant) '''}
	'''
	
	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) '''func (s *«unit») «stageMethodName(stage,constant)»() {
	'''
	
	override protected fillArray(VariableInformation vi, CharSequence regFillValue) '''for i := range «idName(vi, true, NONE)» { «idName(vi, true, NONE)»[i] = «regFillValue» } '''
	
	override protected singleOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int a, EnumSet<Attributes> attributes) {
		if (fi.inst===Instruction.bit_neg){
			val CharSequence assignValue = singleOpValue("^", getCast(targetSizeWithType), a, targetSizeWithType, attributes);
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue);
		}
		super.singleOp(fi, op, targetSizeWithType, pos, a, attributes)
	}
	
	override protected twoOp(FastInstruction fi, String op, int targetSizeWithType, int pos, int leftOperand, int rightOperand, EnumSet<Attributes> attributes) {
		if (fi.inst === Instruction.srl){
			val CharSequence assignValue = doCast("int64", doCast("uint64", getTempName(leftOperand, NONE)) + ">>" +doCast("uint64", getTempName(rightOperand, NONE)))
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue);
		}
		if (fi.inst === Instruction.sll){
			val CharSequence assignValue = doCast("int64", getTempName(leftOperand, NONE) + "<<" +doCast("uint64", getTempName(rightOperand, NONE)))
			return assignTempVar(targetSizeWithType, pos, attributes, assignValue);
		}
		super.twoOp(fi, op, targetSizeWithType, pos, leftOperand, rightOperand, attributes)
	}
	
	override protected writeToNull(String last) '''var _ = «last»
	'''
	
}