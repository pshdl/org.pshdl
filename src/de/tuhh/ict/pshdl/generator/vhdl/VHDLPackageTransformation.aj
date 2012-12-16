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
		HDLQualifiedName entityName = getFullName();
		Entity e = new Entity(entityName.toString('_'));
		VHDLContext unit = new VHDLContext();
		
		Collection<HDLEnumRef> hRefs=getAllObjectsOf(HDLEnumRef.class, true);
		for (HDLEnumRef hdlEnumRef : hRefs) {
			HDLEnum resolveHEnum = hdlEnumRef.resolveHEnum();
			HDLUnit enumContainer=resolveHEnum.getContainer(HDLUnit.class);
			if (enumContainer==null || !enumContainer.equals(hdlEnumRef.getContainer(HDLUnit.class))){
				HDLQualifiedName type=resolveHEnum.getFullName();
				if (!type.getSegment(0).equals("pshdl"))
					unit.addImport(HDLQualifiedName.create("work",getPackageName(type), "all"));
			}
		}

		for (HDLStatement stmnt : getStatements()) {
			unit.merge(stmnt.toVHDL(VHDLContext.DEFAULT_CTX), false);
		}
		addDefaultLibs(res, unit);
		if (unit.hasPkgDeclarations()) {
			String libName = getPackageName(entityName);
			PackageDeclaration pd = new PackageDeclaration(libName);
			pd.getDeclarations().addAll((List) unit.externalTypes);
			pd.getDeclarations().addAll((List) unit.constantsPkg);
			res.add(pd);
			res.add(new UseClause("work." + libName + ".all"));
			addDefaultLibs(res, unit);
		}
		e.getPort().addAll((List) unit.ports);
		e.getGeneric().addAll((List) unit.generics);
		e.getDeclarations().addAll((List) unit.constants);
		res.add(e);
		Architecture a = new Architecture("pshdlGenerated", e);
		e.getDeclarations().addAll((List) unit.internalTypes);
		a.getDeclarations().addAll((List) unit.internals);
		a.getStatements().addAll(unit.concurrentStatements);
		for (Map.Entry<Integer,LinkedList<SequentialStatement>> uc: unit.unclockedStatements.entrySet()){
			ProcessStatement ps = new ProcessStatement();
			ps.getSensitivityList().addAll(createSensitivyList(unit, uc.getKey()));
			ps.getStatements().addAll(uc.getValue());
			a.getStatements().add(ps);
		}
		for (Map.Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> pc : unit.clockedStatements.entrySet()) {
			ProcessStatement ps = new ProcessStatement();
			ps.getStatements().add(createIfStatement(this, ps, pc.getKey(), pc.getValue(), unit));
			a.getStatements().add(ps);
		}
		res.add(a);
		return res;
	}

	public static String getPackageName(HDLQualifiedName entityName) {
		return entityName.toString('_') + "Pkg";
	}
	public static HDLQualifiedName getPackageNameRef(HDLQualifiedName entityName) {
		if (entityName.getSegment(0).equals("VHDL"))
			return entityName.skipFirst(1);
		return HDLQualifiedName.create("work",entityName.toString('_')+"Pkg");
	}
	public static HDLQualifiedName getNameRef(HDLQualifiedName entityName) {
		if (entityName.getSegment(0).equals("VHDL"))
			return entityName.skipFirst(1);
		return HDLQualifiedName.create("work",entityName.toString('_'));
	}

	private static void addDefaultLibs(List<LibraryUnit> res, VHDLContext unit) {
		res.add(new LibraryClause("ieee"));
		res.add(StdLogic1164.USE_CLAUSE);
		res.add(NumericStd.USE_CLAUSE);
		res.add(new LibraryClause("pshdl"));
		res.add(VHDLCastsLibrary.USE_CLAUSE);
		res.add(VHDLShiftLibrary.USE_CLAUSE);
		res.add(new UseClause("pshdl.types.all"));
		Set<String> usedLibs=new HashSet<String>();
		usedLibs.add("pshdl");
		usedLibs.add("ieee");
		usedLibs.add("work");
		for (HDLQualifiedName i : unit.imports) {
			String lib=i.getSegment(0);
			if (!usedLibs.contains(lib)) {
				res.add(new LibraryClause(lib));
				usedLibs.add(lib);
			}
			res.add(new UseClause(i.append("all").toString()));
		}
	}

	private static EnumSet<HDLDirection> notSensitive = EnumSet.of(HDLDirection.HIDDEN, HDLDirection.PARAMETER, HDLDirection.CONSTANT);

	private static Collection<? extends Signal> createSensitivyList(VHDLContext ctx, int pid) {
		if (ctx.noSensitivity.containsKey(pid))
			return Collections.emptyList();
		List<Signal> sensitivity = new LinkedList<Signal>();
		Set<String> vars = new TreeSet<String>();
		for (HDLStatement stmnt : ctx.sensitiveStatements.get(pid)) {
			Collection<HDLVariableRef> refs = stmnt.getAllObjectsOf(HDLVariableRef.class, true);
			for (HDLVariableRef ref : refs) {
				HDLVariable var = ref.resolveVar();
				IHDLObject container = var.getContainer();
				if (container instanceof HDLVariableDeclaration) {
					HDLVariableDeclaration hdv = (HDLVariableDeclaration) container;
					if (!notSensitive.contains(hdv.getDirection())) {
						if (ref.getContainer() instanceof HDLAssignment) {
							HDLAssignment hAss = (HDLAssignment) ref.getContainer();
							if (hAss.getLeft().resolveVar().getRegisterConfig() != null)
								continue;
							if (hAss.getLeft() != ref)
								vars.add(ref.getVHDLName());
						} else
							vars.add(ref.getVHDLName());
					}
				}
			}
		}

		for (String string : vars) {
			sensitivity.add(new Signal(string, UnresolvedType.NO_NAME));
		}
		return sensitivity;
	}

	private static SequentialStatement createIfStatement(HDLUnit hUnit, ProcessStatement ps, HDLRegisterConfig key, LinkedList<SequentialStatement> value, VHDLContext unit) {
		Signal clk = (Signal) new HDLVariableRef().setVar(key.getClkRefName()).toVHDL();
		Signal rst = (Signal) new HDLVariableRef().setVar(key.getRstRefName()).toVHDL();
		HDLConfig config = HDLLibrary.getLibrary(hUnit.getLibURI()).getConfig();
		ps.getSensitivityList().add((Signal) clk);
		EnumerationLiteral activeRst;
		if (config.getRegResetType(hUnit.getFullName(), key.getResetType()) == HDLRegResetActiveType.HIGH)
			activeRst = StdLogic1164.STD_LOGIC_1;
		else
			activeRst = StdLogic1164.STD_LOGIC_0;
		IfStatement rstIfStmnt;
		rstIfStmnt = new IfStatement(new Equals(rst, activeRst));
		LinkedList<SequentialStatement> resets = unit.resetStatements.get(key);
		if (resets != null)
			rstIfStmnt.getStatements().addAll(resets);
		Expression<?> clkEdge;
		if (config.getRegClockType(hUnit.getFullName(), key.getClockType()) == HDLRegClockType.RISING)
			clkEdge = Expressions.risingEdge(clk);
		else
			clkEdge = Expressions.fallingEdge(clk);
		if (config.getRegSyncType(hUnit.getFullName(), key.getSyncType()) == HDLRegSyncType.ASYNC) {
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
			ModificationSet ms=new ModificationSet();
			Collection<HDLVariableDeclaration> hvds = unit.getAllObjectsOf(HDLVariableDeclaration.class, true);
			for (HDLVariableDeclaration hvd : hvds) {
				for (HDLVariable var : hvd.getVariables()) {
					Collection<HDLVariableRef> refs = var.getAllObjectsOf(HDLVariableRef.class, true);
					for (HDLVariableRef ref : refs) {
						//Check which variable declaration contains references and mark those references as the ones that should be declared in a package
						ref.resolveVar().setMeta(VHDLStatementTransformation.EXPORT);
					}
					String origName = var.getName();
					String name=VHDLOutputValidator.getVHDLName(origName);
					if (!origName.equals(name)){
						HDLVariable newVar=var.setName(name);
						ms.replace(var, newVar);
						Collection<HDLVariableRef> varRefs=HDLQuery.select(HDLVariableRef.class).from(this).where(HDLVariableRef.fVar).isEqualTo(var.asRef()).getAll();
						HDLQualifiedName newVarRef=newVar.asRef();
						for (HDLVariableRef ref : varRefs) {
							ms.replace(ref, ref.setVar(newVarRef));
						}
					}
				}
			}
			HDLUnit newUnit=ms.apply(unit);
			res.getElements().addAll(newUnit.toVHDL());
		}
		PackageDeclaration pd=null;
		for (HDLDeclaration decl:getDeclarations()){
			if (decl.getClassType()==HDLClass.HDLVariableDeclaration){
				HDLVariableDeclaration hvd=(HDLVariableDeclaration) decl;
				if (pd==null){
					pd=new PackageDeclaration(getPackageName(new HDLQualifiedName(getPkg())));
					res.getElements().add(pd);
				}
				VHDLContext vhdl = hvd.toVHDL(VHDLContext.DEFAULT_CTX);
				ConstantDeclaration first = vhdl.constants.getFirst();
				if (first==null) {
					first=vhdl.constantsPkg.getFirst();
					if (first==null)
						throw new IllegalArgumentException("Expected constant declaration but found none!");
				}
				pd.getDeclarations().add(first);
			}
			if (decl.getClassType()==HDLClass.HDLEnumDeclaration){
				HDLEnumDeclaration hvd=(HDLEnumDeclaration) decl;
				PackageDeclaration	enumPd=new PackageDeclaration(getPackageName(hvd.getHEnum().getFullName()));
				res.getElements().add(enumPd);
				VHDLContext vhdl = hvd.toVHDL(VHDLContext.DEFAULT_CTX);
				Type first = (Type)vhdl.internalTypes.getFirst();
				if (first==null)
					throw new IllegalArgumentException("Expected enum type declaration but found none!");
				enumPd.getDeclarations().add(first);
			}
		}
		return res;
	}

}
