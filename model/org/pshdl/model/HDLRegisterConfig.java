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
package org.pshdl.model;

import static org.pshdl.model.extensions.FullNameExtension.*;

import javax.annotation.*;

import org.pshdl.model.impl.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLRegisterConfig contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName clk. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName rst. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLRegClockType clockType. Can be <code>null</code>.</li>
 * <li>HDLRegResetActiveType resetType. Can be <code>null</code>.</li>
 * <li>HDLRegSyncType syncType. Can be <code>null</code>.</li>
 * <li>HDLExpression resetValue. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLRegisterConfig extends AbstractHDLRegisterConfig {
	/**
	 * Constructs a new instance of {@link HDLRegisterConfig}
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
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLRegisterConfig(@Nullable IHDLObject container, @Nonnull HDLQualifiedName clk, @Nonnull HDLQualifiedName rst, @Nullable HDLRegClockType clockType,
			@Nullable HDLRegResetActiveType resetType, @Nullable HDLRegSyncType syncType, @Nonnull HDLExpression resetValue, boolean validate) {
		super(container, clk, rst, clockType, resetType, syncType, resetValue, validate);
	}

	public HDLRegisterConfig() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLRegisterConfig;
	}

	public static enum HDLRegClockType {
		RISING, FALLING;
	}

	public static enum HDLRegResetActiveType {
		HIGH, LOW;
	}

	public static enum HDLRegSyncType {
		SYNC, ASYNC;
	}

	/**
	 * The accessor for the field clk which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName> fClk = new HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName>("clk") {
		@Override
		public HDLQualifiedName getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getClkRefName();
		}
	};
	/**
	 * The accessor for the field rst which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName> fRst = new HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName>("rst") {
		@Override
		public HDLQualifiedName getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getRstRefName();
		}
	};
	/**
	 * The accessor for the field clockType which is of type HDLRegClockType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegClockType> fClockType = new HDLFieldAccess<HDLRegisterConfig, HDLRegClockType>("clockType") {
		@Override
		public HDLRegClockType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getClockType();
		}
	};
	/**
	 * The accessor for the field resetType which is of type
	 * HDLRegResetActiveType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegResetActiveType> fResetType = new HDLFieldAccess<HDLRegisterConfig, HDLRegResetActiveType>("resetType") {
		@Override
		public HDLRegResetActiveType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetType();
		}
	};
	/**
	 * The accessor for the field syncType which is of type HDLRegSyncType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType> fSyncType = new HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType>("syncType") {
		@Override
		public HDLRegSyncType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getSyncType();
		}
	};
	/**
	 * The accessor for the field resetValue which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fResetValue = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("resetValue") {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetValue();
		}
	};
	// $CONTENT-BEGIN$

	public static final String EDGE_PARAM = "clockEdge";
	public static final String CLOCK_PARAM = "clock";
	public static final String RESET_PARAM = "reset";
	public static final String RESET_SYNC_PARAM = "resetSync";
	public static final String RESET_TYPE_PARAM = "resetType";
	public static final String RESET_VALUE_PARAM = "resetValue";
	public static final String DEF_RST = "$rst";
	public static final String DEF_CLK = "$clk";

	@Nonnull
	public static HDLRegisterConfig defaultConfig() {
		return new HDLRegisterConfig().setClk(HDLQualifiedName.create(DEF_CLK)).setRst(HDLQualifiedName.create(DEF_RST)).setResetValue(HDLLiteral.get(0));
	}

	@Nonnull
	public static HDLRegisterConfig fromArgs(Iterable<HDLArgument> args) {
		HDLRegisterConfig config = defaultConfig();
		for (HDLArgument genArgs : args) {
			String name = genArgs.getName();
			HDLQualifiedName refName = getRefName(genArgs.getExpression());
			if (RESET_PARAM.equals(name)) {
				if (refName == null)
					throw new IllegalArgumentException("Can not get a name for clk from:" + genArgs.getExpression());
				config = config.setRst(refName);
			}
			if (CLOCK_PARAM.equals(name)) {
				if (refName == null)
					throw new IllegalArgumentException("Can not get a name for rst from:" + genArgs.getExpression());
				config = config.setClk(refName);
			}
			if (EDGE_PARAM.equals(name)) {
				String value = getString(genArgs);
				if (value != null) {
					config = config.setClockType(HDLRegClockType.valueOf(value.toUpperCase()));
				} else {
					if (refName == null)
						throw new IllegalArgumentException("Can not get a name for edge parameter from:" + genArgs.getExpression());
					config = config.setClockType(HDLRegClockType.valueOf(refName.getLastSegment()));
				}
			}
			if (RESET_SYNC_PARAM.equals(name)) {
				String value = getString(genArgs);
				if (value != null) {
					config = config.setSyncType(HDLRegSyncType.valueOf(value.toUpperCase()));
				} else {
					if (refName == null)
						throw new IllegalArgumentException("Can not get a name for sync parameter from:" + genArgs.getExpression());
					config = config.setSyncType(HDLRegSyncType.valueOf(refName.getLastSegment()));
				}
			}
			if (RESET_TYPE_PARAM.equals(name)) {
				String value = getString(genArgs);
				if (value != null) {
					config = config.setResetType(HDLRegResetActiveType.valueOf(value.toUpperCase()));
				} else {
					if (refName == null)
						throw new IllegalArgumentException("Can not get a name for sync parameter from:" + genArgs.getExpression());
					config = config.setResetType(HDLRegResetActiveType.valueOf(refName.getLastSegment()));
				}
			}
			if (RESET_VALUE_PARAM.equals(name)) {
				config = config.setResetValue(genArgs.getExpression());
			}
		}
		return config;
	}

	/**
	 * Attempts to get a name for expression
	 * 
	 * @param expression
	 * @return
	 */
	@Nullable
	private static HDLQualifiedName getRefName(HDLExpression expression) {
		if (expression instanceof HDLVariableRef) {
			HDLResolvedRef rr = (HDLResolvedRef) expression;
			return rr.getVarRefName();
		}
		if (expression instanceof HDLUnresolvedFragment) {
			HDLUnresolvedFragment uf = (HDLUnresolvedFragment) expression;
			HDLUnresolvedFragment sub = uf.getSub();
			if (sub != null)
				return new HDLQualifiedName(uf.getFrag()).append(sub.getFrag());
			return new HDLQualifiedName(uf.getFrag());
		}
		return null;
	}

	@Nullable
	private static String getString(HDLArgument genArgs) {
		if (genArgs.getExpression() instanceof HDLLiteral) {
			HDLLiteral lit = (HDLLiteral) genArgs.getExpression();
			if (lit.getStr())
				return lit.getVal();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRegisterConfig))
			return false;
		// if (!super.equals(obj))
		// return false;
		AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
		if (clk == null) {
			if (other.getClkRefName() != null)
				return false;
		} else if (!clk.equals(other.getClkRefName()))
			return false;
		if (rst == null) {
			if (other.getRstRefName() != null)
				return false;
		} else if (!rst.equals(other.getRstRefName()))
			return false;
		if (clockType == null) {
			if (other.getClockType() != null)
				return false;
		} else if (!clockType.equals(other.getClockType()))
			return false;
		if (resetType == null) {
			if (other.getResetType() != null)
				return false;
		} else if (!resetType.equals(other.getResetType()))
			return false;
		if (syncType == null) {
			if (other.getSyncType() != null)
				return false;
		} else if (!syncType.equals(other.getSyncType()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 1;
		final int prime = 31;
		result = (prime * result) + (clk == null ? 0 : clk.hashCode());
		result = (prime * result) + (rst == null ? 0 : rst.hashCode());
		result = (prime * result) + (clockType == null ? 0 : clockType.hashCode());
		result = (prime * result) + (resetType == null ? 0 : resetType.hashCode());
		result = (prime * result) + (syncType == null ? 0 : syncType.hashCode());
		return result;
	}

	public static HDLVariable defaultClk() {
		return new HDLVariable().setName(DEF_CLK);
	}

	public static HDLVariable defaultRst() {
		return new HDLVariable().setName(DEF_RST);
	}

	public HDLRegisterConfig normalize() {
		HDLRegisterConfig res = this;
		HDLUnit unit = getContainer(HDLUnit.class);
		if (unit != null) {
			HDLQualifiedName fullName = fullNameOf(unit);
			HDLLibrary library = unit.getLibrary();
			if (library != null) {
				HDLConfig config = library.getConfig();
				if (getResetType() == null) {
					res = res.setResetType(config.getRegResetType(fullName, null));
				}
				if (getSyncType() == null) {
					res = res.setSyncType(config.getRegSyncType(fullName, null));
				}
				if (getClockType() == null) {
					res = res.setClockType(config.getRegClockType(fullName, null));
				}
			}
		}
		return res.copyDeepFrozen(getContainer());
	}
	// $CONTENT-END$

}
