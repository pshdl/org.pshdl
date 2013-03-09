package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLAssignment extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>,
	 *            {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLAssignment(@Nullable IHDLObject container, @Nonnull HDLReference left, @Nullable HDLAssignmentType type, @Nonnull HDLExpression right, boolean validate) {
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

	@Visit
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
		if (left == null)
			throw new IllegalArgumentException("The field left can not be null!");
		return left;
	}

	protected final HDLAssignmentType type;

	/**
	 * Get the type field. If <code>null</code>, {@link HDLAssignmentType#ASSGN}
	 * is used as default.
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

	@Visit
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

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAssignment copy() {
		HDLAssignment newObject = new HDLAssignment(null, left, type, right, false);
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
		HDLReference filteredleft = filter.copyObject("left", this, left);
		HDLAssignmentType filteredtype = filter.copyObject("type", this, type);
		HDLExpression filteredright = filter.copyObject("right", this, right);
		return filter.postFilter((HDLAssignment) this, new HDLAssignment(null, filteredleft, filteredtype, filteredright, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLAssignment copyDeepFrozen(IHDLObject container) {
		HDLAssignment copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLAssignment} with the updated
	 *         container field.
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
	 *            sets the new left of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLAssignment} with the updated left
	 *         field.
	 */
	@Nonnull
	public HDLAssignment setLeft(@Nonnull HDLReference left) {
		left = validateLeft(left);
		HDLAssignment res = new HDLAssignment(container, left, type, right, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. If <code>null</code>,
	 *            {@link HDLAssignmentType#ASSGN} is used as default.
	 * @return a new instance of {@link HDLAssignment} with the updated type
	 *         field.
	 */
	@Nonnull
	public HDLAssignment setType(@Nullable HDLAssignmentType type) {
		type = validateType(type);
		HDLAssignment res = new HDLAssignment(container, left, type, right, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRight()}.
	 * 
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLAssignment} with the updated right
	 *         field.
	 */
	@Nonnull
	public HDLAssignment setRight(@Nonnull HDLExpression right) {
		right = validateRight(right);
		HDLAssignment res = new HDLAssignment(container, left, type, right, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLAssignment))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLAssignment other = (AbstractHDLAssignment) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
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
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((right == null) ? 0 : right.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
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
}