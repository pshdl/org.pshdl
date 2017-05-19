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
// Generated from PSHDLLang.g4 by ANTLR 4.7
package org.pshdl.model.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PSHDLLang}.
 */
public interface PSHDLLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsModel(PSHDLLang.PsModelContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsModel(PSHDLLang.PsModelContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsUnit(PSHDLLang.PsUnitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsUnit(PSHDLLang.PsUnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsExtends(PSHDLLang.PsExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsExtends(PSHDLLang.PsExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsImports(PSHDLLang.PsImportsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsImports(PSHDLLang.PsImportsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedNameImport(PSHDLLang.PsQualifiedNameImportContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedNameImport(PSHDLLang.PsQualifiedNameImportContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBlock(PSHDLLang.PsBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBlock(PSHDLLang.PsBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsProcess(PSHDLLang.PsProcessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsProcess(PSHDLLang.PsProcessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInstantiation(PSHDLLang.PsInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInstantiation(PSHDLLang.PsInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLang#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceInstantiation(PSHDLLang.PsInterfaceInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceInstantiation(PSHDLLang.PsInterfaceInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psDirectGeneration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirectGeneration(PSHDLLang.PsDirectGenerationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psDirectGeneration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirectGeneration(PSHDLLang.PsDirectGenerationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPassedArguments(PSHDLLang.PsPassedArgumentsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPassedArguments(PSHDLLang.PsPassedArgumentsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArgument(PSHDLLang.PsArgumentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArgument(PSHDLLang.PsArgumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCast(PSHDLLang.PsCastContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCast(PSHDLLang.PsCastContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitAnd} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAnd(PSHDLLang.PsBitAndContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitAnd} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAnd(PSHDLLang.PsBitAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psAdd} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAdd(PSHDLLang.PsAddContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psAdd} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAdd(PSHDLLang.PsAddContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psConcat} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsConcat(PSHDLLang.PsConcatContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psConcat} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsConcat(PSHDLLang.PsConcatContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psValueExp} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValueExp(PSHDLLang.PsValueExpContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psValueExp} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValueExp(PSHDLLang.PsValueExpContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psShift} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsShift(PSHDLLang.PsShiftContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psShift} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsShift(PSHDLLang.PsShiftContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitLogAnd} labeled
	 * alternative in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogAnd(PSHDLLang.PsBitLogAndContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitLogAnd} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogAnd(PSHDLLang.PsBitLogAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitOr} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitOr(PSHDLLang.PsBitOrContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitOr} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitOr(PSHDLLang.PsBitOrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psParens} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsParens(PSHDLLang.PsParensContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psParens} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsParens(PSHDLLang.PsParensContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psEqualityComp} labeled
	 * alternative in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEqualityComp(PSHDLLang.PsEqualityCompContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psEqualityComp} labeled
	 * alternative in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEqualityComp(PSHDLLang.PsEqualityCompContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitLogOr} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogOr(PSHDLLang.PsBitLogOrContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitLogOr} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogOr(PSHDLLang.PsBitLogOrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psTernary} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTernary(PSHDLLang.PsTernaryContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psTernary} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTernary(PSHDLLang.PsTernaryContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psArrayInitExp} labeled
	 * alternative in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitExp(PSHDLLang.PsArrayInitExpContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psArrayInitExp} labeled
	 * alternative in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitExp(PSHDLLang.PsArrayInitExpContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psManip} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsManip(PSHDLLang.PsManipContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psManip} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsManip(PSHDLLang.PsManipContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psEquality} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEquality(PSHDLLang.PsEqualityContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psEquality} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEquality(PSHDLLang.PsEqualityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitXor} labeled alternative
	 * in {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitXor(PSHDLLang.PsBitXorContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitXor} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitXor(PSHDLLang.PsBitXorContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psMul} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsMul(PSHDLLang.PsMulContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psMul} labeled alternative in
	 * {@link PSHDLLang#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsMul(PSHDLLang.PsMulContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValue(PSHDLLang.PsValueContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValue(PSHDLLang.PsValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAccess(PSHDLLang.PsBitAccessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAccess(PSHDLLang.PsBitAccessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAccessRange(PSHDLLang.PsAccessRangeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAccessRange(PSHDLLang.PsAccessRangeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableRef(PSHDLLang.PsVariableRefContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableRef(PSHDLLang.PsVariableRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsRefPart(PSHDLLang.PsRefPartContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsRefPart(PSHDLLang.PsRefPartContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariable(PSHDLLang.PsVariableContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariable(PSHDLLang.PsVariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psVariableMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableMatch(PSHDLLang.PsVariableMatchContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psVariableMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableMatch(PSHDLLang.PsVariableMatchContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsStatement(PSHDLLang.PsStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsStatement(PSHDLLang.PsStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psGroupMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsGroupMatch(PSHDLLang.PsGroupMatchContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psGroupMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsGroupMatch(PSHDLLang.PsGroupMatchContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psExport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsExport(PSHDLLang.PsExportContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psExport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsExport(PSHDLLang.PsExportContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunctionDeclaration(PSHDLLang.PsFunctionDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunctionDeclaration(PSHDLLang.PsFunctionDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInlineFunction(PSHDLLang.PsInlineFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInlineFunction(PSHDLLang.PsInlineFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSubstituteFunction(PSHDLLang.PsSubstituteFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSubstituteFunction(PSHDLLang.PsSubstituteFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsNativeFunction(PSHDLLang.PsNativeFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsNativeFunction(PSHDLLang.PsNativeFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncRecturnType(PSHDLLang.PsFuncRecturnTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncRecturnType(PSHDLLang.PsFuncRecturnTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParam(PSHDLLang.PsFuncParamContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParam(PSHDLLang.PsFuncParamContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncSpec(PSHDLLang.PsFuncSpecContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncSpec(PSHDLLang.PsFuncSpecContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamWithRW(PSHDLLang.PsFuncParamWithRWContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamWithRW(PSHDLLang.PsFuncParamWithRWContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncOptArray(PSHDLLang.PsFuncOptArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncOptArray(PSHDLLang.PsFuncOptArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamRWType(PSHDLLang.PsFuncParamRWTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamRWType(PSHDLLang.PsFuncParamRWTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamType(PSHDLLang.PsFuncParamTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamType(PSHDLLang.PsFuncParamTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunction(PSHDLLang.PsFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunction(PSHDLLang.PsFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncArgs(PSHDLLang.PsFuncArgsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncArgs(PSHDLLang.PsFuncArgsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psAssignmentOrFunc}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOrFunc(PSHDLLang.PsAssignmentOrFuncContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psAssignmentOrFunc}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOrFunc(PSHDLLang.PsAssignmentOrFuncContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOp(PSHDLLang.PsAssignmentOpContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOp(PSHDLLang.PsAssignmentOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psCompoundStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCompoundStatement(PSHDLLang.PsCompoundStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psCompoundStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCompoundStatement(PSHDLLang.PsCompoundStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsIfStatement(PSHDLLang.PsIfStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsIfStatement(PSHDLLang.PsIfStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSimpleBlock(PSHDLLang.PsSimpleBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSimpleBlock(PSHDLLang.PsSimpleBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsForStatement(PSHDLLang.PsForStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsForStatement(PSHDLLang.PsForStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSwitchStatement(PSHDLLang.PsSwitchStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSwitchStatement(PSHDLLang.PsSwitchStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCaseStatements(PSHDLLang.PsCaseStatementsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCaseStatements(PSHDLLang.PsCaseStatementsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclaration(PSHDLLang.PsDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclaration(PSHDLLang.PsDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclarationType(PSHDLLang.PsDeclarationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclarationType(PSHDLLang.PsDeclarationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTypeDeclaration(PSHDLLang.PsTypeDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTypeDeclaration(PSHDLLang.PsTypeDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnumDeclaration(PSHDLLang.PsEnumDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnumDeclaration(PSHDLLang.PsEnumDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnum(PSHDLLang.PsEnumContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnum(PSHDLLang.PsEnumContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableDeclaration(PSHDLLang.PsVariableDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableDeclaration(PSHDLLang.PsVariableDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclAssignment(PSHDLLang.PsDeclAssignmentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclAssignment(PSHDLLang.PsDeclAssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInit(PSHDLLang.PsArrayInitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInit(PSHDLLang.PsArrayInitContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSubParens(PSHDLLang.PsArrayInitSubParensContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSubParens(PSHDLLang.PsArrayInitSubParensContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSub(PSHDLLang.PsArrayInitSubContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSub(PSHDLLang.PsArrayInitSubContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArray(PSHDLLang.PsArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArray(PSHDLLang.PsArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirection(PSHDLLang.PsDirectionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirection(PSHDLLang.PsDirectionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotation(PSHDLLang.PsAnnotationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotation(PSHDLLang.PsAnnotationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotationType(PSHDLLang.PsAnnotationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotationType(PSHDLLang.PsAnnotationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitive(PSHDLLang.PsPrimitiveContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitive(PSHDLLang.PsPrimitiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitiveType(PSHDLLang.PsPrimitiveTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitiveType(PSHDLLang.PsPrimitiveTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsWidth(PSHDLLang.PsWidthContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsWidth(PSHDLLang.PsWidthContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDeclaration(PSHDLLang.PsInterfaceDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDeclaration(PSHDLLang.PsInterfaceDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterface(PSHDLLang.PsInterfaceContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterface(PSHDLLang.PsInterfaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInterfaceExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceExtends(PSHDLLang.PsInterfaceExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInterfaceExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceExtends(PSHDLLang.PsInterfaceExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDecl(PSHDLLang.PsInterfaceDeclContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDecl(PSHDLLang.PsInterfaceDeclContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPortDeclaration(PSHDLLang.PsPortDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPortDeclaration(PSHDLLang.PsPortDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLang#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedName(PSHDLLang.PsQualifiedNameContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLang#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedName(PSHDLLang.PsQualifiedNameContext ctx);
}