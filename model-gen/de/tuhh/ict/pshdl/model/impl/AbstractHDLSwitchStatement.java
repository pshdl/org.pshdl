package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLSwitchStatement extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLSwitchStatement}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param caseExp
	 *            the value for caseExp. Can <b>not</b> be <code>null</code>.
	 * @param cases
	 *            the value for cases. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLSwitchStatement(@Nullable IHDLObject container, @Nonnull HDLExpression caseExp, @Nullable ArrayList<HDLSwitchCaseStatement> cases, boolean validate) {
		super(container, validate);
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
		this.cases = new ArrayList<HDLSwitchCaseStatement>();
		if (cases != null) {
			for (HDLSwitchCaseStatement newValue : cases) {
				this.cases.add(newValue);
			}
		}
	}

	public AbstractHDLSwitchStatement() {
		super();
		this.caseExp = null;
		this.cases = new ArrayList<HDLSwitchCaseStatement>();
	}

	@Visit
	protected final HDLExpression caseExp;

	/**
	 * Get the caseExp field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	HDLExpression getCaseExp() {
		return caseExp;
	}

	protected HDLExpression validateCaseExp(HDLExpression caseExp) {
		if (caseExp == null)
			throw new IllegalArgumentException("The field caseExp can not be null!");
		return caseExp;
	}

	@Visit
	protected final ArrayList<HDLSwitchCaseStatement> cases;

	/**
	 * Get the cases field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLSwitchCaseStatement> getCases() {
		return (ArrayList<HDLSwitchCaseStatement>) cases.clone();
	}

	protected ArrayList<HDLSwitchCaseStatement> validateCases(ArrayList<HDLSwitchCaseStatement> cases) {
		if (cases == null)
			return new ArrayList<HDLSwitchCaseStatement>();
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
		HDLSwitchStatement newObject = new HDLSwitchStatement(null, caseExp, cases, false);
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
		HDLExpression filteredcaseExp = filter.copyObject("caseExp", this, caseExp);
		ArrayList<HDLSwitchCaseStatement> filteredcases = filter.copyContainer("cases", this, cases);
		return filter.postFilter((HDLSwitchStatement) this, new HDLSwitchStatement(null, filteredcaseExp, filteredcases, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLSwitchStatement copyDeepFrozen(IHDLObject container) {
		HDLSwitchStatement copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLSwitchStatement} with the updated
	 *         container field.
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
	 *            sets the new caseExp of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLSwitchStatement} with the updated
	 *         caseExp field.
	 */
	@Nonnull
	public HDLSwitchStatement setCaseExp(@Nonnull HDLExpression caseExp) {
		caseExp = validateCaseExp(caseExp);
		HDLSwitchStatement res = new HDLSwitchStatement(container, caseExp, cases, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getCases()}.
	 * 
	 * @param cases
	 *            sets the new cases of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLSwitchStatement} with the updated
	 *         cases field.
	 */
	@Nonnull
	public HDLSwitchStatement setCases(@Nullable ArrayList<HDLSwitchCaseStatement> cases) {
		cases = validateCases(cases);
		HDLSwitchStatement res = new HDLSwitchStatement(container, caseExp, cases, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getCases()}.
	 * 
	 * @param newCases
	 *            the value that should be added to the field
	 *            {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated
	 *         cases field.
	 */
	@Nonnull
	public HDLSwitchStatement addCases(@Nullable HDLSwitchCaseStatement newCases) {
		if (newCases == null)
			throw new IllegalArgumentException("Element of cases can not be null!");
		ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.add(newCases);
		HDLSwitchStatement res = new HDLSwitchStatement(container, caseExp, cases, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCases()}.
	 * 
	 * @param newCases
	 *            the value that should be removed from the field
	 *            {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated
	 *         cases field.
	 */
	@Nonnull
	public HDLSwitchStatement removeCases(@Nullable HDLSwitchCaseStatement newCases) {
		if (newCases == null)
			throw new IllegalArgumentException("Removed element of cases can not be null!");
		ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.remove(newCases);
		HDLSwitchStatement res = new HDLSwitchStatement(container, caseExp, cases, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getCases()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getCases()}
	 * @return a new instance of {@link HDLSwitchStatement} with the updated
	 *         cases field.
	 */
	@Nonnull
	public HDLSwitchStatement removeCases(int idx) {
		ArrayList<HDLSwitchCaseStatement> cases = (ArrayList<HDLSwitchCaseStatement>) this.cases.clone();
		cases.remove(idx);
		HDLSwitchStatement res = new HDLSwitchStatement(container, caseExp, cases, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLSwitchStatement))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLSwitchStatement other = (AbstractHDLSwitchStatement) obj;
		if (caseExp == null) {
			if (other.caseExp != null)
				return false;
		} else if (!caseExp.equals(other.caseExp))
			return false;
		if (cases == null) {
			if (other.cases != null)
				return false;
		} else if (!cases.equals(other.cases))
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
		result = (prime * result) + ((caseExp == null) ? 0 : caseExp.hashCode());
		result = (prime * result) + ((cases == null) ? 0 : cases.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLSwitchStatement()");
		if (caseExp != null) {
			sb.append(".setCaseExp(").append(caseExp.toConstructionString(spacing + "\t")).append(")");
		}
		if (cases != null)
			if (cases.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLSwitchCaseStatement o : cases) {
					sb.append(".addCases(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
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
			for (HDLSwitchCaseStatement o : getCases()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLSwitchStatement, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}