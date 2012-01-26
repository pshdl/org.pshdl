package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnum extends HDLValueType{
	/**
	 * Constructs a new instance of {@link AbstractHDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public AbstractHDLEnum(HDLObject container, HDLRegisterConfig register, HDLDirection direction, String name, ArrayList<String> enums) {
		super(container, register, direction);
		this.name=validateName(name);
		enums=validateEnums(enums);
		this.enums=new ArrayList<String>(enums.size());
		for(String newValue:enums){
			this.enums.add(newValue);
		}
	}
	public AbstractHDLEnum() {
		super();
		this.name=null;
		this.enums=null;
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
	protected final ArrayList<String> enums;
	/**
	 * Get the enums field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<String> getEnums(){
			return (ArrayList<String>) enums.clone();
	}
	protected ArrayList<String> validateEnums(ArrayList<String> enums){
		if (enums==null)
			throw new IllegalArgumentException("The field enums can not be null!");
		if (enums.size()<1)
			throw new IllegalArgumentException("The field enums must contain at least one item!");
		return enums;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLEnum copy(){
			return new HDLEnum(container, register, direction, name, enums);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLEnum} with the updated container field.
	 */
	public HDLEnum setContainer(HDLObject container){
		this.container=container;
		return (HDLEnum)this;
	}
	/**
	 * Setter for the field {@link #getRegister()}.
	 * 
	 * @param register
	 *            sets the new register of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLEnum} with the updated register field.
	 */
	public HDLEnum setRegister(HDLRegisterConfig register){
		return new HDLEnum(container, register, direction, name, enums);
	}
	/**
	 * Setter for the field {@link #getDirection()}.
	 * 
	 * @param direction
	 *            sets the new direction of this object. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 * @return 
	 *			  a new instance of {@link HDLEnum} with the updated direction field.
	 */
	public HDLEnum setDirection(HDLDirection direction){
		return new HDLEnum(container, register, direction, name, enums);
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLEnum} with the updated name field.
	 */
	public HDLEnum setName(String name){
		return new HDLEnum(container, register, direction, name, enums);
	}
	/**
	 * Setter for the field {@link #getEnums()}.
	 * 
	 * @param enums
	 *            sets the new enums of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @return 
	 *			  a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public HDLEnum setEnums(ArrayList<String> enums){
		return new HDLEnum(container, register, direction, name, enums);
	}
	
	/**
	 * Adds a new value to the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be added to the field {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public HDLEnum addEnums(String newEnums){
		ArrayList<String> enums=(ArrayList<String>)this.enums.clone();
		enums.add(newEnums);
		return new HDLEnum(container, register, direction, name, enums);
	}
	
	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be removed from the field {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */	
	public HDLEnum removeEnums(String newEnums){
		ArrayList<String> enums=(ArrayList<String>)this.enums.clone();
		enums.remove(newEnums);
		return new HDLEnum(container, register, direction, name, enums);
	}
	
	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */	
	public HDLEnum removeEnums(int idx){
		ArrayList<String> enums=(ArrayList<String>)this.enums.clone();
		enums.remove(idx);
		return new HDLEnum(container, register, direction, name, enums);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnum))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnum other = (AbstractHDLEnum) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (enums == null) {
			if (other.enums != null)
				return false;
		} else if (!enums.equals(other.enums))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((enums == null) ? 0 : enums.hashCode());
		return result;
	}
}