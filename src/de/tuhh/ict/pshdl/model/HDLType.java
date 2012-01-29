package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLType(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, boolean validate) {
		super(container, name, annotations, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 */
	public HDLType(HDLObject container, String name, ArrayList<HDLAnnotation> annotations) {
		this(container, name, annotations, true);
	}
	public HDLType() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
