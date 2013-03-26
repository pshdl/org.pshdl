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
package de.tuhh.ict.pshdl.model;

import javax.annotation.*;

import de.tuhh.ict.pshdl.model.impl.*;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLTernary(@Nullable IHDLObject container, @Nonnull HDLExpression ifExpr, @Nonnull HDLExpression thenExpr, @Nonnull HDLExpression elseExpr, boolean validate) {
		super(container, ifExpr, thenExpr, elseExpr, validate);
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
	public static HDLFieldAccess<HDLTernary, HDLExpression> fIfExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("ifExpr") {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getIfExpr();
		}
	};
	/**
	 * The accessor for the field thenExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fThenExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("thenExpr") {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getThenExpr();
		}
	};
	/**
	 * The accessor for the field elseExpr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLTernary, HDLExpression> fElseExpr = new HDLFieldAccess<HDLTernary, HDLExpression>("elseExpr") {
		@Override
		public HDLExpression getValue(HDLTernary obj) {
			if (obj == null)
				return null;
			return obj.getElseExpr();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
