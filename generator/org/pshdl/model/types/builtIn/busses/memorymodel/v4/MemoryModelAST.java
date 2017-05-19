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
package org.pshdl.model.types.builtIn.busses.memorymodel.v4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.pshdl.model.*;
import org.pshdl.model.parser.PSHDLParser;
import org.pshdl.model.types.builtIn.busses.memorymodel.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Constant.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.WarnType;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.*;
import org.pshdl.model.validation.Problem;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class MemoryModelAST extends MemoryModelBaseListener {

	private final Unit unit = new Unit();
	private NamedElement decl;
	private NamedElement obj;

	@Override
	public void enterAlias(AliasContext ctx) {
		final String id = ctx.ID().getText();
		decl = new Alias(id);
		obj = decl;
	}

	@Override
	public void enterFilling(FillingContext ctx) {
		final Definition definition = new Definition();
		if (ctx.width() != null) {
			definition.width = Integer.parseInt(ctx.width().getText());
		}
		obj = definition;
		addNamedElement(definition);
	}

	@Override
	public void enterRow(RowContext ctx) {
		final String id = ctx.rowID().getText();
		decl = new Row(id);
		obj = decl;
	}

	@Override
	public void enterColumn(ColumnContext ctx) {
		final String id = ctx.ID().getText();
		decl = new Column(id);
		obj = decl;
	}

	@Override
	public void enterDefinition(DefinitionContext ctx) {
		final Definition def = new Definition();
		def.name = ctx.ID().getText();
		def.rw = RWType.valueOf(ctx.rwStatus().getText());
		def.register = ctx.hasRegister != null;
		def.modFlag = ctx.modFlag != null;
		def.type = Type.valueOf(ctx.type().getText().toUpperCase());
		if (ctx.width() != null) {
			def.width = Integer.parseInt(ctx.width().getText());
		}
		for (final TerminalNode dim : ctx.NUMBER()) {
			def.dimensions.add(Integer.parseInt(dim.getText()));
		}
		final WarnTypeContext warnType = ctx.warnType();
		if (warnType != null) {
			final boolean isSilent = warnType.silent != null;
			final String typeString = warnType.typeString.getText();
			if ("mask".equals(typeString)) {
				def.warn = isSilent ? WarnType.silentMask : WarnType.mask;
			}
			if ("error".equals(typeString)) {
				def.warn = isSilent ? WarnType.silentError : WarnType.error;
			}
			if ("limit".equals(typeString)) {
				def.warn = isSilent ? WarnType.silentLimit : WarnType.limit;
			}
		}
		obj = def;
		addNamedElement(def);
	}

	@Override
	public void enterConstant(ConstantContext ctx) {
		String value = ctx.value.getText();
		String name = ctx.ID() != null ? ctx.ID().getText() : null;
		Constant c;
		int width = 32;
		if (ctx.width() != null) {
			String swidth = ctx.width().getText();
			width = HDLLiteral.parseString(swidth).intValue();
		}
		switch (value) {
		case "$date":
			c = new Constant("$date", ConstantType.date);
			break;
		case "$time":
			c = new Constant("$time", ConstantType.time);
			break;
		case "$checkSum":
			c = new Constant("$checkSum", ConstantType.checksum);
			break;
		default:
			int intValue = HDLLiteral.parseString(value).intValue();
			c = new Constant(name, intValue, width);
		}
		obj = c;
		addNamedElement(c);
	}

	private void addNamedElement(NamedElement def) {
		if (decl instanceof Alias) {
			final Alias al = (Alias) decl;
			al.definitions.add(def);
		}
		if (decl instanceof Column) {
			final Column col = (Column) decl;
			col.rows.add(def);
		}
		if (decl instanceof Row) {
			final Row row = (Row) decl;
			row.definitions.add(def);
		}
		if (decl instanceof Memory) {
			final Memory memory = (Memory) decl;
			if (def instanceof Reference) {
				Reference ref = (Reference) def;
				memory.addReference(ref);
			} else {
				memory.addConstant((Constant) def);
			}
		}
	}

	@Override
	public void enterReference(ReferenceContext ctx) {
		final String id = ctx.ID().getText();
		final Reference ref = new Reference(id);
		for (final TerminalNode dim : ctx.NUMBER()) {
			ref.dimensions.add(Integer.parseInt(dim.getText()));
		}
		obj = ref;
		addNamedElement(ref);
	}

	@Override
	public void enterMemory(MemoryContext ctx) {
		final Memory mem = new Memory();
		unit.memory = mem;
		obj = mem;
		decl = mem;
	}

	@Override
	public void exitDeclaration(DeclarationContext ctx) {
		if (decl != null) {
			unit.declarations.put(decl.getName(), decl);
		}
	}

	@Override
	public void exitEveryRule(ParserRuleContext ctx) {
		obj.setLocation(ctx.start);
	}

	public static Unit parseUnit(String string, Set<Problem> problems, int lineOffset) throws IOException {
		final CharStream input = CharStreams.fromString(string);
		final MemoryModelLexer lexer = new MemoryModelLexer(input);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final MemoryModelParser parser = new MemoryModelParser(tokens);
		final PSHDLParser.SyntaxErrorCollector listener = new PSHDLParser.SyntaxErrorCollector(tokens, problems, lineOffset);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);
		final UnitContext unit = parser.unit();
		if (problems.isEmpty()) {
			final MemoryModelAST modelAST = new MemoryModelAST();
			final ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(modelAST, unit);
			return modelAST.unit;
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException, RecognitionException, IOException {
		final Set<Problem> problems = Sets.newHashSet();
		final String string = Files.toString(new File(args[0]), Charsets.UTF_8);
		final Unit parseUnit = parseUnit(string, problems, 0);
		for (final Problem problem : problems) {
			System.err.println("MemoryModelAST.main()" + problem);
		}
		System.out.println("MemorModelAST.main()" + parseUnit);
	}
}
