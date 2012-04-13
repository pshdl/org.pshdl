package de.tuhh.ict.pshdl.model;

import java.util.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLPrimitive extends AbstractHDLPrimitive {
	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLPrimitive(HDLObject container, String name, HDLPrimitiveType type, HDLExpression width, boolean validate) {
		super(container, name, type, width, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 */
	public HDLPrimitive(HDLObject container, String name, HDLPrimitiveType type, HDLExpression width) {
		this(container, name, type, width, true);
	}

	public HDLPrimitive() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLPrimitive;
	}

	public static enum HDLPrimitiveType {
		INT("int"), UINT("uint"), INTEGER("int"), NATURAL("uint"), BIT("bit"), BITVECTOR("bit"), BOOL("<bool>");
		String str;

		HDLPrimitiveType(String op) {
			this.str = op;
		}

		public static HDLPrimitiveType getOp(String op) {
			for (HDLPrimitiveType ass : values()) {
				if (ass.str.equals(op)) {
					return ass;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return str;
		}
	}

	// $CONTENT-BEGIN$

	public static final HDLPrimitive TARGET = getInteger();

	private static WeakHashMap<String, HDLPrimitive> primCache;

	@Override
	protected String validateName(String name) {
		if (this.name == null)
			return "#primitive";
		return super.validateName(name);
	}

	@Override
	public String getName() {
		String strName = "#" + toString();
		getPrimCache().put(strName, this);
		return strName;
	}

	public static HDLType forName(HDLQualifiedName typeRefName) {
		return getPrimCache().get(typeRefName.getLastSegment());
	}

	private static WeakHashMap<String, HDLPrimitive> getPrimCache() {
		if (primCache == null)
			primCache = new WeakHashMap<String, HDLPrimitive>();
		return primCache;
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
		AbstractHDLPrimitive other = (AbstractHDLPrimitive) obj;
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

	// $CONTENT-END$

}
