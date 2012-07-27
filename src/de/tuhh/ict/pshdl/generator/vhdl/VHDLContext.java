package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
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
	public Map<Integer, LinkedList<SequentialStatement>> unclockedStatements = new LinkedHashMap<Integer, LinkedList<SequentialStatement>>();
	public Map<Integer, LinkedList<HDLStatement>> sensitiveStatements = new LinkedHashMap<Integer, LinkedList<HDLStatement>>();
	public LinkedList<Signal> ports = new LinkedList<Signal>();
	public LinkedList<ConstantDeclaration> constants = new LinkedList<ConstantDeclaration>();
	public LinkedList<ConstantDeclaration> constantsPkg = new LinkedList<ConstantDeclaration>();
	public LinkedList<Constant> generics = new LinkedList<Constant>();
	public LinkedList<ObjectDeclaration<?>> internals = new LinkedList<ObjectDeclaration<?>>();
	public LinkedList<BlockDeclarativeItem> internalTypes = new LinkedList<BlockDeclarativeItem>();
	public LinkedList<BlockDeclarativeItem> externalTypes = new LinkedList<BlockDeclarativeItem>();
	public Set<HDLQualifiedName> imports = new HashSet<HDLQualifiedName>();

	public void addClockedStatement(HDLRegisterConfig config, SequentialStatement sa) {
		LinkedList<SequentialStatement> list = clockedStatements.get(config);
		if (list == null) {
			list = new LinkedList<SequentialStatement>();
		}
		list.add(sa);
		clockedStatements.put(config, list);
	}

	public void addUnclockedStatement(int pid, SequentialStatement sa, HDLStatement stmnt) {
		LinkedList<SequentialStatement> list = unclockedStatements.get(pid);
		if (list == null) {
			list = new LinkedList<SequentialStatement>();
		}
		list.add(sa);
		unclockedStatements.put(pid, list);
		LinkedList<HDLStatement> hlist = sensitiveStatements.get(pid);
		if (hlist == null) {
			hlist = new LinkedList<HDLStatement>();
		}
		hlist.add(stmnt);
		sensitiveStatements.put(pid, hlist);
	}

	public static int DEFAULT_CTX = -1;

	public void merge(VHDLContext vhdl) {
		concurrentStatements.addAll(vhdl.concurrentStatements);
		mergeListMap(vhdl, vhdl.sensitiveStatements, sensitiveStatements);
		mergeListMap(vhdl, vhdl.unclockedStatements, unclockedStatements);
		ports.addAll(vhdl.ports);
		generics.addAll(vhdl.generics);
		constants.addAll(vhdl.constants);
		constantsPkg.addAll(vhdl.constantsPkg);
		internals.addAll(vhdl.internals);
		internalTypes.addAll(vhdl.internalTypes);
		externalTypes.addAll(vhdl.externalTypes);
		imports.addAll(vhdl.imports);
		mergeListMap(vhdl, vhdl.clockedStatements, clockedStatements);
		mergeListMap(vhdl, vhdl.resetStatements, resetStatements);
	}

	private <K, T> void mergeListMap(VHDLContext vhdl, Map<K, LinkedList<T>> map, Map<K, LinkedList<T>> local) {
		for (Entry<K, LinkedList<T>> e : map.entrySet()) {
			LinkedList<T> list = local.get(e.getKey());
			if (list == null)
				list = new LinkedList<T>();
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
		for (Entry<Integer, LinkedList<SequentialStatement>> e : unclockedStatements.entrySet()) {
			printList(sb, e.getValue(), "For unclocked process " + e.getKey() + ":");
		}
		printList(sb, ports, "Entity ports:");
		printList(sb, generics, "Entity generics:");
		printList(sb, constants, "Entity constants:");
		printList(sb, constantsPkg, "Pkg constants:");
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
		if ((clockedStatements.size() > 1) || (unclockedStatements.size() > 1))
			throw new IllegalArgumentException("Did not expect to find more than one statement:" + this);
		for (LinkedList<SequentialStatement> clkd : clockedStatements.values()) {
			if (clkd.size() > 1) {
				throw new IllegalArgumentException("Did not expect to find more than one statement:" + this);
			}
			return clkd.getFirst();
		}
		for (LinkedList<SequentialStatement> clkd : unclockedStatements.values()) {
			if (clkd.size() > 1) {
				throw new IllegalArgumentException("Did not expect to find more than one statement:" + this);
			}
			return clkd.getFirst();
		}
		throw new NoSuchElementException("No Statement found");
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

	public boolean hasPkgDeclarations() {
		return (externalTypes.size() != 0) || (constantsPkg.size() != 0);
	}

	public void addConcurrentStatement(ConcurrentStatement stmnt) {
		concurrentStatements.add(stmnt);
	}

	public void addImport(HDLQualifiedName value) {
		imports.add(value.skipLast(1));
	}

	public void addConstantDeclarationPkg(ConstantDeclaration cd) {
		constantsPkg.add(cd);
	}

	private static AtomicInteger ai = new AtomicInteger();

	public int newProcessID() {
		return ai.incrementAndGet();
	}

}
