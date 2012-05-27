package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.concurrent.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.output.*;
import de.upb.hni.vmagic.statement.*;

public class VHDLContext {

	public Map<HDLRegisterConfig, LinkedList<SequentialStatement>> resetStatements = new LinkedHashMap<HDLRegisterConfig, LinkedList<SequentialStatement>>();
	public Map<HDLRegisterConfig, LinkedList<SequentialStatement>> clockedStatements = new LinkedHashMap<HDLRegisterConfig, LinkedList<SequentialStatement>>();
	public LinkedList<ConcurrentStatement> concurrentStatements = new LinkedList<ConcurrentStatement>();
	public LinkedList<SequentialStatement> unclockedStatements = new LinkedList<SequentialStatement>();
	public LinkedList<HDLStatement> sensitiveStatements = new LinkedList<HDLStatement>();
	public LinkedList<Signal> ports = new LinkedList<Signal>();
	public LinkedList<ConstantDeclaration> constants = new LinkedList<ConstantDeclaration>();
	public LinkedList<Constant> generics = new LinkedList<Constant>();
	public LinkedList<ObjectDeclaration<?>> internals = new LinkedList<ObjectDeclaration<?>>();
	public LinkedList<BlockDeclarativeItem> internalTypes = new LinkedList<BlockDeclarativeItem>();
	public LinkedList<BlockDeclarativeItem> externalTypes = new LinkedList<BlockDeclarativeItem>();

	public void addClockedStatement(HDLRegisterConfig config, SequentialStatement sa) {
		LinkedList<SequentialStatement> list = clockedStatements.get(config);
		if (list == null) {
			list = new LinkedList<SequentialStatement>();
		}
		list.add(sa);
		clockedStatements.put(config, list);
	}

	public void addUnclockedStatement(SequentialStatement sa, HDLStatement statement) {
		unclockedStatements.add(sa);
		sensitiveStatements.add(statement);
	}

	public void merge(VHDLContext vhdl) {
		concurrentStatements.addAll(vhdl.concurrentStatements);
		sensitiveStatements.addAll(vhdl.sensitiveStatements);
		unclockedStatements.addAll(vhdl.unclockedStatements);
		ports.addAll(vhdl.ports);
		generics.addAll(vhdl.generics);
		constants.addAll(vhdl.constants);
		internals.addAll(vhdl.internals);
		internalTypes.addAll(vhdl.internalTypes);
		externalTypes.addAll(vhdl.externalTypes);
		mergeListMap(vhdl, vhdl.clockedStatements, clockedStatements);
		mergeListMap(vhdl, vhdl.resetStatements, resetStatements);
	}

	private void mergeListMap(VHDLContext vhdl, Map<HDLRegisterConfig, LinkedList<SequentialStatement>> map, Map<HDLRegisterConfig, LinkedList<SequentialStatement>> local) {
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : map.entrySet()) {
			LinkedList<SequentialStatement> list = local.get(e.getKey());
			if (list == null)
				list = new LinkedList<SequentialStatement>();
			list.addAll(e.getValue());
			local.put(e.getKey(), list);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : clockedStatements.entrySet()) {
			printList(sb, e.getValue(), "For clock config " + e.getKey() + ":");
		}
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : resetStatements.entrySet()) {
			printList(sb, e.getValue(), "For clock config resets " + e.getKey() + ":");
		}
		printList(sb, concurrentStatements, "Concurrent Statements:");
		printList(sb, unclockedStatements, "Unclocked Statements:");
		printList(sb, ports, "Entity ports:");
		printList(sb, generics, "Entity generics:");
		printList(sb, constants, "Entity constants:");
		printList(sb, internals, "Internal signals:");
		// printList(sb, internalTypes, "Internal types:");
		return sb.toString();
	}

	private void printList(StringBuilder sb, LinkedList<?> list, String label) {
		if (list.size() > 0) {
			sb.append(label).append("\n");
			for (Object decl : list) {
				sb.append(VhdlOutput.toVhdlString((VhdlElement) decl)).append('\n');
			}
		}
	}

	public void addPortDeclaration(Signal sd) {
		ports.add(sd);
	}

	public void addInternalSignalDeclaration(ObjectDeclaration<?> sd) {
		internals.add(sd);
	}

	public void addGenericDeclaration(Constant sd) {
		generics.add(sd);
	}

	public void addInternalTypeDeclaration(BlockDeclarativeItem type) {
		internalTypes.add(type);
	}

	public void addResetValue(HDLRegisterConfig config, SequentialStatement sa) {
		LinkedList<SequentialStatement> list = resetStatements.get(config);
		if (list == null) {
			list = new LinkedList<SequentialStatement>();
		}
		list.add(sa);
		resetStatements.put(config, list);
	}

	public SequentialStatement getStatement() {
		for (LinkedList<SequentialStatement> clkd : clockedStatements.values()) {
			if (clkd.size() > 1) {
				throw new IllegalArgumentException("Did not expect to find more than one statement:" + this);
			}
			return clkd.getFirst();
		}
		return unclockedStatements.getFirst();
	}

	public void addConstantDeclaration(ConstantDeclaration cd) {
		constants.add(cd);
	}

	public void addTypeDeclaration(BlockDeclarativeItem type, boolean isExternal) {
		if (isExternal)
			externalTypes.add(type);
		else
			internalTypes.add(type);
	}

	public boolean hasExternalTypes() {
		return (externalTypes.size() != 0);
	}

	public void addConcurrentStatement(ConcurrentStatement stmnt) {
		concurrentStatements.add(stmnt);
	}

}
