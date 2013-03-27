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
package org.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLIterator.Visit;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;

@SuppressWarnings("all")
public abstract class AbstractHDLUnit extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLUnit}
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
	public AbstractHDLUnit(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String libURI, @Nonnull String name,
			@Nullable Iterable<String> imports, @Nullable Iterable<HDLStatement> inits, @Nullable Iterable<HDLStatement> statements, @Nonnull Boolean simulation,
			@Nullable Iterable<HDLQualifiedName> extend, boolean validate) {
		super(container, validate);
		if (validate) {
			annotations = validateAnnotations(annotations);
		}
		this.annotations = new ArrayList<HDLAnnotation>();
		if (annotations != null) {
			for (HDLAnnotation newValue : annotations) {
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
		this.imports = new ArrayList<String>();
		if (imports != null) {
			for (String newValue : imports) {
				this.imports.add(newValue);
			}
		}
		if (validate) {
			inits = validateInits(inits);
		}
		this.inits = new ArrayList<HDLStatement>();
		if (inits != null) {
			for (HDLStatement newValue : inits) {
				this.inits.add(newValue);
			}
		}
		if (validate) {
			statements = validateStatements(statements);
		}
		this.statements = new ArrayList<HDLStatement>();
		if (statements != null) {
			for (HDLStatement newValue : statements) {
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
		this.extend = new ArrayList<HDLQualifiedName>();
		if (extend != null) {
			for (HDLQualifiedName newValue : extend) {
				this.extend.add(newValue);
			}
		}
	}

	public AbstractHDLUnit() {
		super();
		this.annotations = new ArrayList<HDLAnnotation>();
		this.libURI = null;
		this.name = null;
		this.imports = new ArrayList<String>();
		this.inits = new ArrayList<HDLStatement>();
		this.statements = new ArrayList<HDLStatement>();
		this.simulation = null;
		this.extend = new ArrayList<HDLQualifiedName>();
	}

	@Visit
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
		if (annotations == null)
			return new ArrayList<HDLAnnotation>();
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
		if (libURI == null)
			throw new IllegalArgumentException("The field libURI can not be null!");
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
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
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
		if (imports == null)
			return new ArrayList<String>();
		return imports;
	}

	@Visit
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
		if (inits == null)
			return new ArrayList<HDLStatement>();
		return inits;
	}

	@Visit
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
		if (statements == null)
			return new ArrayList<HDLStatement>();
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
		if (simulation == null)
			throw new IllegalArgumentException("The field simulation can not be null!");
		return simulation;
	}

	@Visit
	protected final ArrayList<HDLQualifiedName> extend;

	@Nullable
	public Optional<HDLInterface> resolveExtend(int index) {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveInterface(this, extend.get(index));
	}

	public ArrayList<HDLQualifiedName> getExtendRefName() {
		return extend;
	}

	protected Iterable<HDLQualifiedName> validateExtend(Iterable<HDLQualifiedName> extend) {
		if (extend == null)
			return new ArrayList<HDLQualifiedName>();
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
		HDLUnit newObject = new HDLUnit(null, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
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
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		String filteredlibURI = filter.copyObject("libURI", this, libURI);
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<String> filteredimports = filter.copyContainer("imports", this, imports);
		ArrayList<HDLStatement> filteredinits = filter.copyContainer("inits", this, inits);
		ArrayList<HDLStatement> filteredstatements = filter.copyContainer("statements", this, statements);
		Boolean filteredsimulation = filter.copyObject("simulation", this, simulation);
		ArrayList<HDLQualifiedName> filteredextend = filter.copyContainer("extend", this, extend);
		return filter.postFilter((HDLUnit) this, new HDLUnit(null, filteredannotations, filteredlibURI, filteredname, filteredimports, filteredinits, filteredstatements,
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
		HDLUnit copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLUnit} with the updated container
	 *         field.
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
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated annotations
	 *         field.
	 */
	@Nonnull
	public HDLUnit setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations
	 *         field.
	 */
	@Nonnull
	public HDLUnit addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations
	 *         field.
	 */
	@Nonnull
	public HDLUnit removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLUnit} with the updated annotations
	 *         field.
	 */
	@Nonnull
	public HDLUnit removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getLibURI()}.
	 * 
	 * @param libURI
	 *            sets the new libURI of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated libURI field.
	 */
	@Nonnull
	public HDLUnit setLibURI(@Nonnull String libURI) {
		libURI = validateLibURI(libURI);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated name field.
	 */
	@Nonnull
	public HDLUnit setName(@Nonnull String name) {
		name = validateName(name);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
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
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getImports()}.
	 * 
	 * @param newImports
	 *            the value that should be added to the field
	 *            {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit addImports(@Nullable String newImports) {
		if (newImports == null)
			throw new IllegalArgumentException("Element of imports can not be null!");
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.add(newImports);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getImports()}.
	 * 
	 * @param newImports
	 *            the value that should be removed from the field
	 *            {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit removeImports(@Nullable String newImports) {
		if (newImports == null)
			throw new IllegalArgumentException("Removed element of imports can not be null!");
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(newImports);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getImports()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getImports()}
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	@Nonnull
	public HDLUnit removeImports(int idx) {
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
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
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getInits()}.
	 * 
	 * @param newInits
	 *            the value that should be added to the field
	 *            {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit addInits(@Nullable HDLStatement newInits) {
		if (newInits == null)
			throw new IllegalArgumentException("Element of inits can not be null!");
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.add(newInits);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getInits()}.
	 * 
	 * @param newInits
	 *            the value that should be removed from the field
	 *            {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit removeInits(@Nullable HDLStatement newInits) {
		if (newInits == null)
			throw new IllegalArgumentException("Removed element of inits can not be null!");
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(newInits);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getInits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getInits()}
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	@Nonnull
	public HDLUnit removeInits(int idx) {
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStatements()}.
	 * 
	 * @param statements
	 *            sets the new statements of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLUnit setStatements(@Nullable Iterable<HDLStatement> statements) {
		statements = validateStatements(statements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be added to the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLUnit addStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.add(newStatements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be removed from the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLUnit removeStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Removed element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(newStatements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLUnit} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLUnit removeStatements(int idx) {
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimulation()}.
	 * 
	 * @param simulation
	 *            sets the new simulation of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated simulation
	 *         field.
	 */
	@Nonnull
	public HDLUnit setSimulation(@Nonnull Boolean simulation) {
		simulation = validateSimulation(simulation);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimulation()}.
	 * 
	 * @param simulation
	 *            sets the new simulation of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated simulation
	 *         field.
	 */
	@Nonnull
	public HDLUnit setSimulation(boolean simulation) {
		simulation = validateSimulation(simulation);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getExtend()}.
	 * 
	 * @param extend
	 *            sets the new extend of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit setExtend(@Nullable Iterable<HDLQualifiedName> extend) {
		extend = validateExtend(extend);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getExtend()}.
	 * 
	 * @param newExtend
	 *            the value that should be added to the field
	 *            {@link #getExtend()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit addExtend(@Nullable HDLQualifiedName newExtend) {
		if (newExtend == null)
			throw new IllegalArgumentException("Element of extend can not be null!");
		ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.add(newExtend);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExtend()}.
	 * 
	 * @param newExtend
	 *            the value that should be removed from the field
	 *            {@link #getExtend()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit removeExtend(@Nullable HDLQualifiedName newExtend) {
		if (newExtend == null)
			throw new IllegalArgumentException("Removed element of extend can not be null!");
		ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.remove(newExtend);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getExtend()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getExtend()}
	 * @return a new instance of {@link HDLUnit} with the updated extend field.
	 */
	@Nonnull
	public HDLUnit removeExtend(int idx) {
		ArrayList<HDLQualifiedName> extend = (ArrayList<HDLQualifiedName>) this.extend.clone();
		extend.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, extend, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLUnit))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLUnit other = (AbstractHDLUnit) obj;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		if (libURI == null) {
			if (other.libURI != null)
				return false;
		} else if (!libURI.equals(other.libURI))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (imports == null) {
			if (other.imports != null)
				return false;
		} else if (!imports.equals(other.imports))
			return false;
		if (inits == null) {
			if (other.inits != null)
				return false;
		} else if (!inits.equals(other.inits))
			return false;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
			return false;
		if (simulation == null) {
			if (other.simulation != null)
				return false;
		} else if (!simulation.equals(other.simulation))
			return false;
		if (extend == null) {
			if (other.extend != null)
				return false;
		} else if (!extend.equals(other.extend))
			return false;
		return true;
	}

	private static Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((annotations == null) ? 0 : annotations.hashCode());
		result = (prime * result) + ((libURI == null) ? 0 : libURI.hashCode());
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
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLUnit()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
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
				for (String o : imports) {
					sb.append(".addImports(");
					sb.append(o);
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (inits != null) {
			if (inits.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : inits) {
					sb.append(".addInits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (statements != null) {
			if (statements.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : statements) {
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
			for (HDLAnnotation o : getAnnotations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateLibURI(getLibURI());
		validateName(getName());
		validateImports(getImports());
		validateInits(getInits());
		if (getInits() != null) {
			for (HDLStatement o : getInits()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateStatements(getStatements());
		if (getStatements() != null) {
			for (HDLStatement o : getStatements()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateSimulation(getSimulation());
		validateExtend(getExtendRefName());
		if (checkResolve && (getExtendRefName() != null)) {
			for (int i = 0; i < getExtendRefName().size(); i++)
				if (!resolveExtend(i).isPresent())
					throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getExtendRefName().get(i)));
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnit, HDLClass.HDLObject);
	}
}