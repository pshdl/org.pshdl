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
package org.pshdl.model.types.builtIn;

import org.pshdl.interpreter.JavaPSHDLLib.Active;
import org.pshdl.interpreter.JavaPSHDLLib.Assert;
import org.pshdl.interpreter.JavaPSHDLLib.Edge;
import org.pshdl.interpreter.JavaPSHDLLib.Sync;
import org.pshdl.interpreter.JavaPSHDLLib.TimeUnit;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.utils.HDLCore;

public class PSHDLLib {

	public static final HDLEnum TIMEUNIT = fromEnum(TimeUnit.class);
	public static final HDLEnum EDGE = fromEnum(Edge.class);
	public static final HDLEnum ACTIVE = fromEnum(Active.class);
	public static final HDLEnum SYNC = fromEnum(Sync.class);
	public static final HDLEnum ASSERT = fromEnum(Assert.class);

	private static HDLPackage LIB = null;
	private static Object LIB_LOCK = new Object();

	public static HDLPackage getLib() {
		synchronized (LIB_LOCK) {
			if (LIB == null) {
				HDLPackage pkg = new HDLPackage().setLibURI("PSHDLLib").setPkg("pshdl");
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(TIMEUNIT));
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(EDGE));
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(ACTIVE));
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(SYNC));
				pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(ASSERT));
				pkg.freeze(null);
				LIB = pkg;
			}
		}
		return LIB;
	}

	private static <S extends Enum<?>> HDLEnum fromEnum(Class<S> fromEnum) {
		final String name = fromEnum.getName();
		final String hdlName = name.substring(name.lastIndexOf('$') + 1);
		HDLEnum hdlEnum = new HDLEnum().setName(hdlName);
		for (final Enum<?> e : fromEnum.getEnumConstants()) {
			hdlEnum = hdlEnum.addEnums(new HDLVariable().setName(e.name()));
		}
		return hdlEnum;
	}

	public static void main(String[] args) {
		HDLCore.defaultInit();
		final HDLPackage lib2 = getLib();
		System.out.println(lib2.toString());
		lib2.validateAllFields(null, true);
	}

	public static String asString() {
		return LIB.toString();
	}
}
