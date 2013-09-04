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
package org.pshdl.generator.vhdl

import org.pshdl.generator.vhdl.libraries.VHDLCastsLibrary
import org.pshdl.generator.vhdl.libraries.VHDLCastsLibrary$TargetType
import org.pshdl.generator.vhdl.libraries.VHDLShiftLibrary
import org.pshdl.generator.vhdl.libraries.VHDLTypesLibrary
import org.pshdl.model.HDLArithOp
import org.pshdl.model.HDLBitOp
import org.pshdl.model.HDLClass
import org.pshdl.model.HDLConcat
import org.pshdl.model.HDLEnumRef
import org.pshdl.model.HDLEqualityOp
import org.pshdl.model.HDLExpression
import org.pshdl.model.HDLFunction
import org.pshdl.model.HDLFunctionCall
import org.pshdl.model.HDLInterfaceRef
import org.pshdl.model.HDLLiteral
import org.pshdl.model.HDLManip
import org.pshdl.model.HDLPrimitive
import org.pshdl.model.HDLPrimitive$HDLPrimitiveType
import org.pshdl.model.HDLRange
import org.pshdl.model.HDLReference
import org.pshdl.model.HDLShiftOp
import org.pshdl.model.HDLTernary
import org.pshdl.model.HDLVariableRef
import org.pshdl.model.extensions.TypeExtension
import org.pshdl.model.types.builtIn.HDLFunctions
import org.pshdl.model.types.builtIn.HDLPrimitives
import de.upb.hni.vmagic.AssociationElement
import de.upb.hni.vmagic.Range
import de.upb.hni.vmagic.Range$Direction
import de.upb.hni.vmagic.builtin.Standard
import de.upb.hni.vmagic.expression.Add
import de.upb.hni.vmagic.expression.And
import de.upb.hni.vmagic.expression.Concatenate
import de.upb.hni.vmagic.expression.Divide
import de.upb.hni.vmagic.expression.Equals
import de.upb.hni.vmagic.expression.Expression
import de.upb.hni.vmagic.expression.FunctionCall
import de.upb.hni.vmagic.expression.GreaterEquals
import de.upb.hni.vmagic.expression.GreaterThan
import de.upb.hni.vmagic.expression.LessEquals
import de.upb.hni.vmagic.expression.LessThan
import de.upb.hni.vmagic.expression.Literal
import de.upb.hni.vmagic.expression.Minus
import de.upb.hni.vmagic.expression.Multiply
import de.upb.hni.vmagic.expression.Name
import de.upb.hni.vmagic.expression.Not
import de.upb.hni.vmagic.expression.NotEquals
import de.upb.hni.vmagic.expression.Or
import de.upb.hni.vmagic.expression.Parentheses
import de.upb.hni.vmagic.expression.Pow
import de.upb.hni.vmagic.expression.Rem
import de.upb.hni.vmagic.expression.Subtract
import de.upb.hni.vmagic.expression.Xor
import de.upb.hni.vmagic.literal.BasedLiteral
import de.upb.hni.vmagic.literal.DecimalLiteral
import de.upb.hni.vmagic.literal.StringLiteral
import de.upb.hni.vmagic.object.ArrayElement
import de.upb.hni.vmagic.object.Signal
import de.upb.hni.vmagic.object.Slice
import de.upb.hni.vmagic.type.UnresolvedType
import java.math.BigInteger
import java.util.LinkedList
import java.util.List

import static org.pshdl.generator.vhdl.VHDLExpressionExtension.*
import static org.pshdl.model.HDLArithOp$HDLArithOpType.*
import static org.pshdl.model.HDLBitOp$HDLBitOpType.*
import static org.pshdl.model.HDLEqualityOp$HDLEqualityOpType.*
import static org.pshdl.model.HDLLiteral$HDLLiteralPresentation.*
import static org.pshdl.model.HDLManip$HDLManipType.*
import org.pshdl.model.HDLArrayInit
import de.upb.hni.vmagic.expression.Aggregate
import java.util.ArrayList
import de.upb.hni.vmagic.Choices
import de.upb.hni.vmagic.literal.CharacterLiteral

class VHDLExpressionExtension {

	public static VHDLExpressionExtension INST = new VHDLExpressionExtension

	def static Expression<?> vhdlOf(HDLExpression exp) {
		return INST.toVHDL(exp)
	}

	def dispatch Expression<?> toVHDL(HDLExpression exp) {
		throw new IllegalArgumentException("Not implemented for type:" + exp.classType)
	}

	def dispatch Name<?> toVHDL(HDLReference ref) {
		throw new IllegalArgumentException("Not implemented for type:" + ref.classType)
	}

	def dispatch String getVHDLName(HDLVariableRef obj) {
		return obj.varRefName.lastSegment
	}

	def dispatch String getVHDLName(HDLInterfaceRef obj) {
		return obj.HIfRefName.lastSegment + "_" + obj.varRefName.lastSegment
	}

	def dispatch Name<?> toVHDL(HDLVariableRef obj) {
		var Name<?> result = new Signal(obj.VHDLName, UnresolvedType::NO_NAME)
		result = getRef(result, obj)
		return result
	}

	def private Name<?> getRef(Name<?> name, HDLVariableRef ref) {
		var result = name
		if (ref.array.size != 0) {
			val List<Expression> indices = new LinkedList<Expression>
			for (HDLExpression arr : ref.array) {
				indices.add(arr.toVHDL)
			}
			result = new ArrayElement<Name<?>>(name, indices)
		}
		if (ref.bits.size > 0) {

			//TODO Make any directional access work (5:0, 0:5)
			if (ref.bits.size > 1)
				throw new IllegalArgumentException("Multi bit access not supported")
			val HDLRange r = ref.bits.get(0)
			if (r.from === null) {
				result = new ArrayElement<Name<?>>(result, r.to.toVHDL)
			} else {
				result = new Slice<Name<?>>(result, r.toVHDL(Range$Direction::DOWNTO))
			}
		}
		return result
	}

	def dispatch Expression<?> toVHDL(HDLArrayInit obj) {
		if (obj.exp.size==1)
			return obj.exp.get(0).toVHDL
		val aggr = new Aggregate()
		obj.exp.forEach([e, i|aggr.createAssociation(e.toVHDL, new DecimalLiteral(i))])
		aggr.createAssociation(Aggregate::OTHERS(new CharacterLiteral('0'.charAt(0))), Choices::OTHERS)
		return aggr
	}

	def dispatch Name<?> toVHDL(HDLInterfaceRef obj) {
		var Name<?> result = new Signal(obj.VHDLName, UnresolvedType::NO_NAME)
		if (obj.ifArray.size != 0) {
			result = new ArrayElement<Name<?>>(result,
				obj.ifArray.fold(new LinkedList<Expression>)[l, e|l.add(e.toVHDL); l])
		}
		return getRef(result, obj)
	}

	def dispatch Expression<?> toVHDL(HDLFunctionCall obj) {
		return HDLFunctions::toVHDLExpression(obj)
	}

	def dispatch Signal toVHDL(HDLEnumRef obj) {
		return new Signal(obj.varRefName.lastSegment, UnresolvedType::NO_NAME)
	}

	def dispatch Expression<?> toVHDL(HDLConcat obj) {
		val List<HDLExpression> cats = obj.cats
		var Expression<?> res = cats.get(0).toVHDL
		cats.remove(0)
		for (HDLExpression cat : cats) {
			res = new Concatenate(res, cat.toVHDL)
		}
		return res
	}

	def dispatch Expression<?> toVHDL(HDLManip obj) {
		switch (type:obj.type) {
			case ARITH_NEG:
				return new Minus(obj.target.toVHDL)
			case type === LOGIC_NEG || type === BIT_NEG:
				return new Not(obj.target.toVHDL)
			case CAST: {
				val HDLPrimitive targetType = obj.castTo as HDLPrimitive
				if (targetType.type === HDLPrimitiveType::STRING)
					return obj.target.toVHDL
				val HDLExpression tWidth = targetType.getWidth
				if (obj.target.classType === HDLClass::HDLLiteral) {
					return VHDLCastsLibrary::handleLiteral(obj.container, obj.target as HDLLiteral, targetType, tWidth)
				}
				val HDLPrimitive t = TypeExtension::typeOf(obj.target).get as HDLPrimitive
				var Expression<?> exp = obj.target.toVHDL
				var HDLPrimitiveType actualType = t.type
				if (tWidth !== null) {
					val TargetType resized = VHDLCastsLibrary::getResize(exp, t, tWidth)
					exp = resized.resized
					actualType = resized.newType
				}
				return VHDLCastsLibrary::cast(exp, actualType, targetType.getType)
			}
		}
		throw new IllegalArgumentException("Not supported:" + obj)
	}

	def Range toVHDL(HDLRange obj, Range$Direction dir) {
		val Expression<?> to = HDLPrimitives::simplifyWidth(obj, obj.to).toVHDL
		if (obj.from === null)
			return new Range(to, dir, to)
		return new Range(HDLPrimitives::simplifyWidth(obj, obj.from).toVHDL, dir, to)
	}

	def dispatch Literal<?> toVHDL(HDLLiteral obj) {
		var int length = -1
		if (obj.valueAsBigInt !== null)
			length = obj.valueAsBigInt.bitLength
		return obj.toVHDL(length, false)
	}

	def Literal<?> toVHDL(HDLLiteral obj, int length, boolean asString) {
		var l = length
		var String sVal = obj.^val
		if (l == 0)
			l = 1
		val BigInteger dec = obj.valueAsBigInt
		switch (obj.presentation) {
			case STR:
				return new StringLiteral(sVal)
			case BOOL: {
				if ("true".equals(sVal))
					return Standard::BOOLEAN_TRUE
				return Standard::BOOLEAN_FALSE
			}
			case HEX: {
				if (asString)
					return VHDLUtils::toHexLiteral(l, dec)
				return new BasedLiteral("16#" + sVal.substring(2) + "#")
			}
			case BIN: {
				if (asString)
					return VHDLUtils::toBinaryLiteral(l, dec)
				return new BasedLiteral("2#" + sVal.substring(2) + "#")
			}
		}
		if (dec.bitLength > 31 || asString)
			return VHDLUtils::toBinaryLiteral(l, dec)
		return new DecimalLiteral(sVal)
	}

	def dispatch Expression<?> toVHDL(HDLShiftOp obj) {
		val HDLPrimitive type = TypeExtension::typeOf(obj.left).get as HDLPrimitive
		return VHDLShiftLibrary::shift(obj.left.toVHDL, obj.right.toVHDL, type.type, obj.type)
	}

	def dispatch Expression<?> toVHDL(HDLEqualityOp obj) {
		switch (obj.type) {
			case EQ:
				return new Parentheses(new Equals(obj.left.toVHDL, obj.right.toVHDL))
			case GREATER_EQ:
				return new Parentheses(new GreaterEquals(obj.left.toVHDL, obj.right.toVHDL))
			case GREATER:
				return new Parentheses(new GreaterThan(obj.left.toVHDL, obj.right.toVHDL))
			case LESS_EQ:
				return new Parentheses(new LessEquals(obj.left.toVHDL, obj.right.toVHDL))
			case LESS:
				return new Parentheses(new LessThan(obj.left.toVHDL, obj.right.toVHDL))
			case NOT_EQ:
				return new Parentheses(new NotEquals(obj.left.toVHDL, obj.right.toVHDL))
		}
		throw new IllegalArgumentException("Not supported:" + obj)
	}

	def dispatch Expression<?> toVHDL(HDLBitOp obj) {
		switch (type:obj.type) {
			case type === AND || type === LOGI_AND:
				return new Parentheses(new And(obj.left.toVHDL, obj.right.toVHDL))
			case type === OR || type === LOGI_OR:
				return new Parentheses(new Or(obj.left.toVHDL, obj.right.toVHDL))
			case XOR:
				return new Parentheses(new Xor(obj.left.toVHDL, obj.right.toVHDL))
		}
		throw new IllegalArgumentException("Not supported:" + obj)
	}

	def dispatch Expression<?> toVHDL(HDLArithOp obj) {
		switch (obj.type) {
			case PLUS:
				return new Parentheses(new Add(obj.left.toVHDL, obj.right.toVHDL))
			case MINUS:
				return new Parentheses(new Subtract(obj.left.toVHDL, obj.right.toVHDL))
			case DIV:
				return new Parentheses(new Divide(obj.left.toVHDL, obj.right.toVHDL))
			case MUL:
				return new Parentheses(new Multiply(obj.left.toVHDL, obj.right.toVHDL))
			case MOD:
				return new Parentheses(new Rem(obj.left.toVHDL, obj.right.toVHDL))
			case POW:
				return new Parentheses(new Pow(obj.left.toVHDL, obj.right.toVHDL))
		}
		throw new IllegalArgumentException("Not supported:" + obj)
	}

	def dispatch Expression<?> toVHDL(HDLTernary obj) {
		val FunctionCall fc = new FunctionCall(VHDLTypesLibrary::TERNARY_SLV)
		val List<AssociationElement> parameters = fc.parameters
		parameters.add(new AssociationElement(obj.ifExpr.toVHDL))
		parameters.add(new AssociationElement(obj.thenExpr.toVHDL))
		parameters.add(new AssociationElement(obj.elseExpr.toVHDL))
		return fc
	}

	def dispatch Expression<?> toVHDL(HDLFunction obj) {
		throw new IllegalArgumentException("Not supported:" + obj)
	}
}
