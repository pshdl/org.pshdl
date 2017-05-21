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
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLInstantiation extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLInstantiation}
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
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInstantiation(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull HDLVariable var,
			@Nullable Iterable<HDLArgument> arguments, boolean validate) {
		super(id, container, validate);
		if (validate) {
			annotations = validateAnnotations(annotations);
		}
		this.annotations = new ArrayList<>();
		if (annotations != null) {
			for (final HDLAnnotation newValue : annotations) {
				this.annotations.add(newValue);
			}
		}
		if (validate) {
			var = validateVar(var);
		}
		if (var != null) {
			this.var = var;
		} else {
			this.var = null;
		}
		if (validate) {
			arguments = validateArguments(arguments);
		}
		this.arguments = new ArrayList<>();
		if (arguments != null) {
			for (final HDLArgument newValue : arguments) {
				this.arguments.add(newValue);
			}
		}
	}

	public AbstractHDLInstantiation() {
		super();
		this.annotations = new ArrayList<>();
		this.var = null;
		this.arguments = new ArrayList<>();
	}

	protected final ArrayList<HDLAnnotation> annotations;

	/**
	 * Get the annotations field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLAnnotation> getAnnotations() {
		return (ArrayList<HDLAnnotation>) annotations.clone();
	}

	protected Iterable<HDLAnnotation> validateAnnotations(Iterable<HDLAnnotation> annotations) {
		if (annotations == null) {
			return new ArrayList<>();
		}
		return annotations;
	}

	protected final HDLVariable var;

	/**
	 * Get the var field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLVariable getVar() {
		return var;
	}

	protected HDLVariable validateVar(HDLVariable var) {
		if (var == null) {
			throw new IllegalArgumentException("The field var can not be null!");
		}
		return var;
	}

	protected final ArrayList<HDLArgument> arguments;

	/**
	 * Get the arguments field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLArgument> getArguments() {
		return (ArrayList<HDLArgument>) arguments.clone();
	}

	protected Iterable<HDLArgument> validateArguments(Iterable<HDLArgument> arguments) {
		if (arguments == null) {
			return new ArrayList<>();
		}
		return arguments;
	}

	@Nonnull
	public abstract HDLInstantiation setAnnotations(@Nullable Iterable<HDLAnnotation> annotations);

	@Nonnull
	public abstract HDLInstantiation addAnnotations(@Nullable HDLAnnotation annotations);

	@Nonnull
	public abstract HDLInstantiation removeAnnotations(@Nullable HDLAnnotation annotations);

	@Nonnull
	public abstract HDLInstantiation setVar(@Nonnull HDLVariable var);

	@Nonnull
	public abstract HDLInstantiation setArguments(@Nullable Iterable<HDLArgument> arguments);

	@Nonnull
	public abstract HDLInstantiation addArguments(@Nullable HDLArgument arguments);

	@Nonnull
	public abstract HDLInstantiation removeArguments(@Nullable HDLArgument arguments);

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copy();

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLInstantiation copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractHDLInstantiation)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLInstantiation other = (AbstractHDLInstantiation) obj;
		if (annotations == null) {
			if (other.annotations != null) {
				return false;
			}
		} else if (!annotations.equals(other.annotations)) {
			return false;
		}
		if (var == null) {
			if (other.var != null) {
				return false;
			}
		} else if (!var.equals(other.var)) {
			return false;
		}
		if (arguments == null) {
			if (other.arguments != null) {
				return false;
			}
		} else if (!arguments.equals(other.arguments)) {
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
		result = (prime * result) + ((annotations == null) ? 0 : annotations.hashCode());
		result = (prime * result) + ((var == null) ? 0 : var.hashCode());
		result = (prime * result) + ((arguments == null) ? 0 : arguments.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInstantiation()");
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
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateAnnotations(getAnnotations());
		if (getAnnotations() != null) {
			for (final HDLAnnotation o : getAnnotations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateVar(getVar());
		if (getVar() != null) {
			getVar().validateAllFields(this, checkResolve);
		}
		validateArguments(getArguments());
		if (getArguments() != null) {
			for (final HDLArgument o : getArguments()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInstantiation, HDLClass.HDLStatement, HDLClass.HDLObject);
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