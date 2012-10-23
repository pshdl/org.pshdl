package de.tuhh.ict.pshdl.generator.vhdl.libraries;

import java.util.*;

import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.libraryunit.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.type.*;

public class VHDLTypesLibrary {
	public static final UseClause USE_CLAUSE = new UseClause("pshdl.Types.ALL");
	public static final PackageDeclaration PACKAGE;
	public static final FunctionDeclaration TERNARY_INTEGER;
	public static final FunctionDeclaration TERNARY_SL;
	public static final FunctionDeclaration TERNARY_SLV;
	public static final FunctionDeclaration TERNARY_UNSIGNED;
	public static final FunctionDeclaration TERNARY_SIGNED;
	static {
		PACKAGE = new PackageDeclaration("pshdl.Types");
		List<PackageDeclarativeItem> declarations = PACKAGE.getDeclarations();
		TERNARY_INTEGER = createTernaryOp(declarations, Standard.INTEGER);
		TERNARY_SL = createTernaryOp(declarations, StdLogic1164.STD_LOGIC);
		TERNARY_SLV = createTernaryOp(declarations, StdLogic1164.STD_LOGIC_VECTOR);
		TERNARY_UNSIGNED = createTernaryOp(declarations, NumericStd.UNSIGNED);
		TERNARY_SIGNED = createTernaryOp(declarations, NumericStd.SIGNED);
	}

	private static FunctionDeclaration createTernaryOp(List<PackageDeclarativeItem> declarations, SubtypeIndication type) {
		FunctionDeclaration fd = new FunctionDeclaration("ternaryOp", type, new Constant("condition", Standard.BOOLEAN), new Constant("thenValue", type), new Constant("elseValue",
				type));
		declarations.add(fd);
		return fd;
	}
}
