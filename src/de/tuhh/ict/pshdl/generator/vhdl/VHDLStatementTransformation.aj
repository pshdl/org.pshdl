package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;
import java.util.Map.*;

import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.*;
import de.tuhh.ict.pshdl.model.HDLManip.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.concurrent.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.statement.*;
import de.upb.hni.vmagic.statement.CaseStatement.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
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

	private static EnumSet<HDLDirection> inAndOut = EnumSet.of(HDLDirection.IN, HDLDirection.INOUT, HDLDirection.OUT);

	public VHDLContext HDLInterfaceInstantiation.toVHDL() {
		VHDLContext res = new VHDLContext();
		HDLInterface hIf = resolveHIf();
		String ifName = getVar().getName();
		ArrayList<HDLVariableDeclaration> ports = hIf.getPorts();
		Entity entity = new Entity("work." + hIf.asRef().getLastSegment());

		EntityInstantiation instantiation = new EntityInstantiation(ifName, entity);
		List<AssociationElement> portMap = instantiation.getPortMap();
		for (HDLVariableDeclaration hvd : ports) {
			if (inAndOut.contains(hvd.getDirection())) {
				for (HDLVariable var : hvd.getVariables()) {
					HDLVariable sigVar = var.setName(ifName + "_" + var.getName());
					HDLVariableRef ref = sigVar.asHDLRef();
					for (int i = 0; i < getDimensions().size(); i++) {
						ref = ref.addArray(new HDLVariableRef().setVar(HDLQualifiedName.create(Character.toString((char) (i + 'I')))));
					}
					for (HDLExpression exp : getDimensions()) {
						sigVar = sigVar.addDimensions(exp.copy());
					}
					HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar));
					res.merge(newHVD.toVHDL());
					portMap.add(new AssociationElement(var.getName(), ref.toVHDL()));
				}
			}
		}
		ForGenerateStatement forLoop = null;
		if (getDimensions().size() == 0)
			res.addConcurrentStatement(instantiation);
		else {
			for (int i = 0; i < getDimensions().size(); i++) {
				HDLExpression to = new HDLArithOp().setLeft(getDimensions().get(i)).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
				HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(to);
				range.setContainer(this);
				ForGenerateStatement newFor = new ForGenerateStatement("generate_" + ifName, Character.toString((char) (i + 'I')), range.toVHDL(Range.Direction.TO));
				if (forLoop != null)
					forLoop.getStatements().add(newFor);
				else
					res.addConcurrentStatement(newFor);
				forLoop = newFor;
			}
			forLoop.getStatements().add(instantiation);
		}
		return res;
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
				resetValue = new HDLEnumRef().setHEnum(hEnum.asRef()).setVar(hEnum.getEnums().get(0).asRef());
				resetValue.setContainer(this);
			}
		}
		if (type != null) {
			for (HDLVariable var : getVariables()) {
				SubtypeIndication varType = type;
				if (var.getDimensions().size() != 0) {
					@SuppressWarnings("rawtypes")
					List<DiscreteRange> ranges = new LinkedList<DiscreteRange>();
					for (HDLExpression arrayWidth : var.getDimensions()) {
						HDLExpression newWidth = new HDLArithOp().setLeft(arrayWidth).setType(HDLArithOp.HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
						Range range = new Range(new DecimalLiteral(0), Direction.TO, newWidth.toVHDL());
						ranges.add(range);
					}
					ConstrainedArray arrType = new ConstrainedArray(var.getName() + "_array", type, ranges);
					res.addTypeDeclaration(arrType, isExternal());
					varType = arrType;
				}
				if (resetValue != null) {
					boolean synchedArray = false;
					if (resetValue instanceof HDLVariableRef) {
						HDLVariableRef ref = (HDLVariableRef) resetValue;
						synchedArray = ref.resolveVar().getDimensions().size() != 0;
					}
					HDLStatement initLoop = Insulin.createArrayForLoop(var.getDimensions(), 0, resetValue, new HDLVariableRef().setVar(var.asRef()), synchedArray);
					initLoop.setContainer(this);
					VHDLContext vhdl = initLoop.toVHDL();
					System.out.println("VHDLStatementTransformation.HDLVariableDeclaration.toVHDL()" + vhdl);
					res.addResetValue(getRegister(), vhdl.getStatement());
				}
				Signal s = new Signal(var.getName(), varType);
				Constant constant = new Constant(var.getName(), varType);
				if (var.getDefaultValue() != null)
					constant.setDefaultValue(var.getDefaultValue().toVHDL());
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
					res.addInternalSignalDeclaration(sd);
					break;
				case HIDDEN:
					break;
				case CONSTANT:
					ConstantDeclaration cd = new ConstantDeclaration(constant);
					res.addConstantDeclaration(cd);
					break;
				case PARAMETER:
					res.addGenericDeclaration(constant);
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
			context.addUnclockedStatement(cs, this);
		}
		return context;
	}

	private static Alternative createAlternative(CaseStatement cs, Map.Entry<HDLSwitchCaseStatement, VHDLContext> e) {
		Alternative alt;
		HDLExpression label = e.getKey().getLabel();
		if (label != null) {
			BigInteger eval = label.constantEvaluate(null);
			if (eval != null)
				alt = cs.createAlternative(new HDLLiteral().setVal("0b" + eval.toString(2)).toVHDL());
			else
				alt = cs.createAlternative(label.toVHDL());
		} else {
			alt = cs.createAlternative(Choices.OTHERS);
		}
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
			context.addUnclockedStatement(sa, this);
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
			res.addUnclockedStatement(fStmnt, this);
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
			res.addUnclockedStatement(ifs, this);
		}
		return res;
	}
}
