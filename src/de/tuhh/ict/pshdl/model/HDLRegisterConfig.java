package de.tuhh.ict.pshdl.model;

import java.util.*;

import org.eclipse.jdt.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLRegisterConfig contains the following fields
 * <ul>
 * <li>HDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName clk. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLQualifiedName rst. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLRegClockType clockType. If <code>null</code>,
 * {@link HDLRegClockType#RISING} is used as default.</li>
 * <li>HDLRegResetType resetType. If <code>null</code>,
 * {@link HDLRegResetType#HIGH_ACTIVE} is used as default.</li>
 * <li>HDLRegSyncType syncType. If <code>null</code>,
 * {@link HDLRegSyncType#SYNC} is used as default.</li>
 * <li>HDLExpression resetValue. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLRegisterConfig extends AbstractHDLRegisterConfig {
	/**
	 * Constructs a new instance of {@link HDLRegisterConfig}
	 * 
	 * @param containerID
	 *            a unique ID that identifies this instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param clk
	 *            the value for clk. Can <b>not</b> be <code>null</code>.
	 * @param rst
	 *            the value for rst. Can <b>not</b> be <code>null</code>.
	 * @param clockType
	 *            the value for clockType. If <code>null</code>,
	 *            {@link HDLRegClockType#RISING} is used as default.
	 * @param resetType
	 *            the value for resetType. If <code>null</code>,
	 *            {@link HDLRegResetType#HIGH_ACTIVE} is used as default.
	 * @param syncType
	 *            the value for syncType. If <code>null</code>,
	 *            {@link HDLRegSyncType#SYNC} is used as default.
	 * @param resetValue
	 *            the value for resetValue. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLRegisterConfig(int containerID, @Nullable HDLObject container, @NonNull HDLQualifiedName clk, @NonNull HDLQualifiedName rst, @Nullable HDLRegClockType clockType,
			@Nullable HDLRegResetType resetType, @Nullable HDLRegSyncType syncType, @NonNull HDLExpression resetValue, boolean validate) {
		super(containerID, container, clk, rst, clockType, resetType, syncType, resetValue, validate);
	}

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
	 *            the value for clockType. If <code>null</code>,
	 *            {@link HDLRegClockType#RISING} is used as default.
	 * @param resetType
	 *            the value for resetType. If <code>null</code>,
	 *            {@link HDLRegResetType#HIGH_ACTIVE} is used as default.
	 * @param syncType
	 *            the value for syncType. If <code>null</code>,
	 *            {@link HDLRegSyncType#SYNC} is used as default.
	 * @param resetValue
	 *            the value for resetValue. Can <b>not</b> be <code>null</code>.
	 */
	public HDLRegisterConfig(int containerID, @Nullable HDLObject container, @NonNull HDLQualifiedName clk, @NonNull HDLQualifiedName rst, @Nullable HDLRegClockType clockType,
			@Nullable HDLRegResetType resetType, @Nullable HDLRegSyncType syncType, @NonNull HDLExpression resetValue) {
		this(containerID, container, clk, rst, clockType, resetType, syncType, resetValue, true);
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

	public static enum HDLRegResetType {
		HIGH_ACTIVE, LOW_ACTIVE;
	}

	public static enum HDLRegSyncType {
		SYNC, ASYNC;
	}

	/**
	 * The accessor for the field clk which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName> fClk = new HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName>() {
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
	public static HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName> fRst = new HDLFieldAccess<HDLRegisterConfig, HDLQualifiedName>() {
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
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegClockType> fClockType = new HDLFieldAccess<HDLRegisterConfig, HDLRegClockType>() {
		@Override
		public HDLRegClockType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getClockType();
		}
	};
	/**
	 * The accessor for the field resetType which is of type HDLRegResetType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegResetType> fResetType = new HDLFieldAccess<HDLRegisterConfig, HDLRegResetType>() {
		@Override
		public HDLRegResetType getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetType();
		}
	};
	/**
	 * The accessor for the field syncType which is of type HDLRegSyncType.
	 */
	public static HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType> fSyncType = new HDLFieldAccess<HDLRegisterConfig, HDLRegSyncType>() {
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
	public static HDLFieldAccess<HDLRegisterConfig, HDLExpression> fResetValue = new HDLFieldAccess<HDLRegisterConfig, HDLExpression>() {
		@Override
		public HDLExpression getValue(HDLRegisterConfig obj) {
			if (obj == null)
				return null;
			return obj.getResetValue();
		}
	};
	// $CONTENT-BEGIN$

	public static final String EDGE_PARAM = "clockEdge";
	public static final String RESET_PARAM = "reset";
	public static final String RESET_SYNC_PARAM = "resetSync";
	public static final String CLOCK_PARAM = "clock";
	public static final String RESET_TYPE_PARAM = "resetType";
	public static final String RESET_VALUE_PARAM = "resetValue";
	public static final String DEF_RST = "$rst";
	public static final String DEF_CLK = "$clk";

	public static HDLRegisterConfig defaultConfig() {
		return new HDLRegisterConfig().setClk(HDLQualifiedName.create(DEF_CLK)).setRst(HDLQualifiedName.create(DEF_RST)).setResetValue(HDLLiteral.get(0));
	}

	public static HDLRegisterConfig fromArgs(ArrayList<HDLArgument> args) {
		HDLRegisterConfig config = defaultConfig();
		for (HDLArgument genArgs : args) {
			String name = genArgs.getName();
			if (RESET_PARAM.equals(name))
				config = config.setRst(((HDLVariableRef) genArgs.getExpression()).getVarRefName());
			if (CLOCK_PARAM.equals(name))
				config = config.setClk(((HDLVariableRef) genArgs.getExpression()).getVarRefName());
			if (EDGE_PARAM.equals(name))
				config = config.setClockType(HDLRegClockType.valueOf(genArgs.getValue().toUpperCase()));
			if (RESET_SYNC_PARAM.equals(name))
				config = config.setSyncType(HDLRegSyncType.valueOf(genArgs.getValue().toUpperCase()));
			if (RESET_TYPE_PARAM.equals(name))
				config = config.setResetType(HDLRegResetType.valueOf(genArgs.getValue().toUpperCase()));
			if (RESET_VALUE_PARAM.equals(name))
				config = config.setResetValue(genArgs.getExpression().copy());
		}
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
		result = (prime * result) + ((clk == null) ? 0 : clk.hashCode());
		result = (prime * result) + ((rst == null) ? 0 : rst.hashCode());
		result = (prime * result) + ((clockType == null) ? 0 : clockType.hashCode());
		result = (prime * result) + ((resetType == null) ? 0 : resetType.hashCode());
		result = (prime * result) + ((syncType == null) ? 0 : syncType.hashCode());
		return result;
	}

	public static HDLVariable defaultClk() {
		return new HDLVariable().setName(DEF_CLK);
	}

	public static HDLVariable defaultRst() {
		return new HDLVariable().setName(DEF_RST);
	}
	// $CONTENT-END$

}
