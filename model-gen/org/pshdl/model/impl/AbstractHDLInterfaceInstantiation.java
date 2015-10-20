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
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceInstantiation;
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
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLInterfaceInstantiation extends HDLInstantiation {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterfaceInstantiation}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterfaceInstantiation(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLVariable var,
			@Nullable Iterable<HDLArgument> arguments, @Nonnull HDLQualifiedName hIf, boolean validate) {
		super(id, container, annotations, var, arguments, validate);
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

	public HDLInterface resolveHIfForced(String stage) {
		final Optional<HDLInterface> opt = resolveHIf();
		if (opt.isPresent())
			return opt.get();
		throw new HDLCodeGenerationException(this, "failed to resolve:" + hIf, stage);
	}

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
		final HDLInterfaceInstantiation newObject = new HDLInterfaceInstantiation(id, null, annotations, var, arguments, hIf, false);
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
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final HDLVariable filteredvar = filter.copyObject("var", this, var);
		final ArrayList<HDLArgument> filteredarguments = filter.copyContainer("arguments", this, arguments);
		final HDLQualifiedName filteredhIf = filter.copyObject("hIf", this, hIf);
		return filter.postFilter((HDLInterfaceInstantiation) this,
				new HDLInterfaceInstantiation(id, null, filteredannotations, filteredvar, filteredarguments, filteredhIf, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation copyDeepFrozen(IHDLObject container) {
		final HDLInterfaceInstantiation copy = copyFiltered(CopyFilter.DEEP_META);
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
	 * Setter for the field {@link #getAnnotations()}.
	 *
	 * @param annotations
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInterfaceInstantiation removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInterfaceInstantiation} with the
	 *         updated annotations field.
	 */
	@Nonnull
	public HDLInterfaceInstantiation removeAnnotations(int idx) {
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
		return res;
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
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
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
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
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
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.add(newArguments);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
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
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(newArguments);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
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
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(idx);
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIfRefName()}.
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
		final HDLInterfaceInstantiation res = new HDLInterfaceInstantiation(id, container, annotations, var, arguments, hIf, false);
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
		final AbstractHDLInterfaceInstantiation other = (AbstractHDLInterfaceInstantiation) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
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
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterfaceInstantiation()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLAnnotation o : annotations) {
					sb.append(".addAnnotations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (var != null) {
			sb.append(".setVar(").append(var.toConstructionString(spacing + "\t")).append(")");
		}
		if (arguments != null) {
			if (arguments.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLArgument o : arguments) {
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
						if (var != null) {
							current = Iterators.concat(Iterators.forArray(var), var.deepIterator());
						}
						break;
					case 2:
						if ((arguments != null) && (arguments.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(arguments.size());
							for (final HDLArgument o : arguments) {
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
						if (var != null) {
							current = Iterators.singletonIterator(var);
						}
						break;
					case 2:
						if ((arguments != null) && (arguments.size() != 0)) {
							current = arguments.iterator();
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