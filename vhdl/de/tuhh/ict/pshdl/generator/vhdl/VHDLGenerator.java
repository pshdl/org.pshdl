package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.output.*;

public class VHDLGenerator {

	private final static String FS = System.getProperty("file.separator");

	public static CharSequence generate(HDLPackage unit) {
		VhdlFile visitor = generateVHDL(unit);
		String vhdlString = VhdlOutput.toVhdlString(visitor);
		// System.out.println(vhdlString);
		return vhdlString;
	}

	public static VhdlFile generate(HDLPackage unit, String targetDir) {
		VhdlFile visitor = generateVHDL(unit);
		try {
			VhdlOutput.print(visitor);
			String name = unit.getPkg();
			if (name == null)
				name = unit.getUnits().get(0).getName();
			VhdlOutput.toFile(visitor, targetDir + FS + name + ".vhd");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return visitor;
	}

	private static VhdlFile generateVHDL(HDLPackage pkg) {
		pkg = Insulin.transform(pkg);
		return VHDLPackageExtension.INST.toVHDL(pkg);
	}
}
