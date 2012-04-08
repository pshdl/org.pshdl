package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.impl.*;
import java.util.*;

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
	public HDLRegisterConfig(HDLObject container, HDLQualifiedName clk, HDLQualifiedName rst, HDLRegClockType clockType, HDLRegResetType resetType, HDLRegSyncType syncType,
			HDLExpression resetValue, boolean validate) {
		super(container, clk, rst, clockType, resetType, syncType, resetValue, validate);
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
	public HDLRegisterConfig(HDLObject container, HDLQualifiedName clk, HDLQualifiedName rst, HDLRegClockType clockType, HDLRegResetType resetType, HDLRegSyncType syncType,
			HDLExpression resetValue) {
		this(container, clk, rst, clockType, resetType, syncType, resetValue, true);
	}

	public HDLRegisterConfig() {
		super();
	}

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

	// $CONTENT-BEGIN$

	public static final String EDGE_PARAM = "clockEdge";
	public static final String RESET_PARAM = "reset";
	public static final String RESET_SYNC_PARAM = "resetSync";
	public static final String CLOCK_PARAM = "clock";
	public static final String RESET_TYPE_PARAM = "resetType";
	public static final String RESET_VALUE_PARAM = "resetValue";

	public static HDLRegisterConfig defaultConfig() {
		return new HDLRegisterConfig(null, HDLQualifiedName.create("$clk"), HDLQualifiedName.create("$rst"), null, null, null, new HDLLiteral(null, "0"));
	}

	public static HDLRegisterConfig fromArgs(ArrayList<HDLGeneratorArgument> args) {
		HDLRegisterConfig config = defaultConfig();
		for (HDLGeneratorArgument genArgs : args) {
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
				config = config.setResetValue(genArgs.getExpression());
		}
		return config;
	}
	// $CONTENT-END$

}
