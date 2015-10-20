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
package org.pshdl.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLAnnotation;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAnnotation contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>String value. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLAnnotation extends AbstractHDLAnnotation {
	/**
	 * Constructs a new instance of {@link HDLAnnotation}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param value
	 *            the value for value. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLAnnotation(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable String value, boolean validate) {
		super(id, container, name, value, validate);
	}

	public HDLAnnotation() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLAnnotation;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLAnnotation, String> fName = new HDLFieldAccess<HDLAnnotation, String>("name", String.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}

		@Override
		public HDLAnnotation setValue(HDLAnnotation obj, String value) {
			if (obj == null)
				return null;
			return obj.setName(value);
		}
	};
	/**
	 * The accessor for the field value which is of type String.
	 */
	public static HDLFieldAccess<HDLAnnotation, String> fValue = new HDLFieldAccess<HDLAnnotation, String>("value", String.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public String getValue(HDLAnnotation obj) {
			if (obj == null)
				return null;
			return obj.getValue();
		}

		@Override
		public HDLAnnotation setValue(HDLAnnotation obj, String value) {
			if (obj == null)
				return null;
			return obj.setValue(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (name == obj)
			return fName;
		if (value == obj)
			return fValue;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	@Override
	protected String validateName(String name) {
		final String validateName = super.validateName(name);
		if (validateName.isEmpty())
			throw new IllegalArgumentException("The empty string is not a valid annotation name");
		if (validateName.charAt(0) != '@')
			throw new IllegalArgumentException("Annotation names have to start with an '@'");
		return validateName;
	}

	// $CONTENT-END$

}
