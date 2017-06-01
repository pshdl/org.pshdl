/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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

import static org.pshdl.model.extensions.FullNameExtension.FULLNAME;
import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.impl.AbstractHDLUnit;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.types.builtIn.HDLGenerators;
import org.pshdl.model.utils.HDLCore;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;
import org.pshdl.model.utils.Insulin;
import org.pshdl.model.utils.ModificationSet;
import org.pshdl.model.utils.services.IPortAdder;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * The class HDLUnit contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLAnnotation&gt; annotations. Can be <code>null</code>.</li>
 * <li>String libURI. Can <b>not</b> be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;String&gt; imports. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; inits. Can be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLStatement&gt; statements. Can be <code>null</code>.</li>
 * <li>Boolean simulation. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLQualifiedName&gt; extend. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnit extends AbstractHDLUnit {
	/**
	 * Constructs a new instance of {@link HDLUnit}
	 *
	 * @param id
	 *            a unique ID for this particular node
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
	 * The accessor for the field annotations which is of type ArrayList&lt;HDLAnnotation&gt;.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>> fAnnotations = new HDLFieldAccess<HDLUnit, ArrayList<HDLAnnotation>>("annotations", HDLAnnotation.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLAnnotation> getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getAnnotations();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLAnnotation> value) {
			if (obj == null) {
				return null;
			}
			return obj.setAnnotations(value);
		}
	};
	/**
	 * The accessor for the field libURI which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fLibURI = new HDLFieldAccess<HDLUnit, String>("libURI", String.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getLibURI();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, String value) {
			if (obj == null) {
				return null;
			}
			return obj.setLibURI(value);
		}
	};
	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLUnit, String> fName = new HDLFieldAccess<HDLUnit, String>("name", String.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public String getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getName();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, String value) {
			if (obj == null) {
				return null;
			}
			return obj.setName(value);
		}
	};
	/**
	 * The accessor for the field imports which is of type ArrayList&lt;String&gt;.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<String>> fImports = new HDLFieldAccess<HDLUnit, ArrayList<String>>("imports", String.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<String> getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getImports();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<String> value) {
			if (obj == null) {
				return null;
			}
			return obj.setImports(value);
		}
	};
	/**
	 * The accessor for the field inits which is of type ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fInits = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("inits", HDLStatement.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getInits();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLStatement> value) {
			if (obj == null) {
				return null;
			}
			return obj.setInits(value);
		}
	};
	/**
	 * The accessor for the field statements which is of type ArrayList&lt;HDLStatement&gt;.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>> fStatements = new HDLFieldAccess<HDLUnit, ArrayList<HDLStatement>>("statements", HDLStatement.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLStatement> getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getStatements();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLStatement> value) {
			if (obj == null) {
				return null;
			}
			return obj.setStatements(value);
		}
	};
	/**
	 * The accessor for the field simulation which is of type Boolean.
	 */
	public static HDLFieldAccess<HDLUnit, Boolean> fSimulation = new HDLFieldAccess<HDLUnit, Boolean>("simulation", Boolean.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public Boolean getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getSimulation();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, Boolean value) {
			if (obj == null) {
				return null;
			}
			return obj.setSimulation(value);
		}
	};
	/**
	 * The accessor for the field extend which is of type ArrayList&lt;HDLQualifiedName&gt;.
	 */
	public static HDLFieldAccess<HDLUnit, ArrayList<HDLQualifiedName>> fExtend = new HDLFieldAccess<HDLUnit, ArrayList<HDLQualifiedName>>("extend", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLQualifiedName> getValue(HDLUnit obj) {
			if (obj == null) {
				return null;
			}
			return obj.getExtendRefName();
		}

		@Override
		public HDLUnit setValue(HDLUnit obj, ArrayList<HDLQualifiedName> value) {
			if (obj == null) {
				return null;
			}
			return obj.setExtend(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (annotations.contains(obj)) {
			return fAnnotations;
		}
		if (libURI == obj) {
			return fLibURI;
		}
		if (name == obj) {
			return fName;
		}
		if (imports.contains(obj)) {
			return fImports;
		}
		if (inits.contains(obj)) {
			return fInits;
		}
		if (statements.contains(obj)) {
			return fStatements;
		}
		if (simulation == obj) {
			return fSimulation;
		}
		if (extend.contains(obj)) {
			return fExtend;
		}
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	private HDLInterface unitIF = null;

	public HDLInterface asInterface() {
		if (unitIF != null) {
			return unitIF;
		}
		final HDLQualifiedName fullName = fullNameOf(this);
		unitIF = new HDLInterface().setName(fullName.toString());
		final Map<String, HDLVariableDeclaration> decls = Maps.newLinkedHashMap();
		final HDLVariableDeclaration hvds[] = getAllObjectsOf(HDLVariableDeclaration.class, true);
		for (HDLVariableDeclaration hvd : hvds) {
			if (hvd.getContainer(HDLInterface.class) == null) {
				if (hvd.getPrimitive() == null) {
					final Optional<? extends HDLType> resolveType = hvd.resolveType();
					if (resolveType.isPresent()) {
						final HDLType type = resolveType.get();
						hvd = hvd.setType(FullNameExtension.fullNameOf(type));
					}
				}
				addDecl(HDLObject.asList(hvd), decls);
			}
		}
		final HDLDirectGeneration[] generators = getAllObjectsOf(HDLDirectGeneration.class, true);
		for (final HDLDirectGeneration hdgi : generators) {
			if (hdgi.getInclude()) {
				addDecl(HDLGenerators.getPortAdditions(hdgi), decls);
			}
		}
		final Collection<IPortAdder> portAdder = HDLCore.getAllImplementations(IPortAdder.class);
		for (final IPortAdder iPortAdder : portAdder) {
			addDecl(iPortAdder.getPortAddition(this), decls);
		}
		final HDLExport[] exports = getAllObjectsOf(HDLExport.class, true);
		for (final HDLExport hdlExport : exports) {
			final Optional<ArrayList<HDLInterfaceRef>> variables = hdlExport.resolveVariables();
			if (variables.isPresent()) {
				final List<HDLVariableDeclaration> varDecls = Lists.newArrayList();
				for (final HDLInterfaceRef hirRef : variables.get()) {
					final Optional<HDLVariable> resolveVar = hirRef.resolveVar();
					if (resolveVar.isPresent()) {
						varDecls.add(new HDLVariableDeclaration().addAnnotations(HDLBuiltInAnnotations.exportedSignal.create(null))
								.setVariables(Collections.singleton(resolveVar.get())));
					}
				}
				addDecl(varDecls, decls);
			}
		}
		// TODO export and generic hook for Portadditions
		HDLVariable clk = null, rst = null;
		final Set<HDLRegisterConfig> regConfigs = Sets.newHashSet();
		for (final HDLVariableDeclaration hvd : decls.values()) {
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
				regConfigs.add(hvd.getRegister());
			}
		}
		boolean needDefaultClk = false;
		boolean needDefaultRst = false;
		if (clk == null) {
			if (hasRef(this, HDLRegisterConfig.DEF_CLK)) {
				needDefaultClk = true;
			}
			for (final HDLRegisterConfig config : regConfigs) {
				if ((config.getClk() == null) || hasRef(config, HDLRegisterConfig.DEF_CLK)) {
					needDefaultClk = true;
				}
			}
		}
		if (rst == null) {
			if (hasRef(this, HDLRegisterConfig.DEF_RST)) {
				needDefaultRst = true;
			}
			for (final HDLRegisterConfig config : regConfigs) {
				if ((config.getRst() == null) || hasRef(config, HDLRegisterConfig.DEF_RST)) {
					needDefaultRst = true;
				}
			}
		}
		if (needDefaultClk) {
			addSignal(HDLRegisterConfig.DEF_CLK, HDLBuiltInAnnotations.clock);
		}
		if (needDefaultRst) {
			addSignal(HDLRegisterConfig.DEF_RST, HDLBuiltInAnnotations.reset);
		}
		final ModificationSet ms = new ModificationSet();
		final HDLVariableRef[] refs = unitIF.getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef ref : refs) {
			if (ref.getVarRefName().length == 1) {
				ms.replace(ref, ref.setVar(fullName.append(ref.getVarRefName().getLastSegment())));
			}
		}
		// System.out.println("HDLUnit.asInterface()" + unitIF);
		unitIF = ms.apply(unitIF);
		// unitIF = unitIF.copy().setContainer(this);
		// if (!unitIF.isFrozen()) {
		// }
		unitIF = unitIF.copyDeepFrozen(this);
		unitIF.addMeta(FULLNAME, fullName);
		unitIF.setID(getID());
		return unitIF;
	}

	private void addDecl(Iterable<HDLVariableDeclaration> hvds, Map<String, HDLVariableDeclaration> decls) {
		if (hvds != null) {
			for (final HDLVariableDeclaration hvd : hvds) {
				for (final HDLVariable var : hvd.getVariables()) {
					final String name = var.getName();
					final HDLVariableDeclaration otherDecl = decls.get(name);
					if (otherDecl != null) {
						// This is very likely a re-declaration of an existing
						// variable. It will be indicated as an error
					} else {
						decls.put(name, hvd.setVariables(HDLObject.asList(var)));
					}
				}
			}
		}
	}

	private boolean hasRef(IHDLObject obj, String varName) {
		final boolean hasClkRef = HDLQuery.select(HDLVariableRef.class).from(obj).where(HDLResolvedRef.fVar).lastSegmentIs(varName).getFirst() != null;
		return hasClkRef;
	}

	public HDLVariableDeclaration cleanedVariables(HDLVariableDeclaration hvd) {
		hvd = hvd.setAnnotations(Insulin.filterAnnotations(hvd.getAnnotations()));
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
			var = var.setAnnotations(Insulin.filterAnnotations(var.getAnnotations()));
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
			var = var.setAnnotations(Insulin.filterAnnotations(var.getAnnotations()));
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
