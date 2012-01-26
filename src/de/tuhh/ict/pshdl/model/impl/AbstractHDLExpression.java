package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLExpression extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public AbstractHDLExpression(HDLObject container) {
		super(container);
	}
	public AbstractHDLExpression() {
		super();
	}
		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLExpression))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLExpression other = (AbstractHDLExpression) obj;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		return result;
	}
}