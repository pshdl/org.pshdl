package org.pshdl.model.simulation

import org.pshdl.interpreter.VariableInformation
import java.math.BigInteger
import java.util.EnumSet
import org.pshdl.model.simulation.CommonCodeGenerator.Attributes
import org.pshdl.interpreter.Frame
import org.pshdl.model.simulation.CommonCodeGenerator.ProcessData
import org.pshdl.interpreter.InternalInformation
import org.pshdl.interpreter.ExecutableModel

class GoCodeGenerator extends CommonCodeGenerator{
	
	String pkg 
	String unit
	
	new(ExecutableModel em, int maxCosts, String pkg, String unit) {
		super(em, 64, maxCosts)
		this.pkg=pkg
		this.unit=unit
	}
	
	override protected preFieldDeclarations() '''type «unit» struct {
	varIdx map[string]int

	regUpdates   []regUpdate
	regUpdatePos int
	'''
	
	override protected postFieldDeclarations()'''}
	'''
	
	override protected applyRegUpdates() '''
func (s *«unit») updateRegs() {
	for _, r := range s.regUpdates {
		switch r.internal {
		«FOR VariableInformation vi: em.variables»
		case «vi.varIdx»:
			s.«vi.idName(true, NONE)» = s.«vi.idName(true, EnumSet.of(Attributes.isShadowReg))»
		«ENDFOR»
		}
	}
}
	'''
	
	override protected arrayInit(VariableInformation varInfo, BigInteger initValue, EnumSet<Attributes> attributes) '''make([]«fieldType(varInfo, attributes)», «varInfo.arraySize»)'''
	
	override protected assignNextTime(VariableInformation nextTime, CharSequence currentProcessTime) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected callMethod(String methodName, String... args) '''s.«methodName»((«IF args !== null»«FOR String arg : args SEPARATOR ','»«arg»«ENDFOR»«ENDIF»)'''
	
	override protected callRunMethod() '''s.Run()
	'''
	
	override protected callStage(int stage, boolean constant) '''s.«stageMethodName(stage, constant)»()
		'''
	
	override protected checkRegupdates() '''if len(s.regUpdates) > 0 && !s.disableRegOutputLogic {
			break
		}
'''
	
	override protected checkTestbenchListener() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected clearRegUpdates() '''s.regUpdatePos = 0
'''
	
	override protected copyArray(VariableInformation varInfo) '''/* copy array */'''
	
	override protected fieldType(VariableInformation varInfo, EnumSet<Attributes> attributes) {
		if (varInfo.dimensions.nullOrEmpty || attributes.contains(Attributes.baseType)) {
			if (isBoolean(varInfo, attributes))
				return "bool "
			return "uint64 "
		} else {
			if (isBoolean(varInfo, attributes))
				return "bool[] "
			return "uint64[] "
		}
	}
	
	override protected footer() 
	'''
func (s *SimpleAluGenerator) SetInputWithName(name string, value int64, arrayIdx ...int) {
	s.SetInput(s.GetIndex(name), value, arrayIdx...)
}
 
func (s *SimpleAluGenerator) SetInput(idx int, value int64, arrayIdx ...int) {
	switch idx {
«FOR v : em.variables.excludeNull»
	«IF v.dimensions.length == 0»
		case «varIdx.get(v.name)»: 
			«assignVariable(v, '''«IF v.predicate»value!=0«ELSE»value«ENDIF»''', NONE, true, false)»
			break
	«ELSE»
		case «varIdx.get(v.name)»: 
			«idName(v, true, NONE)»[«v.calculateVariableAccessIndexArr»]=«IF v.predicate»value!=0«ELSE»value«ENDIF»
			break
	«ENDIF»
«ENDFOR»
	default:
		panic("Not a valid index")
	}
}
 
func (s *SimpleAluGenerator) GetIndex(name string) int {
	idx, ok := s.varIdx[name]
	if !ok {
		panic("The name:" + name + " is not a valid index")
	}
	return idx
}
 
func (s *SimpleAluGenerator) GetName(idx int) string {
	switch idx {
	case 1:
		return "$Pred_alu.@if0"
	«FOR VariableInformation vi:em.variables»
	case «vi.varIdx»:
		return "«vi.name»"
	«ENDFOR»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *SimpleAluGenerator) GetOutputWithName(name string, arrayIdx ...int) int64 {
	return s.GetOutput(s.GetIndex(name), arrayIdx...)
}
 
func (s *SimpleAluGenerator) GetOutput(idx int, arrayIdx ...int) int64 {
	switch idx {
«FOR v : em.variables.excludeNull»
	«IF v.dimensions.length == 0»
		case «varIdx.get(v.name)»:
			«IF v.predicate»
			if «v.idName(true, NONE)» {
				return 1
			else {
				return 0
			}
			«ELSE» 
			return «v.idName(true, NONE)»«IF v.width != 64» & «v.width.calcMask.constant»«ENDIF»
			«ENDIF»
	«ELSE»
		case «varIdx.get(v.name)»:
		«IF v.predicate»
			if «v.idName(true, NONE)»[«v.calculateVariableAccessIndexArr»] {
				return 1
			else {
				return 0
			}
			«ELSE» 
			return «v.idName(true, NONE)»[«v.calculateVariableAccessIndexArr»]«IF v.width != 64 &&
!v.predicate» & «v.width.calcMask.constant»«ENDIF»
			«ENDIF» 
	«ENDIF»
«ENDFOR»
	default:
		panic("Not a valid index:")
	}
}
 
func (s *SimpleAluGenerator) getDeltaCycle() int64 {
	return s.deltaCycle
}
	'''
	
	override protected functionFooter(Frame frame) 
	'''}
	'''
	
	override protected functionHeader(Frame frame) 
	'''func (s *SimpleAluGenerator) «frame.frameName» (){
'''
	
	override protected header() 
	'''package «pkg»
 
type regUpdate struct {
	internal, offset, fillValue int
}
 
func New«unit»() *«unit» {
	return NewSimpleAluGeneratorWithArgs(false, false)
}
 
func New«unit»WithArgs(disableEdge, disableRegOutputLogic bool) *«unit» {
	var s = «unit»{
		disableEdge:           disableEdge,
		disableRegOutputLogic: disableRegOutputLogic,
	}
 
	s.regUpdates = make([]regUpdate, «maxRegUpdates»)
	s.varIdx = make(map[string]int, «em.variables.size-1»)
«FOR v : em.variables.excludeNull»
	s.varIdx["«v.name»"] =  «varIdx.get(v.name)»)
«ENDFOR»
 
	return &s
}

func (s *«unit») skipEdge(local uint64) bool {
	var dc = local >> 16 // zero-extended shift
	if dc < s.deltaCycle {
		return false
	}
 
	if (dc == s.deltaCycle) && ((local & 0xFFFF) == s.epsCycle) {
		return false
	}
 
	return true
}
	'''
	
	override protected runMethodsFooter(boolean constant) '''}
	'''
	
	override protected runMethodsHeader(boolean constant) '''func (s *SimpleAluGenerator) «IF !constant»Run«ELSE»InitConstants«ENDIF»() {
	'''
	
	override protected runProcessHeader(ProcessData pd) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected runTestbenchHeader() {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override protected scheduleShadowReg(InternalInformation outputInternal, CharSequence last, CharSequence cpyName, CharSequence offset, boolean force, CharSequence fillValue)
	'''
	«IF !force»if («cpyName»!=«last»)
	«indent()»	«ENDIF»{
	'''
	
	override protected stageMethodsFooter(int stage, int totalStageCosts, boolean constant) '''}
	'''
	
	override protected stageMethodsHeader(int stage, int totalStageCosts, boolean constant) '''func (s *SimpleAluGenerator) «stageMethodName(stage,constant)»() {'''
	
}