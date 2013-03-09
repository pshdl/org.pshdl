package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.utils.*;

@SuppressWarnings("all")
public abstract class AbstractHDLBitOp extends HDLOpExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLBitOp}
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
	public AbstractHDLBitOp(@Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLBitOpType type, boolean validate) {
		super(container, left, right, validate);
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
	}

	public AbstractHDLBitOp() {
		super();
		this.type = null;
	}

	protected final HDLBitOpType type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Override
	@Nonnull
	public HDLBitOpType getType() {
		return type;
	}

	protected HDLBitOpType validateType(HDLBitOpType type) {
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
	public HDLBitOp copy() {
		HDLBitOp newObject = new HDLBitOp(null, left, right, type, false);
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
	public HDLBitOp copyFiltered(CopyFilter filter) {
		HDLExpression filteredleft = filter.copyObject("left", this, left);
		HDLExpression filteredright = filter.copyObject("right", this, right);
		HDLBitOpType filteredtype = filter.copyObject("type", this, type);
		return filter.postFilter((HDLBitOp) this, new HDLBitOp(null, filteredleft, filteredright, filteredtype, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLBitOp copyDeepFrozen(IHDLObject container) {
		HDLBitOp copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLBitOp} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLBitOp setContainer(@Nullable IHDLObject container) {
		return (HDLBitOp) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLeft()}.
	 * 
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBitOp} with the updated left field.
	 */
	@Override
	@Nonnull
	public HDLBitOp setLeft(@Nonnull HDLExpression left) {
		left = validateLeft(left);
		HDLBitOp res = new HDLBitOp(container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRight()}.
	 * 
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBitOp} with the updated right field.
	 */
	@Override
	@Nonnull
	public HDLBitOp setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		HDLBitOp res = new HDLBitOp(container, left, right, type, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBitOp} with the updated type field.
	 */
	@Nonnull
	public HDLBitOp setType(@Nonnull HDLBitOpType type) {
		type = validateType(type);
		HDLBitOp res = new HDLBitOp(container, left, right, type, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLBitOp))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLBitOp other = (AbstractHDLBitOp) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
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
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLBitOp()");
		if (left != null) {
			sb.append(".setLeft(").append(left.toConstructionString(spacing + "\t")).append(")");
		}
		if (right != null) {
			sb.append(".setRight(").append(right.toConstructionString(spacing + "\t")).append(")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(HDLBitOpType.").append(type.name() + ")");
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
		return EnumSet.of(HDLClass.HDLBitOp, HDLClass.HDLOpExpression, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}