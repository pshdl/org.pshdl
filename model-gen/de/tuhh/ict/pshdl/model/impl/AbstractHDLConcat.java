package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLConcat extends HDLObject implements HDLExpression {
	/**
	 * Constructs a new instance of {@link AbstractHDLConcat}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param cats
	 *            the value for cats. Can <b>not</b> be <code>null</code>,
	 *            additionally the collection must contain at least one element.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLConcat(@Nullable IHDLObject container, @Nonnull Iterable<HDLExpression> cats, boolean validate) {
		super(container, validate);
		if (validate) {
			cats = validateCats(cats);
		}
		this.cats = new ArrayList<HDLExpression>();
		if (cats != null) {
			for (HDLExpression newValue : cats) {
				this.cats.add(newValue);
			}
		}
	}

	public AbstractHDLConcat() {
		super();
		this.cats = new ArrayList<HDLExpression>();
	}

	@Visit
	protected final ArrayList<HDLExpression> cats;

	/**
	 * Get the cats field. Can <b>not</b> be <code>null</code>, additionally the
	 * collection must contain at least one element.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLExpression> getCats() {
		return (ArrayList<HDLExpression>) cats.clone();
	}

	protected Iterable<HDLExpression> validateCats(Iterable<HDLExpression> cats) {
		if (cats == null)
			throw new IllegalArgumentException("The field cats can not be null!");
		if (!cats.iterator().hasNext())
			throw new IllegalArgumentException("The field cats must contain at least one item!");
		return cats;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLConcat copy() {
		HDLConcat newObject = new HDLConcat(null, cats, false);
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
	public HDLConcat copyFiltered(CopyFilter filter) {
		ArrayList<HDLExpression> filteredcats = filter.copyContainer("cats", this, cats);
		return filter.postFilter((HDLConcat) this, new HDLConcat(null, filteredcats, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLConcat copyDeepFrozen(IHDLObject container) {
		HDLConcat copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLConcat} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLConcat setContainer(@Nullable IHDLObject container) {
		return (HDLConcat) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getCats()}.
	 * 
	 * @param cats
	 *            sets the new cats of this object. Can <b>not</b> be
	 *            <code>null</code>, additionally the collection must contain at
	 *            least one element.
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat setCats(@Nonnull Iterable<HDLExpression> cats) {
		cats = validateCats(cats);
		HDLConcat res = new HDLConcat(container, cats, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getCats()}.
	 * 
	 * @param newCats
	 *            the value that should be added to the field {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat addCats(@Nonnull HDLExpression newCats) {
		if (newCats == null)
			throw new IllegalArgumentException("Element of cats can not be null!");
		ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.add(newCats);
		HDLConcat res = new HDLConcat(container, cats, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCats()}.
	 * 
	 * @param newCats
	 *            the value that should be removed from the field
	 *            {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat removeCats(@Nonnull HDLExpression newCats) {
		if (newCats == null)
			throw new IllegalArgumentException("Removed element of cats can not be null!");
		ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.remove(newCats);
		HDLConcat res = new HDLConcat(container, cats, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCats()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getCats()}
	 * @return a new instance of {@link HDLConcat} with the updated cats field.
	 */
	@Nonnull
	public HDLConcat removeCats(int idx) {
		ArrayList<HDLExpression> cats = (ArrayList<HDLExpression>) this.cats.clone();
		cats.remove(idx);
		HDLConcat res = new HDLConcat(container, cats, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLConcat))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLConcat other = (AbstractHDLConcat) obj;
		if (cats == null) {
			if (other.cats != null)
				return false;
		} else if (!cats.equals(other.cats))
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
		result = (prime * result) + ((cats == null) ? 0 : cats.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLConcat()");
		if (cats != null) {
			if (cats.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : cats) {
					sb.append(".addCats(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateCats(getCats());
		if (getCats() != null) {
			for (HDLExpression o : getCats()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLConcat, HDLClass.HDLExpression, HDLClass.HDLObject);
	}
}