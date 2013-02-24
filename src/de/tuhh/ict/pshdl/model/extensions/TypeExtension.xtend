package de.tuhh.ict.pshdl.model.extensions

import de.tuhh.ict.pshdl.model.HDLArithOp
import de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType
import de.tuhh.ict.pshdl.model.HDLBitOp
import de.tuhh.ict.pshdl.model.HDLClass
import de.tuhh.ict.pshdl.model.HDLConcat
import de.tuhh.ict.pshdl.model.HDLDirectGeneration
import de.tuhh.ict.pshdl.model.HDLEnumRef
import de.tuhh.ict.pshdl.model.HDLEqualityOp
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLFunctionCall
import de.tuhh.ict.pshdl.model.HDLInlineFunction
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLLiteral$HDLLiteralPresentation
import de.tuhh.ict.pshdl.model.HDLManip
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLPrimitive$HDLPrimitiveType
import de.tuhh.ict.pshdl.model.HDLRange
import de.tuhh.ict.pshdl.model.HDLShiftOp
import de.tuhh.ict.pshdl.model.HDLTernary
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions
import de.tuhh.ict.pshdl.model.types.builtIn.HDLPrimitives
import de.tuhh.ict.pshdl.model.utils.HDLProblemException
import de.tuhh.ict.pshdl.model.validation.Problem
import de.tuhh.ict.pshdl.model.validation.builtin.ErrorCode
import java.math.BigInteger
import java.util.Iterator
import java.util.List

import static de.tuhh.ict.pshdl.model.extensions.TypeExtension.*
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment
import de.tuhh.ict.pshdl.model.utils.Insulin
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLArrayInit
import de.tuhh.ict.pshdl.model.utils.HDLConfig
import de.tuhh.ict.pshdl.model.HDLRegisterConfig

class TypeExtension {
	public static TypeExtension INST=new TypeExtension
	
	def static HDLType typeOf(IHDLObject obj){
		if (!obj.isFrozen)
			throw new IllegalArgumentException("Target needs to be frozen")
		var HDLType res = INST.determineType(obj)
		if (res!=null){
			return res.copyDeepFrozen(obj)
		}
		return res
	}
	
		/**
	 * Attempt to determine the type of this HDLVariable. For this to work it
	 * needs to have a valid container.
	 * 
	 * @return the HDLType if it could be determined, <code>null</code>
	 *         otherwise.
	 */
	def dispatch HDLType determineType(HDLVariable hVar) {
		if (HDLRegisterConfig::DEF_CLK==hVar.name)
			return HDLPrimitive::bit
		if (HDLRegisterConfig::DEF_RST==hVar.name)
			return HDLPrimitive::bit
		val IHDLObject container = hVar.container
		if (container==null)
			return null
		switch (container.classType) {
		case HDLClass::HDLVariableDeclaration:
			return (container as HDLVariableDeclaration).determineType
		case HDLClass::HDLDirectGeneration:
			return (container as HDLDirectGeneration).HIf
		case HDLClass::HDLInterfaceInstantiation:
			return (container as HDLInterfaceInstantiation).resolveHIf
		case HDLClass::HDLForLoop:
			return HDLPrimitive::natural
		case HDLClass::HDLInlineFunction:
			throw new HDLProblemException(new Problem(ErrorCode::INLINE_FUNCTION_NO_TYPE, hVar))
		}
		throw new IllegalArgumentException("Failed to resolve type of " + hVar + " caused by an unexpected container: " + container)
	}

	def dispatch HDLType determineType(HDLVariableDeclaration hvd) {
		if (hvd.primitive != null)
			return hvd.primitive
		return hvd.resolveType
	}
	
	def dispatch HDLType determineType(HDLArrayInit ai){
		if (ai.exp.size==1)
			return ai.exp.get(0).determineType
		var res=HDLPrimitive::natural
		for(exp:ai.exp){
			val sub=exp.determineType
			if (!sub.equals(exp))
				return sub
		}
		return res
	}
	def dispatch HDLType determineType(HDLExpression cat){
		throw new RuntimeException("Did not correctly implement determineType for:"+cat.classType)	
	}

	private static GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", false);

	def dispatch HDLType determineType(HDLUnresolvedFragment cat) {
		if (cat.hasMeta(DETERMINE_TYPE_RESOLVE))
			return null
		cat.setMeta(DETERMINE_TYPE_RESOLVE)
		return Insulin::resolveFragment(cat)?.copyDeepFrozen(cat.container)?.determineType
	}
	
	def dispatch HDLType determineType(HDLConcat cat) {
		val Iterator<HDLExpression> iter = cat.cats.iterator
		var HDLPrimitive type = iter.next.determineType as HDLPrimitive
		var HDLExpression width = getWidth(type)
		while (iter.hasNext) {
			if (width==null)
				//This can happen when we have invalid concatenations
				return null
			type = iter.next.determineType as HDLPrimitive
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(getWidth(type))
			width = HDLPrimitives::simplifyWidth(cat, width)
		}
		return HDLPrimitive::bitvector.setWidth(width).setContainer(cat)
	}

	def static dispatch HDLExpression getWidth(IHDLObject obj) {
		val type=INST.determineType(obj)
		return getWidth(type)
	}
	
	def static dispatch HDLExpression getWidth(HDLPrimitive type) {
		val HDLExpression width = type.width
		if (type.type == HDLPrimitive$HDLPrimitiveType::BIT)
			return HDLLiteral::get(1)
		return width
	}

	def dispatch HDLType determineType(HDLEnumRef ref) {
		return ref.resolveHEnum
	}

	def dispatch HDLType determineType(HDLManip manip) {
		return HDLPrimitives::instance.getManipOpType(manip).result
	}

	def dispatch HDLType determineType(HDLFunctionCall call) {
		return HDLFunctions::getInferenceInfo(call).result
	}

	def dispatch HDLType determineType(HDLLiteral lit) {
		// Actually depends on context
		switch (lit.presentation){
		case HDLLiteral$HDLLiteralPresentation::STR:
			return new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::STRING)
		case HDLLiteral$HDLLiteralPresentation::BOOL:
			return new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::BOOL)
		}
		val boolean isSigned=lit.^val.charAt(0) != '-'
		val BigInteger bigVal=lit.valueAsBigInt
		if (bigVal.bitLength>31)
			return HDLPrimitive::uint.setWidth(HDLLiteral::get(bigVal.bitLength))
		return HDLPrimitive::target(isSigned)
	}

	def dispatch HDLType determineType(HDLVariableRef ref) {
		val List<HDLRange> bits = ref.bits
		if (bits.size == 0)
			return ref.resolveVar?.determineType
		if (bits.size == 1 && bits.get(0).from == null)
			return HDLPrimitive::bit
		val Iterator<HDLRange> iter = bits.iterator
		var HDLExpression width = HDLPrimitives::simplifyWidth(ref, iter.next.width)
		while (iter.hasNext) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(iter.next.width)
			width = HDLPrimitives::simplifyWidth(ref, width)
		}
		return (ref.resolveVar?.determineType as HDLPrimitive).setWidth(width)
	}

	def dispatch HDLType determineType(HDLArithOp aop) {
		return HDLPrimitives::instance.getArithOpType(aop).result
	}

	def dispatch HDLType determineType(HDLBitOp bop) {
		return HDLPrimitives::instance.getBitOpType(bop).result
	}

	def dispatch HDLType determineType(HDLShiftOp sop) {
		return HDLPrimitives::instance.getShiftOpType(sop).result
	}

	def dispatch HDLType determineType(HDLEqualityOp eop) {
		return HDLPrimitives::instance.getEqualityOpType(eop).result
	}
	
	def dispatch HDLType determineType(HDLTernary tern){
		return tern.thenExpr.determineType
	}
	
	def dispatch HDLType determineType(HDLInlineFunction func) {
		throw new HDLProblemException(new Problem(ErrorCode::INLINE_FUNCTION_NO_TYPE, func))
	}
	
}
