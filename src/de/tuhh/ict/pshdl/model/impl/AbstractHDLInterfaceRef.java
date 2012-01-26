package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceRef extends HDLReference{
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 */
	public AbstractHDLInterfaceRef(HDLObject container, HDLQualifiedName hIf, HDLVariableRef var, ArrayList<HDLExpression> array) {
		super(container);
		this.hIf=validateHIf(hIf);
		var=validateVar(var);
		if (this.var!=null)
			this.var=(HDLVariableRef)var.setContainer(this);
		else
			this.var=null;
		array=validateArray(array);
		this.array=new ArrayList<HDLExpression>(array.size());
		for(HDLExpression newValue:array){
			this.array.add((HDLExpression)newValue.setContainer(this));
		}
	}
	public AbstractHDLInterfaceRef() {
		super();
		this.hIf=null;
		this.var=null;
		this.array=null;
	}
	protected final HDLQualifiedName hIf;
	public HDLInterface resolveHIf(){
			return container.resolveInterface(hIf);
	}
	protected HDLQualifiedName validateHIf(HDLQualifiedName hIf){
		if (hIf==null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}
	protected final HDLVariableRef var;
	/**
	 * Get the var field.
	 * 
	 * @return the field
	 */
	public HDLVariableRef getVar(){
			return var;
	}
	protected HDLVariableRef validateVar(HDLVariableRef var){
		if (var==null)
			throw new IllegalArgumentException("The field var can not be null!");
		return var;
	}
	protected final ArrayList<HDLExpression> array;
	/**
	 * Get the array field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLExpression> getArray(){
			return (ArrayList<HDLExpression>) array.clone();
	}
	protected ArrayList<HDLExpression> validateArray(ArrayList<HDLExpression> array){
		if (array==null)
			return new ArrayList<HDLExpression>();
		return array;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLInterfaceRef copy(){
			return new HDLInterfaceRef(container, hIf, var, array);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLInterfaceRef} with the updated container field.
	 */
	public HDLInterfaceRef setContainer(HDLObject container){
		this.container=container;
		return (HDLInterfaceRef)this;
	}
	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceRef} with the updated hIf field.
	 */
	public HDLInterfaceRef setHIf(HDLQualifiedName hIf){
		return new HDLInterfaceRef(container, hIf, var, array);
	}
	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceRef} with the updated var field.
	 */
	public HDLInterfaceRef setVar(HDLVariableRef var){
		return new HDLInterfaceRef(container, hIf, var, array);
	}
	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceRef} with the updated array field.
	 */
	public HDLInterfaceRef setArray(ArrayList<HDLExpression> array){
		return new HDLInterfaceRef(container, hIf, var, array);
	}
	
	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array field.
	 */
	public HDLInterfaceRef addArray(HDLExpression newArray){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.add(newArray);
		return new HDLInterfaceRef(container, hIf, var, array);
	}
	
	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array field.
	 */	
	public HDLInterfaceRef removeArray(HDLExpression newArray){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.remove(newArray);
		return new HDLInterfaceRef(container, hIf, var, array);
	}
	
	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array field.
	 */	
	public HDLInterfaceRef removeArray(int idx){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.remove(idx);
		return new HDLInterfaceRef(container, hIf, var, array);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceRef other = (AbstractHDLInterfaceRef) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
			return false;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((hIf == null) ? 0 : hIf.hashCode());
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		result = prime * result + ((array == null) ? 0 : array.hashCode());
		return result;
	}
}