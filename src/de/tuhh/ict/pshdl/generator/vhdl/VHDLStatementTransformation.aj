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
import de.tuhh.ict.pshdl.model.extensions.*;
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
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;

public aspect VHDLStatementTransformation {

	private static final String ORIGINAL_FULLNAME = "ORIGINAL_FULLNAME";

	public static GenericMeta<Boolean> EXPORT = new GenericMeta<Boolean>("EXPORT", true);

	public abstract VHDLContext HDLStatement.toVHDL(int pid);

	public VHDLContext HDLDirectGeneration.toVHDL(int pid) {
		return new VHDLContext();
	}

	public VHDLContext HDLFunctionCall.toVHDL(int pid) {
		return HDLFunctions.toVHDL(this, pid);
	}

	public VHDLContext HDLBlock.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		boolean process = false;
		if (getProcess() != null && getProcess()) {
			process = true;
		}
		if (process)
			pid = res.newProcessID();
		for (HDLStatement stmnt : getStatements()) {
			res.merge(stmnt.toVHDL(pid), false);
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
		HDLVariable hVar = getVar();
		String ifName = getVar().getName();
		HDLQualifiedName asRef = hIf.asRef();
		HDLInterfaceDeclaration hid = hIf.getContainer(HDLInterfaceDeclaration.class);
		List<AssociationElement> portMap;
		List<AssociationElement> genericMap;
		ConcurrentStatement instantiation;
		// Perform instantiation as Component rather than Entity if
		// VHDLComponent Annotation is present
		ArrayList<HDLVariableDeclaration> ports = hIf.getPorts();
		if (hid != null && hid.getAnnotation(HDLBuiltInAnnotations.VHDLComponent) != null) {
			HDLAnnotation anno = hid.getAnnotation(HDLBuiltInAnnotations.VHDLComponent);
			if ("declare".equals(anno.getValue())){
				Component c=new Component(asRef.getLastSegment().toString());
				VHDLContext cContext=new VHDLContext();
				for (HDLVariableDeclaration port : ports) {
					cContext.merge(port.toVHDL(-1), true);
				}
				for (Signal signal : cContext.ports) {
					c.getPort().add(signal);
				}
				for (ConstantDeclaration cd : cContext.constants) {
					for (Object obj:cd.getObjects())
						c.getGeneric().add((Constant)obj);
				}
				for (Constant constant : cContext.generics) {
					c.getGeneric().add(constant);
				}
				res.addComponent(c);
			} else
				res.addImport(VHDLPackageTransformation.getNameRef(asRef));
			Component entity = new Component(asRef.getLastSegment().toString());
			ComponentInstantiation inst = new ComponentInstantiation(ifName, entity);
			portMap = inst.getPortMap();
			genericMap = inst.getGenericMap();
			instantiation = inst;
		} else {
			Entity entity = new Entity(VHDLPackageTransformation.getNameRef(asRef).toString());
			EntityInstantiation inst = new EntityInstantiation(ifName, entity);
			portMap = inst.getPortMap();
			genericMap = inst.getGenericMap();
			instantiation = inst;
		}
		for (HDLVariableDeclaration hvd : ports) {
			if (inAndOut.contains(hvd.getDirection())) {
				Collection<HDLAnnotation> typeAnno = HDLQuery.select(HDLAnnotation.class).from(hvd).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.VHDLType.toString())
						.getAll();
				for (HDLVariable var : hvd.getVariables()) {
					HDLVariable sigVar = var.setName(ifName + "_" + var.getName());
					HDLVariableRef ref = sigVar.asHDLRef();
					for (int i = 0; i < hVar.getDimensions().size(); i++) {
						ref = ref.addArray(new HDLVariableRef().setVar(HDLQualifiedName.create(Character.toString((char) (i + 'I')))));
					}
					for (HDLExpression exp : hVar.getDimensions()) {
						sigVar = sigVar.addDimensions(exp);
					}
					if (var.getDimensions().size() != 0) {
						//Arrays are always named in VHDL, so the type annotation should be present
						if (typeAnno.isEmpty()) {
							HDLQualifiedName name = VHDLPackageTransformation.getPackageNameRef(asRef).append(getArrayRefName(var, true));
							res.addImport(name);
							HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL)
									.setVariables(HDLObject.asList(sigVar.setDimensions(null).addAnnotations(HDLBuiltInAnnotations.VHDLType.create(name.toString()))))
									.copyDeepFrozen(this);
							res.merge(newHVD.toVHDL(pid), false);
						} else {
							HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar.setDimensions(null))).copyDeepFrozen(this);
							res.merge(newHVD.toVHDL(pid), false);
						}
					} else {
						HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(sigVar)).copyDeepFrozen(this);
						res.merge(newHVD.toVHDL(pid), false);
					}
					portMap.add(new AssociationElement(VHDLOutputValidator.getVHDLName(var.getName()), ref.toVHDL()));
				}
			} else {
				//Parameter get a special treatment because they have been renamed by HDLInterfaceInstantiation resolveIF
				if (hvd.getDirection() == HDLDirection.PARAMETER) {
					for (HDLVariable var : hvd.getVariables()) {
						HDLVariable sigVar = var;
						if (var.getMeta(HDLInterfaceInstantiation.ORIG_NAME) != null)
							sigVar = var.setName(var.getMeta(HDLInterfaceInstantiation.ORIG_NAME));

						HDLVariableRef ref = var.asHDLRef();
						genericMap.add(new AssociationElement(sigVar.getName(), ref.toVHDL()));
					}
				}
			}
		}
		ForGenerateStatement forLoop = null;
		if (hVar.getDimensions().size() == 0)
			res.addConcurrentStatement(instantiation);
		else {
			for (int i = 0; i < hVar.getDimensions().size(); i++) {
				HDLExpression to = new HDLArithOp().setLeft(hVar.getDimensions().get(i)).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
				HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(to).setContainer(this);
				ForGenerateStatement newFor = new ForGenerateStatement("generate_" + ifName, Character.toString((char) (i + 'I')), range.toVHDL(Range.Direction.TO));
				if (forLoop != null)
					forLoop.getStatements().add(newFor);
				else
					res.addConcurrentStatement(newFor);
				forLoop = newFor;
			}
			if (forLoop == null)
				throw new IllegalArgumentException("Should not get here");
			forLoop.getStatements().add(instantiation);
		}
		return res;
	}

	private static String getArrayRefName(HDLVariable var, boolean external) {
		if (external) {
			HDLQualifiedName fullName;
			if (var.getMeta(ORIGINAL_FULLNAME) != null)
				fullName = (HDLQualifiedName) var.getMeta(ORIGINAL_FULLNAME);
			else
				fullName = fullNameOf(var);
			return fullName.toString('_') + "_array";
		}
		return var.getName() + "_array";
	}

	public VHDLContext HDLVariableDeclaration.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		HDLPrimitive primitive = getPrimitive();
		SubtypeIndication type = null;
		HDLExpression resetValue = null;
		HDLAnnotation typeAnno = HDLQuery.select(HDLAnnotation.class).from(this).where(HDLAnnotation.fName).isEqualTo(HDLBuiltInAnnotations.VHDLType.toString()).getFirst();
		if (typeAnno != null) {
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
				boolean noExplicitResetVar = var.getAnnotation(HDLBuiltInAnnotations.VHDLNoExplicitReset) != null;
				SubtypeIndication varType = type;
				if (var.getDimensions().size() != 0) {
					@SuppressWarnings("rawtypes")
					List<DiscreteRange> ranges = new LinkedList<DiscreteRange>();
					for (HDLExpression arrayWidth : var.getDimensions()) {
						HDLExpression newWidth = new HDLArithOp().setLeft(arrayWidth).setType(HDLArithOp.HDLArithOpType.MINUS).setRight(HDLLiteral.get(1));
						Range range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(newWidth).copyDeepFrozen(this).toVHDL(Direction.TO);
						ranges.add(range);
					}
					boolean external = isExternal();
					ConstrainedArray arrType = new ConstrainedArray(getArrayRefName(var, external), type, ranges);
					res.addTypeDeclaration(arrType, external);
					varType = arrType;
				}
				if (resetValue != null && !noExplicitResetVar) {
					boolean synchedArray = false;
					if (resetValue instanceof HDLVariableRef) {
						HDLVariableRef ref = (HDLVariableRef) resetValue;
						synchedArray = ref.resolveVar().getDimensions().size() != 0;
					}
					HDLStatement initLoop = Insulin.createArrayForLoop(var.getDimensions(), 0, resetValue, new HDLVariableRef().setVar(var.asRef()), synchedArray).copyDeepFrozen(
							this);
					VHDLContext vhdl = initLoop.toVHDL(pid);
					res.addResetValue(getRegister(), vhdl.getStatement());
				}
				Signal s = new Signal(var.getName(), varType);
				Constant constant = new Constant(var.getName(), varType);
				if (var.getDefaultValue() != null)
					constant.setDefaultValue(var.getDefaultValue().toVHDL());
				if (noExplicitResetVar) {
					Aggregate assign = Aggregate.OTHERS(new CharacterLiteral('0'));
					for (int i = 0; i < var.getDimensions().size(); i++)
						assign = Aggregate.OTHERS(assign);
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
					if (var.hasMeta(EXPORT))
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
		HDLExpression hCaseExp = getCaseExp();
		BigInteger width = null;
		HDLType type = TypeExtension.typeOf(hCaseExp);
		if (type instanceof HDLPrimitive) {
			width = ConstantEvaluate.valueOf(((HDLPrimitive) type).getWidth(),null);
			if (width == null)
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
				if (e.getValue().unclockedStatements.get(pid) != null)
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
			BigInteger eval = ConstantEvaluate.valueOf(label,null);
			if (eval != null)
				alt = cs.createAlternative(VHDLUtils.toBinaryLiteral(bits.intValue(), eval));
			else
				alt = cs.createAlternative(label.toVHDL());// The only valid
															// reason here is an
															// Enum
		} else {
			alt = cs.createAlternative(Choices.OTHERS);
		}
		return alt;
	}

	public VHDLContext HDLSwitchCaseStatement.toVHDL(int pid) {
		VHDLContext res = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			res.merge(stmnt.toVHDL(pid), false);
		}
		return res;
	}

	public VHDLContext HDLAssignment.toVHDL(int pid) {
		VHDLContext context = new VHDLContext();
		SignalAssignment sa=null;
		HDLReference ref = getLeft();
		HDLVariable var = ref.resolveVar();
		ArrayList<HDLExpression> dim = var.getDimensions();
		if (dim.size()!=0 && ref.getClassType()==HDLClass.HDLVariableRef){
			HDLVariableRef varRef=(HDLVariableRef) ref;
			for (int i=0;i<varRef.getArray().size();i++){
				dim.remove(0);
			}
			if (dim.size()!=0){
				//XXX Implement correct array assignment for non full assignments
				HDLAnnotation typeAnno = var.getAnnotation(HDLBuiltInAnnotations.VHDLType);
				if (typeAnno!=null){
					sa = new SignalAssignment((SignalAssignmentTarget) ref.toVHDL(), new TypeConversion(new UnresolvedType(typeAnno.getValue()), getRight().toVHDL()));
				} else {
					HDLVariableDeclaration hvd=var.getContainer(HDLVariableDeclaration.class);
					sa = new SignalAssignment((SignalAssignmentTarget) ref.toVHDL(), new TypeConversion(new UnresolvedType(getArrayRefName(var, hvd.isExternal())), getRight().toVHDL()));
				}
			} else {
				sa = new SignalAssignment((SignalAssignmentTarget) ref.toVHDL(), getRight().toVHDL());
			}
		} else
			sa = new SignalAssignment((SignalAssignmentTarget) ref.toVHDL(), getRight().toVHDL());
		HDLRegisterConfig config = var.getRegisterConfig();
		if (config != null)
			context.addClockedStatement(config, sa);
		else
			context.addUnclockedStatement(pid, sa, this);
		return context;
	}

	public VHDLContext HDLForLoop.toVHDL(int pid) {
		VHDLContext context = new VHDLContext();
		for (HDLStatement stmnt : getDos()) {
			context.merge(stmnt.toVHDL(pid), false);
		}
		VHDLContext res = new VHDLContext();
		res.merge(context, true);
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
			thenCtx.merge(stmnt.toVHDL(pid), false);
		}
		VHDLContext elseCtx = new VHDLContext();
		for (HDLStatement stmnt : getElseDo()) {
			elseCtx.merge(stmnt.toVHDL(pid), false);
		}
		Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>();
		configs.addAll(thenCtx.clockedStatements.keySet());
		configs.addAll(elseCtx.clockedStatements.keySet());
		VHDLContext res = new VHDLContext();
		res.merge(thenCtx, true);
		res.merge(elseCtx, true);
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
			if (thenCtx.unclockedStatements.get(pid) != null)
				ifs.getStatements().addAll(thenCtx.unclockedStatements.get(pid));
			if (elseCtx.unclockedStatements.get(pid) != null)
				ifs.getElseStatements().addAll(elseCtx.unclockedStatements.get(pid));
			res.addUnclockedStatement(pid, ifs, this);
		}
		return res;
	}

	public VHDLContext HDLFunction.toVHDL(int pid) {
		throw new IllegalArgumentException("Not supported");
	}
}
