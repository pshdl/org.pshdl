// Generated from MemoryModel.g4 by ANTLR 4.0
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;
import org.antlr.v4.runtime.tree.*;

public class MemoryModelBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements MemoryModelVisitor<T> {
	@Override public T visitUnit(MemoryModelParser.UnitContext ctx) { return visitChildren(ctx); }

	@Override public T visitWarnType(MemoryModelParser.WarnTypeContext ctx) { return visitChildren(ctx); }

	@Override public T visitDeclaration(MemoryModelParser.DeclarationContext ctx) { return visitChildren(ctx); }

	@Override public T visitDefinition(MemoryModelParser.DefinitionContext ctx) { return visitChildren(ctx); }

	@Override public T visitAlias(MemoryModelParser.AliasContext ctx) { return visitChildren(ctx); }

	@Override public T visitWidth(MemoryModelParser.WidthContext ctx) { return visitChildren(ctx); }

	@Override public T visitColumn(MemoryModelParser.ColumnContext ctx) { return visitChildren(ctx); }

	@Override public T visitRwStatus(MemoryModelParser.RwStatusContext ctx) { return visitChildren(ctx); }

	@Override public T visitType(MemoryModelParser.TypeContext ctx) { return visitChildren(ctx); }

	@Override public T visitReference(MemoryModelParser.ReferenceContext ctx) { return visitChildren(ctx); }

	@Override public T visitMemory(MemoryModelParser.MemoryContext ctx) { return visitChildren(ctx); }

	@Override public T visitRow(MemoryModelParser.RowContext ctx) { return visitChildren(ctx); }
}