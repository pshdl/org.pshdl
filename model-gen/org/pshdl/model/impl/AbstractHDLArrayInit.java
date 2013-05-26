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
public abstract class AbstractHDLArrayInit extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLArrayInit}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param exp
	 *            the value for exp. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLArrayInit(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLExpression> exp, boolean validate) {
		super(id, container, validate);
		if (validate) {
			exp = validateExp(exp);
		}
		this.exp = new ArrayList<HDLExpression>();
		if (exp != null) {
			for (final HDLExpression newValue : exp) {
				this.exp.add(newValue);
			}
		}
	}

	public AbstractHDLArrayInit() {
		super();
		this.exp = new ArrayList<HDLExpression>();
	}

	protected final ArrayList<HDLExpression> exp;

	/**
	 * Get the exp field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getExp() {
		return (ArrayList<HDLExpression>) exp.clone();
	}

	protected Iterable<HDLExpression> validateExp(Iterable<HDLExpression> exp) {
		if (exp == null)
			return new ArrayList<HDLExpression>();
		return exp;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArrayInit copy() {
		final HDLArrayInit newObject = new HDLArrayInit(id, null, exp, false);
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
	public HDLArrayInit copyFiltered(CopyFilter filter) {
		final ArrayList<HDLExpression> filteredexp = filter.copyContainer("exp", this, exp);
		return filter.postFilter((HDLArrayInit) this, new HDLArrayInit(id, null, filteredexp, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArrayInit copyDeepFrozen(IHDLObject container) {
		final HDLArrayInit copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLArrayInit} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLArrayInit setContainer(@Nullable IHDLObject container) {
		return (HDLArrayInit) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getExp()}.
	 * 
	 * @param exp
	 *            sets the new exp of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLArrayInit} with the updated exp
	 *         field.
	 */
	@Nonnull
	public HDLArrayInit setExp(@Nullable Iterable<HDLExpression> exp) {
		exp = validateExp(exp);
		final HDLArrayInit res = new HDLArrayInit(id, container, exp, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getExp()}.
	 * 
	 * @param newExp
	 *            the value that should be added to the field {@link #getExp()}
	 * @return a new instance of {@link HDLArrayInit} with the updated exp
	 *         field.
	 */
	@Nonnull
	public HDLArrayInit addExp(@Nullable HDLExpression newExp) {
		if (newExp == null)
			throw new IllegalArgumentException("Element of exp can not be null!");
		final ArrayList<HDLExpression> exp = (ArrayList<HDLExpression>) this.exp.clone();
		exp.add(newExp);
		final HDLArrayInit res = new HDLArrayInit(id, container, exp, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExp()}.
	 * 
	 * @param newExp
	 *            the value that should be removed from the field
	 *            {@link #getExp()}
	 * @return a new instance of {@link HDLArrayInit} with the updated exp
	 *         field.
	 */
	@Nonnull
	public HDLArrayInit removeExp(@Nullable HDLExpression newExp) {
		if (newExp == null)
			throw new IllegalArgumentException("Removed element of exp can not be null!");
		final ArrayList<HDLExpression> exp = (ArrayList<HDLExpression>) this.exp.clone();
		exp.remove(newExp);
		final HDLArrayInit res = new HDLArrayInit(id, container, exp, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExp()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getExp()}
	 * @return a new instance of {@link HDLArrayInit} with the updated exp
	 *         field.
	 */
	@Nonnull
	public HDLArrayInit removeExp(int idx) {
		final ArrayList<HDLExpression> exp = (ArrayList<HDLExpression>) this.exp.clone();
		exp.remove(idx);
		final HDLArrayInit res = new HDLArrayInit(id, container, exp, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLArrayInit))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLArrayInit other = (AbstractHDLArrayInit) obj;
		if (exp == null) {
			if (other.exp != null)
				return false;
		} else if (!exp.equals(other.exp))
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
		result = (prime * result) + ((exp == null) ? 0 : exp.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLArrayInit()");
		if (exp != null) {
			if (exp.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : exp) {
					sb.append(".addExp(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateExp(getExp());
		if (getExp() != null) {
			for (final HDLExpression o : getExp()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLArrayInit, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if ((exp != null) && (exp.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(exp.size());
							for (final HDLExpression o : exp) {
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
						if ((exp != null) && (exp.size() != 0)) {
							current = exp.iterator();
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