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
import org.pshdl.model.HDLRange;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLRange extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param inc
	 *            the value for inc. Can be <code>null</code>.
	 * @param dec
	 *            the value for dec. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLRange(int id, @Nullable IHDLObject container, @Nullable HDLExpression from, @Nullable HDLExpression inc, @Nullable HDLExpression dec,
			@Nonnull HDLExpression to, boolean validate) {
		super(id, container, validate);
		if (validate) {
			from = validateFrom(from);
		}
		if (from != null) {
			this.from = from;
		} else {
			this.from = null;
		}
		if (validate) {
			inc = validateInc(inc);
		}
		if (inc != null) {
			this.inc = inc;
		} else {
			this.inc = null;
		}
		if (validate) {
			dec = validateDec(dec);
		}
		if (dec != null) {
			this.dec = dec;
		} else {
			this.dec = null;
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
		this.inc = null;
		this.dec = null;
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

	protected final HDLExpression inc;

	/**
	 * Get the inc field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLExpression getInc() {
		return inc;
	}

	protected HDLExpression validateInc(HDLExpression inc) {
		return inc;
	}

	protected final HDLExpression dec;

	/**
	 * Get the dec field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLExpression getDec() {
		return dec;
	}

	protected HDLExpression validateDec(HDLExpression dec) {
		return dec;
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
		final HDLRange newObject = new HDLRange(id, null, from, inc, dec, to, false);
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
		final HDLExpression filteredfrom = filter.copyObject("from", this, from);
		final HDLExpression filteredinc = filter.copyObject("inc", this, inc);
		final HDLExpression filtereddec = filter.copyObject("dec", this, dec);
		final HDLExpression filteredto = filter.copyObject("to", this, to);
		return filter.postFilter((HDLRange) this, new HDLRange(id, null, filteredfrom, filteredinc, filtereddec, filteredto, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRange copyDeepFrozen(IHDLObject container) {
		final HDLRange copy = copyFiltered(CopyFilter.DEEP_META);
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
		final HDLRange res = new HDLRange(id, container, from, inc, dec, to, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getInc()}.
	 * 
	 * @param inc
	 *            sets the new inc of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRange} with the updated inc field.
	 */
	@Nonnull
	public HDLRange setInc(@Nullable HDLExpression inc) {
		inc = validateInc(inc);
		final HDLRange res = new HDLRange(id, container, from, inc, dec, to, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDec()}.
	 * 
	 * @param dec
	 *            sets the new dec of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRange} with the updated dec field.
	 */
	@Nonnull
	public HDLRange setDec(@Nullable HDLExpression dec) {
		dec = validateDec(dec);
		final HDLRange res = new HDLRange(id, container, from, inc, dec, to, false);
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
		final HDLRange res = new HDLRange(id, container, from, inc, dec, to, false);
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
		final AbstractHDLRange other = (AbstractHDLRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (inc == null) {
			if (other.inc != null)
				return false;
		} else if (!inc.equals(other.inc))
			return false;
		if (dec == null) {
			if (other.dec != null)
				return false;
		} else if (!dec.equals(other.dec))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
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
		result = (prime * result) + ((from == null) ? 0 : from.hashCode());
		result = (prime * result) + ((inc == null) ? 0 : inc.hashCode());
		result = (prime * result) + ((dec == null) ? 0 : dec.hashCode());
		result = (prime * result) + ((to == null) ? 0 : to.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLRange()");
		if (from != null) {
			sb.append(".setFrom(").append(from.toConstructionString(spacing + "\t")).append(")");
		}
		if (inc != null) {
			sb.append(".setInc(").append(inc.toConstructionString(spacing + "\t")).append(")");
		}
		if (dec != null) {
			sb.append(".setDec(").append(dec.toConstructionString(spacing + "\t")).append(")");
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
		validateInc(getInc());
		if (getInc() != null) {
			getInc().validateAllFields(this, checkResolve);
		}
		validateDec(getDec());
		if (getDec() != null) {
			getDec().validateAllFields(this, checkResolve);
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
							current = Iterators.concat(Iterators.forArray(from), from.deepIterator());
						}
						break;
					case 1:
						if (inc != null) {
							current = Iterators.concat(Iterators.forArray(inc), inc.deepIterator());
						}
						break;
					case 2:
						if (dec != null) {
							current = Iterators.concat(Iterators.forArray(dec), dec.deepIterator());
						}
						break;
					case 3:
						if (to != null) {
							current = Iterators.concat(Iterators.forArray(to), to.deepIterator());
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
						if (inc != null) {
							current = Iterators.singletonIterator(inc);
						}
						break;
					case 2:
						if (dec != null) {
							current = Iterators.singletonIterator(dec);
						}
						break;
					case 3:
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