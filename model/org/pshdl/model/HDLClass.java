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
package org.pshdl.model;

import java.util.HashMap;
import java.util.Map;

public enum HDLClass {
	HDLObject(HDLObject.class), // HDLObject
	HDLExpression(HDLExpression.class), // HDLExpression
	HDLReference(HDLReference.class), // HDLReference
	HDLUnresolvedFragment(HDLUnresolvedFragment.class), // HDLUnresolvedFragment
	HDLUnresolvedFragmentFunction(HDLUnresolvedFragmentFunction.class), // HDLUnresolvedFragmentFunction
	HDLResolvedRef(HDLResolvedRef.class), // HDLResolvedRef
	HDLEnumRef(HDLEnumRef.class), // HDLEnumRef
	HDLVariableRef(HDLVariableRef.class), // HDLVariableRef
	HDLInterfaceRef(HDLInterfaceRef.class), // HDLInterfaceRef
	HDLOpExpression(HDLOpExpression.class), // HDLOpExpression
	HDLArithOp(HDLArithOp.class), // HDLArithOp
	HDLBitOp(HDLBitOp.class), // HDLBitOp
	HDLEqualityOp(HDLEqualityOp.class), // HDLEqualityOp
	HDLShiftOp(HDLShiftOp.class), // HDLShiftOp
	HDLLiteral(HDLLiteral.class), // HDLLiteral
	HDLFunctionCall(HDLFunctionCall.class), // HDLFunctionCall
	HDLManip(HDLManip.class), // HDLManip
	HDLConcat(HDLConcat.class), // HDLConcat
	HDLTernary(HDLTernary.class), // HDLTernary
	HDLArrayInit(HDLArrayInit.class), // HDLArrayInit
	HDLStatement(HDLStatement.class), // HDLStatement
	HDLDeclaration(HDLDeclaration.class), // HDLDeclaration
	HDLFunction(HDLFunction.class), // HDLFunction
	HDLNativeFunction(HDLNativeFunction.class), // HDLNativeFunction
	HDLInlineFunction(HDLInlineFunction.class), // HDLInlineFunction
	HDLSubstituteFunction(HDLSubstituteFunction.class), // HDLSubstituteFunction
	HDLInterfaceDeclaration(HDLInterfaceDeclaration.class), // HDLInterfaceDeclaration
	HDLEnumDeclaration(HDLEnumDeclaration.class), // HDLEnumDeclaration
	HDLVariableDeclaration(HDLVariableDeclaration.class), // HDLVariableDeclaration
	HDLInstantiation(HDLInstantiation.class), // HDLInstantiation
	HDLDirectGeneration(HDLDirectGeneration.class), // HDLDirectGeneration
	HDLInterfaceInstantiation(HDLInterfaceInstantiation.class), // HDLInterfaceInstantiation
	HDLAssignment(HDLAssignment.class), // HDLAssignment
	HDLCompound(HDLCompound.class), // HDLCompound
	HDLForLoop(HDLForLoop.class), // HDLForLoop
	HDLIfStatement(HDLIfStatement.class), // HDLIfStatement
	HDLSwitchStatement(HDLSwitchStatement.class), // HDLSwitchStatement
	HDLSwitchCaseStatement(HDLSwitchCaseStatement.class), // HDLSwitchCaseStatement
	HDLBlock(HDLBlock.class), // HDLBlock
	HDLExport(HDLExport.class), // HDLExport
	HDLRange(HDLRange.class), // HDLRange
	HDLFunctionParameter(HDLFunctionParameter.class), // HDLFunctionParameter
	HDLUnit(HDLUnit.class), // HDLUnit
	HDLRegisterConfig(HDLRegisterConfig.class), // HDLRegisterConfig
	HDLVariable(HDLVariable.class), // HDLVariable
	HDLType(HDLType.class), // HDLType
	HDLInterface(HDLInterface.class), // HDLInterface
	HDLValueType(HDLValueType.class), // HDLValueType
	HDLEnum(HDLEnum.class), // HDLEnum
	HDLPrimitive(HDLPrimitive.class), // HDLPrimitive
	HDLAnnotation(HDLAnnotation.class), // HDLAnnotation
	HDLArgument(HDLArgument.class), // HDLArgument
	HDLPackage(HDLPackage.class),// HDLPackage
	;
	public final Class<?> clazz;

	private static Map<Class<?>, HDLClass> classMap;

	HDLClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	public static synchronized HDLClass getClassFor(Class<?> clazz) {
		if (classMap == null) {
			classMap = new HashMap<>();
			for (final HDLClass cl : values()) {
				classMap.put(cl.clazz, cl);
			}
		}
		return classMap.get(clazz);
	}
}