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
public abstract class AbstractHDLRange extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLRange(@Nullable IHDLObject container, @Nullable HDLExpression from, @Nonnull HDLExpression to, boolean validate) {
		super(container, validate);
		if (validate) {
			from = validateFrom(from);
		}
		if (from != null) {
			this.from = from;
		} else {
			this.from = null;
		}
		if (validate) {
			to = validateTo(to);
		}
		if (to != null) {
			this.to = to;
		} else {
			this.to = null;
		}
	}

	public AbstractHDLRange() {
		super();
		this.from = null;
		this.to = null;
	}

	protected final HDLExpression from;

	/**
	 * Get the from field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLExpression getFrom() {
		return from;
	}

	protected HDLExpression validateFrom(HDLExpression from) {
		return from;
	}

	protected final HDLExpression to;

	/**
	 * Get the to field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getTo() {
		return to;
	}

	protected HDLExpression validateTo(HDLExpression to) {
		if (to == null)
			throw new IllegalArgumentException("The field to can not be null!");
		return to;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRange copy() {
		HDLRange newObject = new HDLRange(null, from, to, false);
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
	public HDLRange copyFiltered(CopyFilter filter) {
		HDLExpression filteredfrom = filter.copyObject("from", this, from);
		HDLExpression filteredto = filter.copyObject("to", this, to);
		return filter.postFilter((HDLRange) this, new HDLRange(null, filteredfrom, filteredto, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRange copyDeepFrozen(IHDLObject container) {
		HDLRange copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLRange} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLRange setContainer(@Nullable IHDLObject container) {
		return (HDLRange) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFrom()}.
	 * 
	 * @param from
	 *            sets the new from of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRange} with the updated from field.
	 */
	@Nonnull
	public HDLRange setFrom(@Nullable HDLExpression from) {
		from = validateFrom(from);
		HDLRange res = new HDLRange(container, from, to, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getTo()}.
	 * 
	 * @param to
	 *            sets the new to of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRange} with the updated to field.
	 */
	@Nonnull
	public HDLRange setTo(@Nonnull HDLExpression to) {
		to = validateTo(to);
		HDLRange res = new HDLRange(container, from, to, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRange))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLRange other = (AbstractHDLRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
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
		result = (prime * result) + ((from == null) ? 0 : from.hashCode());
		result = (prime * result) + ((to == null) ? 0 : to.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLRange()");
		if (from != null) {
			sb.append(".setFrom(").append(from.toConstructionString(spacing + "\t")).append(")");
		}
		if (to != null) {
			sb.append(".setTo(").append(to.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateFrom(getFrom());
		if (getFrom() != null) {
			getFrom().validateAllFields(this, checkResolve);
		}
		validateTo(getTo());
		if (getTo() != null) {
			getTo().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLRange, HDLClass.HDLObject);
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
						if (from != null) {
							current = from.deepIterator();
						}
						break;
					case 1:
						if (to != null) {
							current = to.deepIterator();
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
						if (from != null) {
							current = Iterators.singletonIterator(from);
						}
						break;
					case 1:
						if (to != null) {
							current = Iterators.singletonIterator(to);
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