package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLVariableRef extends HDLResolvedRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLVariableRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLVariableRef(@Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits,
			boolean validate) {
		super(container, var, validate);
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
	}

	public AbstractHDLVariableRef() {
		super();
		this.array = new ArrayList<HDLExpression>();
		this.bits = new ArrayList<HDLRange>();
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

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableRef copy() {
		HDLVariableRef newObject = new HDLVariableRef(null, var, array, bits, false);
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
	public HDLVariableRef copyFiltered(CopyFilter filter) {
		HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		return filter.postFilter((HDLVariableRef) this, new HDLVariableRef(null, filteredvar, filteredarray, filteredbits, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableRef copyDeepFrozen(IHDLObject container) {
		HDLVariableRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLVariableRef} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLVariableRef setContainer(@Nullable IHDLObject container) {
		return (HDLVariableRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated var
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLVariableRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef setArray(@Nullable ArrayList<HDLExpression> array) {
		array = validateArray(array);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLVariableRef} with the updated array
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeArray(int idx) {
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef setBits(@Nullable ArrayList<HDLRange> bits) {
		bits = validateBits(bits);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLVariableRef} with the updated bits
	 *         field.
	 */
	@Nonnull
	public HDLVariableRef removeBits(int idx) {
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		HDLVariableRef res = new HDLVariableRef(container, var, array, bits, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariableRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLVariableRef other = (AbstractHDLVariableRef) obj;
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
		return true;
	}

	private static Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((array == null) ? 0 : array.hashCode());
		result = (prime * result) + ((bits == null) ? 0 : bits.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLVariableRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
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
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
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
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLVariableRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}