package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLEnum extends AbstractHDLEnum {
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLEnum(HDLObject container, HDLRegisterConfig register, HDLDirection direction, String name, ArrayList<String> enums, boolean validate) {
		super(container, register, direction, name, enums, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public HDLEnum(HDLObject container, HDLRegisterConfig register, HDLDirection direction, String name, ArrayList<String> enums) {
		this(container, register, direction, name, enums, true);
	}
	public HDLEnum() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
