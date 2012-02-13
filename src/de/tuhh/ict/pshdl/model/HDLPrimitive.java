package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


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
	 *			  if <code>true</code> the paramaters will be validated.
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
	 public static enum HDLPrimitiveType {
	INT("int"), UINT("uint"), INTEGER("int"), NATURAL("uint"), BIT("bit"), BITVECTOR("bit"), BOOL("<BOOL>");	
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
	
//$CONTENT-BEGIN$
	private static WeakHashMap<String, HDLPrimitive> primCache = new WeakHashMap<String, HDLPrimitive>();

	@Override
	public String getName() {
		String strName = "#" + toString();
		primCache.put(strName, this);
		return strName;
	}

	public static HDLType forName(HDLQualifiedName typeRefName) {
		return primCache.get(typeRefName.getLastSegment());
	}
//$CONTENT-END$
	
}	
