package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLTernary extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLTernary}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExpr
	 *            the value for ifExpr. Can <b>not</b> be <code>null</code>.
	 * @param thenExpr
	 *            the value for thenExpr. Can <b>not</b> be <code>null</code>.
	 * @param elseExpr
	 *            the value for elseExpr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLTernary(@Nullable IHDLObject container, @Nonnull HDLExpression ifExpr, @Nonnull HDLExpression thenExpr, @Nonnull HDLExpression elseExpr, boolean validate) {
		super(container, validate);
		if (validate) {
			ifExpr = validateIfExpr(ifExpr);
		}
		if (ifExpr != null) {
			this.ifExpr = ifExpr;
		} else {
			this.ifExpr = null;
		}
		if (validate) {
			thenExpr = validateThenExpr(thenExpr);
		}
		if (thenExpr != null) {
			this.thenExpr = thenExpr;
		} else {
			this.thenExpr = null;
		}
		if (validate) {
			elseExpr = validateElseExpr(elseExpr);
		}
		if (elseExpr != null) {
			this.elseExpr = elseExpr;
		} else {
			this.elseExpr = null;
		}
	}

	public AbstractHDLTernary() {
		super();
		this.ifExpr = null;
		this.thenExpr = null;
		this.elseExpr = null;
	}

	@Visit
	protected final HDLExpression ifExpr;

	/**
	 * Get the ifExpr field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getIfExpr() {
		return ifExpr;
	}

	protected HDLExpression validateIfExpr(HDLExpression ifExpr) {
		if (ifExpr == null)
			throw new IllegalArgumentException("The field ifExpr can not be null!");
		return ifExpr;
	}

	@Visit
	protected final HDLExpression thenExpr;

	/**
	 * Get the thenExpr field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getThenExpr() {
		return thenExpr;
	}

	protected HDLExpression validateThenExpr(HDLExpression thenExpr) {
		if (thenExpr == null)
			throw new IllegalArgumentException("The field thenExpr can not be null!");
		return thenExpr;
	}

	@Visit
	protected final HDLExpression elseExpr;

	/**
	 * Get the elseExpr field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getElseExpr() {
		return elseExpr;
	}

	protected HDLExpression validateElseExpr(HDLExpression elseExpr) {
		if (elseExpr == null)
			throw new IllegalArgumentException("The field elseExpr can not be null!");
		return elseExpr;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLTernary copy() {
		HDLTernary newObject = new HDLTernary(null, ifExpr, thenExpr, elseExpr, false);
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
	public HDLTernary copyFiltered(CopyFilter filter) {
		HDLExpression filteredifExpr = filter.copyObject("ifExpr", this, ifExpr);
		HDLExpression filteredthenExpr = filter.copyObject("thenExpr", this, thenExpr);
		HDLExpression filteredelseExpr = filter.copyObject("elseExpr", this, elseExpr);
		return filter.postFilter((HDLTernary) this, new HDLTernary(null, filteredifExpr, filteredthenExpr, filteredelseExpr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLTernary copyDeepFrozen(IHDLObject container) {
		HDLTernary copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLTernary} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLTernary setContainer(@Nullable IHDLObject container) {
		return (HDLTernary) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getIfExpr()}.
	 * 
	 * @param ifExpr
	 *            sets the new ifExpr of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated ifExpr
	 *         field.
	 */
	@Nonnull
	public HDLTernary setIfExpr(@Nonnull HDLExpression ifExpr) {
		ifExpr = validateIfExpr(ifExpr);
		HDLTernary res = new HDLTernary(container, ifExpr, thenExpr, elseExpr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getThenExpr()}.
	 * 
	 * @param thenExpr
	 *            sets the new thenExpr of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated thenExpr
	 *         field.
	 */
	@Nonnull
	public HDLTernary setThenExpr(@Nonnull HDLExpression thenExpr) {
		thenExpr = validateThenExpr(thenExpr);
		HDLTernary res = new HDLTernary(container, ifExpr, thenExpr, elseExpr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getElseExpr()}.
	 * 
	 * @param elseExpr
	 *            sets the new elseExpr of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLTernary} with the updated elseExpr
	 *         field.
	 */
	@Nonnull
	public HDLTernary setElseExpr(@Nonnull HDLExpression elseExpr) {
		elseExpr = validateElseExpr(elseExpr);
		HDLTernary res = new HDLTernary(container, ifExpr, thenExpr, elseExpr, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLTernary))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLTernary other = (AbstractHDLTernary) obj;
		if (ifExpr == null) {
			if (other.ifExpr != null)
				return false;
		} else if (!ifExpr.equals(other.ifExpr))
			return false;
		if (thenExpr == null) {
			if (other.thenExpr != null)
				return false;
		} else if (!thenExpr.equals(other.thenExpr))
			return false;
		if (elseExpr == null) {
			if (other.elseExpr != null)
				return false;
		} else if (!elseExpr.equals(other.elseExpr))
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
		result = (prime * result) + ((ifExpr == null) ? 0 : ifExpr.hashCode());
		result = (prime * result) + ((thenExpr == null) ? 0 : thenExpr.hashCode());
		result = (prime * result) + ((elseExpr == null) ? 0 : elseExpr.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLTernary()");
		if (ifExpr != null) {
			sb.append(".setIfExpr(").append(ifExpr.toConstructionString(spacing + "\t")).append(")");
		}
		if (thenExpr != null) {
			sb.append(".setThenExpr(").append(thenExpr.toConstructionString(spacing + "\t")).append(")");
		}
		if (elseExpr != null) {
			sb.append(".setElseExpr(").append(elseExpr.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateIfExpr(getIfExpr());
		if (getIfExpr() != null) {
			getIfExpr().validateAllFields(this, checkResolve);
		}
		validateThenExpr(getThenExpr());
		if (getThenExpr() != null) {
			getThenExpr().validateAllFields(this, checkResolve);
		}
		validateElseExpr(getElseExpr());
		if (getElseExpr() != null) {
			getElseExpr().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLTernary, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}