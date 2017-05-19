/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2017 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.*;

public class Constant extends Definition implements NamedElement {
	public static enum ConstantType {
		date, time, checksum, number
	}

	public int value;
	public ConstantType constType;
	public Integer arrayIndex;
	private String constName;

	public Constant(String name, int value, int width) {
		super(getConstName(name, value), false, false, RWType.constant, Type.BIT, width);
		this.constName = name;
		this.value = value;
		this.constType = ConstantType.number;
	}

	private static String getConstName(String name, int value) {
		if (name == null)
			return "const" + Integer.toHexString(value);
		if (name.startsWith("$"))
			return name.substring(1);
		return name;
	}

	public Constant(String name, ConstantType value) {
		super(getConstName(name, -1), false, false, RWType.constant, Type.UINT, 32);
		this.constName = name;
		this.constType = value;
	}

	@Override
	public String toString() {
		try (Formatter f = new Formatter();) {
			f.format("const");
			if ((constType == ConstantType.number) && (width != 32))
				f.format("<%d>", width);
			if (constName != null)
				f.format(" %s", constName);
			if (constType == ConstantType.number)
				f.format(" 0x%X;", value);
			else
				f.format(";");
			return f.toString();
		}
	}

}
