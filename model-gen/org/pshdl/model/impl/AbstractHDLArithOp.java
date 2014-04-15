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
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.utils.*;

import com.google.common.collect.*;

@SuppressWarnings("all")
public abstract class AbstractHDLArithOp extends HDLOpExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLArithOp}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLArithOp(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLArithOpType type, boolean validate) {
		super(id, container, left, right, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
	}

	public AbstractHDLArithOp() {
		super();
		this.type = null;
	}

	protected final HDLArithOpType type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Override
	@Nonnull
	public HDLArithOpType getType() {
		return type;
	}

	protected HDLArithOpType validateType(HDLArithOpType type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArithOp copy() {
		final HDLArithOp newObject = new HDLArithOp(id, null, left, right, type, false);
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
	public HDLArithOp copyFiltered(CopyFilter filter) {
		final HDLExpression filteredleft = filter.copyObject("left", this, left);
		final HDLExpression filteredright = filter.copyObject("right", this, right);
		final HDLArithOpType filteredtype = filter.copyObject("type", this, type);
		return filter.postFilter((HDLArithOp) this, new HDLArithOp(id, null, filteredleft, filteredright, filteredtype, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLArithOp copyDeepFrozen(IHDLObject container) {
		final HDLArithOp copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLArithOp} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLArithOp setContainer(@Nullable IHDLObject container) {
		return (HDLArithOp) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLeft()}.
	 *
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLArithOp} with the updated left field.
	 */
	@Override
	@Nonnull
	public HDLArithOp setLeft(@Nonnull HDLExpression left) {
		left = validateLeft(left);
		final HDLArithOp res = new HDLArithOp(id, container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRight()}.
	 *
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLArithOp} with the updated right
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLArithOp setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		final HDLArithOp res = new HDLArithOp(id, container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 *
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLArithOp} with the updated type field.
	 */
	@Nonnull
	public HDLArithOp setType(@Nonnull HDLArithOpType type) {
		type = validateType(type);
		final HDLArithOp res = new HDLArithOp(id, container, left, right, type, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLArithOp))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLArithOp other = (AbstractHDLArithOp) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
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
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLArithOp()");
		if (left != null) {
			sb.append(".setLeft(").append(left.toConstructionString(spacing + "\t")).append(")");
		}
		if (right != null) {
			sb.append(".setRight(").append(right.toConstructionString(spacing + "\t")).append(")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLArithOpType.").append(type.name() + ")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateType(getType());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLArithOp, HDLClass.HDLOpExpression, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if (left != null) {
							current = Iterators.concat(Iterators.forArray(left), left.deepIterator());
						}
						break;
					case 1:
						if (right != null) {
							current = Iterators.concat(Iterators.forArray(right), right.deepIterator());
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
						if (left != null) {
							current = Iterators.singletonIterator(left);
						}
						break;
					case 1:
						if (right != null) {
							current = Iterators.singletonIterator(right);
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