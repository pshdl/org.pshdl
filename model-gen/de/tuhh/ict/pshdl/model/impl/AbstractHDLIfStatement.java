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
package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLIfStatement extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLIfStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExp
	 *            the value for ifExp. Can <b>not</b> be <code>null</code>.
	 * @param thenDo
	 *            the value for thenDo. Can be <code>null</code>.
	 * @param elseDo
	 *            the value for elseDo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLIfStatement(@Nullable IHDLObject container, @Nonnull HDLExpression ifExp, @Nullable Iterable<HDLStatement> thenDo, @Nullable Iterable<HDLStatement> elseDo,
			boolean validate) {
		super(container, validate);
		if (validate) {
			ifExp = validateIfExp(ifExp);
		}
		if (ifExp != null) {
			this.ifExp = ifExp;
		} else {
			this.ifExp = null;
		}
		if (validate) {
			thenDo = validateThenDo(thenDo);
		}
		this.thenDo = new ArrayList<HDLStatement>();
		if (thenDo != null) {
			for (HDLStatement newValue : thenDo) {
				this.thenDo.add(newValue);
			}
		}
		if (validate) {
			elseDo = validateElseDo(elseDo);
		}
		this.elseDo = new ArrayList<HDLStatement>();
		if (elseDo != null) {
			for (HDLStatement newValue : elseDo) {
				this.elseDo.add(newValue);
			}
		}
	}

	public AbstractHDLIfStatement() {
		super();
		this.ifExp = null;
		this.thenDo = new ArrayList<HDLStatement>();
		this.elseDo = new ArrayList<HDLStatement>();
	}

	@Visit
	protected final HDLExpression ifExp;

	/**
	 * Get the ifExp field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getIfExp() {
		return ifExp;
	}

	protected HDLExpression validateIfExp(HDLExpression ifExp) {
		if (ifExp == null)
			throw new IllegalArgumentException("The field ifExp can not be null!");
		return ifExp;
	}

	@Visit
	protected final ArrayList<HDLStatement> thenDo;

	/**
	 * Get the thenDo field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getThenDo() {
		return (ArrayList<HDLStatement>) thenDo.clone();
	}

	protected Iterable<HDLStatement> validateThenDo(Iterable<HDLStatement> thenDo) {
		if (thenDo == null)
			return new ArrayList<HDLStatement>();
		return thenDo;
	}

	@Visit
	protected final ArrayList<HDLStatement> elseDo;

	/**
	 * Get the elseDo field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getElseDo() {
		return (ArrayList<HDLStatement>) elseDo.clone();
	}

	protected Iterable<HDLStatement> validateElseDo(Iterable<HDLStatement> elseDo) {
		if (elseDo == null)
			return new ArrayList<HDLStatement>();
		return elseDo;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLIfStatement copy() {
		HDLIfStatement newObject = new HDLIfStatement(null, ifExp, thenDo, elseDo, false);
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
	public HDLIfStatement copyFiltered(CopyFilter filter) {
		HDLExpression filteredifExp = filter.copyObject("ifExp", this, ifExp);
		ArrayList<HDLStatement> filteredthenDo = filter.copyContainer("thenDo", this, thenDo);
		ArrayList<HDLStatement> filteredelseDo = filter.copyContainer("elseDo", this, elseDo);
		return filter.postFilter((HDLIfStatement) this, new HDLIfStatement(null, filteredifExp, filteredthenDo, filteredelseDo, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLIfStatement copyDeepFrozen(IHDLObject container) {
		HDLIfStatement copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLIfStatement} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLIfStatement setContainer(@Nullable IHDLObject container) {
		return (HDLIfStatement) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getIfExp()}.
	 * 
	 * @param ifExp
	 *            sets the new ifExp of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated ifExp
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement setIfExp(@Nonnull HDLExpression ifExp) {
		ifExp = validateIfExp(ifExp);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getThenDo()}.
	 * 
	 * @param thenDo
	 *            sets the new thenDo of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement setThenDo(@Nullable Iterable<HDLStatement> thenDo) {
		thenDo = validateThenDo(thenDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getThenDo()}.
	 * 
	 * @param newThenDo
	 *            the value that should be added to the field
	 *            {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement addThenDo(@Nullable HDLStatement newThenDo) {
		if (newThenDo == null)
			throw new IllegalArgumentException("Element of thenDo can not be null!");
		ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.add(newThenDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 * 
	 * @param newThenDo
	 *            the value that should be removed from the field
	 *            {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement removeThenDo(@Nullable HDLStatement newThenDo) {
		if (newThenDo == null)
			throw new IllegalArgumentException("Removed element of thenDo can not be null!");
		ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.remove(newThenDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement removeThenDo(int idx) {
		ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.remove(idx);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getElseDo()}.
	 * 
	 * @param elseDo
	 *            sets the new elseDo of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement setElseDo(@Nullable Iterable<HDLStatement> elseDo) {
		elseDo = validateElseDo(elseDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getElseDo()}.
	 * 
	 * @param newElseDo
	 *            the value that should be added to the field
	 *            {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement addElseDo(@Nullable HDLStatement newElseDo) {
		if (newElseDo == null)
			throw new IllegalArgumentException("Element of elseDo can not be null!");
		ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.add(newElseDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 * 
	 * @param newElseDo
	 *            the value that should be removed from the field
	 *            {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement removeElseDo(@Nullable HDLStatement newElseDo) {
		if (newElseDo == null)
			throw new IllegalArgumentException("Removed element of elseDo can not be null!");
		ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.remove(newElseDo);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo
	 *         field.
	 */
	@Nonnull
	public HDLIfStatement removeElseDo(int idx) {
		ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.remove(idx);
		HDLIfStatement res = new HDLIfStatement(container, ifExp, thenDo, elseDo, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLIfStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLIfStatement other = (AbstractHDLIfStatement) obj;
		if (ifExp == null) {
			if (other.ifExp != null)
				return false;
		} else if (!ifExp.equals(other.ifExp))
			return false;
		if (thenDo == null) {
			if (other.thenDo != null)
				return false;
		} else if (!thenDo.equals(other.thenDo))
			return false;
		if (elseDo == null) {
			if (other.elseDo != null)
				return false;
		} else if (!elseDo.equals(other.elseDo))
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
		result = (prime * result) + ((ifExp == null) ? 0 : ifExp.hashCode());
		result = (prime * result) + ((thenDo == null) ? 0 : thenDo.hashCode());
		result = (prime * result) + ((elseDo == null) ? 0 : elseDo.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLIfStatement()");
		if (ifExp != null) {
			sb.append(".setIfExp(").append(ifExp.toConstructionString(spacing + "\t")).append(")");
		}
		if (thenDo != null) {
			if (thenDo.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : thenDo) {
					sb.append(".addThenDo(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (elseDo != null) {
			if (elseDo.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : elseDo) {
					sb.append(".addElseDo(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateIfExp(getIfExp());
		if (getIfExp() != null) {
			getIfExp().validateAllFields(this, checkResolve);
		}
		validateThenDo(getThenDo());
		if (getThenDo() != null) {
			for (HDLStatement o : getThenDo()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateElseDo(getElseDo());
		if (getElseDo() != null) {
			for (HDLStatement o : getElseDo()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLIfStatement, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}
