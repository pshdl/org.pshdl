/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model.extensions

import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLArithOp$HDLArithOpType
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLClass
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLDirectGeneration
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLInlineFunction
import org.pshdl.model.HDLInterfaceInstantiation
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLLiteral$HDLLiteralPresentation
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLPrimitive$HDLPrimitiveType
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLType
import org.pshdl.model.HDLVariable
import org.pshdl.model.HDLVariableDeclaration
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.IHDLObject
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import org.pshdl.model.utils.HDLProblemException
import org.pshdl.model.validation.Problem
import org.pshdl.model.validation.builtin.ErrorCode
import java.math.BigInteger
import java.util.Iterator
import java.util.List

import static org.pshdl.model.extensions.TypeExtension.*
import org.pshdl.model.HDLUnresolvedFragment
import org.pshdl.model.utils.Insulin
import org.pshdl.model.HDLObject$GenericMeta
import org.pshdl.model.HDLArrayInit
import org.pshdl.model.utils.HDLConfig
import org.pshdl.model.HDLRegisterConfig
import com.google.common.base.Optional

class TypeExtension {
	private static TypeExtension INST = new TypeExtension

	def static Optional<? extends HDLType> typeOf(IHDLObject obj) {
		if (!obj.isFrozen)
			throw new IllegalArgumentException("Target needs to be frozen")
		var res = INST.determineType(obj)
		if (res.present) {
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
		if (HDLRegisterConfig::DEF_CLK == hVar.name)
			return Optional::of(HDLPrimitive::bit)
		if (HDLRegisterConfig::DEF_RST == hVar.name)
			return Optional::of(HDLPrimitive::bit)
		val IHDLObject container = hVar.container
		if (container === null)
			return Optional::absent
		switch (container.classType) {
			case HDLClass::HDLVariableDeclaration:
				return (container as HDLVariableDeclaration).determineType
			case HDLClass::HDLDirectGeneration:
				return Optional::fromNullable((container as HDLDirectGeneration).HIf)
			case HDLClass::HDLInterfaceInstantiation:
				return (container as HDLInterfaceInstantiation).resolveHIf
			case HDLClass::HDLForLoop:
				return Optional::of(HDLPrimitive::natural)
			case HDLClass::HDLInlineFunction:
				return Optional::absent
			case HDLClass::HDLSubstituteFunction:
				return Optional::absent
		}
		return Optional::absent
	}

	def dispatch Optional<? extends HDLType> determineType(HDLVariableDeclaration hvd) {
		if (hvd.primitive !== null)
			return Optional::of(hvd.primitive)
		return hvd.resolveType
	}

	def dispatch Optional<? extends HDLType> determineType(HDLArrayInit ai) {
		if (ai.exp.size == 1)
			return ai.exp.get(0).determineType
		var res = HDLPrimitive::natural
		for (exp : ai.exp) {
			val sub = exp.determineType
			if (sub.present && !sub.get.equals(res))
				return sub
		}
		return Optional::of(res)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLExpression cat) {
		throw new RuntimeException("Did not correctly implement determineType for:" + cat.classType)
	}

	private static GenericMeta<Boolean> DETERMINE_TYPE_RESOLVE = new GenericMeta<Boolean>("DETERMINE_TYPE_RESOLVE",
		false);

	def dispatch Optional<? extends HDLType> determineType(HDLUnresolvedFragment cat) {
		if (cat.hasMeta(DETERMINE_TYPE_RESOLVE))
			return Optional::absent
		cat.setMeta(DETERMINE_TYPE_RESOLVE)
		var resolved = Insulin::resolveFragment(cat)
		if (!resolved.present)
			return Optional::absent
		return resolved.get.copyDeepFrozen(cat.container).determineType
	}

	def dispatch Optional<? extends HDLType> determineType(HDLConcat cat) {
		val Iterator<HDLExpression> iter = cat.cats.iterator
		val nextType = iter.next.determineType;
		if (!nextType.present)
			return Optional::absent
		var HDLType type = nextType.get
		if (!(type instanceof HDLPrimitive))
			return Optional::absent
		var HDLExpression width = getWidth(type)
		while (iter.hasNext) {
			if (width === null)
				//This can happen when we have invalid concatenations
				return Optional::absent
			val nextCatType = iter.next.determineType
			if (!nextCatType.present)
				return Optional::absent
			type = nextCatType.get
			if (!(type instanceof HDLPrimitive))
				return Optional::absent
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(getWidth(type))
			width = HDLPrimitives::simplifyWidth(cat, width)
		}
		return Optional::of(HDLPrimitive::bitvector.setWidth(width).setContainer(cat))
	}

	def static dispatch HDLExpression getWidth(IHDLObject obj) {
		val type = INST.determineType(obj)
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
		return ref.resolveHEnum
	}

	def dispatch Optional<? extends HDLType> determineType(HDLManip manip) {
		return Optional::fromNullable(HDLPrimitives::instance.getManipOpType(manip).result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLFunctionCall call) {
		return Optional::fromNullable(HDLFunctions::getInferenceInfo(call)?.result)
	}

	def dispatch Optional<? extends HDLType> determineType(HDLLiteral lit) {

		// Actually depends on context
		switch (lit.presentation) {
			case HDLLiteral$HDLLiteralPresentation::STR:
				return Optional::of(new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::STRING))
			case HDLLiteral$HDLLiteralPresentation::BOOL:
				return Optional::of(new HDLPrimitive().setType(HDLPrimitive$HDLPrimitiveType::BOOL))
		}
		val boolean isSigned = lit.^val.charAt(0) != '-'
		val BigInteger bigVal = lit.valueAsBigInt
		if (bigVal.bitLength > 31)
			return Optional::of(HDLPrimitive::uint.setWidth(HDLLiteral::get(bigVal.bitLength)))
		return Optional::of(HDLPrimitive::target(isSigned))
	}

	def dispatch Optional<? extends HDLType> determineType(HDLVariableRef ref) {
		val List<HDLRange> bits = ref.bits
		if (bits.size == 0) {
			var res = ref.resolveVar
			if (res.present)
				return res.get.determineType
			else
				return Optional::absent
		}
		if (bits.size == 1 && bits.get(0).from === null)
			return Optional::of(HDLPrimitive::bit)
		val Iterator<HDLRange> iter = bits.iterator
		var HDLExpression width = HDLPrimitives::simplifyWidth(ref, iter.next.width)
		while (iter.hasNext) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOp$HDLArithOpType::PLUS).setRight(iter.next.width)
			width = HDLPrimitives::simplifyWidth(ref, width)
		}
		val hVar = ref.resolveVar
		if (!hVar.present)
			return Optional::absent
		val type = hVar.get.determineType;
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

	def dispatch Optional<? extends HDLType> determineType(HDLTernary tern) {
		return tern.thenExpr.determineType
	}

	def dispatch Optional<? extends HDLType> determineType(HDLInlineFunction func) {
		throw new HDLProblemException(new Problem(ErrorCode::INLINE_FUNCTION_NO_TYPE, func))
	}

}
