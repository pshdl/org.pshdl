package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLIfStatement extends HDLCompound{
	/**
	 * Constructs a new instance of {@link AbstractHDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 */
	public AbstractHDLIfStatement(HDLObject container, HDLExpression ifExp, ArrayList<HDLStatement> thenDo, ArrayList<HDLStatement> elseDo) {
		super(container);
		ifExp=validateIfExp(ifExp);
		if (this.ifExp!=null)
			this.ifExp=(HDLExpression)ifExp.setContainer(this);
		else
			this.ifExp=null;
		thenDo=validateThenDo(thenDo);
		this.thenDo=new ArrayList<HDLStatement>(thenDo.size());
		for(HDLStatement newValue:thenDo){
			this.thenDo.add((HDLStatement)newValue.setContainer(this));
		}
		elseDo=validateElseDo(elseDo);
		this.elseDo=new ArrayList<HDLStatement>(elseDo.size());
		for(HDLStatement newValue:elseDo){
			this.elseDo.add((HDLStatement)newValue.setContainer(this));
		}
	}
	public AbstractHDLIfStatement() {
		super();
		this.ifExp=null;
		this.thenDo=null;
		this.elseDo=null;
	}
	protected final HDLExpression ifExp;
	/**
	 * Get the ifExp field.
	 * 
	 * @return the field
	 */
	public HDLExpression getIfExp(){
			return ifExp;
	}
	protected HDLExpression validateIfExp(HDLExpression ifExp){
		if (ifExp==null)
			throw new IllegalArgumentException("The field ifExp can not be null!");
		return ifExp;
	}
	protected final ArrayList<HDLStatement> thenDo;
	/**
	 * Get the thenDo field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLStatement> getThenDo(){
			return (ArrayList<HDLStatement>) thenDo.clone();
	}
	protected ArrayList<HDLStatement> validateThenDo(ArrayList<HDLStatement> thenDo){
		if (thenDo==null)
			return new ArrayList<HDLStatement>();
		return thenDo;
	}
	protected final ArrayList<HDLStatement> elseDo;
	/**
	 * Get the elseDo field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLStatement> getElseDo(){
			return (ArrayList<HDLStatement>) elseDo.clone();
	}
	protected ArrayList<HDLStatement> validateElseDo(ArrayList<HDLStatement> elseDo){
		if (elseDo==null)
			return new ArrayList<HDLStatement>();
		return elseDo;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLIfStatement copy(){
			return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLIfStatement} with the updated container field.
	 */
	public HDLIfStatement setContainer(HDLObject container){
		this.container=container;
		return (HDLIfStatement)this;
	}
	/**
	 * Setter for the field {@link #getIfExp()}.
	 * 
	 * @param ifExp
	 *            sets the new ifExp of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLIfStatement} with the updated ifExp field.
	 */
	public HDLIfStatement setIfExp(HDLExpression ifExp){
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	/**
	 * Setter for the field {@link #getThenDo()}.
	 * 
	 * @param thenDo
	 *            sets the new thenDo of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	public HDLIfStatement setThenDo(ArrayList<HDLStatement> thenDo){
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Adds a new value to the field {@link #getThenDo()}.
	 * 
	 * @param newThenDo
	 *            the value that should be added to the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	public HDLIfStatement addThenDo(HDLStatement newThenDo){
		ArrayList<HDLStatement> thenDo=(ArrayList<HDLStatement>)this.thenDo.clone();
		thenDo.add(newThenDo);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 * 
	 * @param newThenDo
	 *            the value that should be removed from the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */	
	public HDLIfStatement removeThenDo(HDLStatement newThenDo){
		ArrayList<HDLStatement> thenDo=(ArrayList<HDLStatement>)this.thenDo.clone();
		thenDo.remove(newThenDo);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */	
	public HDLIfStatement removeThenDo(int idx){
		ArrayList<HDLStatement> thenDo=(ArrayList<HDLStatement>)this.thenDo.clone();
		thenDo.remove(idx);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}	
	/**
	 * Setter for the field {@link #getElseDo()}.
	 * 
	 * @param elseDo
	 *            sets the new elseDo of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	public HDLIfStatement setElseDo(ArrayList<HDLStatement> elseDo){
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Adds a new value to the field {@link #getElseDo()}.
	 * 
	 * @param newElseDo
	 *            the value that should be added to the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	public HDLIfStatement addElseDo(HDLStatement newElseDo){
		ArrayList<HDLStatement> elseDo=(ArrayList<HDLStatement>)this.elseDo.clone();
		elseDo.add(newElseDo);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 * 
	 * @param newElseDo
	 *            the value that should be removed from the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */	
	public HDLIfStatement removeElseDo(HDLStatement newElseDo){
		ArrayList<HDLStatement> elseDo=(ArrayList<HDLStatement>)this.elseDo.clone();
		elseDo.remove(newElseDo);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}
	
	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */	
	public HDLIfStatement removeElseDo(int idx){
		ArrayList<HDLStatement> elseDo=(ArrayList<HDLStatement>)this.elseDo.clone();
		elseDo.remove(idx);
		return new HDLIfStatement(container, ifExp, thenDo, elseDo);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLIfStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLIfStatement other = (AbstractHDLIfStatement) obj;
		if (ifExp == null) {
			if (other.ifExp != null)
				return false;
		} else if (!ifExp.equals(other.ifExp))
			return false;
		if (thenDo == null) {
			if (other.thenDo != null)
				return false;
		} else if (!thenDo.equals(other.thenDo))
			return false;
		if (elseDo == null) {
			if (other.elseDo != null)
				return false;
		} else if (!elseDo.equals(other.elseDo))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((ifExp == null) ? 0 : ifExp.hashCode());
		result = prime * result + ((thenDo == null) ? 0 : thenDo.hashCode());
		result = prime * result + ((elseDo == null) ? 0 : elseDo.hashCode());
		return result;
	}
}