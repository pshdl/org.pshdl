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

import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.WarnType;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.AliasContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.ColumnContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.DeclarationContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.DefinitionContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.MemoryContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.ReferenceContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.RowContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.UnitContext;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.WarnTypeContext;
import org.pshdl.model.validation.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

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
	public void enterRow(RowContext ctx) {
		final String id = ctx.ID().getText();
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
		def.type = Type.valueOf(ctx.type().getText().toUpperCase());
		if (ctx.width() != null) {
			def.width = Integer.parseInt(ctx.width().getText());
		}
		for (final TerminalNode dim : ctx.INT()) {
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
			memory.references.add((Reference) def);
		}
	}

	@Override
	public void enterReference(ReferenceContext ctx) {
		final String id = ctx.ID().getText();
		final Reference ref = new Reference(id);
		for (final TerminalNode dim : ctx.INT()) {
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
		final ANTLRInputStream input = new ANTLRInputStream(string);
		final MemoryModelLexer lexer = new MemoryModelLexer(input);
		final CommonTokenStream tokens = new CommonTokenStream(lexer);
		final MemoryModelParser parser = new MemoryModelParser(tokens);
		parser.getErrorListeners().clear();
		parser.addErrorListener(new PSHDLParser.SyntaxErrorCollector(tokens, problems, lineOffset));
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
		final String string = Files.toString(new File("/Users/karstenbecker/Dropbox/PSHDL/Test/adderTest.txt"), Charsets.UTF_8);
		final Unit parseUnit = parseUnit(string, problems, 0);
		for (final Problem problem : problems) {
			System.err.println("MemoryModelAST.main()" + problem);
		}
		System.out.println("MemorModelAST.main()" + parseUnit);
	}
}
