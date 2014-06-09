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

import java.util.EnumSet;
import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExport;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;

@SuppressWarnings("all")
public abstract class AbstractHDLExport extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLExport}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param exportRef
	 *            the value for exportRef. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLExport(int id, @Nullable IHDLObject container, @Nonnull HDLReference exportRef, boolean validate) {
		super(id, container, validate);
		if (validate) {
			exportRef = validateExportRef(exportRef);
		}
		if (exportRef != null) {
			this.exportRef = exportRef;
		} else {
			this.exportRef = null;
		}
	}

	public AbstractHDLExport() {
		super();
		this.exportRef = null;
	}

	protected final HDLReference exportRef;

	/**
	 * Get the exportRef field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLReference getExportRef() {
		return exportRef;
	}

	protected HDLReference validateExportRef(HDLReference exportRef) {
		if (exportRef == null)
			throw new IllegalArgumentException("The field exportRef can not be null!");
		return exportRef;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLExport copy() {
		final HDLExport newObject = new HDLExport(id, null, exportRef, false);
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
		final HDLReference filteredexportRef = filter.copyObject("exportRef", this, exportRef);
		return filter.postFilter((HDLExport) this, new HDLExport(id, null, filteredexportRef, false));
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
	 * Setter for the field {@link #getExportRef()}.
	 *
	 * @param exportRef
	 *            sets the new exportRef of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLExport} with the updated exportRef
	 *         field.
	 */
	@Nonnull
	public HDLExport setExportRef(@Nonnull HDLReference exportRef) {
		exportRef = validateExportRef(exportRef);
		final HDLExport res = new HDLExport(id, container, exportRef, false);
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
		if (exportRef == null) {
			if (other.exportRef != null)
				return false;
		} else if (!exportRef.equals(other.exportRef))
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
		result = (prime * result) + ((exportRef == null) ? 0 : exportRef.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLExport()");
		if (exportRef != null) {
			sb.append(".setExportRef(").append(exportRef.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateExportRef(getExportRef());
		if (getExportRef() != null) {
			getExportRef().validateAllFields(this, checkResolve);
		}
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
					case 0:
						if (exportRef != null) {
							current = Iterators.concat(Iterators.forArray(exportRef), exportRef.deepIterator());
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
						if (exportRef != null) {
							current = Iterators.singletonIterator(exportRef);
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