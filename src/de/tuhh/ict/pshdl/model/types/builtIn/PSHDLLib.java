package de.tuhh.ict.pshdl.model.types.builtIn;

import de.tuhh.ict.pshdl.model.*;

public class PSHDLLib {

	public static final HDLEnum TIMEUNIT = new HDLEnum().setName("TimeUnit").addEnums(new HDLVariable().setName("NS")).addEnums(new HDLVariable().setName("US"))
			.addEnums(new HDLVariable().setName("MS")).addEnums(new HDLVariable().setName("S")).addEnums(new HDLVariable().setName("KS"));
	public static final HDLEnum EDGE = new HDLEnum().setName("Edge").addEnums(new HDLVariable().setName("RAISING")).addEnums(new HDLVariable().setName("FALLING"))
			.addEnums(new HDLVariable().setName("DEFAULT"));
	public static final HDLEnum ACTIVE = new HDLEnum().setName("Active").addEnums(new HDLVariable().setName("LOW")).addEnums(new HDLVariable().setName("HIGH"))
			.addEnums(new HDLVariable().setName("DEFAULT"));
	public static final HDLEnum SYNC = new HDLEnum().setName("Sync").addEnums(new HDLVariable().setName("ASYNC")).addEnums(new HDLVariable().setName("SYNC"))
			.addEnums(new HDLVariable().setName("DEFAULT"));

	public static final HDLPackage LIB = new HDLPackage()
			.addUnits(new HDLUnit().setSimulation(false).setName("pshdl").addStatements(new HDLEnumDeclaration().setHEnum(TIMEUNIT))
					.addStatements(new HDLEnumDeclaration().setHEnum(EDGE)).addStatements(new HDLEnumDeclaration().setHEnum(ACTIVE))
					.addStatements(new HDLEnumDeclaration().setHEnum(SYNC)));

	public static void main(String[] args) {
		System.out.println(LIB.toString());
	}

	public static String asString() {
		return LIB.toString();
	}
}
