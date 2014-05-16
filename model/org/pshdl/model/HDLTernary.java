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

import org.pshdl.model.impl.AbstractHDLTernary;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLTernary contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression ifExpr. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression thenExpr. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression elseExpr. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLTernary extends AbstractHDLTernary {
	/**
	 * Constructs a new instance of {@link HDLTernary}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param ifExpr
	 *            the value for ifExpr. Can <b>not</b> be <code>null</code>.
	 * @param thenExpr
	 *            the value for thenExpr. Can <b>not</b> be <code>null</code>.
	 * @param elseExpr
	 *            the value for elseExpr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLTernary(int id, @Nullable IHDLObject container, @Nonnull HDLExpression ifExpr, @Nonnull HDLExpression thenExpr, @Nonnull HDLExpression elseExpr, boolean validate) {
		super(id, container, ifExpr, thenExpr, elseExpr, validate);
	}

	public HDLTernary() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLTernary;
	}

	/**
	 * The accessor for the field ifExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fIfExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("ifExpr", HDLExpression.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getIfExpr();
		}

		@Override
		public HDLTernary setValue(HDLTernary obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setIfExpr(value);
		}
	};
	/**
	 * The accessor for the field thenExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fThenExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("thenExpr", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getThenExpr();
		}

		@Override
		public HDLTernary setValue(HDLTernary obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setThenExpr(value);
		}
	};
	/**
	 * The accessor for the field elseExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fElseExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("elseExpr", HDLExpression.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getElseExpr();
		}

		@Override
		public HDLTernary setValue(HDLTernary obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setElseExpr(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (ifExpr == obj)
			return fIfExpr;
		if (thenExpr == obj)
			return fThenExpr;
		if (elseExpr == obj)
			return fElseExpr;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
