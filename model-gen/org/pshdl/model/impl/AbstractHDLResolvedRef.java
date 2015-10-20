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

import java.util.EnumSet;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;
import org.pshdl.model.utils.CopyFilter;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;

@SuppressWarnings("all")
public abstract class AbstractHDLResolvedRef extends HDLReference {
	/**
	 * Constructs a new instance of {@link AbstractHDLResolvedRef}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLResolvedRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, boolean validate) {
		super(id, container, validate);
		if (validate) {
			var = validateVar(var);
		}
		this.var = var;
	}

	public AbstractHDLResolvedRef() {
		super();
		this.var = null;
	}

	protected final HDLQualifiedName var;

	public HDLVariable resolveVarForced(String stage) {
		final Optional<HDLVariable> opt = resolveVar();
		if (opt.isPresent())
			return opt.get();
		throw new HDLCodeGenerationException(this, "failed to resolve:" + var, stage);
	}

	public Optional<HDLVariable> resolveVar() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, var);
	}

	public HDLQualifiedName getVarRefName() {
		return var;
	}

	protected HDLQualifiedName validateVar(HDLQualifiedName var) {
		if (var == null)
			throw new IllegalArgumentException("The field var can not be null!");
		return var;
	}

	@Nonnull
	public abstract HDLResolvedRef setVar(@Nonnull HDLQualifiedName var);

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copy();

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLResolvedRef copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLResolvedRef))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLResolvedRef other = (AbstractHDLResolvedRef) obj;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
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
		result = (prime * result) + ((var == null) ? 0 : var.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLResolvedRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateVar(getVarRefName());
		if (checkResolve && (getVarRefName() != null))
			if (!resolveVar().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
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