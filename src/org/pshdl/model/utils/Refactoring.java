/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.utils;

import java.io.*;
import java.util.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.extensions.*;

import com.google.common.base.*;
import com.google.common.collect.*;
import com.google.common.io.*;

public class Refactoring {

	/**
	 * Rename a variable
	 * 
	 * @param var
	 *            the variable to rename
	 * @param to
	 *            a new name
	 * @param obj
	 *            the obj in which to search for the variable
	 * @return the obj with the variable renamed
	 */
	public static <T extends IHDLObject> T renameVariable(HDLVariable var, String to, T obj) {
		final ModificationSet ms = new ModificationSet();
		renameVariable(var, var.asRef().skipLast(1).append(to), obj, ms);
		return ms.apply(obj);
	}

	public static <T extends IHDLObject> void renameVariable(HDLVariable hdlVariable, HDLQualifiedName newName, T obj, ModificationSet ms) {
		ms.replace(hdlVariable, hdlVariable.setName(newName.getLastSegment()));
		final HDLClass classType = hdlVariable.getContainer().getClassType();
		switch (classType) {
		case HDLEnum:
			final Collection<HDLEnumRef> enumRefs = HDLQuery.getEnumRefs(obj, hdlVariable);
			for (final HDLEnumRef ref : enumRefs) {
				ms.replace(ref, ref.setVar(newName));
			}
			break;
		case HDLVariableDeclaration:
		case HDLFunctionParameter:
		case HDLForLoop:
			final Collection<HDLVariableRef> varRefs = HDLQuery.getVarRefs(obj, hdlVariable);
			for (final HDLVariableRef ref : varRefs) {
				ms.replace(ref, ref.setVar(newName));
			}
			break;
		case HDLDirectGeneration:
		case HDLInterfaceInstantiation:
			final Collection<HDLInterfaceRef> ifRefs = HDLQuery.getInterfaceRefs(obj, hdlVariable);
			for (final HDLInterfaceRef hir : ifRefs) {
				ms.replace(hir, hir.setHIf(newName));
			}
			break;
		default:
			throw new RuntimeException("Did not expect a container of type:" + classType);
		}
	}

	public static HDLPackage inlineUnit(HDLUnit container, HDLInterfaceInstantiation hi, HDLUnit subUnit) {
		final HDLVariable hiVar = hi.getVar();
		final String prefix = hiVar.getName();
		final ModificationSet ms = new ModificationSet();
		HDLPackage pkg = createHDLPackage(subUnit, ms);
		subUnit = ms.apply(subUnit);
		subUnit = prefixVariables(subUnit, prefix);
		subUnit = extractDefaultValue(subUnit);
		final ArrayList<HDLExpression> outerDims = hiVar.getDimensions();
		subUnit = addArrayReference(subUnit, outerDims);
		subUnit = addNewDimensions(subUnit, outerDims);
		subUnit = changeDirection(subUnit);
		subUnit = dereferenceRefs(subUnit);
		subUnit = Insulin.generateClkAndReset(subUnit);

		final ModificationSet res = new ModificationSet();
		final Collection<HDLInterfaceRef> ifRefs = HDLQuery.getInterfaceRefs(container, hiVar);
		for (final HDLInterfaceRef hir : ifRefs) {
			final HDLQualifiedName newName = HDLQualifiedName.create(prefix + "_" + hir.getVarRefName().getLastSegment());
			final ArrayList<HDLExpression> ifArray = hir.getIfArray();
			ifArray.addAll(hir.getArray());
			final HDLVariableRef newRef = new HDLVariableRef().setVar(newName).setBits(hir.getBits()).setArray(ifArray);
			res.replace(hir, newRef);
		}
		final List<HDLStatement> allStatements = subUnit.getInits();
		allStatements.addAll(subUnit.getStatements());
		for (final Iterator<HDLStatement> iterator = allStatements.iterator(); iterator.hasNext();) {
			final HDLStatement hdlStatement = iterator.next();
			if (hdlStatement instanceof HDLVariableDeclaration) {
				final HDLVariableDeclaration hvd = (HDLVariableDeclaration) hdlStatement;
				if (hvd.getDirection() == HDLDirection.PARAMETER) {
					iterator.remove();
				}
			}
		}
		if (outerDims.size() == 0) {
			res.replace(hi, allStatements.toArray(new HDLStatement[allStatements.size()]));
		} else {
			final HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(
					new HDLArithOp().setLeft(outerDims.get(0)).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1)));
			HDLForLoop loop = new HDLForLoop().setParam(new HDLVariable().setName("I")).addRange(range);
			for (final Iterator<HDLStatement> iterator = allStatements.iterator(); iterator.hasNext();) {
				final HDLStatement statement = iterator.next();
				if (statement instanceof HDLDeclaration) {
					final HDLDeclaration hdd = (HDLDeclaration) statement;
					res.addTo(container, HDLUnit.fInits, hdd);
					iterator.remove();
				}
			}
			loop = loop.setDos(allStatements);
			res.replace(hi, loop);
		}
		final HDLUnit apply = res.apply(container);
		pkg = pkg.addUnits(apply);
		try {
			apply.validateAllFields(container.getContainer(), true);
		} catch (final RuntimeException e) {
			try {
				Files.write(apply.toString().getBytes(Charsets.UTF_8), new File("CrashedUnit.pshdl"));
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
			throw e;
		}
		return pkg;
	}

	private static HDLPackage createHDLPackage(HDLUnit subUnit, final ModificationSet ms) {
		HDLPackage pkg = new HDLPackage();
		final HDLDeclaration[] decls = subUnit.getAllObjectsOf(HDLDeclaration.class, true);
		for (final HDLDeclaration decl : decls) {
			if (decl instanceof HDLVariableDeclaration) {
				continue;
			}
			if (decl instanceof HDLEnumDeclaration) {
				final HDLEnumDeclaration hde = (HDLEnumDeclaration) decl;
				final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hde.getHEnum());
				final String newName = fqn.toString();
				pkg = pkg.addDeclarations(hde.setHEnum(hde.getHEnum().setName(newName)));
				final HDLQualifiedName sqfn = HDLQualifiedName.create(fqn.getLastSegment());
				final Collection<HDLEnumRef> allRefs = HDLQuery.select(HDLEnumRef.class).from(subUnit).where(HDLEnumRef.fHEnum).isEqualTo(sqfn).getAll();
				for (final HDLEnumRef ref : allRefs) {
					ms.replace(ref, ref.setHEnum(fqn));
				}
				final Collection<HDLVariableDeclaration> allVars = HDLQuery.select(HDLVariableDeclaration.class).from(subUnit).where(HDLVariableDeclaration.fType).isEqualTo(sqfn)
						.getAll();
				for (final HDLVariableDeclaration hvd : allVars) {
					ms.replace(hvd, hvd.setType(fqn));
				}
			}
			if (decl instanceof HDLFunction) {
				final HDLFunction hdf = (HDLFunction) decl;
				final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hdf);
				pkg = pkg.addDeclarations(hdf.setName(fqn.toString()));
				final HDLQualifiedName sqfn = HDLQualifiedName.create(fqn.getLastSegment());
				final Collection<HDLFunctionCall> allRefs = HDLQuery.select(HDLFunctionCall.class).from(subUnit).where(HDLFunctionCall.fName).isEqualTo(sqfn).getAll();
				for (final HDLFunctionCall ref : allRefs) {
					ms.replace(ref, ref.setName(fqn));
				}
			}
			if (decl instanceof HDLInterfaceDeclaration) {
				final HDLInterfaceDeclaration hid = (HDLInterfaceDeclaration) decl;
				final HDLQualifiedName fqn = FullNameExtension.fullNameOf(hid.getHIf());
				final String newName = fqn.toString();
				pkg = pkg.addDeclarations(hid.setHIf(hid.getHIf().setName(newName)));
				final HDLQualifiedName sfqn = HDLQualifiedName.create(fqn.getLastSegment());
				final Collection<HDLInterfaceInstantiation> allRefs = HDLQuery.select(HDLInterfaceInstantiation.class).from(subUnit).where(HDLInterfaceInstantiation.fHIf)
						.isEqualTo(sfqn).getAll();
				for (final HDLInterfaceInstantiation hii : allRefs) {
					ms.replace(hii, hii.setHIf(fqn));
				}
			}
			ms.remove(decl);
		}
		return pkg;
	}

	private static HDLUnit dereferenceRefs(HDLUnit subUnit) {
		final String subUnitName = FullNameExtension.fullNameOf(subUnit).toString();
		final ModificationSet ms = new ModificationSet();
		final HDLVariableRef[] ref = subUnit.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef varRef : ref) {
			final String string = varRef.getVarRefName().toString();
			if (string.startsWith(subUnitName)) {
				ms.replace(varRef, varRef.setVar(varRef.getVarRefName().getLocalPart()));
			}
		}
		return ms.apply(subUnit);
	}

	private static HDLUnit extractDefaultValue(HDLUnit subUnit) {
		final ModificationSet subMS = new ModificationSet();
		final HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			final HDLDirection dir = hdlVariable.getDirection();
			if ((dir == HDLDirection.CONSTANT) || (dir == HDLDirection.PARAMETER)) {
				continue;
			}
			final HDLExpression defaultValue = hdlVariable.getDefaultValue();
			if (defaultValue != null) {
				final HDLAssignment ass = new HDLAssignment().setLeft(hdlVariable.asHDLRef()).setRight(defaultValue);
				subMS.insertAfter(hdlVariable.getContainer(HDLStatement.class), ass);
			}
			subMS.replace(hdlVariable, hdlVariable.setDefaultValue(null));
		}
		return subMS.apply(subUnit);
	}

	private static HDLUnit changeDirection(HDLUnit subUnit) {
		final ModificationSet subMS = new ModificationSet();
		final HDLVariableDeclaration[] hvds = subUnit.getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds) {
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
		final ModificationSet subMS = new ModificationSet();
		final HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			final ArrayList<HDLExpression> dims = hdlVariable.getDimensions();
			dims.addAll(0, outerDims);
			subMS.replace(hdlVariable, hdlVariable.setDimensions(dims));
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}

	private static HDLUnit addArrayReference(HDLUnit subUnit, ArrayList<HDLExpression> outerDims) {
		final ModificationSet subMS = new ModificationSet();
		final HDLVariableRef[] refs = subUnit.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			final LinkedList<HDLExpression> array = Lists.newLinkedList();
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
		final ModificationSet subMS = new ModificationSet();
		final HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			final String newName = prefix + "_" + hdlVariable.getName();
			Refactoring.renameVariable(hdlVariable, HDLQualifiedName.create(newName), subUnit, subMS);
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}
}
