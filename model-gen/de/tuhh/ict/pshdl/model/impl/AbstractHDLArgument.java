package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLArgument extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLArgument(@Nullable IHDLObject container, @Nonnull String name, @Nonnull HDLExpression expression, boolean validate) {
		super(container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			expression = validateExpression(expression);
		}
		if (expression != null) {
			this.expression = expression;
		} else {
			this.expression = null;
		}
	}

	public AbstractHDLArgument() {
		super();
		this.name = null;
		this.expression = null;
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

	@Visit
	protected final HDLExpression expression;

	/**
	 * Get the expression field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLExpression getExpression() {
		return expression;
	}

	protected HDLExpression validateExpression(HDLExpression expression) {
		if (expression == null)
			throw new IllegalArgumentException("The field expression can not be null!");
		return expression;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArgument copy() {
		HDLArgument newObject = new HDLArgument(null, name, expression, false);
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
	public HDLArgument copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		HDLExpression filteredexpression = filter.copyObject("expression", this, expression);
		return filter.postFilter((HDLArgument) this, new HDLArgument(null, filteredname, filteredexpression, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArgument copyDeepFrozen(IHDLObject container) {
		HDLArgument copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLArgument} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLArgument setContainer(@Nullable IHDLObject container) {
		return (HDLArgument) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLArgument} with the updated name
	 *         field.
	 */
	@Nonnull
	public HDLArgument setName(@Nonnull String name) {
		name = validateName(name);
		HDLArgument res = new HDLArgument(container, name, expression, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getExpression()}.
	 * 
	 * @param expression
	 *            sets the new expression of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLArgument} with the updated expression
	 *         field.
	 */
	@Nonnull
	public HDLArgument setExpression(@Nonnull HDLExpression expression) {
		expression = validateExpression(expression);
		HDLArgument res = new HDLArgument(container, name, expression, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLArgument))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLArgument other = (AbstractHDLArgument) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
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
		result = (prime * result) + ((expression == null) ? 0 : expression.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLArgument()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (expression != null) {
			sb.append(".setExpression(").append(expression.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateExpression(getExpression());
		if (getExpression() != null) {
			getExpression().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLArgument, HDLClass.HDLObject);
	}
}