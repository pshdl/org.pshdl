/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.impl;

import java.util.EnumSet;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import org.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLRegisterConfig extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLRegisterConfig}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param clk
	 *            the value for clk. Can <b>not</b> be <code>null</code>.
	 * @param rst
	 *            the value for rst. Can <b>not</b> be <code>null</code>.
	 * @param unresolvedClockType
	 *            the value for unresolvedClockType. Can be <code>null</code>.
	 * @param unresolvedResetType
	 *            the value for unresolvedResetType. Can be <code>null</code>.
	 * @param unresolvedSyncType
	 *            the value for unresolvedSyncType. Can be <code>null</code>.
	 * @param clockType
	 *            the value for clockType. Can be <code>null</code>.
	 * @param resetType
	 *            the value for resetType. Can be <code>null</code>.
	 * @param syncType
	 *            the value for syncType. Can be <code>null</code>.
	 * @param resetValue
	 *            the value for resetValue. Can <b>not</b> be <code>null</code>.
	 * @param delay
	 *            the value for delay. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLRegisterConfig(int id, @Nullable IHDLObject container, @Nonnull HDLExpression clk, @Nonnull HDLExpression rst, @Nullable HDLExpression unresolvedClockType,
			@Nullable HDLExpression unresolvedResetType, @Nullable HDLExpression unresolvedSyncType, @Nullable HDLRegClockType clockType, @Nullable HDLRegResetActiveType resetType,
			@Nullable HDLRegSyncType syncType, @Nonnull HDLExpression resetValue, @Nullable HDLExpression delay, boolean validate) {
		super(id, container, validate);
		if (validate) {
			clk = validateClk(clk);
		}
		if (clk != null) {
			this.clk = clk;
		} else {
			this.clk = null;
		}
		if (validate) {
			rst = validateRst(rst);
		}
		if (rst != null) {
			this.rst = rst;
		} else {
			this.rst = null;
		}
		if (validate) {
			unresolvedClockType = validateUnresolvedClockType(unresolvedClockType);
		}
		if (unresolvedClockType != null) {
			this.unresolvedClockType = unresolvedClockType;
		} else {
			this.unresolvedClockType = null;
		}
		if (validate) {
			unresolvedResetType = validateUnresolvedResetType(unresolvedResetType);
		}
		if (unresolvedResetType != null) {
			this.unresolvedResetType = unresolvedResetType;
		} else {
			this.unresolvedResetType = null;
		}
		if (validate) {
			unresolvedSyncType = validateUnresolvedSyncType(unresolvedSyncType);
		}
		if (unresolvedSyncType != null) {
			this.unresolvedSyncType = unresolvedSyncType;
		} else {
			this.unresolvedSyncType = null;
		}
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
		if (validate) {
			delay = validateDelay(delay);
		}
		if (delay != null) {
			this.delay = delay;
		} else {
			this.delay = null;
		}
	}

	public AbstractHDLRegisterConfig() {
		super();
		this.clk = null;
		this.rst = null;
		this.unresolvedClockType = null;
		this.unresolvedResetType = null;
		this.unresolvedSyncType = null;
		this.clockType = null;
		this.resetType = null;
		this.syncType = null;
		this.resetValue = null;
		this.delay = null;
	}

	protected final HDLExpression clk;

	/**
	 * Get the clk field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getClk() {
		return clk;
	}

	protected HDLExpression validateClk(HDLExpression clk) {
		if (clk == null) {
			throw new IllegalArgumentException("The field clk can not be null!");
		}
		return clk;
	}

	protected final HDLExpression rst;

	/**
	 * Get the rst field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getRst() {
		return rst;
	}

	protected HDLExpression validateRst(HDLExpression rst) {
		if (rst == null) {
			throw new IllegalArgumentException("The field rst can not be null!");
		}
		return rst;
	}

	protected final HDLExpression unresolvedClockType;

	/**
	 * Get the unresolvedClockType field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLExpression getUnresolvedClockType() {
		return unresolvedClockType;
	}

	protected HDLExpression validateUnresolvedClockType(HDLExpression unresolvedClockType) {
		return unresolvedClockType;
	}

	protected final HDLExpression unresolvedResetType;

	/**
	 * Get the unresolvedResetType field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLExpression getUnresolvedResetType() {
		return unresolvedResetType;
	}

	protected HDLExpression validateUnresolvedResetType(HDLExpression unresolvedResetType) {
		return unresolvedResetType;
	}

	protected final HDLExpression unresolvedSyncType;

	/**
	 * Get the unresolvedSyncType field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLExpression getUnresolvedSyncType() {
		return unresolvedSyncType;
	}

	protected HDLExpression validateUnresolvedSyncType(HDLExpression unresolvedSyncType) {
		return unresolvedSyncType;
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
		if (resetValue == null) {
			throw new IllegalArgumentException("The field resetValue can not be null!");
		}
		return resetValue;
	}

	protected final HDLExpression delay;

	/**
	 * Get the delay field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLExpression getDelay() {
		return delay;
	}

	protected HDLExpression validateDelay(HDLExpression delay) {
		return delay;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRegisterConfig copy() {
		final HDLRegisterConfig newObject = new HDLRegisterConfig(id, null, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
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
		final HDLExpression filteredclk = filter.copyObject("clk", this, clk);
		final HDLExpression filteredrst = filter.copyObject("rst", this, rst);
		final HDLExpression filteredunresolvedClockType = filter.copyObject("unresolvedClockType", this, unresolvedClockType);
		final HDLExpression filteredunresolvedResetType = filter.copyObject("unresolvedResetType", this, unresolvedResetType);
		final HDLExpression filteredunresolvedSyncType = filter.copyObject("unresolvedSyncType", this, unresolvedSyncType);
		final HDLRegClockType filteredclockType = filter.copyObject("clockType", this, clockType);
		final HDLRegResetActiveType filteredresetType = filter.copyObject("resetType", this, resetType);
		final HDLRegSyncType filteredsyncType = filter.copyObject("syncType", this, syncType);
		final HDLExpression filteredresetValue = filter.copyObject("resetValue", this, resetValue);
		final HDLExpression filtereddelay = filter.copyObject("delay", this, delay);
		return filter.postFilter((HDLRegisterConfig) this, new HDLRegisterConfig(id, null, filteredclk, filteredrst, filteredunresolvedClockType, filteredunresolvedResetType,
				filteredunresolvedSyncType, filteredclockType, filteredresetType, filteredsyncType, filteredresetValue, filtereddelay, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLRegisterConfig copyDeepFrozen(IHDLObject container) {
		final HDLRegisterConfig copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLRegisterConfig} with the updated container field.
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
	 *            sets the new clk of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated clk field.
	 */
	@Nonnull
	public HDLRegisterConfig setClk(@Nonnull HDLExpression clk) {
		clk = validateClk(clk);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRst()}.
	 *
	 * @param rst
	 *            sets the new rst of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated rst field.
	 */
	@Nonnull
	public HDLRegisterConfig setRst(@Nonnull HDLExpression rst) {
		rst = validateRst(rst);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getUnresolvedClockType()}.
	 *
	 * @param unresolvedClockType
	 *            sets the new unresolvedClockType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated unresolvedClockType field.
	 */
	@Nonnull
	public HDLRegisterConfig setUnresolvedClockType(@Nullable HDLExpression unresolvedClockType) {
		unresolvedClockType = validateUnresolvedClockType(unresolvedClockType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getUnresolvedResetType()}.
	 *
	 * @param unresolvedResetType
	 *            sets the new unresolvedResetType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated unresolvedResetType field.
	 */
	@Nonnull
	public HDLRegisterConfig setUnresolvedResetType(@Nullable HDLExpression unresolvedResetType) {
		unresolvedResetType = validateUnresolvedResetType(unresolvedResetType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getUnresolvedSyncType()}.
	 *
	 * @param unresolvedSyncType
	 *            sets the new unresolvedSyncType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated unresolvedSyncType field.
	 */
	@Nonnull
	public HDLRegisterConfig setUnresolvedSyncType(@Nullable HDLExpression unresolvedSyncType) {
		unresolvedSyncType = validateUnresolvedSyncType(unresolvedSyncType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getClockType()}.
	 *
	 * @param clockType
	 *            sets the new clockType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated clockType field.
	 */
	@Nonnull
	public HDLRegisterConfig setClockType(@Nullable HDLRegClockType clockType) {
		clockType = validateClockType(clockType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getResetType()}.
	 *
	 * @param resetType
	 *            sets the new resetType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated resetType field.
	 */
	@Nonnull
	public HDLRegisterConfig setResetType(@Nullable HDLRegResetActiveType resetType) {
		resetType = validateResetType(resetType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSyncType()}.
	 *
	 * @param syncType
	 *            sets the new syncType of this object. Can be <code>null</code> .
	 * @return a new instance of {@link HDLRegisterConfig} with the updated syncType field.
	 */
	@Nonnull
	public HDLRegisterConfig setSyncType(@Nullable HDLRegSyncType syncType) {
		syncType = validateSyncType(syncType);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getResetValue()}.
	 *
	 * @param resetValue
	 *            sets the new resetValue of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated resetValue field.
	 */
	@Nonnull
	public HDLRegisterConfig setResetValue(@Nonnull HDLExpression resetValue) {
		resetValue = validateResetValue(resetValue);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDelay()}.
	 *
	 * @param delay
	 *            sets the new delay of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated delay field.
	 */
	@Nonnull
	public HDLRegisterConfig setDelay(@Nullable HDLExpression delay) {
		delay = validateDelay(delay);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType,
				resetValue, delay, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractHDLRegisterConfig)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
		if (clk == null) {
			if (other.clk != null) {
				return false;
			}
		} else if (!clk.equals(other.clk)) {
			return false;
		}
		if (rst == null) {
			if (other.rst != null) {
				return false;
			}
		} else if (!rst.equals(other.rst)) {
			return false;
		}
		if (unresolvedClockType == null) {
			if (other.unresolvedClockType != null) {
				return false;
			}
		} else if (!unresolvedClockType.equals(other.unresolvedClockType)) {
			return false;
		}
		if (unresolvedResetType == null) {
			if (other.unresolvedResetType != null) {
				return false;
			}
		} else if (!unresolvedResetType.equals(other.unresolvedResetType)) {
			return false;
		}
		if (unresolvedSyncType == null) {
			if (other.unresolvedSyncType != null) {
				return false;
			}
		} else if (!unresolvedSyncType.equals(other.unresolvedSyncType)) {
			return false;
		}
		if (clockType == null) {
			if (other.clockType != null) {
				return false;
			}
		} else if (!clockType.equals(other.clockType)) {
			return false;
		}
		if (resetType == null) {
			if (other.resetType != null) {
				return false;
			}
		} else if (!resetType.equals(other.resetType)) {
			return false;
		}
		if (syncType == null) {
			if (other.syncType != null) {
				return false;
			}
		} else if (!syncType.equals(other.syncType)) {
			return false;
		}
		if (resetValue == null) {
			if (other.resetValue != null) {
				return false;
			}
		} else if (!resetValue.equals(other.resetValue)) {
			return false;
		}
		if (delay == null) {
			if (other.delay != null) {
				return false;
			}
		} else if (!delay.equals(other.delay)) {
			return false;
		}
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null) {
			return hashCache;
		}
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((clk == null) ? 0 : clk.hashCode());
		result = (prime * result) + ((rst == null) ? 0 : rst.hashCode());
		result = (prime * result) + ((unresolvedClockType == null) ? 0 : unresolvedClockType.hashCode());
		result = (prime * result) + ((unresolvedResetType == null) ? 0 : unresolvedResetType.hashCode());
		result = (prime * result) + ((unresolvedSyncType == null) ? 0 : unresolvedSyncType.hashCode());
		result = (prime * result) + ((clockType == null) ? 0 : clockType.hashCode());
		result = (prime * result) + ((resetType == null) ? 0 : resetType.hashCode());
		result = (prime * result) + ((syncType == null) ? 0 : syncType.hashCode());
		result = (prime * result) + ((resetValue == null) ? 0 : resetValue.hashCode());
		result = (prime * result) + ((delay == null) ? 0 : delay.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLRegisterConfig()");
		if (clk != null) {
			sb.append(".setClk(").append(clk.toConstructionString(spacing + "\t")).append(")");
		}
		if (rst != null) {
			sb.append(".setRst(").append(rst.toConstructionString(spacing + "\t")).append(")");
		}
		if (unresolvedClockType != null) {
			sb.append(".setUnresolvedClockType(").append(unresolvedClockType.toConstructionString(spacing + "\t")).append(")");
		}
		if (unresolvedResetType != null) {
			sb.append(".setUnresolvedResetType(").append(unresolvedResetType.toConstructionString(spacing + "\t")).append(")");
		}
		if (unresolvedSyncType != null) {
			sb.append(".setUnresolvedSyncType(").append(unresolvedSyncType.toConstructionString(spacing + "\t")).append(")");
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
		if (delay != null) {
			sb.append(".setDelay(").append(delay.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateClk(getClk());
		if (getClk() != null) {
			getClk().validateAllFields(this, checkResolve);
		}
		validateRst(getRst());
		if (getRst() != null) {
			getRst().validateAllFields(this, checkResolve);
		}
		validateUnresolvedClockType(getUnresolvedClockType());
		if (getUnresolvedClockType() != null) {
			getUnresolvedClockType().validateAllFields(this, checkResolve);
		}
		validateUnresolvedResetType(getUnresolvedResetType());
		if (getUnresolvedResetType() != null) {
			getUnresolvedResetType().validateAllFields(this, checkResolve);
		}
		validateUnresolvedSyncType(getUnresolvedSyncType());
		if (getUnresolvedSyncType() != null) {
			getUnresolvedSyncType().validateAllFields(this, checkResolve);
		}
		validateClockType(getClockType());
		validateResetType(getResetType());
		validateSyncType(getSyncType());
		validateResetValue(getResetValue());
		if (getResetValue() != null) {
			getResetValue().validateAllFields(this, checkResolve);
		}
		validateDelay(getDelay());
		if (getDelay() != null) {
			getDelay().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLRegisterConfig, HDLClass.HDLObject);
	}

	@Override
	public Iterator<IHDLObject> deepIterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if (clk != null) {
							current = Iterators.concat(Iterators.forArray(clk), clk.deepIterator());
						}
						break;
					case 1:
						if (rst != null) {
							current = Iterators.concat(Iterators.forArray(rst), rst.deepIterator());
						}
						break;
					case 2:
						if (unresolvedClockType != null) {
							current = Iterators.concat(Iterators.forArray(unresolvedClockType), unresolvedClockType.deepIterator());
						}
						break;
					case 3:
						if (unresolvedResetType != null) {
							current = Iterators.concat(Iterators.forArray(unresolvedResetType), unresolvedResetType.deepIterator());
						}
						break;
					case 4:
						if (unresolvedSyncType != null) {
							current = Iterators.concat(Iterators.forArray(unresolvedSyncType), unresolvedSyncType.deepIterator());
						}
						break;
					case 5:
						if (resetValue != null) {
							current = Iterators.concat(Iterators.forArray(resetValue), resetValue.deepIterator());
						}
						break;
					case 6:
						if (delay != null) {
							current = Iterators.concat(Iterators.forArray(delay), delay.deepIterator());
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}

	@Override
	public Iterator<IHDLObject> iterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if (clk != null) {
							current = Iterators.singletonIterator(clk);
						}
						break;
					case 1:
						if (rst != null) {
							current = Iterators.singletonIterator(rst);
						}
						break;
					case 2:
						if (unresolvedClockType != null) {
							current = Iterators.singletonIterator(unresolvedClockType);
						}
						break;
					case 3:
						if (unresolvedResetType != null) {
							current = Iterators.singletonIterator(unresolvedResetType);
						}
						break;
					case 4:
						if (unresolvedSyncType != null) {
							current = Iterators.singletonIterator(unresolvedSyncType);
						}
						break;
					case 5:
						if (resetValue != null) {
							current = Iterators.singletonIterator(resetValue);
						}
						break;
					case 6:
						if (delay != null) {
							current = Iterators.singletonIterator(delay);
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}
}