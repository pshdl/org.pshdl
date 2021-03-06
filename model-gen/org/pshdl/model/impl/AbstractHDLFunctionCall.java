/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLFunctionCall extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLFunctionCall}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param function
	 *            the value for function. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLFunctionCall(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName function, @Nullable Iterable<HDLExpression> params, boolean validate) {
		super(id, container, validate);
		if (validate) {
			function = validateFunction(function);
		}
		this.function = function;
		if (validate) {
			params = validateParams(params);
		}
		this.params = new ArrayList<>();
		if (params != null) {
			for (final HDLExpression newValue : params) {
				this.params.add(newValue);
			}
		}
	}

	public AbstractHDLFunctionCall() {
		super();
		this.function = null;
		this.params = new ArrayList<>();
	}

	protected final HDLQualifiedName function;

	public HDLFunction resolveFunctionForced(String stage) {
		final Optional<HDLFunction> opt = resolveFunction();
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new HDLCodeGenerationException(this, "failed to resolve:" + function, stage);
	}

	public Optional<HDLFunction> resolveFunction() {
		if (!frozen) {
			throw new IllegalArgumentException("Object not frozen");
		}
		return ScopingExtension.INST.resolveFunction(this, function);
	}

	public HDLQualifiedName getFunctionRefName() {
		return function;
	}

	protected HDLQualifiedName validateFunction(HDLQualifiedName function) {
		if (function == null) {
			throw new IllegalArgumentException("The field function can not be null!");
		}
		return function;
	}

	protected final ArrayList<HDLExpression> params;

	/**
	 * Get the params field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getParams() {
		return (ArrayList<HDLExpression>) params.clone();
	}

	protected Iterable<HDLExpression> validateParams(Iterable<HDLExpression> params) {
		if (params == null) {
			return new ArrayList<>();
		}
		return params;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLFunctionCall copy() {
		final HDLFunctionCall newObject = new HDLFunctionCall(id, null, function, params, false);
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
	public HDLFunctionCall copyFiltered(CopyFilter filter) {
		final HDLQualifiedName filteredfunction = filter.copyObject("function", this, function);
		final ArrayList<HDLExpression> filteredparams = filter.copyContainer("params", this, params);
		return filter.postFilter((HDLFunctionCall) this, new HDLFunctionCall(id, null, filteredfunction, filteredparams, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLFunctionCall copyDeepFrozen(IHDLObject container) {
		final HDLFunctionCall copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLFunctionCall} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLFunctionCall setContainer(@Nullable IHDLObject container) {
		return (HDLFunctionCall) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFunctionRefName()}.
	 *
	 * @param function
	 *            sets the new function of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionCall} with the updated function field.
	 */
	@Nonnull
	public HDLFunctionCall setFunction(@Nonnull HDLQualifiedName function) {
		function = validateFunction(function);
		final HDLFunctionCall res = new HDLFunctionCall(id, container, function, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParams()}.
	 *
	 * @param params
	 *            sets the new params of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionCall} with the updated params field.
	 */
	@Nonnull
	public HDLFunctionCall setParams(@Nullable Iterable<HDLExpression> params) {
		params = validateParams(params);
		final HDLFunctionCall res = new HDLFunctionCall(id, container, function, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getParams()}.
	 *
	 * @param newParams
	 *            the value that should be added to the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params field.
	 */
	@Nonnull
	public HDLFunctionCall addParams(@Nullable HDLExpression newParams) {
		if (newParams == null) {
			throw new IllegalArgumentException("Element of params can not be null!");
		}
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.add(newParams);
		final HDLFunctionCall res = new HDLFunctionCall(id, container, function, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 *
	 * @param newParams
	 *            the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params field.
	 */
	@Nonnull
	public HDLFunctionCall removeParams(@Nullable HDLExpression newParams) {
		if (newParams == null) {
			throw new IllegalArgumentException("Removed element of params can not be null!");
		}
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(newParams);
		final HDLFunctionCall res = new HDLFunctionCall(id, container, function, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunctionCall} with the updated params field.
	 */
	@Nonnull
	public HDLFunctionCall removeParams(int idx) {
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(idx);
		final HDLFunctionCall res = new HDLFunctionCall(id, container, function, params, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractHDLFunctionCall)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLFunctionCall other = (AbstractHDLFunctionCall) obj;
		if (function == null) {
			if (other.function != null) {
				return false;
			}
		} else if (!function.equals(other.function)) {
			return false;
		}
		if (params == null) {
			if (other.params != null) {
				return false;
			}
		} else if (!params.equals(other.params)) {
			return false;
		}
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null) {
			return hashCache;
		}
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((function == null) ? 0 : function.hashCode());
		result = (prime * result) + ((params == null) ? 0 : params.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLFunctionCall()");
		if (function != null) {
			sb.append(".setFunction(HDLQualifiedName.create(\"").append(function).append("\"))");
		}
		if (params != null) {
			if (params.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : params) {
					sb.append(".addParams(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateFunction(getFunctionRefName());
		if (checkResolve && (getFunctionRefName() != null)) {
			if (!resolveFunction().isPresent()) {
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getFunctionRefName()));
			}
		}
		validateParams(getParams());
		if (getParams() != null) {
			for (final HDLExpression o : getParams()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLFunctionCall, HDLClass.HDLStatement, HDLClass.HDLExpression, HDLClass.HDLObject);
	}

	@Override
	public Iterator<IHDLObject> deepIterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if ((params != null) && (params.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(params.size());
							for (final HDLExpression o : params) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}

	@Override
	public Iterator<IHDLObject> iterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if ((params != null) && (params.size() != 0)) {
							current = params.iterator();
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}
}