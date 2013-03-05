package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLDeclaration extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLDeclaration(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, boolean validate) {
		super(container, validate);
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

	public AbstractHDLDeclaration() {
		super();
		this.annotations = new ArrayList<HDLAnnotation>();
	}

	@Visit
	protected final ArrayList<HDLAnnotation> annotations;

	/**
	 * Get the annotations field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLAnnotation> getAnnotations() {
		return (ArrayList<HDLAnnotation>) annotations.clone();
	}

	protected ArrayList<HDLAnnotation> validateAnnotations(ArrayList<HDLAnnotation> annotations) {
		if (annotations == null)
			return new ArrayList<HDLAnnotation>();
		return annotations;
	}

	public abstract @Nonnull
	HDLDeclaration setAnnotations(@Nullable ArrayList<HDLAnnotation> annotations);

	public abstract @Nonnull
	HDLDeclaration addAnnotations(@Nullable HDLAnnotation annotations);

	public abstract @Nonnull
	HDLDeclaration removeAnnotations(@Nullable HDLAnnotation annotations);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLDeclaration copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLDeclaration copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLDeclaration copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLDeclaration other = (AbstractHDLDeclaration) obj;
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
		result = (prime * result) + ((annotations == null) ? 0 : annotations.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLDeclaration()");
		if (annotations != null)
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateAnnotations(getAnnotations());
		if (getAnnotations() != null) {
			for (HDLAnnotation o : getAnnotations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}