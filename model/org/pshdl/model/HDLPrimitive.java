/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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
package org.pshdl.model;

import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.ANY_BIT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.ANY_INT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.ANY_UINT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.BIT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.BITVECTOR;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.INT;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.INTEGER;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.NATURAL;
import static org.pshdl.model.HDLPrimitive.HDLPrimitiveType.UINT;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.extensions.StringWriteExtension;
import org.pshdl.model.impl.AbstractHDLPrimitive;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.SyntaxHighlighter;

/**
 * The class HDLPrimitive contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; dim. Can be <code>null</code>.</li>
 * <li>HDLPrimitiveType type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression width. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLPrimitive extends AbstractHDLPrimitive {
	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLPrimitive(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull HDLPrimitiveType type,
			@Nullable HDLExpression width, boolean validate) {
		super(id, container, name, dim, type, width, validate);
	}

	public HDLPrimitive() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLPrimitive;
	}

	public static enum HDLPrimitiveType {
		INT("int"), ANY_INT("int<>"), INTEGER("int32"), UINT("uint"), ANY_UINT("uint<>"), NATURAL("uint32"), BIT("bit"), ANY_BIT("bit<>"), BITVECTOR("bit"), BOOL("bool"), STRING(
				"string");
		String str;

		HDLPrimitiveType(String op) {
			this.str = op;
		}

		@Nullable
		public static HDLPrimitiveType getOp(String op) {
			for (final HDLPrimitiveType ass : values()) {
				if (ass.str.equals(op))
					return ass;
			}
			return null;
		}

		@Override
		@Nonnull
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field type which is of type HDLPrimitiveType.
	 */
	public static HDLFieldAccess<HDLPrimitive, HDLPrimitiveType> fType = new HDLFieldAccess<HDLPrimitive, HDLPrimitiveType>("type", HDLPrimitiveType.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLPrimitiveType getValue(HDLPrimitive obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}

		@Override
		public HDLPrimitive setValue(HDLPrimitive obj, HDLPrimitiveType value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	/**
	 * The accessor for the field width which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLPrimitive, HDLExpression> fWidth = new HDLFieldAccess<HDLPrimitive, HDLExpression>("width", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLPrimitive obj) {
			if (obj == null)
				return null;
			return obj.getWidth();
		}

		@Override
		public HDLPrimitive setValue(HDLPrimitive obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setWidth(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (type == obj)
			return fType;
		if (width == obj)
			return fWidth;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	@Override
	protected String validateName(String name) {
		if (this.name == null)
			return "#primitive";
		return super.validateName(name);
	}

	@Override
	public @Nonnull String getName() {
		return "#" + StringWriteExtension.asString(this, SyntaxHighlighter.none());
	}

	public static HDLPrimitive getInt() {
		return new HDLPrimitive().setType(HDLPrimitiveType.INT).setName("#int<?>");
	}

	public static HDLPrimitive getInteger() {
		return new HDLPrimitive().setType(HDLPrimitiveType.INTEGER).setName("#int");
	}

	public static HDLPrimitive getUint() {
		return new HDLPrimitive().setType(HDLPrimitiveType.UINT).setName("#uint<?>");
	}

	public static HDLPrimitive getNatural() {
		return new HDLPrimitive().setType(HDLPrimitiveType.NATURAL).setName("#uint");
	}

	public static HDLPrimitive getBool() {
		return new HDLPrimitive().setType(HDLPrimitiveType.BOOL).setName("#<bool>");
	}

	public static HDLPrimitive getString() {
		return new HDLPrimitive().setType(HDLPrimitiveType.STRING).setName("#<string>");
	}

	public static HDLPrimitive getBit() {
		return new HDLPrimitive().setType(HDLPrimitiveType.BIT).setName("#bit");
	}

	public static HDLPrimitive getBitvector() {
		return new HDLPrimitive().setType(HDLPrimitiveType.BITVECTOR).setName("#bit<?>");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLPrimitive))
			return false;
		final AbstractHDLPrimitive other = (AbstractHDLPrimitive) obj;
		if (type == null) {
			if (other.getType() != null)
				return false;
		} else if (!type.equals(other.getType()))
			return false;
		if (width == null) {
			if (other.getWidth() != null)
				return false;
		} else if (!width.equals(other.getWidth()))
			return false;
		return true;
	}

	private static final EnumSet<HDLPrimitiveType> numbers = EnumSet.of(INT, INTEGER, UINT, NATURAL, ANY_INT, ANY_UINT);

	public boolean isNumber() {
		return numbers.contains(getType());
	}

	private static final EnumSet<HDLPrimitiveType> bits = EnumSet.of(INT, INTEGER, UINT, NATURAL, BIT, BITVECTOR, ANY_INT, ANY_UINT, ANY_BIT);

	public boolean isBits() {
		return bits.contains(getType());
	}

	private static final EnumSet<HDLPrimitiveType> any = EnumSet.of(ANY_INT, ANY_UINT, ANY_BIT);

	public boolean isAny() {
		return any.contains(getType());
	}

	public static boolean isAny(HDLPrimitiveType type) {
		return any.contains(type);
	}

	public static class AnyPrimitive extends HDLPrimitive {

		public AnyPrimitive(HDLPrimitiveType primitive) {
			super(gid.incrementAndGet(), null, "#any" + primitive, null, primitive, null, false);
		}

	}

	public static AnyPrimitive anyBit() {
		return new AnyPrimitive(HDLPrimitiveType.ANY_BIT);
	}

	public static AnyPrimitive anyInt() {
		return new AnyPrimitive(HDLPrimitiveType.ANY_INT);
	}

	public static AnyPrimitive anyUint() {
		return new AnyPrimitive(HDLPrimitiveType.ANY_UINT);
	}

	public static boolean isTargetMatching(HDLPrimitive lType) {
		switch (lType.type) {
		case ANY_BIT:
		case ANY_INT:
		case ANY_UINT:
			return true;
		case BIT:
		case BITVECTOR:
		case BOOL:
		case INT:
		case INTEGER:
		case NATURAL:
		case STRING:
		case UINT:
		}
		return false;
	}

	// $CONTENT-END$

}
