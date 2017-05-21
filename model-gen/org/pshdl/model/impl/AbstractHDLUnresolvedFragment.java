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
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLUnresolvedFragment extends HDLReference {
	/**
	 * Constructs a new instance of {@link AbstractHDLUnresolvedFragment}
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
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLUnresolvedFragment(int id, @Nullable IHDLObject container, @Nonnull String frag, @Nonnull Boolean isStatement, @Nullable Iterable<HDLExpression> array,
			@Nullable Iterable<HDLRange> bits, @Nullable HDLUnresolvedFragment sub, boolean validate) {
		super(id, container, validate);
		if (validate) {
			frag = validateFrag(frag);
		}
		this.frag = frag;
		if (validate) {
			isStatement = validateIsStatement(isStatement);
		}
		this.isStatement = isStatement;
		if (validate) {
			array = validateArray(array);
		}
		this.array = new ArrayList<>();
		if (array != null) {
			for (final HDLExpression newValue : array) {
				this.array.add(newValue);
			}
		}
		if (validate) {
			bits = validateBits(bits);
		}
		this.bits = new ArrayList<>();
		if (bits != null) {
			for (final HDLRange newValue : bits) {
				this.bits.add(newValue);
			}
		}
		if (validate) {
			sub = validateSub(sub);
		}
		if (sub != null) {
			this.sub = sub;
		} else {
			this.sub = null;
		}
	}

	public AbstractHDLUnresolvedFragment() {
		super();
		this.frag = null;
		this.isStatement = null;
		this.array = new ArrayList<>();
		this.bits = new ArrayList<>();
		this.sub = null;
	}

	protected final String frag;

	/**
	 * Get the frag field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getFrag() {
		return frag;
	}

	protected String validateFrag(String frag) {
		if (frag == null) {
			throw new IllegalArgumentException("The field frag can not be null!");
		}
		return frag;
	}

	protected final Boolean isStatement;

	/**
	 * Get the isStatement field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public Boolean getIsStatement() {
		return isStatement;
	}

	protected Boolean validateIsStatement(Boolean isStatement) {
		if (isStatement == null) {
			throw new IllegalArgumentException("The field isStatement can not be null!");
		}
		return isStatement;
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
		if (array == null) {
			return new ArrayList<>();
		}
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
		if (bits == null) {
			return new ArrayList<>();
		}
		return bits;
	}

	protected final HDLUnresolvedFragment sub;

	/**
	 * Get the sub field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLUnresolvedFragment getSub() {
		return sub;
	}

	protected HDLUnresolvedFragment validateSub(HDLUnresolvedFragment sub) {
		return sub;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment copy() {
		final HDLUnresolvedFragment newObject = new HDLUnresolvedFragment(id, null, frag, isStatement, array, bits, sub, false);
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
	public HDLUnresolvedFragment copyFiltered(CopyFilter filter) {
		final String filteredfrag = filter.copyObject("frag", this, frag);
		final Boolean filteredisStatement = filter.copyObject("isStatement", this, isStatement);
		final ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		final ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		final HDLUnresolvedFragment filteredsub = filter.copyObject("sub", this, sub);
		return filter.postFilter((HDLUnresolvedFragment) this,
				new HDLUnresolvedFragment(id, null, filteredfrag, filteredisStatement, filteredarray, filteredbits, filteredsub, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment copyDeepFrozen(IHDLObject container) {
		final HDLUnresolvedFragment copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLUnresolvedFragment} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment setContainer(@Nullable IHDLObject container) {
		return (HDLUnresolvedFragment) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFrag()}.
	 *
	 * @param frag
	 *            sets the new frag of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated frag field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setFrag(@Nonnull String frag) {
		frag = validateFrag(frag);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIsStatement()}.
	 *
	 * @param isStatement
	 *            sets the new isStatement of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated isStatement field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setIsStatement(@Nonnull Boolean isStatement) {
		isStatement = validateIsStatement(isStatement);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIsStatement()}.
	 *
	 * @param isStatement
	 *            sets the new isStatement of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated isStatement field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setIsStatement(boolean isStatement) {
		isStatement = validateIsStatement(isStatement);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 *
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setArray(@Nullable Iterable<HDLExpression> array) {
		array = validateArray(array);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 *
	 * @param newArray
	 *            the value that should be added to the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment addArray(@Nullable HDLExpression newArray) {
		if (newArray == null) {
			throw new IllegalArgumentException("Element of array can not be null!");
		}
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 *
	 * @param newArray
	 *            the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null) {
			throw new IllegalArgumentException("Removed element of array can not be null!");
		}
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeArray(int idx) {
		final ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 *
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setBits(@Nullable Iterable<HDLRange> bits) {
		bits = validateBits(bits);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 *
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment addBits(@Nullable HDLRange newBits) {
		if (newBits == null) {
			throw new IllegalArgumentException("Element of bits can not be null!");
		}
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 *
	 * @param newBits
	 *            the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeBits(@Nullable HDLRange newBits) {
		if (newBits == null) {
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		}
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeBits(int idx) {
		final ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSub()}.
	 *
	 * @param sub
	 *            sets the new sub of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated sub field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setSub(@Nullable HDLUnresolvedFragment sub) {
		sub = validateSub(sub);
		final HDLUnresolvedFragment res = new HDLUnresolvedFragment(id, container, frag, isStatement, array, bits, sub, false);
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
		if (!(obj instanceof AbstractHDLUnresolvedFragment)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLUnresolvedFragment other = (AbstractHDLUnresolvedFragment) obj;
		if (frag == null) {
			if (other.frag != null) {
				return false;
			}
		} else if (!frag.equals(other.frag)) {
			return false;
		}
		if (isStatement == null) {
			if (other.isStatement != null) {
				return false;
			}
		} else if (!isStatement.equals(other.isStatement)) {
			return false;
		}
		if (array == null) {
			if (other.array != null) {
				return false;
			}
		} else if (!array.equals(other.array)) {
			return false;
		}
		if (bits == null) {
			if (other.bits != null) {
				return false;
			}
		} else if (!bits.equals(other.bits)) {
			return false;
		}
		if (sub == null) {
			if (other.sub != null) {
				return false;
			}
		} else if (!sub.equals(other.sub)) {
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
		result = (prime * result) + ((frag == null) ? 0 : frag.hashCode());
		result = (prime * result) + ((isStatement == null) ? 0 : isStatement.hashCode());
		result = (prime * result) + ((array == null) ? 0 : array.hashCode());
		result = (prime * result) + ((bits == null) ? 0 : bits.hashCode());
		result = (prime * result) + ((sub == null) ? 0 : sub.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnresolvedFragment()");
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
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateFrag(getFrag());
		validateIsStatement(getIsStatement());
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
		validateSub(getSub());
		if (getSub() != null) {
			getSub().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnresolvedFragment, HDLClass.HDLStatement, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
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