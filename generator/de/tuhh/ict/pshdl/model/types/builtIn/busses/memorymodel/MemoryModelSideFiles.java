package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

import java.io.*;
import java.util.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class MemoryModelSideFiles {

	public static List<SideFile> getSideFiles(HDLUnit unit, Unit memUnit, List<Row> rows, String version) {
		List<SideFile> res = new LinkedList<SideFile>();
		String unitName = fullNameOf(unit).toString('_').toLowerCase();
		String ipcorename = unitName + BusGenSideFiles.WRAPPER_APPENDIX;
		String dirName = ipcorename + "_" + version;
		String rootDir = "drivers/";
		BusAccess ba = new BusAccess();
		res.add(new SideFile(rootDir + dirName + "/" + unitName + "Map.xhtml", builtHTML(memUnit, rows)));
		res.add(new SideFile(rootDir + dirName + "/BusAccess.c", ba.generateAccessC(rows).toString().getBytes(Charsets.UTF_8)));
		res.add(new SideFile(rootDir + dirName + "/BusAccess.h", ba.generateAccessH(memUnit, rows).toString().getBytes(Charsets.UTF_8)));
		res.add(new SideFile(rootDir + dirName + "/BusPrint.c", ba.generatePrintC(memUnit, rows).toString().getBytes(Charsets.UTF_8)));
		res.add(new SideFile(rootDir + dirName + "/BusPrint.h", ba.generatePrintH(memUnit, rows).toString().getBytes(Charsets.UTF_8)));
		res.add(new SideFile(rootDir + dirName + "/BusStdDefinitions.h", ba.generateStdDef().toString().getBytes(Charsets.UTF_8)));
		return res;
	}

	public static byte[] builtHTML(Unit unit, List<Row> rows) {
		Map<String, String> options = new HashMap<String, String>();
		options.put("{TITLE}", "Register Overview");
		options.put("{DATE}", new Date().toString());
		Formatter ps = new Formatter();
		ps.format("<tr><td>Offset</td>");
		for (int i = 0; i < Unit.rowWidth; i++) {
			ps.format("<td>%d</td>", Unit.rowWidth - i - 1);
		}
		ps.format("<td>Row</td></tr>");
		options.put("{HEADER}", ps.toString());
		ps.close();
		ps = new Formatter();
		int mul = Unit.rowWidth / 8;
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
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s</td></tr>\n", Unit.rowWidth + 2, "Without Column");
				} else {
					current = row.column;
					colIndex = row.colIndex;
					ps.format("<tr><td colspan='%d' class='columnHeader'>%s [%d]</td></tr>\n", Unit.rowWidth + 2, row.column.name, row.colIndex);
				}
			}
			ps.format("<tr>");
			ps.format("<td class='offset'>%d [0x%02x]</td>", pos * mul, pos * mul);
			for (NamedElement dec : row.definitions) {
				Definition def = (Definition) dec;
				Integer integer = getAndInc(defIndex, def.name);
				int size = MemoryModel.getSize(def);
				if (def.type != Type.UNUSED) {
					String toolTip = String.format(
							"Width:%d Shift:%d Mask:%08X &#10;read: (base[%4$d]&gt;&gt;%2$d)&amp;0x%3$X&#10;write: base[%4$d]=(newVal&amp;0x%3$X)&lt;&lt;%2$d", size,
							(def.bitPos - size) + 1, (1 << size) - 1, (pos * mul) / 4);
					ps.format("<td colspan='%d' title='%s' class='field %s %s'>%s [%d]</td>", size, toolTip, def.rw + "Style", def.register ? "register" : "", def.name, integer);
				} else {
					ps.format("<td colspan='%d' class='field %s %s'>%s</td>", size, def.rw + "Style", "", def.name);
				}

			}
			Integer integer = getAndInc(rowIndex, row.name);
			ps.format("<td class='rowInfo'>%s [%d]</td></tr>\n", row.name, integer);
			pos++;
		}
		options.put("{TABLE}", ps.toString());
		options.put("{HDLINTERFACE}", StringWriteExtension.asString(MemoryModel.buildHDLInterface(unit, rows).setName("Bus"), new HTMLHighlighter(true)));
		ps.close();
		try {
			return Helper.processFile(MemoryModel.class, "memmodelTemplate.html", options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Integer getAndInc(Map<String, Integer> defIndex, String name) {
		Integer integer = defIndex.get(name);
		if (integer == null)
			integer = 0;
		defIndex.put(name, integer + 1);
		return integer;
	}
}
