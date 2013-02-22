package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

@SuppressWarnings("all")
public abstract class AbstractHDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLObject}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLObject(@Nullable IHDLObject container, boolean validate) {
		if (container == this)
			throw new IllegalArgumentException("Object can not contain itself");
		if (validate) {
			container = validateContainer(container);
		}
		this.container = container;
	}

	public AbstractHDLObject() {
		super();
		this.container = null;
	}

	protected IHDLObject container;

	/**
	 * Get the container field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nullable
	IHDLObject getContainer() {
		return container;
	}

	protected IHDLObject validateContainer(IHDLObject container) {
		return container;
	}

	public abstract @Nonnull
	HDLObject setContainer(@Nullable IHDLObject container);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copyDeepFrozen(IHDLObject container);

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

	private static Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = 1;
		final int prime = 31;
		result = (prime * result) + ((container == null) ? 0 : container.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLObject()");
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		if (getContainer() != expectedParent)
			throw new IllegalArgumentException("This object " + this + " does not have the expected container! " + expectedParent);
		validateContainer(getContainer());
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLObject);
	}

	protected boolean frozen = false;
}