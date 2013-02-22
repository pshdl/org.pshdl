package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.FunctionInformation.FunctionType;

public class PSHDLLib {

	public static final HDLEnum TIMEUNIT = new HDLEnum().setName("TimeUnit").addEnums(new HDLVariable().setName("FS")).addEnums(new HDLVariable().setName("PS"))
			.addEnums(new HDLVariable().setName("NS")).addEnums(new HDLVariable().setName("US")).addEnums(new HDLVariable().setName("MS")).addEnums(new HDLVariable().setName("S"))
			.addEnums(new HDLVariable().setName("KS"));
	public static final HDLEnum EDGE = new HDLEnum().setName("Edge").addEnums(new HDLVariable().setName("RISING")).addEnums(new HDLVariable().setName("FALLING"));
	public static final HDLEnum ACTIVE = new HDLEnum().setName("Active").addEnums(new HDLVariable().setName("LOW")).addEnums(new HDLVariable().setName("HIGH"));
	public static final HDLEnum SYNC = new HDLEnum().setName("Sync").addEnums(new HDLVariable().setName("ASYNC")).addEnums(new HDLVariable().setName("SYNC"));

	public static final HDLInlineFunction ABS = new HDLInlineFunction()
			.setName("abs")
			.addArgs(new HDLVariable().setName("a"))
			.setExpr(
					new HDLTernary()
							.setIfExpr(
									new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.abs.a"))).setRight(new HDLLiteral().setVal("0"))
											.setType(HDLEqualityOpType.LESS))
							.setThenExpr(new HDLManip().setType(HDLManipType.ARITH_NEG).setTarget(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.abs.a"))))
							.setElseExpr(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.abs.a"))));

	public static final HDLInlineFunction MIN = new HDLInlineFunction()
			.setName("min")
			.addArgs(new HDLVariable().setName("a"))
			.addArgs(new HDLVariable().setName("b"))
			.setExpr(
					new HDLTernary()
							.setIfExpr(
									new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.min.a")))
											.setRight(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.min.b"))).setType(HDLEqualityOpType.LESS))
							.setThenExpr(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.min.a")))
							.setElseExpr(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.min.b"))));

	public static final HDLInlineFunction MAX = new HDLInlineFunction()
			.setName("max")
			.addArgs(new HDLVariable().setName("a"))
			.addArgs(new HDLVariable().setName("b"))
			.setExpr(
					new HDLTernary()
							.setIfExpr(
									new HDLEqualityOp().setLeft(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.max.a")))
											.setRight(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.max.b"))).setType(HDLEqualityOpType.GREATER))
							.setThenExpr(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.max.a")))
							.setElseExpr(new HDLVariableRef().setVar(new HDLQualifiedName("pshdl.max.b"))));

	public static final HDLFunction[] FUNCTIONS = new HDLFunction[] { MIN, MAX, ABS };

	private static HDLPackage LIB = null;

	public static HDLPackage getLib() {
		if (LIB == null) {
			HDLPackage pkg = new HDLPackage().setPkg("pshdl");
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(TIMEUNIT));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(EDGE));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(ACTIVE));
			pkg = pkg.addDeclarations(new HDLEnumDeclaration().setHEnum(SYNC));
			for (HDLFunction func : FUNCTIONS) {
				pkg = pkg.addDeclarations(func);
			}
			CompilerInformation info = HDLCore.getCompilerInformation();
			for (Entry<String, FunctionInformation> e : info.registeredFunctions.entrySet()) {
				if (e.getValue().type == FunctionType.NATIVE) {
					pkg = pkg.addDeclarations(new HDLNativeFunction().setName(e.getValue().name).setSimOnly(e.getValue().simulationOnly));
				}
			}
			pkg.freeze(null);
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
