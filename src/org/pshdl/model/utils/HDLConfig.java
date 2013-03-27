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
package org.pshdl.model.utils;

import java.util.*;

import org.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import org.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType;

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
		if (name == null) {
			defaultClockType = type;
		} else {
			unitClockType.put(name, type);
		}
	}

	public HDLRegResetActiveType getRegResetType(HDLQualifiedName name, HDLRegResetActiveType actual) {
		if (actual != null)
			return actual;
		if (unitResetType.containsKey(name))
			return unitResetType.get(name);
		return defaultResetType;
	}

	public void setRegResetType(HDLQualifiedName name, HDLRegResetActiveType type) {
		if (name == null) {
			defaultResetType = type;
		} else {
			unitResetType.put(name, type);
		}
	}

	public HDLRegSyncType getRegSyncType(HDLQualifiedName name, HDLRegSyncType actual) {
		if (actual != null)
			return actual;
		if (unitSyncType.containsKey(name))
			return unitSyncType.get(name);
		return defaultSyncType;
	}

	public void setRegSyncType(HDLQualifiedName name, HDLRegSyncType type) {
		if (name == null) {
			defaultSyncType = type;
		} else {
			unitSyncType.put(name, type);
		}
	}

}
