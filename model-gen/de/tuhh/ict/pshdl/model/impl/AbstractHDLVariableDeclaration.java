package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.extensions.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

@SuppressWarnings("all")
public abstract class AbstractHDLVariableDeclaration extends HDLDeclaration {
	/**
	 * Constructs a new instance of {@link AbstractHDLVariableDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param register
	 *            the value for register. Can be <code>null</code>.
	 * @param direction
	 *            the value for direction. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param primitive
	 *            the value for primitive. Can be <code>null</code>.
	 * @param variables
	 *            the value for variables. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLVariableDeclaration(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nullable HDLRegisterConfig register,
			@Nullable HDLDirection direction, @Nonnull HDLQualifiedName type, @Nullable HDLPrimitive primitive, @Nonnull Iterable<HDLVariable> variables, boolean validate) {
		super(container, annotations, validate);
		if (validate) {
			register = validateRegister(register);
		}
		if (register != null) {
			this.register = register;
		} else {
			this.register = null;
		}
		if (validate) {
			direction = validateDirection(direction);
		}
		this.direction = direction;
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			primitive = validatePrimitive(primitive);
		}
		if (primitive != null) {
			this.primitive = primitive;
		} else {
			this.primitive = null;
		}
		if (validate) {
			variables = validateVariables(variables);
		}
		this.variables = new ArrayList<HDLVariable>();
		if (variables != null) {
			for (HDLVariable newValue : variables) {
				this.variables.add(newValue);
			}
		}
	}

	public AbstractHDLVariableDeclaration() {
		super();
		this.register = null;
		this.direction = null;
		this.type = null;
		this.primitive = null;
		this.variables = new ArrayList<HDLVariable>();
	}

	@Visit
	protected final HDLRegisterConfig register;

	/**
	 * Get the register field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLRegisterConfig getRegister() {
		return register;
	}

	protected HDLRegisterConfig validateRegister(HDLRegisterConfig register) {
		return register;
	}

	protected final HDLDirection direction;

	/**
	 * Get the direction field. If <code>null</code>,
	 * {@link HDLDirection#INTERNAL} is used as default.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLDirection getDirection() {
		return direction == null ? HDLDirection.INTERNAL : direction;
	}

	protected HDLDirection validateDirection(HDLDirection direction) {
		return direction == null ? HDLDirection.INTERNAL : direction;
	}

	protected final HDLQualifiedName type;

	@Nullable
	public Optional<? extends HDLType> resolveType() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveType(this, type);
	}

	public HDLQualifiedName getTypeRefName() {
		return type;
	}

	protected HDLQualifiedName validateType(HDLQualifiedName type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

	@Visit
	protected final HDLPrimitive primitive;

	/**
	 * Get the primitive field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLPrimitive getPrimitive() {
		return primitive;
	}

	protected HDLPrimitive validatePrimitive(HDLPrimitive primitive) {
		return primitive;
	}

	@Visit
	protected final ArrayList<HDLVariable> variables;

	/**
	 * Get the variables field. Can <b>not</b> be <code>null</code>,
	 * additionally the collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLVariable> getVariables() {
		return (ArrayList<HDLVariable>) variables.clone();
	}

	protected Iterable<HDLVariable> validateVariables(Iterable<HDLVariable> variables) {
		if (variables == null)
			throw new IllegalArgumentException("The field variables can not be null!");
		if (!variables.iterator().hasNext())
			throw new IllegalArgumentException("The field variables must contain at least one item!");
		return variables;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration copy() {
		HDLVariableDeclaration newObject = new HDLVariableDeclaration(null, annotations, register, direction, type, primitive, variables, false);
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
	public HDLVariableDeclaration copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		HDLRegisterConfig filteredregister = filter.copyObject("register", this, register);
		HDLDirection filtereddirection = filter.copyObject("direction", this, direction);
		HDLQualifiedName filteredtype = filter.copyObject("type", this, type);
		HDLPrimitive filteredprimitive = filter.copyObject("primitive", this, primitive);
		ArrayList<HDLVariable> filteredvariables = filter.copyContainer("variables", this, variables);
		return filter.postFilter((HDLVariableDeclaration) this, new HDLVariableDeclaration(null, filteredannotations, filteredregister, filtereddirection, filteredtype,
				filteredprimitive, filteredvariables, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration copyDeepFrozen(IHDLObject container) {
		HDLVariableDeclaration copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLVariableDeclaration} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration setContainer(@Nullable IHDLObject container) {
		return (HDLVariableDeclaration) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLVariableDeclaration removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLVariableDeclaration removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getRegister()}.
	 * 
	 * @param register
	 *            sets the new register of this object. Can be <code>null</code>
	 *            .
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         register field.
	 */
	@Nonnull
	public HDLVariableDeclaration setRegister(@Nullable HDLRegisterConfig register) {
		register = validateRegister(register);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDirection()}.
	 * 
	 * @param direction
	 *            sets the new direction of this object. If <code>null</code>,
	 *            {@link HDLDirection#INTERNAL} is used as default.
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         direction field.
	 */
	@Nonnull
	public HDLVariableDeclaration setDirection(@Nullable HDLDirection direction) {
		direction = validateDirection(direction);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         type field.
	 */
	@Nonnull
	public HDLVariableDeclaration setType(@Nonnull HDLQualifiedName type) {
		type = validateType(type);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getPrimitive()}.
	 * 
	 * @param primitive
	 *            sets the new primitive of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         primitive field.
	 */
	@Nonnull
	public HDLVariableDeclaration setPrimitive(@Nullable HDLPrimitive primitive) {
		primitive = validatePrimitive(primitive);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getVariables()}.
	 * 
	 * @param variables
	 *            sets the new variables of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         variables field.
	 */
	@Nonnull
	public HDLVariableDeclaration setVariables(@Nonnull Iterable<HDLVariable> variables) {
		variables = validateVariables(variables);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getVariables()}.
	 * 
	 * @param newVariables
	 *            the value that should be added to the field
	 *            {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         variables field.
	 */
	@Nonnull
	public HDLVariableDeclaration addVariables(@Nonnull HDLVariable newVariables) {
		if (newVariables == null)
			throw new IllegalArgumentException("Element of variables can not be null!");
		ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.add(newVariables);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getVariables()}.
	 * 
	 * @param newVariables
	 *            the value that should be removed from the field
	 *            {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         variables field.
	 */
	@Nonnull
	public HDLVariableDeclaration removeVariables(@Nonnull HDLVariable newVariables) {
		if (newVariables == null)
			throw new IllegalArgumentException("Removed element of variables can not be null!");
		ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.remove(newVariables);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getVariables()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getVariables()}
	 * @return a new instance of {@link HDLVariableDeclaration} with the updated
	 *         variables field.
	 */
	@Nonnull
	public HDLVariableDeclaration removeVariables(int idx) {
		ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.remove(idx);
		HDLVariableDeclaration res = new HDLVariableDeclaration(container, annotations, register, direction, type, primitive, variables, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLVariableDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLVariableDeclaration other = (AbstractHDLVariableDeclaration) obj;
		if (register == null) {
			if (other.register != null)
				return false;
		} else if (!register.equals(other.register))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (primitive == null) {
			if (other.primitive != null)
				return false;
		} else if (!primitive.equals(other.primitive))
			return false;
		if (variables == null) {
			if (other.variables != null)
				return false;
		} else if (!variables.equals(other.variables))
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
		result = (prime * result) + ((register == null) ? 0 : register.hashCode());
		result = (prime * result) + ((direction == null) ? 0 : direction.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((primitive == null) ? 0 : primitive.hashCode());
		result = (prime * result) + ((variables == null) ? 0 : variables.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLVariableDeclaration()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (register != null) {
			sb.append(".setRegister(").append(register.toConstructionString(spacing + "\t")).append(")");
		}
		if (direction != null) {
			sb.append("\n").append(spacing + "\t").append(".setDirection(HDLDirection.").append(direction.name() + ")");
		}
		if (type != null) {
			sb.append(".setType(HDLQualifiedName.create(\"").append(type).append("\"))");
		}
		if (primitive != null) {
			sb.append(".setPrimitive(").append(primitive.toConstructionString(spacing + "\t")).append(")");
		}
		if (variables != null) {
			if (variables.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLVariable o : variables) {
					sb.append(".addVariables(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateRegister(getRegister());
		if (getRegister() != null) {
			getRegister().validateAllFields(this, checkResolve);
		}
		validateDirection(getDirection());
		validateType(getTypeRefName());
		if (checkResolve && (getTypeRefName() != null))
			if (!resolveType().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getTypeRefName()));
		validatePrimitive(getPrimitive());
		if (getPrimitive() != null) {
			getPrimitive().validateAllFields(this, checkResolve);
		}
		validateVariables(getVariables());
		if (getVariables() != null) {
			for (HDLVariable o : getVariables()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLVariableDeclaration, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}