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

import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLAssignment extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLAssignment}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>, {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLAssignment(int id, @Nullable IHDLObject container, @Nonnull HDLReference left, @Nullable HDLAssignmentType type, @Nonnull HDLExpression right,
			boolean validate) {
		super(id, container, validate);
		if (validate) {
			left = validateLeft(left);
		}
		if (left != null) {
			this.left = left;
		} else {
			this.left = null;
		}
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			right = validateRight(right);
		}
		if (right != null) {
			this.right = right;
		} else {
			this.right = null;
		}
	}

	public AbstractHDLAssignment() {
		super();
		this.left = null;
		this.type = null;
		this.right = null;
	}

	protected final HDLReference left;

	/**
	 * Get the left field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLReference getLeft() {
		return left;
	}

	protected HDLReference validateLeft(HDLReference left) {
		if (left == null) {
			throw new IllegalArgumentException("The field left can not be null!");
		}
		return left;
	}

	protected final HDLAssignmentType type;

	/**
	 * Get the type field. If <code>null</code>, {@link HDLAssignmentType#ASSGN} is used as default.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLAssignmentType getType() {
		return type == null ? HDLAssignmentType.ASSGN : type;
	}

	protected HDLAssignmentType validateType(HDLAssignmentType type) {
		return type == null ? HDLAssignmentType.ASSGN : type;
	}

	protected final HDLExpression right;

	/**
	 * Get the right field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getRight() {
		return right;
	}

	protected HDLExpression validateRight(HDLExpression right) {
		if (right == null) {
			throw new IllegalArgumentException("The field right can not be null!");
		}
		return right;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAssignment copy() {
		final HDLAssignment newObject = new HDLAssignment(id, null, left, type, right, false);
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
	public HDLAssignment copyFiltered(CopyFilter filter) {
		final HDLReference filteredleft = filter.copyObject("left", this, left);
		final HDLAssignmentType filteredtype = filter.copyObject("type", this, type);
		final HDLExpression filteredright = filter.copyObject("right", this, right);
		return filter.postFilter((HDLAssignment) this, new HDLAssignment(id, null, filteredleft, filteredtype, filteredright, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAssignment copyDeepFrozen(IHDLObject container) {
		final HDLAssignment copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLAssignment} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLAssignment setContainer(@Nullable IHDLObject container) {
		return (HDLAssignment) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLeft()}.
	 *
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLAssignment} with the updated left field.
	 */
	@Nonnull
	public HDLAssignment setLeft(@Nonnull HDLReference left) {
		left = validateLeft(left);
		final HDLAssignment res = new HDLAssignment(id, container, left, type, right, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 *
	 * @param type
	 *            sets the new type of this object. If <code>null</code>, {@link HDLAssignmentType#ASSGN} is used as default.
	 * @return a new instance of {@link HDLAssignment} with the updated type field.
	 */
	@Nonnull
	public HDLAssignment setType(@Nullable HDLAssignmentType type) {
		type = validateType(type);
		final HDLAssignment res = new HDLAssignment(id, container, left, type, right, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRight()}.
	 *
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLAssignment} with the updated right field.
	 */
	@Nonnull
	public HDLAssignment setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		final HDLAssignment res = new HDLAssignment(id, container, left, type, right, false);
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
		if (!(obj instanceof AbstractHDLAssignment)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLAssignment other = (AbstractHDLAssignment) obj;
		if (left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!left.equals(other.left)) {
			return false;
		}
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		if (right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!right.equals(other.right)) {
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
		result = (prime * result) + ((left == null) ? 0 : left.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((right == null) ? 0 : right.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLAssignment()");
		if (left != null) {
			sb.append(".setLeft(").append(left.toConstructionString(spacing + "\t")).append(")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLAssignmentType.").append(type.name() + ")");
		}
		if (right != null) {
			sb.append(".setRight(").append(right.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateLeft(getLeft());
		if (getLeft() != null) {
			getLeft().validateAllFields(this, checkResolve);
		}
		validateType(getType());
		validateRight(getRight());
		if (getRight() != null) {
			getRight().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLAssignment, HDLClass.HDLStatement, HDLClass.HDLObject);
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