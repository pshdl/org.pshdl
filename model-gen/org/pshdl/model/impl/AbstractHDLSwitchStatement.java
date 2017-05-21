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
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLSwitchStatement extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLSwitchStatement}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLSwitchStatement(int id, @Nullable IHDLObject container, @Nonnull HDLExpression caseExp, @Nullable Iterable<HDLSwitchCaseStatement> cases, boolean validate) {
		super(id, container, validate);
		if (validate) {
			caseExp = validateCaseExp(caseExp);
		}
		if (caseExp != null) {
			this.caseExp = caseExp;
		} else {
			this.caseExp = null;
		}
		if (validate) {
			cases = validateCases(cases);
		}
		this.cases = new ArrayList<>();
		if (cases != null) {
			for (final HDLSwitchCaseStatement newValue : cases) {
				this.cases.add(newValue);
			}
		}
	}

	public AbstractHDLSwitchStatement() {
		super();
		this.caseExp = null;
		this.cases = new ArrayList<>();
	}

	protected final HDLExpression caseExp;

	/**
	 * Get the caseExp field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLExpression getCaseExp() {
		return caseExp;
	}

	protected HDLExpression validateCaseExp(HDLExpression caseExp) {
		if (caseExp == null) {
			throw new IllegalArgumentException("The field caseExp can not be null!");
		}
		return caseExp;
	}

	protected final ArrayList<HDLSwitchCaseStatement> cases;

	/**
	 * Get the cases field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLSwitchCaseStatement> getCases() {
		return (ArrayList<HDLSwitchCaseStatement>) cases.clone();
	}

	protected Iterable<HDLSwitchCaseStatement> validateCases(Iterable<HDLSwitchCaseStatement> cases) {
		if (cases == null) {
			return new ArrayList<>();
		}
		return cases;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSwitchStatement copy() {
		final HDLSwitchStatement newObject = new HDLSwitchStatement(id, null, caseExp, cases, false);
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
	public HDLSwitchStatement copyFiltered(CopyFilter filter) {
		final HDLExpression filteredcaseExp = filter.copyObject("caseExp", this, caseExp);
		final ArrayList<HDLSwitchCaseStatement> filteredcases = filter.copyContainer("cases", this, cases);
		return filter.postFilter((HDLSwitchStatement) this, new HDLSwitchStatement(id, null, filteredcaseExp, filteredcases, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSwitchStatement copyDeepFrozen(IHDLObject container) {
		final HDLSwitchStatement copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLSwitchStatement} with the updated container field.
	 */
	@Override
	@Nonnull
	public HDLSwitchStatement setContainer(@Nullable IHDLObject container) {
		return (HDLSwitchStatement) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getCaseExp()}.
	 *
	 * @param caseExp
	 *            sets the new caseExp of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLSwitchStatement} with the updated caseExp field.
	 */
	@Nonnull
	public HDLSwitchStatement setCaseExp(@Nonnull HDLExpression caseExp) {
		caseExp = validateCaseExp(caseExp);
		final HDLSwitchStatement res = new HDLSwitchStatement(id, container, caseExp, cases, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getCases()}.
	 *
	 * @param cases
	 *            sets the new cases of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	@Nonnull
	public HDLSwitchStatement setCases(@Nullable Iterable<HDLSwitchCaseStatement> cases) {
		cases = validateCases(cases);
		final HDLSwitchStatement res = new HDLSwitchStatement(id, container, caseExp, cases, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getCases()}.
	 *
	 * @param newCases
	 *            the value that should be added to the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	@Nonnull
	public HDLSwitchStatement addCases(@Nullable HDLSwitchCaseStatement newCases) {
		if (newCases == null) {
			throw new IllegalArgumentException("Element of cases can not be null!");
		}
		final ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.add(newCases);
		final HDLSwitchStatement res = new HDLSwitchStatement(id, container, caseExp, cases, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCases()}.
	 *
	 * @param newCases
	 *            the value that should be removed from the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	@Nonnull
	public HDLSwitchStatement removeCases(@Nullable HDLSwitchCaseStatement newCases) {
		if (newCases == null) {
			throw new IllegalArgumentException("Removed element of cases can not be null!");
		}
		final ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.remove(newCases);
		final HDLSwitchStatement res = new HDLSwitchStatement(id, container, caseExp, cases, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCases()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated cases field.
	 */
	@Nonnull
	public HDLSwitchStatement removeCases(int idx) {
		final ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.remove(idx);
		final HDLSwitchStatement res = new HDLSwitchStatement(id, container, caseExp, cases, false);
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
		if (!(obj instanceof AbstractHDLSwitchStatement)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLSwitchStatement other = (AbstractHDLSwitchStatement) obj;
		if (caseExp == null) {
			if (other.caseExp != null) {
				return false;
			}
		} else if (!caseExp.equals(other.caseExp)) {
			return false;
		}
		if (cases == null) {
			if (other.cases != null) {
				return false;
			}
		} else if (!cases.equals(other.cases)) {
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
		result = (prime * result) + ((caseExp == null) ? 0 : caseExp.hashCode());
		result = (prime * result) + ((cases == null) ? 0 : cases.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLSwitchStatement()");
		if (caseExp != null) {
			sb.append(".setCaseExp(").append(caseExp.toConstructionString(spacing + "\t")).append(")");
		}
		if (cases != null) {
			if (cases.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLSwitchCaseStatement o : cases) {
					sb.append(".addCases(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateCaseExp(getCaseExp());
		if (getCaseExp() != null) {
			getCaseExp().validateAllFields(this, checkResolve);
		}
		validateCases(getCases());
		if (getCases() != null) {
			for (final HDLSwitchCaseStatement o : getCases()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLSwitchStatement, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if (caseExp != null) {
							current = Iterators.concat(Iterators.forArray(caseExp), caseExp.deepIterator());
						}
						break;
					case 1:
						if ((cases != null) && (cases.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(cases.size());
							for (final HDLSwitchCaseStatement o : cases) {
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
						if (caseExp != null) {
							current = Iterators.singletonIterator(caseExp);
						}
						break;
					case 1:
						if ((cases != null) && (cases.size() != 0)) {
							current = cases.iterator();
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