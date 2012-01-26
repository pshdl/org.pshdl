package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnumRef extends HDLReference{
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLEnumRef(HDLObject container, HDLQualifiedName hEnum, HDLQualifiedName var) {
		super(container);
		this.hEnum=validateHEnum(hEnum);
		this.var=validateVar(var);
	}
	public AbstractHDLEnumRef() {
		super();
		this.hEnum=null;
		this.var=null;
	}
	protected final HDLQualifiedName hEnum;
	public HDLEnum resolveHEnum(){
			return container.resolveEnum(hEnum);
	}
	protected HDLQualifiedName validateHEnum(HDLQualifiedName hEnum){
		if (hEnum==null)
			throw new IllegalArgumentException("The field hEnum can not be null!");
		return hEnum;
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
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLEnumRef copy(){
			return new HDLEnumRef(container, hEnum, var);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLEnumRef} with the updated container field.
	 */
	public HDLEnumRef setContainer(HDLObject container){
		this.container=container;
		return (HDLEnumRef)this;
	}
	/**
	 * Setter for the field {@link #getHEnum()}.
	 * 
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLEnumRef} with the updated hEnum field.
	 */
	public HDLEnumRef setHEnum(HDLQualifiedName hEnum){
		return new HDLEnumRef(container, hEnum, var);
	}
	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLEnumRef} with the updated var field.
	 */
	public HDLEnumRef setVar(HDLQualifiedName var){
		return new HDLEnumRef(container, hEnum, var);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnumRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnumRef other = (AbstractHDLEnumRef) obj;
		if (hEnum == null) {
			if (other.hEnum != null)
				return false;
		} else if (!hEnum.equals(other.hEnum))
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
		result = prime * result + ((hEnum == null) ? 0 : hEnum.hashCode());
		result = prime * result + ((var == null) ? 0 : var.hashCode());
		return result;
	}
}