package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLInstantiation extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInstantiation(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable Iterable<HDLArgument> arguments, boolean validate) {
		super(container, validate);
		if (validate) {
			var = validateVar(var);
		}
		if (var != null) {
			this.var = var;
		} else {
			this.var = null;
		}
		if (validate) {
			arguments = validateArguments(arguments);
		}
		this.arguments = new ArrayList<HDLArgument>();
		if (arguments != null) {
			for (HDLArgument newValue : arguments) {
				this.arguments.add(newValue);
			}
		}
	}

	public AbstractHDLInstantiation() {
		super();
		this.var = null;
		this.arguments = new ArrayList<HDLArgument>();
	}

	@Visit
	protected final HDLVariable var;

	/**
	 * Get the var field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLVariable getVar() {
		return var;
	}

	protected HDLVariable validateVar(HDLVariable var) {
		if (var == null)
			throw new IllegalArgumentException("The field var can not be null!");
		return var;
	}

	@Visit
	protected final ArrayList<HDLArgument> arguments;

	/**
	 * Get the arguments field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLArgument> getArguments() {
		return (ArrayList<HDLArgument>) arguments.clone();
	}

	protected Iterable<HDLArgument> validateArguments(Iterable<HDLArgument> arguments) {
		if (arguments == null)
			return new ArrayList<HDLArgument>();
		return arguments;
	}

	@Nonnull
	public abstract HDLInstantiation setVar(@Nonnull HDLVariable var);

	@Nonnull
	public abstract HDLInstantiation setArguments(@Nullable Iterable<HDLArgument> arguments);

	@Nonnull
	public abstract HDLInstantiation addArguments(@Nullable HDLArgument arguments);

	@Nonnull
	public abstract HDLInstantiation removeArguments(@Nullable HDLArgument arguments);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInstantiation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInstantiation other = (AbstractHDLInstantiation) obj;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
			return false;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
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
		result = (prime * result) + ((arguments == null) ? 0 : arguments.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInstantiation()");
		if (var != null) {
			sb.append(".setVar(").append(var.toConstructionString(spacing + "\t")).append(")");
		}
		if (arguments != null) {
			if (arguments.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLArgument o : arguments) {
					sb.append(".addArguments(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateVar(getVar());
		if (getVar() != null) {
			getVar().validateAllFields(this, checkResolve);
		}
		validateArguments(getArguments());
		if (getArguments() != null) {
			for (HDLArgument o : getArguments()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInstantiation, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}