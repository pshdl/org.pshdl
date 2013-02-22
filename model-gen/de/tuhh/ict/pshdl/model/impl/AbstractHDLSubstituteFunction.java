package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

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
	 * @param stmnts
	 *            the value for stmnts. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLSubstituteFunction(@Nullable IHDLObject container, @Nullable ArrayList<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable ArrayList<HDLVariable> args, @Nullable ArrayList<HDLStatement> stmnts, boolean validate) {
		super(container, annotations, name, validate);
		if (validate) {
			args = validateArgs(args);
		}
		this.args = new ArrayList<HDLVariable>();
		if (args != null) {
			for (HDLVariable newValue : args) {
				this.args.add((HDLVariable) newValue);
			}
		}
		if (validate) {
			stmnts = validateStmnts(stmnts);
		}
		this.stmnts = new ArrayList<HDLStatement>();
		if (stmnts != null) {
			for (HDLStatement newValue : stmnts) {
				this.stmnts.add((HDLStatement) newValue);
			}
		}
	}

	public AbstractHDLSubstituteFunction() {
		super();
		this.args = new ArrayList<HDLVariable>();
		this.stmnts = new ArrayList<HDLStatement>();
	}

	@Visit
	protected final ArrayList<HDLVariable> args;

	/**
	 * Get the args field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLVariable> getArgs() {
		return (ArrayList<HDLVariable>) args.clone();
	}

	protected ArrayList<HDLVariable> validateArgs(ArrayList<HDLVariable> args) {
		if (args == null)
			return new ArrayList<HDLVariable>();
		return args;
	}

	@Visit
	protected final ArrayList<HDLStatement> stmnts;

	/**
	 * Get the stmnts field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLStatement> getStmnts() {
		return (ArrayList<HDLStatement>) stmnts.clone();
	}

	protected ArrayList<HDLStatement> validateStmnts(ArrayList<HDLStatement> stmnts) {
		if (stmnts == null)
			return new ArrayList<HDLStatement>();
		return stmnts;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLSubstituteFunction copy() {
		HDLSubstituteFunction newObject = new HDLSubstituteFunction(null, annotations, name, args, stmnts, false);
		copyMetaData(this, newObject, false);
		return newObject;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLSubstituteFunction copyFiltered(CopyFilter filter) {
		ArrayList<HDLAnnotation> filteredannotations = filter.copyContainer("annotations", this, annotations);
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLVariable> filteredargs = filter.copyContainer("args", this, args);
		ArrayList<HDLStatement> filteredstmnts = filter.copyContainer("stmnts", this, stmnts);
		return filter.postFilter((HDLSubstituteFunction) this, new HDLSubstituteFunction(null, filteredannotations, filteredname, filteredargs, filteredstmnts, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Nonnull
	public HDLSubstituteFunction copyDeepFrozen(IHDLObject container) {
		HDLSubstituteFunction copy = copyFiltered(CopyFilter.DEEP_META);
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
	public @Nonnull
	HDLSubstituteFunction setContainer(@Nullable IHDLObject container) {
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
	public @Nonnull
	HDLSubstituteFunction setAnnotations(@Nullable ArrayList<HDLAnnotation> annotations) {
		annotations = validateAnnotations(annotations);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction addAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.add(newAnnotations);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeAnnotations(@Nullable HDLAnnotation newAnnotations) {
		if (newAnnotations == null)
			throw new IllegalArgumentException("Removed element of annotations can not be null!");
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(newAnnotations);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeAnnotations(int idx) {
		ArrayList<HDLAnnotation> annotations = (ArrayList<HDLAnnotation>) this.annotations.clone();
		annotations.remove(idx);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction setName(@Nonnull String name) {
		name = validateName(name);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction setArgs(@Nullable ArrayList<HDLVariable> args) {
		args = validateArgs(args);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction addArgs(@Nullable HDLVariable newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Element of args can not be null!");
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.add(newArgs);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeArgs(@Nullable HDLVariable newArgs) {
		if (newArgs == null)
			throw new IllegalArgumentException("Removed element of args can not be null!");
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.remove(newArgs);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeArgs(int idx) {
		ArrayList<HDLVariable> args = (ArrayList<HDLVariable>) this.args.clone();
		args.remove(idx);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction setStmnts(@Nullable ArrayList<HDLStatement> stmnts) {
		stmnts = validateStmnts(stmnts);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction addStmnts(@Nullable HDLStatement newStmnts) {
		if (newStmnts == null)
			throw new IllegalArgumentException("Element of stmnts can not be null!");
		ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.add(newStmnts);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeStmnts(@Nullable HDLStatement newStmnts) {
		if (newStmnts == null)
			throw new IllegalArgumentException("Removed element of stmnts can not be null!");
		ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.remove(newStmnts);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
	public @Nonnull
	HDLSubstituteFunction removeStmnts(int idx) {
		ArrayList<HDLStatement> stmnts = (ArrayList<HDLStatement>) this.stmnts.clone();
		stmnts.remove(idx);
		HDLSubstituteFunction res = new HDLSubstituteFunction(container, annotations, name, args, stmnts, false);
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
		AbstractHDLSubstituteFunction other = (AbstractHDLSubstituteFunction) obj;
		if (args == null) {
			if (other.args != null)
				return false;
		} else if (!args.equals(other.args))
			return false;
		if (stmnts == null) {
			if (other.stmnts != null)
				return false;
		} else if (!stmnts.equals(other.stmnts))
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
		result = (prime * result) + ((stmnts == null) ? 0 : stmnts.hashCode());
		hashCache = result;
		return result;
	}

	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLSubstituteFunction()");
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
		if (stmnts != null) {
			if (stmnts.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : stmnts) {
					sb.append(".addStmnts(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateArgs(getArgs());
		if (getArgs() != null) {
			for (HDLVariable o : getArgs()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateStmnts(getStmnts());
		if (getStmnts() != null) {
			for (HDLStatement o : getStmnts()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLSubstituteFunction, HDLClass.HDLFunction, HDLClass.HDLDeclaration, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}