package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegSyncType;

public class HDLConfig {
	private HDLRegClockType defaultClockType = HDLRegClockType.RISING;
	private Map<HDLQualifiedName, HDLRegClockType> unitClockType = new HashMap<HDLQualifiedName, HDLRegClockType>();
	private HDLRegResetActiveType defaultResetType = HDLRegResetActiveType.HIGH;
	private Map<HDLQualifiedName, HDLRegResetActiveType> unitResetType = new HashMap<HDLQualifiedName, HDLRegResetActiveType>();
	private HDLRegSyncType defaultSyncType = HDLRegSyncType.SYNC;
	private Map<HDLQualifiedName, HDLRegSyncType> unitSyncType = new HashMap<HDLQualifiedName, HDLRegSyncType>();

	public HDLRegClockType getRegClockType(HDLQualifiedName name, HDLRegClockType actual) {
		if (actual != null)
			return actual;
		if (unitClockType.containsKey(name))
			return unitClockType.get(name);
		return defaultClockType;
	}

	public void setRegClockType(HDLQualifiedName name, HDLRegClockType type) {
		if (name == null)
			defaultClockType = type;
		else
			unitClockType.put(name, type);
	}

	public HDLRegResetActiveType getRegResetType(HDLQualifiedName name, HDLRegResetActiveType actual) {
		if (actual != null)
			return actual;
		if (unitResetType.containsKey(name))
			return unitResetType.get(name);
		return defaultResetType;
	}

	public void setRegResetType(HDLQualifiedName name, HDLRegResetActiveType type) {
		if (name == null)
			defaultResetType = type;
		else
			unitResetType.put(name, type);
	}

	public HDLRegSyncType getRegSyncType(HDLQualifiedName name, HDLRegSyncType actual) {
		if (actual != null)
			return actual;
		if (unitSyncType.containsKey(name))
			return unitSyncType.get(name);
		return defaultSyncType;
	}

	public void setRegSyncType(HDLQualifiedName name, HDLRegSyncType type) {
		if (name == null)
			defaultSyncType = type;
		else
			unitSyncType.put(name, type);
	}

	public HDLRegisterConfig normalize(HDLRegisterConfig config) {
		return null;
	}

}
