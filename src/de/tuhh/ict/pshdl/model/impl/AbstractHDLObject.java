package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 */
	public AbstractHDLObject(HDLObject container) {
		this.container=validateContainer(container);
	}
	public AbstractHDLObject() {
		super();
		this.container=null;
	}
	protected HDLObject container;
	/**
	 * Get the container field.
	 * 
	 * @return the field
	 */
	public HDLObject getContainer(){
			return container;
	}
	protected HDLObject validateContainer(HDLObject container){
		return container;
	}
	public abstract HDLObject setContainer(HDLObject container);
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLObject))
			return false;
		AbstractHDLObject other = (AbstractHDLObject) obj;
		if (container == null) {
			if (other.container != null)
				return false;
		} else if (!container.equals(other.container))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = 1;
		final int prime = 31;
		result = prime * result + ((container == null) ? 0 : container.hashCode());
		return result;
	}
	public HDLVariable resolveVariable(HDLQualifiedName var) {
		return container.resolveVariable(var);
	}

	public HDLEnum resolveEnum(HDLQualifiedName hEnum) {
		return container.resolveEnum(hEnum);
	}

	public HDLType resolveType(HDLQualifiedName type) {
		return container.resolveType(type);
	}

	public HDLInterface resolveInterface(HDLQualifiedName hIf) {
		return container.resolveInterface(hIf);
	}
}