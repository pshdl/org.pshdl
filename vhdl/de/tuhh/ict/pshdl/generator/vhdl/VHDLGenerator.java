package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.output.*;

public class VHDLGenerator {

	private final static String FS = System.getProperty("file.separator");

	public static CharSequence generate(HDLPackage unit, String src) {
		VhdlFile visitor = generateVHDL(unit, src);
		String vhdlString = VhdlOutput.toVhdlString(visitor);
		// System.out.println(vhdlString);
		return vhdlString;
	}

	public static VhdlFile generate(HDLPackage unit, String targetDir, String src) {
		VhdlFile visitor = generateVHDL(unit, src);
		try {
			VhdlOutput.print(visitor);
			String name = unit.getPkg();
			if (name == null) {
				name = unit.getUnits().get(0).getName();
			}
			VhdlOutput.toFile(visitor, targetDir + FS + name + ".vhd");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return visitor;
	}

	private static VhdlFile generateVHDL(HDLPackage pkg, String src) {
		pkg = Insulin.transform(pkg, src);
		return VHDLPackageExtension.INST.toVHDL(pkg);
	}
}
