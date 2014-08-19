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
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceRef extends HDLVariableRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param ifArray
	 *            the value for ifArray. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterfaceRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable Iterable<HDLExpression> array,
			@Nullable Iterable<HDLRange> bits, @Nonnull HDLQualifiedName hIf, @Nullable Iterable<HDLExpression> ifArray, boolean validate) {
		super(id, container, var, array, bits, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		this.hIf = hIf;
		if (validate) {
			ifArray = validateIfArray(ifArray);
		}
		this.ifArray = new ArrayList<HDLExpression>();
		if (ifArray != null) {
			for (final HDLExpression newValue : ifArray) {
				this.ifArray.add(newValue);
			}
		}
	}

	public AbstractHDLInterfaceRef() {
		super();
		this.hIf = null;
		this.ifArray = new ArrayList<HDLExpression>();
	}

	protected final HDLQualifiedName hIf;

	@Nullable
	public Optional<HDLVariable> resolveHIf() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, hIf);
	}

	public HDLQualifiedName getHIfRefName() {
		return hIf;
	}

	protected HDLQualifiedName validateHIf(HDLQualifiedName hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}

	protected final ArrayList<HDLExpression> ifArray;

	/**
	 * Get the ifArray field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getIfArray() {
		return (ArrayList<HDLExpression>) ifArray.clone();
	}

	protected Iterable<HDLExpression> validateIfArray(Iterable<HDLExpression> ifArray) {
		if (ifArray == null)
			return new ArrayList<HDLExpression>();
		return ifArray;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef copy() {
		final HDLInterfaceRef newObject = new HDLInterfaceRef(id, null, var, array, bits, hIf, ifArray, false);
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
	public HDLInterfaceRef copyFiltered(CopyFilter filter) {
		final HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		final ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		final ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		final HDLQualifiedName filteredhIf = filter.copyObject("hIf", this, hIf);
		final ArrayList<HDLExpression> filteredifArray = filter.copyContainer("ifArray", this, ifArray);
		return filter.postFilter((HDLInterfaceRef) this, new HDLInterfaceRef(id, null, filteredvar, filteredarray, filteredbits, filteredhIf, filteredifArray, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef copyDeepFrozen(IHDLObject container) {
		final HDLInterfaceRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInterfaceRef} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef setContainer(@Nullable IHDLObject container) {
		return (HDLInterfaceRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated var
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef setArray(@Nullable Iterable<HDLExpression> array) {
		array = validateArray(array);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef removeArray(int idx) {
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef setBits(@Nullable Iterable<HDLRange> bits) {
		bits = validateBits(bits);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceRef removeBits(int idx) {
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated hIf
	 *         field.
	 */
	@Nonnull
	public HDLInterfaceRef setHIf(@Nonnull HDLQualifiedName hIf) {
		hIf = validateHIf(hIf);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIfArray()}.
	 * 
	 * @param ifArray
	 *            sets the new ifArray of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	@Nonnull
	public HDLInterfaceRef setIfArray(@Nullable Iterable<HDLExpression> ifArray) {
		ifArray = validateIfArray(ifArray);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getIfArray()}.
	 * 
	 * @param newIfArray
	 *            the value that should be added to the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	@Nonnull
	public HDLInterfaceRef addIfArray(@Nullable HDLExpression newIfArray) {
		if (newIfArray == null)
			throw new IllegalArgumentException("Element of ifArray can not be null!");
		final ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.add(newIfArray);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getIfArray()}.
	 * 
	 * @param newIfArray
	 *            the value that should be removed from the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	@Nonnull
	public HDLInterfaceRef removeIfArray(@Nullable HDLExpression newIfArray) {
		if (newIfArray == null)
			throw new IllegalArgumentException("Removed element of ifArray can not be null!");
		final ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.remove(newIfArray);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getIfArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	@Nonnull
	public HDLInterfaceRef removeIfArray(int idx) {
		final ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.remove(idx);
		final HDLInterfaceRef res = new HDLInterfaceRef(id, container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceRef))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLInterfaceRef other = (AbstractHDLInterfaceRef) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		if (ifArray == null) {
			if (other.ifArray != null)
				return false;
		} else if (!ifArray.equals(other.ifArray))
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
		result = (prime * result) + ((hIf == null) ? 0 : hIf.hashCode());
		result = (prime * result) + ((ifArray == null) ? 0 : ifArray.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterfaceRef()");
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
		if (hIf != null) {
			sb.append(".setHIf(HDLQualifiedName.create(\"").append(hIf).append("\"))");
		}
		if (ifArray != null) {
			if (ifArray.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : ifArray) {
					sb.append(".addIfArray(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHIf(getHIfRefName());
		if (checkResolve && (getHIfRefName() != null))
			if (!resolveHIf().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHIfRefName()));
		validateIfArray(getIfArray());
		if (getIfArray() != null) {
			for (final HDLExpression o : getIfArray()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterfaceRef, HDLClass.HDLVariableRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if ((ifArray != null) && (ifArray.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(ifArray.size());
							for (final HDLExpression o : ifArray) {
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
						if ((ifArray != null) && (ifArray.size() != 0)) {
							current = ifArray.iterator();
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