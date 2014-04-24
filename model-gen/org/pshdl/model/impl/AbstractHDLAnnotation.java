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

import java.util.EnumSet;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

@SuppressWarnings("all")
public abstract class AbstractHDLAnnotation extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLAnnotation}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLAnnotation(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable String value, boolean validate) {
		super(id, container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			value = validateValue(value);
		}
		this.value = value;
	}

	public AbstractHDLAnnotation() {
		super();
		this.name = null;
		this.value = null;
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

	protected final String value;

	/**
	 * Get the value field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public String getValue() {
		return value;
	}

	protected String validateValue(String value) {
		return value;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAnnotation copy() {
		final HDLAnnotation newObject = new HDLAnnotation(id, null, name, value, false);
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
	public HDLAnnotation copyFiltered(CopyFilter filter) {
		final String filteredname = filter.copyObject("name", this, name);
		final String filteredvalue = filter.copyObject("value", this, value);
		return filter.postFilter((HDLAnnotation) this, new HDLAnnotation(id, null, filteredname, filteredvalue, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAnnotation copyDeepFrozen(IHDLObject container) {
		final HDLAnnotation copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLAnnotation} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLAnnotation setContainer(@Nullable IHDLObject container) {
		return (HDLAnnotation) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 *
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLAnnotation} with the updated name
	 *         field.
	 */
	@Nonnull
	public HDLAnnotation setName(@Nonnull String name) {
		name = validateName(name);
		final HDLAnnotation res = new HDLAnnotation(id, container, name, value, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getValue()}.
	 *
	 * @param value
	 *            sets the new value of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLAnnotation} with the updated value
	 *         field.
	 */
	@Nonnull
	public HDLAnnotation setValue(@Nullable String value) {
		value = validateValue(value);
		final HDLAnnotation res = new HDLAnnotation(id, container, name, value, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLAnnotation))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLAnnotation other = (AbstractHDLAnnotation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
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
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((value == null) ? 0 : value.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLAnnotation()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (value != null) {
			sb.append(".setValue(").append('"' + value + '"').append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateValue(getValue());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLAnnotation, HDLClass.HDLObject);
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