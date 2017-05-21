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
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLObject;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

@SuppressWarnings("all")
public abstract class AbstractHDLLiteral extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLLiteral}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 * @param str
	 *            the value for str. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLLiteral(int id, @Nullable IHDLObject container, @Nonnull String val, @Nullable Boolean str, boolean validate) {
		super(id, container, validate);
		if (validate) {
			val = validateVal(val);
		}
		this.val = val;
		if (validate) {
			str = validateStr(str);
		}
		this.str = str;
	}

	public AbstractHDLLiteral() {
		super();
		this.val = null;
		this.str = null;
	}

	protected final String val;

	/**
	 * Get the val field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getVal() {
		return val;
	}

	protected String validateVal(String val) {
		if (val == null) {
			throw new IllegalArgumentException("The field val can not be null!");
		}
		return val;
	}

	protected final Boolean str;

	/**
	 * Get the str field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public Boolean getStr() {
		return str;
	}

	protected Boolean validateStr(Boolean str) {
		return str;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLLiteral copy() {
		final HDLLiteral newObject = new HDLLiteral(id, null, val, str, false);
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
	public HDLLiteral copyFiltered(CopyFilter filter) {
		final String filteredval = filter.copyObject("val", this, val);
		final Boolean filteredstr = filter.copyObject("str", this, str);
		return filter.postFilter((HDLLiteral) this, new HDLLiteral(id, null, filteredval, filteredstr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLLiteral copyDeepFrozen(IHDLObject container) {
		final HDLLiteral copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLLiteral} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLLiteral setContainer(@Nullable IHDLObject container) {
		return (HDLLiteral) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVal()}.
	 *
	 * @param val
	 *            sets the new val of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated val field.
	 */
	@Nonnull
	public HDLLiteral setVal(@Nonnull String val) {
		val = validateVal(val);
		final HDLLiteral res = new HDLLiteral(id, container, val, str, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStr()}.
	 *
	 * @param str
	 *            sets the new str of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated str field.
	 */
	@Nonnull
	public HDLLiteral setStr(@Nullable Boolean str) {
		str = validateStr(str);
		final HDLLiteral res = new HDLLiteral(id, container, val, str, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStr()}.
	 *
	 * @param str
	 *            sets the new str of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated str field.
	 */
	@Nonnull
	public HDLLiteral setStr(boolean str) {
		str = validateStr(str);
		final HDLLiteral res = new HDLLiteral(id, container, val, str, false);
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
		if (!(obj instanceof AbstractHDLLiteral)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLLiteral other = (AbstractHDLLiteral) obj;
		if (val == null) {
			if (other.val != null) {
				return false;
			}
		} else if (!val.equals(other.val)) {
			return false;
		}
		if (str == null) {
			if (other.str != null) {
				return false;
			}
		} else if (!str.equals(other.str)) {
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
		result = (prime * result) + ((val == null) ? 0 : val.hashCode());
		result = (prime * result) + ((str == null) ? 0 : str.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLLiteral()");
		if (val != null) {
			sb.append(".setVal(").append('"' + val + '"').append(")");
		}
		if (str != null) {
			sb.append(".setStr(").append(str).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateVal(getVal());
		validateStr(getStr());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLLiteral, HDLClass.HDLExpression, HDLClass.HDLObject);
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