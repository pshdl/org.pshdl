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
// Generated from MemoryModel.g4 by ANTLR 4.0
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;

import org.antlr.v4.runtime.tree.*;

public interface MemoryModelListener extends ParseTreeListener {
	void enterUnit(MemoryModelParser.UnitContext ctx);

	void exitUnit(MemoryModelParser.UnitContext ctx);

	void enterWarnType(MemoryModelParser.WarnTypeContext ctx);

	void exitWarnType(MemoryModelParser.WarnTypeContext ctx);

	void enterDeclaration(MemoryModelParser.DeclarationContext ctx);

	void exitDeclaration(MemoryModelParser.DeclarationContext ctx);

	void enterDefinition(MemoryModelParser.DefinitionContext ctx);

	void exitDefinition(MemoryModelParser.DefinitionContext ctx);

	void enterAlias(MemoryModelParser.AliasContext ctx);

	void exitAlias(MemoryModelParser.AliasContext ctx);

	void enterWidth(MemoryModelParser.WidthContext ctx);

	void exitWidth(MemoryModelParser.WidthContext ctx);

	void enterColumn(MemoryModelParser.ColumnContext ctx);

	void exitColumn(MemoryModelParser.ColumnContext ctx);

	void enterRwStatus(MemoryModelParser.RwStatusContext ctx);

	void exitRwStatus(MemoryModelParser.RwStatusContext ctx);

	void enterType(MemoryModelParser.TypeContext ctx);

	void exitType(MemoryModelParser.TypeContext ctx);

	void enterReference(MemoryModelParser.ReferenceContext ctx);

	void exitReference(MemoryModelParser.ReferenceContext ctx);

	void enterMemory(MemoryModelParser.MemoryContext ctx);

	void exitMemory(MemoryModelParser.MemoryContext ctx);

	void enterRow(MemoryModelParser.RowContext ctx);

	void exitRow(MemoryModelParser.RowContext ctx);
}
