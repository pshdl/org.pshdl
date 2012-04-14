package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.upb.hni.vmagic.output.*;
import de.upb.hni.vmagic.statement.*;

public class VHDLContext {

	public Map<HDLRegisterConfig, LinkedList<SequentialStatement>> clockedStatements = new LinkedHashMap<HDLRegisterConfig, LinkedList<SequentialStatement>>();
	public LinkedList<SequentialStatement> unclockedStatements = new LinkedList<SequentialStatement>();

	public void addClockedStatement(HDLRegisterConfig config, SequentialStatement sa) {
		LinkedList<SequentialStatement> list = clockedStatements.get(config);
		if (list == null) {
			list = new LinkedList<SequentialStatement>();
		}
		list.add(sa);
		clockedStatements.put(config, list);
	}

	public void addUnclockedStatement(SequentialStatement sa) {
		unclockedStatements.add(sa);
	}

	public void merge(VHDLContext vhdl) {
		unclockedStatements.addAll(vhdl.unclockedStatements);
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : vhdl.clockedStatements.entrySet()) {
			LinkedList<SequentialStatement> list = clockedStatements.get(e.getKey());
			if (list == null)
				list = new LinkedList<SequentialStatement>();
			list.addAll(e.getValue());
			clockedStatements.put(e.getKey(), list);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : clockedStatements.entrySet()) {
			sb.append("For clock config ").append(e.getKey()).append(":\n");
			for (SequentialStatement ss : e.getValue()) {
				sb.append(VhdlOutput.toVhdlString(ss)).append("\n");
			}
		}
		if (unclockedStatements.size() > 0) {
			sb.append("For unclock config:\n");
			for (SequentialStatement ss : unclockedStatements) {
				sb.append(VhdlOutput.toVhdlString(ss)).append("\n");
			}
		}
		return sb.toString();
	}

}
