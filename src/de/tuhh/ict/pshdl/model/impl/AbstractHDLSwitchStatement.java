package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLSwitchStatement extends HDLCompound{
	/**
	 * Constructs a new instance of {@link AbstractHDLSwitchStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 */
	public AbstractHDLSwitchStatement(HDLObject container, ArrayList<HDLSwitchCaseStatement> cases) {
		super(container);
		cases=validateCases(cases);
		this.cases=new ArrayList<HDLSwitchCaseStatement>(cases.size());
		for(HDLSwitchCaseStatement newValue:cases){
			this.cases.add((HDLSwitchCaseStatement)newValue.setContainer(this));
		}
	}
	public AbstractHDLSwitchStatement() {
		super();
		this.cases=null;
	}
	protected final ArrayList<HDLSwitchCaseStatement> cases;
	/**
	 * Get the cases field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLSwitchCaseStatement> getCases(){
			return (ArrayList<HDLSwitchCaseStatement>) cases.clone();
	}
	protected ArrayList<HDLSwitchCaseStatement> validateCases(ArrayList<HDLSwitchCaseStatement> cases){
		if (cases==null)
			return new ArrayList<HDLSwitchCaseStatement>();
		return cases;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLSwitchStatement copy(){
			return new HDLSwitchStatement(container, cases);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLSwitchStatement} with the updated container field.
	 */
	public HDLSwitchStatement setContainer(HDLObject container){
		this.container=container;
		return (HDLSwitchStatement)this;
	}
	/**
	 * Setter for the field {@link #getCases()}.
	 * 
	 * @param cases
	 *            sets the new cases of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	public HDLSwitchStatement setCases(ArrayList<HDLSwitchCaseStatement> cases){
		return new HDLSwitchStatement(container, cases);
	}
	
	/**
	 * Adds a new value to the field {@link #getCases()}.
	 * 
	 * @param newCases
	 *            the value that should be added to the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	public HDLSwitchStatement addCases(HDLSwitchCaseStatement newCases){
		ArrayList<HDLSwitchCaseStatement> cases=(ArrayList<HDLSwitchCaseStatement>)this.cases.clone();
		cases.add(newCases);
		return new HDLSwitchStatement(container, cases);
	}
	
	/**
	 * Removes a value from the field {@link #getCases()}.
	 * 
	 * @param newCases
	 *            the value that should be removed from the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */	
	public HDLSwitchStatement removeCases(HDLSwitchCaseStatement newCases){
		ArrayList<HDLSwitchCaseStatement> cases=(ArrayList<HDLSwitchCaseStatement>)this.cases.clone();
		cases.remove(newCases);
		return new HDLSwitchStatement(container, cases);
	}
	
	/**
	 * Removes a value from the field {@link #getCases()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */	
	public HDLSwitchStatement removeCases(int idx){
		ArrayList<HDLSwitchCaseStatement> cases=(ArrayList<HDLSwitchCaseStatement>)this.cases.clone();
		cases.remove(idx);
		return new HDLSwitchStatement(container, cases);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLSwitchStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLSwitchStatement other = (AbstractHDLSwitchStatement) obj;
		if (cases == null) {
			if (other.cases != null)
				return false;
		} else if (!cases.equals(other.cases))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((cases == null) ? 0 : cases.hashCode());
		return result;
	}
}