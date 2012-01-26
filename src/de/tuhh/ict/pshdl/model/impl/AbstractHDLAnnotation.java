package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLAnnotation extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLAnnotation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLAnnotation(HDLObject container, String name, String value) {
		super(container);
		this.name=validateName(name);
		this.value=validateValue(value);
	}
	public AbstractHDLAnnotation() {
		super();
		this.name=null;
		this.value=null;
	}
	protected final String name;
	/**
	 * Get the name field.
	 * 
	 * @return the field
	 */
	public String getName(){
			return name;
	}
	protected String validateName(String name){
		if (name==null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}
	protected final String value;
	/**
	 * Get the value field.
	 * 
	 * @return the field
	 */
	public String getValue(){
			return value;
	}
	protected String validateValue(String value){
		if (value==null)
			throw new IllegalArgumentException("The field value can not be null!");
		return value;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLAnnotation copy(){
			return new HDLAnnotation(container, name, value);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLAnnotation} with the updated container field.
	 */
	public HDLAnnotation setContainer(HDLObject container){
		this.container=container;
		return (HDLAnnotation)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLAnnotation} with the updated name field.
	 */
	public HDLAnnotation setName(String name){
		return new HDLAnnotation(container, name, value);
	}
	/**
	 * Setter for the field {@link #getValue()}.
	 * 
	 * @param value
	 *            sets the new value of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLAnnotation} with the updated value field.
	 */
	public HDLAnnotation setValue(String value){
		return new HDLAnnotation(container, name, value);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLAnnotation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLAnnotation other = (AbstractHDLAnnotation) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
}