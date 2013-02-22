package de.tuhh.ict.pshdl.model.types.builtIn.busses;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLAssignment.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.*;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class CommonBusCode {
	public static HDLSwitchCaseStatement createReadCase(Row row, int reg, Map<String, Integer> intPos, Map<String, Boolean> isArray, String dataPort, HDLLiteral labelValue) {
		HDLSwitchCaseStatement label = new HDLSwitchCaseStatement().setLabel(labelValue);
		HDLVariableRef target = new HDLVariableRef().setVar(HDLQualifiedName.create(dataPort));
		int bitPos = 31;
		for (NamedElement ne : row.definitions) {
			Definition def = (Definition) ne;
			int size = MemoryModel.getSize(def);
			if ((def.rw == RWType.rw) || (def.rw == RWType.r)) {
				HDLVariableRef source = new HDLVariableRef().setVar(HDLQualifiedName.create(def.name));
				source = createRef(intPos, isArray, def, source);
				HDLRange range = getRange(bitPos, size);
				label = label.addDos(new HDLAssignment().setLeft(target.addBits(range)).setType(HDLAssignmentType.ASSGN).setRight(source));
			}
			bitPos -= size;
		}
		return label;
	}

	public static HDLVariableRef createRef(Map<String, Integer> intPos, Map<String, Boolean> isArray, Definition def, HDLVariableRef source) {
		if (isArray.get(def.name)) {
			Integer integer = intPos.get(def.name);
			if (integer == null) {
				integer = 0;
			}
			source = source.addArray(HDLLiteral.get(integer));
			intPos.put(def.name, ++integer);
		}
		return source;
	}

	public static HDLRange getRange(int bitPos, int size) {
		HDLRange range = new HDLRange().setTo(HDLLiteral.get(bitPos - (size - 1)));
		if (size != 1)
			range = range.setFrom(HDLLiteral.get(bitPos));
		return range;
	}

	public static Map<String, Boolean> buildArrayMap(HDLInterface hdi) {
		Map<String, Boolean> isArray = new HashMap<String, Boolean>();
		for (HDLVariable var : hdi.getAllObjectsOf(HDLVariable.class, true)) {
			if (var.getDimensions().size() != 0)
				isArray.put(var.getName(), true);
			else
				isArray.put(var.getName(), false);
		}
		return isArray;
	}
}
