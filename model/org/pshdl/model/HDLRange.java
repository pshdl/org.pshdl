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

import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.HDLArithOp.HDLArithOpType;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.impl.AbstractHDLRange;
import org.pshdl.model.types.builtIn.HDLBuiltInFunctions;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

import com.google.common.base.Optional;

/**
 * The class HDLRange contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression from. Can be <code>null</code>.</li>
 * <li>HDLExpression inc. Can be <code>null</code>.</li>
 * <li>HDLExpression dec. Can be <code>null</code>.</li>
 * <li>HDLExpression to. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLRange extends AbstractHDLRange {
	/**
	 * Constructs a new instance of {@link HDLRange}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param inc
	 *            the value for inc. Can be <code>null</code>.
	 * @param dec
	 *            the value for dec. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLRange(int id, @Nullable IHDLObject container, @Nullable HDLExpression from, @Nullable HDLExpression inc, @Nullable HDLExpression dec, @Nonnull HDLExpression to,
			boolean validate) {
		super(id, container, from, inc, dec, to, validate);
	}

	public HDLRange() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLRange;
	}

	/**
	 * The accessor for the field from which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fFrom = new HDLFieldAccess<HDLRange, HDLExpression>("from", HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getFrom();
		}

		@Override
		public HDLRange setValue(HDLRange obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setFrom(value);
		}
	};
	/**
	 * The accessor for the field inc which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fInc = new HDLFieldAccess<HDLRange, HDLExpression>("inc", HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getInc();
		}

		@Override
		public HDLRange setValue(HDLRange obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setInc(value);
		}
	};
	/**
	 * The accessor for the field dec which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fDec = new HDLFieldAccess<HDLRange, HDLExpression>("dec", HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_ONE) {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getDec();
		}

		@Override
		public HDLRange setValue(HDLRange obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setDec(value);
		}
	};
	/**
	 * The accessor for the field to which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fTo = new HDLFieldAccess<HDLRange, HDLExpression>("to", HDLExpression.class, HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getTo();
		}

		@Override
		public HDLRange setValue(HDLRange obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setTo(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (from == obj)
			return fFrom;
		if (inc == obj)
			return fInc;
		if (dec == obj)
			return fDec;
		if (to == obj)
			return fTo;
		return super.getContainingFeature(obj);
	}

	// $CONTENT-BEGIN$
	/**
	 * Calculates the width of the Expression as if it used as a downto (the
	 * most common case when the width is needed)
	 *
	 * @return an simplified Expression of the width as if it was used in a
	 *         downto
	 */
	public HDLExpression getWidth() {
		final HDLExpression f = getFrom();
		if (f == null)
			return new HDLLiteral().setVal("1");
		if (getTo() != null) {
			final Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(getTo());
			if (valueOf.isPresent() && BigInteger.ZERO.equals(valueOf.get())) {
				final HDLArithOp simpleWith = new HDLArithOp().setLeft(f).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
				return HDLPrimitives.simplifyWidth(this, simpleWith, null);
			}
		}
		if (getInc() != null)
			return getInc();
		if (getDec() != null)
			return getDec();
		final HDLArithOp rangeDist = new HDLArithOp().setLeft(f).setType(HDLArithOpType.MINUS).setRight(getTo());
		final HDLExpression absRange = HDLBuiltInFunctions.ABS_UINT.getCall(rangeDist);
		final HDLArithOp width = new HDLArithOp().setLeft(absRange).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
		return HDLPrimitives.simplifyWidth(this, width, null);
	}

	public HDLRange normalize() {
		if (getTo().equals(getFrom()))
			return setFrom(null);
		return this;
	}

	public boolean isBit() {
		if ((getInc() == null) && (getDec() == null) && (getFrom() == null))
			return true;
		return false;
	}
	// $CONTENT-END$

}
