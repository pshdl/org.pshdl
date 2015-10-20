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

import org.pshdl.model.impl.AbstractHDLAssignment;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLAssignment contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLReference left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLAssignmentType type. If <code>null</code>,
 * {@link HDLAssignmentType#ASSGN} is used as default.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLAssignment extends AbstractHDLAssignment {
	/**
	 * Constructs a new instance of {@link HDLAssignment}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param type
	 *            the value for type. If <code>null</code>,
	 *            {@link HDLAssignmentType#ASSGN} is used as default.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLAssignment(int id, @Nullable IHDLObject container, @Nonnull HDLReference left, @Nullable HDLAssignmentType type, @Nonnull HDLExpression right, boolean validate) {
		super(id, container, left, type, right, validate);
	}

	public HDLAssignment() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLAssignment;
	}

	public static enum HDLAssignmentType {
		ASSGN("="), ADD_ASSGN("+="), SUB_ASSGN("-="), MUL_ASSGN("*="), DIV_ASSGN("/="), MOD_ASSGN("%="), AND_ASSGN("&="), XOR_ASSGN("^="), OR_ASSGN("|="), SLL_ASSGN(
				"<<="), SRL_ASSGN(">>>="), SRA_ASSGN(">>=");
		String str;

		HDLAssignmentType(String op) {
			this.str = op;
		}

		@Nullable
		public static HDLAssignmentType getOp(String op) {
			for (final HDLAssignmentType ass : values()) {
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
	 * The accessor for the field left which is of type HDLReference.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLReference> fLeft = new HDLFieldAccess<HDLAssignment, HDLReference>("left", HDLReference.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLReference getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getLeft();
		}

		@Override
		public HDLAssignment setValue(HDLAssignment obj, HDLReference value) {
			if (obj == null)
				return null;
			return obj.setLeft(value);
		}
	};
	/**
	 * The accessor for the field type which is of type HDLAssignmentType.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLAssignmentType> fType = new HDLFieldAccess<HDLAssignment, HDLAssignmentType>("type", HDLAssignmentType.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLAssignmentType getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}

		@Override
		public HDLAssignment setValue(HDLAssignment obj, HDLAssignmentType value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	/**
	 * The accessor for the field right which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLExpression> fRight = new HDLFieldAccess<HDLAssignment, HDLExpression>("right", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}

		@Override
		public HDLAssignment setValue(HDLAssignment obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setRight(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (left == obj)
			return fLeft;
		if (type == obj)
			return fType;
		if (right == obj)
			return fRight;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
