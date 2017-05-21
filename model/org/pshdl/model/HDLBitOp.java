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

import org.pshdl.model.impl.AbstractHDLBitOp;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLBitOp contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLBitOpType type. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLBitOp extends AbstractHDLBitOp {
	/**
	 * Constructs a new instance of {@link HDLBitOp}
	 *
	 * @param id
	 *            a unique ID for this particular node
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
	public HDLBitOp(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, @Nonnull HDLBitOpType type, boolean validate) {
		super(id, container, left, right, type, validate);
	}

	public HDLBitOp() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLBitOp;
	}

	public static enum HDLBitOpType {
		AND("&"), OR("|"), XOR("^"), LOGI_AND("&&"), LOGI_OR("||");
		String str;

		HDLBitOpType(String op) {
			this.str = op;
		}

		@Nullable
		public static HDLBitOpType getOp(String op) {
			for (final HDLBitOpType ass : values()) {
				if (ass.str.equals(op)) {
					return ass;
				}
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
	 * The accessor for the field type which is of type HDLBitOpType.
	 */
	public static HDLFieldAccess<HDLBitOp, HDLBitOpType> fType = new HDLFieldAccess<HDLBitOp, HDLBitOpType>("type", HDLBitOpType.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLBitOpType getValue(HDLBitOp obj) {
			if (obj == null) {
				return null;
			}
			return obj.getType();
		}

		@Override
		public HDLBitOp setValue(HDLBitOp obj, HDLBitOpType value) {
			if (obj == null) {
				return null;
			}
			return obj.setType(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (type == obj) {
			return fType;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
