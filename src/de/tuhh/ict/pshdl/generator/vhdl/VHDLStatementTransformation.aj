package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.*;

import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLManip.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.statement.*;
import de.upb.hni.vmagic.statement.CaseStatement.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.object.VhdlObject.*;
import de.upb.hni.vmagic.type.*;

public aspect VHDLStatementTransformation {
	public abstract VHDLContext HDLStatement.toVHDL();

	public VHDLContext HDLDirectGeneration.toVHDL() {
		return new VHDLContext();
	}

	public VHDLContext HDLEnumDeclaration.toVHDL() {
		VHDLContext res = new VHDLContext();
		HDLEnum hEnum = getHEnum();
		List<String> enums = new LinkedList<String>();
		for (HDLVariable var : hEnum.getEnums()) {
			enums.add(var.getName());
		}
		res.addInternalTypeDeclaration(new EnumerationType(hEnum.getName(), enums.toArray(new String[0])));
		return res;
	}

	public VHDLContext HDLInterfaceDeclaration.toVHDL() {
		return new VHDLContext();
	}

	public VHDLContext HDLInterfaceInstantiation.toVHDL() {
		return new VHDLContext();
	}

	public VHDLContext HDLVariableDeclaration.toVHDL() {
		VHDLContext res = new VHDLContext();
		HDLPrimitive primitive = getPrimitive();
		SubtypeIndication type = null;
		HDLExpression resetValue = null;
		if (primitive != null) {
			type = VHDLCastsLibrary.getType(primitive);
			if (getRegister() != null) {
				resetValue = getRegister().getResetValue();
			}
		} else {
			HDLType hType = resolveType();
			if (hType instanceof HDLEnum) {
				HDLEnum hEnum = (HDLEnum) hType;
				type = new EnumerationType(hEnum.getName());
				resetValue = new HDLVariableRef().setVar(hEnum.getEnums().get(0).asRef());
			}
		}
		if (type != null) {
			for (HDLVariable var : getVariables()) {
				if (resetValue != null) {
					HDLStatement initLoop = Insulin.createArrayForLoop(var.getDimensions(), 0, resetValue, new HDLVariableRef().setVar(var.asRef()));
					initLoop.setContainer(this);
					VHDLContext vhdl = initLoop.toVHDL();
					System.out.println("VHDLStatementTransformation.HDLVariableDeclaration.toVHDL()" + vhdl);
					res.addResetValue(getRegister(), vhdl.getStatement());
				}
				Signal s = new Signal(var.getName(), type);
				switch (getDirection()) {
				case IN:
					s.setMode(Mode.IN);
					res.addPortDeclaration(s);
					break;
				case OUT:
					s.setMode(Mode.OUT);
					res.addPortDeclaration(s);
					break;
				case INOUT:
					s.setMode(Mode.INOUT);
					res.addPortDeclaration(s);
					break;
				case INTERNAL:
					SignalDeclaration sd = new SignalDeclaration(s);
					res.addInternalDeclaration(sd);
					break;
				case HIDDEN:
					break;
				case CONSTANT:
					ConstantDeclaration cd = new ConstantDeclaration(new Constant(var.getName(), type));
					res.addInternalDeclaration(cd);
					break;
				case PARAMETER:
					res.addGenericDeclaration(new Constant(var.getName(), type));
					break;
				}
			}
		}
		return res;
	}

	public VHDLContext HDLSwitchStatement.toVHDL() {
		VHDLContext context = new VHDLContext();
		Expression<?> caseExp = getCaseExp().toVHDL();
		Map<HDLSwitchCaseStatement, VHDLContext> ctxs = new LinkedHashMap<HDLSwitchCaseStatement, VHDLContext>();
		Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>();
		boolean hasUnclocked = false;
		for (HDLSwitchCaseStatement cs : getCases()) {
			VHDLContext vhdl = cs.toVHDL();
			ctxs.put(cs, vhdl);
			if (vhdl.unclockedStatements.size() > 0)
				hasUnclocked = true;
			configs.addAll(vhdl.clockedStatements.keySet());
		}
		for (HDLRegisterConfig hdlRegisterConfig : configs) {
			CaseStatement cs = new CaseStatement(caseExp);
			for (Map.Entry<HDLSwitchCaseStatement, VHDLContext> e : ctxs.entrySet()) {
				Alternative alt = createAlternative(cs, e);
				LinkedList<SequentialStatement> clockCase = e.getValue().clockedStatements.get(hdlRegisterConfig);
				if (clockCase != null) {
					alt.getStatements().addAll(clockCase);
				}
			}
			context.addClockedStatement(hdlRegisterConfig, cs);
		}
		if (hasUnclocked) {
			CaseStatement cs = new CaseStatement(caseExp);
			for (Map.Entry<HDLSwitchCaseStatement, VHDLContext> e : ctxs.entrySet()) {
				Alternative alt = createAlternative(cs, e);
				alt.getStatements().addAll(e.getValue().unclockedStatements);
			}
			context.addUnclockedStatement(cs);
		}
		return context;
	}

	private static Alternative createAlternative(CaseStatement cs, Map.Entry<HDLSwitchCaseStatement, VHDLContext> e) {
		Alternative alt;
		if (e.getKey().getLabel() != null)
			alt = cs.createAlternative(e.getKey().getLabel().toVHDL());
		else
			alt = cs.createAlternative(Choices.OTHERS);
		return alt;
	}

	public VHDLContext HDLSwitchCaseStatement.toVHDL() {
		VHDLContext res = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			res.merge(stmnt.toVHDL());
		}
		return res;
	}

	public VHDLContext HDLAssignment.toVHDL() {
		VHDLContext context = new VHDLContext();
		SignalAssignment sa = new SignalAssignment((SignalAssignmentTarget) getLeft().toVHDL(), getRight().toVHDL());
		HDLReference ref = getLeft();
		HDLRegisterConfig config = ref.resolveVar().getRegisterConfig();
		if (config != null)
			context.addClockedStatement(config, sa);
		else
			context.addUnclockedStatement(sa);
		return context;
	}

	public VHDLContext HDLForLoop.toVHDL() {
		VHDLContext context = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			context.merge(stmnt.toVHDL());
		}
		VHDLContext res = new VHDLContext();
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : context.clockedStatements.entrySet()) {
			ForStatement fStmnt = new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(e.getValue());
			res.addClockedStatement(e.getKey(), fStmnt);
		}
		if (context.unclockedStatements.size() > 0) {
			ForStatement fStmnt = new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(context.unclockedStatements);
			res.addUnclockedStatement(fStmnt);
		}
		return res;
	}

	public VHDLContext HDLIfStatement.toVHDL() {
		VHDLContext thenCtx = new VHDLContext();
		for (HDLStatement stmnt : getThenDo()) {
			thenCtx.merge(stmnt.toVHDL());
		}
		VHDLContext elseCtx = new VHDLContext();
		for (HDLStatement stmnt : getElseDo()) {
			elseCtx.merge(stmnt.toVHDL());
		}
		Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>();
		configs.addAll(thenCtx.clockedStatements.keySet());
		configs.addAll(elseCtx.clockedStatements.keySet());
		VHDLContext res = new VHDLContext();
		Expression<?> ifExp = getIfExp().toVHDL();
		for (HDLRegisterConfig config : configs) {
			IfStatement ifs = new IfStatement(ifExp);
			if (thenCtx.clockedStatements.get(config) != null)
				ifs.getStatements().addAll(thenCtx.clockedStatements.get(config));
			if (elseCtx.clockedStatements.get(config) != null)
				ifs.getElseStatements().addAll(elseCtx.clockedStatements.get(config));
			res.addClockedStatement(config, ifs);
		}
		if (thenCtx.unclockedStatements.size() != 0 || elseCtx.unclockedStatements.size() != 0) {
			IfStatement ifs = new IfStatement(ifExp);
			ifs.getStatements().addAll(thenCtx.unclockedStatements);
			ifs.getElseStatements().addAll(elseCtx.unclockedStatements);
			res.addUnclockedStatement(ifs);
		}
		return res;
	}
}
