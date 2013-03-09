package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

@SuppressWarnings("all")
public abstract class AbstractHDLRegisterConfig extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLRegisterConfig}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param clk
	 *            the value for clk. Can <b>not</b> be <code>null</code>.
	 * @param rst
	 *            the value for rst. Can <b>not</b> be <code>null</code>.
	 * @param clockType
	 *            the value for clockType. Can be <code>null</code>.
	 * @param resetType
	 *            the value for resetType. Can be <code>null</code>.
	 * @param syncType
	 *            the value for syncType. Can be <code>null</code>.
	 * @param resetValue
	 *            the value for resetValue. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLRegisterConfig(@Nullable IHDLObject container, @Nonnull HDLQualifiedName clk, @Nonnull HDLQualifiedName rst, @Nullable HDLRegClockType clockType,
			@Nullable HDLRegResetActiveType resetType, @Nullable HDLRegSyncType syncType, @Nonnull HDLExpression resetValue, boolean validate) {
		super(container, validate);
		if (validate) {
			clk = validateClk(clk);
		}
		this.clk = clk;
		if (validate) {
			rst = validateRst(rst);
		}
		this.rst = rst;
		if (validate) {
			clockType = validateClockType(clockType);
		}
		this.clockType = clockType;
		if (validate) {
			resetType = validateResetType(resetType);
		}
		this.resetType = resetType;
		if (validate) {
			syncType = validateSyncType(syncType);
		}
		this.syncType = syncType;
		if (validate) {
			resetValue = validateResetValue(resetValue);
		}
		if (resetValue != null) {
			this.resetValue = resetValue;
		} else {
			this.resetValue = null;
		}
	}

	public AbstractHDLRegisterConfig() {
		super();
		this.clk = null;
		this.rst = null;
		this.clockType = null;
		this.resetType = null;
		this.syncType = null;
		this.resetValue = null;
	}

	protected final HDLQualifiedName clk;

	@Nullable
	public Optional<HDLVariable> resolveClk() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, clk);
	}

	public HDLQualifiedName getClkRefName() {
		return clk;
	}

	protected HDLQualifiedName validateClk(HDLQualifiedName clk) {
		if (clk == null)
			throw new IllegalArgumentException("The field clk can not be null!");
		return clk;
	}

	protected final HDLQualifiedName rst;

	@Nullable
	public Optional<HDLVariable> resolveRst() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, rst);
	}

	public HDLQualifiedName getRstRefName() {
		return rst;
	}

	protected HDLQualifiedName validateRst(HDLQualifiedName rst) {
		if (rst == null)
			throw new IllegalArgumentException("The field rst can not be null!");
		return rst;
	}

	protected final HDLRegClockType clockType;

	/**
	 * Get the clockType field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLRegClockType getClockType() {
		return clockType;
	}

	protected HDLRegClockType validateClockType(HDLRegClockType clockType) {
		return clockType;
	}

	protected final HDLRegResetActiveType resetType;

	/**
	 * Get the resetType field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLRegResetActiveType getResetType() {
		return resetType;
	}

	protected HDLRegResetActiveType validateResetType(HDLRegResetActiveType resetType) {
		return resetType;
	}

	protected final HDLRegSyncType syncType;

	/**
	 * Get the syncType field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLRegSyncType getSyncType() {
		return syncType;
	}

	protected HDLRegSyncType validateSyncType(HDLRegSyncType syncType) {
		return syncType;
	}

	@Visit
	protected final HDLExpression resetValue;

	/**
	 * Get the resetValue field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getResetValue() {
		return resetValue;
	}

	protected HDLExpression validateResetValue(HDLExpression resetValue) {
		if (resetValue == null)
			throw new IllegalArgumentException("The field resetValue can not be null!");
		return resetValue;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRegisterConfig copy() {
		HDLRegisterConfig newObject = new HDLRegisterConfig(null, clk, rst, clockType, resetType, syncType, resetValue, false);
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
	public HDLRegisterConfig copyFiltered(CopyFilter filter) {
		HDLQualifiedName filteredclk = filter.copyObject("clk", this, clk);
		HDLQualifiedName filteredrst = filter.copyObject("rst", this, rst);
		HDLRegClockType filteredclockType = filter.copyObject("clockType", this, clockType);
		HDLRegResetActiveType filteredresetType = filter.copyObject("resetType", this, resetType);
		HDLRegSyncType filteredsyncType = filter.copyObject("syncType", this, syncType);
		HDLExpression filteredresetValue = filter.copyObject("resetValue", this, resetValue);
		return filter.postFilter((HDLRegisterConfig) this, new HDLRegisterConfig(null, filteredclk, filteredrst, filteredclockType, filteredresetType, filteredsyncType,
				filteredresetValue, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRegisterConfig copyDeepFrozen(IHDLObject container) {
		HDLRegisterConfig copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLRegisterConfig} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLRegisterConfig setContainer(@Nullable IHDLObject container) {
		return (HDLRegisterConfig) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getClk()}.
	 * 
	 * @param clk
	 *            sets the new clk of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated clk
	 *         field.
	 */
	@Nonnull
	public HDLRegisterConfig setClk(@Nonnull HDLQualifiedName clk) {
		clk = validateClk(clk);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRst()}.
	 * 
	 * @param rst
	 *            sets the new rst of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated rst
	 *         field.
	 */
	@Nonnull
	public HDLRegisterConfig setRst(@Nonnull HDLQualifiedName rst) {
		rst = validateRst(rst);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getClockType()}.
	 * 
	 * @param clockType
	 *            sets the new clockType of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         clockType field.
	 */
	@Nonnull
	public HDLRegisterConfig setClockType(@Nullable HDLRegClockType clockType) {
		clockType = validateClockType(clockType);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getResetType()}.
	 * 
	 * @param resetType
	 *            sets the new resetType of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         resetType field.
	 */
	@Nonnull
	public HDLRegisterConfig setResetType(@Nullable HDLRegResetActiveType resetType) {
		resetType = validateResetType(resetType);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSyncType()}.
	 * 
	 * @param syncType
	 *            sets the new syncType of this object. Can be <code>null</code>
	 *            .
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         syncType field.
	 */
	@Nonnull
	public HDLRegisterConfig setSyncType(@Nullable HDLRegSyncType syncType) {
		syncType = validateSyncType(syncType);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getResetValue()}.
	 * 
	 * @param resetValue
	 *            sets the new resetValue of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         resetValue field.
	 */
	@Nonnull
	public HDLRegisterConfig setResetValue(@Nonnull HDLExpression resetValue) {
		resetValue = validateResetValue(resetValue);
		HDLRegisterConfig res = new HDLRegisterConfig(container, clk, rst, clockType, resetType, syncType, resetValue, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRegisterConfig))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
		if (clk == null) {
			if (other.clk != null)
				return false;
		} else if (!clk.equals(other.clk))
			return false;
		if (rst == null) {
			if (other.rst != null)
				return false;
		} else if (!rst.equals(other.rst))
			return false;
		if (clockType == null) {
			if (other.clockType != null)
				return false;
		} else if (!clockType.equals(other.clockType))
			return false;
		if (resetType == null) {
			if (other.resetType != null)
				return false;
		} else if (!resetType.equals(other.resetType))
			return false;
		if (syncType == null) {
			if (other.syncType != null)
				return false;
		} else if (!syncType.equals(other.syncType))
			return false;
		if (resetValue == null) {
			if (other.resetValue != null)
				return false;
		} else if (!resetValue.equals(other.resetValue))
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
		result = (prime * result) + ((clk == null) ? 0 : clk.hashCode());
		result = (prime * result) + ((rst == null) ? 0 : rst.hashCode());
		result = (prime * result) + ((clockType == null) ? 0 : clockType.hashCode());
		result = (prime * result) + ((resetType == null) ? 0 : resetType.hashCode());
		result = (prime * result) + ((syncType == null) ? 0 : syncType.hashCode());
		result = (prime * result) + ((resetValue == null) ? 0 : resetValue.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLRegisterConfig()");
		if (clk != null) {
			sb.append(".setClk(HDLQualifiedName.create(\"").append(clk).append("\"))");
		}
		if (rst != null) {
			sb.append(".setRst(HDLQualifiedName.create(\"").append(rst).append("\"))");
		}
		if (clockType != null) {
			sb.append("\n").append(spacing + "\t").append(".setClockType(HDLRegClockType.").append(clockType.name() + ")");
		}
		if (resetType != null) {
			sb.append("\n").append(spacing + "\t").append(".setResetType(HDLRegResetActiveType.").append(resetType.name() + ")");
		}
		if (syncType != null) {
			sb.append("\n").append(spacing + "\t").append(".setSyncType(HDLRegSyncType.").append(syncType.name() + ")");
		}
		if (resetValue != null) {
			sb.append(".setResetValue(").append(resetValue.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateClk(getClkRefName());
		if (checkResolve && (getClkRefName() != null))
			if (resolveClk() == null)
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getClkRefName()));
		validateRst(getRstRefName());
		if (checkResolve && (getRstRefName() != null))
			if (resolveRst() == null)
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getRstRefName()));
		validateClockType(getClockType());
		validateResetType(getResetType());
		validateSyncType(getSyncType());
		validateResetValue(getResetValue());
		if (getResetValue() != null) {
			getResetValue().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLRegisterConfig, HDLClass.HDLObject);
	}
}