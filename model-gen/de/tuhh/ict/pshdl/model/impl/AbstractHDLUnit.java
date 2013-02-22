package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

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
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLUnit(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @Nonnull String libURI, @Nonnull String name,
			@Nullable ArrayList<String> imports, @Nullable ArrayList<HDLStatement> inits, @Nullable ArrayList<HDLStatement> statements, @Nonnull Boolean simulation,
			boolean validate) {
		super(container, validate);
		if (validate) {
			annotations = validateAnnotations(annotations);
		}
		this.annotations = new ArrayList<HDLAnnotation>();
		if (annotations != null) {
			for (HDLAnnotation newValue : annotations) {
				this.annotations.add((HDLAnnotation) newValue);
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
				this.inits.add((HDLStatement) newValue);
			}
		}
		if (validate) {
			statements = validateStatements(statements);
		}
		this.statements = new ArrayList<HDLStatement>();
		if (statements != null) {
			for (HDLStatement newValue : statements) {
				this.statements.add((HDLStatement) newValue);
			}
		}
		if (validate) {
			simulation = validateSimulation(simulation);
		}
		this.simulation = simulation;
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
	}

	@Visit
	protected final ArrayList<HDLAnnotation> annotations;

	/**
	 * Get the annotations field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLAnnotation> getAnnotations() {
		return (ArrayList<HDLAnnotation>) annotations.clone();
	}

	protected ArrayList<HDLAnnotation> validateAnnotations(ArrayList<HDLAnnotation> annotations) {
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
	public @Nonnull
	String getLibURI() {
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
	public @Nonnull
	String getName() {
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
	public @Nonnull
	ArrayList<String> getImports() {
		return (ArrayList<String>) imports.clone();
	}

	protected ArrayList<String> validateImports(ArrayList<String> imports) {
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
	public @Nonnull
	ArrayList<HDLStatement> getInits() {
		return (ArrayList<HDLStatement>) inits.clone();
	}

	protected ArrayList<HDLStatement> validateInits(ArrayList<HDLStatement> inits) {
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
	public @Nonnull
	ArrayList<HDLStatement> getStatements() {
		return (ArrayList<HDLStatement>) statements.clone();
	}

	protected ArrayList<HDLStatement> validateStatements(ArrayList<HDLStatement> statements) {
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
	public @Nonnull
	Boolean getSimulation() {
		return simulation;
	}

	protected Boolean validateSimulation(Boolean simulation) {
		if (simulation == null)
			throw new IllegalArgumentException("The field simulation can not be null!");
		return simulation;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLUnit copy() {
		HDLUnit newObject = new HDLUnit(null, annotations, libURI, name, imports, inits, statements, simulation, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLUnit copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		String filteredlibURI = filter.copyObject("libURI", this, libURI);
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<String> filteredimports = filter.copyContainer("imports", this, imports);
		ArrayList<HDLStatement> filteredinits = filter.copyContainer("inits", this, inits);
		ArrayList<HDLStatement> filteredstatements = filter.copyContainer("statements", this, statements);
		Boolean filteredsimulation = filter.copyObject("simulation", this, simulation);
		return filter.postFilter((HDLUnit) this, new HDLUnit(null, filteredannotations, filteredlibURI, filteredname, filteredimports, filteredinits, filteredstatements,
				filteredsimulation, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
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
	public @Nonnull
	HDLUnit setContainer(@Nullable IHDLObject container) {
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
	public @Nonnull
	HDLUnit setAnnotations(@Nullable ArrayList<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit setLibURI(@Nonnull String libURI) {
		libURI = validateLibURI(libURI);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit setName(@Nonnull String name) {
		name = validateName(name);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getImports()}.
	 * 
	 * @param imports
	 *            sets the new imports of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated imports field.
	 */
	public @Nonnull
	HDLUnit setImports(@Nullable ArrayList<String> imports) {
		imports = validateImports(imports);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit addImports(@Nullable String newImports) {
		if (newImports == null)
			throw new IllegalArgumentException("Element of imports can not be null!");
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.add(newImports);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeImports(@Nullable String newImports) {
		if (newImports == null)
			throw new IllegalArgumentException("Removed element of imports can not be null!");
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(newImports);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeImports(int idx) {
		ArrayList<String> imports = (ArrayList<String>) this.imports.clone();
		imports.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getInits()}.
	 * 
	 * @param inits
	 *            sets the new inits of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLUnit} with the updated inits field.
	 */
	public @Nonnull
	HDLUnit setInits(@Nullable ArrayList<HDLStatement> inits) {
		inits = validateInits(inits);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit addInits(@Nullable HDLStatement newInits) {
		if (newInits == null)
			throw new IllegalArgumentException("Element of inits can not be null!");
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.add(newInits);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeInits(@Nullable HDLStatement newInits) {
		if (newInits == null)
			throw new IllegalArgumentException("Removed element of inits can not be null!");
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(newInits);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeInits(int idx) {
		ArrayList<HDLStatement> inits = (ArrayList<HDLStatement>) this.inits.clone();
		inits.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit setStatements(@Nullable ArrayList<HDLStatement> statements) {
		statements = validateStatements(statements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit addStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.add(newStatements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Removed element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(newStatements);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit removeStatements(int idx) {
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(idx);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit setSimulation(@Nonnull Boolean simulation) {
		simulation = validateSimulation(simulation);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
	public @Nonnull
	HDLUnit setSimulation(boolean simulation) {
		simulation = validateSimulation(simulation);
		HDLUnit res = new HDLUnit(container, annotations, libURI, name, imports, inits, statements, simulation, false);
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
		hashCache = result;
		return result;
	}

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
		return sb.toString();
	}

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
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLUnit, HDLClass.HDLObject);
	}
}