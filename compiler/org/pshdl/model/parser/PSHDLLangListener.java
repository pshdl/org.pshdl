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
// Generated from PSHDLLang.g4 by ANTLR 4.5.3
package org.pshdl.model.parser;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PSHDLLangParser}.
 */
public interface PSHDLLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsModel(PSHDLLangParser.PsModelContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsModel(PSHDLLangParser.PsModelContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsUnit(PSHDLLangParser.PsUnitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsUnit(PSHDLLangParser.PsUnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsExtends(PSHDLLangParser.PsExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsExtends(PSHDLLangParser.PsExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsImports(PSHDLLangParser.PsImportsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsImports(PSHDLLangParser.PsImportsContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedNameImport(PSHDLLangParser.PsQualifiedNameImportContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedNameImport(PSHDLLangParser.PsQualifiedNameImportContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBlock(PSHDLLangParser.PsBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBlock(PSHDLLangParser.PsBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsProcess(PSHDLLangParser.PsProcessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsProcess(PSHDLLangParser.PsProcessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInstantiation(PSHDLLangParser.PsInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInstantiation(PSHDLLangParser.PsInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceInstantiation(PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceInstantiation(PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDirectGeneration}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirectGeneration(PSHDLLangParser.PsDirectGenerationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDirectGeneration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirectGeneration(PSHDLLangParser.PsDirectGenerationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPassedArguments(PSHDLLangParser.PsPassedArgumentsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPassedArguments(PSHDLLangParser.PsPassedArgumentsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArgument(PSHDLLangParser.PsArgumentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArgument(PSHDLLangParser.PsArgumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCast(PSHDLLangParser.PsCastContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCast(PSHDLLangParser.PsCastContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitAnd} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAnd(PSHDLLangParser.PsBitAndContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitAnd} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAnd(PSHDLLangParser.PsBitAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psAdd} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAdd(PSHDLLangParser.PsAddContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psAdd} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAdd(PSHDLLangParser.PsAddContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psConcat} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsConcat(PSHDLLangParser.PsConcatContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psConcat} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsConcat(PSHDLLangParser.PsConcatContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psValueExp} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValueExp(PSHDLLangParser.PsValueExpContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psValueExp} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValueExp(PSHDLLangParser.PsValueExpContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psShift} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsShift(PSHDLLangParser.PsShiftContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psShift} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsShift(PSHDLLangParser.PsShiftContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitLogAnd} labeled
	 * alternative in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogAnd(PSHDLLangParser.PsBitLogAndContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitLogAnd} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogAnd(PSHDLLangParser.PsBitLogAndContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitOr} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitOr(PSHDLLangParser.PsBitOrContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitOr} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitOr(PSHDLLangParser.PsBitOrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psParens} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsParens(PSHDLLangParser.PsParensContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psParens} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsParens(PSHDLLangParser.PsParensContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psEqualityComp} labeled
	 * alternative in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEqualityComp(PSHDLLangParser.PsEqualityCompContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psEqualityComp} labeled
	 * alternative in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEqualityComp(PSHDLLangParser.PsEqualityCompContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitLogOr} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogOr(PSHDLLangParser.PsBitLogOrContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitLogOr} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogOr(PSHDLLangParser.PsBitLogOrContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psTernary} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTernary(PSHDLLangParser.PsTernaryContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psTernary} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTernary(PSHDLLangParser.PsTernaryContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psArrayInitExp} labeled
	 * alternative in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitExp(PSHDLLangParser.PsArrayInitExpContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psArrayInitExp} labeled
	 * alternative in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitExp(PSHDLLangParser.PsArrayInitExpContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psManip} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsManip(PSHDLLangParser.PsManipContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psManip} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsManip(PSHDLLangParser.PsManipContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psEquality} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEquality(PSHDLLangParser.PsEqualityContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psEquality} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEquality(PSHDLLangParser.PsEqualityContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psBitXor} labeled alternative
	 * in {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitXor(PSHDLLangParser.PsBitXorContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psBitXor} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitXor(PSHDLLangParser.PsBitXorContext ctx);

	/**
	 * Enter a parse tree produced by the {@code psMul} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsMul(PSHDLLangParser.PsMulContext ctx);

	/**
	 * Exit a parse tree produced by the {@code psMul} labeled alternative in
	 * {@link PSHDLLangParser#psExpression}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsMul(PSHDLLangParser.PsMulContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValue(PSHDLLangParser.PsValueContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValue(PSHDLLangParser.PsValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAccess(PSHDLLangParser.PsBitAccessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAccess(PSHDLLangParser.PsBitAccessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAccessRange(PSHDLLangParser.PsAccessRangeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAccessRange(PSHDLLangParser.PsAccessRangeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableRef(PSHDLLangParser.PsVariableRefContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableRef(PSHDLLangParser.PsVariableRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsRefPart(PSHDLLangParser.PsRefPartContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsRefPart(PSHDLLangParser.PsRefPartContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariable(PSHDLLangParser.PsVariableContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariable(PSHDLLangParser.PsVariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psVariableMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableMatch(PSHDLLangParser.PsVariableMatchContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psVariableMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableMatch(PSHDLLangParser.PsVariableMatchContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsStatement(PSHDLLangParser.PsStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsStatement(PSHDLLangParser.PsStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psGroupMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsGroupMatch(PSHDLLangParser.PsGroupMatchContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psGroupMatch}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsGroupMatch(PSHDLLangParser.PsGroupMatchContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psExport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsExport(PSHDLLangParser.PsExportContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psExport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsExport(PSHDLLangParser.PsExportContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunctionDeclaration(PSHDLLangParser.PsFunctionDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunctionDeclaration(PSHDLLangParser.PsFunctionDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInlineFunction(PSHDLLangParser.PsInlineFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInlineFunction(PSHDLLangParser.PsInlineFunctionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSubstituteFunction(PSHDLLangParser.PsSubstituteFunctionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSubstituteFunction(PSHDLLangParser.PsSubstituteFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsNativeFunction(PSHDLLangParser.PsNativeFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsNativeFunction(PSHDLLangParser.PsNativeFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncRecturnType(PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncRecturnType(PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParam(PSHDLLangParser.PsFuncParamContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParam(PSHDLLangParser.PsFuncParamContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncSpec(PSHDLLangParser.PsFuncSpecContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncSpec(PSHDLLangParser.PsFuncSpecContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamWithRW(PSHDLLangParser.PsFuncParamWithRWContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamWithRW(PSHDLLangParser.PsFuncParamWithRWContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncOptArray(PSHDLLangParser.PsFuncOptArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncOptArray(PSHDLLangParser.PsFuncOptArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamRWType(PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamRWType(PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamType(PSHDLLangParser.PsFuncParamTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamType(PSHDLLangParser.PsFuncParamTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunction(PSHDLLangParser.PsFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunction(PSHDLLangParser.PsFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncArgs(PSHDLLangParser.PsFuncArgsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncArgs(PSHDLLangParser.PsFuncArgsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAssignmentOrFunc}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOrFunc(PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAssignmentOrFunc}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOrFunc(PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOp(PSHDLLangParser.PsAssignmentOpContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOp(PSHDLLangParser.PsAssignmentOpContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psCompoundStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCompoundStatement(PSHDLLangParser.PsCompoundStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCompoundStatement}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCompoundStatement(PSHDLLangParser.PsCompoundStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsIfStatement(PSHDLLangParser.PsIfStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsIfStatement(PSHDLLangParser.PsIfStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSimpleBlock(PSHDLLangParser.PsSimpleBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSimpleBlock(PSHDLLangParser.PsSimpleBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsForStatement(PSHDLLangParser.PsForStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsForStatement(PSHDLLangParser.PsForStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSwitchStatement(PSHDLLangParser.PsSwitchStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSwitchStatement(PSHDLLangParser.PsSwitchStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCaseStatements(PSHDLLangParser.PsCaseStatementsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCaseStatements(PSHDLLangParser.PsCaseStatementsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclaration(PSHDLLangParser.PsDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclaration(PSHDLLangParser.PsDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclarationType(PSHDLLangParser.PsDeclarationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclarationType(PSHDLLangParser.PsDeclarationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTypeDeclaration(PSHDLLangParser.PsTypeDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTypeDeclaration(PSHDLLangParser.PsTypeDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnumDeclaration(PSHDLLangParser.PsEnumDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnumDeclaration(PSHDLLangParser.PsEnumDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnum(PSHDLLangParser.PsEnumContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnum(PSHDLLangParser.PsEnumContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableDeclaration(PSHDLLangParser.PsVariableDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableDeclaration(PSHDLLangParser.PsVariableDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclAssignment(PSHDLLangParser.PsDeclAssignmentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclAssignment(PSHDLLangParser.PsDeclAssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInit(PSHDLLangParser.PsArrayInitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInit(PSHDLLangParser.PsArrayInitContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSubParens(PSHDLLangParser.PsArrayInitSubParensContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSubParens(PSHDLLangParser.PsArrayInitSubParensContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSub(PSHDLLangParser.PsArrayInitSubContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSub(PSHDLLangParser.PsArrayInitSubContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArray(PSHDLLangParser.PsArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArray(PSHDLLangParser.PsArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirection(PSHDLLangParser.PsDirectionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirection(PSHDLLangParser.PsDirectionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotation(PSHDLLangParser.PsAnnotationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotation(PSHDLLangParser.PsAnnotationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotationType(PSHDLLangParser.PsAnnotationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotationType(PSHDLLangParser.PsAnnotationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitive(PSHDLLangParser.PsPrimitiveContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitive(PSHDLLangParser.PsPrimitiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitiveType(PSHDLLangParser.PsPrimitiveTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitiveType(PSHDLLangParser.PsPrimitiveTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsWidth(PSHDLLangParser.PsWidthContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsWidth(PSHDLLangParser.PsWidthContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDeclaration(PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDeclaration(PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterface(PSHDLLangParser.PsInterfaceContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterface(PSHDLLangParser.PsInterfaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterfaceExtends}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceExtends(PSHDLLangParser.PsInterfaceExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterfaceExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceExtends(PSHDLLangParser.PsInterfaceExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDecl(PSHDLLangParser.PsInterfaceDeclContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDecl(PSHDLLangParser.PsInterfaceDeclContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPortDeclaration(PSHDLLangParser.PsPortDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPortDeclaration(PSHDLLangParser.PsPortDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedName(PSHDLLangParser.PsQualifiedNameContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedName(PSHDLLangParser.PsQualifiedNameContext ctx);
}