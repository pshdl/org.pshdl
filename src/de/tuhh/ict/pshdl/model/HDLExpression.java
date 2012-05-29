package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLExpression contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
@SuppressWarnings("all")
public abstract class HDLExpression extends AbstractHDLExpression {
	/**
	 * Constructs a new instance of {@link HDLExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLExpression(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLExpression(HDLObject container) {
		this(container, true);
	}

	public HDLExpression() {
		super();
	}

	public HDLClass getClassType() {
		return HDLClass.HDLExpression;
	}

	// $CONTENT-BEGIN$
	public HDLStatement getStatement() {
		HDLObject parent = this;
		do {
			parent = parent.getContainer();
		} while (!(parent instanceof HDLStatement) && (parent != null));
		return (HDLStatement) parent;
	}

	// $CONTENT-END$

}
