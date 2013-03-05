package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLUnresolvedFragment extends HDLReference {
	/**
	 * Constructs a new instance of {@link AbstractHDLUnresolvedFragment}
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
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLUnresolvedFragment(@Nullable IHDLObject container, @Nonnull String frag, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits,
			@Nullable HDLUnresolvedFragment sub, boolean validate) {
		super(container, validate);
		if (validate) {
			frag = validateFrag(frag);
		}
		this.frag = frag;
		if (validate) {
			array = validateArray(array);
		}
		this.array = new ArrayList<HDLExpression>();
		if (array != null) {
			for (HDLExpression newValue : array) {
				this.array.add(newValue);
			}
		}
		if (validate) {
			bits = validateBits(bits);
		}
		this.bits = new ArrayList<HDLRange>();
		if (bits != null) {
			for (HDLRange newValue : bits) {
				this.bits.add(newValue);
			}
		}
		if (validate) {
			sub = validateSub(sub);
		}
		if (sub != null) {
			this.sub = sub;
		} else {
			this.sub = null;
		}
	}

	public AbstractHDLUnresolvedFragment() {
		super();
		this.frag = null;
		this.array = new ArrayList<HDLExpression>();
		this.bits = new ArrayList<HDLRange>();
		this.sub = null;
	}

	protected final String frag;

	/**
	 * Get the frag field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	String getFrag() {
		return frag;
	}

	protected String validateFrag(String frag) {
		if (frag == null)
			throw new IllegalArgumentException("The field frag can not be null!");
		return frag;
	}

	@Visit
	protected final ArrayList<HDLExpression> array;

	/**
	 * Get the array field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLExpression> getArray() {
		return (ArrayList<HDLExpression>) array.clone();
	}

	protected ArrayList<HDLExpression> validateArray(ArrayList<HDLExpression> array) {
		if (array == null)
			return new ArrayList<HDLExpression>();
		return array;
	}

	@Visit
	protected final ArrayList<HDLRange> bits;

	/**
	 * Get the bits field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLRange> getBits() {
		return (ArrayList<HDLRange>) bits.clone();
	}

	protected ArrayList<HDLRange> validateBits(ArrayList<HDLRange> bits) {
		if (bits == null)
			return new ArrayList<HDLRange>();
		return bits;
	}

	@Visit
	protected final HDLUnresolvedFragment sub;

	/**
	 * Get the sub field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nullable
	HDLUnresolvedFragment getSub() {
		return sub;
	}

	protected HDLUnresolvedFragment validateSub(HDLUnresolvedFragment sub) {
		return sub;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment copy() {
		HDLUnresolvedFragment newObject = new HDLUnresolvedFragment(null, frag, array, bits, sub, false);
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
	public HDLUnresolvedFragment copyFiltered(CopyFilter filter) {
		String filteredfrag = filter.copyObject("frag", this, frag);
		ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		HDLUnresolvedFragment filteredsub = filter.copyObject("sub", this, sub);
		return filter.postFilter((HDLUnresolvedFragment) this, new HDLUnresolvedFragment(null, filteredfrag, filteredarray, filteredbits, filteredsub, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment copyDeepFrozen(IHDLObject container) {
		HDLUnresolvedFragment copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLUnresolvedFragment} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLUnresolvedFragment setContainer(@Nullable IHDLObject container) {
		return (HDLUnresolvedFragment) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getFrag()}.
	 * 
	 * @param frag
	 *            sets the new frag of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         frag field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setFrag(@Nonnull String frag) {
		frag = validateFrag(frag);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setArray(@Nullable ArrayList<HDLExpression> array) {
		array = validateArray(array);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         array field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeArray(int idx) {
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setBits(@Nullable ArrayList<HDLRange> bits) {
		bits = validateBits(bits);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         bits field.
	 */
	@Nonnull
	public HDLUnresolvedFragment removeBits(int idx) {
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSub()}.
	 * 
	 * @param sub
	 *            sets the new sub of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnresolvedFragment} with the updated
	 *         sub field.
	 */
	@Nonnull
	public HDLUnresolvedFragment setSub(@Nullable HDLUnresolvedFragment sub) {
		sub = validateSub(sub);
		HDLUnresolvedFragment res = new HDLUnresolvedFragment(container, frag, array, bits, sub, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLUnresolvedFragment))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLUnresolvedFragment other = (AbstractHDLUnresolvedFragment) obj;
		if (frag == null) {
			if (other.frag != null)
				return false;
		} else if (!frag.equals(other.frag))
			return false;
		if (array == null) {
			if (other.array != null)
				return false;
		} else if (!array.equals(other.array))
			return false;
		if (bits == null) {
			if (other.bits != null)
				return false;
		} else if (!bits.equals(other.bits))
			return false;
		if (sub == null) {
			if (other.sub != null)
				return false;
		} else if (!sub.equals(other.sub))
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
		result = (prime * result) + ((frag == null) ? 0 : frag.hashCode());
		result = (prime * result) + ((array == null) ? 0 : array.hashCode());
		result = (prime * result) + ((bits == null) ? 0 : bits.hashCode());
		result = (prime * result) + ((sub == null) ? 0 : sub.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnresolvedFragment()");
		if (frag != null) {
			sb.append(".setFrag(").append('"' + frag + '"').append(")");
		}
		if (array != null)
			if (array.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : array) {
					sb.append(".addArray(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		if (bits != null)
			if (bits.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLRange o : bits) {
					sb.append(".addBits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		if (sub != null) {
			sb.append(".setSub(").append(sub.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateFrag(getFrag());
		validateArray(getArray());
		if (getArray() != null) {
			for (HDLExpression o : getArray()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateBits(getBits());
		if (getBits() != null) {
			for (HDLRange o : getBits()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateSub(getSub());
		if (getSub() != null) {
			getSub().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnresolvedFragment, HDLClass.HDLStatement, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}