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
package org.pshdl.model;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import org.pshdl.model.impl.AbstractHDLType;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLType contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> dim. Can be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLType extends AbstractHDLType {
	/**
	 * Constructs a new instance of {@link HDLType}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param dim
	 *            the value for dim. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLType(int id, @Nullable IHDLObject container, @Nonnull String name, @Nullable Iterable<HDLExpression> dim, boolean validate) {
		super(id, container, name, dim, validate);
	}

	public HDLType() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLType;
	}

	/**
	 * The accessor for the field name which is of type String.
	 */
	public static HDLFieldAccess<HDLType, String> fName = new HDLFieldAccess<HDLType, String>("name") {
		@Override
		public String getValue(HDLType obj) {
			if (obj == null)
				return null;
			return obj.getName();
		}

		@Override
		public HDLType setValue(HDLType obj, String value) {
			if (obj == null)
				return null;
			return obj.setName(value);
		}
	};
	/**
	 * The accessor for the field dim which is of type ArrayList<HDLExpression>.
	 */
	public static HDLFieldAccess<HDLType, ArrayList<HDLExpression>> fDim = new HDLFieldAccess<HDLType, ArrayList<HDLExpression>>("dim") {
		@Override
		public ArrayList<HDLExpression> getValue(HDLType obj) {
			if (obj == null)
				return null;
			return obj.getDim();
		}

		@Override
		public HDLType setValue(HDLType obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setDim(value);
		}
	};

	// $CONTENT-BEGIN$
	@Nonnull
	public HDLQualifiedName asRef() {
		return new HDLQualifiedName(getName());
	}

	public HDLExpression getWidth() {
		throw new IllegalArgumentException("Not implemented for this type:" + this);
	}

	public HDLPrimitiveType getType() {
		return null;
	}
	// $CONTENT-END$

}
