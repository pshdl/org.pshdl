package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;


public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLUnit(HDLObject container, String pkg, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements, boolean validate) {
		super(container, pkg, name, imports, statements, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 */
	public HDLUnit(HDLObject container, String pkg, String name, ArrayList<String> imports, ArrayList<HDLStatement> statements) {
		this(container, pkg, name, imports, statements, true);
	}
	public HDLUnit() {
		super();
	}
	
//$CONTENT-BEGIN$
//$CONTENT-END$
	
}	
