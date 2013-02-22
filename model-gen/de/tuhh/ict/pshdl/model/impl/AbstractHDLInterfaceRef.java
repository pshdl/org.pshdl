package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceRef extends HDLVariableRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceRef}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param ifArray
	 *            the value for ifArray. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterfaceRef(@Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nullable ArrayList<HDLExpression> array, @Nullable ArrayList<HDLRange> bits,
			@Nonnull HDLQualifiedName hIf, @Nullable ArrayList<HDLExpression> ifArray, boolean validate) {
		super(container, var, array, bits, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		this.hIf = hIf;
		if (validate) {
			ifArray = validateIfArray(ifArray);
		}
		this.ifArray = new ArrayList<HDLExpression>();
		if (ifArray != null) {
			for (HDLExpression newValue : ifArray) {
				this.ifArray.add((HDLExpression) newValue);
			}
		}
	}

	public AbstractHDLInterfaceRef() {
		super();
		this.hIf = null;
		this.ifArray = new ArrayList<HDLExpression>();
	}

	protected final HDLQualifiedName hIf;

	@Nullable
	public HDLVariable resolveHIf() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, hIf);
	}

	public HDLQualifiedName getHIfRefName() {
		return hIf;
	}

	protected HDLQualifiedName validateHIf(HDLQualifiedName hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}

	@Visit
	protected final ArrayList<HDLExpression> ifArray;

	/**
	 * Get the ifArray field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLExpression> getIfArray() {
		return (ArrayList<HDLExpression>) ifArray.clone();
	}

	protected ArrayList<HDLExpression> validateIfArray(ArrayList<HDLExpression> ifArray) {
		if (ifArray == null)
			return new ArrayList<HDLExpression>();
		return ifArray;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceRef copy() {
		HDLInterfaceRef newObject = new HDLInterfaceRef(null, var, array, bits, hIf, ifArray, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceRef copyFiltered(CopyFilter filter) {
		HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		ArrayList<HDLExpression> filteredarray = filter.copyContainer("array", this, array);
		ArrayList<HDLRange> filteredbits = filter.copyContainer("bits", this, bits);
		HDLQualifiedName filteredhIf = filter.copyObject("hIf", this, hIf);
		ArrayList<HDLExpression> filteredifArray = filter.copyContainer("ifArray", this, ifArray);
		return filter.postFilter((HDLInterfaceRef) this, new HDLInterfaceRef(null, filteredvar, filteredarray, filteredbits, filteredhIf, filteredifArray, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLInterfaceRef copyDeepFrozen(IHDLObject container) {
		HDLInterfaceRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInterfaceRef} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLInterfaceRef setContainer(@Nullable IHDLObject container) {
		return (HDLInterfaceRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated var
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArray()}.
	 * 
	 * @param array
	 *            sets the new array of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef setArray(@Nullable ArrayList<HDLExpression> array) {
		array = validateArray(array);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be added to the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef addArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.add(newArray);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param newArray
	 *            the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef removeArray(@Nullable HDLExpression newArray) {
		if (newArray == null)
			throw new IllegalArgumentException("Removed element of array can not be null!");
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(newArray);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated array
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef removeArray(int idx) {
		ArrayList<HDLExpression> array = (ArrayList<HDLExpression>) this.array.clone();
		array.remove(idx);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getBits()}.
	 * 
	 * @param bits
	 *            sets the new bits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef setBits(@Nullable ArrayList<HDLRange> bits) {
		bits = validateBits(bits);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be added to the field {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef addBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.add(newBits);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param newBits
	 *            the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef removeBits(@Nullable HDLRange newBits) {
		if (newBits == null)
			throw new IllegalArgumentException("Removed element of bits can not be null!");
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(newBits);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getBits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getBits()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated bits
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef removeBits(int idx) {
		ArrayList<HDLRange> bits = (ArrayList<HDLRange>) this.bits.clone();
		bits.remove(idx);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated hIf
	 *         field.
	 */
	public @Nonnull
	HDLInterfaceRef setHIf(@Nonnull HDLQualifiedName hIf) {
		hIf = validateHIf(hIf);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIfArray()}.
	 * 
	 * @param ifArray
	 *            sets the new ifArray of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	public @Nonnull
	HDLInterfaceRef setIfArray(@Nullable ArrayList<HDLExpression> ifArray) {
		ifArray = validateIfArray(ifArray);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getIfArray()}.
	 * 
	 * @param newIfArray
	 *            the value that should be added to the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	public @Nonnull
	HDLInterfaceRef addIfArray(@Nullable HDLExpression newIfArray) {
		if (newIfArray == null)
			throw new IllegalArgumentException("Element of ifArray can not be null!");
		ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.add(newIfArray);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getIfArray()}.
	 * 
	 * @param newIfArray
	 *            the value that should be removed from the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	public @Nonnull
	HDLInterfaceRef removeIfArray(@Nullable HDLExpression newIfArray) {
		if (newIfArray == null)
			throw new IllegalArgumentException("Removed element of ifArray can not be null!");
		ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.remove(newIfArray);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getIfArray()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getIfArray()}
	 * @return a new instance of {@link HDLInterfaceRef} with the updated
	 *         ifArray field.
	 */
	public @Nonnull
	HDLInterfaceRef removeIfArray(int idx) {
		ArrayList<HDLExpression> ifArray = (ArrayList<HDLExpression>) this.ifArray.clone();
		ifArray.remove(idx);
		HDLInterfaceRef res = new HDLInterfaceRef(container, var, array, bits, hIf, ifArray, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceRef))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceRef other = (AbstractHDLInterfaceRef) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		if (ifArray == null) {
			if (other.ifArray != null)
				return false;
		} else if (!ifArray.equals(other.ifArray))
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
		result = (prime * result) + ((ifArray == null) ? 0 : ifArray.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterfaceRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
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
		if (hIf != null) {
			sb.append(".setHIf(HDLQualifiedName.create(\"").append(hIf).append("\"))");
		}
		if (ifArray != null) {
			if (ifArray.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : ifArray) {
					sb.append(".addIfArray(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHIf(getHIfRefName());
		if (checkResolve && (getHIfRefName() != null))
			if (resolveHIf() == null)
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHIfRefName()));
		validateIfArray(getIfArray());
		if (getIfArray() != null) {
			for (HDLExpression o : getIfArray()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterfaceRef, HDLClass.HDLVariableRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}