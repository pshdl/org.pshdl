package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceInstantiation extends HDLInstantiation{
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLInterfaceInstantiation(HDLObject container, HDLQualifiedName hIf, HDLVariable var) {
		super(container);
		this.hIf=validateHIf(hIf);
		var=validateVar(var);
		if (this.var!=null)
			this.var=(HDLVariable)var.setContainer(this);
		else
			this.var=null;
	}
	public AbstractHDLInterfaceInstantiation() {
		super();
		this.hIf=null;
		this.var=null;
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
	protected final HDLVariable var;
	/**
	 * Get the var field.
	 * 
	 * @return the field
	 */
	public HDLVariable getVar(){
			return var;
	}
	protected HDLVariable validateVar(HDLVariable var){
		if (var==null)
			throw new IllegalArgumentException("The field var can not be null!");
		return var;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLInterfaceInstantiation copy(){
			return new HDLInterfaceInstantiation(container, hIf, var);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLInterfaceInstantiation} with the updated container field.
	 */
	public HDLInterfaceInstantiation setContainer(HDLObject container){
		this.container=container;
		return (HDLInterfaceInstantiation)this;
	}
	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceInstantiation} with the updated hIf field.
	 */
	public HDLInterfaceInstantiation setHIf(HDLQualifiedName hIf){
		return new HDLInterfaceInstantiation(container, hIf, var);
	}
	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceInstantiation} with the updated var field.
	 */
	public HDLInterfaceInstantiation setVar(HDLVariable var){
		return new HDLInterfaceInstantiation(container, hIf, var);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceInstantiation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceInstantiation other = (AbstractHDLInterfaceInstantiation) obj;
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
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((hIf == null) ? 0 : hIf.hashCode());
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		return result;
	}
}