// Generated from MemoryModel.g4 by ANTLR 4.0
package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

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