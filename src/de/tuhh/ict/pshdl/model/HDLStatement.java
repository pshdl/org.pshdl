package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

/**
 * The class HDLStatement contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLStatement extends AbstractHDLStatement {
	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLStatement(HDLObject container, boolean validate) {
		super(container, validate);
	}

	/**
	 * Constructs a new instance of {@link HDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public HDLStatement(HDLObject container) {
		this(container, true);
	}

	public HDLStatement() {
		super();
	}

	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLStatement;
	}

	// $CONTENT-BEGIN$
	private HDLResolver resolver = new HDLResolver(this, true);

	@Override
	public HDLType resolveType(HDLQualifiedName var) {
		return resolver.resolveType(var);
	}

	@Override
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		return resolver.resolveVariable(var);
	}

	@Override
	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		return resolver.resolveInterface(hIf);
	}

	@Override
	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		return resolver.resolveEnum(hEnum);
	}
	// $CONTENT-END$

}
