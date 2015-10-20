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
import org.pshdl.model.HDLCompound;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLForLoop extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLForLoop}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param range
	 *            the value for range. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param param
	 *            the value for param. Can <b>not</b> be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLForLoop(int id, @Nullable IHDLObject container, @Nonnull Iterable<HDLRange> range, @Nonnull HDLVariable param, @Nullable Iterable<HDLStatement> dos,
			boolean validate) {
		super(id, container, validate);
		if (validate) {
			range = validateRange(range);
		}
		this.range = new ArrayList<HDLRange>();
		if (range != null) {
			for (final HDLRange newValue : range) {
				this.range.add(newValue);
			}
		}
		if (validate) {
			param = validateParam(param);
		}
		if (param != null) {
			this.param = param;
		} else {
			this.param = null;
		}
		if (validate) {
			dos = validateDos(dos);
		}
		this.dos = new ArrayList<HDLStatement>();
		if (dos != null) {
			for (final HDLStatement newValue : dos) {
				this.dos.add(newValue);
			}
		}
	}

	public AbstractHDLForLoop() {
		super();
		this.range = new ArrayList<HDLRange>();
		this.param = null;
		this.dos = new ArrayList<HDLStatement>();
	}

	protected final ArrayList<HDLRange> range;

	/**
	 * Get the range field. Can <b>not</b> be <code>null</code>, additionally
	 * the collection must contain at least one element.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLRange> getRange() {
		return (ArrayList<HDLRange>) range.clone();
	}

	protected Iterable<HDLRange> validateRange(Iterable<HDLRange> range) {
		if (range == null)
			throw new IllegalArgumentException("The field range can not be null!");
		if (!range.iterator().hasNext())
			throw new IllegalArgumentException("The field range must contain at least one item!");
		return range;
	}

	protected final HDLVariable param;

	/**
	 * Get the param field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLVariable getParam() {
		return param;
	}

	protected HDLVariable validateParam(HDLVariable param) {
		if (param == null)
			throw new IllegalArgumentException("The field param can not be null!");
		return param;
	}

	protected final ArrayList<HDLStatement> dos;

	/**
	 * Get the dos field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getDos() {
		return (ArrayList<HDLStatement>) dos.clone();
	}

	protected Iterable<HDLStatement> validateDos(Iterable<HDLStatement> dos) {
		if (dos == null)
			return new ArrayList<HDLStatement>();
		return dos;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLForLoop copy() {
		final HDLForLoop newObject = new HDLForLoop(id, null, range, param, dos, false);
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
	public HDLForLoop copyFiltered(CopyFilter filter) {
		final ArrayList<HDLRange> filteredrange = filter.copyContainer("range", this, range);
		final HDLVariable filteredparam = filter.copyObject("param", this, param);
		final ArrayList<HDLStatement> filtereddos = filter.copyContainer("dos", this, dos);
		return filter.postFilter((HDLForLoop) this, new HDLForLoop(id, null, filteredrange, filteredparam, filtereddos, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLForLoop copyDeepFrozen(IHDLObject container) {
		final HDLForLoop copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLForLoop} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLForLoop setContainer(@Nullable IHDLObject container) {
		return (HDLForLoop) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getRange()}.
	 *
	 * @param range
	 *            sets the new range of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	@Nonnull
	public HDLForLoop setRange(@Nonnull Iterable<HDLRange> range) {
		range = validateRange(range);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getRange()}.
	 *
	 * @param newRange
	 *            the value that should be added to the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	@Nonnull
	public HDLForLoop addRange(@Nonnull HDLRange newRange) {
		if (newRange == null)
			throw new IllegalArgumentException("Element of range can not be null!");
		final ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.add(newRange);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getRange()}.
	 *
	 * @param newRange
	 *            the value that should be removed from the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	@Nonnull
	public HDLForLoop removeRange(@Nonnull HDLRange newRange) {
		if (newRange == null)
			throw new IllegalArgumentException("Removed element of range can not be null!");
		final ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.remove(newRange);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getRange()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	@Nonnull
	public HDLForLoop removeRange(int idx) {
		final ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.remove(idx);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParam()}.
	 *
	 * @param param
	 *            sets the new param of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLForLoop} with the updated param
	 *         field.
	 */
	@Nonnull
	public HDLForLoop setParam(@Nonnull HDLVariable param) {
		param = validateParam(param);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDos()}.
	 *
	 * @param dos
	 *            sets the new dos of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	@Nonnull
	public HDLForLoop setDos(@Nullable Iterable<HDLStatement> dos) {
		dos = validateDos(dos);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDos()}.
	 *
	 * @param newDos
	 *            the value that should be added to the field {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	@Nonnull
	public HDLForLoop addDos(@Nullable HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Element of dos can not be null!");
		final ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.add(newDos);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 *
	 * @param newDos
	 *            the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	@Nonnull
	public HDLForLoop removeDos(@Nullable HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Removed element of dos can not be null!");
		final ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(newDos);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	@Nonnull
	public HDLForLoop removeDos(int idx) {
		final ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(idx);
		final HDLForLoop res = new HDLForLoop(id, container, range, param, dos, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLForLoop))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLForLoop other = (AbstractHDLForLoop) obj;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		if (dos == null) {
			if (other.dos != null)
				return false;
		} else if (!dos.equals(other.dos))
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
		result = (prime * result) + ((range == null) ? 0 : range.hashCode());
		result = (prime * result) + ((param == null) ? 0 : param.hashCode());
		result = (prime * result) + ((dos == null) ? 0 : dos.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLForLoop()");
		if (range != null) {
			if (range.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLRange o : range) {
					sb.append(".addRange(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (param != null) {
			sb.append(".setParam(").append(param.toConstructionString(spacing + "\t")).append(")");
		}
		if (dos != null) {
			if (dos.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : dos) {
					sb.append(".addDos(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateRange(getRange());
		if (getRange() != null) {
			for (final HDLRange o : getRange()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateParam(getParam());
		if (getParam() != null) {
			getParam().validateAllFields(this, checkResolve);
		}
		validateDos(getDos());
		if (getDos() != null) {
			for (final HDLStatement o : getDos()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLForLoop, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if ((range != null) && (range.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(range.size());
							for (final HDLRange o : range) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if (param != null) {
							current = Iterators.concat(Iterators.forArray(param), param.deepIterator());
						}
						break;
					case 2:
						if ((dos != null) && (dos.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(dos.size());
							for (final HDLStatement o : dos) {
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
						if ((range != null) && (range.size() != 0)) {
							current = range.iterator();
						}
						break;
					case 1:
						if (param != null) {
							current = Iterators.singletonIterator(param);
						}
						break;
					case 2:
						if ((dos != null) && (dos.size() != 0)) {
							current = dos.iterator();
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