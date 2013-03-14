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
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLPrimitive(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull HDLPrimitiveType type,
			@Nullable HDLExpression width, boolean validate) {
		super(container, name, dim, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			width = validateWidth(width);
		}
		if (width != null) {
			this.width = width;
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
	@Nonnull
	public HDLPrimitiveType getType() {
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
	@Override
	@Nullable
	public HDLExpression getWidth() {
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
	@Override
	@Nonnull
	public HDLPrimitive copy() {
		HDLPrimitive newObject = new HDLPrimitive(null, name, dim, type, width, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLPrimitive copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		HDLPrimitiveType filteredtype = filter.copyObject("type", this, type);
		HDLExpression filteredwidth = filter.copyObject("width", this, width);
		return filter.postFilter((HDLPrimitive) this, new HDLPrimitive(null, filteredname, filtereddim, filteredtype, filteredwidth, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
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
	@Override
	@Nonnull
	public HDLPrimitive setContainer(@Nullable IHDLObject container) {
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
	@Override
	@Nonnull
	public HDLPrimitive setName(@Nonnull String name) {
		name = validateName(name);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDim()}.
	 * 
	 * @param dim
	 *            sets the new dim of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLPrimitive} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLPrimitive setDim(@Nullable Iterable<HDLExpression> dim) {
		dim = validateDim(dim);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be added to the field {@link #getDim()}
	 * @return a new instance of {@link HDLPrimitive} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLPrimitive addDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Element of dim can not be null!");
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLPrimitive} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLPrimitive removeDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Removed element of dim can not be null!");
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLPrimitive} with the updated dim
	 *         field.
	 */
	@Nonnull
	public HDLPrimitive removeDim(int idx) {
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
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
	@Nonnull
	public HDLPrimitive setType(@Nonnull HDLPrimitiveType type) {
		type = validateType(type);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
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
	@Nonnull
	public HDLPrimitive setWidth(@Nullable HDLExpression width) {
		width = validateWidth(width);
		HDLPrimitive res = new HDLPrimitive(container, name, dim, type, width, false);
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

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLPrimitive()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (dim != null) {
			if (dim.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : dim) {
					sb.append(".addDim(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLPrimitiveType.").append(type.name() + ")");
		}
		if (width != null) {
			sb.append(".setWidth(").append(width.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateType(getType());
		validateWidth(getWidth());
		if (getWidth() != null) {
			getWidth().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLPrimitive, HDLClass.HDLValueType, HDLClass.HDLType, HDLClass.HDLObject);
	}
}