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
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLUnresolvedFragmentFunction;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLUnresolvedFragmentFunction extends HDLUnresolvedFragment {
	/**
	 * Constructs a new instance of {@link AbstractHDLUnresolvedFragmentFunction}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param frag
	 *            the value for frag. Can <b>not</b> be <code>null</code>.
	 * @param isStatement
	 *            the value for isStatement. Can <b>not</b> be <code>null</code> .
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param sub
	 *            the value for sub. Can be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLUnresolvedFragmentFunction(int id, @Nullable IHDLObject container, @Nonnull String frag, @Nonnull Boolean isStatement,
			@Nullable Iterable<HDLExpression> array, @Nullable Iterable<HDLRange> bits, @Nullable HDLUnresolvedFragment sub, @Nullable Iterable<HDLExpression> params,
			boolean validate) {
		super(id, container, frag, isStatement, array, bits, sub, validate);
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

	public AbstractHDLUnresolvedFragmentFunction() {
		super();
		this.params = new ArrayList<>();
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
	public HDLUnresolvedFragmentFunction copy() {
		final HDLUnresolvedFragmentFunction newObject = new HDLUnresolvedFragmentFunction(id, null, frag, isStatement, array, bits, sub, params, false);
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
	public HDLUnresolvedFragmentFunction copyFiltered(CopyFilter filter) {
		final String filteredfrag = filter.copyObject("frag", this, frag);
		final Boolean filteredisStatement = filter.copyObject("isStatement", this, isStatement);
		final ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		final ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		final HDLUnresolvedFragment filteredsub = filter.copyObject("sub", this, sub);
		final ArrayList<HDLExpression> filteredparams = filter.copyContainer("params", this, params);
		return filter.postFilter((HDLUnresolvedFragmentFunction) this,
				new HDLUnresolvedFragmentFunction(id, null, filteredfrag, filteredisStatement, filteredarray, filteredbits, filteredsub, filteredparams, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction copyDeepFrozen(IHDLObject container) {
		final HDLUnresolvedFragmentFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLUnresolvedFragmentFunction} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setContainer(@Nullable IHDLObject container) {
		return (HDLUnresolvedFragmentFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFrag()}.
	 *
	 * @param frag
	 *            sets the new frag of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated frag field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setFrag(@Nonnull String frag) {
		frag = validateFrag(frag);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIsStatement()}.
	 *
	 * @param isStatement
	 *            sets the new isStatement of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated isStatement field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setIsStatement(@Nonnull Boolean isStatement) {
		isStatement = validateIsStatement(isStatement);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIsStatement()}.
	 *
	 * @param isStatement
	 *            sets the new isStatement of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated isStatement field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setIsStatement(boolean isStatement) {
		isStatement = validateIsStatement(isStatement);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 *
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setArray(@Nullable Iterable<HDLExpression> array) {
		array = validateArray(array);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 *
	 * @param newArray
	 *            the value that should be added to the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction addArray(@Nullable HDLExpression newArray) {
		if (newArray == null) {
			throw new IllegalArgumentException("Element of array can not be null!");
		}
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 *
	 * @param newArray
	 *            the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null) {
			throw new IllegalArgumentException("Removed element of array can not be null!");
		}
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeArray(int idx) {
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 *
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setBits(@Nullable Iterable<HDLRange> bits) {
		bits = validateBits(bits);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 *
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction addBits(@Nullable HDLRange newBits) {
		if (newBits == null) {
			throw new IllegalArgumentException("Element of bits can not be null!");
		}
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 *
	 * @param newBits
	 *            the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeBits(@Nullable HDLRange newBits) {
		if (newBits == null) {
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		}
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeBits(int idx) {
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSub()}.
	 *
	 * @param sub
	 *            sets the new sub of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated sub field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setSub(@Nullable HDLUnresolvedFragment sub) {
		sub = validateSub(sub);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParams()}.
	 *
	 * @param params
	 *            sets the new params of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction setParams(@Nullable Iterable<HDLExpression> params) {
		params = validateParams(params);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getParams()}.
	 *
	 * @param newParams
	 *            the value that should be added to the field {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction addParams(@Nullable HDLExpression newParams) {
		if (newParams == null) {
			throw new IllegalArgumentException("Element of params can not be null!");
		}
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.add(newParams);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 *
	 * @param newParams
	 *            the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction removeParams(@Nullable HDLExpression newParams) {
		if (newParams == null) {
			throw new IllegalArgumentException("Removed element of params can not be null!");
		}
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(newParams);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction removeParams(int idx) {
		final ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(idx);
		final HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(id, container, frag, isStatement, array, bits, sub, params, false);
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
		if (!(obj instanceof AbstractHDLUnresolvedFragmentFunction)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLUnresolvedFragmentFunction other = (AbstractHDLUnresolvedFragmentFunction) obj;
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
		result = (prime * result) + ((params == null) ? 0 : params.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnresolvedFragmentFunction()");
		if (frag != null) {
			sb.append(".setFrag(").append('"' + frag + '"').append(")");
		}
		if (isStatement != null) {
			sb.append(".setIsStatement(").append(isStatement).append(")");
		}
		if (array != null) {
			if (array.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : array) {
					sb.append(".addArray(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (bits != null) {
			if (bits.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLRange o : bits) {
					sb.append(".addBits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (sub != null) {
			sb.append(".setSub(").append(sub.toConstructionString(spacing + "\t")).append(")");
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
		validateParams(getParams());
		if (getParams() != null) {
			for (final HDLExpression o : getParams()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnresolvedFragmentFunction, HDLClass.HDLUnresolvedFragment, HDLClass.HDLStatement, HDLClass.HDLReference, HDLClass.HDLExpression,
				HDLClass.HDLObject);
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
						if ((array != null) && (array.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(array.size());
							for (final HDLExpression o : array) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if ((bits != null) && (bits.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(bits.size());
							for (final HDLRange o : bits) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 2:
						if (sub != null) {
							current = Iterators.concat(Iterators.forArray(sub), sub.deepIterator());
						}
						break;
					case 3:
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
						if ((array != null) && (array.size() != 0)) {
							current = array.iterator();
						}
						break;
					case 1:
						if ((bits != null) && (bits.size() != 0)) {
							current = bits.iterator();
						}
						break;
					case 2:
						if (sub != null) {
							current = Iterators.singletonIterator(sub);
						}
						break;
					case 3:
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