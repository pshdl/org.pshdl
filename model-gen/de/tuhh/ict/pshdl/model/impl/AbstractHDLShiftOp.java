package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.utils.*;

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
	public AbstractHDLShiftOp(@Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLShiftOpType type, boolean validate) {
		super(container, left, right, validate);
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
	public @Nonnull
	HDLShiftOpType getType() {
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
	@Nonnull
	public HDLShiftOp copy() {
		HDLShiftOp newObject = new HDLShiftOp(null, left, right, type, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLShiftOp copyFiltered(CopyFilter filter) {
		HDLExpression filteredleft = filter.copyObject("left", this, left);
		HDLExpression filteredright = filter.copyObject("right", this, right);
		HDLShiftOpType filteredtype = filter.copyObject("type", this, type);
		return filter.postFilter((HDLShiftOp) this, new HDLShiftOp(null, filteredleft, filteredright, filteredtype, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLShiftOp copyDeepFrozen(IHDLObject container) {
		HDLShiftOp copy = copyFiltered(CopyFilter.DEEP_META);
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
	public @Nonnull
	HDLShiftOp setContainer(@Nullable IHDLObject container) {
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
	public @Nonnull
	HDLShiftOp setLeft(@Nonnull HDLExpression left) {
		left = validateLeft(left);
		HDLShiftOp res = new HDLShiftOp(container, left, right, type, false);
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
	public @Nonnull
	HDLShiftOp setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		HDLShiftOp res = new HDLShiftOp(container, left, right, type, false);
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
	public @Nonnull
	HDLShiftOp setType(@Nonnull HDLShiftOpType type) {
		type = validateType(type);
		HDLShiftOp res = new HDLShiftOp(container, left, right, type, false);
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
		AbstractHDLShiftOp other = (AbstractHDLShiftOp) obj;
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

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
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

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateType(getType());
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLShiftOp, HDLClass.HDLOpExpression, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}