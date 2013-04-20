package org.pshdl.model.utils;

import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;

import com.google.common.collect.*;

public class Refactoring {
	public static <T extends IHDLObject> T renameVariable(HDLVariable var, String to, T obj) {
		ModificationSet ms = new ModificationSet();
		renameVariable(var, var.asRef().skipLast(1).append(to), obj, ms);
		return ms.apply(obj);
	}

	public static void renameVariable(HDLVariable hdlVariable, HDLQualifiedName newName, IHDLObject obj, ModificationSet ms) {
		ms.replace(hdlVariable, hdlVariable.setName(newName.getLastSegment()));
		if (hdlVariable.getContainer().getClassType() == HDLClass.HDLVariableDeclaration) {
			Collection<HDLVariableRef> varRefs = HDLQuery.getVarRefs(obj, hdlVariable);
			for (HDLVariableRef ref : varRefs) {
				ms.replace(ref, ref.setVar(newName));
			}
		} else if (hdlVariable.getContainer().getClassType() == HDLClass.HDLInterfaceInstantiation) {
			Collection<HDLInterfaceRef> ifRefs = HDLQuery.getInterfaceRefs(obj, hdlVariable);
			for (HDLInterfaceRef hir : ifRefs) {
				ms.replace(hir, hir.setHIf(newName));
			}
		}
	}

	public static HDLUnit inlineUnit(HDLUnit container, HDLInterfaceInstantiation hi, HDLUnit subUnit) {
		HDLVariable hiVar = hi.getVar();
		String prefix = hiVar.getName();
		subUnit = prefixVariables(subUnit, prefix);
		ArrayList<HDLExpression> outerDims = hiVar.getDimensions();
		subUnit = extractDefaultValue(subUnit);
		subUnit = addArrayReference(subUnit, outerDims);
		subUnit = addNewDimensions(subUnit, outerDims);
		subUnit = changeDirection(subUnit);

		ModificationSet res = new ModificationSet();
		Collection<HDLInterfaceRef> ifRefs = HDLQuery.getInterfaceRefs(container, hiVar);
		for (HDLInterfaceRef hir : ifRefs) {
			HDLQualifiedName newName = HDLQualifiedName.create(prefix + "_" + hir.getVarRefName().getLastSegment());
			ArrayList<HDLExpression> ifArray = hir.getIfArray();
			ifArray.addAll(hir.getArray());
			HDLVariableRef newRef = new HDLVariableRef().setVar(newName).setBits(hir.getBits()).setArray(ifArray);
			res.replace(hir, newRef);
		}
		List<HDLStatement> allStatements = subUnit.getInits();
		allStatements.addAll(subUnit.getStatements());
		if (outerDims.size() == 0) {
			res.replace(hi, allStatements.toArray(new HDLStatement[allStatements.size()]));
		} else {
			HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(new HDLArithOp().setLeft(outerDims.get(0)).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1)));
			HDLForLoop loop = new HDLForLoop().setParam(new HDLVariable().setName("I")).addRange(range);
			for (Iterator<HDLStatement> iterator = allStatements.iterator(); iterator.hasNext();) {
				HDLStatement statement = iterator.next();
				if (statement instanceof HDLDeclaration) {
					HDLDeclaration hdd = (HDLDeclaration) statement;
					res.addTo(container, HDLUnit.fInits, hdd);
					iterator.remove();
				}
			}
			loop = loop.setDos(allStatements);
			res.replace(hi, loop);
		}
		return res.apply(container);
	}

	private static HDLUnit extractDefaultValue(HDLUnit subUnit) {
		ModificationSet subMS = new ModificationSet();
		HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			HDLExpression defaultValue = hdlVariable.getDefaultValue();
			if (defaultValue != null) {
				HDLAssignment ass = new HDLAssignment().setLeft(hdlVariable.asHDLRef()).setRight(defaultValue);
				subMS.insertAfter(hdlVariable.getContainer(HDLStatement.class), ass);
			}
			subMS.replace(hdlVariable, hdlVariable.setDefaultValue(null));
		}
		return subMS.apply(subUnit);
	}

	private static HDLUnit changeDirection(HDLUnit subUnit) {
		ModificationSet subMS = new ModificationSet();
		HDLVariableDeclaration[] hvds = subUnit.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds) {
			switch (hvd.getDirection()) {
			case IN:
			case OUT:
			case INOUT:
				subMS.replace(hvd, hvd.setDirection(HDLDirection.INTERNAL));
				break;
			default:
				break;
			}
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}

	private static HDLUnit addNewDimensions(HDLUnit subUnit, ArrayList<HDLExpression> outerDims) {
		ModificationSet subMS = new ModificationSet();
		HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			ArrayList<HDLExpression> dims = hdlVariable.getDimensions();
			dims.addAll(0, outerDims);
			subMS.replace(hdlVariable, hdlVariable.setDimensions(dims));
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}

	private static HDLUnit addArrayReference(HDLUnit subUnit, ArrayList<HDLExpression> outerDims) {
		ModificationSet subMS = new ModificationSet();
		HDLVariableRef[] refs = subUnit.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs) {
			LinkedList<HDLExpression> array = Lists.newLinkedList();
			for (int i = 0; i < outerDims.size(); i++) {
				array.add(new HDLVariableRef().setVar(HDLQualifiedName.create(Character.toString((char) ('I' + i)))));
			}
			array.addAll(ref.getArray());
			subMS.replace(ref, ref.setArray(array));
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}

	private static HDLUnit prefixVariables(HDLUnit subUnit, String prefix) {
		ModificationSet subMS = new ModificationSet();
		HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (HDLVariable hdlVariable : vars) {
			String newName = prefix + "_" + hdlVariable.getName();
			Refactoring.renameVariable(hdlVariable, HDLQualifiedName.create(newName), subUnit, subMS);
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}
}
