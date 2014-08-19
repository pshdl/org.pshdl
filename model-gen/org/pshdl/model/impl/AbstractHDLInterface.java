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
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLInterface extends HDLType {
	/**
	 * Constructs a new instance of {@link AbstractHDLInterface}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param ports
	 *            the value for ports. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLInterface(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim,
			@Nullable Iterable<HDLVariableDeclaration> ports, boolean validate) {
		super(id, container, name, dim, validate);
		if (validate) {
			ports = validatePorts(ports);
		}
		this.ports = new ArrayList<HDLVariableDeclaration>();
		if (ports != null) {
			for (final HDLVariableDeclaration newValue : ports) {
				this.ports.add(newValue);
			}
		}
	}

	public AbstractHDLInterface() {
		super();
		this.ports = new ArrayList<HDLVariableDeclaration>();
	}

	protected final ArrayList<HDLVariableDeclaration> ports;

	/**
	 * Get the ports field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLVariableDeclaration> getPorts() {
		return (ArrayList<HDLVariableDeclaration>) ports.clone();
	}

	protected Iterable<HDLVariableDeclaration> validatePorts(Iterable<HDLVariableDeclaration> ports) {
		if (ports == null)
			return new ArrayList<HDLVariableDeclaration>();
		return ports;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterface copy() {
		final HDLInterface newObject = new HDLInterface(id, null, name, dim, ports, false);
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
	public HDLInterface copyFiltered(CopyFilter filter) {
		final String filteredname = filter.copyObject("name", this, name);
		final ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		final ArrayList<HDLVariableDeclaration> filteredports = filter.copyContainer("ports", this, ports);
		return filter.postFilter((HDLInterface) this, new HDLInterface(id, null, filteredname, filtereddim, filteredports, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterface copyDeepFrozen(IHDLObject container) {
		final HDLInterface copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLInterface} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLInterface setContainer(@Nullable IHDLObject container) {
		return (HDLInterface) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getName()}.
	 * 
	 * @param name
	 *            sets the new name of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLInterface} with the updated name
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterface setName(@Nonnull String name) {
		name = validateName(name);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDim()}.
	 * 
	 * @param dim
	 *            sets the new dim of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterface} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterface setDim(@Nullable Iterable<HDLExpression> dim) {
		dim = validateDim(dim);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be added to the field {@link #getDim()}
	 * @return a new instance of {@link HDLInterface} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterface addDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Element of dim can not be null!");
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param newDim
	 *            the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLInterface} with the updated dim
	 *         field.
	 */
	@Override
	@Nonnull
	public HDLInterface removeDim(@Nullable HDLExpression newDim) {
		if (newDim == null)
			throw new IllegalArgumentException("Removed element of dim can not be null!");
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDim()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDim()}
	 * @return a new instance of {@link HDLInterface} with the updated dim
	 *         field.
	 */
	@Nonnull
	public HDLInterface removeDim(int idx) {
		final ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getPorts()}.
	 * 
	 * @param ports
	 *            sets the new ports of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLInterface} with the updated ports
	 *         field.
	 */
	@Nonnull
	public HDLInterface setPorts(@Nullable Iterable<HDLVariableDeclaration> ports) {
		ports = validatePorts(ports);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getPorts()}.
	 * 
	 * @param newPorts
	 *            the value that should be added to the field
	 *            {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports
	 *         field.
	 */
	@Nonnull
	public HDLInterface addPorts(@Nullable HDLVariableDeclaration newPorts) {
		if (newPorts == null)
			throw new IllegalArgumentException("Element of ports can not be null!");
		final ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.add(newPorts);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getPorts()}.
	 * 
	 * @param newPorts
	 *            the value that should be removed from the field
	 *            {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports
	 *         field.
	 */
	@Nonnull
	public HDLInterface removePorts(@Nullable HDLVariableDeclaration newPorts) {
		if (newPorts == null)
			throw new IllegalArgumentException("Removed element of ports can not be null!");
		final ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.remove(newPorts);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getPorts()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getPorts()}
	 * @return a new instance of {@link HDLInterface} with the updated ports
	 *         field.
	 */
	@Nonnull
	public HDLInterface removePorts(int idx) {
		final ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.remove(idx);
		final HDLInterface res = new HDLInterface(id, container, name, dim, ports, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLInterface))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLInterface other = (AbstractHDLInterface) obj;
		if (ports == null) {
			if (other.ports != null)
				return false;
		} else if (!ports.equals(other.ports))
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
		result = (prime * result) + ((ports == null) ? 0 : ports.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterface()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (dim != null) {
			if (dim.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLExpression o : dim) {
					sb.append(".addDim(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (ports != null) {
			if (ports.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLVariableDeclaration o : ports) {
					sb.append(".addPorts(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validatePorts(getPorts());
		if (getPorts() != null) {
			for (final HDLVariableDeclaration o : getPorts()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterface, HDLClass.HDLType, HDLClass.HDLObject);
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
						if ((dim != null) && (dim.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(dim.size());
							for (final HDLExpression o : dim) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if ((ports != null) && (ports.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(ports.size());
							for (final HDLVariableDeclaration o : ports) {
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
						if ((dim != null) && (dim.size() != 0)) {
							current = dim.iterator();
						}
						break;
					case 1:
						if ((ports != null) && (ports.size() != 0)) {
							current = ports.iterator();
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