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
package org.pshdl.model.types.builtIn.busses;

import java.util.Map;

import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.types.builtIn.busses.memorymodel.*;
import org.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.collect.Maps;

public class CommonBusCode {
	public static HDLSwitchCaseStatement createWriteSwitch(Row row, Map<String, Integer> intPos, Map<String, Boolean> isArray, HDLSwitchCaseStatement label,
			final HDLVariableRef busData) {
		int bitPos = 31;
		for (final NamedElement ne : row.definitions) {
			final Definition def = (Definition) ne;
			final int size = MemoryModel.getSize(def);
			if (def.modFlag) {
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.getName(row) + "$written"));
				target = createRef(intPos, isArray, def, target, row);
				label = label.addDos(new HDLAssignment().setLeft(target).setType(HDLAssignmentType.ASSGN).setRight(HDLLiteral.getTrue()));
			}
			if ((def.rw == RWType.rw) || (def.rw == RWType.w)) {
				HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(def.getName(row)));
				target = createRef(intPos, isArray, def, target, row);
				final HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target).setType(HDLAssignmentType.ASSGN).setRight(busData.addBits(range)));
			}
			bitPos -= size;
		}
		return label;
	}

	public static HDLSwitchCaseStatement createReadCase(Row row, int reg, Map<String, Integer> intPos, Map<String, Boolean> isArray, String dataPort, HDLLiteral labelValue) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(labelValue);
		final HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(dataPort));
		int bitPos = 31;
		for (final NamedElement ne : row.definitions) {
			final Definition def = (Definition) ne;
			final int size = MemoryModel.getSize(def);
			if ((def.rw == RWType.rw) || (def.rw == RWType.r)) {
				HDLVariableRef source = new HDLVariableRef().setVar(HDLQualifiedName.create(def.getName(row)));
				source = createRef(intPos, isArray, def, source, row);
				final HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target.addBits(range)).setType(HDLAssignmentType.ASSGN).setRight(source));
			}
			if (def.rw == RWType.constant) {
				final HDLRange range = getRange(bitPos, size);
				Constant c = (Constant) def;
				label = label.addDos(new HDLAssignment().setLeft(target.addBits(range)).setType(HDLAssignmentType.ASSGN).setRight(HDLLiteral.get(c.value)));
			}
			bitPos -= size;
		}
		return label;
	}

	public static HDLVariableRef createRef(Map<String, Integer> intPos, Map<String, Boolean> isArray, Definition def, HDLVariableRef source, Row row) {
		final String name = def.getName(row);
		if (isArray.get(name)) {
			Integer integer = intPos.get(name);
			if (integer == null) {
				integer = 0;
			}
			source = source.addArray(HDLLiteral.get(integer));
			intPos.put(name, ++integer);
		}
		return source;
	}

	public static HDLRange getRange(int bitPos, int size) {
		HDLRange range = new HDLRange().setTo(HDLLiteral.get(bitPos - (size - 1)));
		if (size != 1) {
			range = range.setFrom(HDLLiteral.get(bitPos));
		}
		return range;
	}

	public static Map<String, Boolean> buildArrayMap(HDLInterface hdi) {
		final Map<String, Boolean> isArray = Maps.newLinkedHashMap();
		for (final HDLVariable var : hdi.getAllObjectsOf(HDLVariable.class, true))
			if (var.getDimensions().size() != 0) {
				isArray.put(var.getName(), true);
			} else {
				isArray.put(var.getName(), false);
			}
		return isArray;
	}
}
