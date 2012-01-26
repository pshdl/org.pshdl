package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLManip extends HDLExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 */
	public AbstractHDLManip(HDLObject container, HDLManipType type, HDLExpression target, HDLType castTo) {
		super(container);
		this.type=validateType(type);
		target=validateTarget(target);
		if (this.target!=null)
			this.target=(HDLExpression)target.setContainer(this);
		else
			this.target=null;
		castTo=validateCastTo(castTo);
		if (this.castTo!=null)
			this.castTo=(HDLType)castTo.setContainer(this);
		else
			this.castTo=null;
	}
	public AbstractHDLManip() {
		super();
		this.type=null;
		this.target=null;
		this.castTo=null;
	}
	protected final HDLManipType type;
	/**
	 * Get the type field.
	 * 
	 * @return the field
	 */
	public HDLManipType getType(){
			return type;
	}
	protected HDLManipType validateType(HDLManipType type){
		if (type==null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}
	protected final HDLExpression target;
	/**
	 * Get the target field.
	 * 
	 * @return the field
	 */
	public HDLExpression getTarget(){
			return target;
	}
	protected HDLExpression validateTarget(HDLExpression target){
		if (target==null)
			throw new IllegalArgumentException("The field target can not be null!");
		return target;
	}
	protected final HDLType castTo;
	/**
	 * Get the castTo field.
	 * 
	 * @return the field
	 */
	public HDLType getCastTo(){
			return castTo;
	}
	protected HDLType validateCastTo(HDLType castTo){
		return castTo;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLManip copy(){
			return new HDLManip(container, type, target, castTo);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLManip} with the updated container field.
	 */
	public HDLManip setContainer(HDLObject container){
		this.container=container;
		return (HDLManip)this;
	}
	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLManip} with the updated type field.
	 */
	public HDLManip setType(HDLManipType type){
		return new HDLManip(container, type, target, castTo);
	}
	/**
	 * Setter for the field {@link #getTarget()}.
	 * 
	 * @param target
	 *            sets the new target of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLManip} with the updated target field.
	 */
	public HDLManip setTarget(HDLExpression target){
		return new HDLManip(container, type, target, castTo);
	}
	/**
	 * Setter for the field {@link #getCastTo()}.
	 * 
	 * @param castTo
	 *            sets the new castTo of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLManip} with the updated castTo field.
	 */
	public HDLManip setCastTo(HDLType castTo){
		return new HDLManip(container, type, target, castTo);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLManip))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLManip other = (AbstractHDLManip) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		if (castTo == null) {
			if (other.castTo != null)
				return false;
		} else if (!castTo.equals(other.castTo))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + ((castTo == null) ? 0 : castTo.hashCode());
		return result;
	}
}