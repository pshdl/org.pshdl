package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLOpExpression extends HDLExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLOpExpression}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLOpExpression(HDLObject container, HDLExpression left, HDLExpression right) {
		super(container);
		left=validateLeft(left);
		if (this.left!=null)
			this.left=(HDLExpression)left.setContainer(this);
		else
			this.left=null;
		right=validateRight(right);
		if (this.right!=null)
			this.right=(HDLExpression)right.setContainer(this);
		else
			this.right=null;
	}
	public AbstractHDLOpExpression() {
		super();
		this.left=null;
		this.right=null;
	}
	protected final HDLExpression left;
	/**
	 * Get the left field.
	 * 
	 * @return the field
	 */
	public HDLExpression getLeft(){
			return left;
	}
	protected HDLExpression validateLeft(HDLExpression left){
		if (left==null)
			throw new IllegalArgumentException("The field left can not be null!");
		return left;
	}
	protected final HDLExpression right;
	/**
	 * Get the right field.
	 * 
	 * @return the field
	 */
	public HDLExpression getRight(){
			return right;
	}
	protected HDLExpression validateRight(HDLExpression right){
		if (right==null)
			throw new IllegalArgumentException("The field right can not be null!");
		return right;
	}
	public abstract HDLOpExpression setLeft(HDLExpression left);
	public abstract HDLOpExpression setRight(HDLExpression right);
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
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}
}