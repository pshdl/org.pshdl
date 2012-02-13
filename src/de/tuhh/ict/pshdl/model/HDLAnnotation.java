package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;


public class HDLAnnotation extends AbstractHDLAnnotation {
	/**
	 * Constructs a new instance of {@link HDLAnnotation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLAnnotation(HDLObject container, String name, String value, boolean validate) {
		super(container, name, value, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLAnnotation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can <b>not</b> be <code>null</code>.
	 */
	public HDLAnnotation(HDLObject container, String name, String value) {
		this(container, name, value, true);
	}
	public HDLAnnotation() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
