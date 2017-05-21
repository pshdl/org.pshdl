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
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLResolvedRef;
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
public abstract class AbstractHDLEnumRef extends HDLResolvedRef {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumRef}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnumRef(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName var, @Nonnull HDLQualifiedName hEnum, boolean validate) {
		super(id, container, var, validate);
		if (validate) {
			hEnum = validateHEnum(hEnum);
		}
		this.hEnum = hEnum;
	}

	public AbstractHDLEnumRef() {
		super();
		this.hEnum = null;
	}

	protected final HDLQualifiedName hEnum;

	public HDLEnum resolveHEnumForced(String stage) {
		final Optional<HDLEnum> opt = resolveHEnum();
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new HDLCodeGenerationException(this, "failed to resolve:" + hEnum, stage);
	}

	public Optional<HDLEnum> resolveHEnum() {
		if (!frozen) {
			throw new IllegalArgumentException("Object not frozen");
		}
		return ScopingExtension.INST.resolveEnum(this, hEnum);
	}

	public HDLQualifiedName getHEnumRefName() {
		return hEnum;
	}

	protected HDLQualifiedName validateHEnum(HDLQualifiedName hEnum) {
		if (hEnum == null) {
			throw new IllegalArgumentException("The field hEnum can not be null!");
		}
		return hEnum;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumRef copy() {
		final HDLEnumRef newObject = new HDLEnumRef(id, null, var, hEnum, false);
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
	public HDLEnumRef copyFiltered(CopyFilter filter) {
		final HDLQualifiedName filteredvar = filter.copyObject("var", this, var);
		final HDLQualifiedName filteredhEnum = filter.copyObject("hEnum", this, hEnum);
		return filter.postFilter((HDLEnumRef) this, new HDLEnumRef(id, null, filteredvar, filteredhEnum, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumRef copyDeepFrozen(IHDLObject container) {
		final HDLEnumRef copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLEnumRef} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLEnumRef setContainer(@Nullable IHDLObject container) {
		return (HDLEnumRef) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVarRefName()}.
	 *
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLEnumRef} with the updated var field.
	 */
	@Override
	@Nonnull
	public HDLEnumRef setVar(@Nonnull HDLQualifiedName var) {
		var = validateVar(var);
		final HDLEnumRef res = new HDLEnumRef(id, container, var, hEnum, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHEnumRefName()}.
	 *
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLEnumRef} with the updated hEnum field.
	 */
	@Nonnull
	public HDLEnumRef setHEnum(@Nonnull HDLQualifiedName hEnum) {
		hEnum = validateHEnum(hEnum);
		final HDLEnumRef res = new HDLEnumRef(id, container, var, hEnum, false);
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
		if (!(obj instanceof AbstractHDLEnumRef)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLEnumRef other = (AbstractHDLEnumRef) obj;
		if (hEnum == null) {
			if (other.hEnum != null) {
				return false;
			}
		} else if (!hEnum.equals(other.hEnum)) {
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
		result = (prime * result) + ((hEnum == null) ? 0 : hEnum.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnumRef()");
		if (var != null) {
			sb.append(".setVar(HDLQualifiedName.create(\"").append(var).append("\"))");
		}
		if (hEnum != null) {
			sb.append(".setHEnum(HDLQualifiedName.create(\"").append(hEnum).append("\"))");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHEnum(getHEnumRefName());
		if (checkResolve && (getHEnumRefName() != null)) {
			if (!resolveHEnum().isPresent()) {
				throw new HDLProblemException(new Problem(ErrorCode.UNRESOLVED_REFERENCE, this, "to:" + getHEnumRefName()));
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnumRef, HDLClass.HDLResolvedRef, HDLClass.HDLReference, HDLClass.HDLExpression, HDLClass.HDLObject);
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