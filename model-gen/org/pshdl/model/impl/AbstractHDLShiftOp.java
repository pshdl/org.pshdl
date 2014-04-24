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

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLOpExpression;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLShiftOp.HDLShiftOpType;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLShiftOp extends HDLOpExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLShiftOp}
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
	public AbstractHDLShiftOp(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLShiftOpType type, boolean validate) {
		super(id, container, left, right, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
	}

	public AbstractHDLShiftOp() {
		super();
		this.type = null;
	}

	protected final HDLShiftOpType type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Override
	@Nonnull
	public HDLShiftOpType getType() {
		return type;
	}

	protected HDLShiftOpType validateType(HDLShiftOpType type) {
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
	public HDLShiftOp copy() {
		final HDLShiftOp newObject = new HDLShiftOp(id, null, left, right, type, false);
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
	public HDLShiftOp copyFiltered(CopyFilter filter) {
		final HDLExpression filteredleft = filter.copyObject("left", this, left);
		final HDLExpression filteredright = filter.copyObject("right", this, right);
		final HDLShiftOpType filteredtype = filter.copyObject("type", this, type);
		return filter.postFilter((HDLShiftOp) this, new HDLShiftOp(id, null, filteredleft, filteredright, filteredtype, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLShiftOp copyDeepFrozen(IHDLObject container) {
		final HDLShiftOp copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLShiftOp} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLShiftOp setContainer(@Nullable IHDLObject container) {
		return (HDLShiftOp) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLeft()}.
	 *
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLShiftOp} with the updated left field.
	 */
	@Override
	@Nonnull
	public HDLShiftOp setLeft(@Nonnull HDLExpression left) {
		left = validateLeft(left);
		final HDLShiftOp res = new HDLShiftOp(id, container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRight()}.
	 *
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLShiftOp} with the updated right
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLShiftOp setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		final HDLShiftOp res = new HDLShiftOp(id, container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 *
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLShiftOp} with the updated type field.
	 */
	@Nonnull
	public HDLShiftOp setType(@Nonnull HDLShiftOpType type) {
		type = validateType(type);
		final HDLShiftOp res = new HDLShiftOp(id, container, left, right, type, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLShiftOp))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLShiftOp other = (AbstractHDLShiftOp) obj;
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
		sb.append('\n').append(spacing).append("new HDLShiftOp()");
		if (left != null) {
			sb.append(".setLeft(").append(left.toConstructionString(spacing + "\t")).append(")");
		}
		if (right != null) {
			sb.append(".setRight(").append(right.toConstructionString(spacing + "\t")).append(")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLShiftOpType.").append(type.name() + ")");
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
		return EnumSet.of(HDLClass.HDLShiftOp, HDLClass.HDLOpExpression, HDLClass.HDLExpression, HDLClass.HDLObject);
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