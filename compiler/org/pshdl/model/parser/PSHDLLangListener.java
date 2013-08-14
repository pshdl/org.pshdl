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
// Generated from PSHDLLang.g4 by ANTLR 4.1
package org.pshdl.model.parser;

import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PSHDLLangParser}.
 */
public interface PSHDLLangListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableRef(@NotNull PSHDLLangParser.PsVariableRefContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psVariableRef}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableRef(@NotNull PSHDLLangParser.PsVariableRefContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsProcess(@NotNull PSHDLLangParser.PsProcessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psProcess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsProcess(@NotNull PSHDLLangParser.PsProcessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariable(@NotNull PSHDLLangParser.PsVariableContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psVariable}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariable(@NotNull PSHDLLangParser.PsVariableContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPassedArguments(@NotNull PSHDLLangParser.PsPassedArgumentsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPassedArguments}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPassedArguments(@NotNull PSHDLLangParser.PsPassedArgumentsContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSubstituteFunction(@NotNull PSHDLLangParser.PsSubstituteFunctionContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psSubstituteFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSubstituteFunction(@NotNull PSHDLLangParser.PsSubstituteFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamWithRW(@NotNull PSHDLLangParser.PsFuncParamWithRWContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamWithRW}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamWithRW(@NotNull PSHDLLangParser.PsFuncParamWithRWContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psManip}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsManip(@NotNull PSHDLLangParser.PsManipContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psManip}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsManip(@NotNull PSHDLLangParser.PsManipContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitLogAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogAnd(@NotNull PSHDLLangParser.PsBitLogAndContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitLogAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogAnd(@NotNull PSHDLLangParser.PsBitLogAndContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitLogOr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitLogOr(@NotNull PSHDLLangParser.PsBitLogOrContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitLogOr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitLogOr(@NotNull PSHDLLangParser.PsBitLogOrContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsVariableDeclaration(@NotNull PSHDLLangParser.PsVariableDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psVariableDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsVariableDeclaration(@NotNull PSHDLLangParser.PsVariableDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncOptArray(@NotNull PSHDLLangParser.PsFuncOptArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncOptArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncOptArray(@NotNull PSHDLLangParser.PsFuncOptArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncRecturnType(@NotNull PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncRecturnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncRecturnType(@NotNull PSHDLLangParser.PsFuncRecturnTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitXor}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitXor(@NotNull PSHDLLangParser.PsBitXorContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitXor}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitXor(@NotNull PSHDLLangParser.PsBitXorContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsNativeFunction(@NotNull PSHDLLangParser.PsNativeFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psNativeFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsNativeFunction(@NotNull PSHDLLangParser.PsNativeFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnum(@NotNull PSHDLLangParser.PsEnumContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEnum}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnum(@NotNull PSHDLLangParser.PsEnumContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedNameImport(@NotNull PSHDLLangParser.PsQualifiedNameImportContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psQualifiedNameImport}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedNameImport(@NotNull PSHDLLangParser.PsQualifiedNameImportContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceInstantiation(@NotNull PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceInstantiation(@NotNull PSHDLLangParser.PsInterfaceInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsForStatement(@NotNull PSHDLLangParser.PsForStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psForStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsForStatement(@NotNull PSHDLLangParser.PsForStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSwitchStatement(@NotNull PSHDLLangParser.PsSwitchStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psSwitchStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSwitchStatement(@NotNull PSHDLLangParser.PsSwitchStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParam(@NotNull PSHDLLangParser.PsFuncParamContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParam}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParam(@NotNull PSHDLLangParser.PsFuncParamContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOp(@NotNull PSHDLLangParser.PsAssignmentOpContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAssignmentOp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOp(@NotNull PSHDLLangParser.PsAssignmentOpContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEnumDeclaration(@NotNull PSHDLLangParser.PsEnumDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEnumDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEnumDeclaration(@NotNull PSHDLLangParser.PsEnumDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsWidth(@NotNull PSHDLLangParser.PsWidthContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psWidth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsWidth(@NotNull PSHDLLangParser.PsWidthContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAccessRange(@NotNull PSHDLLangParser.PsAccessRangeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAccessRange}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAccessRange(@NotNull PSHDLLangParser.PsAccessRangeContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSubParens(@NotNull PSHDLLangParser.PsArrayInitSubParensContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psArrayInitSubParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSubParens(@NotNull PSHDLLangParser.PsArrayInitSubParensContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInlineFunction(@NotNull PSHDLLangParser.PsInlineFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInlineFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInlineFunction(@NotNull PSHDLLangParser.PsInlineFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsParens(@NotNull PSHDLLangParser.PsParensContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psParens}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsParens(@NotNull PSHDLLangParser.PsParensContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAnd(@NotNull PSHDLLangParser.PsBitAndContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAnd(@NotNull PSHDLLangParser.PsBitAndContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamType(@NotNull PSHDLLangParser.PsFuncParamTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamType(@NotNull PSHDLLangParser.PsFuncParamTypeContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psCompoundStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCompoundStatement(@NotNull PSHDLLangParser.PsCompoundStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCompoundStatement}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCompoundStatement(@NotNull PSHDLLangParser.PsCompoundStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsStatement(@NotNull PSHDLLangParser.PsStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsStatement(@NotNull PSHDLLangParser.PsStatementContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValue(@NotNull PSHDLLangParser.PsValueContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psValue}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValue(@NotNull PSHDLLangParser.PsValueContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDeclaration(@NotNull PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psInterfaceDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDeclaration(@NotNull PSHDLLangParser.PsInterfaceDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEqualityComp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEqualityComp(@NotNull PSHDLLangParser.PsEqualityCompContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEqualityComp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEqualityComp(@NotNull PSHDLLangParser.PsEqualityCompContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psConcat}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsConcat(@NotNull PSHDLLangParser.PsConcatContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psConcat}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsConcat(@NotNull PSHDLLangParser.PsConcatContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPortDeclaration(@NotNull PSHDLLangParser.PsPortDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPortDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPortDeclaration(@NotNull PSHDLLangParser.PsPortDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotationType(@NotNull PSHDLLangParser.PsAnnotationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAnnotationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotationType(@NotNull PSHDLLangParser.PsAnnotationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclAssignment(@NotNull PSHDLLangParser.PsDeclAssignmentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclAssignment}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclAssignment(@NotNull PSHDLLangParser.PsDeclAssignmentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBlock(@NotNull PSHDLLangParser.PsBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBlock(@NotNull PSHDLLangParser.PsBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDirectGeneration}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirectGeneration(@NotNull PSHDLLangParser.PsDirectGenerationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDirectGeneration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirectGeneration(@NotNull PSHDLLangParser.PsDirectGenerationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psEquality}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsEquality(@NotNull PSHDLLangParser.PsEqualityContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psEquality}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsEquality(@NotNull PSHDLLangParser.PsEqualityContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAnnotation(@NotNull PSHDLLangParser.PsAnnotationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAnnotation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAnnotation(@NotNull PSHDLLangParser.PsAnnotationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCast(@NotNull PSHDLLangParser.PsCastContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCast}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCast(@NotNull PSHDLLangParser.PsCastContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsQualifiedName(@NotNull PSHDLLangParser.PsQualifiedNameContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psQualifiedName}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsQualifiedName(@NotNull PSHDLLangParser.PsQualifiedNameContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArrayInitExp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitExp(@NotNull PSHDLLangParser.PsArrayInitExpContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArrayInitExp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitExp(@NotNull PSHDLLangParser.PsArrayInitExpContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsRefPart(@NotNull PSHDLLangParser.PsRefPartContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psRefPart}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsRefPart(@NotNull PSHDLLangParser.PsRefPartContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunction(@NotNull PSHDLLangParser.PsFunctionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFunction}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunction(@NotNull PSHDLLangParser.PsFunctionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInit(@NotNull PSHDLLangParser.PsArrayInitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArrayInit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInit(@NotNull PSHDLLangParser.PsArrayInitContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link PSHDLLangParser#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFunctionDeclaration(@NotNull PSHDLLangParser.PsFunctionDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link PSHDLLangParser#psFunctionDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFunctionDeclaration(@NotNull PSHDLLangParser.PsFunctionDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclarationType(@NotNull PSHDLLangParser.PsDeclarationTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclarationType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclarationType(@NotNull PSHDLLangParser.PsDeclarationTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceDecl(@NotNull PSHDLLangParser.PsInterfaceDeclContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterfaceDecl}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceDecl(@NotNull PSHDLLangParser.PsInterfaceDeclContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterfaceExtends}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterfaceExtends(@NotNull PSHDLLangParser.PsInterfaceExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterfaceExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterfaceExtends(@NotNull PSHDLLangParser.PsInterfaceExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psValueExp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsValueExp(@NotNull PSHDLLangParser.PsValueExpContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psValueExp}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsValueExp(@NotNull PSHDLLangParser.PsValueExpContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsUnit(@NotNull PSHDLLangParser.PsUnitContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psUnit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsUnit(@NotNull PSHDLLangParser.PsUnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInstantiation(@NotNull PSHDLLangParser.PsInstantiationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInstantiation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInstantiation(@NotNull PSHDLLangParser.PsInstantiationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTypeDeclaration(@NotNull PSHDLLangParser.PsTypeDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psTypeDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTypeDeclaration(@NotNull PSHDLLangParser.PsTypeDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAssignmentOrFunc}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAssignmentOrFunc(@NotNull PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAssignmentOrFunc}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAssignmentOrFunc(@NotNull PSHDLLangParser.PsAssignmentOrFuncContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDirection(@NotNull PSHDLLangParser.PsDirectionContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDirection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDirection(@NotNull PSHDLLangParser.PsDirectionContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psMul}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsMul(@NotNull PSHDLLangParser.PsMulContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psMul}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsMul(@NotNull PSHDLLangParser.PsMulContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitiveType(@NotNull PSHDLLangParser.PsPrimitiveTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPrimitiveType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitiveType(@NotNull PSHDLLangParser.PsPrimitiveTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsModel(@NotNull PSHDLLangParser.PsModelContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psModel}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsModel(@NotNull PSHDLLangParser.PsModelContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitAccess(@NotNull PSHDLLangParser.PsBitAccessContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitAccess}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitAccess(@NotNull PSHDLLangParser.PsBitAccessContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArray(@NotNull PSHDLLangParser.PsArrayContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArray}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArray(@NotNull PSHDLLangParser.PsArrayContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncArgs(@NotNull PSHDLLangParser.PsFuncArgsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncArgs}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncArgs(@NotNull PSHDLLangParser.PsFuncArgsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsImports(@NotNull PSHDLLangParser.PsImportsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psImports}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsImports(@NotNull PSHDLLangParser.PsImportsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArrayInitSub(@NotNull PSHDLLangParser.PsArrayInitSubContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArrayInitSub}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArrayInitSub(@NotNull PSHDLLangParser.PsArrayInitSubContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncSpec(@NotNull PSHDLLangParser.PsFuncSpecContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncSpec}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncSpec(@NotNull PSHDLLangParser.PsFuncSpecContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsInterface(@NotNull PSHDLLangParser.PsInterfaceContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psInterface}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsInterface(@NotNull PSHDLLangParser.PsInterfaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsSimpleBlock(@NotNull PSHDLLangParser.PsSimpleBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psSimpleBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsSimpleBlock(@NotNull PSHDLLangParser.PsSimpleBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsPrimitive(@NotNull PSHDLLangParser.PsPrimitiveContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psPrimitive}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsPrimitive(@NotNull PSHDLLangParser.PsPrimitiveContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psBitOr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsBitOr(@NotNull PSHDLLangParser.PsBitOrContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psBitOr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsBitOr(@NotNull PSHDLLangParser.PsBitOrContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsExtends(@NotNull PSHDLLangParser.PsExtendsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psExtends}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsExtends(@NotNull PSHDLLangParser.PsExtendsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsArgument(@NotNull PSHDLLangParser.PsArgumentContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psArgument}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsArgument(@NotNull PSHDLLangParser.PsArgumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psShift}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsShift(@NotNull PSHDLLangParser.PsShiftContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psShift}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsShift(@NotNull PSHDLLangParser.PsShiftContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsCaseStatements(@NotNull PSHDLLangParser.PsCaseStatementsContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psCaseStatements}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsCaseStatements(@NotNull PSHDLLangParser.PsCaseStatementsContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psAdd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsAdd(@NotNull PSHDLLangParser.PsAddContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psAdd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsAdd(@NotNull PSHDLLangParser.PsAddContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psTernary}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsTernary(@NotNull PSHDLLangParser.PsTernaryContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psTernary}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsTernary(@NotNull PSHDLLangParser.PsTernaryContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsDeclaration(@NotNull PSHDLLangParser.PsDeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psDeclaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsDeclaration(@NotNull PSHDLLangParser.PsDeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsFuncParamRWType(@NotNull PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psFuncParamRWType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsFuncParamRWType(@NotNull PSHDLLangParser.PsFuncParamRWTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link PSHDLLangParser#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterPsIfStatement(@NotNull PSHDLLangParser.PsIfStatementContext ctx);

	/**
	 * Exit a parse tree produced by {@link PSHDLLangParser#psIfStatement}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitPsIfStatement(@NotNull PSHDLLangParser.PsIfStatementContext ctx);
}