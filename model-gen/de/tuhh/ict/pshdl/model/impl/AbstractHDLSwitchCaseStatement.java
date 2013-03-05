package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLSwitchCaseStatement extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLSwitchCaseStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param label
	 *            the value for label. Can be <code>null</code>.
	 * @param dos
	 *            the value for dos. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLSwitchCaseStatement(@Nullable IHDLObject container, @Nullable HDLExpression label, @Nullable ArrayList<HDLStatement> dos, boolean validate) {
		super(container, validate);
		if (validate) {
			label = validateLabel(label);
		}
		if (label != null) {
			this.label = label;
		} else {
			this.label = null;
		}
		if (validate) {
			dos = validateDos(dos);
		}
		this.dos = new ArrayList<HDLStatement>();
		if (dos != null) {
			for (HDLStatement newValue : dos) {
				this.dos.add(newValue);
			}
		}
	}

	public AbstractHDLSwitchCaseStatement() {
		super();
		this.label = null;
		this.dos = new ArrayList<HDLStatement>();
	}

	@Visit
	protected final HDLExpression label;

	/**
	 * Get the label field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nullable
	HDLExpression getLabel() {
		return label;
	}

	protected HDLExpression validateLabel(HDLExpression label) {
		return label;
	}

	@Visit
	protected final ArrayList<HDLStatement> dos;

	/**
	 * Get the dos field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLStatement> getDos() {
		return (ArrayList<HDLStatement>) dos.clone();
	}

	protected ArrayList<HDLStatement> validateDos(ArrayList<HDLStatement> dos) {
		if (dos == null)
			return new ArrayList<HDLStatement>();
		return dos;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSwitchCaseStatement copy() {
		HDLSwitchCaseStatement newObject = new HDLSwitchCaseStatement(null, label, dos, false);
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
	public HDLSwitchCaseStatement copyFiltered(CopyFilter filter) {
		HDLExpression filteredlabel = filter.copyObject("label", this, label);
		ArrayList<HDLStatement> filtereddos = filter.copyContainer("dos", this, dos);
		return filter.postFilter((HDLSwitchCaseStatement) this, new HDLSwitchCaseStatement(null, filteredlabel, filtereddos, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSwitchCaseStatement copyDeepFrozen(IHDLObject container) {
		HDLSwitchCaseStatement copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLSwitchCaseStatement} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLSwitchCaseStatement setContainer(@Nullable IHDLObject container) {
		return (HDLSwitchCaseStatement) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLabel()}.
	 * 
	 * @param label
	 *            sets the new label of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated
	 *         label field.
	 */
	@Nonnull
	public HDLSwitchCaseStatement setLabel(@Nullable HDLExpression label) {
		label = validateLabel(label);
		HDLSwitchCaseStatement res = new HDLSwitchCaseStatement(container, label, dos, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDos()}.
	 * 
	 * @param dos
	 *            sets the new dos of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated
	 *         dos field.
	 */
	@Nonnull
	public HDLSwitchCaseStatement setDos(@Nullable ArrayList<HDLStatement> dos) {
		dos = validateDos(dos);
		HDLSwitchCaseStatement res = new HDLSwitchCaseStatement(container, label, dos, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be added to the field {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated
	 *         dos field.
	 */
	@Nonnull
	public HDLSwitchCaseStatement addDos(@Nullable HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Element of dos can not be null!");
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.add(newDos);
		HDLSwitchCaseStatement res = new HDLSwitchCaseStatement(container, label, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param newDos
	 *            the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated
	 *         dos field.
	 */
	@Nonnull
	public HDLSwitchCaseStatement removeDos(@Nullable HDLStatement newDos) {
		if (newDos == null)
			throw new IllegalArgumentException("Removed element of dos can not be null!");
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(newDos);
		HDLSwitchCaseStatement res = new HDLSwitchCaseStatement(container, label, dos, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDos()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDos()}
	 * @return a new instance of {@link HDLSwitchCaseStatement} with the updated
	 *         dos field.
	 */
	@Nonnull
	public HDLSwitchCaseStatement removeDos(int idx) {
		ArrayList<HDLStatement> dos = (ArrayList<HDLStatement>) this.dos.clone();
		dos.remove(idx);
		HDLSwitchCaseStatement res = new HDLSwitchCaseStatement(container, label, dos, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLSwitchCaseStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLSwitchCaseStatement other = (AbstractHDLSwitchCaseStatement) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
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
		result = (prime * result) + ((label == null) ? 0 : label.hashCode());
		result = (prime * result) + ((dos == null) ? 0 : dos.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLSwitchCaseStatement()");
		if (label != null) {
			sb.append(".setLabel(").append(label.toConstructionString(spacing + "\t")).append(")");
		}
		if (dos != null)
			if (dos.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : dos) {
					sb.append(".addDos(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateLabel(getLabel());
		if (getLabel() != null) {
			getLabel().validateAllFields(this, checkResolve);
		}
		validateDos(getDos());
		if (getDos() != null) {
			for (HDLStatement o : getDos()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLSwitchCaseStatement, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}