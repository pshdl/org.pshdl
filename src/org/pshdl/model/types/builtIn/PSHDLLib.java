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

import java.util.Map.Entry;

import org.pshdl.model.*;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.*;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import org.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;

public class PSHDLLib {

	public static final HDLEnum TIMEUNIT = new HDLEnum().setName("TimeUnit").addEnums(new HDLVariable().setName("FS")).addEnums(new HDLVariable().setName("PS"))
			.addEnums(new HDLVariable().setName("NS")).addEnums(new HDLVariable().setName("US")).addEnums(new HDLVariable().setName("MS")).addEnums(new HDLVariable().setName("S"))
			.addEnums(new HDLVariable().setName("KS"));
	public static final HDLEnum EDGE = new HDLEnum().setName("Edge").addEnums(new HDLVariable().setName("RISING")).addEnums(new HDLVariable().setName("FALLING"));
	public static final HDLEnum ACTIVE = new HDLEnum().setName("Active").addEnums(new HDLVariable().setName("LOW")).addEnums(new HDLVariable().setName("HIGH"));
	public static final HDLEnum SYNC = new HDLEnum().setName("Sync").addEnums(new HDLVariable().setName("ASYNC")).addEnums(new HDLVariable().setName("SYNC"));

	public static final HDLFunction ABS_UINT = createABS(Type.ANY_UINT);
	public static final HDLFunction ABS_INT = createABS(Type.ANY_INT);

	private static HDLFunction createABS(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName("abs").setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ));
	}

	public static final HDLFunction MIN_UINT = createMIN(Type.ANY_UINT);
	public static final HDLFunction MIN_INT = createMIN(Type.ANY_INT);

	private static HDLFunction createMIN(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName("min").setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("b")).setRw(RWType.READ));
	}

	public static final HDLFunction MAX_INT = createMAX(Type.ANY_INT);
	public static final HDLFunction MAX_UINT = createMAX(Type.ANY_UINT);

	private static HDLFunction createMAX(Type type) {
		return new HDLNativeFunction().setSimOnly(false).setName("max").setReturnType(new HDLFunctionParameter().setType(type).setRw(RWType.RETURN))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("a")).setRw(RWType.READ))
				.addArgs(new HDLFunctionParameter().setType(type).setName(new HDLVariable().setName("b")).setRw(RWType.READ));
	}

	public static final HDLFunction[] FUNCTIONS = new HDLFunction[] { MIN_UINT, MAX_UINT, ABS_UINT, MIN_INT, MAX_INT, ABS_INT };

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
				for (final HDLFunction func : FUNCTIONS) {
					pkg = pkg.addDeclarations(func);
				}
				final CompilerInformation info = HDLCore.getCompilerInformation();
				for (final Entry<String, FunctionInformation> e : info.registeredFunctions.entrySet())
					if (e.getValue().type == FunctionType.NATIVE) {
						pkg = pkg.addDeclarations(new HDLNativeFunction().setName(e.getValue().name).setSimOnly(e.getValue().simulationOnly));
					}
				pkg.freeze(null);
				LIB = pkg;
			}
		}
		return LIB;
	}

	public static void main(String[] args) {
		HDLCore.init(new IServiceProvider.ServiceLoaderProvider());
		final HDLPackage lib2 = getLib();
		System.out.println(lib2.toString());
		lib2.validateAllFields(null, true);
	}

	public static String asString() {
		return LIB.toString();
	}
}
