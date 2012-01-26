package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceDeclaration extends HDLDeclaration{
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLInterfaceDeclaration(HDLObject container, HDLInterface hIf) {
		super(container);
		hIf=validateHIf(hIf);
		if (this.hIf!=null)
			this.hIf=(HDLInterface)hIf.setContainer(this);
		else
			this.hIf=null;
	}
	public AbstractHDLInterfaceDeclaration() {
		super();
		this.hIf=null;
	}
	protected final HDLInterface hIf;
	/**
	 * Get the hIf field.
	 * 
	 * @return the field
	 */
	public HDLInterface getHIf(){
			return hIf;
	}
	protected HDLInterface validateHIf(HDLInterface hIf){
		if (hIf==null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLInterfaceDeclaration copy(){
			return new HDLInterfaceDeclaration(container, hIf);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLInterfaceDeclaration} with the updated container field.
	 */
	public HDLInterfaceDeclaration setContainer(HDLObject container){
		this.container=container;
		return (HDLInterfaceDeclaration)this;
	}
	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLInterfaceDeclaration} with the updated hIf field.
	 */
	public HDLInterfaceDeclaration setHIf(HDLInterface hIf){
		return new HDLInterfaceDeclaration(container, hIf);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceDeclaration other = (AbstractHDLInterfaceDeclaration) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((hIf == null) ? 0 : hIf.hashCode());
		return result;
	}
}