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
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLManip.HDLManipType;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLType;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLManip extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLManip(int id, @Nullable IHDLObject container, @Nonnull HDLManipType type, @Nonnull HDLExpression target, @Nullable HDLType castTo, boolean validate) {
		super(id, container, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			target = validateTarget(target);
		}
		if (target != null) {
			this.target = target;
		} else {
			this.target = null;
		}
		if (validate) {
			castTo = validateCastTo(castTo);
		}
		if (castTo != null) {
			this.castTo = castTo;
		} else {
			this.castTo = null;
		}
	}

	public AbstractHDLManip() {
		super();
		this.type = null;
		this.target = null;
		this.castTo = null;
	}

	protected final HDLManipType type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLManipType getType() {
		return type;
	}

	protected HDLManipType validateType(HDLManipType type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

	protected final HDLExpression target;

	/**
	 * Get the target field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getTarget() {
		return target;
	}

	protected HDLExpression validateTarget(HDLExpression target) {
		if (target == null)
			throw new IllegalArgumentException("The field target can not be null!");
		return target;
	}

	protected final HDLType castTo;

	/**
	 * Get the castTo field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLType getCastTo() {
		return castTo;
	}

	protected HDLType validateCastTo(HDLType castTo) {
		return castTo;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLManip copy() {
		final HDLManip newObject = new HDLManip(id, null, type, target, castTo, false);
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
	public HDLManip copyFiltered(CopyFilter filter) {
		final HDLManipType filteredtype = filter.copyObject("type", this, type);
		final HDLExpression filteredtarget = filter.copyObject("target", this, target);
		final HDLType filteredcastTo = filter.copyObject("castTo", this, castTo);
		return filter.postFilter((HDLManip) this, new HDLManip(id, null, filteredtype, filteredtarget, filteredcastTo, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLManip copyDeepFrozen(IHDLObject container) {
		final HDLManip copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLManip} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLManip setContainer(@Nullable IHDLObject container) {
		return (HDLManip) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLManip} with the updated type field.
	 */
	@Nonnull
	public HDLManip setType(@Nonnull HDLManipType type) {
		type = validateType(type);
		final HDLManip res = new HDLManip(id, container, type, target, castTo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getTarget()}.
	 * 
	 * @param target
	 *            sets the new target of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLManip} with the updated target field.
	 */
	@Nonnull
	public HDLManip setTarget(@Nonnull HDLExpression target) {
		target = validateTarget(target);
		final HDLManip res = new HDLManip(id, container, type, target, castTo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getCastTo()}.
	 * 
	 * @param castTo
	 *            sets the new castTo of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLManip} with the updated castTo field.
	 */
	@Nonnull
	public HDLManip setCastTo(@Nullable HDLType castTo) {
		castTo = validateCastTo(castTo);
		final HDLManip res = new HDLManip(id, container, type, target, castTo, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLManip))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLManip other = (AbstractHDLManip) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (castTo == null) {
			if (other.castTo != null)
				return false;
		} else if (!castTo.equals(other.castTo))
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
		result = (prime * result) + ((target == null) ? 0 : target.hashCode());
		result = (prime * result) + ((castTo == null) ? 0 : castTo.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLManip()");
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLManipType.").append(type.name() + ")");
		}
		if (target != null) {
			sb.append(".setTarget(").append(target.toConstructionString(spacing + "\t")).append(")");
		}
		if (castTo != null) {
			sb.append(".setCastTo(").append(castTo.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateType(getType());
		validateTarget(getTarget());
		if (getTarget() != null) {
			getTarget().validateAllFields(this, checkResolve);
		}
		validateCastTo(getCastTo());
		if (getCastTo() != null) {
			getCastTo().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLManip, HDLClass.HDLExpression, HDLClass.HDLObject);
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
						if (target != null) {
							current = Iterators.concat(Iterators.forArray(target), target.deepIterator());
						}
						break;
					case 1:
						if (castTo != null) {
							current = Iterators.concat(Iterators.forArray(castTo), castTo.deepIterator());
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
						if (target != null) {
							current = Iterators.singletonIterator(target);
						}
						break;
					case 1:
						if (castTo != null) {
							current = Iterators.singletonIterator(castTo);
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