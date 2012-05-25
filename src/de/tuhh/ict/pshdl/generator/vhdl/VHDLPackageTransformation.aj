package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.*;

import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.statement.*;
import de.upb.hni.vmagic.statement.CaseStatement.*;
import de.upb.hni.vmagic.statement.IfStatement.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.concurrent.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.type.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.libraryunit.*;

public aspect VHDLPackageTransformation {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<LibraryUnit> HDLUnit.toVHDL() {
		List<LibraryUnit> res = new LinkedList<LibraryUnit>();
		addDefaultLibs(res);
		HDLQualifiedName entityName = new HDLQualifiedName(getName());
		Entity e = new Entity(entityName.getLastSegment());
		VHDLContext unit = new VHDLContext();
		for (HDLStatement stmnt : getStatements()) {
			unit.merge(stmnt.toVHDL());
		}
		if (unit.hasExternalTypes()){
			String libName=entityName.getLastSegment()+"Pkg";
			PackageDeclaration pd=new PackageDeclaration(libName);
			pd.getDeclarations().addAll((List)unit.externalTypes);
			res.add(pd);
			res.add(new UseClause("work."+libName+".all"));
			addDefaultLibs(res);
		}
		// System.out.println("VHDLPackageTransformation.HDLUnit.toVHDL()"+unit);
		e.getPort().addAll((List) unit.ports);
		e.getGeneric().addAll((List) unit.generics);
		e.getDeclarations().addAll((List) unit.constants);
		res.add(e);
		Architecture a = new Architecture("pshdlGenerated", e);
		e.getDeclarations().addAll((List) unit.internalTypes);
		a.getDeclarations().addAll((List) unit.internals);
		if (unit.unclockedStatements.size() > 0) {
			ProcessStatement ps = new ProcessStatement();
			ps.getSensitivityList().addAll(createSensitivyList(this));
			ps.getStatements().addAll(unit.unclockedStatements);
			a.getStatements().add(ps);
		}
		for (Map.Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> pc : unit.clockedStatements.entrySet()) {
			ProcessStatement ps = new ProcessStatement();
			ps.getStatements().add(createIfStatement(ps, pc.getKey(), pc.getValue(), unit));
			a.getStatements().add(ps);
		}
		res.add(a);
		return res;
	}

	private static void addDefaultLibs(List<LibraryUnit> res) {
		res.add(new LibraryClause("ieee"));
		res.add(StdLogic1164.USE_CLAUSE);
		res.add(NumericStd.USE_CLAUSE);
		res.add(new LibraryClause("pshdl"));
		res.add(VHDLCastsLibrary.USE_CLAUSE);
		res.add(VHDLShiftLibrary.USE_CLAUSE);
	}

	private static EnumSet<HDLDirection> notSensitive = EnumSet.of(HDLDirection.HIDDEN, HDLDirection.PARAMETER, HDLDirection.CONSTANT);

	private static Collection<? extends Signal> createSensitivyList(HDLUnit unit) {
		List<Signal> sensitivity = new LinkedList<Signal>();
		Set<String> vars = new TreeSet<String>();
		List<HDLVariableRef> refs = unit.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			HDLVariable var = ref.resolveVar();
			HDLObject container = var.getContainer();
			if (container instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hdv = (HDLVariableDeclaration) container;
				if (!notSensitive.contains(hdv.getDirection())) {
					HDLRegisterConfig config = var.getRegisterConfig();
					if (config == null) {
						if (ref.getContainer() instanceof HDLAssignment) {
							HDLAssignment hAss = (HDLAssignment) ref.getContainer();
							if (hAss.getLeft() != ref)
								vars.add(var.getName());
						} else
							vars.add(var.getName());
					}
				}
			}
		}
		for (String string : vars) {
			sensitivity.add(new Signal(string, UnresolvedType.NO_NAME));
		}
		return sensitivity;
	}

	private static SequentialStatement createIfStatement(ProcessStatement ps, HDLRegisterConfig key, LinkedList<SequentialStatement> value, VHDLContext unit) {
		Signal clk = (Signal) new HDLVariableRef().setVar(key.getClkRefName()).toVHDL();
		Signal rst = (Signal) new HDLVariableRef().setVar(key.getRstRefName()).toVHDL();
		ps.getSensitivityList().add((Signal) clk);
		EnumerationLiteral activeRst;
		if (key.getResetType() == HDLRegResetType.HIGH_ACTIVE)
			activeRst = StdLogic1164.STD_LOGIC_1;
		else
			activeRst = StdLogic1164.STD_LOGIC_0;
		IfStatement rstIfStmnt;
		rstIfStmnt = new IfStatement(new Equals(rst, activeRst));
		LinkedList<SequentialStatement> resets = unit.resetStatements.get(key);
		if (resets != null)
			rstIfStmnt.getStatements().addAll(resets);
		Expression<?> clkEdge;
		if (key.getClockType() == HDLRegClockType.RISING)
			clkEdge = Expressions.risingEdge(clk);
		else
			clkEdge = Expressions.fallingEdge(clk);
		if (key.getSyncType() == HDLRegSyncType.ASYNC) {
			ps.getSensitivityList().add(rst);
			ElsifPart elsifPart = rstIfStmnt.createElsifPart(clkEdge);
			elsifPart.getStatements().addAll(value);
			return rstIfStmnt;
		}
		IfStatement clkIf = new IfStatement(clkEdge);
		clkIf.getStatements().add(rstIfStmnt);
		rstIfStmnt.getElseStatements().addAll(value);
		return clkIf;
	}

	public VhdlFile HDLPackage.toVHDL() {
		VhdlFile res = new VhdlFile();
		for (HDLUnit unit : getUnits()) {
			res.getElements().addAll(unit.toVHDL());
		}
		return res;
	}

}
