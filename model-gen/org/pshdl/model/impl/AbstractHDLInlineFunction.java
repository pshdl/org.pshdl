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
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLInlineFunction extends HDLFunction {
	/**
	 * Constructs a new instance of {@link AbstractHDLInlineFunction}
	 *
	 * @param id
	 *            a unique number for each instance
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
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInlineFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nonnull HDLExpression expr, boolean validate) {
		super(id, container, annotations, name, args, returnType, validate);
		if (validate) {
			expr = validateExpr(expr);
		}
		if (expr != null) {
			this.expr = expr;
		} else {
			this.expr = null;
		}
	}

	public AbstractHDLInlineFunction() {
		super();
		this.expr = null;
	}

	protected final HDLExpression expr;

	/**
	 * Get the expr field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getExpr() {
		return expr;
	}

	protected HDLExpression validateExpr(HDLExpression expr) {
		if (expr == null) {
			throw new IllegalArgumentException("The field expr can not be null!");
		}
		return expr;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction copy() {
		final HDLInlineFunction newObject = new HDLInlineFunction(id, null, annotations, name, args, returnType, expr, false);
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
	public HDLInlineFunction copyFiltered(CopyFilter filter) {
		final ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<HDLFunctionParameter> filteredargs = filter.copyContainer("args", this, args);
		final HDLFunctionParameter filteredreturnType = filter.copyObject("returnType", this, returnType);
		final HDLExpression filteredexpr = filter.copyObject("expr", this, expr);
		return filter.postFilter((HDLInlineFunction) this,
				new HDLInlineFunction(id, null, filteredannotations, filteredname, filteredargs, filteredreturnType, filteredexpr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction copyDeepFrozen(IHDLObject container) {
		final HDLInlineFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLInlineFunction} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setContainer(@Nullable IHDLObject container) {
		return (HDLInlineFunction) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getAnnotations()}.
	 *
	 * @param annotations
	 *            sets the new annotations of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be added to the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null) {
			throw new IllegalArgumentException("Element of annotations can not be null!");
		}
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param newAnnotations
	 *            the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null) {
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		}
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated annotations field.
	 */
	@Nonnull
	public HDLInlineFunction removeAnnotations(int idx) {
		final ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 *
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated name field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setName(@Nonnull String name) {
		name = validateName(name);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArgs()}.
	 *
	 * @param args
	 *            sets the new args of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated args field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setArgs(@Nullable Iterable<HDLFunctionParameter> args) {
		args = validateArgs(args);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArgs()}.
	 *
	 * @param newArgs
	 *            the value that should be added to the field {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction addArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null) {
			throw new IllegalArgumentException("Element of args can not be null!");
		}
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.add(newArgs);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 *
	 * @param newArgs
	 *            the value that should be removed from the field {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction removeArgs(@Nullable HDLFunctionParameter newArgs) {
		if (newArgs == null) {
			throw new IllegalArgumentException("Removed element of args can not be null!");
		}
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(newArgs);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args field.
	 */
	@Nonnull
	public HDLInlineFunction removeArgs(int idx) {
		final ArrayList<HDLFunctionParameter> args = (ArrayList<HDLFunctionParameter>) this.args.clone();
		args.remove(idx);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getReturnType()}.
	 *
	 * @param returnType
	 *            sets the new returnType of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated returnType field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setReturnType(@Nullable HDLFunctionParameter returnType) {
		returnType = validateReturnType(returnType);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getExpr()}.
	 *
	 * @param expr
	 *            sets the new expr of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated expr field.
	 */
	@Nonnull
	public HDLInlineFunction setExpr(@Nonnull HDLExpression expr) {
		expr = validateExpr(expr);
		final HDLInlineFunction res = new HDLInlineFunction(id, container, annotations, name, args, returnType, expr, false);
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
		if (!(obj instanceof AbstractHDLInlineFunction)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLInlineFunction other = (AbstractHDLInlineFunction) obj;
		if (expr == null) {
			if (other.expr != null) {
				return false;
			}
		} else if (!expr.equals(other.expr)) {
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
		result = (prime * result) + ((expr == null) ? 0 : expr.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInlineFunction()");
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
		if (expr != null) {
			sb.append(".setExpr(").append(expr.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateExpr(getExpr());
		if (getExpr() != null) {
			getExpr().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInlineFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if (expr != null) {
							current = Iterators.concat(Iterators.forArray(expr), expr.deepIterator());
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
						if (expr != null) {
							current = Iterators.singletonIterator(expr);
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