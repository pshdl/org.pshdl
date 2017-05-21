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

import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLCompound;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLBlock extends HDLCompound {
	/**
	 * Constructs a new instance of {@link AbstractHDLBlock}
	 *
	 * @param id
	 *            a unique number for each instance
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param process
	 *            the value for process. Can <b>not</b> be <code>null</code>.
	 * @param statements
	 *            the value for statements. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLBlock(int id, @Nullable IHDLObject container, @Nonnull Boolean process, @Nullable Iterable<HDLStatement> statements, boolean validate) {
		super(id, container, validate);
		if (validate) {
			process = validateProcess(process);
		}
		this.process = process;
		if (validate) {
			statements = validateStatements(statements);
		}
		this.statements = new ArrayList<>();
		if (statements != null) {
			for (final HDLStatement newValue : statements) {
				this.statements.add(newValue);
			}
		}
	}

	public AbstractHDLBlock() {
		super();
		this.process = null;
		this.statements = new ArrayList<>();
	}

	protected final Boolean process;

	/**
	 * Get the process field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public Boolean getProcess() {
		return process;
	}

	protected Boolean validateProcess(Boolean process) {
		if (process == null) {
			throw new IllegalArgumentException("The field process can not be null!");
		}
		return process;
	}

	protected final ArrayList<HDLStatement> statements;

	/**
	 * Get the statements field. Can be <code>null</code>.
	 *
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLStatement> getStatements() {
		return (ArrayList<HDLStatement>) statements.clone();
	}

	protected Iterable<HDLStatement> validateStatements(Iterable<HDLStatement> statements) {
		if (statements == null) {
			return new ArrayList<>();
		}
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
		final HDLBlock newObject = new HDLBlock(id, null, process, statements, false);
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
		final Boolean filteredprocess = filter.copyObject("process", this, process);
		final ArrayList<HDLStatement> filteredstatements = filter.copyContainer("statements", this, statements);
		return filter.postFilter((HDLBlock) this, new HDLBlock(id, null, filteredprocess, filteredstatements, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLBlock copyDeepFrozen(IHDLObject container) {
		final HDLBlock copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be <code>null</code>.
	 * @return the same instance of {@link HDLBlock} with the updated container field.
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
	 *            sets the new process of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated process field.
	 */
	@Nonnull
	public HDLBlock setProcess(@Nonnull Boolean process) {
		process = validateProcess(process);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getProcess()}.
	 *
	 * @param process
	 *            sets the new process of this object. Can <b>not</b> be <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated process field.
	 */
	@Nonnull
	public HDLBlock setProcess(boolean process) {
		process = validateProcess(process);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getStatements()}.
	 *
	 * @param statements
	 *            sets the new statements of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLBlock} with the updated statements field.
	 */
	@Nonnull
	public HDLBlock setStatements(@Nullable Iterable<HDLStatement> statements) {
		statements = validateStatements(statements);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getStatements()}.
	 *
	 * @param newStatements
	 *            the value that should be added to the field {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements field.
	 */
	@Nonnull
	public HDLBlock addStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null) {
			throw new IllegalArgumentException("Element of statements can not be null!");
		}
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.add(newStatements);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 *
	 * @param newStatements
	 *            the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements field.
	 */
	@Nonnull
	public HDLBlock removeStatements(@Nullable HDLStatement newStatements) {
		if (newStatements == null) {
			throw new IllegalArgumentException("Removed element of statements can not be null!");
		}
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(newStatements);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getStatements()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field {@link #getStatements()}
	 * @return a new instance of {@link HDLBlock} with the updated statements field.
	 */
	@Nonnull
	public HDLBlock removeStatements(int idx) {
		final ArrayList<HDLStatement> statements = (ArrayList<HDLStatement>) this.statements.clone();
		statements.remove(idx);
		final HDLBlock res = new HDLBlock(id, container, process, statements, false);
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
		if (!(obj instanceof AbstractHDLBlock)) {
			return false;
		}
		if (!super.equals(obj)) {
			return false;
		}
		final AbstractHDLBlock other = (AbstractHDLBlock) obj;
		if (process == null) {
			if (other.process != null) {
				return false;
			}
		} else if (!process.equals(other.process)) {
			return false;
		}
		if (statements == null) {
			if (other.statements != null) {
				return false;
			}
		} else if (!statements.equals(other.statements)) {
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
		result = (prime * result) + ((process == null) ? 0 : process.hashCode());
		result = (prime * result) + ((statements == null) ? 0 : statements.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLBlock()");
		if (process != null) {
			sb.append(".setProcess(").append(process).append(")");
		}
		if (statements != null) {
			if (statements.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLStatement o : statements) {
					sb.append(".addStatements(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
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
			for (final HDLStatement o : getStatements()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLBlock, HDLClass.HDLCompound, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if ((statements != null) && (statements.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(statements.size());
							for (final HDLStatement o : statements) {
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
						if ((statements != null) && (statements.size() != 0)) {
							current = statements.iterator();
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