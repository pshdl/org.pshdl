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
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;
import com.google.common.collect.*;

@SuppressWarnings("all")
public abstract class AbstractHDLFunctionParameter extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLFunctionParameter}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param rw
	 *            the value for rw. If <code>null</code>, {@link RWType#READ} is
	 *            used as default.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param enumSpec
	 *            the value for enumSpec. Can be <code>null</code>.
	 * @param ifSpec
	 *            the value for ifSpec. Can be <code>null</code>.
	 * @param funcSpec
	 *            the value for funcSpec. Can be <code>null</code>.
	 * @param funcReturnSpec
	 *            the value for funcReturnSpec. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can be <code>null</code>.
	 * @param width
	 *            the value for width. Can be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLFunctionParameter(int id, @Nullable IHDLObject container, @Nullable RWType rw, @Nonnull Type type, @Nullable HDLQualifiedName enumSpec,
			@Nullable HDLQualifiedName ifSpec, @Nullable Iterable<HDLFunctionParameter> funcSpec, @Nullable HDLFunctionParameter funcReturnSpec, @Nullable HDLVariable name,
			@Nullable HDLExpression width, @Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(id, container, validate);
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
			for (final HDLFunctionParameter newValue : funcSpec) {
				this.funcSpec.add(newValue);
			}
		}
		if (validate) {
			funcReturnSpec = validateFuncReturnSpec(funcReturnSpec);
		}
		if (funcReturnSpec != null) {
			this.funcReturnSpec = funcReturnSpec;
		} else {
			this.funcReturnSpec = null;
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
			width = validateWidth(width);
		}
		if (width != null) {
			this.width = width;
		} else {
			this.width = null;
		}
		if (validate) {
			dim = validateDim(dim);
		}
		this.dim = new ArrayList<HDLExpression>();
		if (dim != null) {
			for (final HDLExpression newValue : dim) {
				this.dim.add(newValue);
			}
		}
	}

	public AbstractHDLFunctionParameter() {
		super();
		this.rw = null;
		this.type = null;
		this.enumSpec = null;
		this.ifSpec = null;
		this.funcSpec = new ArrayList<HDLFunctionParameter>();
		this.funcReturnSpec = null;
		this.name = null;
		this.width = null;
		this.dim = new ArrayList<HDLExpression>();
	}

	protected final RWType rw;

	/**
	 * Get the rw field. If <code>null</code>, {@link RWType#READ} is used as
	 * default.
	 *
	 * @return the field
	 */
	@Nonnull
	public RWType getRw() {
		return rw == null ? RWType.READ : rw;
	}

	protected RWType validateRw(RWType rw) {
		return rw == null ? RWType.READ : rw;
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

	protected final HDLFunctionParameter funcReturnSpec;

	/**
	 * Get the funcReturnSpec field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLFunctionParameter getFuncReturnSpec() {
		return funcReturnSpec;
	}

	protected HDLFunctionParameter validateFuncReturnSpec(HDLFunctionParameter funcReturnSpec) {
		return funcReturnSpec;
	}

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

	protected final HDLExpression width;

	/**
	 * Get the width field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public HDLExpression getWidth() {
		return width;
	}

	protected HDLExpression validateWidth(HDLExpression width) {
		return width;
	}

	protected final ArrayList<HDLExpression> dim;

	/**
	 * Get the dim field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getDim() {
		return (ArrayList<HDLExpression>) dim.clone();
	}

	protected Iterable<HDLExpression> validateDim(Iterable<HDLExpression> dim) {
		if (dim == null)
			return new ArrayList<HDLExpression>();
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
		final HDLFunctionParameter newObject = new HDLFunctionParameter(id, null, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final RWType filteredrw = filter.copyObject("rw", this, rw);
		final Type filteredtype = filter.copyObject("type", this, type);
		final HDLQualifiedName filteredenumSpec = filter.copyObject("enumSpec", this, enumSpec);
		final HDLQualifiedName filteredifSpec = filter.copyObject("ifSpec", this, ifSpec);
		final ArrayList<HDLFunctionParameter> filteredfuncSpec = filter.copyContainer("funcSpec", this, funcSpec);
		final HDLFunctionParameter filteredfuncReturnSpec = filter.copyObject("funcReturnSpec", this, funcReturnSpec);
		final HDLVariable filteredname = filter.copyObject("name", this, name);
		final HDLExpression filteredwidth = filter.copyObject("width", this, width);
		final ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		return filter.postFilter((HDLFunctionParameter) this, new HDLFunctionParameter(id, null, filteredrw, filteredtype, filteredenumSpec, filteredifSpec, filteredfuncSpec,
				filteredfuncReturnSpec, filteredname, filteredwidth, filtereddim, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLFunctionParameter copyDeepFrozen(IHDLObject container) {
		final HDLFunctionParameter copy = copyFiltered(CopyFilter.DEEP_META);
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
	 *            sets the new rw of this object. If <code>null</code>,
	 *            {@link RWType#READ} is used as default.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         rw field.
	 */
	@Nonnull
	public HDLFunctionParameter setRw(@Nullable RWType rw) {
		rw = validateRw(rw);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.add(newFuncSpec);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.remove(newFuncSpec);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final ArrayList<HDLFunctionParameter> funcSpec = (ArrayList<HDLFunctionParameter>) this.funcSpec.clone();
		funcSpec.remove(idx);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getFuncReturnSpec()}.
	 *
	 * @param funcReturnSpec
	 *            sets the new funcReturnSpec of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         funcReturnSpec field.
	 */
	@Nonnull
	public HDLFunctionParameter setFuncReturnSpec(@Nullable HDLFunctionParameter funcReturnSpec) {
		funcReturnSpec = validateFuncReturnSpec(funcReturnSpec);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getWidth()}.
	 *
	 * @param width
	 *            sets the new width of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         width field.
	 */
	@Nonnull
	public HDLFunctionParameter setWidth(@Nullable HDLExpression width) {
		width = validateWidth(width);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDim()}.
	 *
	 * @param dim
	 *            sets the new dim of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         dim field.
	 */
	@Nonnull
	public HDLFunctionParameter setDim(@Nullable Iterable<HDLExpression> dim) {
		dim = validateDim(dim);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDim()}.
	 *
	 * @param newDim
	 *            the value that should be added to the field {@link #getDim()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         dim field.
	 */
	@Nonnull
	public HDLFunctionParameter addDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Element of dim can not be null!");
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 *
	 * @param newDim
	 *            the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         dim field.
	 */
	@Nonnull
	public HDLFunctionParameter removeDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Removed element of dim can not be null!");
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLFunctionParameter} with the updated
	 *         dim field.
	 */
	@Nonnull
	public HDLFunctionParameter removeDim(int idx) {
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		final HDLFunctionParameter res = new HDLFunctionParameter(id, container, rw, type, enumSpec, ifSpec, funcSpec, funcReturnSpec, name, width, dim, false);
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
		final AbstractHDLFunctionParameter other = (AbstractHDLFunctionParameter) obj;
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
		if (funcReturnSpec == null) {
			if (other.funcReturnSpec != null)
				return false;
		} else if (!funcReturnSpec.equals(other.funcReturnSpec))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		if (dim == null) {
			if (other.dim != null)
				return false;
		} else if (!dim.equals(other.dim))
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
		result = (prime * result) + ((rw == null) ? 0 : rw.hashCode());
		result = (prime * result) + ((type == null) ? 0 : type.hashCode());
		result = (prime * result) + ((enumSpec == null) ? 0 : enumSpec.hashCode());
		result = (prime * result) + ((ifSpec == null) ? 0 : ifSpec.hashCode());
		result = (prime * result) + ((funcSpec == null) ? 0 : funcSpec.hashCode());
		result = (prime * result) + ((funcReturnSpec == null) ? 0 : funcReturnSpec.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((width == null) ? 0 : width.hashCode());
		result = (prime * result) + ((dim == null) ? 0 : dim.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
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
				for (final HDLFunctionParameter o : funcSpec) {
					sb.append(".addFuncSpec(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (funcReturnSpec != null) {
			sb.append(".setFuncReturnSpec(").append(funcReturnSpec.toConstructionString(spacing + "\t")).append(")");
		}
		if (name != null) {
			sb.append(".setName(").append(name.toConstructionString(spacing + "\t")).append(")");
		}
		if (width != null) {
			sb.append(".setWidth(").append(width.toConstructionString(spacing + "\t")).append(")");
		}
		if (dim != null) {
			if (dim.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : dim) {
					sb.append(".addDim(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
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
			for (final HDLFunctionParameter o : getFuncSpec()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateFuncReturnSpec(getFuncReturnSpec());
		if (getFuncReturnSpec() != null) {
			getFuncReturnSpec().validateAllFields(this, checkResolve);
		}
		validateName(getName());
		if (getName() != null) {
			getName().validateAllFields(this, checkResolve);
		}
		validateWidth(getWidth());
		if (getWidth() != null) {
			getWidth().validateAllFields(this, checkResolve);
		}
		validateDim(getDim());
		if (getDim() != null) {
			for (final HDLExpression o : getDim()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLFunctionParameter, HDLClass.HDLObject);
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
						if ((funcSpec != null) && (funcSpec.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(funcSpec.size());
							for (final HDLFunctionParameter o : funcSpec) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if (funcReturnSpec != null) {
							current = Iterators.concat(Iterators.forArray(funcReturnSpec), funcReturnSpec.deepIterator());
						}
						break;
					case 2:
						if (name != null) {
							current = Iterators.concat(Iterators.forArray(name), name.deepIterator());
						}
						break;
					case 3:
						if (width != null) {
							current = Iterators.concat(Iterators.forArray(width), width.deepIterator());
						}
						break;
					case 4:
						if ((dim != null) && (dim.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(dim.size());
							for (final HDLExpression o : dim) {
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
						if ((funcSpec != null) && (funcSpec.size() != 0)) {
							current = funcSpec.iterator();
						}
						break;
					case 1:
						if (funcReturnSpec != null) {
							current = Iterators.singletonIterator(funcReturnSpec);
						}
						break;
					case 2:
						if (name != null) {
							current = Iterators.singletonIterator(name);
						}
						break;
					case 3:
						if (width != null) {
							current = Iterators.singletonIterator(width);
						}
						break;
					case 4:
						if ((dim != null) && (dim.size() != 0)) {
							current = dim.iterator();
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