package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

@SuppressWarnings("all")
public abstract class AbstractHDLBlock extends HDLObject implements HDLStatement {
	/**
	 * Constructs a new instance of {@link AbstractHDLBlock}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param process
	 *            the value for process. Can <b>not</b> be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLBlock(@Nullable IHDLObject container, @Nonnull Boolean process, @Nullable ArrayList<HDLStatement> statements, boolean validate) {
		super(container, validate);
		if (validate) {
			process = validateProcess(process);
		}
		this.process = process;
		if (validate) {
			statements = validateStatements(statements);
		}
		this.statements = new ArrayList<HDLStatement>();
		if (statements != null) {
			for (HDLStatement newValue : statements) {
				this.statements.add(newValue);
			}
		}
	}

	public AbstractHDLBlock() {
		super();
		this.process = null;
		this.statements = new ArrayList<HDLStatement>();
	}

	protected final Boolean process;

	/**
	 * Get the process field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	public @Nonnull
	Boolean getProcess() {
		return process;
	}

	protected Boolean validateProcess(Boolean process) {
		if (process == null)
			throw new IllegalArgumentException("The field process can not be null!");
		return process;
	}

	@Visit
	protected final ArrayList<HDLStatement> statements;

	/**
	 * Get the statements field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	public @Nonnull
	ArrayList<HDLStatement> getStatements() {
		return (ArrayList<HDLStatement>) statements.clone();
	}

	protected ArrayList<HDLStatement> validateStatements(ArrayList<HDLStatement> statements) {
		if (statements == null)
			return new ArrayList<HDLStatement>();
		return statements;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLBlock copy() {
		HDLBlock newObject = new HDLBlock(null, process, statements, false);
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
	public HDLBlock copyFiltered(CopyFilter filter) {
		Boolean filteredprocess = filter.copyObject("process", this, process);
		ArrayList<HDLStatement> filteredstatements = filter.copyContainer("statements", this, statements);
		return filter.postFilter((HDLBlock) this, new HDLBlock(null, filteredprocess, filteredstatements, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLBlock copyDeepFrozen(IHDLObject container) {
		HDLBlock copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLBlock} with the updated container
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLBlock setContainer(@Nullable IHDLObject container) {
		return (HDLBlock) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getProcess()}.
	 * 
	 * @param process
	 *            sets the new process of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated process
	 *         field.
	 */
	@Nonnull
	public HDLBlock setProcess(@Nonnull Boolean process) {
		process = validateProcess(process);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getProcess()}.
	 * 
	 * @param process
	 *            sets the new process of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated process
	 *         field.
	 */
	@Nonnull
	public HDLBlock setProcess(boolean process) {
		process = validateProcess(process);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStatements()}.
	 * 
	 * @param statements
	 *            sets the new statements of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLBlock setStatements(@Nullable ArrayList<HDLStatement> statements) {
		statements = validateStatements(statements);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be added to the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLBlock addStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.add(newStatements);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param newStatements
	 *            the value that should be removed from the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLBlock removeStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null)
			throw new IllegalArgumentException("Removed element of statements can not be null!");
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(newStatements);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements
	 *         field.
	 */
	@Nonnull
	public HDLBlock removeStatements(int idx) {
		ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(idx);
		HDLBlock res = new HDLBlock(container, process, statements, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLBlock))
			return false;
		if (!super.equals(obj))
			return false;
		AbstractHDLBlock other = (AbstractHDLBlock) obj;
		if (process == null) {
			if (other.process != null)
				return false;
		} else if (!process.equals(other.process))
			return false;
		if (statements == null) {
			if (other.statements != null)
				return false;
		} else if (!statements.equals(other.statements))
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
		result = (prime * result) + ((process == null) ? 0 : process.hashCode());
		result = (prime * result) + ((statements == null) ? 0 : statements.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLBlock()");
		if (process != null) {
			sb.append(".setProcess(").append(process).append(")");
		}
		if (statements != null)
			if (statements.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLStatement o : statements) {
					sb.append(".addStatements(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateProcess(getProcess());
		validateStatements(getStatements());
		if (getStatements() != null) {
			for (HDLStatement o : getStatements()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLBlock, HDLClass.HDLStatement, HDLClass.HDLObject);
	}
}