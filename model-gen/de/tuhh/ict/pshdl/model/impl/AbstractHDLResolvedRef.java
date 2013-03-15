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
public abstract class AbstractHDLResolvedRef extends HDLReference {
	/**
	 * Constructs a new instance of {@link AbstractHDLResolvedRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLResolvedRef(@Nullable IHDLObject container, @Nonnull HDLQualifiedName var, boolean validate) {
		super(container, validate);
		if (validate) {
			var = validateVar(var);
		}
		this.var = var;
	}

	public AbstractHDLResolvedRef() {
		super();
		this.var = null;
	}

	protected final HDLQualifiedName var;

	@Nullable
	public Optional<HDLVariable> resolveVar() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, var);
	}

	public HDLQualifiedName getVarRefName() {
		return var;
	}

	protected HDLQualifiedName validateVar(HDLQualifiedName var) {
		if (var == null)
			throw new IllegalArgumentException("The field var can not be null!");
		return var;
	}

	@Nonnull
	public abstract HDLResolvedRef setVar(@Nonnull HDLQualifiedName var);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLResolvedRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLResolvedRef other = (AbstractHDLResolvedRef) obj;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
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
		result = (prime * result) + ((var == null) ? 0 : var.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLResolvedRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateVar(getVarRefName());
		if (checkResolve && (getVarRefName() != null))
			if (!resolveVar().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}