package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

@SuppressWarnings("all")
public class HDLVariable extends AbstractHDLVariable {
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param validate
	 *			  if <code>true</code> the paramaters will be validated.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, ArrayList<HDLExpression> dimensions, boolean validate) {
		super(container, name, annotations, dimensions, validate);
	}
	/**
	 * Constructs a new instance of {@link HDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 */
	public HDLVariable(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, ArrayList<HDLExpression> dimensions) {
		this(container, name, annotations, dimensions, true);
	}
	public HDLVariable() {
		super();
	}
//$CONTENT-BEGIN$
//$CONTENT-END$
}	
