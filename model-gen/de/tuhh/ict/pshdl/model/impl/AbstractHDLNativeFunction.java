package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

@SuppressWarnings("all")
public abstract class AbstractHDLNativeFunction extends HDLFunction {
	/**
	 * Constructs a new instance of {@link AbstractHDLNativeFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param simOnly
	 *            the value for simOnly. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLNativeFunction(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, @Nonnull Boolean simOnly, boolean validate) {
		super(container, annotations, name, validate);
		if (validate) {
			simOnly = validateSimOnly(simOnly);
		}
		this.simOnly = simOnly;
	}

	public AbstractHDLNativeFunction() {
		super();
		this.simOnly = null;
	}

	protected final Boolean simOnly;

	/**
	 * Get the simOnly field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public Boolean getSimOnly() {
		return simOnly;
	}

	protected Boolean validateSimOnly(Boolean simOnly) {
		if (simOnly == null)
			throw new IllegalArgumentException("The field simOnly can not be null!");
		return simOnly;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction copy() {
		HDLNativeFunction newObject = new HDLNativeFunction(null, annotations, name, simOnly, false);
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
	public HDLNativeFunction copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		String filteredname = filter.copyObject("name", this, name);
		Boolean filteredsimOnly = filter.copyObject("simOnly", this, simOnly);
		return filter.postFilter((HDLNativeFunction) this, new HDLNativeFunction(null, filteredannotations, filteredname, filteredsimOnly, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction copyDeepFrozen(IHDLObject container) {
		HDLNativeFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLNativeFunction} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setContainer(@Nullable IHDLObject container) {
		return (HDLNativeFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLNativeFunction removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated name
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setName(@Nonnull String name) {
		name = validateName(name);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimOnly()}.
	 * 
	 * @param simOnly
	 *            sets the new simOnly of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         simOnly field.
	 */
	@Nonnull
	public HDLNativeFunction setSimOnly(@Nonnull Boolean simOnly) {
		simOnly = validateSimOnly(simOnly);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimOnly()}.
	 * 
	 * @param simOnly
	 *            sets the new simOnly of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         simOnly field.
	 */
	@Nonnull
	public HDLNativeFunction setSimOnly(boolean simOnly) {
		simOnly = validateSimOnly(simOnly);
		HDLNativeFunction res = new HDLNativeFunction(container, annotations, name, simOnly, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLNativeFunction))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLNativeFunction other = (AbstractHDLNativeFunction) obj;
		if (simOnly == null) {
			if (other.simOnly != null)
				return false;
		} else if (!simOnly.equals(other.simOnly))
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
		result = (prime * result) + ((simOnly == null) ? 0 : simOnly.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLNativeFunction()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (simOnly != null) {
			sb.append(".setSimOnly(").append(simOnly).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateSimOnly(getSimOnly());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLNativeFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}