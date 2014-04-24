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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLEqualityOp;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLEqualityOp contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLEqualityOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLEqualityOp extends AbstractHDLEqualityOp {
	/**
	 * Constructs a new instance of {@link HDLEqualityOp}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLEqualityOp(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLEqualityOpType type, boolean validate) {
		super(id, container, left, right, type, validate);
	}

	public HDLEqualityOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLEqualityOp;
	}

	public static enum HDLEqualityOpType {
		EQ("=="), NOT_EQ("!="), LESS("<"), LESS_EQ("<="), GREATER(">"), GREATER_EQ(">=");
		String str;

		HDLEqualityOpType(String op) {
			this.str = op;
		}

		@Nullable
		public static HDLEqualityOpType getOp(String op) {
			for (final HDLEqualityOpType ass : values()) {
				if (ass.str.equals(op))
					return ass;
			}
			return null;
		}

		@Override
		@Nonnull
		public String toString() {
			return str;
		}
	}

	/**
	 * The accessor for the field type which is of type HDLEqualityOpType.
	 */
	public static HDLFieldAccess<HDLEqualityOp, HDLEqualityOpType> fType = new HDLFieldAccess<HDLEqualityOp, HDLEqualityOpType>("type") {
		@Override
		public HDLEqualityOpType getValue(HDLEqualityOp obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}

		@Override
		public HDLEqualityOp setValue(HDLEqualityOp obj, HDLEqualityOpType value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
