/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
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

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.utils.*;

import com.google.common.collect.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnum extends HDLValueType {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnum(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull Iterable<HDLVariable> enums, boolean validate) {
		super(container, name, dim, validate);
		if (validate) {
			enums = validateEnums(enums);
		}
		this.enums = new ArrayList<HDLVariable>();
		if (enums != null) {
			for (HDLVariable newValue : enums) {
				this.enums.add(newValue);
			}
		}
	}

	public AbstractHDLEnum() {
		super();
		this.enums = new ArrayList<HDLVariable>();
	}

	protected final ArrayList<HDLVariable> enums;

	/**
	 * Get the enums field. Can <b>not</b> be <code>null</code>, additionally
	 * the collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLVariable> getEnums() {
		return (ArrayList<HDLVariable>) enums.clone();
	}

	protected Iterable<HDLVariable> validateEnums(Iterable<HDLVariable> enums) {
		if (enums == null)
			throw new IllegalArgumentException("The field enums can not be null!");
		if (!enums.iterator().hasNext())
			throw new IllegalArgumentException("The field enums must contain at least one item!");
		return enums;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnum copy() {
		HDLEnum newObject = new HDLEnum(null, name, dim, enums, false);
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
	public HDLEnum copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		ArrayList<HDLVariable> filteredenums = filter.copyContainer("enums", this, enums);
		return filter.postFilter((HDLEnum) this, new HDLEnum(null, filteredname, filtereddim, filteredenums, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnum copyDeepFrozen(IHDLObject container) {
		HDLEnum copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLEnum} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLEnum setContainer(@Nullable IHDLObject container) {
		return (HDLEnum) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnum} with the updated name field.
	 */
	@Override
	@Nonnull
	public HDLEnum setName(@Nonnull String name) {
		name = validateName(name);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDim()}.
	 * 
	 * @param dim
	 *            sets the new dim of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLEnum} with the updated dim field.
	 */
	@Override
	@Nonnull
	public HDLEnum setDim(@Nullable Iterable<HDLExpression> dim) {
		dim = validateDim(dim);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be added to the field {@link #getDim()}
	 * @return a new instance of {@link HDLEnum} with the updated dim field.
	 */
	@Override
	@Nonnull
	public HDLEnum addDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Element of dim can not be null!");
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLEnum} with the updated dim field.
	 */
	@Override
	@Nonnull
	public HDLEnum removeDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Removed element of dim can not be null!");
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLEnum} with the updated dim field.
	 */
	@Nonnull
	public HDLEnum removeDim(int idx) {
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getEnums()}.
	 * 
	 * @param enums
	 *            sets the new enums of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	@Nonnull
	public HDLEnum setEnums(@Nonnull Iterable<HDLVariable> enums) {
		enums = validateEnums(enums);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be added to the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	@Nonnull
	public HDLEnum addEnums(@Nonnull HDLVariable newEnums) {
		if (newEnums == null)
			throw new IllegalArgumentException("Element of enums can not be null!");
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.add(newEnums);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be removed from the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	@Nonnull
	public HDLEnum removeEnums(@Nonnull HDLVariable newEnums) {
		if (newEnums == null)
			throw new IllegalArgumentException("Removed element of enums can not be null!");
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.remove(newEnums);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	@Nonnull
	public HDLEnum removeEnums(int idx) {
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.remove(idx);
		HDLEnum res = new HDLEnum(container, name, dim, enums, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnum))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnum other = (AbstractHDLEnum) obj;
		if (enums == null) {
			if (other.enums != null)
				return false;
		} else if (!enums.equals(other.enums))
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
		result = (prime * result) + ((enums == null) ? 0 : enums.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnum()");
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
		if (enums != null) {
			if (enums.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLVariable o : enums) {
					sb.append(".addEnums(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateEnums(getEnums());
		if (getEnums() != null) {
			for (HDLVariable o : getEnums()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnum, HDLClass.HDLValueType, HDLClass.HDLType, HDLClass.HDLObject);
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
						if ((dim != null) && (dim.size() != 0)) {
							List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(dim.size());
							for (HDLExpression o : dim) {
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if ((enums != null) && (enums.size() != 0)) {
							List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(enums.size());
							for (HDLVariable o : enums) {
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
						if ((dim != null) && (dim.size() != 0)) {
							current = dim.iterator();
						}
						break;
					case 1:
						if ((enums != null) && (enums.size() != 0)) {
							current = enums.iterator();
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