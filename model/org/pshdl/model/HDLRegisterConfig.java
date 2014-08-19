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
package org.pshdl.model;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLRegisterConfig;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

/**
 * The class HDLRegisterConfig contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression clk. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression rst. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression unresolvedClockType. Can be <code>null</code>.</li>
 * <li>HDLExpression unresolvedResetType. Can be <code>null</code>.</li>
 * <li>HDLExpression unresolvedSyncType. Can be <code>null</code>.</li>
 * <li>HDLRegClockType clockType. Can be <code>null</code>.</li>
 * <li>HDLRegResetActiveType resetType. Can be <code>null</code>.</li>
 * <li>HDLRegSyncType syncType. Can be <code>null</code>.</li>
 * <li>HDLExpression resetValue. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression delay. Can be <code>null</code>.</li>
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
	public HDLRegisterConfig(int id, @Nullable IHDLObject container, @Nonnull HDLExpression clk, @Nonnull HDLExpression rst, @Nullable HDLExpression unresolvedClockType,
			@Nullable HDLExpression unresolvedResetType, @Nullable HDLExpression unresolvedSyncType, @Nullable HDLRegClockType clockType,
			@Nullable HDLRegResetActiveType resetType, @Nullable HDLRegSyncType syncType, @Nonnull HDLExpression resetValue, @Nullable HDLExpression delay, boolean validate) {
		super(id, container, clk, rst, unresolvedClockType, unresolvedResetType, unresolvedSyncType, clockType, resetType, syncType, resetValue, delay, validate);
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
	 * The accessor for the field clk which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fClk = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("clk", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getClk();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setClk(value);
		}
	};
	/**
	 * The accessor for the field rst which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fRst = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("rst", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getRst();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setRst(value);
		}
	};
	/**
	 * The accessor for the field unresolvedClockType which is of type
	 * HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fUnresolvedClockType = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("unresolvedClockType",
			HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getUnresolvedClockType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setUnresolvedClockType(value);
		}
	};
	/**
	 * The accessor for the field unresolvedResetType which is of type
	 * HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fUnresolvedResetType = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("unresolvedResetType",
			HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getUnresolvedResetType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setUnresolvedResetType(value);
		}
	};
	/**
	 * The accessor for the field unresolvedSyncType which is of type
	 * HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fUnresolvedSyncType = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("unresolvedSyncType",
			HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getUnresolvedSyncType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setUnresolvedSyncType(value);
		}
	};
	/**
	 * The accessor for the field clockType which is of type HDLRegClockType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegClockType> fClockType = new HDLFieldAccess<HDLRegisterConfig, HDLRegClockType>("clockType", HDLRegClockType.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLRegClockType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getClockType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLRegClockType value) {
			if (obj == null)
				return null;
			return obj.setClockType(value);
		}
	};
	/**
	 * The accessor for the field resetType which is of type
	 * HDLRegResetActiveType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegResetActiveType> fResetType = new HDLFieldAccess<HDLRegisterConfig, HDLRegResetActiveType>("resetType",
			HDLRegResetActiveType.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLRegResetActiveType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLRegResetActiveType value) {
			if (obj == null)
				return null;
			return obj.setResetType(value);
		}
	};
	/**
	 * The accessor for the field syncType which is of type HDLRegSyncType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType> fSyncType = new HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType>("syncType", HDLRegSyncType.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLRegSyncType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getSyncType();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLRegSyncType value) {
			if (obj == null)
				return null;
			return obj.setSyncType(value);
		}
	};
	/**
	 * The accessor for the field resetValue which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fResetValue = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("resetValue", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetValue();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setResetValue(value);
		}
	};
	/**
	 * The accessor for the field delay which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fDelay = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>("delay", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getDelay();
		}

		@Override
		public HDLRegisterConfig setValue(HDLRegisterConfig obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setDelay(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (clk == obj)
			return fClk;
		if (rst == obj)
			return fRst;
		if (unresolvedClockType == obj)
			return fUnresolvedClockType;
		if (unresolvedResetType == obj)
			return fUnresolvedResetType;
		if (unresolvedSyncType == obj)
			return fUnresolvedSyncType;
		if (clockType == obj)
			return fClockType;
		if (resetType == obj)
			return fResetType;
		if (syncType == obj)
			return fSyncType;
		if (resetValue == obj)
			return fResetValue;
		if (delay == obj)
			return fDelay;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$

	public static final String EDGE_PARAM = "clockEdge";
	public static final String CLOCK_PARAM = "clock";
	public static final String RESET_PARAM = "reset";
	public static final String DELAY_PARAM = "delay";
	public static final String RESET_SYNC_PARAM = "resetSync";
	public static final String RESET_TYPE_PARAM = "resetType";
	public static final String RESET_VALUE_PARAM = "resetValue";
	public static final Set<String> VALID_PARAMS = Sets.newHashSet(EDGE_PARAM, CLOCK_PARAM, RESET_PARAM, DELAY_PARAM, RESET_SYNC_PARAM, RESET_TYPE_PARAM, RESET_VALUE_PARAM);
	public static final String DEF_RST = "$rst";
	public static final String DEF_CLK = "$clk";

	public static final GenericMeta<Iterable<HDLArgument>> ORIGINAL_ARGS = new GenericMeta<>("OriginalArgs", true);

	public static HDLExpression DEF_RST_REF() {
		return new HDLVariableRef().setVar(HDLQualifiedName.create(DEF_RST));
	}

	public static HDLExpression DEF_CLK_REF() {
		return new HDLVariableRef().setVar(HDLQualifiedName.create(DEF_CLK));
	}

	@Nonnull
	public static HDLRegisterConfig defaultConfig() {
		return new HDLRegisterConfig()//
				.setClk(DEF_CLK_REF())//
				.setRst(DEF_RST_REF())//
				.setResetValue(HDLLiteral.get(0))//
				.normalize()//
		;
	}

	@Nonnull
	public static HDLRegisterConfig fromArgs(Iterable<HDLArgument> args) {
		HDLRegisterConfig config = defaultConfig();
		for (final HDLArgument genArgs : args) {
			final String name = genArgs.getName();
			final HDLExpression expression = genArgs.getExpression();
			if (RESET_PARAM.equals(name)) {
				config = config.setRst(expression);
			}
			if (CLOCK_PARAM.equals(name)) {
				config = config.setClk(expression);
			}
			if (EDGE_PARAM.equals(name)) {
				config = config.setUnresolvedClockType(expression);
			}
			if (RESET_SYNC_PARAM.equals(name)) {
				config = config.setUnresolvedSyncType(expression);
			}
			if (RESET_TYPE_PARAM.equals(name)) {
				config = config.setUnresolvedResetType(expression);
			}
			if (RESET_VALUE_PARAM.equals(name)) {
				config = config.setResetValue(expression);
			}
			if (DELAY_PARAM.equals(name)) {
				config = config.setDelay(expression);
			}
		}
		config.addMeta(ORIGINAL_ARGS, args);
		return config;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLRegisterConfig))
			return false;
		final AbstractHDLRegisterConfig other = (AbstractHDLRegisterConfig) obj;
		if (clk == null) {
			if (other.getClk() != null)
				return false;
		} else if (!clk.equals(other.getClk()))
			return false;
		if (rst == null) {
			if (other.getRst() != null)
				return false;
		} else if (!rst.equals(other.getRst()))
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

	private Integer hashCache = null;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = 1;
		final int prime = 31;
		result = (prime * result) + (clk == null ? 0 : clk.hashCode());
		result = (prime * result) + (rst == null ? 0 : rst.hashCode());
		result = (prime * result) + (clockType == null ? 0 : clockType.hashCode());
		result = (prime * result) + (resetType == null ? 0 : resetType.hashCode());
		result = (prime * result) + (syncType == null ? 0 : syncType.hashCode());
		hashCache = result;
		return result;
	}

	public static HDLVariable defaultClk(boolean internalName) {
		if (internalName)
			return new HDLVariable().setName(DEF_CLK);
		return new HDLVariable().setName(DEF_CLK.substring(1));
	}

	public static HDLVariable defaultRst(boolean internalName) {
		if (internalName)
			return new HDLVariable().setName(DEF_RST);
		return new HDLVariable().setName(DEF_RST.substring(1));
	}

	public HDLRegisterConfig normalize() {
		HDLRegisterConfig res = this;
		if (getResetType() == null) {
			final HDLExpression unresolvedType = getUnresolvedResetType();
			if (unresolvedType != null) {
				res = res.setResetTypeFromUnresolved();
			} else {
				res = res.setResetType(HDLRegResetActiveType.HIGH);
			}
		}
		if (getSyncType() == null) {
			final HDLExpression unresolvedType = getUnresolvedSyncType();
			if (unresolvedType != null) {
				res = res.setSyncTypeFromUnresolved();
			} else {
				res = res.setSyncType(HDLRegSyncType.SYNC);
			}
		}
		if (getClockType() == null) {
			final HDLExpression unresolvedType = getUnresolvedClockType();
			if (unresolvedType != null) {
				res = res.setClockTypeFromUnresolved();
			} else {
				res = res.setClockType(HDLRegClockType.RISING);
			}
		}
		if (res == this)
			return this;
		return res.copyFiltered(CopyFilter.DEEP_META);
	}

	public HDLRegisterConfig setResetTypeFromUnresolved() {
		final HDLExpression unresolvedType = getUnresolvedResetType();
		if (unresolvedType == null)
			return this;
		HDLRegisterConfig res = this;
		if (unresolvedType.getClassType() == HDLClass.HDLEnumRef) {
			final HDLEnumRef ref = (HDLEnumRef) unresolvedType;
			final Optional<HDLVariable> var = ref.resolveVar();
			if (var.isPresent()) {
				switch (var.get().getName()) {
				case "HIGH":
					res = res.setResetType(HDLRegResetActiveType.HIGH);
					break;
				case "LOW":
					res = res.setResetType(HDLRegResetActiveType.LOW);
					break;
				default:
				}
				res = res.setUnresolvedResetType(null);
			}
		}
		res.addMeta(ORIGINAL_ARGS, getMeta(ORIGINAL_ARGS));
		return res;
	}

	public HDLRegisterConfig setSyncTypeFromUnresolved() {
		final HDLExpression unresolvedType = getUnresolvedSyncType();
		if (unresolvedType == null)
			return this;
		HDLRegisterConfig res = this;
		if (unresolvedType.getClassType() == HDLClass.HDLEnumRef) {
			final HDLEnumRef ref = (HDLEnumRef) unresolvedType;
			final Optional<HDLVariable> var = ref.resolveVar();
			if (var.isPresent()) {
				switch (var.get().getName()) {
				case "ASYNC":
					res = res.setSyncType(HDLRegSyncType.ASYNC);
					break;
				case "SYNC":
					res = res.setSyncType(HDLRegSyncType.SYNC);
					break;
				default:
				}
				res = res.setUnresolvedSyncType(null);
			}
		}
		res.addMeta(ORIGINAL_ARGS, getMeta(ORIGINAL_ARGS));
		return res;
	}

	public HDLRegisterConfig setClockTypeFromUnresolved() {
		final HDLExpression unresolvedType = getUnresolvedClockType();
		if (unresolvedType == null)
			return this;
		HDLRegisterConfig res = this;
		if (unresolvedType.getClassType() == HDLClass.HDLEnumRef) {
			final HDLEnumRef ref = (HDLEnumRef) unresolvedType;
			final Optional<HDLVariable> var = ref.resolveVar();
			if (var.isPresent()) {
				switch (var.get().getName()) {
				case "RISING":
					res = res.setClockType(HDLRegClockType.RISING);
					break;
				case "FALLING":
					res = res.setClockType(HDLRegClockType.FALLING);
					break;
				default:
				}
				res = res.setUnresolvedClockType(null);
			}
		}
		res.addMeta(ORIGINAL_ARGS, getMeta(ORIGINAL_ARGS));
		return res;
	}

	// $CONTENT-END$

}
