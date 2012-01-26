package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLStatement extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public AbstractHDLStatement(HDLObject container) {
		super(container);
	}
	public AbstractHDLStatement() {
		super();
	}
		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLStatement other = (AbstractHDLStatement) obj;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		return result;
	}
}