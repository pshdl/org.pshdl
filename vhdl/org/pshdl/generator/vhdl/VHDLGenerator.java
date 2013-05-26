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

import java.io.*;

import org.pshdl.model.*;
import org.pshdl.model.utils.*;

import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.output.*;

public class VHDLGenerator {

	private final static String FS = System.getProperty("file.separator");

	public static CharSequence generate(HDLPackage unit, String src) {
		final VhdlFile visitor = generateVHDL(unit, src);
		final String vhdlString = VhdlOutput.toVhdlString(visitor);
		// System.out.println(vhdlString);
		return vhdlString;
	}

	public static VhdlFile generate(HDLPackage unit, String targetDir, String src) {
		final VhdlFile visitor = generateVHDL(unit, src);
		try {
			VhdlOutput.print(visitor);
			String name = unit.getPkg();
			if (name == null) {
				name = unit.getUnits().get(0).getName();
			}
			VhdlOutput.toFile(visitor, targetDir + FS + name + ".vhd");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return visitor;
	}

	private static VhdlFile generateVHDL(HDLPackage pkg, String src) {
		pkg = Insulin.transform(pkg, src);
		return VHDLPackageExtension.INST.toVHDL(pkg);
	}
}
