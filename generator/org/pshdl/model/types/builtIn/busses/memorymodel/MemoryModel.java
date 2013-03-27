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
package org.pshdl.model.types.builtIn.busses.memorymodel;

import java.io.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLVariableDeclaration.*;
import org.pshdl.model.validation.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

import org.pshdl.model.types.builtIn.busses.memorymodel.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.*;

public class MemoryModel {

	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		Set<Problem> problems = Sets.newHashSet();
		Unit unit = MemoryModelAST.parseUnit(Files.toString(file, Charsets.UTF_8), problems);
		System.out.println(unit);
		List<Row> rows = buildRows(unit);
		byte[] builtHTML = MemoryModelSideFiles.builtHTML(unit, rows);
		System.out.println(new BusAccess().generateAccessC(rows));
		System.out.println(new BusAccess().generateAccessH(unit, rows));
		// // SideFile[] cFiles = MemoryModelSideFiles.getCFiles(unit, rows);
		// for (SideFile sideFile : cFiles) {
		// System.out.println(sideFile.relPath);
		// System.out.println(new String(sideFile.contents));
		// }
		FileOutputStream ps = new FileOutputStream(args[0] + "Map.html");
		ps.write(builtHTML);
		ps.close();
		HDLInterface hdi = buildHDLInterface(unit, rows);
		System.out.println(hdi);
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
				if (val == null) {
					defDimension.put(name, 1);
				} else {
					defDimension.put(name, val + 1);
				}

			}
		}
		HDLInterface hdi = new HDLInterface();
		for (Definition def : definitions.values()) {
			HDLPrimitive type = null;
			switch (def.type) {
			case BIT:
				if (def.width == -1) {
					type = HDLPrimitive.getBit();
				} else {
					type = HDLPrimitive.getBitvector().setWidth(HDLLiteral.get(def.width));
				}
				break;
			case INT:
				if (def.width == -1) {
					type = HDLPrimitive.getInteger();
				} else {
					type = HDLPrimitive.getInt().setWidth(HDLLiteral.get(def.width));
				}
				break;
			case UINT:
				if (def.width == -1) {
					type = HDLPrimitive.getNatural();
				} else {
					type = HDLPrimitive.getUint().setWidth(HDLLiteral.get(def.width));
				}
				break;
			case UNUSED:
				continue;
			}
			if (type == null)
				throw new IllegalArgumentException("Should not happen");
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
			if (dim != 1) {
				var = var.addDimensions(HDLLiteral.get(dim));
			}
			hdv = hdv.addVariables(var);
			hdi = hdi.addPorts(hdv);
		}
		return hdi;
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
		for (Row row : rows) {
			row.updateInfo();
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
			if (ref.dimensions.size() != 0) {
				for (Integer num : ref.dimensions) {
					for (int i = 0; i < num; i++) {
						addDeclarations(unit, rows, decl, parent, colIndex);
					}
				}
			} else {
				addDeclarations(unit, rows, decl, parent, colIndex);
			}
			// addDeclarations(unit, rows, decl, parent, colIndex);
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
		if (usedSize > Unit.rowWidth)
			throw new IllegalArgumentException("The row:" + row.name + " has more bits (" + usedSize + ") than a row has bits.");
		if ((usedSize != Unit.rowWidth) && (fillFound == false))
			throw new IllegalArgumentException("The row:" + row.name + " has a size of:" + usedSize + " but does not contain a fill");
		if (usedSize == Unit.rowWidth) {
			definitions.remove(unusedFill);
		}
		unusedFill.width = Unit.rowWidth - usedSize;
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
					Definition withoutDim = def.withoutDim();
					definitions.add(withoutDim);
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
