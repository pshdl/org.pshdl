package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnumDeclaration extends HDLDeclaration{
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLEnumDeclaration(HDLObject container, HDLEnum hEnum) {
		super(container);
		hEnum=validateHEnum(hEnum);
		if (this.hEnum!=null)
			this.hEnum=(HDLEnum)hEnum.setContainer(this);
		else
			this.hEnum=null;
	}
	public AbstractHDLEnumDeclaration() {
		super();
		this.hEnum=null;
	}
	protected final HDLEnum hEnum;
	/**
	 * Get the hEnum field.
	 * 
	 * @return the field
	 */
	public HDLEnum getHEnum(){
			return hEnum;
	}
	protected HDLEnum validateHEnum(HDLEnum hEnum){
		if (hEnum==null)
			throw new IllegalArgumentException("The field hEnum can not be null!");
		return hEnum;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLEnumDeclaration copy(){
			return new HDLEnumDeclaration(container, hEnum);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLEnumDeclaration} with the updated container field.
	 */
	public HDLEnumDeclaration setContainer(HDLObject container){
		this.container=container;
		return (HDLEnumDeclaration)this;
	}
	/**
	 * Setter for the field {@link #getHEnum()}.
	 * 
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLEnumDeclaration} with the updated hEnum field.
	 */
	public HDLEnumDeclaration setHEnum(HDLEnum hEnum){
		return new HDLEnumDeclaration(container, hEnum);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnumDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnumDeclaration other = (AbstractHDLEnumDeclaration) obj;
		if (hEnum == null) {
			if (other.hEnum != null)
				return false;
		} else if (!hEnum.equals(other.hEnum))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((hEnum == null) ? 0 : hEnum.hashCode());
		return result;
	}
}