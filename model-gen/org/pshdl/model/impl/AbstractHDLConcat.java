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
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLConcat extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLConcat}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLConcat(int id, @Nullable IHDLObject container, @Nonnull Iterable<HDLExpression> cats, boolean validate) {
		super(id, container, validate);
		if (validate) {
			cats = validateCats(cats);
		}
		this.cats = new ArrayList<>();
		if (cats != null) {
			for (final HDLExpression newValue : cats) {
				this.cats.add(newValue);
			}
		}
	}

	public AbstractHDLConcat() {
		super();
		this.cats = new ArrayList<>();
	}

	protected final ArrayList<HDLExpression> cats;

	/**
	 * Get the cats field. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getCats() {
		return (ArrayList<HDLExpression>) cats.clone();
	}

	protected Iterable<HDLExpression> validateCats(Iterable<HDLExpression> cats) {
		if (cats == null) {
			throw new IllegalArgumentException("The field cats can not be null!");
		}
		if (!cats.iterator().hasNext()) {
			throw new IllegalArgumentException("The field cats must contain at least one item!");
		}
		return cats;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLConcat copy() {
		final HDLConcat newObject = new HDLConcat(id, null, cats, false);
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
	public HDLConcat copyFiltered(CopyFilter filter) {
		final ArrayList<HDLExpression> filteredcats = filter.copyContainer("cats", this, cats);
		return filter.postFilter((HDLConcat) this, new HDLConcat(id, null, filteredcats, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLConcat copyDeepFrozen(IHDLObject container) {
		final HDLConcat copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLConcat} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLConcat setContainer(@Nullable IHDLObject container) {
		return (HDLConcat) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getCats()}.
	 *
	 * @param cats
	 *            sets the new cats of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least
	 *            one element.
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat setCats(@Nonnull Iterable<HDLExpression> cats) {
		cats = validateCats(cats);
		final HDLConcat res = new HDLConcat(id, container, cats, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getCats()}.
	 *
	 * @param newCats
	 *            the value that should be added to the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat addCats(@Nonnull HDLExpression newCats) {
		if (newCats == null) {
			throw new IllegalArgumentException("Element of cats can not be null!");
		}
		final ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.add(newCats);
		final HDLConcat res = new HDLConcat(id, container, cats, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCats()}.
	 *
	 * @param newCats
	 *            the value that should be removed from the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat removeCats(@Nonnull HDLExpression newCats) {
		if (newCats == null) {
			throw new IllegalArgumentException("Removed element of cats can not be null!");
		}
		final ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.remove(newCats);
		final HDLConcat res = new HDLConcat(id, container, cats, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCats()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat removeCats(int idx) {
		final ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.remove(idx);
		final HDLConcat res = new HDLConcat(id, container, cats, false);
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
		if (!(obj instanceof AbstractHDLConcat)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLConcat other = (AbstractHDLConcat) obj;
		if (cats == null) {
			if (other.cats != null) {
				return false;
			}
		} else if (!cats.equals(other.cats)) {
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
		result = (prime * result) + ((cats == null) ? 0 : cats.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLConcat()");
		if (cats != null) {
			if (cats.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : cats) {
					sb.append(".addCats(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateCats(getCats());
		if (getCats() != null) {
			for (final HDLExpression o : getCats()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLConcat, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if ((cats != null) && (cats.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(cats.size());
							for (final HDLExpression o : cats) {
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
						if ((cats != null) && (cats.size() != 0)) {
							current = cats.iterator();
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