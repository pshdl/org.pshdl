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
import org.pshdl.model.validation.*;
import org.pshdl.model.validation.builtin.*;

import com.google.common.base.*;

import org.pshdl.model.extensions.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceInstantiation extends HDLInstantiation {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceInstantiation}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterfaceInstantiation(@Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable Iterable<HDLArgument> arguments, @Nonnull HDLQualifiedName hIf,
			boolean validate) {
		super(container, var, arguments, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		this.hIf = hIf;
	}

	public AbstractHDLInterfaceInstantiation() {
		super();
		this.hIf = null;
	}

	protected final HDLQualifiedName hIf;

	@Nullable
	public Optional<HDLInterface> resolveHIf() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveInterface(this, hIf);
	}

	public HDLQualifiedName getHIfRefName() {
		return hIf;
	}

	protected HDLQualifiedName validateHIf(HDLQualifiedName hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation copy() {
		HDLInterfaceInstantiation newObject = new HDLInterfaceInstantiation(null, var, arguments, hIf, false);
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
	public HDLInterfaceInstantiation copyFiltered(CopyFilter filter) {
		HDLVariable filteredvar = filter.copyObject("var", this, var);
		ArrayList<HDLArgument> filteredarguments = filter.copyContainer("arguments", this, arguments);
		HDLQualifiedName filteredhIf = filter.copyObject("hIf", this, hIf);
		return filter.postFilter((HDLInterfaceInstantiation) this, new HDLInterfaceInstantiation(null, filteredvar, filteredarguments, filteredhIf, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation copyDeepFrozen(IHDLObject container) {
		HDLInterfaceInstantiation copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInterfaceInstantiation} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation setContainer(@Nullable IHDLObject container) {
		return (HDLInterfaceInstantiation) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 * 
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated var field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation setVar(@Nonnull HDLVariable var) {
		var = validateVar(var);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArguments()}.
	 * 
	 * @param arguments
	 *            sets the new arguments of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated arguments field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation setArguments(@Nullable Iterable<HDLArgument> arguments) {
		arguments = validateArguments(arguments);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArguments()}.
	 * 
	 * @param newArguments
	 *            the value that should be added to the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated arguments field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation addArguments(@Nullable HDLArgument newArguments) {
		if (newArguments == null)
			throw new IllegalArgumentException("Element of arguments can not be null!");
		ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.add(newArguments);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArguments()}.
	 * 
	 * @param newArguments
	 *            the value that should be removed from the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated arguments field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation removeArguments(@Nullable HDLArgument newArguments) {
		if (newArguments == null)
			throw new IllegalArgumentException("Removed element of arguments can not be null!");
		ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(newArguments);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArguments()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated arguments field.
	 */
	@Nonnull
	public HDLInterfaceInstantiation removeArguments(int idx) {
		ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(idx);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIf()}.
	 * 
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated hIf field.
	 */
	@Nonnull
	public HDLInterfaceInstantiation setHIf(@Nonnull HDLQualifiedName hIf) {
		hIf = validateHIf(hIf);
		HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(container, var, arguments, hIf, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterfaceInstantiation))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInterfaceInstantiation other = (AbstractHDLInterfaceInstantiation) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
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
		result = (prime * result) + ((hIf == null) ? 0 : hIf.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterfaceInstantiation()");
		if (var != null) {
			sb.append(".setVar(").append(var.toConstructionString(spacing + "\t")).append(")");
		}
		if (arguments != null) {
			if (arguments.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLArgument o : arguments) {
					sb.append(".addArguments(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (hIf != null) {
			sb.append(".setHIf(HDLQualifiedName.create(\"").append(hIf).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHIf(getHIfRefName());
		if (checkResolve && (getHIfRefName() != null))
			if (!resolveHIf().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHIfRefName()));
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterfaceInstantiation, HDLClass.HDLInstantiation, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}
