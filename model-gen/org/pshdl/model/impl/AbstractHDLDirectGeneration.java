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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLDirectGeneration extends HDLInstantiation {
	/**
	 * Constructs a new instance of {@link AbstractHDLDirectGeneration}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param var
	 *            the value for var. Can <b>not</b> be <code>null</code>.
	 * @param arguments
	 *            the value for arguments. Can be <code>null</code>.
	 * @param hIf
	 *            the value for hIf. Can <b>not</b> be <code>null</code>.
	 * @param generatorID
	 *            the value for generatorID. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param generatorContent
	 *            the value for generatorContent. Can <b>not</b> be
	 *            <code>null</code>.
	 * @param include
	 *            the value for include. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLDirectGeneration(int id, @Nullable IHDLObject container, @Nonnull HDLVariable var, @Nullable Iterable<HDLArgument> arguments, @Nonnull HDLInterface hIf,
			@Nonnull String generatorID, @Nonnull String generatorContent, @Nonnull Boolean include, boolean validate) {
		super(id, container, var, arguments, validate);
		if (validate) {
			hIf = validateHIf(hIf);
		}
		if (hIf != null) {
			this.hIf = hIf;
		} else {
			this.hIf = null;
		}
		if (validate) {
			generatorID = validateGeneratorID(generatorID);
		}
		this.generatorID = generatorID;
		if (validate) {
			generatorContent = validateGeneratorContent(generatorContent);
		}
		this.generatorContent = generatorContent;
		if (validate) {
			include = validateInclude(include);
		}
		this.include = include;
	}

	public AbstractHDLDirectGeneration() {
		super();
		this.hIf = null;
		this.generatorID = null;
		this.generatorContent = null;
		this.include = null;
	}

	protected final HDLInterface hIf;

	/**
	 * Get the hIf field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public HDLInterface getHIf() {
		return hIf;
	}

	protected HDLInterface validateHIf(HDLInterface hIf) {
		if (hIf == null)
			throw new IllegalArgumentException("The field hIf can not be null!");
		return hIf;
	}

	protected final String generatorID;

	/**
	 * Get the generatorID field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getGeneratorID() {
		return generatorID;
	}

	protected String validateGeneratorID(String generatorID) {
		if (generatorID == null)
			throw new IllegalArgumentException("The field generatorID can not be null!");
		return generatorID;
	}

	protected final String generatorContent;

	/**
	 * Get the generatorContent field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public String getGeneratorContent() {
		return generatorContent;
	}

	protected String validateGeneratorContent(String generatorContent) {
		if (generatorContent == null)
			throw new IllegalArgumentException("The field generatorContent can not be null!");
		return generatorContent;
	}

	protected final Boolean include;

	/**
	 * Get the include field. Can <b>not</b> be <code>null</code>.
	 *
	 * @return the field
	 */
	@Nonnull
	public Boolean getInclude() {
		return include;
	}

	protected Boolean validateInclude(Boolean include) {
		if (include == null)
			throw new IllegalArgumentException("The field include can not be null!");
		return include;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration copy() {
		final HDLDirectGeneration newObject = new HDLDirectGeneration(id, null, var, arguments, hIf, generatorID, generatorContent, include, false);
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
	public HDLDirectGeneration copyFiltered(CopyFilter filter) {
		final HDLVariable filteredvar = filter.copyObject("var", this, var);
		final ArrayList<HDLArgument> filteredarguments = filter.copyContainer("arguments", this, arguments);
		final HDLInterface filteredhIf = filter.copyObject("hIf", this, hIf);
		final String filteredgeneratorID = filter.copyObject("generatorID", this, generatorID);
		final String filteredgeneratorContent = filter.copyObject("generatorContent", this, generatorContent);
		final Boolean filteredinclude = filter.copyObject("include", this, include);
		return filter.postFilter((HDLDirectGeneration) this, new HDLDirectGeneration(id, null, filteredvar, filteredarguments, filteredhIf, filteredgeneratorID,
				filteredgeneratorContent, filteredinclude, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 *
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration copyDeepFrozen(IHDLObject container) {
		final HDLDirectGeneration copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 *
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLDirectGeneration} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration setContainer(@Nullable IHDLObject container) {
		return (HDLDirectGeneration) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getVar()}.
	 *
	 * @param var
	 *            sets the new var of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         var field.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration setVar(@Nonnull HDLVariable var) {
		var = validateVar(var);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getArguments()}.
	 *
	 * @param arguments
	 *            sets the new arguments of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         arguments field.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration setArguments(@Nullable Iterable<HDLArgument> arguments) {
		arguments = validateArguments(arguments);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getArguments()}.
	 *
	 * @param newArguments
	 *            the value that should be added to the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         arguments field.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration addArguments(@Nullable HDLArgument newArguments) {
		if (newArguments == null)
			throw new IllegalArgumentException("Element of arguments can not be null!");
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.add(newArguments);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArguments()}.
	 *
	 * @param newArguments
	 *            the value that should be removed from the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         arguments field.
	 */
	@Override
	@Nonnull
	public HDLDirectGeneration removeArguments(@Nullable HDLArgument newArguments) {
		if (newArguments == null)
			throw new IllegalArgumentException("Removed element of arguments can not be null!");
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(newArguments);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getArguments()}.
	 *
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getArguments()}
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         arguments field.
	 */
	@Nonnull
	public HDLDirectGeneration removeArguments(int idx) {
		final ArrayList<HDLArgument> arguments = (ArrayList<HDLArgument>) this.arguments.clone();
		arguments.remove(idx);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getHIf()}.
	 *
	 * @param hIf
	 *            sets the new hIf of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         hIf field.
	 */
	@Nonnull
	public HDLDirectGeneration setHIf(@Nonnull HDLInterface hIf) {
		hIf = validateHIf(hIf);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getGeneratorID()}.
	 *
	 * @param generatorID
	 *            sets the new generatorID of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         generatorID field.
	 */
	@Nonnull
	public HDLDirectGeneration setGeneratorID(@Nonnull String generatorID) {
		generatorID = validateGeneratorID(generatorID);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getGeneratorContent()}.
	 *
	 * @param generatorContent
	 *            sets the new generatorContent of this object. Can <b>not</b>
	 *            be <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         generatorContent field.
	 */
	@Nonnull
	public HDLDirectGeneration setGeneratorContent(@Nonnull String generatorContent) {
		generatorContent = validateGeneratorContent(generatorContent);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getInclude()}.
	 *
	 * @param include
	 *            sets the new include of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         include field.
	 */
	@Nonnull
	public HDLDirectGeneration setInclude(@Nonnull Boolean include) {
		include = validateInclude(include);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getInclude()}.
	 *
	 * @param include
	 *            sets the new include of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLDirectGeneration} with the updated
	 *         include field.
	 */
	@Nonnull
	public HDLDirectGeneration setInclude(boolean include) {
		include = validateInclude(include);
		final HDLDirectGeneration res = new HDLDirectGeneration(id, container, var, arguments, hIf, generatorID, generatorContent, include, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLDirectGeneration))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLDirectGeneration other = (AbstractHDLDirectGeneration) obj;
		if (hIf == null) {
			if (other.hIf != null)
				return false;
		} else if (!hIf.equals(other.hIf))
			return false;
		if (generatorID == null) {
			if (other.generatorID != null)
				return false;
		} else if (!generatorID.equals(other.generatorID))
			return false;
		if (generatorContent == null) {
			if (other.generatorContent != null)
				return false;
		} else if (!generatorContent.equals(other.generatorContent))
			return false;
		if (include == null) {
			if (other.include != null)
				return false;
		} else if (!include.equals(other.include))
			return false;
		return true;
	}

	private Integer hashCache;

	@Override
	public int hashCode() {
		if (hashCache != null)
			return hashCache;
		int result = super.hashCode();
		final int prime = 31;
		result = (prime * result) + ((hIf == null) ? 0 : hIf.hashCode());
		result = (prime * result) + ((generatorID == null) ? 0 : generatorID.hashCode());
		result = (prime * result) + ((generatorContent == null) ? 0 : generatorContent.hashCode());
		result = (prime * result) + ((include == null) ? 0 : include.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLDirectGeneration()");
		if (var != null) {
			sb.append(".setVar(").append(var.toConstructionString(spacing + "\t")).append(")");
		}
		if (arguments != null) {
			if (arguments.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLArgument o : arguments) {
					sb.append(".addArguments(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (hIf != null) {
			sb.append(".setHIf(").append(hIf.toConstructionString(spacing + "\t")).append(")");
		}
		if (generatorID != null) {
			sb.append(".setGeneratorID(").append('"' + generatorID + '"').append(")");
		}
		if (generatorContent != null) {
			sb.append(".setGeneratorContent(").append('"' + generatorContent + '"').append(")");
		}
		if (include != null) {
			sb.append(".setInclude(").append(include).append(")");
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateHIf(getHIf());
		if (getHIf() != null) {
			getHIf().validateAllFields(this, checkResolve);
		}
		validateGeneratorID(getGeneratorID());
		validateGeneratorContent(getGeneratorContent());
		validateInclude(getInclude());
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLDirectGeneration, HDLClass.HDLInstantiation, HDLClass.HDLStatement, HDLClass.HDLObject);
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
						if (var != null) {
							current = Iterators.concat(Iterators.forArray(var), var.deepIterator());
						}
						break;
					case 1:
						if ((arguments != null) && (arguments.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(arguments.size());
							for (final HDLArgument o : arguments) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 2:
						if (hIf != null) {
							current = Iterators.concat(Iterators.forArray(hIf), hIf.deepIterator());
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
						if (var != null) {
							current = Iterators.singletonIterator(var);
						}
						break;
					case 1:
						if ((arguments != null) && (arguments.size() != 0)) {
							current = arguments.iterator();
						}
						break;
					case 2:
						if (hIf != null) {
							current = Iterators.singletonIterator(hIf);
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