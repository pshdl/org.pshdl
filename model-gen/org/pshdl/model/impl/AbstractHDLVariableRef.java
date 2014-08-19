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
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLVariableRef extends HDLResolvedRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLVariableRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable Iterable<HDLExpression> array,
			@Nullable Iterable<HDLRange> bits, boolean validate) {
		super(id, container, var, validate);
		if (validate) {
			array = validateArray(array);
		}
		this.array = new ArrayList<HDLExpression>();
		if (array != null) {
			for (final HDLExpression newValue : array) {
				this.array.add(newValue);
			}
		}
		if (validate) {
			bits = validateBits(bits);
		}
		this.bits = new ArrayList<HDLRange>();
		if (bits != null) {
			for (final HDLRange newValue : bits) {
				this.bits.add(newValue);
			}
		}
	}

	public AbstractHDLVariableRef() {
		super();
		this.array = new ArrayList<HDLExpression>();
		this.bits = new ArrayList<HDLRange>();
	}

	protected final ArrayList<HDLExpression> array;

	/**
	 * Get the array field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getArray() {
		return (ArrayList<HDLExpression>) array.clone();
	}

	protected Iterable<HDLExpression> validateArray(Iterable<HDLExpression> array) {
		if (array == null)
			return new ArrayList<HDLExpression>();
		return array;
	}

	protected final ArrayList<HDLRange> bits;

	/**
	 * Get the bits field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLRange> getBits() {
		return (ArrayList<HDLRange>) bits.clone();
	}

	protected Iterable<HDLRange> validateBits(Iterable<HDLRange> bits) {
		if (bits == null)
			return new ArrayList<HDLRange>();
		return bits;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableRef copy() {
		final HDLVariableRef newObject = new HDLVariableRef(id, null, var, array, bits, false);
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
	public HDLVariableRef copyFiltered(CopyFilter filter) {
		final HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		final ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		final ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		return filter.postFilter((HDLVariableRef) this, new HDLVariableRef(id, null, filteredvar, filteredarray, filteredbits, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableRef copyDeepFrozen(IHDLObject container) {
		final HDLVariableRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLVariableRef} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLVariableRef setContainer(@Nullable IHDLObject container) {
		return (HDLVariableRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated var
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLVariableRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef setArray(@Nullable Iterable<HDLExpression> array) {
		array = validateArray(array);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeArray(int idx) {
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef setBits(@Nullable Iterable<HDLRange> bits) {
		bits = validateBits(bits);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeBits(int idx) {
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		final HDLVariableRef res = new HDLVariableRef(id, container, var, array, bits, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariableRef))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLVariableRef other = (AbstractHDLVariableRef) obj;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((array == null) ? 0 : array.hashCode());
		result = (prime * result) + ((bits == null) ? 0 : bits.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLVariableRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
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
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateArray(getArray());
		if (getArray() != null) {
			for (final HDLExpression o : getArray()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateBits(getBits());
		if (getBits() != null) {
			for (final HDLRange o : getBits()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLVariableRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
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