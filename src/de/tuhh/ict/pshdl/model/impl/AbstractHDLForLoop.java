package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLForLoop extends HDLCompound{
	/**
	 * Constructs a new instance of {@link AbstractHDLForLoop}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param range
	 *            the value for range. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @param param
	 *            the value for param. Can <b>not</b> be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 */
	public AbstractHDLForLoop(HDLObject container, ArrayList<HDLRange> range, HDLVariable param, ArrayList<HDLStatement> dos) {
		super(container);
		range=validateRange(range);
		this.range=new ArrayList<HDLRange>(range.size());
		for(HDLRange newValue:range){
			this.range.add((HDLRange)newValue.setContainer(this));
		}
		param=validateParam(param);
		if (this.param!=null)
			this.param=(HDLVariable)param.setContainer(this);
		else
			this.param=null;
		dos=validateDos(dos);
		this.dos=new ArrayList<HDLStatement>(dos.size());
		for(HDLStatement newValue:dos){
			this.dos.add((HDLStatement)newValue.setContainer(this));
		}
	}
	public AbstractHDLForLoop() {
		super();
		this.range=null;
		this.param=null;
		this.dos=null;
	}
	protected final ArrayList<HDLRange> range;
	/**
	 * Get the range field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLRange> getRange(){
			return (ArrayList<HDLRange>) range.clone();
	}
	protected ArrayList<HDLRange> validateRange(ArrayList<HDLRange> range){
		if (range==null)
			throw new IllegalArgumentException("The field range can not be null!");
		if (range.size()<1)
			throw new IllegalArgumentException("The field range must contain at least one item!");
		return range;
	}
	protected final HDLVariable param;
	/**
	 * Get the param field.
	 * 
	 * @return the field
	 */
	public HDLVariable getParam(){
			return param;
	}
	protected HDLVariable validateParam(HDLVariable param){
		if (param==null)
			throw new IllegalArgumentException("The field param can not be null!");
		return param;
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
			throw new IllegalArgumentException("The field dos can not be null!");
		if (dos.size()<1)
			throw new IllegalArgumentException("The field dos must contain at least one item!");
		return dos;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLForLoop copy(){
			return new HDLForLoop(container, range, param, dos);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLForLoop} with the updated container field.
	 */
	public HDLForLoop setContainer(HDLObject container){
		this.container=container;
		return (HDLForLoop)this;
	}
	/**
	 * Setter for the field {@link #getRange()}.
	 * 
	 * @param range
	 *            sets the new range of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @return 
	 *			  a new instance of {@link HDLForLoop} with the updated range field.
	 */
	public HDLForLoop setRange(ArrayList<HDLRange> range){
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Adds a new value to the field {@link #getRange()}.
	 * 
	 * @param newRange
	 *            the value that should be added to the field {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range field.
	 */
	public HDLForLoop addRange(HDLRange newRange){
		ArrayList<HDLRange> range=(ArrayList<HDLRange>)this.range.clone();
		range.add(newRange);
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getRange()}.
	 * 
	 * @param newRange
	 *            the value that should be removed from the field {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range field.
	 */	
	public HDLForLoop removeRange(HDLRange newRange){
		ArrayList<HDLRange> range=(ArrayList<HDLRange>)this.range.clone();
		range.remove(newRange);
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getRange()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range field.
	 */	
	public HDLForLoop removeRange(int idx){
		ArrayList<HDLRange> range=(ArrayList<HDLRange>)this.range.clone();
		range.remove(idx);
		return new HDLForLoop(container, range, param, dos);
	}	
	/**
	 * Setter for the field {@link #getParam()}.
	 * 
	 * @param param
	 *            sets the new param of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLForLoop} with the updated param field.
	 */
	public HDLForLoop setParam(HDLVariable param){
		return new HDLForLoop(container, range, param, dos);
	}
	/**
	 * Setter for the field {@link #getDos()}.
	 * 
	 * @param dos
	 *            sets the new dos of this object. Can <b>not</b> be <code>null</code>, additionally the collection must contain at least one element.
	 * @return 
	 *			  a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public HDLForLoop setDos(ArrayList<HDLStatement> dos){
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Adds a new value to the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be added to the field {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public HDLForLoop addDos(HDLStatement newDos){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.add(newDos);
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be removed from the field {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */	
	public HDLForLoop removeDos(HDLStatement newDos){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.remove(newDos);
		return new HDLForLoop(container, range, param, dos);
	}
	
	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */	
	public HDLForLoop removeDos(int idx){
		ArrayList<HDLStatement> dos=(ArrayList<HDLStatement>)this.dos.clone();
		dos.remove(idx);
		return new HDLForLoop(container, range, param, dos);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLForLoop))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLForLoop other = (AbstractHDLForLoop) obj;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
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
		result = prime * result + ((range == null) ? 0 : range.hashCode());
		result = prime * result + ((param == null) ? 0 : param.hashCode());
		result = prime * result + ((dos == null) ? 0 : dos.hashCode());
		return result;
	}
}