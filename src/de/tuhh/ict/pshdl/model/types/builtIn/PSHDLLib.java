package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;

public class PSHDLLib {

	public static final HDLEnum TIMEUNIT = new HDLEnum().setName("TimeUnit").addEnums(new HDLVariable().setName("NS")).addEnums(new HDLVariable().setName("US"))
			.addEnums(new HDLVariable().setName("MS")).addEnums(new HDLVariable().setName("S")).addEnums(new HDLVariable().setName("KS"));
	public static final HDLEnum EDGE = new HDLEnum().setName("Edge").addEnums(new HDLVariable().setName("RISING")).addEnums(new HDLVariable().setName("FALLING"));
	public static final HDLEnum ACTIVE = new HDLEnum().setName("Active").addEnums(new HDLVariable().setName("LOW")).addEnums(new HDLVariable().setName("HIGH"));
	public static final HDLEnum SYNC = new HDLEnum().setName("Sync").addEnums(new HDLVariable().setName("ASYNC")).addEnums(new HDLVariable().setName("SYNC"));

	private static HDLPackage LIB = null;

	public static HDLPackage getLib() {
		if (LIB == null) {
			HDLPackage pkg = new HDLPackage().setPkg("pshdl");
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(TIMEUNIT));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(EDGE));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(ACTIVE));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(SYNC));
			CompilerInformation info = HDLCore.getCompilerInformation();
			for (Entry<String, FunctionInformation> e : info.registeredFunctions.entrySet()) {
				pkg = pkg.addDeclarations(new HDLNativeFunction().setName(e.getValue().name).setSimOnly(e.getValue().simulationOnly));
			}
			LIB = pkg;
		}
		return LIB;
	}

	public static void main(String[] args) {
		HDLCore.init(new IServiceProvider.ServiceLoaderProvider());
		System.out.println(getLib().toString());
	}

	public static String asString() {
		return LIB.toString();
	}
}
