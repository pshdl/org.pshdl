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
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.utils.CopyFilter;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public abstract class AbstractHDLPackage extends HDLObject {
	/**
	 * Constructs a new instance of {@link AbstractHDLPackage}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param libURI
	 *            the value for libURI. Can <b>not</b> be <code>null</code>.
	 * @param pkg
	 *            the value for pkg. Can be <code>null</code>.
	 * @param units
	 *            the value for units. Can be <code>null</code>.
	 * @param declarations
	 *            the value for declarations. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public AbstractHDLPackage(int id, @Nullable IHDLObject container, @Nonnull String libURI, @Nullable String pkg, @Nullable Iterable<HDLUnit> units,
			@Nullable Iterable<HDLDeclaration> declarations, boolean validate) {
		super(id, container, validate);
		if (validate) {
			libURI = validateLibURI(libURI);
		}
		this.libURI = libURI;
		if (validate) {
			pkg = validatePkg(pkg);
		}
		this.pkg = pkg;
		if (validate) {
			units = validateUnits(units);
		}
		this.units = new ArrayList<HDLUnit>();
		if (units != null) {
			for (final HDLUnit newValue : units) {
				this.units.add(newValue);
			}
		}
		if (validate) {
			declarations = validateDeclarations(declarations);
		}
		this.declarations = new ArrayList<HDLDeclaration>();
		if (declarations != null) {
			for (final HDLDeclaration newValue : declarations) {
				this.declarations.add(newValue);
			}
		}
	}

	public AbstractHDLPackage() {
		super();
		this.libURI = null;
		this.pkg = null;
		this.units = new ArrayList<HDLUnit>();
		this.declarations = new ArrayList<HDLDeclaration>();
	}

	protected final String libURI;

	/**
	 * Get the libURI field. Can <b>not</b> be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nonnull
	public String getLibURI() {
		return libURI;
	}

	protected String validateLibURI(String libURI) {
		if (libURI == null)
			throw new IllegalArgumentException("The field libURI can not be null!");
		return libURI;
	}

	protected final String pkg;

	/**
	 * Get the pkg field. Can be <code>null</code>.
	 * 
	 * @return the field
	 */
	@Nullable
	public String getPkg() {
		return pkg;
	}

	protected String validatePkg(String pkg) {
		return pkg;
	}

	protected final ArrayList<HDLUnit> units;

	/**
	 * Get the units field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLUnit> getUnits() {
		return (ArrayList<HDLUnit>) units.clone();
	}

	protected Iterable<HDLUnit> validateUnits(Iterable<HDLUnit> units) {
		if (units == null)
			return new ArrayList<HDLUnit>();
		return units;
	}

	protected final ArrayList<HDLDeclaration> declarations;

	/**
	 * Get the declarations field. Can be <code>null</code>.
	 * 
	 * @return a clone of the field. Will never return <code>null</code>.
	 */
	@Nonnull
	public ArrayList<HDLDeclaration> getDeclarations() {
		return (ArrayList<HDLDeclaration>) declarations.clone();
	}

	protected Iterable<HDLDeclaration> validateDeclarations(Iterable<HDLDeclaration> declarations) {
		if (declarations == null)
			return new ArrayList<HDLDeclaration>();
		return declarations;
	}

	/**
	 * Creates a copy of this class with the same fields.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLPackage copy() {
		final HDLPackage newObject = new HDLPackage(id, null, libURI, pkg, units, declarations, false);
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
	public HDLPackage copyFiltered(CopyFilter filter) {
		final String filteredlibURI = filter.copyObject("libURI", this, libURI);
		final String filteredpkg = filter.copyObject("pkg", this, pkg);
		final ArrayList<HDLUnit> filteredunits = filter.copyContainer("units", this, units);
		final ArrayList<HDLDeclaration> filtereddeclarations = filter.copyContainer("declarations", this, declarations);
		return filter.postFilter((HDLPackage) this, new HDLPackage(id, null, filteredlibURI, filteredpkg, filteredunits, filtereddeclarations, false));
	}

	/**
	 * Creates a deep copy of this class with the same fields and freezes it.
	 * 
	 * @return a new instance of this class.
	 */
	@Override
	@Nonnull
	public HDLPackage copyDeepFrozen(IHDLObject container) {
		final HDLPackage copy = copyFiltered(CopyFilter.DEEP_META);
		copy.freeze(container);
		return copy;
	}

	/**
	 * Setter for the field {@link #getContainer()}.
	 * 
	 * @param container
	 *            sets the new container of this object. Can be
	 *            <code>null</code>.
	 * @return the same instance of {@link HDLPackage} with the updated
	 *         container field.
	 */
	@Override
	@Nonnull
	public HDLPackage setContainer(@Nullable IHDLObject container) {
		return (HDLPackage) super.setContainer(container);
	}

	/**
	 * Setter for the field {@link #getLibURI()}.
	 * 
	 * @param libURI
	 *            sets the new libURI of this object. Can <b>not</b> be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLPackage} with the updated libURI
	 *         field.
	 */
	@Nonnull
	public HDLPackage setLibURI(@Nonnull String libURI) {
		libURI = validateLibURI(libURI);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getPkg()}.
	 * 
	 * @param pkg
	 *            sets the new pkg of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLPackage} with the updated pkg field.
	 */
	@Nonnull
	public HDLPackage setPkg(@Nullable String pkg) {
		pkg = validatePkg(pkg);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getUnits()}.
	 * 
	 * @param units
	 *            sets the new units of this object. Can be <code>null</code>.
	 * @return a new instance of {@link HDLPackage} with the updated units
	 *         field.
	 */
	@Nonnull
	public HDLPackage setUnits(@Nullable Iterable<HDLUnit> units) {
		units = validateUnits(units);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getUnits()}.
	 * 
	 * @param newUnits
	 *            the value that should be added to the field
	 *            {@link #getUnits()}
	 * @return a new instance of {@link HDLPackage} with the updated units
	 *         field.
	 */
	@Nonnull
	public HDLPackage addUnits(@Nullable HDLUnit newUnits) {
		if (newUnits == null)
			throw new IllegalArgumentException("Element of units can not be null!");
		final ArrayList<HDLUnit> units = (ArrayList<HDLUnit>) this.units.clone();
		units.add(newUnits);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getUnits()}.
	 * 
	 * @param newUnits
	 *            the value that should be removed from the field
	 *            {@link #getUnits()}
	 * @return a new instance of {@link HDLPackage} with the updated units
	 *         field.
	 */
	@Nonnull
	public HDLPackage removeUnits(@Nullable HDLUnit newUnits) {
		if (newUnits == null)
			throw new IllegalArgumentException("Removed element of units can not be null!");
		final ArrayList<HDLUnit> units = (ArrayList<HDLUnit>) this.units.clone();
		units.remove(newUnits);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getUnits()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getUnits()}
	 * @return a new instance of {@link HDLPackage} with the updated units
	 *         field.
	 */
	@Nonnull
	public HDLPackage removeUnits(int idx) {
		final ArrayList<HDLUnit> units = (ArrayList<HDLUnit>) this.units.clone();
		units.remove(idx);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Setter for the field {@link #getDeclarations()}.
	 * 
	 * @param declarations
	 *            sets the new declarations of this object. Can be
	 *            <code>null</code>.
	 * @return a new instance of {@link HDLPackage} with the updated
	 *         declarations field.
	 */
	@Nonnull
	public HDLPackage setDeclarations(@Nullable Iterable<HDLDeclaration> declarations) {
		declarations = validateDeclarations(declarations);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Adds a new value to the field {@link #getDeclarations()}.
	 * 
	 * @param newDeclarations
	 *            the value that should be added to the field
	 *            {@link #getDeclarations()}
	 * @return a new instance of {@link HDLPackage} with the updated
	 *         declarations field.
	 */
	@Nonnull
	public HDLPackage addDeclarations(@Nullable HDLDeclaration newDeclarations) {
		if (newDeclarations == null)
			throw new IllegalArgumentException("Element of declarations can not be null!");
		final ArrayList<HDLDeclaration> declarations = (ArrayList<HDLDeclaration>) this.declarations.clone();
		declarations.add(newDeclarations);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDeclarations()}.
	 * 
	 * @param newDeclarations
	 *            the value that should be removed from the field
	 *            {@link #getDeclarations()}
	 * @return a new instance of {@link HDLPackage} with the updated
	 *         declarations field.
	 */
	@Nonnull
	public HDLPackage removeDeclarations(@Nullable HDLDeclaration newDeclarations) {
		if (newDeclarations == null)
			throw new IllegalArgumentException("Removed element of declarations can not be null!");
		final ArrayList<HDLDeclaration> declarations = (ArrayList<HDLDeclaration>) this.declarations.clone();
		declarations.remove(newDeclarations);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	/**
	 * Removes a value from the field {@link #getDeclarations()}.
	 * 
	 * @param idx
	 *            the index of the value that should be removed from the field
	 *            {@link #getDeclarations()}
	 * @return a new instance of {@link HDLPackage} with the updated
	 *         declarations field.
	 */
	@Nonnull
	public HDLPackage removeDeclarations(int idx) {
		final ArrayList<HDLDeclaration> declarations = (ArrayList<HDLDeclaration>) this.declarations.clone();
		declarations.remove(idx);
		final HDLPackage res = new HDLPackage(id, container, libURI, pkg, units, declarations, false);
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractHDLPackage))
			return false;
		if (!super.equals(obj))
			return false;
		final AbstractHDLPackage other = (AbstractHDLPackage) obj;
		if (pkg == null) {
			if (other.pkg != null)
				return false;
		} else if (!pkg.equals(other.pkg))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		if (declarations == null) {
			if (other.declarations != null)
				return false;
		} else if (!declarations.equals(other.declarations))
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
		result = (prime * result) + ((pkg == null) ? 0 : pkg.hashCode());
		result = (prime * result) + ((units == null) ? 0 : units.hashCode());
		result = (prime * result) + ((declarations == null) ? 0 : declarations.hashCode());
		hashCache = result;
		return result;
	}

	@Override
	public String toConstructionString(String spacing) {
		final boolean first = true;
		final StringBuilder sb = new StringBuilder();
		sb.append('\n').append(spacing).append("new HDLPackage()");
		if (libURI != null) {
			sb.append(".setLibURI(").append('"' + libURI + '"').append(")");
		}
		if (pkg != null) {
			sb.append(".setPkg(").append('"' + pkg + '"').append(")");
		}
		if (units != null) {
			if (units.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLUnit o : units) {
					sb.append(".addUnits(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		if (declarations != null) {
			if (declarations.size() > 0) {
				sb.append('\n').append(spacing);
				for (final HDLDeclaration o : declarations) {
					sb.append(".addDeclarations(").append(o.toConstructionString(spacing + "\t\t"));
					sb.append('\n').append(spacing).append(")");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public void validateAllFields(IHDLObject expectedParent, boolean checkResolve) {
		super.validateAllFields(expectedParent, checkResolve);
		validateLibURI(getLibURI());
		validatePkg(getPkg());
		validateUnits(getUnits());
		if (getUnits() != null) {
			for (final HDLUnit o : getUnits()) {
				o.validateAllFields(this, checkResolve);
			}
		}
		validateDeclarations(getDeclarations());
		if (getDeclarations() != null) {
			for (final HDLDeclaration o : getDeclarations()) {
				o.validateAllFields(this, checkResolve);
			}
		}
	}

	@Override
	public EnumSet<HDLClass> getClassSet() {
		return EnumSet.of(HDLClass.HDLPackage, HDLClass.HDLObject);
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
						if ((units != null) && (units.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(units.size());
							for (final HDLUnit o : units) {
								iters.add(Iterators.forArray(o));
								iters.add(o.deepIterator());
							}
							current = Iterators.concat(iters.iterator());
						}
						break;
					case 1:
						if ((declarations != null) && (declarations.size() != 0)) {
							final List<Iterator<? extends IHDLObject>> iters = Lists.newArrayListWithCapacity(declarations.size());
							for (final HDLDeclaration o : declarations) {
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
						if ((units != null) && (units.size() != 0)) {
							current = units.iterator();
						}
						break;
					case 1:
						if ((declarations != null) && (declarations.size() != 0)) {
							current = declarations.iterator();
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