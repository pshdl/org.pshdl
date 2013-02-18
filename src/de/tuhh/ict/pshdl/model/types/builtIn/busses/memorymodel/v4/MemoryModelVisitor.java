// Generated from MemoryModel.g4 by ANTLR 4.0
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

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