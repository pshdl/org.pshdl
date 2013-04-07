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
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLFunctionParameter.Type;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLIterator.Visit;
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;

@SuppressWarnings("all")
public abstract class AbstractHDLFunctionParameter extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLFunctionParameter}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param rw
	 *            the value for rw. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param enumSpec
	 *            the value for enumSpec. Can be <code>null</code>.
	 * @param ifSpec
	 *            the value for ifSpec. Can be <code>null</code>.
	 * @param funcSpec
	 *            the value for funcSpec. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLFunctionParameter(@Nullable IHDLObject container, @Nonnull RWType rw, @Nonnull Type type, @Nullable HDLQualifiedName enumSpec,
			@Nullable HDLQualifiedName ifSpec, @Nullable Iterable<HDLFunctionParameter> funcSpec, @Nullable HDLVariable name, @Nonnull Integer dim, boolean validate) {
		super(container, validate);
		if (validate) {
			rw = validateRw(rw);
		}
		this.rw = rw;
		if (validate) {
			type = validateType(type);
		}
		this.type = type;
		if (validate) {
			enumSpec = validateEnumSpec(enumSpec);
		}
		this.enumSpec = enumSpec;
		if (validate) {
			ifSpec = validateIfSpec(ifSpec);
		}
		this.ifSpec = ifSpec;
		if (validate) {
			funcSpec = validateFuncSpec(funcSpec);
		}
		this.funcSpec = new ArrayList<HDLFunctionParameter>();
		if (funcSpec != null) {
			for (HDLFunctionParameter newValue : funcSpec) {
				this.funcSpec.add(newValue);
			}
		}
		if (validate) {
			name = validateName(name);
		}
		if (name != null) {
			this.name = name;
		} else {
			this.name = null;
		}
		if (validate) {
			dim = validateDim(dim);
		}
		this.dim = dim;
	}

	public AbstractHDLFunctionParameter() {
		super();
		this.rw = null;
		this.type = null;
		this.enumSpec = null;
		this.ifSpec = null;
		this.funcSpec = new ArrayList<HDLFunctionParameter>();
		this.name = null;
		this.dim = null;
	}

	protected final RWType rw;

	/**
	 * Get the rw field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public RWType getRw() {
		return rw;
	}

	protected RWType validateRw(RWType rw) {
		if (rw == null)
			throw new IllegalArgumentException("The field rw can not be null!");
		return rw;
	}

	protected final Type type;

	/**
	 * Get the type field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public Type getType() {
		return type;
	}

	protected Type validateType(Type type) {
		if (type == null)
			throw new IllegalArgumentException("The field type can not be null!");
		return type;
	}

	protected final HDLQualifiedName enumSpec;

	@Nullable
	public Optional<HDLEnum> resolveEnumSpec() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveEnum(this, enumSpec);
	}

	public HDLQualifiedName getEnumSpecRefName() {
		return enumSpec;
	}

	protected HDLQualifiedName validateEnumSpec(HDLQualifiedName enumSpec) {
		return enumSpec;
	}

	protected final HDLQualifiedName ifSpec;

	@Nullable
	public Optional<HDLInterface> resolveIfSpec() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveInterface(this, ifSpec);
	}

	public HDLQualifiedName getIfSpecRefName() {
		return ifSpec;
	}

	protected HDLQualifiedName validateIfSpec(HDLQualifiedName ifSpec) {
		return ifSpec;
	}

	@Visit
	protected final ArrayList<HDLFunctionParameter> funcSpec;

	/**
	 * Get the funcSpec field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLFunctionParameter> getFuncSpec() {
		return (ArrayList<HDLFunctionParameter>) funcSpec.clone();
	}

	protected Iterable<HDLFunctionParameter> validateFuncSpec(Iterable<HDLFunctionParameter> funcSpec) {
		if (funcSpec == null)
			return new ArrayList<HDLFunctionParameter>();
		return funcSpec;
	}

	@Visit
	protected final HDLVariable name;

	/**
	 * Get the name field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLVariable getName() {
		return name;
	}

	protected HDLVariable validateName(HDLVariable name) {
		return name;
	}

	protected final Integer dim;

	/**
	 * Get the dim field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public Integer getDim() {
		return dim;
	}

	protected Integer validateDim(Integer dim) {
		if (dim == null)
			throw new IllegalArgumentException("The field dim can not be null!");
		return dim;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLFunctionParameter copy() {
		HDLFunctionParameter newObject = new HDLFunctionParameter(null, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
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
	public HDLFunctionParameter copyFiltered(CopyFilter filter) {
		RWType filteredrw = filter.copyObject("rw", this, rw);
		Type filteredtype = filter.copyObject("type", this, type);
		HDLQualifiedName filteredenumSpec = filter.copyObject("enumSpec", this, enumSpec);
		HDLQualifiedName filteredifSpec = filter.copyObject("ifSpec", this, ifSpec);
		ArrayList<HDLFunctionParameter> filteredfuncSpec = filter.copyContainer("funcSpec", this, funcSpec);
		HDLVariable filteredname = filter.copyObject("name", this, name);
		Integer filtereddim = filter.copyObject("dim", this, dim);
		return filter.postFilter((HDLFunctionParameter) this, new HDLFunctionParameter(null, filteredrw, filteredtype, filteredenumSpec, filteredifSpec, filteredfuncSpec,
				filteredname, filtereddim, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLFunctionParameter copyDeepFrozen(IHDLObject container) {
		HDLFunctionParameter copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLFunctionParameter} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLFunctionParameter setContainer(@Nullable IHDLObject container) {
		return (HDLFunctionParameter) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getRw()}.
	 * 
	 * @param rw
	 *            sets the new rw of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         rw field.
	 */
	@Nonnull
	public HDLFunctionParameter setRw(@Nonnull RWType rw) {
		rw = validateRw(rw);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getType()}.
	 * 
	 * @param type
	 *            sets the new type of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         type field.
	 */
	@Nonnull
	public HDLFunctionParameter setType(@Nonnull Type type) {
		type = validateType(type);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getEnumSpec()}.
	 * 
	 * @param enumSpec
	 *            sets the new enumSpec of this object. Can be <code>null</code>
	 *            .
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         enumSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter setEnumSpec(@Nullable HDLQualifiedName enumSpec) {
		enumSpec = validateEnumSpec(enumSpec);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getIfSpec()}.
	 * 
	 * @param ifSpec
	 *            sets the new ifSpec of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         ifSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter setIfSpec(@Nullable HDLQualifiedName ifSpec) {
		ifSpec = validateIfSpec(ifSpec);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getFuncSpec()}.
	 * 
	 * @param funcSpec
	 *            sets the new funcSpec of this object. Can be <code>null</code>
	 *            .
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         funcSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter setFuncSpec(@Nullable Iterable<HDLFunctionParameter> funcSpec) {
		funcSpec = validateFuncSpec(funcSpec);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getFuncSpec()}.
	 * 
	 * @param newFuncSpec
	 *            the value that should be added to the field
	 *            {@link #getFuncSpec()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         funcSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter addFuncSpec(@Nullable HDLFunctionParameter newFuncSpec) {
		if (newFuncSpec == null)
			throw new IllegalArgumentException("Element of funcSpec can not be null!");
		ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.add(newFuncSpec);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getFuncSpec()}.
	 * 
	 * @param newFuncSpec
	 *            the value that should be removed from the field
	 *            {@link #getFuncSpec()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         funcSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter removeFuncSpec(@Nullable HDLFunctionParameter newFuncSpec) {
		if (newFuncSpec == null)
			throw new IllegalArgumentException("Removed element of funcSpec can not be null!");
		ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.remove(newFuncSpec);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getFuncSpec()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getFuncSpec()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         funcSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter removeFuncSpec(int idx) {
		ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.remove(idx);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         name field.
	 */
	@Nonnull
	public HDLFunctionParameter setName(@Nullable HDLVariable name) {
		name = validateName(name);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDim()}.
	 * 
	 * @param dim
	 *            sets the new dim of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         dim field.
	 */
	@Nonnull
	public HDLFunctionParameter setDim(@Nonnull Integer dim) {
		dim = validateDim(dim);
		HDLFunctionParameter res = new HDLFunctionParameter(container, rw, type, enumSpec, ifSpec, funcSpec, name, dim, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLFunctionParameter))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLFunctionParameter other = (AbstractHDLFunctionParameter) obj;
		if (rw == null) {
			if (other.rw != null)
				return false;
		} else if (!rw.equals(other.rw))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (enumSpec == null) {
			if (other.enumSpec != null)
				return false;
		} else if (!enumSpec.equals(other.enumSpec))
			return false;
		if (ifSpec == null) {
			if (other.ifSpec != null)
				return false;
		} else if (!ifSpec.equals(other.ifSpec))
			return false;
		if (funcSpec == null) {
			if (other.funcSpec != null)
				return false;
		} else if (!funcSpec.equals(other.funcSpec))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (dim == null) {
			if (other.dim != null)
				return false;
		} else if (!dim.equals(other.dim))
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
		result = (prime * result) + ((rw == null) ? 0 : rw.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((enumSpec == null) ? 0 : enumSpec.hashCode());
		result = (prime * result) + ((ifSpec == null) ? 0 : ifSpec.hashCode());
		result = (prime * result) + ((funcSpec == null) ? 0 : funcSpec.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((dim == null) ? 0 : dim.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLFunctionParameter()");
		if (rw != null) {
			sb.append("\n").append(spacing + "\t").append(".setRw(RWType.").append(rw.name() + ")");
		}
		if (type != null) {
			sb.append("\n").append(spacing + "\t").append(".setType(Type.").append(type.name() + ")");
		}
		if (enumSpec != null) {
			sb.append(".setEnumSpec(HDLQualifiedName.create(\"").append(enumSpec).append("\"))");
		}
		if (ifSpec != null) {
			sb.append(".setIfSpec(HDLQualifiedName.create(\"").append(ifSpec).append("\"))");
		}
		if (funcSpec != null) {
			if (funcSpec.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLFunctionParameter o : funcSpec) {
					sb.append(".addFuncSpec(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (name != null) {
			sb.append(".setName(").append(name.toConstructionString(spacing + "\t")).append(")");
		}
		if (dim != null) {
			sb.append(".setDim(").append(dim).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateRw(getRw());
		validateType(getType());
		validateEnumSpec(getEnumSpecRefName());
		if (checkResolve && (getEnumSpecRefName() != null))
			if (!resolveEnumSpec().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getEnumSpecRefName()));
		validateIfSpec(getIfSpecRefName());
		if (checkResolve && (getIfSpecRefName() != null))
			if (!resolveIfSpec().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getIfSpecRefName()));
		validateFuncSpec(getFuncSpec());
		if (getFuncSpec() != null) {
			for (HDLFunctionParameter o : getFuncSpec()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateName(getName());
		if (getName() != null) {
			getName().validateAllFields(this, checkResolve);
		}
		validateDim(getDim());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLFunctionParameter, HDLClass.HDLObject);
	}
}