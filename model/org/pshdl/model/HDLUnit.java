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

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.impl.*;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
	};
	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null)
			return unitIF;
		HDLQualifiedName fullName = fullNameOf(this);
		unitIF = new HDLInterface().setName(fullName.toString());
		Collection<HDLVariableDeclaration> declarations = new LinkedList<HDLVariableDeclaration>();
		HDLVariableDeclaration hvds[] = getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds)
			if (hvd.getContainer(HDLInterface.class) == null) {
				declarations.add(hvd);
			}
		HDLDirectGeneration[] generators = getAllObjectsOf(HDLDirectGeneration.class, true);
		for (HDLDirectGeneration hdgi : generators)
			if (hdgi.getInclude()) {
				List<HDLVariableDeclaration> portAdditions = HDLGenerators.getPortAdditions(hdgi);
				if (portAdditions != null) {
					declarations.addAll(portAdditions);
				}
			}
		HDLVariable clk = null, rst = null;
		boolean hasReg = false;
		for (HDLVariableDeclaration hvd : declarations) {
			switch (hvd.getDirection()) {
			case PARAMETER:
			case CONSTANT:
			case IN:
			case INOUT:
			case OUT:
				unitIF = unitIF.addPorts(hvd.copyFiltered(CopyFilter.DEEP_META));
				break;
			default:
				break;
			}
			for (HDLAnnotation hdla : hvd.getAnnotations()) {
				if (HDLBuiltInAnnotations.clock.is(hdla)) {
					clk = hvd.getVariables().get(0);
				}
				if (HDLBuiltInAnnotations.reset.is(hdla)) {
					rst = hvd.getVariables().get(0);
				}
			}
			for (HDLVariable var : hvd.getVariables()) {
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
		if (hasReg)
			if ((clk == null) && (rst == null)) {
				unitIF = unitIF.addPorts(new HDLVariableDeclaration().setDirection(HDLDirection.IN).setType(HDLPrimitive.getBit()) //
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_CLK.substring(1)))//
						.addVariables(new HDLVariable().setName(HDLRegisterConfig.DEF_RST.substring(1))));
			}
		ModificationSet ms = new ModificationSet();
		HDLVariableRef[] refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (HDLVariableRef ref : refs)
			if (ref.getVarRefName().length == 1) {
				ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
			}
		// System.out.println("HDLUnit.asInterface()" + unitIF);
		unitIF = ms.apply(unitIF);
		if (!unitIF.isFrozen()) {
			unitIF.freeze(this);
		}
		unitIF.addMeta(FULLNAME, fullName);
		unitIF.setID(getID());
		return unitIF;
	}

	@Override
	public HDLLibrary getLibrary() {
		return HDLLibrary.getLibrary(libURI);

	}

	// $CONTENT-END$

}
