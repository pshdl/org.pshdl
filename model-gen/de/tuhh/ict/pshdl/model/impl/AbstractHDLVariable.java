/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLVariable extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLVariable}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dimensions
	 *            the value for dimensions. Can be <code>null</code>.
	 * @param defaultValue
	 *            the value for defaultValue. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLVariable(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dimensions, @Nullable HDLExpression defaultValue,
			@Nullable Iterable<HDLAnnotation> annotations, boolean validate) {
		super(container, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			dimensions = validateDimensions(dimensions);
		}
		this.dimensions = new ArrayList<HDLExpression>();
		if (dimensions != null) {
			for (HDLExpression newValue : dimensions) {
				this.dimensions.add(newValue);
			}
		}
		if (validate) {
			defaultValue = validateDefaultValue(defaultValue);
		}
		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		} else {
			this.defaultValue = null;
		}
		if (validate) {
			annotations = validateAnnotations(annotations);
		}
		this.annotations = new ArrayList<HDLAnnotation>();
		if (annotations != null) {
			for (HDLAnnotation newValue : annotations) {
				this.annotations.add(newValue);
			}
		}
	}

	public AbstractHDLVariable() {
		super();
		this.name = null;
		this.dimensions = new ArrayList<HDLExpression>();
		this.defaultValue = null;
		this.annotations = new ArrayList<HDLAnnotation>();
	}

	protected final String name;

	/**
	 * Get the name field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	protected String validateName(String name) {
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}

	@Visit
	protected final ArrayList<HDLExpression> dimensions;

	/**
	 * Get the dimensions field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getDimensions() {
		return (ArrayList<HDLExpression>) dimensions.clone();
	}

	protected Iterable<HDLExpression> validateDimensions(Iterable<HDLExpression> dimensions) {
		if (dimensions == null)
			return new ArrayList<HDLExpression>();
		return dimensions;
	}

	@Visit
	protected final HDLExpression defaultValue;

	/**
	 * Get the defaultValue field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLExpression getDefaultValue() {
		return defaultValue;
	}

	protected HDLExpression validateDefaultValue(HDLExpression defaultValue) {
		return defaultValue;
	}

	@Visit
	protected final ArrayList<HDLAnnotation> annotations;

	/**
	 * Get the annotations field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLAnnotation> getAnnotations() {
		return (ArrayList<HDLAnnotation>) annotations.clone();
	}

	protected Iterable<HDLAnnotation> validateAnnotations(Iterable<HDLAnnotation> annotations) {
		if (annotations == null)
			return new ArrayList<HDLAnnotation>();
		return annotations;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariable copy() {
		HDLVariable newObject = new HDLVariable(null, name, dimensions, defaultValue, annotations, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariable copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLExpression> filtereddimensions = filter.copyContainer("dimensions", this, dimensions);
		HDLExpression filtereddefaultValue = filter.copyObject("defaultValue", this, defaultValue);
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		return filter.postFilter((HDLVariable) this, new HDLVariable(null, filteredname, filtereddimensions, filtereddefaultValue, filteredannotations, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariable copyDeepFrozen(IHDLObject container) {
		HDLVariable copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLVariable} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLVariable setContainer(@Nullable IHDLObject container) {
		return (HDLVariable) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariable} with the updated name
	 *         field.
	 */
	@Nonnull
	public HDLVariable setName(@Nonnull String name) {
		name = validateName(name);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDimensions()}.
	 * 
	 * @param dimensions
	 *            sets the new dimensions of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariable} with the updated dimensions
	 *         field.
	 */
	@Nonnull
	public HDLVariable setDimensions(@Nullable Iterable<HDLExpression> dimensions) {
		dimensions = validateDimensions(dimensions);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDimensions()}.
	 * 
	 * @param newDimensions
	 *            the value that should be added to the field
	 *            {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions
	 *         field.
	 */
	@Nonnull
	public HDLVariable addDimensions(@Nullable HDLExpression newDimensions) {
		if (newDimensions == null)
			throw new IllegalArgumentException("Element of dimensions can not be null!");
		ArrayList<HDLExpression> dimensions = (ArrayList<HDLExpression>) this.dimensions.clone();
		dimensions.add(newDimensions);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDimensions()}.
	 * 
	 * @param newDimensions
	 *            the value that should be removed from the field
	 *            {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions
	 *         field.
	 */
	@Nonnull
	public HDLVariable removeDimensions(@Nullable HDLExpression newDimensions) {
		if (newDimensions == null)
			throw new IllegalArgumentException("Removed element of dimensions can not be null!");
		ArrayList<HDLExpression> dimensions = (ArrayList<HDLExpression>) this.dimensions.clone();
		dimensions.remove(newDimensions);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDimensions()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDimensions()}
	 * @return a new instance of {@link HDLVariable} with the updated dimensions
	 *         field.
	 */
	@Nonnull
	public HDLVariable removeDimensions(int idx) {
		ArrayList<HDLExpression> dimensions = (ArrayList<HDLExpression>) this.dimensions.clone();
		dimensions.remove(idx);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDefaultValue()}.
	 * 
	 * @param defaultValue
	 *            sets the new defaultValue of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariable} with the updated
	 *         defaultValue field.
	 */
	@Nonnull
	public HDLVariable setDefaultValue(@Nullable HDLExpression defaultValue) {
		defaultValue = validateDefaultValue(defaultValue);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariable} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLVariable setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLVariable addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLVariable removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariable} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLVariable removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLVariable res = new HDLVariable(container, name, dimensions, defaultValue, annotations, false);
		return res;
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
		if (dimensions == null) {
			if (other.dimensions != null)
				return false;
		} else if (!dimensions.equals(other.dimensions))
			return false;
		if (defaultValue == null) {
			if (other.defaultValue != null)
				return false;
		} else if (!defaultValue.equals(other.defaultValue))
			return false;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		return true;
	}

	private static Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((dimensions == null) ? 0 : dimensions.hashCode());
		result = (prime * result) + ((defaultValue == null) ? 0 : defaultValue.hashCode());
		result = (prime * result) + ((annotations == null) ? 0 : annotations.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLVariable()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (dimensions != null) {
			if (dimensions.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : dimensions) {
					sb.append(".addDimensions(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (defaultValue != null) {
			sb.append(".setDefaultValue(").append(defaultValue.toConstructionString(spacing + "\t")).append(")");
		}
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateDimensions(getDimensions());
		if (getDimensions() != null) {
			for (HDLExpression o : getDimensions()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateDefaultValue(getDefaultValue());
		if (getDefaultValue() != null) {
			getDefaultValue().validateAllFields(this, checkResolve);
		}
		validateAnnotations(getAnnotations());
		if (getAnnotations() != null) {
			for (HDLAnnotation o : getAnnotations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLVariable, HDLClass.HDLObject);
	}
}
