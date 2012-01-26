package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLLiteral extends HDLExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLLiteral(HDLObject container, String val) {
		super(container);
		this.val=validateVal(val);
	}
	public AbstractHDLLiteral() {
		super();
		this.val=null;
	}
	protected final String val;
	/**
	 * Get the val field.
	 * 
	 * @return the field
	 */
	public String getVal(){
			return val;
	}
	protected String validateVal(String val){
		if (val==null)
			throw new IllegalArgumentException("The field val can not be null!");
		return val;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLLiteral copy(){
			return new HDLLiteral(container, val);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLLiteral} with the updated container field.
	 */
	public HDLLiteral setContainer(HDLObject container){
		this.container=container;
		return (HDLLiteral)this;
	}
	/**
	 * Setter for the field {@link #getVal()}.
	 * 
	 * @param val
	 *            sets the new val of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLLiteral} with the updated val field.
	 */
	public HDLLiteral setVal(String val){
		return new HDLLiteral(container, val);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLLiteral))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLLiteral other = (AbstractHDLLiteral) obj;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((val == null) ? 0 : val.hashCode());
		return result;
	}
}