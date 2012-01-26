package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class HDLValueType extends AbstractHDLValueType {
	/**
	 * Constructs a new instance of {@link HDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLValueType(HDLObject container, HDLRegisterConfig register, HDLDirection direction, boolean validate) {
		super(container, register, direction, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 */
	public HDLValueType(HDLObject container, HDLRegisterConfig register, HDLDirection direction) {
		this(container, register, direction, true);
	}
	public HDLValueType() {
		super();
	}
	 	public static enum HDLDirection {
			IN,OUT,INOUT,PARAMETER,CONST,INTERNAL,HIDDEN,
		}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
