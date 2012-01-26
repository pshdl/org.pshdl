package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLDeclaration extends HDLStatement{
	/**
	 * Constructs a new instance of {@link AbstractHDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public AbstractHDLDeclaration(HDLObject container) {
		super(container);
	}
	public AbstractHDLDeclaration() {
		super();
	}
		@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLDeclaration other = (AbstractHDLDeclaration) obj;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		return result;
	}
}