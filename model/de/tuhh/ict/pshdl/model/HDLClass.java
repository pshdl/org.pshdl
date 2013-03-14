package de.tuhh.ict.pshdl.model;

import java.util.*;

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
	HDLRange(HDLRange.class), // HDLRange
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
	HDLPackage(HDLPackage.class), // HDLPackage
	;
	public final Class<?> clazz;

	private static Map<Class<?>, HDLClass> classMap;

	HDLClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	public static synchronized HDLClass getClassFor(Class<?> clazz) {
		if (classMap == null) {
			classMap = new HashMap<Class<?>, HDLClass>();
			for (HDLClass cl : values()) {
				classMap.put(cl.clazz, cl);
			}
		}
		return classMap.get(clazz);
	}
}