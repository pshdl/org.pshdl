package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLVariableRef extends HDLReference{
	/**
	 * Constructs a new instance of {@link AbstractHDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 */
	public AbstractHDLVariableRef(HDLObject container, HDLQualifiedName var, ArrayList<HDLExpression> array, ArrayList<HDLRange> bits) {
		super(container);
		this.var=validateVar(var);
		array=validateArray(array);
		this.array=new ArrayList<HDLExpression>(array.size());
		for(HDLExpression newValue:array){
			this.array.add((HDLExpression)newValue.setContainer(this));
		}
		bits=validateBits(bits);
		this.bits=new ArrayList<HDLRange>(bits.size());
		for(HDLRange newValue:bits){
			this.bits.add((HDLRange)newValue.setContainer(this));
		}
	}
	public AbstractHDLVariableRef() {
		super();
		this.var=null;
		this.array=null;
		this.bits=null;
	}
	protected final HDLQualifiedName var;
	public HDLVariable resolveVar(){
			return container.resolveVariable(var);
	}
	protected HDLQualifiedName validateVar(HDLQualifiedName var){
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
	protected final ArrayList<HDLRange> bits;
	/**
	 * Get the bits field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLRange> getBits(){
			return (ArrayList<HDLRange>) bits.clone();
	}
	protected ArrayList<HDLRange> validateBits(ArrayList<HDLRange> bits){
		if (bits==null)
			return new ArrayList<HDLRange>();
		return bits;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLVariableRef copy(){
			return new HDLVariableRef(container, var, array, bits);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLVariableRef} with the updated container field.
	 */
	public HDLVariableRef setContainer(HDLObject container){
		this.container=container;
		return (HDLVariableRef)this;
	}
	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariableRef} with the updated var field.
	 */
	public HDLVariableRef setVar(HDLQualifiedName var){
		return new HDLVariableRef(container, var, array, bits);
	}
	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariableRef} with the updated array field.
	 */
	public HDLVariableRef setArray(ArrayList<HDLExpression> array){
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array field.
	 */
	public HDLVariableRef addArray(HDLExpression newArray){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.add(newArray);
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array field.
	 */	
	public HDLVariableRef removeArray(HDLExpression newArray){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.remove(newArray);
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array field.
	 */	
	public HDLVariableRef removeArray(int idx){
		ArrayList<HDLExpression> array=(ArrayList<HDLExpression>)this.array.clone();
		array.remove(idx);
		return new HDLVariableRef(container, var, array, bits);
	}	
	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariableRef} with the updated bits field.
	 */
	public HDLVariableRef setBits(ArrayList<HDLRange> bits){
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits field.
	 */
	public HDLVariableRef addBits(HDLRange newBits){
		ArrayList<HDLRange> bits=(ArrayList<HDLRange>)this.bits.clone();
		bits.add(newBits);
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits field.
	 */	
	public HDLVariableRef removeBits(HDLRange newBits){
		ArrayList<HDLRange> bits=(ArrayList<HDLRange>)this.bits.clone();
		bits.remove(newBits);
		return new HDLVariableRef(container, var, array, bits);
	}
	
	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits field.
	 */	
	public HDLVariableRef removeBits(int idx){
		ArrayList<HDLRange> bits=(ArrayList<HDLRange>)this.bits.clone();
		bits.remove(idx);
		return new HDLVariableRef(container, var, array, bits);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariableRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLVariableRef other = (AbstractHDLVariableRef) obj;
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
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		result = prime * result + ((array == null) ? 0 : array.hashCode());
		result = prime * result + ((bits == null) ? 0 : bits.hashCode());
		return result;
	}
}