package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;

import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;

public class VHDLUtils {
	public static Literal<?> toBinaryLiteral(int widthInt, BigInteger lit) {
		String binLit = lit.toString(2);
		StringBuilder sb = new StringBuilder(widthInt);
		for (int i = widthInt; i > binLit.length(); i--)
			sb.append('0');
		if (binLit.length() > widthInt)
			sb.append(binLit.substring(binLit.length() - widthInt));
		else
			sb.append(binLit);
		// System.out.println("VHDLUtils.toBinaryLiteral()" + sb);
		return new BinaryLiteral(sb.toString());
	}

	public static void main(String[] args) {
		toBinaryLiteral(5, BigInteger.ONE);
		toBinaryLiteral(3, BigInteger.TEN);
	}
}
