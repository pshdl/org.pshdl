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
package org.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;
import java.util.regex.*;

import org.pshdl.model.*;

import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;

public class VHDLUtils {
	public static Literal<?> toBinaryLiteral(int widthInt, BigInteger lit) {
		StringBuilder sb = new StringBuilder(widthInt);
		if (lit.signum() < 0) {
			final BigInteger mask = BigInteger.ONE.shiftLeft(widthInt).subtract(BigInteger.ONE);
			sb.append(lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(2));
		} else {
			final String binLit = lit.toString(2);
			sb = zeroFill(widthInt, binLit);
		}
		// System.out.println("VHDLUtils.toBinaryLiteral()" + sb);
		final String string = sb.toString();
		return new BinaryLiteral(string);
	}

	public static Literal<?> toHexLiteral(int widthInt, BigInteger lit) {
		StringBuilder sb = new StringBuilder(widthInt / 4);
		if ((widthInt % 4) != 0)
			return toBinaryLiteral(widthInt, lit);
		if (lit.signum() < 0) {
			final BigInteger mask = BigInteger.ONE.shiftLeft(widthInt).subtract(BigInteger.ONE);
			sb.append(lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(16));
		} else {
			sb = zeroFill(widthInt / 4, lit.toString(16));
		}
		// System.out.println("VHDLUtils.toBinaryLiteral()" + sb);
		return new HexLiteral(sb.toString());
	}

	private static StringBuilder zeroFill(int widthInt, String binLit) {
		final StringBuilder sb = new StringBuilder();
		for (int i = widthInt; i > binLit.length(); i--) {
			sb.append('0');
		}
		if (binLit.length() > widthInt) {
			sb.append(binLit.substring(binLit.length() - widthInt));
		} else {
			sb.append(binLit);
		}
		return sb;
	}

	public final static String[] keywords = { "abs", "if", "access", "impure", "after", "in", "alias", "inertial", "all", "inout", "and", "is", "architecture", "label", "array",
			"library", "assert", "linkage", "attribute", "literal", "begin", "loop", "block", "map", "body", "mod", "buffer", "nand", "bus", "new", "case", "next", "component",
			"nor", "configuration", "not", "constant", "null", "disconnect", "of", "downto", "on", "else", "open", "elsif", "or", "end", "others", "entity", "out", "exit",
			"package", "file", "port", "for", "postponed", "function", "procedure", "generate", "process", "generic", "pure", "group", "range", "guarded", "record", "register",
			"reject", "rem", "report", "return", "rol", "ror", "select", "severity", "signal", "shared", "sla", "sll", "sra", "srl", "subtype", "then", "to", "transport", "type",
			"unaffected", "units", "until", "use", "variable", "wait", "when", "while", "with", "xnor", "xor" };

	public final static Set<String> keywordSet;
	static {
		keywordSet = new HashSet<String>();
		for (final String keyword : keywords) {
			keywordSet.add(keyword);
		}
	}

	private static final Pattern vhdlName = Pattern.compile("[a-zA-Z]\\w*");

	public static String getVHDLName(String name) {
		if ((name.charAt(0) == '\\') && (name.charAt(name.length() - 1) == '\\'))
			return name;
		if (keywordSet.contains(name))
			return "\\" + name + "\\";
		if (vhdlName.matcher(name).matches())
			return name;
		return "\\" + name + "\\";
	}

	public static void main(String[] args) {
		final long a = -4373652435859253850L;
		final BigInteger lit = BigInteger.valueOf(a);
		final BigInteger litB = new BigInteger("-4373652435859253850");
		System.out.println("VHDLUtils.main()" + lit.equals(litB));
		final BigInteger mask = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
		System.out.println("Long.toBinaryString(a)                             " + Long.toBinaryString(a));
		System.out.println("Long.toBinaryString(lit.longValue())               " + Long.toBinaryString(lit.longValue()));
		System.out.println("lit.xor(mask).toString(2)                         " + lit.xor(mask).toString(2));
		System.out.println("lit.xor(mask).add(BigInteger.ONE).toString(2)      " + lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(2));
		System.out.println("Long.toBinaryString(lit.not().longValue())           " + Long.toBinaryString(lit.not().longValue()));
		System.out.println("Long.toBinaryString(~a)                              " + Long.toBinaryString(~a));
		System.out.println("lit.not().toString(2)                                " + lit.not().toString(2));
		System.out.println("lit.toString(2)                                     " + lit.toString(2));
		System.out.println("lit.abs().not().add(BigInteger.ONE).toString(2)     " + lit.abs().not().add(BigInteger.ONE).toString(2));
		System.out.println("lit.abs()                                            " + lit.abs().toString(2));
		System.out.print("byte array                                         ");
		final byte[] ba = lit.toByteArray();
		for (final byte b : ba) {
			System.out.print(zeroFill(8, Integer.toBinaryString(b & 0xFF)));
		}
		System.out.println();
		System.out.println(toBinaryLiteral(80, lit));
		System.out.println(toBinaryLiteral(64, lit));
		System.out.println(toBinaryLiteral(32, lit));
		// toBinaryLiteral(3, BigInteger.TEN);
	}

	public static boolean isKeyword(String name) {
		return keywordSet.contains(name.toLowerCase());
	}

	public static String mapName(HDLInterfaceRef ref) {
		final String hIf = ref.getHIfRefName().getLastSegment();
		final String hVar = ref.getVarRefName().getLastSegment();
		return mapName(hIf, hVar);
	}

	public static String mapName(String hIf, String hVar) {
		return getVHDLName("$map_" + hIf + "_" + hVar);
	}

	public static String unescapeVHDLName(String string) {
		if (string.charAt(0) == '\\')
			return string.substring(1, string.length() - 1);
		return string;
	}
}
