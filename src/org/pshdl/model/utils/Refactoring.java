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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.FullNameExtension;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Refactoring {

	public static interface ISideEffecResolver {
		void resolveSideEffectExpression(HDLExpression exp, HDLReference ref, ModificationSet ms);

		void resolveSideEffectStatement(HDLStatement ass, HDLReference ref, ModificationSet ms);
	}

	public HDLUnit removeVariable(HDLUnit obj, HDLVariable hdlVariable, ISideEffecResolver resolver) {
		final ModificationSet ms = new ModificationSet();
		final HDLClass classType = hdlVariable.getContainer().getClassType();
		Iterable<? extends HDLReference> refs;
		switch (classType) {
		case HDLEnum:
			refs = HDLQuery.getEnumRefs(obj, hdlVariable);
			break;
		case HDLVariableDeclaration:
		case HDLFunctionParameter:
		case HDLForLoop:
			refs = HDLQuery.getVarRefs(obj, hdlVariable);
			break;
		case HDLDirectGeneration:
		case HDLInterfaceInstantiation:
			refs = HDLQuery.getInterfaceRefs(obj, hdlVariable);
			break;
		default:
			throw new RuntimeException("Did not expect a container of type:" + classType);
		}
		for (final HDLReference ref : refs) {
			final IHDLObject container = ref.getContainer();
			if (container instanceof HDLExpression) {
				final HDLExpression exp = (HDLExpression) container;
				resolver.resolveSideEffectExpression(exp, ref, ms);
			}
			if (container instanceof HDLStatement) {
				final HDLStatement stmnt = (HDLStatement) container;
				resolver.resolveSideEffectStatement(stmnt, ref, ms);
			}
		}
		return ms.apply(obj);
	}

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

	public static HDLPackage inlineUnit(HDLUnit container, HDLInterfaceInstantiation hi, HDLUnit subUnit, char separator) {
		final HDLVariable hiVar = hi.getVar();
		final String prefix = hiVar.getName();
		final ModificationSet ms = new ModificationSet();
		HDLPackage pkg = createHDLPackage(subUnit, ms);
		subUnit = ms.apply(subUnit);
		subUnit = prefixVariables(subUnit, prefix, separator);
		subUnit = extractDefaultValue(subUnit);
		final ArrayList<HDLExpression> outerDims = hiVar.getDimensions();
		final List<HDLQualifiedName> iterNames = Lists.newArrayListWithCapacity(outerDims.size());
		for (int i = 0; i < outerDims.size(); i++) {
			iterNames.add(new HDLQualifiedName(Insulin.getTempName("inline", "idx")));
		}
		subUnit = addArrayReference(subUnit, outerDims, iterNames);
		subUnit = addNewDimensions(subUnit, outerDims);
		subUnit = changeDirection(subUnit);
		subUnit = dereferenceRefs(subUnit);
		subUnit = Insulin.generateClkAndReset(subUnit);

		final ModificationSet res = new ModificationSet();
		final Collection<HDLInterfaceRef> ifRefs = HDLQuery.getInterfaceRefs(container, hiVar);
		for (final HDLInterfaceRef hir : ifRefs) {
			final HDLQualifiedName newName = HDLQualifiedName.create(prefix + separator + hir.getVarRefName().getLastSegment());
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
		res.replace(hi, allStatements.toArray(new HDLStatement[allStatements.size()]));
		HDLUnit apply = res.apply(container);
		for (int i = 0; i < iterNames.size(); i++) {
			final HDLQualifiedName iterName = iterNames.get(i);
			final HDLVariable loopName = new HDLVariable().setName(iterName.toString());
			final HDLExpression loopTarget = outerDims.get(i);
			final HDLRange range = new HDLRange().setFrom(HDLLiteral.get(0)).setTo(new HDLArithOp().setLeft(loopTarget).setType(HDLArithOpType.MINUS).setRight(HDLLiteral.get(1)));
			final HDLUnit derefed = dereferenceRefs(apply);
			final List<HDLStatement> combined = derefed.getInits();
			combined.addAll(derefed.getStatements());
			final List<HDLStatement> newStmnts = Lists.newArrayList();
			for (final Iterator<HDLStatement> iterator = combined.iterator(); iterator.hasNext();) {
				final HDLStatement hdlStatement = iterator.next();
				if (hdlStatement instanceof HDLVariableDeclaration) {
					final HDLVariableDeclaration hvd = (HDLVariableDeclaration) hdlStatement;
					if ((hvd.getDirection() == HDLDirection.CONSTANT) || (hvd.getDirection() == HDLDirection.PARAMETER)) {
						iterator.remove();
						newStmnts.add(hvd);
					}
				}
			}
			final HDLForLoop loop = new HDLForLoop().setParam(loopName).addRange(range).setDos(combined);
			newStmnts.add(loop);
			apply = apply.setInits(null).setStatements(newStmnts).copyDeepFrozen(container.getContainer());
		}
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
				ms.replace(varRef, varRef.setVar(new HDLQualifiedName(varRef.getVarRefName().getLastSegment())));
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

	private static HDLUnit addArrayReference(HDLUnit subUnit, ArrayList<HDLExpression> outerDims, List<HDLQualifiedName> iteratorNames) {
		final ModificationSet subMS = new ModificationSet();
		final HDLVariableRef[] refs = subUnit.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			final LinkedList<HDLExpression> array = Lists.newLinkedList();
			for (int i = 0; i < outerDims.size(); i++) {
				array.add(new HDLVariableRef().setVar(iteratorNames.get(i)));
			}
			array.addAll(ref.getArray());
			subMS.replace(ref, ref.setArray(array));
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}

	private static HDLUnit prefixVariables(HDLUnit subUnit, String prefix, char separator) {
		final ModificationSet subMS = new ModificationSet();
		final HDLVariable[] vars = subUnit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			final String newName = prefix + separator + hdlVariable.getName();
			Refactoring.renameVariable(hdlVariable, HDLQualifiedName.create(newName), subUnit, subMS);
		}
		subUnit = subMS.apply(subUnit);
		return subUnit;
	}
}
