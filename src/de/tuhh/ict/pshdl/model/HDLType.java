package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLType(HDLObject container, boolean validate) {
		super(container, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLType(HDLObject container) {
		this(container, true);
	}
	public HDLType() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
