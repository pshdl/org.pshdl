package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

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
	public AbstractHDLOpExpression(@Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, boolean validate) {
		super(container, validate);
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

	@Visit
	protected final HDLExpression left;

	/**
	 * Get the left field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLExpression getLeft() {
		return left;
	}

	protected HDLExpression validateLeft(HDLExpression left) {
		if (left == null)
			throw new IllegalArgumentException("The field left can not be null!");
		return left;
	}

	@Visit
	protected final HDLExpression right;

	/**
	 * Get the right field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLExpression getRight() {
		return right;
	}

	protected HDLExpression validateRight(HDLExpression right) {
		if (right == null)
			throw new IllegalArgumentException("The field right can not be null!");
		return right;
	}

	public abstract @Nonnull
	HDLOpExpression setLeft(@Nonnull HDLExpression left);

	public abstract @Nonnull
	HDLOpExpression setRight(@Nonnull HDLExpression right);

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
		AbstractHDLOpExpression other = (AbstractHDLOpExpression) obj;
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

	private static Integer hashCache;

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
		boolean first = true;
		StringBuilder sb = new StringBuilder();
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
}