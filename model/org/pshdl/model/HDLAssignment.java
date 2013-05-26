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

import javax.annotation.*;

import org.pshdl.model.impl.*;
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
		ASSGN("="), ADD_ASSGN("+="), SUB_ASSGN("-="), MUL_ASSGN("*="), DIV_ASSGN("/="), MOD_ASSGN("%="), AND_ASSGN("&="), XOR_ASSGN("^="), OR_ASSGN("|="), SLL_ASSGN("<<="), SRL_ASSGN(
				">>>="), SRA_ASSGN(">>=");
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
	public static HDLFieldAccess<HDLAssignment, HDLReference> fLeft = new HDLFieldAccess<HDLAssignment, HDLReference>("left") {
		@Override
		public HDLReference getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getLeft();
		}
	};
	/**
	 * The accessor for the field type which is of type HDLAssignmentType.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLAssignmentType> fType = new HDLFieldAccess<HDLAssignment, HDLAssignmentType>("type") {
		@Override
		public HDLAssignmentType getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field right which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLAssignment, HDLExpression> fRight = new HDLFieldAccess<HDLAssignment, HDLExpression>("right") {
		@Override
		public HDLExpression getValue(HDLAssignment obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}
	};
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
