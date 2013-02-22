package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

@SuppressWarnings("all")
public abstract class AbstractHDLAnnotation extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLAnnotation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLAnnotation(@Nullable IHDLObject container, @Nonnull String name, @Nullable String value, boolean validate) {
		super(container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			value = validateValue(value);
		}
		this.value = value;
	}

	public AbstractHDLAnnotation() {
		super();
		this.name = null;
		this.value = null;
	}

	protected final String name;

	/**
	 * Get the name field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	String getName() {
		return name;
	}

	protected String validateName(String name) {
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}

	protected final String value;

	/**
	 * Get the value field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nullable
	String getValue() {
		return value;
	}

	protected String validateValue(String value) {
		return value;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLAnnotation copy() {
		HDLAnnotation newObject = new HDLAnnotation(null, name, value, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLAnnotation copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		String filteredvalue = filter.copyObject("value", this, value);
		return filter.postFilter((HDLAnnotation) this, new HDLAnnotation(null, filteredname, filteredvalue, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLAnnotation copyDeepFrozen(IHDLObject container) {
		HDLAnnotation copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLAnnotation} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLAnnotation setContainer(@Nullable IHDLObject container) {
		return (HDLAnnotation) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLAnnotation} with the updated name
	 *         field.
	 */
	public @Nonnull
	HDLAnnotation setName(@Nonnull String name) {
		name = validateName(name);
		HDLAnnotation res = new HDLAnnotation(container, name, value, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getValue()}.
	 * 
	 * @param value
	 *            sets the new value of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLAnnotation} with the updated value
	 *         field.
	 */
	public @Nonnull
	HDLAnnotation setValue(@Nullable String value) {
		value = validateValue(value);
		HDLAnnotation res = new HDLAnnotation(container, name, value, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLAnnotation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLAnnotation other = (AbstractHDLAnnotation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	private static Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((value == null) ? 0 : value.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLAnnotation()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (value != null) {
			sb.append(".setValue(").append('"' + value + '"').append(")");
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateValue(getValue());
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLAnnotation, HDLClass.HDLObject);
	}
}