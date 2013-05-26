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
public abstract class AbstractHDLNativeFunction extends HDLFunction {
	/**
	 * Constructs a new instance of {@link AbstractHDLNativeFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param returnType
	 *            the value for returnType. Can be <code>null</code>.
	 * @param simOnly
	 *            the value for simOnly. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLNativeFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nonnull Boolean simOnly, boolean validate) {
		super(id, container, annotations, name, args, returnType, validate);
		if (validate) {
			simOnly = validateSimOnly(simOnly);
		}
		this.simOnly = simOnly;
	}

	public AbstractHDLNativeFunction() {
		super();
		this.simOnly = null;
	}

	protected final Boolean simOnly;

	/**
	 * Get the simOnly field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public Boolean getSimOnly() {
		return simOnly;
	}

	protected Boolean validateSimOnly(Boolean simOnly) {
		if (simOnly == null)
			throw new IllegalArgumentException("The field simOnly can not be null!");
		return simOnly;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction copy() {
		final HDLNativeFunction newObject = new HDLNativeFunction(id, null, annotations, name, args, returnType, simOnly, false);
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
	public HDLNativeFunction copyFiltered(CopyFilter filter) {
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<HDLFunctionParameter> filteredargs = filter.copyContainer("args", this, args);
		final HDLFunctionParameter filteredreturnType = filter.copyObject("returnType", this, returnType);
		final Boolean filteredsimOnly = filter.copyObject("simOnly", this, simOnly);
		return filter.postFilter((HDLNativeFunction) this, new HDLNativeFunction(id, null, filteredannotations, filteredname, filteredargs, filteredreturnType, filteredsimOnly,
				false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction copyDeepFrozen(IHDLObject container) {
		final HDLNativeFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLNativeFunction} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setContainer(@Nullable IHDLObject container) {
		return (HDLNativeFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLNativeFunction removeAnnotations(int idx) {
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated name
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setName(@Nonnull String name) {
		name = validateName(name);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArgs()}.
	 * 
	 * @param args
	 *            sets the new args of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated args
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setArgs(@Nullable Iterable<HDLFunctionParameter> args) {
		args = validateArgs(args);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be added to the field {@link #getArgs()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated args
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction addArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Element of args can not be null!");
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.add(newArgs);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated args
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction removeArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Removed element of args can not be null!");
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(newArgs);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLNativeFunction} with the updated args
	 *         field.
	 */
	@Nonnull
	public HDLNativeFunction removeArgs(int idx) {
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(idx);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getReturnType()}.
	 * 
	 * @param returnType
	 *            sets the new returnType of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         returnType field.
	 */
	@Override
	@Nonnull
	public HDLNativeFunction setReturnType(@Nullable HDLFunctionParameter returnType) {
		returnType = validateReturnType(returnType);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimOnly()}.
	 * 
	 * @param simOnly
	 *            sets the new simOnly of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         simOnly field.
	 */
	@Nonnull
	public HDLNativeFunction setSimOnly(@Nonnull Boolean simOnly) {
		simOnly = validateSimOnly(simOnly);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getSimOnly()}.
	 * 
	 * @param simOnly
	 *            sets the new simOnly of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLNativeFunction} with the updated
	 *         simOnly field.
	 */
	@Nonnull
	public HDLNativeFunction setSimOnly(boolean simOnly) {
		simOnly = validateSimOnly(simOnly);
		final HDLNativeFunction res = new HDLNativeFunction(id, container, annotations, name, args, returnType, simOnly, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLNativeFunction))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLNativeFunction other = (AbstractHDLNativeFunction) obj;
		if (simOnly == null) {
			if (other.simOnly != null)
				return false;
		} else if (!simOnly.equals(other.simOnly))
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
		result = (prime * result) + ((simOnly == null) ? 0 : simOnly.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLNativeFunction()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (args != null) {
			if (args.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLFunctionParameter o : args) {
					sb.append(".addArgs(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (returnType != null) {
			sb.append(".setReturnType(").append(returnType.toConstructionString(spacing + "\t")).append(")");
		}
		if (simOnly != null) {
			sb.append(".setSimOnly(").append(simOnly).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateSimOnly(getSimOnly());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLNativeFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if ((args != null) && (args.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(args.size());
							for (final HDLFunctionParameter o : args) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 2:
						if (returnType != null) {
							current = Iterators.concat(Iterators.forArray(returnType), returnType.deepIterator());
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
						if ((args != null) && (args.size() != 0)) {
							current = args.iterator();
						}
						break;
					case 2:
						if (returnType != null) {
							current = Iterators.singletonIterator(returnType);
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