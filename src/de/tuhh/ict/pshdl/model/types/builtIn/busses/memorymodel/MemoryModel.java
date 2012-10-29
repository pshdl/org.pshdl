package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.io.*;
import java.util.*;

import org.antlr.runtime.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class MemoryModel {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		Unit unit = parseUnit(new FileInputStream(file));
		System.out.println(unit);
		List<Row> rows = buildRows(unit);
		byte[] builtHTML = builtHTML(unit, rows);
		FileOutputStream ps = new FileOutputStream(args[0] + "Map.html");
		ps.write(builtHTML);
		ps.close();
		HDLInterface hdi = buildHDLInterface(unit, rows);
		System.out.println(hdi);
	}

	public static Unit parseUnit(InputStream stream) throws FileNotFoundException, IOException, RecognitionException {
		ANTLRInputStream input = new ANTLRInputStream(stream);
		MemoryModelLexer lexer = new MemoryModelLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MemoryModelParser parser = new MemoryModelParser(tokens);
		Unit unit = parser.unit();
		return unit;
	}

	public static HDLInterface buildHDLInterface(Unit unit, List<Row> rows) {
		Map<String, Definition> definitions = new HashMap<String, Definition>();
		Map<String, Integer> defDimension = new HashMap<String, Integer>();
		for (Row row : rows) {
			for (NamedElement ne : row.definitions) {
				Definition def = (Definition) ne;
				String name = def.name;
				Definition stockDef = definitions.get(name);
				if ((stockDef != null) && !def.equals(stockDef) && (def.type != Type.UNUSED))
					throw new IllegalArgumentException("Two definitions with same name exist, but their type differs:" + def + " vs " + stockDef);
				definitions.put(name, def);
				Integer val = defDimension.get(name);
				if (val == null)
					defDimension.put(name, 1);
				else
					defDimension.put(name, val + 1);

			}
		}
		HDLInterface hdi = new HDLInterface();
		for (Definition def : definitions.values()) {
			HDLPrimitive type = null;
			switch (def.type) {
			case BIT:
				if (def.width == -1)
					type = HDLPrimitive.getBit();
				else
					type = HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(def.width));
				break;
			case INT:
				if (def.width == -1)
					type = HDLPrimitive.getInteger();
				else
					type = HDLPrimitive.getInt().setWidth(HDLLiteral.get(def.width));
				break;
			case UINT:
				if (def.width == -1)
					type = HDLPrimitive.getNatural();
				else
					type = HDLPrimitive.getUint().setWidth(HDLLiteral.get(def.width));
				break;
			case UNUSED:
				continue;
			}
			HDLVariableDeclaration hdv = new HDLVariableDeclaration().setType(type);
			hdv = hdv.setRegister(def.register ? HDLRegisterConfig.defaultConfig() : null);
			switch (def.rw) {
			case r:
				hdv = hdv.setDirection(HDLDirection.IN);
				break;
			case rw:
				hdv = hdv.setDirection(HDLDirection.INOUT);
				break;
			case w:
				hdv = hdv.setDirection(HDLDirection.OUT);
				break;
			}
			HDLVariable var = new HDLVariable().setName(def.name);
			Integer dim = defDimension.get(def.name);
			if (dim != 1)
				var = var.addDimensions(HDLLiteral.get(dim));
			hdv = hdv.addVariables(var);
			hdi = hdi.addPorts(hdv);
		}
		return hdi;
	}

	public static byte[] builtHTML(Unit unit, List<Row> rows) throws IOException {
		Map<String, String> options = new HashMap<String, String>();
		options.put("{TITLE}", "Register Overview");
		options.put("{DATE}", new Date().toString());
		Formatter ps = new Formatter();
		ps.format("<tr><td>Offset</td>");
		for (int i = 0; i < unit.rowWidth; i++) {
			ps.format("<td>%d</td>", unit.rowWidth - i - 1);
		}
		ps.format("<td>Row</td></tr>");
		options.put("{HEADER}", ps.toString());
		ps.close();
		ps = new Formatter();
		int mul = unit.rowWidth / 8;
		int pos = 0;
		Column current = null;
		int colIndex = -1;
		for (Row row : rows) {
			if ((row.column != current) || (row.colIndex != colIndex)) {
				if (row.column == null) {
					current = null;
					colIndex = -1;
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s</td></tr>\n", unit.rowWidth + 2, "Without Column");
				} else {
					current = row.column;
					colIndex = row.colIndex;
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s[%d]</td></tr>\n", unit.rowWidth + 2, row.column.name, row.colIndex);
				}
			}
			ps.format("<tr>");
			ps.format("<td class='offset'>%d [0x%02x]</td>", pos * mul, pos * mul);
			for (NamedElement dec : row.definitions) {
				Definition def = (Definition) dec;
				ps.format("<td colspan='%d' class='field %s %s'>%s</td>", getSize(def), def.rw + "Style", def.register ? "register" : "", def.name);
			}
			ps.format("<td>%s</td></tr>\n", row.name);
			pos++;
		}
		options.put("{TABLE}", ps.toString());
		ps.close();
		return Helper.processFile(MemoryModel.class, "memmodelTemplate.html", options);
	}

	public static List<Row> buildRows(Unit unit) {
		List<Row> rows = new LinkedList<Row>();
		for (Reference ref : unit.memory.references) {
			NamedElement declaration = unit.resolve(ref);
			if (ref.dimensions.size() != 0) {
				for (Integer num : ref.dimensions) {
					for (int i = 0; i < num; i++) {
						addDeclarations(unit, rows, declaration, null, i);
					}
				}
			} else {
				addDeclarations(unit, rows, declaration, null, 0);
			}
		}
		return rows;
	}

	private static void addDeclarations(Unit unit, List<Row> rows, NamedElement declaration, Column parent, int colIndex) {
		if (declaration instanceof Column) {
			Column col = (Column) declaration;
			for (NamedElement row : col.rows) {
				addDeclarations(unit, rows, row, col, colIndex);
			}
			return;
		}
		if (declaration instanceof Row) {
			Row row = (Row) declaration;
			Row normalize = normalize(unit, row);
			normalize.column = parent;
			normalize.colIndex = colIndex;
			rows.add(normalize);
			return;
		}
		if (declaration instanceof Reference) {
			Reference ref = (Reference) declaration;
			NamedElement decl = unit.resolve(ref);
			addDeclarations(unit, rows, decl, parent, colIndex);
			return;
		}
		throw new IllegalArgumentException("Reference not a row, column or reference:" + declaration);
	}

	private static Row normalize(Unit unit, Row row) {
		int usedSize = 0;
		boolean fillFound = false;
		Row res = new Row(row.name);
		List<Definition> definitions = new LinkedList<Definition>();
		Definition unusedFill = new Definition();
		unusedFill.name = "unused";
		for (NamedElement decl : row.definitions) {
			if (decl instanceof Reference) {
				Reference ref = (Reference) decl;
				if ("fill".equals(ref.name)) {
					if (fillFound)
						throw new IllegalArgumentException("Can not have more than one fill");
					fillFound = true;
					definitions.add(unusedFill);
					continue;
				}
				decl = unit.resolve(ref);
			}
			if (decl instanceof Alias) {
				Alias alias = (Alias) decl;
				usedSize += addDeclarations(unit, definitions, alias.definitions);
			}
			if (decl instanceof Definition) {
				Definition def = (Definition) decl;
				usedSize += handleDefinition(definitions, def);
			}
		}
		if (usedSize > unit.rowWidth)
			throw new IllegalArgumentException("The row:" + row.name + " has more bits (" + usedSize + ") than a row has bits.");
		if ((usedSize != unit.rowWidth) && (fillFound == false))
			throw new IllegalArgumentException("The row:" + row.name + " has a size of:" + usedSize + " but does not contain a fill");
		if (usedSize == unit.rowWidth)
			definitions.remove(unusedFill);
		unusedFill.width = unit.rowWidth - usedSize;
		for (Definition definition : definitions) {
			res.definitions.add(definition);
		}
		return res;
	}

	private static int addDeclarations(Unit unit, List<Definition> definitions, Collection<NamedElement> values) {
		int usedSize = 0;
		for (NamedElement declaration : values) {
			if (declaration instanceof Reference) {
				Reference ref = (Reference) declaration;
				if ("fill".equals(ref.name))
					throw new IllegalArgumentException("Fill not allowed in reference");
				declaration = unit.resolve(ref);
			}
			if (declaration instanceof Alias) {
				Alias alias = (Alias) declaration;
				usedSize += addDeclarations(unit, definitions, alias.definitions);
			}
			if (declaration instanceof Definition) {
				Definition def = (Definition) declaration;
				usedSize += handleDefinition(definitions, def);
			}
		}
		return usedSize;
	}

	private static int handleDefinition(List<Definition> definitions, Definition def) {
		int usedSize = 0;
		if (def.dimensions.size() != 0) {
			for (Integer dim : def.dimensions) {
				for (int i = 0; i < dim; i++) {
					usedSize += getSize(def);
					definitions.add(def.withoutDim());
				}
			}
		} else {
			usedSize += getSize(def);
			definitions.add(def);
		}
		return usedSize;
	}

	public static int getSize(Definition def) {
		if (def.width == -1) {
			switch (def.type) {
			case BIT:
				return 1;
			case INT:
			case UINT:
				return 32;
			default:
			}
		}
		return def.width;
	}

}