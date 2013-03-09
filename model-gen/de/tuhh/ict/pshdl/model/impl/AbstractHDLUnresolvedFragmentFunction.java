package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLUnresolvedFragmentFunction extends HDLUnresolvedFragment {
	/**
	 * Constructs a new instance of
	 * {@link AbstractHDLUnresolvedFragmentFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param frag
	 *            the value for frag. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param sub
	 *            the value for sub. Can be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLUnresolvedFragmentFunction(@Nullable IHDLObject container, @Nonnull String frag, @Nullable Iterable<HDLExpression> array, @Nullable Iterable<HDLRange> bits,
			@Nullable HDLUnresolvedFragment sub, @Nullable Iterable<HDLExpression> params, boolean validate) {
		super(container, frag, array, bits, sub, validate);
		if (validate) {
			params = validateParams(params);
		}
		this.params = new ArrayList<HDLExpression>();
		if (params != null) {
			for (HDLExpression newValue : params) {
				this.params.add(newValue);
			}
		}
	}

	public AbstractHDLUnresolvedFragmentFunction() {
		super();
		this.params = new ArrayList<HDLExpression>();
	}

	@Visit
	protected final ArrayList<HDLExpression> params;

	/**
	 * Get the params field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getParams() {
		return (ArrayList<HDLExpression>) params.clone();
	}

	protected Iterable<HDLExpression> validateParams(Iterable<HDLExpression> params) {
		if (params == null)
			return new ArrayList<HDLExpression>();
		return params;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction copy() {
		HDLUnresolvedFragmentFunction newObject = new HDLUnresolvedFragmentFunction(null, frag, array, bits, sub, params, false);
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
	public HDLUnresolvedFragmentFunction copyFiltered(CopyFilter filter) {
		String filteredfrag = filter.copyObject("frag", this, frag);
		ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		HDLUnresolvedFragment filteredsub = filter.copyObject("sub", this, sub);
		ArrayList<HDLExpression> filteredparams = filter.copyContainer("params", this, params);
		return filter.postFilter((HDLUnresolvedFragmentFunction) this, new HDLUnresolvedFragmentFunction(null, filteredfrag, filteredarray, filteredbits, filteredsub,
				filteredparams, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction copyDeepFrozen(IHDLObject container) {
		HDLUnresolvedFragmentFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLUnresolvedFragmentFunction} with
	 *         the updated container field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setContainer(@Nullable IHDLObject container) {
		return (HDLUnresolvedFragmentFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFrag()}.
	 * 
	 * @param frag
	 *            sets the new frag of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated frag field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setFrag(@Nonnull String frag) {
		frag = validateFrag(frag);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setArray(@Nullable Iterable<HDLExpression> array) {
		array = validateArray(array);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated array field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeArray(int idx) {
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setBits(@Nullable Iterable<HDLRange> bits) {
		bits = validateBits(bits);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated bits field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction removeBits(int idx) {
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSub()}.
	 * 
	 * @param sub
	 *            sets the new sub of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated sub field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragmentFunction setSub(@Nullable HDLUnresolvedFragment sub) {
		sub = validateSub(sub);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParams()}.
	 * 
	 * @param params
	 *            sets the new params of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction setParams(@Nullable Iterable<HDLExpression> params) {
		params = validateParams(params);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be added to the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction addParams(@Nullable HDLExpression newParams) {
		if (newParams == null)
			throw new IllegalArgumentException("Element of params can not be null!");
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.add(newParams);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param newParams
	 *            the value that should be removed from the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction removeParams(@Nullable HDLExpression newParams) {
		if (newParams == null)
			throw new IllegalArgumentException("Removed element of params can not be null!");
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(newParams);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getParams()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getParams()}
	 * @return a new instance of {@link HDLUnresolvedFragmentFunction} with the
	 *         updated params field.
	 */
	@Nonnull
	public HDLUnresolvedFragmentFunction removeParams(int idx) {
		ArrayList<HDLExpression> params = (ArrayList<HDLExpression>) this.params.clone();
		params.remove(idx);
		HDLUnresolvedFragmentFunction res = new HDLUnresolvedFragmentFunction(container, frag, array, bits, sub, params, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLUnresolvedFragmentFunction))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLUnresolvedFragmentFunction other = (AbstractHDLUnresolvedFragmentFunction) obj;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
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
		result = (prime * result) + ((params == null) ? 0 : params.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnresolvedFragmentFunction()");
		if (frag != null) {
			sb.append(".setFrag(").append('"' + frag + '"').append(")");
		}
		if (array != null) {
			if (array.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : array) {
					sb.append(".addArray(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (bits != null) {
			if (bits.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLRange o : bits) {
					sb.append(".addBits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (sub != null) {
			sb.append(".setSub(").append(sub.toConstructionString(spacing + "\t")).append(")");
		}
		if (params != null) {
			if (params.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : params) {
					sb.append(".addParams(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateParams(getParams());
		if (getParams() != null) {
			for (HDLExpression o : getParams()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnresolvedFragmentFunction, HDLClass.HDLUnresolvedFragment, HDLClass.HDLStatement, HDLClass.HDLReference, HDLClass.HDLExpression,
				HDLClass.HDLObject);
	}
}