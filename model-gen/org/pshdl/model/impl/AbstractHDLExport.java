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
import org.pshdl.model.HDLExport;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLStatement;
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
public abstract class AbstractHDLExport extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLExport}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param var
	 *            the value for var. Can be <code>null</code>.
	 * @param match
	 *            the value for match. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLExport(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName hIf, @Nullable HDLQualifiedName var, @Nullable String match, boolean validate) {
		super(id, container, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		this.hIf = hIf;
		if (validate) {
			var = validateVar(var);
		}
		this.var = var;
		if (validate) {
			match = validateMatch(match);
		}
		this.match = match;
	}

	public AbstractHDLExport() {
		super();
		this.hIf = null;
		this.var = null;
		this.match = null;
	}

	protected final HDLQualifiedName hIf;

	public HDLVariable resolveHIfForced(String stage) {
		final Optional<HDLVariable> opt = resolveHIf();
		if (opt.isPresent())
			return opt.get();
		throw new HDLCodeGenerationException(this, "failed to resolve:" + hIf, stage);
	}

	public Optional<HDLVariable> resolveHIf() {
		if (!frozen)
			throw new IllegalArgumentException("Object not frozen");
		return ScopingExtension.INST.resolveVariable(this, hIf);
	}

	public HDLQualifiedName getHIfRefName() {
		return hIf;
	}

	protected HDLQualifiedName validateHIf(HDLQualifiedName hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
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
		return var;
	}

	protected final String match;

	/**
	 * Get the match field. Can be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nullable
	public String getMatch() {
		return match;
	}

	protected String validateMatch(String match) {
		return match;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLExport copy() {
		final HDLExport newObject = new HDLExport(id, null, hIf, var, match, false);
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
	public HDLExport copyFiltered(CopyFilter filter) {
		final HDLQualifiedName filteredhIf = filter.copyObject("hIf", this, hIf);
		final HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		final String filteredmatch = filter.copyObject("match", this, match);
		return filter.postFilter((HDLExport) this, new HDLExport(id, null, filteredhIf, filteredvar, filteredmatch, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLExport copyDeepFrozen(IHDLObject container) {
		final HDLExport copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLExport} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLExport setContainer(@Nullable IHDLObject container) {
		return (HDLExport) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getHIfRefName()}.
	 *
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLExport} with the updated hIf field.
	 */
	@Nonnull
	public HDLExport setHIf(@Nonnull HDLQualifiedName hIf) {
		hIf = validateHIf(hIf);
		final HDLExport res = new HDLExport(id, container, hIf, var, match, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getVarRefName()}.
	 *
	 * @param var
	 *            sets the new var of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLExport} with the updated var field.
	 */
	@Nonnull
	public HDLExport setVar(@Nullable HDLQualifiedName var) {
		var = validateVar(var);
		final HDLExport res = new HDLExport(id, container, hIf, var, match, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getMatch()}.
	 *
	 * @param match
	 *            sets the new match of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLExport} with the updated match field.
	 */
	@Nonnull
	public HDLExport setMatch(@Nullable String match) {
		match = validateMatch(match);
		final HDLExport res = new HDLExport(id, container, hIf, var, match, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLExport))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLExport other = (AbstractHDLExport) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		if (var == null) {
			if (other.var != null)
				return false;
		} else if (!var.equals(other.var))
			return false;
		if (match == null) {
			if (other.match != null)
				return false;
		} else if (!match.equals(other.match))
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
		result = (prime * result) + ((hIf == null) ? 0 : hIf.hashCode());
		result = (prime * result) + ((var == null) ? 0 : var.hashCode());
		result = (prime * result) + ((match == null) ? 0 : match.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLExport()");
		if (hIf != null) {
			sb.append(".setHIf(HDLQualifiedName.create(\"").append(hIf).append("\"))");
		}
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
		}
		if (match != null) {
			sb.append(".setMatch(").append('"' + match + '"').append(")");
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
		validateVar(getVarRefName());
		if (checkResolve && (getVarRefName() != null))
			if (!resolveVar().isPresent())
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getVarRefName()));
		validateMatch(getMatch());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLExport, HDLClass.HDLStatement, HDLClass.HDLObject);
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