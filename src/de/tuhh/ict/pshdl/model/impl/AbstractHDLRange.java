package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLRange extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 */
	public AbstractHDLRange(HDLObject container, HDLExpression from, HDLExpression to) {
		super(container);
		from=validateFrom(from);
		if (this.from!=null)
			this.from=(HDLExpression)from.setContainer(this);
		else
			this.from=null;
		to=validateTo(to);
		if (this.to!=null)
			this.to=(HDLExpression)to.setContainer(this);
		else
			this.to=null;
	}
	public AbstractHDLRange() {
		super();
		this.from=null;
		this.to=null;
	}
	protected final HDLExpression from;
	/**
	 * Get the from field.
	 * 
	 * @return the field
	 */
	public HDLExpression getFrom(){
			return from;
	}
	protected HDLExpression validateFrom(HDLExpression from){
		return from;
	}
	protected final HDLExpression to;
	/**
	 * Get the to field.
	 * 
	 * @return the field
	 */
	public HDLExpression getTo(){
			return to;
	}
	protected HDLExpression validateTo(HDLExpression to){
		if (to==null)
			throw new IllegalArgumentException("The field to can not be null!");
		return to;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLRange copy(){
			return new HDLRange(container, from, to);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLRange} with the updated container field.
	 */
	public HDLRange setContainer(HDLObject container){
		this.container=container;
		return (HDLRange)this;
	}
	/**
	 * Setter for the field {@link #getFrom()}.
	 * 
	 * @param from
	 *            sets the new from of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLRange} with the updated from field.
	 */
	public HDLRange setFrom(HDLExpression from){
		return new HDLRange(container, from, to);
	}
	/**
	 * Setter for the field {@link #getTo()}.
	 * 
	 * @param to
	 *            sets the new to of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLRange} with the updated to field.
	 */
	public HDLRange setTo(HDLExpression to){
		return new HDLRange(container, from, to);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRange))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLRange other = (AbstractHDLRange) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}
}