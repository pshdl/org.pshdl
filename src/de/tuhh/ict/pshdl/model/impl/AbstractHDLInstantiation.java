package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInstantiation extends HDLStatement{
	/**
	 * Constructs a new instance of {@link AbstractHDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public AbstractHDLInstantiation(HDLObject container) {
		super(container);
	}
	public AbstractHDLInstantiation() {
		super();
	}
		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInstantiation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInstantiation other = (AbstractHDLInstantiation) obj;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		return result;
	}
}