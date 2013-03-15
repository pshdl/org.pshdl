package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnumRef extends HDLResolvedRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnumRef(@Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nonnull HDLQualifiedName hEnum, boolean validate) {
		super(container, var, validate);
		if (validate) {
			hEnum = validateHEnum(hEnum);
		}
		this.hEnum = hEnum;
	}

	public AbstractHDLEnumRef() {
		super();
		this.hEnum = null;
	}

	protected final HDLQualifiedName hEnum;

	@Nullable
	public Optional<HDLEnum> resolveHEnum() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveEnum(this, hEnum);
	}

	public HDLQualifiedName getHEnumRefName() {
		return hEnum;
	}

	protected HDLQualifiedName validateHEnum(HDLQualifiedName hEnum) {
		if (hEnum == null)
			throw new IllegalArgumentException("The field hEnum can not be null!");
		return hEnum;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumRef copy() {
		HDLEnumRef newObject = new HDLEnumRef(null, var, hEnum, false);
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
	public HDLEnumRef copyFiltered(CopyFilter filter) {
		HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		HDLQualifiedName filteredhEnum = filter.copyObject("hEnum", this, hEnum);
		return filter.postFilter((HDLEnumRef) this, new HDLEnumRef(null, filteredvar, filteredhEnum, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumRef copyDeepFrozen(IHDLObject container) {
		HDLEnumRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLEnumRef} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLEnumRef setContainer(@Nullable IHDLObject container) {
		return (HDLEnumRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumRef} with the updated var field.
	 */
	@Override
	@Nonnull
	public HDLEnumRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		HDLEnumRef res = new HDLEnumRef(container, var, hEnum, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHEnum()}.
	 * 
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumRef} with the updated hEnum
	 *         field.
	 */
	@Nonnull
	public HDLEnumRef setHEnum(@Nonnull HDLQualifiedName hEnum) {
		hEnum = validateHEnum(hEnum);
		HDLEnumRef res = new HDLEnumRef(container, var, hEnum, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnumRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnumRef other = (AbstractHDLEnumRef) obj;
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

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnumRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
		}
		if (hEnum != null) {
			sb.append(".setHEnum(HDLQualifiedName.create(\"").append(hEnum).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHEnum(getHEnumRefName());
		if (checkResolve && (getHEnumRefName() != null))
			if (!resolveHEnum().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHEnumRefName()));
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnumRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}