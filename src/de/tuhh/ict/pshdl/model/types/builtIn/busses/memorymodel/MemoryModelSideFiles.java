package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import java.io.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class MemoryModelSideFiles {

	public static SideFile[] getCFiles(Unit unit, List<Row> rows) {
		StringBuilder structDefinitions = new StringBuilder();
		StringBuilder setDirectFunctions = new StringBuilder();
		StringBuilder setFunctions = new StringBuilder();
		StringBuilder updateFunctions = new StringBuilder();
		StringBuilder setDirectFunctionsDeclarations = new StringBuilder();
		StringBuilder setFunctionsDeclarations = new StringBuilder();
		StringBuilder updateFunctionsDeclarations = new StringBuilder();
		Set<String> handledRows = new HashSet<String>();
		for (Row row : rows) {
			if (handledRows.contains(row.name))
				continue;
			handledRows.add(row.name);
			structDefinitions.append("typedef struct ").append(row.name).append("{\n");
			StringBuilder setDirectFunctionsDeclaration = new StringBuilder();
			setDirectFunctionsDeclaration.append("int set").append(firstUpper(row.name)).append("Direct").append("(uint32_t *base, int index");
			setFunctionsDeclarations.append("int set").append(firstUpper(row.name)).append("(uint32_t *base, int index, ").append(row.name).append("_t *res);\n");
			StringBuilder setDirectFunction = new StringBuilder();
			Map<String, Integer> defCount = new HashMap<String, Integer>();
			for (NamedElement ne : row.definitions) {
				Definition def = (Definition) ne;
				Integer integer = defCount.get(def.name);
				if (integer == null)
					integer = 0;
				def.arrayIndex = integer;
				defCount.put(def.name, ++integer);
			}
			for (NamedElement ne : row.definitions) {
				Definition def = (Definition) ne;
				if (def.type == Type.UNUSED)
					continue;
				int size = MemoryModel.getSize(def);
				String type = "bus_" + def.type.toString().toLowerCase() + size + "_t";
				String name = def.name;
				Integer dim = defCount.get(name);
				if (def.arrayIndex == 0) {
					if (dim == 1)
						structDefinitions.append("\t").append(type).append("\t").append(name).append(";\n");
					else
						structDefinitions.append("\t").append(type).append("\t").append(name).append('[').append(dim).append(']').append(";\n");
				}
				if (def.rw != RWType.r) {
					long maskValue = (1 << size) - 1;
					long maxValue;
					if (def.type != Type.INT)
						maxValue = (1 << size) - 1;
					else
						maxValue = (1 << (size - 1)) - 1;
					String varName;
					String varNameIndex;
					if (dim == 1) {
						setDirectFunctionsDeclaration.append(", ").append(type).append(' ').append(name);
						varName = name;
						varNameIndex = name;
					} else {
						setDirectFunctionsDeclaration.append(", ").append(type).append(' ').append(name).append(def.arrayIndex);
						varName = name + def.arrayIndex;
						varNameIndex = name + "[" + def.arrayIndex + "]";
					}
					switch (def.warn) {
					case limit:
						setDirectFunction.append("\tif (").append(varName).append(">").append(maxValue).append(") {\n");
						setDirectFunction.append("\t\twarn(limit, ").append(varName).append(",\"").append(varNameIndex).append("\",\"").append(row.name).append("\",\" using ")
								.append(maxValue).append("\");\n");
						setDirectFunction.append("\t\t").append(varName).append("=").append(maxValue).append(";\n");
						setDirectFunction.append("\t}\n");
						if (def.type == Type.INT) {
							long negMax = -maxValue - 1;
							setDirectFunction.append("\telse if (").append(varName).append("<").append(negMax).append(") {\n");
							setDirectFunction.append("\t\twarn(limit, ").append(varName).append(",\"").append(varNameIndex).append("\",\"").append(row.name).append("\",\" using ")
									.append(negMax).append("\");\n");
							setDirectFunction.append("\t\t").append(varName).append("=").append(negMax).append(";\n");
							setDirectFunction.append("\t}\n");
						}
						break;
					case silentLimit:
						setDirectFunction.append("\tif (").append(varName).append(">").append(maxValue).append(") {\n");
						setDirectFunction.append("\t\t").append(varName).append("=").append(maxValue).append(";\n");
						setDirectFunction.append("\t}\n");
						if (def.type == Type.INT) {
							long negMax = -maxValue - 1;
							setDirectFunction.append("\telse if (").append(varName).append("<").append(negMax).append(") {\n");
							setDirectFunction.append("\t\t").append(varName).append("=").append(negMax).append(";\n");
							setDirectFunction.append("\t}\n");
						}
						break;
					case mask:
						setDirectFunction.append("\tif (").append(varName).append(">").append(maxValue).append(") {\n");
						setDirectFunction.append("\t\twarn(mask, ").append(varName).append(",\"").append(varNameIndex).append("\",\"").append(row.name)
								.append("\",\" masking with ").append(maskValue).append("\");\n");
						setDirectFunction.append("\t\t").append(varName).append("&=").append(maskValue).append(";\n");
						setDirectFunction.append("\t}\n");
						if (def.type == Type.INT) {
							long negMax = -maxValue - 1;
							setDirectFunction.append("\telse if (").append(varName).append("<").append(negMax).append(") {\n");
							setDirectFunction.append("\t\twarn(mask, ").append(varName).append(",\"").append(varNameIndex).append("\",\"").append(row.name)
									.append("\",\" masking with ").append(maskValue).append("\");\n");
							setDirectFunction.append("\t\t").append(varName).append("&=").append(maskValue).append(";\n");
							setDirectFunction.append("\t}\n");
						}
						break;
					case silentMask:
						setDirectFunction.append("\tif (").append(varName).append(">").append(maxValue).append(") {\n");
						setDirectFunction.append("\t\t").append(varName).append("&=").append(maskValue).append(";\n");
						setDirectFunction.append("\t}\n");
						if (def.type == Type.INT) {
							long negMax = -maxValue - 1;
							setDirectFunction.append("\telse if (").append(varName).append("<").append(negMax).append(") {\n");
							setDirectFunction.append("\t\t").append(varName).append("&=").append(maskValue).append(";\n");
							setDirectFunction.append("\t}\n");
						}
						break;
					}
				}
			}
			structDefinitions.append("} ").append(row.name).append("_t;\n\n");
			setDirectFunctionsDeclaration.append(")");
			setDirectFunctions.append(setDirectFunctionsDeclaration).append("{\n");
			setDirectFunctions.append(setDirectFunction);
			setDirectFunctions.append("}\n");
			setDirectFunctionsDeclaration.append(";\n");
			setDirectFunctionsDeclarations.append(setDirectFunctionsDeclaration);
		}
		System.out.println(structDefinitions);
		System.out.println(setDirectFunctionsDeclarations);
		System.out.println(setFunctionsDeclarations);
		System.out.println(setDirectFunctions);
		return null;
	}

	private static String firstUpper(String name) {
		return name;
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
		Map<String, Integer> defIndex = new HashMap<String, Integer>();
		Map<String, Integer> rowIndex = new HashMap<String, Integer>();
		for (Row row : rows) {
			if ((row.column != current) || (row.colIndex != colIndex)) {
				if (row.column == null) {
					current = null;
					colIndex = -1;
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s</td></tr>\n", unit.rowWidth + 2, "Without Column");
				} else {
					current = row.column;
					colIndex = row.colIndex;
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s [%d]</td></tr>\n", unit.rowWidth + 2, row.column.name, row.colIndex);
				}
			}
			ps.format("<tr>");
			ps.format("<td class='offset'>%d [0x%02x]</td>", pos * mul, pos * mul);
			for (NamedElement dec : row.definitions) {
				Definition def = (Definition) dec;
				Integer integer = getAndInc(defIndex, def.name);
				if (def.type != Type.UNUSED)
					ps.format("<td colspan='%d' class='field %s %s'>%s [%d]</td>", MemoryModel.getSize(def), def.rw + "Style", def.register ? "register" : "", def.name, integer);
				else
					ps.format("<td colspan='%d' class='field %s %s'>%s</td>", MemoryModel.getSize(def), def.rw + "Style", "", def.name);

			}
			Integer integer = getAndInc(rowIndex, row.name);
			ps.format("<td class='rowInfo'>%s [%d]</td></tr>\n", row.name, integer);
			pos++;
		}
		options.put("{TABLE}", ps.toString());
		options.put("{HDLINTERFACE}", MemoryModel.buildHDLInterface(unit, rows).setName("Bus").toString());
		ps.close();
		return Helper.processFile(MemoryModel.class, "memmodelTemplate.html", options);
	}

	private static Integer getAndInc(Map<String, Integer> defIndex, String name) {
		Integer integer = defIndex.get(name);
		if (integer == null)
			integer = 0;
		defIndex.put(name, integer + 1);
		return integer;
	}
}
