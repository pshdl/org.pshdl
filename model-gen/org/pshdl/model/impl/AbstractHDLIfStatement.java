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

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLCompound;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLIfStatement extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLIfStatement}
	 *
	 * @param id
	 *            a unique number for each instance
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
	public AbstractHDLIfStatement(int id, @Nullable IHDLObject container, @Nonnull HDLExpression ifExp, @Nullable Iterable<HDLStatement> thenDo,
			@Nullable Iterable<HDLStatement> elseDo, boolean validate) {
		super(id, container, validate);
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
		this.thenDo = new ArrayList<>();
		if (thenDo != null) {
			for (final HDLStatement newValue : thenDo) {
				this.thenDo.add(newValue);
			}
		}
		if (validate) {
			elseDo = validateElseDo(elseDo);
		}
		this.elseDo = new ArrayList<>();
		if (elseDo != null) {
			for (final HDLStatement newValue : elseDo) {
				this.elseDo.add(newValue);
			}
		}
	}

	public AbstractHDLIfStatement() {
		super();
		this.ifExp = null;
		this.thenDo = new ArrayList<>();
		this.elseDo = new ArrayList<>();
	}

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
		if (ifExp == null) {
			throw new IllegalArgumentException("The field ifExp can not be null!");
		}
		return ifExp;
	}

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
		if (thenDo == null) {
			return new ArrayList<>();
		}
		return thenDo;
	}

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
		if (elseDo == null) {
			return new ArrayList<>();
		}
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
		final HDLIfStatement newObject = new HDLIfStatement(id, null, ifExp, thenDo, elseDo, false);
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
		final HDLExpression filteredifExp = filter.copyObject("ifExp", this, ifExp);
		final ArrayList<HDLStatement> filteredthenDo = filter.copyContainer("thenDo", this, thenDo);
		final ArrayList<HDLStatement> filteredelseDo = filter.copyContainer("elseDo", this, elseDo);
		return filter.postFilter((HDLIfStatement) this, new HDLIfStatement(id, null, filteredifExp, filteredthenDo, filteredelseDo, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLIfStatement copyDeepFrozen(IHDLObject container) {
		final HDLIfStatement copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLIfStatement} with the updated container field.
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
	 *            sets the new ifExp of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated ifExp field.
	 */
	@Nonnull
	public HDLIfStatement setIfExp(@Nonnull HDLExpression ifExp) {
		ifExp = validateIfExp(ifExp);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getThenDo()}.
	 *
	 * @param thenDo
	 *            sets the new thenDo of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	@Nonnull
	public HDLIfStatement setThenDo(@Nullable Iterable<HDLStatement> thenDo) {
		thenDo = validateThenDo(thenDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getThenDo()}.
	 *
	 * @param newThenDo
	 *            the value that should be added to the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	@Nonnull
	public HDLIfStatement addThenDo(@Nullable HDLStatement newThenDo) {
		if (newThenDo == null) {
			throw new IllegalArgumentException("Element of thenDo can not be null!");
		}
		final ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.add(newThenDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 *
	 * @param newThenDo
	 *            the value that should be removed from the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	@Nonnull
	public HDLIfStatement removeThenDo(@Nullable HDLStatement newThenDo) {
		if (newThenDo == null) {
			throw new IllegalArgumentException("Removed element of thenDo can not be null!");
		}
		final ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.remove(newThenDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getThenDo()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getThenDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated thenDo field.
	 */
	@Nonnull
	public HDLIfStatement removeThenDo(int idx) {
		final ArrayList<HDLStatement> thenDo = (ArrayList<HDLStatement>) this.thenDo.clone();
		thenDo.remove(idx);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getElseDo()}.
	 *
	 * @param elseDo
	 *            sets the new elseDo of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	@Nonnull
	public HDLIfStatement setElseDo(@Nullable Iterable<HDLStatement> elseDo) {
		elseDo = validateElseDo(elseDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getElseDo()}.
	 *
	 * @param newElseDo
	 *            the value that should be added to the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	@Nonnull
	public HDLIfStatement addElseDo(@Nullable HDLStatement newElseDo) {
		if (newElseDo == null) {
			throw new IllegalArgumentException("Element of elseDo can not be null!");
		}
		final ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.add(newElseDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 *
	 * @param newElseDo
	 *            the value that should be removed from the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	@Nonnull
	public HDLIfStatement removeElseDo(@Nullable HDLStatement newElseDo) {
		if (newElseDo == null) {
			throw new IllegalArgumentException("Removed element of elseDo can not be null!");
		}
		final ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.remove(newElseDo);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getElseDo()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getElseDo()}
	 * @return a new instance of {@link HDLIfStatement} with the updated elseDo field.
	 */
	@Nonnull
	public HDLIfStatement removeElseDo(int idx) {
		final ArrayList<HDLStatement> elseDo = (ArrayList<HDLStatement>) this.elseDo.clone();
		elseDo.remove(idx);
		final HDLIfStatement res = new HDLIfStatement(id, container, ifExp, thenDo, elseDo, false);
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
		if (!(obj instanceof AbstractHDLIfStatement)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLIfStatement other = (AbstractHDLIfStatement) obj;
		if (ifExp == null) {
			if (other.ifExp != null) {
				return false;
			}
		} else if (!ifExp.equals(other.ifExp)) {
			return false;
		}
		if (thenDo == null) {
			if (other.thenDo != null) {
				return false;
			}
		} else if (!thenDo.equals(other.thenDo)) {
			return false;
		}
		if (elseDo == null) {
			if (other.elseDo != null) {
				return false;
			}
		} else if (!elseDo.equals(other.elseDo)) {
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
		result = (prime * result) + ((ifExp == null) ? 0 : ifExp.hashCode());
		result = (prime * result) + ((thenDo == null) ? 0 : thenDo.hashCode());
		result = (prime * result) + ((elseDo == null) ? 0 : elseDo.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLIfStatement()");
		if (ifExp != null) {
			sb.append(".setIfExp(").append(ifExp.toConstructionString(spacing + "\t")).append(")");
		}
		if (thenDo != null) {
			if (thenDo.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : thenDo) {
					sb.append(".addThenDo(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (elseDo != null) {
			if (elseDo.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : elseDo) {
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
			for (final HDLStatement o : getThenDo()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateElseDo(getElseDo());
		if (getElseDo() != null) {
			for (final HDLStatement o : getElseDo()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLIfStatement, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if (ifExp != null) {
							current = Iterators.concat(Iterators.forArray(ifExp), ifExp.deepIterator());
						}
						break;
					case 1:
						if ((thenDo != null) && (thenDo.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(thenDo.size());
							for (final HDLStatement o : thenDo) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 2:
						if ((elseDo != null) && (elseDo.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(elseDo.size());
							for (final HDLStatement o : elseDo) {
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
						if (ifExp != null) {
							current = Iterators.singletonIterator(ifExp);
						}
						break;
					case 1:
						if ((thenDo != null) && (thenDo.size() != 0)) {
							current = thenDo.iterator();
						}
						break;
					case 2:
						if ((elseDo != null) && (elseDo.size() != 0)) {
							current = elseDo.iterator();
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