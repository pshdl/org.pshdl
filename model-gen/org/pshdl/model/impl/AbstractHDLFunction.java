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
public abstract class AbstractHDLFunction extends HDLDeclaration {
	/**
	 * Constructs a new instance of {@link AbstractHDLFunction}
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
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, boolean validate) {
		super(id, container, annotations, validate);
		if (validate) {
			name = validateName(name);
		}
		this.name = name;
		if (validate) {
			args = validateArgs(args);
		}
		this.args = new ArrayList<HDLFunctionParameter>();
		if (args != null) {
			for (HDLFunctionParameter newValue : args) {
				this.args.add(newValue);
			}
		}
		if (validate) {
			returnType = validateReturnType(returnType);
		}
		if (returnType != null) {
			this.returnType = returnType;
		} else {
			this.returnType = null;
		}
	}

	public AbstractHDLFunction() {
		super();
		this.name = null;
		this.args = new ArrayList<HDLFunctionParameter>();
		this.returnType = null;
	}

	protected final String name;

	/**
	 * Get the name field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public String getName() {
		return name;
	}

	protected String validateName(String name) {
		if (name == null)
			throw new IllegalArgumentException("The field name can not be null!");
		return name;
	}

	protected final ArrayList<HDLFunctionParameter> args;

	/**
	 * Get the args field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLFunctionParameter> getArgs() {
		return (ArrayList<HDLFunctionParameter>) args.clone();
	}

	protected Iterable<HDLFunctionParameter> validateArgs(Iterable<HDLFunctionParameter> args) {
		if (args == null)
			return new ArrayList<HDLFunctionParameter>();
		return args;
	}

	protected final HDLFunctionParameter returnType;

	/**
	 * Get the returnType field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public HDLFunctionParameter getReturnType() {
		return returnType;
	}

	protected HDLFunctionParameter validateReturnType(HDLFunctionParameter returnType) {
		return returnType;
	}

	@Nonnull
	public abstract HDLFunction setName(@Nonnull String name);

	@Nonnull
	public abstract HDLFunction setArgs(@Nullable Iterable<HDLFunctionParameter> args);

	@Nonnull
	public abstract HDLFunction addArgs(@Nullable HDLFunctionParameter args);

	@Nonnull
	public abstract HDLFunction removeArgs(@Nullable HDLFunctionParameter args);

	@Nonnull
	public abstract HDLFunction setReturnType(@Nullable HDLFunctionParameter returnType);

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLFunction copy();

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLFunction copyFiltered(CopyFilter filter);

	/**
	 * Creates a deep copy of this class with the same fields and frozen
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public abstract HDLFunction copyDeepFrozen(IHDLObject container);

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLFunction))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLFunction other = (AbstractHDLFunction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
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
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + ((args == null) ? 0 : args.hashCode());
		result = (prime * result) + ((returnType == null) ? 0 : returnType.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLFunction()");
		if (annotations != null) {
			if (annotations.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLAnnotation o : annotations) {
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
				for (HDLFunctionParameter o : args) {
					sb.append(".addArgs(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (returnType != null) {
			sb.append(".setReturnType(").append(returnType.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateName(getName());
		validateArgs(getArgs());
		if (getArgs() != null) {
			for (HDLFunctionParameter o : getArgs()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateReturnType(getReturnType());
		if (getReturnType() != null) {
			getReturnType().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if ((args != null) && (args.size() != 0)) {
							List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(args.size());
							for (HDLFunctionParameter o : args) {
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