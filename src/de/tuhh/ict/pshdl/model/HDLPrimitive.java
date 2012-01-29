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
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLPrimitive(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, HDLRegisterConfig register, HDLDirection direction, HDLPrimitiveType type, HDLExpression width, boolean validate) {
		super(container, name, annotations, register, direction, type, width, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 */
	public HDLPrimitive(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, HDLRegisterConfig register, HDLDirection direction, HDLPrimitiveType type, HDLExpression width) {
		this(container, name, annotations, register, direction, type, width, true);
	}
	public HDLPrimitive() {
		super();
	}
	 public static enum HDLPrimitiveType {
	INT, UINT, INTEGER, NATURAL, BIT, BITVECTOR, BOOL;
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
