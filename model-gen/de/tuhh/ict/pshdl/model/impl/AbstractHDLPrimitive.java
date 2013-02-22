package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLPrimitive extends HDLValueType {
	/**
	 * Constructs a new instance of {@link AbstractHDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLPrimitive(@Nullable IHDLObject container, @Nonnull String name, @Nonnull HDLPrimitiveType type, @Nullable HDLExpression width, boolean validate) {
		super(container, name, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			width = validateWidth(width);
		}
		if (width != null) {
			this.width = (HDLExpression) width;
		} else {
			this.width = null;
		}
	}

	public AbstractHDLPrimitive() {
		super();
		this.type = null;
		this.width = null;
	}

	protected final HDLPrimitiveType type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLPrimitiveType getType() {
		return type;
	}

	protected HDLPrimitiveType validateType(HDLPrimitiveType type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

	@Visit
	protected final HDLExpression width;

	/**
	 * Get the width field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nullable
	HDLExpression getWidth() {
		return width;
	}

	protected HDLExpression validateWidth(HDLExpression width) {
		return width;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLPrimitive copy() {
		HDLPrimitive newObject = new HDLPrimitive(null, name, type, width, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLPrimitive copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		HDLPrimitiveType filteredtype = filter.copyObject("type", this, type);
		HDLExpression filteredwidth = filter.copyObject("width", this, width);
		return filter.postFilter((HDLPrimitive) this, new HDLPrimitive(null, filteredname, filteredtype, filteredwidth, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLPrimitive copyDeepFrozen(IHDLObject container) {
		HDLPrimitive copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLPrimitive} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLPrimitive setContainer(@Nullable IHDLObject container) {
		return (HDLPrimitive) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLPrimitive} with the updated name
	 *         field.
	 */
	public @Nonnull
	HDLPrimitive setName(@Nonnull String name) {
		name = validateName(name);
		HDLPrimitive res = new HDLPrimitive(container, name, type, width, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLPrimitive} with the updated type
	 *         field.
	 */
	public @Nonnull
	HDLPrimitive setType(@Nonnull HDLPrimitiveType type) {
		type = validateType(type);
		HDLPrimitive res = new HDLPrimitive(container, name, type, width, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getWidth()}.
	 * 
	 * @param width
	 *            sets the new width of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLPrimitive} with the updated width
	 *         field.
	 */
	public @Nonnull
	HDLPrimitive setWidth(@Nullable HDLExpression width) {
		width = validateWidth(width);
		HDLPrimitive res = new HDLPrimitive(container, name, type, width, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLPrimitive))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLPrimitive other = (AbstractHDLPrimitive) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
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
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((width == null) ? 0 : width.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLPrimitive()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLPrimitiveType.").append(type.name() + ")");
		}
		if (width != null) {
			sb.append(".setWidth(").append(width.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateType(getType());
		validateWidth(getWidth());
		if (getWidth() != null) {
			getWidth().validateAllFields(this, checkResolve);
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLPrimitive, HDLClass.HDLValueType, HDLClass.HDLType, HDLClass.HDLObject);
	}
}