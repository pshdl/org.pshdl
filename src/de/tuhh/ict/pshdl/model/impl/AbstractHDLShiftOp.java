package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLShiftOp extends HDLOpExpression{
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
	 */
	public AbstractHDLShiftOp(HDLObject container, HDLExpression left, HDLExpression right, HDLShiftOpType type) {
		super(container, left, right);
		this.type=validateType(type);
	}
	public AbstractHDLShiftOp() {
		super();
		this.type=null;
	}
	protected final HDLShiftOpType type;
	/**
	 * Get the type field.
	 * 
	 * @return the field
	 */
	public HDLShiftOpType getType(){
			return type;
	}
	protected HDLShiftOpType validateType(HDLShiftOpType type){
		if (type==null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLShiftOp copy(){
			return new HDLShiftOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLShiftOp} with the updated container field.
	 */
	public HDLShiftOp setContainer(HDLObject container){
		this.container=container;
		return (HDLShiftOp)this;
	}
	/**
	 * Setter for the field {@link #getLeft()}.
	 * 
	 * @param left
	 *            sets the new left of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLShiftOp} with the updated left field.
	 */
	public HDLShiftOp setLeft(HDLExpression left){
		return new HDLShiftOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getRight()}.
	 * 
	 * @param right
	 *            sets the new right of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLShiftOp} with the updated right field.
	 */
	public HDLShiftOp setRight(HDLExpression right){
		return new HDLShiftOp(container, left, right, type);
	}
	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLShiftOp} with the updated type field.
	 */
	public HDLShiftOp setType(HDLShiftOpType type){
		return new HDLShiftOp(container, left, right, type);
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
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
}