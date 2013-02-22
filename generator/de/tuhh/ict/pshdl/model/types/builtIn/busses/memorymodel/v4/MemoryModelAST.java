package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4;

import java.io.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.*;

public class MemoryModelAST extends MemoryModelBaseListener {

	private Unit unit = new Unit();
	private NamedElement decl;

	@Override
	public void enterAlias(AliasContext ctx) {
		String id = ctx.ID().getText();
		decl = new Alias(id);
	}

	@Override
	public void enterRow(RowContext ctx) {
		String id = ctx.ID().getText();
		decl = new Row(id);
	}

	@Override
	public void enterColumn(ColumnContext ctx) {
		String id = ctx.ID().getText();
		decl = new Column(id);
	}

	@Override
	public void enterDefinition(DefinitionContext ctx) {
		Definition def = new Definition();
		def.name = ctx.ID().getText();
		def.rw = RWType.valueOf(ctx.rwStatus().getText());
		def.register = ctx.hasRegister != null;
		def.type = Type.valueOf(ctx.type().getText().toUpperCase());
		if (ctx.width() != null) {
			def.width = Integer.parseInt(ctx.width().getText());
		}
		for (TerminalNode dim : ctx.INT()) {
			def.dimensions.add(Integer.parseInt(dim.getText()));
		}
		WarnTypeContext warnType = ctx.warnType();
		if (warnType != null) {
			boolean isSilent = warnType.silent != null;
			String typeString = warnType.typeString.getText();
			if ("mask".equals(typeString))
				def.warn = isSilent ? WarnType.silentMask : WarnType.mask;
			if ("error".equals(typeString))
				def.warn = isSilent ? WarnType.silentError : WarnType.error;
			if ("limit".equals(typeString))
				def.warn = isSilent ? WarnType.silentLimit : WarnType.limit;
		}
		addNamedElement(def);
	}

	private void addNamedElement(NamedElement def) {
		if (decl instanceof Alias) {
			Alias al = (Alias) decl;
			al.definitions.add(def);
		}
		if (decl instanceof Column) {
			Column col = (Column) decl;
			col.rows.add(def);
		}
		if (decl instanceof Row) {
			Row row = (Row) decl;
			row.definitions.add(def);
		}
		if (decl instanceof Memory) {
			Memory memory = (Memory) decl;
			memory.references.add((Reference) def);
		}
	}

	@Override
	public void enterReference(ReferenceContext ctx) {
		String id = ctx.ID().getText();
		Reference ref = new Reference(id);
		for (TerminalNode dim : ctx.INT()) {
			ref.dimensions.add(Integer.parseInt(dim.getText()));
		}
		addNamedElement(ref);
	}

	@Override
	public void enterMemory(MemoryContext ctx) {
		Memory mem = new Memory();
		unit.memory = mem;
		decl = mem;
	}

	@Override
	public void exitDeclaration(DeclarationContext ctx) {
		if (decl != null)
			unit.declarations.put(decl.getName(), decl);
	}

	public static Unit parseUnit(InputStream stream) throws FileNotFoundException, IOException, RecognitionException {
		ANTLRInputStream input = new ANTLRInputStream(stream);
		MemoryModelLexer lexer = new MemoryModelLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MemoryModelParser parser = new MemoryModelParser(tokens);
		UnitContext unit = parser.unit();
		MemoryModelAST modelAST = new MemoryModelAST();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(modelAST, unit);
		return modelAST.unit;
	}

	public static void main(String[] args) throws FileNotFoundException, RecognitionException, IOException {
		FileInputStream stream = new FileInputStream("/Users/karstenbecker/Dropbox/PSHDL/Test/adderTest.txt");
		Unit parseUnit = parseUnit(stream);
		stream.close();
		System.out.println("MemorModelAST.main()" + parseUnit);
	}
}
