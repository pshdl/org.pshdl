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

import org.pshdl.model.impl.AbstractHDLManip;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

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
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLManip(int id, @Nullable IHDLObject container, @Nonnull HDLManipType type, @Nonnull HDLExpression target, @Nullable HDLType castTo, boolean validate) {
		super(id, container, type, target, castTo, validate);
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
	public static HDLFieldAccess<HDLManip, HDLManipType> fType = new HDLFieldAccess<HDLManip, HDLManipType>("type", HDLManipType.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLManipType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getType();
		}

		@Override
		public HDLManip setValue(HDLManip obj, HDLManipType value) {
			if (obj == null)
				return null;
			return obj.setType(value);
		}
	};
	/**
	 * The accessor for the field target which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLManip, HDLExpression> fTarget = new HDLFieldAccess<HDLManip, HDLExpression>("target", HDLExpression.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getTarget();
		}

		@Override
		public HDLManip setValue(HDLManip obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setTarget(value);
		}
	};
	/**
	 * The accessor for the field castTo which is of type HDLType.
	 */
	public static HDLFieldAccess<HDLManip, HDLType> fCastTo = new HDLFieldAccess<HDLManip, HDLType>("castTo", HDLType.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLType getValue(HDLManip obj) {
			if (obj == null)
				return null;
			return obj.getCastTo();
		}

		@Override
		public HDLManip setValue(HDLManip obj, HDLType value) {
			if (obj == null)
				return null;
			return obj.setCastTo(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (type == obj)
			return fType;
		if (target == obj)
			return fTarget;
		if (castTo == obj)
			return fCastTo;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
