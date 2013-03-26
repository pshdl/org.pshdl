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
 * The class HDLManip contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLManipType type. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLExpression target. Can <b>not</b> be <code>null</code>.</li>
 * <li>HDLType castTo. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLManip extends AbstractHDLManip {
	/**
	 * Constructs a new instance of {@link HDLManip}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param type
	 *            the value for type. Can <b>not</b> be <code>null</code>.
	 * @param target
	 *            the value for target. Can <b>not</b> be <code>null</code>.
	 * @param castTo
	 *            the value for castTo. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLManip(@Nullable IHDLObject container, @Nonnull HDLManipType type, @Nonnull HDLExpression target, @Nullable HDLType castTo, boolean validate) {
		super(container, type, target, castTo, validate);
	}

	public HDLManip() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLManip;
	}

	public static enum HDLManipType {
		CAST, ARITH_NEG, BIT_NEG, LOGIC_NEG;
	}

	/**
	 * The accessor for the field type which is of type HDLManipType.
	 */
	public static HDLFieldAccess<HDLManip, HDLManipType> fType = new HDLFieldAccess<HDLManip, HDLManipType>("type") {
		@Override
		public HDLManipType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}
	};
	/**
	 * The accessor for the field target which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLManip, HDLExpression> fTarget = new HDLFieldAccess<HDLManip, HDLExpression>("target") {
		@Override
		public HDLExpression getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getTarget();
		}
	};
	/**
	 * The accessor for the field castTo which is of type HDLType.
	 */
	public static HDLFieldAccess<HDLManip, HDLType> fCastTo = new HDLFieldAccess<HDLManip, HDLType>("castTo") {
		@Override
		public HDLType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getCastTo();
		}
	};
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
