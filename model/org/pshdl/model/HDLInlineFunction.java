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

import java.util.*;

import javax.annotation.*;

import org.pshdl.model.impl.*;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLInlineFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLAnnotation> annotations. Can be <code>null</code>.</li>
 * <li>String name. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLFunctionParameter> args. Can be <code>null</code>.</li>
 * <li>HDLFunctionParameter returnType. Can be <code>null</code>.</li>
 * <li>HDLExpression expr. Can <b>not</b> be <code>null</code>.</li>
 * </ul>
 */
public class HDLInlineFunction extends AbstractHDLInlineFunction {
	/**
	 * Constructs a new instance of {@link HDLInlineFunction}
	 *
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param annotations
	 *            the value for annotations. Can be <code>null</code>.
	 * @param name
	 *            the value for name. Can <b>not</b> be <code>null</code>.
	 * @param args
	 *            the value for args. Can be <code>null</code>.
	 * @param returnType
	 *            the value for returnType. Can be <code>null</code>.
	 * @param expr
	 *            the value for expr. Can <b>not</b> be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLInlineFunction(int id, @Nullable IHDLObject container, @Nullable Iterable<HDLAnnotation> annotations, @Nonnull String name,
			@Nullable Iterable<HDLFunctionParameter> args, @Nullable HDLFunctionParameter returnType, @Nonnull HDLExpression expr, boolean validate) {
		super(id, container, annotations, name, args, returnType, expr, validate);
	}

	public HDLInlineFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLInlineFunction;
	}

	/**
	 * The accessor for the field expr which is of type HDLExpression.
	 */
	public static HDLFieldAccess<HDLInlineFunction, HDLExpression> fExpr = new HDLFieldAccess<HDLInlineFunction, HDLExpression>("expr") {
		@Override
		public HDLExpression getValue(HDLInlineFunction obj) {
			if (obj == null)
				return null;
			return obj.getExpr();
		}

		@Override
		public HDLInlineFunction setValue(HDLInlineFunction obj, HDLExpression value) {
			if (obj == null)
				return null;
			return obj.setExpr(value);
		}
	};

	// $CONTENT-BEGIN$

	@Override
	protected HDLFunctionParameter validateReturnType(HDLFunctionParameter returnType) {
		if (returnType == null)
			throw new IllegalArgumentException("The return type can not be null for inline functions");
		return returnType;
	}

	public HDLExpression getReplacementExpression(HDLFunctionCall hdi) {
		final ArrayList<HDLFunctionParameter> args = getArgs();
		final ArrayList<HDLExpression> params = hdi.getParams();
		return createExpression(args, params, hdi);
	}

	private HDLExpression createExpression(ArrayList<HDLFunctionParameter> args, Iterable<HDLExpression> params, IHDLObject origin) {
		return substitute(args, params, getExpr(), origin);
	}

	public HDLExpression getReplacementExpressionArgs(IHDLObject origin, HDLExpression... args) {
		return createExpression(getArgs(), asList(args), origin);
	}
	// $CONTENT-END$

}
