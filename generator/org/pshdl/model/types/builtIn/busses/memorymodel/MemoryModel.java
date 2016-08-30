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

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.types.builtIn.busses.memorymodel.v4.MemoryModelAST;
import org.pshdl.model.validation.Problem;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class MemoryModel {

	public static void main(String[] args) throws Exception {
		final File file = new File(args[0]);
		final Set<Problem> problems = Sets.newHashSet();
		final Unit unit = MemoryModelAST.parseUnit(Files.toString(file, Charsets.UTF_8), problems, 0);
		System.out.println(unit);
		final List<Row> rows = buildRows(unit);
		final byte[] builtHTML = MemoryModelSideFiles.builtHTML(unit, rows, true);
		if (builtHTML == null)
			throw new IllegalArgumentException("buildHTML returned null");
		System.out.println(new BusAccess().generateAccessC(rows, "test", true));
		System.out.println(new BusAccess().generateAccessH(unit, "test", rows, true));
		// // SideFile[] cFiles = MemoryModelSideFiles.getCFiles(unit, rows);
		// for (SideFile sideFile : cFiles) {
		// System.out.println(sideFile.relPath);
		// System.out.println(new String(sideFile.contents));
		// }
		Files.write(builtHTML, new File(args[0] + "Map.html"));
		final HDLInterface hdi = buildHDLInterface(unit, rows);
		System.out.println(hdi);
	}

	public static HDLInterface buildHDLInterface(Unit unit, List<Row> rows) {
		final Map<String, Definition> definitions = Maps.newLinkedHashMap();
		final Map<String, Integer> defDimension = Maps.newLinkedHashMap();
		for (final Row row : rows) {
			for (final NamedElement ne : row.definitions) {
				final Definition def = (Definition) ne;
				final String name = def.getName(row);
				final Definition stockDef = definitions.get(name);
				if ((stockDef != null) && !def.equals(stockDef) && (def.type != Type.UNUSED))
					throw new IllegalArgumentException("Two definitions with same name exist, but their type differs:" + def + " vs " + stockDef);
				definitions.put(name, def);
				final Integer val = defDimension.get(name);
				if (val == null) {
					defDimension.put(name, 1);
				} else {
					defDimension.put(name, val + 1);
				}

			}
		}
		HDLInterface hdi = new HDLInterface();
		for (final Entry<String, Definition> entry : definitions.entrySet()) {
			final Definition def = entry.getValue();
			final String name = entry.getKey();
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
			HDLVariable var = new HDLVariable().setName(name);
			final Integer dim = defDimension.get(name);
			if (dim != 1) {
				var = var.addDimensions(HDLLiteral.get(dim));
			}
			hdv = hdv.addVariables(var);
			hdi = hdi.addPorts(hdv);
		}
		return hdi;
	}

	public static List<Row> buildRows(Unit unit) {
		final List<Row> rows = new LinkedList<Row>();
		for (final Reference ref : unit.memory.references) {
			final Optional<NamedElement> optRes = unit.resolve(ref);
			if (!optRes.isPresent()) {
				continue;
			}
			final NamedElement declaration = optRes.get();
			if (ref.dimensions.size() != 0) {
				for (final Integer num : ref.dimensions) {
					for (int i = 0; i < num; i++) {
						addDeclarations(unit, rows, declaration, null, i);
					}
				}
			} else {
				addDeclarations(unit, rows, declaration, null, 0);
			}
		}
		for (final Row row : rows) {
			row.updateInfo();
		}
		return rows;
	}

	private static void addDeclarations(Unit unit, List<Row> rows, NamedElement declaration, Column parent, int colIndex) {
		if (declaration instanceof Column) {
			final Column col = (Column) declaration;
			for (final NamedElement row : col.rows) {
				addDeclarations(unit, rows, row, col, colIndex);
			}
			return;
		}
		if (declaration instanceof Row) {
			final Row row = (Row) declaration;
			final Row normalize = normalize(unit, row);
			normalize.column = parent;
			normalize.colIndex = colIndex;
			rows.add(normalize);
			return;
		}
		if (declaration instanceof Reference) {
			final Reference ref = (Reference) declaration;
			final Optional<NamedElement> optRes = unit.resolve(ref);
			if (!optRes.isPresent())
				throw new IllegalArgumentException("Reference not a row, column or reference:" + declaration);
			final NamedElement decl = optRes.get();
			if (ref.dimensions.size() != 0) {
				for (final Integer num : ref.dimensions) {
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
		final Row res = new Row(row.name);
		final List<Definition> definitions = new LinkedList<Definition>();
		final Definition unusedFill = new Definition();
		unusedFill.name = "unused";
		for (NamedElement decl : row.definitions) {
			if (decl instanceof Reference) {
				final Reference ref = (Reference) decl;
				final Optional<NamedElement> optRes = unit.resolve(ref);
				if (!optRes.isPresent()) {
					continue;
				}
				decl = optRes.get();
			}
			if (decl instanceof Alias) {
				final Alias alias = (Alias) decl;
				usedSize += addDeclarations(unit, definitions, alias.definitions);
			}
			if (decl instanceof Definition) {
				final Definition def = (Definition) decl;
				if ((def.type == Type.UNUSED) && (def.width < 0)) {
					if (fillFound)
						throw new IllegalArgumentException("Can not have more than one fill");
					fillFound = true;
					definitions.add(unusedFill);
					continue;
				}
				usedSize += handleDefinition(definitions, def);
			}
		}
		if (usedSize > Unit.rowWidth)
			throw new IllegalArgumentException("The row:" + row.getName() + " has more bits (" + usedSize + ") than a row has bits.");
		if ((usedSize != Unit.rowWidth) && (fillFound == false))
			throw new IllegalArgumentException("The row:" + row.getName() + " has a size of:" + usedSize + " but does not contain a fill");
		if (usedSize == Unit.rowWidth) {
			definitions.remove(unusedFill);
		}
		unusedFill.width = Unit.rowWidth - usedSize;
		for (final Definition definition : definitions) {
			res.definitions.add(definition);
		}
		return res;
	}

	private static int addDeclarations(Unit unit, List<Definition> definitions, Collection<NamedElement> values) {
		int usedSize = 0;
		for (NamedElement declaration : values) {
			if (declaration instanceof Reference) {
				final Reference ref = (Reference) declaration;
				if ("fill".equals(ref.name))
					throw new IllegalArgumentException("Fill not allowed in reference");
				final Optional<NamedElement> optRes = unit.resolve(ref);
				if (!optRes.isPresent()) {
					continue;
				}
				declaration = optRes.get();
			}
			if (declaration instanceof Alias) {
				final Alias alias = (Alias) declaration;
				usedSize += addDeclarations(unit, definitions, alias.definitions);
			}
			if (declaration instanceof Definition) {
				final Definition def = (Definition) declaration;
				usedSize += handleDefinition(definitions, def);
			}
		}
		return usedSize;
	}

	private static int handleDefinition(List<Definition> definitions, Definition def) {
		int usedSize = 0;
		if (def.dimensions.size() != 0) {
			for (final Integer dim : def.dimensions) {
				for (int i = 0; i < dim; i++) {
					usedSize += getSize(def);
					final Definition withoutDim = def.withoutDim();
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
