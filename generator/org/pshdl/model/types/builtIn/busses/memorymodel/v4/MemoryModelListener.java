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
// Generated from MemoryModel.g4 by ANTLR 4.2.2
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.misc.NotNull;
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
	void enterUnit(@NotNull MemoryModelParser.UnitContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#unit}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitUnit(@NotNull MemoryModelParser.UnitContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#warnType}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterWarnType(@NotNull MemoryModelParser.WarnTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#warnType}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitWarnType(@NotNull MemoryModelParser.WarnTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#declaration}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterDeclaration(@NotNull MemoryModelParser.DeclarationContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#declaration}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitDeclaration(@NotNull MemoryModelParser.DeclarationContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#definition}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterDefinition(@NotNull MemoryModelParser.DefinitionContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#definition}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitDefinition(@NotNull MemoryModelParser.DefinitionContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#alias}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterAlias(@NotNull MemoryModelParser.AliasContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#alias}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitAlias(@NotNull MemoryModelParser.AliasContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#width}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterWidth(@NotNull MemoryModelParser.WidthContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#width}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitWidth(@NotNull MemoryModelParser.WidthContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#column}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterColumn(@NotNull MemoryModelParser.ColumnContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#column}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitColumn(@NotNull MemoryModelParser.ColumnContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#rwStatus}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterRwStatus(@NotNull MemoryModelParser.RwStatusContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#rwStatus}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitRwStatus(@NotNull MemoryModelParser.RwStatusContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#type}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterType(@NotNull MemoryModelParser.TypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#type}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitType(@NotNull MemoryModelParser.TypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#reference}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterReference(@NotNull MemoryModelParser.ReferenceContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#reference}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitReference(@NotNull MemoryModelParser.ReferenceContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#memory}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterMemory(@NotNull MemoryModelParser.MemoryContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#memory}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitMemory(@NotNull MemoryModelParser.MemoryContext ctx);

	/**
	 * Enter a parse tree produced by {@link MemoryModelParser#row}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void enterRow(@NotNull MemoryModelParser.RowContext ctx);

	/**
	 * Exit a parse tree produced by {@link MemoryModelParser#row}.
	 *
	 * @param ctx
	 *            the parse tree
	 */
	void exitRow(@NotNull MemoryModelParser.RowContext ctx);
}