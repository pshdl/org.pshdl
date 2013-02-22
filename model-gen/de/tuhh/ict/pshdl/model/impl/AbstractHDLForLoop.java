package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLForLoop extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLForLoop}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param range
	 *            the value for range. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param param
	 *            the value for param. Can <b>not</b> be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLForLoop(@Nullable IHDLObject container, @Nonnull ArrayList<HDLRange> range, @Nonnull HDLVariable param, @Nonnull ArrayList<HDLStatement> dos, boolean validate) {
		super(container, validate);
		if (validate) {
			range = validateRange(range);
		}
		this.range = new ArrayList<HDLRange>();
		if (range != null) {
			for (HDLRange newValue : range) {
				this.range.add((HDLRange) newValue);
			}
		}
		if (validate) {
			param = validateParam(param);
		}
		if (param != null) {
			this.param = (HDLVariable) param;
		} else {
			this.param = null;
		}
		if (validate) {
			dos = validateDos(dos);
		}
		this.dos = new ArrayList<HDLStatement>();
		if (dos != null) {
			for (HDLStatement newValue : dos) {
				this.dos.add((HDLStatement) newValue);
			}
		}
	}

	public AbstractHDLForLoop() {
		super();
		this.range = new ArrayList<HDLRange>();
		this.param = null;
		this.dos = new ArrayList<HDLStatement>();
	}

	@Visit
	protected final ArrayList<HDLRange> range;

	/**
	 * Get the range field. Can <b>not</b> be <code>null</code>, additionally
	 * the collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLRange> getRange() {
		return (ArrayList<HDLRange>) range.clone();
	}

	protected ArrayList<HDLRange> validateRange(ArrayList<HDLRange> range) {
		if (range == null)
			throw new IllegalArgumentException("The field range can not be null!");
		if (range.size() < 1)
			throw new IllegalArgumentException("The field range must contain at least one item!");
		return range;
	}

	@Visit
	protected final HDLVariable param;

	/**
	 * Get the param field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLVariable getParam() {
		return param;
	}

	protected HDLVariable validateParam(HDLVariable param) {
		if (param == null)
			throw new IllegalArgumentException("The field param can not be null!");
		return param;
	}

	@Visit
	protected final ArrayList<HDLStatement> dos;

	/**
	 * Get the dos field. Can <b>not</b> be <code>null</code>, additionally the
	 * collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLStatement> getDos() {
		return (ArrayList<HDLStatement>) dos.clone();
	}

	protected ArrayList<HDLStatement> validateDos(ArrayList<HDLStatement> dos) {
		if (dos == null)
			throw new IllegalArgumentException("The field dos can not be null!");
		if (dos.size() < 1)
			throw new IllegalArgumentException("The field dos must contain at least one item!");
		return dos;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLForLoop copy() {
		HDLForLoop newObject = new HDLForLoop(null, range, param, dos, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLForLoop copyFiltered(CopyFilter filter) {
		ArrayList<HDLRange> filteredrange = filter.copyContainer("range", this, range);
		HDLVariable filteredparam = filter.copyObject("param", this, param);
		ArrayList<HDLStatement> filtereddos = filter.copyContainer("dos", this, dos);
		return filter.postFilter((HDLForLoop) this, new HDLForLoop(null, filteredrange, filteredparam, filtereddos, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLForLoop copyDeepFrozen(IHDLObject container) {
		HDLForLoop copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLForLoop} with the updated
	 *         container field.
	 */
	public @Nonnull
	HDLForLoop setContainer(@Nullable IHDLObject container) {
		return (HDLForLoop) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getRange()}.
	 * 
	 * @param range
	 *            sets the new range of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	public @Nonnull
	HDLForLoop setRange(@Nonnull ArrayList<HDLRange> range) {
		range = validateRange(range);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getRange()}.
	 * 
	 * @param newRange
	 *            the value that should be added to the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	public @Nonnull
	HDLForLoop addRange(@Nonnull HDLRange newRange) {
		if (newRange == null)
			throw new IllegalArgumentException("Element of range can not be null!");
		ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.add(newRange);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getRange()}.
	 * 
	 * @param newRange
	 *            the value that should be removed from the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	public @Nonnull
	HDLForLoop removeRange(@Nonnull HDLRange newRange) {
		if (newRange == null)
			throw new IllegalArgumentException("Removed element of range can not be null!");
		ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.remove(newRange);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getRange()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getRange()}
	 * @return a new instance of {@link HDLForLoop} with the updated range
	 *         field.
	 */
	public @Nonnull
	HDLForLoop removeRange(int idx) {
		ArrayList<HDLRange> range = (ArrayList<HDLRange>) this.range.clone();
		range.remove(idx);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getParam()}.
	 * 
	 * @param param
	 *            sets the new param of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLForLoop} with the updated param
	 *         field.
	 */
	public @Nonnull
	HDLForLoop setParam(@Nonnull HDLVariable param) {
		param = validateParam(param);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDos()}.
	 * 
	 * @param dos
	 *            sets the new dos of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public @Nonnull
	HDLForLoop setDos(@Nonnull ArrayList<HDLStatement> dos) {
		dos = validateDos(dos);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be added to the field {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public @Nonnull
	HDLForLoop addDos(@Nonnull HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Element of dos can not be null!");
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.add(newDos);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public @Nonnull
	HDLForLoop removeDos(@Nonnull HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Removed element of dos can not be null!");
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(newDos);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLForLoop} with the updated dos field.
	 */
	public @Nonnull
	HDLForLoop removeDos(int idx) {
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(idx);
		HDLForLoop res = new HDLForLoop(container, range, param, dos, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLForLoop))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLForLoop other = (AbstractHDLForLoop) obj;
		if (range == null) {
			if (other.range != null)
				return false;
		} else if (!range.equals(other.range))
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		if (dos == null) {
			if (other.dos != null)
				return false;
		} else if (!dos.equals(other.dos))
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
		result = (prime * result) + ((range == null) ? 0 : range.hashCode());
		result = (prime * result) + ((param == null) ? 0 : param.hashCode());
		result = (prime * result) + ((dos == null) ? 0 : dos.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLForLoop()");
		if (range != null) {
			if (range.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLRange o : range) {
					sb.append(".addRange(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (param != null) {
			sb.append(".setParam(").append(param.toConstructionString(spacing + "\t")).append(")");
		}
		if (dos != null) {
			if (dos.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : dos) {
					sb.append(".addDos(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateRange(getRange());
		if (getRange() != null) {
			for (HDLRange o : getRange()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateParam(getParam());
		if (getParam() != null) {
			getParam().validateAllFields(this, checkResolve);
		}
		validateDos(getDos());
		if (getDos() != null) {
			for (HDLStatement o : getDos()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLForLoop, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}