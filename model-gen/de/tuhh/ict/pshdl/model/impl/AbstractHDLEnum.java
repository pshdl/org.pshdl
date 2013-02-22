package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLEnum extends HDLValueType {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnum}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param enums
	 *            the value for enums. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnum(@Nullable IHDLObject container, @Nonnull String name, @Nonnull ArrayList<HDLVariable> enums, boolean validate) {
		super(container, name, validate);
		if (validate) {
			enums = validateEnums(enums);
		}
		this.enums = new ArrayList<HDLVariable>();
		if (enums != null) {
			for (HDLVariable newValue : enums) {
				this.enums.add((HDLVariable) newValue);
			}
		}
	}

	public AbstractHDLEnum() {
		super();
		this.enums = new ArrayList<HDLVariable>();
	}

	@Visit
	protected final ArrayList<HDLVariable> enums;

	/**
	 * Get the enums field. Can <b>not</b> be <code>null</code>, additionally
	 * the collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLVariable> getEnums() {
		return (ArrayList<HDLVariable>) enums.clone();
	}

	protected ArrayList<HDLVariable> validateEnums(ArrayList<HDLVariable> enums) {
		if (enums == null)
			throw new IllegalArgumentException("The field enums can not be null!");
		if (enums.size() < 1)
			throw new IllegalArgumentException("The field enums must contain at least one item!");
		return enums;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnum copy() {
		HDLEnum newObject = new HDLEnum(null, name, enums, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnum copyFiltered(CopyFilter filter) {
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLVariable> filteredenums = filter.copyContainer("enums", this, enums);
		return filter.postFilter((HDLEnum) this, new HDLEnum(null, filteredname, filteredenums, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLEnum copyDeepFrozen(IHDLObject container) {
		HDLEnum copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLEnum} with the updated container
	 *         field.
	 */
	public @Nonnull
	HDLEnum setContainer(@Nullable IHDLObject container) {
		return (HDLEnum) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnum} with the updated name field.
	 */
	public @Nonnull
	HDLEnum setName(@Nonnull String name) {
		name = validateName(name);
		HDLEnum res = new HDLEnum(container, name, enums, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getEnums()}.
	 * 
	 * @param enums
	 *            sets the new enums of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public @Nonnull
	HDLEnum setEnums(@Nonnull ArrayList<HDLVariable> enums) {
		enums = validateEnums(enums);
		HDLEnum res = new HDLEnum(container, name, enums, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be added to the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public @Nonnull
	HDLEnum addEnums(@Nonnull HDLVariable newEnums) {
		if (newEnums == null)
			throw new IllegalArgumentException("Element of enums can not be null!");
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.add(newEnums);
		HDLEnum res = new HDLEnum(container, name, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param newEnums
	 *            the value that should be removed from the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public @Nonnull
	HDLEnum removeEnums(@Nonnull HDLVariable newEnums) {
		if (newEnums == null)
			throw new IllegalArgumentException("Removed element of enums can not be null!");
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.remove(newEnums);
		HDLEnum res = new HDLEnum(container, name, enums, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getEnums()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getEnums()}
	 * @return a new instance of {@link HDLEnum} with the updated enums field.
	 */
	public @Nonnull
	HDLEnum removeEnums(int idx) {
		ArrayList<HDLVariable> enums = (ArrayList<HDLVariable>) this.enums.clone();
		enums.remove(idx);
		HDLEnum res = new HDLEnum(container, name, enums, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnum))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnum other = (AbstractHDLEnum) obj;
		if (enums == null) {
			if (other.enums != null)
				return false;
		} else if (!enums.equals(other.enums))
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
		result = (prime * result) + ((enums == null) ? 0 : enums.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnum()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (enums != null) {
			if (enums.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLVariable o : enums) {
					sb.append(".addEnums(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateEnums(getEnums());
		if (getEnums() != null) {
			for (HDLVariable o : getEnums()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnum, HDLClass.HDLValueType, HDLClass.HDLType, HDLClass.HDLObject);
	}
}