package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLInterface extends AbstractHDLInterface {
	/**
	 * Constructs a new instance of {@link HDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLInterface(HDLObject container, String name, ArrayList<HDLVariableDeclaration> ports, boolean validate) {
		super(container, name, ports, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 */
	public HDLInterface(HDLObject container, String name, ArrayList<HDLVariableDeclaration> ports) {
		this(container, name, ports, true);
	}
	public HDLInterface() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
