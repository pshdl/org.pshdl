package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLEnumDeclaration extends HDLDeclaration {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnumDeclaration(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @Nonnull HDLEnum hEnum, boolean validate) {
		super(container, annotations, validate);
		if (validate) {
			hEnum = validateHEnum(hEnum);
		}
		if (hEnum != null) {
			this.hEnum = (HDLEnum) hEnum;
		} else {
			this.hEnum = null;
		}
	}

	public AbstractHDLEnumDeclaration() {
		super();
		this.hEnum = null;
	}

	@Visit
	protected final HDLEnum hEnum;

	/**
	 * Get the hEnum field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLEnum getHEnum() {
		return hEnum;
	}

	protected HDLEnum validateHEnum(HDLEnum hEnum) {
		if (hEnum == null)
			throw new IllegalArgumentException("The field hEnum can not be null!");
		return hEnum;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnumDeclaration copy() {
		HDLEnumDeclaration newObject = new HDLEnumDeclaration(null, annotations, hEnum, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnumDeclaration copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		HDLEnum filteredhEnum = filter.copyObject("hEnum", this, hEnum);
		return filter.postFilter((HDLEnumDeclaration) this, new HDLEnumDeclaration(null, filteredannotations, filteredhEnum, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnumDeclaration copyDeepFrozen(IHDLObject container) {
		HDLEnumDeclaration copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLEnumDeclaration} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLEnumDeclaration setContainer(@Nullable IHDLObject container) {
		return (HDLEnumDeclaration) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	public @Nonnull
	HDLEnumDeclaration setAnnotations(@Nullable ArrayList<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	public @Nonnull
	HDLEnumDeclaration addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	public @Nonnull
	HDLEnumDeclaration removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	public @Nonnull
	HDLEnumDeclaration removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLEnumDeclaration res = new HDLEnumDeclaration(container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHEnum()}.
	 * 
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         hEnum field.
	 */
	public @Nonnull
	HDLEnumDeclaration setHEnum(@Nonnull HDLEnum hEnum) {
		hEnum = validateHEnum(hEnum);
		HDLEnumDeclaration res = new HDLEnumDeclaration(container, annotations, hEnum, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnumDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnumDeclaration other = (AbstractHDLEnumDeclaration) obj;
		if (hEnum == null) {
			if (other.hEnum != null)
				return false;
		} else if (!hEnum.equals(other.hEnum))
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
		result = (prime * result) + ((hEnum == null) ? 0 : hEnum.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnumDeclaration()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (hEnum != null) {
			sb.append(".setHEnum(").append(hEnum.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHEnum(getHEnum());
		if (getHEnum() != null) {
			getHEnum().validateAllFields(this, checkResolve);
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnumDeclaration, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}