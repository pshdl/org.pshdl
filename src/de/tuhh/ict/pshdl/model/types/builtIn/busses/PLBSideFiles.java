package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class PLBSideFiles {

	public static List<SideFile> getSideFiles(HDLUnit unit, int regCount, String version) {
		List<SideFile> res = new LinkedList<SideFile>();
		String unitName = unit.getFullName().getLastSegment().toLowerCase();
		String dirName = unitName + "_" + version;
		res.add(mpdFile(unit, unitName, dirName));
		res.add(paoFile(unitName, dirName));
		res.add(wrapperFile(unit, unitName, dirName, version, regCount));
		res.add(new SideFile(dirName + "/hdl/vhdl/" + unitName + ".vhd", SideFile.THIS));
		return res;
	}

	private static SideFile wrapperFile(HDLUnit unit, String ipcoreName, String dirName, String version, int regCount) {
		String wrapperName = ipcoreName + "_wrapper";
		String relPath = dirName + "/hdl/vhdl/" + wrapperName + ".vhd";
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", wrapperName);
		options.put("{VERSION}", version.replaceAll("_", "."));
		options.put("{DATE}", new Date().toString());
		options.put("{REGCOUNT}", Integer.toString(regCount));
		HDLInterface asInterface = unit.asInterface();
		StringBuilder generics = new StringBuilder();
		StringBuilder genericsMap = new StringBuilder();
		StringBuilder ports = new StringBuilder();
		StringBuilder portMap = new StringBuilder();
		for (HDLVariableDeclaration vhd : asInterface.getPorts()) {
			StringBuilder targetMap = null;
			StringBuilder target = null;
			switch (vhd.getDirection()) {
			case PARAMETER:
				targetMap = genericsMap;
				target = generics;
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
				target.append('\t').append(v.getName()).append("\t:\t").append(vhd.getDirection()).append(" ");
				switch (vhd.getPrimitive().getType()) {
				case BIT:
					target.append("std_logic");
					break;
				case BITVECTOR:
					target.append("std_logic_vector");
					target.append("(").append(vhd.getPrimitive().getWidth()).append(" downto 0)");
					break;
				case UINT:
					target.append("unsigned");
					target.append("(").append(vhd.getPrimitive().getWidth()).append(" downto 0)");
					break;
				case INT:
					target.append("signed");
					target.append("(").append(vhd.getPrimitive().getWidth()).append(" downto 0)");
					break;
				case NATURAL:
					target.append("NATURAL");
					break;
				case INTEGER:
					target.append("INTEGER");
					break;
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
			return new SideFile(relPath, processFile("plb_wrapper.vhd", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile paoFile(String ipcoreName, String dirName) {
		String relPath = dirName + "/data/" + ipcoreName + "_v2_1_0.pao";
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{TARGETFILE}", relPath);
		options.put("{DIRNAME}", dirName);
		options.put("{WRAPPERNAME}", ipcoreName + "_wrapper");
		options.put("{DATE}", new Date().toString());
		try {
			return new SideFile(relPath, processFile("plb_v2_1_0.pao", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SideFile mpdFile(HDLUnit unit, String ipcoreName, String dirName) {
		HDLInterface asInterface = unit.asInterface();
		StringBuilder generics = new StringBuilder();
		StringBuilder ports = new StringBuilder();
		for (HDLVariableDeclaration vhd : asInterface.getPorts()) {
			if (vhd.getPrimitive() == null)
				throw new IllegalArgumentException("Only primitive types are supported as in/out pins in a bus based unit");
			StringBuilder type = new StringBuilder();
			switch (vhd.getDirection()) {
			case IN:
				type.append(" = \"\", DIR= I");
				if (vhd.getPrimitive().getWidth() != null) {
					type.append(", VEC = [" + vhd.getPrimitive().getWidth() + ":0]");
				}
				break;
			case OUT:
				type.append(" = \"\", DIR= O");
				if (vhd.getPrimitive().getWidth() != null) {
					type.append(", VEC = [" + vhd.getPrimitive().getWidth() + ":0]");
				}
				break;
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
					throw new IllegalArgumentException("Not supported");
				}
				break;
			default:
				throw new IllegalArgumentException("Not supported");
			}
			for (HDLVariable var : vhd.getVariables()) {
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
		}
		Map<String, String> options = new HashMap<String, String>();
		options.put("{NAME}", ipcoreName);
		options.put("{DATE}", new Date().toString());
		options.put("{PORTS}", ports.toString());
		options.put("{GENERICS}", generics.toString());
		try {
			return new SideFile(dirName + "/data/" + ipcoreName + "_v2_1_0.mpd", processFile("plb_v2_1_0.mpd", options));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] processFile(String string, Map<String, String> options) throws IOException {
		InputStream stream = PLBSideFiles.class.getResourceAsStream(string);
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		boolean first = true;
		for (String key : options.keySet()) {
			if (!first)
				sb.append('|');
			sb.append(Pattern.quote(key));
			first = false;
		}
		sb.append(")");
		Pattern p = Pattern.compile(sb.toString());
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = null;
		StringBuilder res = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			Matcher matcher = p.matcher(line);
			int offset = 0;
			while (matcher.find()) {
				res.append(line.substring(offset, matcher.start()));
				String group = matcher.group(1);
				String replacement = options.get(group);
				res.append(replacement);
				offset = matcher.end();
			}
			res.append(line.substring(offset));
			res.append("\n");
		}
		return res.toString().getBytes();
	}

	public static void main(String[] args) {
		Map<String, String> options = new HashMap<String, String>();
		options.put("{WRAPPERNAME}", "Hallo");
		String ports = "Meine Ports";
		options.put("{PORTS}", ports);
		options.put("{DATE}", new Date().toString());
		try {
			System.out.println(new String(processFile("plbMPD.txt", options)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
