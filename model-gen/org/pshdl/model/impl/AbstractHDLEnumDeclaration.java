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
import org.pshdl.model.utils.*;

import com.google.common.collect.*;

@SuppressWarnings("all")
public abstract class AbstractHDLEnumDeclaration extends HDLDeclaration {
	/**
	 * Constructs a new instance of {@link AbstractHDLEnumDeclaration}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param hEnum
	 *            the value for hEnum. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLEnumDeclaration(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLEnum hEnum, boolean validate) {
		super(id, container, annotations, validate);
		if (validate) {
			hEnum = validateHEnum(hEnum);
		}
		if (hEnum != null) {
			this.hEnum = hEnum;
		} else {
			this.hEnum = null;
		}
	}

	public AbstractHDLEnumDeclaration() {
		super();
		this.hEnum = null;
	}

	protected final HDLEnum hEnum;

	/**
	 * Get the hEnum field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLEnum getHEnum() {
		return hEnum;
	}

	protected HDLEnum validateHEnum(HDLEnum hEnum) {
		if (hEnum == null)
			throw new IllegalArgumentException("The field hEnum can not be null!");
		return hEnum;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration copy() {
		HDLEnumDeclaration newObject = new HDLEnumDeclaration(id, null, annotations, hEnum, false);
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
	public HDLEnumDeclaration copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		HDLEnum filteredhEnum = filter.copyObject("hEnum", this, hEnum);
		return filter.postFilter((HDLEnumDeclaration) this, new HDLEnumDeclaration(id, null, filteredannotations, filteredhEnum, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration copyDeepFrozen(IHDLObject container) {
		HDLEnumDeclaration copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLEnumDeclaration} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration setContainer(@Nullable IHDLObject container) {
		return (HDLEnumDeclaration) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(id, container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(id, container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLEnumDeclaration removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLEnumDeclaration res = new HDLEnumDeclaration(id, container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLEnumDeclaration removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLEnumDeclaration res = new HDLEnumDeclaration(id, container, annotations, hEnum, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHEnum()}.
	 * 
	 * @param hEnum
	 *            sets the new hEnum of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLEnumDeclaration} with the updated
	 *         hEnum field.
	 */
	@Nonnull
	public HDLEnumDeclaration setHEnum(@Nonnull HDLEnum hEnum) {
		hEnum = validateHEnum(hEnum);
		HDLEnumDeclaration res = new HDLEnumDeclaration(id, container, annotations, hEnum, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLEnumDeclaration))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLEnumDeclaration other = (AbstractHDLEnumDeclaration) obj;
		if (hEnum == null) {
			if (other.hEnum != null)
				return false;
		} else if (!hEnum.equals(other.hEnum))
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
		result = (prime * result) + ((hEnum == null) ? 0 : hEnum.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLEnumDeclaration()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (hEnum != null) {
			sb.append(".setHEnum(").append(hEnum.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHEnum(getHEnum());
		if (getHEnum() != null) {
			getHEnum().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLEnumDeclaration, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
							List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(annotations.size());
							for (HDLAnnotation o : annotations) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if (hEnum != null) {
							current = Iterators.concat(Iterators.forArray(hEnum), hEnum.deepIterator());
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
						if (hEnum != null) {
							current = Iterators.singletonIterator(hEnum);
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