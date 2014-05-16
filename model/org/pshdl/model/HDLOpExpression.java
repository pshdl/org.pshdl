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

import org.pshdl.model.impl.AbstractHDLOpExpression;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLOpExpression contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression left. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression right. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public abstract class HDLOpExpression extends AbstractHDLOpExpression {
	/**
	 * Constructs a new instance of {@link HDLOpExpression}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param left
	 *            the value for left. Can <b>not</b> be <code>null</code>.
	 * @param right
	 *            the value for right. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLOpExpression(int id, @Nullable IHDLObject container, @Nonnull HDLExpression left, @Nonnull HDLExpression right, boolean validate) {
		super(id, container, left, right, validate);
	}

	public HDLOpExpression() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLOpExpression;
	}

	/**
	 * The accessor for the field left which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLOpExpression, HDLExpression> fLeft = new HDLFieldAccess<HDLOpExpression, HDLExpression>("left", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLOpExpression obj) {
			if (obj == null)
				return null;
			return obj.getLeft();
		}

		@Override
		public HDLOpExpression setValue(HDLOpExpression obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setLeft(value);
		}
	};
	/**
	 * The accessor for the field right which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLOpExpression, HDLExpression> fRight = new HDLFieldAccess<HDLOpExpression, HDLExpression>("right", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLOpExpression obj) {
			if (obj == null)
				return null;
			return obj.getRight();
		}

		@Override
		public HDLOpExpression setValue(HDLOpExpression obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setRight(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (left == obj)
			return fLeft;
		if (right == obj)
			return fRight;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	public abstract Enum<?> getType();
	// $CONTENT-END$

}
