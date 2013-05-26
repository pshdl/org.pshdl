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
import java.math.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.utils.*;

import com.google.common.base.*;

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
	private static class Scopes {
		public final RootDeclarativeRegion rootScope;
		public final LibraryDeclarativeRegion workScope;

		public Scopes(RootDeclarativeRegion rootScope, LibraryDeclarativeRegion workScope) {
			super();
			this.rootScope = rootScope;
			this.workScope = workScope;
		}
	}

	public static List<HDLInterface> importFile(HDLQualifiedName pkg, InputStream is, HDLLibrary lib, String src) {
		final Scopes scopes = getScopes(lib);
		final List<HDLInterface> res = new LinkedList<HDLInterface>();
		try {
			final VhdlFile file = VhdlParser.parseStream(is, new VhdlParserSettings(), scopes.rootScope, scopes.workScope);
			final List<LibraryUnit> list = file.getElements();
			for (final LibraryUnit unit : list) {
				if (unit instanceof Entity) {
					final Entity entity = (Entity) unit;
					final String id = entity.getIdentifier();
					HDLInterface vInterface = new HDLInterface().setName(pkg.append(id).toString());
					final List<VhdlObjectProvider<Signal>> ports = entity.getPort();
					for (final VhdlObjectProvider<Signal> port : ports) {
						final List<Signal> signals = port.getVhdlObjects();
						for (final Signal signal : signals) {
							final HDLDirection direction = HDLDirection.valueOf(signal.getMode().getUpperCase());
							final HDLQualifiedName qfn = pkg.append(id).append(signal.getIdentifier());
							final HDLVariableDeclaration var = getVariable(null, signal.getType(), direction, qfn, null, new ArrayList<HDLExpression>(), scopes);
							vInterface = vInterface.addPorts(var);
						}
					}
					final List<VhdlObjectProvider<Constant>> param = entity.getGeneric();
					for (final VhdlObjectProvider<Constant> port : param) {
						final List<Constant> signals = port.getVhdlObjects();
						for (final Constant signal : signals) {
							final HDLDirection direction = HDLDirection.valueOf(signal.getMode().getUpperCase());
							final HDLQualifiedName qfn = pkg.append(id).append(signal.getIdentifier());
							HDLVariableDeclaration var = getVariable(signal.getDefaultValue(), signal.getType(), direction, qfn, null, new ArrayList<HDLExpression>(), scopes);
							var = var.setDirection(HDLDirection.PARAMETER);
							vInterface = vInterface.addPorts(var);
						}
					}
					vInterface.freeze(null);
					res.add(vInterface);
					lib.addInterface(vInterface, src);
				}
				getScopes(lib).workScope.getFiles().add(file);
				// System.out.println("VHDLImporter.importFile()" + unit);
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final VhdlParserException e) {
			e.printStackTrace();
		}
		return res;
	}

	private final static MetaAccess<Scopes> SCOPES = new GenericMeta<VHDLImporter.Scopes>("SCOPES", true);

	private static Scopes getScopes(HDLLibrary lib) {
		Scopes scopes = lib.getMeta(SCOPES);
		if (scopes != null)
			return scopes;
		final RootDeclarativeRegion rootScope = new RootDeclarativeRegion();
		final LibraryDeclarativeRegion workScope = new LibraryDeclarativeRegion("work");
		rootScope.getLibraries().add(workScope);
		scopes = new Scopes(rootScope, workScope);
		lib.addMeta(SCOPES, scopes);
		return scopes;
	}

	public static HDLVariableDeclaration getVariable(Expression<?> defaultValue, SubtypeIndication left, HDLDirection direction, HDLQualifiedName qfn, HDLExpression width,
			ArrayList<HDLExpression> dimensions, Scopes scopes) {
		if (left instanceof IndexSubtypeIndication) {
			final IndexSubtypeIndication isi = (IndexSubtypeIndication) left;
			final Range dr = (Range) isi.getRanges().get(0);
			return getVariable(defaultValue, isi.getBaseType(), direction, qfn, convertRange(dr), dimensions, scopes);
		}
		if (StdLogic1164.STD_LOGIC.equals(left) || StdLogic1164.STD_ULOGIC.equals(left) || Standard.BIT.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.BIT, qfn, width, dimensions);
		final boolean isBitVector = Standard.BIT_VECTOR.equals(left);
		if (StdLogic1164.STD_LOGIC_VECTOR.equals(left) || StdLogic1164.STD_ULOGIC.equals(left) || isBitVector)
			return createVar(defaultValue, direction, HDLPrimitiveType.BITVECTOR, qfn, width, dimensions);
		if (NumericStd.SIGNED.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.INT, qfn, width, dimensions);
		if (NumericStd.UNSIGNED.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.UINT, qfn, width, dimensions);
		if (Standard.INTEGER.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.INTEGER, qfn, width, dimensions);
		if (Standard.NATURAL.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.NATURAL, qfn, width, dimensions);
		if (Standard.STRING.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.STRING, qfn, width, dimensions);
		if (Standard.BOOLEAN.equals(left))
			return createVar(defaultValue, direction, HDLPrimitiveType.BOOL, qfn, width, dimensions);
		if (left instanceof ConstrainedArray) {
			final ConstrainedArray ca = (ConstrainedArray) left;
			@SuppressWarnings("rawtypes")
			final List<DiscreteRange> ranges = ca.getIndexRanges();
			scopes.workScope.getScope().resolve(ca.getIdentifier());
			for (final DiscreteRange<?> discreteRange : ranges) {
				dimensions.add(convertRange((Range) discreteRange));
			}
			HDLVariableDeclaration var = getVariable(defaultValue, ca.getElementType(), direction, qfn, null, dimensions, scopes);
			var = var.addAnnotations(new HDLAnnotation().setName(HDLBuiltInAnnotations.VHDLType.toString()).setValue(getFullName(ca.getIdentifier(), scopes)));
			return var;
		}
		if (left instanceof UnconstrainedArray) {
			final UnconstrainedArray ca = (UnconstrainedArray) left;
			scopes.workScope.getScope().resolve(ca.getIdentifier());
			dimensions.add(HDLLiteral.get(-20));
			HDLVariableDeclaration var = getVariable(defaultValue, ca.getElementType(), direction, qfn, null, dimensions, scopes);
			var = var.addAnnotations(new HDLAnnotation().setName(HDLBuiltInAnnotations.VHDLType.toString()).setValue(getFullName(ca.getIdentifier(), scopes)));
			return var;
		}
		if (left instanceof EnumerationType) {
			System.out.println("VHDLImporter.getVariable()" + ((EnumerationType) left).getIdentifier());
		}
		throw new IllegalArgumentException("Unexpected Type:" + left);
	}

	private static String getFullName(String identifier, Scopes scopes) {
		String pkg = null;
		for (final LibraryDeclarativeRegion lib : scopes.rootScope.getLibraries()) {
			for (final VhdlFile file : lib.getFiles()) {
				for (final LibraryUnit libraryUnit : file.getElements())
					if (libraryUnit instanceof PackageDeclaration) {
						final PackageDeclaration pd = (PackageDeclaration) libraryUnit;
						pkg = pd.getIdentifier();
						final List<PackageDeclarativeItem> declarations = pd.getDeclarations();
						for (final PackageDeclarativeItem pdi : declarations)
							if (pdi instanceof Type) {
								final Type t = (Type) pdi;
								if (t.getIdentifier().equalsIgnoreCase(identifier))
									return "work." + pkg + "." + identifier;
							}
					}
			}
		}
		return null;
	}

	private static HDLExpression convertRange(Range dr) {
		final HDLExpression from = getExpression(dr.getFrom(), false);
		final HDLExpression to = getExpression(dr.getTo(), false);
		HDLExpression width;
		if (dr.getDirection() == Direction.DOWNTO) {
			width = subThenPlus1(from, to);
		} else {
			width = subThenPlus1(to, from);
		}
		return width;
	}

	private static HDLVariableDeclaration createVar(Expression<?> defaultValue, HDLDirection direction, HDLPrimitiveType pt, HDLQualifiedName name, HDLExpression width,
			ArrayList<HDLExpression> dimensions) {
		final HDLPrimitive p = new HDLPrimitive().setType(pt).setWidth(width);
		HDLExpression hDefault = null;
		if (defaultValue != null) {
			hDefault = getExpression(defaultValue, pt == HDLPrimitiveType.STRING);
		}
		return new HDLVariableDeclaration().setDirection(direction).setType(p)
				.addVariables(new HDLVariable().setName(name.getLastSegment()).setDimensions(dimensions).setDefaultValue(hDefault));
	}

	private static HDLExpression subThenPlus1(HDLExpression from, HDLExpression to) {
		final HDLArithOp left = new HDLArithOp().setLeft(from).setType(HDLArithOpType.MINUS).setRight(to);
		final HDLArithOp op = new HDLArithOp().setLeft(left).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1)).copyDeepFrozen(null);
		final Optional<BigInteger> constant = ConstantEvaluate.valueOf(op);
		if (constant.isPresent())
			return HDLLiteral.get(constant.get());
		return op;
	}

	@SuppressWarnings("incomplete-switch")
	private static HDLExpression getExpression(Expression<?> from, boolean canBeString) {
		// TODO Support references to Generics
		if ((from instanceof HexLiteral) || (from instanceof BinaryLiteral) || (from instanceof CharacterLiteral)) {
			final String hex = from.toString();
			final String hexValue = hex.substring(2, hex.length() - 1);
			if (hexValue.length() != 0)
				return HDLLiteral.get(new BigInteger(Integer.toString(Integer.parseInt(hexValue, 16))));
			return HDLLiteral.get(0);
		}
		if (from instanceof DecimalLiteral) {
			final DecimalLiteral dl = (DecimalLiteral) from;
			return HDLLiteral.get(new BigInteger(dl.getValue()));
		}
		if (from instanceof StringLiteral) {
			final StringLiteral sl = (StringLiteral) from;
			return new HDLLiteral().setStr(canBeString).setVal(sl.getString());
		}
		if (from instanceof BinaryExpression<?>) {
			final BinaryExpression<?> bin = (BinaryExpression<?>) from;
			final HDLExpression left = getExpression(bin.getLeft(), false);
			final HDLExpression right = getExpression(bin.getRight(), false);
			final ExpressionKind kind = bin.getExpressionKind();
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
			final Constant c = (Constant) from;
			return getExpression(c.getDefaultValue(), true);
		}
		if (Standard.BOOLEAN_FALSE.equals(from))
			return new HDLLiteral().setVal("false");
		if (Standard.BOOLEAN_TRUE.equals(from))
			return new HDLLiteral().setVal("true");
		if (from instanceof Parentheses) {
			final Parentheses p = (Parentheses) from;
			return getExpression(p.getExpression(), canBeString);
		}
		throw new IllegalArgumentException("Expression not yet supported:" + from);
	}

	public static void main(String[] args) throws IOException {
		final String targetPackage = args[0];
		final HDLLibrary lib = new HDLLibrary();
		for (int i = 1; i < args.length; i++) {
			final String string = args[i];
			final File file = new File(string);
			if (file.isDirectory()) {
				final File[] files = file.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File arg0, String arg1) {
						return arg1.endsWith("vhd");
					}
				});
				for (final File f : files) {
					try {
						importFile(f, lib, targetPackage);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					importFile(file, lib, targetPackage);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void importFile(File f, HDLLibrary lib, String targetPackage) throws IOException {
		final FileInputStream fis = new FileInputStream(f);
		final List<HDLInterface> hifs = importFile(new HDLQualifiedName(targetPackage), fis, lib, f.getAbsolutePath());
		for (final HDLInterface hdi : hifs) {
			System.out.println(hdi);
		}
		fis.close();
	}
}
