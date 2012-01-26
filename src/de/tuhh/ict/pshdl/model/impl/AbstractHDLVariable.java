package de.tuhh.ict.pshdl.model.impl;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import java.util.*;

@SuppressWarnings("all")
public abstract class AbstractHDLVariable extends HDLObject{
	/**
	 * Constructs a new instance of {@link AbstractHDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 */
	public AbstractHDLVariable(HDLObject container, String name, ArrayList<HDLAnnotation> annotations, ArrayList<HDLExpression> dimensions) {
		super(container);
		this.name=validateName(name);
		annotations=validateAnnotations(annotations);
		this.annotations=new ArrayList<HDLAnnotation>(annotations.size());
		for(HDLAnnotation newValue:annotations){
			this.annotations.add((HDLAnnotation)newValue.setContainer(this));
		}
		dimensions=validateDimensions(dimensions);
		this.dimensions=new ArrayList<HDLExpression>(dimensions.size());
		for(HDLExpression newValue:dimensions){
			this.dimensions.add((HDLExpression)newValue.setContainer(this));
		}
	}
	public AbstractHDLVariable() {
		super();
		this.name=null;
		this.annotations=null;
		this.dimensions=null;
	}
	protected final String name;
	/**
	 * Get the name field.
	 * 
	 * @return the field
	 */
	public String getName(){
			return name;
	}
	protected String validateName(String name){
		if (name==null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}
	protected final ArrayList<HDLAnnotation> annotations;
	/**
	 * Get the annotations field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLAnnotation> getAnnotations(){
			return (ArrayList<HDLAnnotation>) annotations.clone();
	}
	protected ArrayList<HDLAnnotation> validateAnnotations(ArrayList<HDLAnnotation> annotations){
		if (annotations==null)
			return new ArrayList<HDLAnnotation>();
		return annotations;
	}
	protected final ArrayList<HDLExpression> dimensions;
	/**
	 * Get the dimensions field.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public ArrayList<HDLExpression> getDimensions(){
			return (ArrayList<HDLExpression>) dimensions.clone();
	}
	protected ArrayList<HDLExpression> validateDimensions(ArrayList<HDLExpression> dimensions){
		if (dimensions==null)
			return new ArrayList<HDLExpression>();
		return dimensions;
	}
	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	public HDLVariable copy(){
			return new HDLVariable(container, name, annotations, dimensions);
	}
	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return 
	 *			  the same instance of {@link HDLVariable} with the updated container field.
	 */
	public HDLVariable setContainer(HDLObject container){
		this.container=container;
		return (HDLVariable)this;
	}
	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariable} with the updated name field.
	 */
	public HDLVariable setName(String name){
		return new HDLVariable(container, name, annotations, dimensions);
	}
	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariable} with the updated annotations field.
	 */
	public HDLVariable setAnnotations(ArrayList<HDLAnnotation> annotations){
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated annotations field.
	 */
	public HDLVariable addAnnotations(HDLAnnotation newAnnotations){
		ArrayList<HDLAnnotation> annotations=(ArrayList<HDLAnnotation>)this.annotations.clone();
		annotations.add(newAnnotations);
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated annotations field.
	 */	
	public HDLVariable removeAnnotations(HDLAnnotation newAnnotations){
		ArrayList<HDLAnnotation> annotations=(ArrayList<HDLAnnotation>)this.annotations.clone();
		annotations.remove(newAnnotations);
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated annotations field.
	 */	
	public HDLVariable removeAnnotations(int idx){
		ArrayList<HDLAnnotation> annotations=(ArrayList<HDLAnnotation>)this.annotations.clone();
		annotations.remove(idx);
		return new HDLVariable(container, name, annotations, dimensions);
	}	
	/**
	 * Setter for the field {@link #getDimensions()}.
	 * 
	 * @param dimensions
	 *            sets the new dimensions of this object. Can be <code>null</code>.
	 * @return 
	 *			  a new instance of {@link HDLVariable} with the updated dimensions field.
	 */
	public HDLVariable setDimensions(ArrayList<HDLExpression> dimensions){
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Adds a new value to the field {@link #getDimensions()}.
	 * 
	 * @param newDimensions
	 *            the value that should be added to the field {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions field.
	 */
	public HDLVariable addDimensions(HDLExpression newDimensions){
		ArrayList<HDLExpression> dimensions=(ArrayList<HDLExpression>)this.dimensions.clone();
		dimensions.add(newDimensions);
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Removes a value from the field {@link #getDimensions()}.
	 * 
	 * @param newDimensions
	 *            the value that should be removed from the field {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions field.
	 */	
	public HDLVariable removeDimensions(HDLExpression newDimensions){
		ArrayList<HDLExpression> dimensions=(ArrayList<HDLExpression>)this.dimensions.clone();
		dimensions.remove(newDimensions);
		return new HDLVariable(container, name, annotations, dimensions);
	}
	
	/**
	 * Removes a value from the field {@link #getDimensions()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions field.
	 */	
	public HDLVariable removeDimensions(int idx){
		ArrayList<HDLExpression> dimensions=(ArrayList<HDLExpression>)this.dimensions.clone();
		dimensions.remove(idx);
		return new HDLVariable(container, name, annotations, dimensions);
	}	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariable))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLVariable other = (AbstractHDLVariable) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		if (dimensions == null) {
			if (other.dimensions != null)
				return false;
		} else if (!dimensions.equals(other.dimensions))
			return false;
		return true;
	}
	@Override
	public int hashCode() {
		int result = super.hashCode();
		final int prime = 31;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		result = prime * result + ((dimensions == null) ? 0 : dimensions.hashCode());
		return result;
	}
}