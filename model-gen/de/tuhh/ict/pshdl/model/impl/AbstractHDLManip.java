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
package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

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
	public AbstractHDLManip(@Nullable IHDLObject container, @Nonnull HDLManipType type, @Nonnull HDLExpression target, @Nullable HDLType castTo, boolean validate) {
		super(container, validate);
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

	@Visit
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

	@Visit
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
		HDLManip newObject = new HDLManip(null, type, target, castTo, false);
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
		HDLManipType filteredtype = filter.copyObject("type", this, type);
		HDLExpression filteredtarget = filter.copyObject("target", this, target);
		HDLType filteredcastTo = filter.copyObject("castTo", this, castTo);
		return filter.postFilter((HDLManip) this, new HDLManip(null, filteredtype, filteredtarget, filteredcastTo, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLManip copyDeepFrozen(IHDLObject container) {
		HDLManip copy = copyFiltered(CopyFilter.DEEP_META);
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
		HDLManip res = new HDLManip(container, type, target, castTo, false);
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
		HDLManip res = new HDLManip(container, type, target, castTo, false);
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
		HDLManip res = new HDLManip(container, type, target, castTo, false);
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
		AbstractHDLManip other = (AbstractHDLManip) obj;
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

	private static Integer hashCache;

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
		boolean first = true;
		StringBuilder sb = new StringBuilder();
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
}
