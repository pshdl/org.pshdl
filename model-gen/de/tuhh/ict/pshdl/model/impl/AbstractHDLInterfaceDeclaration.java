package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceDeclaration extends HDLDeclaration {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterfaceDeclaration(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @Nonnull HDLInterface hIf, boolean validate) {
		super(container, annotations, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		if (hIf != null) {
			this.hIf = (HDLInterface) hIf;
		} else {
			this.hIf = null;
		}
	}

	public AbstractHDLInterfaceDeclaration() {
		super();
		this.hIf = null;
	}

	@Visit
	protected final HDLInterface hIf;

	/**
	 * Get the hIf field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLInterface getHIf() {
		return hIf;
	}

	protected HDLInterface validateHIf(HDLInterface hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceDeclaration copy() {
		HDLInterfaceDeclaration newObject = new HDLInterfaceDeclaration(null, annotations, hIf, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceDeclaration copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		HDLInterface filteredhIf = filter.copyObject("hIf", this, hIf);
		return filter.postFilter((HDLInterfaceDeclaration) this, new HDLInterfaceDeclaration(null, filteredannotations, filteredhIf, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceDeclaration copyDeepFrozen(IHDLObject container) {
		HDLInterfaceDeclaration copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInterfaceDeclaration} with the
	 *         updated container field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration setContainer(@Nullable IHDLObject container) {
		return (HDLInterfaceDeclaration) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceDeclaration} with the
	 *         updated annotations field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration setAnnotations(@Nullable ArrayList<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLInterfaceDeclaration res = new HDLInterfaceDeclaration(container, annotations, hIf, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceDeclaration} with the
	 *         updated annotations field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLInterfaceDeclaration res = new HDLInterfaceDeclaration(container, annotations, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceDeclaration} with the
	 *         updated annotations field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLInterfaceDeclaration res = new HDLInterfaceDeclaration(container, annotations, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceDeclaration} with the
	 *         updated annotations field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLInterfaceDeclaration res = new HDLInterfaceDeclaration(container, annotations, hIf, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceDeclaration} with the
	 *         updated hIf field.
	 */
	public @Nonnull
	HDLInterfaceDeclaration setHIf(@Nonnull HDLInterface hIf) {
		hIf = validateHIf(hIf);
		HDLInterfaceDeclaration res = new HDLInterfaceDeclaration(container, annotations, hIf, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceDeclaration other = (AbstractHDLInterfaceDeclaration) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
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
		result = (prime * result) + ((hIf == null) ? 0 : hIf.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterfaceDeclaration()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (hIf != null) {
			sb.append(".setHIf(").append(hIf.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHIf(getHIf());
		if (getHIf() != null) {
			getHIf().validateAllFields(this, checkResolve);
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterfaceDeclaration, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}