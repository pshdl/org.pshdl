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

import java.math.*;

import javax.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.HDLArithOp.*;
import org.pshdl.model.evaluation.*;
import org.pshdl.model.impl.*;
import org.pshdl.model.types.builtIn.*;
import org.pshdl.model.utils.HDLQuery.*;

import com.google.common.base.*;

import org.pshdl.model.evaluation.*;

/**
 * The class HDLRange contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLExpression from. Can be <code>null</code>.</li>
 * <li>HDLExpression to. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLRange extends AbstractHDLRange {
	/**
	 * Constructs a new instance of {@link HDLRange}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param from
	 *            the value for from. Can be <code>null</code>.
	 * @param to
	 *            the value for to. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the paramaters will be validated.
	 */
	public HDLRange(@Nullable IHDLObject container, @Nullable HDLExpression from, @Nonnull HDLExpression to, boolean validate) {
		super(container, from, to, validate);
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
	public static HDLFieldAccess<HDLRange, HDLExpression> fFrom = new HDLFieldAccess<HDLRange, HDLExpression>("from") {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getFrom();
		}
	};
	/**
	 * The accessor for the field to which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLRange, HDLExpression> fTo = new HDLFieldAccess<HDLRange, HDLExpression>("to") {
		@Override
		public HDLExpression getValue(HDLRange obj) {
			if (obj == null)
				return null;
			return obj.getTo();
		}
	};

	// $CONTENT-BEGIN$
	/**
	 * Calculates the width of the Expression as if it used as a downto (the
	 * most common case when the width is needed)
	 * 
	 * @return
	 */
	public HDLExpression getWidth() {
		HDLExpression f = getFrom();
		if (f == null)
			return new HDLLiteral().setVal("1");
		if (getTo() != null) {
			Optional<BigInteger> valueOf = ConstantEvaluate.valueOf(getTo());
			if (valueOf.isPresent() && BigInteger.ZERO.equals(valueOf.get())) {
				HDLArithOp simpleWith = new HDLArithOp().setLeft(f).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
				return HDLPrimitives.simplifyWidth(this, simpleWith);
			}
		}
		HDLArithOp rangeDist = new HDLArithOp().setLeft(f).setType(HDLArithOpType.MINUS).setRight(getTo());
		HDLExpression absRange = PSHDLLib.ABS.getReplacementExpressionArgs(this, rangeDist);
		HDLArithOp width = new HDLArithOp().setLeft(absRange).setType(HDLArithOpType.PLUS).setRight(HDLLiteral.get(1));
		return HDLPrimitives.simplifyWidth(this, width);
	}

	public HDLRange normalize() {
		if (getTo().equals(getFrom()))
			return setFrom(null);
		return this;
	}
	// $CONTENT-END$

}
