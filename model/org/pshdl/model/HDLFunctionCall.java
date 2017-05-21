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

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.pshdl.model.impl.AbstractHDLFunctionCall;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLFunctionCall contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>HDLQualifiedName function. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList&lt;HDLExpression&gt; params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLFunctionCall extends AbstractHDLFunctionCall implements HDLStatement {
	/**
	 * Constructs a new instance of {@link HDLFunctionCall}
	 *
	 * @param id
	 *            a unique ID for this particular node
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param function
	 *            the value for function. Can <b>not</b> be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLFunctionCall(int id, @Nullable IHDLObject container, @Nonnull HDLQualifiedName function, @Nullable Iterable<HDLExpression> params, boolean validate) {
		super(id, container, function, params, validate);
	}

	public HDLFunctionCall() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLFunctionCall;
	}

	/**
	 * The accessor for the field function which is of type HDLQualifiedName.
	 */
	public static HDLFieldAccess<HDLFunctionCall, HDLQualifiedName> fFunction = new HDLFieldAccess<HDLFunctionCall, HDLQualifiedName>("function", HDLQualifiedName.class,
			HDLFieldAccess.Quantifier.ONE) {
		@Override
		public HDLQualifiedName getValue(HDLFunctionCall obj) {
			if (obj == null) {
				return null;
			}
			return obj.getFunctionRefName();
		}

		@Override
		public HDLFunctionCall setValue(HDLFunctionCall obj, HDLQualifiedName value) {
			if (obj == null) {
				return null;
			}
			return obj.setFunction(value);
		}
	};
	/**
	 * The accessor for the field params which is of type ArrayList&lt;HDLExpression&gt;.
	 */
	public static HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLFunctionCall, ArrayList<HDLExpression>>("params", HDLExpression.class,
			HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLExpression> getValue(HDLFunctionCall obj) {
			if (obj == null) {
				return null;
			}
			return obj.getParams();
		}

		@Override
		public HDLFunctionCall setValue(HDLFunctionCall obj, ArrayList<HDLExpression> value) {
			if (obj == null) {
				return null;
			}
			return obj.setParams(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (function == obj) {
			return fFunction;
		}
		if (params.contains(obj)) {
			return fParams;
		}
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$

	// $CONTENT-END$

}
