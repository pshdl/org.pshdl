package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLAssignment extends HDLStatement{
	/**
	 * Constructs a new instance of {@link AbstractHDLAssignment}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLAssignment(HDLObject container, HDLVariableRef left, HDLExpression right) {
		super(container);
		left=validateLeft(left);
		if (this.left!=null)
			this.left=(HDLVariableRef)left.setContainer(this);
		else
			this.left=null;
		right=validateRight(right);
		if (this.right!=null)
			this.right=(HDLExpression)right.setContainer(this);
		else
			this.right=null;
	}
	public AbstractHDLAssignment() {
		super();
		this.left=null;
		this.right=null;
	}
	protected final HDLVariableRef left;
	/**
	 * Get the left field.
	 * 
	 * @return the field
	 */
	public HDLVariableRef getLeft(){
			return left;
	}
	protected HDLVariableRef validateLeft(HDLVariableRef left){
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
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLAssignment copy(){
			return new HDLAssignment(container, left, right);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLAssignment} with the updated container field.
	 */
	public HDLAssignment setContainer(HDLObject container){
		this.container=container;
		return (HDLAssignment)this;
	}
	/**
	 * Setter for the field {@link #getLeft()}.
	 * 
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLAssignment} with the updated left field.
	 */
	public HDLAssignment setLeft(HDLVariableRef left){
		return new HDLAssignment(container, left, right);
	}
	/**
	 * Setter for the field {@link #getRight()}.
	 * 
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLAssignment} with the updated right field.
	 */
	public HDLAssignment setRight(HDLExpression right){
		return new HDLAssignment(container, left, right);
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