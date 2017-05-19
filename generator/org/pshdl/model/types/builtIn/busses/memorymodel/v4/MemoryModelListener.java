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
// Generated from MemoryModel.g4 by ANTLR 4.7
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MemoryModelParser}.
 */
public interface MemoryModelListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#unit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterUnit(MemoryModelParser.UnitContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#unit}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitUnit(MemoryModelParser.UnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#declaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterDeclaration(MemoryModelParser.DeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#declaration}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitDeclaration(MemoryModelParser.DeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#row}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRow(MemoryModelParser.RowContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#row}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRow(MemoryModelParser.RowContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#rowID}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRowID(MemoryModelParser.RowIDContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#rowID}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRowID(MemoryModelParser.RowIDContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#constant}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterConstant(MemoryModelParser.ConstantContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#constant}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitConstant(MemoryModelParser.ConstantContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#filling}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterFilling(MemoryModelParser.FillingContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#filling}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitFilling(MemoryModelParser.FillingContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#column}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterColumn(MemoryModelParser.ColumnContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#column}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitColumn(MemoryModelParser.ColumnContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#alias}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterAlias(MemoryModelParser.AliasContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#alias}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitAlias(MemoryModelParser.AliasContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#memory}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterMemory(MemoryModelParser.MemoryContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#memory}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitMemory(MemoryModelParser.MemoryContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#definition}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterDefinition(MemoryModelParser.DefinitionContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#definition}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitDefinition(MemoryModelParser.DefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#warnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterWarnType(MemoryModelParser.WarnTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#warnType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitWarnType(MemoryModelParser.WarnTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#rwStatus}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRwStatus(MemoryModelParser.RwStatusContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#rwStatus}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRwStatus(MemoryModelParser.RwStatusContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#width}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterWidth(MemoryModelParser.WidthContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#width}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitWidth(MemoryModelParser.WidthContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#type}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterType(MemoryModelParser.TypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#type}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitType(MemoryModelParser.TypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#reference}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterReference(MemoryModelParser.ReferenceContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#reference}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitReference(MemoryModelParser.ReferenceContext ctx);
}