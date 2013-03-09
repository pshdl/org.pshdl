package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

@SuppressWarnings("all")
public abstract class AbstractHDLLiteral extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLLiteral}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param val
	 *            the value for val. Can <b>not</b> be <code>null</code>.
	 * @param str
	 *            the value for str. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLLiteral(@Nullable IHDLObject container, @Nonnull String val, @Nullable Boolean str, boolean validate) {
		super(container, validate);
		if (validate) {
			val = validateVal(val);
		}
		this.val = val;
		if (validate) {
			str = validateStr(str);
		}
		this.str = str;
	}

	public AbstractHDLLiteral() {
		super();
		this.val = null;
		this.str = null;
	}

	protected final String val;

	/**
	 * Get the val field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public String getVal() {
		return val;
	}

	protected String validateVal(String val) {
		if (val == null)
			throw new IllegalArgumentException("The field val can not be null!");
		return val;
	}

	protected final Boolean str;

	/**
	 * Get the str field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public Boolean getStr() {
		return str;
	}

	protected Boolean validateStr(Boolean str) {
		return str;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLLiteral copy() {
		HDLLiteral newObject = new HDLLiteral(null, val, str, false);
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
	public HDLLiteral copyFiltered(CopyFilter filter) {
		String filteredval = filter.copyObject("val", this, val);
		Boolean filteredstr = filter.copyObject("str", this, str);
		return filter.postFilter((HDLLiteral) this, new HDLLiteral(null, filteredval, filteredstr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLLiteral copyDeepFrozen(IHDLObject container) {
		HDLLiteral copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLLiteral} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLLiteral setContainer(@Nullable IHDLObject container) {
		return (HDLLiteral) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVal()}.
	 * 
	 * @param val
	 *            sets the new val of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated val field.
	 */
	@Nonnull
	public HDLLiteral setVal(@Nonnull String val) {
		val = validateVal(val);
		HDLLiteral res = new HDLLiteral(container, val, str, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStr()}.
	 * 
	 * @param str
	 *            sets the new str of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated str field.
	 */
	@Nonnull
	public HDLLiteral setStr(@Nullable Boolean str) {
		str = validateStr(str);
		HDLLiteral res = new HDLLiteral(container, val, str, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStr()}.
	 * 
	 * @param str
	 *            sets the new str of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLLiteral} with the updated str field.
	 */
	@Nonnull
	public HDLLiteral setStr(boolean str) {
		str = validateStr(str);
		HDLLiteral res = new HDLLiteral(container, val, str, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLLiteral))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLLiteral other = (AbstractHDLLiteral) obj;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		if (str == null) {
			if (other.str != null)
				return false;
		} else if (!str.equals(other.str))
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
		result = (prime * result) + ((val == null) ? 0 : val.hashCode());
		result = (prime * result) + ((str == null) ? 0 : str.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLLiteral()");
		if (val != null) {
			sb.append(".setVal(").append('"' + val + '"').append(")");
		}
		if (str != null) {
			sb.append(".setStr(").append(str).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateVal(getVal());
		validateStr(getStr());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLLiteral, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}