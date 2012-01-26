package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLConcat extends HDLExpression{
	/**
	 * Constructs a new instance of {@link AbstractHDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public AbstractHDLConcat(HDLObject container, ArrayList<HDLExpression> cats) {
		super(container);
		cats=validateCats(cats);
		this.cats=new ArrayList<HDLExpression>(cats.size());
		for(HDLExpression newValue:cats){
			this.cats.add((HDLExpression)newValue.setContainer(this));
		}
	}
	public AbstractHDLConcat() {
		super();
		this.cats=null;
	}
	protected final ArrayList<HDLExpression> cats;
	/**
	 * Get the cats field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLExpression> getCats(){
			return (ArrayList<HDLExpression>) cats.clone();
	}
	protected ArrayList<HDLExpression> validateCats(ArrayList<HDLExpression> cats){
		if (cats==null)
			throw new IllegalArgumentException("The field cats can not be null!");
		if (cats.size()<1)
			throw new IllegalArgumentException("The field cats must contain at least one item!");
		return cats;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLConcat copy(){
			return new HDLConcat(container, cats);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLConcat} with the updated container field.
	 */
	public HDLConcat setContainer(HDLObject container){
		this.container=container;
		return (HDLConcat)this;
	}
	/**
	 * Setter for the field {@link #getCats()}.
	 * 
	 * @param cats
	 *            sets the new cats of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @return 
	 *			  a new instance of {@link HDLConcat} with the updated cats field.
	 */
	public HDLConcat setCats(ArrayList<HDLExpression> cats){
		return new HDLConcat(container, cats);
	}
	
	/**
	 * Adds a new value to the field {@link #getCats()}.
	 * 
	 * @param newCats
	 *            the value that should be added to the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	public HDLConcat addCats(HDLExpression newCats){
		ArrayList<HDLExpression> cats=(ArrayList<HDLExpression>)this.cats.clone();
		cats.add(newCats);
		return new HDLConcat(container, cats);
	}
	
	/**
	 * Removes a value from the field {@link #getCats()}.
	 * 
	 * @param newCats
	 *            the value that should be removed from the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */	
	public HDLConcat removeCats(HDLExpression newCats){
		ArrayList<HDLExpression> cats=(ArrayList<HDLExpression>)this.cats.clone();
		cats.remove(newCats);
		return new HDLConcat(container, cats);
	}
	
	/**
	 * Removes a value from the field {@link #getCats()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */	
	public HDLConcat removeCats(int idx){
		ArrayList<HDLExpression> cats=(ArrayList<HDLExpression>)this.cats.clone();
		cats.remove(idx);
		return new HDLConcat(container, cats);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLConcat))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLConcat other = (AbstractHDLConcat) obj;
		if (cats == null) {
			if (other.cats != null)
				return false;
		} else if (!cats.equals(other.cats))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((cats == null) ? 0 : cats.hashCode());
		return result;
	}
}