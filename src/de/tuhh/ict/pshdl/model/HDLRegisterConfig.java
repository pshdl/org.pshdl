package de.tuhh.ict.pshdl.model;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.*;

import java.util.*;

@SuppressWarnings("all")
public class HDLRegisterConfig extends AbstractHDLRegisterConfig {
	public HDLRegisterConfig(HDLObject container, HDLQualifiedName clk, HDLQualifiedName rst, HDLRegClockType clockType, HDLRegResetType resetType, HDLRegSyncType syncType,
			HDLExpression resetValue) {
		super(container, clk, rst, clockType, resetType, syncType, resetValue);
	}

	public HDLRegisterConfig() {
		super();
	}

	public static enum HDLRegClockType {
		RISING, FALLING,
	}

	public static enum HDLRegResetType {
		HIGH_ACTIVE, LOW_ACTIVE,
	}

	public static enum HDLRegSyncType {
		SYNC, ASYNC,
	}

	// $CONTENT-BEGIN$
	public static HDLRegisterConfig defaultConfig() {
		return new HDLRegisterConfig(null, HDLQualifiedName.create("$clk"), HDLQualifiedName.create("$rst"), null, null, null, new HDLLiteral(null, "0"));
	}
	// $CONTENT-END$
}
