/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
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

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import org.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;
import com.google.common.collect.*;

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
	 * @param enable
	 *            the value for enable. Can be <code>null</code>.
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
	public AbstractHDLRegisterConfig(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName clk, @Nonnull HDLQualifiedName rst, @Nullable HDLQualifiedName enable,
			@Nullable HDLRegClockType clockType, @Nullable HDLRegResetActiveType resetType, @Nullable HDLRegSyncType syncType, @Nonnull HDLExpression resetValue,
			@Nullable HDLExpression delay, boolean validate) {
		super(id, container, validate);
		if (validate) {
			clk = validateClk(clk);
		}
		this.clk = clk;
		if (validate) {
			rst = validateRst(rst);
		}
		this.rst = rst;
		if (validate) {
			enable = validateEnable(enable);
		}
		this.enable = enable;
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
		this.enable = null;
		this.clockType = null;
		this.resetType = null;
		this.syncType = null;
		this.resetValue = null;
		this.delay = null;
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

	protected final HDLQualifiedName enable;

	@Nullable
	public Optional<HDLVariable> resolveEnable() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, enable);
	}

	public HDLQualifiedName getEnableRefName() {
		return enable;
	}

	protected HDLQualifiedName validateEnable(HDLQualifiedName enable) {
		return enable;
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
		if (resetValue == null)
			throw new IllegalArgumentException("The field resetValue can not be null!");
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
		final HDLRegisterConfig newObject = new HDLRegisterConfig(id, null, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLQualifiedName filteredclk = filter.copyObject("clk", this, clk);
		final HDLQualifiedName filteredrst = filter.copyObject("rst", this, rst);
		final HDLQualifiedName filteredenable = filter.copyObject("enable", this, enable);
		final HDLRegClockType filteredclockType = filter.copyObject("clockType", this, clockType);
		final HDLRegResetActiveType filteredresetType = filter.copyObject("resetType", this, resetType);
		final HDLRegSyncType filteredsyncType = filter.copyObject("syncType", this, syncType);
		final HDLExpression filteredresetValue = filter.copyObject("resetValue", this, resetValue);
		final HDLExpression filtereddelay = filter.copyObject("delay", this, delay);
		return filter.postFilter((HDLRegisterConfig) this, new HDLRegisterConfig(id, null, filteredclk, filteredrst, filteredenable, filteredclockType, filteredresetType,
				filteredsyncType, filteredresetValue, filtereddelay, false));
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getEnable()}.
	 * 
	 * @param enable
	 *            sets the new enable of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         enable field.
	 */
	@Nonnull
	public HDLRegisterConfig setEnable(@Nullable HDLQualifiedName enable) {
		enable = validateEnable(enable);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDelay()}.
	 * 
	 * @param delay
	 *            sets the new delay of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLRegisterConfig} with the updated
	 *         delay field.
	 */
	@Nonnull
	public HDLRegisterConfig setDelay(@Nullable HDLExpression delay) {
		delay = validateDelay(delay);
		final HDLRegisterConfig res = new HDLRegisterConfig(id, container, clk, rst, enable, clockType, resetType, syncType, resetValue, delay, false);
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
		final AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
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
		if (enable == null) {
			if (other.enable != null)
				return false;
		} else if (!enable.equals(other.enable))
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
		if (delay == null) {
			if (other.delay != null)
				return false;
		} else if (!delay.equals(other.delay))
			return false;
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((clk == null) ? 0 : clk.hashCode());
		result = (prime * result) + ((rst == null) ? 0 : rst.hashCode());
		result = (prime * result) + ((enable == null) ? 0 : enable.hashCode());
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
			sb.append(".setClk(HDLQualifiedName.create(\"").append(clk).append("\"))");
		}
		if (rst != null) {
			sb.append(".setRst(HDLQualifiedName.create(\"").append(rst).append("\"))");
		}
		if (enable != null) {
			sb.append(".setEnable(HDLQualifiedName.create(\"").append(enable).append("\"))");
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
		validateClk(getClkRefName());
		if (checkResolve && (getClkRefName() != null))
			if (!resolveClk().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getClkRefName()));
		validateRst(getRstRefName());
		if (checkResolve && (getRstRefName() != null))
			if (!resolveRst().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getRstRefName()));
		validateEnable(getEnableRefName());
		if (checkResolve && (getEnableRefName() != null))
			if (!resolveEnable().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getEnableRefName()));
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
						if (resetValue != null) {
							current = Iterators.concat(Iterators.forArray(resetValue), resetValue.deepIterator());
						}
						break;
					case 1:
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
						if (resetValue != null) {
							current = Iterators.singletonIterator(resetValue);
						}
						break;
					case 1:
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