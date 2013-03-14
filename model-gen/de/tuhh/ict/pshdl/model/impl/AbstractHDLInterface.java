package de.tuhh.ict.pshdl.model.impl;

import java.util.*;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.HDLIterator.Visit;

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
	public AbstractHDLInterface(@Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, @Nullable Iterable<HDLVariableDeclaration> ports,
			boolean validate) {
		super(container, name, dim, validate);
		if (validate) {
			ports = validatePorts(ports);
		}
		this.ports = new ArrayList<HDLVariableDeclaration>();
		if (ports != null) {
			for (HDLVariableDeclaration newValue : ports) {
				this.ports.add(newValue);
			}
		}
	}

	public AbstractHDLInterface() {
		super();
		this.ports = new ArrayList<HDLVariableDeclaration>();
	}

	@Visit
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
		HDLInterface newObject = new HDLInterface(null, name, dim, ports, false);
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
		String filteredname = filter.copyObject("name", this, name);
		ArrayList<HDLExpression> filtereddim = filter.copyContainer("dim", this, dim);
		ArrayList<HDLVariableDeclaration> filteredports = filter.copyContainer("ports", this, ports);
		return filter.postFilter((HDLInterface) this, new HDLInterface(null, filteredname, filtereddim, filteredports, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLInterface copyDeepFrozen(IHDLObject container) {
		HDLInterface copy = copyFiltered(CopyFilter.DEEP_META);
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
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.add(newDim);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(newDim);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLExpression> dim = (ArrayList<HDLExpression>) this.dim.clone();
		dim.remove(idx);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.add(newPorts);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.remove(newPorts);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		ArrayList<HDLVariableDeclaration> ports = (ArrayList<HDLVariableDeclaration>) this.ports.clone();
		ports.remove(idx);
		HDLInterface res = new HDLInterface(container, name, dim, ports, false);
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
		AbstractHDLInterface other = (AbstractHDLInterface) obj;
		if (ports == null) {
			if (other.ports != null)
				return false;
		} else if (!ports.equals(other.ports))
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
		result = (prime * result) + ((ports == null) ? 0 : ports.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		boolean first = true;
		StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLInterface()");
		if (name != null) {
			sb.append(".setName(").append('"' + name + '"').append(")");
		}
		if (dim != null) {
			if (dim.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLExpression o : dim) {
					sb.append(".addDim(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (ports != null) {
			if (ports.size() > 0) {
				sb.append('\n').append(spacing);
				for (HDLVariableDeclaration o : ports) {
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
			for (HDLVariableDeclaration o : getPorts()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLInterface, HDLClass.HDLType, HDLClass.HDLObject);
	}
}