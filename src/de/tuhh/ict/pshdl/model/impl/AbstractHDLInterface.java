package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterface extends HDLType{
	/**
	 * Constructs a new instance of {@link AbstractHDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 */
	public AbstractHDLInterface(HDLObject container, String name, ArrayList<HDLVariable> ports) {
		super(container);
		this.name=validateName(name);
		ports=validatePorts(ports);
		this.ports=new ArrayList<HDLVariable>(ports.size());
		for(HDLVariable newValue:ports){
			this.ports.add((HDLVariable)newValue.setContainer(this));
		}
	}
	public AbstractHDLInterface() {
		super();
		this.name=null;
		this.ports=null;
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
	protected final ArrayList<HDLVariable> ports;
	/**
	 * Get the ports field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLVariable> getPorts(){
			return (ArrayList<HDLVariable>) ports.clone();
	}
	protected ArrayList<HDLVariable> validatePorts(ArrayList<HDLVariable> ports){
		if (ports==null)
			return new ArrayList<HDLVariable>();
		return ports;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLInterface copy(){
			return new HDLInterface(container, name, ports);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLInterface} with the updated container field.
	 */
	public HDLInterface setContainer(HDLObject container){
		this.container=container;
		return (HDLInterface)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterface} with the updated name field.
	 */
	public HDLInterface setName(String name){
		return new HDLInterface(container, name, ports);
	}
	/**
	 * Setter for the field {@link #getPorts()}.
	 * 
	 * @param ports
	 *            sets the new ports of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterface} with the updated ports field.
	 */
	public HDLInterface setPorts(ArrayList<HDLVariable> ports){
		return new HDLInterface(container, name, ports);
	}
	
	/**
	 * Adds a new value to the field {@link #getPorts()}.
	 * 
	 * @param newPorts
	 *            the value that should be added to the field {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports field.
	 */
	public HDLInterface addPorts(HDLVariable newPorts){
		ArrayList<HDLVariable> ports=(ArrayList<HDLVariable>)this.ports.clone();
		ports.add(newPorts);
		return new HDLInterface(container, name, ports);
	}
	
	/**
	 * Removes a value from the field {@link #getPorts()}.
	 * 
	 * @param newPorts
	 *            the value that should be removed from the field {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports field.
	 */	
	public HDLInterface removePorts(HDLVariable newPorts){
		ArrayList<HDLVariable> ports=(ArrayList<HDLVariable>)this.ports.clone();
		ports.remove(newPorts);
		return new HDLInterface(container, name, ports);
	}
	
	/**
	 * Removes a value from the field {@link #getPorts()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports field.
	 */	
	public HDLInterface removePorts(int idx){
		ArrayList<HDLVariable> ports=(ArrayList<HDLVariable>)this.ports.clone();
		ports.remove(idx);
		return new HDLInterface(container, name, ports);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterface))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterface other = (AbstractHDLInterface) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (ports == null) {
			if (other.ports != null)
				return false;
		} else if (!ports.equals(other.ports))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((ports == null) ? 0 : ports.hashCode());
		return result;
	}
}