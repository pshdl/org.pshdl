package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLPrimitive contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLPrimitiveType type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression width. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLPrimitive extends AbstractHDLPrimitive {
	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
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
	public HDLPrimitive(int containerID, @Nullable HDLObject container, @NonNull String name, @NonNull HDLPrimitiveType type, @Nullable HDLExpression width, boolean validate) {
		super(containerID, container, name, type, width, validate);
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
	public HDLPrimitive(int containerID, @Nullable HDLObject container, @NonNull String name, @NonNull HDLPrimitiveType type, @Nullable HDLExpression width) {
		this(containerID, container, name, type, width, true);
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

	/**
	 * The accessor for the field type which is of type HDLPrimitiveType.
	 */
	public static HDLFieldAccess<HDLPrimitive, HDLPrimitiveType> fType = new HDLFieldAccess<HDLPrimitive, HDLPrimitiveType>() {
		@Override
		public HDLPrimitiveType getValue(HDLPrimitive obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field width which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLPrimitive, HDLExpression> fWidth = new HDLFieldAccess<HDLPrimitive, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLPrimitive obj) {
			if (obj == null)
				return null;
			return obj.getWidth();
		}
	};

	// $CONTENT-BEGIN$

	public static enum TargetMeta implements MetaAccess<Boolean> {
		TARGET;
	}

	public static HDLPrimitive target(boolean positive) {
		if (positive) {
			HDLPrimitive natural = getNatural();
			natural.addMeta(TargetMeta.TARGET, true);
			return natural;
		}
		HDLPrimitive integer = getInteger();
		integer.addMeta(TargetMeta.TARGET, true);
		return integer;
	}

	public static boolean isTargetMatching(HDLPrimitive prim) {
		Boolean meta = prim.getMeta(TargetMeta.TARGET);
		return meta == null ? false : meta;
	}

	@Override
	protected String validateName(String name) {
		if (this.name == null)
			return "#primitive";
		return super.validateName(name);
	}

	@Override
	public String getName() {
		return "#" + toString();
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
