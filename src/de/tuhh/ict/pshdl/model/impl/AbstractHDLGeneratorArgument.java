package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLGeneratorArgument extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLGeneratorArgument}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param expression
	 *            the value for expression. Can be <code>null</code>.
	 */
	public AbstractHDLGeneratorArgument(HDLObject container, String name, String value, HDLExpression expression) {
		super(container);
		this.name=validateName(name);
		this.value=validateValue(value);
		expression=validateExpression(expression);
		if (this.expression!=null)
			this.expression=(HDLExpression)expression.setContainer(this);
		else
			this.expression=null;
	}
	public AbstractHDLGeneratorArgument() {
		super();
		this.name=null;
		this.value=null;
		this.expression=null;
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
		return value;
	}
	protected final HDLExpression expression;
	/**
	 * Get the expression field.
	 * 
	 * @return the field
	 */
	public HDLExpression getExpression(){
			return expression;
	}
	protected HDLExpression validateExpression(HDLExpression expression){
		return expression;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLGeneratorArgument copy(){
			return new HDLGeneratorArgument(container, name, value, expression);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLGeneratorArgument} with the updated container field.
	 */
	public HDLGeneratorArgument setContainer(HDLObject container){
		this.container=container;
		return (HDLGeneratorArgument)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLGeneratorArgument} with the updated name field.
	 */
	public HDLGeneratorArgument setName(String name){
		return new HDLGeneratorArgument(container, name, value, expression);
	}
	/**
	 * Setter for the field {@link #getValue()}.
	 * 
	 * @param value
	 *            sets the new value of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLGeneratorArgument} with the updated value field.
	 */
	public HDLGeneratorArgument setValue(String value){
		return new HDLGeneratorArgument(container, name, value, expression);
	}
	/**
	 * Setter for the field {@link #getExpression()}.
	 * 
	 * @param expression
	 *            sets the new expression of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLGeneratorArgument} with the updated expression field.
	 */
	public HDLGeneratorArgument setExpression(HDLExpression expression){
		return new HDLGeneratorArgument(container, name, value, expression);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLGeneratorArgument))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLGeneratorArgument other = (AbstractHDLGeneratorArgument) obj;
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
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		return result;
	}
}