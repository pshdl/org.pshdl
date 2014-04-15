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
package org.pshdl.model;

import static org.pshdl.model.extensions.FullNameExtension.*;

import java.math.*;
import java.util.*;

import javax.annotation.*;

import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.*;
import com.google.common.collect.*;

/**
 * The class HDLUnit contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<String> imports. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> inits. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLStatement> statements. Can be <code>null</code>.</li>
 * <li>Boolean simulation. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLQualifiedName> extend. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param imports
	 *            the value for imports. Can be <code>null</code>.
	 * @param inits
	 *            the value for inits. Can be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param simulation
	 *            the value for simulation. Can <b>not</b> be <code>null</code>.
	 * @param extend
	 *            the value for extend. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLUnit(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String libURI, @Nonnull String name,
			@Nullable Iterable<String> imports, @Nullable Iterable<HDLStatement> inits, @Nullable Iterable<HDLStatement> statements, @Nonnull Boolean simulation,
			@Nullable Iterable<HDLQualifiedName> extend, boolean validate) {
		super(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, validate);
	}

	public HDLUnit() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnit;
	}

	/**
	 * The accessor for the field annotations which is of type
	 * ArrayList<HDLAnnotation>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>>("annotations") {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getAnnotations();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLAnnotation> value) {
			if (obj == null)
				return null;
			return obj.setAnnotations(value);
		}
	};
	/**
	 * The accessor for the field libURI which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fLibURI = new HDLFieldAccess<HDLUnit, String>("libURI") {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getLibURI();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, String value) {
			if (obj == null)
				return null;
			return obj.setLibURI(value);
		}
	};
	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fName = new HDLFieldAccess<HDLUnit, String>("name") {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, String value) {
			if (obj == null)
				return null;
			return obj.setName(value);
		}
	};
	/**
	 * The accessor for the field imports which is of type ArrayList<String>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<String>> fImports = new HDLFieldAccess<HDLUnit, ArrayList<String>>("imports") {
		@Override
		public ArrayList<String> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getImports();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<String> value) {
			if (obj == null)
				return null;
			return obj.setImports(value);
		}
	};
	/**
	 * The accessor for the field inits which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fInits = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("inits") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getInits();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setInits(value);
		}
	};
	/**
	 * The accessor for the field statements which is of type
	 * ArrayList<HDLStatement>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("statements") {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getStatements();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLStatement> value) {
			if (obj == null)
				return null;
			return obj.setStatements(value);
		}
	};
	/**
	 * The accessor for the field simulation which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLUnit, Boolean> fSimulation = new HDLFieldAccess<HDLUnit, Boolean>("simulation") {
		@Override
		public Boolean getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getSimulation();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, Boolean value) {
			if (obj == null)
				return null;
			return obj.setSimulation(value);
		}
	};
	/**
	 * The accessor for the field extend which is of type
	 * ArrayList<HDLQualifiedName>.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLQualifiedName>> fExtend = new HDLFieldAccess<HDLUnit, ArrayList<HDLQualifiedName>>("extend") {
		@Override
		public ArrayList<HDLQualifiedName> getValue(HDLUnit obj) {
			if (obj == null)
				return null;
			return obj.getExtendRefName();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLQualifiedName> value) {
			if (obj == null)
				return null;
			return obj.setExtend(value);
		}
	};
	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		final HDLQualifiedName fullName = fullNameOf(this);
		unitIF = new HDLInterface().setName(fullName.toString());
		final Collection<HDLVariableDeclaration> declarations = new LinkedList<HDLVariableDeclaration>();
		final HDLVariableDeclaration hvds[] = getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (final HDLVariableDeclaration hvd : hvds)
			if (hvd.getContainer(HDLInterface.class) == null) {
				declarations.add(hvd);
			}
		final HDLDirectGeneration[] generators = getAllObjectsOf(HDLDirectGeneration.class, true);
		for (final HDLDirectGeneration hdgi : generators)
			if (hdgi.getInclude()) {
				final List<HDLVariableDeclaration> portAdditions = HDLGenerators.getPortAdditions(hdgi);
				if (portAdditions != null) {
					declarations.addAll(portAdditions);
				}
			}
		HDLVariable clk = null, rst = null;
		boolean hasReg = false;
		for (final HDLVariableDeclaration hvd : declarations) {
			switch (hvd.getDirection()) {
			case PARAMETER:
			case CONSTANT:
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(cleanedVariables(hvd));
				break;
			default:
				break;
			}
			for (final HDLAnnotation hdla : hvd.getAnnotations()) {
				if (HDLBuiltInAnnotations.clock.is(hdla)) {
					clk = hvd.getVariables().get(0);
				}
				if (HDLBuiltInAnnotations.reset.is(hdla)) {
					rst = hvd.getVariables().get(0);
				}
			}
			for (final HDLVariable var : hvd.getVariables()) {
				if (var.getAnnotation(HDLBuiltInAnnotations.clock) != null) {
					clk = var;
				}
				if (var.getAnnotation(HDLBuiltInAnnotations.reset) != null) {
					rst = var;
				}
			}
			if (hvd.getRegister() != null) {
				hasReg = true;
			}
		}
		if (hasReg) {
			if (clk == null) {
				addSignal(HDLRegisterConfig.DEF_CLK, HDLBuiltInAnnotations.clock);
			}
			if (rst == null) {
				addSignal(HDLRegisterConfig.DEF_RST, HDLBuiltInAnnotations.reset);
			}
		} else {
			final boolean hasClkRef = HDLQuery.select(HDLVariableRef.class).from(this).where(HDLResolvedRef.fVar).lastSegmentIs(HDLRegisterConfig.DEF_CLK).getFirst() != null;
			if (hasClkRef && (clk == null)) {
				addSignal(HDLRegisterConfig.DEF_CLK, HDLBuiltInAnnotations.clock);
			}
			final boolean hasRstRef = HDLQuery.select(HDLVariableRef.class).from(this).where(HDLResolvedRef.fVar).lastSegmentIs(HDLRegisterConfig.DEF_RST).getFirst() != null;
			if (hasRstRef && (rst == null)) {
				addSignal(HDLRegisterConfig.DEF_RST, HDLBuiltInAnnotations.reset);
			}
		}
		final ModificationSet ms = new ModificationSet();
		final HDLVariableRef[] refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs)
			if (ref.getVarRefName().length == 1) {
				ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
			}
		// System.out.println("HDLUnit.asInterface()" + unitIF);
		unitIF = ms.apply(unitIF);
		if (!unitIF.isFrozen()) {
			unitIF = unitIF.copyDeepFrozen(this);
		}
		unitIF.addMeta(FULLNAME, fullName);
		unitIF.setID(getID());
		return unitIF;
	}

	public HDLVariableDeclaration cleanedVariables(HDLVariableDeclaration hvd) {
		switch (hvd.getDirection()) {
		case CONSTANT:
		case PARAMETER:
			hvd = hvd.setVariables(withConstantValue(hvd.getVariables()));
			break;
		case IN:
		case INOUT:
		case INTERNAL:
		case OUT:
			hvd = hvd.setVariables(withNullValue(hvd.getVariables()));
			break;
		default:
			break;
		}
		return hvd;
	}

	private Iterable<HDLVariable> withNullValue(ArrayList<HDLVariable> variables) {
		final List<HDLVariable> res = Lists.newArrayListWithCapacity(variables.size());
		for (HDLVariable var : variables) {
			if (var.getDefaultValue() != null) {
				var = var.setDefaultValue(null);
			}
			res.add(var);
		}
		return res;
	}

	private Iterable<HDLVariable> withConstantValue(ArrayList<HDLVariable> variables) {
		final List<HDLVariable> res = Lists.newArrayListWithCapacity(variables.size());
		for (HDLVariable var : variables) {
			if (var.getDefaultValue() != null) {
				final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(var.getDefaultValue());
				if (valueOf.isPresent()) {
					var = var.setDefaultValue(HDLLiteral.get(valueOf.get()));
				}
			}
			res.add(var);
		}
		return res;
	}

	private void addSignal(String sigName, HDLBuiltInAnnotations annotation) {
		unitIF = unitIF.addPorts(new HDLVariableDeclaration().addAnnotations(annotation.create(null)).setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()) //
				.addVariables(new HDLVariable().setName(sigName.substring(1))));
	}

	@Override
	public HDLLibrary getLibrary() {
		return HDLLibrary.getLibrary(libURI);

	}

	// $CONTENT-END$

}
