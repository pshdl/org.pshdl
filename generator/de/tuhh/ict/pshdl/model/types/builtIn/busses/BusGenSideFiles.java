package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

import java.io.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.internal.*;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class BusGenSideFiles {

	public static final String WRAPPER_APPENDIX = "core";

	public static List<SideFile> getSideFiles(HDLUnit unit, int regCount, int memCount, String version, boolean axi) {
		List<SideFile> res = new LinkedList<SideFile>();
		String unitName = fullNameOf(unit).toString('_').toLowerCase();
		String ipcorename = unitName + WRAPPER_APPENDIX;
		String dirName = ipcorename + "_" + version;
		String type = axi ? "axi" : "plb";
		String pCore = "pcores/";
		res.add(mpdFile(unit, ipcorename, dirName, type, pCore, memCount));
		res.add(paoFile(unitName, dirName, type, pCore));
		res.add(wrapperFile(unit, unitName, dirName, version, regCount, memCount, type, pCore));
		res.add(new SideFile(pCore + dirName + "/hdl/vhdl/" + unitName + ".vhd", SideFile.THIS));
		return res;
	}

	private static SideFile wrapperFile(HDLUnit unit, String unitName, String dirName, String version, int regCount, int memCount, String type, String rootDir) {
		String wrapperName = unitName + WRAPPER_APPENDIX;
		String relPath = dirName + "/hdl/vhdl/" + wrapperName + ".vhd";
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", unitName);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", wrapperName);
		options.put("{VERSION}", version.replaceAll("_", "."));
		options.put("{DATE}", new Date().toString());
		options.put("{REGCOUNT}", Integer.toString(regCount));
		StringBuilder memGenerics = new StringBuilder(); // {MEM_GENERICS}
		StringBuilder memArray = new StringBuilder(); // {MEM_ARRAY}
		StringBuilder memCes = new StringBuilder(); // {MEM_CES}
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
		HDLInterface asInterface = unit.asInterface();
		StringBuilder generics = new StringBuilder();
		StringBuilder genericsMap = new StringBuilder();
		StringBuilder ports = new StringBuilder();
		StringBuilder portMap = new StringBuilder();
		for (HDLVariableDeclaration vhd : asInterface.getPorts()) {
			if (vhd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.genSignal) != null) {
				continue;
			}
			StringBuilder targetMap = null;
			StringBuilder target = null;
			HDLDirection direction = vhd.getDirection();
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
			for (HDLVariable v : vhd.getVariables()) {
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
			return new SideFile(rootDir + relPath, Helper.processFile(BusGenSideFiles.class, type + "_wrapper.vhd", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile paoFile(String ipcoreName, String dirName, String type, String rootDir) {
		String relPath = dirName + "/data/" + ipcoreName + WRAPPER_APPENDIX + "_v2_1_0.pao";
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{TARGETFILE}", relPath);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", ipcoreName + WRAPPER_APPENDIX);
		options.put("{DATE}", new Date().toString());
		try {
			return new SideFile(rootDir + relPath, Helper.processFile(BusGenSideFiles.class, type + "_v2_1_0.pao", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile mpdFile(HDLUnit unit, String ipcoreName, String dirName, String bus_type, String rootDir, int memCount) {
		HDLInterface asInterface = unit.asInterface();
		StringBuilder generics = new StringBuilder();
		StringBuilder ports = new StringBuilder();
		for (HDLVariableDeclaration vhd : asInterface.getPorts()) {
			if (vhd.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.genSignal) != null) {
				continue;
			}
			if (vhd.getPrimitive() == null)
				throw new IllegalArgumentException("Only primitive types are supported as in/out pins in a bus based unit");
			StringBuilder type = new StringBuilder();
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
			for (HDLVariable var : vhd.getVariables())
				if (vhd.getDirection() == HDLDirection.PARAMETER) {
					HDLExpression defaultValue = var.getDefaultValue();
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
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{DATE}", new Date().toString());
		options.put("{PORTS}", ports.toString());
		options.put("{GENERICS}", generics.toString());
		try {
			return new SideFile(rootDir + dirName + "/data/" + ipcoreName + "_v2_1_0.mpd", Helper.processFile(BusGenSideFiles.class, bus_type + "_v2_1_0.mpd", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
