package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLValueType.HDLDirection;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLValueType extends HDLType{
	/**
	 * Constructs a new instance of {@link AbstractHDLValueType}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>, {@link HDLDirection#INTERNAL} is used as default.
	 */
	public AbstractHDLValueType(HDLObject container, HDLRegisterConfig register, HDLDirection direction) {
		super(container);
		register=validateRegister(register);
		if (this.register!=null)
			this.register=(HDLRegisterConfig)register.setContainer(this);
		else
			this.register=null;
		this.direction=validateDirection(direction);
	}
	public AbstractHDLValueType() {
		super();
		this.register=null;
		this.direction=null;
	}
	protected final HDLRegisterConfig register;
	/**
	 * Get the register field.
	 * 
	 * @return the field
	 */
	public HDLRegisterConfig getRegister(){
			return register;
	}
	protected HDLRegisterConfig validateRegister(HDLRegisterConfig register){
		return register;
	}
	protected final HDLDirection direction;
	/**
	 * Get the direction field.
	 * 
	 * @return the field
	 */
	public HDLDirection getDirection(){
			return direction;
	}
	protected HDLDirection validateDirection(HDLDirection direction){
		return direction==null?HDLDirection.INTERNAL:direction;
	}
	public abstract HDLValueType setRegister(HDLRegisterConfig register);
	public abstract HDLValueType setDirection(HDLDirection direction);
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLValueType))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLValueType other = (AbstractHDLValueType) obj;
		if (register == null) {
			if (other.register != null)
				return false;
		} else if (!register.equals(other.register))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((register == null) ? 0 : register.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		return result;
	}
}