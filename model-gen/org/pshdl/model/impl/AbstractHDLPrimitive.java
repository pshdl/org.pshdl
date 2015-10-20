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
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.HDLValueType;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLPrimitive extends HDLValueType {
	/**
	 * Constructs a new instance of {@link AbstractHDLPrimitive}
	 *
	 * @param id
	 *            a unique number for each instance
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
	public AbstractHDLPrimitive(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nonnull HDLPrimitiveType type,
			@Nullable HDLExpression width, boolean validate) {
		super(id, container, name, dim, validate);
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
	@Override
	@Nonnull
	public HDLPrimitiveType getType() {
		return type;
	}

	protected HDLPrimitiveType validateType(HDLPrimitiveType type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

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
		final HDLPrimitive newObject = new HDLPrimitive(id, null, name, dim, type, width, false);
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
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		final HDLPrimitiveType filteredtype = filter.copyObject("type", this, type);
		final HDLExpression filteredwidth = filter.copyObject("width", this, width);
		return filter.postFilter((HDLPrimitive) this, new HDLPrimitive(id, null, filteredname, filtereddim, filteredtype, filteredwidth, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLPrimitive copyDeepFrozen(IHDLObject container) {
		final HDLPrimitive copy = copyFiltered(CopyFilter.DEEP_META);
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
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final HDLPrimitive res = new HDLPrimitive(id, container, name, dim, type, width, false);
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
		final AbstractHDLPrimitive other = (AbstractHDLPrimitive) obj;
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

	private Integer hashCache;

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
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLPrimitive()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (dim != null) {
			if (dim.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : dim) {
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
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(dim.size());
							for (final HDLExpression o : dim) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if (width != null) {
							current = Iterators.concat(Iterators.forArray(width), width.deepIterator());
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
						if (width != null) {
							current = Iterators.singletonIterator(width);
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