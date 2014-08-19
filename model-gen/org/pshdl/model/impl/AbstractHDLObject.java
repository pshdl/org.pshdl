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

import java.util.EnumSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

@SuppressWarnings("all")
public abstract class AbstractHDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLObject}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLObject(int id, @Nullable IHDLObject container, boolean validate) {
		this.id = id;
		if (container == this)
			throw new IllegalArgumentException("Object can not contain itself");
		if (validate) {
			container = validateContainer(container);
		}
		this.container = container;
	}

	public AbstractHDLObject() {
		super();
		this.id = gid.incrementAndGet();
		this.container = null;
	}

	protected IHDLObject container;

	/**
	 * Get the container field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public IHDLObject getContainer() {
		return container;
	}

	protected IHDLObject validateContainer(IHDLObject container) {
		return container;
	}

	@Nonnull
	public abstract HDLObject setContainer(@Nullable IHDLObject container);

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copy();

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 *
	 * @return a new instance of this class.
	 */
	@Nonnull
	public abstract IHDLObject copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLObject))
			return false;
		final AbstractHDLObject other = (AbstractHDLObject) obj;
		if (container == null) {
			if (other.container != null)
				return false;
		} else if (!container.equals(other.container))
			return false;
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = 1;
		final int prime = 31;
		result = (prime * result) + ((container == null) ? 0 : container.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLObject()");
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		if (getContainer() != expectedParent)
			throw new IllegalArgumentException("This object " + this + " does not have the expected container! " + expectedParent);
		validateContainer(getContainer());
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLObject);
	}

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

	protected boolean frozen = false;
	protected static final AtomicInteger gid = new AtomicInteger();
	protected int id;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
}