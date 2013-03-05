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
import com.google.common.base.Optional

class TypeExtension {
	public static TypeExtension INST=new TypeExtension
	
	def static Optional<? extends HDLType> typeOf(IHDLObject obj){
		if (!obj.isFrozen)
			throw new IllegalArgumentException("Target needs to be frozen")
		var res = INST.determineType(obj)
		if (res.present){
			return Optional::of(res.get.copyDeepFrozen(obj))
		}
		return Optional::absent
	}
	
	/**
	 * Attempt to determine the type of this HDLVariable. For this to work it
	 * needs to have a valid container.
	 * 
	 * @return the HDLType if it could be determined, <code>null</code>
	 *         otherwise.
	 */
	def dispatch Optional<? extends HDLType> determineType(HDLVariable hVar) {
		if (HDLRegisterConfig::DEF_CLK==hVar.name)
			return Optional::of(HDLPrimitive::bit)
		if (HDLRegisterConfig::DEF_RST==hVar.name)
			return Optional::of(HDLPrimitive::bit)
		val IHDLObject container = hVar.container
		if (container==null)
			Optional::absent
		switch (container.classType) {
		case HDLClass::HDLVariableDeclaration:
			return (container as HDLVariableDeclaration).determineType
		case HDLClass::HDLDirectGeneration:
			return Optional::of((container as HDLDirectGeneration).HIf)
		case HDLClass::HDLInterfaceInstantiation:
			return Optional::fromNullable((container as HDLInterfaceInstantiation).resolveHIf)
		case HDLClass::HDLForLoop:
			return Optional::of(HDLPrimitive::natural)
		case HDLClass::HDLInlineFunction:
			throw new HDLProblemException(new Problem(ErrorCode::INLINE_FUNCTION_NO_TYPE, hVar))
		}
		throw new IllegalArgumentException("Failed to resolve type of " + hVar + " caused by an unexpected container: " + container)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLVariableDeclaration hvd) {
		if (hvd.primitive != null)
			return Optional::of(hvd.primitive)
		return Optional::of(hvd.resolveType)
	}
	
	def dispatch Optional<? extends HDLType> determineType(HDLArrayInit ai){
		if (ai.exp.size==1)
			return ai.exp.get(0).determineType
		var res=HDLPrimitive::natural
		for(exp:ai.exp){
			val sub=exp.determineType
			if (!sub.equals(exp))
				return sub
		}
		return Optional::of(res)
	}
	def dispatch Optional<? extends HDLType> determineType(HDLExpression cat){
		throw new RuntimeException("Did not correctly implement determineType for:"+cat.classType)	
	}

	private static GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE", false);

	def dispatch Optional<? extends HDLType> determineType(HDLUnresolvedFragment cat) {
		if (cat.hasMeta(DETERMINE_TYPE_RESOLVE))
			return Optional::absent
		cat.setMeta(DETERMINE_TYPE_RESOLVE)
		var resolved=Insulin::resolveFragment(cat)
		if (resolved==null)
			return Optional::absent
		return resolved.copyDeepFrozen(cat.container).determineType
	}
	
	def dispatch Optional<? extends HDLType> determineType(HDLConcat cat) {
		val Iterator<HDLExpression> iter = cat.cats.iterator
		val nextType=iter.next.determineType;
		if (!nextType.present)
			return Optional::absent
		var HDLPrimitive type = nextType.get as HDLPrimitive
		var HDLExpression width = getWidth(type)
		while (iter.hasNext) {
			if (width==null)
				//This can happen when we have invalid concatenations
				Optional::absent
			val nextCatType=iter.next.determineType
			if (!nextCatType.present)
				return Optional::absent
			type = nextCatType.get as HDLPrimitive
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(getWidth(type))
			width = HDLPrimitives::simplifyWidth(cat, width)
		}
		return Optional::of(HDLPrimitive::bitvector.setWidth(width).setContainer(cat))
	}

	def static dispatch HDLExpression getWidth(IHDLObject obj) {
		val type=INST.determineType(obj)
		if (!type.present)
			return null;
		return getWidth(type.get)
	}
	
	def static dispatch HDLExpression getWidth(HDLPrimitive type) {
		val HDLExpression width = type.width
		if (type.type == HDLPrimitive$HDLPrimitiveType::BIT)
			return HDLLiteral::get(1)
		return width
	}

	def dispatch Optional<? extends HDLType> determineType(HDLEnumRef ref) {
		return Optional::fromNullable(ref.resolveHEnum)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLManip manip) {
		return Optional::fromNullable(HDLPrimitives::instance.getManipOpType(manip).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLFunctionCall call) {
		return Optional::fromNullable(HDLFunctions::getInferenceInfo(call).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLLiteral lit) {
		// Actually depends on context
		switch (lit.presentation){
		case HDLLiteral$HDLLiteralPresentation::STR:
			return Optional::of(new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::STRING))
		case HDLLiteral$HDLLiteralPresentation::BOOL:
			return Optional::of(new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::BOOL))
		}
		val boolean isSigned=lit.^val.charAt(0) != '-'
		val BigInteger bigVal=lit.valueAsBigInt
		if (bigVal.bitLength>31)
			return Optional::of(HDLPrimitive::uint.setWidth(HDLLiteral::get(bigVal.bitLength)))
		return Optional::of(HDLPrimitive::target(isSigned))
	}

	def dispatch Optional<? extends HDLType> determineType(HDLVariableRef ref) {
		val List<HDLRange> bits = ref.bits
		if (bits.size == 0)
			return ref.resolveVar?.determineType
		if (bits.size == 1 && bits.get(0).from == null)
			return Optional::of(HDLPrimitive::bit)
		val Iterator<HDLRange> iter = bits.iterator
		var HDLExpression width = HDLPrimitives::simplifyWidth(ref, iter.next.width)
		while (iter.hasNext) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(iter.next.width)
			width = HDLPrimitives::simplifyWidth(ref, width)
		}
		val hVar=ref.resolveVar
		if (hVar==null)
			return Optional::absent
		val type=hVar.determineType;
		if (!type.present)
			return Optional::absent
		return Optional::of((type.get as HDLPrimitive).setWidth(width))
	}

	def dispatch Optional<? extends HDLType> determineType(HDLArithOp aop) {
		return Optional::fromNullable(HDLPrimitives::instance.getArithOpType(aop).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLBitOp bop) {
		return Optional::fromNullable(HDLPrimitives::instance.getBitOpType(bop).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLShiftOp sop) {
		return Optional::fromNullable(HDLPrimitives::instance.getShiftOpType(sop).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLEqualityOp eop) {
		return Optional::fromNullable(HDLPrimitives::instance.getEqualityOpType(eop).result)
	}
	
	def dispatch Optional<? extends HDLType> determineType(HDLTernary tern){
		return tern.thenExpr.determineType
	}
	
	def dispatch Optional<? extends HDLType> determineType(HDLInlineFunction func) {
		throw new HDLProblemException(new Problem(ErrorCode::INLINE_FUNCTION_NO_TYPE, func))
	}
	
}
