package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLFunction extends HDLExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 */
	public AbstractHDLFunction(HDLObject container, String name, ArrayList<HDLExpression> params) {
		super(container);
		this.name=validateName(name);
		params=validateParams(params);
		this.params=new ArrayList<HDLExpression>(params.size());
		for(HDLExpression newValue:params){
			this.params.add((HDLExpression)newValue.setContainer(this));
		}
	}
	public AbstractHDLFunction() {
		super();
		this.name=null;
		this.params=null;
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
	protected final ArrayList<HDLExpression> params;
	/**
	 * Get the params field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLExpression> getParams(){
			return (ArrayList<HDLExpression>) params.clone();
	}
	protected ArrayList<HDLExpression> validateParams(ArrayList<HDLExpression> params){
		if (params==null)
			return new ArrayList<HDLExpression>();
		return params;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLFunction copy(){
			return new HDLFunction(container, name, params);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLFunction} with the updated container field.
	 */
	public HDLFunction setContainer(HDLObject container){
		this.container=container;
		return (HDLFunction)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLFunction} with the updated name field.
	 */
	public HDLFunction setName(String name){
		return new HDLFunction(container, name, params);
	}
	/**
	 * Setter for the field {@link #getParams()}.
	 * 
	 * @param params
	 *            sets the new params of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLFunction} with the updated params field.
	 */
	public HDLFunction setParams(ArrayList<HDLExpression> params){
		return new HDLFunction(container, name, params);
	}
	
	/**
	 * Adds a new value to the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be added to the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunction} with the updated params field.
	 */
	public HDLFunction addParams(HDLExpression newParams){
		ArrayList<HDLExpression> params=(ArrayList<HDLExpression>)this.params.clone();
		params.add(newParams);
		return new HDLFunction(container, name, params);
	}
	
	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunction} with the updated params field.
	 */	
	public HDLFunction removeParams(HDLExpression newParams){
		ArrayList<HDLExpression> params=(ArrayList<HDLExpression>)this.params.clone();
		params.remove(newParams);
		return new HDLFunction(container, name, params);
	}
	
	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getParams()}
	 * @return a new instance of {@link HDLFunction} with the updated params field.
	 */	
	public HDLFunction removeParams(int idx){
		ArrayList<HDLExpression> params=(ArrayList<HDLExpression>)this.params.clone();
		params.remove(idx);
		return new HDLFunction(container, name, params);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLFunction))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLFunction other = (AbstractHDLFunction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}
}