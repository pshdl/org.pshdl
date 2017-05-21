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
package org.pshdl.model.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLUnit extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLUnit}
	 *
	 * @param id
	 *            a unique number for each instance
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
	public AbstractHDLUnit(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String libURI, @Nonnull String name,
			@Nullable Iterable<String> imports, @Nullable Iterable<HDLStatement> inits, @Nullable Iterable<HDLStatement> statements, @Nonnull Boolean simulation,
			@Nullable Iterable<HDLQualifiedName> extend, boolean validate) {
		super(id, container, validate);
		if (validate) {
			annotations = validateAnnotations(annotations);
		}
		this.annotations = new ArrayList<>();
		if (annotations != null) {
			for (final HDLAnnotation newValue : annotations) {
				this.annotations.add(newValue);
			}
		}
		if (validate) {
			libURI = validateLibURI(libURI);
		}
		this.libURI = libURI;
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			imports = validateImports(imports);
		}
		this.imports = new ArrayList<>();
		if (imports != null) {
			for (final String newValue : imports) {
				this.imports.add(newValue);
			}
		}
		if (validate) {
			inits = validateInits(inits);
		}
		this.inits = new ArrayList<>();
		if (inits != null) {
			for (final HDLStatement newValue : inits) {
				this.inits.add(newValue);
			}
		}
		if (validate) {
			statements = validateStatements(statements);
		}
		this.statements = new ArrayList<>();
		if (statements != null) {
			for (final HDLStatement newValue : statements) {
				this.statements.add(newValue);
			}
		}
		if (validate) {
			simulation = validateSimulation(simulation);
		}
		this.simulation = simulation;
		if (validate) {
			extend = validateExtend(extend);
		}
		this.extend = new ArrayList<>();
		if (extend != null) {
			for (final HDLQualifiedName newValue : extend) {
				this.extend.add(newValue);
			}
		}
	}

	public AbstractHDLUnit() {
		super();
		this.annotations = new ArrayList<>();
		this.libURI = null;
		this.name = null;
		this.imports = new ArrayList<>();
		this.inits = new ArrayList<>();
		this.statements = new ArrayList<>();
		this.simulation = null;
		this.extend = new ArrayList<>();
	}

	protected final ArrayList<HDLAnnotation> annotations;

	/**
	 * Get the annotations field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLAnnotation> getAnnotations() {
		return (ArrayList<HDLAnnotation>) annotations.clone();
	}

	protected Iterable<HDLAnnotation> validateAnnotations(Iterable<HDLAnnotation> annotations) {
		if (annotations == null) {
			return new ArrayList<>();
		}
		return annotations;
	}

	protected final String libURI;

	/**
	 * Get the libURI field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getLibURI() {
		return libURI;
	}

	protected String validateLibURI(String libURI) {
		if (libURI == null) {
			throw new IllegalArgumentException("The field libURI can not be null!");
		}
		return libURI;
	}

	protected final String name;

	/**
	 * Get the name field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	protected String validateName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("The field name can not be null!");
		}
		return name;
	}

	protected final ArrayList<String> imports;

	/**
	 * Get the imports field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<String> getImports() {
		return (ArrayList<String>) imports.clone();
	}

	protected Iterable<String> validateImports(Iterable<String> imports) {
		if (imports == null) {
			return new ArrayList<>();
		}
		return imports;
	}

	protected final ArrayList<HDLStatement> inits;

	/**
	 * Get the inits field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getInits() {
		return (ArrayList<HDLStatement>) inits.clone();
	}

	protected Iterable<HDLStatement> validateInits(Iterable<HDLStatement> inits) {
		if (inits == null) {
			return new ArrayList<>();
		}
		return inits;
	}

	protected final ArrayList<HDLStatement> statements;

	/**
	 * Get the statements field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getStatements() {
		return (ArrayList<HDLStatement>) statements.clone();
	}

	protected Iterable<HDLStatement> validateStatements(Iterable<HDLStatement> statements) {
		if (statements == null) {
			return new ArrayList<>();
		}
		return statements;
	}

	protected final Boolean simulation;

	/**
	 * Get the simulation field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public Boolean getSimulation() {
		return simulation;
	}

	protected Boolean validateSimulation(Boolean simulation) {
		if (simulation == null) {
			throw new IllegalArgumentException("The field simulation can not be null!");
		}
		return simulation;
	}

	protected final ArrayList<HDLQualifiedName> extend;

	public HDLInterface resolveExtendForced(int index, String stage) {
		final Optional<HDLInterface> opt = resolveExtend(index);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new HDLCodeGenerationException(this, "failed to resolve:" + extend.get(index), stage);
	}

	public Optional<HDLInterface> resolveExtend(int index) {
		if (!frozen) {
			throw new IllegalArgumentException("Object not frozen");
		}
		return ScopingExtension.INST.resolveInterface(this, extend.get(index));
	}

	public ArrayList<HDLQualifiedName> getExtendRefName() {
		return extend;
	}

	protected Iterable<HDLQualifiedName> validateExtend(Iterable<HDLQualifiedName> extend) {
		if (extend == null) {
			return new ArrayList<>();
		}
		return extend;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnit copy() {
		final HDLUnit newObject = new HDLUnit(id, null, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnit copyFiltered(CopyFilter filter) {
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final String filteredlibURI = filter.copyObject("libURI", this, libURI);
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<String> filteredimports = filter.copyContainer("imports", this, imports);
		final ArrayList<HDLStatement> filteredinits = filter.copyContainer("inits", this, inits);
		final ArrayList<HDLStatement> filteredstatements = filter.copyContainer("statements", this, statements);
		final Boolean filteredsimulation = filter.copyObject("simulation", this, simulation);
		final ArrayList<HDLQualifiedName> filteredextend = filter.copyContainer("extend", this, extend);
		return filter.postFilter((HDLUnit) this, new HDLUnit(id, null, filteredannotations, filteredlibURI, filteredname, filteredimports, filteredinits, filteredstatements,
				filteredsimulation, filteredextend, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLUnit copyDeepFrozen(IHDLObject container) {
		final HDLUnit copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLUnit} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLUnit setContainer(@Nullable IHDLObject container) {
		return (HDLUnit) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 *
	 * @param annotations
	 *            sets the new annotations of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated annotations field.
	 */
	@Nonnull
	public HDLUnit setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be added to the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations field.
	 */
	@Nonnull
	public HDLUnit addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null) {
			throw new IllegalArgumentException("Element of annotations can not be null!");
		}
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations field.
	 */
	@Nonnull
	public HDLUnit removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null) {
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		}
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations field.
	 */
	@Nonnull
	public HDLUnit removeAnnotations(int idx) {
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getLibURI()}.
	 *
	 * @param libURI
	 *            sets the new libURI of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated libURI field.
	 */
	@Nonnull
	public HDLUnit setLibURI(@Nonnull String libURI) {
		libURI = validateLibURI(libURI);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 *
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated name field.
	 */
	@Nonnull
	public HDLUnit setName(@Nonnull String name) {
		name = validateName(name);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getImports()}.
	 *
	 * @param imports
	 *            sets the new imports of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit setImports(@Nullable Iterable<String> imports) {
		imports = validateImports(imports);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getImports()}.
	 *
	 * @param newImports
	 *            the value that should be added to the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit addImports(@Nullable String newImports) {
		if (newImports == null) {
			throw new IllegalArgumentException("Element of imports can not be null!");
		}
		final ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.add(newImports);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getImports()}.
	 *
	 * @param newImports
	 *            the value that should be removed from the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit removeImports(@Nullable String newImports) {
		if (newImports == null) {
			throw new IllegalArgumentException("Removed element of imports can not be null!");
		}
		final ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(newImports);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getImports()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit removeImports(int idx) {
		final ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(idx);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getInits()}.
	 *
	 * @param inits
	 *            sets the new inits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit setInits(@Nullable Iterable<HDLStatement> inits) {
		inits = validateInits(inits);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getInits()}.
	 *
	 * @param newInits
	 *            the value that should be added to the field {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit addInits(@Nullable HDLStatement newInits) {
		if (newInits == null) {
			throw new IllegalArgumentException("Element of inits can not be null!");
		}
		final ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.add(newInits);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getInits()}.
	 *
	 * @param newInits
	 *            the value that should be removed from the field {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit removeInits(@Nullable HDLStatement newInits) {
		if (newInits == null) {
			throw new IllegalArgumentException("Removed element of inits can not be null!");
		}
		final ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(newInits);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getInits()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit removeInits(int idx) {
		final ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(idx);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStatements()}.
	 *
	 * @param statements
	 *            sets the new statements of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */
	@Nonnull
	public HDLUnit setStatements(@Nullable Iterable<HDLStatement> statements) {
		statements = validateStatements(statements);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getStatements()}.
	 *
	 * @param newStatements
	 *            the value that should be added to the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */
	@Nonnull
	public HDLUnit addStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null) {
			throw new IllegalArgumentException("Element of statements can not be null!");
		}
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.add(newStatements);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 *
	 * @param newStatements
	 *            the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */
	@Nonnull
	public HDLUnit removeStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null) {
			throw new IllegalArgumentException("Removed element of statements can not be null!");
		}
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(newStatements);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements field.
	 */
	@Nonnull
	public HDLUnit removeStatements(int idx) {
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(idx);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimulation()}.
	 *
	 * @param simulation
	 *            sets the new simulation of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated simulation field.
	 */
	@Nonnull
	public HDLUnit setSimulation(@Nonnull Boolean simulation) {
		simulation = validateSimulation(simulation);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimulation()}.
	 *
	 * @param simulation
	 *            sets the new simulation of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated simulation field.
	 */
	@Nonnull
	public HDLUnit setSimulation(boolean simulation) {
		simulation = validateSimulation(simulation);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getExtendRefName()}.
	 *
	 * @param extend
	 *            sets the new extend of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit setExtend(@Nullable Iterable<HDLQualifiedName> extend) {
		extend = validateExtend(extend);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getExtendRefName()}.
	 *
	 * @param newExtend
	 *            the value that should be added to the field {@link #getExtendRefName()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit addExtend(@Nullable HDLQualifiedName newExtend) {
		if (newExtend == null) {
			throw new IllegalArgumentException("Element of extend can not be null!");
		}
		final ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.add(newExtend);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExtendRefName()}.
	 *
	 * @param newExtend
	 *            the value that should be removed from the field {@link #getExtendRefName()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit removeExtend(@Nullable HDLQualifiedName newExtend) {
		if (newExtend == null) {
			throw new IllegalArgumentException("Removed element of extend can not be null!");
		}
		final ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.remove(newExtend);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExtendRefName()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getExtendRefName()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit removeExtend(int idx) {
		final ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.remove(idx);
		final HDLUnit res = new HDLUnit(id, container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractHDLUnit)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLUnit other = (AbstractHDLUnit) obj;
		if (annotations == null) {
			if (other.annotations != null) {
				return false;
			}
		} else if (!annotations.equals(other.annotations)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (imports == null) {
			if (other.imports != null) {
				return false;
			}
		} else if (!imports.equals(other.imports)) {
			return false;
		}
		if (inits == null) {
			if (other.inits != null) {
				return false;
			}
		} else if (!inits.equals(other.inits)) {
			return false;
		}
		if (statements == null) {
			if (other.statements != null) {
				return false;
			}
		} else if (!statements.equals(other.statements)) {
			return false;
		}
		if (simulation == null) {
			if (other.simulation != null) {
				return false;
			}
		} else if (!simulation.equals(other.simulation)) {
			return false;
		}
		if (extend == null) {
			if (other.extend != null) {
				return false;
			}
		} else if (!extend.equals(other.extend)) {
			return false;
		}
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null) {
			return hashCache;
		}
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((annotations == null) ? 0 : annotations.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((imports == null) ? 0 : imports.hashCode());
		result = (prime * result) + ((inits == null) ? 0 : inits.hashCode());
		result = (prime * result) + ((statements == null) ? 0 : statements.hashCode());
		result = (prime * result) + ((simulation == null) ? 0 : simulation.hashCode());
		result = (prime * result) + ((extend == null) ? 0 : extend.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnit()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (libURI != null) {
			sb.append(".setLibURI(").append('"' + libURI + '"').append(")");
		}
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (imports != null) {
			if (imports.size() > 0) {
				sb.append('\n').append(spacing);
				for (final String o : imports) {
					sb.append(".addImports(");
					sb.append(o);
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (inits != null) {
			if (inits.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : inits) {
					sb.append(".addInits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (statements != null) {
			if (statements.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : statements) {
					sb.append(".addStatements(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (simulation != null) {
			sb.append(".setSimulation(").append(simulation).append(")");
		}
		if (extend != null) {
			sb.append(".setExtend(HDLQualifiedName.create(\"").append(extend).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateAnnotations(getAnnotations());
		if (getAnnotations() != null) {
			for (final HDLAnnotation o : getAnnotations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateLibURI(getLibURI());
		validateName(getName());
		validateImports(getImports());
		validateInits(getInits());
		if (getInits() != null) {
			for (final HDLStatement o : getInits()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateStatements(getStatements());
		if (getStatements() != null) {
			for (final HDLStatement o : getStatements()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateSimulation(getSimulation());
		validateExtend(getExtendRefName());
		if (checkResolve && (getExtendRefName() != null)) {
			for (int i = 0; i < getExtendRefName().size(); i++) {
				if (!resolveExtend(i).isPresent()) {
					throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getExtendRefName().get(i)));
				}
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnit, HDLClass.HDLObject);
	}

	@Override
	public Iterator<IHDLObject> deepIterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if ((annotations != null) && (annotations.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(annotations.size());
							for (final HDLAnnotation o : annotations) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if ((inits != null) && (inits.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(inits.size());
							for (final HDLStatement o : inits) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 2:
						if ((statements != null) && (statements.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(statements.size());
							for (final HDLStatement o : statements) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}

	@Override
	public Iterator<IHDLObject> iterator() {
		return new Iterator<IHDLObject>() {

			private int pos = 0;
			private Iterator<? extends IHDLObject> current;

			@Override
			public boolean hasNext() {
				if ((current != null) && !current.hasNext()) {
					current = null;
				}
				while (current == null) {
					switch (pos++) {
					case 0:
						if ((annotations != null) && (annotations.size() != 0)) {
							current = annotations.iterator();
						}
						break;
					case 1:
						if ((inits != null) && (inits.size() != 0)) {
							current = inits.iterator();
						}
						break;
					case 2:
						if ((statements != null) && (statements.size() != 0)) {
							current = statements.iterator();
						}
						break;
					default:
						return false;
					}
				}
				return (current != null) && current.hasNext();
			}

			@Override
			public IHDLObject next() {
				return current.next();
			}

			@Override
			public void remove() {
				throw new IllegalArgumentException("Not supported");
			}

		};
	}
}