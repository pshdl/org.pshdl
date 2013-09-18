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
package org.pshdl.model.types.builtIn.busses;

import static org.pshdl.model.extensions.FullNameExtension.*;

import java.io.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.internal.*;
import org.pshdl.model.utils.services.IHDLGenerator.SideFile;

import com.google.common.io.*;

public class BusGenSideFiles {

	public static final String WRAPPER_APPENDIX = "core";

	public static List<SideFile> getSideFiles(HDLUnit unit, int regCount, int memCount, String version, boolean axi) {
		final List<SideFile> res = new LinkedList<SideFile>();
		final String unitName = fullNameOf(unit).toString('_').toLowerCase();
		final String ipcorename = unitName + WRAPPER_APPENDIX;
		final String dirName = ipcorename + "_" + version;
		final String type = axi ? "axi" : "plb";
		final String pCore = "pcores/";
		res.add(mpdFile(unit, ipcorename, dirName, type, pCore, memCount));
		res.add(paoFile(unitName, dirName, type, pCore));
		res.add(wrapperFile(unit, unitName, dirName, version, regCount, memCount, type, pCore));
		res.add(new SideFile(pCore + dirName + "/hdl/vhdl/" + unitName + ".vhd", SideFile.THIS, true));
		try {
			res.add(new SideFile(pCore + dirName + "/hdl/vhdl/pshdl_pkg.vhd", ByteStreams.toByteArray(BusGenSideFiles.class.getResourceAsStream("/pshdl_pkg.vhd")), true));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private static SideFile wrapperFile(HDLUnit unit, String unitName, String dirName, String version, int regCount, int memCount, String type, String rootDir) {
		final String wrapperName = unitName + WRAPPER_APPENDIX;
		final String relPath = dirName + "/hdl/vhdl/" + wrapperName + ".vhd";
		final Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", unitName);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", wrapperName);
		options.put("{VERSION}", version.replaceAll("_", "."));
		options.put("{DATE}", new Date().toString());
		options.put("{REGCOUNT}", Integer.toString(regCount));
		final StringBuilder memGenerics = new StringBuilder(); // {MEM_GENERICS}
		final StringBuilder memArray = new StringBuilder(); // {MEM_ARRAY}
		final StringBuilder memCes = new StringBuilder(); // {MEM_CES}
		for (int i = 0; i < memCount; i++) {
			memGenerics.append("    C_MEM" + i + "_BASEADDR                : std_logic_vector     := X\"FFFFFFFF\";\n");
			memGenerics.append("    C_MEM" + i + "_HIGHADDR                : std_logic_vector     := X\"00000000\";");
			memArray.append("      ,ZERO_ADDR_PAD & C_MEM" + i + "_BASEADDR    -- user logic memory space " + i + " base address\n");
			memArray.append("      ,ZERO_ADDR_PAD & C_MEM" + i + "_HIGHADDR    -- user logic memory space " + i + " high address\n");
			memCes.append(",").append(i + 1).append(" => 1\n");
		}
		options.put("{MEM_GENERICS}", memGenerics.toString());
		options.put("{MEM_ARRAY}", memArray.toString());
		options.put("{MEM_CES}", memCes.toString());
		String memPortMap;
		if (memCount > 0) {
			memPortMap = "      Bus2IP_Addr                    => ipif_Bus2IP_Addr,\n" + //
					"Bus2IP_CS                      => ipif_Bus2IP_CS(1 to " + memCount + "),\n" + //
					"Bus2IP_RNW                     => ipif_Bus2IP_RNW,";
		} else {
			memPortMap = "";
		}
		options.put("{MEM_PORTMAP}", memPortMap);
		final HDLInterface asInterface = unit.asInterface();
		final StringBuilder generics = new StringBuilder();
		final StringBuilder genericsMap = new StringBuilder();
		final StringBuilder ports = new StringBuilder();
		final StringBuilder portMap = new StringBuilder();
		for (final HDLVariableDeclaration vhd : asInterface.getPorts()) {
			if (vhd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.genSignal) != null) {
				continue;
			}
			StringBuilder targetMap = null;
			StringBuilder target = null;
			final HDLDirection direction = vhd.getDirection();
			String dir = direction.toString();
			switch (direction) {
			case PARAMETER:
				targetMap = genericsMap;
				target = generics;
				dir = "";
				break;
			case IN:
			case OUT:
				targetMap = portMap;
				target = ports;
				break;
			default:
				continue;
			}
			for (final HDLVariable v : vhd.getVariables()) {
				target.append('\t').append(v.getName()).append("\t:\t").append(dir).append(" ");
				switch (vhd.getPrimitive().getType()) {
				case BIT:
					target.append("std_logic");
					break;
				case BITVECTOR:
					target.append("std_logic_vector");
					target.append("(").append(vhd.getPrimitive().getWidth()).append("-1 downto 0)");
					break;
				case UINT:
					target.append("unsigned");
					target.append("(").append(vhd.getPrimitive().getWidth()).append("-1 downto 0)");
					break;
				case INT:
					target.append("signed");
					target.append("(").append(vhd.getPrimitive().getWidth()).append("-1 downto 0)");
					break;
				case NATURAL:
					target.append("NATURAL");
					break;
				case INTEGER:
					target.append("INTEGER");
					break;
				default:
				}
				if ((vhd.getDirection() == HDLDirection.PARAMETER) && (v.getDefaultValue() != null)) {
					target.append(":=").append(v.getDefaultValue());
				}
				target.append(';');
				targetMap.append('\t').append(v.getName()).append(" => ").append(v.getName()).append(",\n");
			}
		}
		options.put("{PORTS}", ports.toString());
		options.put("{PORTMAP}", portMap.toString());
		options.put("{GENERICS}", generics.toString());
		options.put("{GENERICSMAP}", genericsMap.toString());
		try {
			return new SideFile(rootDir + relPath, Helper.processFile(BusGenSideFiles.class, type + "_wrapper.vhd", options), true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile paoFile(String ipcoreName, String dirName, String type, String rootDir) {
		final String relPath = dirName + "/data/" + ipcoreName + WRAPPER_APPENDIX + "_v2_1_0.pao";
		final Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{TARGETFILE}", relPath);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", ipcoreName + WRAPPER_APPENDIX);
		options.put("{DATE}", new Date().toString());
		try {
			return new SideFile(rootDir + relPath, Helper.processFile(BusGenSideFiles.class, type + "_v2_1_0.pao", options), true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile mpdFile(HDLUnit unit, String ipcoreName, String dirName, String bus_type, String rootDir, int memCount) {
		final HDLInterface asInterface = unit.asInterface();
		final StringBuilder generics = new StringBuilder();
		final StringBuilder ports = new StringBuilder();
		for (final HDLVariableDeclaration vhd : asInterface.getPorts()) {
			if (vhd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.genSignal) != null) {
				continue;
			}
			if (vhd.getPrimitive() == null)
				throw new IllegalArgumentException("Only primitive types are supported as in/out pins in a bus based unit");
			final StringBuilder type = new StringBuilder();
			switch (vhd.getDirection()) {
			case IN:
				type.append(" = \"\", DIR= I");
				if (vhd.getPrimitive().getWidth() != null) {
					type.append(", VEC = [" + vhd.getPrimitive().getWidth() + "-1:0]");
				}
				break;
			case OUT:
				type.append(" = \"\", DIR= O");
				if (vhd.getPrimitive().getWidth() != null) {
					type.append(", VEC = [" + vhd.getPrimitive().getWidth() + "-1:0]");
				}
				break;
			case CONSTANT:
			case PARAMETER:
				type.append(", DT = ");
				switch (vhd.getPrimitive().getType()) {
				case BIT:
					type.append("std_logic");
					break;
				case INT:
				case UINT:
				case BITVECTOR:
					type.append("std_logic_vector");
					break;
				case INTEGER:
				case NATURAL:
					type.append("INTEGER");
					break;
				default:
					throw new IllegalArgumentException("Parameter type:+" + vhd.getPrimitive().getType() + " not supported");
				}
				break;
			case INTERNAL:
			case HIDDEN:
				break;
			default:
				throw new IllegalArgumentException("Direction:" + vhd.getDirection() + " not supported");
			}
			for (final HDLVariable var : vhd.getVariables())
				if (vhd.getDirection() == HDLDirection.PARAMETER) {
					final HDLExpression defaultValue = var.getDefaultValue();
					String init = "";
					if (defaultValue != null) {
						init = " = " + defaultValue;
					}
					generics.append("PARAMETER ").append(var.getName()).append(init).append(type).append('\n');
				} else {
					ports.append("PORT ").append(var.getName()).append(type).append('\n');
				}
		}
		for (int i = 0; i < memCount; i++) {
			generics.append("PARAMETER C_MEM" + i + "_BASEADDR = 0xffffffff, DT = std_logic_vector, PAIR = C_MEM" + i + "_HIGHADDR, ADDRESS = BASE, BUS = SPLB");
			generics.append("PARAMETER C_MEM" + i + "_HIGHADDR = 0x00000000, DT = std_logic_vector, PAIR = C_MEM" + i + "_BASEADDR, ADDRESS = HIGH, BUS = SPLB");
		}
		final Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{DATE}", new Date().toString());
		options.put("{PORTS}", ports.toString());
		options.put("{GENERICS}", generics.toString());
		try {
			return new SideFile(rootDir + dirName + "/data/" + ipcoreName + "_v2_1_0.mpd", Helper.processFile(BusGenSideFiles.class, bus_type + "_v2_1_0.mpd", options), true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
