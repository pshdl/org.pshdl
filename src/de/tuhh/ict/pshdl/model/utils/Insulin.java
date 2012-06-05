package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLObject.MetaAccess;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.IHDLGenerator.HDLGenerationInfo;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.IntegerMeta;

public class Insulin {
	public static HDLPackage transform(HDLPackage orig) {
		annotateReadCount(orig);
		annotateWriteCount(orig);
		HDLPackage apply = handleOutPortRead(orig);
		apply = includeGenerators(apply);
		// System.out.println("Insulin.transform()" + apply);
		// apply.validateAllFields(null, true);
		apply = generateClkAndReset(apply);
		apply = handleMultiBitAccess(apply);
		apply = handleMultiForLoop(apply);
		apply = handlePostfixOp(apply);
		apply = generateInitializations(apply);
		apply = fortifyType(apply);
		// apply = simplifyExpressions(apply);
		apply.validateAllFields(null, true);
		return apply;
	}

	/**
	 * Checks for HDLDirectGenerations and calls the generators. If they are
	 * includes, it will be included and the references resolved
	 * 
	 * @param apply
	 * @return
	 */
	private static HDLPackage includeGenerators(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		List<HDLDirectGeneration> gens = apply.getAllObjectsOf(HDLDirectGeneration.class, true);
		for (HDLDirectGeneration generation : gens) {
			HDLGenerationInfo generationInfo = HDLGenerators.getImplementation(generation);
			if (generationInfo.include) {
				HDLQualifiedName ifRef = generation.getHIf().asRef();
				HDLQualifiedName fullName = generation.getFullName();
				System.out.println("Insulin.includeGenerators()" + generationInfo.unit);
				HDLStatement[] stmnts = generationInfo.unit.getStatements().toArray(new HDLStatement[0]);
				ms.replace(generation, stmnts);
				List<HDLInterfaceRef> ifRefs = apply.getAllObjectsOf(HDLInterfaceRef.class, true);
				for (HDLInterfaceRef hdI : ifRefs) {
					if (hdI.resolveHIf().determineType().asRef().equals(ifRef)) {
						HDLQualifiedName newName = fullName.append(hdI.getVarRefName().getLastSegment());
						ms.replace(hdI, new HDLVariableRef(null, newName, hdI.getArray(), hdI.getBits()));
					}
				}
			}
		}
		return ms.apply(apply);
	}

	private static EnumSet<HDLDirection> doNotInit = EnumSet.of(HDLDirection.HIDDEN, HDLDirection.CONSTANT, HDLDirection.PARAMETER, HDLDirection.IN);

	private static HDLPackage generateInitializations(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		List<HDLVariableDeclaration> allVarDecls = apply.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : allVarDecls) {
			if (!doNotInit.contains(hvd.getDirection())) {
				HDLRegisterConfig register = hvd.getRegister();
				for (HDLVariable var : hvd.getVariables()) {
					HDLExpression defaultValue = var.getDefaultValue();
					if ((defaultValue == null) && (register == null)) {
						if ((hvd.getPrimitive() != null))
							defaultValue = HDLLiteral.get(0);
						else {
							HDLType resolveType = hvd.resolveType();
							if (resolveType instanceof HDLEnum) {
								HDLEnum hEnum = (HDLEnum) resolveType;
								defaultValue = new HDLVariableRef().setVar(hEnum.getEnums().get(0).asRef());
							}
						}
					}
					if (defaultValue != null) {
						ArrayList<HDLExpression> dimensions = var.getDimensions();
						HDLVariableRef setVar = new HDLVariableRef().setVar(var.asRef());
						boolean synchedArray = false;
						if (defaultValue instanceof HDLVariableRef) {
							HDLVariableRef ref = (HDLVariableRef) defaultValue;
							synchedArray = ref.resolveVar().getDimensions().size() != 0;
						}
						HDLStatement init = createArrayForLoop(dimensions, 0, defaultValue, setVar, synchedArray);
						insertFirstStatement(ms, var, init);
						ms.replace(var, var.setDefaultValue(null));
					}

				}
			}
		}
		return ms.apply(apply);
	}

	public static HDLStatement createArrayForLoop(ArrayList<HDLExpression> dimensions, int i, HDLExpression defaultValue, HDLVariableRef ref, boolean synchedArray) {
		if (i == dimensions.size()) {
			return new HDLAssignment().setLeft(ref).setRight(defaultValue);
		}
		HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(new HDLArithOp().setLeft(dimensions.get(i)).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1)));
		HDLVariable param = new HDLVariable().setName(Character.toString((char) (i + 'I')));
		HDLForLoop loop = new HDLForLoop().setRange(HDLObject.asList(range)).setParam(param);
		HDLVariableRef paramRef = new HDLVariableRef().setVar(param.asRef());
		if (synchedArray) {
			HDLVariableRef defRef = (HDLVariableRef) defaultValue;
			defaultValue = defRef.addArray(paramRef.copy());
		}
		return loop.addDos(createArrayForLoop(dimensions, i + 1, defaultValue, ref.addArray(paramRef), synchedArray));
	}

	private static void insertFirstStatement(ModificationSet ms, HDLObject container, HDLStatement stmnt) {
		if (container.getClassType() != HDLClass.HDLUnit) {
			insertFirstStatement(ms, container.getContainer(), stmnt);
			return;
		}
		HDLUnit unit = (HDLUnit) container;
		HDLStatement statement = unit.getStatements().get(0);
		ms.insertBefore(statement, stmnt);
	}

	/**
	 * Checks for the clock and reset annotation and replaces all $clk and $rst
	 * references with it.
	 * 
	 * @param apply
	 * @return
	 */
	private static HDLPackage generateClkAndReset(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		HDLVariable defClkVar = new HDLVariable().setName("clk");
		boolean customClk = false, customRst = false;
		HDLVariable defRstVar = new HDLVariable().setName("rst");
		List<HDLAnnotation> annotations = apply.getAllObjectsOf(HDLAnnotation.class, true);
		for (HDLAnnotation hdlAnnotation : annotations) {
			if ("@clock".equalsIgnoreCase(hdlAnnotation.getName())) {
				HDLObject container = hdlAnnotation.getContainer();
				if (container instanceof HDLVariableDeclaration) {
					HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
					defClkVar = hvd.getVariables().get(0);
					customClk = true;
				}
			}
			if ("@reset".equalsIgnoreCase(hdlAnnotation.getName())) {
				HDLObject container = hdlAnnotation.getContainer();
				if (container instanceof HDLVariableDeclaration) {
					HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
					defRstVar = hvd.getVariables().get(0);
					customRst = true;
				}
			}
		}

		List<HDLVariableDeclaration> decls = apply.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration decl : decls) {
			HDLRegisterConfig register = decl.getRegister();
			if (register != null) {
				if ("$clk".equals(register.getClkRefName().getLastSegment())) {
					register = register.setClk(HDLQualifiedName.create(defClkVar.getName()));
					if (!customClk)
						insertClk(ms, register.getContainer(), defClkVar, SignalInserted.ClkInserted);
				}
				if ("$rst".equals(register.getRstRefName().getLastSegment())) {
					register = register.setRst(HDLQualifiedName.create(defRstVar.getName()));
					if (!customRst)
						insertClk(ms, register.getContainer(), defRstVar, SignalInserted.RstInserted);
				}
				if (register != decl.getRegister())
					ms.replace(decl.getRegister(), register);
			}
		}

		List<HDLVariableRef> varRefs = apply.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : varRefs) {
			if ("$clk".equals(ref.getVarRefName().getLastSegment())) {
				ms.replace(ref, defClkVar.asHDLRef());
			}
			if ("$rst".equals(ref.getVarRefName().getLastSegment())) {
				ms.replace(ref, defRstVar.asHDLRef());
			}
		}
		return ms.apply(apply);
	}

	public static enum SignalInserted implements MetaAccess<Boolean> {
		ClkInserted, RstInserted;
	}

	private static void insertClk(ModificationSet ms, HDLObject container, HDLVariable defClkVar, SignalInserted signalInserted) {
		if (container.getClassType() != HDLClass.HDLUnit) {
			insertClk(ms, container.getContainer(), defClkVar, signalInserted);
			return;
		}
		boolean hasMeta = container.hasMeta(signalInserted);
		if (!hasMeta) {
			HDLUnit unit = (HDLUnit) container;
			HDLStatement statement = unit.getStatements().get(0);
			HDLPrimitive bit = HDLPrimitive.getBit();
			container.setMeta(signalInserted);
			ms.insertBefore(statement, new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(bit).addVariables(defClkVar));
		}
	}

	private static HDLPackage fortifyType(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		fortifyOpExpressions(apply, ms);
		fortifyAssignments(apply, ms);
		fortifyIfExpressions(apply, ms);
		fortifyRanges(apply, ms);
		fortifyWidthExpressions(apply, ms);
		fortifyDefaultAndResetExpressions(apply, ms);
		foritfyArrays(apply, ms);
		foritfyFunctions(apply, ms);
		return ms.apply(apply);
	}

	private static void foritfyFunctions(HDLPackage apply, ModificationSet ms) {
		List<HDLFunction> functions = apply.getAllObjectsOf(HDLFunction.class, true);
		for (HDLFunction function : functions) {
			HDLTypeInferenceInfo info = HDLFunctions.getInferenceInfo(function);
			if (info != null) {
				ArrayList<HDLExpression> params = function.getParams();
				for (int j = 0; j < params.size(); j++) {
					HDLExpression exp = params.get(j);
					fortify(ms, exp, info.args[j]);
				}
			}
		}
	}

	private static void foritfyArrays(HDLPackage apply, ModificationSet ms) {
		List<HDLVariableRef> varRefs = apply.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : varRefs) {
			for (HDLExpression exp : ref.getArray()) {
				fortify(ms, exp, HDLPrimitive.getNatural());
			}
			if (ref.getClassType() == HDLClass.HDLInterfaceRef) {
				HDLInterfaceRef hir = (HDLInterfaceRef) ref;
				for (HDLExpression exp : hir.getIfArray()) {
					fortify(ms, exp, HDLPrimitive.getNatural());
				}
			}
		}
	}

	private static void fortifyWidthExpressions(HDLPackage apply, ModificationSet ms) {
		List<HDLPrimitive> primitives = apply.getAllObjectsOf(HDLPrimitive.class, true);
		for (HDLPrimitive hdlPrimitive : primitives) {
			HDLExpression width = hdlPrimitive.getWidth();
			if (width != null) {
				fortify(ms, width, HDLPrimitive.getNatural());
			}
		}
	}

	private static void fortifyDefaultAndResetExpressions(HDLPackage apply, ModificationSet ms) {
		List<HDLVariableDeclaration> primitives = apply.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : primitives) {
			HDLRegisterConfig reg = hvd.getRegister();
			HDLType determineType = hvd.determineType();
			if (reg != null) {
				fortify(ms, reg.getResetValue(), determineType);
			}
			for (HDLVariable var : hvd.getVariables()) {
				if (var.getDefaultValue() != null) {
					fortify(ms, var.getDefaultValue(), determineType);
				}
			}
		}
	}

	private static void fortifyRanges(HDLPackage apply, ModificationSet ms) {
		List<HDLRange> ranges = apply.getAllObjectsOf(HDLRange.class, true);
		for (HDLRange range : ranges) {
			HDLExpression exp = range.getFrom();
			if (exp != null) {
				fortify(ms, exp, HDLPrimitive.getNatural());
			}
			fortify(ms, range.getTo(), HDLPrimitive.getNatural());
		}
	}

	private static void fortifyIfExpressions(HDLPackage apply, ModificationSet ms) {
		List<HDLIfStatement> ifs = apply.getAllObjectsOf(HDLIfStatement.class, true);
		for (HDLIfStatement assignment : ifs) {
			fortify(ms, assignment.getIfExp(), HDLPrimitive.getBool());
		}
	}

	private static void fortifyAssignments(HDLPackage apply, ModificationSet ms) {
		List<HDLAssignment> assignments = apply.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment assignment : assignments) {
			HDLType leftType = assignment.getLeft().determineType();
			HDLExpression exp = assignment.getRight();
			if (leftType instanceof HDLPrimitive) {
				HDLPrimitive pt = (HDLPrimitive) leftType;
				fortify(ms, exp, pt);
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	private static void fortifyOpExpressions(HDLPackage apply, ModificationSet ms) {
		List<HDLExpression> opEx = apply.getAllObjectsOf(HDLExpression.class, true);
		for (HDLExpression opExpression : opEx) {
			HDLTypeInferenceInfo inferenceInfo = null;
			HDLExpression left = null;
			HDLExpression right = null;
			switch (opExpression.getClassType()) {
			case HDLArithOp:
				HDLArithOp aop = (HDLArithOp) opExpression;
				left = aop.getLeft();
				right = aop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getArithOpType(aop);
				break;
			case HDLShiftOp:
				HDLShiftOp sop = (HDLShiftOp) opExpression;
				left = sop.getLeft();
				right = sop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getShiftOpType(sop);
				break;
			case HDLBitOp:
				HDLBitOp bop = (HDLBitOp) opExpression;
				left = bop.getLeft();
				right = bop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getBitOpType(bop);
				break;
			case HDLEqualityOp:
				HDLEqualityOp eop = (HDLEqualityOp) opExpression;
				left = eop.getLeft();
				right = eop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getEqualityOpType(eop);
				break;
			case HDLManip:
				HDLManip manip = (HDLManip) opExpression;
				left = manip.getTarget();
				inferenceInfo = HDLPrimitives.getInstance().getManipOpType(manip);
			}
			if (inferenceInfo != null) {
				if (inferenceInfo.error != null) {
					throw new IllegalArgumentException("The expression has an error:" + inferenceInfo.error);
				}
				fortify(ms, left, inferenceInfo.args[0]);
				if (right != null)
					fortify(ms, right, inferenceInfo.args[1]);
			}
		}
	}

	private static void cast(ModificationSet ms, HDLPrimitive targetType, HDLExpression exp) {
		ms.replace(exp, new HDLManip().setType(HDLManipType.CAST).setCastTo(targetType.copy()).setTarget(exp.copy()));
	}

	private static void makeBool(ModificationSet ms, HDLExpression exp) {
		HDLManip zero = new HDLManip().setType(HDLManipType.CAST).setCastTo(exp.determineType()).setTarget(HDLLiteral.get(0));
		ms.replace(exp, new HDLEqualityOp().setLeft(exp.copy()).setType(HDLEqualityOpType.NOT_EQ).setRight(zero));
	}

	private static void fortify(ModificationSet ms, HDLExpression exp, HDLType targetType) {
		if (targetType instanceof HDLPrimitive) {
			HDLPrimitive pt = (HDLPrimitive) targetType;
			HDLType lt = exp.determineType();
			if (!targetType.equals(lt)) {
				if (pt.getType() == HDLPrimitiveType.BOOL)
					makeBool(ms, exp);
				else
					cast(ms, pt, exp);
			}
		}
	}

	@SuppressWarnings("null")
	private static HDLPackage handlePostfixOp(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		List<HDLAssignment> loops = apply.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment hdlAssignment : loops) {
			HDLAssignmentType type = hdlAssignment.getType();
			HDLOpExpression op = null;
			switch (type) {
			case DIV_ASSGN:
				op = new HDLArithOp().setType(HDLArithOpType.DIV);
				break;
			case MOD_ASSGN:
				op = new HDLArithOp().setType(HDLArithOpType.MOD);
				break;
			case MUL_ASSGN:
				op = new HDLArithOp().setType(HDLArithOpType.MUL);
				break;
			case ADD_ASSGN:
				op = new HDLArithOp().setType(HDLArithOpType.PLUS);
				break;
			case SUB_ASSGN:
				op = new HDLArithOp().setType(HDLArithOpType.MINUS);
				break;
			case AND_ASSGN:
				op = new HDLBitOp().setType(HDLBitOpType.AND);
				break;
			case OR_ASSGN:
				op = new HDLBitOp().setType(HDLBitOpType.OR);
				break;
			case XOR_ASSGN:
				op = new HDLBitOp().setType(HDLBitOpType.XOR);
				break;
			case SLL_ASSGN:
				op = new HDLShiftOp().setType(HDLShiftOpType.SLL);
				break;
			case SRA_ASSGN:
				op = new HDLShiftOp().setType(HDLShiftOpType.SRA);
				break;
			case SRL_ASSGN:
				op = new HDLShiftOp().setType(HDLShiftOpType.SRL);
				break;
			case ASSGN:
				continue;
			}
			op = op.setLeft(hdlAssignment.getLeft().copy()).setRight(hdlAssignment.getRight());
			HDLAssignment newAss = new HDLAssignment().setLeft(hdlAssignment.getLeft().copy()).setType(HDLAssignmentType.ASSGN).setRight(op);
			ms.replace(hdlAssignment, newAss);
		}
		return ms.apply(apply);
	}

	private static HDLPackage handleMultiForLoop(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		List<HDLForLoop> loops = apply.getAllObjectsOf(HDLForLoop.class, true);
		for (HDLForLoop loop : loops) {
			if (loop.getRange().size() > 1) {
				HDLForLoop[] newLoops = new HDLForLoop[loop.getRange().size()];
				int i = 0;
				for (HDLRange r : loop.getRange()) {
					newLoops[i++] = loop.setRange(HDLObject.asList(r));
				}
				ms.replace(loop, newLoops);
			}
		}
		return ms.apply(apply);
	}

	private static HDLPackage handleMultiBitAccess(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		List<HDLVariableRef> refs = apply.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			if (ref.getBits().size() > 1) {
				if (ref.getContainer() instanceof HDLAssignment) {
					HDLAssignment ass = (HDLAssignment) ref.getContainer();
					if (ass.getLeft() == ref) {
						// Multi Bit write access i.e b{1,2:3}=8;
						// variant 1
						// b{1}=8>>((3-2)+1)
						// b{2:3}=8
						// variant 2
						// bit<widthOfResult> b_bitAcces=8;
						// b{1}=b_bitAcces{sumOfWidthRightToIdx};
						// b{2:3}=b_bitAccess{from-min(from,to)+sumOfWidthRightToIdx:to-min(from,to)+sumOfWidthRightToIdx};
						throw new IllegalArgumentException("Multi bit write access not implemented");
					}
				}
				// Multi bit read access
				// b{1,4:5} == b{1}#b{4:5}
				HDLConcat concat = new HDLConcat();
				for (HDLRange r : ref.getBits()) {
					concat = concat.addCats(ref.setBits(HDLObject.asList(r)));
				}
				ms.replace(ref, concat);
			}
		}
		return ms.apply(apply);
	}

	private static HDLPackage handleOutPortRead(HDLPackage orig) {
		ModificationSet ms = new ModificationSet();
		List<HDLVariableDeclaration> list = orig.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hdv : list) {
			HDLVariableDeclaration origHdv = hdv;
			if (hdv.getDirection() == HDLDirection.OUT) {
				for (HDLVariable var : hdv.getVariables()) {
					Integer readCount = var.getMeta(IntegerMeta.READ_COUNT);
					if (readCount != null) {
						// Create new Declaration out uint<W> bar=bar_OutRead;
						// Extract bar from declaration out uint<W> x,y,bar=5;
						// Create a new Declaration uint<W> bar_outRead=5;
						// Change all reference to bar_OutRead

						HDLVariable outVar = var.setName(var.getName() + "_OutRead");
						ms.insertAfter(origHdv, hdv.setRegister(null).setVariables(HDLObject.asList(var.setDefaultValue(outVar.asHDLRef()))));

						if (hdv.getVariables().size() == 1) {
							hdv = hdv.setDirection(HDLDirection.INTERNAL).setVariables(HDLObject.asList(outVar));
						} else {
							ms.insertAfter(origHdv, new HDLVariableDeclaration().setType(origHdv.resolveType()).addVariables(outVar));
							hdv = hdv.removeVariables(var);
						}

						// Replace all read occourances with the new bar_outRead
						HDLQualifiedName originalFQN = var.asRef();
						HDLQualifiedName newFQN = outVar.asRef();
						List<HDLVariableRef> reads = orig.getAllObjectsOf(HDLVariableRef.class, true);
						for (HDLVariableRef varRef : reads) {
							if (varRef.getVarRefName().equals(originalFQN)) {
								ms.replace(varRef, varRef.setVar(newFQN));
							}
						}
					}
				}
			}
			if (origHdv != hdv)
				ms.replace(origHdv, hdv);
		}
		return ms.apply(orig);
	}

	private static void annotateReadCount(HDLPackage orig) {
		List<HDLVariableRef> list = orig.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : list) {
			if (ref.getContainer() instanceof HDLAssignment) {
				HDLAssignment ass = (HDLAssignment) ref.getContainer();
				if ((ass.getLeft() == ref) && (ass.getType() == HDLAssignmentType.ASSGN))
					// If it is a non-trivial assign, it contains a read
					continue;
			}
			HDLVariable var = ref.resolveVar();
			Integer meta = var.getMeta(IntegerMeta.READ_COUNT);
			if (meta == null)
				meta = 1;
			else
				meta++;
			var.addMeta(IntegerMeta.READ_COUNT, meta);
		}
	}

	private static void annotateWriteCount(HDLPackage orig) {
		List<HDLAssignment> list = orig.getAllObjectsOf(HDLAssignment.class, true);
		for (HDLAssignment ass : list) {
			HDLReference left = ass.getLeft();
			if (left instanceof HDLVariableRef) {
				HDLVariableRef ref = (HDLVariableRef) left;
				HDLVariable var = ref.resolveVar();
				Integer meta = var.getMeta(IntegerMeta.WRITE_COUNT);
				if (meta == null)
					meta = 1;
				else
					meta++;
				var.addMeta(IntegerMeta.WRITE_COUNT, meta);
			}
		}
	}
}
