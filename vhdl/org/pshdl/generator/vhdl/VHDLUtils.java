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

import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;

public class VHDLUtils {
	public static Literal<?> toBinaryLiteral(int widthInt, BigInteger lit) {
		StringBuilder sb = new StringBuilder(widthInt);
		if (lit.signum() < 0) {
			BigInteger mask = BigInteger.ONE.shiftLeft(widthInt).subtract(BigInteger.ONE);
			sb.append(lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(2));
		} else {
			String binLit = lit.toString(2);
			sb = zeroFill(widthInt, binLit);
		}
		// System.out.println("VHDLUtils.toBinaryLiteral()" + sb);
		String string = sb.toString();
		return new BinaryLiteral(string);
	}

	public static Literal<?> toHexLiteral(int widthInt, BigInteger lit) {
		StringBuilder sb = new StringBuilder(widthInt / 4);
		if ((widthInt % 4) != 0)
			return toBinaryLiteral(widthInt, lit);
		if (lit.signum() < 0) {
			BigInteger mask = BigInteger.ONE.shiftLeft(widthInt).subtract(BigInteger.ONE);
			sb.append(lit.abs().and(mask).xor(mask).add(BigInteger.ONE).toString(16));
		} else {
			sb = zeroFill(widthInt / 4, lit.toString(16));
		}
		// System.out.println("VHDLUtils.toBinaryLiteral()" + sb);
		return new HexLiteral(sb.toString());
	}

	private static StringBuilder zeroFill(int widthInt, String binLit) {
		StringBuilder sb = new StringBuilder();
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

	public static void main(String[] args) {
		long a = -4373652435859253850L;
		BigInteger lit = BigInteger.valueOf(a);
		BigInteger litB = new BigInteger("-4373652435859253850");
		System.out.println("VHDLUtils.main()" + lit.equals(litB));
		BigInteger mask = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
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
		byte[] ba = lit.toByteArray();
		for (byte b : ba) {
			System.out.print(zeroFill(8, Integer.toBinaryString(b & 0xFF)));
		}
		System.out.println();
		System.out.println(toBinaryLiteral(80, lit));
		System.out.println(toBinaryLiteral(64, lit));
		System.out.println(toBinaryLiteral(32, lit));
		// toBinaryLiteral(3, BigInteger.TEN);
	}
}
