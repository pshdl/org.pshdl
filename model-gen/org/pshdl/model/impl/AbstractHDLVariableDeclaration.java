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
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;
import com.google.common.collect.*;

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
	public AbstractHDLVariableDeclaration(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nullable HDLRegisterConfig register,
			@Nullable HDLDirection direction, @Nonnull HDLQualifiedName type, @Nullable HDLPrimitive primitive, @Nonnull Iterable<HDLVariable> variables, boolean validate) {
		super(id, container, annotations, validate);
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
			for (final HDLVariable newValue : variables) {
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
		final HDLVariableDeclaration newObject = new HDLVariableDeclaration(id, null, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final HDLRegisterConfig filteredregister = filter.copyObject("register", this, register);
		final HDLDirection filtereddirection = filter.copyObject("direction", this, direction);
		final HDLQualifiedName filteredtype = filter.copyObject("type", this, type);
		final HDLPrimitive filteredprimitive = filter.copyObject("primitive", this, primitive);
		final ArrayList<HDLVariable> filteredvariables = filter.copyContainer("variables", this, variables);
		return filter.postFilter((HDLVariableDeclaration) this, new HDLVariableDeclaration(id, null, filteredannotations, filteredregister, filtereddirection, filteredtype,
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
		final HDLVariableDeclaration copy = copyFiltered(CopyFilter.DEEP_META);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.add(newVariables);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.remove(newVariables);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final ArrayList<HDLVariable> variables = (ArrayList<HDLVariable>) this.variables.clone();
		variables.remove(idx);
		final HDLVariableDeclaration res = new HDLVariableDeclaration(id, container, annotations, register, direction, type, primitive, variables, false);
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
		final AbstractHDLVariableDeclaration other = (AbstractHDLVariableDeclaration) obj;
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

	private Integer hashCache;

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
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLVariableDeclaration()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLAnnotation o : annotations) {
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
				for (final HDLVariable o : variables) {
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
			for (final HDLVariable o : getVariables()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLVariableDeclaration, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if (register != null) {
							current = Iterators.concat(Iterators.forArray(register), register.deepIterator());
						}
						break;
					case 2:
						if (primitive != null) {
							current = Iterators.concat(Iterators.forArray(primitive), primitive.deepIterator());
						}
						break;
					case 3:
						if ((variables != null) && (variables.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(variables.size());
							for (final HDLVariable o : variables) {
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
						if (register != null) {
							current = Iterators.singletonIterator(register);
						}
						break;
					case 2:
						if (primitive != null) {
							current = Iterators.singletonIterator(primitive);
						}
						break;
					case 3:
						if ((variables != null) && (variables.size() != 0)) {
							current = variables.iterator();
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