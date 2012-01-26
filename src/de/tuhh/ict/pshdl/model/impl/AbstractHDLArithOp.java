package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLArithOp extends HDLOpExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLArithOp}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLArithOp(HDLObject container, HDLExpression left, HDLExpression right, HDLArithOpType type) {
		super(container, left, right);
		this.type=validateType(type);
	}
	public AbstractHDLArithOp() {
		super();
		this.type=null;
	}
	protected final HDLArithOpType type;
	/**
	 * Get the type field.
	 * 
	 * @return the field
	 */
	public HDLArithOpType getType(){
			return type;
	}
	protected HDLArithOpType validateType(HDLArithOpType type){
		if (type==null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLArithOp copy(){
			return new HDLArithOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLArithOp} with the updated container field.
	 */
	public HDLArithOp setContainer(HDLObject container){
		this.container=container;
		return (HDLArithOp)this;
	}
	/**
	 * Setter for the field {@link #getLeft()}.
	 * 
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLArithOp} with the updated left field.
	 */
	public HDLArithOp setLeft(HDLExpression left){
		return new HDLArithOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getRight()}.
	 * 
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLArithOp} with the updated right field.
	 */
	public HDLArithOp setRight(HDLExpression right){
		return new HDLArithOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLArithOp} with the updated type field.
	 */
	public HDLArithOp setType(HDLArithOpType type){
		return new HDLArithOp(container, left, right, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLArithOp))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLArithOp other = (AbstractHDLArithOp) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
}