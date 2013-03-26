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

public interface MemoryModelVisitor<T> extends ParseTreeVisitor<T> {
	T visitUnit(MemoryModelParser.UnitContext ctx);

	T visitWarnType(MemoryModelParser.WarnTypeContext ctx);

	T visitDeclaration(MemoryModelParser.DeclarationContext ctx);

	T visitDefinition(MemoryModelParser.DefinitionContext ctx);

	T visitAlias(MemoryModelParser.AliasContext ctx);

	T visitWidth(MemoryModelParser.WidthContext ctx);

	T visitColumn(MemoryModelParser.ColumnContext ctx);

	T visitRwStatus(MemoryModelParser.RwStatusContext ctx);

	T visitType(MemoryModelParser.TypeContext ctx);

	T visitReference(MemoryModelParser.ReferenceContext ctx);

	T visitMemory(MemoryModelParser.MemoryContext ctx);

	T visitRow(MemoryModelParser.RowContext ctx);
}
