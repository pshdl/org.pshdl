package de.tuhh.ict.pshdl.generator.vhdl;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.parser.*;
import de.upb.hni.vmagic.type.*;

public class VHDLImporter {

	private static RootDeclarativeRegion rootScope;
	private static LibraryDeclarativeRegion workScope;

	public static List<HDLInterface> importFile(URI location, InputStream is, HDLLibrary lib) {
		init();
		List<HDLInterface> res = new LinkedList<HDLInterface>();
		try {
			VhdlFile file = VhdlParser.parseStream(is, new VhdlParserSettings(), rootScope, workScope);
			List<LibraryUnit> list = file.getElements();
			for (LibraryUnit unit : list) {
				if (unit instanceof Entity) {
					Entity entity = (Entity) unit;
					String id = entity.getIdentifier();
					HDLInterface vInterface = new HDLInterface().setName("VHDL.work." + id);
					List<VhdlObjectProvider<Signal>> ports = entity.getPort();
					for (VhdlObjectProvider<Signal> port : ports) {
						List<Signal> signals = port.getVhdlObjects();
						for (Signal signal : signals) {
							HDLDirection direction = HDLDirection.valueOf(signal.getMode().getUpperCase());
							HDLQualifiedName qfn = HDLQualifiedName.create("VHDL", "work", id, signal.getIdentifier());
							HDLVariableDeclaration var = getVariable(signal.getType(), direction, qfn, null, new ArrayList<HDLExpression>());
							vInterface = vInterface.addPorts(var);
						}
					}
					res.add(vInterface);
					lib.addInterface(vInterface);
				}
				workScope.getFiles().add(file);
				// System.out.println("VHDLImporter.importFile()" + unit);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (VhdlParserException e) {
			e.printStackTrace();
		}
		return res;
	}

	private static void init() {
		if (rootScope != null)
			return;
		rootScope = new RootDeclarativeRegion();
		workScope = new LibraryDeclarativeRegion("work");
		rootScope.getLibraries().add(workScope);
	}

	public static HDLVariableDeclaration getVariable(SubtypeIndication left, HDLDirection direction, HDLQualifiedName qfn, HDLExpression width, ArrayList<HDLExpression> dimensions) {
		if (left instanceof IndexSubtypeIndication) {
			IndexSubtypeIndication isi = (IndexSubtypeIndication) left;
			Range dr = (Range) isi.getRanges().get(0);
			return getVariable(isi.getBaseType(), direction, qfn, convertRange(dr), dimensions);
		}
		if (StdLogic1164.STD_LOGIC.equals(left))
			return createVar(direction, HDLPrimitiveType.BIT, qfn, width, dimensions);
		if (StdLogic1164.STD_LOGIC_VECTOR.equals(left))
			return createVar(direction, HDLPrimitiveType.BITVECTOR, qfn, width, dimensions);
		if (NumericStd.SIGNED.equals(left))
			return createVar(direction, HDLPrimitiveType.INT, qfn, width, dimensions);
		if (NumericStd.UNSIGNED.equals(left))
			return createVar(direction, HDLPrimitiveType.UINT, qfn, width, dimensions);
		if (Standard.INTEGER.equals(left))
			return createVar(direction, HDLPrimitiveType.INTEGER, qfn, width, dimensions);
		if (Standard.NATURAL.equals(left))
			return createVar(direction, HDLPrimitiveType.NATURAL, qfn, width, dimensions);
		if (left instanceof ConstrainedArray) {
			ConstrainedArray ca = (ConstrainedArray) left;
			@SuppressWarnings("rawtypes")
			List<DiscreteRange> ranges = ca.getIndexRanges();
			workScope.getScope().resolve(ca.getIdentifier());
			for (DiscreteRange<?> discreteRange : ranges) {
				dimensions.add(convertRange((Range) discreteRange));
			}
			HDLVariableDeclaration var = getVariable(ca.getElementType(), direction, qfn, null, dimensions);
			var = var.addAnnotations(new HDLAnnotation().setName(HDLBuiltInAnnotations.VHDLType.toString()).setValue(getFullName(ca.getIdentifier())));
			return var;
		}
		throw new IllegalArgumentException("Unexpected Type:" + left);
	}

	private static String getFullName(String identifier) {
		String pkg = null;
		for (LibraryDeclarativeRegion lib : rootScope.getLibraries()) {
			for (VhdlFile file : lib.getFiles()) {
				for (LibraryUnit libraryUnit : file.getElements()) {
					if (libraryUnit instanceof PackageDeclaration) {
						PackageDeclaration pd = (PackageDeclaration) libraryUnit;
						pkg = pd.getIdentifier();
						List<PackageDeclarativeItem> declarations = pd.getDeclarations();
						for (PackageDeclarativeItem pdi : declarations) {
							if (pdi instanceof Type) {
								Type t = (Type) pdi;
								if (t.getIdentifier().equalsIgnoreCase(identifier))
									return "work." + pkg + "." + identifier;
							}
						}
					}
				}
			}
		}
		return null;
	}

	private static HDLExpression convertRange(Range dr) {
		HDLExpression from = getExpression(dr.getFrom());
		HDLExpression to = getExpression(dr.getTo());
		HDLExpression width;
		if (dr.getDirection() == Direction.DOWNTO)
			width = subThenPlus1(from, to);
		else
			width = subThenPlus1(to, from);
		return width;
	}

	private static HDLVariableDeclaration createVar(HDLDirection direction, HDLPrimitiveType pt, HDLQualifiedName name, HDLExpression width, ArrayList<HDLExpression> dimensions) {
		HDLPrimitive p = new HDLPrimitive().setType(pt).setWidth(width);
		return new HDLVariableDeclaration().setDirection(direction).setType(p).addVariables(new HDLVariable().setName(name.getLastSegment()).setDimensions(dimensions));
	}

	private static HDLExpression subThenPlus1(HDLExpression from, HDLExpression to) {
		HDLArithOp left = new HDLArithOp().setLeft(from).setType(HDLArithOpType.MINUS).setRight(to);
		HDLArithOp op = new HDLArithOp().setLeft(left).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
		BigInteger constant = op.constantEvaluate(null);
		if (constant != null)
			return HDLLiteral.get(constant);
		return op;
	}

	@SuppressWarnings("incomplete-switch")
	private static HDLExpression getExpression(Expression<?> from) {
		// TODO Support references to Generics
		if (from instanceof DecimalLiteral) {
			DecimalLiteral dl = (DecimalLiteral) from;
			return HDLLiteral.get(new BigInteger(dl.getValue()));
		}
		if (from instanceof BinaryExpression<?>) {
			BinaryExpression<?> bin = (BinaryExpression<?>) from;
			HDLExpression left = getExpression(bin.getLeft());
			HDLExpression right = getExpression(bin.getRight());
			ExpressionKind kind = bin.getExpressionKind();
			switch (kind) {
			case PLUS:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.PLUS).setRight(right);
			case MINUS:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.MINUS).setRight(right);
			case MULTIPLY:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.MUL).setRight(right);
			case DIVIDE:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.DIV).setRight(right);
			case MOD:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.MOD).setRight(right);
			case POW:
				return new HDLArithOp().setLeft(left).setType(HDLArithOpType.POW).setRight(right);
			}
		}
		if (from instanceof Constant) {
			Constant c = (Constant) from;
			return getExpression(c.getDefaultValue());
		}
		throw new IllegalArgumentException("Expression not yet supported:" + from);
	}
}
