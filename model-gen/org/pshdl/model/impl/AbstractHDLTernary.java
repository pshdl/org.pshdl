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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLTernary extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLTernary}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExpr
	 *            the value for ifExpr. Can <b>not</b> be <code>null</code>.
	 * @param thenExpr
	 *            the value for thenExpr. Can <b>not</b> be <code>null</code>.
	 * @param elseExpr
	 *            the value for elseExpr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLTernary(int id, @Nullable IHDLObject container, @Nonnull HDLExpression ifExpr, @Nonnull HDLExpression thenExpr, @Nonnull HDLExpression elseExpr,
			boolean validate) {
		super(id, container, validate);
		if (validate) {
			ifExpr = validateIfExpr(ifExpr);
		}
		if (ifExpr != null) {
			this.ifExpr = ifExpr;
		} else {
			this.ifExpr = null;
		}
		if (validate) {
			thenExpr = validateThenExpr(thenExpr);
		}
		if (thenExpr != null) {
			this.thenExpr = thenExpr;
		} else {
			this.thenExpr = null;
		}
		if (validate) {
			elseExpr = validateElseExpr(elseExpr);
		}
		if (elseExpr != null) {
			this.elseExpr = elseExpr;
		} else {
			this.elseExpr = null;
		}
	}

	public AbstractHDLTernary() {
		super();
		this.ifExpr = null;
		this.thenExpr = null;
		this.elseExpr = null;
	}

	protected final HDLExpression ifExpr;

	/**
	 * Get the ifExpr field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getIfExpr() {
		return ifExpr;
	}

	protected HDLExpression validateIfExpr(HDLExpression ifExpr) {
		if (ifExpr == null) {
			throw new IllegalArgumentException("The field ifExpr can not be null!");
		}
		return ifExpr;
	}

	protected final HDLExpression thenExpr;

	/**
	 * Get the thenExpr field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getThenExpr() {
		return thenExpr;
	}

	protected HDLExpression validateThenExpr(HDLExpression thenExpr) {
		if (thenExpr == null) {
			throw new IllegalArgumentException("The field thenExpr can not be null!");
		}
		return thenExpr;
	}

	protected final HDLExpression elseExpr;

	/**
	 * Get the elseExpr field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getElseExpr() {
		return elseExpr;
	}

	protected HDLExpression validateElseExpr(HDLExpression elseExpr) {
		if (elseExpr == null) {
			throw new IllegalArgumentException("The field elseExpr can not be null!");
		}
		return elseExpr;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLTernary copy() {
		final HDLTernary newObject = new HDLTernary(id, null, ifExpr, thenExpr, elseExpr, false);
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
	public HDLTernary copyFiltered(CopyFilter filter) {
		final HDLExpression filteredifExpr = filter.copyObject("ifExpr", this, ifExpr);
		final HDLExpression filteredthenExpr = filter.copyObject("thenExpr", this, thenExpr);
		final HDLExpression filteredelseExpr = filter.copyObject("elseExpr", this, elseExpr);
		return filter.postFilter((HDLTernary) this, new HDLTernary(id, null, filteredifExpr, filteredthenExpr, filteredelseExpr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLTernary copyDeepFrozen(IHDLObject container) {
		final HDLTernary copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLTernary} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLTernary setContainer(@Nullable IHDLObject container) {
		return (HDLTernary) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getIfExpr()}.
	 *
	 * @param ifExpr
	 *            sets the new ifExpr of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated ifExpr field.
	 */
	@Nonnull
	public HDLTernary setIfExpr(@Nonnull HDLExpression ifExpr) {
		ifExpr = validateIfExpr(ifExpr);
		final HDLTernary res = new HDLTernary(id, container, ifExpr, thenExpr, elseExpr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getThenExpr()}.
	 *
	 * @param thenExpr
	 *            sets the new thenExpr of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated thenExpr field.
	 */
	@Nonnull
	public HDLTernary setThenExpr(@Nonnull HDLExpression thenExpr) {
		thenExpr = validateThenExpr(thenExpr);
		final HDLTernary res = new HDLTernary(id, container, ifExpr, thenExpr, elseExpr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getElseExpr()}.
	 *
	 * @param elseExpr
	 *            sets the new elseExpr of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated elseExpr field.
	 */
	@Nonnull
	public HDLTernary setElseExpr(@Nonnull HDLExpression elseExpr) {
		elseExpr = validateElseExpr(elseExpr);
		final HDLTernary res = new HDLTernary(id, container, ifExpr, thenExpr, elseExpr, false);
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
		if (!(obj instanceof AbstractHDLTernary)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLTernary other = (AbstractHDLTernary) obj;
		if (ifExpr == null) {
			if (other.ifExpr != null) {
				return false;
			}
		} else if (!ifExpr.equals(other.ifExpr)) {
			return false;
		}
		if (thenExpr == null) {
			if (other.thenExpr != null) {
				return false;
			}
		} else if (!thenExpr.equals(other.thenExpr)) {
			return false;
		}
		if (elseExpr == null) {
			if (other.elseExpr != null) {
				return false;
			}
		} else if (!elseExpr.equals(other.elseExpr)) {
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
		result = (prime * result) + ((ifExpr == null) ? 0 : ifExpr.hashCode());
		result = (prime * result) + ((thenExpr == null) ? 0 : thenExpr.hashCode());
		result = (prime * result) + ((elseExpr == null) ? 0 : elseExpr.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLTernary()");
		if (ifExpr != null) {
			sb.append(".setIfExpr(").append(ifExpr.toConstructionString(spacing + "\t")).append(")");
		}
		if (thenExpr != null) {
			sb.append(".setThenExpr(").append(thenExpr.toConstructionString(spacing + "\t")).append(")");
		}
		if (elseExpr != null) {
			sb.append(".setElseExpr(").append(elseExpr.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateIfExpr(getIfExpr());
		if (getIfExpr() != null) {
			getIfExpr().validateAllFields(this, checkResolve);
		}
		validateThenExpr(getThenExpr());
		if (getThenExpr() != null) {
			getThenExpr().validateAllFields(this, checkResolve);
		}
		validateElseExpr(getElseExpr());
		if (getElseExpr() != null) {
			getElseExpr().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLTernary, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if (ifExpr != null) {
							current = Iterators.concat(Iterators.forArray(ifExpr), ifExpr.deepIterator());
						}
						break;
					case 1:
						if (thenExpr != null) {
							current = Iterators.concat(Iterators.forArray(thenExpr), thenExpr.deepIterator());
						}
						break;
					case 2:
						if (elseExpr != null) {
							current = Iterators.concat(Iterators.forArray(elseExpr), elseExpr.deepIterator());
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
						if (ifExpr != null) {
							current = Iterators.singletonIterator(ifExpr);
						}
						break;
					case 1:
						if (thenExpr != null) {
							current = Iterators.singletonIterator(thenExpr);
						}
						break;
					case 2:
						if (elseExpr != null) {
							current = Iterators.singletonIterator(elseExpr);
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