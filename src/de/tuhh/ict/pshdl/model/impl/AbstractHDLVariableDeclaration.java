package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLVariableDeclaration extends HDLDeclaration{
	/**
	 * Constructs a new instance of {@link AbstractHDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public AbstractHDLVariableDeclaration(HDLObject container, HDLType type, ArrayList<HDLVariable> variables) {
		super(container);
		type=validateType(type);
		if (this.type!=null)
			this.type=(HDLType)type.setContainer(this);
		else
			this.type=null;
		variables=validateVariables(variables);
		this.variables=new ArrayList<HDLVariable>(variables.size());
		for(HDLVariable newValue:variables){
			this.variables.add((HDLVariable)newValue.setContainer(this));
		}
	}
	public AbstractHDLVariableDeclaration() {
		super();
		this.type=null;
		this.variables=null;
	}
	protected final HDLType type;
	/**
	 * Get the type field.
	 * 
	 * @return the field
	 */
	public HDLType getType(){
			return type;
	}
	protected HDLType validateType(HDLType type){
		if (type==null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}
	protected final ArrayList<HDLVariable> variables;
	/**
	 * Get the variables field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLVariable> getVariables(){
			return (ArrayList<HDLVariable>) variables.clone();
	}
	protected ArrayList<HDLVariable> validateVariables(ArrayList<HDLVariable> variables){
		if (variables==null)
			throw new IllegalArgumentException("The field variables can not be null!");
		if (variables.size()<1)
			throw new IllegalArgumentException("The field variables must contain at least one item!");
		return variables;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLVariableDeclaration copy(){
			return new HDLVariableDeclaration(container, type, variables);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLVariableDeclaration} with the updated container field.
	 */
	public HDLVariableDeclaration setContainer(HDLObject container){
		this.container=container;
		return (HDLVariableDeclaration)this;
	}
	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariableDeclaration} with the updated type field.
	 */
	public HDLVariableDeclaration setType(HDLType type){
		return new HDLVariableDeclaration(container, type, variables);
	}
	/**
	 * Setter for the field {@link #getVariables()}.
	 * 
	 * @param variables
	 *            sets the new variables of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @return 
	 *			  a new instance of {@link HDLVariableDeclaration} with the updated variables field.
	 */
	public HDLVariableDeclaration setVariables(ArrayList<HDLVariable> variables){
		return new HDLVariableDeclaration(container, type, variables);
	}
	
	/**
	 * Adds a new value to the field {@link #getVariables()}.
	 * 
	 * @param newVariables
	 *            the value that should be added to the field {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated variables field.
	 */
	public HDLVariableDeclaration addVariables(HDLVariable newVariables){
		ArrayList<HDLVariable> variables=(ArrayList<HDLVariable>)this.variables.clone();
		variables.add(newVariables);
		return new HDLVariableDeclaration(container, type, variables);
	}
	
	/**
	 * Removes a value from the field {@link #getVariables()}.
	 * 
	 * @param newVariables
	 *            the value that should be removed from the field {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated variables field.
	 */	
	public HDLVariableDeclaration removeVariables(HDLVariable newVariables){
		ArrayList<HDLVariable> variables=(ArrayList<HDLVariable>)this.variables.clone();
		variables.remove(newVariables);
		return new HDLVariableDeclaration(container, type, variables);
	}
	
	/**
	 * Removes a value from the field {@link #getVariables()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated variables field.
	 */	
	public HDLVariableDeclaration removeVariables(int idx){
		ArrayList<HDLVariable> variables=(ArrayList<HDLVariable>)this.variables.clone();
		variables.remove(idx);
		return new HDLVariableDeclaration(container, type, variables);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariableDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLVariableDeclaration other = (AbstractHDLVariableDeclaration) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}
}