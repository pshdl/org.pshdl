package de.tuhh.ict.pshdl.model;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;

/**
 * The class HDLExpression contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLExpression extends AbstractHDLExpression {
	/**
	 * Constructs a new instance of {@link HDLExpression}
	 * 
	 * @param objectID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLExpression(int objectID, @Nullable HDLObject container, boolean validate, boolean updateContainer) {
		super(objectID, container, validate, updateContainer);
	}

	/**
	 * Constructs a new instance of {@link HDLExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLExpression(int objectID, @Nullable HDLObject container) {
		this(objectID, container, true, true);
	}

	public HDLExpression() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
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

	// $CONTENT-END$

}
