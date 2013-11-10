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
public abstract class AbstractHDLOpExpression extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLOpExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLOpExpression(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, boolean validate) {
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
			right = validateRight(right);
		}
		if (right != null) {
			this.right = right;
		} else {
			this.right = null;
		}
	}

	public AbstractHDLOpExpression() {
		super();
		this.left = null;
		this.right = null;
	}

	protected final HDLExpression left;

	/**
	 * Get the left field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getLeft() {
		return left;
	}

	protected HDLExpression validateLeft(HDLExpression left) {
		if (left == null)
			throw new IllegalArgumentException("The field left can not be null!");
		return left;
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
		if (right == null)
			throw new IllegalArgumentException("The field right can not be null!");
		return right;
	}

	@Nonnull
	public abstract HDLOpExpression setLeft(@Nonnull HDLExpression left);

	@Nonnull
	public abstract HDLOpExpression setRight(@Nonnull HDLExpression right);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLOpExpression copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLOpExpression copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLOpExpression copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLOpExpression))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLOpExpression other = (AbstractHDLOpExpression) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
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
		result = (prime * result) + ((left == null) ? 0 : left.hashCode());
		result = (prime * result) + ((right == null) ? 0 : right.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLOpExpression()");
		if (left != null) {
			sb.append(".setLeft(").append(left.toConstructionString(spacing + "\t")).append(")");
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
		validateRight(getRight());
		if (getRight() != null) {
			getRight().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLOpExpression, HDLClass.HDLExpression, HDLClass.HDLObject);
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