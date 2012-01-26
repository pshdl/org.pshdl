package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLDirectGeneration extends HDLInstantiation{
	/**
	 * Constructs a new instance of {@link AbstractHDLDirectGeneration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>.
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 */
	public AbstractHDLDirectGeneration(HDLObject container, String generatorID, String generatorContent, ArrayList<HDLGeneratorArgument> arguments) {
		super(container);
		this.generatorID=validateGeneratorID(generatorID);
		this.generatorContent=validateGeneratorContent(generatorContent);
		arguments=validateArguments(arguments);
		this.arguments=new ArrayList<HDLGeneratorArgument>(arguments.size());
		for(HDLGeneratorArgument newValue:arguments){
			this.arguments.add((HDLGeneratorArgument)newValue.setContainer(this));
		}
	}
	public AbstractHDLDirectGeneration() {
		super();
		this.generatorID=null;
		this.generatorContent=null;
		this.arguments=null;
	}
	protected final String generatorID;
	/**
	 * Get the generatorID field.
	 * 
	 * @return the field
	 */
	public String getGeneratorID(){
			return generatorID;
	}
	protected String validateGeneratorID(String generatorID){
		if (generatorID==null)
			throw new IllegalArgumentException("The field generatorID can not be null!");
		return generatorID;
	}
	protected final String generatorContent;
	/**
	 * Get the generatorContent field.
	 * 
	 * @return the field
	 */
	public String getGeneratorContent(){
			return generatorContent;
	}
	protected String validateGeneratorContent(String generatorContent){
		if (generatorContent==null)
			throw new IllegalArgumentException("The field generatorContent can not be null!");
		return generatorContent;
	}
	protected final ArrayList<HDLGeneratorArgument> arguments;
	/**
	 * Get the arguments field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLGeneratorArgument> getArguments(){
			return (ArrayList<HDLGeneratorArgument>) arguments.clone();
	}
	protected ArrayList<HDLGeneratorArgument> validateArguments(ArrayList<HDLGeneratorArgument> arguments){
		if (arguments==null)
			return new ArrayList<HDLGeneratorArgument>();
		return arguments;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLDirectGeneration copy(){
			return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLDirectGeneration} with the updated container field.
	 */
	public HDLDirectGeneration setContainer(HDLObject container){
		this.container=container;
		return (HDLDirectGeneration)this;
	}
	/**
	 * Setter for the field {@link #getGeneratorID()}.
	 * 
	 * @param generatorID
	 *            sets the new generatorID of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLDirectGeneration} with the updated generatorID field.
	 */
	public HDLDirectGeneration setGeneratorID(String generatorID){
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	/**
	 * Setter for the field {@link #getGeneratorContent()}.
	 * 
	 * @param generatorContent
	 *            sets the new generatorContent of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLDirectGeneration} with the updated generatorContent field.
	 */
	public HDLDirectGeneration setGeneratorContent(String generatorContent){
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	/**
	 * Setter for the field {@link #getArguments()}.
	 * 
	 * @param arguments
	 *            sets the new arguments of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLDirectGeneration} with the updated arguments field.
	 */
	public HDLDirectGeneration setArguments(ArrayList<HDLGeneratorArgument> arguments){
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	
	/**
	 * Adds a new value to the field {@link #getArguments()}.
	 * 
	 * @param newArguments
	 *            the value that should be added to the field {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated arguments field.
	 */
	public HDLDirectGeneration addArguments(HDLGeneratorArgument newArguments){
		ArrayList<HDLGeneratorArgument> arguments=(ArrayList<HDLGeneratorArgument>)this.arguments.clone();
		arguments.add(newArguments);
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	
	/**
	 * Removes a value from the field {@link #getArguments()}.
	 * 
	 * @param newArguments
	 *            the value that should be removed from the field {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated arguments field.
	 */	
	public HDLDirectGeneration removeArguments(HDLGeneratorArgument newArguments){
		ArrayList<HDLGeneratorArgument> arguments=(ArrayList<HDLGeneratorArgument>)this.arguments.clone();
		arguments.remove(newArguments);
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}
	
	/**
	 * Removes a value from the field {@link #getArguments()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated arguments field.
	 */	
	public HDLDirectGeneration removeArguments(int idx){
		ArrayList<HDLGeneratorArgument> arguments=(ArrayList<HDLGeneratorArgument>)this.arguments.clone();
		arguments.remove(idx);
		return new HDLDirectGeneration(container, generatorID, generatorContent, arguments);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLDirectGeneration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLDirectGeneration other = (AbstractHDLDirectGeneration) obj;
		if (generatorID == null) {
			if (other.generatorID != null)
				return false;
		} else if (!generatorID.equals(other.generatorID))
			return false;
		if (generatorContent == null) {
			if (other.generatorContent != null)
				return false;
		} else if (!generatorContent.equals(other.generatorContent))
			return false;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((generatorID == null) ? 0 : generatorID.hashCode());
		result = prime * result + ((generatorContent == null) ? 0 : generatorContent.hashCode());
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		return result;
	}
}