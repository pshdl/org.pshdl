package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

@SuppressWarnings("all")
public abstract class AbstractHDLFunctionCall extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLFunctionCall}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLFunctionCall(@Nullable IHDLObject container, @Nonnull HDLQualifiedName name, @Nullable ArrayList<HDLExpression> params, boolean validate) {
		super(container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			params = validateParams(params);
		}
		this.params = new ArrayList<HDLExpression>();
		if (params != null) {
			for (HDLExpression newValue : params) {
				this.params.add((HDLExpression) newValue);
			}
		}
	}

	public AbstractHDLFunctionCall() {
		super();
		this.name = null;
		this.params = new ArrayList<HDLExpression>();
	}

	protected final HDLQualifiedName name;

	@Nullable
	public HDLFunction resolveName() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveFunction(this, name);
	}

	public HDLQualifiedName getNameRefName() {
		return name;
	}

	protected HDLQualifiedName validateName(HDLQualifiedName name) {
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}

	@Visit
	protected final ArrayList<HDLExpression> params;

	/**
	 * Get the params field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLExpression> getParams() {
		return (ArrayList<HDLExpression>) params.clone();
	}

	protected ArrayList<HDLExpression> validateParams(ArrayList<HDLExpression> params) {
		if (params == null)
			return new ArrayList<HDLExpression>();
		return params;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLFunctionCall copy() {
		HDLFunctionCall newObject = new HDLFunctionCall(null, name, params, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLFunctionCall copyFiltered(CopyFilter filter) {
		HDLQualifiedName filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLExpression> filteredparams = filter.copyContainer("params", this, params);
		return filter.postFilter((HDLFunctionCall) this, new HDLFunctionCall(null, filteredname, filteredparams, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLFunctionCall copyDeepFrozen(IHDLObject container) {
		HDLFunctionCall copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLFunctionCall} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLFunctionCall setContainer(@Nullable IHDLObject container) {
		return (HDLFunctionCall) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLFunctionCall} with the updated name
	 *         field.
	 */
	public @Nonnull
	HDLFunctionCall setName(@Nonnull HDLQualifiedName name) {
		name = validateName(name);
		HDLFunctionCall res = new HDLFunctionCall(container, name, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParams()}.
	 * 
	 * @param params
	 *            sets the new params of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionCall} with the updated params
	 *         field.
	 */
	public @Nonnull
	HDLFunctionCall setParams(@Nullable ArrayList<HDLExpression> params) {
		params = validateParams(params);
		HDLFunctionCall res = new HDLFunctionCall(container, name, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be added to the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params
	 *         field.
	 */
	public @Nonnull
	HDLFunctionCall addParams(@Nullable HDLExpression newParams) {
		if (newParams == null)
			throw new IllegalArgumentException("Element of params can not be null!");
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.add(newParams);
		HDLFunctionCall res = new HDLFunctionCall(container, name, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be removed from the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params
	 *         field.
	 */
	public @Nonnull
	HDLFunctionCall removeParams(@Nullable HDLExpression newParams) {
		if (newParams == null)
			throw new IllegalArgumentException("Removed element of params can not be null!");
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(newParams);
		HDLFunctionCall res = new HDLFunctionCall(container, name, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params
	 *         field.
	 */
	public @Nonnull
	HDLFunctionCall removeParams(int idx) {
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(idx);
		HDLFunctionCall res = new HDLFunctionCall(container, name, params, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLFunctionCall))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLFunctionCall other = (AbstractHDLFunctionCall) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
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
		result = (prime * result) + ((params == null) ? 0 : params.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLFunctionCall()");
		if (name != null) {
			sb.append(".setName(HDLQualifiedName.create(\"").append(name).append("\"))");
		}
		if (params != null) {
			if (params.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : params) {
					sb.append(".addParams(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getNameRefName());
		if (checkResolve && (getNameRefName() != null))
			if (resolveName() == null)
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getNameRefName()));
		validateParams(getParams());
		if (getParams() != null) {
			for (HDLExpression o : getParams()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLFunctionCall, HDLClass.HDLStatement, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}