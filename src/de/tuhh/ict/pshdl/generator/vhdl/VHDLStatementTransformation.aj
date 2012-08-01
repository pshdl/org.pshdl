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
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.*;
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
	
	public enum Exportable implements MetaAccess<Boolean> {
		EXPORT
	}
	public abstract VHDLContext HDLStatement.toVHDL(int pid);

	public VHDLContext HDLDirectGeneration.toVHDL(int pid) {
		return new VHDLContext();
	}

	public VHDLContext HDLBlock.toVHDL(int pid){
		VHDLContext res = new VHDLContext();
		boolean process=false;
		if (getProcess()!=null && getProcess()){
			process=true;
		}
		if (process)
			pid=res.newProcessID();
		for(HDLStatement stmnt:getStatements()){
			res.merge(stmnt.toVHDL(pid));
		}
		return res;
	}
	
	public VHDLContext HDLEnumDeclaration.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		HDLEnum hEnum = getHEnum();
		List<String> enums = new LinkedList<String>();
		for (HDLVariable var : hEnum.getEnums()) {
			enums.add(var.getName());
		}
		res.addInternalTypeDeclaration(new EnumerationType(hEnum.getName(), enums.toArray(new String[0])));
		return res;
	}

	public VHDLContext HDLInterfaceDeclaration.toVHDL(int pid) {
		return new VHDLContext();
	}

	private static EnumSet<HDLDirection> inAndOut = EnumSet.of(HDLDirection.IN, HDLDirection.INOUT, HDLDirection.OUT);

	public VHDLContext HDLInterfaceInstantiation.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		HDLInterface hIf = resolveHIf();
		HDLVariable hVar=getVar();
		String ifName = getVar().getName();
		HDLQualifiedName asRef = hIf.asRef();
		Entity entity = new Entity("work." + asRef.getLastSegment());

		EntityInstantiation instantiation = new EntityInstantiation(ifName, entity);
		List<AssociationElement> portMap = instantiation.getPortMap();
		List<AssociationElement> genericMap = instantiation.getGenericMap();
		ModificationSet ms=new ModificationSet();
		Collection<HDLVariableRef> refs=hIf.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (ref.resolveVar().getDirection()==HDLDirection.PARAMETER)
				ms.replace(ref, ref.setVar(new HDLQualifiedName(hVar.getName()+"_"+ ref.getVarRefName().getLastSegment())));
		}
		hIf=ms.apply(hIf).copyFiltered(CopyFilter.DEEP).setContainer(getContainer());
		ArrayList<HDLVariableDeclaration> ports = hIf.getPorts();
		for (HDLVariableDeclaration hvd : ports) {
			if (inAndOut.contains(hvd.getDirection())) {
				Collection<HDLAnnotation> typeAnno = HDLQuery.select(HDLAnnotation.class).from(hvd).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.VHDLType.toString()).getAll();
				for (HDLVariable var : hvd.getVariables()) {
					HDLVariable sigVar = var.setName(ifName + "_" + var.getName());
					HDLVariableRef ref = sigVar.asHDLRef();
					for (int i = 0; i < hVar.getDimensions().size(); i++) {
						ref = ref.addArray(new HDLVariableRef().setVar(HDLQualifiedName.create(Character.toString((char) (i + 'I')))));
					}
					for (HDLExpression exp : hVar.getDimensions()) {
						sigVar = sigVar.addDimensions(exp.copy());
					}
					if (var.getDimensions().size() != 0) {
						if (typeAnno.isEmpty()) {
							HDLQualifiedName name = HDLQualifiedName.create("work").append(asRef.getLastSegment() + "Pkg").append(var.getName()+"_array");
							res.addImport(name);
							HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar.setDimensions(null).addAnnotations(HDLBuiltInAnnotations.VHDLType.create(name.toString()))));
							res.merge(newHVD.toVHDL(pid));
						} else {
							HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar.setDimensions(null)));
							res.merge(newHVD.toVHDL(pid));
						}
					} else {
						HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar));
						res.merge(newHVD.toVHDL(pid));
					}
					portMap.add(new AssociationElement(var.getName(), ref.toVHDL()));
				}
			} else if (hvd.getDirection()==HDLDirection.PARAMETER){
				for (HDLVariable var : hvd.getVariables()) {
					HDLVariable sigVar = var.setName(ifName + "_" + var.getName());
					HDLVariableRef ref = sigVar.asHDLRef();
					genericMap.add(new AssociationElement(var.getName(), ref.toVHDL()));
				}
			}
		}
		ForGenerateStatement forLoop = null;
		if (hVar.getDimensions().size() == 0)
			res.addConcurrentStatement(instantiation);
		else {
			for (int i = 0; i < hVar.getDimensions().size(); i++) {
				HDLExpression to = new HDLArithOp().setLeft(hVar.getDimensions().get(i).copy()).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
				HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(to).setContainer(this);
				ForGenerateStatement newFor = new ForGenerateStatement("generate_" + ifName, Character.toString((char) (i + 'I')), range.toVHDL(Range.Direction.TO));
				if (forLoop != null)
					forLoop.getStatements().add(newFor);
				else
					res.addConcurrentStatement(newFor);
				forLoop = newFor;
			}
			if (forLoop==null)
				throw new IllegalArgumentException("Should not get here");
			forLoop.getStatements().add(instantiation);
		}
		return res;
	}

	public VHDLContext HDLVariableDeclaration.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		HDLPrimitive primitive = getPrimitive();
		SubtypeIndication type = null;
		HDLExpression resetValue = null;
		HDLAnnotation typeAnno = HDLQuery.select(HDLAnnotation.class).from(this).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.VHDLType.toString()).getFirst();
		if (typeAnno!=null) {
			HDLQualifiedName value = new HDLQualifiedName(typeAnno.getValue());
			res.addImport(value);
			type = new EnumerationType(value.getLastSegment());
		} else {
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
		}
		if (type != null) {
			for (HDLVariable var : getVariables()) {
				boolean noExplicitResetVar=var.getAnnotation(HDLBuiltInAnnotations.VHDLNoExplicitReset)!=null;
				SubtypeIndication varType = type;
				if (var.getDimensions().size() != 0) {
					@SuppressWarnings("rawtypes")
					List<DiscreteRange> ranges = new LinkedList<DiscreteRange>();
					for (HDLExpression arrayWidth : var.getDimensions()) {
						HDLExpression newWidth = new HDLArithOp().setLeft(arrayWidth.copy()).setType(HDLArithOp.HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
						Range range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(newWidth).setContainer(this).toVHDL(Direction.TO);
						ranges.add(range);
					}
					ConstrainedArray arrType = new ConstrainedArray(var.getName() + "_array", type, ranges);
					res.addTypeDeclaration(arrType, isExternal());
					varType = arrType;
				}
				if (resetValue != null && !noExplicitResetVar) {
					boolean synchedArray = false;
					if (resetValue instanceof HDLVariableRef) {
						HDLVariableRef ref = (HDLVariableRef) resetValue;
						synchedArray = ref.resolveVar().getDimensions().size() != 0;
					}
					HDLStatement initLoop = Insulin.createArrayForLoop(var.getDimensions(), 0, resetValue, new HDLVariableRef().setVar(var.asRef()), synchedArray);
					initLoop.setContainer(this);
					VHDLContext vhdl = initLoop.toVHDL(pid);
					res.addResetValue(getRegister(), vhdl.getStatement());
				}
				Signal s = new Signal(var.getName(), varType);
				Constant constant = new Constant(var.getName(), varType);
				if (var.getDefaultValue() != null)
					constant.setDefaultValue(var.getDefaultValue().toVHDL());
				if (noExplicitResetVar){
					Aggregate assign=Aggregate.OTHERS(new CharacterLiteral('0'));
					for (int i=0;i<var.getDimensions().size();i++)
						assign=Aggregate.OTHERS(assign);
					s.setDefaultValue(assign);
				}
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
					if (var.hasMeta(Exportable.EXPORT))
						res.addConstantDeclarationPkg(cd);
					else
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

	public VHDLContext HDLSwitchStatement.toVHDL(int pid) {
		VHDLContext context = new VHDLContext();
		HDLExpression hCaseExp=getCaseExp();
		BigInteger width=null;
		HDLType type=hCaseExp.determineType();
		if (type instanceof HDLPrimitive){
			width=((HDLPrimitive)type).getWidth().constantEvaluate(null);
			if (width==null)
				throw new IllegalArgumentException("HDLPrimitive switch case needs to have constant width");
		}
		Expression<?> caseExp = hCaseExp.toVHDL();
		Map<HDLSwitchCaseStatement, VHDLContext> ctxs = new LinkedHashMap<HDLSwitchCaseStatement, VHDLContext>();
		Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>();
		boolean hasUnclocked = false;
		for (HDLSwitchCaseStatement cs : getCases()) {
			VHDLContext vhdl = cs.toVHDL(pid);
			ctxs.put(cs, vhdl);
			if (vhdl.unclockedStatements.size() > 0)
				hasUnclocked = true;
			configs.addAll(vhdl.clockedStatements.keySet());
		}
		for (HDLRegisterConfig hdlRegisterConfig : configs) {
			CaseStatement cs = new CaseStatement(caseExp);
			for (Map.Entry<HDLSwitchCaseStatement, VHDLContext> e : ctxs.entrySet()) {
				Alternative alt = createAlternative(cs, e, width);
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
				Alternative alt = createAlternative(cs, e, width);
				if (e.getValue().unclockedStatements.get(pid)!=null)
					alt.getStatements().addAll(e.getValue().unclockedStatements.get(pid));
			}
			context.addUnclockedStatement(pid, cs, this);
		}
		return context;
	}

	private static Alternative createAlternative(CaseStatement cs, Map.Entry<HDLSwitchCaseStatement, VHDLContext> e, BigInteger bits) {
		Alternative alt;
		HDLExpression label = e.getKey().getLabel();
		if (label != null) {
			BigInteger eval = label.constantEvaluate(null);
			if (eval != null)
				alt = cs.createAlternative(VHDLUtils.toBinaryLiteral(bits.intValue(), eval));
			else
				alt = cs.createAlternative(label.toVHDL());//The only valid reason here is an Enum
		} else {
			alt = cs.createAlternative(Choices.OTHERS);
		}
		return alt;
	}

	public VHDLContext HDLSwitchCaseStatement.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			res.merge(stmnt.toVHDL(pid));
		}
		return res;
	}

	public VHDLContext HDLAssignment.toVHDL(int pid) {
		VHDLContext context = new VHDLContext();
		SignalAssignment sa = new SignalAssignment((SignalAssignmentTarget) getLeft().toVHDL(), getRight().toVHDL());
		HDLReference ref = getLeft();
		HDLRegisterConfig config = ref.resolveVar().getRegisterConfig();
		if (config != null)
			context.addClockedStatement(config, sa);
		else
			context.addUnclockedStatement(pid, sa, this);
		return context;
	}

	public VHDLContext HDLForLoop.toVHDL(int pid) {
		VHDLContext context = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			context.merge(stmnt.toVHDL(pid));
		}
		VHDLContext res = new VHDLContext();
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : context.clockedStatements.entrySet()) {
			ForStatement fStmnt = new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(e.getValue());
			res.addClockedStatement(e.getKey(), fStmnt);
		}
		if (context.unclockedStatements.get(pid) != null) {
			ForStatement fStmnt = new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(context.unclockedStatements.get(pid));
			res.addUnclockedStatement(pid, fStmnt, this);
		}
		return res;
	}

	public VHDLContext HDLIfStatement.toVHDL(int pid) {
		VHDLContext thenCtx = new VHDLContext();
		for (HDLStatement stmnt : getThenDo()) {
			thenCtx.merge(stmnt.toVHDL(pid));
		}
		VHDLContext elseCtx = new VHDLContext();
		for (HDLStatement stmnt : getElseDo()) {
			elseCtx.merge(stmnt.toVHDL(pid));
		}
		Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>();
		configs.addAll(thenCtx.clockedStatements.keySet());
		configs.addAll(elseCtx.clockedStatements.keySet());
		VHDLContext res = new VHDLContext();
		res.merge(thenCtx);
		res.merge(elseCtx);
		res.clockedStatements.clear();
		res.unclockedStatements.clear();
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
			if (thenCtx.unclockedStatements.get(pid)!=null)
				ifs.getStatements().addAll(thenCtx.unclockedStatements.get(pid));
			if (elseCtx.unclockedStatements.get(pid)!=null)
				ifs.getElseStatements().addAll(elseCtx.unclockedStatements.get(pid));
			res.addUnclockedStatement(pid, ifs, this);
		}
		return res;
	}
}
