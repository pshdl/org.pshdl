package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public abstract class HDLValueType extends AbstractHDLValueType {
	/**
	 * Constructs a new instance of {@link HDLValueType}
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
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLValueType(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, HDLRegisterConfig register, HDLDirection direction, boolean validate) {
		super(container, name, annotations, register, direction, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLValueType}
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
	 */
	public HDLValueType(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, HDLRegisterConfig register, HDLDirection direction) {
		this(container, name, annotations, register, direction, true);
	}
	public HDLValueType() {
		super();
	}
	 public static enum HDLDirection {
	IN("in"), OUT("out"), INOUT("inout"), PARAMETER("param"), CONSTANT("const"), INTERNAL(""), HIDDEN("");	
		String str;
	
		HDLDirection(String op) {
			this.str = op;
		}
	
		public static HDLDirection getOp(String op) {
			for (HDLDirection ass : values()) {
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
//$CONTENT-END$
	
}	
