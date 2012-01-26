package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLPrimitive extends HDLValueType{
	/**
	 * Constructs a new instance of {@link AbstractHDLPrimitive}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 */
	public AbstractHDLPrimitive(HDLObject container, HDLRegisterConfig register, HDLDirection direction, HDLPrimitiveType type, HDLExpression width) {
		super(container, register, direction);
		this.type=validateType(type);
		width=validateWidth(width);
		if (this.width!=null)
			this.width=(HDLExpression)width.setContainer(this);
		else
			this.width=null;
	}
	public AbstractHDLPrimitive() {
		super();
		this.type=null;
		this.width=null;
	}
	protected final HDLPrimitiveType type;
	/**
	 * Get the type field.
	 * 
	 * @return the field
	 */
	public HDLPrimitiveType getType(){
			return type;
	}
	protected HDLPrimitiveType validateType(HDLPrimitiveType type){
		if (type==null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}
	protected final HDLExpression width;
	/**
	 * Get the width field.
	 * 
	 * @return the field
	 */
	public HDLExpression getWidth(){
			return width;
	}
	protected HDLExpression validateWidth(HDLExpression width){
		return width;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLPrimitive copy(){
			return new HDLPrimitive(container, register, direction, type, width);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLPrimitive} with the updated container field.
	 */
	public HDLPrimitive setContainer(HDLObject container){
		this.container=container;
		return (HDLPrimitive)this;
	}
	/**
	 * Setter for the field {@link #getRegister()}.
	 * 
	 * @param register
	 *            sets the new register of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLPrimitive} with the updated register field.
	 */
	public HDLPrimitive setRegister(HDLRegisterConfig register){
		return new HDLPrimitive(container, register, direction, type, width);
	}
	/**
	 * Setter for the field {@link #getDirection()}.
	 * 
	 * @param direction
	 *            sets the new direction of this object. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @return 
	 *			  a new instance of {@link HDLPrimitive} with the updated direction field.
	 */
	public HDLPrimitive setDirection(HDLDirection direction){
		return new HDLPrimitive(container, register, direction, type, width);
	}
	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLPrimitive} with the updated type field.
	 */
	public HDLPrimitive setType(HDLPrimitiveType type){
		return new HDLPrimitive(container, register, direction, type, width);
	}
	/**
	 * Setter for the field {@link #getWidth()}.
	 * 
	 * @param width
	 *            sets the new width of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLPrimitive} with the updated width field.
	 */
	public HDLPrimitive setWidth(HDLExpression width){
		return new HDLPrimitive(container, register, direction, type, width);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLPrimitive))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLPrimitive other = (AbstractHDLPrimitive) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
		return result;
	}
}