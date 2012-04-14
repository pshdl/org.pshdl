package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.validation.HDLValidator.IntegerMeta;

public class Insulin {
	public static HDLPackage transform(HDLPackage orig) {
		annotateReadCount(orig);
		annotateWriteCount(orig);
		HDLPackage apply = handleOutPortRead(orig);
		apply = handleMultiBitAccess(apply);
		apply = handleMultiForLoop(apply);
		apply = handlePostfixOp(apply);
		apply = fortifyType(apply);
		return apply;
	}

	private static HDLPackage fortifyType(HDLPackage apply) {
		ModificationSet ms = new ModificationSet();
		fortifyOpExpressions(apply, ms);
		fortifyAssignments(apply, ms);
		fortifyIfExpressions(apply, ms);
		fortifyRanges(apply, ms);
		fortifyWidthExpressions(apply, ms);
		foritfyArrays(apply, ms);
		foritfyFunctions(apply, ms);
		HDLPackage result = ms.apply(apply);
		result.validateAllFields(null, true);
		return result;
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
			if (ref instanceof HDLInterfaceRef) {
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
			HDLPrimitive leftType = assignment.getLeft().determineType();
			HDLExpression exp = assignment.getRight();
			fortify(ms, exp, leftType);
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
				inferenceInfo = HDLPrimitives.getInstance().getArithOpType(left, aop.getType(), right);
				break;
			case HDLShiftOp:
				HDLShiftOp sop = (HDLShiftOp) opExpression;
				left = sop.getLeft();
				right = sop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getShiftOpType(left, sop.getType(), right);
				break;
			case HDLBitOp:
				HDLBitOp bop = (HDLBitOp) opExpression;
				left = bop.getLeft();
				right = bop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getBitOpType(left, bop.getType(), right);
				break;
			case HDLEqualityOp:
				HDLEqualityOp eop = (HDLEqualityOp) opExpression;
				left = eop.getLeft();
				right = eop.getRight();
				inferenceInfo = HDLPrimitives.getInstance().getEqualityOpType(left, eop.getType(), right);
				break;
			case HDLManip:
				HDLManip manip = (HDLManip) opExpression;
				left = manip.getTarget();
				inferenceInfo = HDLPrimitives.getInstance().getManipOpType(left, manip.getType(), manip.getCastTo());
			}
			if (inferenceInfo != null) {
				if (inferenceInfo.error != null) {
					throw new IllegalArgumentException("Expression has error:" + inferenceInfo.error);
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
		ms.replace(exp, new HDLEqualityOp().setLeft(exp.copy()).setType(HDLEqualityOpType.NOT_EQ).setRight(new HDLLiteral().setVal("0")));
	}

	private static void fortify(ModificationSet ms, HDLExpression exp, HDLPrimitive targetType) {
		HDLPrimitive lt = exp.determineType();
		if (!targetType.equals(lt)) {
			if (targetType.getType() == HDLPrimitiveType.BOOL)
				makeBool(ms, exp);
			else
				cast(ms, targetType, exp);
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
					newLoops[i++] = loop.copyFiltered(CopyFilter.DEEP).setRange(HDLObject.asList(r));
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
			if (hdv.getDirection() == HDLDirection.OUT) {
				for (HDLVariable var : hdv.getVariables()) {
					Integer readCount = var.getMeta(IntegerMeta.READ_COUNT);
					if (readCount != null) {
						// out bit a, b=a;
						HDLQualifiedName oldFQN = new HDLQualifiedName(var.getName());
						HDLVariable newVar = var.copy().setName(var.getName() + "_OutRead");
						HDLQualifiedName newFQN = new HDLQualifiedName(newVar.getName());
						ms.replace(var, var.setDefaultValue(new HDLVariableRef().setVar(newFQN)));
						HDLVariableDeclaration tempDecl = new HDLVariableDeclaration().setType(hdv.getTypeRefName()).addVariables(newVar).setContainer(hdv.getContainer());
						ms.insertAfter(hdv, tempDecl);
						List<HDLVariableRef> reads = orig.getAllObjectsOf(HDLVariableRef.class, true);
						for (HDLVariableRef varRef : reads) {
							if (varRef.getVarRefName().equals(oldFQN)) {
								ms.replace(varRef, varRef.setVar(newFQN));
							}
						}
					}
				}
			}
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
