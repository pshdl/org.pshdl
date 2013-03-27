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
import org.pshdl.model.utils.HDLIterator.*;

@SuppressWarnings("all")
public abstract class AbstractHDLInlineFunction extends HDLFunction {
	/**
	 * Constructs a new instance of {@link AbstractHDLInlineFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInlineFunction(@Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name, @Nullable Iterable<HDLVariable> args,
			@Nonnull HDLExpression expr, boolean validate) {
		super(container, annotations, name, validate);
		if (validate) {
			args = validateArgs(args);
		}
		this.args = new ArrayList<HDLVariable>();
		if (args != null) {
			for (HDLVariable newValue : args) {
				this.args.add(newValue);
			}
		}
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
		this.args = new ArrayList<HDLVariable>();
		this.expr = null;
	}

	@Visit
	protected final ArrayList<HDLVariable> args;

	/**
	 * Get the args field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLVariable> getArgs() {
		return (ArrayList<HDLVariable>) args.clone();
	}

	protected Iterable<HDLVariable> validateArgs(Iterable<HDLVariable> args) {
		if (args == null)
			return new ArrayList<HDLVariable>();
		return args;
	}

	@Visit
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
		if (expr == null)
			throw new IllegalArgumentException("The field expr can not be null!");
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
		HDLInlineFunction newObject = new HDLInlineFunction(null, annotations, name, args, expr, false);
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
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLVariable> filteredargs = filter.copyContainer("args", this, args);
		HDLExpression filteredexpr = filter.copyObject("expr", this, expr);
		return filter.postFilter((HDLInlineFunction) this, new HDLInlineFunction(null, filteredannotations, filteredname, filteredargs, filteredexpr, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction copyDeepFrozen(IHDLObject container) {
		HDLInlineFunction copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInlineFunction} with the updated
	 *         container field.
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
	 *            sets the new annotations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setAnnotations(@Nullable Iterable<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be added to the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param newAnnotations
	 *            the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated
	 *         annotations field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getAnnotations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getAnnotations()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated
	 *         annotations field.
	 */
	@Nonnull
	public HDLInlineFunction removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated name
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInlineFunction setName(@Nonnull String name) {
		name = validateName(name);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArgs()}.
	 * 
	 * @param args
	 *            sets the new args of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated args
	 *         field.
	 */
	@Nonnull
	public HDLInlineFunction setArgs(@Nullable Iterable<HDLVariable> args) {
		args = validateArgs(args);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be added to the field {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args
	 *         field.
	 */
	@Nonnull
	public HDLInlineFunction addArgs(@Nullable HDLVariable newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Element of args can not be null!");
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.add(newArgs);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param newArgs
	 *            the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args
	 *         field.
	 */
	@Nonnull
	public HDLInlineFunction removeArgs(@Nullable HDLVariable newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Removed element of args can not be null!");
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.remove(newArgs);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArgs()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArgs()}
	 * @return a new instance of {@link HDLInlineFunction} with the updated args
	 *         field.
	 */
	@Nonnull
	public HDLInlineFunction removeArgs(int idx) {
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.remove(idx);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getExpr()}.
	 * 
	 * @param expr
	 *            sets the new expr of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInlineFunction} with the updated expr
	 *         field.
	 */
	@Nonnull
	public HDLInlineFunction setExpr(@Nonnull HDLExpression expr) {
		expr = validateExpr(expr);
		HDLInlineFunction res = new HDLInlineFunction(container, annotations, name, args, expr, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInlineFunction))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLInlineFunction other = (AbstractHDLInlineFunction) obj;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (expr == null) {
			if (other.expr != null)
				return false;
		} else if (!expr.equals(other.expr))
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
		result = (prime * result) + ((args == null) ? 0 : args.hashCode());
		result = (prime * result) + ((expr == null) ? 0 : expr.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInlineFunction()");
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
				for (HDLVariable o : args) {
					sb.append(".addArgs(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (expr != null) {
			sb.append(".setExpr(").append(expr.toConstructionString(spacing + "\t")).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateArgs(getArgs());
		if (getArgs() != null) {
			for (HDLVariable o : getArgs()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateExpr(getExpr());
		if (getExpr() != null) {
			getExpr().validateAllFields(this, checkResolve);
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInlineFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}
