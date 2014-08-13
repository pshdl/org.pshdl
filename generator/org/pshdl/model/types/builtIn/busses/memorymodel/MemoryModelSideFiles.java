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

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;

import java.io.IOException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pshdl.model.HDLUnit;
import org.pshdl.model.extensions.StringWriteExtension;
import org.pshdl.model.types.builtIn.busses.BusGenSideFiles;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import org.pshdl.model.utils.SyntaxHighlighter;
import org.pshdl.model.utils.internal.Helper;
import org.pshdl.model.utils.services.AuxiliaryContent;

import com.google.common.base.Charsets;

public class MemoryModelSideFiles {

	public static List<AuxiliaryContent> getSideFiles(HDLUnit unit, Unit memUnit, List<Row> rows, String version, boolean withDate) {
		final List<AuxiliaryContent> res = new LinkedList<AuxiliaryContent>();
		final String unitName = fullNameOf(unit).toString('_').toLowerCase();
		final String ipcorename = unitName + BusGenSideFiles.WRAPPER_APPENDIX;
		final String dirName = ipcorename + "_" + version;
		final String rootDir = "drivers/";
		final BusAccess ba = new BusAccess();
		res.add(new AuxiliaryContent(rootDir + dirName + "/" + unitName + "Map.xhtml", builtHTML(memUnit, rows, withDate), true));
		res.add(new AuxiliaryContent(rootDir + dirName + "/BusAccess.c", ba.generateAccessC(rows, withDate).toString().getBytes(Charsets.UTF_8), true));
		res.add(new AuxiliaryContent(rootDir + dirName + "/BusAccess.h", ba.generateAccessH(memUnit, rows, withDate).toString().getBytes(Charsets.UTF_8), true));
		res.add(new AuxiliaryContent(rootDir + dirName + "/BusPrint.c", ba.generatePrintC(memUnit, rows, withDate).toString().getBytes(Charsets.UTF_8), true));
		res.add(new AuxiliaryContent(rootDir + dirName + "/BusPrint.h", ba.generatePrintH(memUnit, rows, withDate).toString().getBytes(Charsets.UTF_8), true));
		res.add(new AuxiliaryContent(rootDir + dirName + "/BusStdDefinitions.h", ba.generateStdDef(withDate).toString().getBytes(Charsets.UTF_8), true));
		return res;
	}

	public static byte[] builtHTML(Unit unit, List<Row> rows, boolean withDate) {
		final Map<String, String> options = new HashMap<String, String>();
		options.put("{TITLE}", "Register Overview");
		if (withDate) {
			options.put("{DATE}", new Date().toString());
		}
		options.put("{HEADER}", tableHeader());
		options.put("{TABLE}", generateTableBody(rows));
		options.put("{HDLINTERFACE}", StringWriteExtension.asString(MemoryModel.buildHDLInterface(unit, rows).setName("Bus"), new SyntaxHighlighter.HTMLHighlighter(true)));
		try {
			return Helper.processFile(MemoryModel.class, "memmodelTemplate.html", options);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String tableHeader() {
		try (final Formatter formatter = new Formatter()) {
			formatter.format("<TH><TD>Offset</TD>");
			for (int i = 0; i < Unit.rowWidth; i++) {
				formatter.format("<TD>%d</TD>", Unit.rowWidth - i - 1);
			}
			formatter.format("<TD>Row</TD></TH>");
			return formatter.toString();
		}
	}

	public static String generateTableBody(List<Row> rows) {
		try (Formatter formatter = new Formatter()) {
			final int mul = Unit.rowWidth / 8;
			int pos = 0;
			Column current = null;
			int colIndex = -1;
			final Map<String, Integer> defIndex = new HashMap<String, Integer>();
			final Map<String, Integer> rowIndex = new HashMap<String, Integer>();
			for (final Row row : rows) {
				if ((row.column != current) || (row.colIndex != colIndex))
					if (row.column == null) {
						current = null;
						colIndex = -1;
						formatter.format("<TR><TD colspan='%d' class='columnHeader'>%s</TD></TR>%n", Unit.rowWidth + 2, "Without Column");
					} else {
						current = row.column;
						colIndex = row.colIndex;
						formatter.format("<TR><TD colspan='%d' class='columnHeader'>%s [%d]</TD></TR>%n", Unit.rowWidth + 2, row.column.name, row.colIndex);
					}
				formatter.format("<TR>");
				formatter.format("<TD class='offset'>%d [0x%02x]</TD>", pos * mul, pos * mul);
				for (final NamedElement dec : row.definitions) {
					final Definition def = (Definition) dec;
					final Integer integer = getAndInc(defIndex, def.name);
					final int size = MemoryModel.getSize(def);
					if (def.type != Type.UNUSED) {
						final String toolTip = String.format(
								"Width:%d Shift:%d Mask:%08X &#10;read: (base[%4$d]&gt;&gt;%2$d)&amp;0x%3$X&#10;write: base[%4$d]=(newVal&amp;0x%3$X)&lt;&lt;%2$d", size,
								(def.bitPos - size) + 1, (1 << size) - 1, (pos * mul) / 4);
						formatter.format("<TD colspan='%d' title='%s' class='field %s %s'>%s [%d]</TD>", size, toolTip, def.rw + "Style", def.register ? "register" : "", def.name,
								integer);
					} else {
						formatter.format("<TD colspan='%d' class='field %s %s'>%s</TD>", size, def.rw + "Style", "", def.name);
					}

				}
				final Integer integer = getAndInc(rowIndex, row.name);
				formatter.format("<TD class='rowInfo'>%s [%d]</TD></TR>%n", row.name, integer);
				pos++;
			}
			return formatter.toString();
		}
	}

	private static Integer getAndInc(Map<String, Integer> defIndex, String name) {
		Integer integer = defIndex.get(name);
		if (integer == null) {
			integer = 0;
		}
		defIndex.put(name, integer + 1);
		return integer;
	}
}
