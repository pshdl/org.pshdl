package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLSwitchCaseStatement extends HDLCompound{
	/**
	 * Constructs a new instance of {@link AbstractHDLSwitchCaseStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 */
	public AbstractHDLSwitchCaseStatement(HDLObject container, HDLExpression label, ArrayList<HDLStatement> dos) {
		super(container);
		label=validateLabel(label);
		if (this.label!=null)
			this.label=(HDLExpression)label.setContainer(this);
		else
			this.label=null;
		dos=validateDos(dos);
		this.dos=new ArrayList<HDLStatement>(dos.size());
		for(HDLStatement newValue:dos){
			this.dos.add((HDLStatement)newValue.setContainer(this));
		}
	}
	public AbstractHDLSwitchCaseStatement() {
		super();
		this.label=null;
		this.dos=null;
	}
	protected final HDLExpression label;
	/**
	 * Get the label field.
	 * 
	 * @return the field
	 */
	public HDLExpression getLabel(){
			return label;
	}
	protected HDLExpression validateLabel(HDLExpression label){
		return label;
	}
	protected final ArrayList<HDLStatement> dos;
	/**
	 * Get the dos field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLStatement> getDos(){
			return (ArrayList<HDLStatement>) dos.clone();
	}
	protected ArrayList<HDLStatement> validateDos(ArrayList<HDLStatement> dos){
		if (dos==null)
			return new ArrayList<HDLStatement>();
		return dos;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLSwitchCaseStatement copy(){
			return new HDLSwitchCaseStatement(container, label, dos);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLSwitchCaseStatement} with the updated container field.
	 */
	public HDLSwitchCaseStatement setContainer(HDLObject container){
		this.container=container;
		return (HDLSwitchCaseStatement)this;
	}
	/**
	 * Setter for the field {@link #getLabel()}.
	 * 
	 * @param label
	 *            sets the new label of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLSwitchCaseStatement} with the updated label field.
	 */
	public HDLSwitchCaseStatement setLabel(HDLExpression label){
		return new HDLSwitchCaseStatement(container, label, dos);
	}
	/**
	 * Setter for the field {@link #getDos()}.
	 * 
	 * @param dos
	 *            sets the new dos of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLSwitchCaseStatement} with the updated dos field.
	 */
	public HDLSwitchCaseStatement setDos(ArrayList<HDLStatement> dos){
		return new HDLSwitchCaseStatement(container, label, dos);
	}
	
	/**
	 * Adds a new value to the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be added to the field {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated dos field.
	 */
	public HDLSwitchCaseStatement addDos(HDLStatement newDos){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.add(newDos);
		return new HDLSwitchCaseStatement(container, label, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be removed from the field {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated dos field.
	 */	
	public HDLSwitchCaseStatement removeDos(HDLStatement newDos){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.remove(newDos);
		return new HDLSwitchCaseStatement(container, label, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated dos field.
	 */	
	public HDLSwitchCaseStatement removeDos(int idx){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.remove(idx);
		return new HDLSwitchCaseStatement(container, label, dos);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLSwitchCaseStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLSwitchCaseStatement other = (AbstractHDLSwitchCaseStatement) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (dos == null) {
			if (other.dos != null)
				return false;
		} else if (!dos.equals(other.dos))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((dos == null) ? 0 : dos.hashCode());
		return result;
	}
}