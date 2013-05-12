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
// Generated from PSHDLLang.g4 by ANTLR 4.0
package org.pshdl.model.parser;

import org.antlr.v4.runtime.tree.*;

public interface PSHDLLangListener extends ParseTreeListener {
	void enterPsVariableRef(PSHDLLangParser.PsVariableRefContext ctx);

	void exitPsVariableRef(PSHDLLangParser.PsVariableRefContext ctx);

	void enterPsProcess(PSHDLLangParser.PsProcessContext ctx);

	void exitPsProcess(PSHDLLangParser.PsProcessContext ctx);

	void enterPsVariable(PSHDLLangParser.PsVariableContext ctx);

	void exitPsVariable(PSHDLLangParser.PsVariableContext ctx);

	void enterPsPassedArguments(PSHDLLangParser.PsPassedArgumentsContext ctx);

	void exitPsPassedArguments(PSHDLLangParser.PsPassedArgumentsContext ctx);

	void enterPsSubstituteFunction(PSHDLLangParser.PsSubstituteFunctionContext ctx);

	void exitPsSubstituteFunction(PSHDLLangParser.PsSubstituteFunctionContext ctx);

	void enterPsFuncParamWithRW(PSHDLLangParser.PsFuncParamWithRWContext ctx);

	void exitPsFuncParamWithRW(PSHDLLangParser.PsFuncParamWithRWContext ctx);

	void enterPsManip(PSHDLLangParser.PsManipContext ctx);

	void exitPsManip(PSHDLLangParser.PsManipContext ctx);

	void enterPsBitLogAnd(PSHDLLangParser.PsBitLogAndContext ctx);

	void exitPsBitLogAnd(PSHDLLangParser.PsBitLogAndContext ctx);

	void enterPsBitLogOr(PSHDLLangParser.PsBitLogOrContext ctx);

	void exitPsBitLogOr(PSHDLLangParser.PsBitLogOrContext ctx);

	void enterPsVariableDeclaration(PSHDLLangParser.PsVariableDeclarationContext ctx);

	void exitPsVariableDeclaration(PSHDLLangParser.PsVariableDeclarationContext ctx);

	void enterPsFuncOptArray(PSHDLLangParser.PsFuncOptArrayContext ctx);

	void exitPsFuncOptArray(PSHDLLangParser.PsFuncOptArrayContext ctx);

	void enterPsFuncRecturnType(PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	void exitPsFuncRecturnType(PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	void enterPsBitXor(PSHDLLangParser.PsBitXorContext ctx);

	void exitPsBitXor(PSHDLLangParser.PsBitXorContext ctx);

	void enterPsNativeFunction(PSHDLLangParser.PsNativeFunctionContext ctx);

	void exitPsNativeFunction(PSHDLLangParser.PsNativeFunctionContext ctx);

	void enterPsEnum(PSHDLLangParser.PsEnumContext ctx);

	void exitPsEnum(PSHDLLangParser.PsEnumContext ctx);

	void enterPsQualifiedNameImport(PSHDLLangParser.PsQualifiedNameImportContext ctx);

	void exitPsQualifiedNameImport(PSHDLLangParser.PsQualifiedNameImportContext ctx);

	void enterPsInterfaceInstantiation(PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	void exitPsInterfaceInstantiation(PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	void enterPsForStatement(PSHDLLangParser.PsForStatementContext ctx);

	void exitPsForStatement(PSHDLLangParser.PsForStatementContext ctx);

	void enterPsSwitchStatement(PSHDLLangParser.PsSwitchStatementContext ctx);

	void exitPsSwitchStatement(PSHDLLangParser.PsSwitchStatementContext ctx);

	void enterPsFuncParam(PSHDLLangParser.PsFuncParamContext ctx);

	void exitPsFuncParam(PSHDLLangParser.PsFuncParamContext ctx);

	void enterPsAssignmentOp(PSHDLLangParser.PsAssignmentOpContext ctx);

	void exitPsAssignmentOp(PSHDLLangParser.PsAssignmentOpContext ctx);

	void enterPsEnumDeclaration(PSHDLLangParser.PsEnumDeclarationContext ctx);

	void exitPsEnumDeclaration(PSHDLLangParser.PsEnumDeclarationContext ctx);

	void enterPsWidth(PSHDLLangParser.PsWidthContext ctx);

	void exitPsWidth(PSHDLLangParser.PsWidthContext ctx);

	void enterPsAccessRange(PSHDLLangParser.PsAccessRangeContext ctx);

	void exitPsAccessRange(PSHDLLangParser.PsAccessRangeContext ctx);

	void enterPsArrayInitSubParens(PSHDLLangParser.PsArrayInitSubParensContext ctx);

	void exitPsArrayInitSubParens(PSHDLLangParser.PsArrayInitSubParensContext ctx);

	void enterPsInlineFunction(PSHDLLangParser.PsInlineFunctionContext ctx);

	void exitPsInlineFunction(PSHDLLangParser.PsInlineFunctionContext ctx);

	void enterPsParens(PSHDLLangParser.PsParensContext ctx);

	void exitPsParens(PSHDLLangParser.PsParensContext ctx);

	void enterPsBitAnd(PSHDLLangParser.PsBitAndContext ctx);

	void exitPsBitAnd(PSHDLLangParser.PsBitAndContext ctx);

	void enterPsFuncParamType(PSHDLLangParser.PsFuncParamTypeContext ctx);

	void exitPsFuncParamType(PSHDLLangParser.PsFuncParamTypeContext ctx);

	void enterPsCompoundStatement(PSHDLLangParser.PsCompoundStatementContext ctx);

	void exitPsCompoundStatement(PSHDLLangParser.PsCompoundStatementContext ctx);

	void enterPsStatement(PSHDLLangParser.PsStatementContext ctx);

	void exitPsStatement(PSHDLLangParser.PsStatementContext ctx);

	void enterPsValue(PSHDLLangParser.PsValueContext ctx);

	void exitPsValue(PSHDLLangParser.PsValueContext ctx);

	void enterPsInterfaceDeclaration(PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	void exitPsInterfaceDeclaration(PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	void enterPsEqualityComp(PSHDLLangParser.PsEqualityCompContext ctx);

	void exitPsEqualityComp(PSHDLLangParser.PsEqualityCompContext ctx);

	void enterPsConcat(PSHDLLangParser.PsConcatContext ctx);

	void exitPsConcat(PSHDLLangParser.PsConcatContext ctx);

	void enterPsPortDeclaration(PSHDLLangParser.PsPortDeclarationContext ctx);

	void exitPsPortDeclaration(PSHDLLangParser.PsPortDeclarationContext ctx);

	void enterPsAnnotationType(PSHDLLangParser.PsAnnotationTypeContext ctx);

	void exitPsAnnotationType(PSHDLLangParser.PsAnnotationTypeContext ctx);

	void enterPsDeclAssignment(PSHDLLangParser.PsDeclAssignmentContext ctx);

	void exitPsDeclAssignment(PSHDLLangParser.PsDeclAssignmentContext ctx);

	void enterPsBlock(PSHDLLangParser.PsBlockContext ctx);

	void exitPsBlock(PSHDLLangParser.PsBlockContext ctx);

	void enterPsDirectGeneration(PSHDLLangParser.PsDirectGenerationContext ctx);

	void exitPsDirectGeneration(PSHDLLangParser.PsDirectGenerationContext ctx);

	void enterPsEquality(PSHDLLangParser.PsEqualityContext ctx);

	void exitPsEquality(PSHDLLangParser.PsEqualityContext ctx);

	void enterPsAnnotation(PSHDLLangParser.PsAnnotationContext ctx);

	void exitPsAnnotation(PSHDLLangParser.PsAnnotationContext ctx);

	void enterPsCast(PSHDLLangParser.PsCastContext ctx);

	void exitPsCast(PSHDLLangParser.PsCastContext ctx);

	void enterPsQualifiedName(PSHDLLangParser.PsQualifiedNameContext ctx);

	void exitPsQualifiedName(PSHDLLangParser.PsQualifiedNameContext ctx);

	void enterPsArrayInitExp(PSHDLLangParser.PsArrayInitExpContext ctx);

	void exitPsArrayInitExp(PSHDLLangParser.PsArrayInitExpContext ctx);

	void enterPsRefPart(PSHDLLangParser.PsRefPartContext ctx);

	void exitPsRefPart(PSHDLLangParser.PsRefPartContext ctx);

	void enterPsFunction(PSHDLLangParser.PsFunctionContext ctx);

	void exitPsFunction(PSHDLLangParser.PsFunctionContext ctx);

	void enterPsArrayInit(PSHDLLangParser.PsArrayInitContext ctx);

	void exitPsArrayInit(PSHDLLangParser.PsArrayInitContext ctx);

	void enterPsFunctionDeclaration(PSHDLLangParser.PsFunctionDeclarationContext ctx);

	void exitPsFunctionDeclaration(PSHDLLangParser.PsFunctionDeclarationContext ctx);

	void enterPsDeclarationType(PSHDLLangParser.PsDeclarationTypeContext ctx);

	void exitPsDeclarationType(PSHDLLangParser.PsDeclarationTypeContext ctx);

	void enterPsInterfaceDecl(PSHDLLangParser.PsInterfaceDeclContext ctx);

	void exitPsInterfaceDecl(PSHDLLangParser.PsInterfaceDeclContext ctx);

	void enterPsInterfaceExtends(PSHDLLangParser.PsInterfaceExtendsContext ctx);

	void exitPsInterfaceExtends(PSHDLLangParser.PsInterfaceExtendsContext ctx);

	void enterPsValueExp(PSHDLLangParser.PsValueExpContext ctx);

	void exitPsValueExp(PSHDLLangParser.PsValueExpContext ctx);

	void enterPsUnit(PSHDLLangParser.PsUnitContext ctx);

	void exitPsUnit(PSHDLLangParser.PsUnitContext ctx);

	void enterPsInstantiation(PSHDLLangParser.PsInstantiationContext ctx);

	void exitPsInstantiation(PSHDLLangParser.PsInstantiationContext ctx);

	void enterPsTypeDeclaration(PSHDLLangParser.PsTypeDeclarationContext ctx);

	void exitPsTypeDeclaration(PSHDLLangParser.PsTypeDeclarationContext ctx);

	void enterPsAssignmentOrFunc(PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	void exitPsAssignmentOrFunc(PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	void enterPsDirection(PSHDLLangParser.PsDirectionContext ctx);

	void exitPsDirection(PSHDLLangParser.PsDirectionContext ctx);

	void enterPsMul(PSHDLLangParser.PsMulContext ctx);

	void exitPsMul(PSHDLLangParser.PsMulContext ctx);

	void enterPsPrimitiveType(PSHDLLangParser.PsPrimitiveTypeContext ctx);

	void exitPsPrimitiveType(PSHDLLangParser.PsPrimitiveTypeContext ctx);

	void enterPsModel(PSHDLLangParser.PsModelContext ctx);

	void exitPsModel(PSHDLLangParser.PsModelContext ctx);

	void enterPsBitAccess(PSHDLLangParser.PsBitAccessContext ctx);

	void exitPsBitAccess(PSHDLLangParser.PsBitAccessContext ctx);

	void enterPsArray(PSHDLLangParser.PsArrayContext ctx);

	void exitPsArray(PSHDLLangParser.PsArrayContext ctx);

	void enterPsFuncArgs(PSHDLLangParser.PsFuncArgsContext ctx);

	void exitPsFuncArgs(PSHDLLangParser.PsFuncArgsContext ctx);

	void enterPsImports(PSHDLLangParser.PsImportsContext ctx);

	void exitPsImports(PSHDLLangParser.PsImportsContext ctx);

	void enterPsArrayInitSub(PSHDLLangParser.PsArrayInitSubContext ctx);

	void exitPsArrayInitSub(PSHDLLangParser.PsArrayInitSubContext ctx);

	void enterPsFuncSpec(PSHDLLangParser.PsFuncSpecContext ctx);

	void exitPsFuncSpec(PSHDLLangParser.PsFuncSpecContext ctx);

	void enterPsInterface(PSHDLLangParser.PsInterfaceContext ctx);

	void exitPsInterface(PSHDLLangParser.PsInterfaceContext ctx);

	void enterPsSimpleBlock(PSHDLLangParser.PsSimpleBlockContext ctx);

	void exitPsSimpleBlock(PSHDLLangParser.PsSimpleBlockContext ctx);

	void enterPsPrimitive(PSHDLLangParser.PsPrimitiveContext ctx);

	void exitPsPrimitive(PSHDLLangParser.PsPrimitiveContext ctx);

	void enterPsBitOr(PSHDLLangParser.PsBitOrContext ctx);

	void exitPsBitOr(PSHDLLangParser.PsBitOrContext ctx);

	void enterPsExtends(PSHDLLangParser.PsExtendsContext ctx);

	void exitPsExtends(PSHDLLangParser.PsExtendsContext ctx);

	void enterPsArgument(PSHDLLangParser.PsArgumentContext ctx);

	void exitPsArgument(PSHDLLangParser.PsArgumentContext ctx);

	void enterPsShift(PSHDLLangParser.PsShiftContext ctx);

	void exitPsShift(PSHDLLangParser.PsShiftContext ctx);

	void enterPsCaseStatements(PSHDLLangParser.PsCaseStatementsContext ctx);

	void exitPsCaseStatements(PSHDLLangParser.PsCaseStatementsContext ctx);

	void enterPsAdd(PSHDLLangParser.PsAddContext ctx);

	void exitPsAdd(PSHDLLangParser.PsAddContext ctx);

	void enterPsTernary(PSHDLLangParser.PsTernaryContext ctx);

	void exitPsTernary(PSHDLLangParser.PsTernaryContext ctx);

	void enterPsDeclaration(PSHDLLangParser.PsDeclarationContext ctx);

	void exitPsDeclaration(PSHDLLangParser.PsDeclarationContext ctx);

	void enterPsFuncParamRWType(PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	void exitPsFuncParamRWType(PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	void enterPsIfStatement(PSHDLLangParser.PsIfStatementContext ctx);

	void exitPsIfStatement(PSHDLLangParser.PsIfStatementContext ctx);
}