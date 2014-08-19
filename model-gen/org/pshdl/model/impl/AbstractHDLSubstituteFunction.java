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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLSubstituteFunction extends HDLFunction {
	/**
	 * Constructs a new instance of {@link AbstractHDLSubstituteFunction}
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
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLSubstituteFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nullable Iterable<HDLStatement> stmnts, boolean validate) {
		super(id, container, annotations, name, args, returnType, validate);
		if (validate) {
			stmnts = validateStmnts(stmnts);
		}
		this.stmnts = new ArrayList<HDLStatement>();
		if (stmnts != null) {
			for (final HDLStatement newValue : stmnts) {
				this.stmnts.add(newValue);
			}
		}
	}

	public AbstractHDLSubstituteFunction() {
		super();
		this.stmnts = new ArrayList<HDLStatement>();
	}

	protected final ArrayList<HDLStatement> stmnts;

	/**
	 * Get the stmnts field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getStmnts() {
		return (ArrayList<HDLStatement>) stmnts.clone();
	}

	protected Iterable<HDLStatement> validateStmnts(Iterable<HDLStatement> stmnts) {
		if (stmnts == null)
			return new ArrayList<HDLStatement>();
		return stmnts;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction copy() {
		final HDLSubstituteFunction newObject = new HDLSubstituteFunction(id, null, annotations, name, args, returnType, stmnts, false);
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
	public HDLSubstituteFunction copyFiltered(CopyFilter filter) {
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<HDLFunctionParameter> filteredargs = filter.copyContainer("args", this, args);
		final HDLFunctionParameter filteredreturnType = filter.copyObject("returnType", this, returnType);
		final ArrayList<HDLStatement> filteredstmnts = filter.copyContainer("stmnts", this, stmnts);
		return filter.postFilter((HDLSubstituteFunction) this, new HDLSubstituteFunction(id, null, filteredannotations, filteredname, filteredargs, filteredreturnType,
				filteredstmnts, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction copyDeepFrozen(IHDLObject container) {
		final HDLSubstituteFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLSubstituteFunction} with the
	 *         updated container field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction setContainer(@Nullable IHDLObject container) {
		return (HDLSubstituteFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 * 
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLSubstituteFunction removeAnnotations(int idx) {
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         name field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction setName(@Nonnull String name) {
		name = validateName(name);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArgs()}.
	 * 
	 * @param args
	 *            sets the new args of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         args field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction setArgs(@Nullable Iterable<HDLFunctionParameter> args) {
		args = validateArgs(args);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be added to the field {@link #getArgs()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         args field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction addArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Element of args can not be null!");
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.add(newArgs);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         args field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction removeArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Removed element of args can not be null!");
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(newArgs);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         args field.
	 */
	@Nonnull
	public HDLSubstituteFunction removeArgs(int idx) {
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(idx);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getReturnType()}.
	 * 
	 * @param returnType
	 *            sets the new returnType of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         returnType field.
	 */
	@Override
	@Nonnull
	public HDLSubstituteFunction setReturnType(@Nullable HDLFunctionParameter returnType) {
		returnType = validateReturnType(returnType);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStmnts()}.
	 * 
	 * @param stmnts
	 *            sets the new stmnts of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         stmnts field.
	 */
	@Nonnull
	public HDLSubstituteFunction setStmnts(@Nullable Iterable<HDLStatement> stmnts) {
		stmnts = validateStmnts(stmnts);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getStmnts()}.
	 * 
	 * @param newStmnts
	 *            the value that should be added to the field
	 *            {@link #getStmnts()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         stmnts field.
	 */
	@Nonnull
	public HDLSubstituteFunction addStmnts(@Nullable HDLStatement newStmnts) {
		if (newStmnts == null)
			throw new IllegalArgumentException("Element of stmnts can not be null!");
		final ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.add(newStmnts);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStmnts()}.
	 * 
	 * @param newStmnts
	 *            the value that should be removed from the field
	 *            {@link #getStmnts()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         stmnts field.
	 */
	@Nonnull
	public HDLSubstituteFunction removeStmnts(@Nullable HDLStatement newStmnts) {
		if (newStmnts == null)
			throw new IllegalArgumentException("Removed element of stmnts can not be null!");
		final ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.remove(newStmnts);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStmnts()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getStmnts()}
	 * @return a new instance of {@link HDLSubstituteFunction} with the updated
	 *         stmnts field.
	 */
	@Nonnull
	public HDLSubstituteFunction removeStmnts(int idx) {
		final ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.remove(idx);
		final HDLSubstituteFunction res = new HDLSubstituteFunction(id, container, annotations, name, args, returnType, stmnts, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLSubstituteFunction))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLSubstituteFunction other = (AbstractHDLSubstituteFunction) obj;
		if (stmnts == null) {
			if (other.stmnts != null)
				return false;
		} else if (!stmnts.equals(other.stmnts))
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
		result = (prime * result) + ((stmnts == null) ? 0 : stmnts.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLSubstituteFunction()");
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
		if (stmnts != null) {
			if (stmnts.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : stmnts) {
					sb.append(".addStmnts(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateStmnts(getStmnts());
		if (getStmnts() != null) {
			for (final HDLStatement o : getStmnts()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLSubstituteFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
					case 3:
						if ((stmnts != null) && (stmnts.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(stmnts.size());
							for (final HDLStatement o : stmnts) {
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
						if ((args != null) && (args.size() != 0)) {
							current = args.iterator();
						}
						break;
					case 2:
						if (returnType != null) {
							current = Iterators.singletonIterator(returnType);
						}
						break;
					case 3:
						if ((stmnts != null) && (stmnts.size() != 0)) {
							current = stmnts.iterator();
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