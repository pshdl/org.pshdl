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
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelParser.*;
import org.pshdl.model.validation.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

public class MemoryModelAST extends MemoryModelBaseListener {

	private Unit unit = new Unit();
	private NamedElement decl;
	private NamedElement obj;

	@Override
	public void enterAlias(AliasContext ctx) {
		String id = ctx.ID().getText();
		decl = new Alias(id);
		obj = decl;
	}

	@Override
	public void enterRow(RowContext ctx) {
		String id = ctx.ID().getText();
		decl = new Row(id);
		obj = decl;
	}

	@Override
	public void enterColumn(ColumnContext ctx) {
		String id = ctx.ID().getText();
		decl = new Column(id);
		obj = decl;
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
		obj = ref;
		addNamedElement(ref);
	}

	@Override
	public void enterMemory(MemoryContext ctx) {
		Memory mem = new Memory();
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

	public static Unit parseUnit(String string, Set<Problem> problems) throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(string);
		MemoryModelLexer lexer = new MemoryModelLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MemoryModelParser parser = new MemoryModelParser(tokens);
		parser.getErrorListeners().clear();
		parser.addErrorListener(new PSHDLParser.SyntaxErrorCollector(problems));
		UnitContext unit = parser.unit();
		if (problems.isEmpty()) {
			MemoryModelAST modelAST = new MemoryModelAST();
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(modelAST, unit);
			return modelAST.unit;
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException, RecognitionException, IOException {
		Set<Problem> problems = Sets.newHashSet();
		String string = Files.toString(new File("/Users/karstenbecker/Dropbox/PSHDL/Test/adderTest.txt"), Charsets.UTF_8);
		Unit parseUnit = parseUnit(string, problems);
		for (Problem problem : problems) {
			System.err.println("MemoryModelAST.main()" + problem);
		}
		System.out.println("MemorModelAST.main()" + parseUnit);
	}
}
