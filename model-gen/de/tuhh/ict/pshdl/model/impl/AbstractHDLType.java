package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLType extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLType(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			dim = validateDim(dim);
		}
		this.dim = new ArrayList<HDLExpression>();
		if (dim != null) {
			for (HDLExpression newValue : dim) {
				this.dim.add(newValue);
			}
		}
	}

	public AbstractHDLType() {
		super();
		this.name = null;
		this.dim = new ArrayList<HDLExpression>();
	}

	protected final String name;

	/**
	 * Get the name field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	protected String validateName(String name) {
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}

	@Visit
	protected final ArrayList<HDLExpression> dim;

	/**
	 * Get the dim field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getDim() {
		return (ArrayList<HDLExpression>) dim.clone();
	}

	protected Iterable<HDLExpression> validateDim(Iterable<HDLExpression> dim) {
		if (dim == null)
			return new ArrayList<HDLExpression>();
		return dim;
	}

	@Nonnull
	public abstract HDLType setName(@Nonnull String name);

	@Nonnull
	public abstract HDLType setDim(@Nullable Iterable<HDLExpression> dim);

	@Nonnull
	public abstract HDLType addDim(@Nullable HDLExpression dim);

	@Nonnull
	public abstract HDLType removeDim(@Nullable HDLExpression dim);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLType copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLType copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLType copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLType))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLType other = (AbstractHDLType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (dim == null) {
			if (other.dim != null)
				return false;
		} else if (!dim.equals(other.dim))
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
		result = (prime * result) + ((dim == null) ? 0 : dim.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLType()");
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
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateDim(getDim());
		if (getDim() != null) {
			for (HDLExpression o : getDim()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLType, HDLClass.HDLObject);
	}
}