package de.tuhh.ict.pshdl.generator.vhdl.libraries;

import java.util.*;

import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.type.*;

public class VHDLShiftLibrary {
	public static final UseClause USE_CLAUSE = new UseClause("pshdl.ShiftOps.ALL");
	public static final PackageDeclaration PACKAGE;
	static {
		PACKAGE = new PackageDeclaration("pshdl.ShiftOps");
		List<PackageDeclarativeItem> declarations = PACKAGE.getDeclarations();
		HDLPrimitiveType[] values = HDLPrimitiveType.values();
		for (HDLShiftOpType op : HDLShiftOpType.values()) {
			for (int i = 0; i < values.length; i++) {
				HDLPrimitiveType left = values[i];
				String name = getFunctionName(op, left);
				SubtypeIndication lt = VHDLCastsLibrary.getType(left);
				if (lt != null) {
					FunctionDeclaration fd = new FunctionDeclaration(name, VHDLCastsLibrary.getType(left), new Constant("arg", VHDLCastsLibrary.getType(left)), new Constant("s",
							Standard.NATURAL));
					declarations.add(fd);
				}
			}
		}
	}

	private static String getFunctionName(HDLShiftOpType op, HDLPrimitiveType left) {
		String rightName = left.name().charAt(0) + left.name().substring(1).toLowerCase();
		String name = op.name().toLowerCase() + rightName;
		return name;
	}

	public static Expression<?> shift(Expression<?> vhdlExpr, Expression<?> amount, HDLPrimitiveType type, HDLShiftOpType op, Scope scope) {
		String name = getFunctionName(op, type);
		Function resolve = PACKAGE.getScope().resolve(name, Function.class);
		FunctionCall call = new FunctionCall(resolve);
		call.getParameters().add(new AssociationElement(vhdlExpr));
		call.getParameters().add(new AssociationElement(amount));
		return call;
	}
}
