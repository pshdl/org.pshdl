package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;

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

	@Override
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

	public abstract HDLPrimitive determineType();
	// $CONTENT-END$

}
