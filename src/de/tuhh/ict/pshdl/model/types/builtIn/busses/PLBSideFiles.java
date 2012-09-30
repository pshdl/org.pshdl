package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.services.IHDLGenerator.SideFile;

public class PLBSideFiles {
	public static List<SideFile> getSideFiles(HDLUnit unit, int regCount) {
		List<SideFile> res = new LinkedList<SideFile>();
		String unitName = unit.getFullName().getLastSegment();
		String ipcoreName = unitName + "_wrapper";
		String dirName = unitName + "_v1_00_a";
		res.add(mpdFile(unit, ipcoreName, dirName));
		return res;
	}

	private static SideFile mpdFile(HDLUnit unit, String ipcoreName, String dirName) {
		Map<String, String> options = new HashMap<String, String>();
		options.put("{WRAPPERNAME}", ipcoreName);
		HDLInterface asInterface = unit.asInterface();
		StringBuilder generics = new StringBuilder();
		StringBuilder ports = new StringBuilder();
		for (HDLVariableDeclaration vhd : asInterface.getPorts()) {
			StringBuilder type = new StringBuilder();
			switch (vhd.getDirection()) {
			case IN:
				type.append(" = \"\", DIR= I");
				break;
			case OUT:
				type.append(" = \"\", DIR= O");
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
		options.put("{PORTS}", ports.toString());
		options.put("{DATE}", new Date().toString());
		try {
			return new SideFile(dirName + "/data/" + ipcoreName + "_v2_1.mpd", processFile("plbMPD.txt", options));
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
