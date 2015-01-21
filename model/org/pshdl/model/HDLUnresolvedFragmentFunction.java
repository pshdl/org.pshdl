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

import org.pshdl.model.impl.AbstractHDLUnresolvedFragmentFunction;
import org.pshdl.model.utils.HDLQuery.HDLFieldAccess;

/**
 * The class HDLUnresolvedFragmentFunction contains the following fields
 * <ul>
 * <li>IHDLObject container. Can be <code>null</code>.</li>
 * <li>String frag. Can <b>not</b> be <code>null</code>.</li>
 * <li>Boolean isStatement. Can <b>not</b> be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> array. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLRange> bits. Can be <code>null</code>.</li>
 * <li>HDLUnresolvedFragment sub. Can be <code>null</code>.</li>
 * <li>ArrayList<HDLExpression> params. Can be <code>null</code>.</li>
 * </ul>
 */
public class HDLUnresolvedFragmentFunction extends AbstractHDLUnresolvedFragmentFunction {
	/**
	 * Constructs a new instance of {@link HDLUnresolvedFragmentFunction}
	 * 
	 * @param container
	 *            the value for container. Can be <code>null</code>.
	 * @param frag
	 *            the value for frag. Can <b>not</b> be <code>null</code>.
	 * @param isStatement
	 *            the value for isStatement. Can <b>not</b> be <code>null</code>
	 *            .
	 * @param array
	 *            the value for array. Can be <code>null</code>.
	 * @param bits
	 *            the value for bits. Can be <code>null</code>.
	 * @param sub
	 *            the value for sub. Can be <code>null</code>.
	 * @param params
	 *            the value for params. Can be <code>null</code>.
	 * @param validate
	 *            if <code>true</code> the parameters will be validated.
	 */
	public HDLUnresolvedFragmentFunction(int id, @Nullable IHDLObject container, @Nonnull String frag, @Nonnull Boolean isStatement, @Nullable Iterable<HDLExpression> array,
			@Nullable Iterable<HDLRange> bits, @Nullable HDLUnresolvedFragment sub, @Nullable Iterable<HDLExpression> params, boolean validate) {
		super(id, container, frag, isStatement, array, bits, sub, params, validate);
	}

	public HDLUnresolvedFragmentFunction() {
		super();
	}

	/**
	 * Returns the ClassType of this instance
	 */
	@Override
	public HDLClass getClassType() {
		return HDLClass.HDLUnresolvedFragmentFunction;
	}

	/**
	 * The accessor for the field params which is of type
	 * ArrayList&lt;HDLExpression&gt;.
	 */
	public static HDLFieldAccess<HDLUnresolvedFragmentFunction, ArrayList<HDLExpression>> fParams = new HDLFieldAccess<HDLUnresolvedFragmentFunction, ArrayList<HDLExpression>>(
			"params", HDLExpression.class, HDLFieldAccess.Quantifier.ZERO_OR_MORE) {
		@Override
		public ArrayList<HDLExpression> getValue(HDLUnresolvedFragmentFunction obj) {
			if (obj == null)
				return null;
			return obj.getParams();
		}

		@Override
		public HDLUnresolvedFragmentFunction setValue(HDLUnresolvedFragmentFunction obj, ArrayList<HDLExpression> value) {
			if (obj == null)
				return null;
			return obj.setParams(value);
		}
	};

	@Override
	public HDLFieldAccess<?, ?> getContainingFeature(Object obj) {
		if (params.contains(obj))
			return fParams;
		return super.getContainingFeature(obj);
	}
	// $CONTENT-BEGIN$
	// $CONTENT-END$

}
